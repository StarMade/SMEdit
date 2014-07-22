
package jo.sm.logic.macro;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import jo.sm.mods.IPluginCallback;


public class NullPluginCallback implements IPluginCallback {
    private static final Logger log = Logger.getLogger(NullPluginCallback.class.getName());

    private int mTotal = 0;
    private int mDone = 0;
    private int mPC = 0;

    @Override
    public void setStatus(String status) {
        log.log(Level.INFO, "Status: " + status);
        System.out.println("Status: " + status);
    }

    @Override
    public void startTask(int size) {
        mTotal = size;
        mDone = 0;
        mPC = 0;
    }

    @Override
    public void workTask(int amnt) {
        int oldPC = mPC;
        mDone += amnt;
        mPC = mDone * 100 / mTotal;
        if (mPC != oldPC) {
            if (mPC % 10 == 0) {
                log.log(Level.INFO, mPC + "%");
                System.out.print(mPC + "%");
            } else {
                log.log(Level.INFO, ".");
                System.out.println(".");
            }
        }
    }

    @Override
    public void endTask() {
        log.log(Level.INFO, "");
        System.out.println();
    }

    @Override
    public boolean isPleaseCancel() {
        return false;
    }

    @Override
    public void setErrorTitle(String title) {
        log.log(Level.INFO, "Error: " + title);
        System.out.println("Error: " + title);
    }

    @Override
    public void setErrorDescription(String desc) {
        log.log(Level.INFO, desc);
        System.out.println(desc);
    }

    @Override
    public void setError(Throwable t) {
        log.log(Level.INFO, "",t);
        t.printStackTrace();
    }

}
