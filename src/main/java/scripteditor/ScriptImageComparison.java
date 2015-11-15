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
 * ScriptImageComparison.java
 *
 * Created on December 14, 2008, 7:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Ben
 */
public class ScriptImageComparison extends IQImageComparison {
    String mScriptName;
    String mSessionName;
    String mScriptFileName;
    
    /** Creates a new instance of ScriptImageComparison */
    public ScriptImageComparison(Component parentForm, JLabel displayLabel, String[][] fileNames, double delay, String taxLevel, 
                String[] taxa, JPanel p, ActionListener al, String name, boolean test_mode, ImageCollection ic, ProgressClass pc, 
                String ScriptName, String SessionName, String scriptFileName) {
        super (parentForm, displayLabel, fileNames, delay, taxLevel, taxa, p, al, name, test_mode, ic, pc);
        mScriptName = ScriptName;
        mSessionName = SessionName;
        mScriptFileName = scriptFileName;
    }
    
    @Override
       public void SaveResult(){
        ScriptResult rs;

        rs = new ScriptResult("Image Comparison", mTaxLevel, mTaxa, mUserName, 
                    mGrade.getGradeInstance().FinalGrade(), mScriptName, mSessionName,  mGrade.getGradeInstance().TotalQuestions(),
                    myCollection.NumberOfImages()/ 2, mScriptFileName);
        rs.writeResults();
    }
}
