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

/*
 * pr_jtable.java
 *
 * Created on Jan 20, 2012, 1:04:55 PM
 */
package scripteditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Tel
 */
public class Search_table extends javax.swing.JDialog implements ActionListener {

    /** Creates new form pr_jtable
     *
     */
    String[] list ;
    String[] items;
    Object[][] data ;
    String[] prev;
    DefaultTableModel model;
    String[] txasel ;
    String lev;
    String[] mTaxa;
    String  taxaLevel;
    String searchLevel;
    MainForm mainF;
    TableColumn tc;
    public boolean close = false;
    boolean search=false;//This flag is used to check search done or not
    Object[] columns;
    DataBaseDriver db;
    boolean exist=false;
    boolean nullTaxaSearch=false;
    public Search_table(String[] mTaxa,String taxonomicLevel, MainForm obj,int maxImages) {

        this.mainF = obj;//parent form obj
        this.mTaxa=mTaxa;
        taxaLevel=taxonomicLevel;

        columns = new Object[] {"Select", "Common Name","Family","Genus","Species","Common Name 2"}; //columns of search table

        model = new DefaultTableModel(data, columns);
        // model1 = new DefaultTableModel(data, columns1);
        jTable1 = new JTable(model);
        jTable1.setCellSelectionEnabled(true);

        jScrollPane1 = new JScrollPane(jTable1);
        tc = jTable1.getColumnModel().getColumn(0);
        tc.setCellEditor(jTable1.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(jTable1.getDefaultRenderer(Boolean.class));

        initTable();
        this.pack();
//         Image image=null;
//        try {
//            image=ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/icon.jpg"));
//           //  image=ImageIO.read(new File(Configuration.UserPath() + "/Graphics/icon.jpg"));
//        } catch (IOException ex) {
//            Logger.getLogger(Search_table.class.getName()).log(Level.SEVERE, null, ex);
//        }
//          setIconImage(image);

//        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
//        setIconImage(icon);
//
          if(maxImages >0){//maxImage is the no of images selected in the advanced options
            jLabel1.setText("Advanced Options set, number of images selected: "+maxImages);
            jLabel1.setForeground(Color.red);
          }
      /*   TableColumn column = null;
    for (int i = 0; i < 6; i++) {
        column = jTable1.getColumnModel().getColumn(i);

        if (i == 0) {
            column.setPreferredWidth(30);
        } else if (i == 1) {
            column.setPreferredWidth(150);
        }
        else {
            column.setPreferredWidth(50);
        }
    }*/
    }
      private void initTable(){

          setTitle("Search");
          setResizable(false);
        jLabel5= new javax.swing.JLabel("Search Item");
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField(10);

        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
         jButton3.setText("Cancel");
        buttonGroup1 = new javax.swing.ButtonGroup();
        ch1 = new javax.swing.JCheckBox();
        ch2 = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

       jButton1.setText("Search");
       jButton5.setText("Add Selected Taxa");
        buttonGroup1.add(ch1);
        ch1.setText("Genus");

        buttonGroup1.add(ch2);
        ch2.setText("Common Name");
         jLabel1.setText("");
       jButton1.addActionListener(this);
        jButton5.addActionListener(this);
         jButton3.addActionListener(this);

        jTextField1.setText("");
      jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
         jScrollPane1 = new JScrollPane(jTable1);
        add(jScrollPane1);

      layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(28, 28, 28)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ch1)
                                .addGap(18, 18, 18)
                                .addComponent(ch2)
                                .addGap(16, 16, 16)
                                .addComponent(jButton1))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jButton3)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(ch1)
                            .addComponent(ch2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
        );


        pack();
      }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jButton3.setText("jButton3");

        jButton4.setText("jButton4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        jTable1.setVisible(false);
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:

        close=true;
        this.setVisible(false);
        //mainF.showMenuButtons();

    }//GEN-LAST:event_formWindowClosing
    private void jTextField2MouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        jTextField1.setText("");
    }
    private void jTextField1MouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
       // jTextField2.setText("");
    }
    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:

         int keyCode = evt.getKeyCode();
          if (keyCode == KeyEvent.VK_ENTER)
          {
              String genus="";
                String cName="";
               if(ch1.isSelected()){
             genus=jTextField1.getText().trim();
            }else{
              cName= jTextField1.getText().trim();
            }

        try{
            if((!ch1.isSelected()) && (!ch2.isSelected()))
            {
                Utilities.MessageDialog(this, "Please select one of the check boxes.");

            }
             if((genus.isEmpty())&&(cName.isEmpty())) {

                 Utilities.MessageDialog(this, "Please enter values in the text box.");
             }
             else if(ch1.isSelected()&& !genus.isEmpty()) {
                 search=true;
               serchItem(genus);//search by genus

            } else if(ch2.isSelected() && (!cName.isEmpty())) {
                search=true;
                 searchCname(cName);//search by common name
            }
          /*  else
            {
                search=true;
               searchCname(cName);
            }*/

        }catch(Exception ex){}


          }
          getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
    }

    /**
     * @param args the command line arguments
     */private void fun(String[] list,String name){


         for (int i = 0; i < model.getRowCount(); i++) {

        }

        for (int i = 0; i < list.length; i++) {

            model.insertRow(i,new Object[] {false,"", list[i],name});//inserting search values to jtable

        }

     }



    @Override
    public void actionPerformed(ActionEvent e) {
        SortedSet taxa=new TreeSet();
         SortedSet grptaxa=new TreeSet();
       if(e.getSource()==jButton1){
           String genus = "";
           String cName = "";
            if(ch1.isSelected()){
             genus=jTextField1.getText().trim();
            }else{
              cName= jTextField1.getText().trim();
            }



        try{
             if((!ch1.isSelected()) && (!ch2.isSelected()))
            {
                Utilities.MessageDialog(this, "Please select one of the check boxes.");

            }else if((genus.isEmpty())&&(cName.isEmpty())) {
                Utilities.MessageDialog(this, "Please enter values in  the text box.");
            }
             else if((!genus.isEmpty())&&(cName.isEmpty())) {
                 search=true;
               serchItem(genus);//`search by genus

            } else if((genus.isEmpty())&&(!cName.isEmpty())) {
                search=true;
                 searchCname(cName);//search by common name
            }
         /*   else
            {
                search=true;
               searchCname(cName);
            }*/

        }catch(Exception ex){}

       }
      else if(e.getSource()==jButton5){

            boolean flag=false;
            ArrayList val=new ArrayList();
            exist=false;
            if(mTaxa !=null)
            {

                 //if((ch1.isSelected())&&(!jTextField1.getText().isEmpty()))
                if(ch1.isSelected())
                {
                     searchLevel="Genus";
                     val=new ArrayList();
                     for(int i = 0; i < model.getRowCount(); i++) {
                        if((Boolean)model.getValueAt(i, 0)==true){

                        String str=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,3 );
                        val.add(str);
                        boolean isexist = false;
                        for(int j=0;j<mTaxa.length;j++)
                         {
                             if(mTaxa[j].equals(str))
                             {
                                 flag=true;
                                isexist = true;
                             break;
                             }
                         }
                      grptaxa.add(str);
                        if(!isexist)
                            taxa.add(str);

                        }
                     }
                  } else if(ch2.isSelected())
                    {

                        searchLevel="Common Name";
                        val=new ArrayList();

                         for(int i = 0; i < model.getRowCount(); i++) {
                          if((Boolean)model.getValueAt(i, 0)==true){
                          String str1=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,1);
                          val.add(str1);
                          boolean isexist = false;
                          for(int j=0;j<mTaxa.length;j++)
                           {
                             if(mTaxa[j].equals(str1))
                             {
                               flag=true;
                               isexist = true;
                               break;
                             }

                            }
                           grptaxa.add(str1);
                          if(!isexist)
                              taxa.add(str1);
                          }
                         }
                   }
               /*   else{

                       searchLevel="Common Name";
                        val=new ArrayList();
                         for(int i = 0; i < model.getRowCount(); i++) {
                          if((Boolean)model.getValueAt(i, 0)==true){

                          String str1=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,1);
                          boolean isexist = false;
                          val.add(str1);
                          for(int j=0;j<mTaxa.length;j++)
                           {
                             if(mTaxa[j].equals(str1))
                             {
                                  flag=true;
                               isexist = true;
                               break;
                             }

                            }
                           grptaxa.add(str1);
                          if(!isexist)
                              taxa.add(str1);
                          }

                          }
                    }*/
                  String[]  list2= db.treeToList(grptaxa);
                if(taxa.size()>0)
                {
                String[]  list= db.treeToList(taxa);
                addSelectedTaxa(list,taxaLevel,list2);
                }else if(search==false)
                {
                    Utilities.MessageDialog(this, "Cannot add taxa.");
                }else if(val.size()==0)
                {
                    Utilities.MessageDialog(this, "Please make any selections to add taxa.");
                }

                else if((taxa.size()==0) && (search==true) &&(flag==true)){

                  exist=true;

                   addSelectedTaxa(list,taxaLevel,list2);

                }

            }
            else{

                 int count=0;
                  for(int i = 0; i < model.getRowCount(); i++) {
                          if((Boolean)model.getValueAt(i, 0)==true){
                              count++;
                          }
                          }
                 if(search==false)
                {
                    Utilities.MessageDialog(this, "Cannot add taxa.");
                }else if((count==0)){
                    Utilities.MessageDialog(this, "Please make any selections to add taxa.");
                }
                  else{
                       if(ch1.isSelected())
                        {
                     searchLevel="Genus";
                     val=new ArrayList();
                     for(int i = 0; i < model.getRowCount(); i++) {

                        if((Boolean)model.getValueAt(i, 0)==true){

                        String str=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,3 );
                        val.add(str);
                        taxa.add(str);

                        }
                     }
                  } else if(ch2.isSelected())
                    {

                         searchLevel="Common Name";
                            val=new ArrayList();
                         for(int i = 0; i < model.getRowCount(); i++) {
                          if((Boolean)model.getValueAt(i, 0)==true){

                          String str1=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,1);
                          val.add(str1);

                              taxa.add(str1);
                          }

                          }
                     }
                  else{

                       searchLevel="Common Name";
                        val=new ArrayList();
                         for(int i = 0; i < model.getRowCount(); i++) {
                          if((Boolean)model.getValueAt(i, 0)==true){

                          String str1=(String)model.getValueAt(i,2 )+" "+(String)model.getValueAt(i,1);
                          val.add(str1);
                          taxa.add(str1);

                          }

                          }
                    }
                      if(taxa.size()>0)
                     {
                       String[]  list= db.treeToList(taxa);
                       String[]  list2= db.treeToList(grptaxa);

                       addSelectedTaxa(list,taxaLevel,list2);
                       mainF.isFromTexaSelect = false;
                     }
                }
             }
       }
      else if(e.getSource()==jButton3){

           this.setVisible(false);
        }


    }
    private void addSelectedTaxa(String[] list,String level,String[] list2){
                            SearchRes rs=new SearchRes(this.mainF,this,searchLevel,level,exist);
                            rs.currentSet(mTaxa);
                            rs.setItems(list);
                            rs.setItems2(list2);
                            rs.setLocationRelativeTo(this);
                            rs.setVisible(true);

     }
     public String[] returntaxa()
     {
         return txasel;

     }
    private void Cnfun(String[] family, String[] list, String[] species,String[] CN1, String[] CN2, String[] CN3, String[] CN4,String com) {

        Object[] columns = new Object[] {"Select","CommonName", "Family","Genus","Species","CommonName2"};

             TableColumn column = null;
     for (int i = 0; i < 6; i++) {
        column = jTable1.getColumnModel().getColumn(i);

        if (i == 0) {
            column.setPreferredWidth(30);
        } else if (i == 1) {
            column.setPreferredWidth(90);
        }else if (i == 4) {
            column.setPreferredWidth(70);
        }else if (i == 5) {
            column.setPreferredWidth(60);
        }
        else {
            column.setPreferredWidth(50);
        }
    }

        for (int i = 0; i < list.length; i++) {
            if(list[i]!=null){
              /*  if(com.equals("Com3")){
                   model.insertRow(i,new Object[] {false,CN1[i], family[i],list[i],species[i],CN3[i]});
                }
                else if(com.equals("Com4")){
                   model.insertRow(i,new Object[] {false,CN1[i], family[i],list[i],species[i],CN4[i]});
                }else*/
                 model.insertRow(i,new Object[] {false,CN1[i], family[i],list[i],species[i],CN2[i]});
            }
        }

    }

    void AddSelected(String[] mTaxa,String taxal) {
      //  throw new UnsupportedOperationException("Not yet implemented");
      if(taxal!=null){
     for(int i=0;i<mTaxa.length;i++){

        prev=mTaxa;
         lev=taxal;
     }
     }
    }

    private class MyTableModel extends DefaultTableModel {
         private Object[][] data;
        private Object[] columns;
        public MyTableModel(Object[][] data, Object[] columns) {
            this.data = data;
            this.columns = columns;        }
        public Class getColumnClass(int columnIndex) {
            return data[0][columnIndex].getClass();        }
        public int getColumnCount() {
            return columns.length;        }
        public int getRowCount() {
            return data.length;        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];        }
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;        }
    }

  CheckBoxCellEditor cbh;
  CheckBoxCellEditor rendererComponent;
   public CheckBoxCellEditor getRendererComponent() {
        return rendererComponent;
    }
    public void setRendererComponent(CheckBoxCellEditor rendererComponent) {
        rendererComponent.setText("Check All");
        this.rendererComponent = rendererComponent;
    }
      class CheckBoxCellEditor extends JCheckBox implements TableCellRenderer, MouseListener {
      protected int column;
      protected boolean mousePressed = false;
      ItemListener it1;
      public CheckBoxCellEditor(ItemListener itemListener) {
          setRendererComponent(this);
          this.it1 = itemListener;
          rendererComponent.addItemListener(it1);
      }
      public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        if (table != null) {
          JTableHeader header = table.getTableHeader();
          if (header != null) {
            rendererComponent.setForeground(header.getForeground());
            rendererComponent.setBackground(header.getBackground());
            rendererComponent.setFont(header.getFont());
            header.addMouseListener(rendererComponent);
          }
        }
        setColumn(column);
        //setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return rendererComponent;
      }
      protected void setColumn(int column) {
        this.column = column;
      }
      public int getColumn() {
        return column;
      }
      protected void handleClickEvent(MouseEvent e) {
        if (mousePressed) {
          mousePressed=false;
          JTableHeader header = (JTableHeader)(e.getSource());
          JTable tableView = header.getTable();
          TableColumnModel columnModel = tableView.getColumnModel();
          int viewColumn = columnModel.getColumnIndexAtX(e.getX());
          int column = tableView.convertColumnIndexToModel(viewColumn);
          if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {
              doClick();
          }
        }
      }
      public void mouseClicked(MouseEvent e) {
        handleClickEvent(e);
        ((JTableHeader)e.getSource()).repaint();
      }
      public void mousePressed(MouseEvent e) {
        mousePressed = true;
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
    }


    String it;
        public void serchItem(String Genus)
     {   int flag=0,k=0;
          db = new DataBaseDriver(Configuration.DataBaseName());
           BufferedReader buffer = db.myFileReader();
           String[] gen = db.getTaxa("Genus");//this array contains the genus names from database
           SortedSet gList = new TreeSet();
           String s1;
           try{

                if(jTextField1.getText().contains("and")){
                      String s2,s3;
                        String arr[]=jTextField1.getText().split("and");
                        s2=arr[0];
                        s3=arr[1];
                  for(int j=0;j<gen.length;j++){//compairing values of text box and genus names
                    if((gen[j].toLowerCase().startsWith(s2.toString().trim())) || (gen[j].toLowerCase().startsWith(s3.toString().trim())))
                    {
                        gList.add(gen[j]);
                    }
                   }
                }if(jTextField1.getText().endsWith("*")){ //searching the names ends with star.this will search all of genus name starting from the text before star
                   s1=jTextField1.getText().substring(0,jTextField1.getText().length()-1);
                    for(int j=0;j<gen.length;j++){
                   if(gen[j].toLowerCase().contains(s1))
                      {
                        gList.add(gen[j]);
                      }
                    }
                }
                else{
                for(int j=0;j<gen.length;j++){
                    if(gen[j].toLowerCase().contains(jTextField1.getText()))
                    {
                        gList.add(gen[j]);
                    }
                }

                }
             }catch(Exception e){ }

        if(gList.size()==0){
                 for (int i = model.getRowCount()-1; i >=0; i--) {
                          model.removeRow(i);

                    }
                  model.getDataVector().removeAllElements();

                  model.fireTableDataChanged();

                  jTable1.repaint();
                  Utilities.MessageDialog(this, "No matching item found.");
         }
        else
        {
             try{
                  for (int i = model.getRowCount()-1; i >=0; i--) {
                          model.removeRow(i);
                    }
                  model.getDataVector().removeAllElements();

                  model.fireTableDataChanged();

                  jTable1.repaint();

            String[] name = db.treeToList(gList);

           for( int i=0;i<name.length;i++){

              String it=name[i];
              String[] list = db.getFamily(it);

              fun(list,it); //adding seach results to jtable
           }
            TableColumn cols3 = jTable1.getColumnModel().getColumn(5);
            jTable1.removeColumn(cols3);
            TableColumn cols2 = jTable1.getColumnModel().getColumn(4);
            jTable1.removeColumn(cols2);
            TableColumn cols1 = jTable1.getColumnModel().getColumn(1);
            jTable1.removeColumn(cols1);
           }catch(Exception e){ }
        }
     }


          //Search by Common Name
   private void searchCname(String item) {
       boolean flag1=false,flag2=false,flag3=false,flag4=false;
       boolean c1=false,c2=false,c3=false,c4=false;
       boolean AndFlag =false;
       boolean star=false;
       boolean single=false;
       boolean one=false;//for checking whether the search result contain  only one value or not
       boolean many=false;//for checking whether the search result contain multiple value or not
       int total=0;
       db = new DataBaseDriver(Configuration.DataBaseName());

        String[] cn =null;
        int flag=0;
        SortedSet cList = new TreeSet();
        SortedSet cList2 = new TreeSet();
        SortedSet cList3 = new TreeSet();
        SortedSet cList4 = new TreeSet();

        try{

            String[]  cName = db.getTaxa("Common Name");//selecting common name 1 from database
          //   String[] cname3=db.getTaxa("Common Name3");//Commented by preethy on 20--04-2012
          //  String[] cname4=db.getTaxa("Common Name4");
          // String[] cname2=db.getTaxa("Common Name2");
            String[] cname2=db.getTaxa3("common name 2");//common name 2 from db
            String[] cname3=db.getTaxa3("common name 3");//common name 3 from db
            String[] cname4=db.getTaxa3("common name 4");//common name4
            String s1;
            String search_item=jTextField1.getText();
             if(jTextField1.getText().endsWith("*"))//searching all values from database starting with the strings before star
                {
                  total=0;
                   star=true;
                   s1=jTextField1.getText().substring(0,jTextField1.getText().length()-1);
                   for(int j=0;j<cName.length;j++){
                   if(cName[j].toLowerCase().contains(s1))
                      {
                        flag1=true;
                        c1=true;
                        cList.add(cName[j]);
                        total++;
                      }
                    }

                      for(int j=0;j<cname2.length;j++){
                      if(cname2[j].toLowerCase().contains(s1))
                        {

                            flag2=true;
                             c2=true;
                            cList2.add(cname2[j]);
                           total++;
                        }
                      }

                      for(int j=0;j<cname3.length;j++){
                      if(cname3[j].toLowerCase().contains(s1))
                        {
                          flag3=true;
                           c3=true;
                          cList3.add(cname3[j]);
                          total++;
                        }
                       }

                       for(int j=0;j<cname4.length;j++){
                       if(cname4[j].toLowerCase().contains(s1))
                         {
                           flag4=true;
                            c4=true;
                           cList4.add(cname4[j]);
                           total++;
                        }
                        }
                        if(total==1){
                          one=true;
                         // single=false;
                      }else if(total>1){
                          many=true;
                         // single=false;
                      }


                }
              else if(search_item.contains("and")&& search_item.substring(search_item.lastIndexOf("and")-1,search_item.lastIndexOf("and")).contains(" ") && search_item.substring(search_item.lastIndexOf("and")+3,search_item.lastIndexOf("and")+4).contains(" "))
                      //search_item.substring(search_item.lastIndexOf("and")-1,search_item.lastIndexOf("and")).equals("") && search_item.substring(search_item.lastIndexOf("and")+3,search_item.lastIndexOf("and")+4).equals(""))
                {        //search data containg and
                        String s2,s3;
                        String arr[]=jTextField1.getText().split("and");
                        s2=arr[0];//first string
                        s3=arr[1];//second string

                        boolean first=false,second=false;


                          AndFlag=true;

                    for(int j=0;j<cName.length;j++){

                    if(cName[j].toLowerCase().contains(s2.toString().trim()) || cName[j].toLowerCase().contains(s3.toString().trim()))
                        {//compairing common name1 with s1 and s2.if they are equal then added to array list

                            c1=true;
                            if(cName[j].toLowerCase().contains(s2.toString().trim())){

                              cList.add(cName[j]);
                              first=true;
                            }
                            if(cName[j].toLowerCase().contains(s3.toString().trim())){

                             cList.add(cName[j]);
                             second=true;
                            }
                         }
                    }
                    if(first==false || second==false){
                       for(int j=0;j<cname2.length;j++){

                          if(cname2[j].toLowerCase().contains(s2.toString().trim()) || cname2[j].toLowerCase().contains(s3.toString().trim()))
                            {
                              c2=true;
                                if(first==false){
                                if(cname2[j].toLowerCase().contains(s2.toString().trim()))
                                {
                                    cList2.add(cname2[j]);
                                    first=true;
                                }
                              }
                              if(second==false){
                                 if(cname2[j].toLowerCase().contains(s3.toString().trim())){
                                    cList2.add(cname2[j]);
                                    second=true;
                                 }
                              }
                            }
                       }
                    }
                   if(first==false || second==false){
                      for(int j=0;j<cname3.length;j++){
                         if(cname3[j].toLowerCase().contains(s2.toString().trim()) || cname3[j].toLowerCase().contains(s3.toString().trim()))
                           {

                               c3=true;

                               if(first==false){
                                if(cname3[j].toLowerCase().contains(s2.toString().trim()))
                                {
                                  cList3.add(cname3[j]);
                                  first=true;
                                }
                               }
                               if(second==false){
                                 if(cname3[j].toLowerCase().contains(s3.toString().trim())){
                                     cList3.add(cname3[j]);
                                     second=true;
                                }
                              }

                           }
                       }

                    }
                   if(first==false || second==false){

                      for(int j=0;j<cname4.length;j++){
                        if(cname4[j].toLowerCase().contains(s2.toString().trim()) || cname4[j].toLowerCase().contains(s3.toString().trim()))
                         {

                           c4=true;
                           if(first==false){
                           if(cname4[j].toLowerCase().contains(s2.toString().trim())){
                           cList4.add(cname4[j]);
                           first=true;
                           }
                           }
                           if(second==false){
                            if(cname4[j].toLowerCase().contains(s3.toString().trim())){
                           cList4.add(cname4[j]);
                           second=true;
                           }
                          }
                         }
                      }
                   }
                 }
                 else{

                    single=true;
                    for(int j=0;j<cName.length;j++)
                    {
                    if(cName[j].toLowerCase().contains(jTextField1.getText().trim()))//compairing serach data with common name1
                        {

                            c1=true;
                            flag1=true;
                            cList.add(cName[j]);//if it is equal then added to array list
                            total++;

                        }
                    }

                       for(int j=0;j<cname2.length;j++){
                          if(cname2[j].toLowerCase().contains(jTextField1.getText().trim()))
                            {

                                c2=true;
                               flag2=true;
                              cList2.add(cname2[j]);
                              total++;

                            }
                       }


                      for(int j=0;j<cname3.length;j++){
                         if(cname3[j].toLowerCase().contains(jTextField1.getText().trim()))
                           {

                             c3=true;
                             flag3=true;
                             cList3.add(cname3[j]);
                             total++;

                           }
                       }
                     for(int j=0;j<cname4.length;j++){
                        if(cname4[j].toLowerCase().contains(jTextField1.getText().trim()))
                         {

                            c4=true;
                           flag4=true;
                           cList4.add(cname4[j]);
                           total++;

                         }
                      }
                      if(total==1){
                          one=true;
                          single=false;
                      }else if(total>1){
                          many=true;
                          single=false;
                      }

                 }
          }catch(Exception e){
                 System.out.println(e);
        }

        if((cList.size()==0) && (cList2.size()==0) && (cList3.size()==0) && (cList4.size()==0)){
                  for (int i = model.getRowCount()-1; i >=0; i--) {
                          model.removeRow(i);

                    }
                  model.getDataVector().removeAllElements();

                  model.fireTableDataChanged();

                  jTable1.repaint();
                  Utilities.MessageDialog(this, "No matching item found.");
        }
        else{


        try{
               for (int i = model.getRowCount()-1; i >=0; i--) {
                          model.removeRow(i);
                  }
                  model.getDataVector().removeAllElements();
                  model = new DefaultTableModel(data, columns);
                  model.fireTableDataChanged();

                 jTable1.setModel(model);
                 tc = jTable1.getColumnModel().getColumn(0);
                 tc.setCellEditor(jTable1.getDefaultEditor(Boolean.class));
                 tc.setCellRenderer(jTable1.getDefaultRenderer(Boolean.class));
                 jTable1.repaint();

             int p=0;int idx=0,cn1,cn2=0,cn3=0,cn4=0; int count=0;
              String[] name = db.treeToList(cList);
              cn1=db.GetIndex2("common name");
              cn2=db.GetIndex2("common name 2");
              cn3=db.GetIndex2("common name 3");
              cn4=db.GetIndex2("common name 4");

             if(AndFlag){
               //selected common name size is stored in count
                 if(c1)
                     count=count+cList.size();
                 if(c2)
                     count=count+cList2.size();
                 if(c3)
                     count=count+cList3.size();
                 if(c4)
                     count=count+cList4.size();
             }
            // else if(star || single){
                else if(single){
                if(c1)
                     count=count+cList.size();
                 if(c2)
                     count=count+cList.size();
                 if(c3)
                     count=count+cList.size();
                 if(c4)
                     count=count+cList.size();

             }
             else if(one){

                if(c1){
                     count=count+cList.size();
                      name = db.treeToList(cList);
                     }
                 if(c2){
                     count=count+cList2.size();
                     name = db.treeToList(cList2);
                      }
                 if(c3){
                     count=count+cList3.size();
                     name = db.treeToList(cList3);
                 }
                 if(c4){
                     count=count+cList4.size();
                     name = db.treeToList(cList4);
                 }
             } else if(many){
                 count=total;//if search result is many then total is assigned to count
              }
             else{

                count=name.length;}

            String[] genlist = new String[count];
            String[] species=new String[count];
            String[] family=new String[count];
            String[] CN2=new String[count];
            String[] CN1=new String[count];
            String[] CN3=new String[count];
            String[] CN4=new String[count];

            String com="";

            if(AndFlag){

                ArrayList a1=new ArrayList();
                ArrayList a2=new ArrayList();
                ArrayList a3=new ArrayList();
                ArrayList a4=new ArrayList();
                ArrayList a5=new ArrayList();
                ArrayList a6=new ArrayList();
                ArrayList a7=new ArrayList();
                int j=0;

               if(c1){

                   //  idx=-15;
                       idx=cn1;
               String[] name1 = db.treeToList(cList);
               j=name1.length;
                ArrayList sel=new ArrayList();

               for(int i=0;i<name1.length;i++){
                String it=name1[i];
                  sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {
                     String[] rec = (String[])sel.get(k);
                     if(!a1.contains(rec[1]))
                     {

                     a1.add(rec[1]);
                     a2.add(rec[2]);
                     a3.add(rec[3]);
                     a4.add(rec[4]);

                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                     a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                                  if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }
                     }
                  /*  String[] rec = (String[])sel.get(k);
                     if(!a1.contains(rec[1]))
                     a1.add(rec[1]);
                    if(!a2.contains(rec[2]))
                     a2.add(rec[2]);
                    if(!a3.contains(rec[3]))
                     a3.add(rec[3]);
                    if(!a4.contains(rec[4]))
                     a4.add(rec[4]);

                     if(!a5.contains(rec[cn2])){

                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                     a5.add(rec[cn2]); }}
                     if(!a6.contains(rec[cn3])){
                     if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }}
                    if(!a7.contains(rec[cn4])){

                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }}*/
                   /*  if(!a5.contains(rec[11])){
                    if(rec[11].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[11]); }}
                     if(!a6.contains(rec[12])){
                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }}
                    if(!a7.contains(rec[13])){
                    if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }} */


                 }
               }

               }
                if(c2){

                   // idx=-16;
                      idx=cn2;
               String[] name1 = db.treeToList(cList2);

               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {

                    String[] rec = (String[])sel.get(k);
                    if(!a5.contains(rec[cn2]))
                    {

                    a1.add(rec[1]);
                    a2.add(rec[2]);
                    a3.add(rec[3]);
                    a4.add(rec[4]);

                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }
                    }
                 /*  String[] rec = (String[])sel.get(k);
                   if(!a1.contains(rec[1]))
                    a1.add(rec[1]);
                    if(!a2.contains(rec[2]))
                   a2.add(rec[2]);
                    if(!a3.contains(rec[3]))
                    a3.add(rec[3]);
                    if(!a4.contains(rec[4]))
                   a4.add(rec[4]);

                      if(!a5.contains(rec[cn2])){
                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }}
                     if(!a6.contains(rec[cn3])){
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }}
                    if(!a7.contains(rec[cn4])){
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }}*/



               /*      if(!a5.contains(rec[11])){
                  if(rec[11].equals(""))
                   a5.add("");
                  else{

                     a5.add(rec[11]); }}
                      if(!a6.contains(rec[12])){
                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }}
                    if(!a7.contains(rec[13])){
                    if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }} */

                 }

                  j++;
               }

                 }

                 if(c3)
                 {

                    // idx=-17;
                     idx=cn3;
                String[] name1 = db.treeToList(cList3);

               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);
                  for(int k=0;k<sel.size();k++)
                 {

                     String[] rec = (String[])sel.get(k);
                    if(!a6.contains(rec[cn3]))
                    {

                    a1.add(rec[1]);
                    a2.add(rec[2]);
                    a3.add(rec[3]);
                    a4.add(rec[4]);

                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }
                    }
                 /*  String[] rec = (String[])sel.get(k);
                   if(!a1.contains(rec[1]))
                    a1.add(rec[1]);
                    if(!a2.contains(rec[2]))
                   a2.add(rec[2]);
                    if(!a3.contains(rec[3]))
                    a3.add(rec[3]);
                    if(!a4.contains(rec[4]))
                   a4.add(rec[4]);

                      if(!a5.contains(rec[cn2])){
                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }}
                     if(!a6.contains(rec[cn3])){
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }}
                    if(!a7.contains(rec[cn4])){
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }}*/
                /*     if(!a5.contains(rec[11])){
                  if(rec[11].equals(""))
                   a5.add("");
                  else{

                     a5.add(rec[11]); }}
                      if(!a6.contains(rec[12])){
                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }}
                    if(!a7.contains(rec[13])){
                    if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }} */

                 }
                  j++;
               }

                 }
                 if(c4){

                   //  idx=-18;
                     idx=cn4;
               String[] name1 = db.treeToList(cList4);
               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {
                    String[] rec = (String[])sel.get(k);
                    if(!a7.contains(rec[cn4]))
                    {

                    a1.add(rec[1]);
                    a2.add(rec[2]);
                    a3.add(rec[3]);
                    a4.add(rec[4]);

                     if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }

                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }
                    }
                  /* String[] rec = (String[])sel.get(k);//check here
                   if(!a1.contains(rec[1]))
                    a1.add(rec[1]);
                    if(!a2.contains(rec[2]))
                   a2.add(rec[2]);
                    if(!a3.contains(rec[3]))
                    a3.add(rec[3]);
                    if(!a4.contains(rec[4]))
                   a4.add(rec[4]);

                      if(!a5.contains(rec[cn2])){
                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }}
                     if(!a6.contains(rec[cn3])){
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }}
                    if(!a7.contains(rec[cn4])){
                    if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }}*/
                /*     if(!a5.contains(rec[11])){
                  if(rec[11].equals(""))
                   a5.add("");
                  else{

                     a5.add(rec[11]); }}
                      if(!a6.contains(rec[12])){
                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }}
                    if(!a7.contains(rec[13])){
                    if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }} */

                 }
                  j++;
               }
                 }

                String s2,s3;
                String arr[]=jTextField1.getText().split("and");
                s2=arr[0];
                s3=arr[1];

                String[] comn=new String[count];
                 int i=-1;
                boolean check_name=false;//this flag is used to check whether the hit is in cmn2,cm3,cm4
                //s2.toString().trim())

              for(int m=0;m<a4.size();m++){
                     check_name=false;

                     family[m]=a4.get(m).toString();
                      genlist[m]=a2.get(m).toString();
                      species[m]=a3.get(m).toString();
                      try{
                          CN1[m]=a1.get(m).toString();
                      }
                      catch(Exception e){
                           CN1[m]="";
                      }

                     try{

                        if(a5.get(m).toString().equals(s2.toString().trim()) || a5.get(m).toString().equals(s3.toString().trim())){

                             //CN2[m]=a5.get(m).toString();
                             comn[++i]=a5.get(m).toString();
                             check_name=true;
                        }
                       }
                       catch(Exception e){
                         //comn[++i]="";
                       }
                      try{

                           if(a6.get(m).toString().equals(s2.toString().trim()) || a6.get(m).toString().equals(s3.toString().trim())){
                           //CN2[m]=a6.get(m).toString();
                           comn[++i]=a6.get(m).toString();
                           check_name=true;
                          }
                      }
                       catch(Exception e){
                         // comn[++i]="";
                      }
                      try{
                          if(a7.get(m).toString().equals(s2.toString().trim()) || a7.get(m).toString().equals(s3.toString().trim())){
                          //CN2[m]=a7.get(m).toString();
                          comn[++i]=a7.get(m).toString();
                          check_name=true;
                          }
                      }
                       catch(Exception e){
                          //comn[++i]="";
                      }
                       if(!check_name) {
                           comn[++i]=a5.get(m).toString();
                       }

             /*    try{
                      CN2[m]=a5.get(m).toString();
                       }
                      catch(Exception e){
                           CN2[m]="";
                      }
                       try{
                      CN3[m]=a6.get(m).toString();
                       }
                      catch(Exception e){
                           CN3[m]="";
                      }
                        try{
                      CN4[m]=a7.get(m).toString();
                       }
                      catch(Exception e){
                           CN4[m]="";
                      }*/
                 }
              CN2=comn;//copy common names to array CN2

            }else if(star || single || many){

                ArrayList a1=new ArrayList();
                ArrayList a2=new ArrayList();
                ArrayList a3=new ArrayList();
                ArrayList a4=new ArrayList();
                ArrayList a5=new ArrayList();
                ArrayList a6=new ArrayList();
                ArrayList a7=new ArrayList();
                boolean check_name=false;
                int j=0;
                  String[] comn=new String[count];
                  int c=-1;
               if(c1){

                  // idx=-15;
                idx=cn1;
               String[] name1 = db.treeToList(cList);
               j=name1.length;
                ArrayList sel=new ArrayList();

               for(int i=0;i<name1.length;i++){
                String it=name1[i];
                  sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {
                    String[] rec = (String[])sel.get(k);
                     if(!a1.contains(rec[1])){
                     a1.add(rec[1]);
                     a2.add(rec[2]);
                     a3.add(rec[3]);
                     a4.add(rec[4]);
                  /*  if(rec[11].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[11]); }
                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }
                   if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }*/
                      if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                   if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }

                 }
                 }
               }

               }
                if(c2){
                   // idx=-16;
                    idx=cn2;
               String[] name1 = db.treeToList(cList2);

               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {

                   String[] rec = (String[])sel.get(k);
                   if(!a1.contains(rec[1])){
                    a1.add(rec[1]);
                    a2.add(rec[2]);
                    a3.add(rec[3]);
                    a4.add(rec[4]);
               /*  if(rec[11].equals(""))
                   a5.add("");
                  else{
                    a5.add(rec[11]); }
                   if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }
                   if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }*/
                     if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                   if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }

                 }
                 }
                  j++;
               }

                 }

                 if(c3)
                 {

                   //  idx=-17;
                     idx=cn3;
                String[] name1 = db.treeToList(cList3);

               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);
                  for(int k=0;k<sel.size();k++)
                 {

                   String[] rec = (String[])sel.get(k);
                   if(!a1.contains(rec[1])){

                    a1.add(rec[1]);

                   a2.add(rec[2]);

                    a3.add(rec[3]);

                   a4.add(rec[4]);

                /*  if(rec[11].equals(""))
                   a5.add("");
                  else{

                     a5.add(rec[11]); }

                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }

                    if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }*/
                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                   if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }

                 }
                 }
                  j++;
               }

                 }
                 if(c4){

                   //  idx=-18;
                     idx=cn4;
                String[] name1 = db.treeToList(cList4);
               for(int i=0;i<name1.length;i++){
                String it=name1[i];

                 ArrayList sel=db.getRows(it,idx);

                  for(int k=0;k<sel.size();k++)
                 {

                   String[] rec = (String[])sel.get(k);//check here
                   if(!a1.contains(rec[1])){
                    a1.add(rec[1]);

                    a2.add(rec[2]);

                    a3.add(rec[3]);

                   a4.add(rec[4]);
                    if(rec[cn2].equals(""))
                     a5.add("");
                    else{
                    a5.add(rec[cn2]); }
                    if(rec[cn3].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[cn3]); }
                   if(rec[cn4].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[cn4]); }

                /*  if(rec[11].equals(""))
                   a5.add("");
                  else{

                     a5.add(rec[11]); }

                    if(rec[12].equals(""))
                     a6.add("");
                    else{
                    a6.add(rec[12]); }
                   if(rec[13].equals(""))
                     a7.add("");
                    else{
                    a7.add(rec[13]); }*/

                 }
                 }
                  j++;
               }
                 }

                 for(int m=0;m<a4.size();m++){
                     check_name=false;
                     family[m]=a4.get(m).toString();
                      genlist[m]=a2.get(m).toString();
                      species[m]=a3.get(m).toString();
                      try{
                          CN1[m]=a1.get(m).toString();
                      }
                      catch(Exception e){
                           CN1[m]="";
                      }
                     if(!star){
                      try{

                        if(a5.get(m).toString().contains(jTextField1.getText())){

                             comn[++c]=a5.get(m).toString();
                             check_name=true;
                             continue;
                        }
                       }
                       catch(Exception e){
                        // comn[++c]="";
                       }
                      try{

                        if(a6.get(m).toString().contains(jTextField1.getText())){

                             comn[++c]=a6.get(m).toString();
                              check_name=true;
                              continue;
                        }
                       }
                       catch(Exception e){
                        // comn[++c]="";
                       }
                      try{

                        if(a7.get(m).toString().contains(jTextField1.getText())){

                             comn[++c]=a7.get(m).toString();
                              check_name=true;
                              continue;
                        }
                       }
                       catch(Exception e){
                        // comn[++c]="";
                       }
                       if(!check_name) {
                           comn[++c]=a5.get(m).toString();
                       }
                     // CN2=comn;
                      }
                      else{
                      try{
                      CN2[m]=a5.get(m).toString();
                       }
                      catch(Exception e){
                           CN2[m]="";
                      }
                       try{
                      CN3[m]=a6.get(m).toString();
                       }
                      catch(Exception e){
                           CN3[m]="";
                      }
                        try{
                      CN4[m]=a7.get(m).toString();
                       }
                      catch(Exception e){
                           CN4[m]="";
                      }
                      }
                 }
                 if(!star)
                  CN2=comn;
            }
            else{

            if(flag1){
               // idx=-15;
                idx=cn1;
            }
            if(flag2){
                //idx=-16;
                idx=cn2;
            }
            if(flag3){
                //idx=-17;
                idx=cn3;
            }
            if(flag4){
               // idx=-18;
                idx=cn4;
            }

            for(int i=0;i<name.length;i++){

              String it=name[i];

               ArrayList sel=db.getRows(it,idx);//selected rows from db

               for(int k=0;k<sel.size();k++)
               {
                   String[] rec = (String[])sel.get(k);

                   CN1[i]=rec[1] ;//com1

                    genlist[i]= rec[2];//genus

                    species[i]=rec[3];//species

                   family[i]=rec[4];//family
                   if(rec[cn2].equals(jTextField1.getText()))
                   {
                    CN2[i]= rec[cn2] ;
                   }
                   else if(rec[cn3].equals(jTextField1.getText())){
                    CN2[i]= rec[cn3] ;
                   }
                   else if(rec[cn4].equals(jTextField1.getText())){
                    CN2[i]= rec[cn4] ;
                   }
               /*   if(rec[11].equals(""))
                    CN2[i]= "";
                 else
                    CN2[i]=rec[11] ;
                   if(rec[12].equals(""))
                    CN3[i]= "";
                 else
                    CN3[i]=rec[12] ;
                   if(rec[13].equals(""))
                    CN4[i]= "";
                 else
                    CN4[i]=rec[13] ;*/
                    if(rec[cn2].equals(""))
                    CN2[i]= "";
                 else
                    CN2[i]=rec[cn2] ;
                   if(rec[cn3].equals(""))
                    CN3[i]= "";
                 else
                    CN3[i]=rec[cn3] ;
                   if(rec[cn4].equals(""))
                    CN4[i]= "";
                 else
                    CN4[i]=rec[cn4] ;

               }

               }
            /* if(c3){
                 com="Com3";
             }else if(c4){
                com="Com4";
            }*/
             if(c3){
                 CN2=CN3;
             }else if(c4){
               CN2=CN4;
            }

             //}
          }

            Cnfun(family,genlist,species,CN1,CN2,CN3,CN4,com);//Adding selected values to jtable

        }catch(Exception e){
            e.printStackTrace();
        }

        }
 }


    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;

     private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel5;

    private javax.swing.JButton jButton5;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox ch1;
    private javax.swing.JCheckBox ch2;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
