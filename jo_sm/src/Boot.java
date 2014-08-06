/**
 * Copyright 2014 SMEdit
 * https://github.com/StarMade/SMEdit SMTools
 * https://github.com/StarMade/SMTools
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
 *
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;

public class Boot {

    private static Properties mProps;

    public static void loadProps() {
        File home = new File(System.getProperty("user.home"));
        File props = new File(home, ".josm");
        if (props.exists()) {
            mProps = new Properties();
            try {
                try (FileInputStream fis = new FileInputStream(props)) {
                    mProps.load(fis);
                }
            } catch (IOException e) {

            }
        } else {
            mProps = new Properties();
        }
    }

    public static void main(final String[] args) throws IOException {
        loadProps();
        String location = Boot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        location = URLDecoder.decode(location, "UTF-8").replaceAll("\\\\", "/");
        final String os = System.getProperty("os.name").toLowerCase();
        final String flags = "-Xmx" + mProps.getProperty("memory", "") + "g";
        //final String flags = "-Xmx2g";

        if (os.contains("windows")) {
            Runtime.getRuntime().exec("javaw " + flags + " -classpath \""
                    + location + "\" jo.sm.ui.RenderFrame");
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{"/bin/sh",
                "-c", "java " + flags + " -Xdock:name=\"SMEdit\""
                + " -Xdock:icon=resources/images/icon.png"
                + " -classpath \"" + location + "\" jo.sm.ui.RenderFrame"});
        } else {
            Runtime.getRuntime().exec(new String[]{"/bin/sh",
                "-c", "java " + flags + " -classpath \"" + location + "\" jo.sm.ui.RenderFramen"});
        }

    }
}
