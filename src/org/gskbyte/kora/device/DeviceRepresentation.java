package org.gskbyte.kora.device;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
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
    
    static AssetManager sAssetManager;
    
    protected String mName;
    
    protected Vector<Bitmap> mIcons = new Vector<Bitmap>();
    protected HashMap<String, DeviceControl> mControls = 
        new HashMap<String, DeviceControl>();
    
    public static void setAssetManager(AssetManager am)
    {
        sAssetManager = am;
    }
    
    public DeviceRepresentation(java.io.InputStream stream, String path) throws Exception
    {
    	// Alojar espacio para iconos
        mIcons.setSize(5);
        
        // Parsear documento
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
                    String iconClass = nm.getNamedItem("class").getNodeValue();
                    String iconPath = "icons/"+iconClass+"/device.png";
                    setIcon(iconClass, path+iconPath);
                } else if(cur_node.getNodeName().equals("control")){
                    String controlName = nm.getNamedItem("name").getNodeValue();
                    String typeStr = nm.getNamedItem("type").getNodeValue();
                    int controlType = DeviceControl.TYPE_SCALAR;
                    if(typeStr.equals("binary")) {
                    	controlType = DeviceControl.TYPE_BINARY;
                    } // else SCALAR, y otros que añada en un futuro
                    
                    DeviceControl dc = new DeviceControl(controlType, controlName);
                    NodeList dcIcons = cur_node.getChildNodes();
                    for(int j=0; j<dcIcons.getLength(); ++j){
                    	Node controlIconNode = dcIcons.item(j);
                    	if(controlIconNode.getNodeType() == Node.ELEMENT_NODE){
                    		NamedNodeMap iconAttr = controlIconNode.getAttributes();
                    		int index = Integer.parseInt( iconAttr.getNamedItem("index").getNodeValue() );
                    		String iconName = iconAttr.getNamedItem("file").getNodeValue();
                    		for(int k=0; k<ICON_TAGS.length; ++k){
                    			if(mIcons.get(k) != null){
                    				String iconPath = "icons/"+ICON_TAGS[k]+"/"+iconName;
                    				dc.setIcon(k, index, path+iconPath);
                    			}
                    		}
                    	}
                    }
                    mControls.put(controlName, dc);
                }
            }
        }
    }
    
    public String getName()
    {
        return mName;
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
    
    public int getNDeviceControls()
    {
    	return mControls.size();
    }
    
    public Set<String> getDeviceControlNames()
    {
    	return mControls.keySet();
    }
    
    public DeviceControl getDeviceControl(String name)
    {
    	return mControls.get(name);
    }
}
