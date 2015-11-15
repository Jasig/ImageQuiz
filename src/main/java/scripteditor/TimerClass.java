/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*
 * TimerClass.java
 *
 * Created on June 27, 2006, 12:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.lang.Thread;
/**
 *
 * @author Moz123
 */
public class TimerClass extends Thread{
    
    boolean timeExpired;
    double mTime;
    /** Creates a new instance of TimerClass */
    public TimerClass(double time){
        mTime = time;
    }
    
    public void run(){
        
        timeExpired = true;
        mTime = mTime * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + mTime;
        while(System.currentTimeMillis() < stopTime && timeExpired == false){
              try {
                Thread.sleep(10);
            } catch (InterruptedException e){ }
        }
            
        timeExpired = true;
        
        
    }
    
    public boolean getTimeExpired(){
        return true;
    }
    
    public void cancel(){
        timeExpired = true;
    }
    
}
