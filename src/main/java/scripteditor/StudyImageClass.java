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
 * StudyImageClass.java
 *
 * Created on May 30, 2006, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

//import com.sun.org.apache.bcel.internal.generic.DDIV;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.util.Calendar;

/**
 *
 * @author Moz123
 */
public class StudyImageClass extends Thread {


    double imageDisplayTime = 1; // Time to display image before image name appears
    double mFixationTime = 0.5;
    boolean Running = false;
    boolean autoRun = false;
    boolean imageWithName = false;
    boolean mNaiContinue = false; // If key must be pressed to continue
    boolean mKeyToContinue = false;
    Component mForm;
    JLabel mDisplayLabel;
    boolean mRandomize;
    JLabel mNameLabel;
    JLabel mFamilyLabel;
    Dimension ddd;
    boolean mPaused = false;
    boolean mShowFamily;
    ActionListener myListener;
    ImageCollection myCollection;
    String[][] mResults;
    String mTlevel;
    int yCenter;
    int xCenter;
    int mNameLabelHeight;
    int mNameLabelY;
    int mFamilyLabelY;
    MainForm mForm2;//Added by preethy 0n 28-01-2012
    JPanel mPanel;
    boolean fromScript;
        /** Creates a new instance of StudyImageClass */

        public StudyImageClass(Component parentForm, JLabel displayLabel, boolean randomize, ActionListener al, String[][] fileNames, boolean sf, String taxaLevel,MainForm obj,JPanel p){

        mForm = parentForm;
        mDisplayLabel = displayLabel;
        mRandomize = randomize;
        myListener = al;
        mShowFamily = sf;
        mResults = fileNames;
        mTlevel = taxaLevel;
        ddd = displayLabel.getSize();
        mForm2=obj;
        mPanel=p;
    }

        /**
         * following constructor call is added by anurag to provide alphabatical sorting for study scripting functionality
        **/
    public StudyImageClass(Component parentForm, JLabel displayLabel, boolean randomize, ActionListener al, String[][] fileNames, boolean sf,
            String taxaLevel,MainForm obj,JPanel p, boolean  fromScript){

        mForm = parentForm;
        mDisplayLabel = displayLabel;
        mRandomize = randomize;
        myListener = al;
        mShowFamily = sf;
        mResults = fileNames;
        mTlevel = taxaLevel;
        ddd = displayLabel.getSize();
        mForm2=obj;
        mPanel=p;
        this.fromScript = fromScript;
    }

    @Override
    public void run(){
        Running = true;
        //   THIS IS THE MAIN LOOP FOR STUDYING!!!!!
        String currentDirectory = Configuration.ApplicationPath() + "/images";
      //  String currentDirectory = Configuration.UserPath() + "/images";
        String[] info = new String[mResults.length];

        for(int i = 0; i < info.length; i++){
            info[i] = mResults[i][0] + ";" + mResults[i][1]; // Concats example ( 01332.jpg;Acer )

        }
        try{ //Added by preethy
       // myCollection = new ImageCollection(info, currentDirectory, mRandomize,mPanel,mForm2);
        myCollection = new ImageCollection(info, currentDirectory, mRandomize, this.fromScript);
        myCollection.fromScript = true;
        myCollection.start();
        if(autoRun)
            AutoRunning();
        else
            ManuelRun();
        myCollection.quit();
        mDisplayLabel.setIcon(null);
        //Notify MainForm that StudyImage session is over.
        myListener.actionPerformed(new ActionEvent(this, 1, "StudyImageClass"));
        }
         //Added by preethy 28-01-2012
        catch(Exception e){

            if(info.length>0)
            {
                Utilities.MessageDialog(mPanel, "Images specified in the database are missing in images folder. Add images to continue.");
              mForm2.showMenuButtons();
            }

        }
        /**/
    }

    public void setDisplayTime(double t){
        imageDisplayTime = t;
    }

    public void setFixationTime(double t){
        mFixationTime = t;
    }

    public void setImageWithName(boolean b){
        imageWithName = b;
    }

    public void setAutoRun(boolean b){
        autoRun = b;
    }

    public void stopStudying(){
        Running = false;
        mDisplayLabel.setIcon(null);
        mDisplayLabel.setText("");
         mDisplayLabel.removeAll();
    }

    public void keyHit(){
        mNaiContinue = true;
    }

    public boolean nextImage(){// Returns a boolean telling if there are anymore next Images.
        boolean isMore = false;
        try {
            isMore = myCollection.MoveNext();

        } catch(Exception npe) {
            //npe.printStackTrace();
            String retmsg = npe.toString();
            String msg = npe.getMessage();
            if(retmsg.contains("NullPointerException"))
                msg = "Images specified in the database are missing in images folder. Add images to continue.";
            Utilities.MessageDialog(mForm, msg);
            return false;
        }
        Image tempImage;
        if(isMore == true){

            tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
            mDisplayLabel.setIcon(new ImageIcon(tempImage));


            if(autoRun || (!autoRun && imageWithName)){

                    setImageName(mNameLabel, getParsedName(getImageName()), mNameLabelY);
            }
            if(mShowFamily){

                String temp = getImageName();
                setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);
                if(imageWithName){
                mFamilyLabel.setVisible(true);
                }
            }
            return true;
        } else{// It is the end of the run!!
            return false;
        }
    }

    public void setImageName(JLabel lbl, String txt, int labelY){
        String[] b=txt.split("#");
        if(b.length>1)
        {
            txt=b[1];
        }
        else
            txt= b[0];
        int width = (int)(3 * lbl.getFont().getSize() / 5.0 * (txt.length() + 4));
        lbl.setLocation(xCenter - width/2, labelY);
        lbl.setSize(width, mNameLabelHeight);

        lbl.setText(txt);

    }

    public String getImageName(){
        return myCollection.getImageName();
    }

    public void prevImage(){
        boolean isMore = myCollection.MovePrevious();
        Image tempImage;
        if(isMore == true){
            tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
        mDisplayLabel.setIcon(new ImageIcon(tempImage));
            if(imageWithName){

                setImageName(mNameLabel, getParsedName(getImageName()), mNameLabelY);
            }
            if(mShowFamily){

                String temp = getImageName();
                setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);
            }
        }
    }

    @SuppressWarnings("empty-statement")
    private void JCDelay(double de) {
        de = de * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + de;
        while(System.currentTimeMillis() < stopTime)
        {

            if(mPaused == true)
                break;

        }

    }



    private void AutoRunning(){
        //Load name label
        //Add to DisplayLabel
        Image tempImage;
        // Create Name Label!!!
        setStandardNameLabel();
        Dimension dd = new Dimension();
        dd = mDisplayLabel.getSize();
        xCenter = (int)dd.getWidth() / 2;
        // Place Name Label where it needs to go
        if(imageWithName){
            mNameLabelY = 200 + (int)dd.getHeight() / 2;
        } else{
            mNameLabelY = (int)dd.getHeight() / 2;
        }
        // Creating a Label to show Family Name if selected
        if(mShowFamily){// && imageWithName) {  // If show family name is selected Create this label
            mFamilyLabel = new JLabel();
            mFamilyLabel.setVisible(false);
            mFamilyLabel.setHorizontalAlignment(JLabel.CENTER);
            Font myFont2 = new Font("Tahoma", Font.BOLD, 30);
            mFamilyLabel.setFont(myFont2);

            String temp = getImageName();
            mFamilyLabel.setSize(mForm.getWidth(), 30);
            // depending on the mode put the Family name up of center
            if(imageWithName){
            mFamilyLabelY = (int)dd.getHeight() / 8;
            }
            else
                mFamilyLabelY = (int)(dd.getHeight() / 2) - mNameLabel.getHeight();



            mFamilyLabel.setOpaque(true);
            mDisplayLabel.add(mFamilyLabel);
            setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);
        }
        mDisplayLabel.setHorizontalAlignment(JLabel.CENTER);
        mDisplayLabel.setText("+");
        JCDelay(mFixationTime);
        mDisplayLabel.setText("");
        mDisplayLabel.setVisible(false);                            //////// Optimizing
        tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
        mDisplayLabel.setIcon(new ImageIcon(tempImage));

        String tempName = myCollection.getImageName();
        int idx = tempName.indexOf(' ');
        // Need if Statement here for NameLabel;
//        if( mShowFamily){//!imageWithName &&
//        setImageName(mNameLabel, tempName, mNameLabelY);
//        }
//        else
        setImageName(mNameLabel, getParsedName(tempName), mNameLabelY);

        if(imageWithName){
            mNameLabel.setVisible(true);
        }
        if(mShowFamily  && imageWithName){
            setImageName(mFamilyLabel, tempName.substring(0, idx), mFamilyLabelY);
            mFamilyLabel.setVisible(true);
        }
        mDisplayLabel.setVisible(true);
        JCDelay(imageDisplayTime);
        mDisplayLabel.setIcon(null);
        if(mShowFamily  && imageWithName){
            mFamilyLabel.setVisible(false);
        }
        if(!imageWithName){
            mNameLabel.setVisible(true);
            if(mShowFamily){
                setImageName(mFamilyLabel, tempName.substring(0, idx), mFamilyLabelY);
                mFamilyLabel.setVisible(true);
        }
            if(!mKeyToContinue){
            //JCDelay(imageDisplayTime);
                JCDelay(1);
            }
            else
                while(!mNaiContinue){
                    System.out.println(".");
                if(Running == false)
                break;
                while(mPaused == true && Running == true){
                try{
                    StudyImageClass.sleep(10);
                } catch(InterruptedException e){}
            }
                }
             mNaiContinue = false;
            mNameLabel.setVisible(false);
            if(mShowFamily){
                mFamilyLabel.setVisible(false);
            }
        } else
            mNameLabel.setVisible(false);
        //System.out.println("run :"+Running);
        while(Running)
        {

          // System.out.println("while======");
            // Fixation Point
            while(mPaused == true && Running == true)
            {
                try{
                    StudyImageClass.sleep(10);
                } catch(InterruptedException e){}
            }


            if(Running == false)
                break;
            mDisplayLabel.setIcon(null);
            if(mShowFamily && imageWithName){
                mFamilyLabel.setVisible(false);
            }

            mDisplayLabel.setText("");
            mDisplayLabel.setText("+");
            JCDelay(mFixationTime);
            mDisplayLabel.setText("");
            mDisplayLabel.setVisible(false);                         //  Optimizzz
            Running = nextImage();      // Returns true if there is a next image!!
            if(Running == false){
                break;
            }

            if(imageWithName){
                mNameLabel.setVisible(true);
            }
            mDisplayLabel.setVisible(true);           // Optimizzzz
            JCDelay(imageDisplayTime);
            mDisplayLabel.setIcon(null);
            if(imageWithName){
                mNameLabel.setVisible(false);
            }

            if(imageWithName == false){
                //mDisplayLabel.setIcon(null); //clear the image
                if(mShowFamily){// && imageWithName){
                    mFamilyLabel.setVisible(true);
                }
                mNameLabel.setVisible(true); //show name label
                 if(!mKeyToContinue){
            //JCDelay(imageDisplayTime);
                     JCDelay(1);
            }
            else
                while(!mNaiContinue){
                    System.out.println(".");
                     if(Running == false)
                break;
                while(mPaused == true && Running == true){
                try{
                    StudyImageClass.sleep(10);
                } catch(InterruptedException e){}
            }
                }
             mNaiContinue = false;
                 if(mShowFamily){
                     mFamilyLabel.setVisible(false);
                    }
                mNameLabel.setVisible(false);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e){ }
        }
        mDisplayLabel.removeAll();
        mDisplayLabel.setVisible(true);
    }

     public boolean nextImageQ(){// Returns a boolean telling if there are anymore next Images.
         boolean isMore = false;
         try {
             isMore = myCollection.MoveNext();

         } catch(NullPointerException npe) {
             String msg = npe.getMessage();
             Utilities.MessageDialog(mForm, msg);
             return false;
         }
         Image tempImage;
         if(isMore == true){
             tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
            mDisplayLabel.setIcon(new ImageIcon(tempImage));

             setImageName(mNameLabel, getParsedName(getImageName()), mNameLabelY);

             String temp = getImageName();
             setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);
             return true;
         } else{// It is the end of the run!!
             return false;
         }
     }

    private void AutoRunningQ() {

        //Load name label
        //Add to DisplayLabel
        Image tempImage;
        // Create Name Label!!!
        setStandardNameLabel();
        Dimension dd = new Dimension();
        dd = mDisplayLabel.getSize();
        xCenter = (int) dd.getWidth() / 2;
        // Place Name Label where it needs to go
        mNameLabelY = (int) dd.getHeight() / 2;
        // Creating a Label to show Family Name if selected
        mFamilyLabel = new JLabel();
        mFamilyLabel.setVisible(false);
        mFamilyLabel.setHorizontalAlignment(JLabel.CENTER);
        Font myFont2 = new Font("Tahoma", Font.BOLD, 30);
        mFamilyLabel.setFont(myFont2);

        String temp = getImageName();
        mFamilyLabel.setSize(mForm.getWidth(), 30);
        // depending on the mode put the Family name up of center
        mFamilyLabelY = (int) (dd.getHeight() / 2) - mNameLabel.getHeight();
        mFamilyLabel.setOpaque(true);
        mDisplayLabel.add(mFamilyLabel);
        setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);

        mDisplayLabel.setHorizontalAlignment(JLabel.CENTER);
        mDisplayLabel.setText("+");
        JCDelay(mFixationTime);
        mDisplayLabel.setText("");
        mDisplayLabel.setVisible(false);                            //////// Optimizing
        tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
        mDisplayLabel.setIcon(new ImageIcon(tempImage));

        String tempName = myCollection.getImageName();
        int idx = tempName.indexOf(' ');
        setImageName(mNameLabel, getParsedName(tempName), mNameLabelY);
        mDisplayLabel.setVisible(true);
        JCDelay(imageDisplayTime);
        mDisplayLabel.setIcon(null);

        mNameLabel.setVisible(true);
        setImageName(mFamilyLabel, tempName.substring(0, idx), mFamilyLabelY);
        mFamilyLabel.setVisible(true);

        JCDelay(1);//imageDisplayTime);

        mNameLabel.setVisible(false);
        mFamilyLabel.setVisible(false);
        while (Running) {

            // Fixation Point
            while (mPaused == true && Running == true) {
                try {
                    StudyImageClass.sleep(1);
                } catch (InterruptedException e) {
                }
            }
            if (Running == false) {
                break;
            }
            mDisplayLabel.setIcon(null);
            mDisplayLabel.setText("");
            mDisplayLabel.setText("+");
            JCDelay(mFixationTime);
            mDisplayLabel.setText("");
            mDisplayLabel.setVisible(false);                         //  Optimizzz
            Running = nextImage();      // Returns true if there is a next image!!
            if (Running == false) {
                break;
            }
            mDisplayLabel.setVisible(true);           // Optimizzzz
            JCDelay(imageDisplayTime);
            mDisplayLabel.setIcon(null);

            mFamilyLabel.setVisible(true);

            mNameLabel.setVisible(true); //show name label

            JCDelay(1); //imageDisplayTime);

            mFamilyLabel.setVisible(false);

            mNameLabel.setVisible(false);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        mDisplayLabel.removeAll();
        mDisplayLabel.setVisible(true);

    }

     private void setStandardNameLabel(){
        mNameLabel = new JLabel();
        mNameLabel.setVisible(false);
        mNameLabel.setHorizontalAlignment(JLabel.CENTER);
         Font myFont;
        if(mTlevel.equals("Genus")||(mTlevel.equals("Species"))){
          myFont = new Font("Tahoma", Font.ITALIC, 30);
        }else{
          myFont = new Font("Tahoma", Font.BOLD, 30);
        }
        mNameLabel.setFont(myFont);
        mNameLabelHeight = 6 * mNameLabel.getFont().getSize()/5;
        mNameLabel.setSize(mForm.getWidth(), mNameLabelHeight);
        mNameLabel.setOpaque(true);
        mDisplayLabel.add(mNameLabel);
    }

    private void ManuelRun(){
        Image tempImage;
        if(imageWithName){
            //System.out.println("========");
            setStandardNameLabel();
            Dimension dd = new Dimension();
            dd = mDisplayLabel.getSize();
            mNameLabelY = 200 + (int)ddd.getHeight() / 2;
            xCenter = (int)dd.getWidth() / 2;

            setImageName(mNameLabel, getParsedName(getImageName()), mNameLabelY);
            mNameLabel.setVisible(true);
            // Label for Family name at top of screen
            if(mShowFamily) {
                mFamilyLabel = new JLabel();
                mFamilyLabel.setVisible(true);
                mFamilyLabel.setHorizontalAlignment(JLabel.CENTER);
                Font myFont2 = new Font("Tahoma", Font.BOLD, 30);
                mFamilyLabel.setFont(myFont2);

                String temp = getImageName();
                mFamilyLabel.setSize(mForm.getWidth(), 30);
                mFamilyLabel.setOpaque(true);
                mDisplayLabel.add(mFamilyLabel);
                mFamilyLabelY = (int)mDisplayLabel.getHeight()/8;
                setImageName(mFamilyLabel, temp.substring(0, temp.indexOf(' ')), mFamilyLabelY);
            }
        }
        tempImage = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), (int) ddd.getWidth(), (int) ddd.getHeight(), mDisplayLabel);
        mDisplayLabel.setIcon(new ImageIcon(tempImage));
        while(Running){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e){ }
        }
        if(imageWithName)
            mDisplayLabel.remove(mNameLabel);
        if(mShowFamily)
            mDisplayLabel.remove(mFamilyLabel);
    }

    public void paused(boolean pause){
        mPaused = pause;
    }

    public void setKeyToContinue(boolean temp){
        mKeyToContinue = temp;
    }

    private String getParsedName(String temp){
        int index = 0;

        if(mTlevel.compareTo("Family") != 0){
            index = temp.indexOf(' ');
            temp = temp.substring(index + 1, temp.length());
        }
        return temp;
    }
}
