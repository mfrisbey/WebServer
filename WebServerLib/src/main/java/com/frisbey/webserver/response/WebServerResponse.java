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

package com.frisbey.webserver.response;

import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.request.WebServerRequestFactory;
import com.frisbey.webserver.utility.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Represents a response that can be returned by a {@link com.frisbey.webserver.request.WebServerRequest}. A
 * response will consist of an HTTP response, the HTTP version, a header, and a body. The body can be written
 * directly to an {@Link java.io.OutputStream}.
 *
 * @author Mark Frisbey
 * @see com.frisbey.webserver.request.WebServerRequest
 */
public class WebServerResponse {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    // the HTTP version that will be returned with the response
    private HttpVersion version;

    // the HTTP response associated with the web server response
    private HttpResponse response;

    // the header that will be returned with the response.
    private WebServerHeader header;

    // a stream to the body associated with the response
    private InputStream bodyStream;

    // the newline sequence that will be used in the response
    protected static final String kResponseNewLine = "\r\n";

    // the delimiter that will be used to separate key/value pairs in the response header
    protected static final String kHeaderValueDelimiter = ": ";

    /**
     * Creates a new response consisting of an HTTP version and HTTP response.
     *
     * @param version The HTTP version that will be provided with the response.
     * @param response The HTTP response that will be associated with the response.
     */
    public WebServerResponse(HttpVersion version, HttpResponse response) {
        this(version, response, null, null);
    }

    /**
     * Creates a new response consisting of an HTTP version and HTTP response.
     *
     * @param version The HTTP version that will be provided with the response.
     * @param response The HTTP response that will be associated with the response.
     * @param header The header that will be used with the response.
     */
    public WebServerResponse(HttpVersion version, HttpResponse response, WebServerHeader header) {
        this(version, response, header, null);
    }

    /**
     * Creates a new response consisting of an HTTP version and HTTP response.
     *
     * @param version The HTTP version that will be provided with the response.
     * @param response The HTTP response that will be associated with the response.
     * @param header The header that will be used with the response.
     * @param body A stream to the body that can be written to an output stream.
     */
    public WebServerResponse(HttpVersion version, HttpResponse response, WebServerHeader header, InputStream body) {
        this.version = version;
        this.response = response;
        this.header = header;
        this.bodyStream = body;
    }

    /**
     * Retrieves the HTTP version portion of the response.
     *
     * @return An HTTP version.
     */
    public HttpVersion getVersion() {
        return this.version;
    }

    /**
     * Retrieves the HTTP response portion of the web server's response.
     *
     * @return An HTTP response.
     */
    public HttpResponse getResponse() {
        return this.response;
    }

    /**
     * Retrieves the value for a key in the response's header.
     *
     * @param key The header key whose value should be retrieved.
     * @return The value associated with a given header key.
     */
    public String getHeaderValue(String key) {
        return this.header.getValue(key);
    }

    /**
     * Writes the entire response to an output stream. The output will include the HTTP response data, the header,
     * and the entire contents of the response's body.
     *
     * @param output The output stream to which the response's body will be written.
     * @throws IOException thrown if there is an issue writing to the output stream or reading from the body input stream.
     */
    public void writeResponse(OutputStream output) throws IOException {
        logger.debug("entering with output={}", output);

        StringBuilder responseData = new StringBuilder(String.format("%s %d %s", version.getVersion(), response.getCode(), response.getText()));
        responseData.append(kResponseNewLine);

        // write the header if supplied
        if (header != null) {
            logger.debug("writing header to output");

            Iterable<String> headerKeys = header.getKeys();

            for (String key : headerKeys) {
                responseData.append(String.format("%s%s%s", key, kHeaderValueDelimiter, header.getValue(key)));
                responseData.append(kResponseNewLine);
            }
        }

        // add the separator between the header and body
        responseData.append(kResponseNewLine);

        String headerResponse = responseData.toString();

        logger.debug("Writing header to output: {}", headerResponse.replace(kResponseNewLine, "[NL]"));

        output.write(headerResponse.getBytes());

        // write the body if supplied
        if (bodyStream != null) {
            logger.debug("writing body to output from stream {}", bodyStream);

            BufferedReader reader = StreamUtils.getStreamReader(bodyStream);
            int currByte = 0;

            while ((currByte = reader.read()) != -1) {
                output.write(currByte);
            }
        }
    }

    /**
     * Sets the stream that the response will use as its body.
     *
     * @param stream The stream to use as the response's body.
     */
    public void setBodyStream(InputStream stream) {
        this.bodyStream = stream;
    }
}
