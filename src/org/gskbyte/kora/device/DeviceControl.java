package org.gskbyte.kora.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DeviceControl
{
	public static final int TYPE_BINARY = 0,
							TYPE_SCALAR = 1;
	
	protected int mType;
	protected String mName;
	protected Vector< Vector<String> > mIconPaths;
	protected Vector< Vector<Bitmap> > mIcons;
	
	public DeviceControl(int type, String name)
	{
		mIconPaths = new Vector< Vector<String> >();
		mIcons = new Vector< Vector<Bitmap> >();
		for(int i=0; i<5; ++i){
			mIconPaths.add(new Vector<String>());
			mIcons.add(new Vector<Bitmap>());
		}
		
		mType = type;
		mName = name;
	}
	
	public int getType()
	{
		return mType;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getNIcons(int iconMode)
	{
		return mIconPaths.get(iconMode).size();
	}
	
	/* Métodos delicados 
	 * Se carga solo la ruta del fichero para no petar la memoria.
	 * */
	public void setIcon(int iconMode, int index, String path)
	{
		// Comprobar que exista el fichero, y en tal caso, añadirlo
		try {
			InputStream in = DeviceRepresentation.sAssetManager.open(path);
			in.close();
			mIconPaths.get(iconMode).add(index, path);
		} catch (IOException e) {
			; // Simplemente no existe, no pasa NADA
			// No es necesario hacer close()
		}
	}
	
	/* Precarga todos los iconos para un modo concreto, evitando
	 * que el método getIcon tenga que cargarlos uno a uno cuando se llamen
	 */
	public void preloadIconsForMode(int iconMode)
	{
		// Liberar vector de iconos
		mIcons.clear();
		for(int i=0; i<5; ++i){
			mIcons.add(new Vector<Bitmap>());
		}
		
		// Y cargar los correspondientes
		int nicons = mIconPaths.get(iconMode).size();
		for(int i=0; i<nicons; ++i)
			getIcon(iconMode, i);
	}
	
	/*
	 * Devuelve el icono solicitado, para el modo indicado.
	 * Carga la imagen en memoria si es necesario.
	 * Los pasos que sigue son:
	 * 
	 * 1. Intentar recuperar el icono.
	 * 2. Si existe:
	 *        Devolverlo.
	 *    Si no existe:
	 *        3.1 Intentar cargarlo desde flash.
	 *        3.2 Si lo ha cargado:
	 *                Devolverlo.
	 *            Si no lo ha cargado:
	 *                Intentar devolver el correspondiente en la config. por defecto.
	 * */
	public Bitmap getIcon(int iconMode, int index)
	{
		Bitmap ret = null;

		// Intentar cargar el icono indicado
		int size = mIcons.get(iconMode).size();
		if(size>index)
			ret = mIcons.get(iconMode).get(index);
		
		if(ret == null){ // si no lo he cogido
			// Intentar cargarlo
			String path = null;
			int pathsSize = mIconPaths.get(iconMode).size();
			if(pathsSize>index)
				path = mIconPaths.get(iconMode).get(index);
			
			if(path != null) {
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					InputStream is = DeviceRepresentation.sAssetManager.open(path);
					Bitmap b = BitmapFactory.decodeStream(is, null, options);
					is.close();
					// Redimensionar el vector de iconos para añadirme, en caso de ser necesario
					if(size<=index)
						mIcons.get(iconMode).setSize(index+1);
					mIcons.get(iconMode).add(index, b);
					ret = b;
				}catch (IOException e) {
					ret = getIcon(DeviceRepresentation.ICON_DEFAULT, index);
				}
			} else { // Si no se puede cargar desde memoria, devolver el correspondiente por defecto
				ret = getIcon(DeviceRepresentation.ICON_DEFAULT, index);
			}
		}
		
		// Devolver lo que hayamos recuperado
		return ret;
	}
}
