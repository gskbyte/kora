package org.gskbyte.kora.device;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DeviceRepresentation
{
    public static final int ICON_DEFAULT = 0,
                            ICON_HIGH_CONTRAST = 1,
                            ICON_BLACK_WHITE = 2,
                            ICON_PHOTO = 3,
                            ICON_ANIMATION = 4;
    
    protected static final String[] ICON_TAGS = {
        "default","highContrast","blackWhite","photo","animation"
        };
    
    protected static AssetManager sAssetManager;
    
    protected String mSystemName;
    protected String mName;
    
    protected Vector<Bitmap> mIcons = new Vector<Bitmap>();
    protected HashMap<String, AbstractDeviceControl> mControls = 
        new HashMap<String, AbstractDeviceControl>();
    
    public static void setAssetManager(AssetManager am)
    {
        sAssetManager = am;
    }
    
    public DeviceRepresentation(java.io.InputStream stream, String path) throws Exception
    {
        mIcons.setSize(5);
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(stream);
        
        Element root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        
        mName = root.getAttribute("name");
        
        for(int i=0; i<nodes.getLength(); ++i){
            Node cur_node = nodes.item(i);
            if(cur_node.getNodeType() == Node.ELEMENT_NODE){
                NamedNodeMap nm = cur_node.getAttributes();
                if(cur_node.getNodeName().equals("icon")){
                    String iconName = nm.getNamedItem("name").getNodeValue();
                    String iconPath = nm.getNamedItem("path").getNodeValue();
                    setIcon(iconName, path+iconPath);
                } else if(cur_node.getNodeName().equals("control")){
                    /* AÑADIR CONTROLES! */
                }
            }
        }
    }
    
    public String getSystemName()
    {
        return mSystemName;
    }
    
    public Bitmap getIcon(int which)
    {
        if(which<mIcons.size()){
            Bitmap r = mIcons.get(which);
            if(r != null)
                return r;
        }
        
        return mIcons.get(ICON_DEFAULT);
    }
    
    protected void setIcon(String name, String path) throws IOException
    {
        Bitmap b = BitmapFactory.decodeStream(sAssetManager.open(path));
        
        for(int i =0; i< ICON_TAGS.length; ++i){
            if(name.equals(ICON_TAGS[i])){
                mIcons.set(i, b);
                break;
            }
        }
    }
}
