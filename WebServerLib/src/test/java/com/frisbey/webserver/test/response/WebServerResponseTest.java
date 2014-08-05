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

package com.frisbey.webserver.test.response;

import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.utility.StreamUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Exercises the WebServerResponse class.
 *
 * @author Mark Frisbey
 */
public class WebServerResponseTest {

    /**
     * Verifies the getVersion method
     */
    @Test
    public void getVersionTest() {
        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK);
        assertEquals("Unexpected HTTP version", HttpVersion.HTTP_1_1, response.getVersion());
    }

    /**
     * Verifies the getResponse method
     */
    @Test
    public void getResponseTest() {
        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK);
        assertEquals("Unexpected HTTP response", HttpResponse.OK, response.getResponse());
    }

    /**
     * Verifies the getHeaderValue method
     */
    @Test
    public void getHeaderValueTest() {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");

        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK, header);
        assertEquals("Unexpected header value", "www.adobe.com", response.getHeaderValue("Host"));
    }

    /**
     * Verifies that the writeResponse method functions correctly when provided with valid information.
     */
    @Test
    public void writeResponseTest() throws IOException {
        String body = "this is the body";
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");

        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK, header, StreamUtils.getInputStreamFromString(body));

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        assertEquals("Unexpected response output", "HTTP/1.1 200 OK\r\nHost: www.adobe.com\r\n\r\nthis is the body", finalOutput);
    }

    /**
     * Verifies that the writeResponse method functions correctly without a header but with a body
     */
    @Test
    public void writeResponseNoHeaderTest() throws IOException {
        String body = "this is the body";

        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK, null, StreamUtils.getInputStreamFromString(body));

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        assertEquals("Unexpected response output", "HTTP/1.1 200 OK\r\n\r\nthis is the body", finalOutput);
    }

    /**
     * Verifies that the writeResponse method functions correctly with a header but without a body
     */
    @Test
    public void writeResponseNoBodyTest() throws IOException {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");

        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK, header);

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        assertEquals("Unexpected response output", "HTTP/1.1 200 OK\r\nHost: www.adobe.com\r\n\r\n", finalOutput);
    }

    /**
     * Verifies that the writeResponse method functions correctly without a header or body
     */
    @Test
    public void writeResponseNoHeaderOrBodyTest() throws IOException {
        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK);

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        assertEquals("Unexpected response output", "HTTP/1.1 200 OK\r\n\r\n", finalOutput);
    }

    /**
     * Verifies the setBodyStream method
     */
    @Test
    public void setBodyStreamTest() throws IOException {
        String body = "this is the body";

        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK);
        response.setBodyStream(StreamUtils.getInputStreamFromString(body));

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        assertEquals("Unexpected response output", "HTTP/1.1 200 OK\r\n\r\nthis is the body", finalOutput);
    }
}
