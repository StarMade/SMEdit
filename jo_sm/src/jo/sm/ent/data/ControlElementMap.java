
package jo.sm.ent.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public class ControlElementMap {

    private byte mFactory;
    private List<ControlElement> mElements;

    public ControlElementMap() {
        mElements = new ArrayList<>();
    }

    public List<ControlElement> getElements() {
        return mElements;
    }

    public void setElements(List<ControlElement> elements) {
        mElements = elements;
    }

    public byte getFactory() {
        return mFactory;
    }

    public void setFactory(byte factory) {
        mFactory = factory;
    }
}
