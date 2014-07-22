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
package jo.sm.ui.act.plugin;

import java.awt.Component;
import java.beans.PropertyEditorSupport;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class FilePropertyEditor extends PropertyEditorSupport {

    private final FilePropertyInfo mInfo;
    private FilePropertyPanel mPanel;

    public FilePropertyEditor(FilePropertyInfo info) {
        super();
        mInfo = info;
    }

    public FilePropertyEditor(Object bean, FilePropertyInfo info) {
        super(bean);
        mInfo = info;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public Component getCustomEditor() {
        mPanel = new FilePropertyPanel(mInfo, this);
        return mPanel;
    }

    @Override
    public String getAsText() {
        return (String) getValue();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }
}
