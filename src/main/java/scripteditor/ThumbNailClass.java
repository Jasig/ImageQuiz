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
 * ThumbNailClass.java
 *
 * Created on September 12, 2007, 4:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package scripteditor;

import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Ben
 */
public class ThumbNailClass {
    
    private String mDirectory = Configuration.ApplicationPath();;
 //   private String mDirectory = Configuration.UserPath();;
    private int MAXHIEGHT = 100;
    private int MAXWIDTH = 100;
    
 
    /**
     * Creates a new instance of ThumbNailClass
     */
    public ThumbNailClass() {
    }
    
    
    
    // Program must be in same directory as 
   /* public void getThumbnail(String filename){
        Image temp = null;
        Image temp2 = null;
        BufferedImage bi = new BufferedImage(MAXHIEGHT, MAXWIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        
        try {
            temp = ImageIO.read(new File(mDirectory + "/Images/" + filename));
             //return new ImageIcon(temp);
        } catch (IOException ex) {
            //TODO:  Propogate exception back to calling function.
           // return null;
        }  
  
        
        // Resize Image to correct Size     
        //  temp = VerifyImageSize(temp, MAXHIEGHT, MAXWIDTH);
          
        // ImageIcon imgIcon = new ImageIcon(temp);
        // temp = null;
        // temp2 = null;
                 
        //return imgIcon;
    }*/
    
    public ImageIcon tempFunction(String filename){
        double h;
        double w;
        try {
            
            BufferedImage img = ImageIO.read(new File(mDirectory + "/Images/" + filename));
            h = img.getHeight(null);
            w = img.getWidth(null);
            
            if(h > MAXHIEGHT){ 
            w = w * (MAXHIEGHT / h);
            h = MAXHIEGHT;
        }
        if(w > MAXWIDTH){
            h = h * (MAXWIDTH / w);
            w = MAXWIDTH;
        }

            
            Image image1 = img.getScaledInstance((int)w, (int)h, BufferedImage.SCALE_FAST);
            BufferedImage bi = new BufferedImage((int)w,(int)h, BufferedImage.TYPE_INT_RGB);
            Graphics bg = bi.getGraphics();
            bg.drawImage(image1, 0, 0, null);
            bg.dispose();
            ImageIcon ww = new ImageIcon(bi);
            return ww;
            //ImageIO.write(bi, "jpeg", new File(mDirectory + "/Scaled/" + filename));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
        
    }
    
    
   /* private Image ScaleImage(Image image, int width, int height){
        
        double h = image.getHeight(null);
        double w = image.getWidth(null);
        
        if(h > height){ 
            w = w * (height / h);
            h = height;
        }
        if(w > width){
            h = h * (width / w);
            w = width;
        }
        Image ScaledImage = image.getScaledInstance((int)w,(int)h,Image.SCALE_FAST);
        
        return ScaledImage;
    }
    
     private Image VerifyImageSize(Image img, int maxWidth, int maxHeight){
            if(img.getWidth(null) > maxWidth || img.getHeight(null) > maxHeight)
                return ScaleImage(img, maxWidth, maxHeight);        
            else
                return img;
    }*/
    
}
