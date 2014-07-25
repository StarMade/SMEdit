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
 */
package jo.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/**
 * Handles the configuration of the main app layout and logic start up
 *
 * @author Robert Barefoot for SMEdit - version 1.0
 */
@SuppressWarnings({"CallToPrintStackTrace", "null"})
public class GlobalConfiguration {


    public static final String NAME = "SMEdit";
    public static final String NAME_LOWERCASE = NAME.toLowerCase();
    public static final String SITE_NAME = "Lazygamerz";
    private static final OperatingSystem CURRENT_OS;
    private static boolean RUNNING_FROM_JAR = false;

    static {
        final URL resource = GlobalConfiguration.class.getClassLoader().getResource(Resources.SVERSION);
        if (resource != null) {
            GlobalConfiguration.setRUNNING_FROM_JAR(true);

        }
        final String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            CURRENT_OS = OperatingSystem.MAC;
        } else if (os.contains("Windows")) {
            CURRENT_OS = OperatingSystem.WINDOWS;
        } else if (os.contains("Linux")) {
            CURRENT_OS = OperatingSystem.LINUX;
        } else {
            CURRENT_OS = OperatingSystem.UNKNOWN;
        }

        if (GlobalConfiguration.isRUNNING_FROM_JAR()) {
            String path;
            path = resource.toString();
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (final UnsupportedEncodingException ignored) {
            }
            final String prefix = "jar:file:/";
            if (path.indexOf(prefix) == 0) {
                path = path.substring(prefix.length());
                path = path.substring(0, path.indexOf('!'));
                if (File.separatorChar != '/') {
                    path = path.replace('/', File.separatorChar);
                }
                try {
                    final File pathfile = new File(Paths.getPathCache());
                    if (pathfile.exists()) {
                        pathfile.delete();
                    }
                    pathfile.createNewFile();
                    try (Writer out = new BufferedWriter(new FileWriter(
                            Paths.getPathCache()))) {
                        out.write(path);
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDirectories() {
        final ArrayList<String> dirs;
        dirs = new ArrayList<>();
        dirs.add(Paths.getHomeDirectory());
        dirs.add(Paths.getResourceDirectory());
        dirs.add(Paths.getIconDirectory());
        dirs.add(Paths.getLogsDirectory());
        dirs.add(Paths.getScreenshotsDirectory());
        dirs.add(Paths.getPluginsDirectory());
        dirs.add(Paths.getShapeLibraryDirectory());
        dirs.add(Paths.getCacheDirectory());
        dirs.add(Paths.getSettingsDirectory());
        dirs.add(Paths.getIsanthDirectory());
        dirs.add(Paths.getIsanthDataDirectory());   
        for (final String name : dirs) {
            final File dir = new File(name);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    public static OperatingSystem getCurrentOperatingSystem() {
        return GlobalConfiguration.CURRENT_OS;
    }

    public static Image getImage(final String resource) {
        try {
            return Toolkit.getDefaultToolkit().getImage(
                    getResourceURL(resource));
        } catch (final MalformedURLException e) {
        }
        return null;
    }

    public static BufferedImage getImageFile(final File resource) {
        try {
            return ImageIO.read(resource);
        } catch (final IOException e) {
        }
        return null;
    }

    public static URL getResourceURL(final String path)
            throws MalformedURLException {
        return isRUNNING_FROM_JAR() ? GlobalConfiguration.class.getResource(path)
                : new File(path).toURI().toURL();
    }

    public static int getVersion() {
        try {
            //File file = new File(Paths.getVersionCache());
            //final InputStream is = new FileInputStream(file);
            final InputStream is = isRUNNING_FROM_JAR() ? GlobalConfiguration.class.getClassLoader().getResourceAsStream(Resources.SVERSION) : new FileInputStream(Paths.SVERSION);

            int off = 0;
            final byte[] b = new byte[2];
            while ((off += is.read(b, off, 2 - off)) != 2) {
            }

            return ((0xFF & b[0]) << 8) + (0xFF & b[1]);
        } catch (final IOException e) {
            
        }
        return -1;
    }

    /**
     * @return the RUNNING_FROM_JAR
     */
    public static boolean isRUNNING_FROM_JAR() {
        return RUNNING_FROM_JAR;
    }

    /**
     * @param aRUNNING_FROM_JAR the RUNNING_FROM_JAR to set
     */
    public static void setRUNNING_FROM_JAR(boolean aRUNNING_FROM_JAR) {
        RUNNING_FROM_JAR = aRUNNING_FROM_JAR;
    }
    
    
}
