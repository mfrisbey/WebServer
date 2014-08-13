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

package com.frisbey.webserverprocess;

import com.frisbey.webserver.WebServer;
import com.frisbey.webserver.WebServerFactory;
import com.frisbey.webserver.utility.StreamUtils;
import com.frisbey.webserver.utility.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides an executable for starting a {@link com.frisbey.webserver.WebServer}.
 *
 * <p>The web server executor will receive various configurable properties of a web server (such as a port and local
 * directory) and launch a web server process. The server instance itself will be launched in its own thread so the
 * executor will still be able to receive keyboard input. To stop the server, simply press the enter key and the
 * web server will shut down.</p>
 *
 * <p>Usage of the executor process is as follows:</p>
 * <p>
 *     {@code java -jar [path to web server jar] [port (required)] [server root path (required)] [max threads (optional-default 10)}
 * </p>
 * <p>The result of the previous command will be a web server listening on the given [port]. When the server receives a
 * request, it will look in the given [server root path] for any resources requested as part of the request. Each request
 * will be launched in its own thread, but the number of threads executing at a single time will never
 * exceed [max threads]</p>
 *
 * @author Mark Frisbey
 */
public class WebServerExecutor {

    private final static String kUsageTab = "  ";

    /**
     * Entry point for the web server executor program.
     *
     * @param args Values that were given to the program from the command line.
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            printUsage();
            return;
        }

        int port = -1;
        int poolSize = 10;

        // ensure port and pool size are valid integers
        try {
            port = Integer.parseInt(args[0]);

            if (args.length > 2) {
                poolSize = Integer.parseInt(args[2]);
            }

        } catch (NumberFormatException ex) {
            printUsage("PORT and MAX_THREADS must be valid integers");
            return;
        }

        String webServerRoot = args[1];

        File rootDir = new File(webServerRoot);

        // ensure root directory exists
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            printUsage("The web server root must exist and must be a directory");
            return;
        }

        // let's do this! fire up a thread pool for the server itself
        ExecutorService service = Executors.newFixedThreadPool(5);

        try {
            WebServer server = WebServerFactory.getFixedThreadPoolServer(port, poolSize, webServerRoot);

            System.out.println("Starting Server");
            service.execute(server);

            System.out.println(String.format("Server started on port %d. Press <Enter> to stop server.", port));

            BufferedReader in = StreamUtils.getStreamReader(System.in);

            in.read();

            System.out.println(String.format("Stopping Server. Processed a total of %d requests.", server.getRequestsProcessed()));
            server.stop();
            System.out.println("Exiting");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            service.shutdown();
        }
    }

    /**
     * Prints general instructions for using the program.
     */
    private static void printUsage() {
        printUsage(null);
    }

    /**
     * Prints instructions for using the program. Includes an additional message that will be set apart from the
     * default usage information.
     */
    private static void printUsage(String message) {
        if (!StringUtils.isNullOrEmpty(message)) {
            System.out.println(message);
            System.out.println("");
        }

        System.out.println("SYNOPSIS");
        System.out.println(kUsageTab+"java -jar PATH_TO_WEBSERVER.JAR PORT WEB_SERVER_ROOT [MAX_THREADS]");
        System.out.println("");

        System.out.println("DESCRIPTION");
        System.out.println(kUsageTab+"Starts a very simple web server that will listen on a given port.");
        System.out.println(kUsageTab+"To use the web server, follow these example steps:");
        System.out.println(kUsageTab+kUsageTab+"1. Choose a local directory with at least read access and create a static web file. For example, simple.html.");
        System.out.println(kUsageTab+kUsageTab+"2. Start the web server, giving it a port number and the full path to the local directory. For this example, assume port 9080.");
        System.out.println(kUsageTab+kUsageTab+"3. Open a web browser and navigate to the URL http://localhost:9080/simple.html. Change the port and file name as needed.");
        System.out.println(kUsageTab+kUsageTab+"4. Enjoy the viewing pleasure of your HTML page.");
        System.out.println("");

        System.out.println("ARGUMENTS");
        System.out.println(kUsageTab+"PATH_TO_WEBSERVER.JAR");
        System.out.println(kUsageTab+kUsageTab+"The path to the previously compiled Java artifact that contains the web server artifacts.");
        System.out.println(kUsageTab+"PORT");
        System.out.println(kUsageTab+kUsageTab+"The port number to which the web server will listen. Must be a valid integer and within acceptable port ranges.");
        System.out.println(kUsageTab+"WEB_SERVER_ROOT");
        System.out.println(kUsageTab+kUsageTab+"The full path to a local directory where the web server will look for requested resources.");
        System.out.println(kUsageTab+"MAX_THREADS");
        System.out.println(kUsageTab+kUsageTab+"Maximum number of request threads that the web server will spawn at any time. The parameter is optional and defaults to 10.");
    }
}
