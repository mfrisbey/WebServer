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

import com.frisbey.webserver.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

/**
 * A mock version of a web server. Allows simulation of the server being stopped as if by an interrupt.
 *
 * @author Mark Frisbey
 */
public class MockWebServer extends WebServer {

    private int requestThreshold;

    /**
     * Initializes a new server using the given information. The server will be in a stopped state after initialization.
     */
    public MockWebServer(String webServerRoot, ExecutorService threadPool, ServerSocket listenSocket) {
        super(webServerRoot, threadPool, listenSocket);
        this.requestThreshold = -1;
    }

    /**
     * Sets the number of requests that the server should process before returning true for isStopped.
     *
     * @param threshold The number of requests to process before returning true for isStopped.
     */
    public void setRequestThreshold(int threshold) {
        this.requestThreshold = threshold;
    }

    /**
     * Overridden to return true after a specified number of invocations.
     */
    @Override
    public boolean isStopped() {
        if (this.requestThreshold >= 0 && getRequestsProcessed() >= this.requestThreshold) {
            try {
                this.stop();
            } catch (IOException ex) {
                throw new RuntimeException("There was an issue stopping the mock web server", ex);
            }
        }

        return super.isStopped();
    }
}