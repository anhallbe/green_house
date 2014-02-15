/**
	SendMessageTask - Sends a message to the Raspberry Pi. A message can contain a Job (i.e turn device on/off, schedule event, perform maintanance etc.).

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
import messageHelp.ConfirmationMessage;
import messageHelp.Job;
import messageHelp.Message;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import com.google.gson.Gson;

public class SendMessageTask extends AsyncTask<Button, Void, Void> {
	
	Button onButton;
	Button offButton;
	int onOff;
	int id;
	String brand;
//	Activity parent;
	
	private String URL = "192.168.1.17";
	private int PORT = 4711;

	public SendMessageTask(int id, String brand, int onOff, String URL) {
		this.URL = URL;
		this.onOff = onOff;
		this.id = id;
		this.brand = brand;
	}
	
	@Override
	protected Void doInBackground(Button... arg0) {
		onButton = arg0[0];
		offButton = arg0[1];
		Log.d("SendMessageTask", "OnOff value: "+onOff);
		
		Job job = new Job(brand, id, onOff);
		Message message = new Message(1, job);
		try {
//			InputStream is = parent.getResources().openRawResource(R.raw.my_keystore);
//			Log.d("Default type", KeyStore.getDefaultType());
//			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//			ks.load(is, "123456".toCharArray());
//			is.close();
//		    SSLSocketFactory factory = new SSLSocketFactory(ks);
//		    Socket socket = new Socket();
//		    socket = factory.connectSocket(socket, URL, PORT, InetAddress.getLocalHost(), PORT, null);
			
			Socket socket = new Socket(URL, PORT);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    Gson gson = new Gson();
		    Log.d("SendMessageTask", "Sending message...");
		    out.println(gson.toJson(message, Message.class));
		    out.flush();
		    Log.d("SendMessageTask", "Message sent: " + gson.toJson(message, Message.class));
		    ConfirmationMessage response = gson.fromJson(in.readLine(), ConfirmationMessage.class);
		    if(response.success)
		    	Log.d("SendMessageTask", "Success!");
		    else
		    	Log.d("SendMessageTask", "Fail...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e("SendMessageTask", "Unknown Host");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		onButton.setEnabled(true);
		offButton.setEnabled(true);
	}
	
	

}
