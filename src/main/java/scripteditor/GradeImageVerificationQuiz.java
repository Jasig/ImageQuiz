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

import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class GradeImageVerificationQuiz extends QuizGrade implements IGradeImageVerification{
    boolean Running = true;//Added by Preethy on 19-01-2012
    boolean fontItalic=false; //Added by preethy 
    public GradeImageVerificationQuiz(QuizResultClass QRCInstance,boolean italic){
            mQuizResult = QRCInstance;
            fontItalic=italic;
    }

    @Override
    public IGrade getGradeInstance(){
        return (IGrade)this;
    }

    @Override
    public boolean Grade(String answer, String realName, String randomName, String actualName) {
        //If user wants to repeat, return false.
        //Return true to go to the next question.
               
        String tempRealName = realName.trim().toLowerCase();
        String tempAnswer = answer.trim().toLowerCase();
        String random=randomName.trim().toLowerCase();//Added by Preethy on 19-01-2012
        if (tempRealName.compareTo(randomName.trim().toLowerCase()) == 0 && tempAnswer.compareTo("y") == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(actualName, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if (tempRealName.compareTo(randomName.trim().toLowerCase()) == 0 && tempAnswer.compareTo("yes") == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(actualName, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if (tempRealName.compareTo(randomName.trim().toLowerCase()) != 0 && tempAnswer.compareTo("n") == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(actualName, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if (tempRealName.compareTo(randomName.trim().toLowerCase()) != 0 && tempAnswer.compareTo("no") == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(actualName, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else { // Incorrect answer
            if(mTries == 1){
              this.getQuizResult().applyResult(actualName, false); // Even If they retry Count one wrong  
            }
            //Commented by preethy on 19-01-2012
          /*  if (IQImageVerification.ShowTryAgainDialog() != JOptionPane.OK_OPTION) {
                this.SkipQuestion();
            } else {
                this.NextTry();
                return false;
            }*/
               //Added by preethy on 19-01-2012
             int resp=IQImageComparison.ShowAgainDialog();
             if (resp == JOptionPane.NO_OPTION) {
                this.SkipQuestion();
            } else  if (resp == JOptionPane.YES_OPTION){
                String ans;
                if(tempRealName.equals(random)){
                    ans=realName;
                 }
                else
                    ans=realName;
                    
                 correct cr=new correct(fontItalic);
                 cr.Answer(ans);
                     
                // cr.setLocation(260, 250);
                 cr.setLocationRelativeTo(mParent);
                 cr.setVisible(true);
                 JCDelay(1);
                 cr.setVisible(false);
                
                 
                this.NextTry();
                
               
                return false;   //Repeat the question.
            }
            else if(resp ==2){
                this.NextTry();
                return false;
            }
             //***//
        }
        return true;
    }
     //Added by preethy on 19-01-2012
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
