package org.gskbyte.kora.device;

import java.io.InputStream;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DeviceControl
{
	public static final int TYPE_BINARY = 0,
							TYPE_SCALAR = 1;

	public final static class State
	{
		protected int mIconMode = -1;
		protected String mName;
		protected String mFileName;
		protected String mAbsoluteAction;
		protected Bitmap mIcon;
		
		public State(String fileName, String name, String absoluteAction)
        {
            mFileName = fileName;
            mName = name;
            mAbsoluteAction = absoluteAction;
        }
		
		protected Bitmap getIcon(int mode, String basePath, Bitmap parentIcon)
		{
			if(mIconMode != mode) {
				String modeFolder = DeviceRepresentation.ICON_TAGS[mode];
				String path = basePath+"icons/"+modeFolder+"/"+mFileName;
				
				// Intentar cargar el fichero, si no existe, cargar fichero por defecto
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
				try {
                    InputStream is = DeviceRepresentation.sAssetManager.open(path);
                    mIcon = BitmapFactory.decodeStream(is, null, options);
				} catch (Exception e1) {
					modeFolder = DeviceRepresentation.ICON_TAGS[0];
					path = basePath+"icons/"+modeFolder+"/"+mFileName;
					try {
	                    InputStream is = DeviceRepresentation.sAssetManager.open(path);
	                    mIcon = BitmapFactory.decodeStream(is, null, options);
					} catch (Exception e2) {
						// Cargar icono de dispositivo, a la desesperada
						mIcon = parentIcon;
					}
				}
			}
			
			mIconMode = mode;
			return mIcon;
		}
	}
	
	protected DeviceRepresentation mParent;
	protected int mType;
	protected String mName;
	protected Vector<State> mStates;
	
	public DeviceControl(DeviceRepresentation parent, int type, String name)
    {
		mParent = parent;
		mType = type;
		mName = name;
		mStates = new Vector<State>();
    }
	
    public int getType()
    {
        return mType;
    }
    
    public String getName()
    {
        return mName;
    }
    
    public int getStateCount()
    {
        return mStates.size();
    }
    
    public void addState(State s)
    {
    	mStates.add(s);
    }
    
    public void loadIconsForMode(int iconMode)
    {
    	String parentPath = mParent.getPath();
    	Bitmap parentIcon = mParent.getIcon(iconMode);
    	for(int i=0; i<mStates.size(); ++i)
    		mStates.get(i).getIcon(iconMode, parentPath, parentIcon);
    }
    
    public Bitmap getIcon(int iconMode, int index)
    {
    	String parentPath = mParent.getPath();
    	Bitmap parentIcon = mParent.getIcon(iconMode);
    	return mStates.get(index).getIcon(iconMode, parentPath, parentIcon);
    }
    
    public String getStateName(int index)
    {
    	return mStates.get(index).mName;
    }
    
    public String getStateAbsoluteAction(int index)
    {
        return mStates.get(index).mAbsoluteAction;
    }

    
}
