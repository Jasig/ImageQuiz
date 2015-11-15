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
 * UserClass.java
 *
 * Created on June 7, 2006, 9:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;


import javax.swing.*;
import java.awt.*;
import java.security.*;
import java.io.*;
import java.lang.String;



/**
 *
 * @author Moz123
 */
public class UserClass {
    
    //Advanced options.
    //String userPath = System.getenv("USERPROFILE");
    public boolean advanced_options_loaded = false;
    public boolean showProgressQuiz;
    public boolean showProgressTest;
    public int spelling_val;
    public double fixation_time;
    public int max_images;
    /** Creates a new instance of UserClass */
    public UserClass() {
            
    }
    
    public String login(String id, String nada){//char[] password){ //Will return FileNotFound, CantReadLine, AccessGranted, or AccessDenied.
        
        char[] password = nada.toCharArray();
        FileReader myReader;
      //  String path = Configuration.UserPath();
        String path = Configuration.ApplicationPath();
        try{
            myReader = new FileReader(path + "/UserFiles/" + id + ".csv");
        } catch(FileNotFoundException fnf){
            
            return "FileNotFound";
            
        }
        String cPassword = "";
        for(int i = 0; i < password.length; i++){
            cPassword = cPassword + password[i];    
        }
        
//        String hashVal = SHAHash(cPassword); //hashVal =  Currently passed in password in SHA form.
        BufferedReader inputfile = new BufferedReader(myReader);
        String firstLine;
        try{
            firstLine = inputfile.readLine();
        } catch(IOException ioe){
            return "CantReadLine"; // If there is nothing in the file on first line.
        }
        
        int indexOfSemiColon;
        indexOfSemiColon = firstLine.lastIndexOf(';');
        firstLine = firstLine.trim();
        String temp = firstLine.substring(indexOfSemiColon + 1);
        temp = temp.substring(0, temp.length()-1);
        //if(hashVal.compareTo(temp) != 0)
            //return "AccessDenied";

        String options = "";
        try {
            options = inputfile.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "AccessGranted";
            //Apparently, there's no more to the file.
        }
        
        //For some reason, there's a comma at the end of the line.
        if(options == null){
            try {
                
                inputfile.close();
                return "AccessGranted";
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
            
        if(options.charAt(options.length()-1)==',')
            options = options.substring(0, options.length() - 1);
        
        ///////////////////////////////////////////////////////////////
        // Code for loading AdvancedOptions
    if(options.substring(0, 17).compareTo("Advanced Options:") == 0){
            int s = 17;
            indexOfSemiColon = options.indexOf(';', 0);
            showProgressQuiz = Boolean.parseBoolean(options.substring(s, indexOfSemiColon));
            s = indexOfSemiColon + 1;
            indexOfSemiColon = options.indexOf(';', s);
            showProgressTest = Boolean.parseBoolean(options.substring(s, indexOfSemiColon));
            s = indexOfSemiColon+ 1;
            indexOfSemiColon = options.indexOf(';', s);
            spelling_val = Integer.parseInt(options.substring(s, indexOfSemiColon));
            s = indexOfSemiColon+ 1;
            indexOfSemiColon = options.indexOf(';', s);
            fixation_time = Double.parseDouble(options.substring(s, indexOfSemiColon));
            s = indexOfSemiColon + 1;
            max_images = Integer.parseInt(options.substring(s, options.length()));
            advanced_options_loaded = true;
            ///////////////////////////////////////////////////////////
        }
        
        try {
            
            inputfile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "AccessGranted";
        
        
    }
    
    public String createNewUser(String id, String nada) {//char[] password){// Returns CanNotCreateUser if there was an error, UserCreated if successful
        // OR UserAlreadyExists if there is already a user named id.
        char[] password = nada.toCharArray();
        FileWriter myFileWriter = null;
        File myFile = new File(".");
       // String path = Configuration.UserPath();
        String path = Configuration.ApplicationPath();
        //// CREATE DIRECTORIES !!!
        
        String strManyDirectories = path + "/UserFiles/";
        boolean success = (new File(strManyDirectories)).mkdirs();

       // if (success == false) {
        //    return "CanNotCreateUser";
       // }
        String filename = path + "/UserFiles/" + id + ".csv";

        File test = new File(filename);
        String cPassword = "";
        for (int i = 0; i < password.length; i++) {
            cPassword = cPassword + password[i];
        }
        String hashVal = SHAHash(cPassword);
        if (test.exists()) {
            return "UserAlreadyExists";
        }
        if (hashVal.compareTo("CouldNotHash") == 0)  {
            return "CanNotCreateUser";
        }

        try {
            myFileWriter = new FileWriter(filename);
        } catch (IOException ioe) {
            return "CanNotCreateUser";
        }

        PrintWriter diskfile = new PrintWriter(myFileWriter);
        diskfile.println(id + ";" + hashVal + "1"); // 1 is a flag for the help Popup
        // Only Print this line for Project #1
        //diskfile.println("Mode,Type,TaxaLevel,Family,Genus,Species,Subspecies,Delay1,Delay2,1stTry,2ndTry,3rdTry,4orMoreTrys,Skipped,Grade");
        diskfile.close();
        return "UserCreated";

    }
    
    private static String getString( byte[] bytes ) {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<bytes.length; i++ ) {
            byte b = bytes[ i ];
            sb.append( ( int )( 0x00FF & b ) );
            if( i+1 <bytes.length ) {
                sb.append( "-" );
            }
        }
        return sb.toString();
    }
    
    private String SHAHash(String pass){ // Pass this a String and it will convert it to a SHA value
        String md5Pass;
        MessageDigest md;
        try {
            md =  MessageDigest.getInstance("SHA");
            byte[] result = md.digest(pass.getBytes());
            md5Pass =  getString(result);
            return md5Pass;
            
        } catch (NoSuchAlgorithmException cnse) {
            return "CouldNotHash";
        }
        
    }
    
  
}



