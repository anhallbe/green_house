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

import java.io.IOException;
//import java.io.Serializable;
import java.util.Date;


public class Job {
	public JobTypes type;
	public Date start_date;
	public Date stop_date;
	//simpledateformat
	public Date start_time;
	public Date stop_time;
	public String brand;
	public int unit;
	public int onoff;
	public int group;
	
//	private final String[] execStrings;
	
	public Job(JobTypes type, String brand, int unit, int onoff, int group, Date start_date, Date stop_date, Date start_time, Date stop_time){
		this.type = type;
		this.start_date = start_date;
		this.stop_date = stop_date;
		this.start_time = start_time;
		this.stop_time = stop_time;
		this.brand = brand;
		this.unit = unit;
		this.onoff = onoff;
		this.group = group;
//		this.execStrings = generateExecStrings();
	}
	
	public Job(String brand, int unit, int onoff) {
		this(JobTypes.onoff, brand, unit, onoff, 0, null, null, null, null);
	}
	
	@Override
	public String toString() {
		return "Brand: " + brand +
				"\nUnit: " + unit + 
				"\nGroup: " + group +
				"\nDate: " + start_date + " to " + stop_date + 
				"\nTime: " + start_time + " to " + stop_time + "\n";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Job) {
			return o.toString().equals(this.toString());
		}
		else
			return false;
	}
	
	private String[] generateExecStrings() {
		String scriptName;
		if(brand.equals("jula"))
			scriptName = "JULA.py";
		else if(brand.equals("nexa"))
			scriptName = "NEXA.py";
		else
			scriptName = "";
		
		String[] s = {
					"sudo",
					"python",
					scriptName, 
					Integer.toString(group),
					Integer.toString(onoff), 
					Integer.toString(unit)};
		return s;
	}
	
	public boolean execute(){
//		if(PiGate.debug) System.out.println("Schedule: executing action..");
		long start = System.currentTimeMillis();
		Process process;
		try {
			process = new ProcessBuilder(generateExecStrings()).start();
			process.waitFor();
			long stop = System.currentTimeMillis();
//			if(PiGate.debug) System.out.println("Schedule: Exec was success.. Time: " + Long.toString(stop-start));
			return true;
		} catch (Exception e) {
//			if(PiGate.debug) System.out.println("Schedule: exec was failed..");
			return false;
		}	
	}
}
