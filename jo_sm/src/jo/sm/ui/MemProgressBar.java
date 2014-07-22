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
package jo.sm.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author elkes
 */
public class MemProgressBar extends JProgressBar {

    private final long kbFactor = (long) Math.pow(1024, 2);
    private final int maxHeapSizeMb;

    public MemProgressBar() {
        super();
        setStringPainted(true);
        maxHeapSizeMb = (int) (Runtime.getRuntime().maxMemory() / kbFactor);
        setMinimum(0);
        setMaximum(maxHeapSizeMb); 
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.gc();
            }
            @Override
            public void mousePressed(MouseEvent me) {}
            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}
            @Override
            public void mouseExited(MouseEvent me) {}
        });
        startTimer();
    }

    private void startTimer(){
        MemoryMonitorTimer memoryMonitorTimer = new MemoryMonitorTimer();
        memoryMonitorTimer.start();
    }
    
    private class MemoryMonitorTimer extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    final int usedMemoryMb = (int) (usedMem / kbFactor);
                    final String barString = usedMemoryMb + "Mb/" + maxHeapSizeMb + "Mb";
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setValue(usedMemoryMb);
                            setString(barString);
                        }
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException exc) {
                System.err.println("MemProgressBar.MemoryMonitorTimer: "+exc);
            }
        }
    }
}
