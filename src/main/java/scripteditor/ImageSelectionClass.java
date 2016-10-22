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
 * ImageSelectionClass.java
 *
 * Created on July 25, 2007, 8:50 PM
 */

package scripteditor;
import java.lang.Runtime;


import javax.swing.*;
import java.util.*;
import java.awt.event.*;
//import org.omg.SendingContext.RunTime;
/**
 *
 * @author  Ben
 */


public class ImageSelectionClass extends javax.swing.JFrame {

    private String mTaxaLevel; // The Taxonomic Level eg. Family Genus or Species
    private String[] mFileNames; // The File names of the images to view
    private DataBaseDriver db;
    private ThumbNailClass thumbnail = new ThumbNailClass();
    private ActionListener mAL;
    private JFrame mMain;
    private String[] mMappedFileNames; // Contains the actual file names.. eg  12345.jpg
    private ImageIcon[] mm;
    private int mTotalinViewer = 0;
    private ArrayList<String> myArray = new ArrayList<String>();
    private boolean SSave = false;
    //private boolean useSize = false;


    /** Creates new form ImageSelectionClass */
    public ImageSelectionClass(String taxaLevel, String[] filenames, ActionListener al, JFrame main, int width, int height, int lx, int ly, DataBaseDriver db_driver) {
        initComponents();
        db = db_driver;
        //GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
        //this.setSize(new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()));
        //this.setLocationRelativeTo(null);

        //if(height == -1){
            this.setExtendedState(MAXIMIZED_BOTH);
       // }
       // else{

       //     this.setLocation(lx, ly);
       //     this.setSize(width, height);
       //     this.setExtendedState(NORMAL);
       // }

        mAL = al;
        mMain = main;
        mTaxaLevel = taxaLevel;
        if (filenames != null){
            mFileNames = filenames;
        }
        String[] taxa = db.getTaxa("Family");
            for( int i = 0; i < taxa.length; i++){
                listFamily.add(taxa[i].toString());
            }
        if(filenames != null){
            for(int i=0; i<filenames.length; i++){ //Add filenames to the list
                myArray.add(filenames[i]);
            }
        }
        jList1.setListData(myArray.toArray());
        // Add Action Listner here to Image Viewer

        this.setVisible(true);


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        taxaLevelGroup = new javax.swing.ButtonGroup();
        listFamily = new java.awt.List();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        listGenus = new java.awt.List();
        listSpecies = new java.awt.List();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        thumbnailViewer = new javax.swing.JList();
        jButton6 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton7 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Image Selection Dialog");
        setExtendedState(1);
        setFocusCycleRoot(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        listFamily.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listFamilyMouseReleased(evt);
            }
        });
        listFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listFamilyActionPerformed(evt);
            }
        });
        listFamily.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                listFamilyPropertyChange(evt);
            }
        });
        listFamily.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listFamilyKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Family");

        jButton1.setText("Save and Exit");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });

        listGenus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listGenusMouseReleased(evt);
            }
        });
        listGenus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listGenusKeyPressed(evt);
            }
        });

        listSpecies.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listSpeciesMouseReleased(evt);
            }
        });
        listSpecies.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listSpeciesKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Genus");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Species/Common");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Selected Images");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Click on an image to add it to the selected images.");

        jButton2.setText("Cancel without Saving");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });

        jButton3.setText("View Selected Image Set");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton3MouseReleased(evt);
            }
        });

        jButton4.setText("Remove");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton4MouseReleased(evt);
            }
        });

        thumbnailViewer.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        thumbnailViewer.setDragEnabled(true);
        thumbnailViewer.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        thumbnailViewer.setVisibleRowCount(3);
        thumbnailViewer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                thumbnailViewerMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                thumbnailViewerMouseReleased(evt);
            }
        });

        jScrollPane1.setViewportView(thumbnailViewer);

        jButton6.setText("Remove All");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton6MouseReleased(evt);
            }
        });

        jProgressBar1.setStringPainted(true);

        jButton7.setText("Select All");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton7MouseReleased(evt);
            }
        });

        jScrollPane2.setViewportView(jList1);

        jButton5.setText("Set Image Quantities");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton5MouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(jLabel1, 0, 0, Short.MAX_VALUE)
                                    .add(listFamily, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                                .add(10, 10, 10)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(listGenus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(listSpecies, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                            .add(layout.createSequentialGroup()
                                .add(jButton3)
                                .add(23, 23, 23)
                                .add(jButton7)
                                .add(19, 19, 19)
                                .add(jButton5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton2)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jScrollPane2)
                    .add(listFamily, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .add(listSpecies, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .add(listGenus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButton4)
                        .add(jButton6)
                        .add(jButton1)
                        .add(jButton2))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButton3)
                        .add(jButton7)
                        .add(jButton5)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseReleased
// This will set images according to the user defined settings!

        if (db.DataBaseExists() == false) {
            Utilities.MessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            //JOptionPane.showMessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            return;
        }
        String[] tempArray = new String[myArray.size()];
        for(int i = 0; i < myArray.size(); i++){
            tempArray[i] = myArray.get(i).toString();
        }
        mFileNames = tempArray;


    ImageCountClass myQtys = new ImageCountClass(mFileNames, mTaxaLevel, db);
    myQtys.setLocationRelativeTo(this);
    myQtys.setModal(true);
    myQtys.setVisible(true);
    mFileNames = myQtys.getFileNames();
    jList1.removeAll();
    myArray.clear();
     if(mFileNames != null){
            for(int i=0; i<mFileNames.length; i++){ //Add filenames to the list
                myArray.add(mFileNames[i]);
            }
        }
        jList1.setListData(myArray.toArray());

// TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        mMain.setVisible(true);

      /*  if(this.getExtendedState() == super.MAXIMIZED_BOTH){
            mMain.imageselectionHeight = -1;
            mMain.imageselectionWidth = -1;
            mMain.imageselectionXlocation = -1;
            mMain.imageselectionYlocation = -1;
        }
        else{
        mMain.imageselectionHeight = this.HEIGHT;
        mMain.imageselectionWidth = this.WIDTH;
        mMain.imageselectionXlocation = this.getLocation().x;
        mMain.imageselectionYlocation = this.getLocation().y;
        }*/
        mAL.actionPerformed(new ActionEvent(this, 1, "ImageSelectionClass"));
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void listSpeciesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listSpeciesKeyPressed
        thumbnailViewer.setListData(new ImageIcon[0]);// TODO add your handling code here:
    }//GEN-LAST:event_listSpeciesKeyPressed

    private void listGenusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listGenusKeyPressed
        thumbnailViewer.setListData(new ImageIcon[0]);// TODO add your handling code here:
    }//GEN-LAST:event_listGenusKeyPressed

    private void listFamilyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listFamilyKeyPressed
        thumbnailViewer.setListData(new ImageIcon[0]);// TODO add your handling code here:
    }//GEN-LAST:event_listFamilyKeyPressed

    private void listFamilyPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_listFamilyPropertyChange

    }//GEN-LAST:event_listFamilyPropertyChange

    private void listSpeciesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listSpeciesMouseReleased
        thumbnailViewer.setListData(new ImageIcon[0]);// TODO add your handling code here:
    }//GEN-LAST:event_listSpeciesMouseReleased

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
      //  int window_state = this.getExtendedState();
      //  if(window_state == super.NORMAL || window_state == super.MAXIMIZED_BOTH){
      //  if(this.getExtendedState() == super.MAXIMIZED_BOTH){
      //      (ScriptMain)mMain.imageselectionHeight = -1;
      //      mMain.imageselectionWidth = -1;
      //      mMain.imageselectionXlocation = -1;
      //      mMain.imageselectionYlocation = -1;
      //  }
      //  else{
      //      Dimension d = this.getSize();
      //  mMain.imageselectionHeight = d.height;
      //  mMain.imageselectionWidth = d.width;
      //  mMain.imageselectionXlocation = this.getX();
      //  mMain.imageselectionYlocation = this.getY();
      //  }
      //  }
    }//GEN-LAST:event_formWindowStateChanged

    private void thumbnailViewerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thumbnailViewerMouseReleased
        AddSelected();
    }//GEN-LAST:event_thumbnailViewerMouseReleased

    private void jButton7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseReleased

         // Build Header!!

        String header = listFamily.getSelectedItem();
        if(listGenus.getSelectedItem() != null && mTaxaLevel.compareTo("Common Name") != 0  ){
            header = header + " | " + listGenus.getSelectedItem();
        }
        if(listSpecies.getSelectedItem() != null){
            header = header + " | " + listSpecies.getSelectedItem();
        }

        //int[] indexes =  thumbnailViewer.getSelectedIndices();


            mapFilenames();
        for(int i = 0; i < mMappedFileNames.length; i++){
            if(!ImageExistInList(header + " | " + mMappedFileNames[i])){
                myArray.add(header + " | " + mMappedFileNames[i]);
            }
        }
        jList1.setListData(myArray.toArray());
        thumbnailViewer.clearSelection();


    }//GEN-LAST:event_jButton7MouseReleased

    private void thumbnailViewerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thumbnailViewerMouseClicked

    }//GEN-LAST:event_thumbnailViewerMouseClicked

    private void jButton3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseReleased
     populateViewer();
    }//GEN-LAST:event_jButton3MouseReleased

    private void jButton6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseReleased
        //listFileNames.removeAll();
        myArray.clear();
        jList1.setListData(myArray.toArray());
    }//GEN-LAST:event_jButton6MouseReleased

    private void jButton4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseReleased
        Object[] indexes = jList1.getSelectedValues();//listFileNames.getSelectedIndexes();

        for(int i=0; i < indexes.length; i++){
            myArray.remove(indexes[i]);
        }
        jList1.setListData(myArray.toArray());
    }//GEN-LAST:event_jButton4MouseReleased

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
// TODO add your handling code here:
    }//GEN-LAST:event_formMouseReleased

    private void AddSelected(){
           // Add image selected to the collection
        // Build Header!!
        String header = listFamily.getSelectedItem();
        if(listGenus.getSelectedItem() != null && mTaxaLevel.compareTo("Common Name") != 0 ){
            header = header + " | " + listGenus.getSelectedItem();
        }
        if(listSpecies.getSelectedItem() != null){
            header = header + " | " + listSpecies.getSelectedItem();
        }

        int index =  thumbnailViewer.getSelectedIndex();
        //for(int i = 0; i < indexes.length; i++){
            if(!ImageExistInList(header + " | " + mMappedFileNames[index])){
                myArray.add(header + " | " + mMappedFileNames[index]);
            }

        jList1.setListData(myArray.toArray());
        thumbnailViewer.clearSelection();
    }


    private boolean ImageExistInList(String img){
        for(int i = 0; i < myArray.size(); i++){
            if(img.compareTo(myArray.get(i)) == 0){
                return true;
            }
        }
        return false;
    }

    private void listGenusMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listGenusMouseReleased
        if (db.DataBaseExists() == false) {
            Utilities.MessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            //JOptionPane.showMessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            return;
        }
        thumbnailViewer.setListData(new ImageIcon[0]);
        if(mTaxaLevel.compareTo("Family") == 0)
            return;
        else if(mTaxaLevel.compareTo("Genus") == 0)
            return;

        String[] s;
        if(mTaxaLevel.compareTo("Common Name")==0){
            //Load species
            s = db.getCommonName(this.listGenus.getSelectedItem());
        } else{
            s = db.getSpecies(this.listGenus.getSelectedItem());
        }
        listSpecies.removeAll();
        //  if(this.list4.isEnabled())
        //    list4.removeAll();
        for(int i = 0; i < s.length; i++)
            this.listSpecies.add(s[i]);

    }//GEN-LAST:event_listGenusMouseReleased

    private void listFamilyMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listFamilyMouseReleased


        thumbnailViewer.setListData(new ImageIcon[0]);
        if(mTaxaLevel.compareTo("Family") != 0)
        {
            listGenus.removeAll();
            if(listSpecies.isEnabled())
                listSpecies.removeAll();
          //  if(list4.isEnabled())
           //     list4.removeAll();
            LoadGenus(listFamily.getSelectedItem());
        }
    }//GEN-LAST:event_listFamilyMouseReleased

    private void listFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listFamilyActionPerformed

    }//GEN-LAST:event_listFamilyActionPerformed

    private void jButton2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseReleased
        mAL.actionPerformed(new ActionEvent(this, 1, "ImageSelectionClass"));
        mMain.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2MouseReleased

    public String[] getFileNames(){
        return mFileNames;
    }


    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased

        SSave = true;
        // Save File Names Selected
        String[] tempArray = new String[myArray.size()];
        for(int i = 0; i < myArray.size(); i++){
            tempArray[i] = myArray.get(i).toString();
        }
        mFileNames = tempArray;
        mAL.actionPerformed(new ActionEvent(this, 1, "ImageSelectionClass"));
        this.setVisible(false);
        mMain.setVisible(true);
    }//GEN-LAST:event_jButton1MouseReleased

    private void mapFilenames(){
        if (db.DataBaseExists() == false) {
            Utilities.MessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            //JOptionPane.showMessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            return;
        }
        String[] temp = new String[1];
        if(this.mTaxaLevel.compareTo("Family") == 0){
            temp[0] = listFamily.getSelectedItem();
        } else if(this.mTaxaLevel.compareTo("Genus") == 0){
            temp[0] = listFamily.getSelectedItem() + " " +listGenus.getSelectedItem();
        } else if(this.mTaxaLevel.compareTo("Species") == 0)
            temp[0] = listFamily.getSelectedItem() + " " +listGenus.getSelectedItem() + " " + listSpecies.getSelectedItem();
        else
            temp[0] = listFamily.getSelectedItem() + " " + listSpecies.getSelectedItem();

        String[][] files = db.getFileNames(mTaxaLevel, temp, false, 0);

        String[] mappedFileNames = new String[files.length];

        for(int i = 0; i < files.length; i++){
            mappedFileNames[i] = files[i][0];
        }
        mMappedFileNames = mappedFileNames;
    }


    private void populateViewer(){
        if (db.DataBaseExists() == false) {
            Utilities.MessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            //JOptionPane.showMessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            return;
        }
        double rows;
        mm = null;
        // View Selected Image Set
        Runtime.getRuntime().gc();
        thumbnailViewer.removeAll();
        String[] temp = new String[1];
        if(this.mTaxaLevel.compareTo("Family") == 0){
            temp[0] = listFamily.getSelectedItem();
        } else if(this.mTaxaLevel.compareTo("Genus") == 0){
            temp[0] = listFamily.getSelectedItem() + " " +listGenus.getSelectedItem();
        } else if(this.mTaxaLevel.compareTo("Species") == 0)
            temp[0] = listFamily.getSelectedItem() + " " +listGenus.getSelectedItem() + " " + listSpecies.getSelectedItem();
        else
            temp[0] = listFamily.getSelectedItem() + " " + listSpecies.getSelectedItem();

        String[][] files = db.getFileNames(mTaxaLevel, temp, false, 0);
        String st;

        mm = new ImageIcon[files.length];
        String[] mappedFileNames = new String[files.length];
        jProgressBar1.setMaximum(files.length);
        jProgressBar1.setMinimum(0);
        mTotalinViewer = files.length;
        for(int i = 0; i < files.length; i++){
            mappedFileNames[i] = files[i][0];
            mm[i] = thumbnail.tempFunction(files[i][0]);
            jProgressBar1.setValue(i+1);
            jProgressBar1.update(jProgressBar1.getGraphics());
        }
        mMappedFileNames = mappedFileNames;
        // Set the number of rows that can be displayed in the viewer
        rows = thumbnailViewer.getHeight() / 100;
        thumbnailViewer.setVisibleRowCount((int)rows);
        thumbnailViewer.setListData(mm);

    }

    public boolean ShouldSave(){
        return SSave;
    }



    private void LoadGenus(String family)
    {
        if (db.DataBaseExists() == false) {
            Utilities.MessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            //JOptionPane.showMessageDialog(this, "The database file " + Configuration.DataBaseName() + " cannot be found!");
            return;
        }
        String[] genus = db.getGenus(family);
        for(int i = 0; i < genus.length; i++)
            listGenus.add(genus[i]);
    }
    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.List listFamily;
    private java.awt.List listGenus;
    private java.awt.List listSpecies;
    private javax.swing.ButtonGroup taxaLevelGroup;
    private javax.swing.JList thumbnailViewer;
    // End of variables declaration//GEN-END:variables

//    addListSelectionListener
//
//    public void valueChanged(ListSelectionEvent e) {
//    if (e.getValueIsAdjusting() == false) {
//
//        if (list.getSelectedIndex() == -1) {
//        //No selection, disable fire button.
//            fireButton.setEnabled(false);
//
//        } else {
//        //Selection, enable the fire button.
//            fireButton.setEnabled(true);
//        }
//    }
//}
//




}




/*class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    private ThumbNailClass thumbnail = new ThumbNailClass();

    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
/*
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)
       // int selectedIndex = ((Integer)value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        //Set the icon and text.  If icon was null, say so.
        ImageIcon icon = thumbnail.getThumbnail("01012.jpg");
        String pet = "WORK";
        setIcon(icon);
        if (icon != null) {
            setText(pet);
            setFont(list.getFont());
        } else {

        }

        return this;
    }
}*/
