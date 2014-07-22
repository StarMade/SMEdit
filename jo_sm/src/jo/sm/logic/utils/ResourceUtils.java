/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.logic.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ResourceUtils {

    public static String loadSystemResourceString(String path) throws IOException {
        return new String(loadSystemResourceBinary(path));
    }

    public static String loadSystemResourceString(String path, Class<?> source) throws IOException {
        return new String(loadSystemResourceBinary(path, source));
    }

    public static byte[] loadSystemResourceBinary(String path) throws IOException {
        return loadSystemResourceBinary(path, ResourceUtils.class);
    }

    public static byte[] loadSystemResourceBinary(String path, Class<?> source) throws IOException {
        ByteArrayOutputStream baos;
        try (InputStream is = loadSystemResourceStream(path, source)) {
            if (is == null) {
                return null;
            }   baos = new ByteArrayOutputStream();
        for (;;) {
            int ch = is.read();
            if (ch < 0) {
                break;
            }
            baos.write(ch);
        }
        }
        return baos.toByteArray();
    }

    public static InputStream loadSystemResourceStream(String path) {
        return loadSystemResourceStream(path, ResourceUtils.class);
    }

    public static InputStream loadSystemResourceStream(String path, Class<?> source) {
        ClassLoader loader = source.getClassLoader();
        InputStream is = loader.getResourceAsStream(path);
        if (is == null) {
            StringTokenizer st = new StringTokenizer(source.getName(), ".");
            int toks = st.countTokens();
            StringBuilder newPath = new StringBuilder();
            for (int i = 0; i < toks - 1; i++) {
                newPath.append(st.nextToken());
                newPath.append("/");
            }
            newPath.append(path);
            is = loader.getResourceAsStream(newPath.toString());
        }
        return is;
    }

    public static URL loadSystemResourceURL(String path) {
        return loadSystemResourceURL(path, ResourceUtils.class);
    }

    public static URL loadSystemResourceURL(String path, Class<?> source) {
        ClassLoader loader = source.getClassLoader();
        return loader.getResource(path);
    }

}
