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
public class ImageNamingQuiz2 {
 
    SpellingClass spelling;
    int mMode;
    int xCenter;
    int mNameLabelY;
    int mNameLabelHeight;
    JLabel mName;
    JLabel mNameLabel;
    boolean Running = true;
    boolean mPaused = false;
    boolean mScriptMode = false;
    SessionInfo mSessionInfo;
    QuizResponseDialog dlgQR; 
     boolean showprmpt;
      String user_response;
      String actual_answer;
      QuizResultClass mQResultsClass;
      Image temp;
      private String tempPart;
      String level;
      boolean fontItalic=false;
    public ImageNamingQuiz2(SessionInfo session, int mode, boolean script_mode,String taxaLevel) {
        mSessionInfo = session;
        mMode = mode;
        mScriptMode = script_mode;
        level=taxaLevel;
         if(level.equals("Genus")||(level.equals("Species"))){
             fontItalic=true;
         }
        
    }

    public void RunWithPrompt(){
        Main(true);
    }

    public void RunWithoutPrompt(){
        Main(false);
    }

    private void Main(boolean showPrompt) {
        
        boolean isRetry = false;
        if (showPrompt) {
            mQResultsClass = new QuizResultClass(2, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay); // arg
        } else {
            mQResultsClass = new QuizResultClass(1, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay); // arg
        }
        showprmpt=showPrompt;
       
        dlgQR = new QuizResponseDialog(mSessionInfo.mTaxaLevel, false);
        //Display image with name
        //User types response
       
         temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
        AddNameLabel();
        if (showPrompt) {
            setImageName(getParsedName(mSessionInfo.mImageCollection.getImageName()));
        } else {
            ShowFixation();
        }

        ////////////  INITIALIZE PIC BEFORE SHOWING

        mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        test();
    }
       private void test()
       {
          
        QuizGrade mGrade = new QuizGrade();
      
        for (int i = 0; i < mSessionInfo.mFileNames.length; i++) {
            mGrade.BeginQuestion();
            while(Running) {
                
                user_response = null;
                dlgQR.forCloseResponse();
                SetDlgPos(dlgQR, showprmpt);
                dlgQR.show();
                if (!dlgQR.mRunning) {
                    mSessionInfo.mDisplayLabel.setIcon(null);
                    mSessionInfo.mDisplayLabel.setText("");
                    mSessionInfo.mDisplayLabel.removeAll();
                    Running = false;
                    mGrade.QuitEarly();
                                        
                    break;
                }
                //Added
                if((dlgQR.close==true) && (dlgQR.no==true)){
                 dlgQR.setLocationRelativeTo(mSessionInfo.mParentForm);
                dlgQR.setVisible(true);
                }
          
               
                user_response = dlgQR.getResponse();
                
                if(user_response == null )
                {
                    dlgQR.mRunning = true;
                }
                else{
                   tempPart = mSessionInfo.mImageCollection.getImageName().toLowerCase().trim();
                 
                   String ans=AdvancedTrim.trim(mSessionInfo.mImageCollection.getImageName());
                   
                   String answer=getParsedName(ans);
                   
                   tempPart = AdvancedTrim.trim(tempPart);
                 //String completeAnswer = tempPart;//Commented by preethy on 11-04-2012
                 String completeAnswer = mSessionInfo.mImageCollection.getImageName();//Added by preethy on 11-04-2012
                actual_answer = getParsedName(completeAnswer.toLowerCase().trim());
                tempPart = getParsedName(tempPart);
                if (SpellingClass.getPassOrFail(SessionInfo.SpellingValue, actual_answer, user_response)) {
                    mGrade.RightAnswer();
                     if (mGrade.getTries() == 1) {
                        mQResultsClass.applyResult(completeAnswer, true);
                    }
                    RandomResponse.ShowPositiveResponse(mSessionInfo.mParentForm);
                    break;
                  } else{
                       if (mGrade.getTries() == 1) {
                        mQResultsClass.applyResult(completeAnswer, false);
                    }
                          int resp=mGrade.ShowAgainDialog();
           
                          correct cr;
                          if(resp== JOptionPane.YES_OPTION){
                          cr=new correct(fontItalic);
                          cr.Answer(answer);
                          cr.setLocationRelativeTo(mSessionInfo.mParentForm);
                          cr.setVisible(true);
                         
                          JCDelay(1);
                          cr.setVisible(false);
                          mSessionInfo.mDisplayLabel.setVisible(true);   
                        //  JCDelay(1);
                          mGrade.NextTry();
                         // test();
                         // break ; 
                     } 
                    else if(resp==JOptionPane.NO_OPTION)
                    {
                       
                         break;     
                    }
                    else if(resp==2)
                    {
                         mSessionInfo.mDisplayLabel.setVisible(true);
                         JCDelay(1);
                         mGrade.NextTry();
                        // test();
                          
                    }
                 //  break;   
            
            }
              
               }
            }
            if (!Running || i == mSessionInfo.mFileNames.length - 1) {
                
                mSessionInfo.mDisplayLabel.setIcon(null);
                mSessionInfo.mDisplayLabel.setText("");
                mSessionInfo.mDisplayLabel.removeAll();
                 break;    
            }
            mSessionInfo.mDisplayLabel.setIcon(null);
            
            if (!showprmpt) {
                mSessionInfo.mDisplayLabel.setVisible(true);
                ShowFixation();
            } else {
                mNameLabel.setText("");
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
            mSessionInfo.mImageCollection.MoveNext();
            temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
            mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
            if (showprmpt) {
                setImageName(getParsedName(mSessionInfo.mImageCollection.getImageName()));
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }
        mSessionInfo.mDisplayLabel.setIcon(null);
        mSessionInfo.mDisplayLabel.setVisible(true);
        if (mMode == 1) {
            mNameLabel.setVisible(false);
        }
        if (mScriptMode) 
        {
           
            double gradeval = 0;
            if(mGrade.TotalQuestions() == 0)
                gradeval = 0;
            else
                gradeval = mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions();
            
            /*ScriptResult rs = new ScriptResult("Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions(),
                    mSessionInfo.mScriptName, mSessionInfo.mSessionName, mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);*/
            ScriptResult rs = new ScriptResult("Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, gradeval,
                    mSessionInfo.mScriptName, mSessionInfo.mSessionName, mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);
            rs.writeResults();
        } 
        else 
        {
            double gradeval = 0;
            if(mGrade.TotalQuestions() == 0)
                gradeval = 0;
            else
                gradeval = mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions();
            
            /*ScriptResult rs = new ScriptResult("Quiz-Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions(),
                    "n/a", "n/a", mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);*/
            
            ScriptResult rs = new ScriptResult("Quiz-Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, gradeval,
                    "n/a", "n/a", mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);
            
            rs.writeResults(false);
        }
        mSessionInfo.mProgress.StoreResult(mQResultsClass, String.valueOf(DecimalFormat.getPercentInstance().format(mGrade.NumberOfRightAnswers() / (double) mSessionInfo.mImageCollection.NumberOfImages())),fontItalic);
    }
    private void AddNameLabel() {
        mNameLabel = new JLabel();
        mNameLabel.setHorizontalAlignment(JLabel.CENTER);
         if(level.equals("Genus")||(level.equals("Species"))){
           mNameLabel.setFont(new Font("Tahoma", Font.ITALIC, 30));
        }else{
        mNameLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
         }
        xCenter = mSessionInfo.mDisplayLabel.getWidth() / 2;
        mNameLabelHeight = 6 * mNameLabel.getFont().getSize() / 5;
        mNameLabel.setOpaque(true);
        if (mMode == 1) {
            mNameLabelY = (int) (mSessionInfo.mDisplayLabel.getSize().getHeight() / 2) + 200;
        } else if (mMode == 4) {
            mNameLabelY = (int) (mSessionInfo.mDisplayLabel.getSize().getHeight() / 2);
        }
        mSessionInfo.mDisplayLabel.add(mNameLabel);
    }

    public void setImageName(String txt) {
        int width = (int) (3 * mNameLabel.getFont().getSize() / 5 * (txt.length() + 4));
        mNameLabel.setLocation(xCenter - width / 2, mNameLabelY);
        mNameLabel.setSize(width, mNameLabelHeight);
       
        mNameLabel.setText(txt);
    }

    private void SetDlgPos(QuizResponseDialog dr, boolean showPrompt) {
        if (showPrompt) {
            JCDelay(mSessionInfo.mDelay);
            int dlgX = (mSessionInfo.mParentForm.getWidth() / 2) - 125;
            int dlgY = (mSessionInfo.mParentForm.getHeight() / 4) - 63;
            dr.setLocation(dlgX, dlgY); // Setting location upper center!
        } else {
            mSessionInfo.mDisplayLabel.setVisible(true);
            JCDelay(mSessionInfo.mDelay);
            mSessionInfo.mDisplayLabel.setVisible(false);
            dr.setLocationRelativeTo(mSessionInfo.mParentForm);
        }
    }

    private void ShowFixation() {
        mSessionInfo.mDisplayLabel.setVisible(true);
        mSessionInfo.mDisplayLabel.setText("+");
        JCDelay(mSessionInfo.mFixationTime);
        mSessionInfo.mDisplayLabel.setText("");
    }

    public void paused(boolean pause) {
        mPaused = pause;

    }

    @SuppressWarnings("empty-statement")
    private void JCDelay(double de) {
        de = de * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + de;
        while (System.currentTimeMillis() < stopTime && Running) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }
        ;
    }

    private String getParsedName(String temp) {
        int index = 0;

        // IF mTlevel = Family Do nothing
        // IF mTlevel = Genus show only Genus
        // If mTlevel = other show all but no Family name

        if (mSessionInfo.mTaxaLevel.compareTo("Family") != 0) {
            index = temp.indexOf(' ');
            temp = temp.substring(index + 1, temp.length());

        }
        return temp;
    }
}
