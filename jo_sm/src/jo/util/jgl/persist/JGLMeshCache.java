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
package jo.util.jgl.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jo.sm.logic.utils.ArrayUtils;

public class JGLMeshCache {

    private static final Map<Integer, JGLMesh> mIDToMesh = new HashMap<>();
    private static final Set<Integer> mMRUMesh = new HashSet<>();

    public static void markMesh() {
        synchronized (mIDToMesh) {
            mMRUMesh.clear();
        }
    }

    public static void useMesh(int meshID) {
        synchronized (mIDToMesh) {
            if (meshID == 0) {
                return;
            }
            mMRUMesh.add(meshID);
        }
    }

    public static void useMesh(JGLMesh mesh) {
        useMesh(mesh.getMeshID());
    }

    public static int[] getUsedMeshes() {
        synchronized (mIDToMesh) {
            return ArrayUtils.toIntArray(mMRUMesh);
        }
    }

    public static int[] getUnUsedMeshes() {
        Set<Integer> unused = new HashSet<>();
        synchronized (mIDToMesh) {
            unused.addAll(mIDToMesh.keySet());
            unused.removeAll(mMRUMesh);
        }
        return ArrayUtils.toIntArray(unused);
    }

    public static JGLMesh getMesh(int meshID) {
        return mIDToMesh.get(meshID);
    }

    public static void registerMesh(int id, JGLMesh mesh) {
        synchronized (mIDToMesh) {
            mMRUMesh.add(id);
            mIDToMesh.put(id, mesh);
        }
    }

    public static void unregisterMesh(int id) {
        synchronized (mIDToMesh) {
            mMRUMesh.remove(id);
            mIDToMesh.remove(id);
        }
    }

    public static void unregisterMesh(JGLMesh mesh) {
        unregisterMesh(mesh.getMeshID());
    }
}
