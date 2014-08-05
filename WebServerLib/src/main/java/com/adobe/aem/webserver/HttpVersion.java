package com.adobe.aem.webserver;

/**
 * Represents valid HTTP versions. The enumeration can be easily extended to support additional versions. A
 * version consists of the name of the version as defined in HTTP standards.
 *
 * @author Mark Frisbey
 */
public enum HttpVersion {

    /**
     * Signifies that the response will be in HTTP 1.0 format.
     */
    HTTP_1_0("HTTP/1.0"),

    /**
     * Signifies that the response will be in HTTP 1.1 format.
     */
    HTTP_1_1("HTTP/1.1");

    // stores the name of the version
    private String version;

    /**
     * Constructs a new version from a given version description.
     *
     * @param version The name of the HTTP version according to HTTP standards.
     */
    HttpVersion(String version) {
        this.version = version;
    }

    /**
     * Retrieves the text description of the HTTP version.
     *
     * @return The HTTP version name according to HTTP standards.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Creates an HttpVersion enum value from the text description of the HTTP version. The string comparison will be
     * case insensitive. If the HttpVersion enumeration doesn't recognize the version value then the instance returned
     * will be null.
     *
     * @param version The HTTP version name according to HTTP standards.
     * @return The enumeration value corresponding with the given version description, or null if the version name was
     * not recognized.
     */
    public static HttpVersion fromString(String version) {
        HttpVersion finalVer = null;

        if (version != null) {
            // loop through all the known enumerations and see if the version text matches.
            for (HttpVersion ver : HttpVersion.values()) {
                if (version.equalsIgnoreCase(ver.getVersion())) {
                    finalVer = ver;
                    break;
                }
            }
        }

        return finalVer;
    }
}
