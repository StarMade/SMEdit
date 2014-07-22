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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author jgrant
 *
 */
public class XMLUtils {

    private static final Logger log = Logger.getLogger(XMLUtils.class.getPackage().getName());
    private static DocumentBuilderFactory mFactory = null;

    /**
     * @return A new document to populate
     */
    public static Document newDocument() {
        ensureFactory();
        try {
            DocumentBuilder builder = mFactory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    /**
     * @param is the input stream to read
     * @return the document contained in that input stream
     */
    public static Document readStream(InputStream is) {
        ensureFactory();
        try {
            DocumentBuilder builder = mFactory.newDocumentBuilder();
            return builder.parse(is);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param xmlFile is the file to read
     * @return the Document contained in that file
     */
    public static Document readFile(File xmlFile) {
        return readFile(xmlFile, false);
    }

    /**
     * @param xmlFile is the file to read
     * @param compress if true, the file is assumed to be compressed with GZIP
     * @return the Document contained in that file
     */
    public static Document readFile(File xmlFile, boolean compress) {
        ensureFactory();
        try {
            DocumentBuilder builder = mFactory.newDocumentBuilder();
            //builder.setEntityResolver(new EntityUtils());
            Document ret = null;
            if (xmlFile.exists() && xmlFile.canRead()) {
                InputStream is = new FileInputStream(xmlFile);

                if (compress) {
                    is = new GZIPInputStream(is);
                }
                ret = builder.parse(is);
                is.close();
            } else {
                log.log(Level.FINER, "XML file=''{0}'' does not exist or can not be read", xmlFile.getName());
            }
            return ret;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.log(Level.SEVERE, "Cannot read file '" + xmlFile + ", compress=" + compress, e);
            return null;
        }
    }

    /**
     * @param xmlURL is a URL to an XML document
     * @return the Document contained in the content at tha tURL
     */
    public static Document readURL(String xmlURL) {
        ensureFactory();
        try {
            URL u = new URL(xmlURL);
            DocumentBuilder builder = mFactory.newDocumentBuilder();
            return builder.parse(u.openStream());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.log(Level.WARNING, "readURL failed!", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param xml is XML for a document
     * @return the Document contained in that XML
     */
    public static Document readString(String xml) {
        ensureFactory();
        try {
            DocumentBuilder builder = mFactory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            if (e instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) e;
                int l = spe.getLineNumber();
                int c = spe.getColumnNumber();
                StringTokenizer st = new StringTokenizer(xml, "\r\n");
                String line = "";
                for (int i = 0; (i < l) && st.hasMoreTokens(); i++) {
                    line = st.nextToken();
                }
                log.log(Level.INFO, "Line=" + l + ", column=" + c);
                log.log(Level.INFO, line);
                for (int i = 0; i < c - 1; i++) {
                    log.log(Level.INFO, "-");
                }
                log.log(Level.INFO, "^");
            }
            log.log(Level.INFO, "",e);
            e.printStackTrace();
            return null;
        }
    }

    private static synchronized void ensureFactory() {
        if (mFactory == null) {
            mFactory = DocumentBuilderFactory.newInstance();
            //mFactory.setIgnoringComments(true);
            mFactory.setValidating(false);
            mFactory.setCoalescing(true);
            mFactory.setExpandEntityReferences(false);
        }
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @param key an attribute value to test on qualifying nodes
     * @param value the value expected in that attribute
     * @return the list of nodes matching the criteria
     */
    public static List<Node> findNodes(Node root, String path, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return findNodes(root, path, map);
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @param key an attribute value to test on qualifying nodes
     * @param value the value expected in that attribute
     * @return the first node matching the criteria
     */
    public static Node findFirstNode(Node root, String path, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return findFirstNode(root, path, map);
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @param attributeMap a key-value map of attribute-value pairs to test for
     * inclusion
     * @return the list of nodes matching the criteria
     */
    public static List<Node> findNodes(Node root, String path, Map<String, String> attributeMap) {
        List<Node> nodes = findNodes(root, path);
        for (Iterator<Node> i = nodes.iterator(); i.hasNext();) {
            if (!attributeMatch(i.next(), attributeMap)) {
                i.remove();
            }
        }
        return nodes;
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @param attributeMap a key-value map of attribute-value pairs to test for
     * inclusion
     * @return the first node matching the criteria
     */
    public static Node findFirstNode(Node root, String path, Map<String, String> attributeMap) {
        Node node = findFirstNode(root, path);
        if ((node != null) && !attributeMatch(node, attributeMap)) {
            node = null;
        }
        return node;
    }

    /**
     * @param n a Node to examine attributes on
     * @param attributeMap a key-value map of attribute-value pairs to test
     * @return true if all keys are present and contain the mapped values
     */
    public static boolean attributeMatch(Node n, Map<String, String> attributeMap) {
        for (String key : attributeMap.keySet()) {
            String val = (String) attributeMap.get(key);
            if (!val.equals(XMLUtils.getAttribute(n, key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @return the list of nodes matching the criteria
     */
    public static List<Node> findNodes(Node root, String path) {
        List<Node> ret = new ArrayList<>();
        if (path != null) {
            String[] paths = ArrayUtils.toStringArray(new StringTokenizer(path, "/,"));
            findNodes(ret, root, paths, 0);
        }
        return ret;
    }

    /**
     * @param root the root node to search underneath (non-inclusive)
     * @param path a slash delimited list of hierarchal node names
     * @return the first node matching the criteria
     */
    public static Node findFirstNode(Node root, String path) {
        List<Node> list = findNodes(root, path);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param found a list of Nodes to add found nodes to
     * @param root the node from which to search (non-inclusive)
     * @param path a list of node names to match
     * @param o where in the list we are
     */
    public static void findNodes(List<Node> found, Node root, String[] path, int o) {
        for (Node n = root.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeName().startsWith("#")) {
                continue;
            }
            if (path[o].equals("*")) {
                if (o + 1 == path.length) {
                    found.add(n);
                } else {
                    if (n.getNodeName().equals(path[o + 1])) {
                        if (o + 2 == path.length) {
                            found.add(n);
                        } else {
                            findNodes(found, n, path, o + 2);
                        }
                    } else {
                        findNodes(found, n, path, o);
                    }
                }
            } else {
                String nn = n.getNodeName();
                int off = nn.indexOf(':');
                if (off >= 0) {
                    nn = nn.substring(off + 1);
                }
                if (nn.equals(path[o])) {
                    if (o + 1 == path.length) {
                        found.add(n);
                    } else {
                        findNodes(found, n, path, o + 1);
                    }
                }
            }
        }
    }

    /**
     * @param n a root node to search beneath
     * @param name a node name to look for
     * @return the first instance of that node name in the tree beneath the root
     * (depth first)
     */
    public static Node findFirstNodeRecursive(Node n, String name) {
        List<Node> nodes = new ArrayList<>();
        findRecursive(n, name, nodes, true);
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.get(0);
    }

    /**
     * @param n a root node to search beneath
     * @param name a node name to look for
     * @return all instances of that node name in the tree beneath the root
     */
    public static List<Node> findAllNodesRecursive(Node n, String name) {
        List<Node> nodes = new ArrayList<>();
        findRecursive(n, name, nodes, false);
        return nodes;
    }

    private static void findRecursive(Node parent, String name, List<Node> nodes, boolean onlyOne) {
        String nn = parent.getNodeName();
        int off = nn.indexOf(':');
        if (off >= 0) {
            nn = nn.substring(off + 1);
        }
        if (nn.equals(name)) {
            nodes.add(parent);
            if (onlyOne) {
                return;
            }
        }
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            findRecursive(child, name, nodes, onlyOne);
            if (onlyOne && (nodes.size() > 0)) {
                return;
            }
        }
    }

    /**
     * @param n Node to examine
     * @param attr Attribute to look for
     * @return true if the Node contains the named Attribute
     */
    public static boolean hasAttribute(Node n, String attr) {
        NamedNodeMap attrs = n.getAttributes();
        if (attrs == null) {
            return false;
        }
        Node ret = attrs.getNamedItem(attr);
        return ret != null;
    }

    /**
     * @param n Node to examine
     * @param attr Attribute to look for
     * @param def Default value to return if attribute is not present
     * @return if the Node contains the named Attribute, the value, if not, the
     * def parameter
     */
    public static String getAttribute(Node n, String attr, String def) {
        NamedNodeMap attrs = n.getAttributes();
        if (attrs == null) {
            return def;
        }
        Node ret = attrs.getNamedItem(attr);
        if (ret == null) {
            return def;
        } else {
            return ret.getNodeValue();
        }
    }

    /**
     * @param n Node to examine
     * @param attr Attribute to look for
     * @return if the Node contains the named Attribute, the value, if not,
     * empty string
     */
    public static String getAttribute(Node n, String attr) {
        return getAttribute(n, attr, "");
    }

    /**
     * @param n the node to look for text on
     * @return The conjoined values of all #text nodes immediately under this
     * node
     */
    public static String getText(Node n) {
        StringBuilder sb = new StringBuilder();
        for (Node ele = n.getFirstChild(); ele != null; ele = ele.getNextSibling()) {
            String name = ele.getNodeName();
            if (name.equalsIgnoreCase("#text")) {
                sb.append(ele.getNodeValue());
            }
        }
        return sb.toString().trim();
    }

    /**
     * @param n the Node to look for a text node under
     * @param nodeName the name of the text node to look for
     * @return finds the first node named nodeName and returns any text under it
     */
    public static String getTextTag(Node n, String nodeName) {
        Node textNode = findFirstNode(n, nodeName);
        if (textNode == null) {
            return "";
        } else {
            return getText(textNode);
        }
    }

    /**
     * @param n Node to look under
     * @return true if the given node contains any #text children
     */
    public static boolean isTextNode(Node n) {
        for (Node ele = n.getFirstChild(); ele != null; ele = ele.getNextSibling()) {
            String name = ele.getNodeName();
            if (!name.equalsIgnoreCase("#text")) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param xml some raw XML
     * @return formatted an indented XML text
     */
    public static String format(String xml) {
        return format(xml, false);
    }

    /**
     * @param xml some raw XML
     * @param latin1 true if latin1 entities are to be encoded
     * @return formatted an indented XML text
     */
    public static String format(String xml, boolean latin1) {
        Document doc = readString(xml);
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"");
        //sb.append(doc.getXmlVersion());
        sb.append("1.0");
        sb.append("\" encoding=\"");
        //sb.append(doc.getXmlEncoding());
        sb.append("UTF-8");
        sb.append("\"?>\n");
        format(doc.getLastChild(), sb, "", latin1);
        return sb.toString();
    }

    private static void format(Node n, StringBuffer out, String indent, boolean latin1) {
        if ("#text".equals(n.getNodeName())) {
            String txt = n.getNodeValue().trim();
            if (txt.length() > 0) {
                out.append(indent);
                out.append(n.getNodeValue());
                out.append("\n");
            }
            return;
        }
        if ("#comment".equals(n.getNodeName())) {
            out.append("<!--");
            out.append(n.getNodeValue());
            out.append("-->\n");
            return;
        }
        out.append(indent);
        out.append("<");
        out.append(n.getNodeName());
        NamedNodeMap map = n.getAttributes();
        if (map != null) {
            if (map.getLength() == 1) {
                Node ele = map.item(0);
                String v = ele.getNodeValue();
                if (v != null) {
                    out.append(" ");
                    out.append(ele.getNodeName());
                    out.append("=\"");
                    out.append(EntityUtils.insertEntities(v, latin1));
                    out.append("\"");
                }
            } else {
                for (int i = 0; i < map.getLength(); i++) {
                    Node ele = map.item(i);
                    out.append("\n");
                    out.append(indent);
                    out.append("    ");
                    out.append(ele.getNodeName());
                    out.append("=\"");
                    out.append(EntityUtils.insertEntities(ele.getNodeValue(), latin1));
                    out.append("\"");
                }
            }
        }
        if (isTextNode(n)) {
            String txt = getText(n);
            if (txt.length() == 0) {
                out.append("/>\n");
            } else {
                out.append(">");
                out.append(EntityUtils.insertEntities(txt, latin1));
                out.append("</");
                out.append(n.getNodeName());
                out.append(">\n");
            }
        } else {
            out.append(">\n");
            for (Node ele = n.getFirstChild(); ele != null; ele = ele.getNextSibling()) {
                format(ele, out, indent + "  ", latin1);
            }
            out.append(indent);
            out.append("</");
            out.append(n.getNodeName());
            out.append(">\n");
        }
    }

    /**
     * @param node root of tree to write
     * @param f file to write to
     * @return true if successful
     */
    public static boolean writeFile(Node node, File f) {
        return writeFile(node, f, false);
    }

    /**
     * @param node root of tree to write
     * @param f file to write to
     * @param latin1 true if latin1 entities are to be encoded
     * @return true if successful
     */
    public static boolean writeFile(Node node, File f, boolean latin1) {
        return writeFile(node, f, latin1, false);
    }

    /**
     * @param node root of tree to write
     * @param f file to write to
     * @param latin1 true if latin1 entities are to be encoded
     * @param compress true if GZIP compression to be used on file
     * @return true if successful
     */
    public static boolean writeFile(Node node, File f, boolean latin1, boolean compress) {
        if (node instanceof Document) {
            node = node.getFirstChild();
        }
        try {
            StringBuffer sb = new StringBuffer();
            format(node, sb, "", latin1);
            OutputStream os = new FileOutputStream(f);
            if (compress) {
                os = new GZIPOutputStream(os);
            }
            try (OutputStreamWriter fw = new OutputStreamWriter(os)) {
                fw.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param node Node to start from
     * @return the XML contents in that node in a string
     */
    public static String writeString(Node node) {
        return writeString(node, false);
    }

    /**
     * @param node Node to start from
     * @param latin1 true if latin1 entities are to be encoded
     * @return the XML contents in that node in a string
     */
    public static String writeString(Node node, boolean latin1) {
        StringBuffer sb = new StringBuffer();
        format(node, sb, "", latin1);
        return sb.toString();
    }

    /**
     * @param n1 first Node to test
     * @param n2 second Node to test
     * @return true if a deep compare show the same children and attributes in
     * the same order
     */
    public static boolean equals(Node n1, Node n2) {
        // compare type
        if (!n1.getNodeName().equals(n2.getNodeName())) {
            return false;
        }
        // compare attributes
        NamedNodeMap nnm1 = n1.getAttributes();
        NamedNodeMap nnm2 = n2.getAttributes();
        if (nnm1.getLength() != nnm2.getLength()) {
            return false;
        }
        for (int i = 0; i < nnm1.getLength(); i++) {
            Node attr1 = nnm1.item(i);
            if (!getAttribute(n1, attr1.getNodeName()).equals(getAttribute(n2, attr1.getNodeName()))) {
                return false;
            }
        }
        // compare children
        Node c1 = n1.getFirstChild();
        Node c2 = n2.getFirstChild();
        for (;;) {
            while ((c1 != null) && c1.getNodeName().startsWith("#")) {
                c1 = c1.getNextSibling();
            }
            while ((c2 != null) && c2.getNodeName().startsWith("#")) {
                c2 = c2.getNextSibling();
            }
            if ((c1 == null) && (c2 == null)) {
                break;
            }
            if ((c1 == null) || (c2 == null)) {
                return false;
            }
            if (!equals(c1, c2)) {
                return false;
            }
            c1 = c1.getNextSibling();
            c2 = c2.getNextSibling();
        }
        return true;
    }
}
