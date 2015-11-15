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
 * ScriptResult.java
 *
 * Created on December 14, 2008, 7:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scripteditor;

import java.io.*;
import java.text.*;
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class ScriptResult {

    /** Creates a new instance of ScriptResult */
    //String userPath = System.getenv("USERPROFILE");
    String mMode;               //Quiz, Test
    String mType;               //ImageVerification, Comparison, Naming.
    String mTaxanomicLevel;     //Family, Genus, Species
    String[] mTaxa;             //
    String mName;
    double Grade;
    int mFirstTry = 0;
    int mSecondTry = 0;
    int mThirdTry = 0;
    int m4orMore = 0;
    int mSkipped = 0;
    String mScriptName;
    String mSessionName;
    int mCompleted;
    int mTotalQuestions;
    double mGradesTotal;
    int mNumGrades = 0;
    String mSessionFileName;
    /** Creates a new instance of Results */
    public ScriptResult(String type, String taxaLevel, String[] taxa, String name, double grade,
            String ScriptName, String SessionName, int completed, int totalQuestions, String sessionFileName) {

        mType = type;
        mTaxanomicLevel = taxaLevel;
        mTaxa = taxa;
        mName = name;
        Grade = grade;
        mScriptName = ScriptName;
        mSessionName = SessionName;
        mCompleted = completed;
        mTotalQuestions = totalQuestions;
        mSessionFileName = sessionFileName;
    }

    private String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }
/*
    private String getYesterday() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());

    }*/

    public boolean writeResults() {

        return writeResults(true);
    }

    public boolean writeResults(boolean ScriptMode) {


        DecimalFormat mformat = new DecimalFormat("#0.00");
     
        PrintWriter diskfile = getPrintWriter(ScriptMode);
        diskfile.print(mName + ",");
        diskfile.print(mScriptName + ",");
        diskfile.print(mSessionName + ",");
        diskfile.print(mType + ",");
        diskfile.print(getCurrentDateString() + ",");
        WriteTaxaHeader(diskfile, 0);
        float percentComplete;
        if(mTotalQuestions != 0){
        percentComplete = mCompleted / (float) mTotalQuestions;
        }
        else{
             percentComplete = 0;
        }
     
        double overAll = (mGradesTotal + (percentComplete * Grade))/(mNumGrades + 1);
        
        mGradesTotal += Grade;
        mGradesTotal = (mGradesTotal / (mNumGrades + 1));
        diskfile.print(mformat.format(100 * percentComplete) + ",");
        
        diskfile.print(mformat.format(100 * Grade) + ",");            //percent Correct.
        diskfile.println(mformat.format(100 * (percentComplete * Grade)));  //OverAll
        
        if (mTaxa != null) {
            for (int i = 1; i < mTaxa.length; i++) {
                diskfile.print(",,,,,");
                WriteTaxaHeader(diskfile, i);
                diskfile.println("");
            }
        }
        if (ScriptMode) {
//            double f = mGradesTotal / mName.length();
//            double code = 100 * ((mGradesTotal - Grade + (percentComplete * Grade))/ mName.length());       //mGradesTotal / mName.length());
//            diskfile.println(",,,,,,,,,," + mformat.format(code) + ",Final Score");
            double finalOverall = 100 * overAll / mName.length();
            diskfile.println(",,,,,,,,,," + mformat.format(finalOverall) + ",Final Score");
        }
        diskfile.close();

        return true;
    }

   // private String CSVFileName(String path, String date, int FileNameNumber){
    //    return path + "/Grades/" + date.replace('/', '-') + "_" + mName + "_" + mScriptName + "_" + String.valueOf(FileNameNumber) + ".csv";
   // }

    private PrintWriter getPrintWriter(boolean ScriptMode) {
      //  String path = Configuration.UserPath();
        String path = Configuration.ApplicationPath();
        FileWriter myFileWriter = null;
        String filename;
        boolean fileExists;

        if (ScriptMode) {
            filename = mSessionFileName;
        } else {
            filename = path + "/Grades/" + mName + ".csv";
        }
        fileExists = (new File(filename)).exists();
        try {
            myFileWriter = new FileWriter(filename, true);
        } catch (IOException ioe) {
        }
        PrintWriter diskfile = new PrintWriter(myFileWriter);
        if (!fileExists) {
            diskfile.println("Username, Script Name, Session, Session Type, Date/Time, Family, Genus, Species/Common Name, % Complete, %Correct, %Overall");
        } else {
            if (ScriptMode) {
                eraseFinalScore(mSessionFileName);
            }
        }

        return diskfile;
    }

    private void eraseFinalScore(String filename) {

        BufferedReader buffer = myFileReader(filename);
        String myLine;
        ArrayList<String> myLines = new ArrayList<String>();
        FileWriter myFileWriter;

        try {
            while ((myLine = buffer.readLine()) != null) {
                myLines.add(myLine);
            }
            buffer.close();
        } catch (Exception e) {
        }

        if (myLines.size() != 0) {
            myLines.remove(myLines.size() - 1);
        }


        try {
            myFileWriter = new FileWriter(filename, false);
            String aLine;
            for (int i = 0; i < myLines.size(); i++) {
                aLine = myLines.get(i);
                String[] fields = aLine.split(",");
                if (fields.length == 11) {
                    String temp = fields[10].replace("%", "");
                    
                    if (temp.trim().compareTo("Overall") != 0) {
                        mGradesTotal += Double.parseDouble(temp)/100;
                        mNumGrades++;
                    }

                }
                myFileWriter.write(aLine + '\n');

            }
            myFileWriter.close();
        } catch (IOException ioe) {
        }
    }
    //
    // TO BE REMOVED  when ScriptGrade Reader/Viewer is implemented.
    //
    private void WriteTaxaHeader(PrintWriter diskfile, int taxa_index) {
       if(mTaxa == null){
           diskfile.print(",,,");
           return;
       }

        mTaxa = formatTaxa(mTaxa);
        String[] taxaSplit = mTaxa[taxa_index].split(" ");
        int k;

        if (this.mTaxanomicLevel.compareTo("Common Name") == 0) {
            diskfile.print(taxaSplit[0] + ",,");// ,, puts Common Name under Species column
            for (k = 1; k < taxaSplit.length; k++) {
                diskfile.print(taxaSplit[k] + " ");
            }
            diskfile.print(",");
        } else if (this.mTaxanomicLevel.compareTo("Family") == 0) {
            for (k = 0; k < taxaSplit.length; k++) {
                diskfile.print(taxaSplit[k]);
                diskfile.print(",");
            }
            diskfile.print(",,"); //Fill Genus and species with empty
        } else if (this.mTaxanomicLevel.compareTo("Genus") == 0) {
            for (k = 0; k < taxaSplit.length; k++) {
                diskfile.print(taxaSplit[k]);
                diskfile.print(",");
            }
            diskfile.print(","); //Fill species with empty
        } else // if at the species level
        {
            for (k = 0; k < taxaSplit.length; k++) {
                diskfile.print(taxaSplit[k]);
                diskfile.print(",");
            }
        }
    }

    private String[] formatTaxa(String[] taxa) {

        String[] splitted;
        String[] Arrtemp = new String[taxa.length];
        String temp = "";

        for (int i = 0; i < taxa.length; i++) {
            splitted = taxa[i].split(" ");
            for (int k = 0; k < splitted.length; k++) {
                if (splitted[k].compareTo("") != 0) {
                    temp = temp + splitted[k] + " ";
                }
            }

            Arrtemp[i] = temp.trim();
            temp = "";
        }

        return Arrtemp;
    }

    private BufferedReader myFileReader(String filepathname) {

        FileReader inFile;
        String path = filepathname;
        try {
            inFile = new FileReader(path);
        } catch (FileNotFoundException ec) {
            return null;
        }

        return new BufferedReader(inFile);
    }
}
