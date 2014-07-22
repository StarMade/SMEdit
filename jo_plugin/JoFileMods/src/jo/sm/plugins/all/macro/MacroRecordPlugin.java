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

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.File;
import java.io.IOException;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.macro.MacroFunctionOpLogic;
import jo.sm.logic.utils.FileUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.BlocksPluginAction;
import jo.sm.ui.act.plugin.DescribedBeanInfo;
import jo.sm.ui.act.plugin.IPluginInvocationListener;
import jo.util.Paths;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroRecordPlugin implements IBlocksPlugin, IPluginInvocationListener {

    public static final String NAME_START = "Macro/Record...";
    public static final String NAME_STOP = "Macro/Stop Recording";
    public static final String DESC = "Record a macro to a file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS
            = {
                {TYPE_ALL, SUBTYPE_EDIT, 91},};

    private boolean mRecording;
    private MacroRecordParameters mParams;
    private StringBuffer mMacro;

    @Override
    public String getName() {
        if (mRecording) {
            return NAME_STOP;
        } else {
            return NAME_START;
        }
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getAuthor() {
        return AUTH;
    }

    @Override
    public Object newParameterBean() {
        if (mRecording) {
            return null;
        } else {
            return new MacroRecordParameters();
        }
    }

    @Override
    public void initParameterBean(SparseMatrix<Block> original, Object params,
            StarMade sm, IPluginCallback cb) {
    }

    @Override
    public int[][] getClassifications() {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb) {
        try {
            if (mRecording) {
                stopRecording(sm);
            } else {
                startRecording(sm, (MacroRecordParameters) p);
            }
            return null;
        } catch (Exception e) {
            cb.setError(e);
            return null;
        }
    }

    private void stopRecording(StarMade sm) throws IOException {
        mRecording = false;
        BlocksPluginAction.removePluginInvocationListener(this);
        if (mMacro.length() == 0) {
            sm.setStatusMessage("No commands issued to record.");
            return;
        }
        mMacro.append("grid\n"); // final return value
        File plugins = new File(Paths.getPluginsDirectory());
        File macroDir = new File(plugins, "macros");
        StringBuilder macroName = new StringBuilder();
        for (char c : mParams.getName().toCharArray()) {
            if (Character.isJavaIdentifierPart(c)) {
                macroName.append(c);
            }
        }
        macroName.append(".js");
        File macroFile = new File(macroDir, macroName.toString());
        if (!macroDir.exists()) {
            macroDir.mkdirs();
        }
        FileUtils.writeFile(mMacro.toString(), macroFile);
        sm.setStatusMessage("Finished recording macro " + mParams.getName());
    }

    private void startRecording(StarMade sm, MacroRecordParameters p) {
        mParams = p;
        mRecording = true;
        BlocksPluginAction.addPluginInvocationListener(this);
        sm.setStatusMessage("Recording macro");
        mMacro = new StringBuffer();
        mMacro.append("// Name: ").append(mParams.getName()).append("\n");
        mMacro.append("// Author: ").append(mParams.getAuthor()).append("\n");
        mMacro.append("// Classification: ").append(mParams.getEnablement()).append(" ").append(mParams.getPlacement()).append(" ").append(mParams.getPriority()).append("\n");
        mMacro.append("/* DescriptionStart\n");
        mMacro.append(mParams.getDescription());
        mMacro.append("\n DescriptionEnd */\n");
        mMacro.append("\n");
        sm.setStatusMessage("Recording macro " + mParams.getName());
    }

    @Override
    public void pluginInvoked(IBlocksPlugin plugin,
            SparseMatrix<Block> original, Object params, StarMade sm,
            IPluginCallback cb) {
        if (plugin == this) {
            return;
        }
        mMacro.append("grid = @");
        mMacro.append(MacroFunctionOpLogic.getID(plugin));
        mMacro.append("(grid");
        if (params != null) {
            DescribedBeanInfo info = new DescribedBeanInfo(params);
            for (PropertyDescriptor prop : info.getOrderedProps()) {
                mMacro.append(", ");
                PropertyEditor edit = info.getEditors().get(prop.getName());
                Object val = edit.getValue();
                if (val == null) {
                    mMacro.append("null");
                } else {
                    if (val instanceof String) {
                        mMacro.append("\"");
                        mMacro.append(((String) val).replace("\"", "\\\""));
                        mMacro.append("\"");
                    } else if ((val instanceof Integer) || (val instanceof Boolean)) {
                        mMacro.append(val.toString());
                    } else {
                        mMacro.append("\"");
                        String sval = edit.getAsText();
                        mMacro.append(sval);
                        mMacro.append("\"");
                    }
                }
            }
        }
        mMacro.append(");\n");
    }
}
