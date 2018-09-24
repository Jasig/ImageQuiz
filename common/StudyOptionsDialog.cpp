#include "StudyOptionsDialog.h"
#include "StudyDialog.h"
#include "sysfolders.h"
#include "sysscale.h"

#include <QDebug>

#include <sstream>

#ifdef __APPLE__
#define COMBOBOX_H 30
#else
#define COMBOBOX_H 20
#endif

StudyOptionsDialog * StudyOptionsDialog::s_instance = nullptr;

StudyOptionsDialog::StudyOptionsDialog(QWidget * parent)
    : QDialog(parent)
{
    width = ppp(300);
    height = ppp(340);

    setGeometry(QRect(0, 0, width, height));
    setMaximumSize(width, height);
    setMinimumSize(width, height);
    setWindowTitle("Study Options");
    setFocusPolicy(Qt::StrongFocus);

    modeSelectGroupBox = new QGroupBox(this);
    modeSelectGroupBox->setGeometry(QRect(ppp(10), ppp(10), ppp(280), ppp(145)));
    modeSelectGroupBox->setTitle("Mode / Settings");

    orderComboBox = new QComboBox(modeSelectGroupBox);
    orderComboBox->setGeometry(QRect(ppp(10), ppp(30), ppp(260), ppp(COMBOBOX_H)));
    orderComboBox->addItem("Alphabetical Order");
    orderComboBox->addItem("Randomize");
    orderComboBox->setCurrentIndex(0);

    interactionComboBox = new QComboBox(modeSelectGroupBox);
    interactionComboBox->setGeometry(QRect(ppp(10), ppp(60), ppp(260), ppp(COMBOBOX_H)));
    interactionComboBox->addItem("Use Arrow Keys");
    interactionComboBox->addItem("Auto Image Display");
    interactionComboBox->setCurrentIndex(0);

    QObject::connect(interactionComboBox, SIGNAL(activated(int)), this, SLOT(setInteractionOption(int)));

    nameComboBox1 = new QComboBox( modeSelectGroupBox );
    nameComboBox1->setGeometry(QRect(ppp(10), ppp(90), ppp(260), ppp(COMBOBOX_H)));
    nameComboBox1->addItem("Show Image With Name");
    nameComboBox1->addItem("Show Image Only");
    nameComboBox1->setCurrentIndex(0);

    nameComboBox2 = new QComboBox(modeSelectGroupBox);
    nameComboBox2->setGeometry(QRect(ppp(10), ppp(90), ppp(260), ppp(COMBOBOX_H)));
    nameComboBox2->addItem("Show Name With Image");
    nameComboBox2->addItem("Show Name After Image");
    nameComboBox2->setCurrentIndex(0);
    nameComboBox2->setVisible(false);

    QObject::connect(nameComboBox2, SIGNAL(activated(int)), this, SLOT(setNameOption2(int)));

    stopCheckBox = new QCheckBox(modeSelectGroupBox);
    stopCheckBox->setGeometry(QRect(ppp(80), ppp(115), ppp(200), ppp(20)));
    stopCheckBox->setText("Stop until key press");
    stopCheckBox->setEnabled(false);

    speedGroupBox = new QGroupBox(this);
    speedGroupBox->setTitle("Speed");
    speedGroupBox->setGeometry(QRect(ppp(10), ppp(165), ppp(280), ppp(120)));

    label1 = new QLabel(speedGroupBox);
    label1->setText("Image Display time: (0.1 - 4 seconds)" );
    label1->setGeometry(QRect(ppp(20), ppp(25), ppp(280), ppp(20)));
    label1->setEnabled(false);

    label2 = new QLabel(speedGroupBox);
    label2->setText("Less");
    label2->setGeometry(QRect(ppp(10), ppp(60), ppp(50), ppp(20)));
    label2->setEnabled(false);

    label3 = new QLabel(speedGroupBox);
    label3->setText("1.0 seconds");
    label3->setGeometry(QRect(ppp(100), ppp(90), ppp(280), ppp(20)));
    label3->setEnabled(false);

    label4 = new QLabel(speedGroupBox);
    label4->setText("More");
    label4->setGeometry(QRect(ppp(245), ppp(60), ppp(50), ppp(20)));
    label4->setEnabled(false);

    speedSlider = new QSlider(speedGroupBox);
    speedSlider->setEnabled(false);
    speedSlider->setOrientation(Qt::Horizontal);
    speedSlider->setMinimum(1);
    speedSlider->setMaximum(40);
    speedSlider->setValue(10);
    speedSlider->setGeometry(QRect(ppp(40), ppp(60), ppp(200), ppp(20)));
    QObject::connect(speedSlider, SIGNAL(valueChanged(int)), this, SLOT(setInterval(int)));

    continueButton = new QPushButton("Continue", this);
    continueButton->setGeometry(QRect(ppp(10), ppp(295), ppp(135), ppp(25)));
    continueButton->setDefault(true);
    continueButton->setAutoDefault(true);

    QObject::connect(continueButton, SIGNAL(clicked()), this, SLOT(continueClicked()));

    cancelButton = new QPushButton("Cancel", this);
    cancelButton->setGeometry(QRect(ppp(155), ppp(295), ppp(135), ppp(25)));
    QObject::connect(cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));

    orderOption = 0;
    interactionOption = 0;
    name1Option = 0;
    name2Option = 0;
    stopOption = false;
    speed = 10;

#ifndef APP_AMINO
    propertyType = 0;
    answerType = 0;
#endif
}

// Set interaction option
void StudyOptionsDialog::setInteractionOption(int index)
{
    // case 0: -> Use Arrow Keys
    // case 1: -> Auto Image display
    switch (index)
    {
        case 0:
            speedSlider->setValue(speed);
            speedSlider->setEnabled(false);
            label1->setEnabled(false);
            label2->setEnabled(false);
            label3->setEnabled(false);
            label4->setEnabled(false);

            nameComboBox1->setVisible(true);
            nameComboBox1->setEnabled(true);

            nameComboBox2->setVisible(false);
            nameComboBox2->setEnabled(false);
            stopCheckBox->setChecked(false);
            stopCheckBox->setEnabled(false);
            break;
        case 1:
            speedSlider->setEnabled(true);
            label1->setEnabled(true);
            label2->setEnabled(true);
            label3->setEnabled(true);
            label4->setEnabled(true);

            nameComboBox1->setVisible(false);
            nameComboBox1->setEnabled(false);

            nameComboBox2->setVisible(true);
            nameComboBox2->setEnabled(true);
            stopCheckBox->setChecked(false);
            stopCheckBox->setEnabled(false);
            break;
    }
}

// Set the naming option
void StudyOptionsDialog::setNameOption2(int index)
{
    // case 0: -> Show name with image
    // case 1: -> Show name after image
    switch (index)
    {
        case 0:
            // disable stop box and uncheck
            stopCheckBox->setChecked(false);
            stopCheckBox->setEnabled(false);
            break;
        case 1:
            // enable stop box
            stopCheckBox->setEnabled(true);
            break;
    }
}

// Set the bg color
void StudyOptionsDialog::setBackgroundColor(int color)
{
    StudyDialog::instance()->setBackgroundColor(color);
}

// Save the coordinates of the center
void StudyOptionsDialog::setCoordinates( int center_x, int center_y )
{
    x = center_x - width / 2;
    y = center_y - height / 2;
}

// Update the slider
void StudyOptionsDialog::setInterval(int value)
{
	std::stringstream s;
	s << value / 10 << "." << value % 10 << " seconds";
	label3->setText(s.str().c_str());
}

#ifdef APP_AMINO
// Set image type
void StudyOptionsDialog::setImageType(QString iType, int color)
{
    iType.replace("&&", "&");

    qDebug() << "set image type '" << iType << "'";
    imageType = iType;
    StudyOptionsDialog::instance()->setBackgroundColor(color);
}

// Set name type
void StudyOptionsDialog::setNameType(int nType)
{
    nameType = nType;
}

// Set property type
void StudyOptionsDialog::setPropertyType(int pType)
{
    propertyType = pType;
}
#else
// Set property type
void StudyOptionsDialog::setPropertyType(int pType)
{
    propertyType = pType;
}

// Method to set answer type
// Name and Property
// Only Name
// Only Property
void StudyOptionsDialog::setAnswerType(int aType)
{
    answerType = aType;
}
#endif

// Randomize instead to displaying in
// alphabetical order. Just picking any two random
// numbers from the list and swaping their
// positions in the list
void StudyOptionsDialog::randomize()
{
    int size = StudyDialog::instance()->indexList.size();

    // Find two different indices and
    // then swap the elements.
    for (int i = 0; i < size; ++i)
    {
        int index1 = rand() % size;
        int index2 = rand() % size;

        while (index1 == index2)
            index2 = rand() % size;

        int temp = StudyDialog::instance()->indexList[index1];
        StudyDialog::instance()->indexList[index1] = StudyDialog::instance()->indexList[index2];
        StudyDialog::instance()->indexList[index2] = temp;
    }
}

// Create a list of indecies for the filename list
void StudyOptionsDialog::createIndexList()
{
    StudyDialog::instance()->indexList.clear();
    int size = StudyDialog::instance()->filenameList.size();

    // the list contains numbers 0 to size-1 in increasing order
    for (int i = 0; i < size; ++i)
        StudyDialog::instance()->indexList << i;

    if (orderOption == 1)
        randomize();
}

#ifdef APP_AMINO
// Create a Hash table
// <name> : <name+"|  "+property type>
void StudyOptionsDialog::createHashMap()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Properties.csv").c_str());
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
        QMessageBox::warning(this, "Invalid Filename", tr("Cannot be opened for reading"));

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

    while(!in.atEnd())
    {
        line = in.readLine();
        QStringList attributeList = line.split(",");
        QString key = attributeList.at(0);
        QString value;

        // if name type is not No Name
        if (nameType != 3)
            value = attributeList.at(nameType) + "\t";

        // if property type is not No Property
        if (propertyType != 5)
            value = value + attributeList.at(3 + propertyType);

        hash.insert(key, value);
    }

    inFile.close();
}
#endif

// Create a list of filenames of
// the chosen images
void StudyOptionsDialog::createFilenameList()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Database.csv").c_str());
    if (!inFile.open( QIODevice::ReadOnly | QIODevice::Text ))
        QMessageBox::warning(this, "Invalid Filename", tr("Cannot be opened for reading"));

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

#ifndef APP_AMINO
    QString propertyHeader = line.split(",").at(2 + propertyType);
#endif

    StudyDialog::instance()->filenameList.clear();
    StudyDialog::instance()->captionList.clear();

    while (!in.atEnd())
    {
        line = in.readLine();
        QStringList columns = line.split(",");

    #ifdef APP_AMINO
        // columns.at(0) has the file name
        // columns.at(1) has the type of image
        // columns.at(2) has the Amino Acid name
        if (columns.at(1) == imageType && imagesToStudy.contains(columns.at(2)))
        {
            StudyDialog::instance()->filenameList << columns.at(0);
            StudyDialog::instance()->captionList << hash[columns.at(2)];
        }
    #else
        // columns.at(0) has the file name
        // columns.at(1) has the name of image
        // columns.at(2 + propertyType) has the property name (the 1st property will be in column 2 and so on)
        if (imagesToStudy.contains(columns.at(1)))
        {
            StudyDialog::instance()->filenameList << columns.at(0);

            QString propertyName = columns.at(2 + propertyType);
            if (propertyName.length() == 0)
                propertyName = "No " + propertyHeader;
            QString caption;
            switch (answerType)
            {
            case 0: // name and property
                caption = columns.at(1) + " " + propertyName;
                break;

            case 1: // only name
                caption = columns.at(1);
                break;

            case 2: // only property
                caption = propertyName;
                break;
            }


            StudyDialog::instance()->captionList << caption;
        }
    #endif
    }

    inFile.close();
}


// Called when continue is clicked
// to pass on the settings to the study dialog
void StudyOptionsDialog::setOptions()
{
    // Alphabetic or Randomized
    orderOption = orderComboBox->currentIndex();

    interactionOption = interactionComboBox->currentIndex();
    StudyDialog::instance()->setInteractionOption(interactionOption);

    if (interactionOption == 0)
    {
        // interactionOption == 0 -> Use Arrow Keys
        // name1Option = 0 -> Show Image With name
        // name1Option = 1 -> Show Image only
        name1Option = nameComboBox1->currentIndex();
        StudyDialog::instance()->setNameOption1(name1Option);
    }
    else
    {
        // interactionOption == 1 -> Auto Image Display
        // name2Option = 0 -> Show Image With name
        // name2Option = 1 -> Show Image After Image
        name2Option = nameComboBox2->currentIndex();
        StudyDialog::instance()->setNameOption2(name2Option);
    }

    // Stop until key press is available if
    // Interaction is auto and name option is 1
    stopOption = stopCheckBox->checkState();
    StudyDialog::instance()->setStopOption(stopOption);

    speed = speedSlider->value();
    StudyDialog::instance()->setInterval(speed * 100);
}

// Called when the continue button is clicked
void StudyOptionsDialog::continueClicked()
{
    setOptions();
    hide();

#ifdef APP_AMINO
    createHashMap();
#endif

    createFilenameList();
    createIndexList();
    StudyDialog::instance()->initialize();
    StudyDialog::instance()->exec();
}

// Called when the cancel button is clicked to reset all the values
void StudyOptionsDialog::resetOptions()
{
    orderComboBox->setCurrentIndex(orderOption);

    interactionComboBox->setCurrentIndex(interactionOption);
    setInteractionOption(interactionOption);

    nameComboBox1->setCurrentIndex(name1Option);

    nameComboBox2->setCurrentIndex(name2Option);
    setNameOption2(name2Option);

    stopCheckBox->setChecked(stopOption);

    speedSlider->setValue(speed);
    setInterval(speed);
}

// Called when the cancel button is clicked
void StudyOptionsDialog::cancelClicked()
{
    resetOptions();
    hide();
}

StudyOptionsDialog * StudyOptionsDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new StudyOptionsDialog(parent);
    return s_instance;
}

void StudyOptionsDialog::moveToCenter()
{
    move(x, y);
}

StudyOptionsDialog::~StudyOptionsDialog()
{
}
