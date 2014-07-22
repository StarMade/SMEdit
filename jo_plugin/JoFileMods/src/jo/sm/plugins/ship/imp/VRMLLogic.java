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
package jo.sm.plugins.ship.imp;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class VRMLLogic {
    private static final Logger log = Logger.getLogger(VRMLLogic.class.getName());

    public static List<VRMLNode> read(Reader r) throws IOException {
        PushbackReader rdr;
        rdr = new PushbackReader(r);
        List<VRMLNode> nodes;
        nodes = new ArrayList<>();
        for (;;) {
            VRMLNode node = readNext(rdr);
            if (node == null) {
                break;
            }
            nodes.add(node);
        }
        return nodes;
    }

    private static VRMLNode readNext(PushbackReader rdr) throws IOException {
        int c;
        c = skipWhitespace(rdr);
        if (c == -1) {
            return null;
        }
        if (c == '}') {
            return new VRMLNode("}");
        }
        rdr.unread(c);
        String name = readValue(rdr, true);
        if (name.equals("DEF")) {
            name = readValue(rdr, true);
            readValue(rdr, true);
        }
        c = skipWhitespace(rdr);
        if (c == -1) {
            return null;
        }
        log.log(Level.INFO, "Reading: ", name);
        if (c == '{') {   // map node
            List<VRMLNode> value = new ArrayList<>();
            for (;;) {
                VRMLNode next = readNext(rdr);
                if (next == null) {
                    return null;
                }
                if (next.getName().equals("}")) {
                    break;
                }
                value.add(next);
            }
            return new VRMLNode(name, value);
        } else if (c == '[') {   // array node
            List<String> value;
            value = new ArrayList<>();
            for (;;) {
                String next = readValue(rdr, true);
                if (next == null) {
                    return null;
                }
                if (next.equals("]")) {
                    break;
                }
                value.add(next);
            }
            return new VRMLNode(name, value);
        } else {   // singleton
            rdr.unread(c);
            List<String> value = new ArrayList<>();
            for (;;) {
                String next;
                next = readValue(rdr, false);
                if (next == null) {
                    return null;
                }
                if (next.equals("\n")) {
                    break;
                }
                value.add(next);
            }
            return new VRMLNode(name, value);
        }
    }

    private static String readValue(PushbackReader rdr, boolean skipEOLN) throws IOException {
        StringBuilder value;
        value = new StringBuilder();
        int c;
        c = skipWhitespace(rdr, skipEOLN);
        if (c == -1) {
            return null;
        }
        if (c == ']') {
            return "]";
        }
        if (c == '\n') {
            return "\n";
        }
        boolean inQuote = false;
        if (c == '"') {
            inQuote = true;
        } else {
            value.append((char) c);
        }
        for (;;) {
            c = rdr.read();
            if (c == -1) {
                return null;
            }
            if (c == '\n') {
                rdr.unread(c);
                break;
            }
            if (inQuote) {
                if (c == '"') {
                    break;
                }
            } else {
                if (isWhitespace(c)) {
                    break;
                }
                if (c == ']') {
                    rdr.unread(c);
                    break;
                }
            }
            value.append((char) c);
        }
        return value.toString();
    }

    private static int skipWhitespace(PushbackReader rdr) throws IOException {
        return skipWhitespace(rdr, true);
    }

    private static int skipWhitespace(PushbackReader rdr, boolean includingEOLN) throws IOException {
        boolean skipComment;
        skipComment = false;
        for (;;) {
            int c = rdr.read();
            if (c == -1) {
                return -1;
            }
            if (skipComment) {
                if (c == '\n') {
                    if (!includingEOLN) {
                        return c;
                    }
                    skipComment = false;
                }
            } else {
                if (c == '#') {
                    skipComment = true;
                } else if (c == '\n') {
                    if (!includingEOLN) {
                        return c;
                    }
                } else if (!isWhitespace(c)) {
                    return c;
                }
            }
        }
    }

    private static boolean isWhitespace(int ch) {
        return ((ch == ',') || Character.isWhitespace(ch));
    }
}
