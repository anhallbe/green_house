/**
	SensorAdapter - Used to populate the list of sensors.

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
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorAdapter extends CursorAdapter{

	public SensorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View v, Context arg1, Cursor c) {

		TextView nameView = (TextView) v.findViewById(R.id.sensor_name);
		final TextView valueView = (TextView) v.findViewById(R.id.sensor_numeric_value);
		TextView appendageView = (TextView) v.findViewById(R.id.sensor_value_appendage);
		
		nameView.setText(c.getString(1));
		valueView.setText(String.valueOf(c.getString(2)));
		appendageView.setText(" °C");
	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.sensor_list_item, parent, false); 
        return v;
	}
	
}
