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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Anurag

 * this class is currently obsolete, ztesler 08/2016
 */
public class PropertyFileReader {

    java.util.Properties properties = new Properties();

    public PropertyFileReader() throws FileNotFoundException, IOException {
    InputStream inputStream = new FileInputStream(Configuration.ApplicationPath()+File.separator+"application.properties");
        properties.load(inputStream);
    }

    public String getPropertyValue(String key)
    {
        if(properties==null){
        }else{
          return properties.getProperty(key);
        }
        return null;
    }


}
