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

import java.awt.Component;
import java.text.DecimalFormat;

/**
 *
 * @author Administrator
 */
public class QuizGrade implements IGrade{
    int mTotal = 0;         //Number of questions the student attempted
    int mRightAnswers = 0;
    int mFirst = 0;
    int mSecond = 0;
    int mThird = 0;
    int m4orMore = 0;
    int mSkipped = 0;
    int mTries = 1;
    QuizResultClass mQuizResult;
    boolean mShowPositiveResponse = false;
    Component mParent;

        // Keeps track of how many tries the user tried on one image.
    private void AddPoints(){
        if(mTries == 1)
            mFirst++;
        else if(mTries == 2)
            mSecond++;
        else if(mTries == 3)
            mThird++;
        else
            m4orMore++;
    }


    @Override
    public void BeginQuestion(){
        mTries = 1;
        mTotal++;
    }
    
    @Override
    public double FinalGrade(){
        if(mTotal == 0)
            return 0;
        else
            return mRightAnswers/(double)mTotal;
    }
    
    @Override
    public String FinalGradeAsPercentString(){
        return String.valueOf(DecimalFormat.getPercentInstance().format(this.FinalGrade()));
    }
    
    public int getTries(){
        return mTries;
    }
        
    @Override
    public QuizResultClass getQuizResult(){
        return mQuizResult;
    }

    @Override
    public int NumberOfRightAnswers(){
        return mRightAnswers;
    }

    public void NextTry(){
        mTries++;
    }

    public void RightAnswer(){
        if(mTries == 1){
            mRightAnswers++;
        }
        AddPoints();
    }
    
    public void ShowPositiveResponse(Component parent){
        mShowPositiveResponse = true;
        mParent = parent;
    }
    
    public void SkipQuestion(){
        mSkipped++;
    }
        
    // Shows the try again dialog box to ask if the user would like to try again.
    public int ShowTryAgainDialog(){
        dlgTryAgain dlg = new dlgTryAgain();
        dlg.setLocationRelativeTo(mParent);
        dlg.setVisible(true);
       return dlg.getResponse();
    }
    
    @Override
    public int TotalQuestions(){
        return mTotal;
    }

    @Override
    public void QuitEarly(){
        if(mTotal > 0)
            mTotal--;
    }
     //Added by preethy on 19-01-2012
     public int ShowAgainDialog(){
        dlgTryAgain2 tf = new dlgTryAgain2();
       // tf.setLocation(260, 250);
        tf.setLocationRelativeTo(mParent);
        tf.setVisible(true);
        return tf.getResponse();
    }

}
