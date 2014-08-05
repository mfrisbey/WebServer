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
import com.adobe.aem.webserver.HttpVersion;
import com.adobe.aem.webserver.exception.InvalidRequestException;
import com.adobe.aem.webserver.response.WebServerResponse;

/**
 * Represents an HTTP request, which consists of a header and a body. The header is comprised of a collection of
 * key/value pairs that were received in the client request. The body is simply a raw string representing the data
 * from the request.
 *
 * @author Mark Frisbey
 * @see com.adobe.aem.webserver.request.WebServerHeader
 * @see com.adobe.aem.webserver.response.WebServerResponse
 */
public abstract class WebServerRequest {

    // the method as set in the client request
    private HttpMethod method;

    // the uri as set in the client request
    private String uri;

    // the http version as set in the client request
    private HttpVersion version;

    // the header portion of the request
    private WebServerHeader requestHeader;

    /**
     * Initializes a new request comprised of the given header and body.
     *
     * @param method The method of the raw request.
     * @param uri The full local path of the URI of the resource being requested.
     * @param version The HTTP version provided with a raw request.
     * @param header The header of a raw request.
     */
    public WebServerRequest(HttpMethod method, String uri, HttpVersion version, WebServerHeader header) {
        this.method = method;
        this.requestHeader = header;
        this.uri = uri;
        this.version = version;
    }

    /**
     * Retrieves the HTTP method that was used in the request.
     *
     * @return The method for the request.
     */
    public HttpMethod getMethod() {
        return this.method;
    }

    /**
     * Retrieves the full local path of the URI that was provided with the request.
     *
     * @return The URI of the request.
     */
    public String getUri() {
        return this.uri;
    }

    /**
     * Retrieves the HTTP version that was provided with the request.
     *
     * @return The version of the request.
     */
    public HttpVersion getVersion() {
        return this.version;
    }

    /**
     * Retrieves the header value associated with a given key.
     *
     * @param key The key whose raw value should be retrieved.
     * @return The header value as it was received from a client.
     */
    public String getHeaderValue(String key) {
        return requestHeader.getValue(key);
    }

    /**
     * Should be implemented to retrieve the response that the request should provide back to the client.
     *
     * @return The response that will be provided to the client.
     */
    public abstract WebServerResponse getResponse() throws InvalidRequestException;
}
