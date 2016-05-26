package com.nixmash.springdata.jsoup.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by daveburke on 5/21/16.
 * Some of the utility methods here taken from Jsoup Source at https://github.com/jhy/jsoup
 */
public class JsoupUtil {

    public static File getFile(String resourceName) {
        try {
            File file = new File(JsoupUtil.class.getResource(resourceName).toURI());
            return file;
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }


    public static String getBaseUri(String url) {
        String baseUri = null;
        try {
            URL linkURL = new URL(url);
            baseUri = String.format("%s://%s", linkURL.getProtocol(), linkURL.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return baseUri;
    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     *
     * @param baseUrl the existing absolute base URL
     * @param relUrl  the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return an absolute URL if one was able to be generated, or the empty string if not
     */
    public static String resolve(final String baseUrl, final String relUrl) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute/rel may be abs on its own, so try that
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            }
            return resolve(base, relUrl).toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }

    }

    /**
     * Create a new absolute URL, from a provided existing absolute URL and a relative URL component.
     *
     * @param base   the existing absolulte base URL
     * @param relUrl the relative URL to resolve. (If it's already absolute, it will be returned)
     * @return the resolved absolute URL
     * @throws MalformedURLException if an error occurred generating the URL
     */
    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (relUrl.startsWith("?"))
            relUrl = base.getPath() + relUrl;
        // workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base, relUrl);
    }

    public static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + "...";
        else
            return s;
    }

    public static Integer attrIntToNull(String s) {
        Integer i = 0;
        if (s == null) {
            return null;
        } else {
            try {
                i = Integer.parseInt(s);
            } catch (Exception ex) {
                i = null;
            }
        }
        return i;
    }
}
