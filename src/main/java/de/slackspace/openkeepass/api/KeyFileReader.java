package de.slackspace.openkeepass.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.bouncycastle.util.encoders.Base64;

import de.slackspace.openkeepass.crypto.Sha256;
import de.slackspace.openkeepass.domain.KeyFile;
import de.slackspace.openkeepass.exception.KeyFileUnreadableException;
import de.slackspace.openkeepass.util.StreamUtils;
import de.slackspace.openkeepass.xml.KeyFileXmlParser;

public class KeyFileReader {

	private static final String UTF_8 = "UTF-8";
	private static final String MSG_UTF8_NOT_SUPPORTED = "The encoding UTF-8 is not supported";

	protected KeyFileXmlParser keyFileXmlParser = new KeyFileXmlParser();

	public byte[] readKeyFile(InputStream keyFileStream) {
		KeyFile keyFile = keyFileXmlParser.fromXml(keyFileStream);

		if(!keyFile.isXmlFile()) {
			return readBinaryKeyFile(keyFileStream);
		}

		try {
			byte[] protectedBuffer = Base64.decode(keyFile.getKey().getData().getBytes(UTF_8));
			return hashKeyFileIfNecessary(protectedBuffer);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(MSG_UTF8_NOT_SUPPORTED, e);
		}
	}

	private byte[] readBinaryKeyFile(InputStream keyFileStream) {
		try {
			byte[] protectedBuffer;
			byte[] keepassFile = StreamUtils.toByteArray(keyFileStream);

			int length = keepassFile.length;
			if (length == 64) {
				protectedBuffer = Base64.decode(keepassFile);
			}

			protectedBuffer = hashKeyFileIfNecessary(keepassFile);
			return protectedBuffer;
		} catch (IOException e) {
			throw new KeyFileUnreadableException("Could not read binary key file", e);
		}
	}

	private byte[] hashKeyFileIfNecessary(byte[] protectedBuffer) {
		if (protectedBuffer.length != 32) {
			protectedBuffer = Sha256.hash(protectedBuffer);
		}
		return protectedBuffer;
	}
}
