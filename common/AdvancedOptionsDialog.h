#pragma once

#include <QDialog>
#include <QGroupBox>
#include <QLabel>
#include <QComboBox>
#include <QSlider>
#include <QCheckBox>
#include <QPushButton>

class AdvancedOptionsDialog : public QDialog
{
    Q_OBJECT
    static AdvancedOptionsDialog * s_instance;
    
    int width;     // width of the dialog
    int height;    // height of the dialog

    QGroupBox * spellingGroupBox;
    QLabel * group1Line1Label;     
    QLabel * group1Line2Label;
    QLabel * group1Line3Label;
    QLabel * group1Line4Label;
    QComboBox * sensitivityComboBox;

    QGroupBox * numberOfImagesGroupBox;
    QLabel * group2Line1Label;
    QLabel * group2Line2Label;
    QComboBox * numberOfImagesComboBox;

    QGroupBox * fixationSpeedGroupBox;
    QSlider * fixationIntervalSlider;
    QLabel * label1;
    QLabel * label2;
    QLabel * label3;
    QLabel * label4;

    QGroupBox * progressGroupBox;
    QCheckBox * afterQuizCheckBox;
    QCheckBox * afterTestCheckBox;

    QPushButton * applyButton;    // button to apply changes
    QPushButton * cancelButton;   // button to cancel changes and hide dialog

    int spellingSensitivityIndex;
    int numberOfImagesIndex;
    int fixationInterval;
    bool viewProgressAfterQuizChecked;
    bool viewProgressAfterTestChecked;

    void setOptions();            // set advanced option variables in the Quiz and Study dialog
    void resetOptions();          // set the advanced options dialog to show current state

    AdvancedOptionsDialog(QWidget * parent = 0);
    ~AdvancedOptionsDialog();
   
public:
   void updateVariables(QString); // update the state variables with the value of the previous session
   QString getSettings();         // return '|' delimited string of the state variables to save to file
   
   static AdvancedOptionsDialog * instance(QWidget * parent = 0);

public slots:
    void setFixationInterval(int);
    void applyClicked();
    void cancelClicked();
    void closeEvent(QCloseEvent *);
    void reject();
};
