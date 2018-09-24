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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
//javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Administrator
 */
public class Configuration {

    static String mDBPath = "DBPath.txt";
    public static String DataBaseName() {
        return "database.csv";
    }

    public static String DataBaseHashValue() {
       // return "93344572f819b9e5b5fbee43a5ae3698"; //WPSEUS FULL DATABASE
        //return "39f9776bab154dd6d4a7cd9b8e494664"; //WPSEUS REDUCED DATABASE
        String hashfile = "i8087f432.txt";

        BufferedReader _reader = getReader(getDatabaseFolderPath() + "\\" + hashfile);
        String line = "";
        if (_reader != null) {
            //Read contents.
            try {
                line = _reader.readLine();
            } catch (IOException ec) {
                line = "";
            }
            try {
                _reader.close();
            } catch (IOException ec) {

            }
        }
        if (line == null)
            return "";
        else
            return line;

    }

    public static String getDatabaseFolderPath() {
        BufferedReader _reader = getReader(ApplicationPath() + "\\" + mDBPath);//Commented by preethy on 16-03-2012
      // BufferedReader _reader = getReader(UserPath() + "\\" + mDBPath);
        String line = "";
        if (_reader != null) {
            //Read contents.
            try {
                line = _reader.readLine();
            } catch (IOException ec) {
                line = "";
            }
            try {
                _reader.close();
            } catch (IOException ec) {

            }
        }
        if (line == null)
            return "";
        else
            return line;
    }

    private static BufferedReader getReader(String path){
        FileReader inFile = null;
        //String currentDirectory = Configuration.ApplicationPath();
        //String path = currentDirectory + "\\" + _path;
        File checkFile = new File(path);
        if(checkFile.exists() == false)
            return null;
        try {
            inFile = new FileReader(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBaseDriver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new BufferedReader(inFile);

    }
    public static void setDatabaseFolderPath(String path_to_db){
        PrintWriter pw = getPrintWriter(ApplicationPath() + "\\" + mDBPath);
        // PrintWriter pw = getPrintWriter(UserPath() + "\\" + mDBPath);
        try{
             pw.println(path_to_db);
            pw.close();
        } catch (Exception e) {
            String err = e.getMessage();
        }

    }

    private static PrintWriter getPrintWriter(String filename) {
        //String path = Configuration.UserPath();
        FileWriter myFileWriter = null;
        boolean fileExists;

        fileExists = (new File(filename)).exists();
        try {
            myFileWriter = new FileWriter(filename, false);

        } catch (IOException ioe) {

        }
        PrintWriter diskfile = new PrintWriter(myFileWriter);

//        if (!fileExists) {
//            diskfile.println("Username, Script Name, Session, Session Type, Date/Time, Family, Genus, Species/Common Name, % Complete, %Correct, %Overall");
//        } else {
//            if (ScriptMode) {
//                eraseFinalScore(mSessionFileName);
//            }
//        }

        return diskfile;
    }

    public static String ApplicationPath() {
        File myFile = new File("");
        try {
            if(!Utilities.IsWindows()) {
            //if (OSType.compareToIgnoreCase("MAC") == 0) {
                return myFile.getCanonicalPath() + "/..";
            } else {
                return myFile.getCanonicalPath() + "/";
            }
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public static void CreateGradesFolder() {
       // String path = UserPath() + "Grades/";
       String path = ApplicationPath() + "/Grades/";
         boolean success = (new File(path)).mkdirs();
    }

    /*public static String UserPath() {
        //if(OS == 0){
         JFileChooser fr = new JFileChooser();
         javax.swing.filechooser.FileSystemView fw = fr.getFileSystemView();
         return   fw.getDefaultDirectory() + "/" + ApplicationName() + "/";
         //    return System.getenv("USERPROFILE") + "/Documents/" + ApplicationName() + "/";
       // }
       // else if(OS == 1){
       //      return System.getenv("user.home") + "/" + ApplicationName() + "/";
       // }
       // else
        //    return "OS not found";

    }*/

    /*public static String ApplicationName() {
         return "Visual Learning";
    }*/

    // Tell program how many user records to hold before deleteing old records
    // View Progress Records
    public static int ProgressRecords() {
        return 1000;
    }
}
