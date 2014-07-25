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
package jo.util;

import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import jo.util.io.HttpClient;

/**
 * @Auther Robert Barefoot for SMEdit - version 1.0
 */
public class Update {

    private static final Logger log = Logger.getLogger(Update.class.getName());
    private static Window parent;
    private static UpdateGUI download = null;
    private static final byte[] buffer = new byte[1024];

    private static int getLatestVersion() {
        try {
            InputStream is = new URL(URLs.SVERSION).openConnection().getInputStream();

            int off = 0;
            byte[] b = new byte[2];
            while ((off += is.read(b, off, 2 - off)) != 2) {
            }

            return ((0xFF & b[0]) << 8) + (0xFF & b[1]);
        } catch (final IOException e) {
            log.log(Level.INFO, "Failed to get version information! {0}", e.getMessage());
            return -1;
        }
    }
    public int update = -1;

    public Update(final Window parent) {
        this.parent = parent;
    }

    public void checkUpdate(final boolean checkup) {
        if (getLatestVersion() > GlobalConfiguration.getVersion()) {
            Update.log.info("New SMEdit_Classic App version available!");
            update = JOptionPane.showConfirmDialog(parent,
                    "A newer version of the SMEdit_Classic is available.\n\n"
                    + " Do you wish to update?\n\n"
                    + "\n\n"
                    + "Choosing not to update may result\n"
                    + "in problems running the client.",
                    "Update Avalable", JOptionPane.YES_NO_OPTION);
            if (update == 0) {
                for (final Map.Entry<String, File> item : Paths.getDownloadCaches().entrySet()) {
                    try {
                        HttpClient.download(new URL(item.getKey()), item.getValue());
                    } catch (final IOException e) {
                    }
                }
                updateBot();
            }
        }
    }

    public void download(final String address, final String localFileName) {
        try {
            final BufferedInputStream in = new BufferedInputStream(new URL(address).openStream());
            final FileOutputStream file = new FileOutputStream(localFileName);
            final BufferedOutputStream out = new BufferedOutputStream(file, 65535);

            int downloaded = 0;
            int percent = 0;
            int numRead = 0;
            final int totalBytes = in.available() * 3;

            while ((numRead = in.read(buffer, 0, 1024)) >= 0) {
                out.write(buffer, 0, numRead);
                downloaded += numRead / 1024;
                percent = downloaded / 24;
                UpdateGUI.percent = (percent);
                UpdateGUI.jLabel3.setText(" KBs " + downloaded + " / " + totalBytes + " - " + UpdateGUI.percent + "%");
            }

            out.flush();
            out.close();
            in.close();
        } catch (final IOException e) {
        }
    }

    public void updateBot() {
        if (download == null) {
            download = new UpdateGUI();
        }
        final String jarNew = GlobalConfiguration.NAME + "_1." + getLatestVersion() + ".jar";
        download(URLs.DOWNSTART, jarNew);
        final Runtime run = Runtime.getRuntime();
        try {
            run.exec("javaw -jar " + jarNew);
            System.exit(0);
        } catch (final IOException e) {
            log.log(Level.INFO, "Jar failed! {0}", e.getMessage());
        }

        return;
    }
}
