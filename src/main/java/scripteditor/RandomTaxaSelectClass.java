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
 * RandomTaxaSelectClass.java
 *
 * Created on February 5, 2007, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.util.Collections;
import java.util.Random;
import java.util.*;
/**
 *
 * @author Moz123
 */
public class RandomTaxaSelectClass {
    
    Random myRandom = new Random();
    DataBaseDriver db;
    String mTaxaLevel;
    int mNumberTaxa;
   // int mNumberImages;
    boolean mCommonNames;
    String[] mTaxa;
    
    
    
    /** Creates a new instance of RandomTaxaSelectClass */
    public RandomTaxaSelectClass(String tLevel, int numTaxa, boolean commonNames, DataBaseDriver db_driver) {
        db = db_driver;
        mTaxaLevel = tLevel;
        mNumberTaxa = numTaxa;
    //    mNumberImages = numImages;
        mCommonNames = commonNames;
        makeRandomTaxa();
        
    }
    private void makeRandomTaxa(){
        
        ArrayList myAList = new ArrayList();
        String[] myTaxa = db.getTaxa("Family"); // Gets All Family names
        String[] randomTaxa = new String[mNumberTaxa];
        String[] taxaWithGenus;
        int count = myTaxa.length;
        int tempInt;
        
        // Get the random taxa only if it is Family
        // Get all the family names and go to Genus level
        if(mTaxaLevel.compareTo("Family") == 0){
            tempInt = myRandom.nextInt(count);
            randomTaxa[0] = myTaxa[tempInt];
            int i = 1;
            
            while(i < mNumberTaxa){
                tempInt = myRandom.nextInt(count);
                // If the taxa is not already in the list Add IT.
                if(exists(randomTaxa, myTaxa[tempInt]) == false){  
                    randomTaxa[i] = myTaxa[tempInt];
                    i++;
                }
            }
            mTaxa = randomTaxa;
            return;   
        }
        
            //////////////////////////////////////////////  OK to here
        
      
           
          // Level is Genus OR Species
        
            for(int j = 0; j < myTaxa.length; j++){
              
                    taxaWithGenus = db.getGenus(myTaxa[j]); // is all Genus for the One family passed in
                
                for(int k = 0; k < taxaWithGenus.length; k++){
                    taxaWithGenus[k] = myTaxa[j] + " " + taxaWithGenus[k]; // Adding family name to genus eg. Aceran Acer
                    myAList.add(taxaWithGenus[k]);
                }
                
            }
            
        
        
            // MUST randomly choose taxa with genus according to mNumberTaxa
            if(mTaxaLevel.compareTo("Genus") == 0){
                 count = myAList.size();
                 tempInt = myRandom.nextInt(count);
                 randomTaxa[0] = myAList.get(tempInt).toString();
                 int i = 1;
            
                 while(i < mNumberTaxa){
                
                     tempInt = myRandom.nextInt(count);
                     // If the taxa is not already in the list Add IT.
                     if(exists(randomTaxa, myAList.get(tempInt).toString()) == false){
                         randomTaxa[i] = myAList.get(tempInt).toString();
                         i++;
                     }
            }
                 mTaxa = randomTaxa;
                 return;
                 
            }
        
        
            //////////////////////  OK to HERE
            String[] fullList = new String[myAList.size()];
            for( int g = 0; g < myAList.size(); g++){        
                fullList[g] = myAList.get(g).toString();    
            }
        
            
            
        if(mTaxaLevel.compareTo("Species") == 0 || mTaxaLevel.compareTo("Common Name") == 0){
            // Add Species to Family Name and Genus here!!!
            ArrayList mySpecies = new ArrayList();
            for(int j = 0; j < fullList.length; j++){
                
                if(mTaxaLevel.compareTo("Species") == 0){
                taxaWithGenus = db.getSpecies(fullList[j].substring(fullList[j].lastIndexOf(" ") + 1));
                }
                else
                    taxaWithGenus = db.getCommonName(fullList[j].substring(fullList[j].lastIndexOf(" ") + 1));
                for(int k = 0; k < taxaWithGenus.length; k++){
                    if(mTaxaLevel.compareTo("Species") == 0){
                            taxaWithGenus[k] = fullList[j] + " " + taxaWithGenus[k]; // Adding family name and genus to species eg. Aceran Acer BLABLA
                    }
                    else // Common Names Only in the Else Statement
                    {
                        String temp = "";
                        temp = fullList[j].substring(0, fullList[j].indexOf(" "));
                        taxaWithGenus[k] = temp + " " + taxaWithGenus[k];

                    }
                    mySpecies.add(taxaWithGenus[k]);
                }
            }
            
            
            count = mySpecies.size();
            tempInt = myRandom.nextInt(count);
            randomTaxa[0] = mySpecies.get(tempInt).toString();
            int i = 1;
            while(i < mNumberTaxa){
                
                tempInt = myRandom.nextInt(count);
                // If the taxa is not already in the list Add IT.
                if(exists(randomTaxa, mySpecies.get(tempInt).toString()) == false){
                    randomTaxa[i] = mySpecies.get(tempInt).toString();
                    i++;
                }
            }
            
        } 
      
        mTaxa = randomTaxa;
    }
    private boolean exists(String[] myIntArray, String value){
        
        for(int i = 0; i < myIntArray.length; i++){
            if(myIntArray[i] == value){
                return true;
            }
        }
        return false;
    }
    public String[] getTaxa(){     
        return mTaxa;
    }
    
}

