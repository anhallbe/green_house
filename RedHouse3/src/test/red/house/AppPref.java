/**
	AppPref - A PreferenceActivity used to set user preferences.

Copyright (C) 2013  Andreas Hallberg, Oskar Lundh

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package test.red.house;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class AppPref extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		Preference button = (Preference)findPreference("reset_button");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                @Override
		                public boolean onPreferenceClick(Preference arg0) { 
		                    Log.d("PREF","FACTORY RESET");
		                    MainActivity.db.clearDB();
		                    PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		                    manager.set(AlarmManager.RTC, System.currentTimeMillis() + 200, intent);
		                    System.exit(2);
		                    return true;
		                }
		            });
	}

}