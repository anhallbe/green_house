/**
	DatabaseHandler - A wrapper for the database "greenhouse_db". Provides ways of accessing and modifying the database.
	
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "greenhouse_db";
	private static final String TABLE_DEVICES = "devices";
	private static final String TABLE_SENSORS = "sensors";
	
	public static final String KEY_DEVICE_ID = "_id";
	public static final String KEY_DEVICE_NAME = "_name";
	public static final String KEY_DEVICE_BRAND = "_brand";
	public static final String KEY_DEVICE_GROUP = "_group";
	
	public static final String KEY_SENSOR_ID = "_id";
	public static final String KEY_SENSOR_NAME = "_name";
	public static final String KEY_SENSOR_VALUE = "_value";
	public static final String KEY_SENSOR_TYPE = "_type";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDeviceTableQuery = "CREATE TABLE " + TABLE_DEVICES + "(" + 
				KEY_DEVICE_ID + " INTEGER," + 
				KEY_DEVICE_NAME + " TEXT," + 
				KEY_DEVICE_BRAND + " TEXT," + 
				KEY_DEVICE_GROUP + " INTEGER);";
		String createSensorTableQuery = "CREATE TABLE " + TABLE_SENSORS + "(" + 
				KEY_SENSOR_ID + " INTEGER PRIMARY KEY," + 
				KEY_SENSOR_NAME + " TEXT," + 
				KEY_SENSOR_VALUE + " INTEGER," +
				KEY_SENSOR_TYPE + " TEXT);";
		
		db.execSQL(createDeviceTableQuery);
		db.execSQL(createSensorTableQuery);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);
		onCreate(db);
	}
	
	//Add a new device to the database
	synchronized public void addDevice(Device device) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DEVICE_ID, device.getId());
		values.put(KEY_DEVICE_NAME, device.getName());
		values.put(KEY_DEVICE_BRAND, device.getBrand());
		values.put(KEY_DEVICE_GROUP, device.getGroup());
		db.insert(TABLE_DEVICES, null, values);
		db.close();
	}
	
	//Add a new sensor to the database.
	synchronized public void addSensor(Sensor sensor) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
//		values.put(KEY_SENSOR_ID, sensor.getId());
		values.put(KEY_SENSOR_NAME, sensor.getName());
		values.put(KEY_SENSOR_VALUE, sensor.getValue());
		values.put(KEY_SENSOR_TYPE, sensor.getType());
		db.insert(TABLE_SENSORS, null, values);
		
		db.close();
	}
	
	//Get a Sensor with the given ID.
	synchronized public Sensor getSensor(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_SENSORS, null, KEY_SENSOR_ID+"="+id, null, null, null, null);
		Sensor s = new Sensor(
				c.getInt(c.getColumnIndex(KEY_SENSOR_ID)),
				c.getString(c.getColumnIndex(KEY_SENSOR_NAME)),
				c.getInt(c.getColumnIndex(KEY_SENSOR_VALUE)),
				c.getString(c.getColumnIndex(KEY_SENSOR_TYPE)));
		db.close();
		c.close();
		return s;		
	}
	
	//Get a remote device with the given ID.
	synchronized public Device getDevice(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_DEVICES, null, KEY_DEVICE_ID+"="+id, null, null, null, null);
		Device d = new Device(
				c.getInt(c.getColumnIndex(KEY_DEVICE_ID)),
				c.getString(c.getColumnIndex(KEY_DEVICE_NAME)),
				c.getString(c.getColumnIndex(KEY_DEVICE_BRAND)), 
				c.getInt(c.getColumnIndex(KEY_DEVICE_GROUP)));
		db.close();
		return d;
	}
	
	//Updates the value of a sensor.
	synchronized public void updateSensor(int id, int value) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SENSOR_VALUE, value);
		db.update(TABLE_SENSORS, values, KEY_SENSOR_ID+"="+id, null);
		db.close();
	}
	
	//Get a cursor to all the devices in the database.
	synchronized public Cursor getDevices() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_DEVICES, new String[] {KEY_DEVICE_ID, KEY_DEVICE_NAME, KEY_DEVICE_BRAND}, null, null, null, null, null);
		if(cursor != null)  
			cursor.moveToFirst();
		db.close();
		return cursor;
	}
	
	//Get a cursor to all the sensors in the database.
	synchronized public Cursor getSensors() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_SENSORS, new String[] {KEY_SENSOR_ID, KEY_SENSOR_NAME, KEY_SENSOR_VALUE, KEY_SENSOR_TYPE}, null, null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		db.close();
		return cursor;
	}
	
	//Remove the device with a given ID.
	synchronized public void removeDevice(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DEVICES, KEY_DEVICE_ID + "=" + id, null);
		db.close();
	}
	
	//Remove the sensor with a given ID.
	synchronized public void removeSensor(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SENSORS, KEY_SENSOR_ID + "=" + id, null);
		db.close();
	}
	
	//Clears the database
	synchronized public void clearDB(){
		SQLiteDatabase db = this.getWritableDatabase();
		onUpgrade(db, 0, 0);
	}
}
