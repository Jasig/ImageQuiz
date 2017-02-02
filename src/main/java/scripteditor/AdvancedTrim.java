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
 * Utility class for trimming Strings.
 */
public class AdvancedTrim {

    /**
     * Returns a trimmed copy of a String containing (potentially among other characters) zero or
     * more sequences of one or more space characters, such that those sequences instead each
     * have exactly one space character.
     *
     * Returns unchanged copies of Strings containing no sequences of multiple space characters.
     *
     * @param temp non-null String
     * @return a copy of temp, except trimming multiple trailing spaces down to just one.
     * @throws NullPointerException if temp is null
     */
    public static String trim(String temp) {
        String ret = "";
        int i = 0;
        while (i < temp.length()) {
            char ch = temp.charAt(i);
            if (ch == ' ') {
                ret += ' ';

                while (temp.charAt(i) == ' ') {
                    i++;
                    if (i == temp.length()) {
                        break;
                    }
                }
            } else {
                ret += ch;
                i++;
            }
        }

        return ret;
    }
}
