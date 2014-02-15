/**
	DeviceFragment - A GUI component that shows a list of remote devices.

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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DeviceFragment extends ListFragment {
	
	static public DeviceAdapter adapter;
//	private final ArrayList<Device> deviceList = new ArrayList<Device>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setRetainInstance(true);
		Cursor cursor = MainActivity.db.getDevices();
		if(adapter == null)
			adapter = new DeviceAdapter(this.getActivity(), cursor, 0);
		else
			adapter.changeCursor(cursor);
		this.setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("DataListFragment", "Item clicked: " + id);
        Cursor c = (Cursor) getListAdapter().getItem(position);
        Log.d("DeviceFragment", "ID: " + c.getInt(0));
        Log.d("DeviceFragment", "Name: " + c.getString(1));
        
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, 
	   ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  menu.setHeaderTitle("Options");  
	  menu.add(0, v.getId(), 0, "Remove Device");  

	}
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		Log.d("DeviceFragment", "item = "+ item + ", getGroupId() = " + item.getGroupId());
		if (item.getGroupId() == 0){
			switch (item.getOrder()) {
	        case 0:
	        	Log.d("DeviceFragment", "item.getOrder() = 0");
	        	
	        	Cursor c = (Cursor) getListAdapter().getItem(info.position);
	        	
	        	Log.d("DeviceFragment", "Device OPTION1 remove object: "+c.getInt(0));
	        	MainActivity.db.removeDevice(c.getInt(0));
	        	c.close();
	        	Cursor c1 = MainActivity.db.getDevices();
	    		DeviceFragment.adapter.changeCursor(c1);
	            return true;
	        case 1:
	            return true;
	        default:
			}
	    return super.onContextItemSelected(item);
		}
		else if (item.getGroupId() == 1){
		    switch (item.getOrder()) {
		        case 0:
		        	Log.d("DeviceFragment", "P");
		        	
//		        	Cursor c = (Cursor) getListAdapter().getItem(info.position);
		        	Cursor c = (Cursor) SensorFragment.adapter.getItem(info.position);
		        	
		        	Log.d("DeviceFragment", "Sensor OPTION1 remove object:"+c.getInt(0));
		        	MainActivity.db.removeSensor(c.getInt(0));
		        	c.close();
		        	Cursor c1 = MainActivity.db.getSensors();
		    		SensorFragment.adapter.changeCursor(c1);
		            return true;
		        case 1:
		            return true;
		        default:
		    }
		    return super.onContextItemSelected(item);
		}
		else{
			return true;
		}
	}
}
