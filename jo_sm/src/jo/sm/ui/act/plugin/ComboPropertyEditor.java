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

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ComboPropertyEditor extends PropertyEditorSupport {

    private final Map<String, Object> mKeyToValue;
    private Map<Object, String> mValueToKey;
    private String[] mTags;

    public ComboPropertyEditor(Map<String, Object> keyToValue) {
        super();
        mKeyToValue = keyToValue;
        init();
    }

    public ComboPropertyEditor(Object bean, Map<String, Object> keyToValue) {
        super(bean);
        mKeyToValue = keyToValue;
        init();
    }

    private void init() {
        mTags = mKeyToValue.keySet().toArray(new String[0]);
        mValueToKey = new HashMap<>();
        for (String key : mTags) {
            mValueToKey.put(mKeyToValue.get(key), key);
        }
    }

    @Override
    public String getAsText() {
        return mValueToKey.get(getValue());
    }

    @Override
    public String[] getTags() {
        return mTags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(mKeyToValue.get(text));
    }
}
