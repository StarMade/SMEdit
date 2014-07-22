package jo.util.lwjgl.win;

import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;

public class GroupDrawHandler extends NodeDrawHandler {

    @Override
    public void draw(long tick, JGLNode node) {
        preDraw(tick, node);
        JGLGroup obj = (JGLGroup) node;
        JGLNode[] children;
        synchronized (obj) {
            children = obj.getChildren().toArray(new JGLNode[0]);
        }
        for (JGLNode n : children) {
            DrawLogic.draw(tick, n);
        }
        postDraw(tick, node);
    }
}
