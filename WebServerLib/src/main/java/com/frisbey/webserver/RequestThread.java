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

package com.frisbey.webserver;

import com.frisbey.webserver.exception.InvalidRequestException;
import com.frisbey.webserver.request.WebServerRequest;
import com.frisbey.webserver.request.WebServerRequestFactory;
import com.frisbey.webserver.response.WebServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Represents a {@link java.util.concurrent.Callable} object that can be invoked in a threaded manner. The class
 * is intended to be used to make requests to the server execute concurrently. Callable is used so that the thread
 * can report exceptions and return the results of its invocation.
 *
 * @author Mark Frisbey
 * @see com.frisbey.webserver.request.WebServerRequest
 * @see com.frisbey.webserver.response.WebServerResponse
 */
public class RequestThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    // the socket on which the original request was received.
    private Socket clientSocket = null;

    // the full path to the root directory of the web server.
    private String webServerRoot;

    /**
     * Initializes a new thread using the provided information.
     *
     * @param clientSocket The socket on which the original request was received. Will be used to read the request and
     *                     write the response.
     * @param webServerRoot The full path to the root directory of the web server. Will be used to retrieve resources
     *                      requested in the URI portion of the request.
     */
    public RequestThread(Socket clientSocket, String webServerRoot) {
        this.clientSocket = clientSocket;
        this.webServerRoot = webServerRoot;
    }

    /**
     * Performs the work of reading a request and producing a response. The request will be read from the thread's
     * socket and the response will be written back to the same socket.
     *
     * @return The response that the server generated for the received request.
     * @throws Exception thrown if there are issues reading the request and creating the response.
     */
    @Override
    public void run() {
        logger.debug("entering");
        WebServerResponse response = null;
        try {
            // grab streams from the socket
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            // interpret the request and generate a response
            WebServerRequest request = getRequest(input, this.webServerRoot);
            response = request.getResponse();

            // write the response back to the socket
            response.writeResponse(output);
        } catch (Exception ex) {
            logger.error("exception when processing request", ex);
        } finally {
            // close the socket when finished
            try {
                this.clientSocket.close();
            } catch (IOException ex) {
                logger.error("there was an unhandled exception while attempting to close request socket", ex);
            }
        }
    }

    /**
     * Creates a request instance from the raw data provided in the given input stream.
     *
     * @param input The input to be read when creating the request.
     * @param webServerRoot The full path to the root directory of the web server. Will be used to retrieve resources
     *                      requested in the URI portion of the request.
     * @return The request represented by the raw data in the given input.
     * @throws IOException thrown if there are issues reading from the input stream.
     * @throws InvalidRequestException thrown if the raw request in the input stream is invalid.
     */
    protected WebServerRequest getRequest(InputStream input, String webServerRoot) throws IOException, InvalidRequestException {
        return WebServerRequestFactory.getRequest(input, webServerRoot);
    }
}
