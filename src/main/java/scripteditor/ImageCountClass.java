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
 * ImageCountClass.java
 *
 * Created on March 26, 2008, 9:17 PM
 */
package scripteditor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Random;

/**
 *
 * @author  Ben
 */
public class ImageCountClass extends javax.swing.JDialog {

    String[] mFileNames;
    String mTaxaLevel;
    Random myRandom = new Random();
    DataBaseDriver db;
    ArrayList<String> items = new ArrayList<String>(); // contains the name of each item.
    ArrayList<Integer> count = new ArrayList<Integer>(); // holds count of each item. mapped 1 to 1 by index with items.

    /** Creates new form ImageCountClass */
    public ImageCountClass(String[] filenames, String taxaLevel, DataBaseDriver db_driver) {
        db = db_driver;
        mFileNames = filenames;
        mTaxaLevel = taxaLevel;
        initComponents();
        populateList();

    }

    private String[][] getFileList(String taxaset) throws FileNotFoundException {
        String[] temp = new String[1];
        String family;
        String commonname;
        temp[0] = taxaset.replace('|', ' ');
        temp[0] = temp[0].replace("   ", " ");

        if (mTaxaLevel.compareTo("Common Name") == 0) {
            family = temp[0].substring(0, temp[0].indexOf(" ") + 1);
            commonname = temp[0].substring(temp[0].indexOf(" ") + 1);

            //commonname = commonname.substring(commonname.indexOf(" "));
            family = family.trim();
            commonname = commonname.trim();
            temp[0] = family + " " + commonname;
        }
         //Commented by Preethy on 12-01-2012
       // String[][] files = db.getFileNames(mTaxaLevel, temp, false, 0);
        String[][] files = db.getFileNames(mTaxaLevel, temp, false, 0);
        return files;
    }

    private ArrayList<String> resizeList(String taxaset, ArrayList<String> list, int size) throws FileNotFoundException {

        ArrayList<String> returnArray = new ArrayList<String>();
        //ArrayList<String> myTempList = new ArrayList<String>();
        int numHelper = size;
        int numberNeeded;
        int randomnumber;

        if (list.size() == size) {
            return list;
        } else if (list.size() > size) {
            // Randomly take away from the list

            // #1 put list into 

            for (int i = 0; i < size; i++) {
                randomnumber = myRandom.nextInt(list.size() - 1);
                returnArray.add(list.get(randomnumber));
                list.remove(randomnumber);
            }
        } else {
            // Randomly add to the list
            String[][] allfiles = getFileList(taxaset);
            ArrayList<String> tempArray = new ArrayList<String>();
            String temp;
            if (size > allfiles.length) {
                JOptionPane.showMessageDialog(this, "The max quantity for " + taxaset + " is " + allfiles.length + ". Please select a lower quantity.");
                return null;
            }

            // put all files into tempArray
            for (int i = 0; i < allfiles.length; i++) {
                temp = allfiles[i][0]; // file name only
                tempArray.add(temp);

            }
            // remove files that user has already selected
            for (int i = 0; i < list.size(); i++) {
                tempArray.remove(list.get(i).substring(list.get(i).lastIndexOf(" ") + 1));
            }

            numberNeeded = size - list.size(); // The number needed to add to the list
            String header = list.get(0).substring(0, list.get(0).lastIndexOf(" "));

            for (int i = 0; i < numberNeeded; i++) {
                numHelper = myRandom.nextInt(tempArray.size());
                list.add(header + " " + tempArray.get(numHelper));
                tempArray.remove(numHelper);
            }

            returnArray = list;




        }

        return returnArray;
    }

    private void populateList() {

        int temp;
        int qty;
        String tempItem;
        int indexOfPipe;

        //reset all count quantities....

        count.clear();
        items.clear();

        for (int i = 0; i < mFileNames.length; i++) {

            indexOfPipe = mFileNames[i].lastIndexOf("|");
            indexOfPipe = indexOfPipe - 1;
            tempItem = mFileNames[i].substring(0, indexOfPipe);


            if (items.contains(tempItem) == true) {
                temp = items.indexOf(tempItem);
                qty = count.get(temp) + 1;
                count.set(temp, qty);
            } else {
                items.add(tempItem);
                count.add(1);
            }

        }

        //add to List
        listImages.removeAll();
        for (int i = 0; i < count.size(); i++) {
            listImages.add(count.get(i).toString() + "  " + items.get(i));
        }
    }

    public String[] getFileNames() {
        return mFileNames;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        tfSelected = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tfQTY = new javax.swing.JTextField();
        listImages = new java.awt.List();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jButton1.setText("Set Quantity");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tfSelected.setEditable(false);

        jLabel1.setText("Qty:");

        listImages.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listImagesItemStateChanged(evt);
            }
        });
        listImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listImagesActionPerformed(evt);
            }
        });

        jLabel2.setText("Image Set:");

        jButton2.setText("Make All Equal");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });

        jButton3.setText("Finished");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton3MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listImages, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfQTY, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jButton1)
                                    .addGap(43, 43, 43)
                                    .addComponent(jButton2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3))
                                .addComponent(tfSelected, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(listImages, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfQTY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseReleased

        int maxsize = 0;
        int currentsize = 0;
        String strholder;
        String temp;
        ArrayList<String> tempMainNames = new ArrayList<String>();
        ArrayList<String> tempPartNames = new ArrayList<String>();
        String[] tempfnames;
        dlgEqualize eq = new dlgEqualize();
        // Get the MAX for equalization
        try {

            maxsize = getFileList(items.get(0)).length; // gets first element max
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(ImageCountClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 1; i < items.size(); i++) {
            try {
                currentsize = getFileList(items.get(i)).length;
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                Logger.getLogger(ImageCountClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (currentsize < maxsize) {
                maxsize = currentsize;
            }
        }

        eq.setMax(maxsize);
        eq.setLocationRelativeTo(this);
        eq.setModal(true);
        eq.setVisible(true);
        maxsize = eq.getPsize();
        if (maxsize == -1) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            temp = items.get(i);
            for (int k = 0; k < mFileNames.length; k++) {
                strholder = mFileNames[k].substring(0, mFileNames[k].lastIndexOf(" ") - 1);
                if (strholder.trim().compareTo(temp) != 0) {
                    tempMainNames.add(mFileNames[k]);
                } else {
                    tempPartNames.add(mFileNames[k]);
                }
            }
            try {

                tempPartNames = resizeList(temp, tempPartNames, maxsize);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                Logger.getLogger(ImageCountClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempfnames = new String[tempPartNames.size() + tempMainNames.size()];
            for (int k = 0; k < tempPartNames.size(); k++) {
                tempfnames[k] = tempPartNames.get(k);
            }
            for (int k = 0; k < tempMainNames.size(); k++) {
                tempfnames[k + tempPartNames.size()] = tempMainNames.get(k);
            }

            mFileNames = tempfnames;
            tempMainNames.clear();
            tempPartNames.clear();
            tempfnames = null;

        }

        populateList();

    }//GEN-LAST:event_jButton2MouseReleased

    private void jButton3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseReleased
        this.setVisible(false);
    }//GEN-LAST:event_jButton3MouseReleased

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased

        String strQty;
        String strholder;
        String temp;
        String[] tempfnames;
        ArrayList<String> tempMainNames = new ArrayList<String>();
        ArrayList<String> tempPartNames = new ArrayList<String>();
        int intQty;
        strQty = tfQTY.getText();

        // try{
        intQty = Integer.parseInt(strQty); // Error checking needed for user input
        //}
        //catch {

        //}
        // Take out all related images from mFileNames
        temp = this.tfSelected.getText();
        //temp = temp.replace(" ", "");
        if (temp.compareTo("") == 0) {
            JOptionPane.showMessageDialog(this, "Please select an item from the list.");
            return;
        }
        for (int i = 0; i < mFileNames.length; i++) {
            strholder = mFileNames[i].substring(0, mFileNames[i].lastIndexOf(" ") - 1);
            if (strholder.trim().compareTo(temp) != 0) {
                tempMainNames.add(mFileNames[i]);
            } else {
                tempPartNames.add(mFileNames[i]);
            }
        }

        //Call the resize List
        String selectedtext = this.tfSelected.getText();
        try {

            tempPartNames = resizeList(selectedtext, tempPartNames, intQty);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(ImageCountClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tempPartNames == null) {
            return;
        }
        // Add tempfnames and mFileNames together

        tempfnames = new String[tempPartNames.size() + tempMainNames.size()];
        for (int i = 0; i < tempPartNames.size(); i++) {
            tempfnames[i] = tempPartNames.get(i);
        }
        for (int i = 0; i < tempMainNames.size(); i++) {
            tempfnames[i + tempPartNames.size()] = tempMainNames.get(i);
        }

        mFileNames = tempfnames;

        //populate

        populateList();



    }//GEN-LAST:event_jButton1MouseReleased

    private void listImagesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listImagesItemStateChanged
        String temp = listImages.getSelectedItem();
        String tempQty = listImages.getSelectedItem();
        temp = temp.substring(temp.indexOf(" ") + 2, temp.length());
        tempQty = tempQty.substring(0, tempQty.indexOf(" "));
        tfSelected.setText(temp);
        tfQTY.setText(tempQty);
    }//GEN-LAST:event_listImagesItemStateChanged

    private void listImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listImagesActionPerformed
    }//GEN-LAST:event_listImagesActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private java.awt.List listImages;
    private javax.swing.JTextField tfQTY;
    private javax.swing.JTextField tfSelected;
    // End of variables declaration//GEN-END:variables
    
}
