/**
	SensorFragment - A GUI-component that contains a list of sensors.

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

//import java.util.ArrayList;
//import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class SensorFragment extends ListFragment {

	static public SensorAdapter adapter;
//	private final ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
//	private Activity activity = this.getActivity();
	private Cursor cursor;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);

		cursor = MainActivity.db.getSensors();
		if(adapter == null)
			adapter = new SensorAdapter(this.getActivity(), cursor, 0);
		else
			adapter.changeCursor(cursor);
		this.setListAdapter(adapter);
		registerForContextMenu(getListView());
		
//		getListView().setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View arg0) {
//				new AlertDialog.Builder(getActivity())
//					.setMessage("Remove this sensor?")
//					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							
//						}
//					}).setNegativeButton("No", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//			}
//		});
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("DataListFragment", "Item clicked: " + id);
        Cursor c = (Cursor) getListAdapter().getItem(position);
        Log.d("SensorFragment", "ID: " + c.getInt(0));
        Log.d("SensorFragment", "Name: " + c.getString(1));
        
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
    	String ip = SP.getString("greenhousebox_address", "192.168.1.17");
        new FetchSensorValueTask(adapter, c.getInt(0), ip).execute();
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, 
	   ContextMenuInfo menuInfo) {

	  super.onCreateContextMenu(menu, v, menuInfo);
	  menu.setHeaderTitle("Options");  
	  menu.add(1, v.getId(), 0, "Remove Sensor");  

	}

//	@Override
//	public boolean onContextItemSelected(android.view.MenuItem item) {
//		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//		Log.d("SensorFragment", "item = "+ item + ", getGroupId() = " + item.getGroupId());
//		if (item.getGroupId() == 0){
//			switch (item.getOrder()) {
//	        case 0:
//	        	Log.d("SensorFragment", "Device OPTION1");
//	        	
//	        	Cursor c = (Cursor) getListAdapter().getItem(info.position);
//	        	
//	        	Log.d("SensorFragment", "Device OPTION1 remove object:"+c.getInt(0));
//	        	MainActivity.db.removeDevice(c.getInt(0));
//	        	c.close();
//	        	Cursor c1 = MainActivity.db.getDevices();
//	    		DeviceFragment.adapter.changeCursor(c1);
//	            return true;
//	        case 1:
//	            return true;
//	        default:
//			}
//	    return super.onContextItemSelected(item);
//		}
//		else if (item.getGroupId() == 1){
//		    switch (item.getOrder()) {
//		        case 0:
//		        	Log.d("SensorFragment", "Sensor OPTION1");
//		        	
//		        	Cursor c = (Cursor) getListAdapter().getItem(info.position);
//		        	
//		        	Log.d("SensorFragment", "Sensor OPTION1 remove object:"+c.getInt(0));
//		        	MainActivity.db.removeSensor(c.getInt(0));
//		        	c.close();
//		        	Cursor c1 = MainActivity.db.getSensors();
//		    		SensorFragment.adapter.changeCursor(c1);
//		            return true;
//		        case 1:
//		            return true;
//		        default:
//		    }
//		    return super.onContextItemSelected(item);
//		}
//		else{
//			return true;
//		}
//	}
}
