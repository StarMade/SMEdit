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
package jo.sm.logic.macro;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.MapUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IStarMadePlugin;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.DescribedBeanInfo;


public class MacroFunctionOpLogic {

    private static Map<String, IStarMadePlugin> FUNCTIONS = null;
    private static Map<String, JavascriptWrapper> JAVASCRIPT_FUNCTIONS = null;
    private static Map<Thread, Stack<ScriptContext>> mInstanceProps = new HashMap<>();
    private static final Logger log = Logger.getLogger(MacroFunctionOpLogic.class.getName());

    private static synchronized void init() {
        if (FUNCTIONS != null) {
            return;
        }
        FUNCTIONS = new HashMap<>();
        register(IBlocksPlugin.SUBTYPE_FILE);
        register(IBlocksPlugin.SUBTYPE_EDIT);
        register(IBlocksPlugin.SUBTYPE_GENERATE);
        register(IBlocksPlugin.SUBTYPE_MODIFY);
        register(IBlocksPlugin.SUBTYPE_PAINT);
        register(IBlocksPlugin.SUBTYPE_VIEW);
    }

    public static String getID(IStarMadePlugin plugin) {
        int[] classification = plugin.getClassifications()[0];
        return getPrefix(classification[1]) + getBaseID(plugin);
    }

    private static String getPrefix(int subtype) {
        switch (subtype) {
            case IBlocksPlugin.SUBTYPE_FILE:
                return "file";
            case IBlocksPlugin.SUBTYPE_EDIT:
                return "edit";
            case IBlocksPlugin.SUBTYPE_GENERATE:
                return "gen";
            case IBlocksPlugin.SUBTYPE_MODIFY:
                return "mod";
            case IBlocksPlugin.SUBTYPE_PAINT:
                return "paint";
            case IBlocksPlugin.SUBTYPE_VIEW:
                return "view";
        }
        throw new IllegalStateException("Unknown subtype=" + subtype);
    }

    private static void register(int subtype) {
        String prefix = getPrefix(subtype);
        List<IBlocksPlugin> plugins = StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, subtype);
        for (IBlocksPlugin plugin : plugins) {
            String name = prefix + getBaseID(plugin);
            log.log(Level.INFO, "Registering '"+name+"'");
            FUNCTIONS.put(name.toLowerCase(), plugin);
        }
    }

    private static String getBaseID(IStarMadePlugin plugin) {
        String name = plugin.getName();
        name = StringUtils.substitute(name, "/", "");
        name = StringUtils.substitute(name, ".", "");
        name = StringUtils.substitute(name, " ", "");
        return name;
    }

    public static String expandFunctions(String expr,
            Map<String, Object> props) {
        init();
        for (int start = expr.indexOf('@'); start >= 0; start = expr.indexOf('@', start + 1)) {
            int end = start + 1;
            while ((end < expr.length()) && Character.isJavaIdentifierPart(expr.charAt(end))) {
                end++;
            }
            String func = expr.substring(start + 1, end).toLowerCase();
            if (FUNCTIONS.containsKey(func)) {
                expr = expr.substring(0, start) + "func_" + func.toLowerCase() + ".run" + expr.substring(end);
            }
        }
        return expr;
    }

    public static ScriptContext getInstanceProps() {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if ((stack != null) && (stack.size() > 0)) {
            return stack.get(stack.size() - 1);
        } else {
            return null;//throw new IllegalStateException("Tried to get instance props and none set!");
        }
    }

    public static void setInstanceProps(ScriptContext props) {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if (stack == null) {
            stack = new Stack<>();
            mInstanceProps.put(Thread.currentThread(), stack);
        }
        stack.push(props);
    }

    public static void clearInstanceProps() {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if (stack != null) {
            stack.pop();
            if (stack.size() == 0) {
                mInstanceProps.remove(Thread.currentThread());
            }
        }
    }

    public static void makeJavascriptBindings(Bindings b,
            Map<String, Object> props) {
        init();
        if (JAVASCRIPT_FUNCTIONS == null) {
            JAVASCRIPT_FUNCTIONS = new HashMap<>();
            for (String name : FUNCTIONS.keySet()) {
                JavascriptWrapper wrapper = new JavascriptWrapper(name);
                JAVASCRIPT_FUNCTIONS.put("func_" + name, wrapper);
            }
        }
        MapUtils.copy(b, JAVASCRIPT_FUNCTIONS);
    }

    static Object evaluateFunction(String name, Object[] args, Map<String, Object> props) {
        init();
        IStarMadePlugin func = FUNCTIONS.get(name.toLowerCase());
        if (func instanceof IBlocksPlugin) {
            return evaluateBlocksPlugin((IBlocksPlugin) func, props, args);
        }
        throw new IllegalStateException("Unknown function " + name);
    }

    static Object evaluateBlocksPlugin(IBlocksPlugin plugin, Map<String, Object> props, Object[] args) {
        @SuppressWarnings("unchecked")
        SparseMatrix<Block> grid = (SparseMatrix<Block>) find(SparseMatrix.class, "grid", props, args);
        if (grid == null) {
            throw new IllegalArgumentException("Cannot find grid argument");
        }
        StarMade sm = (StarMade) find(StarMade.class, "sm", props, args);
        if (sm == null) {
            sm = StarMadeLogic.getInstance();
        }
        IPluginCallback cb = (IPluginCallback) find(IPluginCallback.class, "cb", props, args);
        if (cb == null) {
            cb = new NullPluginCallback();
        }
        Object params = plugin.newParameterBean();
        if (params != null) {
            plugin.initParameterBean(grid, params, sm, cb);
        }
        DescribedBeanInfo info = new DescribedBeanInfo(params);
        int off = 0;
        if ((args.length > 0) && (args[0] instanceof SparseMatrix)) {
            off++;
        }
        for (int i = 0; i < info.getOrderedProps().size(); i++) {
            PropertyDescriptor prop = info.getOrderedProps().get(i);
            Object value = find(prop, i + off, args);
            if (value instanceof String) {
                info.setAsText(prop.getName(), (String) value);
            } else if (value != null) {
                info.setAsValue(prop.getName(), value);
            }
        }
        return plugin.modify(grid, params, sm, cb);
    }

    private static Object find(PropertyDescriptor prop, int i, Object[] args) {
        Class<?> propType = prop.getPropertyType();
        propType = promoteType(propType);
        if (i < args.length) {
            Class<?> argType = args[i].getClass();
            if ((args[i] instanceof String) || propType.isAssignableFrom(argType)) {
                return args[i];
            } else if (Number.class.isAssignableFrom(propType) && Number.class.isAssignableFrom(argType)) {
                Number arg = (Number) args[i];
                if (propType == Double.class) {
                    return arg.doubleValue();
                } else if (propType == Float.class) {
                    return arg.floatValue();
                } else if (propType == Long.class) {
                    return arg.longValue();
                } else if (propType == Integer.class) {
                    return arg.intValue();
                } else if (propType == Short.class) {
                    return arg.shortValue();
                } else if (propType == Byte.class) {
                    return arg.byteValue();
                }
            }
        }
        // TODO: search for javascript struct
        return null;
    }

    private static Class<?> promoteType(Class<?> type) {
        if (type == boolean.class) {
            return Boolean.class;
        }
        if (type == int.class) {
            return Integer.class;
        }
        if (type == short.class) {
            return Short.class;
        }
        if (type == byte.class) {
            return Byte.class;
        }
        if (type == long.class) {
            return Long.class;
        }
        if (type == double.class) {
            return Double.class;
        }
        if (type == float.class) {
            return Float.class;
        }
        if (type == char.class) {
            return Character.class;
        }
        return type;
    }

    private static Object find(Class<?> theClass,
            String theName, Map<String, Object> props, Object[] args) {
        if (args != null) {
            for (Object o : args) {
                if (theClass.isInstance(o)) {
                    return o;
                }
            }
        }
        if (props != null) {
            if (props.containsKey(theName)) {
                return props.get(theName);
            }
            for (String key : props.keySet()) {
                if (key.equalsIgnoreCase(theName)) {
                    return props.get(key);
                }
            }
        }
        return null;
    }
}
