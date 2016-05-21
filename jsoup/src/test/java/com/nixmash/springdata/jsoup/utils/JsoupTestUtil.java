package com.nixmash.springdata.jsoup.utils;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by daveburke on 5/21/16.
 */
public class JsoupTestUtil {

    public static File getFile(String resourceName) {
        try {
            File file = new File(JsoupTestUtil.class.getResource(resourceName).toURI());
            return file;
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}
