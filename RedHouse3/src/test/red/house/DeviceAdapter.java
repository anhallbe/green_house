/**
	DeviceAdapter - A connection to the Device database, used to populate the GUI

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

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DeviceAdapter extends CursorAdapter{
	
	public DeviceAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		final int id = c.getInt(0);
		final String name = c.getString(1);
		final String brand = c.getString(2);
		
		TextView tv = (TextView) v.findViewById(R.id.name);
        tv.setText(name);
        
        final Button onButton = (Button) v.findViewById(R.id.ButtonOn);
        final Button offButton = (Button) v.findViewById(R.id.ButtonOff);
        
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
    	final String ip = SP.getString("greenhousebox_address", "192.168.1.17");

        onButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onButton.setEnabled(false);
				offButton.setEnabled(false);
				Log.d("ButtonClick", "ID = " + id);
				new SendMessageTask(id, brand, 1, ip).execute(onButton, offButton);
			}
		});

        offButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onButton.setEnabled(false);
				offButton.setEnabled(false);
				
				Log.d("ButtonClick", "ID = " + id);
				Log.d("ButtonClick", "Name = " + name);
				new SendMessageTask(id, brand, 0, ip).execute(onButton, offButton);
			}
		});
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.remote_device_list_item, parent, false);
        return v;
	}
}
