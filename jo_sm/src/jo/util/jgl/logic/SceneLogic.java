/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.util.jgl.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;
import jo.vecmath.Point3f;

public class SceneLogic {

    public static Iterator<JGLNode> sceneIterator(JGLNode root) {
        List<JGLNode> items;
        items = new ArrayList<>();
        addItems(items, root, false);
        return items.iterator();
    }

    public static Iterator<JGLObj> objIterator(JGLNode root) {
        List<JGLObj> items;
        items = new ArrayList<>();
        addItems(items, root, true);
        return items.iterator();
    }

    @SuppressWarnings("unchecked")
    private static void addItems(@SuppressWarnings("rawtypes") List items, JGLNode root, boolean objOnly) {
        if (root instanceof JGLGroup) {
            if (!objOnly) {
                items.add(root);
            }
            for (JGLNode child : ((JGLGroup) root).getChildren()) {
                addItems(items, child, objOnly);
            }
        } else {
            items.add(root);
        }
    }

    public static JGLNode find(JGLNode root, String id) {
        if (id.equals(root.getID())) {
            return root;
        }
        if (root instanceof JGLGroup) {
            for (JGLNode n : ((JGLGroup) root).getChildren()) {
                JGLNode found = find(n, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public static List<JGLObj> intersect(JGLNode root, float x, float y) {
        List<JGLObj> hits;
        hits = new ArrayList<>();
        for (Iterator<JGLObj> i = objIterator(root); i.hasNext();) {
            JGLObj o = i.next();
            if (isIntersect(o, x, y)) {
                hits.add(o);
            }
        }
        Collections.sort(hits, new Comparator<JGLObj>() {
            @Override
            public int compare(JGLObj lhs, JGLObj rhs) {
                float lhsz;
                lhsz = (lhs.getScreenLowBounds().z + lhs.getScreenHighBounds().z) / 2f;
                float rhsz;
                rhsz = (rhs.getScreenLowBounds().z + rhs.getScreenHighBounds().z) / 2f;
                return (int) Math.signum(lhsz - rhsz);
            }
        });
        return hits;
    }

    private static boolean isIntersect(JGLObj obj, float x, float y) {
        Point3f lowBounds = obj.getScreenLowBounds();
        Point3f highBounds = obj.getScreenHighBounds();
        if ((lowBounds == null) || (highBounds == null)) {
            return false;
        }
        float lowX;
        lowX = Math.min(lowBounds.x, highBounds.x);
        float highX;
        highX = Math.max(lowBounds.x, highBounds.x);
        float lowY;
        lowY = Math.min(lowBounds.y, highBounds.y);
        float highY;
        highY = Math.max(lowBounds.y, highBounds.y);
        return (lowX <= x) && (x <= highX) && (lowY <= y) && (y <= highY);
    }
}
