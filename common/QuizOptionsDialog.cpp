#include "QuizOptionsDialog.h"
#include "QuizDialog.h"
#include <sstream>
#include <sysfolders.h>
#include <sysscale.h>

#include <QDebug>

#ifdef __APPLE__
#define COMBOBOX_H 30
#else
#define COMBOBOX_H 20
#endif

QuizOptionsDialog * QuizOptionsDialog::s_instance = nullptr;

QuizOptionsDialog::QuizOptionsDialog( QWidget *parent )
    : QDialog(parent)
{
    width = ppp(350);
    height = ppp(270);

    setGeometry(QRect(0, 0, width, height));
    setMaximumSize(width, height );
    setMinimumSize(width, height );
    setWindowTitle("Quiz / Test Options");

    modeSelectGroupBox = new QGroupBox(this);
    modeSelectGroupBox->setGeometry(QRect(ppp(10), ppp(10), ppp(330), ppp(80)));
    modeSelectGroupBox->setTitle("Mode Select");

    modeSelectComboBox = new QComboBox(modeSelectGroupBox);
    modeSelectComboBox->setGeometry(QRect(ppp(20), ppp(35), ppp(290), ppp(COMBOBOX_H)));
    QObject::connect(modeSelectComboBox, SIGNAL(activated(int)), this, SLOT(setQuizOption(int)));

    speedGroupBox = new QGroupBox(this);
    speedGroupBox->setTitle("Speed");
    speedGroupBox->setGeometry(QRect(ppp(10), ppp(100), ppp(330), ppp(120)));

    label1 = new QLabel(speedGroupBox);
    label1->setText("Image Display time: (0.1 - 4 seconds)");
    label1->setGeometry(QRect(ppp(50), ppp(25), ppp(330), ppp(20)));

    label2 = new QLabel(speedGroupBox);
    label2->setText("Less");
    label2->setGeometry(QRect(ppp(10), ppp(50), ppp(50), ppp(20)));

    label3 = new QLabel(speedGroupBox);
    label3->setText("1.0 seconds");
    label3->setGeometry(QRect(ppp(100), ppp(90), ppp(280), ppp(20)));

    label4 = new QLabel(speedGroupBox);
    label4->setText("More");
    label4->setGeometry(QRect(ppp(295), ppp(60), ppp(50), ppp(20)));

    speedSlider = new QSlider(speedGroupBox);
    speedSlider->setOrientation(Qt::Horizontal);
    speedSlider->setMinimum(1);
    speedSlider->setMaximum(40);
    speedSlider->setValue(10);
    speedSlider->setGeometry(QRect(ppp(40), ppp(60), ppp(250), ppp(20)));
    QObject::connect(speedSlider, SIGNAL(valueChanged(int)), this, SLOT(setValue(int)));

    continueButton = new QPushButton("Continue", this);
    continueButton->setGeometry(QRect(ppp(10), ppp(230), ppp(135), ppp(25)));
    continueButton->setDefault(true);
    continueButton->setAutoDefault(true);
    QObject::connect(continueButton, SIGNAL(clicked()), this, SLOT(continueClicked()));

    cancelButton = new QPushButton("Cancel", this);
    cancelButton->setGeometry(QRect(ppp(205), ppp(230), ppp(135), ppp(25)));
    QObject::connect(cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));

    option = 0;
    tempOption = 0;
    interval = 10;
#ifdef APP_AMINO
    nameType = 0;
#else
    answerType = 0;
#endif
    propertyType = 0;
    quiz = true;
}

// Method to add the modes below to the combobox.
// Test mode does not have the first option
// "Image Naming with Prompt",
// "Image Naming without Prompt",
// "Image Comparison",
// "Image Verification"
void QuizOptionsDialog::addItemToComboBox(QString item)
{
    modeSelectComboBox->addItem(item);
}

// Method to clear the combobox. The same dialog is
// used for both test and quiz. The modes for test
// and quiz are the same except for the first quiz mode.
// We clear the combobox before switching modes
// between quiz and test.
void QuizOptionsDialog::clearComboBox()
{
    modeSelectComboBox->clear();
    modeSelectComboBox->setCurrentIndex(0);
}

// Method to set the more, Quiz or Test
// true -> Quiz Mode
// false-> Test Mode
void QuizOptionsDialog::setMode(bool flag)
{
    quiz = flag;
}

// Method to randomize the list of indices
void QuizOptionsDialog::randomize()
{
    int size = QuizDialog::instance()->indexList.size();

    // Find two different indices and
    // then swap the elements.
    for (int i = 0; i < size; ++i)
    {
        int index1=rand()%size;
        int index2=rand()%size;

        while (index1 == index2)
            index2 = rand() % size;

        int temp = QuizDialog::instance()->indexList[index1];
        QuizDialog::instance()->indexList[index1] = QuizDialog::instance()->indexList[index2];
        QuizDialog::instance()->indexList[index2] = temp;
    }
}

// Method to create the index list
// Just add integers, 0 thru size-1
// and is then randomized
void QuizOptionsDialog::createIndexList()
{
    QuizDialog::instance()->indexList.clear();
    int size = QuizDialog::instance()->filenameList.size();
    for (int i = 0; i < size; ++i)
        QuizDialog::instance()->indexList << i;
    randomize();
}

#ifdef APP_AMINO
// Method to create a Hashmap between
// Amino acid name and the answer
// Key = <Amino Acid name>
// Value = <Amino Acid name>|<Answer>
void QuizOptionsDialog::createHashMap()
{
    hash.clear();
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Properties.csv").c_str());
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning( this, "Invalid Filename", tr("Properties file cannot be opened for reading"));
        return;
    }

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

    while (!in.atEnd())
    {
        line = in.readLine();
        QStringList attributeList = line.split(',');
        QString key = attributeList.at(0);
        QString value = key + "|";

        if (nameType != 3)
            value += attributeList.at(nameType) + " ";

        if (propertyType != 5)
            value += attributeList.at(3 + propertyType);
        hash.insert(key, value);
    }

    inFile.close();
}
#endif

// Method to create a list of filename of
// the chosen Amino Acid and the list of
// correct answers from the Hashmap
void QuizOptionsDialog::createFilenameList()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Database.csv").c_str());
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning( this, "Invalid Filename", tr("Database file cannot be opened for reading"));
        return;
    }

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

#ifndef APP_AMINO
    QString propertyHeader = line.split(",").at(2 + propertyType);
#endif

    QuizDialog::instance()->filenameList.clear();
    QuizDialog::instance()->answerList.clear();

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
            QuizDialog::instance()->filenameList << columns.at(0);
            QuizDialog::instance()->answerList << hash[columns.at(2)];
        }
    #else
        // columns.at(0) has the file name
        // columns.at(1) has the name of image
        // columns.at(2 + propertyType) has the property name (the 1st property will be in column 2 and so on)
        if (imagesToStudy.contains(columns.at(1)))
        {
            QuizDialog::instance()->filenameList << columns.at(0);
            QString propertyName = columns.at(2 + propertyType);
            // If there is no property for that property type, then assign "No <property name>"
            if (propertyName.length() == 0)
                propertyName = "No " + propertyHeader;
            QString caption = columns.at(1) + "|";
            switch (answerType)
            {
            case 0: // name and property
                caption += columns.at(1) + " " + propertyName;
                break;

            case 1: // only name
                caption += columns.at(1);
                break;

            case 2: // only property
                caption += propertyName;
                break;
            }

            QuizDialog::instance()->answerList << caption;
        }
    #endif
    }

    inFile.close();
}

// Method called when continue is clicked
// to pass on the settings to the Quiz dialog
void QuizOptionsDialog::setOptions()
{
    option = modeSelectComboBox->currentIndex();

    // Test mode 0 is Quiz mode 1
    // Test mode 1 is Quiz mode 2 and so on
    if (quiz)
        QuizDialog::instance()->setQuizOption(option);
    else
        QuizDialog::instance()->setQuizOption(option + 1);

    // Let the quize dialog know what type
    // of property, name and image
#ifdef APP_AMINO
    QuizDialog::instance()->setPropertyType(propertyType);
    QuizDialog::instance()->setNameType(nameType);
    QuizDialog::instance()->setImageType(imageType);
#else
    QuizDialog::instance()->setPropertyType(propertyType, propertyString);
#endif

    interval = speedSlider->value();
    QuizDialog::instance()->setInterval(interval * 100);
}

// Method called when the
// continue button is clicked
void QuizOptionsDialog::continueClicked()
{
#ifdef APP_AMINO
    createHashMap();
#endif
    createFilenameList();
    setOptions();
    hide();
    createIndexList();

    QuizDialog::instance()->initialize(quiz);
    QuizDialog::instance()->exec();
}

// Method called when the cancel button
// is clicked to reset all the values
void QuizOptionsDialog::resetOptions()
{
    modeSelectComboBox->setCurrentIndex(option);
    speedSlider->setValue(interval);
    setValue(interval);
}

// Method called when the
// cancel button is clicked
void QuizOptionsDialog::cancelClicked()
{
    resetOptions();
    hide();
}

// Method to handle the slider text
void QuizOptionsDialog::setValue(int value)
{
	std::stringstream s;
	s << value / 10 << "." << value % 10 << " seconds";
	label3->setText(s.str().c_str());
}

// For image comparison and verification valid answers
// are either y or n. This method ensures that given the
// the list of images selected for Quiz/Test the and
// a questiong with n as the right answer can be created
bool QuizOptionsDialog::isNotAbleToCreateNegativeQuestions()
{
    QStringList temp = QuizDialog::instance()->answerList;
    int n = QuizDialog::instance()->answerList.size();
    for (int i = 0; i < n - 1; ++i)
    {
        QString str1 = QuizDialog::instance()->answerList.at(i).split('|').at(1);
        QString str2 = QuizDialog::instance()->answerList.at(i + 1).split('|').at(1);
        if (str1 != str2)
            return false;
    }
    return true;
}

// Method called when the combobox
// selection is changed.
void QuizOptionsDialog::setQuizOption( int index )
{
    // If Quiz, leave as is, because in
    // Test mode 0 is Quiz mode 1
    // Test mode 1 is Quiz mode 2 and so on
    if (!quiz)
        index++;

    // For modes > 1 i.e., Image Comparison and
    // Image verification at least 2 Amino Acids
    // must be selected.
    if (index > 1)
    {
        // Check if at least 2 images were selected
        if (imagesToStudy.count() < 2)
        {
        #ifdef APP_AMINO
            QString infoMessage = tr("There must be more than one amino acid selected for the %1 quiz mode.").arg(modeSelectComboBox->currentText());
        #else
            QString infoMessage = tr("There must be more than one image selected for the %1 quiz mode.").arg(modeSelectComboBox->currentText());
        #endif

            QMessageBox::warning(this, "Invalid Selection", infoMessage, QMessageBox::Ok, QMessageBox::Ok);
            modeSelectComboBox->setCurrentIndex(tempOption);
            return;
        }

    #ifdef APP_AMINO
        createHashMap();
        createFilenameList();

        // In the Image Comparision and Verification modes
        // we need to create questions with both y and n as
        // right answers. If the properties of amino acids
        // are the same we will not be able create questions
        // that have n as the right answer.
        if (isNotAbleToCreateNegativeQuestions())
        {
            QString infoMessage = tr("You have selected amino acids with the same properties, "
                                     "and have selected the \"no name\" option. These selections "
                                     "will result in problems in the %2 quiz mode. Please either "
                                     "select different amino acids, or choose a type of name to "
                                     "display with the amino acids you have selected."
                                     ).arg( modeSelectComboBox->currentText()
            );

            QMessageBox::warning(this, "Invalid Selection", infoMessage, QMessageBox::Ok, QMessageBox::Ok);

            modeSelectComboBox->setCurrentIndex(tempOption);
            return;
        }
    #endif
    }
    else // not Image Comparison and verification
    {
        // Decrement the index since we
        // incremented earlier.
        if (!quiz)
            index--;

        // We save the index to reset if a
        // wrong selection is made.
        tempOption = index;
    }
}

#ifdef APP_AMINO
// Methed to set the image type for the Quiz session.
// The backgroud color depends on the image type.
// Background is white for structured formula, black otherwise.
void QuizOptionsDialog::setImageType(QString iType, int color)
{
    iType.replace("&&", "&");

    qDebug() << "set image type '" << iType << "'";
    imageType = iType;
    QuizDialog::instance()->setBackgroundColor(color);
}

// Method to set the name type
void QuizOptionsDialog::setNameType(int nType)
{
    nameType = nType;
}
#endif

#ifdef APP_AMINO
// Method to set the property type
void QuizOptionsDialog::setPropertyType(int pType)
{
    propertyType = pType;
}
#else
// Method to set the property type
void QuizOptionsDialog::setPropertyType(int pType, QString pString)
{
    propertyType = pType;
    propertyString = pString;
}

// Method to set answer type
// Name and Property
// Only Name
// Only Property
void QuizOptionsDialog::setAnswerType(int aType)
{
    answerType = aType;
}
#endif

QuizOptionsDialog * QuizOptionsDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new QuizOptionsDialog(parent);
    return s_instance;
}

// Method to move the dialog to the
// center of the main screen.
void QuizOptionsDialog::moveToCenter()
{
    move(x, y);
}

// Method to save the coordinates
void QuizOptionsDialog::setCoordinates(int center_x, int center_y)
{
    x = center_x - width / 2;
    y = center_y - height / 2;
}

QuizOptionsDialog::~QuizOptionsDialog()
{
}
