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
 * ScriptImageVerification.java
 *
 * Created on January 6, 2009, 7:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

/**
 *
 * @author Ben
 */
public class ScriptImageVerification extends IQImageVerification {
    String mScriptName;
    String mSessionName;
    String mScriptFileName;
    boolean italic=false;
    /** Creates a new instance of ScriptImageVerification */
    public ScriptImageVerification(SessionInfo session,boolean fontItalic) {
        super(session,fontItalic);
        mScriptName = session.mScriptName;
        mSessionName = session.mSessionName;
        mScriptFileName = session.mScriptFileName;
    }
    
    @Override
    public void SaveResult(){
        ScriptResult rs;
        rs = new ScriptResult("Image Verification", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, 
                    mGrade.getGradeInstance().FinalGrade(), mScriptName, mSessionName, mGrade.getGradeInstance().TotalQuestions(),
                    mSessionInfo.mImageCollection.NumberOfImages(), mScriptFileName);
        rs.writeResults();
    }
}
