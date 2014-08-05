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

import com.frisbey.webserver.HttpMethod;
import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.exception.InvalidRequestException;
import com.frisbey.webserver.request.GetRequest;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.test.mock.MockGetRequest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Exercises the GetRequest class.
 *
 * @author Mark Frisbey
 */
public class GetRequestTest {

    /**
     * Verifies that getRequest functions as expected when provided with expected input
     */
    @Test
    public void getResponseTest() throws InvalidRequestException, IOException {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");

        GetRequest request = new MockGetRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, header);
        WebServerResponse response = request.getResponse();

        OutputStream output = new ByteArrayOutputStream();
        response.writeResponse(output);

        String finalOutput = output.toString();
        String[] outputLines = finalOutput.split("\r\n");
        assertTrue("Unexpected number of lines in response", outputLines.length >= 3);
        assertEquals("Unexpected response data in response", "HTTP/1.1 200 OK", outputLines[0]);
        assertEquals("Unexpected body in response", "uri", outputLines[outputLines.length - 1]);
    }

    /**
     * Verifies that getRequest does not modify the HTTP response when anything other than OK.
     */
    @Test
    public void getResponseNotOkTest() throws InvalidRequestException {
        MockGetRequest request = new MockGetRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        request.setHttpResponse(HttpResponse.NotFound);

        WebServerResponse response = request.getResponse();
        assertEquals("Unexpected HTTP response when response not OK", HttpResponse.NotFound, response.getResponse());
    }
}
