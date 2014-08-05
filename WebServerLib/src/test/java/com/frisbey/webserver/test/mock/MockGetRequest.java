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

package com.frisbey.webserver.test.mock;

import com.frisbey.webserver.HttpMethod;
import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.exception.InvalidRequestException;
import com.frisbey.webserver.request.GetRequest;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.utility.StreamUtils;

import java.io.InputStream;

/**
 * A mock version of the GetRequest. Eliminates the need for files to exist in order for the request to be successful.
 *
 * @author Mark Frisbey
 */
public class MockGetRequest extends GetRequest {

    // the response that the Mock request will return.
    private HttpResponse response;

    /**
     * Constructs a mock GET request.
     */
    public MockGetRequest(HttpMethod method, String uri, HttpVersion version, WebServerHeader header) {
        super(method, uri, version, header);
        response = HttpResponse.OK;
    }

    /**
     * Sets the HTTP response that will be returned by the Mock object.
     *
     * @param response An HTTP response to return.
     */
    public void setHttpResponse(HttpResponse response) {
        this.response = response;
    }

    /**
     * Overridden to allow the response that the request will return to be set on the Mock object.
     *
     * @return The HTTP response that the request will return.
     */
    @Override
    protected HttpResponse getHttpResponse() {
        return this.response;
    }

    /**
     * Overridden to return a mock response.
     * @param version The HTTP version that will be provided with the response.
     * @param response The HTTP response that will be included with the response.
     * @param header Header values that will be included in the response.
     * @return A mock version of a web server response.
     */
    @Override
    protected WebServerResponse createResponse(HttpVersion version, HttpResponse response, WebServerHeader header) {
        return new MockWebServerResponse(version, response, header, null);
    }
}
