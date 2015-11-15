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

/**
 *
 * @author Administrator
 */
public class GradeImageComparisonFactory {

    public static int ImageComparisonTest = 2;
    public static int ImageComparisonQuiz = 3;


    public static IGradeImageComparison GetGradeInstance(int Mode, QuizResultClass QRCInstance,boolean italic) { //italic added by preethy
        if (Mode == ImageComparisonTest) {
            return new GradeImageComparisonTest(QRCInstance);
        } else if (Mode == ImageComparisonQuiz) {
            return new GradeImageComparisonQuiz(QRCInstance,italic);
        } else {
            return null;
        }
        
    }
}
