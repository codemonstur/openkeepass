package de.slackspace.openkeepass;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class KeePassDatabaseTest {

    // Generates an NPE
    @Test
    public void openDatabaseV2_7_1() throws IOException {
        final InputStream archive = KeePassDatabaseTest.class.getResourceAsStream("/DatabaseWithNPE2.kdbx");
        try {
            KeePassDatabase.getInstance(archive).openDatabase("123456");
        } finally {
            archive.close();
        }
    }

}
