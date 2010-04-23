package org.gskbyte.kora.settingsActivities;

import org.gskbyte.kora.R;
import org.gskbyte.kora.settingsActivities.deviceProfiles.DeviceProfilesActivity;
import org.gskbyte.kora.settingsActivities.useProfiles.UseProfilesActivity;
import org.gskbyte.kora.settingsActivities.users.UsersActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SettingsActivity extends TabActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        final TabHost tabHost = getTabHost();
        Resources r = getResources();
        
        TabSpec usersTab = tabHost.newTabSpec("usersTab");
        usersTab.setIndicator(r.getString(R.string.usersTab), 
                r.getDrawable(R.drawable.icon_user));
        usersTab.setContent(new Intent(this, UsersActivity.class));
        tabHost.addTab(usersTab);
        
        TabSpec useProfilesTab = tabHost.newTabSpec("useProfilesTab");
        useProfilesTab.setIndicator(r.getString(R.string.useProfilesTab), 
                r.getDrawable(R.drawable.icon_use_profile));
        useProfilesTab.setContent(new Intent(this, UseProfilesActivity.class));
        tabHost.addTab(useProfilesTab);

        TabSpec deviceProfilesTab = tabHost.newTabSpec("deviceProfilesTab");
        deviceProfilesTab.setIndicator(r.getString(R.string.deviceProfilesTab), 
                r.getDrawable(R.drawable.icon_device_profile));
        deviceProfilesTab.setContent(new Intent(this, DeviceProfilesActivity.class));
        tabHost.addTab(deviceProfilesTab);

        tabHost.setCurrentTab(0);
    }
}
