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
 * SelectTaxa.java
 *
 * Created on Jan 13, 2012, 8:57 AM
 */
package scripteditor;

//import aleksPack10.panel.PanelApplet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import static javax.swing.SwingConstants.*;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Preethy This dialog box is used to select images based on the
 * selected values in list box
 */
public class SelectImage2 extends javax.swing.JDialog {

    DataBaseDriver db;
    public boolean bCancel = false;
    boolean mUseCommonNames;
    public boolean close = false;
    int imageCount = 0;//Added by preethy on 12-01-2012
    public static boolean esc = false;
    public boolean select = false;
    int adv_options = 0;
    int lastCheckedGroup = -1;
    ArrayList header = new ArrayList();
    ArrayList extraList = new ArrayList();
    ArrayList extraLabel = new ArrayList();
    ArrayList extraGroup = new ArrayList();
    ArrayList extraPanel = new ArrayList();

    public SelectImage2(DataBaseDriver db_driver, int maxImages) {
        initComponents();
       //this.setBounds(0, 0, 1112, 533);

        //jButton1.setBounds(394, 287, 50, 26);
        ch1.setBounds(29, 393, 150, 24);
        ch2.setBounds(29, 421, 160, 24);
        ch3.setBounds(29, 449, 250, 24);
        ch4.setBounds(29, 477, 250, 24);
        label_advanced_optns.setBounds(35, 380, 360, 15);
        // label_advanced_optns.setVisible(true);
        jPanel1A.setVisible(false);
        jPanel1B.setVisible(false);
        jPanel1C.setVisible(false);
        jPanel1D.setVisible(false);
        jPanel1E.setVisible(false);
        jPanel1F.setVisible(false);
        
        imagecountLabel.setBounds(35, 360, 199, 10);
        count.setBounds(252, 360, 39, 12);
        addEscapeListener(this);
        db = db_driver;
        String relStatus = db.GetFieldRelationships();
        String frFile = DataBaseDriver.DATABASE_FOLDER + "/" + DataBaseDriver.FIELD_RELATIONSHIPS_FILE;
        // do nothing if relStatus is "success" or "no file"
        if (relStatus.equals("read error")) {
            JOptionPane.showMessageDialog(this, "The file " + frFile + " was not read properly. Please close the Groups window and try again.");
        }
        else if (relStatus.equals("duplicate in set")) {
            JOptionPane.showMessageDialog(this, "At least one duplicate value was detected in " + frFile + " within a single set. Please close the Groups window, double-check the file, and make sure you don't repeat a column within a single group set (that is, between one RECIPROCAL column and the next).");
        }
        else if (relStatus.equals("invalid header value")) {
            //JOptionPane.showMessageDialog(this, "One of your columns in " + frFile + " doesn't match any columns in your database file. Please close the Groups window, check both CSV files, and try again.");
        }
        adv_options = maxImages;
        header = db.GetHeader();
        Object[] list;
        int pos[] = {27, 204, 383, 560, 737, 914};
        int lblpos[] = {71, 253, 432, 600, 782, 949};
        list = new Object[]{"jLabel1", "jLabel2", "jLabel3", "jLabel4", "jLabel5", "jLabel6"};
        int k = 0;

        /*    for(int i=0;i<header.size();i++){
         if(header.get(i).equals("subject") || header.get(i).equals("Subject")){
         jLabel1.setText(header.get(i).toString());
         }
         if(header.get(i).equals("Group") || header.get(i).equals("group")){
         jLabel2.setText(header.get(i).toString());
         }
         if(header.get(i).equals("origin") || header.get(i).equals("Origin")){
         jLabel3.setText(header.get(i).toString());
         }
         if(header.get(i).equals("form") || header.get(i).equals("Form")){
         jLabel4.setText(header.get(i).toString());
         }
         if(header.get(i).equals("use") || header.get(i).equals("Use")){
         jLabel5.setText(header.get(i).toString());
         }
         if(header.get(i).equals("Photo-Type") || header.get(i).equals("photo-type")){
         jLabel6.setText(header.get(i).toString());  
         }
         }
     
         if(header.contains("subject") || header.contains("Subject")){
         jScrollPane1.setBounds(pos[k],62,158,192);
         jScrollPane1.setVisible(true);
         jLabel1.setBounds(lblpos[k],40,67,16);
         // jLabel1.setText(header.get(k).toString());
         k++;
         }
         if(header.contains("Group") || header.contains("group")){
         jScrollPane2.setBounds(pos[k],62,158,192);
         jScrollPane2.setVisible(true);
         jLabel2.setBounds(lblpos[k],40,67,16);
         // jLabel2.setText(header.get(k).toString());
         k++;
         }
         if(header.contains("origin") || header.contains("Origin")){
         jScrollPane3.setBounds(pos[k],62,158,192);
         jScrollPane3.setVisible(true);
         jLabel3.setBounds(lblpos[k],40,67,16);
         //  jLabel3.setText(header.get(k).toString());
         k++;
         }
         if(header.contains("form") || header.contains("Form")){
         jScrollPane4.setBounds(pos[k],62,158,192);
         jScrollPane4.setVisible(true);
         jLabel4.setBounds(lblpos[k],40,67,16);
         // jLabel4.setText(header.get(k).toString());
         k++;  
         }
         if(header.contains("use") || header.contains("Use")){
         jScrollPane5.setBounds(pos[k],62,158,192);
         jScrollPane5.setVisible(true);
         jLabel5.setBounds(lblpos[k],40,67,16);
         // jLabel5.setText(header.get(k).toString());
         k++;
         }
         if(header.contains("Photo-Type") || header.contains("photo-type")){
         jScrollPane6.setBounds(pos[k],62,158,192);
         jScrollPane6.setVisible(true);
         jLabel6.setBounds(lblpos[k],40,67,16);
         // jLabel6.setText(header.get(k).toString());
         k++;
         }*/
        for (int i = 0; i < header.size(); i++) {
            if (i == 0) {
                jLabel1.setText(header.get(i).toString());
                jScrollPane1.setBounds(pos[k], 62, 162, 192);
                jPanel1A.setVisible(true);
                jLabel1.setBounds(lblpos[k], 40, 80, 16);
                // jLabel1.setText(header.get(k).toString());
                k++;
            } else if (i == 1) {
                jLabel2.setText(header.get(i).toString());
                jScrollPane2.setBounds(pos[k], 62, 162, 192);
                jPanel1B.setVisible(true);
                jLabel2.setBounds(lblpos[k], 40, 80, 16);
                // jLabel2.setText(header.get(k).toString());
                k++;
            } else if (i == 2) {
                jLabel3.setText(header.get(i).toString());
                jScrollPane3.setBounds(pos[k], 62, 162, 192);
                jPanel1C.setVisible(true);
                jLabel3.setBounds(lblpos[k], 40, 80, 16);
                //  jLabel3.setText(header.get(k).toString());
                k++;
            } else if (i == 3) {
                jLabel4.setText(header.get(i).toString());
                jScrollPane4.setBounds(pos[k], 62, 162, 192);
                jPanel1D.setVisible(true);
                jLabel4.setBounds(lblpos[k], 40, 80, 16);
                // jLabel4.setText(header.get(k).toString());
                k++;

            } else if (i == 4) {
                jLabel5.setText(header.get(i).toString());
                jScrollPane5.setBounds(pos[k], 62, 162, 192);
                jPanel1E.setVisible(true);
                jLabel5.setBounds(lblpos[k], 40, 80, 16);
                // jLabel5.setText(header.get(k).toString());
                k++;
            } else if (i == 5) {
                jLabel6.setText(header.get(i).toString());
                jScrollPane6.setBounds(pos[k], 62, 162, 192);
                jPanel1F.setVisible(true);
                jLabel6.setBounds(lblpos[k], 40, 80, 16);
                // jLabel6.setText(header.get(k).toString());
                k++;
            }
            else if (i > 5) {
                JLabel newLabel = new JLabel(header.get(i).toString());
                JPanel newPanel = new JPanel(null);
                final JList newList = new JList();
                JScrollPane newGroup = new JScrollPane(newList);

                newPanel.setBackground(new java.awt.Color(153, 153, 153));

                newPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
                newLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                newLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                newPanel.add(newLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));
                newPanel.setBackground(new Color(238, 238, 238));

                newGroup.setPreferredSize(new java.awt.Dimension(170, 220));

                newList.setName(header.get(i).toString().toLowerCase()); // NOI18N
                newList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    @Override
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                        extraListValueChanged(evt, newList);
                    }
                });
                newGroup.setViewportView(newList);

                newPanel.add(newGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

                jPanel1.add(newPanel);

                extraList.add(newList);
                extraLabel.add(newLabel);
                extraGroup.add(newGroup);
                extraPanel.add(newPanel);

                k++;
            }
        }

        if (k == 1) {
            this.setSize(414, 537);
            jButton1.setBounds(320, 460, 60, 35);
            jScrollPaneGroups.setBounds(100, 50, 200, 265);
        } else if (k == 2) {
            this.setSize(435, 537);
            jButton1.setBounds(320, 460, 60, 35);
            jScrollPaneGroups.setSize(380, 265);
        } else if (k == 3) {
            this.setSize(578, 537);
            jButton1.setBounds(380, 460, 60, 35);
            jScrollPaneGroups.setSize(550, 265);
        } else if (k == 4) {
            this.setSize(738, 537);
            jButton1.setBounds(347, 460, 60, 35);
            jScrollPaneGroups.setSize(720, 265);
        } else if (k == 5) {
            this.setSize(903, 537);
            jButton1.setBounds(425, 460, 60, 35);
            jScrollPaneGroups.setSize(890, 265);
        } else if (k > 5) {
            this.setSize(1000, 537);
            jButton1.setBounds(501, 460, 60, 35);
            jScrollPaneGroups.setSize(980, 280);            
        }
        for (int j = 0; j < header.size(); j++) {
            if (j == 0) {
                LoadSubject(header.get(j).toString());
            }
            if (j == 1) {
                LoadGroup(header.get(j).toString());
            }
            if (j == 2) {
                LoadOrigin(header.get(j).toString());
            }
            if (j == 3) {
                LoadForm(header.get(j).toString());
            }
            if (j == 4) {
                LoadUse(header.get(j).toString());
            }
            if (j == 5) {
                LoadPhoto_type(header.get(j).toString());
            }
            if (j > 5) {
                LoadExtraGroup(header.get(j).toString(), (JList) extraList.get(j - 6));
            }
        }

      //  LoadSubject();
        // LoadGroup();
        //  LoadOrigin();
        //  LoadForm();
        //  LoadUse();
        //  LoadPhoto_type();
        if (adv_options > 0) {
            label_advanced_optns.setText("Advanced Options set, number of images selected: " + adv_options);//no of images selected in adv options
            label_advanced_optns.setForeground(Color.red);
        }

    }

    private ArrayList getGroupValues(JList groupList) {
        ListModel model = groupList.getModel();
        int total = 0;
        int i = 0;
        ArrayList selection = new ArrayList();
        for (int k = 0; k < model.getSize(); k++) {
            InstallData data = (InstallData) model.getElementAt(k);
            if (data.isSelected()) {
                selection.add(data.getName());
            }
        }
        return selection;
    }
    
    public void calcTotalImage(JList hh, ArrayList header) {
        int total = 0;
        int j = 0;
        ArrayList selection = getGroupValues(hh);

        String name = hh.getName();

        if (name.equals("subject"))//Selected values are added to array list
        {
            db.setSubject(selection);
        }
        if (name.equals("group")) {
            db.setGroup(selection);
        }
        if (name.equals("origin")) {
            db.setOrigin(selection);
        }
        if (name.equals("form")) {
            db.setForm(selection);
        }
        if (name.equals("use")) {
            db.setUse(selection);
        }

        if (name.equals("photo-type")) {
            db.setPhototype(selection);
        }
        //total = db.GetImageCount(name, header);//image count
        //total=db.GetImageCount(name);

        ArrayList allGroupValues = new ArrayList();
        int listCount = header.size();
        ArrayList modelArray = new ArrayList(); // holds the checkbox vals for each group

        /****
         * If there are anywhere between 1 and 6 groups, use the switch/case to
         * add only the appropriate hardcoded lists (list1, list2, etc.). Break
         * statements are deliberately left out so that, if there are, say, 5
         * groups, it'll waterfall down and add 5, 4, etc. The default case
         * should only fire if there are more than 6 groups (because of the
         * break above it), in which case all 6 hard-coded lists need to be
         * added, plus any extraList groups beyond 6.
        ****/

        switch (listCount) {

            case 6:
                allGroupValues.add(0, getGroupValues(list6));
                modelArray.add(0, list6.getModel());

            case 5:
                allGroupValues.add(0, getGroupValues(list5));
                modelArray.add(0, list5.getModel());

            case 4:
                allGroupValues.add(0, getGroupValues(list4));
                modelArray.add(0, list4.getModel());

            case 3:
                allGroupValues.add(0, getGroupValues(list3));
                modelArray.add(0, list3.getModel());

            case 2:
                allGroupValues.add(0, getGroupValues(list2));
                modelArray.add(0, list2.getModel());

            case 1:
                allGroupValues.add(0, getGroupValues(list1));
                modelArray.add(0, list1.getModel());
                break;

            case 0:  // should never be called, but just in case someone supplies a db with no group columns...
                break;

            default:
                allGroupValues = new ArrayList<ArrayList>(Arrays.asList(getGroupValues(list1), getGroupValues(list2),
                    getGroupValues(list3), getGroupValues(list4), getGroupValues(list5), getGroupValues(list6)));
                /*allGroupValues.add(getGroupValues(list1));
                allGroupValues.add(getGroupValues(list2));
                allGroupValues.add(getGroupValues(list3));
                allGroupValues.add(getGroupValues(list4));
                allGroupValues.add(getGroupValues(list5));
                allGroupValues.add(getGroupValues(list6));*/
                modelArray = new ArrayList<ListModel>(Arrays.asList(list1.getModel(), list2.getModel(), list3.getModel(), list4.getModel(), list5.getModel(), list6.getModel()));
                for (j = 0; j < extraList.size(); j++) {
                    allGroupValues.add(getGroupValues((JList) extraList.get(j)));
                    modelArray.add(((JList) extraList.get(j)).getModel());
                }
        }

        // Iterate through all the groups. If every group is clear of checked
        // items, that will be a signal later to reset all groups to all values.
        boolean allGroupsClear = true;
        for (Object groupValues : allGroupValues) {
            if (!((ArrayList) groupValues).isEmpty()) {
                allGroupsClear = false;
                break;
            }
        }

        try {
            // show/hide group options based on current selections so that only valid choices show
            ArrayList matchingResult = db.GetMatchingGroupItems(allGroupValues, name, header);
            ArrayList matchingItems = (ArrayList) matchingResult.get(1);

            // Element 2 in the result of GetMatchingGroupItems is the index, between
            // 0 and the number of groups minus 1, of the group that was clicked. This
            // allows us to later skip that group when adding or removing options.
            int selectedIndex = ((Integer) matchingResult.get(2)).intValue();

            // display image count from db.GetMatchingGroupItems
            imageCount = ((Integer) matchingResult.get(0)).intValue();
            imagecountLabel.setText("Total number of images selected: ");
            count.setText("" + imageCount);
            if (imageCount == 0) {
                count.setForeground(Color.RED);
            } else {
                count.setForeground(Color.BLACK);
            }

//            ArrayList names = new ArrayList<String>(Arrays.asList("subject", "group", "origin", "form", "use", "phototype"));
            /*ArrayList modelArray = new ArrayList<ListModel>(Arrays.asList(list1.getModel(), list2.getModel(), list3.getModel(), list4.getModel(), list5.getModel(), list6.getModel()));
            for (index = 0; index < extraList.size(); index++) {
                modelArray.add(((JList) extraList.get(index)).getModel());
            }*/
            Object matchingObject, modelObject;
            ArrayList matchingList, nameList;
            DefaultListModel currentModel;
            InstallData data, newData;
            String matchName;
            int numChecked;

            if (matchingItems != null) {
                for (j = 0; j < listCount; j++) {

                    // If the group just checked was selected, and if it was not
                    // the only group checked, skip if it has any checked values.
                    // This keeps us from immediately limiting to a checked value
                    // and removing all others in the group simply because no
                    // database lines have that value together with others.
                    // However, if multiple groups were selected, or if no groups
                    // were selected, go ahead and process. The selectedIndex will
                    // be -1 if only one group was selected or 0 groups were
                    // selected.
                    if (j == selectedIndex)
                        continue;

                    // First, get a DefaultListModel that we can modify. Someday it
                    // would be nice to define a SortedListModel to auto-sort, but
                    // for now, we'll do that manually.
                    modelObject = modelArray.get(j);
                    currentModel = (DefaultListModel) modelObject;
                    matchingObject = matchingItems.get(j);
                    matchingList = (ArrayList) matchingObject;
                    nameList = new ArrayList();
                    numChecked = ((ArrayList) allGroupValues.get(j)).size();

                    // Next, iterate through the model and record all the option
                    // names. As we do so, remove any elements that aren't in the
                    // matchingList. Note that we skip the group that was just clicked,
                    // since we don't really want to add or remove from a group while
                    // it's being clicked. (Otherwise, when you click a specific item,
                    // you'd get all the other ones to disappear since two items won't
                    // normally be on the same database line.)
                    for (int k = 0; k < currentModel.getSize(); k++) {
                        data = (InstallData) currentModel.getElementAt(k);
                        if (matchingList.indexOf(data.m_name) < 0) {
                            currentModel.removeElementAt(k);
                            k--;
                        }
                        else
                            nameList.add(data.m_name);
                    }

                    // Next, iterate through the matching names and add any not
                    // already present. These should be unchecked, as they were
                    // just made visible by virtue of checking another box.

                    for (Object matchNameObj : matchingList) {
                        matchName = matchNameObj.toString();
                        if (nameList.indexOf(matchName) < 0 && !matchName.isEmpty()) {
                            nameList.add(matchName);
                            newData = new InstallData(matchName);
                            modelAddAlpha(currentModel, newData, numChecked, currentModel.getSize() - 1);
                        }
                    }
                    
                }

                // Now that only the appropriate values are being shown, check to
                // see if any previously checked values have been removed. (Any
                // such values are passed as element 3 from the GetAllMatchingGroupItems
                // function.) If so, show a dialog box explaining this.
                if (MainForm.diffShow) {
                    ArrayList groupDiff = (ArrayList) matchingResult.get(3);
                    ArrayList groupDiffList, diffResultsList, diffResultsListVals;
                    ArrayList diffResults = new ArrayList();
                    for (j = 0; j < groupDiff.size(); j++) {
                        if (j == lastCheckedGroup && lastCheckedGroup == selectedIndex)
                            continue;
                        groupDiffList = (ArrayList) groupDiff.get(j);
                        if (!groupDiffList.isEmpty()) {
                            diffResultsList = new ArrayList();
                            diffResultsList.add(0, (String) header.get(j));
                            diffResultsList.add(1, (ArrayList) groupDiffList.clone());
                            diffResults.add(diffResultsList);
                        }
                    }
                    if (!diffResults.isEmpty()) {
                        String diffMessage = "The following values were checked, but have been removed because they no longer affect the number of selected images based on the box you just checked.\n\n";
                        String diffVals;
                        ArrayList diff;
                        for (Object diffObj : diffResults) {
                            diff = (ArrayList) diffObj;
                            diffVals = diff.get(1).toString();
                            diffMessage += (String) diff.get(0) + ": " + diffVals.substring(1, diffVals.length() - 1) + "\n";    
                        }
                        JDialog diffDialog = new DiffDialog((Dialog) this, true, diffMessage);
                        diffDialog.setLocationRelativeTo(this);
                        diffDialog.setVisible(true);
                    }
                }
            }
            lastCheckedGroup = selectedIndex;
        } catch (IOException ex) {
            Logger.getLogger(SelectImage2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* This function acts as a binary tree sorter for DefaultListModel. This has
       already been implmeneted in Java as a TreeSort, but we did it manually here
       to avoid implementation type headaches. Someday it can be changed to use
       Java native sorting.
     */
    private boolean modelAddAlpha (DefaultListModel model, Comparable data, int min, int max) {
        int compare = -1;
        int pos;
        while (max >= min) {  // once max is less than min, insert at max + 1
            pos = (max + min) / 2;
            compare = data.compareTo(model.get(pos));
            // If data is equal to the current model position, then we don't need
            // to add it. If data is greater, it's alphabetically later, so we
            // raise the minimum to the current position. Otherwise, it's
            // alphabetically earlier, so we lower the max to the current position.
            if (compare == 0)
                break;
            else if (compare > 0)
                min = pos + 1;
            else
                max = pos - 1;
        }
        if (compare == 0)
            return false;
        else {
            model.add(max + 1, data);
            return true;
        }
    }

    private void LoadExtraGroup(String header, JList groupList) {
        DefaultListModel listModel = new DefaultListModel();
        String[] groupValues = db.getValues(header);
        InstallData newData;
        for (String groupValue : groupValues) {
            newData = new InstallData(groupValue);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        groupList.setName(header.toLowerCase());
        groupList.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        groupList.setCellRenderer(renderer);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, groupList);
        groupList.addMouseListener(lst);
        groupList.addKeyListener(lst);
        groupList.setVisible(true);
        groupList.validate();
    }
    
    private void LoadSubject(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] subject = db.getValues(header);
        InstallData newData;
        for (String subject1 : subject) {
            newData = new InstallData(subject1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list1.setName(header.toLowerCase());
        this.list1.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list1.setCellRenderer(renderer);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list1);
        list1.addMouseListener(lst);
        list1.addKeyListener(lst);

    }

    private void LoadGroup(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] group = db.getValues(header);
        InstallData newData;
        for (String group1 : group) {
            newData = new InstallData(group1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list2.setName(header.toLowerCase());
        this.list2.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list2.setCellRenderer(renderer);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list2);
        list2.addMouseListener(lst);
        list2.addKeyListener(lst);
    }

    private void LoadOrigin(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] origin = db.getValues(header);
        InstallData newData;
        for (String origin1 : origin) {
            newData = new InstallData(origin1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list3.setName(header.toLowerCase());
        this.list3.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list3.setCellRenderer(renderer);
        list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list3);
        list3.addMouseListener(lst);
        list3.addKeyListener(lst);
    }

    private void LoadForm(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] form = db.getValues(header);
        InstallData newData;
        for (String form1 : form) {
            newData = new InstallData(form1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list4.setName(header.toLowerCase());
        this.list4.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list4.setCellRenderer(renderer);
        list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list4);
        list4.addMouseListener(lst);
        list4.addKeyListener(lst);
    }

    private void LoadUse(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] use = db.getValues(header);
        InstallData newData;
        for (String use1 : use) {
            newData = new InstallData(use1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list5.setName(header.toLowerCase());
        this.list5.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list5.setCellRenderer(renderer);
        list5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list5);
        list5.addMouseListener(lst);
        list5.addKeyListener(lst);
    }

    private void LoadPhoto_type(String header) {
        DefaultListModel listModel = new DefaultListModel();
        String[] phototype = db.getValues(header);
        InstallData newData;
        for (String phototype1 : phototype) {
            newData = new InstallData(phototype1);
            modelAddAlpha(listModel, newData, 0, listModel.getSize() - 1);
        }
        this.list6.setName(header.toLowerCase());
        this.list6.setModel(listModel);
        CheckListCellRenderer renderer = new CheckListCellRenderer();
        list6.setCellRenderer(renderer);
        list6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(this, list6);
        list6.addMouseListener(lst);
        list6.addKeyListener(lst);
    }
    /*private void LoadSubject()
     {
     DefaultListModel listModel = new DefaultListModel(); 
     String[] subject = db.getValues("subject");
     for(int i = 0; i < subject.length; i++)
     { 
     listModel.addElement(new InstallData(subject[i]));  
         
     }
     this.list1.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list1.setCellRenderer(renderer);
     list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list1);
     list1.addMouseListener(lst);
     list1.addKeyListener(lst); 
        
     }
     private void LoadGroup()
     {
     DefaultListModel listModel = new DefaultListModel(); 
     String[] group = db.getValues("Group");
     for(int i = 0; i < group.length; i++)
     {
     listModel.addElement(new InstallData(group[i]));  
     }
     this.list2.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list2.setCellRenderer(renderer);
     list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list2);
     list2.addMouseListener(lst);
     list2.addKeyListener(lst); 
     }
     private void LoadOrigin()
     {
     DefaultListModel listModel = new DefaultListModel();
     String[] origin = db.getValues("origin");
     for(int i = 0; i < origin.length; i++)
     {
     listModel.addElement(new InstallData(origin[i]));  
     }
     this.list3.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list3.setCellRenderer(renderer);
     list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list3);
     list3.addMouseListener(lst);
     list3.addKeyListener(lst); 
     }
     private void LoadForm()
     {
     DefaultListModel listModel = new DefaultListModel();
     String[] form = db.getValues("form");
     for(int i = 0; i < form.length; i++)
     {
     listModel.addElement(new InstallData(form[i])); 
     }
     this.list4.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list4.setCellRenderer(renderer);
     list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list4);
     list4.addMouseListener(lst);
     list4.addKeyListener(lst); 
     }
     private void LoadUse()
     {
     DefaultListModel listModel = new DefaultListModel();
     String[] use = db.getValues("use");
     for(int i = 0; i < use.length; i++)
     {
     listModel.addElement(new InstallData(use[i]));  
     }
     this.list5.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list5.setCellRenderer(renderer);
     list5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list5);
     list5.addMouseListener(lst);
     list5.addKeyListener(lst);
     }
     private void LoadPhoto_type()
     {
     DefaultListModel listModel = new DefaultListModel();
     String[] phototype = db.getValues("Photo-Type");
     for(int i = 0; i < phototype.length; i++)
     {
     listModel.addElement(new InstallData(phototype[i]));  
     }
     this.list6.setModel(listModel);  
     CheckListCellRenderer renderer = new CheckListCellRenderer();
     list6.setCellRenderer(renderer);
     list6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     CheckListener lst = new CheckListener(this,list6);
     list6.addMouseListener(lst);
     list6.addKeyListener(lst); 
     }*/

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        lblCaption = new javax.swing.JLabel();
        imagecountLabel = new javax.swing.JLabel();
        count = new javax.swing.JLabel();
        ch1 = new javax.swing.JCheckBox();
        ch2 = new javax.swing.JCheckBox();
        ch3 = new javax.swing.JCheckBox();
        ch4 = new javax.swing.JCheckBox();
        label_advanced_optns = new javax.swing.JLabel();
        jScrollPaneGroups = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel1A = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list1 = new javax.swing.JList();
        jPanel1B = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list2 = new javax.swing.JList();
        jPanel1C = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list3 = new javax.swing.JList();
        jPanel1D = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        list4 = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        jPanel1E = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        list5 = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jPanel1F = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        list6 = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Groups to Work With");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        jButton1.setText("OK");
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
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(435, 360, 90, 40);

        lblCaption.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCaption.setText("Please Make Your Selections:");
        getContentPane().add(lblCaption);
        lblCaption.setBounds(10, 11, 1027, 22);

        imagecountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        getContentPane().add(imagecountLabel);
        imagecountLabel.setBounds(35, 295, 199, 10);
        getContentPane().add(count);
        count.setBounds(252, 295, 39, 12);

        buttonGroup1.add(ch1);
        ch1.setText("Select by family");
        getContentPane().add(ch1);
        ch1.setBounds(20, 350, 131, 40);

        buttonGroup1.add(ch2);
        ch2.setText("Select by Genus");
        getContentPane().add(ch2);
        ch2.setBounds(20, 380, 131, 23);

        buttonGroup1.add(ch3);
        ch3.setText("Select by species-scientific name");
        getContentPane().add(ch3);
        ch3.setBounds(20, 400, 243, 23);

        buttonGroup1.add(ch4);
        ch4.setText("Select by species-common name");
        getContentPane().add(ch4);
        ch4.setBounds(20, 420, 240, 23);

        label_advanced_optns.setText("   ");
        getContentPane().add(label_advanced_optns);
        label_advanced_optns.setBounds(40, 310, 334, 10);

        jScrollPaneGroups.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel1A.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1A.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(170, 220));

        list1.setName("subject"); // NOI18N
        list1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(list1);

        jPanel1A.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jPanel1.add(jPanel1A);

        jPanel1B.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1B.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(170, 220));

        list2.setName("group"); // NOI18N
        list2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list2);

        jPanel1B.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jPanel1.add(jPanel1B);

        jPanel1C.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1C.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(170, 220));

        list3.setName("origin"); // NOI18N
        list3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list3ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(list3);

        jPanel1C.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jPanel1.add(jPanel1C);

        jPanel1D.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setPreferredSize(new java.awt.Dimension(170, 220));

        list4.setName("form"); // NOI18N
        list4.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list4ValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(list4);

        jPanel1D.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1D.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jPanel1.add(jPanel1D);

        jPanel1E.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane5.setPreferredSize(new java.awt.Dimension(170, 220));

        list5.setName("use"); // NOI18N
        list5.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list5ValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(list5);

        jPanel1E.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1E.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jPanel1.add(jPanel1E);

        jPanel1F.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setPreferredSize(new java.awt.Dimension(170, 220));

        list6.setName("phototype"); // NOI18N
        list6.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list6ValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(list6);

        jPanel1F.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1F.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 130, 20));

        jPanel1.add(jPanel1F);

        jScrollPaneGroups.setViewportView(jPanel1);

        getContentPane().add(jScrollPaneGroups);
        jScrollPaneGroups.setBounds(10, 60, 1060, 270);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // TODO add your handling code here:
        if (imageCount == 0) {
            JOptionPane.showMessageDialog(this, "There are currently no images selected.");
            return;
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        // TODO add your handling code here:
        boolean flag = true;
        if (imageCount == 0) {
            imagecountLabel.setText("Total number of images selected: ");
            count.setText("0");
            count.setForeground(Color.red);
            JOptionPane.showMessageDialog(this, "There are currently no images selected. \n Press escape to close the dialogue box without saving your selections.");
            //this.setVisible(false);
            return;

        } else if ((!ch1.isSelected()) && (!ch2.isSelected()) && (!ch3.isSelected()) && (!ch4.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please select one of the \"Select by\" check boxes below to determine what you'll be quizzed on.");

        } else {
            select = true;

            this.setVisible(false);

            db.getGroupFileNames(db.selectedRow, getTaxaLevel(), false, 0);
        }

    }//GEN-LAST:event_jButton1MouseReleased
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                esc = true;
                dialog.setVisible(false);
            }
        };
        dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public String getTaxaLevel() {
        if (ch1.isSelected()) {
            return "Family";
        } else if (ch2.isSelected()) {
            return "Genus";
        } else if (ch3.isSelected()) {
            return "Species";
        } else {
            return "Common Name";
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        close = true;
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        int keyCode = evt.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_formKeyPressed

    private void extraListValueChanged(javax.swing.event.ListSelectionEvent evt, JList currList) {
        list1.getCellRenderer().getListCellRendererComponent(currList, currList.getSelectedValue(), currList.getSelectedIndex(), true, true);
    }
    
    private void list1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list1ValueChanged
        // TODO add your handling code here:
        list1.getCellRenderer().getListCellRendererComponent(list1, list1.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list1ValueChanged

    private void list2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list2ValueChanged
        // TODO add your handling code here:
        list2.getCellRenderer().getListCellRendererComponent(list1, list2.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list2ValueChanged

    private void list3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list3ValueChanged
        // TODO add your handling code here:
        list2.getCellRenderer().getListCellRendererComponent(list1, list3.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list3ValueChanged

    private void list4ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list4ValueChanged
        // TODO add your handling code here:
        list2.getCellRenderer().getListCellRendererComponent(list1, list4.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list4ValueChanged

    private void list5ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list5ValueChanged
        // TODO add your handling code here:

        list2.getCellRenderer().getListCellRendererComponent(list1, list5.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list5ValueChanged

    private void list6ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list6ValueChanged
        // TODO add your handling code here:

        list2.getCellRenderer().getListCellRendererComponent(list1, list6.getSelectedValue(), list1.getSelectedIndex(), true, true);
    }//GEN-LAST:event_list6ValueChanged

    class CheckListCellRenderer extends JCheckBox
            implements ListCellRenderer {

        protected Border m_noFocusBorder
                = new EmptyBorder(1, 1, 1, 1);

        public CheckListCellRenderer() {
            super();
            setOpaque(true);
            setBorder(m_noFocusBorder);
        }

        public Component getListCellRendererComponent(JList list,
                Object value, int index, boolean isSelected, boolean cellHasFocus) {
  //  setSelected(isSelected);

            try {
                setText(value.toString());
                setBackground(isSelected ? list.getSelectionBackground()
                        : list.getBackground());
                setForeground(isSelected ? list.getSelectionForeground()
                        : list.getForeground());
                InstallData data = (InstallData) value;
                setSelected(data.isSelected());

                setFont(list.getFont());
                setBorder((cellHasFocus)
                        ? UIManager.getBorder("List.focusCellHighlightBorder")
                        : m_noFocusBorder);
            }
            catch (Exception e) {
                // DO SOMETHING
            }
            return this;
        }
    }

    class CheckListener implements MouseListener, KeyListener {

        protected SelectImage2 m_parent;
        protected JList m_list;

        public CheckListener(SelectImage2 parent, JList list) {
            m_parent = parent;
            m_list = list;
        }

        public void mouseClicked(MouseEvent e) {
//if (e.getX() < 20)
            doCheck();
            calcTotalImage(m_list, header);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == ' ') {
                doCheck();
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        private int getCheckedCount(DefaultListModel model) {
            int total = 0;
            InstallData data;
            for (int k = 0; k < model.getSize(); k++) {
                data = (InstallData) model.getElementAt(k);
                if (data.isSelected()) {
                    total++;
                }
            }
            return total;
        }
        
        protected void doCheck() {
            int min, max;
            int index = m_list.getSelectedIndex();
            if (index < 0) {
                return;
            }
            DefaultListModel model = (DefaultListModel) m_list.getModel();
            InstallData data = (InstallData) model.getElementAt(index);
            data.invertSelected();
            
            model.remove(index);
            
            if (data.m_selected) {
                min = 0;
                max = getCheckedCount(model) - 1;
            }
            else {
                min = getCheckedCount(model);
                max = model.getSize() - 1;
            }

            modelAddAlpha(model, data, min, max);
            if(data.m_selected) {
                index = model.indexOf(data);
                m_list.ensureIndexIsVisible(index);
            }
            
            m_list.repaint();
        }
    }

    class InstallData implements Comparable {

        protected String m_name;
        protected boolean m_selected;

        public InstallData(String name) {
            m_name = name;
            m_selected = false;
        }

        @Override
        public int compareTo(Object o) {
            if (o.getClass().toString().equalsIgnoreCase("InstallData")) {
                InstallData data = (InstallData) o;
                return m_name.compareToIgnoreCase(data.m_name);
            }
            else {
                return this.toString().compareToIgnoreCase(o.toString());
            }
        }

        public String getName() {
            return m_name;
        }

        public void setSelected(boolean selected) {
            m_selected = selected;
        }

        public void invertSelected() {
            m_selected = !m_selected;
        }

        public boolean isSelected() {
            return m_selected;
        }

        public String toString() {
            return m_name;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox ch1;
    private javax.swing.JCheckBox ch2;
    private javax.swing.JCheckBox ch3;
    private javax.swing.JCheckBox ch4;
    private javax.swing.JLabel count;
    private javax.swing.JLabel imagecountLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel1A;
    private javax.swing.JPanel jPanel1B;
    private javax.swing.JPanel jPanel1C;
    private javax.swing.JPanel jPanel1D;
    private javax.swing.JPanel jPanel1E;
    private javax.swing.JPanel jPanel1F;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPaneGroups;
    private javax.swing.JLabel label_advanced_optns;
    private javax.swing.JLabel lblCaption;
    private javax.swing.JList list1;
    private javax.swing.JList list2;
    private javax.swing.JList list3;
    private javax.swing.JList list4;
    private javax.swing.JList list5;
    private javax.swing.JList list6;
    // End of variables declaration//GEN-END:variables

}
