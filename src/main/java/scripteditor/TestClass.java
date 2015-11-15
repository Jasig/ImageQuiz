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
 * TestClass.java
 *
 * Created on July 25, 2006, 8:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import java.awt.event.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Programmer2
 */
public class TestClass extends Thread{
    int mMode;
    boolean Running = true;
    boolean mPaused = false;
    boolean mScriptMode = false;
    SessionInfo mSessionInfo;
    boolean fontItalic=false;
    // MainForm mForm;//Added by preethy 0n 28-01-2012
    /** Creates a new instance of TestClass */
   // public TestClass(SessionInfo session, int mode,MainForm obj) {
    public TestClass(SessionInfo session, int mode) {
//        mSessionInfo = new SessionInfo(parentForm, displayLabel, fileNames, delay, taxLevel, taxa, p, al, UserName, true, null, fixation, prog);
        mSessionInfo = session;
        mMode    = mode;
      //  mForm=obj;//Added by preethy on 28-01-2012
    }
//
//        public TestClass(Component parentForm, JLabel displayLabel, String[][] fileNames, int mode, double delay, String taxLevel, String[] taxa, JPanel p, ActionListener al, String UserName, double fixation, ProgressClass prog) {
//        mSessionInfo = new SessionInfo(parentForm, displayLabel, fileNames, delay, taxLevel, taxa, p, al, UserName, true, null, fixation, prog);
//        mMode    = mode;
//
//    }
    public TestClass(SessionInfo session, int mode, boolean scriptmode, String scriptName, String sessionName) {
        mSessionInfo = session;
        mMode    = mode;
        mScriptMode = true;
    }
    
    @Override
    public void run(){
        
        String currentDirectory = "";
        currentDirectory =  Configuration.ApplicationPath() + "/images";
      //  currentDirectory =  Configuration.UserPath() + "/images";
        String[] info = new String[mSessionInfo.mFileNames.length];
        for(int i = 0; i < info.length; i++){
            info[i] = mSessionInfo.mFileNames[i][0] + ";" + mSessionInfo.mFileNames[i][1]; // Concats example ( 01332.jpg;Acer )
        }
        
       // try{ //Added by preethy 0n 28-01-2012
        mSessionInfo.mImageCollection = new ImageCollection(info, currentDirectory, true);
        mSessionInfo.mImageCollection.start();
        //create name label
         switch(mMode){
            case 1:
                if(mScriptMode){
                    ScriptImageNaming();
                }
                else{
                    ImageNaming();  
                }
                
                break;
            case 2:
                LaunchImageComparison();
                break;
            case 3:
                LaunchImageVerification();
                break;
            default:
                
                //
        }//end switch
        
        mSessionInfo.mImageCollection.quit();
        mSessionInfo.mCallback.actionPerformed(new ActionEvent(this, 1, "StudyImageClass"));
       // }
         //Added by preethy 0n 28-01-2012
      /*   catch(Exception e){
             e.printStackTrace();
            
            if(info.length>0)
            {
                JOptionPane.showMessageDialog(mSessionInfo.mPanel, "Images specified in the database are missing in images folder. Add images to continue.");
              mForm.showMenuButtons();
            }
         }
        /**/
        
    }//end StartQuiz
    
    private void ImageNaming(){
        ImageNamingTest test = new ImageNamingTest(mSessionInfo, mMode);//Commented by preethy on 19-01-2012
       //  TestGrade td=new TestGrade();
       // td.getSession(mSessionInfo);
       // ImageNamingTest2 test=new ImageNamingTest2(mSessionInfo, mMode);//Commented by preethy on 27-02-2012
        test.RunTest();
    }
    
    private void ScriptImageNaming(){
            ImageNamingTestScript test = new ImageNamingTestScript(mSessionInfo, mMode);
            test.RunScript();
    }

    
    private void LaunchImageComparison(){
        
        IQImageComparison ic;
        if(mScriptMode){
            ic = new ScriptImageComparison(mSessionInfo.mParentForm, mSessionInfo.mDisplayLabel, mSessionInfo.mFileNames, mSessionInfo.mDelay,
                    mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mPanel,mSessionInfo.mCallback, mSessionInfo.mUserName, true,
                    mSessionInfo.mImageCollection, mSessionInfo.mProgress, mSessionInfo.mScriptName, mSessionInfo.mSessionName, mSessionInfo.mScriptFileName);
            
        } else{
            ic = new IQImageComparison(mSessionInfo.mParentForm, mSessionInfo.mDisplayLabel, mSessionInfo.mFileNames, mSessionInfo.mDelay,
                    mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mPanel,mSessionInfo.mCallback, mSessionInfo.mUserName, true,
                    mSessionInfo.mImageCollection, mSessionInfo.mProgress);
            
        }
        if(mSessionInfo.mTaxa.length > 1 ){
           ic.SaveResult();
        }
    }
    
    private void LaunchImageVerification(){
        
        // NOV 30th Update Max 30 verifications or 10 verifications Minimum
        // Jan 6th Update: Call ScriptImageVerification if in Scriptmode.
        IQImageVerification iv;
        if(mScriptMode) {
            iv = new ScriptImageVerification(mSessionInfo,fontItalic);
        } else {
            iv  = new IQImageVerification(mSessionInfo,fontItalic);
        }
        if(mSessionInfo.mTaxa.length > 1 ){
           iv.SaveResult();
        }
    }

}

