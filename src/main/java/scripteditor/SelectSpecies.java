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
 * SelectSpecies.java
 *
 * Created on Jan 16, 2012, 9:30 AM
 */

package scripteditor;

import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author  Preethy
 * This dialog box is used to select species based on family names.
 *
 */
public class SelectSpecies extends javax.swing.JDialog {

    String mTaxaLevel;
    DataBaseDriver db;
    ArrayList families = new ArrayList();
    ArrayList fm = new ArrayList();
    ArrayList gen = new ArrayList();
     public boolean bCancel = false;
     public boolean CommonNames=false;
     public static boolean esc=false;
     String [] genus;

      String[][] fileNames;
      int adv_options=0;
     public SelectSpecies(DataBaseDriver db_driver,int maxImages){

        initComponents();//two hidden list boxes list3 and listSelections
        addEscapeListener(this);
        this.list1.setMultipleMode(true);
        LoadFamilies();
        adv_options=maxImages;

        if(adv_options>0){
            label_advanced_optns.setText("Advanced Options set, number of images selected: "+adv_options);
            label_advanced_optns.setForeground(Color.red);
        }
    }
    private void LoadFamilies()
    {
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
        String[] families = db.getTaxa("Family");
        for(int i = 0; i < families.length; i++)
            list1.add(families[i]);
    }

    private String[] LoadGenus(String[] family)
    {
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
        String[] genus = db.getGenus2(family);

        for(int i = 0; i < genus.length; i++)
        fm=db.fm;
        return genus;
    }
    private void LoadSpecies(String[] family,String[] genus){
       DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
       String[] s = null;
       if(ch1.isSelected())
        {
           s = db.getSpecies2(genus);  //if ch1 is selected then list3 contains species names otherwise list3 contains common names
        }
        if(ch2.isSelected())
        {
           CommonNames=true;
           s = db.getCommonName2(genus);
        }
        gen=db.gen;
        list3.removeAll();
        for(int i = 0; i < s.length; i++){
        this.list3.add(s[i]); }
    }
    public String[] getItems()
    {
        String[] items = listSelections.getItems();

        return items;//return selected taxa
    }
    public String getTaxaLevel(){
        if(ch1.isSelected())
            return "Species";
         else
            return "Common Name";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblCaption = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        list1 = new java.awt.List();
        ch1 = new javax.swing.JCheckBox();
        ch2 = new javax.swing.JCheckBox();
        list3 = new java.awt.List();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        listSelections = new java.awt.List();
        label_advanced_optns = new javax.swing.JLabel();
        image_count = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Species By Family");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblCaption.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Family");

        list1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                list1MouseReleased(evt);
            }
        });

        buttonGroup1.add(ch1);
        ch1.setText("Select By Scientific Name");
        ch1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ch1MouseClicked(evt);
            }
        });
        ch1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ch1StateChanged(evt);
            }
        });
        ch1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ch1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(ch2);
        ch2.setText("Select By Common Name");
        ch2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ch2MouseClicked(evt);
            }
        });
        ch2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ch2StateChanged(evt);
            }
        });

        list3.setVisible(false);

        jButton1.setText("Ok");
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

        jButton2.setText("Cancel");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton2KeyPressed(evt);
            }
        });

        listSelections.setVisible(false);
        listSelections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listSelectionsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(lblCaption, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(ch1)
                            .add(ch2)
                            .add(list1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 218, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(144, 144, 144)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createSequentialGroup()
                                        .add(listSelections, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(142, 142, 142))
                                    .add(list3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(image_count, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                                    .add(label_advanced_optns, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)))))
                    .add(layout.createSequentialGroup()
                        .add(201, 201, 201)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(33, 33, 33)
                        .add(jButton2))
                    .add(layout.createSequentialGroup()
                        .add(112, 112, 112)
                        .add(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblCaption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(list1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                        .add(23, 23, 23))
                    .add(layout.createSequentialGroup()
                        .add(list3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 284, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(144, 144, 144)
                        .add(image_count)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 76, Short.MAX_VALUE)
                        .add(label_advanced_optns)
                        .add(112, 112, 112)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(ch1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(ch2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton1)
                            .add(jButton2)))
                    .add(listSelections, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ch1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ch1MouseClicked
        // TODO add your handling code here:
    //  String[] mTaxa;
     // String taxonomicLevel;
       if(ch1.isSelected())
        {
          list3.removeAll();
        }
     //   countImage();
     //  taxonomicLevel=getTaxaLevel();
     //  System.out.println("level----->"+taxonomicLevel);
     //  System.out.println("mTaxa----->"+getItems().length);
     //  mTaxa=getItems();
      //  fileNames=db.getFileNames(taxonomicLevel, mTaxa, false, 0);
      //  image_count.setText("Total number of images selected :"+fileNames.length);
       // image_count.setForeground(Color.red);
    }//GEN-LAST:event_ch1MouseClicked

    private void ch2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ch2MouseClicked
        // TODO add your handling code here:
    //  String[] mTaxa;
    //  String taxonomicLevel;
       if(ch2.isSelected())
        {
         list3.removeAll();
        }
       //countImage();
       //taxonomicLevel=getTaxaLevel();
      // mTaxa=getItems();
       //System.out.println("level----->"+taxonomicLevel);
       //System.out.println("mTaxa----->"+getItems().length);
      // fileNames=db.getFileNames(taxonomicLevel, mTaxa, false, 0);
      // image_count.setText("Total number of images selected :"+fileNames.length);
      // image_count.setForeground(Color.red);

    }//GEN-LAST:event_ch2MouseClicked

    private void ch1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ch1ItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_ch1ItemStateChanged

    private void list1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list1MouseReleased
        // TODO add your handling code here:
          list3.removeAll();
         genus= LoadGenus(list1.getSelectedItems());

    }//GEN-LAST:event_list1MouseReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowOpened

    private void ch2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ch2StateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_ch2StateChanged

    private void ch1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ch1StateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_ch1StateChanged

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        // TODO add your handling code here:
        if((!ch1.isSelected()) && (!ch2.isSelected()))
        {
            Utilities.MessageDialog(this, "Please select one of the check boxes.");

        }if(((ch1.isSelected()) ||  (ch2.isSelected())) && (list1.getSelectedItems().length==0))
        {
            Utilities.MessageDialog(this, "Please make any selections.");
        }
        else if(((ch1.isSelected()) ||  (ch2.isSelected())) && (list1.getSelectedItems().length!=0))
        {
         LoadSpecies(list1.getSelectedItems(),genus);
         addFromList(list3);
         this.setVisible(false);
        }

    }//GEN-LAST:event_jButton1MouseReleased
    private void countImage(){
        if(((ch1.isSelected()) ||  (ch2.isSelected())) && (list1.getSelectedItems().length!=0))
        {
         LoadSpecies(list1.getSelectedItems(),genus);
         addFromList(list3);

        }
    }
    private void addFromList(java.awt.List list)
    {
        String selectedSpecies = "";
        String cname="";
        String[] selections = list.getItems();

        for(int i = 0; i < selections.length; i++){
           for(int j=0;j<gen.size();j++)
            {
                  String a=(String)gen.get(j);
                  if(selections[i].equals(a.split("#")[1]))
                    {
                      for(int k=0;k<fm.size();k++)
                        {
                            String b=(String)fm.get(k);
                            if(a.split("#")[0].equals(b.split("#")[1]))
                             {
                                  if(ch1.isSelected())
                                  {
                                    selectedSpecies= b.split("#")[0]+" "+a.split("#")[0]+" "+selections[i];
                                  }
                                  else if(ch2.isSelected())
                                  {
                                      selectedSpecies= b.split("#")[0]+" "+selections[i];
                                      cname=a.split("#")[0]+" "+a.split("#")[2];
                                  }
                               listSelections.add(selectedSpecies);//Adding selected taxa
                             }
                        }

                    }
             }
        }

     }
    private void jButton2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseReleased
        // TODO add your handling code here:
         bCancel = true;
         this.setVisible(false);
    }//GEN-LAST:event_jButton2MouseReleased

    private void listSelectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listSelectionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listSelectionsActionPerformed

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // TODO add your handling code here:
          if((!ch1.isSelected()) && (!ch2.isSelected()))
        {
            Utilities.MessageDialog(this, "Please select one of the check boxes.");

        }if(((ch1.isSelected()) ||  (ch2.isSelected())) && (list1.getSelectedItems().length==0))
        {
            Utilities.MessageDialog(this, "Please make any selections.");
        }
        else if(((ch1.isSelected()) ||  (ch2.isSelected())) && (list1.getSelectedItems().length!=0))
        {
         LoadSpecies(list1.getSelectedItems(),genus);
         addFromList(list3);
         this.setVisible(false);
        }
    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyPressed
        // TODO add your handling code here:
         bCancel = true;
         this.setVisible(false);
    }//GEN-LAST:event_jButton2KeyPressed
  public static void addEscapeListener(final JDialog dialog) {
    ActionListener escListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            esc=true;
            dialog.setVisible(false);        }    };
    dialog.getRootPane().registerKeyboardAction(escListener,KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),JComponent.WHEN_IN_FOCUSED_WINDOW);
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox ch1;
    private javax.swing.JCheckBox ch2;
    private javax.swing.JLabel image_count;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label_advanced_optns;
    private javax.swing.JLabel lblCaption;
    private java.awt.List list1;
    private java.awt.List list3;
    private java.awt.List listSelections;
    // End of variables declaration//GEN-END:variables

}
