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
 * SpellingClass.java
 *
 * Created on January 16, 2007, 6:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

/**
 *
 * @author Moz123
 */
public class SpellingClass {
    
    /** Creates a new instance of SpellingClass */
    public SpellingClass() {

    }
    
    public static boolean getPassOrFail(int percentVal, String rightWord, String userWord){
        
        double percent = 100;
        int count;
        
        rightWord = rightWord.toLowerCase().trim();
        userWord = userWord.toLowerCase().trim();
        
        
       if(rightWord.compareTo(userWord) == 0)
           return true;
        
        if(percentVal == 1)
            percent = 90;
        else if(percentVal == 2)
            percent = 80;
         else if(percentVal == 3)
            percent = 70;
         else if(percentVal == 4)
            percent = 60;
         
        count = CheckSpelling(rightWord, userWord);
        int largest = rightWord.length();
        if(userWord.length() > largest)
            largest = userWord.length();
        double per = (double)count/(double)largest;
        per = per * 100;
        
        if(per > percent){
            return true;
        }
        else return false;    
    }
    
     private static int CheckSpelling(String word, String attempt){
        int i = 0;
        int m = 0;
        int matchCount = 0;
        int MaxSearchLength = 3;
        //Find first pair of matching letters.
        //If goest past end, oh well!
        
        if(word.length() == 0 || attempt.length() == 0){
            return 0;
        }
        
        while(i < word.length()){
            if(word.charAt(i) == attempt.charAt(m))
                break;
            m++;
            if(m == attempt.length() || m == MaxSearchLength){
                i++;
                m = 0;
            }
        }
        
        //Get the matching substring beginning at i, m
        if(i < word.length()){
            int j = i + 1;
            int k = m + 1;
            while(j < word.length() && k < attempt.length()){
                if(word.charAt(j) == attempt.charAt(k)){
                    j++;
                    k++;
                } else { break; }
            }
            //Add to list for tester to see.
            //Make recursive call with rest of words.
            if(j > i){
                matchCount = j - i;
                //list1.add(word.substring(i, j));
                if(j < word.length() && k < attempt.length())
                    matchCount += CheckSpelling(word.substring(j), attempt.substring(k));
            }
        }
        return matchCount;
    }
    
}
