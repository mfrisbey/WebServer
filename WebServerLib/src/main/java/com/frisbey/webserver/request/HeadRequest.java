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

        WebServerHeader header = new WebServerHeader();
        header.setValue("Server", "AemWebServer");
        header.setValue("Content-Length", "0");
        header.setValue("Connection", "close");

        HttpResponse response = getHttpResponse();

        logger.debug("returning response with status {}", response.getText());

        return new WebServerResponse(HttpVersion.HTTP_1_1, response, header);
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
     * Retrieves the file in the location specified by the given uri.
     *
     * @param uri The URI of the file to be retrieved.
     * @return The file at the given location.
     */
    protected File getUriFile(String uri) {
        return new File(uri);
    }

    /**
     * Retrieves an input stream to a URI resource.
     *
     * @param uri The URI to which a stream should be opened.
     * @return An input stream to a URI.
     * @throws InvalidRequestException thrown if the URI could not be found.
     */
    protected InputStream getBodyInput(String uri) throws InvalidRequestException {
        logger.debug("entering with uri={}", uri);

        InputStream stream = null;

        try {
            stream = new FileInputStream(uri);
        } catch (FileNotFoundException ex) {
            logger.warn("exception due to file that does not exist: {}", uri);
            throw new InvalidRequestException("Unable to create response due to invalid URI GET request", ex);
        }

        logger.debug("leaving with stream {}", stream);

        return stream;
    }
}
