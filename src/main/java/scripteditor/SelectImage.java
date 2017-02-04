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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author  Preethy
 * This dialog box is used to select images based on the selected values in list box
 */
public class SelectImage extends javax.swing.JDialog {
    DataBaseDriver db;
    public boolean bCancel = false;
    boolean mUseCommonNames;
    public boolean close = false;
    int imageCount=0;//Added by preethy on 12-01-2012
    public static boolean esc=false;
    public boolean select=false;
    int adv_options=0;
    public SelectImage(DataBaseDriver db_driver,int maxImages){
        initComponents();
        addEscapeListener(this);
        db = db_driver;
        adv_options=maxImages;
        LoadSubject();
        LoadGroup();
        LoadOrigin();
        LoadForm();
        LoadUse();
        if(adv_options>0){
            label_advanced_optns.setText("Advanced Options set, number of images selected: "+adv_options);
            label_advanced_optns.setForeground(Color.red);
        }
        db.GetHeader();
   }
 public void calcTotalImage(JList hh) {
 ListModel model = hh.getModel();
 int total = 0;
 int i=0;
 String name=hh.getName();
 ArrayList selection = new ArrayList();
 for (int k=0; k<model.getSize(); k++) {
 InstallData data = (InstallData)model.getElementAt(k);
 if (data.isSelected()){
 selection.add(data.getName());
 }
 }
 if(name.equals("subject"))
    {
       db.setSubject(selection);

    }
     if(name.equals("group"))
    {
       db.setGroup(selection);
    }
      if(name.equals("origin"))
    {
       db.setOrigin(selection);
    }
       if(name.equals("form"))
    {
      db.setForm(selection);
    }
        if(name.equals("use"))
    {
       db.setUse(selection);
    }
 //total=db.GetImageCount(name);
 imageCount=0;
 imageCount=total;
 imagecountLabel.setText("Total number of images selected: ");
if(total==0)
{
count.setText(""+total);
count.setForeground(Color.red);

}
else
 count.setForeground(Color.BLACK);
 count.setText(""+total);
}
  private void LoadSubject()
    {
        DefaultListModel listModel = new DefaultListModel();
        String[] subject = db.getValues("Subject");
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
        String[] origin = db.getValues("Origin");
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
        String[] form = db.getValues("Form");
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
        String[] use = db.getValues("Use");
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        list2 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        list3 = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        list4 = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        list5 = new javax.swing.JList();
        imagecountLabel = new javax.swing.JLabel();
        count = new javax.swing.JLabel();
        ch1 = new javax.swing.JCheckBox();
        ch2 = new javax.swing.JCheckBox();
        ch3 = new javax.swing.JCheckBox();
        ch4 = new javax.swing.JCheckBox();
        label_advanced_optns = new javax.swing.JLabel();

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

        lblCaption.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblCaption.setText("Please Make Your Selections:");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Course");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Group");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Origin");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Use");

        jButton1.setText("Ok");
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

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Form");

        list1.setName("subject"); // NOI18N
        list1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(list1);

        list2.setName("group"); // NOI18N
        list2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list2);

        list3.setName("origin"); // NOI18N
        list3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list3ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(list3);

        list4.setName("form"); // NOI18N
        list4.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list4ValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(list4);

        list5.setName("use"); // NOI18N
        list5.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list5ValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(list5);

        imagecountLabel.setFont(new java.awt.Font("Tahoma", 1, 11));

        buttonGroup1.add(ch1);
        ch1.setText("Select by family");

        buttonGroup1.add(ch2);
        ch2.setText("Select by Genus");

        buttonGroup1.add(ch3);
        ch3.setText("Select by species-scientific name");

        buttonGroup1.add(ch4);
        ch4.setText("Select by species-common name");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCaption, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(59, 59, 59)
                                        .add(jLabel1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 91, Short.MAX_VALUE)
                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(68, 68, 68))
                                    .add(layout.createSequentialGroup()
                                        .add(15, 15, 15)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(ch2)
                                            .add(ch3)
                                            .add(ch4)
                                            .add(layout.createSequentialGroup()
                                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(18, 18, 18)
                                                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(ch1))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(2, 2, 2)
                                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(27, 27, 27)
                                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(layout.createSequentialGroup()
                                        .add(56, 56, 56)
                                        .add(jLabel3)
                                        .add(127, 127, 127)
                                        .add(jLabel6)))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(95, 95, 95)
                                        .add(jLabel5))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)
                                        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(9, 9, 9))))))
                    .add(layout.createSequentialGroup()
                        .add(35, 35, 35)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(label_advanced_optns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 334, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(imagecountLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(count, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(103, 103, 103)
                                .add(jButton1)))
                        .add(411, 411, 411)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(lblCaption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel5)
                    .add(jLabel6)
                    .add(jLabel3)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(44, 44, 44)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(count, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(imagecountLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(label_advanced_optns)
                        .add(13, 13, 13)
                        .add(ch1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(ch2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(ch3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ch4))
                    .add(layout.createSequentialGroup()
                        .add(33, 33, 33)
                        .add(jButton1)))
                .add(63, 63, 63))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // TODO add your handling code here:
        if(imageCount==0)
        {
            Utilities.MessageDialog(this, "There are currently no images selected.");
            return;
        }
        else
         this.setVisible(false);
    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        // TODO add your handling code here:
        boolean flag=true;
        if(imageCount==0)
        {
            imagecountLabel.setText("Total number of images selected: ");
            count.setText("0");
            count.setForeground(Color.red);
            Utilities.MessageDialog(this, "There are currently no images selected. \n Press escape to close the dialogue box without saving your selections.");
            this.setVisible(false);
            return;

        }else if((!ch1.isSelected()) && (!ch2.isSelected()) && (!ch3.isSelected()) && (!ch4.isSelected()))
        {
            Utilities.MessageDialog(this, "Please select one of the check box.");

        }
        else{
            select=true;

            this.setVisible(false);

             db.getGroupFileNames(db.selectedRow, getTaxaLevel(), false, 0);
        }

    }//GEN-LAST:event_jButton1MouseReleased
public static void addEscapeListener(final JDialog dialog) {
    ActionListener escListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            esc=true;
            dialog.setVisible(false);        }    };
    dialog.getRootPane().registerKeyboardAction(escListener,KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),JComponent.WHEN_IN_FOCUSED_WINDOW);
}
 public String getTaxaLevel(){
        if(ch1.isSelected())
            return "Family";
        else if(ch2.isSelected())
            return "Genus";
        else if(ch3.isSelected())
            return "Species";
        else
            return "Common Name";
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        close=true;
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
         int keyCode = evt.getKeyCode();
        if(keyCode == KeyEvent.VK_ESCAPE){
            this.setVisible(false);
        }
    }//GEN-LAST:event_formKeyPressed

    private void list1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list1ValueChanged
        // TODO add your handling code here:
        list1.getCellRenderer().getListCellRendererComponent(list1,list1.getSelectedValue(),list1.getSelectedIndex(),true,true);
    }//GEN-LAST:event_list1ValueChanged

    private void list2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list2ValueChanged
        // TODO add your handling code here:
        list2.getCellRenderer().getListCellRendererComponent(list1,list1.getSelectedValue(),list1.getSelectedIndex(),true,true);
    }//GEN-LAST:event_list2ValueChanged

    private void list3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list3ValueChanged
        // TODO add your handling code here:
         list2.getCellRenderer().getListCellRendererComponent(list1,list1.getSelectedValue(),list1.getSelectedIndex(),true,true);
    }//GEN-LAST:event_list3ValueChanged

    private void list4ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list4ValueChanged
        // TODO add your handling code here:
         list2.getCellRenderer().getListCellRendererComponent(list1,list1.getSelectedValue(),list1.getSelectedIndex(),true,true);
    }//GEN-LAST:event_list4ValueChanged

    private void list5ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list5ValueChanged
        // TODO add your handling code here:
         list2.getCellRenderer().getListCellRendererComponent(list1,list1.getSelectedValue(),list1.getSelectedIndex(),true,true);
    }//GEN-LAST:event_list5ValueChanged

class CheckListCellRenderer extends JCheckBox
implements ListCellRenderer
{
protected  Border m_noFocusBorder =
new EmptyBorder(1, 1, 1, 1);
public CheckListCellRenderer() {
super();
setOpaque(true);
setBorder(m_noFocusBorder);
}
public Component getListCellRendererComponent(JList list,
Object value, int index, boolean isSelected, boolean cellHasFocus)
{
  //  setSelected(isSelected);
setText(value.toString());
setBackground(isSelected ? list.getSelectionBackground() :
list.getBackground());
setForeground(isSelected ? list.getSelectionForeground() :
list.getForeground());
InstallData data = (InstallData)value;
setSelected(data.isSelected());

setFont(list.getFont());
setBorder((cellHasFocus) ?
UIManager.getBorder("List.focusCellHighlightBorder")
: m_noFocusBorder);
return this;
}
}
 class CheckListener implements MouseListener, KeyListener
{
protected SelectImage m_parent;
protected JList m_list;
public CheckListener(SelectImage parent,JList list) {
m_parent = parent;
m_list = list;
}
public void mouseClicked(MouseEvent e) {
//if (e.getX() < 20)
doCheck();
calcTotalImage(m_list);
}
public void mousePressed(MouseEvent e) {}
public void mouseReleased(MouseEvent e) {}
public void mouseEntered(MouseEvent e) {}
public void mouseExited(MouseEvent e) {}

public void keyPressed(KeyEvent e) {
if (e.getKeyChar() == ' ')
doCheck();
}
public void keyTyped(KeyEvent e) {}
public void keyReleased(KeyEvent e) {}
protected void doCheck() {
int index = m_list.getSelectedIndex();
if (index < 0)
return;
InstallData data = (InstallData)m_list.getModel().
getElementAt(index);
data.invertSelected();
m_list.repaint();
}
}
 class InstallData
{
protected String m_name;
protected boolean m_selected;
public InstallData(String name) {
m_name = name;
m_selected = false;
}
public String getName() { return m_name; }
public void setSelected(boolean selected) {
m_selected = selected;
}
public void invertSelected() { m_selected = !m_selected; }
public boolean isSelected() { return m_selected; }
public String toString() { return m_name; }
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel label_advanced_optns;
    private javax.swing.JLabel lblCaption;
    private javax.swing.JList list1;
    private javax.swing.JList list2;
    private javax.swing.JList list3;
    private javax.swing.JList list4;
    private javax.swing.JList list5;
    // End of variables declaration//GEN-END:variables

}
