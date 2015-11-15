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
 * ProgressClass.java
 *
 * Created on January 17, 2007, 2:10 PM
 */

package scripteditor;

import java.util.*;
import java.util.Collections;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author  Moz123
 */
public class ProgressClass1 extends javax.swing.JDialog {
    
    ArrayList myResults = new ArrayList();
    String results;
    boolean controlPressed;
    
    /** Creates new form ProgressClass */
    
    public ProgressClass1(){
        this.initComponents();

        // Code to listen for Ctrl+p/P and launch print window
        controlPressed = false;
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    controlPressed = false;
                }
                else if ((e.getKeyCode() == 80 || e.getKeyCode() == 118) && controlPressed == true) {
                    try {
                        boolean print = taResultWindow.print();
                    } catch (PrinterException ex) {
                        Logger.getLogger(ProgressClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    controlPressed = true;
                }
            }
        });
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.jButton1.setBounds(this.getWidth() / 2 - (62/2), this.getHeight()-100, 62, 35);
        this.printButton.setBounds(this.getWidth() - 62*2, this.getHeight()-100, 75, 35);
        //this.jButton1.requestFocus(); (commented out so window listener can work for ctrl+p)
    }
    
    public void ShowResults(){ 
        for(int i = 0; i < myResults.size(); i++){
            results += (String)myResults.get(i);
        }
       // this.jTextArea1.setText(results);
       

        taResultWindow.setText(results);
        taResultWindow.setCaretPosition(0);
    }
    
    public void setResults(ArrayList temp){ // will set the results at beginning of program

        myResults = temp;
 
    }
    public ArrayList getProgressArray(){
      
        return myResults;
    }
    
    public void StoreResult(QuizResultClass mResult, String grade,boolean fontItalic){
        if(myResults.size() == Configuration.ProgressRecords()){
            myResults.remove(Configuration.ProgressRecords() - 1);
        }
        String header = "Session type: " + SessionTypeString(mResult.mQuizType);
        header += '\n' + mResult.date_time + "\n\n";
        Map mFamilyKeys = new HashMap();
        for(int i = 0; i < mResult.alTaxa.size(); i++){
            String tx = mResult.alTaxa.get(i).toString().trim();
            String fam;
            String sub_level;
            try {
                fam = tx.substring(0, tx.indexOf(" "));
                sub_level = tx.substring(tx.indexOf(" ") + 1);
               
            } catch(Exception e) {
                fam = tx;
                sub_level = "";
            }
            double d =  Double.parseDouble(mResult.alCorrect.get(i).toString());
            d /= Double.parseDouble(mResult.totalViewed.get(i).toString());
            double score = 100.0 * d;
            String strScore = Double.toString(score);
            
            //See if family name is already stored in the keys.
            //If not:
            //  Add the family name.
            //  Add a new array
            
            if(mFamilyKeys.containsKey(fam) == false){
                Map anon_map = new HashMap();
                ArrayList al = new ArrayList();
                anon_map.put("Sublevels", al);
                anon_map.put("Scores", new ArrayList());
                mFamilyKeys.put(fam, anon_map);
            }
            //Add the sub levels, and the score to the nested array lists.
            //ONLY IF there ARE sublevels.
            //Otherwise, only add the score list.
            Map anon_map = (HashMap)mFamilyKeys.get(fam);
            if(sub_level.length() > 0){
            ArrayList sub_levels = (ArrayList)anon_map.get("Sublevels");
            sub_levels.add(sub_level);
            }
            //Add the score
            ArrayList scores = (ArrayList)anon_map.get("Scores");
            scores.add(strScore);
        }
        
        String result_string = "";
        //Create the string to display this result.
        for(Iterator it = mFamilyKeys.keySet().iterator(); it.hasNext();){
            String fam = it.next().toString();
            double total = 0.0;
            int denom = 0;
            String sub_string = "";
            Map anon_map = (HashMap)mFamilyKeys.get(fam);
            ArrayList sub_levels = (ArrayList)anon_map.get("Sublevels");
            ArrayList scores = (ArrayList)anon_map.get("Scores");
            if(sub_levels.size() > 0){
                for(int i = 0; i < sub_levels.size(); i++){
                    if(fontItalic){
                    sub_string += "\t\t<i>" + sub_levels.get(i).toString() + ':'+"</i>";//Added by preethy
                    }
                    else{
                       sub_string += "\t\t" + sub_levels.get(i).toString() + ':';  
                    }
                    double score = Double.parseDouble(scores.get(i).toString());
                    total += score;
                    denom++;
                    sub_string += "  " + to_percent(Double.toString(score)) + '\n'; 
                }
                result_string += "\t" + fam + ":  " + to_percent(Double.toString(total/denom)) + '\n' + sub_string;
            } 
            else {  //Family only, no sub-levels
                total = Double.parseDouble(scores.get(0).toString());
                denom = 1;
                result_string += '\t' + fam + ":  " + to_percent(Double.toString(total/denom)) + '\n';
            }
        }
        result_string += '\n' + "Overall Grade: " + grade + '\n';
        myResults.add(0,  header + result_string + '\n');
    }
    
    private String to_percent(String number){

        return String.valueOf(Math.round(Double.parseDouble(number))) + "%";
    }
    private String SessionTypeString(int i){
        if(i == 1)
            return "(Quiz) Image Naming without Prompt";
        if(i == 2)
            return "(Quiz) Image Naming with Prompt";
        if(i == 3)
            return "(Quiz) Image Comparison";
        if(i == 4)
            return "(Quiz) Image Verifcation";
        if(i == 5)
            return "(Test) Image Naming without Prompt";
         if(i == 6)
            return "(Test) Image Comparison";
         if(i == 7)
            return "(Test) Image Verification";
        
        return "Unkown Session Type";
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taResultWindow = new javax.swing.JEditorPane();
        printButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setTitle("View Progress");
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jButton1.setText("OK");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("View Progress");

        jScrollPane1.setViewportView(taResultWindow);

        printButton.setText("Print");
        printButton.setToolTipText("");
        printButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        jLabel2.setToolTipText("");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(136, 136, 136)
                        .add(printButton)
                        .add(30, 30, 30))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(printButton)
                    .add(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        int keyCode = evt.getKeyCode();
        // Login user if Enter is pressed!!
         if( keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) { 
                this.setVisible(false);
         }
    }//GEN-LAST:event_jButton1KeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int keyCode = evt.getKeyCode();
        jLabel2.setText(((Integer) keyCode).toString());
        // Login user if Enter is pressed!!
         if( keyCode == KeyEvent.VK_ENTER) { 
                this.setVisible(false);
         }
    }//GEN-LAST:event_formKeyPressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        this.setVisible(false);
    }//GEN-LAST:event_jButton1MouseReleased

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        try {
          boolean print = taResultWindow.print();
        } catch (PrinterException ex) {
            Logger.getLogger(ProgressClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_printButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton printButton;
    private javax.swing.JEditorPane taResultWindow;
    // End of variables declaration//GEN-END:variables
    
}
