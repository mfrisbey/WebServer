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
import com.frisbey.webserver.request.HeadRequest;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.test.mock.MockHeadRequest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Exercises the HeadRequest class
 *
 * @author Mark Frisbey
 */
public class HeadRequestTest {

    /**
     * Verifies that the getResponse method works correctly with provided with valid input.
     */
    @Test
    public void getResponseTest() throws InvalidRequestException {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");
        header.setValue("Some-Key", "some value");

        HeadRequest request = new MockHeadRequest(HttpMethod.HEAD, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        WebServerResponse response = request.getResponse();
        assertEquals("Unexpected HTTP version in response", HttpVersion.HTTP_1_1, response.getVersion());
        assertEquals("Unexpected HTTP response", HttpResponse.OK, response.getResponse());
        assertEquals("Unexpected Server header value", "AemWebServer", response.getHeaderValue("Server"));
    }
}
