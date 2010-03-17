package org.gskbyte.kora.util;

import java.lang.reflect.Field;
import java.util.Vector;

public class ResourceFinder
{
    public static Integer findAttr(String name)
    {
        return ResourceFinder.find(name, "attr");
    }
    public static Vector<Integer> findContainsAttr(String name)
    {
        return ResourceFinder.findContains(name, "attr");
    }
    public static Vector<Integer> listAttrs()
    {
        return ResourceFinder.list("attr");
    }
    
    public static Integer findDrawable(String name)
    {
        return ResourceFinder.find(name, "drawable");
    }
    public static Vector<Integer> findContainsDrawable(String name)
    {
        return ResourceFinder.findContains(name, "drawable");
    }
    public static Vector<Integer> listDrawables()
    {
        return ResourceFinder.list("drawable");
    }
    
    public static Integer findId(String name)
    {
        return ResourceFinder.find(name, "id");
    }
    public static Vector<Integer> findContainsId(String name)
    {
        return ResourceFinder.findContains(name, "id");
    }
    public static Vector<Integer> listIds()
    {
        return ResourceFinder.list("id");
    }
    
    public static Integer findLayout(String name)
    {
        return ResourceFinder.find(name, "layout");
    }
    public static Vector<Integer> findContainsLayout(String name)
    {
        return ResourceFinder.findContains(name, "layout");
    }
    public static Vector<Integer> listLayouts()
    {
        return ResourceFinder.list("layout");
    }
    
    public static Integer findString(String name)
    {
        return ResourceFinder.find(name, "string");
    }
    public static Vector<Integer> findContainsString(String name)
    {
        return ResourceFinder.findContains(name, "string");
    }
    public static Vector<Integer> listStrings()
    {
        return ResourceFinder.list("string");
    }
    
    public static Integer findXml(String name)
    {
        return ResourceFinder.find(name, "xml");
    }
    public static Vector<Integer> findContainsXml(String name)
    {
        return ResourceFinder.findContains(name, "xml");
    }
    public static Vector<Integer> listXmls()
    {
        return ResourceFinder.list("xml");
    }
    
    protected static Integer find(String name, String section)
    {
        Vector<Integer> integers = new Vector<Integer>();
        try {
            Class c = Class.forName("org.gskbyte.kora.R$"+section);
            Field[] fields = c.getDeclaredFields(); 
            for(Field f : fields){
                String fname = f.getName();
                Object fp = f.get(c);
                if(fname.equals(name) && fp instanceof Integer)
                    return (Integer) fp;
            }
            
        } catch (Exception e) {
            return 0;
        }
        
        return 0;
    }
    
    protected static Vector<Integer> findContains(String name, String section)
    {
        Vector<Integer> integers = new Vector<Integer>();
        try {
            Class c = Class.forName("org.gskbyte.kora.R$"+section);
            Field[] fields = c.getDeclaredFields(); 
            for(Field f : fields){
                String fname = f.getName();
                Object fp = f.get(c);
                if(fname.contains(name) && fp instanceof Integer)
                    integers.add((Integer) fp);
            }
            
        } catch (Exception e) {
            return integers;
        }
        
        return integers;
    }
    
    protected static Vector<Integer> list(String section)
    {
        Vector<Integer> integers = new Vector<Integer>();
        try {
            Class c = Class.forName("org.gskbyte.kora.R$"+section);
            Field[] fields = c.getDeclaredFields(); 
            for(Field f : fields){
                String fname = f.getName();
                Object fp = f.get(c);
                integers.add((Integer) fp);
            }
            
        } catch (Exception e) {
            return integers;
        }
        
        return integers;
    }
}
