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

package com.frisbey.webserver.test.utility;

import com.frisbey.webserver.utility.StringUtils;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Exercises the StringUtils class.
 *
 * @author Mark Frisbey
 */
public class StringUtilsTest {

    /**
     * Validates the isNullOrEmpty method.
     */
    @Test
    public void isNullOrEmptyTest() {
        assertTrue("Unexpected result from null string test", StringUtils.isNullOrEmpty(null));
        assertTrue("Unexpected result from empty string test", StringUtils.isNullOrEmpty(""));
        assertFalse("Unexpected result from non-empty string test", StringUtils.isNullOrEmpty("hello"));
        assertFalse("Unexpected result from whitespace string test", StringUtils.isNullOrEmpty(" "));
    }

    /**
     * Validates the createPath method.
     */
    @Test
    public void createPathTest() {
        assertEquals("Unexpected result from string with backslashes", "/dir1", StringUtils.buildPath("\\dir1"));
        assertEquals("Unexpected result from string ending with slash", "dir1/", StringUtils.buildPath("dir1/"));
        assertEquals("Unexpected result from subsequent value not beginning with slash", "/dir1/dir2", StringUtils.buildPath("/dir1", "dir2"));
        assertEquals("Unexpected result from initial value ending with slash", "/dir1/dir2", StringUtils.buildPath("/dir1\\", "dir2"));
        assertEquals("Unexpected result from initial value ending with slash AND subsequent value beginning with slash", "/dir1/dir2/", StringUtils.buildPath("/dir1/", "/dir2\\"));
    }

    @Test
    public void trimQueryStringTest() {
        assertEquals("Unexpected result from string with query string", "/file1.html", StringUtils.trimQueryString("/file1.html?id=10"));
        assertEquals("Unexpected result from string without query string", "/file1.html", StringUtils.trimQueryString("/file1.html"));
    }
}
