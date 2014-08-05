/*
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.frisbey.webserver.test.request;

import com.frisbey.webserver.exception.InvalidHeaderException;
import com.frisbey.webserver.request.WebServerHeader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Exercises the WebServerHeader class
 */
public class WebServerHeaderTest {

    /**
     * Verifies that a standard header line is processed correctly.
     */
    @Test
    public void addRawLineValueTest() throws InvalidHeaderException {
        WebServerHeader header = new WebServerHeader();
        header.addRawValue("Host: www.adobe.com");
        assertEquals("Unexpected Host value", "www.adobe.com", header.getValue("Host"));
    }

    /**
     * Verifies that addRawLine fails correctly when given an invalid value.
     */
    @Test(expected = InvalidHeaderException.class)
    public void addRawLineValueInvalidTest() throws InvalidHeaderException {
        WebServerHeader header = new WebServerHeader();
        header.addRawValue("INVALID");
        assertTrue("Exception should have been throws", false);
    }

    /**
     * Verifies that addRawLine fails correctly when given a header line that's missing its value.
     */
    @Test(expected = InvalidHeaderException.class)
    public void addRawLineValueMissingValueTest() throws InvalidHeaderException {
        WebServerHeader header = new WebServerHeader();
        header.addRawValue("Host: ");
        assertTrue("Exception should have been throws", false);
    }

    /**
     * Verifies that the getValue method works correctly.
     */
    @Test
    public void getValueTest() throws InvalidHeaderException {
        WebServerHeader header = new WebServerHeader();
        header.addRawValue("Host: www.adobe.com");
        assertEquals("Unexpected Host value", "www.adobe.com", header.getValue("Host"));
    }

    /**
     * Verifies that the getKeys method works as expected.
     */
    @Test
    public void getKeysTest() throws InvalidHeaderException {
        WebServerHeader header = new WebServerHeader();
        header.addRawValue("Host: www.adobe.com");
        header.addRawValue("Keep-Alive: true");

        Iterable<String> keys = header.getKeys();

        boolean hasHost = false;
        boolean hasKeepAlive = false;
        int keyCount = 0;

        for (String key: keys) {
            if (key.compareTo("Host") == 0) {
                hasHost = true;
            } else if (key.compareTo("Keep-Alive") == 0) {
                hasKeepAlive = true;
            }

            keyCount++;
        }

        assertTrue("Host key not found", hasHost);
        assertTrue("Keep alive key not found", hasKeepAlive);
        assertEquals("Unexpected number of keys", 2, keyCount);
    }

    /**
     * Verifies that the setKey method works as intended.
     */
    @Test
    public void setKeyTest() {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");
        assertEquals("Unexpected key value", "www.adobe.com", header.getValue("Host"));

        // overwrite the value
        header.setValue("Host", "adobe.com");
        assertEquals("Unexpected key value", "adobe.com", header.getValue("Host"));
    }
}
