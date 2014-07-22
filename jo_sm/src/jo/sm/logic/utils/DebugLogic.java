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
package jo.sm.logic.utils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class DebugLogic {

    public static boolean DEBUG = true;

    private static String mIndent = "";

    public static final boolean HULL_ONLY = false;
    private static final Logger log = Logger.getLogger(DebugLogic.class.getName());

    public static void setIndent(String indent) {
        mIndent = indent;
    }

    public static void indent() {
        mIndent += "  ";
    }

    public static void outdent() {
        mIndent = mIndent.substring(2);
    }

    public static void debug(String msg) {
        if (DEBUG) {
            log.log(Level.INFO, mIndent + msg);
            System.out.println(mIndent + msg);
        }
    }

}
