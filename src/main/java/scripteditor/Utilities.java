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

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author bpurcell
 */
public class Utilities {

    //static String OSType = "MAC";      //"MAC"  "PC"

    public static boolean IsWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    public static void OpenExternalFile(String path) throws IOException {
        String cmd[];
        //if (OSType.compareToIgnoreCase("MAC") == 0) {
        if(!IsWindows()) {
            cmd = new String[] {"open", path};
        } else {
            cmd = new String[] {"cmd", "/c", "start \"\" \"" + path + "\""};
        }

        if (new File(path).exists() == false) {
            throw new FileNotFoundException();
        }

        //Throws exception
        Runtime.getRuntime().exec(cmd);

    }

    public static void MessageDialog(Component component, String message) {
        ImageIcon dialogIcon = new ImageIcon(Configuration.ApplicationPath() + "/Graphics/icon.png");
        String appDisplayName = ConfigFileReader.getProjectName();

        JOptionPane.showMessageDialog(component, message, appDisplayName, JOptionPane.INFORMATION_MESSAGE, dialogIcon);
    }

    public static int ConfirmDialog(Component component, String message, String title) {
        ImageIcon dialogIcon = new ImageIcon(Configuration.ApplicationPath() + "/Graphics/icon.png");

        return JOptionPane.showConfirmDialog(component, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, dialogIcon);
    }

}
