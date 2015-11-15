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
public class GradeImageComparisonQuiz extends scripteditor.QuizGrade implements IGradeImageComparison {
  boolean Running = true;
  boolean fontItalic=false;
    public GradeImageComparisonQuiz(QuizResultClass QRCInstance,boolean italic){
        mQuizResult = QRCInstance;
        fontItalic=italic;
    }

    @Override
    public IGrade getGradeInstance(){
        return (IGrade)this;
    }

    @Override
    public boolean Grade(String answer, String leftAnswer, String rightAnswer,String taxaLevel) {
   
    boolean both=false;
        if ((answer.trim().toLowerCase().compareTo("y") == 0) && leftAnswer.compareTo(rightAnswer) == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(leftAnswer, true);
                this.getQuizResult().applyResult(rightAnswer, true); // Adding second image Correct !!!!
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if ((answer.trim().toLowerCase().compareTo("yes") == 0) && leftAnswer.compareTo(rightAnswer) == 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(leftAnswer, true);
                this.getQuizResult().applyResult(rightAnswer, true); // Adding second image Correct !!!!
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if ((answer.trim().toLowerCase().compareTo("n") == 0) && leftAnswer.compareTo(rightAnswer) != 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(leftAnswer, true);
                this.getQuizResult().applyResult(rightAnswer, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else if ((answer.trim().toLowerCase().compareTo("no") == 0) && leftAnswer.compareTo(rightAnswer) != 0) {
            if (mTries == 1) {
                this.getQuizResult().applyResult(leftAnswer, true);
                this.getQuizResult().applyResult(rightAnswer, true);
                this.RightAnswer();
            }
            RandomResponse.ShowPositiveResponse(mParent);
        } else {
            //if(leftAnswer.compareTo(rightAnswer) != 0){// Fail both if they are not equal
            // Always Fail both (NEW)
            if (mTries == 1) {
                this.getQuizResult().applyResult(leftAnswer, false);
                this.getQuizResult().applyResult(rightAnswer, false);
            }
                //Commented by preethy on 19-01-2012   
         /*   if (IQImageComparison.ShowTryAgainDialog() != JOptionPane.OK_OPTION) {
                this.SkipQuestion();
            } else {
                
                this.NextTry();
                return false;   //Repeat the question.
            }*/
            //Added by preethy on 19-01-2012
             int resp=IQImageComparison.ShowAgainDialog();
             if (resp == JOptionPane.NO_OPTION) {
                 
                this.SkipQuestion();
                
            } else  if (resp == JOptionPane.YES_OPTION){
                String ans;String leftAns;String rightAns;
                 
                 if(taxaLevel.equals("Species"))
                 {
                   leftAns=leftAnswer.split(" ")[1]+ " "+leftAnswer.split(" ")[2];
                   rightAns=rightAnswer.split(" ")[1]+ " "+rightAnswer.split(" ")[2];
                 }else{
                     //leftAns=leftAnswer;
                    // rightAns=rightAnswer;
                     if(taxaLevel.equals("Genus") ||(taxaLevel.equals("Common Name"))){
                        //leftAns=leftAnswer.split(" ")[1];
                        //rightAns=rightAnswer.split(" ")[1];  
                         leftAns=leftAnswer.substring(leftAnswer.split(" ")[0].length()+1);
                         rightAns=rightAnswer.substring(rightAnswer.split(" ")[0].length()+1);
                     }
                     else{
                        leftAns=leftAnswer;
                        rightAns=rightAnswer; 
                     }
                     
                 }
               
              /*  if(leftAnswer.equals(rightAnswer)){
                    ans=leftAnswer;
                }*/
               // else{ 
                ans=leftAns+","+rightAns;
                both=true;//This flag is used to check whether the two answers are different or not.
              //  }
                 correct cr=new correct(fontItalic,both);
                           cr.Answer(ans);
                                      
                         //  cr.setLocation(260, 250);
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
     /**/
    
}
