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
package jo.sm.ent.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import jo.sm.ent.data.Tag;
import jo.sm.ent.data.TagType;


public class EntityUtils {
    private static final Logger log = Logger.getLogger(EntityUtils.class.getName());

    public static void dump(Tag obj, String prefix) {
        log.log(Level.INFO, prefix);
        log.log(Level.INFO, "<" + obj.getType());
        if (obj.getName() != null) {
            log.log(Level.INFO, " name=\"" + obj.getName() + "\"");
        }
        log.log(Level.INFO, ">");
        switch (obj.getType()) {
            case STRUCT: {
                Tag[] val = (Tag[]) obj.getValue();
            for (Tag val1 : val) {
                if (val1.getType() == TagType.FINISH) {
                    break;
                } else {
                    dump(val1, prefix + "  ");
                }
            }
            log.log(Level.INFO, prefix);
                break;
            }
            case LIST: {
                Tag[] val = (Tag[]) obj.getValue();
            for (Tag val1 : val) {
                if (val1.getType() == TagType.FINISH) {
                    break;
                } else {
                    dump(val1, prefix + "  ");
                }
            }
            log.log(Level.INFO, prefix);
                break;
            }
            default:
                if (obj.getValue() == null) {
                    log.log(Level.INFO, "<null>");
                } else {
                    log.log(Level.INFO, obj.getValue().toString());
                }
        }
        log.log(Level.INFO, "</" + obj.getType() + ">");
    }

    public static Tag lookup(Tag obj, String id) {
        if ((id == null) || (id.length() == 0)) {
            return obj;
        }
        String[] ids = id.split("/");
        for (String i : ids) {
            obj = find(obj, i);
        }
        return obj;
    }

    private static Tag find(Tag obj, String id) {
        if (obj.getName().equals(id)) {
            return obj;
        }
        if (obj.getType() == TagType.STRUCT) {
            for (Tag sub : (Tag[]) obj.getValue()) {
                if (id.equals(sub.getName())) {
                    return sub;
                }
            }
        }
        if (obj.getType() == TagType.LIST) {
            int n = Integer.parseInt(id);
            Tag[] subs = (Tag[]) obj.getValue();
            if (n < subs.length) {
                return subs[n];
            }
        }
        return null;
    }

    public static void decr(Tag obj, String val) {
        switch (obj.getType()) {
            case INT: {
                Integer v = (Integer) obj.getValue();
                v -= Integer.parseInt(val);
                obj.setValue(v);
                break;
            }
            default:
                throw new IllegalArgumentException("set is not supported on " + obj.getType());
        }
    }

    public static void incr(Tag obj, String val) {
        switch (obj.getType()) {
            case INT: {
                Integer v = (Integer) obj.getValue();
                v += Integer.parseInt(val);
                obj.setValue(v);
                break;
            }
            default:
                throw new IllegalArgumentException("set is not supported on " + obj.getType());
        }
    }

    public static void set(Tag obj, String val) {
        switch (obj.getType()) {
            case INT: {
                obj.setValue(new Integer(val));
                break;
            }
            default:
                throw new IllegalArgumentException("set is not supported on " + obj.getType());
        }
    }

}
