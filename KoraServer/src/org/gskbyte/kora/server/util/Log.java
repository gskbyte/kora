package org.gskbyte.kora.server.util;

public class Log
{
    public static final void log(String tag, String str)
    {
        System.out.println(tag + "\t" + str);
    }
    
    public static final void log(String str)
    {
        System.out.println(str);
    }
}
