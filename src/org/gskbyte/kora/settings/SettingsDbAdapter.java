package org.gskbyte.kora.settings;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingsDbAdapter
{
    public static final String DATABASE_NAME = "kora";
    public static final int DATABASE_VERSION = 3;
    
    public static final String TABLE_USEPROFILE = "use_profile",
         USEPROFILE_NAME = "name",
         USEPROFILE_ISDEFAULT = "isDefault",
         USEPROFILE_MAININTERACTION = "mainInteraction",
         USEPROFILE_TOUCHMODE = "touchMode",
         USEPROFILE_SCANMILLIS = "scanMillis",
         USEPROFILE_VOICEINTERACTION = "voiceInteraction",
         
         USEPROFILE_VIEWMODE = "viewMode",
         USEPROFILE_BACKGROUNDCOLOR = "backgroundColor",
         USEPROFILE_ROWS = "rows",
         USEPROFILE_COLUMNS = "columns",
         USEPROFILE_ORIENTATIONS = "orientation",
         USEPROFILE_SHOWTEXT = "showText",
         USEPROFILE_FONTSIZE = "fontSize",
         USEPROFILE_TYPOGRAPHY = "typography",
         USEPROFILE_TEXTCOLOR = "textColor",
         USEPROFILE_ICONMODE = "iconMode",
         USEPROFILE_PAGINATIONMODE = "paginationMode",
         
         USEPROFILE_VIBRATION = "vibration",
         USEPROFILE_CONFIRMATION = "confirmation",
         USEPROFILE_CONFIRMATIONMILLIS = "confirmationMillis",
         USEPROFILE_CONTENTHIGHLIGHT = "contentHighlight",
         USEPROFILE_BORDERHIGHLIGHT = "borderHighlight",

         USEPROFILE_SOUNDMODE = "soundMode",
         USEPROFILE_SOUNDONSELECTION = "soundOnSelection",
         USEPROFILE_SOUNDONACTION = "soundOnAction",
         USEPROFILE_VOICEMODE = "voiceMode";
        
    public static final String TABLE_DEVICEPROFILE = "device_profile",
        DEVICEPROFILE_NAME = "name",
        DEVICEPROFILE_ISDEFAULT = "isDefault";

    
    public static final String TABLE_USER = "user",
         USER_NAME = "name",
         USER_ISDEFAULT = "isDefault",
         USER_SCHOOL = "school",
         USER_PHOTO = "photo",
         USER_AUTOSTART = "autostart",
         USER_AUTOSTARTSECONDS = "autostartSeconds",
         USER_USEPROFILE = "useProfile",
         USER_DEVICEPROFILE = "deviceProfile";
    
    private static final String TABLE_CREATE_USEPROFILE =
        "create table "+TABLE_USEPROFILE+"\n"+
        "(\n"+
            USEPROFILE_NAME+" TEXT PRIMARY KEY,\n"+
            USEPROFILE_ISDEFAULT+" INTEGER,\n"+
            USEPROFILE_MAININTERACTION+" INTEGER,\n"+
            USEPROFILE_TOUCHMODE+" INTEGER,\n"+
            USEPROFILE_SCANMILLIS+" INTEGER,\n"+
            USEPROFILE_VOICEINTERACTION+" INTEGER,\n"+
            USEPROFILE_VIEWMODE+" INTEGER,\n"+
            USEPROFILE_BACKGROUNDCOLOR+" INTEGER,\n"+
            USEPROFILE_ROWS+" INTEGER,\n"+
            USEPROFILE_COLUMNS+" INTEGER,\n"+
            USEPROFILE_ORIENTATIONS+" INTEGER,\n"+
            USEPROFILE_SHOWTEXT+" INTEGER,\n"+
            USEPROFILE_FONTSIZE+" INTEGER,\n"+
            USEPROFILE_TYPOGRAPHY+" INTEGER,\n"+
            USEPROFILE_TEXTCOLOR+" INTEGER,\n"+
            USEPROFILE_ICONMODE+" INTEGER,\n"+
            USEPROFILE_PAGINATIONMODE+" INTEGER,\n"+
            USEPROFILE_VIBRATION+" INTEGER,\n"+
            USEPROFILE_CONFIRMATION+" INTEGER,\n"+
            USEPROFILE_CONFIRMATIONMILLIS+" INTEGER,\n"+
            USEPROFILE_CONTENTHIGHLIGHT+" INTEGER,\n"+
            USEPROFILE_BORDERHIGHLIGHT+" INTEGER,\n"+
            USEPROFILE_SOUNDMODE+" INTEGER,\n"+
            USEPROFILE_SOUNDONSELECTION+" INTEGER,\n"+
            USEPROFILE_SOUNDONACTION+" INTEGER,\n"+
            USEPROFILE_VOICEMODE+" INTEGER\n"+
        ");";
    
    private static final String TABLE_CREATE_DEVICEPROFILE =
        "create table "+TABLE_DEVICEPROFILE+"\n"+
        "(\n"+
            DEVICEPROFILE_NAME+" TEXT PRIMARY KEY,\n"+
            DEVICEPROFILE_ISDEFAULT+" INTEGER"+
            
        ");";
    
    private static final String TABLE_CREATE_USER = 
        "create table "+TABLE_USER+""+
        "("+
            USER_NAME+" TEXT PRIMARY KEY,"+
            USER_ISDEFAULT+" INTEGER,"+
            USER_SCHOOL+" TEXT,"+
            USER_PHOTO+" TEXT,"+ // ruta a fichero
            USER_AUTOSTART+" INTEGER,"+
            USER_AUTOSTARTSECONDS+" INTEGER,"+
            USER_USEPROFILE+" TEXT,"+
            USER_DEVICEPROFILE+" TEXT, "+
            "FOREIGN KEY ("+USER_USEPROFILE+") REFERENCES "+
                        TABLE_USEPROFILE+"("+USEPROFILE_NAME+"),"+
            "FOREIGN KEY ("+USER_DEVICEPROFILE+") REFERENCES "+
                        TABLE_DEVICEPROFILE+"("+DEVICEPROFILE_NAME+")"+
        ");";
    
    private static final String INSERT_DEFAULT_USEPROFILE = 
        "INSERT INTO "+TABLE_USEPROFILE+" VALUES"+
        "("+
            "'Default'"+","+
            1+","+
            UseProfile.interaction.touch_mode+","+
            UseProfile.interaction.press_and_drag+","+
            2500+","+
            UseProfile.interaction.no_voice+","+
            UseProfile.visualization.view_standard+","+
            0xFFFFFFFF+","+
            2+","+
            2+","+
            UseProfile.visualization.orientation_both+","+
            1+","+
            UseProfile.visualization.text_size_small+","+
            UseProfile.visualization.font_sans+","+
            0xFF000000+","+
            UseProfile.visualization.icon_pictogram+","+
            UseProfile.visualization.pagination_standard+","+
            0+","+
            0+","+
            3000+","+
            UseProfile.feedback.content_highlight_none+","+
            0+","+
            UseProfile.sound.no_sounds+","+
            0+","+
            0+","+
            UseProfile.sound.voice_default+
        ");";
    private static final String INSERT_DEFAULT_DEVICEPROFILE = 
        "INSERT INTO "+TABLE_DEVICEPROFILE+" VALUES"+
        "("+
            "'Default'"+","+
            1+
        ");";
    private static final String INSERT_DEFAULT_USER = 
        "INSERT INTO "+TABLE_USER+" VALUES"+
        "("+
            "'Default'"+","+
            1+","+
            "''"+","+
            "''"+","+
            1+","+
            10+","+
            "'Default'"+","+
            "'Default'"+
        ");";
    private static final String INSERT_DEFAULT_USER2 = 
        "INSERT INTO "+TABLE_USER+" VALUES"+
        "("+
            "'Jose'"+","+
            1+","+
            "''"+","+
            "''"+","+
            0+","+
            10+","+
            "'Default'"+","+
            "'Default'"+
        ");";
    
    private static final String TAG = "SettingsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mContext;
    
    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            Log.e(TAG, "Creando base de datos");
            
            db.execSQL(TABLE_CREATE_USEPROFILE);
            db.execSQL(TABLE_CREATE_DEVICEPROFILE);
            db.execSQL(TABLE_CREATE_USER);

            db.execSQL(INSERT_DEFAULT_USEPROFILE);
            db.execSQL(INSERT_DEFAULT_DEVICEPROFILE);
            db.execSQL(INSERT_DEFAULT_USER);
            db.execSQL(INSERT_DEFAULT_USER2);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_USEPROFILE);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_DEVICEPROFILE);
            onCreate(db);
        }
    }
    
    public SettingsDbAdapter(Context context)
    {
        mContext = context;
    }
    
    public SettingsDbAdapter open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close()
    {
        mDbHelper.close();
    }
    
    public List<String> getUsersList()
    {
        Cursor cursor =
            mDb.query(true, TABLE_USER, 
                    new String[] {
                        USER_NAME
                        }, 
                    null, null,
                    null, null, USER_NAME, null);
        List<String> names = new ArrayList<String>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                names.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }
    
    public List<User> getUsers(String selection)
    {
        Cursor cursor =
            mDb.query(true, TABLE_USER, 
                    new String[] {
                        USER_NAME,
                        USER_ISDEFAULT,
                        USER_SCHOOL,
                        USER_PHOTO,
                        USER_AUTOSTART,
                        USER_AUTOSTARTSECONDS,
                        USER_USEPROFILE,
                        USER_DEVICEPROFILE
                        }, 
                    selection, null,
                    null, null, USER_NAME, null);
        List<User> u = new ArrayList<User>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                u.add(cursor2User(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return u;
    }
    
    public boolean addUser(User u)
    {
        try{                   
            mDb.execSQL("INSERT INTO "+TABLE_USER+" VALUES"+
                "("+
                    "'"+u.getName()+"'"+","+
                    (u.isDefault() ? 1 : 0)+","+
                    "'"+u.getSchool()+"'"+","+
                    "'"+u.getPhotoPath()+"'"+","+
                    (u.wantsAutoStart() ? 1 : 0)+","+
                    u.getAutoStartSeconds()+","+
                    "'"+u.getUseProfileName()+"'"+","+
                    "'"+u.getDeviceProfileName()+"'"+
                ");");
            return true;
        } catch (Exception e){
            if(u != null)
                Log.w(TAG, "Error adding user "+u.getName());
            else
                Log.w(TAG, "Error adding null user");
            return false;
        }
    }
    
    
    public boolean removeUser(String name)
    {
        try{
            mDb.execSQL("DELETE FROM "+TABLE_USER+
                    " WHERE "+USER_NAME+"='"+name+"';");
            return true;
        } catch (Exception e){
            Log.w(TAG, "Error deleting user "+name);
            return false;
        }
    }
    
    public List<String> getUseProfilesList()
    {
        Cursor cursor =
            mDb.query(true, TABLE_USEPROFILE, 
                    new String[] {
                        USEPROFILE_NAME
                        }, 
                    null, null,
                    null, null, USEPROFILE_NAME, null);
        List<String> names = new ArrayList<String>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                names.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }
    
    public List<UseProfile> getUseProfiles(String selection)
    {
        Cursor cursor =
            mDb.query(true, TABLE_USEPROFILE, 
                    new String[] {
                        USEPROFILE_NAME,
                        USEPROFILE_ISDEFAULT,
                        USEPROFILE_MAININTERACTION,
                        USEPROFILE_TOUCHMODE,
                        USEPROFILE_SCANMILLIS,
                        USEPROFILE_VOICEINTERACTION,
                        
                        USEPROFILE_VIEWMODE,
                        USEPROFILE_BACKGROUNDCOLOR,
                        USEPROFILE_ROWS,
                        USEPROFILE_COLUMNS,
                        USEPROFILE_ORIENTATIONS,
                        USEPROFILE_SHOWTEXT,
                        USEPROFILE_FONTSIZE,
                        USEPROFILE_TYPOGRAPHY,
                        USEPROFILE_TEXTCOLOR,
                        USEPROFILE_ICONMODE,
                        USEPROFILE_PAGINATIONMODE,
                        
                        USEPROFILE_VIBRATION,
                        USEPROFILE_CONFIRMATION,
                        USEPROFILE_CONFIRMATIONMILLIS,
                        USEPROFILE_CONTENTHIGHLIGHT,
                        USEPROFILE_BORDERHIGHLIGHT,
                        
                        USEPROFILE_SOUNDMODE,
                        USEPROFILE_SOUNDONSELECTION,
                        USEPROFILE_SOUNDONACTION,
                        USEPROFILE_VOICEMODE
                        }, 
                    selection, null,
                    null, null, USEPROFILE_NAME, null);
        List<UseProfile> u = new ArrayList<UseProfile>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                u.add(cursor2UseProfile(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return u;
    }
    
    public boolean addUseProfile(UseProfile up)
    {
        try{
            String s ="INSERT INTO "+TABLE_USEPROFILE+" VALUES"+
            "("+
            "'"+up.name+"'"+","+
            (up.isDefault ? 1 : 0)+ ","+
            up.mainInteraction + ","+
            up.touchMode + ","+
            up.scanTimeMillis + ","+
            up.voiceInteraction + ","+
            up.viewMode + ","+
            up.backgroundColor + ","+
            up.rows + ","+
            up.columns + ","+
            up.orientations + ","+
            (up.showText ? 1 : 0) + ","+
            up.fontSize + ","+
            up.typography + ","+
            up.textColor + ","+
            up.iconMode + ","+
            up.paginationMode + ","+
            (up.vibration ? 1 : 0) + ","+
            (up.confirmation ? 1 : 0) + ","+
            up.confirmationTimeMillis + ","+
            up.contentHighlight + ","+
            (up.borderHighlight ? 1 : 0) + ","+
            up.soundMode + ","+
            (up.soundOnSelection ? 1 : 0) + ","+
            (up.soundOnAction ? 1 : 0) + ","+
            up.voiceMode +
        ");";
            mDb.execSQL(s);
            return true;
        } catch (Exception e){
            if(up != null)
                Log.e(TAG, "Error adding user "+up.getName());
            else
                Log.e(TAG, "Error adding null user");
            return false;
        }
    }
    
    public boolean removeUseProfile(String name)
    {
        try{
            mDb.execSQL("DELETE FROM "+TABLE_USEPROFILE+
                    " WHERE "+USEPROFILE_NAME+"='"+name+"';");
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error deleting use profile "+name);
            return false;
        }
    }
    
    public List<String> getDeviceProfilesList()
    {
        Cursor cursor =
            mDb.query(true, TABLE_DEVICEPROFILE, 
                    new String[] {
                        DEVICEPROFILE_NAME
                        }, 
                    null, null,
                    null, null, DEVICEPROFILE_NAME, null);
        List<String> names = new ArrayList<String>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                names.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return names;
    }
    
    public List<DeviceProfile> getDeviceProfiles(String selection)
    {
        return null;
    }
    
    public boolean addDeviceProfile(DeviceProfile u)
    {
        return true;
    }
    
    public int removeDeviceProfile(String name)
    {
        return 0;
    }
    
    private User cursor2User(Cursor c)
    {
        return new User(c.getString(0),
                        c.getInt(1)==1 ? true : false,
                        c.getString(2),
                        null, // cargar foto!
                        c.getInt(4)==1 ? true : false,
                        c.getInt(5),
                        c.getString(6),
                        c.getString(7)
                );
    }
    
    private UseProfile cursor2UseProfile(Cursor c)
    {
        UseProfile u = new UseProfile(c.getString(0));
        u.isDefault = c.getInt(1) == 1 ? true : false;
        
        u.mainInteraction = c.getInt(2);
        u.touchMode = c.getInt(3);
        u.scanTimeMillis = c.getInt(4);
        u.voiceInteraction = c.getInt(5);
        
        u.viewMode = c.getInt(6);
        u.backgroundColor = c.getInt(7);
        u.rows = c.getInt(8);
        u.columns = c.getInt(9);
        u.orientations = c.getInt(10);
        u.showText = c.getInt(11) == 1 ? true : false;
        u.fontSize = c.getInt(12);
        u.typography = c.getInt(13);
        u.textColor = c.getInt(14);
        u.iconMode = c.getInt(15);
        u.paginationMode = c.getInt(16);
        
        u.vibration = c.getInt(17) == 1 ? true : false;
        u.confirmation = c.getInt(18) == 1 ? true : false;
        u.confirmationTimeMillis = c.getInt(19);
        u.contentHighlight = c.getInt(20);
        u.borderHighlight = c.getInt(21) == 1 ? true : false;
        
        u.soundMode = c.getInt(22);
        u.soundOnSelection = c.getInt(23) == 1 ? true : false;
        u.soundOnAction = c.getInt(24) == 1 ? true : false;
        u.voiceMode = c.getInt(25);
        
        return u;
    }
}
