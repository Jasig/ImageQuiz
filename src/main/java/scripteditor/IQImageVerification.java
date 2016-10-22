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
 * IQImageVerification.java
 *
 * Created on December 26, 2006, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class IQImageVerification {
    int mMode;
    JLabel mName;
    JLabel mNameLabel;
    boolean Running = true;
    boolean mPaused = false;
    IGradeImageVerification mGrade;
    static SessionInfo mSessionInfo;
    ImageScaler mScaler;
    boolean italic=false;//Added by preethy on 03-03-2012
    public IQImageVerification(SessionInfo session,boolean fontItalic){
        mSessionInfo = session;
        mScaler = new ImageScaler();
        italic=fontItalic;
        ImageVerification();
        mScaler = null;


    }

    private void ImageVerification(){

        if(mSessionInfo.mTaxa.length < 2){
            Utilities.MessageDialog(mSessionInfo.mParentForm, "There must be more than one taxa selected for Image Verification.");
            //JOptionPane.showMessageDialog(mSessionInfo.mParentForm, "There must be more than one taxa selected for Image Verification.");
            return;
        }



//        //  Clear point-accumulating variables if not in testmode
//        if (!mSessionInfo.mIsTest) {
//            mGrade = new GradeImageVerificationQuiz(new QuizResultClass(4));
//        }
//        else{
//            mGrade = new GradeImageVerificationQuiz(new QuizResultClass(7));
//
//        }

        if (mSessionInfo.mIsTest)
            mGrade = GradeImageVerificationFactory.GetQuizGradeInstance(GradeImageVerificationFactory.ImageVerificationTest, new QuizResultClass(7, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay),italic);
        else
            mGrade = GradeImageVerificationFactory.GetQuizGradeInstance(GradeImageVerificationFactory.ImageVerificationQuiz, new QuizResultClass(4, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay),italic);

        //Random Pic for taxa!!
        Random generator = new Random();
        String[] myTwoAnswers = new String[2];
        int seed = mSessionInfo.mTaxa.length;
        Image image =  ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
        String randomName = getParsedName(mSessionInfo.mTaxa[generator.nextInt(seed)]); // must be random to compare with image;
        String realName = getParsedName(mSessionInfo.mImageCollection.getImageName());

        while(realName.compareTo(randomName) == 0){
            randomName = getParsedName(mSessionInfo.mTaxa[generator.nextInt(seed)]);
        }
        myTwoAnswers[0] = randomName;
        myTwoAnswers[1] = realName;
        randomName = myTwoAnswers[generator.nextInt(2)];
        QuizResponseDialog dlgResponse = new QuizResponseDialog(mSessionInfo.mTaxaLevel, true);
        String answer; // response from user
        dlgResponse.setLocationRelativeTo(mSessionInfo.mParentForm);
        AddNameLabel();

        mNameLabel.setVisible(false);
        ImageIcon tempIcon = new ImageIcon(image);
        for(int i = 0; i < mSessionInfo.mFileNames.length; i++){
            ////  Use the mNameLabel as the fixation point
            /// Never set mSessionInfo.mDisplayLabel to null. Always have image loaded.
            /// Just set visable or not

             mGrade.getGradeInstance().BeginQuestion();
             while(true){
                answer = null;
                dlgResponse.forCloseResponse();
                mNameLabel.setText(randomName);
                ShowFixation();
                mSessionInfo.mDisplayLabel.setIcon(tempIcon);
                JCDelay(mSessionInfo.mDelay);
                mSessionInfo.mDisplayLabel.setIcon(null);
                mNameLabel.setVisible(true);
                JCDelay(1); // name shown for only 1 second!
                mNameLabel.setVisible(false);

                if(!Running){
                    break;
                }
                dlgResponse.setVisible(true);
                if(!dlgResponse.mRunning){
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
                if(mGrade.Grade(answer, realName, randomName, mSessionInfo.mImageCollection.getImageName()) == true){
                        break;  //Don't repeat.
                }
             }
            }

            if (!Running) {
                mGrade.getGradeInstance().QuitEarly();
                break;
            }

            // Update progress class.

            //
            mSessionInfo.mImageCollection.MoveNext();
            image =  ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
            tempIcon = new ImageIcon(image);
            realName = getParsedName(mSessionInfo.mImageCollection.getImageName());
            randomName = getParsedName(mSessionInfo.mTaxa[generator.nextInt(seed)]);

            while(realName.compareTo(randomName) == 0){
                randomName = getParsedName(mSessionInfo.mTaxa[generator.nextInt(seed)]);
            }
            myTwoAnswers[0] = randomName;
            myTwoAnswers[1] = realName;
            randomName = myTwoAnswers[generator.nextInt(2)];
        }
        mSessionInfo.mProgress.StoreResult(mGrade.getGradeInstance().getQuizResult(), String.valueOf(DecimalFormat.getPercentInstance().format(mGrade.getGradeInstance().NumberOfRightAnswers()/(double)mSessionInfo.mImageCollection.NumberOfImages())),italic); // Adds the result to the progress
    }

//    public void SaveResult() {
//        if(mSessionInfo.mIsTest){
//            Results rs = new Results("Test", "Image Verification", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, String.valueOf(mRightAnswers) + "/" + String.valueOf(mTotal), mDelay, mSessionInfo.mFixationTime);
//            rs.writeTestResults();
//        }else{
//            Results rs = new Results("Quiz", "Image Verification", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mFirst, mSecond, mThird, m4orMore, mSkipped);
//            rs.writeQuizResults();
//        }
//
//    }

     public void SaveResult(){
        ScriptResult rs;
        if(mSessionInfo.mIsTest){
          rs =  new ScriptResult("Test-Image Verification", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName,
                    mGrade.getGradeInstance().FinalGrade(),
                    "n/a", "n/a", mGrade.getGradeInstance().TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), null);

        } else{
             rs =  new ScriptResult("Quiz-Image Verification", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName,
                    mGrade.getGradeInstance().FinalGrade(),
                    "n/a", "n/a",  mGrade.getGradeInstance().TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), null);
        }
        rs.writeResults(false);
    }

    private void AddNameLabel(){
        mNameLabel = new JLabel();
        mNameLabel.setHorizontalAlignment(JLabel.CENTER);

        if(italic){
           mNameLabel.setFont(new Font("Tahoma", Font.ITALIC, 30));
        }else{
        mNameLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        }
        mNameLabel.setSize(mSessionInfo.mParentForm.getWidth(), 6 * 30 / 5);
        mNameLabel.setOpaque(true);
        mNameLabel.setLocation(0, (int)mSessionInfo.mDisplayLabel.getSize().getHeight()/2);
        mSessionInfo.mDisplayLabel.add(mNameLabel);
    }

    private String getParsedName(String temp){
        int index = 0;

        if(mSessionInfo.mTaxaLevel.compareTo("Family") != 0){
            index = temp.indexOf(' ');
            temp = temp.substring(index + 1, temp.length());
        }
        return temp;
    }

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

    private void ShowFixation(){
        mSessionInfo.mDisplayLabel.setText("+");
        JCDelay(mSessionInfo.mFixationTime);
        mSessionInfo.mDisplayLabel.setText("");
    }

    public static int ShowTryAgainDialog(){
        dlgTryAgain dlg = new dlgTryAgain();
        dlg.setLocationRelativeTo(mSessionInfo.mParentForm);
        dlg.setVisible(true);
        return dlg.getResponse();
    }
     //Added by preethy on 19-01-2012
     public static int ShowAgainDialog(){
        dlgTryAgain2 tf = new dlgTryAgain2();
        tf.setLocationRelativeTo(mSessionInfo.mParentForm);
        tf.setVisible(true);
        return tf.getResponse();
    }
     /***/


}
