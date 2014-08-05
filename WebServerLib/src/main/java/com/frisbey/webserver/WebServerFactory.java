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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

/**
 * Provides various methods for retrieving {@link com.frisbey.webserver.WebServer} instances that have been
 * configured to perform in different ways.
 *
 * @author Mark Frisbey
 * @see com.frisbey.webserver.WebServer
 */
public class WebServerFactory {

    /**
     * Retrieves a web server that will used a fixed thread pool of a given size. The server will also listen on the
     * provided port and look in the given directory for resources that have been requested.
     *
     * @param port The port on which the server will listen.
     * @param poolSize The number of threads that the web server can execute at one time.
     * @param webServerRoot Full path to the local directory that the web server will search when looking for requested
     *                      resources.
     * @return A web server configured with a fixed thread pool.
     * @throws IOException thrown if there issues initializing a thread pool for the server.
     */
    public static WebServer getFixedThreadPoolServer(int port, int poolSize, String webServerRoot) throws IOException {
        return new WebServer(webServerRoot, Executors.newFixedThreadPool(poolSize), new ServerSocket(port));
    }
}
