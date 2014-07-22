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
package jo.sm.logic.utils;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class XMLEditUtils {
    private static final Logger log = Logger.getLogger(XMLEditUtils.class.getName());

    public static Node addTextTag(Node parent, String name, String value) {
        Node node = parent.getOwnerDocument().createElement(name);
        parent.appendChild(node);
        Text text = parent.getOwnerDocument().createTextNode(value);
        node.appendChild(text);
        return node;
    }

    public static Node addText(Node parent, String value) {
        Text text = parent.getOwnerDocument().createTextNode(value);
        parent.appendChild(text);
        return text;
    }

    public static Element addElement(Node parent, String name) {
        Element node;
        if (parent.getOwnerDocument() != null) {
            node = parent.getOwnerDocument().createElement(name);
        } else if (parent instanceof Document) {
            node = ((Document) parent).createElement(name);
        } else {
            return null;
        }
        parent.appendChild(node);
        return node;
    }

    public static Attr addAttribute(Node parent, String name, String value) {
        if (value == null) {
            return null;
        }
        Attr node = parent.getOwnerDocument().createAttribute(name);
        try {
            node.setValue(value);
            parent.getAttributes().setNamedItem(node);
        } catch (DOMException e) {
            log.log(Level.WARNING, "Problem rewriting " + parent.getNodeName() + "." + name + "='" + node.getValue() + "' -> '" + value + "'");
            System.out.println("Problem rewriting " + parent.getNodeName() + "." + name + "='" + node.getValue() + "' -> '" + value + "'");
        }
        return node;
    }

    public static void removeAttribute(Node parent, String name, String value, boolean recursive) {
        NamedNodeMap nnm = parent.getAttributes();
        if (nnm != null) {
            if (value == null) {
                nnm.removeNamedItem(name);
            } else {
                Node attr = nnm.getNamedItem(name);
                if (attr != null) {
                    String attrVal = attr.getNodeValue();
                    if (value.equals(attrVal)) {
                        nnm.removeNamedItem(name);
                    }
                }
            }
        }
        if (recursive) {
            for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
                removeAttribute(child, name, value, recursive);
            }
        }
    }

    public static Node duplicate(Node node, Document ownerDocument) {
        if (node instanceof Text) {
            return ownerDocument.createTextNode(node.getNodeValue());
        }
        if (node instanceof Comment) {
            return ownerDocument.createComment(node.getNodeValue());
        }
        Node newNode = ownerDocument.createElement(node.getNodeName());
        NamedNodeMap attribs = node.getAttributes();
        for (int i = 0; i < attribs.getLength(); i++) {
            Node attrib = attribs.item(i);
            addAttribute(newNode, attrib.getNodeName(), attrib.getNodeValue());
        }
        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
            Node newN = duplicate(n, ownerDocument);
            newNode.appendChild(newN);
        }
        return newNode;
    }

    public static void join(Node target, Node source) {
        for (Node s = source.getFirstChild(); s != null; s = s.getNextSibling()) {
            if (s.getNodeName().startsWith("#")) {
                continue;
            }
            Node t = findIdentical(target, s);
            if (t == null) {
                t = duplicate(s, target.getOwnerDocument());
                target.appendChild(t);
            } else {
                join(t, s);
            }
        }
    }

    private static Node findIdentical(Node target, Node s) {
        NamedNodeMap sAttrs = s.getAttributes();
        String sText = XMLUtils.getText(s).trim();
        for (Node t = target.getFirstChild(); t != null; t = t.getNextSibling()) {
            if (!t.getNodeName().equals(s.getNodeName())) {
                continue;
            }
            NamedNodeMap tAttrs = t.getAttributes();
            if (sAttrs.getLength() != tAttrs.getLength()) {
                continue;
            }
            boolean allMatch = true;
            for (int i = 0; i < sAttrs.getLength(); i++) {
                Node attr = sAttrs.item(i);
                String key = attr.getNodeName();
                String val = attr.getNodeValue();
                if (!val.equals(XMLUtils.getAttribute(t, key))) {
                    allMatch = false;
                    break;
                }
            }
            if (!allMatch) {
                continue;
            }
            String tText = XMLUtils.getText(t).trim();
            if (!sText.equals(tText)) {
                continue;
            }
            return t;
        }
        return null;
    }

    public static void merge(Node target, Node source) {
        for (Node s = source.getFirstChild(); s != null; s = s.getNextSibling()) {
            if (s.getNodeName().startsWith("#")) {
                continue;
            }
            String id = XMLUtils.getAttribute(s, "id");
            // find target
            Node t;
            for (t = target.getFirstChild(); t != null; t = t.getNextSibling()) {
                if (t.getNodeName().equals(s.getNodeName())) {
                    if (StringUtils.isTrivial(id) || id.equals(XMLUtils.getAttribute(t, "id"))) {
                        break;
                    }
                }
            }
            if (t != null) {
                merge(t, s);
            } else {
                t = duplicate(s, target.getOwnerDocument());
                target.appendChild(t);
            }
        }
    }

    public static void removeAllChildren(Node node) {
        while (node.getFirstChild() != null) {
            node.removeChild(node.getFirstChild());
        }
    }

    public static void setInnerXML(Node node, String xml) {
        removeAllChildren(node);
        Document doc = XMLUtils.readString("<html>" + xml + "</html>"); // must give root element
        if ((doc != null) && (doc.getFirstChild() != null)) {
            for (Node n = doc.getFirstChild().getFirstChild(); n != null; n = n.getNextSibling()) {
                try {
                    Node dup = duplicate(n, node.getOwnerDocument());
                    node.appendChild(dup);
                } catch (DOMException e) {
                    log.log(Level.WARNING, "Problem adding duplicate for '" + XMLUtils.writeString(n) + "'");
                    log.log(Level.WARNING, "Full text: '" + xml + "'");
                    System.out.println("Problem adding duplicate for '" + XMLUtils.writeString(n) + "'");
                    System.out.println("Full text: '" + xml + "'");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyAttributes(Node from, Node to) {
        NamedNodeMap map = from.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Node attr = map.item(i);
            addAttribute(to, attr.getNodeName(), attr.getNodeValue());
        }
    }

    public static void merge(File file1, String point1, File file2, String point2, File file3) {
        Document doc1 = XMLUtils.readFile(file1);
        Document doc2 = XMLUtils.readFile(file2);
        List<Node> points1 = XMLUtils.findNodes(doc1, point1);
        List<Node> points2 = XMLUtils.findNodes(doc2, point2);
        for (int i = 0; i < Math.min(points1.size(), points2.size()); i++) {
            Node node1 = points1.get(i);
            Node node2 = points2.get(i);
            Node dup1 = duplicate(node1, node2.getOwnerDocument());
            node2.getParentNode().replaceChild(dup1, node2);
        }
        XMLUtils.writeFile(doc2.getFirstChild(), file3);
    }

    public static void replaceText(Node node, String text) {
        for (;;) {
            Node n = node.getFirstChild();
            if (n == null) {
                break;
            }
            node.removeChild(n);
        }
        Text t = node.getOwnerDocument().createTextNode(text);
        node.appendChild(t);
    }

    public static Node addElementAndAttributes(Node parent, String name, String[] atts) {
        Node n = addElement(parent, name);
        for (int i = 0; i < atts.length; i += 2) {
            addAttribute(n, atts[i], atts[i + 1]);
        }
        return n;
    }

    public static void addAtEndOfGroup(Node parent, Node newChild) {
        addAtEndOfGroup(parent, newChild.getNodeName(), newChild);
    }

    public static void addAtEndOfGroup(Node parent, String childType, Node newChild) {
        List<Node> peers = XMLUtils.findNodes(parent, childType);
        if (peers.isEmpty()) {
            parent.appendChild(newChild);
        } else {
            Node lastPeer = peers.get(peers.size() - 1);
            if (lastPeer.getNextSibling() == null) {
                parent.appendChild(newChild);
            } else {
                parent.insertBefore(newChild, lastPeer.getNextSibling());
            }
        }

    }
}
