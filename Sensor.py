"""
Script for reading temperature value of a a MCP9700a using a pic and the sending
to raspberry pi using uart

"""

__author__ = 'lundh'


from serial import Serial
import time
import sys


class Sensor():
    def __init__(self):
        self.serialPort = Serial("/dev/ttyAMA0", 4800, timeout=3)

    def check_temp(self,reps):
        mean = float()
        hits = 0
        for x in range(reps):
            try:
                mean += self.get_value()
                hits += 1
                time.sleep(0.001)
            except TypeError:
                continue
        return int(round(mean / hits))
    
    def get_value(self):
        if not self.serialPort.isOpen():
            self.serialPort.open()
        self.serialPort.flushInput()
        self.serialPort.flushOutput()
        self.serialPort.write("TEMP\r")
        string = ""
        while 1:
            new = self.serialPort.readline(self.serialPort.inWaiting())
            if "\n" in new:
                break;
            else:
                string += new
        try:
            string = string.replace("VALUE","")
            value = float(string)

            value * 10 - 1024
            value *= 322
            value /= 1000
            value -= 50

        except ValueError:
            print string
            return "False"
        return value
        

if __name__ == "__main__":
    sensor = Sensor()
    if len(sys.argv) >= 2:
        print sensor.check_temp(10)
        sensor.serialPort.close()
        sys.exit(0)
