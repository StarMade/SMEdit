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
package jo.sm.ui.act;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public abstract class GenericAction extends AbstractAction {

    public void setName(String name) {
        putValue(NAME, name);
    }

    public String getName() {
        return (String) getValue(NAME);
    }

    public void setToolTipText(String toolTipText) {
        putValue(SHORT_DESCRIPTION, toolTipText);
    }

    public String getToolTipText() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    public void setChecked(boolean checked) {
        putValue(SELECTED_KEY, checked ? "check" : null);
    }

    public boolean getChecked() {
        return (getValue(SELECTED_KEY) != null);
    }
}
