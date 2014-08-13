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

import com.frisbey.webserver.HttpContentType;
import com.frisbey.webserver.HttpMethod;
import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.exception.InvalidRequestException;
import com.frisbey.webserver.response.WebServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Represents a HEAD request from a client. The response to this type of request will be the standard response data
 * in addition to the header data that would be returned with a GET request.
 *
 * @author Mark Frisbey
 * @see com.frisbey.webserver.request.WebServerRequest
 */
public class HeadRequest extends WebServerRequest {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    /**
     * Initializes a HEAD request consisting of the provided attributes.
     *
     * @param method The method of the raw request.
     * @param uri The full local path of the URI of the resource being requested.
     * @param version The HTTP version provided with a raw request.
     * @param header The header of a raw request.
     */
    public HeadRequest(HttpMethod method, String uri, HttpVersion version, WebServerHeader header) {
        super(method, uri, version, header);
    }

    /**
     * Retrieves a response consisting of standard response data and the header portion of a GET response.
     *
     * @return Data representing the response to a HEAD request.
     * @throws InvalidRequestException thrown if there are issues with the request that prevent returning a response.
     */
    @Override
    public WebServerResponse getResponse() throws InvalidRequestException {
        logger.debug("entering");

        HttpResponse response = getHttpResponse();

        WebServerHeader header = new WebServerHeader();

        WebServerResponse serverResponse = createResponse(HttpVersion.HTTP_1_1, response, header);

        if (response == HttpResponse.OK) {
            serverResponse.setHeaderValue("Content-Length", Long.toString(getContentLength()));
            serverResponse.setHeaderValue("Content-Type", HttpContentType.fromFilePath(this.getUri()).getContentType());
        }

        logger.debug("returning response with status {}", response.getText());

        return serverResponse;
    }

    /**
     * Initializes the response that the request will return.
     *
     * @param version The HTTP version that will be provided with the response.
     * @param response The HTTP response that will be included with the response.
     * @param header Header values that will be included in the response.
     * @return An initialized WebServerResponse instance.
     */
    protected WebServerResponse createResponse(HttpVersion version, HttpResponse response, WebServerHeader header) {
        return new WebServerResponse(version, response, header);
    }

    /**
     * Retrieves the appropriate HTTP response based on the configured request.
     *
     * @return The HTTP response that should be returned by the request.
     */
    protected HttpResponse getHttpResponse() {
        HttpResponse response = HttpResponse.OK;

        File file = new File(this.getUri());

        // check to make sure that the request resource exists.
        if (!file.exists() || !file.isFile()) {
            response = HttpResponse.NotFound;
        }

        return response;
    }

    /**
     * Retrieves the content length of the response that will be sent.
     *
     * @return The length (in bytes) of the response's body.
     */
    protected long getContentLength() {
        File file = new File(this.getUri());

        return file.length();
    }
}
