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

package com.frisbey.webserver.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A collection of helper methods for easily accomplishing common actions dealing with streams.
 *
 * @author Mark Frisbey
 */
public class StreamUtils {

    /**
     * Retrieves a reader instance that can be used to read the contents of an input stream.
     *
     * @param input The stream for which a reader will be retrieved.
     * @return The object for reading an input stream.
     */
    public static BufferedReader getStreamReader(InputStream input) {
        return new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Retrieves an input stream whose contents will be the value of a String.
     *
     * @param input The string from which to create an input stream.
     * @return An input stream that will contain the contents of the string.
     */
    public static InputStream getInputStreamFromString(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }
}
