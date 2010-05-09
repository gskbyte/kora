package org.gskbyte.kora.devices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.gskbyte.kora.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DeviceRepresentation
{
    protected static final String TAG = "DeviceRepresentation";
    
    public static final int ICON_DEFAULT = 0,
                            ICON_HIGH_CONTRAST = 1,
                            ICON_BLACK_WHITE = 2,
                            ICON_PHOTO = 3,
                            ICON_ANIMATION = 4;
    
    static final String[] ICON_TAGS = {
        "default","highContrast","blackWhite","photo","animation"
        };
    
    public static class State
    {
        String tag;
        String filename;
        Bitmap icon;
        
        public State(String tag, String filename){
            this.tag = tag;
            this.filename = filename;
        }
    }
    
    public static class Control
    {
        public static final int ACCESS_READ = 0x01,
                                ACCESS_WRITE = 0x02,
                                ACCESS_READ_WRITE = 0x03; //(= ACCESS_READ | ACCESS_WRITE)
        
        public String name;
        public int access;
    }
    
    public static class BinaryControl extends Control
    {
        public String minimumTag, maximumTag;
        
        public BinaryControl(String name, int access, String minimumTag, String maximumTag)
        {
            this.name = name;
            this.access = access;
            this.minimumTag = minimumTag;
            this.maximumTag = maximumTag;
        }
    }
    
    public static class ScalarControl extends Control
    {
        public String decreaseTag, increaseTag;
        
        public ScalarControl(String name, int access, String decreaseTag, String increaseTag)
        {
            this.name = name;
            this.access = access;
            this.decreaseTag = decreaseTag;
            this.increaseTag = increaseTag;
        }
    }
    
    protected static int sIconMode;
    protected static Vector<DeviceRepresentation> sRepresentations = new Vector<DeviceRepresentation>();
    protected static Resources sResources;
    protected static AssetManager sAssetManager;
    protected static int sMaxIconWidth = 0, sMaxIconHeight = 0;

    protected String mPath, mIconPath;
    protected String mName;
    protected Bitmap mIcon;
    protected Vector<State> mStates = new Vector<State>();
    protected HashMap<String, Control> mControls = new HashMap<String, Control>();
    
    public static void init(Resources res, AssetManager am)
    {
        sResources = res;
        sAssetManager = am;
        sIconMode = -1;
    }
    
    public static void setMaxIconSize(int width, int height)
    {
        sMaxIconWidth = width;
        sMaxIconHeight = height;
    }
    
    public static void setIconMode(int mode)
    {
        if(mode != sIconMode){
            sIconMode = mode;
            for(DeviceRepresentation dr : sRepresentations){
                dr.mIcon = null;
                dr.mIconPath = dr.mPath+"icons/"+ICON_TAGS[sIconMode]+"/";
                for(State s : dr.mStates){
                    // s.icon.recycle()?
                    s.icon = null;
                }
            }
        }
    }
    
    public DeviceRepresentation(java.io.InputStream stream, String path) throws Exception
    {
        // Parsear documento
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(stream);
        
        Element root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        
        mPath = path;
        mName = root.getAttribute("name");
        
        for(int i=0; i<nodes.getLength(); ++i){
            Node cur_node = nodes.item(i);
            if(cur_node.getNodeType() == Node.ELEMENT_NODE){
                if(cur_node.getNodeName().equals("states")){
                    NodeList states = cur_node.getChildNodes();
                    for(int j=0; j<states.getLength(); ++j){
                        Node state = states.item(j);
                        if(state.getNodeType() == Node.ELEMENT_NODE){
                            if(state.getNodeName().equals("state")){
                                NamedNodeMap attr = state.getAttributes();
                                //int index = Integer.parseInt( attr.getNamedItem("index").getNodeValue() );
                                String tag = attr.getNamedItem("tag").getNodeValue();
                                String filename = attr.getNamedItem("icon").getNodeValue();
                                
                                mStates.add(new State(tag, filename));
                            }
                        }
                    }
                } else if(cur_node.getNodeName().equals("controls")){
                    NodeList controls = cur_node.getChildNodes();
                    for(int j=0; j<controls.getLength(); ++j){
                        Node control = controls.item(j);
                        if(control.getNodeType() == Node.ELEMENT_NODE){
                            String nodeName = control.getNodeName();
                            NamedNodeMap attr = control.getAttributes();
                            Control dc = null;
                            String name = attr.getNamedItem("name").getNodeValue();
                            String accessStr = attr.getNamedItem("access").getNodeValue();
                            int accessMode = 0;
                            if(accessStr.contains("W"))
                                accessMode |= Control.ACCESS_WRITE;
                            if(accessStr.contains("R"))
                                accessMode |= Control.ACCESS_READ;
                            if(nodeName.equals("binaryControl")){
                                String minimumTag = attr.getNamedItem("minimumTag").getNodeValue(),
                                       maximumTag = attr.getNamedItem("maximumTag").getNodeValue();
                                
                                dc = new BinaryControl(name, accessMode, minimumTag, maximumTag);
                                
                            } else if(nodeName.equals("scalarControl")) {
                                String decreaseTag = attr.getNamedItem("decreaseTag").getNodeValue(),
                                       increaseTag = attr.getNamedItem("increaseTag").getNodeValue();

                                dc = new ScalarControl(name, accessMode, decreaseTag, increaseTag);
                            }
                            mControls.put(name, dc);
                        }
                    }
                }
            }
        }
        
        sRepresentations.add(this);
    }
    
    public String getName()
    {
        return mName;
    }
    
    public Bitmap getIcon()
    {
        if(mIcon == null){
            mIcon = loadIcon("device.png");
        }
        return mIcon;
    }
    
    public int getStateCount()
    {
        return mStates.size();
    }
    
    public String getStateTag(int index)
    {
        return mStates.get(index).tag;
    }
    
    public Bitmap getStateIcon(int index)
    {
        Bitmap icon = mStates.get(index).icon;
        if(icon == null){
            icon = loadIcon(mStates.get(index).filename);
        }
        return icon;
    }
    
    public int getControlsCount()
    {
    	return mControls.size();
    }
    
    public Set<String> getControlNames()
    {
    	return mControls.keySet();
    }
    
    public Control getControl(String name)
    {
    	return mControls.get(name);
    }
    
    protected Bitmap scaleIcon(Bitmap b)
    {
        if(sMaxIconWidth!=0 && sMaxIconHeight!=0){
            int iw = b.getWidth(),
                ih = b.getHeight();
            if(iw>sMaxIconWidth || ih>sMaxIconHeight){
                int rw = (sMaxIconWidth<<10) / iw,
                    rh = (sMaxIconHeight<<10) / ih;
                int res = Math.min(rw, rh);
                
                b = Bitmap.createScaledBitmap(b, (res*iw)>>10, (res*ih)>>10, true);
            }
        }
        return b;
    }
    
    protected Bitmap loadIcon(String filename)
    {
        Bitmap b = null;
        
        try {
            b = BitmapFactory.decodeStream(sAssetManager.open(mIconPath+filename));
        } catch (IOException e) {
            try { // intentar cargar el del estado por defecto
                String fname2 = mPath+"icons/"+ICON_TAGS[ICON_DEFAULT]+"/"+filename;
                b = BitmapFactory.decodeStream(sAssetManager.open(fname2));
            } catch (IOException e1) {
                Log.e(TAG, "Bad device representation: main default icon missing for "+mName);
                b = BitmapFactory.decodeResource(sResources, R.drawable.icon_device_error);
            }
        }
        
        b = scaleIcon(b);
        return b;
    }
}
