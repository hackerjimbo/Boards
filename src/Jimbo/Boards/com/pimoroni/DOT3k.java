/*
 * Copyright (C) 2016 jim.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package Jimbo.Boards.com.pimoroni;

import java.io.IOException;

//import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import Jimbo.Devices.Pi2C;
import Jimbo.Devices.SN3218;
import Jimbo.Devices.ST7036;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

/**
 *
 * @author jim
 */
public class DOT3k {
    public DOT3k () throws IOException, InterruptedException
    {
        leds = new SN3218 (Pi2C.useBus ());
        lcd = new ST7036 (RaspiPin.GPIO_06);
        
        GpioController gpio = GpioFactory.getInstance ();
        
        up    = gpio.provisionDigitalInputPin (RaspiPin.GPIO_02, "Up",    PinPullResistance.PULL_UP);
        down  = gpio.provisionDigitalInputPin (RaspiPin.GPIO_13, "Down",  PinPullResistance.PULL_UP);
        left  = gpio.provisionDigitalInputPin (RaspiPin.GPIO_00, "Left",  PinPullResistance.PULL_UP);
        right = gpio.provisionDigitalInputPin (RaspiPin.GPIO_03, "Right", PinPullResistance.PULL_UP);
        fire  = gpio.provisionDigitalInputPin (RaspiPin.GPIO_07, "Fire!", PinPullResistance.PULL_UP);
    }
    
    public void led (int led, int r, int g, int b) throws IOException
    {
        if (led >= 3)
            throw new IOException ("Invalid RGB LED " + led);
        
        leds.set(2 - led, r, b, g);
    }
    
    public void led (int led, int value) throws IOException
    {
        if (led >= 9)
            throw new IOException ("Invalid LED " + led);
        
        leds.set (led + 9, value);
    }
    
    public void update () throws IOException
    {
        leds.update ();
    }
    
    public void clear () throws IOException
    {
        lcd.clear ();
    }
    
    public void home () throws IOException
    {
        lcd.home ();
    }
    
    public void write (String s) throws IOException
    {
        lcd.write (s);
    }

    public GpioPinDigitalInput getLeft ()
    {
        return left;
    }
    
    public GpioPinDigitalInput getRight ()
    {
        return right;
    }
     
    public GpioPinDigitalInput getUp ()
    {
        return up;
    }
      
    public GpioPinDigitalInput getDown ()
    {
        return down;
    }
      
    public GpioPinDigitalInput getFire ()
    {
        return fire;
    }
      
    private final SN3218 leds;
    private final ST7036 lcd;
    private final GpioPinDigitalInput up;
    private final GpioPinDigitalInput down;
    private final GpioPinDigitalInput left;
    private final GpioPinDigitalInput right;
    private final GpioPinDigitalInput fire;
    
    public static void main (String args[]) throws IOException, InterruptedException
    {
        DOT3k d3k = new DOT3k ();
        
        d3k.clear ();
        d3k.write ("Ahoy from Java!");
        d3k.led (0, 255, 0, 0);
        d3k.led (1, 0, 255, 0);
        d3k.led (2, 0, 0, 255);
        
        for (int i = 0; i < 9; ++i)
            d3k.led (i, ((i + 1) * 36) / 9);
        
        d3k.update ();
        
        System.out.println ("Hello from the main thread " + Thread.currentThread().getName());
        
        Listener l = new Listener ();
        
        d3k.getLeft  ().addListener (l);
        d3k.getRight ().addListener (l);
        d3k.getUp    ().addListener (l);
        d3k.getDown  ().addListener (l);
        d3k.getFire  ().addListener (l);
        
        GpioPinDigitalInput left = d3k.getLeft  ();
        
        System.out.println ("Left is " + left + " state " + left.getState () + " pull " + left.getPullResistance()); 
        
        while (true)
        {
            Thread.sleep (20);
        }
    }
    
    private static class Listener implements GpioPinListenerDigital
    {
        @Override
        public void handleGpioPinDigitalStateChangeEvent (GpioPinDigitalStateChangeEvent e)
        {
            System.out.println ("Meanwhile in thread " + Thread.currentThread().getName());
            System.out.println ("Event " + e.getEventType() + " on pin " +e.getPin() + " new state " + e.getState());
        }
        
    }
}