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
import com.frisbey.webserver.request.HeadRequest;
import com.frisbey.webserver.request.WebServerHeader;

/**
 * A mock version of the HeadRequest. Eliminates the need for files to exist in order for the request to be successful.
 *
 * @author Mark Frisbey
 */
public class MockHeadRequest extends HeadRequest {

    /**
     * Constructs a mock HEAD request.
     */
    public MockHeadRequest(HttpMethod method, String uri, HttpVersion version, WebServerHeader header) {
        super(method, uri, version, header);
    }

    /**
     * Overridden to always return OK.
     */
    protected HttpResponse getHttpResponse() {
        return HttpResponse.OK;
    }
}
