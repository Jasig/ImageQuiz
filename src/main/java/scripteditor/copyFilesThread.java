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
package scripteditor;


import java.beans.PropertyChangeEvent;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ProgressMonitorInputStream;

public class copyFilesThread extends Thread 
        //implements PropertyChangeListener
{
   Component Parent;
   String source,dest,temp;
   public boolean threadStop=false;
   String destination = "";
   private ProgressMonitor progressMonitor;
   JProgressBar progressBar;
  public boolean cancel=false;
   JFrame theFrame = new JFrame("Copying files");
     Image image=null;
       
  // JButton aJButton = new JButton("Cancel");
  Container contentPane = theFrame.getContentPane();
    private javax.swing.JButton jButton1;
    private javax.swing.JProgressBar jProgressBar1;
    
  public copyFilesThread(Component Parent,String temp)
  {
    this.Parent = Parent;
    this.temp=temp;
    initComponents2();
    try {
             image=ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/icon.jpg"));
         //    image=ImageIO.read(new File(Configuration.UserPath() + "/Graphics/icon.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
         theFrame.setIconImage(image);
          theFrame.setResizable(false);
          theFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
  }
 
  public void run()
  {
   copyThread cpThread;
   Random random = new Random();
   boolean db=false;
   boolean img=false;
   boolean formatdiff=false;
   boolean test=false;
   OutputStream out = null; 
   ZipInputStream in = null;
  // progressMonitor = new ProgressMonitor(Parent,"Copying files",  "", 0, 100);
  // progressBar = new JProgressBar();
    final JProgressBar aJProgressBar = new JProgressBar(0, 100);
    aJProgressBar.setIndeterminate(true);
   String path = Configuration.ApplicationPath();
  //  String path = Configuration.UserPath();
    try {
           in = new ZipInputStream(new FileInputStream(temp));
            ZipEntry entry = null;
           // Get the first entry
            int initsize = 0;
            while ((entry = in.getNextEntry()) != null) {///extracts zip file
            String outFilename = entry.getName();
            if(db==false){
                if(outFilename.equals("Database.csv")){
                    File dirStr = new File(path+"/DataBase"+"/Database.csv");
                    dirStr.delete();//existing database file deleted
                     destination=path+"/DataBase";
                    db=true;
                }
               
            }
             if(img==false){//already existing image folder deleted and creating a new image folder in that place
                 if(outFilename.startsWith("images")){
                  File dirStr = new File(path+"/images");
                  deleteDirectory(dirStr);
                  destination=path;
                   if(!dirStr.exists()){
                  new File(destination, "/images").mkdirs();
                  }
                 img=true;
                 }
            }
            
             if(entry.isDirectory()) {
             new File(destination, outFilename).mkdirs();
              
            } else {
                     
                    try {
                      /* if(progressMonitor.isCanceled())
                         {
                               threadStop = true;
                               File dirStr = new File(path+"/DataBase"+"\\Database.csv");
                               File dirStr2 = new File(path+"/DataBase");
                               if(!dirStr2.exists()){
                                  new File(destination, "DataBase").mkdir();
                               }
                               dirStr.delete();
                               dirStr = new File(path+"\\images");
                             
                               if(!dirStr.exists()){
                                  new File(destination, "images").mkdirs(); 
                               }else{
                               deleteDirectory(dirStr);
                               new File(destination, "images").mkdirs(); 
                              }
                              this.stop();
                         }*/
                        Thread.sleep(10);
                     } catch (InterruptedException ex) {
                        Logger.getLogger(copyFilesThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     initsize++;
                     
                     String message =  String.format("Copying %d files.\n", initsize);
    
                    // progressMonitor.setNote(message);
                    // progressMonitor.setProgress(initsize);  
                    // progressBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    //progressBar.setIndeterminate(true);
                        
                        //theFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                         
                       // contentPane.add(aJProgressBar, BorderLayout.NORTH);
                       // contentPane.add(aJButton, BorderLayout.SOUTH);
                       // theFrame.setSize(300, 100);
                     if(cancel==false){
                        theFrame.setLocationRelativeTo(Parent);
                        theFrame.show();
                        }
                       
                        if(cancel){//if copying is cancelled then any of deleted folders created
                           File dirStr = new File(path+"/DataBase"+"\\Database.csv");
                               File dirStr2 = new File(path+"/DataBase");
                               if(!dirStr2.exists()){
                                  new File(destination, "DataBase").mkdir();
                               }
                                dirStr.delete();
                               dirStr = new File(path+"\\images");
                               if(!dirStr.exists()){
                                  new File(destination, "images").mkdirs(); 
                               }else{
                               deleteDirectory(dirStr);
                               new File(destination, "images").mkdirs(); 
                              }
                            
                            this.stop();
                        }
                    
                     cpThread = new copyThread(Parent,destination,outFilename,in);
                     cpThread.start();
                         try
                            {
                             cpThread.join();
                            }
                         catch(Exception e)
                           {
                             e.printStackTrace();
                           }
                 }
            }
            theFrame.hide();
    }
        catch (IOException ex) {
            Logger.getLogger(copyFilesThread.class.getName()).log(Level.SEVERE, null, ex);
        }       
    finally {
      // Close the stream
      if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(copyFilesThread.class.getName()).log(Level.SEVERE, null, ex);
                }
      }
      if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(copyFilesThread.class.getName()).log(Level.SEVERE, null, ex);
                }
      }
    }
           
  //progressMonitor = new ProgressMonitor(Parent,"Copying files",  "", 0, 100);
  
   }
  

  public class copyThread extends Thread
  {
    Component Parent;
    String source,dest;
    ZipInputStream in2 = null;
    OutputStream out = null;
    String destination = "";
    String fileName = "";

    public copyThread(Component Parent,OutputStream out,ZipInputStream in)
    {
      this.Parent = Parent;
      this.out = out;
      this.in2=in;
    }
    
     public copyThread(Component Parent,String dest, String filename,ZipInputStream in)
    {
      this.Parent = Parent;
      this.destination = dest;
      this.fileName = filename;
      this.in2=in;
    }
     
    public void run()
    {//copying files to destination folder
      int read;
      try
      {
           out = new FileOutputStream(new File(destination,fileName));
          byte[] buf = new byte[1024*4];
          int len;
          
          while ((len = in2.read(buf)) > 0) {
             
             out.write(buf, 0, len);
          }
          out.close();
      }
      catch(Exception e)
      {
        e.printStackTrace();
        //errorCopying = true;
      }
    }
  }
  public void deleteDirectory(File file)throws IOException{

    	if(file.isDirectory()){

    		//directory is empty, then delete it
    		if(file.list().length==0)
                {
     		   file.delete();
     		}else{

    		   //list all the directory contents
        	   String files[] = file.list();

        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);

        	      //recursive delete
        	     deleteDirectory(fileDelete);
        	   }

        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	   }
    		}

    	}else{
    		//if file, then delete it
    		file.delete();
    	}
    }

   private void initComponents2() {
     
       // Container contentPane = theFrame.getContentPane();
     
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();

        theFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //setTitle("Copying files");

        jProgressBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jProgressBar1.setIndeterminate(true);

        jButton1.setText("Cancel");
       jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jButton1)
                .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        
        theFrame.pack();
    } 
      private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
          cancel=true;
         
          theFrame.hide();
          
      }  
  
}


