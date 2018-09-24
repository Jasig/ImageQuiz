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


import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class RandomResponse {


    public static void ShowPositiveResponse(Component mParentForm) {
        Random generator = new Random();
        String msg = "";
        switch (generator.nextInt(9)) {
            case 0:
                msg = "Nice Job!";
                break;
            case 1:
                msg = "Great!";
                break;
            case 2:
                msg = "Correct!";
                break;
            case 3:
                msg = "Affirmative!";
                break;
            case 4:
                msg = "That's it!";
                break;
            case 5:
                msg = "Got It!";
                break;
            case 6:
                msg = "Way to go!";
                break;
            case 7:
                msg = "Good Job!";
                break;
            case 8:
                msg = "Excellent!";
                break;
        }
        Utilities.MessageDialog(mParentForm, msg);
    }

}
