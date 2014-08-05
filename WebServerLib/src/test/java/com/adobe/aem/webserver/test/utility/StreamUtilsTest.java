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

package com.adobe.aem.webserver.test.utility;

import com.adobe.aem.webserver.utility.StreamUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Exercises the StreamUtils class.
 *
 * @author Mark Frisbey
 */
public class StreamUtilsTest {

    /**
     * Validates the getStreamReader method
     */
    @Test
    public void getStreamReaderTest() throws IOException {
        String value = "Hello World!";
        InputStream input = StreamUtils.getInputStreamFromString(value);

        BufferedReader reader = StreamUtils.getStreamReader(input);
        assertNotNull("Invalid reader retrieved", reader);
        assertEquals("Unexpected input stream data", value, reader.readLine());
    }

    /**
     * Validates the getInputStreamFromString method
     */
    @Test
    public void getInputStreamFromStringTest() {
        String value = "somevalue";
        InputStream stream = StreamUtils.getInputStreamFromString(value);
        assertNotNull("Invalid stream retrieved", stream);
    }
}
