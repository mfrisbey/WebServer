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

/**
 * Represents valid HTTP responses. The enumeration can be easily extended to support additional response types. A
 * response consists of a status code and a brief message.
 *
 * @author Mark Frisbey
 */
public enum HttpResponse {

    /**
     * An OK response, meaning that the web server was able to understand and process the original request.
     */
    OK(200, "OK"),

    /**
     * A Not Found response, meaning that the web server was unable to locate the URI in the request.
     */
    NotFound(404, "Not Found"),

    /**
     * An internal server error response, meaning that the web server encountered an error and was unable to
     * process the request.
     */
    InternalServerError(500, "Internal Server Error"),

    /**
     * The request could not be understood by the server due to malformed syntax.
     */
    BadRequest(400, "Bad Request"),

    /**
     * The server does not support the functionality required to fulfill the request.
     */
    NotImplemented(501, "Not Implemented");

    // the code portion of the response
    private int code;

    // the message part of the response
    private String text;

    /**
     * Constructs a new Http response from a status code and message.
     * @param code The code that will be associated with the response.
     * @param text The message that will be provided with the response.
     */
    HttpResponse(int code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * Retrieves the status code that will be provided with the response.
     *
     * @return An integer status code value.
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Retrieves the message that will be provided with the response.
     *
     * @return The message of the response.
     */
    public String getText() {
        return this.text;
    }
}
