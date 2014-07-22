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
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.ConvertLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ship.data.Block;
import jo.sm.ui.DlgError;
import jo.sm.ui.RenderPanel;
import jo.sm.ui.act.GenericAction;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
@SuppressWarnings("serial")
public class BlocksPluginAction extends GenericAction {

    private final IBlocksPlugin mPlugin;
    private final RenderPanel mPanel;

    public BlocksPluginAction(RenderPanel panel, IBlocksPlugin plugin) {
        mPanel = panel;
        mPlugin = plugin;
        String name = mPlugin.getName();
        int o = name.indexOf('/');
        if (o >= 0) {
            name = name.substring(o + 1);
        }
        setName(name);
        setToolTipText(mPlugin.getDescription());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            final Object params = mPlugin.newParameterBean();
            loadProps(params);
            mPlugin.initParameterBean(StarMadeLogic.getModel(), params, StarMadeLogic.getInstance(), null);
            if (!getParams(params)) {
                return;
            }
            saveProps(params);
            IRunnableWithProgress t;
            t = new IRunnableWithProgress() {
                @Override
                public void run(IPluginCallback cb) {
                    try {
                        SparseMatrix<Block> original = StarMadeLogic.getModel();
                        mPanel.getUndoer().checkpoint(original);
                        StarMade sm = StarMadeLogic.getInstance();
                        pluginInvoked(mPlugin, original, params, sm, cb);
                        SparseMatrix<Block> modified = mPlugin.modify(original, params, sm, cb);
                        if (!cb.isPleaseCancel()) {
                            if (modified != null) {
                                StarMadeLogic.setModel(modified);
                            } else {
                                mPanel.updateTiles();
                            }
                        }
                        if (((((PluginProgressDlg) cb).getErrorTitle() != null))
                                || (((PluginProgressDlg) cb).getErrorDescription() != null)) {
                            ((PluginProgressDlg) cb).setPleaseCancel(true); // stop display if not yet auto-displayed
                            StringBuilder description = new StringBuilder();
                            if (((PluginProgressDlg) cb).getErrorDescription() != null) {
                                description.append("<hr/>").append(((PluginProgressDlg) cb).getErrorDescription());
                            }
                            description.append(composeDescription(params));
                            DlgError.showError(getFrame(), ((PluginProgressDlg) cb).getErrorTitle(),
                                    description.toString(), ((PluginProgressDlg) cb).getError());
                        } else {
                        }
                    } catch (Throwable t) {
                        DlgError.showError(getFrame(), "Error executing plugin",
                                composeDescription(params), t);
                    }
                }
            };
            RunnableLogic.run(getFrame(), mPlugin.getName(), t);
        } catch (Throwable t) {
            DlgError.showError(getFrame(), "Error launching plugin",
                    composeDescription(null), t);
        }
    }

    private String composeDescription(Object params) {
        StringBuilder html = new StringBuilder();
        html.append("Plugin: <b>").append(mPlugin.getName()).append("</b><br/>");
        if (params == null) {
            return html.toString();
        }
        html.append("Arguments:<br/>");
        html.append("<ul>");
        try {
            BeanInfo info = Introspector.getBeanInfo(params.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if ((pd.getReadMethod() == null) || (pd.getWriteMethod() == null)) {
                    continue;
                }
                try {
                    Object val = pd.getReadMethod().invoke(params);
                    if (val != null) {
                        html.append("<li>");
                        html.append(pd.getName());
                        html.append(" := <i>");
                        html.append(val.toString());
                        html.append("</i></li>");
                    } else {
                        html.append("<li>");
                        html.append(pd.getName());
                        html.append(" := null</li>");
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        html.append("</ul>");
        return html.toString();
    }

    private void saveProps(Object params) {
        if (params == null) {
            return;
        }
        Properties props = StarMadeLogic.getProps();
        String prefix = params.getClass().getName() + "$";
        try {
            BeanInfo info = Introspector.getBeanInfo(params.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if ((pd.getReadMethod() == null) || (pd.getWriteMethod() == null)) {
                    continue;
                }
                try {
                    Object val = pd.getReadMethod().invoke(params);
                    if (val != null) {
                        props.setProperty(prefix + pd.getName(), val.toString());
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        StarMadeLogic.saveProps();
    }

    private void loadProps(Object params) {
        if (params == null) {
            return;
        }
        Properties props = StarMadeLogic.getProps();
        String prefix = params.getClass().getName() + "$";
        try {
            BeanInfo info = Introspector.getBeanInfo(params.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if ((pd.getReadMethod() == null) || (pd.getWriteMethod() == null)) {
                    continue;
                }
                if (!props.containsKey(prefix + pd.getName())) {
                    continue;
                }
                String val = props.getProperty(prefix + pd.getName());
                try {
                    pd.getWriteMethod().invoke(params, ConvertLogic.toObject(val, pd.getPropertyType()));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    private boolean getParams(Object params) {
        if (params == null) {
            return true;
        }
        BeanEditDlg dlg = new BeanEditDlg(getFrame(), params);
        dlg.setVisible(true);
        params = dlg.getBean();
        return params != null;
    }

    private JFrame getFrame() {
        for (Component c = mPanel; c != null; c = c.getParent()) {
            if (c instanceof JFrame) {
                return (JFrame) c;
            }
        }
        return null;
    }

    private static final List<IPluginInvocationListener> mListeners = new ArrayList<>();

    public static void addPluginInvocationListener(IPluginInvocationListener listener) {
        mListeners.add(listener);
    }

    public static void removePluginInvocationListener(IPluginInvocationListener listener) {
        mListeners.remove(listener);
    }

    private void pluginInvoked(IBlocksPlugin plugin, SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb) {
        for (IPluginInvocationListener listener : mListeners.toArray(new IPluginInvocationListener[0])) {
            listener.pluginInvoked(plugin, original, params, sm, cb);
        }
    }
}
