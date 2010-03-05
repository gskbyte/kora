package org.gskbyte.Kora.SettingsActivities;

import org.gskbyte.Kora.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class UserPreferencesActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_user);
        
        Preference customPref = (Preference) findPreference("userSchoolPref");
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        
        public boolean onPreferenceClick(Preference preference) {
            Toast.makeText(getBaseContext(), "The custom preference has been clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        
        });
    }
}