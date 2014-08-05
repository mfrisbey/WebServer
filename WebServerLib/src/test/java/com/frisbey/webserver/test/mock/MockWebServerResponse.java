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

import com.frisbey.webserver.HttpResponse;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.response.WebServerResponse;
import com.frisbey.webserver.utility.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Mock response that eliminates the need for a response to reference a file system file.
 *
 * @author Mark Frisbey
 */
public class MockWebServerResponse extends WebServerResponse {

    /**
     * Invokes the parent constructor.
     */
    public MockWebServerResponse(HttpVersion version, HttpResponse response, WebServerHeader header, String bodyUri) {
        super(version, response, header, bodyUri);
    }

    /**
     * Overridden to return a stream to the provided uri String instance.
     */
    @Override
    protected InputStream getUriInputStream(String uri) throws IOException {
        return StreamUtils.getInputStreamFromString(uri);
    }
}
