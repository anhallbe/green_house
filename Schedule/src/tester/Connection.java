package tester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;

import com.google.gson.*;
import pigate.Job;
import pigate.Message;
import pigate.ConfirmationMessage;
import pigate.MessageTypes;
import pigate.JobTypes;
import pigate.Sensor;

import java.util.Date;

public class Connection extends Thread{
	int id;
	private String address;
	
	public Connection(int id, String address){
		this.id = id;
		this.address = address;
	}
	public void run(){
//		Job job = new Job(JobTypes.onoff,"jula",0,1,0, new Date(2013-1900, 2, 24), new Date(2013-1900, 2, 30), new Date(0, 0, 0, 17, 06), new Date(0,0,0,17,07));
//		Job job = new Job(JobTypes.onoff,"jula",0,0,0, null, null, null, null);
//		Message msg = new Message(MessageTypes.job,(int)this.id,job,null, null);
		Sensor sensor = new Sensor(1231);
		Message msg = new Message(123123, sensor, MessageTypes.getSensor);
		//ObjectMapper om = new ObjectMapper();
		Gson gson = new Gson();
		try {
			//String json = om.writeValueAsString(msg);
			String json = gson.toJson(msg);
			System.out.println("Json: " + json);
			ConfirmationMessage response;
		    BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));

//			System.setProperty("javax.net.ssl.trustStore", "Keys/myKeystore");
//			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
//		    SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
//		    
//		    SSLSocket clientSocket = (SSLSocket) f.createSocket(address, 4711);
		    Socket clientSocket;		
			clientSocket = new Socket(this.address, 4711);  
			//clientSocket = new Socket("192.168.1.6", 4711);
//			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			PrintWriter wr = new PrintWriter(clientSocket.getOutputStream());
		    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    
		    long start = System.currentTimeMillis();
		    long sslStart = System.currentTimeMillis();
//		    outToServer.writeBytes(json + "\n");
		    wr.println(json);
		    wr.flush();
//		    wr.close();
		    long sslStop = System.currentTimeMillis();
		    System.out.println(" SSL send time: " + (int)(sslStop-sslStart) + "ms.");
		    System.out.println("Sent Message");
		    
		    //response = om.readValue(inFromServer.readLine(), ConfirmationMessage.class);
		    response = gson.fromJson(inFromServer.readLine(), ConfirmationMessage.class);
		    
		    long end = System.currentTimeMillis();
		    
		    System.out.println("FROM SERVER: " + response.id + "" + response.success + " Time: " + (int)(end-start) + "ms");
		    System.out.println("Value: " + response.value);
			
		    clientSocket.close();
		} catch (IOException e) {
			System.out.println("Could not connect");
			e.printStackTrace();
		}
	}
}
