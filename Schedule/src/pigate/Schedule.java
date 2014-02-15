/**
	Schedule - Handles scheduled events based on time- and sensor values.

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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import com.google.gson.Gson;

public class Schedule extends Thread{
	
	private LinkedBlockingQueue<Message> input_q;
	private Vector<ConfirmationMessage> output_q;
	private ArrayList<Job> job_list;
	private ArrayList<Sensor> sensor_list;
	private long wait = 1000;
	private Gson gson = new Gson();
	
	public Schedule(LinkedBlockingQueue<Message> input_q, Vector<ConfirmationMessage> output_q){
		this.input_q = input_q;
		this.output_q = output_q;
	}

	public void run(){
		if(PiGate.debug) System.out.println("Schedule: Starting..");
		this.init_joblist();
		//Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		if(PiGate.debug) System.out.println("Schedule: Running..");
		while(true){		
			if(this.input_q.isEmpty()){
				//increase wait more and more, but no more than the next schedule task
				this.check_schedule();
				if(PiGate.debug) System.out.println("Schedule: Queue didn't have message and no jobs.");
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				Message new_msg = this.input_q.poll();
				if(new_msg.type == MessageTypes.service){							//Service
					if(new_msg.service.type == ServiceTypes.shutdown){
						System.out.println("Shutting down..");
					}
					else if(new_msg.service.type == ServiceTypes.reset){
						System.out.println("Resetting..");
					}
					else if(new_msg.service.type == ServiceTypes.lcd){
						System.out.println("Changing lcd..");
					}
				}
				else if(new_msg.type == MessageTypes.addSensor) {						//Sensor
					Sensor s = new_msg.sensor;
					if(sensor_list.contains(s))
						output_q.add(new ConfirmationMessage(new_msg.id, false));
					else {
						sensor_list.add(s);
						output_q.add(new ConfirmationMessage(new_msg.id, true));
						if(PiGate.debug) System.out.println("Added sensor " + s.id + " to list.");
					}
				}
				else if(new_msg.type == MessageTypes.job){							//Job
			
					if(new_msg.job.type == JobTypes.schedule){
						if(PiGate.debug) System.out.println("Schedule: Got message addschedule..");
						this.add_job((new_msg.job));
						this.output_q.add(new ConfirmationMessage(new_msg.id,true));
					}
					
					else{
						if(PiGate.debug) System.out.println("Schedule: Got unknown message..");
						this.output_q.add(new ConfirmationMessage(new_msg.id,false));
					}
					
					synchronized(output_q) {
						output_q.notifyAll();		//Kastar antagligen IllegalMonitorStateException.....
						if(PiGate.debug) System.out.println("Notify threads to check outgoing messages.");
					}
					this.check_schedule();
				}
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void check_schedule() {
		if(PiGate.debug) System.out.println("Schedule: Checking schedule..");
		if (this.job_list.isEmpty())
			return;
		Date now = new Date();
		Iterator<Job> i = job_list.iterator();
		while (i.hasNext()) {
		   Job job = i.next();
			if(now.after(job.stop_date)) {
				i.remove();
				continue;	
			}
			else{
				if(job.start_date.before(now)){
					if(job.start_time.getHours() == now.getHours() && job.start_time.getMinutes() == now.getMinutes())
						if(Math.abs(now.getSeconds()- job.start_time.getSeconds())< 2){			//Behöver kalibreras, beror av wait
							job.onoff = 1;
//							if(this.exec_onoff_job(job.execStrings()))
							if(job.execute())
								if(PiGate.debug) System.out.println("Schedule: Scheduling went great..");
							else
								if(PiGate.debug) System.out.println("Schedule: Scheduling went wrong..");
						}
					
					if(job.stop_time.getHours() == now.getHours() && job.stop_time.getMinutes() == now.getMinutes())
						if(Math.abs(now.getSeconds()- job.stop_time.getSeconds())< 2){			//Behöver kalibreras, beror av wait
							job.onoff = 0;
//							if(this.exec_onoff_job(job.execStrings()))
							if(job.execute())
								if(PiGate.debug) System.out.println("Schedule: Scheduling went great..");
							else
								if(PiGate.debug) System.out.println("Schedule: Scheduling went wrong..");
						}
				}
			}
		}
	}
	
	private void check_sensors(){
		for(Sensor sensor : sensor_list){
			sensor.updateValue(false);
			if(sensor.isTriggered())
				sensor.executeJob();
		}
	}
	
	private void add_job(Job job){
		if(PiGate.debug) System.out.println("Schedule: Adding job..");
		if(this.job_list.contains(job)){
			System.out.println("Already contains job !");
			return;
		}
		this.job_list.add(job);
		
		try{
			FileWriter fw = new FileWriter("joblist.ser");
			BufferedWriter bw = new BufferedWriter(fw);
		      
		      try{
		    	  for(Job j : job_list)
		    		  bw.write(gson.toJson(j)+"\n");
		      }
		      finally{
		        bw.close();
		        fw.close();
		      }
		    }  
		    catch(IOException e){
		      System.out.println("Could not save jobs..");
		      e.printStackTrace();
		    }
	}
	
	private void init_joblist(){
		if(PiGate.debug) System.out.println("Schedule: Initializing joblist..");
		job_list = new ArrayList<Job>();
		try{
		      InputStream file = new FileInputStream( "joblist.ser" );
		      Scanner s = new Scanner(file);
		      try{
		    	  while(s.hasNext())
		    		  job_list.add(gson.fromJson(s.nextLine(), Job.class));
		      }
		      finally{
		    	  s.close();
		      }
		    }
		    catch(IOException e){
		      System.out.println("Could not read file..");
		      this.job_list = new ArrayList<Job>();
		      e.printStackTrace();
		    }
	}
	
//	public boolean exec_onoff_job(String[] exec_strings){
//		if(PiGate.debug) System.out.println("Schedule: executing action..");
//		Process process;
//		try {
//			process = new ProcessBuilder(exec_strings).start();
//			process.waitFor();
//			if(PiGate.debug) System.out.println("Schedule: Exec was success..");
//			return true;
//		} catch (IOException | InterruptedException e) {
//			if(PiGate.debug) System.out.println("Schedule: exec was failed..");
//			return false;
//		}	
//	}
	
	
	
}
