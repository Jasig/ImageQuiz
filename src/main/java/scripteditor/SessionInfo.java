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
 * SessionInfo.java
 *
 * Created on January 6, 2009, 7:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Ben
 */
public class SessionInfo {
    
    public Component mParentForm;
    public JLabel mDisplayLabel;
    public String[][] mFileNames;
    public double mDelay;
    public String mTaxaLevel;
    public String[] mTaxa;
    public JPanel mPanel;
    public ActionListener mCallback;
    public String mUserName;
    public boolean mIsTest;
    public ImageCollection mImageCollection;
    public double mFixationTime;
    public ProgressClass mProgress;
   //  public ProgressClass mProgress;
    public String mScriptName;
    public String mSessionName;
    public String mScriptFileName; // FileName of the results file.
    public static int SpellingValue;

    /** Creates a new instance of SessionInfo */
    public SessionInfo() {
    }
    
    public SessionInfo(Component parentForm, JLabel displayLabel, String[][] fileNames, double delay, String taxLevel, String[] taxa, JPanel p, ActionListener al, String user_name, boolean isTest, ImageCollection ic, double fixationTime, ProgressClass pc) {
        mParentForm = parentForm;
        mDisplayLabel = displayLabel;
        mFileNames = fileNames;
        mDelay = delay;
        mTaxaLevel = taxLevel;
        mTaxa = taxa;
        mPanel = p;
        mCallback = al;
        mUserName = user_name;
        mIsTest = isTest;
        mImageCollection = ic;
        mFixationTime = fixationTime;
        mProgress = pc;
    }
    public SessionInfo(Component parentForm, JLabel displayLabel, String[][] fileNames, double delay, String taxLevel, String[] taxa, JPanel p, ActionListener al, String user_name, boolean isTest, ImageCollection ic, double fixationTime, ProgressClass pc, String scriptName, String sessionName, String scriptFileName) {
        mParentForm = parentForm;
        mDisplayLabel = displayLabel;
        mFileNames = fileNames;
        mDelay = delay;
        mTaxaLevel = taxLevel;
        mTaxa = taxa;
        mPanel = p;
        mCallback = al;
        mUserName = user_name;
        mIsTest = isTest;
        mImageCollection = ic;
        mFixationTime = fixationTime;
        mProgress = pc;
        mScriptName = scriptName;
        mSessionName = sessionName;
        mScriptFileName = scriptFileName;
    }
}

