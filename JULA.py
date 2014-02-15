#! /usr/bin/env python
__author__ = 'lundh'

MY_HOMECODE = 0x9394ff80

TSHORT = 250
TLONG = (TSHORT * 5)
TSTART = (TSHORT * 10)
TSTOP = (TSHORT * 40)

REPEATS = 1


import time
import sys
import RPi.GPIO as GPIO


class JULA():
    GPIOMode = GPIO.BCM

    def __init__(self, pin):
        self.pin = pin

        GPIO.setmode(self.GPIOMode)
        GPIO.setup(self.pin, GPIO.OUT)

    def twait(self, us):
        time.sleep(us / 1000000.0)

    def set_level(self, polarity, time):
        if polarity:
            pass
            GPIO.output(self.pin, 1)
        else:
            pass
            GPIO.output(self.pin, 0)
        self.twait(time)

    def one_bit(self):
        self.set_level(1, TSHORT)
        self.set_level(0, TLONG)

    def zero_bit(self):
        self.set_level(1, TSHORT)
        self.set_level(0, TSHORT)

    def shift_out(self, data):
        self.set_level(1, TSHORT)
        self.set_level(0, TSTART)
        for bit in range(32):
            if data & 0x80000000:
                self.one_bit()
                self.zero_bit()
            else:
                self.zero_bit()
                self.one_bit()
            data <<= 1

        self.set_level(1, TSHORT)
        self.set_level(0, TSTOP)

    def send_data(self, id, group, onoff, channel):
        for i in range(REPEATS):
            id &= 0xFFFFFFC0
            id |= ((group & 1) << 5)
            id |= ((onoff & 1) << 4)
            id |= ((~channel) & 15)
            self.shift_out(id)

    def delayms(self, v):
        for x in range(v):
            self.twait(1000)

    def generate_homecode(self):
        return
        import random
        MY_HOMECODE = 0x9394ff80


if __name__ == "__main__":
    t = JULA(17)
    if len(sys.argv) < 4:
        t = JULA(17)
        t.send_data(MY_HOMECODE, 1, 1, 1)
        print -1
        sys.exit(1)
    else:
        t = JULA(17)
        t.send_data(MY_HOMECODE, int(sys.argv[1]), int(sys.argv[2]), int(sys.argv[3]))
        print 1
        sys.exit(0)
