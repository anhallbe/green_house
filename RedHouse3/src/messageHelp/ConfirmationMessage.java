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

public class ConfirmationMessage {
	
	public int id;		//Unik id
	public boolean success; 	//OK, fail, mm
	public int value;
	
	public ConfirmationMessage(int id, boolean success){
		this.id = id;
		this.success = success;
	}
	
	public ConfirmationMessage(int id, boolean success, int value) {
		this.id = id;
		this.success = success;
		this.value = value;
	}
}
