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
package jo.sm.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.act.plugin.PluginProgressDlg;


public class RunnableLogic {

    private static final Logger log = Logger.getLogger(RunnableLogic.class.getName());

    public static void run(JFrame frame, String title, final IRunnableWithProgress runnable) {
        final PluginProgressDlg progress = new PluginProgressDlg(frame, title);
        Thread t = new Thread(title) {
            @Override
            public void run() {
                runnable.run(progress);
                progress.dispose();
            }
        };
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (t.isAlive() && !progress.isPleaseCancel()) {
            progress.setVisible(true);
            try {
                t.join();
            } catch (InterruptedException e) {
                log.log(Level.WARNING, "run failed!", e);
            }
        }
    }
}
