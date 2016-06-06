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

import java.lang.Math;

import Jimbo.Devices.SN3218;
import Jimbo.Devices.Pi2C;

/**
 * This class allows control of the Piglow.
 * @author Jim Darby.
 */
public class Piglow
{
    public Piglow () throws IOException, InterruptedException
    {
        pg = new SN3218 (Pi2C.useBus ());
    }
    
    public void set (int led, int value) throws IOException
    {
        pg.set (led, value);
    }
    
    public void setReds (int value) throws IOException
    {
        set (REDS, value);
    }
 
    public void setOranges (int value) throws IOException
    {
        set (ORANGES, value);
    }
 
    public void setYellows (int value) throws IOException
    {
        set (YELLOWS, value);
    }
 
    public void setGreens (int value) throws IOException
    {
        set (GREENS, value);
    }
 
    public void setBlues (int value) throws IOException
    {
        set (BLUES, value);
    }
 
    public void setWhites (int value) throws IOException
    {
        set (WHITES, value);
    }

    public void setLeg0 (int value) throws IOException
    {
        set (LEG0, value);
    }
 
    public void setLeg1 (int value) throws IOException
    {
        set (LEG1, value);
    }
    
    public void setLeg2 (int value) throws IOException
    {
        set (LEG2, value);
    }

    public void update () throws IOException
    {
        pg.update ();
    }
    
    private void set (byte[] leds, int value) throws IOException
    {
        for (int i = 0; i < leds.length; ++i)
            set (leds[i], value);
    }
 
    private final SN3218 pg;
    
    private final byte[] REDS    = { 6, 17,  0 };
    private final byte[] ORANGES = { 7, 16,  1 };
    private final byte[] YELLOWS = { 8, 15,  2 };
    private final byte[] GREENS  = { 5, 13,  3 };
    private final byte[] BLUES   = { 4, 11, 14 };
    private final byte[] WHITES  = { 9, 10, 12 };
    
    private final byte[] LEG0    = {  6,  7,  8,  5,  4,  9 };
    private final byte[] LEG1    = { 10, 11, 13, 15, 16, 17 };
    private final byte[] LEG2    = { 12, 14,  3,  2,  1,  0 };
    
    public static void main (String args[]) throws IOException, InterruptedException
    {
        Piglow pg = new Piglow ();
        
        for (int i = 0; i < 18; ++i)
        {
            System.out.println (i);
            pg.set (i, 40);
            pg.update ();
            Thread.sleep (100);
            pg.set (i, 0);
            pg.update ();
            Thread.sleep (100);
        }
        
        pg.setReds (255);
        pg.update ();
        Thread.sleep (100);
        pg.setReds (0);
        
        pg.setOranges (255);
        pg.update ();
        Thread.sleep (100);
        pg.setOranges (0);
        
        pg.setYellows (255);
        pg.update ();
        Thread.sleep (100);
        pg.setYellows (0);
        
        pg.setGreens (255);
        pg.update ();
        Thread.sleep (100);
        pg.setGreens (0);
        
        pg.setBlues (255);
        pg.update ();
        Thread.sleep (100);
        pg.setBlues (0);
        
        pg.setWhites (255);
        pg.update ();
        Thread.sleep (100);
        pg.setWhites (0);

        pg.setLeg0 (255);
        pg.update ();
        Thread.sleep (100);
        pg.setLeg0 (0);
        
        pg.setLeg1 (255);
        pg.update ();
        Thread.sleep (100);
        pg.setLeg1 (0);
        
        pg.setLeg2 (255);
        pg.update ();
        Thread.sleep (100);
        pg.setLeg2 (0);
        pg.update ();
        
        while (true)
        {
            for (int step = 0; step < 256; ++step)
            {
                final double offset = (Math.PI * step) / 255;
                
                pg.setReds    (toLed (Math.sin (Math.PI * 0 / 6 + offset)));
                pg.setOranges (toLed (Math.sin (Math.PI * 1 / 6 + offset)));
                pg.setYellows (toLed (Math.sin (Math.PI * 2 / 6 + offset)));
                pg.setGreens  (toLed (Math.sin (Math.PI * 3 / 6 + offset)));
                pg.setBlues   (toLed (Math.sin (Math.PI * 4 / 6 + offset)));
                pg.setWhites  (toLed (Math.sin (Math.PI * 5 / 6 + offset)));
                pg.update     ();
                Thread.sleep (4);
            }
        }
    }
    
    private static int toLed (double value)
    {
        if (value < 0)
            value = -value;
        
        return (int) (255 * value);
    }
}
