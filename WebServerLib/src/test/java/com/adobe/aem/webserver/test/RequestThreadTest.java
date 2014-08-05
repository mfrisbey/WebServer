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

package com.adobe.aem.webserver.test;

import com.adobe.aem.webserver.HttpResponse;
import com.adobe.aem.webserver.HttpVersion;
import com.adobe.aem.webserver.RequestThread;
import com.adobe.aem.webserver.request.GetRequest;
import com.adobe.aem.webserver.response.WebServerResponse;
import com.adobe.aem.webserver.test.mock.MockRequestThread;
import com.adobe.aem.webserver.utility.StreamUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static junit.framework.TestCase.*;

/**
 * Exercises the RequestThread class.
 *
 * @author Mark Frisbey
 */
public class RequestThreadTest {

    @Mock
    private Socket mockSocket;

    /**
     * Initialize unit test.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Ensures the call method works correctly when given valid data.
     */
    @Test
    public void callTest() throws Exception {
        InputStream input = StreamUtils.getInputStreamFromString("GET /someuri HTTP/1.1");
        OutputStream output = new ByteArrayOutputStream();

        Mockito.when(mockSocket.getInputStream()).thenReturn(input);
        Mockito.when(mockSocket.getOutputStream()).thenReturn(output);

        RequestThread thread = new MockRequestThread(mockSocket, "/webserverroot");
        thread.run();

        // ensure the socket was closed as expected
        Mockito.verify(mockSocket).close();
    }

    /**
     * Ensures that call fails as expected when an exception is thrown.
     */
    @Test
    public void callTestException() throws Exception {
        Mockito.when(mockSocket.getInputStream()).thenThrow(IOException.class);

        RequestThread thread = new MockRequestThread(mockSocket, "/webserverroot");

        try {
            thread.run();
        } finally {
            // ensure that socket was closed even with exception
            Mockito.verify(mockSocket).close();
        }
    }
}
