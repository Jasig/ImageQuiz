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
import java.security.*;
import java.io.*;
/**
 *
 * @author Administrator
 */
public class VerifyDatabase {
             
    String mDataBaseName;
    String mHashValue;
    boolean result;
    
    public VerifyDatabase(String CSV_FileName, String hashValue){
        mDataBaseName = CSV_FileName;
        mHashValue = hashValue;
        result = verifyDatabase();
    }
    
    public boolean IsOK(){
        return result;
    }
    
      private boolean verifyDatabase(){
          // Test to see if Database is valid!!
          //NOTE: don't call this function unless database exists!

          //TODO: remove commented code.
          //TODO: read hash value and verify database.

        //File myFile = new File(".");
       // String path = Configuration.ApplicationPath();
        String temp = "";
        File theFile = new File(Configuration.ApplicationPath() +"/DataBase/" + mDataBaseName);
      //  File theFile = new File(Configuration.UserPath() +"\\DataBase\\" + mDataBaseName);
        //File theFile = new File("C:\\WoodyTreesProgram\\Program Layout\\DataBase\\SEtrees.csv");
        byte[] theTextToDigestAsBytes = null;
        try {
            theTextToDigestAsBytes = getBytes(theFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            try {
                md.update( theTextToDigestAsBytes );
            } finally {
            }
             byte[] digest = md.digest();
              for (int i = 0; i < digest.length; i++)
            {
             temp = temp + Integer.toHexString( digest[i] & 0xff );
            }
             
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
        if(temp.compareTo(mHashValue) == 0){
            return true;  
        }
        else
            return false;
      
    }
       
    // This returns the bytes of a file to validate the database file..
    private byte[] getBytes( File file )
        throws Exception
     {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         InputStream stream = new FileInputStream( file );
         byte buf[] = new byte[1024];
         int len = 0;
 
         while ( ( len = stream.read( buf, 0, 1024 ) ) != -1 )
         {
             baos.write( buf, 0, len );
         }
         try
         {
             stream.close();
         }
         catch ( IOException e )
         {
             // Nothing to do
         }
         return baos.toByteArray();
}
 

}
