#include "AdvancedOptionsDialog.h"
#include "StudyDialog.h"
#include "QuizDialog.h"
#include "sysscale.h"

#include <QDebug>

#include <sstream>

AdvancedOptionsDialog * AdvancedOptionsDialog::s_instance = nullptr;

AdvancedOptionsDialog::AdvancedOptionsDialog(QWidget * parent)
    : QDialog( parent )
{
    width = ppp(400);
    height = ppp(460);

    setGeometry(QRect(0, 0, width, height));
    setMinimumSize(width, height);
    setMaximumSize(width, height);
    setWindowTitle("Advanced Options");

    spellingGroupBox = new QGroupBox("Spelling", this);
    spellingGroupBox->setGeometry(QRect(ppp(10), ppp(10), width - ppp(20), ppp(125)));

    group1Line1Label = new QLabel("This setting sets the sensitivity of the spelling in both", spellingGroupBox);
    group1Line1Label->setGeometry(QRect(ppp(10), ppp(30), width - ppp(40), ppp(20)));
    group1Line1Label->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);

    group1Line2Label = new QLabel("quiz and test modes. The higher the setting, the better", spellingGroupBox);
    group1Line2Label->setGeometry(QRect(ppp(10), ppp(50), width - ppp(40), ppp(20)));
    group1Line2Label->setAlignment( Qt::AlignHCenter | Qt::AlignVCenter );

    group1Line3Label = new QLabel("the spelling must be. Eg. 100% is perfect spelling.", spellingGroupBox);
    group1Line3Label->setGeometry(QRect(ppp(10), ppp(70), width - ppp(40), ppp(20)));
    group1Line3Label->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);

    group1Line4Label = new QLabel("Spelling sensitivity : ", spellingGroupBox);
    group1Line4Label->setGeometry(QRect((width - ppp(40)) / 2 - ppp(65), ppp(90), ppp(200), ppp(20)));
    group1Line4Label->setAlignment(Qt::AlignVCenter);

    sensitivityComboBox = new QComboBox(spellingGroupBox);
    sensitivityComboBox->setGeometry(QRect((width - ppp(40)) / 2 + ppp(75), ppp(90), ppp(60 + 20), ppp(20)));
    sensitivityComboBox->addItem("100%");
    sensitivityComboBox->addItem("90%");
    sensitivityComboBox->addItem("80%");
    sensitivityComboBox->addItem("70%");

    numberOfImagesGroupBox = new QGroupBox("Number of Images", this);
    numberOfImagesGroupBox->setGeometry(QRect(ppp(10), ppp(140), width - ppp(20), ppp(80)));

    group2Line1Label = new QLabel("Please select the total number of images in", numberOfImagesGroupBox);
    group2Line1Label->setGeometry(QRect(ppp(10), ppp(30), width - ppp(40), ppp(20)));
    group2Line1Label->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);

    group2Line2Label = new QLabel("the quiz and test sessions.", numberOfImagesGroupBox);
    group2Line2Label->setGeometry(QRect((width - ppp(40)) / 2 - ppp(90), ppp(50), ppp(200), ppp(20)));

    numberOfImagesComboBox = new QComboBox(numberOfImagesGroupBox);
    numberOfImagesComboBox->addItem("Show All");

    for (int i = 15; i <= 45; i += 5)
    {
        std::stringstream s;
        s << i;
        numberOfImagesComboBox->addItem(s.str().c_str());
    }

    numberOfImagesComboBox->setGeometry(QRect((width - ppp(40)) / 2 + ppp(85), ppp(50), ppp(70 + 20), ppp(20)));

    fixationSpeedGroupBox = new QGroupBox("Fixation Speed", this);
    fixationSpeedGroupBox->setGeometry(QRect(ppp(10), ppp(225), width - ppp(20), ppp(95)));

    label1 = new QLabel(fixationSpeedGroupBox);
    label1->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    label1->setText("Fixation Display time: (0.5 - 3 seconds)");
    label1->setGeometry(QRect(0, ppp(30), width - ppp(20), ppp(20)));

    label2 = new QLabel(fixationSpeedGroupBox);
    label2->setText("Less");
    label2->setGeometry(QRect(ppp(15), ppp(47), ppp(50), ppp(20)));

    label3 = new QLabel(fixationSpeedGroupBox);
    label3->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    label3->setText("0.5 seconds");
    label3->setGeometry(QRect(0, ppp(60), width - ppp(20), ppp(20)));

    label4 = new QLabel(fixationSpeedGroupBox);
    label4->setText("More");
    label4->setGeometry(QRect(ppp(330), ppp(47), ppp(50), ppp(20)));

    fixationIntervalSlider = new QSlider( fixationSpeedGroupBox );
    fixationIntervalSlider->setOrientation( Qt::Horizontal );
    fixationIntervalSlider->setMinimum(5);
    fixationIntervalSlider->setMaximum(30);
    fixationIntervalSlider->setValue(5);
    fixationIntervalSlider->setGeometry(QRect(ppp(45), ppp(47), ppp(280), ppp(20)));

    QObject::connect(fixationIntervalSlider, SIGNAL(valueChanged(int)), this, SLOT(setFixationInterval(int)));

    progressGroupBox = new QGroupBox( "Progress", this );
    progressGroupBox->setGeometry(QRect(ppp(10), ppp(325), width - ppp(20), ppp(80)));

    afterQuizCheckBox = new QCheckBox( "Show progress after quiz.", progressGroupBox );
    afterTestCheckBox = new QCheckBox( "Show progress after test.", progressGroupBox );
    afterQuizCheckBox->setGeometry(QRect(width / 2 - ppp(80), ppp(30), ppp(200), ppp(20)));
    afterTestCheckBox->setGeometry(QRect(width / 2 - ppp(80), ppp(50), ppp(200), ppp(20)));

    applyButton = new QPushButton( "Apply", this );
    applyButton->setDefault(true);
    applyButton->setAutoDefault(true);
    applyButton->setGeometry(QRect(ppp(10), ppp(415), width / 2 - ppp(15), ppp(25)));

    QObject::connect(applyButton, SIGNAL(clicked()), this, SLOT(applyClicked()));

    cancelButton = new QPushButton( "Cancel", this);
    cancelButton->setGeometry(QRect(width / 2 + ppp(5), ppp(415), width / 2 - ppp(15), ppp(25)));
    QObject::connect(cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));

    spellingSensitivityIndex = 0;             // Option 0 = 100% sensitivity
    numberOfImagesIndex = 0;                  // Option 0 = show all images
    fixationInterval = 5;                     // fixation invertal is 5*100 msec
    viewProgressAfterQuizChecked = false;     // Flag to view progress report after the Quiz
    viewProgressAfterTestChecked = false;     // Flag to view progress report after the Test
}

// Read the user file to find out the advanced
// options that were set during the last session for the user
// fname: user filename
void AdvancedOptionsDialog::updateVariables(QString fname)
{
    QFile inFile;
    inFile.setFileName(fname + ".csv");
    qDebug() << "opening settings file " << fname;
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning( this, "No Advanced Settings",
        tr("<b>Session is configured using default advanced settings.</b>"));
        return;
    }

    QTextStream in;
    in.setDevice(&inFile);

    // Read the first line of the user file
    // If the header of the first line is 'AdvancedOptions', a previous sessions was saved
    // otherwise do nothing, constructor would set the variables to default values.

    QString line = in.readLine();
    QStringList options = line.split(",");
    bool ok;

    if (options[0] == "AdvancedOptions")
    {
        spellingSensitivityIndex = options[1].toInt(&ok, 10);
        numberOfImagesIndex = options[2].toInt(&ok, 10 );
        fixationInterval = options[3].toInt(&ok, 10);
        viewProgressAfterQuizChecked = options[4].toInt(&ok, 10);
        QuizDialog::instance()->setQuizProgressReport( viewProgressAfterQuizChecked );
        viewProgressAfterTestChecked = options[5].toInt(&ok, 10);
        QuizDialog::instance()->setTestProgressReport( viewProgressAfterTestChecked );
    }
    
    setOptions();        // Set advanced option variables in the Quiz and Study dialog
    resetOptions();      // Set the advanced options dialog to show current state
    inFile.close();
}

// Set advanced option variables in the Quiz and Study dialog
void AdvancedOptionsDialog::setOptions()
{
    QuizDialog::instance()->setSpellingSensitivity(spellingSensitivityIndex);
    QuizDialog::instance()->setNumberOfImages(numberOfImagesIndex);
    StudyDialog::instance()->setFixationInterval(fixationInterval * 100);
    QuizDialog::instance()->setFixationInterval(fixationInterval * 100);
    QuizDialog::instance()->setQuizProgressReport(viewProgressAfterQuizChecked);
    QuizDialog::instance()->setTestProgressReport(viewProgressAfterTestChecked);
}

void AdvancedOptionsDialog::applyClicked()
{
    spellingSensitivityIndex = sensitivityComboBox->currentIndex();
    numberOfImagesIndex = numberOfImagesComboBox->currentIndex();
    fixationInterval = fixationIntervalSlider->value();
    viewProgressAfterQuizChecked = afterQuizCheckBox->isChecked();
    viewProgressAfterTestChecked = afterTestCheckBox->isChecked();
    setOptions();
    hide();
}

// Set the advanced options dialog to show current state
void AdvancedOptionsDialog::resetOptions()
{
    sensitivityComboBox->setCurrentIndex(spellingSensitivityIndex);
    numberOfImagesComboBox->setCurrentIndex(numberOfImagesIndex);
    fixationIntervalSlider->setValue(fixationInterval);
    setFixationInterval(fixationInterval);
    afterQuizCheckBox->setChecked(viewProgressAfterQuizChecked);
    afterTestCheckBox->setChecked(viewProgressAfterTestChecked);
}

void AdvancedOptionsDialog::cancelClicked()
{
    resetOptions();
    hide();
}

// Same as clicking cancel
void AdvancedOptionsDialog::closeEvent(QCloseEvent *)
{
    cancelClicked();
}

// If the user presses the ESCAPE key in a dialog, QDialog::reject() will be called
// Overriding reject() to do nothing
void AdvancedOptionsDialog::reject()
{
}

// Updates the slider label to show the current fixation interval
void AdvancedOptionsDialog::setFixationInterval(int value)
{
    std::stringstream s;
    s << value / 10 << "." << value % 10 << " seconds";
    label3->setText(s.str().c_str());
}

// Return a '|'-delimited concatentated string of the state variables to store to file
QString AdvancedOptionsDialog::getSettings()
{
    QString settings="AdvancedOptions,";
    settings.append(QString::number(spellingSensitivityIndex));
    settings.append(",");
    settings.append(QString::number(numberOfImagesIndex));
    settings.append(",");
    settings.append(QString::number(fixationInterval));
    settings.append(",");
    settings.append(QString::number(viewProgressAfterQuizChecked));
    settings.append(",");
    settings.append(QString::number(viewProgressAfterTestChecked));
    settings.append("\n");
    return settings;
}

AdvancedOptionsDialog * AdvancedOptionsDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new AdvancedOptionsDialog(parent);
    return s_instance;
}

AdvancedOptionsDialog::~AdvancedOptionsDialog()
{
}
