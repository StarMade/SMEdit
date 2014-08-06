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
 *
 */
package jo.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import jo.log.LabelLogHandler;
import jo.log.LogOutputStream;
import jo.log.SystemConsoleHandler;
import jo.sm.logic.StarMadeLogic;

/**
 * This is the main splash or load screen for the app. It handles the start up
 * of the client app logic in the config file and other areas
 *
 * @authoe Robert Barefoot - version 1.0
 */
public class SplashScreen extends JDialog {

    private final static Logger log = Logger.getLogger(SplashScreen.class.getName());
    private static final long serialVersionUID = 5520543482560560389L;
    private static SplashScreen instance = null;

    private static void bootstrap() {
        Logger.getLogger("").addHandler(new SystemConsoleHandler());
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            private final Logger log = Logger.getLogger("EXCEPTION");

            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                final String ex = "Exception", msg = t.getName() + ": ";
                if (GlobalConfiguration.isRUNNING_FROM_JAR()) {
                    Logger.getLogger(ex).logp(Level.SEVERE, "EXCEPTION", "", "Unhandled exception in thread " + t.getName() + ": ", e);
                } else {
                    log.logp(Level.SEVERE, ex, "", msg, e);
                }
            }
        });
        if (!GlobalConfiguration.isRUNNING_FROM_JAR()) {
            System.setErr(new PrintStream(new LogOutputStream(Logger.getLogger("STDERR"), Level.SEVERE), true));
        }
    }

    private final String[] args;
    public final boolean error;

    public SplashScreen(final String[] args) {
        instance = this;
        this.args = args;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(1);
            }
        });

        setTitle(GlobalConfiguration.NAME);
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        final ImageIcon icon = new ImageIcon();
        icon.setImage(GlobalConfiguration.getImage(Resources.SPLASH));
        final JLabel label1 = new JLabel();
        label1.setIcon(icon);
        final LabelLogHandler handler = new LabelLogHandler();
        Logger.getLogger("").addHandler(handler);
        handler.label.setBorder(createTitledBorder(" Startup Events "));
        final Font font = handler.label.getFont();
        handler.label.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        handler.label.setPreferredSize(new Dimension(400, 30 + 12));
        final JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setOpaque(true);
        progress.setSize(380, 15);
        progress.setLocation(10, 255);
        label1.add(progress);
        add(label1, BorderLayout.NORTH);
        add(handler.label, BorderLayout.SOUTH);
        pack();
        try {
            log.info("Loading");
            setLocationRelativeTo(getOwner());
            setResizable(false);
            setVisible(true);
            setAlwaysOnTop(true);
            Thread.sleep(300);
        } catch (InterruptedException | SecurityException exc) {
        }
        String err = null;
        try {
            log.info("Starting Bootstrap");
            bootstrap();
            Thread.sleep(300);
        } catch (InterruptedException exc) {
        }
        if (err == null) {
            this.error = false;
            try {
                log.info("Loading Application");
                GlobalConfiguration.registerLogging();
                Logger.getLogger("").removeHandler(handler);
                Thread.sleep(300);

            } catch (InterruptedException | SecurityException exc) {
            }
        } else {
            this.error = true;
            progress.setIndeterminate(false);
            log.severe(err);
        }

    }

    public void close() {
        if (instance != null) {
            instance.dispose();
        }
    }

}
