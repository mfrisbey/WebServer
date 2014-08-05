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

package com.frisbey.webserver.test.request;

import com.frisbey.webserver.HttpMethod;
import com.frisbey.webserver.HttpVersion;
import com.frisbey.webserver.request.HeadRequest;
import com.frisbey.webserver.request.WebServerHeader;
import com.frisbey.webserver.request.WebServerRequest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Exercises the WebServerRequest class.
 * @author Mark Frisbey
 */
public class WebServerRequestTest {

    /**
     * Verifies the getMethod method.
     */
    @Test
    public void getMethodTest() {
        WebServerRequest request  = new HeadRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        assertEquals("Unexpected HTTP method", HttpMethod.GET, request.getMethod());
    }

    /**
     * Verifies the getUri method.
     */
    @Test
    public void getUriTest() {
        WebServerRequest request  = new HeadRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        assertEquals("Unexpected URI", "uri", request.getUri());
    }

    /**
     * verifies the getLocalUriPath method.
     */
    @Test
    public void getLocalUriPathTest() {
        WebServerRequest request = new HeadRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        assertEquals("Unexpected local path", "uri", request.getUri());
    }

    /**
     * Verifies the getVersion method.
     */
    @Test
    public void getVersionTest() {
        WebServerRequest request  = new HeadRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, new WebServerHeader());
        assertEquals("Unexpected HTTP version", HttpVersion.HTTP_1_1, request.getVersion());
    }

    /**
     * Verifies the getHeaderValue method.
     */
    @Test
    public void getHeaderValueTest() {
        WebServerHeader header = new WebServerHeader();
        header.setValue("Host", "www.adobe.com");
        WebServerRequest request  = new HeadRequest(HttpMethod.GET, "uri", HttpVersion.HTTP_1_1, header);
        assertEquals("Unexpected header value", "www.adobe.com", request.getHeaderValue("Host"));
    }
}
