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
import com.adobe.aem.webserver.WebServer;
import com.adobe.aem.webserver.response.WebServerResponse;
import com.adobe.aem.webserver.test.mock.MockRequestThread;
import com.adobe.aem.webserver.test.mock.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Exercises the WebServer class.
 *
 * @author Mark Frisbey
 */
public class WebServerTest {

    // mocks the thread pool
    @Mock
    private ExecutorService mockPool;

    // mocks the server socket
    @Mock
    private ServerSocket mockServerSocket;

    // mocks a client socket
    @Mock
    private Socket mockClientSocket;

    /**
     * Initialize common test resources
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Verifies that the call method works as intended when provided with valid data.
     */
    @Test
    public void callTest() throws Exception {
        WebServerResponse response = new WebServerResponse(HttpVersion.HTTP_1_1, HttpResponse.OK);

        Future<WebServerResponse> future = (Future<WebServerResponse>)Mockito.mock(Future.class);
        Mockito.when(future.get()).thenReturn(response);

        Mockito.when(mockServerSocket.accept()).thenReturn(mockClientSocket).thenThrow(IOException.class);

        MockWebServer server = new MockWebServer("/webserverroot", mockPool, mockServerSocket);
        server.setRequestThreshold(1);
        assertFalse("Server should not be started after initialization", server.isStopped());

        server.run();
        assertEquals("Unexpected number of requests processed", 1, server.getRequestsProcessed());

        Mockito.verify(mockPool).shutdown();
        Mockito.verify(mockServerSocket).close();
    }

    /**
     * Verifies that call fails gracefully when an exception occurs.
     */
    @Test
    public void callExceptionTest() throws Exception {
        Mockito.when(mockServerSocket.accept()).thenThrow(IOException.class);

        WebServer server = new WebServer("/webserverroot", mockPool, mockServerSocket);
        server.run();
        Mockito.verify(mockPool).shutdown();
        Mockito.verify(mockServerSocket).close();
    }

    /**
     * Exercises the stop method
     */
    @Test
    public void stopTest() throws IOException {
        WebServer server = new WebServer("/webserverroot", mockPool, mockServerSocket);
        server.stop();
        assertTrue("Unexpected isStopped value after stopping", server.isStopped());

        Mockito.verify(mockPool).shutdown();
        Mockito.verify(mockServerSocket).close();
    }

    /**
     * Exercises the stop method when an IO exception is thrown.
     */
    @Test(expected = IOException.class)
    public void stopExceptionTest() throws IOException {
        Mockito.doThrow(new IOException()).when(mockServerSocket).close();

        WebServer server = new WebServer("/webserverroot", mockPool, mockServerSocket);
        server.stop();

        Mockito.verify(mockPool).shutdown();
        Mockito.verify(mockServerSocket).close();
    }
}
