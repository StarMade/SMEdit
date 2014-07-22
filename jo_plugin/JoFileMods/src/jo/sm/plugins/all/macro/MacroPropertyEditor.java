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
package jo.sm.plugins.all.macro;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.sm.factories.all.macro.MacroPlugin;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.macro.MacroFunctionOpLogic;
import jo.sm.mods.IBlocksPlugin;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroPropertyEditor extends PropertyEditorSupport {

    private Map<String, Object> mKeyToValue;
    private Map<Object, String> mValueToKey;
    private String[] mTags;

    public MacroPropertyEditor() {
        super();
        init();
    }

    public MacroPropertyEditor(Object bean) {
        super(bean);
        init();
    }

    private void init() {
        mKeyToValue = new HashMap<>();
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_FILE));
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_EDIT));
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_VIEW));
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_MODIFY));
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_GENERATE));
        addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_PAINT));
        mTags = mKeyToValue.keySet().toArray(new String[0]);
        mValueToKey = new HashMap<>();
        for (String key : mTags) {
            mValueToKey.put(mKeyToValue.get(key), key);
        }
    }

    private void addPlugins(List<IBlocksPlugin> blocksPlugins) {
        for (IBlocksPlugin plugin : blocksPlugins) {
            if (plugin instanceof MacroPlugin) {
                mKeyToValue.put(plugin.getName(), MacroFunctionOpLogic.getID(plugin));
            }
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
