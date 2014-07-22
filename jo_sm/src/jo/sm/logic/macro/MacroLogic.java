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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jo.sm.logic.utils.FileUtils;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class MacroLogic {

    private static final ScriptEngineManager mScriptManager = new ScriptEngineManager();

    public static Object eval(File scriptFile, Map<String, Object> props) throws IOException {
        String script = FileUtils.readFileAsString(scriptFile.toString());
        // TODO: implement #include
        return eval(script, props);
    }

    public static Object eval(String script, Map<String, Object> props) {
        ScriptEngine engine = mScriptManager.getEngineByMimeType("text/javascript");
        script = MacroFunctionOpLogic.expandFunctions(script, props);
        ScriptContext context = MacroFunctionOpLogic.getInstanceProps();
        try {
            if (context == null) {
                context = makeContext(engine, props, null);
                MacroFunctionOpLogic.setInstanceProps(context);
                Object ret = engine.eval(script, context);
                MacroFunctionOpLogic.clearInstanceProps();
                return ret;
            } else {
                Map<String, String> nameMap = new HashMap<>();
                updateContext(context, engine, props, nameMap);
                Object ret = engine.eval(script, context);
                List<Integer> scopes = context.getScopes();
                if (scopes.size() > 0) {
                    Bindings b = context.getBindings(scopes.get(0));
                    for (String key : b.keySet()) {
                        if (key.startsWith("func_")) {
                            continue;
                        }
                        if (key.equals("print") || (key.equals("println"))) {
                            continue;
                        }
                        Object val = b.get(key);
                        if (nameMap.containsKey(key)) {
                            key = nameMap.get(key);
                        }
                        if (!props.containsKey(key)) {
                            props.put(key, val);
                        } else if (props.get(key) != val) {
                            props.put(key, val);
                        }
                    }
                }
                return ret;
            }
        } catch (ScriptException e) {
            throw new IllegalStateException("Script error executing '" + script + "'", e);
        }
    }

    private static void updateContext(ScriptContext context,
            ScriptEngine engine, Map<String, Object> props,
            Map<String, String> nameMap) {
        Bindings b = engine.createBindings();
        makeBindings(b, props, nameMap);
        int scope = context.getScopes().get(0);
        context.setBindings(b, scope);
        MacroFunctionOpLogic.makeJavascriptBindings(b, props);
    }

    private static ScriptContext makeContext(ScriptEngine engine,
            Map<String, Object> props, Map<String, String> nameMap) {
        ScriptContext context = engine.getContext();
        Bindings b = engine.createBindings();
        makeBindings(b, props, nameMap);
        int scope = context.getScopes().get(0);
        context.setBindings(b, scope);
        MacroFunctionOpLogic.makeJavascriptBindings(b, props);
        return context;
    }

    private static void makeBindings(Bindings b, Map<String, Object> props,
            Map<String, String> nameMap) {
        for (String k : props.keySet()) {
            Object v = props.get(k);
            String newk = k.replace('.', '_');
            b.put(newk, v);
            if (nameMap != null) {
                nameMap.put(newk, k);
            }
        }
    }
}
