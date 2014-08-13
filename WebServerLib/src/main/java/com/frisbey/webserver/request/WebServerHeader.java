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

package com.frisbey.webserver.request;

import com.frisbey.webserver.exception.InvalidHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the header portion of an HTTP request. The header functions as a collection of name/value pairs, so to
 * retrieve the value of "Host" in the header, use the method  {@code header.getValue("Host");}
 *
 * @author Mark Frisbey
 */
public class WebServerHeader {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    // internally stores the header's name/value pairs
    private Map<String, String> rawValues;

    // regular expression for parsing the name/value from a raw header line
    private static final String kHeaderValueRegex = "^([^:]+):\\s(.+)$";

    /**
     * Initializes an empty header.
     */
    public WebServerHeader() {
        rawValues = new HashMap<String, String>();
        rawValues.put("Connection", "close");
        rawValues.put("Content-Length", "0");
        rawValues.put("Server", "AemWebServer");
    }

    /**
     * Parses a raw header line an adds it to the header's collection of name/value pairs. An example of a valid line
     * is "Host: www.adobe.com", where the name will become "Host" and the value will become "www.adobe.com".
     *
     * @param rawValue The raw HTTP header line whose name/value will be added to the header.
     * @throws InvalidHeaderException will be thrown if the header line does not meet the required format.
     */
    public void addRawValue(String rawValue) throws InvalidHeaderException {
        logger.debug("entering with input={}", rawValue);

        // use a regular expression to parse the contents of the raw header value
        Pattern regex = Pattern.compile(kHeaderValueRegex);
        Matcher matches = regex.matcher(rawValue);

        // expression should capture the key and value
        boolean isMatch = matches.matches();
        boolean isCaptured = (matches.groupCount() == 2);

        if (!isMatch || !isCaptured) {
            logger.warn("exception due to invalid header value of {}", rawValue);
            throw new InvalidHeaderException("Invalid header: value does not follow pattern <key>: <value>");
        }
        String key = matches.group(1);
        String value = matches.group(2);

        setValue(key, value);

        logger.info("received {} header value of {}", key, value);
    }

    /**
     * Retrieves the header value associated with a given key.
     *
     * @param key The key whose value should be retrieved.
     * @return The raw value assigned to an HTTP header key.
     */
    public String getValue(String key) {
        return rawValues.get(key);
    }

    /**
     * Sets the value of a key. If the key doesn't exist in the header already it will be added. If the key already
     * exists the current value will be overwritten with the new value.
     *
     * @param key The key whose value should be set.
     * @param value The value that will be associated with the given key.
     */
    public void setValue(String key, String value) {
        rawValues.put(key, value);
    }

    /**
     * Retrieves all the keys available in the header.
     *
     * @return All the keys available in the header.
     */
    public Iterable<String> getKeys() {
        return rawValues.keySet();
    }
}
