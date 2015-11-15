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
 * ImageCollection.java
 *
 * Created on May 29, 2006, 3:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.lang.Thread;
import java.util.Random;


/**
 *
 * @author Moz123
 */
public class ImageCollection extends Thread{
    
//    ImageIcon mPrevImage; // Remember the old Image
//    ImageIcon mCurrentImage;// Current Image
//    ImageIcon mNextImage;// Preloaded Image
    int listSize;
    ListItem first;
    ListItem current;
    ListItem last;
    String[] mImages;
    private int mNumberOfImages;
//    int mCounter = 0;
    String mDirectory;
    String mCommand = "";
    boolean running = true;
    boolean BUSY;
    boolean isGroup;
    boolean fromScript = false;
    //JPanel mPanel;
   // MainForm mForm;
//    boolean mDone = false;
    /** Creates a new instance of ImageCollection */
    public ImageCollection(String[] names, String dir, boolean random){
        mImages = names;
       
        mNumberOfImages = mImages.length;
        if(random){
            String temp;
            int myRandomNumber;
            Random generator = new Random();
            
            for(int i = 0; i < mImages.length; i++){
                myRandomNumber = generator.nextInt(mImages.length - 1);
                temp = mImages[myRandomNumber];
                mImages[myRandomNumber] = mImages[i];
                mImages[i] = temp;
               
            }
            
            
        } else{
            
            String[] tempAr = new String[names.length];
            // MAKE ALPH ORDER
            //current format is 54365.jpg;Acredftyet;fewgrw;gwg
          
            for(int i = 0; i < tempAr.length; i++){
                tempAr[i] = names[i].substring(names[i].indexOf(";") + 1) + ";" + names[i].substring(0,names[i].indexOf(";"));
               
               }
            // Sorts by Family Name
            //java.util.Arrays.sort(tempAr);//Commented by preethy on 02/03/2012
           
            
            for(int i = 0; i < tempAr.length; i++){
                names[i] = tempAr[i].substring(tempAr[i].lastIndexOf(";") + 1) + ";" + tempAr[i].substring(0, tempAr[i].lastIndexOf(";"));
            
            }
        }
        // sortForComparisons();   // This function is used to give a 50 50 chance in Image Comparison
        
        mDirectory = dir;
        if(mImages.length < 4)
            setupList(mImages.length);
        else
            setupList(4);
      }
    
    /**
         * following constructor call is added by anurag to provide alphabatical sorting for study scripting functionality
        **/
    public ImageCollection(String[] names, String dir, boolean random, boolean fromScript){
        this.fromScript = fromScript;
        mImages = names;
       
        mNumberOfImages = mImages.length;
        if(random){
            String temp;
            int myRandomNumber;
            Random generator = new Random();
            
            for(int i = 0; i < mImages.length; i++){
                myRandomNumber = generator.nextInt(mImages.length - 1);
                temp = mImages[myRandomNumber];
                mImages[myRandomNumber] = mImages[i];
                mImages[i] = temp;
               
            }
            
            
        } else{
            
            String[] tempAr = new String[names.length];
            // MAKE ALPH ORDER
            //current format is 54365.jpg;Acredftyet;fewgrw;gwg
          
            for(int i = 0; i < tempAr.length; i++){
                tempAr[i] = names[i].substring(names[i].indexOf(";") + 1) + ";" + names[i].substring(0,names[i].indexOf(";"));
               
               }
            // Sorts by Family Name
            if(this.fromScript){  
            java.util.Arrays.sort(tempAr);//Commented by preethy on 02/03/2012
            }
            
            for(int i = 0; i < tempAr.length; i++){
                names[i] = tempAr[i].substring(tempAr[i].lastIndexOf(";") + 1) + ";" + tempAr[i].substring(0, tempAr[i].lastIndexOf(";"));
            
            }
        }
        // sortForComparisons();   // This function is used to give a 50 50 chance in Image Comparison
        
        mDirectory = dir;
        if(mImages.length < 4)
            setupList(mImages.length);
        else
            setupList(4);
      }
    
    public void run(){
        while(running){
//            while(BUSY){
//                try{
//                    Thread.sleep(10);
//                } catch(InterruptedException ec) {
//
//                }
//            }
            if(mCommand.compareTo("LoadNext") == 0){
//                BUSY = true;
                loadNextImage();
                mCommand = "";
                BUSY = false;
            } else if(mCommand.compareTo("LoadPrev") == 0){
                BUSY = true;
                loadPrevImage();
                mCommand = "";
                BUSY = false;
            }
            try{
                Thread.sleep(10);
            } catch(InterruptedException ec) {
             
            }
            
        }
    }
    
    private ListItem loadItem(int index){
        
        ListItem newItem;
       
        String test = "";
        // Sometimes mImages does not get initialized
        String filename = mImages[index].substring(0, mImages[index].lastIndexOf(';'));
      
        Image temp;
        try {
           
            temp = ImageIO.read(new File(mDirectory + "/" + filename));
             
           /* File imgfile = new File(mDirectory + "/" + filename);
            BufferedImage imgtemp = ImageIO.read(imgfile);
            imgtemp.flush();
            newItem = new ListItem(new ImageIcon(imgtemp), index);*/
         
            newItem = new ListItem(new ImageIcon(temp), index);
            return newItem;
        } catch (IOException ex) {
            //TODO:  Propogate exception back to calling function.
            return null;
        }
    }
    
    private void setupList(int listSize){
        
        //TODO:
        //  If error occurs when calling loadItem()
        //  Someone needs to be notified - the file doesn't exist
        
        first = loadItem(0);
       
        ListItem ptr;
        ptr = first;
        for(int i = 1; i < listSize; i++) {
            ListItem temp = loadItem(i);
            if(temp != null){
                ptr.next = temp;
                ptr.next.prev = ptr;
                ptr = ptr.next;
            }
        }
        last = ptr;
        current = first;
    }
    
    public ImageIcon getCurrentImage(){
        while(BUSY){
            try{
                Thread.sleep(10);
            } catch(InterruptedException ec) {
                
            }
        }
       return current.item;
    }
    
    public String getImageName(){
        String temp = mImages[current.index];
        
        int firstIndex = temp.lastIndexOf(';');
        temp = temp.substring(firstIndex + 1, temp.length());
        
        return temp;
    }
    
    public boolean MoveNext(){
        
        if(current == last){
            if(last.index < mImages.length - 1){
                //Up against the wall: HAVE to wait for an image to load!
                BUSY = true;
                mCommand = "LoadNext";
                while(BUSY){
                    try{
                        Thread.sleep(10);
                    } catch(InterruptedException ec) {
                        
                    }
                }
            } else{        //Absolutely NO MORE Images to load.
                return false;
            }
        }
        
        //Is current 2 images from last?
        //If yes, load another image
        
        if(current.next.next == last || current.next.next == null){
            //Check if another image CAN be loaded.
            if(last.index < mImages.length - 1){
                mCommand = "LoadNext";
            }
        }
        current = current.next;
        return true;
    }
    
    public boolean MovePrevious(){
        
        //Check if iterator is at front of list
        if(current == first){
            if(first.index == 0)
                return false;
            else{
                BUSY = true;
                mCommand = "LoadPrev";
                while(BUSY){
                    try{
                        Thread.sleep(10);
                    } catch(InterruptedException ec) {
                        
                    }
                }
            }
        }
        //if Current is 2 from first, move first back
        if(current.prev == first || current.prev == null){
            //Check if another image CAN be loaded.
            if(first.index > 0){
                mCommand = "LoadPrev";
                
            }
        }
        current = current.prev;
        return true;
    }
    
    public int NumberOfImages(){
        return mNumberOfImages;
    }
    
    private void loadNextImage(){
        int idx = last.index + 1;
        ListItem tmp = loadItem(idx);
        if(tmp != null){
            last.next = tmp;
            last.next.prev = last;
            last = last.next;
            last.index = idx;
            first = first.next;
            first.prev = null;
        }
    }
    
    private void loadPrevImage(){
      
        int idx = first.index - 1;
        ListItem tmp = loadItem(idx);
        if(tmp != null){
            first.prev = tmp;
            first.prev.next = first;
            first = first.prev;
            first.index = idx;
            last = last.prev;
            last.next = null;
        }
    }
    
    public void quit(){
        this.running = false;
    }
    
    // IT MAKES THE COMPARISONS BE  50 50 CHANCE THEY ARE THE SAME.
    public void sortForComparisons(){
        Random myRan = new Random();
        int temp = myRan.nextInt(2);
        int idx;
        String tempString;
        for(int i = 0; i < mImages.length - 1; i = i+2){
            if(temp == 0 && mImages[i].substring(mImages[i].indexOf(";") + 1).compareTo(mImages[i+1].substring(mImages[i+1].indexOf(";") + 1)) != 0){ //MAKE THE PAIR OF IMAGES THE SAME
                
                for(int j = i+1; j < mImages.length; j++){
                    
                    if(mImages[i].substring(mImages[i].indexOf(";") + 1).compareTo(mImages[j].substring(mImages[j].indexOf(";") + 1)) == 0){ // Switch Images with the one beside of [i]
                        tempString = mImages[i+1];
                        mImages[i+1] = mImages[j];
                        mImages[j] = tempString;
                        break;
                    }
                }
           } else if(temp == 1 && mImages[i].substring(mImages[i].indexOf(";") + 1).compareTo(mImages[i+1].substring(mImages[i+1].indexOf(";") + 1)) == 0){// MAKE THE PAIR OF IMAGES different
                
                for(int j = i+1; j < mImages.length; j++){
                    
                    if(mImages[i].substring(mImages[i].indexOf(";") + 1).compareTo(mImages[j].substring(mImages[j].indexOf(";") + 1)) != 0){ // Switch Images with the one beside of [i]
                        tempString = mImages[i+1];
                        mImages[i+1] = mImages[j];
                        mImages[j] = tempString;
                        break;
                    }
               }
            }
           temp = myRan.nextInt(2);
        }
        if(mImages.length < 4)
            setupList(mImages.length);
        else
            setupList(4);
   }
   private class ListItem{
        int index;
        ImageIcon item;
        ListItem prev;
        ListItem next;
        public ListItem(ImageIcon icon, int idx){
            item = icon;
            index = idx;
        }
    }
    
}

