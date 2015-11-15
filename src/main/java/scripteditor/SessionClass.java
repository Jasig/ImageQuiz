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
 * SessionClass.java
 *
 * Created on July 8, 2007, 1:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.Serializable;
import javax.swing.JFrame;

/**
 *
 * @author Ben
 */
public class SessionClass implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean mComplete = false; // Allows the program to know if a session has been completed or not
    private int mType = 0; // If it is a 0 = Study 1 = Quiz or 2 = Test
    private double mTime = 1.0; // Delay time
    private int mModeQuiz = 1; // if it is Image Naming, Image Comparison, or Verification 0-3
    private int mModeTest = 1; // 0-2
    private String[] filenames; // Filenames of images and will display in order selected
    private int mSpelling = 0; // sets Spelling Sensitivity
    private double mFixationSpeed = 0.5; // Sets how long the fixation point will appear
    private boolean mShowProg = false; //shows results at end of run
    private String mTaxaLevel; // Family Genus or Species SubSpecies
    private boolean mCommonName = false; // If session uses common names or not
    private String mSessionName; // the name of the session
    private ArrayList mImages; // holds the array of images used for a session
    private String mMessage = ""; // This is the message that will be show before each session is executed.
    private boolean mRandomize = false;
    private boolean mUseArrowKeys = true;
    private boolean mImageOnly = true;
    private boolean mStopUntilKeyPress = false;
    private boolean mShowFamilyName = false;
    transient private JFrame mMain;
    transient private ImageSelectionClass isC;
    transient public int imageSelectionWidth;
    transient public int imageSelectionHeight;
    transient public int imageSelectionXlocation;
    transient public int imageSelectionYLocation;
    
    
    /** Creates a new instance of SessionClass */
    public SessionClass(int type, String sessionName, String sessionTaxaLevel, JFrame main) {
        mSessionName = sessionName;
        mType = type;
        mTaxaLevel = sessionTaxaLevel;
        mMain = main;
    }
    
    public boolean getCommonName(){
        return mCommonName;
    }
    
    public double getDelayTime(){
        return mTime;
    }
    
    public String getMessage(){
        return mMessage;
    }
    
    public double getFixation(){
        return mFixationSpeed;
    }
    public boolean getRandomize(){
        return mRandomize;
    }
    public boolean getUseArrowKeys(){
        return mUseArrowKeys;
    }
    public boolean getImageOnly(){
        return mImageOnly;
    }
    public boolean getStopUntilKeyPress(){
        return mStopUntilKeyPress;
    }
    public String[] getImages(){
        if(filenames == null){
            return null;
        }
        else
            return filenames;
    }
    public boolean getShowFamilyName(){
        return mShowFamilyName;
    }
    public int getModeQuiz(){
        return mModeQuiz;
    }
    public int getModeTest(){
        return mModeTest;
    }
    public boolean getShowProg(){
        return mShowProg;
    }
    public void setSessionName(String name){
        mSessionName = name;
    }
    public boolean isComplete(){
        return mComplete;
    }
    public void setUseArrowKeys(boolean temp){
        mUseArrowKeys = temp;
    }
    public void setStopUntilKeyPress(boolean temp){
        mStopUntilKeyPress = temp;
    }
    public void setShowFamilyName(boolean temp){
        mShowFamilyName = temp;
    }
    public void setImageOnly(boolean temp){
        mImageOnly = temp;
    }
    public void setComplete(boolean temp){
        mComplete = temp;
    }
    
    public String getTaxaLevel(){
        return mTaxaLevel;
    }
    
    public void setCopyFilenames(String[] fns){
        filenames = fns;
    }
    
    public String getSessionName(){
        if(this.mType == 0)
             return mSessionName + "  (STUDY)";
        else if(this.mType == 1)
             return mSessionName + "  (QUIZ)";
        else
             return mSessionName + "  (TEST)";
    }
    
    public String getmType(){
        if(mType == 0)
            return "study";
        else if(mType == 1) 
            return "quiz";
        else
            return "test";
    }
    
    public int getSpelling(){
        return mSpelling;
    }
    
    public void setCommonName(boolean temp){
        mCommonName = temp;
        if(mCommonName){
            mTaxaLevel = "Common Name";
        }
    }
    
    public void setMessage(String message){
        mMessage = message;
    }
   
    
    public void setDelayTime(double temp){
        mTime = temp;
    }
    
    public void setFixation(double temp){
        mFixationSpeed = temp;
    }
    
    public void setImages(JFrame main, int w, int h, int x, int y){
        // must call an editor to edit selection of images
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
        if (db.DataBaseExists() == false) {
            return;
        }
        isC = new ImageSelectionClass(mTaxaLevel,filenames,new ActionListener(){ public void actionPerformed(ActionEvent evt){ saveFilenames(evt);}}, main,w,h,x,y, db);
        //isC.setLocationRelativeTo(this);
        isC.setVisible(true);
       
    }
    
    public void setModeQuiz(int temp){
        mModeQuiz = temp;
    }
    public void setModeTest(int temp){
        mModeTest = temp;
    }
    public void setShowProg(boolean temp){
        mShowProg = temp;
    }
    public void setmRandomize(boolean temp){
        mRandomize = temp;
    }
    
    public void setSpelling(int temp){
        mSpelling = temp;
    }
    
    public void setTaxaLevel(String temp){
        mTaxaLevel = temp;
    }
    
    private void saveFilenames(ActionEvent evt){
        
        if(isC.ShouldSave() == true){
             filenames = isC.getFileNames(); 
        }
       
        imageSelectionWidth = isC.getWidth();
        imageSelectionHeight = isC.getHeight();
        Point temp = isC.getLocationOnScreen();
        imageSelectionXlocation = (int)temp.getX();;
        imageSelectionYLocation = (int)temp.getY();;
        
        
       
        
       
    }
   
    
    
}
