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

package com.adobe.aem.webserver.utility;

/**
 * A collection of helper methods for easily accomplishing common actions dealing with strings.
 *
 * @author Mark Frisbey
 */
public class StringUtils {

    /**
     * Determines whether a string value is null or empty. A string is considered empty when the instance is not null
     * and the length is 0.
     *
     * @param value The string to be checked.
     * @return true if the string value is null or has 0 length, otherwise false.
     */
    public static boolean isNullOrEmpty(String value) {
        return (value == null || value.length() == 0);
    }

    /**
     * Concatenates multiple string values together, ensuring that each value consists of only forward slashes. Each
     * value that is provided will be separated from any previous values with a forward slash. For example, if the
     * invocation is {@code StringUtils.buildPath("\\directory1", "directory2/", "/file1.txt");} then the return value
     * will be {@code "/directory1/directory2/file1.txt"}. Note that the path consists of only forward slashes and
     * each directory name/file name is properly separated by forward slashes.
     *
     * @param paths The directory/file paths to be concatenated. Each value will be cleansed of invalid slashes and
     *              separated from previous paths with a single forward slash.
     * @return A properly formatted file system path.
     */
    public static String buildPath(String... paths) {
        StringBuilder finalPath = new StringBuilder();

        for (String path : paths) {
            // ensure only one type of slash is used
            path = path.replace("\\", "/");

            // handle slash separator if previous values exist
            if (finalPath.length() > 0) {
                // make sure any previous path ended with a slash
                if (finalPath.charAt(finalPath.length() - 1) != '/') {
                    finalPath.append("/");
                }

                // strip backslash from beginning of current path value
                if (path.length() > 0 && path.charAt(0) == '/') {
                    path = path.substring(1);
                }
            }

            finalPath.append(path);
        }

        return finalPath.toString();
    }
}
