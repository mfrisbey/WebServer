Web Server
=========
# Overview
This project contains code for a simple web server that can handle GET requests for static content files. Examples of
these types of files are html, gif, jpeg, and zip. The server is unable to process server-side scripting languages
like PHP or ASP, and it's also only able to handle HEAD and GET requests.

**_IMPORTANT NOTE:_** It's worthwhile to point out that this web server is in no way intended to be used in a production
environment. The purpose of the application is to showcase the author's coding practices and styles. To that end, the
project purposely avoids usage of notable libraries (such as [Jetty] (http://www.eclipse.org/jetty/)) that would make
the code much simpler. This was done to maximize the amount of original code. Under normal circumstances a more
concerted effort would have been made to avoid "reinventing the wheel."

# Usage
The repository includes pre-compiled jars called WebServerProcess-[version].jar. These jars were built using Maven 3.1.1 
on a Windows 7 environment using JDK 1.7. They have been verified to run correctly with JRE 7.0.

For detailed usage information, see the help message displayed when attempting to execute the jar without
parameters. For convenience, the jar is an "uber" jar that contains all dependent jars. A couple of ways to run the
jar are:

1. From a command line, execute the command `java -cp [path/to/jar/directory]/* com.frisbey.webserverprocess.WebServerExecutor`

2. From a command line, change directories to the same location as the jar and execute the command 
`java com.frisbey.webserverprocess.WebServerExecutor`.

## Building
The following dependencies are required in order to build the source code:

* Maven >= 3.1.1

* JDK >= 1.7

Once dependencies are in place, follow these steps to build the executable jar:

1. Retrieve source code from Git repository.

2. From a command line, change to the root source code directory. This is the same directory as the README.md and root
pom.xml file.

3. Run the command `mvn package`.

4. The executable jar will be located in the "WebServerProcess/target" directory.

# Architecture
The project consists of two modules:

* WebServerLib - a collection of classes and factories that make up the web server.
 
* WebServerProcess - a simple console application that uses the web server libraries to start the web server process.

The web server process will launch an instance of the web server in a separate thread using a thread pool. The server
itself will listen on a specified port; whenever a request is made to the configured port the server will launch a new
thread that will process the request that was received. This new thread will also execute in a thread pool.

From a high level, the web server will follow the flow illustrated by this diagram:

![Example SFTP Process](https://github.com/mfrisbey/WebServer/raw/master/images/RequestSequence.GIF)

* The web server will receive a request from a client.

* The server will convert the raw request into a WebServerRequest instance.

* The WebServerRequest will be interpreted and produce an appropriate WebServerResponse instance.

* The WebServerResponse will be converted to a raw response, which will be sent back to the client.

# Development Highlights
As mentioned previously, the purpose of this code base is to illustrate the coding and development practices of the
author. Here are some highlights worth mentioning:

* Architecture
    * The project employs principles from factory design patterns to provide alternate implementations of certain
    objects depending on configuration.
    * Efforts have been made to follow the Law of Demeter, ensuring that object dependency trees are simple and
    assisting in making code testable.
* Commenting
    * The solution makes use of extensive Javadoc comments.
    * While not every line of code is commented (nor should it be), an effort was made to provide explanations for the
    reasons that things were done the way they were.
* Logging
    * The project uses slf4j. This library was chosen for its simple ability to include parameterized lists of values
    in its log messages. It also allows a logging framework to be chosen _at deployment time_, meaning that the
    framework being used can be easily changed without requiring modifications to the source code.
    * In general, complicated methods include debug log messages at the beginning and end of the method body. Additional
    debug messages are used throughout the body as needed. These messages attempt to include useful information about
    notable variable values. This would help provide useful information when attempting to debug issues that occur in a 
    production environment.
    * Different log levels are used as follows:
        * DEBUG: messages that would only be useful to a developer. Are used as liberally as needed.
        * INFO: messages that highlight major milestones in the execution process.
        * WARNING: used when something happens that's out of the ordinary but not detrimental to execution of the 
        program. Handled exceptions are an example of something that generates a warning message.
        * ERROR: an exception or condition that prevents normal execution of the process has occurred. Details of
        the source of the error are logged with the message.
* Exception Handling
    * The rule for exception handling in the code is that exceptions are bubbled up to the highest level possible and
    then reported appropriately. Low level methods will simply throw their unhandled exceptions and rely on a higher 
    entities to report or handle them.
    * Specific exception types are used in order to provide useful insight into why an exception was generated and
    what the root cause was.
* Unit Testing
    * Tests exists for as many classes and methods as possible.
    * The starting point for unit tests was code coverage, meaning that an attempt was made to execute every line of
    code. Additional tests were added as unhandled errors were encountered.
    * The tests were in place before the process could run from start to finish, and oftentimes the tests were
    completed before a class was implemented.
    * Various techniques are employed to use mock objects so that the unit tests are environment agnostic. This means
    that the tests can be run on any machine without requiring external resources like files.
        * Some objects and operations are mocked using the Mockito framework.
        * Dependency Injection is utilized to provide alternate functionality in some cases.
        * Certain objects make use of object inheritance to alter the behavior and dependencies of key methods.
* Documentation
    * Documentation for how to use the solution is made available via GitHub.
    * Javadoc comments are exported to html and hosted on GitHub as part of the build process.
* Git Usage
    * The source code is housed in a Git repository.
    * Development of the solution was done using Git Flow. All new development was done on the develop branch or on 
    feature branches. Release branches were created whenever a new release of the server was cut. Hotfixes can be done
    on release branches. All branches created during the development cycle are available in the repository on GitHub.
* Maven Usage
    * The project uses Maven for simple compilation and resolution of dependencies.
    * The server is packaged into a single uber jar for convenient execution.