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
 * Represents valid HTTP methods. The enumeration can be easily extended to support additional method types. A method
 * consists of a string description.
 *
 * @author Mark Frisbey
 */
public enum HttpMethod {

    /**
     * An HTTP GET request.
     */
    GET("GET"),

    /**
     * An HTTP HEAD request.
     */
    HEAD("HEAD");

    // stores the string description of the method
    private String method;

    /**
     * Initializes a new method that will have the given description.
     *
     * @param method The description that will be associated with the method.
     */
    HttpMethod(String method) {
        this.method = method;
    }

    /**
     * Retrieves a method's string description.
     *
     * @return The description of an HTTP method.
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Retrieves an HTTP method based on a description. The comparison will be case insensitive. If a method
     * with the given description cannot be found then the a null value will be returned.
     *
     * @param method The description of an HTTP method to retrieve.
     * @return The HTTP method matching the given string description.
     */
    public static HttpMethod fromString(String method) {
        HttpMethod finalMethod = null;

        if (method != null) {
            for (HttpMethod m : HttpMethod.values()) {
                if (method.equalsIgnoreCase(m.getMethod())) {
                    finalMethod = m;
                    break;
                }
            }
        }

        return finalMethod;
    }
}
