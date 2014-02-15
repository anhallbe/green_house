/**
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

package messageHelp;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sensor {
	public final int id;
	private int trigger;
	private int value;
	private Job job;
	private String [] execStrings;
	private long timeOfLastUpdate;
	
	private final int UPDATE_FREQUENCY = 5000;
	
	/**
	 * 
	 * @param id
	 * ID of the sensor.
	 * @param trigger
	 * The value which will trigger the sensor.
	 * @param job
	 * The job that will be executed when triggered.
	 */
	public Sensor(int id, int trigger, Job job){
		this.id = id;
		this.trigger = trigger;
		this.job = job;
		this.execStrings = execStrings();
		timeOfLastUpdate = System.currentTimeMillis();
		updateValue(true);
	}
	
	public Sensor(int id) {
		this.id = id;
		this.execStrings = execStrings();
	}
	
	public boolean isTriggered() {
		return this.trigger > value;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Sensor)
			return toString().equals(o.toString());
		else
			return false;
	}
	
	public void executeJob() {
		//if(job is not handled)
			job.execute();
	}
	
	@Override
	public String toString() {
		return "ID: " + id + "TRIGGER: " + trigger + "JOB: " + job;
	}

	public String[] execStrings() {
		String[] s = {
					"sudo",
					"python",
					"Sensor.py", 
					Integer.toString(id)};
		return s;
	}
	
	/**
	 * 
	 * @param urgent
	 * true if an update needs to be now.
	 * @return
	 * The new value.
	 */
	public int updateValue(boolean urgent){
		long now = System.currentTimeMillis();
		if(!urgent && (now-timeOfLastUpdate < UPDATE_FREQUENCY))
				return value;
		
//		if(PiGate.debug) System.out.println("Schedule: executing sensorvalue..");
		Process process;
		try {
			process = new ProcessBuilder(execStrings).start();
			process.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = br.readLine();
//			value = Integer.parseInt(line);
			value = Integer.parseInt(line);
			timeOfLastUpdate = System.currentTimeMillis();
			return value;
		}
		catch (Exception e) {
//			if(PiGate.debug) System.out.println("Schedule: sensorvalue was failed..");
			return  -9999;
		}	
	}
	
}
