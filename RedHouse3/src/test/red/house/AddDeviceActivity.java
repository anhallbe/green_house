/**
	AddDeviceActivity - Lets the user add new remote devices.

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddDeviceActivity extends Activity{
	public ArrayList<String> type_list = new ArrayList<String>();
	public Activity activity = this;
	
	//Called when the Activity is created
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_device_layout);
		Spinner spinner = (Spinner) findViewById(R.id.deviceBrand_spinner);
		type_list.add("NEXA");
		type_list.add("JULA");
		type_list.add("PROOVE");
		
	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_list);
	spinner.setAdapter(spinnerArrayAdapter);
	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {				
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {		
		}
	});
	
	final Button addButton = (Button) findViewById(R.id.deviceAdd_button);
	addButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText nameEdit = (EditText) findViewById(R.id.deviceName_edit);
			EditText idEdit = (EditText) findViewById(R.id.deviceId_edit);
			EditText groupEdit = (EditText) findViewById(R.id.deviceGroup_edit);
			Spinner s = (Spinner) findViewById(R.id.deviceBrand_spinner);
			Log.d("add", idEdit.getText().toString());
			MainActivity.db.addDevice(new Device(Integer.parseInt(idEdit.getText().toString()),nameEdit.getText().toString(), s.getSelectedItem().toString().toLowerCase(), Integer.parseInt(groupEdit.getText().toString())));
			Cursor c = MainActivity.db.getDevices();
			DeviceFragment.adapter.changeCursor(c);
			activity.finish();
		}
	});

}

}
