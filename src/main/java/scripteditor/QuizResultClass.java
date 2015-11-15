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
 * QuizResultClass.java
 *
 * Created on January 26, 2007, 3:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import java.util.*;
import java.text.*;

/**
 *
 * @author Moz123
 */
public class QuizResultClass {
    
    // Qiuz types: 
    public int mQuizType;
    public String date_time;
    public int numImages;
    public int sensitivity;
    public double displayTime;
    public ArrayList alTaxa = new ArrayList();
    public ArrayList alCorrect = new ArrayList();
    public ArrayList totalViewed = new ArrayList();
    ArrayList mFamilyKeys = new ArrayList();
    ArrayList mSubLevel = new ArrayList();
    ArrayList mScore = new ArrayList();
    
    /** Creates a new instance of QuizResultClass */
    /*  Accumlate response results for an entire Quiz
     *  session.  The full record consists of all three arraylists.
     */
    public QuizResultClass(int type, int numFiles, int spelling, double dTime) {
        mQuizType = type;
        numImages = numFiles;
        sensitivity = spelling;
        displayTime = dTime;
        //DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        java.util.Date date = new java.util.Date();
        date_time = dateFormat.format(date);
    }
        
    public void applyResult(String taxa, boolean res){
        
    /* taxa = taxa name being studied.
     * res = answered correctly or not.
     * 
     * Accumulate response results in the three ArrayList members.
     *  Store results in ArrayLists 1-1.
     *
     *  If alTaxa(taxa) exists, accumulate res.
     *  If alTaxa(taxa) not exists, create new record.
     */
        for(int i = 0; i < alTaxa.size(); i++){
            if(alTaxa.get(i).toString().compareTo(taxa) == 0){
                int total = Integer.parseInt(totalViewed.get(i).toString()) + 1;
                totalViewed.remove(i);
                totalViewed.add(i, Integer.toString(total));
                if(res == true){
                    int correct = Integer.parseInt(alCorrect.get(i).toString()) + 1;
                    alCorrect.remove(i);
                    alCorrect.add(i, Integer.toString(correct));
                }
                return;
            }
        }
        alTaxa.add(taxa);
        totalViewed.add("1");
        if(res == true)
            alCorrect.add("1");
        else
            alCorrect.add("0");
    }  
}
