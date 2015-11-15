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
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author bpurcell
 */
public class Utilities {

    static String OSType = "MAC";      //"MAC"  "PC"

    public static void OpenExternalFile(String path) throws IOException {
        String cmd = "";
        if (OSType.compareToIgnoreCase("MAC") == 0) {
            cmd = "open ".concat(path);
        } else {
            cmd = "cmd /c start ".concat(path);
        }

        if (new File(path).exists() == false) {
            throw new FileNotFoundException();
        }

        //Throws exception
        Runtime.getRuntime().exec(cmd);

    }
}
