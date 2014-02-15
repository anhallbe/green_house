/**
	Server - Receivs and handle connection-, Job- and Service-requests.

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

package pigate;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//import java.net.Socket;
//import java.net.ServerSocket;
//import java.security.KeyManagementException;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
//import java.net.InetAddress;
import javax.net.ssl.*;
//import java.net.Socket;
//import java.net.ServerSocket;


import com.google.gson.Gson;

public class Server extends Thread {
	
//	public static final boolean debug = false;
	private Gson gson = new Gson();
	private final LinkedBlockingQueue<Message> inQueue = new LinkedBlockingQueue<Message>();
	private final Vector<ConfirmationMessage> outQueue = new Vector<ConfirmationMessage>();
	private final Schedule schedule;
//	private SSLServerSocket serverSocket;
	private ServerSocket serverSocket;
	private int port;
	
//	private Semaphore mutex = new Semaphore(1);
	

//String pathToKeyMaterial = "/path/to/.keystore";
//char[] password = "changeit".toCharArray();
//KeyMaterial km = new KeyMaterial( pathToKeyMaterial, password ); 
//
//server.setKeyMaterial( km );
//
//// This server trusts all client certificates presented (usually people won't present
//// client certs, but if they do, we'll give them a socket at the very least).
//server.addTrustMaterial( TrustMaterial.TRUST_ALL );
//SSLServerSocket ss = (SSLServerSocket) server.createServerSocket( 7443 );
//SSLSocket socket = (SSLSocket) ss.accept();
	
	public Server(int port) throws IOException {
		this.port = port;
		schedule  = new Schedule(inQueue, outQueue);
		init();
	}
	
	private void init() throws IOException {
//		System.setProperty("javax.net.ssl.keyStore", "Keys/myKeystore");
//		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
//		SSLServerSocketFactory f = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//		serverSocket = (SSLServerSocket) f.createServerSocket(port);
		Process process;
		try {
			process = new ProcessBuilder(new String[]{"sudo", "python", "LCD.py"}).start();
//			process.waitFor();
			if(PiGate.debug) System.out.println("Exec was success.. Time: " );
		} catch (Exception e) {
			if(PiGate.debug) System.out.println("exec was failed..");
		}	
		
		serverSocket = new ServerSocket(port);
		schedule.start();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}
	
	public void run() {
		System.out.println("Server started. Adress: " + serverSocket.getInetAddress() + " : " + port);
		while(true) {
			try {
//				SSLSocket client = (SSLSocket) serverSocket.accept();
				Socket client = serverSocket.accept();
				(new SocketHandler(client)).start();
				System.out.println("New connection accepted from " + client.getInetAddress());
			} catch (IOException e) {
				if(PiGate.debug) System.err.println("Connection error");
			}
		}
	}
	
	
	
	private class SocketHandler extends Thread {
		
//		private SSLSocket clientSocket;
		private Socket clientSocket;
		
//		public SocketHandler(SSLSocket s) {
		public SocketHandler(Socket s) {
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			clientSocket = s;
		}
		
		public void run() {
			try {
				if(PiGate.debug) System.out.println("Starting socket handler");
				BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter wr = new PrintWriter(clientSocket.getOutputStream());
				if(PiGate.debug) System.out.println("Waiting for message...");
				String s;
				s = rd.readLine();
				System.out.println(s);
				Message m = gson.fromJson(s, Message.class);
//				Message m = om.readValue(rd.readLine(), Message.class);
				ConfirmationMessage cm;
				//TODO Kan orsaka problem när flera trådar försöker använda GPIO
				if(m.type.equals(MessageTypes.job) && m.job.type.equals(JobTypes.onoff))
					cm = new ConfirmationMessage(m.id, m.job.execute());
				else if(m.type.equals(MessageTypes.getSensor)) {
					int result = m.sensor.updateValue(true);
					if(result != -9999)
						cm = new ConfirmationMessage(m.id, true, result);
					else
						cm = new ConfirmationMessage(m.id, false);
				}
				else {
					inQueue.add(m);
					int id = m.id;
					
					if(PiGate.debug) System.out.println("Added message with id: " + id + " to queue, awaiting confirmation");
					
					while((cm = isHandled(id)) == null)	//Wait for the id to be handled
						synchronized(outQueue) {
							outQueue.wait();				//Kommer antagligen att kasta IllegalMonitorStateException, fixa med Synchronized
							if(PiGate.debug) System.out.println("Got signal, checking condition");
						}
				}
				if(PiGate.debug) System.out.println("Confirmation received, sending it to client...");
				long start = System.currentTimeMillis();
				wr.println(gson.toJson(cm));
				long stop = System.currentTimeMillis();
				System.out.println("Message sent after " + (int)(stop-start) + " ms.");
//				wr.println(om.writeValueAsString(cm));
				wr.flush();
				
				if(PiGate.debug) System.out.println("Done!");
				
				rd.close();
				wr.close();
				clientSocket.close();
			} catch(IOException | InterruptedException e){
				e.printStackTrace();
			}
		}
		
		private ConfirmationMessage isHandled(int id) throws InterruptedException {
//			try{
//				mutex.acquire();
			synchronized(outQueue) {
				Iterator<ConfirmationMessage> i = outQueue.iterator();
//				for(ConfirmationMessage cm : outQueue) {
				while(i.hasNext()) {
					ConfirmationMessage cm = i.next();
					if(PiGate.debug) System.out.println("ID " + cm.id + " and state " + cm.success + " is in list.");
					if(id == cm.id) {
						if(PiGate.debug) System.out.println("Found id " + id + ", remove from list");
//						outQueue.remove(cm);
						i.remove();
						return cm;
					}
				}
			}
//			}
//			finally{mutex.release();}
			
			if(PiGate.debug) System.out.println("ID " + id + " is not in list.");
			return null;
		}
	}
}
