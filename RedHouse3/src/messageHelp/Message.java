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


public class Message {
	
	public MessageTypes type;
	public int id;
	public Job job;
	public Service service;
	public Sensor sensor;
	public Message(MessageTypes type, int id, Job job, Service service, Sensor sensor){
		this.type = type;
		this.id = id;
		this.job = job;
		this.service = service;
		this.sensor = sensor;
	}
	
	public Message(int id, Job job) {
		this.type = MessageTypes.job;
		this.id = id;
		this.job = job;
		this.service = null;
		this.sensor = null;
	}
	
	public Message(int id, Sensor sensor, MessageTypes type) {
		this.type = type;
		this.id = id;
		this.sensor = sensor;
	}
}

