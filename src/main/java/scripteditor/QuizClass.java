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
 * QuizClass.java
 *
 * Created on June 12, 2006, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scripteditor;

import javax.swing.*;
import java.lang.Thread;
import java.awt.event.*;

/**
 *
 * @author Moz123
 */
public class QuizClass extends Thread {

    SpellingClass spelling;
    int mMode;
    int xCenter;
    int mNameLabelY;
    int mNameLabelHeight;
    JLabel mName;
    JLabel mNameLabel;
    int mSpellVal;
    boolean Running = true;
    boolean mPaused = false;
    boolean mScriptMode = false;
    SessionInfo mSessionInfo;
    boolean mInSession;
    MainForm mForm;//Added by preethy 0n 28-01-2012
    boolean fontItalic=false;//Added by preethy on 03-03-2012
    String taxonomicLevel;
    /** Creates a new instance of QuizClass */
    public QuizClass(SessionInfo session, int mode, int spell,MainForm obj) {
        mSessionInfo = session;
        mMode = mode;
        mSpellVal = spell;
        mForm=obj;//Added by preethy on 28-01-2012
         if(mForm.taxonomicLevel.equals("Genus")||(mForm.taxonomicLevel.equals("Species"))){
            fontItalic=true;
         }
        taxonomicLevel= mForm.taxonomicLevel;
    }

    public QuizClass(SessionInfo session, int mode, int spell, boolean inSession,String level,MainForm obj) {
        mSessionInfo = session;
        mMode = mode;
        mSpellVal = spell;
        mScriptMode = true;
        mInSession = inSession;
        taxonomicLevel=level;
        mForm=obj;//Added by preethy on 28-01-2012
    }

    @Override
    public void run() {
//        if(mMode != 3){
        String currentDirectory = Configuration.ApplicationPath() + "/images";
      //  String currentDirectory = Configuration.UserPath() + "/images";
        String[] info = new String[mSessionInfo.mFileNames.length];

        for (int i = 0; i < info.length; i++) {
            info[i] = mSessionInfo.mFileNames[i][0] + ";" + mSessionInfo.mFileNames[i][1]; // Concats example ( 01332.jpg;Acer )

        }
        try{ //Added by preethy 0n 28-01-2012
        mSessionInfo.mImageCollection = new ImageCollection(info, currentDirectory, true);
        mSessionInfo.mImageCollection.start();

//        }
        switch (mMode) {

            case 1:
                //  if(mScriptMode){
                //     ScriptImageNaming(true);
                // } else
                ImageNaming(true);
                break;
            case 2:
                // if(mScriptMode){
                //    ScriptImageNaming(false);
                //} else{
                ImageNaming(false);
                // }
                break;
            case 3:
                LaunchImageComparison();
                break;
            case 4:
                LaunchImageVerification();
                break;
            default:

            //
        }//end switch
        mSessionInfo.mImageCollection.quit();
        mSessionInfo.mCallback.actionPerformed(new ActionEvent(this, 1, "StudyImageClass"));
        }
         //Added by preethy 0n 28-01-2012
        catch(Exception e){
            if(info.length>0)
            {

                Utilities.MessageDialog(mSessionInfo.mPanel, "Images specified in the database are missing in images folder. Add images to continue.");
                //JOptionPane.showMessageDialog(mSessionInfo.mPanel, "Images specified in the database are missing in images folder. Add images to continue.");
              mForm.showMenuButtons();
            }

        }
        /**/
    }//end StartQuiz

    private void ImageNaming(boolean showPrompt) {
        ImageNamingQuiz2 quiz = new ImageNamingQuiz2(mSessionInfo, mMode, mScriptMode,taxonomicLevel);

      //  ImageNamingQuiz quiz=new ImageNamingQuiz( mSessionInfo, mMode, mScriptMode);//Commented by preethy on 19-01-2012
        if(showPrompt)
            quiz.RunWithPrompt();
        else
            quiz.RunWithoutPrompt();
        }

    private void LaunchImageComparison() {

        IQImageComparison ic;
        if (mScriptMode) {
            ic = new ScriptImageComparison(mSessionInfo.mParentForm, mSessionInfo.mDisplayLabel, mSessionInfo.mFileNames, mSessionInfo.mDelay,
                    mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mPanel, mSessionInfo.mCallback, mSessionInfo.mUserName, false,
                    mSessionInfo.mImageCollection, mSessionInfo.mProgress, mSessionInfo.mScriptName, mSessionInfo.mSessionName, mSessionInfo.mScriptFileName);

        } else {
            ic = new IQImageComparison(mSessionInfo.mParentForm, mSessionInfo.mDisplayLabel, mSessionInfo.mFileNames, mSessionInfo.mDelay,
                    mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mPanel, mSessionInfo.mCallback, mSessionInfo.mUserName, false,
                    mSessionInfo.mImageCollection, mSessionInfo.mProgress);

        }
        if(mSessionInfo.mTaxa.length > 1 ){
           ic.SaveResult();
        }

    }

    private void LaunchImageVerification() {

        IQImageVerification iv;
        if (mScriptMode) {
            mSessionInfo.mIsTest = false;
            iv = new ScriptImageVerification(mSessionInfo,fontItalic);
        } else {
            iv = new IQImageVerification(mSessionInfo,fontItalic);
        }
        if(mSessionInfo.mTaxa.length > 1 ){
             iv.SaveResult();
        }


    }

}
