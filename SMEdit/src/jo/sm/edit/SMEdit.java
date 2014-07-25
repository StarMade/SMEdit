/**
 * Copyright 2014 SMEdit https://github.com/StarMade/SMEdit SMTools
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
 */
package jo.sm.edit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JFrame;
import jo.util.GlobalConfiguration;
import jo.util.OptionScreen;
import jo.util.Paths;
import static jo.util.Paths.getDownloadCaches;
import jo.util.Update;
import jo.util.io.HttpClient;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 * @Auther Robert Barefoot for SMEdit - version 1.1
 */
public class SMEdit extends JFrame {

    private static File mOptionDir;
    private static SMEdit app;
    private static final long serialVersionUID = 1L;

    private static final Update updater = new Update(app);
    private static final Logger log = Logger.getLogger(SMEdit.class.getName());

    public static void main(final String[] args) {
        GlobalConfiguration.createDirectories();
        if (!Paths.validateCurrentDirectory()) {
            return;
        }
        updater.checkUpdate(true);
        if (updater.update == -1) {
            OptionScreen opts = new OptionScreen(args);
        }

    }
    private final String[] mArgs;

    public SMEdit(final String[] args) {
        mArgs = args;
        mOptionDir = new File(Paths.getHomeDirectory());
        File jo_smJar = new File(mOptionDir, "jo_sm.jar");
        if (!jo_smJar.exists()) {
            for (final Map.Entry<String, File> item : getDownloadCaches().entrySet()) {
                try {
                    HttpClient.download(new URL(item.getKey()), item.getValue());
                } catch (final IOException e) {
                }
            }
        }
        try {
            URL josmURL = jo_smJar.toURI().toURL();
            URLClassLoader smLoader = new URLClassLoader(new URL[]{josmURL}, SMEdit.class.getClassLoader());
            Class<?> rf = smLoader.loadClass("Boot");
            Method main = rf.getMethod("main", String[].class);
            main.invoke(null, (Object) mArgs);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException | MalformedURLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
