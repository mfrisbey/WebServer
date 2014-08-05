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

package com.adobe.aem.webserver.test.mock;

import com.adobe.aem.webserver.HttpMethod;
import com.adobe.aem.webserver.HttpVersion;
import com.adobe.aem.webserver.RequestThread;
import com.adobe.aem.webserver.exception.InvalidRequestException;
import com.adobe.aem.webserver.request.WebServerHeader;
import com.adobe.aem.webserver.request.WebServerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * A mock version of a request thread. Allows the type of request to be specified and assists in testing thrown
 * exceptions
 *
 * @author Mark Frisbey
 */
public class MockRequestThread extends RequestThread {

    /**
     * Initializes a new thread using the provided information.
     *
     * @param socket The socket on which the original request was received. Will be used to read the request and
     *                     write the response.
     * @param webServerRoot The full path to the root directory of the web server. Will be used to retrieve resources
     *                      requested in the URI portion of the request.
     */
    public MockRequestThread(Socket socket, String webServerRoot) {
        super(socket, webServerRoot);
    }

    /**
     * Overridden to retrieve a specified request.
     *
     * @param input The input to be read when creating the request.
     * @param webServerRoot The full path to the root directory of the web server. Will be used to retrieve resources
     *                      requested in the URI portion of the request.
     * @return The request represented by the raw data in the given input.
     * @throws IOException thrown if there are issues reading from the input stream.
     * @throws InvalidRequestException thrown if the raw request in the input stream is invalid.
     */
    @Override
    protected WebServerRequest getRequest(InputStream input, String webServerRoot) throws IOException, InvalidRequestException {
        return new MockGetRequest(HttpMethod.GET, "mockuri", HttpVersion.HTTP_1_1, new WebServerHeader());
    }
}
