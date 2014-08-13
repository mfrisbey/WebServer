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

package com.frisbey.webserver.exception;

/**
 * An exception that signifies that the method of a raw HTTP header was invalid.
 *
 * @author Mark Frisbey
 */
public class InvalidMethodException extends InvalidRequestException {

    /**
     * Initializes an exception without a message.
     */
    public InvalidMethodException() {
        super();
    }

    /**
     * Initializes an exception with a message.
     *
     * @param message The message that will be associated with the exception.
     */
    public InvalidMethodException(String message) {
        super(message);
    }

    /**
     * Initializes an exception with a message and a cause.
     *
     * @param message The message that will be associated with the exception.
     * @param cause An exception that was the original cause of the issue.
     */
    public InvalidMethodException(String message, Exception cause) {
        super(message, cause);
    }
}
