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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scripteditor;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Preethy
 */
public class ImageNamingTest2 {
     int mMode;
    boolean Running = true;
    boolean mPaused = false;
    String tempPart;
    String user_response;
    SessionInfo mSessionInfo;
    QuizResultClass mQResultsClass;
    Image temp;
    QuizResponseDialog dlgQR;
    boolean fontItalic;
    public ImageNamingTest2(SessionInfo session, int mode){
        mSessionInfo = session;
        mMode = mode;
    }
   public void RunTest(){
       
        mQResultsClass = new QuizResultClass(5, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay); // arg
        
        dlgQR = new QuizResponseDialog(mSessionInfo.mTaxaLevel, false);
        //Display image with name
        //User types response
        temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
       
        ShowFixation();
        mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
        try {
            Thread.sleep(5); // was set at 20
        } catch (InterruptedException e){ }
       test();
   }
   public void test()
   {
       
        TestGrade mGrade = new TestGrade(mQResultsClass);

        for(int i = 0; i<mSessionInfo.mFileNames.length; i++){
            mGrade.BeginQuestion();
            while(Running){
                user_response = null;
                dlgQR.forCloseResponse();
                // mPic.setVisible(true);
                JCDelay(mSessionInfo.mDelay);
                mSessionInfo.mDisplayLabel.setVisible(false);
                dlgQR.setLocationRelativeTo(mSessionInfo.mParentForm);
                dlgQR.setVisible(true);
               
                if(!dlgQR.mRunning){
                    Running = false;
                    mGrade.QuitEarly();
                    break;  // exit while - do next image(if exists)
                }
               
                user_response = dlgQR.getResponse();
                if(user_response == null )
                {
                    dlgQR.mRunning = true;
                }
                else
                {
               
                tempPart = mSessionInfo.mImageCollection.getImageName().toLowerCase().trim();
                tempPart = AdvancedTrim.Trim(tempPart);
                String completeAnswer = tempPart;
                tempPart = getParsedName(tempPart);
                
//                if(answer.toLowerCase().compareTo(tempPart) == 0)
                if(SpellingClass.getPassOrFail(SessionInfo.SpellingValue, tempPart, user_response)){
                   // mQResultsClass.applyResult(completeAnswer, true);
                    mGrade.RightAnswer();
                    
                    
                    if (mGrade.getTries() == 1) {
                        mQResultsClass.applyResult(completeAnswer, true);
                    }
                    RandomResponse.ShowPositiveResponse(mSessionInfo.mParentForm);
                     break;// exit while - do next image(if exists)
                } else{
                    
                    int resp=mGrade.ShowAgainDialog();
                                     
                    if ( resp== JOptionPane.YES_OPTION){
                         correct cr=new correct(fontItalic);
                         cr.Answer(tempPart);
                         
                           cr.setLocationRelativeTo(mSessionInfo.mParentForm);
                           cr.setVisible(true);
                           JCDelay(mSessionInfo.mDelay);
                            cr.setVisible(false);
                          mSessionInfo.mDisplayLabel.setVisible(true);
                          JCDelay(mSessionInfo.mDelay);
                          test();
                           
                         break; 
                    } 
                    else if(resp==JOptionPane.NO_OPTION)
                    {
                     break;   
                    }
                    else if(resp==2)
                    {
                        // Running = true;
                       
                        mSessionInfo.mDisplayLabel.setVisible(true);
                         JCDelay(mSessionInfo.mDelay);
                          test();
                    }
                   break;
                   // }
                    // ANSWER IS WRONG // exit while - do next image(if exists)
                }
                }
            }
            if(!Running || i == mSessionInfo.mFileNames.length - 1){
                break; //exit For
            }
            
            mSessionInfo.mDisplayLabel.setIcon(null);
            mSessionInfo.mDisplayLabel.setVisible(true);
            ShowFixation();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e){ }

            mSessionInfo.mImageCollection.MoveNext();

            temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
            mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));

            try {
                Thread.sleep(5);
            } catch (InterruptedException e){ }

        }
       mSessionInfo.mDisplayLabel.setIcon(null);

        mSessionInfo.mDisplayLabel.setVisible(true); 

        //////////////////////////////////
        // CALL Results Class to save Results
        // Saves in User file for Project #1
        // GRADING
       // Results rs = new Results("Test", "Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mRightAnswers/mTotal, mSessionInfo.mDelay, mSessionInfo.mFixationTime);
        //rs.writeTestResults();
        ScriptResult rs;
        rs =  new ScriptResult("Test-Image Naming",mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName,
                    mGrade.FinalGrade(), "n/a", "n/a", mGrade.mTotal, mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);
        rs.writeResults(false);
        ////////////////////// View Progress ///////////////
        mSessionInfo.mProgress.StoreResult(mQResultsClass,String.valueOf(DecimalFormat.getPercentInstance().format(mGrade.NumberOfRightAnswers()/(double)mSessionInfo.mImageCollection.NumberOfImages())),fontItalic);
       mGrade = null;
   }
    private String getParsedName(String temp){
        int index = 0;

        // IF mTlevel = Family Do nothing
        // IF mTlevel = Genus show only Genus
        // If mTlevel = other show all but no Family name

        if(mSessionInfo.mTaxaLevel.compareTo("Family") != 0){
            index = temp.indexOf(' ');
            temp = temp.substring(index + 1, temp.length());
        }
        return temp;
    }

    private void ShowFixation(){
        mSessionInfo.mDisplayLabel.setText("+");
        JCDelay(mSessionInfo.mFixationTime);
        mSessionInfo.mDisplayLabel.setText("");
    }

    private void JCDelay(double seconds) {
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + (seconds *  1000);
        while(System.currentTimeMillis() < stopTime && Running){
            try{
                Thread.sleep(5);
            } catch(InterruptedException e){}
        }
    }
   
}
