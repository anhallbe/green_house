/**
	FetchSensorValueTask - Open a connection to the in-home Raspberry Pi, asks for a new value of a given sensor.

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import messageHelp.MessageTypes;
import messageHelp.Sensor;
import messageHelp.ConfirmationMessage;
import messageHelp.Message;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

public class FetchSensorValueTask extends AsyncTask<Void, Void, Integer> {
	

	SensorAdapter adapter;
	int id;
	private String URL = "192.168.1.17";
	private int PORT = 4711;
	
	public FetchSensorValueTask(SensorAdapter adapter, int id, String URL) {
		this.URL = URL;
		this.id = id;
		this.adapter = adapter;
	}
	@Override
	protected Integer doInBackground(Void... arg0) {

		Sensor sensor = new Sensor(this.id);
		Message message = new Message(123123, sensor, MessageTypes.getSensor);
		try {
			Socket socket = new Socket(URL, PORT);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    Gson gson = new Gson();
		    Log.d("SendMessageTask", "Sending message...");
		    out.println(gson.toJson(message, Message.class));
		    out.flush();
		    Log.d("SendMessageTask", "Message sent: " + gson.toJson(message, Message.class));
		    ConfirmationMessage response = gson.fromJson(in.readLine(), ConfirmationMessage.class);
		    if(response.success) {
		    	Log.d("SendMessageTask", "Success!");
		    	return response.value;
		    }
		    else
		    	Log.d("SendMessageTask", "Fail...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e("SendMessageTask", "Unknown Host");
		} catch (IOException e) {
			Log.e("SendMessageTask", "IOException");
			e.printStackTrace();
		}
		return -9999;
	}

	@Override
	protected void onPostExecute(Integer result) {
		Log.d("VALUE", result.toString());
		
		MainActivity.db.updateSensor(this.id, result);
		Cursor c = MainActivity.db.getSensors();
		adapter.changeCursor(c);
	}
	
	

}
