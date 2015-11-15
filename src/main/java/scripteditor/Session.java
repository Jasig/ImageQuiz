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
 * Session.java
 *
 * Created on February 11, 2007, 3:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.EventQueue;
import java.awt.event.*;
/**
 *
 * @author Administrator
 */
public class Session {
    public Component mParent;
    public String[][] mResults;
    public JLabel mPic;
    public int mMode;
    public int mSpellVal;
    public double mDelay;
    public String mTaxLevel;
    public String[] mTaxa;
    public ActionListener myListener;
    public JPanel mPanel;
    public String mUserName = "";
    public SpellingClass mSpelling;
    public ProgressClass user_progress;
    public QuizResultClass cur_result;
    
    /** Creates a new instance of Session */
    public Session(Component parentForm, JLabel displayLabel, String[][] fileNames, int mode, double delay, String taxLevel, String[] taxa, JPanel p, int spell, ActionListener al, String UserName) {
        mResults   = fileNames;
        mPic       = displayLabel;
        mMode      = mode;
        mParent    = parentForm;
        mDelay     = delay;
        mTaxLevel  = taxLevel;
        mTaxa      = taxa;
        myListener = al;
        mPanel     = p;
        mUserName  = UserName;
        mSpellVal = spell;
    }
}
