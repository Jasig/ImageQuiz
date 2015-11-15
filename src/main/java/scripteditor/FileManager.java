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
 * FileManager.java
 *
 * Created on June 4, 2006, 8:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.*;


/**
 *
 * @author Moz123
 */
public class FileManager {
    
    String mResult;
    Results myObjectResult;
    
    /** Creates a new instance of FileManager */
    public FileManager() {
    }
    
   /* public String getResults(String fileName){
        FileReader myFileReader;
        BufferedReader inputFile;
    
         try{
            myFileReader = new FileReader(fileName);
            inputFile = new BufferedReader (myFileReader);
          mResult = inputFile.readLine();
            inputFile.close();
        } catch (IOException ioe){
            return null;
        }
    
        return mResult;
    }
    */
    public Results getResults(String fileName) throws IOException{
        FileInputStream myInput;
        Object anyObject = null;
        
        myInput = new FileInputStream(fileName);
        ObjectInputStream myObjectIn = new ObjectInputStream(myInput);
        
        try{
            anyObject = myObjectIn.readObject();
            
        } catch (ClassNotFoundException ioe){
            return null;
            
        }
        
        myObjectIn.close();
        return (Results)anyObject;
    }
    
    public void writeInfo(String temp, String fileName){
        
        FileWriter myFileWriter;
        try{
            myFileWriter = new FileWriter(fileName);
            myFileWriter.write(temp);
            myFileWriter.close();
        } catch (IOException ioe){
            return;
        }
        
    }
    public void writeInfo(Results myResult, String filename){
        FileOutputStream myOut;
        try{
            myOut = new FileOutputStream(filename);
            ObjectOutputStream myObjectOut = new ObjectOutputStream(myOut);
            //myResult.mName = "First Result";
            myObjectOut.writeObject(myResult);
            myOut.close();
        } catch (IOException ioe){
            return;
        }
    }
}
