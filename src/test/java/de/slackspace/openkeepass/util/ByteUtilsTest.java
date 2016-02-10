package de.slackspace.openkeepass.util;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public class ByteUtilsTest {

	@Test
	public void shouldConvertUUIDToBytesAndBackAgain() {
		UUID uuid = UUID.randomUUID();

		byte[] bytes = ByteUtils.uuidToBytes(uuid);
		UUID uuid2 = ByteUtils.bytesToUUID(bytes);

		assertEquals(uuid, uuid2);
	}
}
