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
 * MainForm.java
 *
 * Created on May 14, 2006, 10:02 PM
 */

package scripteditor;

//import com.sun.swing.internal.plaf.metal.resources.metal;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.text.*;
import java.util.zip.ZipInputStream;
import javax.swing.filechooser.FileFilter;


public class MainForm extends javax.swing.JFrame {
    
    //String userPath = System.getenv("USERPROFILE");
    int sqORt; // Identifies to the program if Study Quiz or Test has been selected. 1 = Study 2 = Quiz 3 = Test
    boolean showProgressQuiz = false;  // shows progress to user after a quiz has finished
    boolean showProgressTest = false;  // shows progress to user after a test has finished
    boolean viewSplash; // If true the splash screen is shown at startup.
    boolean closeMainF;
    String mUserName; // This will be the user's name and will be set at start up.'
    boolean scriptMode = false; // Tells if a script has been loaded or not. Locks Options if true.
    double delayTime = 1.0; // Display times..
    int mode = 1; // Only four modes 1 - 4, Default is 1..
    int maxImages = 0;
    double delayQT = 1.0;
    boolean mRandomize = false;
    boolean mAutoImageDisplay = false;
    boolean mShowImageWithName = true;
    boolean mShowFamilyName = false;
    boolean mKeyToContinue = false;
    boolean mCommonNames = false;
    boolean mCommonNamesRandom = false;
    boolean studying = false;
    boolean helpPop = false;
    static boolean diffShow = true;
    int numTaxaRandom = 1;
    double mFixationTime = .5;  // Time to show the fixation point throughout program!
    int programState = 0; // This will tell what state the program is in at any given time.
    StudyImageClass mStudyClass;
    QuizClass quiz;
    TestClass test;
    String[] mTaxa; // These are the taxa that will be studied.
    String taxonomicLevel; // This is the level that will be studied.
    boolean mKeyBusy = false;
    Window w = new Window(this);
    ProgressClass progress = new ProgressClass();
    AboutClass about;
    boolean showHelp = true;
    DisplayMode OldDisplayMode; // This is the old displaysettings on program startup/ Must reset them back on close    JPanel testPanel = new JPanel();
    String userFirstLine; // This is the first line of the user file!!
    boolean DataBaseCheckEnabled = false; //Whether to check for a modified database or not
    //Added by preethy on 12-01-2012
    ArrayList sel=new ArrayList();//used for storing the selected image names from group selection box
    boolean selCname = false;
    boolean taxanull=false;//checking whether taxa selected or not
    boolean selctfamilyTaxanull=false;//used for checking whether select by family is selected or not
    boolean group=false;//it is used for checking whether the group selection box is checked or not
    boolean prgres=false;
    boolean selScientificName = false;
    boolean btnOkClicked = false;
    public boolean isFromTexaSelect=false;
    
    /***/
    //////////////////////////////////////////////////////////////////
    /// SCRIPT DATA MEMBERS !!!
    String mScriptFileName;
    String mScriptName;
    int mSessionCount;
    SessionClass[] mySessionsArray;
    int mGlobalSessionIndex = 0; // Index for which session from script mode to run..
    File ScriptDirectory = null;
    public boolean inSession = true; // Tells the Script results writer if it needs to create a new file or add to the existion one.
    
    String savedTaxaSetsPath = null;//added by anurag  
    //private Image image;
    /**
     * Creates new form MainForm
     */
    PropertyFileReader propertyFileReader = null;
    //private Image image;
 
    /** Creates new form MainForm */
    
    public MainForm() {
        
       
        initComponents();
        
        
        try {
            propertyFileReader = new PropertyFileReader();
            this.savedTaxaSetsPath = propertyFileReader.getPropertyValue("savedTaxaSetsFolderName");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
         Image image=null;
        try {
            // image=new ImageIcon(Configuration.ApplicationPath() + "/Graphics/icon.jpg");
             //image=ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/icon.icon"));
             image=ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/icon.png"));
             //image=ImageIO.read(new File(Configuration.UserPath() + "/Graphics/icon.jpg"));
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
           setIconImage(icon);
        }
         setIconImage(image);
        
//        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
//        setIconImage(icon);
              
//        setExtendedState(MAXIMIZED_BOTH);
        GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
        this.setSize(new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()));
        
        mnuFile.setMnemonic(java.awt.event.KeyEvent.VK_F);
        mnuLoadScript.setMnemonic(java.awt.event.KeyEvent.VK_L);
        //mnuLoadScript.setAccelerator();
        //KeyEvent.VK_T, ActionEvent.ALT_MASK));

        //mnuReset.setMnemonic(java.awt.event.KeyEvent.VK_R);
        mnuViewProgress.setMnemonic(java.awt.event.KeyEvent.VK_V);
        mnuRun.setMnemonic(java.awt.event.KeyEvent.VK_R);
        mnuStudy.setMnemonic(java.awt.event.KeyEvent.VK_S);
        mnuQuiz.setMnemonic(java.awt.event.KeyEvent.VK_Q);
        mnuTest.setMnemonic(java.awt.event.KeyEvent.VK_T);
        mnuOptions.setMnemonic(java.awt.event.KeyEvent.VK_O);
        mnuHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
        //mnuHelpTopics.setMnemonic(java.awt.event.KeyEvent.VK_H);
        //mnuAbout.setMnemonic(java.awt.event.KeyEvent.VK_A);
       
        
        showSplashScreen();
        JCDelay(3); // Delay time for the splash screen to be shown!!
        EventQueue.invokeLater( new SplashScreenCloser() );
                
        
        this.showLoginScreen();
//        // Test validation of database
//        if (DataBaseCheckEnabled) {
//            VerifyDatabase objVerifyDB = new VerifyDatabase(Configuration.DataBaseName(), Configuration.DataBaseHashValue());
//            if (!objVerifyDB.IsOK()) {
//                JOptionPane.showMessageDialog(this, "The DataBase has been modified. Please reinstall the program or replace the database with the original file.");
//                System.exit(0);
//
//            }
//        objVerifyDB = null;
//        }
        
        if(closeMainF){
            System.exit(0);
        }
        
       // FolderCheck();//Commented by preethy on 16-05-2012
        
        ///  Set the main help Popup from user file if needing to display
        FileReader myReader;
      //  String path = Configuration.UserPath();//getApplicationPath(false);
        String path = Configuration.ApplicationPath();//getApplicationPath(false);
        try{
            myReader = new FileReader(path + "/UserFiles/" + mUserName + ".csv");
            BufferedReader inputfile = new BufferedReader(myReader);
            String firstLine;
            try{
                firstLine = inputfile.readLine();
                userFirstLine = firstLine;
                firstLine = firstLine.substring(firstLine.length()-1);
                int flag;   
                flag = Integer.parseInt(firstLine);
                if(flag == 0){
                    showHelp = false;
                }
            } catch(IOException ioe){
                
            }
            
        } catch(FileNotFoundException fnf){ }   
        
      
        setButtonIcons(); // set the menu buttons for main screen
        setProgress(); // reads from user file and sets View Progress!!
        
        // Call a paint method here!!
        this.getContentPane().update(this.getGraphics());
    
          }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">
    
    
      //Splash Screen Code
    private static SplashScreen fSplashScreen;
    
    private static void showSplashScreen(){
        fSplashScreen = new SplashScreen();
        fSplashScreen.splash();
        
        
    }
    
    private static final class SplashScreenCloser implements Runnable {
        @Override
        public void run(){
            fSplashScreen.dispose();
        }
    }
    private void setButtonIcons(){
        
        File dir = new File(".");
        Image tempIcon = null;
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton3.jpg"));// application path changed to ApplicationPath
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btQuiz.setIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton3Roll.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btQuiz.setRolloverIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton4.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btTest.setIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton4Roll.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btTest.setRolloverIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton2.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btStudy.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton2Roll.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btStudy.setRolloverIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/myButton1.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btTaxaSelect.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/button1RollOver.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btTaxaSelect.setRolloverIcon(new ImageIcon(tempIcon));
     // Added by preethy on 04-01-2012
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/r1.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btScript.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/r1RollOver.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btScript.setRolloverIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/sp1.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSpecies.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/sp1RollOver.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSpecies.setRolloverIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/sr1.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSearch.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/sr1RollOver.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSearch.setRolloverIcon(new ImageIcon(tempIcon));
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/Gpsel.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSelectByGroup.setIcon(new ImageIcon(tempIcon));
        
        try {
            tempIcon = ImageIO.read(new File(Configuration.ApplicationPath() + "/Graphics/GpselRollOver.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.btSelectByGroup.setRolloverIcon(new ImageIcon(tempIcon));
        //***//
    }
   
   // private void showMenuButtons(){//commented by preethy
    public void showMenuButtons(){
        this.remove(jPanel1);
        jMenuBar1.setVisible(true);
        this.getContentPane().add(panelMenuButtons);
        panelMenuButtons.setLocation((int)(this.getWidth()-panelMenuButtons.getWidth())/2, (int)(this.getHeight()-panelMenuButtons.getHeight())/3);
        panelMenuButtons.setVisible(true);
        btQuiz.repaint();
        btTest.repaint();
        btTaxaSelect.repaint();
        btStudy.repaint();
       //Added by preethy on 04-01-2012
        btScript.repaint();
        btSpecies.repaint();
        btSearch.repaint();
        btSelectByGroup.repaint();
        /****/
    } 
    
    private void setWorkingState(){
        jMenuBar1.setVisible(false);
        this.validate();
        this.repaint();
        this.remove(panelMenuButtons);
        jPanel1.setLocation(0,0);
        jPanel1.setSize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
        this.getContentPane().add(jPanel1);
        lbPictureBox.setLocation(0,0);
        lbPictureBox.setSize(jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.setVisible(true);
       
 
        
    }
    
    private void mnuQuizActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        takeQuiz();
    }
    
    private void mnuStudyActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
          studyImages();
    }
    
    private void mnuAboutMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:
        getAbout();
    }
    
    private void mnuHelpTopicsMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:   
     
    }
    
    private void mnuOptionTaxaSelectionMouseReleased(java.awt.event.MouseEvent evt) {
      /*  
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
      */
        /* if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        
      /*  panelMenuButtons.setVisible(false);
        selectTaxa(db);
        panelMenuButtons.setVisible(true);
        db = null;*/
    }
    
    private void mnuOptionStudyMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:
        showStudyOptions();
        //setStudy();
    }
    
    private void mnuTestMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:
        //takeTest();
    }
    
    private void mnuQuizMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:
        //takeQuiz();
    }
    
    private void mnuStudyMouseReleased(java.awt.event.MouseEvent evt) {
// TODO add your handling code here:
        //studyImages();
    }
    
    private void mnuViewProgressMouseReleased(java.awt.event.MouseEvent evt) {
        
// TODO add your handling code here:
      /*  progress.setModal(true);
        progress.setLocationRelativeTo(this);
        progress.ShowResults();
        progress.setVisible(true);*/
       
    }
    
    private void mnuResetActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void mnuOpenActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    // Resets script File...
    private void mnuResetMouseReleased(java.awt.event.MouseEvent evt) {
        // clearScriptFile();
    }
    
    private void mnuResetMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }
    
    private void mnuResetKeyReleased(java.awt.event.KeyEvent evt) {
           // TODO add your handling code here:
    }
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        //System.out.println("key presses......."+evt.getKeyCode()+": "+programState);
        if (mKeyBusy == true) {
            return;
        }
        mKeyBusy = true;
        int keyCode = evt.getKeyCode();
 
        if (programState == 0) {
            if (keyCode == KeyEvent.VK_O && evt.isControlDown()) {
                //openScriptFile();
            } else if (keyCode == KeyEvent.VK_R && evt.isControlDown()) {
                DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
               /* if (db.DataBaseExists() == false) {
                    JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
                    return;
                }*/
                selectTaxa(db);
                db = null;
            } else if (keyCode == KeyEvent.VK_L && evt.isControlDown()) {
                openScript();
            } else if (keyCode == KeyEvent.VK_F1) {
                getMainHelp();
            } else if (keyCode == KeyEvent.VK_F2) {
                shortcutKeys dlgCut = new shortcutKeys();
                dlgCut.setLocationRelativeTo(this);
                dlgCut.setModal(true);
                dlgCut.setVisible(true);

            } else if (keyCode == KeyEvent.VK_V && evt.isControlDown()) {
                progress.setModal(true);
                progress.setLocationRelativeTo(this);
                progress.ShowResults();
                progress.setVisible(true);
            } else if (keyCode == KeyEvent.VK_S && evt.isControlDown()) {
                studyImages();
            } else if (keyCode == KeyEvent.VK_Q && evt.isControlDown()) {
                takeQuiz();
            } else if (keyCode == KeyEvent.VK_T && evt.isControlDown()) {
                takeTest();
            //} else if (keyCode == KeyEvent.VK_A && evt.isControlDown()) {
            //    getAbout();
            } else if (keyCode == KeyEvent.VK_F1) {
                getMainHelp();
            }
                 //Added by Preethy on 19-01-2012   
              else if (keyCode == KeyEvent.VK_B && evt.isControlDown()) {
               selectSpecies();
           } else if (keyCode == KeyEvent.VK_F && evt.isControlDown()) {
                search();
           }else if (keyCode == KeyEvent.VK_G && evt.isControlDown()) {
                selectByGroup();
           }
            else if (keyCode == KeyEvent.VK_S && evt.isAltDown()) {
                jMenuItem6ActionPerformed(null);
            }
            else if (keyCode == KeyEvent.VK_L && evt.isAltDown()) {
                jMenuItem7ActionPerformed(null);
            }
            
            /*****/
        } else if (programState == 1) { // WHEN IN STUDY MODE
            
            if ((keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT) && !mAutoImageDisplay) 
            {

                if (!mStudyClass.nextImage()) {
                    JOptionPane.showMessageDialog(this, "No more images. \n\r Press CTRL + E or ESCAPE to Exit");
                }
            } 
            else if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT) && !mAutoImageDisplay) 
            {
                mStudyClass.prevImage();
            } 
            else if ((keyCode == KeyEvent.VK_E && evt.isControlDown()) || keyCode == KeyEvent.VK_ESCAPE) 
            {
                
                mStudyClass.paused(true);
              
                if(JOptionPane.showConfirmDialog(this, "Are you sure you wish to quit this study session?", 
                        "Exit to Main Menu?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                
                    mStudyClass.mDisplayLabel.setIcon(null);
                    mStudyClass.mDisplayLabel.setText("");
                    mStudyClass.mDisplayLabel.removeAll();
                    mStudyClass.stopStudying();
                } else {
                    
                    
                    mStudyClass.paused(false);
                }
               
            }

            if (mKeyToContinue && keyCode != KeyEvent.VK_CONTROL) {
                mStudyClass.keyHit();
            }


        } //else if(programState == 2){ // When in QUIZ MODE
//            if(keyCode == KeyEvent.VK_E && evt.isControlDown()) {
//                quiz.paused(true);
//                int ret = JOptionPane.showConfirmDialog(this, "Are you sure you want to Quit?", "Quit", JOptionPane.YES_NO_OPTION);
//                if(ret == JOptionPane.YES_OPTION){
//                    quiz.Running = false;
//                } else{
//                    quiz.paused(false);
//                }
//            }
//        }

//        else if(programState == 3){ // When in Test MODE
//            if(keyCode == KeyEvent.VK_E && evt.isControlDown()) {
//                test.paused(true);
//                int ret = JOptionPane.showConfirmDialog(this, "Are you sure you want to Quit?", "Quit", JOptionPane.YES_NO_OPTION);
//                if(ret == JOptionPane.YES_OPTION){
//                    test.Running = false;
//                } else{
//                    test.paused(false);
//                }
//            }
//        }
        mKeyBusy = false;
    }
    
    private void formKeyReleased(java.awt.event.KeyEvent evt){
 
    }
    // This is the Open Menu Item...
    private void mnuOpenMouseReleased(java.awt.event.MouseEvent evt) {
        // openScriptFile();  For other versions of IQ
        
    }
    // This is the Exit Menu Item ...
    private void mnuExitMouseReleased(java.awt.event.MouseEvent evt) {
        //exitFunction();
// TODO add your handling code here:
    }
    
    private void studyImages() {
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
        db.isFromTexaSelect = this.isFromTexaSelect;    
      /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
      
        setWorkingState();
       // if(!group){

        if(taxonomicLevel == null || mTaxa == null || mTaxa.length == 0){
           /* int retVal = JOptionPane.showConfirmDialog(this, "There are no taxa selected for studying.\n\rWould you like to select taxa now?", "No Taxa Selected!", JOptionPane.YES_NO_OPTION);
            if(retVal == JOptionPane.YES_OPTION){
                selectTaxa(db);
                studyImages(); // brings user back to studying after taxa selection..
            } else showMenuButtons();
            db = null;
            return;*/
             JOptionPane.showMessageDialog(this, "There are no taxa selected.\n\r Press OK to be returned to the Main Screen\n where you can add taxa to the study set.");
             showMenuButtons();
             db = null;
             return;
        } 
    
    //}
               
        if(showStudyOptions() == 1){
            programState = 1;
            String[][] fileNames;
            //fileNames = db.getFileNames(taxonomicLevel, mTaxa, false, 0);//commented by preethy on 12-01-2012
            //Added by preethy on 12-01-2012
            if(selCname)
            {
                db.cname=true;
            }
             if(taxonomicLevel.equals("Family")){
          //  fileNames = db.getFileNames2(sel, taxonomicLevel, mTaxa, false, 0);
                 if(group)
                    fileNames=db.getGroupFileNames(sel, taxonomicLevel,false, 0); 
                   else
                    fileNames = db.getFileNames(taxonomicLevel, mTaxa, false, 0);
             }
             else{
                 if(!group){
                 if(taxanull || selctfamilyTaxanull){
                     
                   fileNames=db.getFileNamesSearch(taxonomicLevel, mTaxa, false, 0); 
                 }else{
                       
                       fileNames=db.getFileNamesSearch(taxonomicLevel, mTaxa, false, 0);
                     //fileNames=db.getFileNames3(sel, taxonomicLevel, mTaxa, false, 0);
                 }
                 }else{
                      
                  fileNames=db.getGroupFileNames(sel, taxonomicLevel,false, 0);     
                 }
           
             }
            //Added by preethy on 26-01-2012
            if(fileNames.length==0){
              JOptionPane.showMessageDialog(this, "No images selected.");
              showMenuButtons();
            }
             /***/
            else{//Added by preethy
                //Commented by preethy
           // mStudyClass = new StudyImageClass(this, lbPictureBox, mRandomize,
                    //quitStudyImagesCallback(), fileNames, mShowFamilyName, taxonomicLevel); // last argument is Option for family name
            //System.out.println("befoooooo");
            mStudyClass = new StudyImageClass(this, lbPictureBox, mRandomize,
                    quitStudyImagesCallback(), fileNames, mShowFamilyName, taxonomicLevel,this,this.jPanel1);
            mStudyClass.setDisplayTime(delayQT);
            mStudyClass.setFixationTime(mFixationTime);
            mStudyClass.setKeyToContinue(mKeyToContinue);
            mStudyClass.setAutoRun(mAutoImageDisplay);
            mStudyClass.setImageWithName(mShowImageWithName);
            //jMenuBar1.setVisible(false);
            mStudyClass.start();}
        } else{
            
            showMenuButtons();
        }
        db = null;
    }
    // Returns a 1 if the Continue button was pressed. else a 0 for quit.
    private int showOptions()// runMode tells if the RUN Menu has been selected
    {
        JStudyDialog dlgStudy = new JStudyDialog(mode, delayQT, scriptMode, programState);
        dlgStudy.setModal(true);
        dlgStudy.setLocationRelativeTo(this);
        dlgStudy.setVisible(true);
        int retVal = dlgStudy.getResults();
        if(retVal == 1 && scriptMode == false) {
            delayQT = dlgStudy.getDelay();
            mode = dlgStudy.getMode();
        }
        return retVal;
    }
    private int showStudyOptions() {
       
        // Show the StudySettingsDialog!!!
        int retVal = 0;
        StudySettings dlgStudy = new StudySettings(delayQT, scriptMode,taxonomicLevel, mShowFamilyName, mKeyToContinue );
        dlgStudy.setModal(true);
        dlgStudy.setAutoImageDisplay(mAutoImageDisplay);
        dlgStudy.setRandomize(mRandomize);
        dlgStudy.setShowImageWithName(mShowImageWithName);
        dlgStudy.setLocationRelativeTo(this);
        dlgStudy.setVisible(true);
        
        if(dlgStudy.getContinue()){
            retVal = 1;
            mAutoImageDisplay = dlgStudy.getAutoImageDisplay();
            mRandomize = dlgStudy.getRandomize();
            mShowImageWithName = dlgStudy.getShowImageWithName();
            mShowFamilyName = dlgStudy.getShowFamilyName();
            mKeyToContinue = dlgStudy.getKeyPress();
            if(mAutoImageDisplay){
                
                delayQT = dlgStudy.getDelayTimeStudy();
            }
        }
        
        
        return retVal;
    }
    
    public void quitStudyImages(ActionEvent avt){
        //This function is called by StudyImagesClass, QuizClass and TestClass when they close.
        //Whatever needs to happen after those classes can
        //happen here.
        
        if(programState == 2 && showProgressQuiz){
            if(!prgres){
            progress.setModal(true);
            progress.setLocationRelativeTo(this);
            progress.ShowResults();
            progress.setVisible(true);
            }
        }
        if(programState == 3 && showProgressTest){
            progress.setModal(true);
            progress.setLocationRelativeTo(this);
            progress.ShowResults();
            progress.setVisible(true);
        }
       // jMenuBar1.setVisible(true);
        setWorkingState();
        showMenuButtons();
        programState = 0;
        try
        {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } 
        if(scriptMode == true){
            mGlobalSessionIndex = mGlobalSessionIndex + 1;
            if(this.studying == false){
                 inSession = true;
            }
           
            startScript();
        }
    }
    
    private void takeQuiz() {
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
      /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/

        setWorkingState();
        //     if(!group){
        if (taxonomicLevel == null || mTaxa == null || mTaxa.length == 0) {
           /* int retVal = JOptionPane.showConfirmDialog(this, "There are no taxa selected for taking a quiz.\n\rWould you like to select taxa now?", "No Taxa Selected!", JOptionPane.YES_NO_OPTION);
            if (retVal == JOptionPane.YES_OPTION) {
                selectTaxa(db);
                takeQuiz(); // brings user back to taking a quiz after taxa selection..
            } else {
                showMenuButtons();
            }
            db = null;
            return;*/
             JOptionPane.showMessageDialog(this, "There are no taxa selected.\n\r Press OK to be returned to the Main Screen\n where you can add taxa to the study set.");
             showMenuButtons();
             db = null;
             return;
        }
        // }
        programState = 2;
        if (showOptions() == 0) // if they press the Quit button on the dialog do nothing
        {
            programState = 0;
            showMenuButtons();
            //jMenuBar1.setVisible(true);
            db = null;
            return;
        }
         String[][] fileNames;
        //String[][] fileNames = db.getFileNames(taxonomicLevel, mTaxa, true, maxImages);//Commented by preethy on 12-01-2012
       if(!group){//if select by group is not selected
         if(taxanull || selctfamilyTaxanull){
             
             fileNames = db.getFileNamesSearch(taxonomicLevel, mTaxa, false, maxImages);  
        }else{
               
             fileNames=db.getFileNamesSearch(taxonomicLevel, mTaxa, false, maxImages);
           // fileNames = db.getFileNames(taxonomicLevel, mTaxa, true, maxImages);
            
           //fileNames = db.getFileNames2(sel, taxonomicLevel, mTaxa, true, maxImages);//Added by preethy on 12-01-2012
        }
       }else{
           
          fileNames=db.getGroupFileNames(sel, taxonomicLevel,false, maxImages);  
       }
          
       if(mTaxa.length<2){
           prgres=true;
       }else
           prgres=false;
        ActionListener al = new ActionListener() {
      
            public void actionPerformed(ActionEvent evt) {
                
                quitStudyImages(evt);
            }
        };
       
        //Added by preethy on 26-01-2012
        if(fileNames.length==0){
               JOptionPane.showMessageDialog(this, "No images selected.");
               showMenuButtons();
            }
        else{
         /**/
        SessionInfo session = new SessionInfo(this, lbPictureBox, fileNames, delayQT, taxonomicLevel, mTaxa, this.jPanel1, al, mUserName, false, null, mFixationTime, progress);
        //quiz = new QuizClass(session, mode, SessionInfo.SpellingValue);//Commented by preethy
          quiz = new QuizClass(session, mode, SessionInfo.SpellingValue,this);//Added by preethy on 28-01-2012
        //jMenuBar1.setVisible(false);
        quiz.start();
        }//Added by preethy
        db = null;
    }
    
    private void takeTest() {

        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
     /*   if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        setWorkingState();
        // if(!group){
        if (taxonomicLevel == null || mTaxa == null || mTaxa.length == 0) {
          /*  int retVal = JOptionPane.showConfirmDialog(this, "There are no taxa selected for taking a test.\n\rWould you like to select taxa now?", "No Taxa Selected!", JOptionPane.YES_NO_OPTION);
            if (retVal == JOptionPane.YES_OPTION) {
                selectTaxa(db);
                takeTest(); // brings user back to taking a test after taxa selection..
            } else {
                showMenuButtons();
            }
            db = null;
            return;*/
             JOptionPane.showMessageDialog(this, "There are no taxa selected.\n\r Press OK to be returned to the Main Screen\n where you can add taxa to the study set.");
             showMenuButtons();
             db = null;
             return;
        }
    //}
        programState = 3;

        if (showOptions() == 0) // if they press the Quit button on the dialog do nothing
        {
            programState = 0;
            showMenuButtons();
           // jMenuBar1.setVisible(true);
            db = null;
            return;
        }
            String[][] fileNames;
            // String[][] fileNames = db.getFileNames(taxonomicLevel, mTaxa, true, maxImages);//Commented by preethy on 12-01-2012
        if(!group){
            if(taxanull || selctfamilyTaxanull){  //Added by preethy
              
           fileNames = db.getFileNames(taxonomicLevel, mTaxa, true, maxImages); 
        }else{
              
                fileNames=db.getFileNamesSearch(taxonomicLevel, mTaxa, false, maxImages);
            //fileNames = db.getFileNames(taxonomicLevel, mTaxa, true, maxImages);  
           //fileNames = db.getFileNames2(sel ,taxonomicLevel, mTaxa, true, maxImages);//Added by preethy on 12-01-2012
        }
        }else{
             
            fileNames=db.getGroupFileNames(sel, taxonomicLevel,false, maxImages); 
        }
        //Added by preethy on 26-01-2012
        if(fileNames.length==0){
               JOptionPane.showMessageDialog(this, "No images selected.");
               showMenuButtons();
            }
        else{
         /***/
        SessionInfo  mSessionInfo = new SessionInfo(this, lbPictureBox, fileNames, delayQT, taxonomicLevel, mTaxa, this.jPanel1, quitStudyImagesCallback(),
                mUserName, true, null, mFixationTime, progress);

        test = new TestClass(mSessionInfo, mode);//Commented by preethy on 29-01-2012
        //  test = new TestClass(mSessionInfo, mode,this);//Added by preethy on 29-01-2012
        //jMenuBar1.setVisible(false);
        test.start();
       }//Added by preethy
        db = null;
    }
    
    private void exitFunction() {
        int retVal;
        retVal = JOptionPane.showConfirmDialog(this,"Are you sure you wish to quit?", "Exit?",JOptionPane.YES_NO_OPTION);
        if(retVal == JOptionPane.YES_OPTION) {
            // TODO: need to save progress to user file
            WriteProgressString(progress.getProgressArray(), mUserName);
            System.exit(0);
        }
    }
    
//    private void openScriptFile() {
//        JFileChooser myFileBrowser = new JFileChooser();
//        myFileBrowser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        ExampleFileFilter filter = new ExampleFileFilter();
//        filter.addExtension("iqs");
//        filter.setDescription("Image Quiz Script file");
//        myFileBrowser.setFileFilter(filter);
//        int returnVal = myFileBrowser.showOpenDialog(this);
//        if(returnVal == JFileChooser.APPROVE_OPTION) {
//            scriptMode = true;
//            mnuOptionTaxaSelection.setEnabled(false);
//
//
//        }
//    }
    
//    private void clearScriptFile() {
//        int retVal;
//        retVal = JOptionPane.showConfirmDialog(this,"Are you sure you wish to unload the script file? \n\r Settings will be set back to default.", "Unload Script File?",JOptionPane.YES_NO_OPTION);
//        if(retVal == JOptionPane.YES_OPTION) {
//            //Actually clear script file HERE!!
//            mnuOptionTaxaSelection.setEnabled(true);
//            scriptMode = false;
//        }// TODO add your handling code here:
//    }
   
    private void getHelp() {
        String path = getApplicationPath(true);
         boolean exists = (new File(path + "/HelpFiles/taxa.pdf")).exists();
        
        if(!exists){
            JOptionPane.showMessageDialog(this, "The file taxa.pdf could not be found.");
            return;
        }
        
        try {
          //  Runtime.getRuntime().exec("cmd /c start HelpFiles/taxa.pdf");
            Utilities.OpenExternalFile(Configuration.ApplicationPath()+"/HelpFiles/taxa.pdf");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    private void getAbout() {
       // This is the about info HERE!!!
       about = new AboutClass();
       about.setLocationRelativeTo(this);
       about.setVisible(true);
              
    } 
    
    private void JCDelay(double de) {
        de = de * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + de;
        while(System.currentTimeMillis() < stopTime){
            try{
                Thread.sleep(20);
            } catch(InterruptedException e){}
            
        }
    }
    
    private void showLoginScreen(){
        UserClass user = new UserClass();
        LoginDialog dlgLogin = new LoginDialog(this, user);
        mUserName = dlgLogin.mUserName;
        closeMainF = dlgLogin.closeMain;
        if(user.advanced_options_loaded){
            showProgressQuiz = user.showProgressQuiz;
            showProgressTest = user.showProgressTest;
            SessionInfo.SpellingValue = user.spelling_val;  //Set static variable
            mFixationTime = user.fixation_time;
            maxImages = user.max_images;
        }
        
    }
    
    private void selectTaxa(DataBaseDriver db){

        //If not database path exists, inform user!
        String path = Configuration.ApplicationPath();
        // String path = Configuration.UserPath();
        group=false;
        if(path.compareTo("") == 0)
        {
            //display error mesage.
            JOptionPane.showMessageDialog(this, "There is no package selected for this session.");
            return;
        }
       
        if (!VerifyDatabase()){
            return;
        }
        
        String taxonomicLevelTemp = taxonomicLevel;
        if(taxonomicLevel == null)
            taxonomicLevelTemp = "";
        //Start selection of taxa to study!!!
        
        if(scriptMode == false){
           //Added by preethy on 6-01-2012
           // SelectImage selImage=new SelectImage(db);
         /*  SelectImage selImage=new SelectImage(db);
            selImage.esc=false;
            selImage.setLocationRelativeTo(this);
            selImage.setVisible(true);
            sel=selImage.db.selectedRow;
               if(selImage.esc==true)
               {
                 
                   sel=new ArrayList();
               }*/
             
            //***//
            //
            //  CHOOSE TAXA LEVEL
            //
           
          //  if(selImage.close==false && sel.size()!=0 && selImage.esc==false)
          //  {
              SelectTaxaLevel select  = new SelectTaxaLevel(this, true); 
            select.esc = false;
            select.setLocationRelativeTo(this);
            select.setVisible(true);
            select.setModal(true);
          
            if(select.cancelled || select.esc)
                return;
            taxonomicLevel = select.getTaxaLevel();
            selCname = select.getUseCommonNames();
            
            //
            //  CHOOSE TAXA NAMES
            //
            SelectTaxa dlg;
            
            if(taxonomicLevel.compareTo("Species") == 0 && select.getUseCommonNames()){
                if(taxonomicLevelTemp.compareTo(taxonomicLevel) == 0){
                   
                dlg = new SelectTaxa(taxonomicLevel, true, mTaxa, db,maxImages);
                 
                }
                else
                    
                    dlg = new SelectTaxa(taxonomicLevel, true, null, db,maxImages);
            } else {
                if(taxonomicLevelTemp.compareTo(taxonomicLevel) == 0){
                dlg = new SelectTaxa(taxonomicLevel, false,mTaxa, db,maxImages);
                }
                else{
                    dlg = new SelectTaxa(taxonomicLevel, false, null, db,maxImages);
                }
            }
                        
            dlg.setLocationRelativeTo(this);
            dlg.setModal(true);
            dlg.setVisible(true);
            if(dlg.bCancel || dlg.esc){
               
                mTaxa = null; // These are the taxa that will be studied.
                taxonomicLevel = null; // This is the level that will be studied.
            } else {
               
              mTaxa = dlg.getItems();  
              isFromTexaSelect=true;
            }
          // }
            
        }
    }
    
    private void advancedOptions(){
        AdvancedOptions dlgStudy = new AdvancedOptions(SessionInfo.SpellingValue, showProgressQuiz, showProgressTest, mFixationTime, maxImages);
        dlgStudy.setModal(true);
        dlgStudy.setLocationRelativeTo(this);
        dlgStudy.setVisible(true);
        int retVal = dlgStudy.getResults();
        if(retVal == 1 && scriptMode == false) {
            showProgressQuiz = dlgStudy.getProgressQuiz();
            showProgressTest = dlgStudy.getProgressTest();
            SessionInfo.SpellingValue = dlgStudy.getSpelling();
            mFixationTime = dlgStudy.getFixationTime();
            maxImages = dlgStudy.getNumberofImages();
           
        }
        
    }
    
    private void selectRandomTaxa(DataBaseDriver db){

        RandomSelectDialog randomDlg = new RandomSelectDialog(taxonomicLevel, mCommonNamesRandom, numTaxaRandom);
        randomDlg.setLocationRelativeTo(this);
        randomDlg.setModal(true);
        randomDlg.setVisible(true);
        int retVal = randomDlg.getResults();
        randomDlg.setVisible(false);
        if(retVal == 1) {
            // Set Random Taxa HERE!!!
         
            taxonomicLevel = randomDlg.getTaxaLevel();
            int modeType = randomDlg.getMode();
            numTaxaRandom = randomDlg.getTaxaCount();
            //  int numImages = randomDlg.getImageCount();
            mCommonNamesRandom = randomDlg.getUseCommonNames();
            WaitRandomClass wait = new WaitRandomClass();
            wait.setLocationRelativeTo(this);
            wait.setVisible(true);
            wait.getContentPane().update(wait.getGraphics());
            RandomTaxaSelectClass myRandomTaxa = new RandomTaxaSelectClass(taxonomicLevel, numTaxaRandom, mCommonNamesRandom, db);
            // must set taxa[] then call studyImages takeQuiz or takeTest depending on modeType..
            mTaxa = myRandomTaxa.getTaxa();
            wait.setVisible(false);
            wait.dispose();
            
            if(modeType == 1){ // Study
                studyImages();
            } else if(modeType == 2){
                takeQuiz();
            } else if(modeType == 3){
                takeTest();
            } else{
                randomDlg.setVisible(false);
            }
            
        } else{
            randomDlg.setVisible(false);
        }
        panelMenuButtons.setVisible(true);
    }
    

    public void setMenuVisible(boolean bool){
       // jMenuBar1.setVisible(bool);
    }
    
    private void showHelpPopup(){
        HelpPopup help = new HelpPopup(mUserName, userFirstLine, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    setMousePointer(evt);
                }});
        help.setLocationRelativeTo(this);
        help.setModal(true);
        help.setVisible(true);
        helpPop = true;
           
    }
    
    private void setMousePointer(ActionEvent avt){

         if(avt.getActionCommand().compareTo("Tutorial") == 0){    
             LaunchPDF("Tutorial.html");
         }
         else if (avt.getActionCommand().compareTo("Intro") == 0){  
             LaunchPDF("Intro.pdf");
         }
       }

      private void setProgress(){
        ArrayList myArray = new ArrayList();
        FileReader myReader;
       // String path = Configuration.UserPath();//getApplicationPath(false);
        String path = Configuration.ApplicationPath();//getApplicationPath(false);
        String temp = "";
        try{
            myReader = new FileReader(path + "/UserFiles/" + mUserName + ".csv");
            BufferedReader inputfile = new BufferedReader(myReader);
            
            try{
                temp = inputfile.readLine(); // Is user name and pass (Throw away)
                String line = "";
                temp = inputfile.readLine();    //Discard advanced options.
                temp = inputfile.readLine();// First Usable Line for Progress Report
                if(temp != null){
                    line = temp + "\n";
                    temp = " ";
                }
                while(temp != null){
                    ///////////   BAD LOOP!!!!!
                    if(temp.length() > 0){
                        if(temp.charAt(0) == 'S'){
                            // Add to the Result #1
                            line = line + "\n";
                            myArray.add(line);
                            line = "";
                            line = temp + "\n";
                            temp = " ";
                        } else{ // keep adding
                            temp = inputfile.readLine();
                            if(temp == null){
                                // Do nothing
                            } else if(temp.length() > 0){
                                if(temp.charAt(0) != 'S')
                                    line = line + temp + "\n";
                                // else temp = " ";
                            }
                        }
                    } else
                        temp = "\n";
                    if(temp == null){
                        myArray.add(line);
                    }
                }
                inputfile.close();
            } catch(IOException ioe){
            }
        } catch(FileNotFoundException fnf){
            
        }
        if(myArray.size() > 0){
            progress.setResults(myArray);
        }
    }
 
      private void WriteProgressString(ArrayList myArray, String id){
        //Read the first line from the file and do not change.
        //  It contains the hash of their user password and what-nots.
        //
        FileReader myReader;
      //  String path = Configuration.UserPath();//getApplicationPath(false);
        String path = Configuration.ApplicationPath();//getApplicationPath(false);
        String firstLine = "";

        try{
            myReader = new FileReader(path + "/UserFiles/" + id + ".csv");
            BufferedReader inputfile = new BufferedReader(myReader);
            
            try{
                firstLine = inputfile.readLine();
                inputfile.close();
            } catch(IOException ioe){
                
            }
            
        } catch(FileNotFoundException fnf){
            
        }
        
        FileWriter myFileWriter = null;
        try{
            myFileWriter = new FileWriter(path + "/UserFiles/" + id + ".csv");
        } catch(IOException ioe){
            
        }
        //
        //Write out first line and second line-advanced options.
        //
        PrintWriter diskfile = new PrintWriter(myFileWriter);
        diskfile.println(firstLine);                    //Rewrite first line.
        String advanced_options = "Advanced Options:";
        advanced_options += Boolean.toString(showProgressQuiz) + ";";
        advanced_options += Boolean.toString(showProgressTest) + ";";
        advanced_options += Integer.toString(SessionInfo.SpellingValue) + ";";
        advanced_options += Double.toString(mFixationTime) + ";";
        advanced_options += Integer.toString(maxImages);
        diskfile.println(advanced_options);             //Writeout Advanced Options.
        
        // Write Progress results.
        for(int i = 0; i < myArray.size(); i++){
            String tempString = myArray.get(i).toString();
            String[] lines = tempString.split("\n");
            for(int j = 0; j < lines.length; j++){
                diskfile.println(lines[j]);
            }
        }
        diskfile.close();
    }

    public static void main(String args[]) {
       // showSplashScreen();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem advancedOptions;
    private javax.swing.JButton btQuiz;
    private javax.swing.JButton btScript;
    private javax.swing.JButton btScript1;
    private javax.swing.JButton btSearch;
    private javax.swing.JButton btSelectByGroup;
    private javax.swing.JButton btSpecies;
    private javax.swing.JButton btStudy;
    private javax.swing.JButton btTaxaSelect;
    private javax.swing.JButton btTest;
    private javax.swing.JMenuItem introMenuItem;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lbPictureBox;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuLoadScript;
    private javax.swing.JMenuItem mnuOptionRandomTaxa;
    private javax.swing.JMenuItem mnuOptionTaxaSelection;
    private javax.swing.JMenu mnuOptions;
    private javax.swing.JMenuItem mnuQuiz;
    private javax.swing.JMenu mnuRun;
    private javax.swing.JMenuItem mnuStudy;
    private javax.swing.JMenuItem mnuTest;
    private javax.swing.JMenuItem mnuViewProgress;
    private javax.swing.JMenuItem mnuaddimages;
    private javax.swing.JMenuItem openingScreen;
    private javax.swing.JPanel panelMenuButtons;
    private javax.swing.JMenuItem tutorialMenuItem;
    // End of variables declaration//GEN-END:variables
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        btScript1 = new javax.swing.JButton();
        panelMenuButtons = new javax.swing.JPanel();
        btTaxaSelect = new javax.swing.JButton();
        btQuiz = new javax.swing.JButton();
        btStudy = new javax.swing.JButton();
        btTest = new javax.swing.JButton();
        btScript = new javax.swing.JButton();
        btSpecies = new javax.swing.JButton();
        btSearch = new javax.swing.JButton();
        btSelectByGroup = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbPictureBox = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuViewProgress = new javax.swing.JMenuItem();
        mnuLoadScript = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        mnuaddimages = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        mnuExit = new javax.swing.JMenuItem();
        mnuRun = new javax.swing.JMenu();
        mnuOptionTaxaSelection = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        mnuStudy = new javax.swing.JMenuItem();
        mnuQuiz = new javax.swing.JMenuItem();
        mnuTest = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnuOptions = new javax.swing.JMenu();
        mnuOptionRandomTaxa = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        advancedOptions = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        introMenuItem = new javax.swing.JMenuItem();
        tutorialMenuItem = new javax.swing.JMenuItem();
        openingScreen = new javax.swing.JMenuItem();

        org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        btScript1.setToolTipText("");
        btScript1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(ConfigFileReader.getProjectName());
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        panelMenuButtons.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btTaxaSelect.setToolTipText("");
        btTaxaSelect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btTaxaSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btTaxaSelectMouseReleased(evt);
            }
        });
        btTaxaSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTaxaSelectActionPerformed(evt);
            }
        });

        btQuiz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btQuiz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btQuizMouseReleased(evt);
            }
        });

        btStudy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btStudy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btStudyMouseReleased(evt);
            }
        });

        btTest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btTestMouseReleased(evt);
            }
        });

        btScript.setToolTipText("");
        btScript.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btScript.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btScriptMouseReleased(evt);
            }
        });
        btScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btScriptActionPerformed(evt);
            }
        });

        btSpecies.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSpecies.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btSpeciesMouseReleased(evt);
            }
        });

        btSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btSearchMouseReleased(evt);
            }
        });

        btSelectByGroup.setToolTipText("");
        btSelectByGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btSelectByGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btSelectByGroupMouseReleased(evt);
            }
        });
        btSelectByGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectByGroupActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panelMenuButtonsLayout = new org.jdesktop.layout.GroupLayout(panelMenuButtons);
        panelMenuButtons.setLayout(panelMenuButtonsLayout);
        panelMenuButtonsLayout.setHorizontalGroup(
            panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMenuButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelMenuButtonsLayout.createSequentialGroup()
                        .add(btTest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btScript, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelMenuButtonsLayout.createSequentialGroup()
                        .add(btStudy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btQuiz, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelMenuButtonsLayout.createSequentialGroup()
                        .add(btSelectByGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btTaxaSelect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelMenuButtonsLayout.createSequentialGroup()
                        .add(btSpecies, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(btSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelMenuButtonsLayout.setVerticalGroup(
            panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMenuButtonsLayout.createSequentialGroup()
                .add(17, 17, 17)
                .add(panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btTaxaSelect)
                    .add(btSelectByGroup))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btSearch)
                    .add(btSpecies))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btQuiz)
                    .add(btStudy))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelMenuButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btScript)
                    .add(btTest))
                .addContainerGap())
        );

        lbPictureBox.setFont(new java.awt.Font("Courier New", 1, 48)); // NOI18N
        lbPictureBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPictureBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, lbPictureBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, lbPictureBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jMenuBar1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        mnuFile.setText("File");
        mnuFile.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuFileKeyPressed(evt);
            }
        });

        mnuViewProgress.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuViewProgress.setText("View Progress    Ctrl+V");
        mnuViewProgress.setToolTipText("View contents of your output file.");
        mnuViewProgress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuViewProgressMouseReleased(evt);
            }
        });
        mnuViewProgress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuViewProgressActionPerformed(evt);
            }
        });
        mnuViewProgress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuViewProgressKeyPressed(evt);
            }
        });
        mnuViewProgress.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuViewProgressMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuFile.add(mnuViewProgress);

        mnuLoadScript.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuLoadScript.setText("Load Script    Ctrl+L");
        mnuLoadScript.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuLoadScriptMouseReleased(evt);
            }
        });
        mnuLoadScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLoadScriptActionPerformed(evt);
            }
        });
        mnuLoadScript.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                mnuLoadScriptInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        mnuLoadScript.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mnuLoadScriptKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mnuLoadScriptKeyReleased(evt);
            }
        });
        mnuLoadScript.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuLoadScriptMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuLoadScript.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                mnuLoadScriptVetoableChange(evt);
            }
        });
        mnuFile.add(mnuLoadScript);
        mnuFile.add(jSeparator5);

        mnuaddimages.setText("Install New Database and Images");
        mnuaddimages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuaddimagesMouseReleased(evt);
            }
        });
        mnuaddimages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuaddimagesActionPerformed(evt);
            }
        });
        mnuFile.add(mnuaddimages);
        mnuFile.add(jSeparator6);

        jMenuItem6.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem6.setText("Save Taxa Set     Alt+S");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        mnuFile.add(jMenuItem6);

        jMenuItem7.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem7.setText("Load Saved Taxa Set    Alt+L");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        mnuFile.add(jMenuItem7);

        mnuExit.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuExit.setText("Exit");
        mnuExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuExitMouseReleased(evt);
            }
        });
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        mnuExit.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuExitMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuFile.add(mnuExit);

        jMenuBar1.add(mnuFile);

        mnuRun.setText("Run");
        mnuRun.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuRunMouseReleased(evt);
            }
        });

        mnuOptionTaxaSelection.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuOptionTaxaSelection.setText("Taxa Selection     Ctrl+R");
        mnuOptionTaxaSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuOptionTaxaSelectionMouseReleased(evt);
            }
        });
        mnuOptionTaxaSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOptionTaxaSelectionActionPerformed(evt);
            }
        });
        mnuOptionTaxaSelection.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mnuOptionTaxaSelectionKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuOptionTaxaSelectionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mnuOptionTaxaSelectionKeyReleased(evt);
            }
        });
        mnuOptionTaxaSelection.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuOptionTaxaSelectionMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
                mnuOptionTaxaSelectionMenuKeyReleased(evt);
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuRun.add(mnuOptionTaxaSelection);
        mnuRun.add(jSeparator3);

        mnuStudy.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuStudy.setText("Study                       Ctrl+S");
        mnuStudy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuStudyMouseReleased(evt);
            }
        });
        mnuStudy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuStudyActionPerformed(evt);
            }
        });
        mnuStudy.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuStudyMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuRun.add(mnuStudy);

        mnuQuiz.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuQuiz.setText("Quiz                        Ctrl+Q");
        mnuQuiz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuQuizMouseReleased(evt);
            }
        });
        mnuQuiz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuQuizActionPerformed(evt);
            }
        });
        mnuQuiz.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuQuizMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuRun.add(mnuQuiz);

        mnuTest.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuTest.setText("Test                         Ctrl+T");
        mnuTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuTestMouseReleased(evt);
            }
        });
        mnuTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTestActionPerformed(evt);
            }
        });
        mnuTest.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuTestMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuRun.add(mnuTest);

        jMenuItem3.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem3.setText("Select by Family   Ctrl+B");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseReleased(evt);
            }
        });
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mnuRun.add(jMenuItem3);

        jMenuItem5.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem5.setText("Select by Group     Ctrl+G");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem5MouseReleased(evt);
            }
        });
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        mnuRun.add(jMenuItem5);

        jMenuItem4.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem4.setText("Search                   Ctrl+F");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem4MouseReleased(evt);
            }
        });
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuRun.add(jMenuItem4);

        jMenuBar1.add(mnuRun);

        mnuOptions.setText("Options");
        mnuOptions.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOptionsActionPerformed(evt);
            }
        });

        mnuOptionRandomTaxa.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        mnuOptionRandomTaxa.setText("Random Taxa Selection");
        mnuOptionRandomTaxa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mnuOptionRandomTaxaMouseReleased(evt);
            }
        });
        mnuOptionRandomTaxa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOptionRandomTaxaActionPerformed(evt);
            }
        });
        mnuOptionRandomTaxa.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                mnuOptionRandomTaxaMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuOptions.add(mnuOptionRandomTaxa);
        mnuOptions.add(jSeparator1);

        advancedOptions.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        advancedOptions.setText("Advanced Options");
        advancedOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                advancedOptionsMouseReleased(evt);
            }
        });
        advancedOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptionsActionPerformed(evt);
            }
        });
        advancedOptions.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                advancedOptionsMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuOptions.add(advancedOptions);

        jMenuBar1.add(mnuOptions);

        mnuHelp.setText("Help");
        mnuHelp.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem1.setText("Help Topics     F1");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseReleased(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuItem1.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                jMenuItem1MenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuHelp.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        jMenuItem2.setText("Keyboard Shortcuts   F2");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseReleased(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuHelp.add(jMenuItem2);

        introMenuItem.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        introMenuItem.setText("The Image Quiz Family of Programs");
        introMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                introMenuItemMouseReleased(evt);
            }
        });
        introMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introMenuItemActionPerformed(evt);
            }
        });
        introMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                introMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuHelp.add(introMenuItem);

        tutorialMenuItem.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        tutorialMenuItem.setText("Tutorial");
        tutorialMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tutorialMenuItemMouseReleased(evt);
            }
        });
        tutorialMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutorialMenuItemActionPerformed(evt);
            }
        });
        tutorialMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                tutorialMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuHelp.add(tutorialMenuItem);

        openingScreen.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        openingScreen.setText("Opening Screen");
        openingScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                openingScreenMouseReleased(evt);
            }
        });
        openingScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openingScreenActionPerformed(evt);
            }
        });
        openingScreen.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                openingScreenMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        mnuHelp.add(openingScreen);

        jMenuBar1.add(mnuHelp);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panelMenuButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(83, 83, 83)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panelMenuButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuLoadScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoadScriptActionPerformed
    openScript();
    }//GEN-LAST:event_mnuLoadScriptActionPerformed

    private void mnuLoadScriptInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_mnuLoadScriptInputMethodTextChanged
 
    }//GEN-LAST:event_mnuLoadScriptInputMethodTextChanged

    private void mnuLoadScriptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuLoadScriptKeyReleased
      
    }//GEN-LAST:event_mnuLoadScriptKeyReleased

    private void mnuLoadScriptVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_mnuLoadScriptVetoableChange
        
    }//GEN-LAST:event_mnuLoadScriptVetoableChange

    private void mnuLoadScriptKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuLoadScriptKeyTyped
    // int keyCode = evt.getKeyCode();
    //    if(keyCode == KeyEvent.VK_L){
    //         openScript();
    //    }
    }//GEN-LAST:event_mnuLoadScriptKeyTyped

    private void openingScreenMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_openingScreenMenuKeyPressed
        
      //      int keyCode = evt.getKeyCode();
      //  if(keyCode == KeyEvent.VK_ENTER){
      //       showHelpPopup(); 
      //  }  
       
    }//GEN-LAST:event_openingScreenMenuKeyPressed

    private void tutorialMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_tutorialMenuItemMenuKeyPressed
    //        int keyCode = evt.getKeyCode();
    //    if(keyCode == KeyEvent.VK_ENTER){
    //           showTutorial(); 
     //   }  
       
    }//GEN-LAST:event_tutorialMenuItemMenuKeyPressed

    private void introMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_introMenuItemMenuKeyPressed
    //         int keyCode = evt.getKeyCode();
    //    if(keyCode == KeyEvent.VK_ENTER){
     //           showIntro();
     //   }  
       
    }//GEN-LAST:event_introMenuItemMenuKeyPressed

    private void jMenuItem1MenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_jMenuItem1MenuKeyPressed
     //      int keyCode = evt.getKeyCode();
     //   if(keyCode == KeyEvent.VK_ENTER){
     //            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
     //    setCursor(hourglassCursor);
     //    getMainHelp();
     //  }
      
    }//GEN-LAST:event_jMenuItem1MenuKeyPressed

    private void advancedOptionsMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_advancedOptionsMenuKeyPressed
    //      int keyCode = evt.getKeyCode();
    //    if(keyCode == KeyEvent.VK_ENTER){
     //          advancedOptions();
     //   }
    }//GEN-LAST:event_advancedOptionsMenuKeyPressed

    private void mnuOptionRandomTaxaMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuOptionRandomTaxaMenuKeyPressed
     //      int keyCode = evt.getKeyCode();
     //   if(keyCode == KeyEvent.VK_ENTER){
     //          panelMenuButtons.setVisible(false);
     //   selectRandomTaxa();
     //   }
    }//GEN-LAST:event_mnuOptionRandomTaxaMenuKeyPressed

    private void mnuTestMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuTestMenuKeyPressed
    //       int keyCode = evt.getKeyCode();
     //   if(keyCode == KeyEvent.VK_ENTER){
     //        takeTest();
      //  }
    }//GEN-LAST:event_mnuTestMenuKeyPressed

    private void mnuQuizMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuQuizMenuKeyPressed
     //      int keyCode = evt.getKeyCode();
    //    if(keyCode == KeyEvent.VK_ENTER){
     //         takeQuiz();
     //   }
       
    }//GEN-LAST:event_mnuQuizMenuKeyPressed

    private void mnuStudyMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuStudyMenuKeyPressed
      //     int keyCode = evt.getKeyCode();
      //  if(keyCode == KeyEvent.VK_ENTER){
      //        studyImages();
      //  }
       
    }//GEN-LAST:event_mnuStudyMenuKeyPressed

    private void mnuExitMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuExitMenuKeyPressed
      //    int keyCode = evt.getKeyCode();
      //  if(keyCode == KeyEvent.VK_ENTER){
      //      exitFunction();
      //  }
        
    }//GEN-LAST:event_mnuExitMenuKeyPressed

    private void mnuOptionTaxaSelectionMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionMenuKeyPressed
      //     int keyCode = evt.getKeyCode();
      //  if(keyCode == KeyEvent.VK_ENTER){
      //        panelMenuButtons.setVisible(false);
     //   selectTaxa();
     //   panelMenuButtons.setVisible(true);
     //   }
       
       
    }//GEN-LAST:event_mnuOptionTaxaSelectionMenuKeyPressed

    private void mnuLoadScriptMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuLoadScriptMenuKeyPressed
       // int keyCode = evt.getKeyCode();
      //  if(keyCode == KeyEvent.VK_ENTER){
      //       openScript();
      //  }
    }//GEN-LAST:event_mnuLoadScriptMenuKeyPressed

    private void mnuViewProgressMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuViewProgressMenuKeyPressed
     //      int keyCode = evt.getKeyCode();
     //   if(keyCode == KeyEvent.VK_ENTER){
     //        progress.setModal(true);
     //   progress.setLocationRelativeTo(this);
      //  progress.ShowResults();
      //  progress.setVisible(true);
      //  }
    }//GEN-LAST:event_mnuViewProgressMenuKeyPressed

    private void mnuViewProgressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuViewProgressKeyPressed
      //  int keyCode = evt.getKeyCode();
     //   if(keyCode == KeyEvent.VK_ENTER){
      //       progress.setModal(true);
      //  progress.setLocationRelativeTo(this);
     //  progress.ShowResults();
     //   progress.setVisible(true);
     //   }
    }//GEN-LAST:event_mnuViewProgressKeyPressed

    private void openScript() {
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("iqs");
        filter.setDescription("Image Quiz Script file");
        JFileChooser myFileBrowser = new JFileChooser();
        if (ScriptDirectory == null) {
            // try {
            //    ScriptDirectory = new File(new File(".").getCanonicalPath() + "\\Scripts");
            //ScriptDirectory = new File(Configuration.UserPath() + "\\Scripts");
            ScriptDirectory = new File(Configuration.ApplicationPath() + "/Scripts");
            // } catch (IOException ex) {
            //     ex.printStackTrace();
            // }
        }
        myFileBrowser.setCurrentDirectory(ScriptDirectory);
        myFileBrowser.setMultiSelectionEnabled(false);
        myFileBrowser.setDialogTitle("Load a Script");
        myFileBrowser.setFileFilter(filter);
        myFileBrowser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = myFileBrowser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File tempFile = myFileBrowser.getSelectedFile();
            String temp = tempFile.getAbsolutePath();
            ScriptDirectory = tempFile;
          
            // JOptionPane.showMessageDialog(this,temp);
            this.loadScriptFile(temp);
            // Call The Load Script Function
        }
        // }

    }
    
    private void mnuLoadScriptMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuLoadScriptMouseReleased
    
        //openScript();
     
    }//GEN-LAST:event_mnuLoadScriptMouseReleased

    private void openingScreenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openingScreenMouseReleased

       //showHelpPopup();  
    }//GEN-LAST:event_openingScreenMouseReleased

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if(helpPop == false){
        Cursor defaultCurs = new Cursor(Cursor.DEFAULT_CURSOR);
         setCursor(defaultCurs);
        }
        else helpPop = false;
    }//GEN-LAST:event_formWindowGainedFocus

    private void introMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_introMenuItemMouseReleased
// TODO add your handling code here:
       // LaunchPDF("Intro.pdf");

    }//GEN-LAST:event_introMenuItemMouseReleased

    private void LaunchPDF(String fileName){
      
        String path = getApplicationPath(true);
        boolean exists = (new File(path + "/HelpFiles/" + fileName)).exists();
        if (!exists) {
            JOptionPane.showMessageDialog(this, "The file " + fileName + " could not be found.");
            return;
        }
        try {
            // Probably will not work on a mac
            //Runtime.getRuntime().exec("cmd /c start HelpFiles/" + fileName);
             Utilities.OpenExternalFile(Configuration.ApplicationPath()+"/HelpFiles/"+fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }
    

      
    private void tutorialMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tutorialMenuItemMouseReleased
// TODO add your handling code here:
     //LaunchPDF("Tutorial.html");
    }//GEN-LAST:event_tutorialMenuItemMouseReleased
 
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
      
        this.requestFocus(); // Gives focus to the main windows so shourtcut keys will work..
        if(showHelp == true){
            showHelp = false;
             showHelpPopup();
        }
       
    }//GEN-LAST:event_formWindowActivated

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private String getApplicationPath(boolean showCursor) {
        if (showCursor == true) {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
        }
        File myFile = new File(".");
        try {
            return myFile.getCanonicalPath();
        } catch (IOException ex) {
            return "";
        }
    }
    
    private void getMainHelp() {
        String path = getApplicationPath(true);
         
        boolean exists = (new File(path + "/HelpFiles/HelpFiles.html")).exists();
        if (!exists) {
            JOptionPane.showMessageDialog(this, "The file HelpFiles.html could not be found.");
            return;
        }

        try {
           // Runtime.getRuntime().exec("cmd /c start HelpFiles/HelpFiles.html");
           Utilities.OpenExternalFile(Configuration.ApplicationPath()+"/HelpFiles/HelpFiles.html");

        } catch (IOException ex) {
            ex.printStackTrace();
       }

    }
    
     private void openFiles(String filename){
         String path = getApplicationPath(true);
        
         boolean exists = (new File(path + "/HelpFiles/" + filename)).exists();
         if (!exists) {
             JOptionPane.showMessageDialog(this, "The file " + filename + " could not be found.");
             return;
         }
       try {
            // Runtime.getRuntime().exec("cmd /c start HelpFiles/" + filename);
            Utilities.OpenExternalFile(Configuration.ApplicationPath()+"/HelpFiles/"+filename);

         } catch (IOException ex) {
             ex.printStackTrace();
         }
        
    }
     
    private void jMenuItem1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseReleased
         /*Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
         setCursor(hourglassCursor);
         getMainHelp();*/
    }//GEN-LAST:event_jMenuItem1MouseReleased
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //TODO:  need to save progress to user file
        WriteProgressString(progress.getProgressArray(), mUserName);
    }//GEN-LAST:event_formWindowClosing
    
    private void mnuOptionRandomTaxaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuOptionRandomTaxaMouseReleased

      /*  DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
      */
        /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        /*panelMenuButtons.setVisible(false);
        selectRandomTaxa(db);
        db = null;*/
    }//GEN-LAST:event_mnuOptionRandomTaxaMouseReleased
    
    private void btTestMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btTestMouseReleased
        
        takeTest();// TODO add your handling code here:
    }//GEN-LAST:event_btTestMouseReleased
    
    private void btQuizMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btQuizMouseReleased
        
        takeQuiz();// TODO add your handling code here:
    }//GEN-LAST:event_btQuizMouseReleased
    
    private void btStudyMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btStudyMouseReleased
                
        studyImages();
    }//GEN-LAST:event_btStudyMouseReleased
    
    private void btTaxaSelectMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btTaxaSelectMouseReleased
        
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
      /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        panelMenuButtons.setVisible(false);
        selectTaxa(db);
        db = null;
        panelMenuButtons.setVisible(true);
    }//GEN-LAST:event_btTaxaSelectMouseReleased
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       this.setTitle(ConfigFileReader.getProjectName());
        showMenuButtons();  // Show Menu Buttons on startup!!
        
    }//GEN-LAST:event_formWindowOpened
    
    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
    }//GEN-LAST:event_formWindowStateChanged
    
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        
    }//GEN-LAST:event_formMouseReleased
    
    private void advancedOptionsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advancedOptionsMouseReleased
        //advancedOptions();// TODO add your handling code here:
    }//GEN-LAST:event_advancedOptionsMouseReleased
    
    private void mnuOptionTaxaSelectionMenuKeyReleased(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionMenuKeyReleased
        
    }//GEN-LAST:event_mnuOptionTaxaSelectionMenuKeyReleased
    
    private void mnuOptionTaxaSelectionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionKeyTyped
// TODO add your handling code here:
        
    }//GEN-LAST:event_mnuOptionTaxaSelectionKeyTyped
    
    private void mnuOptionTaxaSelectionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_mnuOptionTaxaSelectionKeyPressed
    
    private void mnuOptionTaxaSelectionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_mnuOptionTaxaSelectionKeyReleased
    
    private void mnuFileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuFileKeyPressed
        
    }//GEN-LAST:event_mnuFileKeyPressed

    private void btTaxaSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTaxaSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btTaxaSelectActionPerformed
  //Added by preethy on 4-01-2012
    private void btScriptMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btScriptMouseReleased
        // TODO add your handling code here:
         openScript();
    }//GEN-LAST:event_btScriptMouseReleased
/****/
    private void btScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btScriptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btScriptActionPerformed

    private void btSpeciesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btSpeciesMouseReleased
        // TODO add your handling code here:
       
        selectSpecies(); //Added by preethy on 17-01-2012
               
    }//GEN-LAST:event_btSpeciesMouseReleased

    private void btSearchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btSearchMouseReleased
         // TODO add your handling code here:
        panelMenuButtons.setVisible(false);
        search();
        panelMenuButtons.setVisible(true);
         
    }//GEN-LAST:event_btSearchMouseReleased

    private void mnuRunMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuRunMouseReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_mnuRunMouseReleased

    private void jMenuItem4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MouseReleased
        // TODO add your handling code here:
        //search();//Added by preethy on 19-01-2012
    }//GEN-LAST:event_jMenuItem4MouseReleased

    private void jMenuItem3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseReleased
        // TODO add your handling code here:
         //selectSpecies(); //Added by preethy on 17-01-2012
    }//GEN-LAST:event_jMenuItem3MouseReleased

    private void mnuaddimagesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuaddimagesMouseReleased
    
    }//GEN-LAST:event_mnuaddimagesMouseReleased

    private void mnuaddimagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuaddimagesActionPerformed
        // TODO add your handling code here:

    Object source = evt.getSource();
    if (source == mnuaddimages) {
    
        try {
                try {
                    // TODO add your handling code here:
                    addImagesAndDatabase();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    }//GEN-LAST:event_mnuaddimagesActionPerformed

    private void jMenuItem2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseReleased
        // TODO add your handling code here:
         /*shortcutKeys dlgCut = new shortcutKeys();
         dlgCut.setLocationRelativeTo(this);
         dlgCut.setModal(true);
         dlgCut.setVisible(true);*/
    }//GEN-LAST:event_jMenuItem2MouseReleased

    private void btSelectByGroupMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btSelectByGroupMouseReleased
        // TODO add your handling code here:
        selectByGroup();
    }//GEN-LAST:event_btSelectByGroupMouseReleased

    private void btSelectByGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectByGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btSelectByGroupActionPerformed

    private void jMenuItem5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MouseReleased
        // TODO add your handling code here:
        //selectByGroup();
    }//GEN-LAST:event_jMenuItem5MouseReleased

private void mnuOptionTaxaSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOptionTaxaSelectionActionPerformed
// TODO add your handling code here:
       
        DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
       /* if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        
        panelMenuButtons.setVisible(false);
        selectTaxa(db);
        panelMenuButtons.setVisible(true);
        db = null;
}//GEN-LAST:event_mnuOptionTaxaSelectionActionPerformed

private void mnuTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTestActionPerformed
// TODO add your handling code here:
      takeTest();
}//GEN-LAST:event_mnuTestActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
// TODO add your handling code here:
     selectSpecies(); 
}//GEN-LAST:event_jMenuItem3ActionPerformed

private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
// TODO add your handling code here:
    selectByGroup();
}//GEN-LAST:event_jMenuItem5ActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
// TODO add your handling code here:
    search();
}//GEN-LAST:event_jMenuItem4ActionPerformed

private void mnuOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOptionsActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_mnuOptionsActionPerformed

private void mnuOptionRandomTaxaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOptionRandomTaxaActionPerformed
// TODO add your handling code here:
       DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
      /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
        }*/
        panelMenuButtons.setVisible(false);
        selectRandomTaxa(db);
        db = null;
}//GEN-LAST:event_mnuOptionRandomTaxaActionPerformed

private void advancedOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptionsActionPerformed
// TODO add your handling code here:
    advancedOptions();
}//GEN-LAST:event_advancedOptionsActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO add your handling code here:
     Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
         setCursor(hourglassCursor);
         getMainHelp();
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
// TODO add your handling code here:
         shortcutKeys dlgCut = new shortcutKeys();
         dlgCut.setLocationRelativeTo(this);
         dlgCut.setModal(true);
         dlgCut.setVisible(true);
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void introMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introMenuItemActionPerformed
// TODO add your handling code here:
    LaunchPDF("Intro.pdf");
}//GEN-LAST:event_introMenuItemActionPerformed

private void tutorialMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tutorialMenuItemActionPerformed
// TODO add your handling code here:
     LaunchPDF("Tutorial.html");
}//GEN-LAST:event_tutorialMenuItemActionPerformed

private void openingScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openingScreenActionPerformed
// TODO add your handling code here:
    showHelpPopup();  
}//GEN-LAST:event_openingScreenActionPerformed

private void mnuViewProgressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuViewProgressActionPerformed
// TODO add your handling code here:
        progress.setModal(true);
        progress.setLocationRelativeTo(this);
        progress.ShowResults();
        progress.setVisible(true);
   
}//GEN-LAST:event_mnuViewProgressActionPerformed

private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
// TODO add your handling code here:
   exitFunction(); 
}//GEN-LAST:event_mnuExitActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
           if (taxonomicLevel == null || mTaxa == null || mTaxa.length == 0) {
            JOptionPane.showMessageDialog(this, "There are no taxa selected.\n\r Press OK to be returned to the Main Screen.");
            return;
        } 

        try {
            saveTexaSet();
            //JOptionPane.showMessageDialog(this,"File Saved Successfuly!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Some problem occout while processing you request.Please try again!");
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        openTexaFile();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void loadScriptFile(String filename) {
        
        scriptMode = true;
        inSession = false;
        GlobalVariableClass gvClass = new GlobalVariableClass();
        //filename = "testing.iqs";
        FileInputStream fis = null;
        ObjectInputStream in = null;

        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            try {
                gvClass = (GlobalVariableClass) in.readObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            mScriptName = gvClass.getScriptName();
            mScriptFileName = findScriptFileName();
            mSessionCount = gvClass.getSessionCount();
            mySessionsArray = new SessionClass[mSessionCount];
            
            for (int i = 0; i < mSessionCount; i++) {
                try {
                    mySessionsArray[i] = (SessionClass) in.readObject();
                   
                   
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            in.close();
        } catch (IOException ex) {
        }
        startScript();

    }
    
    private String Wrap(String msg, int lineLength){
        if(msg.length() <= lineLength)
            return msg;
        String newMsg = "";
        int i = lineLength;
        while(msg.charAt(i) != ' '){
           // May need to handel negative i condition
            i--;
        }
        newMsg = msg.substring(0, i) + "\n";
        return newMsg + Wrap(msg.substring(i + 1, msg.length()), lineLength);
    }
    
    private void startScript(){
         // Begin Main Scripting loop
         
         String sessionType;
         String StudentMessage = "";
         String tempMess;
         dlgSessionCommand dlgSC = new dlgSessionCommand();
         
         // Check if there are any more sessions to execute...
         if(mSessionCount == mGlobalSessionIndex){ // Then there are no more sessions
             JOptionPane.showMessageDialog(this,"All sessions have been completed for this script.");
             taxonomicLevel = null;
             mTaxa = null;
             scriptMode = false;
             mGlobalSessionIndex = 0;
             return;
         }
         // If there is a student message display it!!!
         StudentMessage = mySessionsArray[mGlobalSessionIndex].getMessage();
       
         //StudentMessage = "If there is a student message, you need to talk to you teacher about receiving an F-";
         tempMess = StudentMessage.replaceAll(" ", "");
         // say about 60
         StudentMessage = StudentMessage.replace("\n", " ");
         StudentMessage = Wrap(StudentMessage, 60);
         
         if(tempMess.compareTo("") != 0){
            JOptionPane.showMessageDialog(this, StudentMessage);
         
         }
         
         dlgSC.setModal(true);
        
         
         dlgSC.setText("Would you like to run the session " + mySessionsArray[mGlobalSessionIndex].getSessionName() + "?");
         dlgSC.setLocationRelativeTo(this);
         dlgSC.setVisible(true);
         
         int res = dlgSC.getResponse();
         
         //int res = JOptionPane.showConfirmDialog(this, "Would you like to run the session " + mySessionsArray[mGlobalSessionIndex].getSessionName() + "?", "Run Session?", JOptionPane.YES_NO_CANCEL_OPTION);
         
         // Closes the Script File and Exits to main menu
         if(res == 2){
             scriptMode = false;
             mGlobalSessionIndex = 0;
             taxonomicLevel = null;
             mTaxa = null;
             return;
         }
         
         // Skips the currect Session to the next
        if (res == 1) {
            sessionType = mySessionsArray[mGlobalSessionIndex].getmType();
             if (sessionType.compareTo("study") != 0) {// Don't record a grade for a study session
                JOptionPane.showMessageDialog(this, "Session " + mySessionsArray[mGlobalSessionIndex].getSessionName() + " has been skipped.\nA zero has been recorded for this session.");
                recordZeroForSession();
            }
             else{
                  JOptionPane.showMessageDialog(this, "Session " + mySessionsArray[mGlobalSessionIndex].getSessionName() + " has been skipped.");
             }
          
            // Suppress the show progress if session is skipped!!
            showProgressQuiz = false;
            showProgressTest = false;
           
           
            quitStudyImages(null);
        } else {
            sessionType = mySessionsArray[mGlobalSessionIndex].getmType();
            if (sessionType.compareTo("study") == 0) { // must be study mode
              
                studyScripting();
            } else if (sessionType.compareTo("quiz") == 0) {
                          
                takeQuizScripting();
            } else {
                
                testScripting();
            }
        }
    }

     private void recordZeroForSession(){

        ScriptResult rs;
        rs = new ScriptResult(mySessionsArray[mGlobalSessionIndex].getmType(), mySessionsArray[mGlobalSessionIndex].getTaxaLevel(), mTaxa, mUserName,
                    0, mScriptName, mySessionsArray[mGlobalSessionIndex].getSessionName(),  0,
                    0, mScriptFileName);
        rs.writeResults();

     };
    


    private ActionListener quitStudyImagesCallback(){
       return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                quitStudyImages(evt);
            }
        };
    }
           
    private void FolderCheck() {

       // File toWork = new File(Configuration.UserPath() + "Grades");
         File toWork = new File(Configuration.ApplicationPath() + "/Grades");
        if (toWork.exists() == false) {
            toWork.mkdir();
        }
      //  toWork = new File(Configuration.UserPath() + "/Scripts");
       toWork = new File(Configuration.ApplicationPath() + "/Scripts");
        if (toWork.exists() == false) {
            toWork.mkdir();
        }
    }
   
    private void studyScripting(){
        executeScript("Study");
       }
 
    private String[][] getFileNames(){
        String levels;
        String fileN;
        ArrayList myAlist = new ArrayList();
        String[] imgTemp = mySessionsArray[mGlobalSessionIndex].getImages();
      
        String[][] fileNames = new String[imgTemp.length][2];
        for (int k = 0; k < imgTemp.length; k++) {
            imgTemp[k] = imgTemp[k].trim();
            fileN = imgTemp[k].substring(imgTemp[k].lastIndexOf(" ") + 1);
            levels = imgTemp[k].substring(0, imgTemp[k].lastIndexOf(" ") - 1);
            levels = levels.replace("|", " ");
            levels = levels.trim();
          
            fileNames[k][0] = fileN;
            fileNames[k][1] = levels;
        }
        
        // MUST SET TAXA HERE
        for (int i = 0; i < imgTemp.length; i++) {
            if (!stringExistInArray(myAlist, fileNames[i][1])) {
                myAlist.add(fileNames[i][1]);
            }
        }
        mTaxa = null;
        mTaxa = new String[myAlist.size()];
        for (int i = 0; i < myAlist.size(); i++) {
            mTaxa[i] = myAlist.get(i).toString();
        }
        return fileNames;
    }
    
    private void executeScript(String scriptType) {
       
        studying = (scriptType.compareTo("Study") == 0);
        if (studying) {
            programState = 1;
        } else {
            programState = 2;
            JOptionPane.showMessageDialog(this, "You are about to start a " + scriptType + " from a script. Make sure you have time to complete it.\nThe session will start immediately, after clicking on OK.");
        }

        setWorkingState();
        if (studying) {
            if (mySessionsArray[mGlobalSessionIndex].getCommonName() == true) {
                taxonomicLevel = "Common Name";
            } else {
                taxonomicLevel = mySessionsArray[mGlobalSessionIndex].getTaxaLevel();
            }
        } else {            // Test or Quiz initializations.
            taxonomicLevel = mySessionsArray[mGlobalSessionIndex].getTaxaLevel();
            if (scriptType.compareTo("Test") == 0) {
                mode = mySessionsArray[mGlobalSessionIndex].getModeTest() + 1;
            } else {
                mode = mySessionsArray[mGlobalSessionIndex].getModeQuiz() + 1;
            }
        }
        
        delayQT = mySessionsArray[mGlobalSessionIndex].getDelayTime();
        mFixationTime = mySessionsArray[mGlobalSessionIndex].getFixation();

        String[][] fileNames = getFileNames();
        if (studying) {               //Study
            runStudyScript(fileNames);
        } else {                      //Test or Quiz
            SessionInfo.SpellingValue = mySessionsArray[mGlobalSessionIndex].getSpelling();
            showProgressQuiz = mySessionsArray[mGlobalSessionIndex].getShowProg();
            if (scriptType.compareTo("Test") == 0) {
                runTestScript(fileNames);
            } else {
                runQuizScript(fileNames);
            }
        }
    }
    
    private void runQuizScript(String[][] fileNames) {
        SessionInfo session = new SessionInfo(this, lbPictureBox, fileNames, delayQT,
                taxonomicLevel, mTaxa, this.jPanel1, quitStudyImagesCallback(), mUserName,
                false, null, mFixationTime, progress, mScriptName, mySessionsArray[mGlobalSessionIndex].getSessionName(), mScriptFileName);
          
       // quiz = new QuizClass(session, mode, SessionInfo.SpellingValue, inSession,taxonomicLevel);
        quiz = new QuizClass(session, mode, SessionInfo.SpellingValue, inSession,taxonomicLevel, this);
       // jMenuBar1.setVisible(false);
        quiz.start();
    }
    private void runStudyScript(String[][] fileNames) {
        mShowFamilyName = mySessionsArray[mGlobalSessionIndex].getShowFamilyName(); // REPLACE
        mKeyToContinue = mySessionsArray[mGlobalSessionIndex].getStopUntilKeyPress();
        mAutoImageDisplay = !mySessionsArray[mGlobalSessionIndex].getUseArrowKeys();
        mShowImageWithName = !mySessionsArray[mGlobalSessionIndex].getImageOnly();
       
        //mStudyClass = new StudyImageClass(this, lbPictureBox, mySessionsArray[mGlobalSessionIndex].getRandomize(),
       // quitStudyImagesCallback(), fileNames, mShowFamilyName, taxonomicLevel,this,this.jPanel1); // this,this.jPanel1 Added by preethy on 28-01-2012
        
        /**
         * following constructor call is added by anurag to provide alphabatical sorting for study scripting functionality
        **/
        mStudyClass = new StudyImageClass(this, lbPictureBox, mySessionsArray[mGlobalSessionIndex].getRandomize(),
        quitStudyImagesCallback(), fileNames, mShowFamilyName, taxonomicLevel,this,this.jPanel1, true); 
        
        mStudyClass.setDisplayTime(delayQT);
        mStudyClass.setFixationTime(mFixationTime);
        mStudyClass.setKeyToContinue(mKeyToContinue);
        mStudyClass.setAutoRun(mAutoImageDisplay);
        mStudyClass.setImageWithName(mShowImageWithName);
       // jMenuBar1.setVisible(false);
        mStudyClass.start();
    }
    private void runTestScript(String[][] fileNames) {
        SessionInfo mSessionInfo = new SessionInfo(this, lbPictureBox, fileNames, delayQT, taxonomicLevel, mTaxa, this.jPanel1, quitStudyImagesCallback(),
                mUserName, true, null, mFixationTime, progress, mScriptName, mySessionsArray[mGlobalSessionIndex].getSessionName(), mScriptFileName);

        test = new TestClass(mSessionInfo, mode, scriptMode, mScriptName, mySessionsArray[mGlobalSessionIndex].getSessionName());
        //jMenuBar1.setVisible(false);
        test.start();
    }
    
    private void testScripting() {
        executeScript("Test");
    }

    private void takeQuizScripting() {
        executeScript("Quiz");
    }

    private boolean stringExistInArray(ArrayList al, String temp) {
        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).toString().compareTo(temp) == 0) {
                return true;
            }
        }
        return false;
    }

    private String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

    private String findScriptFileName() {
    
      //  String path = Configuration.UserPath();
         String path = Configuration.ApplicationPath();
        FileWriter myFileWriter = null;
        String filename;
        boolean fileExists;
        int filenamenumber = 1;

        filename = CSVFileName(path, getCurrentDateString(), filenamenumber);
      
        fileExists = (new File(filename)).exists();

        while (fileExists) {
            filenamenumber++;
            filename = CSVFileName(path, getCurrentDateString(), filenamenumber);
            fileExists = (new File(filename)).exists();
            
        }
        
        return filename;
    }

    private String CSVFileName(String path, String date, int FileNameNumber) {
        return path + "/Grades/" + mScriptName + "_" +  mUserName + "_" +  date.replace('/', '-')  +  "_" + String.valueOf(FileNameNumber) + ".csv";
    }
    
    
    private boolean VerifyDatabase(){
       
         // Test validation of database
        if (DataBaseCheckEnabled) {
            VerifyDatabase objVerifyDB = new VerifyDatabase(Configuration.DataBaseName(), Configuration.DataBaseHashValue());
         
            if (!objVerifyDB.IsOK()) {
                JOptionPane.showMessageDialog(this, "The DataBase has been modified. Please reinstall the program or replace the database with the original file.");
                return false;

            }
 
        }
         return true;
        
    }
    //Added by preethy on 17-01-2012
     private void selectSpecies() {
         DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
        /*  if (db.DataBaseExists() == false) {
            JOptionPane.showMessageDialog(this, "You must select an image package to continue the program.");
            return;
          }*/
         
         SelectSpecies selSpecies=new SelectSpecies(db,maxImages);
         selSpecies.setLocationRelativeTo(this);
         selSpecies.setVisible(true);
         if(selSpecies.bCancel || selSpecies.esc){
               if(selSpecies.getItems().length!=0){
                mTaxa = null; // These are the taxa that will be studied.
                taxonomicLevel = null; // This is the level that will be studied.
               }
            } else {
             if((taxonomicLevel==null) && (mTaxa==null)){
                selctfamilyTaxanull=true;
             }
               taxonomicLevel=selSpecies.getTaxaLevel();//selected taxa level from select species
             
               mTaxa = selSpecies.getItems(); //selected taxa 
               selCname=selSpecies.CommonNames;//if common name is selected or not
               
            if (!selCname) {
                selScientificName = false;
            }
            
               group=false;//Addedd by preethy on 12-07-2012
               sel=new ArrayList();// afer selecting select species by family already selcted filenames initialized to zero
               isFromTexaSelect = false;
            }
     }
     private void search() {
         
         Search_table pr=new Search_table(mTaxa,taxonomicLevel,this,maxImages);
          pr.setLocationRelativeTo(this);
          pr.setVisible(true);
       
         /* if(pr.close==true){
              showMenuButtons();
              
         }*/
     }
      private void addImagesAndDatabase() throws IOException, InterruptedException {
    
      
       String desktopStr=System.getProperty("user.home");
    
       JFileChooser chooser = new JFileChooser();
       chooser.setCurrentDirectory(new File(desktopStr+"/Desktop"));
       chooser.setFileFilter(new FileFilter() {
        public boolean accept(File f) {
          return f.getName().toLowerCase().endsWith(".zip");//accept only .zip files
        }

        public String getDescription() {
          return "ZIP Files";
        }
      });
            int r = chooser.showOpenDialog(this);
            String path = Configuration.ApplicationPath();
        //  String path = Configuration.UserPath();
            if (r== JFileChooser.APPROVE_OPTION) {
             File tempFile = chooser.getSelectedFile();//select zip file
            String temp = tempFile.getAbsolutePath();
            
            int size=temp.lastIndexOf("\\");
            String zip= temp.substring(size+1);
            
            ZipInputStream in = null;
            int i=0;
              copyFilesThread cp;
             if(zip.endsWith("zip"))
            {
            int retVal = JOptionPane.showConfirmDialog(this, "The old database and images will be erased, and the new\n\r database and images in "+zip+" will be installed in their place.\n\rAre you sure you want to continue?", "No Taxa Selected!", JOptionPane.OK_CANCEL_OPTION);

            if(retVal == JOptionPane.OK_OPTION)
            {
                cp = new copyFilesThread(this,temp);//this thred extrcts and copy the contents of zip files correct location
                cp.start();
              
            }
            }else {
                  JOptionPane.showMessageDialog(this, "Please select zip files only");
             }
            
            }
      }
      private void selectByGroup() {
          
            DataBaseDriver db = new DataBaseDriver(Configuration.DataBaseName());
            SelectImage2 selImage=new SelectImage2(db,maxImages);
            selImage.esc=false;//whether esc key pressed or not 
            selImage.setLocationRelativeTo(this);
            selImage.setVisible(true);
            sel=selImage.db.selectedRow;//this array list contains the rows of csv file, selected through select by group
         
            for(int i=0;i<sel.size();i++){
                  
            }
            group=selImage.select;
            if(selImage.esc==true)//if esc is pressed then the array list is cleared
              {
                sel=new ArrayList();
              }
           
            String tmp = null;
        tmp = selImage.getTaxaLevel();

        if (tmp.equals("Common Name")) {
            selCname = true;
            selScientificName = false;
        } else if (tmp.equals("Species")) {
            selScientificName = true;
            selCname = false;
        }
        
            if(group){
             taxonomicLevel=selImage.getTaxaLevel();//selected taxa level from select by group
             mTaxa=db.taxaList; //selected taxa
             isFromTexaSelect = false;
            }
      }
  
      /**
     * 2
     * added by anurag
     *
     */
    private void saveTexaSet() throws IOException {
        MYFolderCheck();
        
        String selectBy = null;

        if (selCname) {
            selectBy = "Common Name";
        } else if (selScientificName) {
            selectBy = "Species Name";
        }

        this.btnOkClicked = false;
        
        CurrentTexaInformationDialog informationDialog = new CurrentTexaInformationDialog(this, true, "Save Taxa Set");
        informationDialog.setCmnName(selectBy);
        informationDialog.setmTaxa(mTaxa);
        informationDialog.setTaxonomicLevel(taxonomicLevel);
        
        informationDialog.setLocationRelativeTo(this);
        informationDialog.setModal(true);
        informationDialog.setVisible(true);


        if (btnOkClicked) {
        
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("dat");
            filter.setDescription("Save Taxa Set");
            JFileChooser myFileBrowser = new JFileChooser();
            myFileBrowser.setDialogTitle("Save");
            File f = null;
            try {
                f = new File(Configuration.ApplicationPath() + "/" + savedTaxaSetsPath);
                myFileBrowser.setCurrentDirectory(f);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            myFileBrowser.setMultiSelectionEnabled(false);
            myFileBrowser.setFileFilter(filter);
            myFileBrowser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            myFileBrowser.setApproveButtonText("Save");
            myFileBrowser.setApproveButtonToolTipText("Saved Taxa Set file");

            int returnVal = myFileBrowser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                
            } else {
                return;
            }

            String path = null;
            // CHECK IF FILE EXISTS AND ASK TO OVERWRITE
            // If file exist you can write the file !!
            String tempFile = myFileBrowser.getSelectedFile().getName();
            if (tempFile.indexOf(".") != -1) {
                tempFile = tempFile.substring(0, tempFile.indexOf("."));
            }
            tempFile = tempFile + ".dat";
            path = myFileBrowser.getCurrentDirectory().getPath();
            boolean exists = (new File(path + "\\" + tempFile)).exists();

            if (exists) {
                int res = JOptionPane.showConfirmDialog(this, "Would you like to overwrite " + tempFile + "?");
                if (res == JOptionPane.YES_OPTION) {
                    if (isValidFileName(tempFile)) {
                        saveTexaFile(path, tempFile);
                    } else {
                        JOptionPane.showMessageDialog(this, "File names cannnot contain / \\ : * ? \" < > | ");
                        return;
                    }
                }
            } else {
                if (isValidFileName(tempFile)) {
                    saveTexaFile(path, tempFile);
                } else {
                    JOptionPane.showMessageDialog(this, "File names cannnot contain / \\ : * ? \" < > | ");
                    return;
                }
            }
        }
        this.btnOkClicked = false;
    }

    public boolean saveTexaFile(String filePath, String fileName) {
        boolean status = false;
        
        
        String selectBy = null;

        if (selCname) {
            selectBy = "Common Name";
        } else if (selScientificName) {
            selectBy = "Species";
        }

        UserDataBean bean = new UserDataBean();
        bean.setTaxonomicLevel(taxonomicLevel);
        bean.setmTaxa(mTaxa);
        bean.setSelectBy(selectBy);
        bean.setSelCname(selCname);
        bean.setSel(sel);
        bean.setGroup(group);
        bean.setTaxanull(taxanull);
        bean.setSelctfamilyTaxanull(selctfamilyTaxanull);
        
        try {

            OutputStream os = new FileOutputStream(filePath + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(bean);
            oos.flush();
            oos.close();
            os.close();
            return true;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, e);
        }
        finally{
            
        }
        return status;
    }
    
    private void MYFolderCheck() {
        File toWork = new File(Configuration.ApplicationPath() + "/" + savedTaxaSetsPath);
        if (toWork.exists() == false) {
            toWork.mkdir();
        }
    }
    
    public boolean isValidFileName(String name) {

        if (name.indexOf("\"") > 0) {
            return false;
        }
        if (name.indexOf("\\") > 0) {
            return false;
        }
        if (name.indexOf("/") > 0) {
            return false;
        }
        if (name.indexOf(":") > 0) {
            return false;
        }
        if (name.indexOf("*") > 0) {
            return false;
        }
        if (name.indexOf("?") > 0) {
            return false;
        }
        if (name.indexOf("<") > 0) {
            return false;
        }
        if (name.indexOf(">") > 0) {
            return false;
        }
        if (name.indexOf("|") > 0) {
            return false;
        }
        return true;
    }
    
    private boolean loadTexa(String filePath) {
        
        InputStream is = null;
            ObjectInputStream inputStream = null;
            
        try {
             is = new FileInputStream(filePath);
             inputStream = new ObjectInputStream(is);
            UserDataBean dataBean = (UserDataBean) inputStream.readObject();
            
            this.btnOkClicked = false;
                    
            CurrentTexaInformationDialog informationDialog = new CurrentTexaInformationDialog(this, true,"Load Saved Taxa Set");
            informationDialog.setCmnName(dataBean.getSelectBy());
            informationDialog.setmTaxa(dataBean.getmTaxa());
            informationDialog.setTaxonomicLevel(dataBean.getTaxonomicLevel());
            informationDialog.setLocationRelativeTo(this);
            informationDialog.setModal(true);
            informationDialog.setVisible(true);
            
            if(this.btnOkClicked) {
                this.mTaxa = dataBean.getmTaxa();
                this.taxonomicLevel = dataBean.getTaxonomicLevel();
                this.selCname = dataBean.isSelCname();
                this.sel = dataBean.getSel();
                this.group = dataBean.isGroup();
                this.taxanull = dataBean.isTaxanull();
                this.selctfamilyTaxanull = dataBean.isSelctfamilyTaxanull();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
        }
        finally {
            try {
                inputStream.close();
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.btnOkClicked = false;
        return true;
    }

    private boolean openTexaFile() {
        boolean status = false;
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("dat");
        filter.setDescription("Saved Taxa Set file");
        JFileChooser myFileBrowser = new JFileChooser();
        myFileBrowser.setDialogTitle("Load Saved Taxa Set");
        File f = null;
        try {
            f = new File(Configuration.ApplicationPath() + "/" + savedTaxaSetsPath);
            myFileBrowser.setCurrentDirectory(f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        myFileBrowser.setMultiSelectionEnabled(false);
        myFileBrowser.setFileFilter(filter);
        myFileBrowser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        myFileBrowser.setApproveButtonText("Open");
        myFileBrowser.setApproveButtonToolTipText("Open Texa File");

        int returnVal = myFileBrowser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String tempFile = myFileBrowser.getSelectedFile().getAbsolutePath();
            loadTexa(tempFile);
        } else {
            return false;
        }
        return status;
    }
}