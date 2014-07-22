package jo.sm.ent.cmd;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import jo.sm.data.StarMade;

public class TestBeans {

    public static void main(String[] argv) {
        try {
            Object bean = new StarMade();
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            if (info == null) {
                System.out.println("No bean info!");
                return;
            }
            System.out.println("BeanInfo = " + info.getClass().getSimpleName());
            if (info.getIcon(BeanInfo.ICON_COLOR_32x32) != null) {
                System.out.println("32x32 color icon present");
            } else if (info.getIcon(BeanInfo.ICON_COLOR_16x16) != null) {
                System.out.println("16x16 color icon present");
            } else if (info.getIcon(BeanInfo.ICON_MONO_32x32) != null) {
                System.out.println("32x32 B&W icon present");
            } else if (info.getIcon(BeanInfo.ICON_MONO_16x16) != null) {
                System.out.println("16x16 B&W icon present");
            } else {
                System.out.println("No icon");
            }
            BeanDescriptor desc = info.getBeanDescriptor();
            if (desc == null) {
                System.out.println("No bean description!");
                return;
            }
            System.out.println("BeanDesc = " + desc.getClass().getSimpleName());
            System.out.println("Name = " + desc.getName() + " / " + desc.getDisplayName() + " / " + desc.getShortDescription());
            System.out.println("Class = " + desc.getBeanClass());
            System.out.println("Customizer = " + desc.getCustomizerClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
