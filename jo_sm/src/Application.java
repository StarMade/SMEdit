
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

import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ui.RenderFrame;
import jo.util.SplashScreen;

/**
 * @author Robert Barefoot - version 1.0
 */
public class Application {

    private static RenderFrame gui;

    public static void preLoad() {
        Properties props = StarMadeLogic.getProps();
        String home = props.getProperty("starmade.home", "");
        if (!StarMadeLogic.isStarMadeDirectory(home)) {
            home = System.getProperty("user.dir");
            if (!StarMadeLogic.isStarMadeDirectory(home)) {
                home = JOptionPane.showInputDialog(null, "Enter in the home directory for StarMade", home);
                if (home == null) {
                    System.exit(0);
                }
            }
            props.put("starmade.home", home);
            StarMadeLogic.saveProps();
        }
        StarMadeLogic.setBaseDir(home);
    }
    
    public static void main(final String[] args) {
        
        final SplashScreen splash = new SplashScreen(args);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                splash.close();
                preLoad();
                gui.main(args);
            }
        });
    }
}
