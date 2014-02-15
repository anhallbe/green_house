/**
	Device - Represents a remote device (switch)

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

public class Device {
	private String name;		//Ex. Kökslampa, vardagsrumslampa, spis, dator
	private int id;			//Ex. Lampa, PC, 
	private String brand;
	private int group;
	
	/**
	 * @param name
	 * @param id
	 * @param brand
	 * @param group
	 */
	public Device(int id, String name, String brand, int group) {
		this.name = name;
		this.id = id;
		this.brand = brand;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getBrand() {
		return brand;
	}

	public int getGroup() {
		return group;
	}
	
	
}