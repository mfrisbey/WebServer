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

package com.adobe.aem.webserver.test.request;

import com.adobe.aem.webserver.exception.InvalidRequestException;
import com.adobe.aem.webserver.request.GetRequest;
import com.adobe.aem.webserver.request.WebServerRequest;
import com.adobe.aem.webserver.request.WebServerRequestFactory;
import com.adobe.aem.webserver.utility.StreamUtils;
import com.adobe.aem.webserver.utility.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Exercises the WebServerRequestFactory.
 */
public class WebServerRequestFactoryTest {

    /**
     * Retrieves a sample raw HTTP request.
     * @return A raw HTML request.
     */
    private String getTestRawRequest() {
        return getTestRawRequest("Host: www.adobe.com");
    }

    /**
     * Retrieves a sample raw HTTP request.
     * @param header The value to be used as the header of the request. A null or empty value will create a request
     *               without a header.
     * @return A raw HTML request.
     */
    private String getTestRawRequest(String header) {
        String request = "GET /someuri HTTP/1.1";

        if (!StringUtils.isNullOrEmpty(header)) {
            request += "\r\n"+header;
        }

        return request;
    }

    /**
     * Creates an input stream that will read the given string value.
     * @param rawRequest The value to be read by the InputStream.
     * @return An input stream that will read a string.
     */
    private InputStream getTestRawRequestInput(String rawRequest) {
        return new ByteArrayInputStream(rawRequest.getBytes(Charset.defaultCharset()));
    }

    /**
     * Test a standard request with a header and body
     */
    @Test
    public void getRequestTest() throws IOException, InvalidRequestException {
        WebServerRequest request = WebServerRequestFactory.getRequest(getTestRawRequestInput(getTestRawRequest()), "/webserverroot");
        assertNotNull("Invalid request retrieved from factory", request);
        assertTrue("Request retrieved from factory is of unexpected type", GetRequest.class.isAssignableFrom(request.getClass()));
        assertEquals("Unexpected request URI", "/webserverroot/someuri", request.getUri());
        assertEquals("Unexpected header value", "www.adobe.com", request.getHeaderValue("Host"));
    }

    /**
     * Test a request without a header or body.
     */
    @Test
    public void getRequestSingleLineTest() throws IOException, InvalidRequestException {
        WebServerRequest request = WebServerRequestFactory.getRequest(getTestRawRequestInput(getTestRawRequest(null)), "/webserverroot");
        assertEquals("Unexpected header value", null, request.getHeaderValue("Host"));
    }

    /**
     * Test a request with invalid request data.
     */
    @Test(expected = InvalidRequestException.class)
    public void getRequestInvalidDataTest() throws IOException, InvalidRequestException {
        WebServerRequest request = WebServerRequestFactory.getRequest(getTestRawRequestInput("INVALID"), "/webserverroot");
        assertNull("Exception should have thrown by this point", request);
    }

    /**
     * Test an empty request.
     */
    @Test(expected = InvalidRequestException.class)
    public void getRequestEmptyException() throws IOException, InvalidRequestException {
        WebServerRequest request = WebServerRequestFactory.getRequest(StreamUtils.getInputStreamFromString(""), "webserverroot");
        assertNull("Exception should have thrown by this point", request);
    }

    /**
     * Test a request with an invalid header.
     */
    @Test(expected = InvalidRequestException.class)
    public void getRequestInvalidHeaderTest() throws IOException, InvalidRequestException {
        WebServerRequest request = WebServerRequestFactory.getRequest(getTestRawRequestInput(getTestRawRequest("INVALIDHEADER")), "/webserverroot");
        assertNull("Exception should have thrown by this point", request);
    }
}
