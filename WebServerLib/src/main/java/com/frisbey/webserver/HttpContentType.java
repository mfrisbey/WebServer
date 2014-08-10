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

import com.frisbey.webserver.utility.StringUtils;

/**
 * Enumeration representing various HTTP content types that the server can provide. The enumeration can be easily
 * extended to support additional content types. A content type consists of a string description.
 *
 * @author Mark Frisbey
 */
public enum HttpContentType {

    /**
     * The content is text or html.
     */
    TEXT_HTML("text/html", new String[] { }),

    /**
     * The content is a jpeg image.
     */
    IMAGE_JPEG("image/jpeg", new String[] {".jpg", ".jpeg"}),

    /**
     * The content is a gif image.
     */
    IMAGE_GIF("image/gif", new String[] {".gif"}),

    /**
     * The content is a compressed zip file.
     */
    ZIP_COMPRESSED("application/x-zip-compressed", new String[] {".zip"}),

    /**
     * The content is a javascript file.
     */
    JAVASCRIPT("application/javascript", new String[] {".js"}),

    /**
     * The content is a css file.
     */
    CSS("text/css", new String[] {".css"});

    // the content type description
    private String content;

    // the extensions that will result in the content type
    private String[] extensions;

    HttpContentType(String content, String[] extensions) {
        this.content = content;
        this.extensions = extensions;
    }

    /**
     * Retrieves the string description of the content type.
     *
     * @return A description of the content.
     */
    public String getContentType() {
        return this.content;
    }

    /**
     * Retrieves the appropriate content type based on the extension of a file. If the file extension is not recognized
     * then the type will be assumed as text/html.
     *
     * @param filePath The full path to the file whose content type should be determined.
     * @return The content type for a file.
     */
    public static HttpContentType fromFilePath(String filePath) {
        HttpContentType finalContent = TEXT_HTML;

        if (!StringUtils.isNullOrEmpty(filePath)) {
            for (HttpContentType t : HttpContentType.values()) {
                for (String extension : t.extensions) {
                    if (filePath.length() >= extension.length() && filePath.substring(filePath.length() - extension.length()).compareToIgnoreCase(extension) == 0) {
                        finalContent = t;
                        break;
                    }
                }
            }
        }

        return finalContent;
    }
}
