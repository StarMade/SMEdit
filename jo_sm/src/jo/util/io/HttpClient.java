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
package jo.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import jo.util.GlobalConfiguration;
import jo.util.OperatingSystem;


public class HttpClient {

    private static final Logger log = Logger.getLogger(HttpClient.class
            .getName());
    private static String httpUserAgent = null;

    public static String getHttpUserAgent() {
        if (httpUserAgent == null) {
            httpUserAgent = getDefaultHttpUserAgent();
        }
        return httpUserAgent;
    }

    private static String getDefaultHttpUserAgent() {
        final boolean x64 = System.getProperty("sun.arch.data.model").equals("64");
        final String os;
        switch (GlobalConfiguration.getCurrentOperatingSystem()) {
            case MAC:
                os = "Macintosh; Intel Mac OS X 10_7_3";
                break;
            case LINUX:
                os = "X11; Linux " + (x64 ? "x86_64" : "i686");
                break;
            default:
                os = "Windows NT 6.1" + (x64 ? "; WOW64" : "");
                break;
        }
        final StringBuilder buf = new StringBuilder(125);
        buf.append("Mozilla/5.0 (").append(os).append(")");
        buf.append(" AppleWebKit/534.55.3 (KHTML, like Gecko) Chrome/35.0.1985.125 Safari/534.55.3");
        return buf.toString();
    }

    public static HttpURLConnection getHttpConnection(final URL url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        con.addRequestProperty("Accept-Encoding", "gzip");
        con.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        con.addRequestProperty("Host", url.getHost());
        con.addRequestProperty("User-Agent", getHttpUserAgent());
        con.setConnectTimeout(10000);
        return con;
    }

    private static HttpURLConnection getConnection(final URL url) throws IOException {
        final HttpURLConnection con = getHttpConnection(url);
        con.setUseCaches(true);
        return con;
    }

    public static URL getFinalURL(final URL url) throws IOException {
        return getFinalURL(url, true);
    }

    private static URL getFinalURL(final URL url, final boolean httpHead) throws IOException {
        final HttpURLConnection con = getConnection(url);
        con.setInstanceFollowRedirects(false);
        if (httpHead) {
            con.setRequestMethod("HEAD");
        }
        con.connect();
        switch (con.getResponseCode()) {
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
                return getFinalURL(new URL(con.getHeaderField("Location")), true);
            case HttpURLConnection.HTTP_BAD_METHOD:
                return getFinalURL(url, false);
            default:
                return url;
        }
    }

    private static HttpURLConnection cloneConnection(final HttpURLConnection con) throws IOException {
        final HttpURLConnection cloned = (HttpURLConnection) con.getURL().openConnection();
        for (final Entry<String, List<String>> prop : con.getRequestProperties().entrySet()) {
            final String key = prop.getKey();
            for (final String value : prop.getValue()) {
                cloned.addRequestProperty(key, value);
            }
        }
        return cloned;
    }

    public static boolean isModifiedSince(URL url, long date) {
        try {
            url = getFinalURL(url);
            date -= TimeZone.getDefault().getOffset(date);
            final HttpURLConnection con = getConnection(url);
            con.setRequestMethod("HEAD");
            con.connect();
            final int resp = con.getResponseCode();
            con.disconnect();
            return resp != HttpURLConnection.HTTP_NOT_MODIFIED;
        } catch (final IOException ignored) {
            return true;
        }
    }

    public static HttpURLConnection download(final HttpURLConnection con, final File file) throws IOException {
        if (file.exists()) {
            final HttpURLConnection head = cloneConnection(con);
            final int offset = TimeZone.getDefault().getOffset(file.lastModified());
            head.setIfModifiedSince(file.lastModified() - offset);
            head.setRequestMethod("HEAD");
            head.connect();
            if (head.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                log.fine("Using " + file.getName() + " from cache");
                con.disconnect();
                head.disconnect();
                return head;
            }
        }

        log.fine("Downloading new " + file.getName());

        final byte[] buffer = downloadBinary(con);

        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.exists() && (!file.canRead() || file.canWrite())) {
            file.setReadable(true);
            file.setWritable(true);
        }
        if (file.exists() && file.canRead() && file.canWrite()) {
            final FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.flush();
            fos.close();
        }

        if (con.getLastModified() != 0L) {
            final int offset = TimeZone.getDefault().getOffset(con.getLastModified());
            file.setLastModified(con.getLastModified() + offset);
        }

        con.disconnect();
        return con;
    }

    public static byte[] download(final URL url) throws IOException {
        final URLConnection uc = url.openConnection();
        uc.setConnectTimeout(10000);
        final DataInputStream di = new DataInputStream(uc.getInputStream());
        final byte[] buffer = new byte[uc.getContentLength()];
        di.readFully(buffer);
        di.close();
        return buffer;
    }

    public static HttpURLConnection download(final URL url, final File file)
            throws IOException {
        return download(getConnection(url), file);
    }

    public static String downloadAsString(final URL url) throws IOException {
        return downloadAsString(getConnection(url));
    }

    public static String downloadAsString(final HttpURLConnection con) throws IOException {
        final byte[] buffer = downloadBinary(con);
        return new String(buffer);
    }

    public static byte[] downloadBinary(final URL url) throws IOException {
        return downloadBinary(getConnection(url));
    }

    private static byte[] downloadBinary(final URLConnection con) throws IOException {
        final DataInputStream di = new DataInputStream(con.getInputStream());
        byte[] buffer;
        final int len = con.getContentLength();
        if (len == -1) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b;
            while ((b = di.read()) != -1) {
                out.write(b);
            }
            buffer = out.toByteArray();
        } else {
            buffer = new byte[con.getContentLength()];
            di.readFully(buffer);
        }
        di.close();
        if (buffer != null) {
            buffer = ungzip(buffer);
        }
        return buffer;
    }

    public static byte[] load(final InputStream is) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final byte[] buffer = new byte[4096];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer, 0, n);
        }
        return os.toByteArray();
    }

    /**
     * This method initializes url use opening the users web browser to link to
     * a given url
     */
    public static void openURL(final String url) {
        final OperatingSystem os = GlobalConfiguration
                .getCurrentOperatingSystem();
        try {
            if (os == OperatingSystem.MAC) {
                final Class<?> fileMgr = Class
                        .forName("com.apple.eio.FileManager");
                final Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[]{String.class});
                openURL.invoke(null, url);
            } else if (os == OperatingSystem.WINDOWS) {
                Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
            } else { // assume Unix or Linux
                final String[] browsers = {"firefox", "opera", "konqueror",
                    "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; (count < browsers.length)
                        && (browser == null); count++) {
                    if (Runtime.getRuntime()
                            .exec(new String[]{"which", browsers[count]})
                            .waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (final Exception e) {
        }
    }

    private static byte[] ungzip(final byte[] data) {
        if (data.length < 2) {
            return data;
        }

        final int header = (data[0] | data[1] << 8) ^ 0xffff0000;
        if (header != GZIPInputStream.GZIP_MAGIC) {
            return data;
        }

        try {
            final ByteArrayInputStream b = new ByteArrayInputStream(data);
            final GZIPInputStream gzin = new GZIPInputStream(b);
            final ByteArrayOutputStream out = new ByteArrayOutputStream(
                    data.length);
            for (int c = gzin.read(); c != -1; c = gzin.read()) {
                out.write(c);
            }
            return out.toByteArray();
        } catch (final IOException e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * @param aHttpUserAgent the httpUserAgent to set
     */
    public static void setHttpUserAgent(String aHttpUserAgent) {
        httpUserAgent = aHttpUserAgent;
    }

    private HttpClient() {
    }
}
