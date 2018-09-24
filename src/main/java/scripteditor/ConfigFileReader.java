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

import java.io.File;
import java.io.FileInputStream;


public class ConfigFileReader {

    public static String getProjectName()
    {

        String returnString = "Visual Learning"; //default string

        File file = new File(Configuration.ApplicationPath()+File.separator+"Graphics"+File.separator+"name.txt");

        if(file.exists())
        {
            try {
                byte dataArray[];
                FileInputStream fileInputStream = new FileInputStream(file);
                dataArray = new byte[fileInputStream.available()];
                fileInputStream.read(dataArray);
                returnString = new String(dataArray);
            }
            catch (Exception ex) {

            }
        }
        return returnString;
    }

    public static String getWelcomeText()
    {

        String returnString = ""; //default string

        File file = new File(Configuration.ApplicationPath()+File.separator+"Graphics"+File.separator+"welcome.txt");

        if(file.exists())
        {
            try {
                byte dataArray[];
                FileInputStream fileInputStream = new FileInputStream(file);
                dataArray = new byte[fileInputStream.available()];
                fileInputStream.read(dataArray);
                returnString = new String(dataArray);
            }
            catch (Exception ex) {

            }
        }
        return returnString;
    }
}
