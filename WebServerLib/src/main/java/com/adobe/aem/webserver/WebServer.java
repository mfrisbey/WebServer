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

package com.adobe.aem.webserver;

import com.adobe.aem.webserver.request.WebServerRequestFactory;
import com.adobe.aem.webserver.response.WebServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A very simple web server that, when running, will listen on a given port. Whenever data is received on the port, the
 * server will launch a thread that will process the data received and provide an appropriate response based on the
 * contents of the data received.
 *
 * <p>The server makes use of a thread pool for ensuring that its child threads do not become unmanageable for the
 * machine running the server process. The {@link com.adobe.aem.webserver.WebServerFactory} provides various means
 * for retrieving an instance of a WebServer.</p>
 *
 * @author Mark Frisbey
 * @see com.adobe.aem.webserver.WebServerFactory
 */
public class WebServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WebServerRequestFactory.class);

    // the socket that the web server will use for listening
    private ServerSocket serverSocket;

    // stores whether or not the server is stopped
    private boolean isStopped;

    // the thread pool that the server will use to launch threads
    private ExecutorService serverThreadPool;

    // the full path to the local directory where the server will look for files
    private String webServerRoot;

    // stores the total number of requests that have been received by the server
    private int requestsProcessed;

    /**
     * Initializes a new server using the given information. The server will be in a stopped state after initialization.
     *
     * @param webServerRoot The full path to the local directory where the server will look for requested resources.
     * @param threadPool The pool that the server will use to launch its child threads. The server will assume ownership
     *                   of the thread pool and will shut down the pool when it is finished with it.
     * @param listenSocket The socket that the server will use to listen for requests. The server will assume ownership
     *                     of the socket and will close it when it has finished with it.
     */
    public WebServer(String webServerRoot, ExecutorService threadPool, ServerSocket listenSocket){
        this.serverThreadPool = threadPool;
        this.webServerRoot = webServerRoot;
        this.isStopped = false;
        this.serverSocket = null;
        this.requestsProcessed = 0;
        this.serverSocket = listenSocket;
    }

    /**
     * Starts the server so that it will begin listening for requests to its configured port.
     */
    @Override
    public void run() {
        logger.debug("entering");

        setIsStopped(false);
        while(!isStopped()) {
            Socket clientSocket = null;
            try {
                logger.debug("listening for requests");
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(!isStopped()) {
                    logger.warn("unexpected exception while listening for requests");

                    // attempt to stop the server to free resources
                    try {
                        stop();
                    } catch (IOException ex) {
                        logger.error("unhandled exception when attempting to stop web server", ex);
                    }

                    logger.error("Error accepting client connection", e);
                }

                logger.warn("swallowing io exception under assumption that it was thrown due to the socket being closed as a result of the server being stopped", e);
            }

            if (!isStopped()) {
                this.requestsProcessed++;
                logger.debug("received request. adding thread for request {} to thread pool.", this.requestsProcessed);
                getThreadPool().execute(new RequestThread(clientSocket, this.webServerRoot));
            }
        }
        logger.debug("shutting down after processing {} requests", this.requestsProcessed);
    }

    /**
     * Retrieves the number of requests that the server has processed so far.
     *
     * @return The number of processed requests.
     */
    public int getRequestsProcessed() {
        return this.requestsProcessed;
    }

    /**
     * Instructs the server to stop listening for requests.
     */
    public void stop() throws IOException{
        setIsStopped(true);
        try {
            // close the server's socket, which will for the socket's accept() call to return.
            this.serverSocket.close();
        } catch (IOException e) {
            logger.warn("unexpected io exception when attempting to close server socket");
            throw new IOException("There was an issue closing the server's socket", e);
        } finally {
            // shut down the server's thread pool
            getThreadPool().shutdown();
        }
    }

    /**
     * Retrieves the thread pool that the server is using to launch child threads.
     *
     * @return A server for launching child threads.
     */
    private ExecutorService getThreadPool() {
        return this.serverThreadPool;
    }

    /**
     * Returns a value indicating whether the server is running or not.
     *
     * @return true if the server is NOT running, false if the server is currently running.
     */
    public boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Sets a value indicating whether or not the server is currently running.
     *
     * @param stopped If true the server will be considered stopped.
     */
    private void setIsStopped(boolean stopped) {
        this.isStopped = stopped;
    }
}
