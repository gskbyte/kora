package org.gskbyte.kora.profiles;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.drawable.Drawable;


public class UserParser
{
    // names of the XML tags
    private static final String[] tags = {"user", "name", "school", "photo",
        "autoStart", "autoStartSeconds",
        "useProfile", "deviceProfile"};
    
    private static final int IUSER = 0,
        INAME = 1,
        ISCHOOL = 2,
        IPHOTO = 3,
        IAUTOSTART = 4,
        IAUTOSTARTSECONDS = 5,
        IUSEPROFILENAME = 6,
        IDEVICEPROFILENAME = 7;
    
    
    public static User parse(XmlPullParser parser, boolean defaultUser)
    {
        String[] xmlStrings = new String[tags.length];
        for(int i=0; i<tags.length; ++i)
            xmlStrings[i]="";
        int currentIndex = -1;
        boolean startOk = false, // variables usadas para comprobar la primera etiqueta
                endOk = true;
        
        try{
            // Leer cabecera
            int eventType = parser.getEventType();
            while(!startOk && eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG)
                    if( parser.getName().equalsIgnoreCase(tags[IUSER]) )
                        startOk=true;
                eventType = parser.next();
            }
            
            // Leer cuerpo y pie
            while(/*!endOk && */eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    currentIndex = -1;
                    for(int i=0; i<tags.length; ++i){
                        if(parser.getName().equalsIgnoreCase(tags[i])){
                            currentIndex = i;
                            break;
                        }
                    }
                } else if(eventType == XmlPullParser.TEXT){
                    if(currentIndex != -1)
                        xmlStrings[currentIndex] = parser.getText().trim();
                } else if(eventType == XmlPullParser.END_TAG){
                    if( parser.getName().equalsIgnoreCase(tags[IUSER]) ){
                        endOk=true;
                    }
                }
                eventType = parser.next();
            }
            if(startOk && endOk){
                boolean autoStart = Boolean.parseBoolean(xmlStrings[IAUTOSTART]);
                int seconds = 5;
                if(!xmlStrings[IAUTOSTARTSECONDS].equals(""))
                    seconds = Integer.parseInt(xmlStrings[IAUTOSTARTSECONDS]);
                
                return new User(xmlStrings[INAME], defaultUser, 
                        xmlStrings[ISCHOOL], xmlStrings[IPHOTO],
                        autoStart, seconds,
                        xmlStrings[IUSEPROFILENAME], xmlStrings[IDEVICEPROFILENAME]);
                }
            else
                return null;
            
        } catch (Exception e) {
            return null;
        }
    }

    public static User parse(String contents, boolean isDefault)
    {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
    
            xpp.setInput( new StringReader(contents) );
            
            return UserParser.parse(xpp, isDefault);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static User parseFile(String path, boolean isDefault)
    {
        return UserParser.parse("", isDefault);
    }
    
}
