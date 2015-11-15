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

import java.text.DecimalFormat;

/**
 *
 * @author Administrator
 */
public class TestGrade implements IGrade{
    int mTotal = 0;         //Number of questions the student attempted
    int mRightAnswers = 0;
   // SessionInfo mSession;
    QuizResultClass mQuizResult;
   // int mSkipped = 0;
   // int mTries = 1;
  //  public TestGrade(){}
    public TestGrade(QuizResultClass QRCInstance){
        mQuizResult = QRCInstance;
    }

    @Override
    public void BeginQuestion(){
       //  mTries = 1;
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
       return 0;
        //     return mTries;
    }

    @Override
    public int NumberOfRightAnswers(){
        return mRightAnswers;
    }

    @Override
    public QuizResultClass getQuizResult(){
        return mQuizResult;
    }
    
    public void NextTry(){
       //   mTries++;
    }
    
    public void RightAnswer(){
         //if(mTries == 1){
        mRightAnswers++;//}
    }
    
    public void SkipQuestion(){
    //mSkipped++;
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
        dlgTryAgain tf = new dlgTryAgain();
        tf.setLocation(260, 250);
        tf.setVisible(true);
        return tf.getResponse();
    }
  /*   public void getSession(SessionInfo session)
    {
        mSession=session;
    }*/
     /**/
}
