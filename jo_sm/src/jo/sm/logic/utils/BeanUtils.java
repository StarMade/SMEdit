/**
 * Created on Aug 21, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of file
 * comments go to Window>Preferences>Java>Code Generation.
 */
package jo.sm.logic.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.sm.data.Bean;

public class BeanUtils {

    @SuppressWarnings("unchecked")
    public static void set(Object to, Object from) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        try {
            BeanInfo fromClassInfo = Introspector.getBeanInfo(fromClass);
            BeanInfo toClassInfo = Introspector.getBeanInfo(toClass);
            PropertyDescriptor[] fromProps = fromClassInfo.getPropertyDescriptors();
            PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
            for (PropertyDescriptor fromProp : fromProps) {
                for (PropertyDescriptor toProp : toProps) {
                    if (!fromProp.getName().equals(toProp.getName())) {
                        continue;
                    }
                    Class<?> fromType = fromProp.getPropertyType();
                    Class<?> toType = toProp.getPropertyType();
                    if ((fromType == null) || (toType == null)) {
                        continue;
                    }
                    if (!fromType.equals(toType)) {
                        continue;
                    }
                    Method read = fromProp.getReadMethod();
                    Method write = toProp.getWriteMethod();
                    if ((read != null) && (write != null)) {
                    //System.out.println("From: "+fromClass.getName()+", "+fromProps[i].getName()+", "+fromType.getName()+", "+read.getName());
                    //System.out.println("  To: "+toClass.getName()+", "+toProps[j].getName()+", "+toType.getName()+", "+write.getName());	                	
                        Object[] args = new Object[1];
                        args[0] = read.invoke(from, new Object[0]);
                    //System.out.println(" Val: "+args[0]);                        
                        if (args[0] instanceof Bean) {
                            try {
                                args[0] = ((Bean) args[0]).clone();
                            } catch (CloneNotSupportedException e) {
                            }
                        } else if (args[0] instanceof ArrayList) {
                            args[0] = cloneArray((List<Object>) args[0]);
                        }
                        write.invoke(to, args);
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e3) {
        }
    }

    public static boolean set(Object to, String key, Object value) {
        Class<?> toClass = to.getClass();
        try {
            BeanInfo toClassInfo = Introspector.getBeanInfo(toClass);
            PropertyDescriptor[] toProps = toClassInfo.getPropertyDescriptors();
            for (PropertyDescriptor toProp : toProps) {
                if (!toProp.getName().equalsIgnoreCase(key)) {
                    continue;
                }
                Class<?> toType = toProp.getPropertyType();
                if (toType == null) {
                    continue;
                }
                if ((value instanceof String) && !toType.getName().equals("java.lang.String")) {
                    String type = toType.getName();
                    if (type.equals("long")) {
                        value = new Long((String) value);
                    } else if (type.equals("int")) {
                        value = new Integer((String) value);
                    } else if (type.equals("double")) {
                        value = new Double((String) value);
                    } else if (type.equals("float")) {
                        value = new Float((String) value);
                    } else if (type.equals("boolean")) {
                        value = new Boolean((String) value);
                    } else {
                        System.err.println("BeanUtils.set, need to convert " + type);
                    }
                }
                Method write = toProp.getWriteMethod();
                if (write != null) {
                //System.out.println("From: "+fromClass.getName()+", "+fromProps[i].getName()+", "+fromType.getName()+", "+read.getName());
                //System.out.println("  To: "+toClass.getName()+", "+toProps[j].getName()+", "+toType.getName()+", "+write.getName());                      
                    Object[] args = new Object[1];
                    args[0] = value;
                    //System.out.println(" Val: "+args[0]);                        
                    write.invoke(to, args);
                    return true;
                }
            }
        } catch (IntrospectionException | IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e3) {
        }
        return false;
    }

    public static Object get(Object bean, String val) {
        int o = val.indexOf(".");
        if (o > 0) {
            Object primary = get(bean, val.substring(0, o));
            if (primary == null) {
                return null;
            }
            return get(primary, val.substring(o + 1));
        }
        if (bean instanceof Map) {
            return ((Map<?, ?>) bean).get(val);
        }
        Class<?> beanClass = bean.getClass();
        try {
            BeanInfo beanClassInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] beanProps = beanClassInfo.getPropertyDescriptors();
            for (int i = 0; i < beanProps.length; i++) {
                if (!beanProps[i].getName().equalsIgnoreCase(val)) {
                    continue;
                }
                Method read = beanProps[i].getReadMethod();
                if (read == null) {
                    return null;
                }
                return read.invoke(bean, new Object[0]);
            }
        } catch (IntrospectionException | IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e3) {
        }
        return null;
    }

    public static List<Object> cloneArray(List<Object> array) {
        List<Object> ret = new ArrayList<>();
        for (Iterator<?> i = array.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Bean) {
                try {
                    ret.add(((Bean) o).clone());
                } catch (CloneNotSupportedException e) {
                    ret.add(o);
                }
            } else {
                ret.add(o);
            }
        }
        return ret;
    }

    public static boolean equals(Object to, Object from) {
        if ((to == null) && (from == null)) {
            return true;
        }
        if ((to == null) || (from == null)) {
            return false;
        }
        if (to == from) {
            return true;
        }
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        Class<?> theClass;
        if (fromClass.isAssignableFrom(toClass)) {
            theClass = fromClass;
        } else if (toClass.isAssignableFrom(fromClass)) {
            theClass = fromClass;
        } else {
            return false;
        }
        try {
            BeanInfo classInfo = Introspector.getBeanInfo(theClass);
            PropertyDescriptor[] props = classInfo.getPropertyDescriptors();
            for (int i = 0; i < props.length; i++) {
                if ((to instanceof Bean) && props[i].getName().equalsIgnoreCase("Parent")) {
                    continue;
                }
                Method read = props[i].getReadMethod();
                Method write = props[i].getWriteMethod();
                if ((read != null) && (write != null)) {
                    Object v1 = read.invoke(from, new Object[0]);
                    Object v2 = read.invoke(to, new Object[0]);
                    if ((v1 == null) && (v2 != null)) {
                        return false;
                    }
                    if ((v1 == null) && (v2 == null)) {
                        return true;
                    }
                    if (!v1.equals(v2)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IntrospectionException | IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.getTargetException().printStackTrace();
        }
        return false;
    }

    public static String toXML(Bean b) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XMLEncoder enc = new XMLEncoder(baos)) {
            enc.setOwner(b);
            enc.writeObject(b);
        }
        try {
            return new String(baos.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static Bean fromXML(String xml, Object owner) {
        XMLDecoder dec;
        try {
            dec = new XMLDecoder(new ByteArrayInputStream(xml.getBytes("utf-8")));
            dec.setOwner(owner);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        Object ret = dec.readObject();
        dec.close();
        return (Bean) ret;
    }

    public static Object get(Map<String, Object> map, String ident) {
        int o = ident.indexOf(".");
        if (o < 0) {
            return map.get(ident);
        }
        Object b = map.get(ident.substring(0, o));
        if (b != null) {
            return get(b, ident.substring(o + 1));
        }
        return null;
    }

    public static boolean match(Bean b, String[] cols, String[] vals, boolean isOr, boolean fuzzy) {
        if (cols == null) {
            return true;
        }
        for (int i = 0; i < cols.length; i++) {
            Object v = get(b, cols[i]);
            boolean match = false;
            if (v != null) {
                if (fuzzy) {
                    match = v.toString().contains(vals[i]);
                } else {
                    match = v.toString().equals(vals[i]);
                }
            }
            if (match && isOr) {
                return true;
            }
            if (!match && !isOr) {
                return false;
            }
        }
        return !isOr;
    }

    public static List<String> getProperties(Bean bean) {
        Class<?> beanClass = bean.getClass();
        return getProperties(beanClass);
    }

    public static List<String> getProperties(Class<?> beanClass) {
        List<String> list = new ArrayList<>();
        try {
            BeanInfo beanClassInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] beanProps = beanClassInfo.getPropertyDescriptors();
            for (int i = 0; i < beanProps.length; i++) {
                Class<?> propType = beanProps[i].getPropertyType();
                if (propType == null) {
                    continue;
                }
                Method read = beanProps[i].getReadMethod();
                Method write = beanProps[i].getWriteMethod();
                if ((read != null) && (write != null)) {
                    list.add(beanProps[i].getName());
                    //System.out.println("Adding property '"+beanProps[i].getName()+" with read="+read.getName()+", write="+write.getName());
                }
            }
        } catch (IntrospectionException e1) {
            e1.printStackTrace();
        }
        return list;
    }

    public static Method getMethod(Class<?> clazz, String name) {
        try {
            Method m = clazz.getMethod(name);
            if (m != null) {
                return m;
            }
        } catch (NoSuchMethodException | SecurityException e) {
        }
        for (Method m : clazz.getMethods()) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}
