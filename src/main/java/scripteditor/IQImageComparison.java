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
 * ImageComparison.java
 *
 * Created on December 26, 2006, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;
import java.text.DecimalFormat;

/*
 *
 *
 * @author Administrator
 */


public class IQImageComparison {

//*********************************************************
//          Member Variables

    String[][] mResults;
    ImageCollection myCollection;
    int mMode;
    JLabel mPic;
    JLabel mName;
    JLabel mNameLabel;
    static Component mParent;
    JPanel mPanel;
    double mDelay;
    String mTaxLevel;
    boolean Running = true;
    boolean mPaused = false;
    String[] mTaxa;
    ActionListener myListener;
    double mFixation = .5;
    String mUserName = "";
    boolean mTestMode;
    ProgressClass mProgress; // Will be initialized calling the setProgressInstance();
    ImageScaler mScaler;
    IGradeImageComparison mGrade;
     boolean fontItalic=false;//Added by preethy on 21-04-2012
//
//**********************************************************

    /** Creates a new instance of ImageComparison */
    public IQImageComparison(Component parentForm, JLabel displayLabel, String[][] fileNames, double delay, String taxLevel,
            String[] taxa, JPanel p, ActionListener al, String name, boolean test_mode, ImageCollection ic, ProgressClass pc) {

        mResults = fileNames;
        mPic     = displayLabel;
        mParent  = parentForm;
        mDelay   = delay;
        mTaxLevel = taxLevel;
        mTaxa = taxa;
        mPanel = p;
        myListener = al;
        mUserName = name;
        mTestMode = test_mode;
        myCollection = ic;
        mProgress = pc;
        myCollection.sortForComparisons();
        mScaler = new ImageScaler();
         if(mTaxLevel.equals("Genus")||(mTaxLevel.equals("Species"))){
            fontItalic=true;
         }
        Main();
        mScaler = null;

    }


    // Main loop for an Image Comparison Run!
    private void Main() {

        // Checks to see if more than 2 taxa are selected
        if (mTaxa.length < 2) {
            Utilities.MessageDialog(mParent, "There must be more than one taxa selected for Image Comparison.");
            //JOptionPane.showMessageDialog(mParent, "There must be more than one taxa selected for Image Comparison.");

            return;
        }


        if (mTestMode)
            mGrade = GradeImageComparisonFactory.GetGradeInstance(GradeImageComparisonFactory.ImageComparisonTest, new QuizResultClass(6, mResults.length, SessionInfo.SpellingValue, mDelay),fontItalic);
        else
            mGrade = GradeImageComparisonFactory.GetGradeInstance(GradeImageComparisonFactory.ImageComparisonQuiz, new QuizResultClass(3, mResults.length, SessionInfo.SpellingValue, mDelay),fontItalic);

        //Display 2 imagees side by side and compare
        // Move leftPictureBox to left and dynamically create another pictureBox
        // for the right image to compare.
        String leftAnswer = "";
        String rightAnswer = "";
        int pbWidth = (mPanel.getWidth() / 2) - (5);
        int pbHeight = mPanel.getHeight();
        QuizResponseDialog dlgResponse = new QuizResponseDialog(mTaxLevel, true);
        String answer = "";
        boolean retVal = false;
        LayoutManager layout = mPanel.getLayout();
        mPanel.setLayout(new GridLayout(1, 2, 10, 10));
        mPic.setSize(pbWidth, pbHeight);
        mPic.setHorizontalAlignment(JLabel.RIGHT);
        mPic.setVisible(true);

        // Create Right Picture box for comparison
        JLabel rightPictureBox = new JLabel();
        mPanel.add(rightPictureBox);
        rightPictureBox.setHorizontalAlignment(JLabel.LEFT);
        rightPictureBox.setVisible(true);
        mPanel.validate();

        Image left = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), pbWidth, pbHeight, mPic);
        Image right;
        leftAnswer = parseString(myCollection.getImageName());
        //Limit # images to even numbers only!
        //If mod = 0, it's even
        int lastIndex = mResults.length;
        if ((int) (lastIndex % 2) == 1) {
            lastIndex -= 1;
        }

        for (int i = 0; i < lastIndex; i += 2) {
            mGrade.getGradeInstance().BeginQuestion();
            mPanel.setVisible(false);
            //Get Second Image (right-side)
            retVal = myCollection.MoveNext();

            right = myCollection.getCurrentImage().getImage();
            right = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), pbWidth, pbHeight, mPic);
            rightAnswer = parseString(myCollection.getImageName());
            mPic.setIcon(new ImageIcon(left));
            rightPictureBox.setIcon(new ImageIcon(right));

            while (true) {

                ////////////////////////////////////////////////
                //////  FIX IMAGE DISPLAY FOR FIRST IMAGE //////
                answer = null;
                dlgResponse.forCloseResponse();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                ////////////////////////////////////////////////
                mPanel.setVisible(true);
                JCDelay(mDelay);
                mPanel.setVisible(false);

                //get response
                dlgResponse.setLocationRelativeTo(mParent);
                //PauseAndRun();

                if (!Running) {
                    mGrade.getGradeInstance().QuitEarly();
                    break;
                }
                dlgResponse.setVisible(true);
                if (!dlgResponse.mRunning) {
                    Running = false;
                    break;
                }
                answer = dlgResponse.getResponse();
                 if(answer == null )
                {
                    dlgResponse.mRunning = true;
                }
                else{
                dlgResponse.resetResponse();

                if (mTestMode) {
                    //
                    // Send results to GradeTest for accumulation.
                    //
                   if(mGrade.Grade(answer, leftAnswer, rightAnswer,mTaxLevel)==true){
                    break; }     //Go to next question.
                } else {

                    if (mGrade.Grade(answer, leftAnswer, rightAnswer,mTaxLevel) == true) {
                        break;  //Go to next question
                    }
                }
            }
            }

            if (!Running) {
                mGrade.getGradeInstance().QuitEarly();
                break;
            }
            //Update Progress class here.
            //
            retVal = myCollection.MoveNext();
            left = myCollection.getCurrentImage().getImage();
            left = ImageScaler.VerifyImageSize(myCollection.getCurrentImage().getImage(), pbWidth, pbHeight, mPic);
            leftAnswer = parseString(myCollection.getImageName());
        }

        mPanel.remove(rightPictureBox);
        mPanel.setLayout(layout);
        mPic.setIcon(null);
        mPic.setVisible(true);
        mPanel.setVisible(true);
        mPic.setSize(mPanel.getWidth(), mPanel.getHeight());
        mPic.setHorizontalAlignment(JLabel.CENTER);
        mPanel.invalidate();
        mProgress.StoreResult(mGrade.getGradeInstance().getQuizResult(), String.valueOf(DecimalFormat.getPercentInstance().format(mGrade.getGradeInstance().NumberOfRightAnswers()/(double)(myCollection.NumberOfImages()/2))),fontItalic); // Adds the relust to the progress
    }

    public void SaveResult(){
        ScriptResult rs;
        if(mTestMode){
          rs = new ScriptResult("Test-Image Comparison", mTaxLevel, mTaxa, mUserName,
                    mGrade.getGradeInstance().FinalGrade(), "n/a", "n/a", mGrade.getGradeInstance().TotalQuestions(),
                    myCollection.NumberOfImages()/ 2, null);

        } else{
             rs = new ScriptResult("Quiz-Image Comparison", mTaxLevel, mTaxa, mUserName,
                    mGrade.getGradeInstance().FinalGrade(),
                    "n/a", "n/a", mGrade.getGradeInstance().TotalQuestions(), myCollection.NumberOfImages()/ 2, null);
            //rs = new Results("Quiz", "Image Comparison", mTaxLevel, mTaxa, mUserName, mFirst, mSecond, mThird, m4orMore, mSkipped);
            // Write the results to the user's file of this quiz or test'
      //rs.writeQuizResults();
        }
        rs.writeResults(false);
    }


    // This function will take a string and take off all begining words and return the last
    //example takes (Hello how are you) and returns (you)
    private String parseString(String name){
        // Don't need tofix string in Comparison'
        return name.trim();
    }

    // If paused this function will stop the progress of the test or quiz
    // until mPaused or Running is set to false.
    private void PauseAndRun(){
        while(mPaused && Running){
            try{
                Thread.sleep(100);
            } catch(InterruptedException e){}
        }
    }

    // A basic delay function. Pass in the amount of seconds to pause.
    private void JCDelay(double de) {
        de = de * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + de;
        while(System.currentTimeMillis() < stopTime && Running){
            try{
                Thread.sleep(5);
            } catch(InterruptedException e){}
        }
    }

    // Shows the try again dialog box to ask if the user would like to try again.
    public static int ShowTryAgainDialog(){
        dlgTryAgain dlg = new dlgTryAgain();
        dlg.setLocationRelativeTo(mParent);
        dlg.setVisible(true);
        return dlg.getResponse();
    }
      //Added by preethy on 19-01-2012
     public static int ShowAgainDialog(){
        dlgTryAgain2 tf = new dlgTryAgain2();
        tf.setLocationRelativeTo(mParent);
        tf.setVisible(true);
        return tf.getResponse();
    }
     /***/

}
