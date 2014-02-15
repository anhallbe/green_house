#!/usr/bin/python

#
# HD44780 LCD Script for
# Raspberry Pi
#
#
# Author : Oskar Lundh
# Based on code by Matt Hawkins(http://www.raspberrypi-spy.co.uk)
# fixed multiple line, added clear, added center align, added IP show,
# added string format print,
# Date : 05/04/2013
#

# The wiring for the LCD is as follows:

# 1 : GND
# 2 : 5V
# 3 : Contrast (0-5V)*      1kOhm
# 4 : RS (Register Select)  GPIO 7
# 5 : R/W (Read Write)      - GROUND THIS PIN
# 6 : Enable or Strobe      GPIO 8
# 7 : Data Bit 0            - NOT USED
# 8 : Data Bit 1            - NOT USED
# 9 : Data Bit 2            - NOT USED
# 10: Data Bit 3            - NOT USED
# 11: Data Bit 4            GPIO 25
# 12: Data Bit 5            GPIO 24
# 13: Data Bit 6            GPIO 23
# 14: Data Bit 7            GPIO 18
# 1(15): LCD Backlight +5V**
# 2(16): LCD Backlight GND

__author__ = 'lundh'

import RPi.GPIO as GPIO
import time
import sys


class LCD():
    def __init__(self):
        GPIO.setwarnings(False)
        self.LCD_WIDTH = 16    # Maximum characters per line
        self.LCD_CHR = True
        self.LCD_CMD = False
        # LCD RAM address for the 1st line
        self.LCD_LINE_1 = 0x80
        # LCD RAM address for the 2nd line
        self.LCD_LINE_2 = 0xC0

        # Timing constants
        self.E_PULSE = 0.00005
        self.E_DELAY = 0.00005

        # Interface
        self.LCD_RS = 7
        self.LCD_E = 8
        self.LCD_D4 = 25
        self.LCD_D5 = 24
        self.LCD_D6 = 23
        self.LCD_D7 = 18
        # Use BCM GPIO numbers
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.LCD_E, GPIO.OUT)
        GPIO.setup(self.LCD_RS, GPIO.OUT)
        GPIO.setup(self.LCD_D4, GPIO.OUT)
        GPIO.setup(self.LCD_D5, GPIO.OUT)
        GPIO.setup(self.LCD_D6, GPIO.OUT)
        GPIO.setup(self.LCD_D7, GPIO.OUT)

    # Initialise display
        self.lcd_init()

    def set_pin_interface(self, rs, e, d4, d5, d6, d7):
        """
        Define all the different pins
        """
        self.LCD_RS = rs
        self.LCD_E = e
        self.LCD_D4 = d4
        self.LCD_D5 = d5
        self.LCD_D6 = d6
        self.LCD_D7 = d7

    def lcd_init(self):
        """
        Since RW is connected to ground, we don't need to set it when we
        send commands
        """
    # Initialise display
        #GPIO.output(self.LCD_RS, False)

        self.lcd_byte(0x33, self.LCD_CMD)
        self.lcd_byte(0x32, self.LCD_CMD)
        self.lcd_byte(0x28, self.LCD_CMD)
        self.lcd_byte(0x0C, self.LCD_CMD)
        self.lcd_byte(0x06, self.LCD_CMD)
        self.lcd_byte(0x01, self.LCD_CMD)

        #GPIO.output(self.LCD_RS, True)

    def output_clear(self):
        self.lcd_byte(0x33, self.LCD_CMD)
        self.lcd_byte(0x32, self.LCD_CMD)
        self.lcd_byte(0x28, self.LCD_CMD)
        self.lcd_byte(0x0C, self.LCD_CMD)
        self.lcd_byte(0x06, self.LCD_CMD)
        self.lcd_byte(0x01, self.LCD_CMD)

    def lcd_byte(self, bits, mode):
        # Send byte to data pins
        # bits = data
        # mode = True  for character
        #        False for command

        GPIO.output(self.LCD_RS, mode)

        # High bits
        GPIO.output(self.LCD_D4, False)
        GPIO.output(self.LCD_D5, False)
        GPIO.output(self.LCD_D6, False)
        GPIO.output(self.LCD_D7, False)
        if bits & 0x10 == 0x10:
            GPIO.output(self.LCD_D4, True)
        if bits & 0x20 == 0x20:
            GPIO.output(self.LCD_D5, True)
        if bits & 0x40 == 0x40:
            GPIO.output(self.LCD_D6, True)
        if bits & 0x80 == 0x80:
            GPIO.output(self.LCD_D7, True)

        # Toggle 'Enable' pin
        time.sleep(self.E_DELAY)
        GPIO.output(self.LCD_E, True)
        time.sleep(self.E_PULSE)
        GPIO.output(self.LCD_E, False)
        time.sleep(self.E_DELAY)

        # Low bits
        GPIO.output(self.LCD_D4, False)
        GPIO.output(self.LCD_D5, False)
        GPIO.output(self.LCD_D6, False)
        GPIO.output(self.LCD_D7, False)
        if bits & 0x01 == 0x01:
            GPIO.output(self.LCD_D4, True)
        if bits & 0x02 == 0x02:
            GPIO.output(self.LCD_D5, True)
        if bits & 0x04 == 0x04:
            GPIO.output(self.LCD_D6, True)
        if bits & 0x08 == 0x08:
            GPIO.output(self.LCD_D7, True)

        # Toggle 'Enable' pin
        time.sleep(self.E_DELAY)
        GPIO.output(self.LCD_E, True)
        time.sleep(self.E_PULSE)
        GPIO.output(self.LCD_E, False)
        time.sleep(self.E_DELAY)

    def output_string(self, message):
        """
        Prints a line that that can be 32 chars or less, cuts off at the 16th char
        """
        if len(message) <= 16:
            self.output_line(message, 1)
        elif len(message) <= 32:
            row2 = message[16:]
            row1 = message[:16]
            self.output_line(row1, 1)
            self.output_line(row2, 2)

    def output_line(self, message, row):
        """
        Prints a line and fills the rest of the line with spaces
        """
        time.sleep(0.05)
        if row == 1:
            self.lcd_byte(self.LCD_LINE_1, self.LCD_CMD)
        elif row == 2:
            self.lcd_byte(self.LCD_LINE_2, self.LCD_CMD)

        message = message.ljust(self.LCD_WIDTH, " ")
        for i in range(self.LCD_WIDTH):
            self.lcd_byte(ord(message[i]), self.LCD_CHR)

    def output_line_center(self, string, row):
        """
        Prints a line that is centered in the 16 spaces
        """
        time.sleep(0.05)
        if len(string) > 16:
            return

        if row == 1:
            self.lcd_byte(self.LCD_LINE_1, self.LCD_CMD)
        elif row == 2:
            self.lcd_byte(self.LCD_LINE_2, self.LCD_CMD)

        output = ""
        space = 16 - len(string)
        for x in range(int(space / 2)):
            output += " "
        output += string
        output = output.ljust(self.LCD_WIDTH, " ")
        for i in range(self.LCD_WIDTH):
            self.lcd_byte(ord(output[i]), self.LCD_CHR)

    def output_start_message(self):
        self.output_line_center("GreenBox", 1)
        self.output_line_center("VERSION: 0.2", 2)

    def show_ip(self, row):
        """
        Finds the ip using socket connecting to default gateway adress
        if it doesn't work, try using 192.168.1.1 instead of 10.0.1.1
        """
        import socket

        def get_local_ip_address(target):
            ipaddr = ''
            try:
                s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                s.connect((target, 8000))
                ipaddr = s.getsockname()[0]
                s.close()
            except:
                print "IP-find went wrong"
            return ipaddr

        print "IP displayed...", get_local_ip_address('10.0.1.1')
        self.output_clear()
        self.output_line("IP: " + str(get_local_ip_address('10.0.1.1')), row)


if __name__ == '__main__':
    lcd = LCD()
    if len(sys.argv) < 2:
        lcd.output_start_message()
        time.sleep(4)
        lcd.show_ip(1)
        lcd.output_line("HOMECODE", 2)
    else:
        lcd.output_start_message()
        time.sleep(3)
        string = ""
        for x in sys.argv[1:]:
            string += str(x) + " "
        print string
        lcd.output_clear()
        lcd.output_string(string)
