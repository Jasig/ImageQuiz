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
 * Results.java
 *
 * Created on June 4, 2006, 8:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.io.*;
import java.text.*;
/**
 *
 * @author Moz123
 */
public class Results{
    //String userPath = System.getenv("USERPROFILE");
    String mMode;
    String mType;
    String mTaxanomicLevel;
    double mDelayTime1 = -1;
    double mDelayTime2 = -1;
    String[] mTaxa;
    String mName;
    float Grade;
    int mFirstTry = 0;
    int mSecondTry = 0;
    int mThirdTry = 0;
    int m4orMore = 0;
    int mSkipped = 0;
    /** Creates a new instance of Results */
    public Results(String mode, String type, String taxaLevel, String[] taxa, String name, float grade, double delay1, double delay2) {
        
        mMode = mode;
        mType = type;
        mTaxanomicLevel = taxaLevel;
        mDelayTime1 = delay1;
        mDelayTime2 = delay2;
        mTaxa = taxa;
        mName = name;
       // Grade = grade;
    }
     public Results(String mode, String type, String taxaLevel, String[] taxa, String name, float grade, double delay1) {
        
         mMode = mode;
         mType = type;
         mTaxanomicLevel = taxaLevel;
         mDelayTime1 = delay1;
         mTaxa = taxa;
         mName = name;
        // Grade = grade;
    }
     //Quiz Constructor
      public Results(String mode, String type, String taxaLevel, String[] taxa, String name, int first, int second, int third, int fourOrMore, int skipped) {
        
          mMode = mode;
          mType = type;
          mTaxanomicLevel = taxaLevel;
          mTaxa = taxa;
          mName = name;
          mFirstTry = first;
          mSecondTry = second;
          mThirdTry = third;
          m4orMore = fourOrMore;
          mSkipped = skipped;
    }
     
     public boolean writeTestResults(){
         
        PrintWriter diskfile = getPrintWriter();
        diskfile.print(getCurrentDateString() + ",");
        diskfile.print(mMode + ",");
        diskfile.print(mType + ",");
        diskfile.print(mTaxanomicLevel + ",");
        
        WriteTaxaHeader(diskfile, 0);
        
        //diskfile.print(mDelayTime1 + ",");
        //if(mDelayTime2 != -1){
        //diskfile.print(mDelayTime2 + ",");
        //}
        
        // Do not apply during Test Phase
//        diskfile.print("n/a,"); // 1st Try
//        diskfile.print("n/a,"); // 2nd try
//        diskfile.print("n/a,"); // 3rd try
//        diskfile.print("n/a,"); // 4th or more
//        diskfile.print("n/a,"); // Skipped
        ////////////////////////////////////
        
        diskfile.println(DecimalFormat.getPercentInstance().format(Grade));
        for(int i = 1; i < mTaxa.length; i++){
            diskfile.print(",,,");
            WriteTaxa(diskfile, i);
        }
        diskfile.close();
         return true;
     }
    
     private PrintWriter getPrintWriter(){
                  
        FileWriter myFileWriter = null;
       // String path = Configuration.UserPath();
        String path = Configuration.ApplicationPath();
         String filename = path + "/Grades/" + mName + ".csv";

        try{
            myFileWriter = new FileWriter(filename, true);
        } catch(IOException ioe){
            
        }
        PrintWriter diskfile = new PrintWriter(myFileWriter);
        return diskfile;
     }
     
     private void WriteTaxaHeader(PrintWriter diskfile, int taxa_index){
            String[] taxaSplit = mTaxa[taxa_index].split(" ");
            int k;
            for( k = 0 ; k < taxaSplit.length; k++){
                diskfile.print(taxaSplit[k]);
                if(taxaSplit[k].compareTo("ssp.") == 0)
                    diskfile.print(" ");
                else
                    diskfile.print(",");
            }
            
            while(k < 4){
                diskfile.print(",");
                k++;
            }
     }
     private void WriteTaxa(PrintWriter diskfile, int taxa_index){
         WriteTaxaHeader(diskfile, taxa_index);
         diskfile.println("");
     }
     
     public boolean writeQuizResults(){
         
        PrintWriter diskfile = getPrintWriter();
        diskfile.print(getCurrentDateString() + ",");
        diskfile.print(mMode + ",");
        diskfile.print(mType + ",");
        diskfile.print(mTaxanomicLevel + ",");
        
        WriteTaxaHeader(diskfile, 0);

//        diskfile.print("n/a,"); // delay 1
//        diskfile.print("n/a,"); // delay 2
//        diskfile.print(mFirstTry + ",");
//        diskfile.print(mSecondTry + ",");
//        diskfile.print(mThirdTry + ",");
//        diskfile.print(m4orMore + ",");
//        diskfile.print(mSkipped + ",");
//       diskfile.println("n/a"); // Grade
        
          for(int i = 1; i < mTaxa.length; i++){
            diskfile.print(",,,");
            WriteTaxa(diskfile, i);
        }
        
        diskfile.close();

         return true;
     }

      private String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH:mm:ss
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }
}
