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

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Administrator
 */
public class ImageScaler {

    
//    public ImageScaler() {
//
//    }

    public static Image VerifyImageSize(Image img, int maxWidth, int maxHeight, JLabel mDisplayLabel) {
        if (img.getWidth(null) > mDisplayLabel.getWidth() || img.getHeight(null) > mDisplayLabel.getHeight()) {
           
        double h = img.getHeight(null);
        double w = img.getWidth(null);

        if (h > maxHeight) {
            w = w * (maxHeight / h);
            h = maxHeight;
        }
        if (w > maxWidth) {
            h = h * (maxWidth / w);
            w = maxWidth;
        }
        Image ScaledImage = img.getScaledInstance((int) w, (int) h, Image.SCALE_FAST);
        return ScaledImage;
        } else {
            return img;
        }
    }

}
