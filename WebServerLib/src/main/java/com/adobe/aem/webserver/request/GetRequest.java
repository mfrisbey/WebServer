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

package com.adobe.aem.webserver.request;

import com.adobe.aem.webserver.HttpMethod;
import com.adobe.aem.webserver.HttpResponse;
import com.adobe.aem.webserver.HttpVersion;
import com.adobe.aem.webserver.exception.InvalidRequestException;
import com.adobe.aem.webserver.response.WebServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A specialized {@link com.adobe.aem.webserver.request.HeadRequest} that represents an HTTP GET request. Provides
 * the functionality necessary for working with a GET request.
 *
 * @author Mark Frisbey
 * @see com.adobe.aem.webserver.request.HeadRequest
 */
public class GetRequest extends HeadRequest {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    /**
     * Initializes a GET request that contains the given header and body.
     *
     * @param method The method of the raw request.
     * @param uri The full local path of the URI of the resource being requested.
     * @param version The HTTP version provided with a raw request.
     * @param header The header of a raw request.
     */
    public GetRequest(HttpMethod method, String uri, HttpVersion version, WebServerHeader header) {
        super(method, uri, version, header);
    }

    /**
     * Retrieves the response to a GET request, which consists of standard response data, header data, and the body
     * of the response.
     *
     * @return A response to a GET request.
     * @throws InvalidRequestException thrown if there are issues with the request, such as an invalid URI.
     */
    @Override
    public WebServerResponse getResponse() throws InvalidRequestException {
        logger.debug("entering");

        WebServerResponse response = super.getResponse();

        if (response.getResponse() == HttpResponse.OK) {
            logger.debug("HEAD response was OK, attempting to retrieve body");
            response.setBodyStream(getBodyInput(this.getUri()));
        }

        logger.debug("leaving with response {}", response.getResponse().getText());

        return response;
    }
}
