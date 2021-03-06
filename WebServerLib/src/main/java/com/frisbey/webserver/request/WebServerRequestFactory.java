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
import com.frisbey.webserver.exception.InvalidHeaderException;
import com.frisbey.webserver.exception.InvalidMethodException;
import com.frisbey.webserver.exception.InvalidRequestException;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.utility.StreamUtils;
import com.frisbey.webserver.utility.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class consists of static methods that will create instances of  the
 * {@link com.frisbey.webserver.request.WebServerRequest} class. The specific implementation of the WebServerRequest
 * will change depending on the type of request being processed.
 *
 * @author Mark Frisbey
 * @see com.frisbey.webserver.request.WebServerRequest
 */
public class WebServerRequestFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    /**
     * Instantiates a {@link com.frisbey.webserver.request.WebServerRequest} from the raw HTTP request contained
     * in the provided InputStream. The contents of the stream will be interpreted as a String. The specific
     * implementation of the WebServerRequest will vary depending on the request data in the raw HTTP request. For
     * example, a GET request would result in a {@link com.frisbey.webserver.request.GetRequest}.
     *
     * @param input A stream whose contents should be a valid raw HTTP request.
     * @param webServerRoot The full path to the local root directory of the web server.
     * @return A WebServerRequest representing the raw HTTP request provided in the input stream.
     * @throws IOException thrown when there are issues retrieving information from the InputStream.
     * @throws InvalidRequestException throw if the raw request provided by the InputStream is not in an expected
     *         format.
     */
    public static WebServerRequest getRequest(InputStream input, String webServerRoot) throws IOException, InvalidRequestException {
        logger.debug("entering with input={}, webServerRoot={}", input, webServerRoot);

        WebServerHeader header = new WebServerHeader();

        BufferedReader inputReader = StreamUtils.getStreamReader(input);

        // retrieve the first line of the request, which should contain the request method
        String inputLine = inputReader.readLine();

        if (StringUtils.isNullOrEmpty(inputLine)) {
            throw new InvalidRequestException("Invalid request - no data found");
        }

        String[] requestData = inputLine.split(" ");

        if (requestData.length != 3) {
            logger.warn("exception due to invalid request");
            throw new InvalidRequestException("Invalid request: unexpected number of values in request line.");
        }

        // retrieve the HTTP method from the header
        String rawMethod = requestData[0];
        String uri = StringUtils.buildPath(webServerRoot, StringUtils.trimQueryString(requestData[1]));
        String rawVersion = requestData[2];

        logger.debug("Received URI value of {} from raw request, converted to local path {}", requestData[1], uri);

        // retrieve the header of the request
        try {
            while ((inputLine = inputReader.readLine()) != null && inputLine.length() > 0) {
                // processing the header
                header.addRawValue(inputLine);
            }
        } catch (InvalidHeaderException ex) {
            logger.warn("exception due to invalid header");
            throw new InvalidRequestException("Invalid request: header format is unexpected.", ex);
        }

        // retrieve the request instance
        WebServerRequest request = null;

        // determine request method and version
        HttpMethod method = HttpMethod.fromString(rawMethod);
        HttpVersion version = HttpVersion.fromString(rawVersion);

        if (version == null) {
            logger.warn("exception due to unrecognized http version {}", rawVersion);
            throw new InvalidRequestException("Invalid request: unrecognized HTTP Version: "+rawVersion);
        }

        if (method != null) {
            switch (method) {
                case GET:
                    request = new GetRequest(method, uri, version, header);
                    logger.info("client request interpreted as GET");
                    break;
                case HEAD:
                    request = new HeadRequest(method, uri, version, header);
                    logger.info("client request interpreted as HEAD");
                default:
                    method = null;
            }
        }

        if (method == null) {
            logger.warn("exception due to unhandled method {}", rawMethod);
            throw new InvalidMethodException("Invalid method: " + rawMethod);
        }

        logger.debug("leaving: {}", request);

        return request;
    }
}
