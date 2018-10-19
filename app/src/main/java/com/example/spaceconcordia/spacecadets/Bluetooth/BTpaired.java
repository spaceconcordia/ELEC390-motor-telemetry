package com.example.spaceconcordia.spacecadets.Bluetooth;

import java.util.ArrayList;
//This class stores the paired bluetooth device
public class BTpaired {
    public String[] Name;
    public String[] Address;
    public Integer count;
    public Integer Select;
    public Boolean Standalone;
    BTpaired(){
        Standalone = true; // be in stand alone mode by default
         Name[0] = "test";
        count = 0;
    }
}
