/**
	AddSensorActivity - Lets the user add new sensors.

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

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddSensorActivity extends Activity{
	public ArrayList<String> type_list = new ArrayList<String>();
	public Activity activity = this;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_sensor_layout);
		Spinner spinner = (Spinner) findViewById(R.id.type_spinner);
		type_list.add("Temperature");
		type_list.add("Motion");
		type_list.add("Light");
		type_list.add("Energy");
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_list);
		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {	
//				Spinner spinner = (Spinner) findViewById(R.id.type_spinner);
				
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {		
			}
		});
		
		final Button addButton = (Button) findViewById(R.id.sensorSubmit_button);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(activity);
				EditText e = (EditText) findViewById(R.id.sensorName_edit);
				Spinner s = (Spinner) findViewById(R.id.type_spinner);
				Log.d("VALUE", e.getText().toString() + " " + s.getSelectedItem().toString());
				db.addSensor(new Sensor(0,e.getText().toString(), 0, s.getSelectedItem().toString()));
				Cursor c = MainActivity.db.getSensors();
				SensorFragment.adapter.changeCursor(c);
				activity.finish();
			}
		});

	}

}

