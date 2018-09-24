#include "SelectImagesDialog.h"

#include <StudyOptionsDialog.h>
#include <QuizOptionsDialog.h>
#include "quizapp.h"
#include <sysfolders.h>
#include <sysscale.h>

// Test for answer type labels
const char * answerTypeLabel[MAX_ANSWER_TYPES] = {
   "Name and Property",
   "Only Name",
   "Only Property"
};

SelectImagesDialog * SelectImagesDialog::s_instance = nullptr;

SelectImagesDialog * SelectImagesDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new SelectImagesDialog(parent);
    return s_instance;
}

SelectImagesDialog::SelectImagesDialog(QWidget * parent)
    : QDialog(parent)
{
    int width = ppp(470);
    int height = ppp(485);

    setWindowTitle( "Select Images" );
    setGeometry(QRect(ppp(100), ppp(100), width, height));
    setMaximumSize(width, height);
    setMinimumSize(width, height);

    imagesGroupBox = new QGroupBox(this);
    imagesGroupBox->setGeometry(QRect(ppp(10), ppp(10), width - ppp(20), ppp(280)));
    imagesGroupBox->setTitle("Select Images");

    choseImagesListWidget = new QListWidget(imagesGroupBox);
    choseImagesListWidget->setGeometry(QRect(ppp(10), ppp(30), ppp(150), ppp(235)));
    choseImagesListWidget->setSelectionMode(QAbstractItemView::ExtendedSelection );
    choseImagesListWidget->setSortingEnabled(true);
    addListWidgetItems();

    selectedImagesListWidget = new QListWidget(imagesGroupBox );
    selectedImagesListWidget->setGeometry(QRect(ppp(290), ppp(30), ppp(150), ppp(235)));
    selectedImagesListWidget->setSelectionMode(QAbstractItemView::ExtendedSelection );
    selectedImagesListWidget->setSortingEnabled(true);

    addAllButton = new QPushButton("Add All->", imagesGroupBox);
    addAllButton->setGeometry(QRect(ppp(170), ppp(85), ppp(110), ppp(25)));
    QObject::connect(addAllButton, SIGNAL(clicked()), this, SLOT(addAll()));

    addButton = new QPushButton("Add->", imagesGroupBox);
    addButton->setGeometry(QRect(ppp(170), ppp(120), ppp(110), ppp(25)));
    QObject::connect(addButton, SIGNAL(clicked()), this, SLOT(add()));

    removeButton = new QPushButton("<-Remove", imagesGroupBox);
    removeButton->setGeometry(QRect(ppp(170), ppp(155), ppp(110), ppp(25)));
    QObject::connect(removeButton, SIGNAL(clicked()), this, SLOT(remove()));

    removeAllButton = new QPushButton("<-Remove All", imagesGroupBox);
    removeAllButton->setGeometry(QRect(ppp(170), ppp(190), ppp(110), ppp(25)));
    QObject::connect(removeAllButton, SIGNAL(clicked()), this, SLOT(removeAll()));

    propertiesGroupBox = new QGroupBox(this);
    propertiesGroupBox->setGeometry(QRect(ppp(10), ppp(295), width - ppp(20), ppp(70)));
    propertiesGroupBox->setTitle("Select Property");

    propertiesComboBox = new QComboBox(propertiesGroupBox);
    propertiesComboBox->setGeometry(QRect(ppp(10), ppp(30), width - ppp(40), ppp(25)));
    addComboBoxItems();
    QObject::connect(propertiesComboBox, SIGNAL(activated(int)), this, SLOT(setPropertyType(int)));

    answerTypeGroupBox = new QGroupBox(this);
    answerTypeGroupBox->setGeometry(QRect(ppp(10), ppp(370), width - ppp(20), ppp(65)));
    answerTypeGroupBox->setTitle("Answer mode");

    answerTypeRadioButton[0] = new QRadioButton(answerTypeLabel[0], answerTypeGroupBox);
    answerTypeRadioButton[0]->setGeometry(ppp(10), ppp(35), ppp(140), ppp(20));
    QObject::connect(answerTypeRadioButton[0], SIGNAL(clicked()), this, SLOT(setAnswerMode()));

    answerTypeRadioButton[0]->setChecked(true);
    answerTypeChecked[0] = true;

    answerTypeRadioButton[1] = new QRadioButton(answerTypeLabel[1], answerTypeGroupBox);
    answerTypeRadioButton[1]->setGeometry(ppp(185), ppp(35), ppp(125), ppp(20));
    QObject::connect(answerTypeRadioButton[1], SIGNAL(clicked()), this, SLOT(setAnswerMode()));

    answerTypeChecked[1] = false;

    answerTypeRadioButton[2] = new QRadioButton(answerTypeLabel[2], answerTypeGroupBox);
    answerTypeRadioButton[2]->setGeometry(ppp(300), ppp(35), ppp(125), ppp(20));
    QObject::connect(answerTypeRadioButton[2], SIGNAL(clicked()), this, SLOT(setAnswerMode()));
    answerTypeChecked[2] = false;  

    finishButton = new QPushButton("Finish", this);
    finishButton->setGeometry(QRect(ppp(120), ppp(445), width - ppp(130), ppp(25)));
    finishButton->setDefault(true);
    finishButton->setAutoDefault(true);
    QObject::connect(finishButton, SIGNAL(clicked()), this, SLOT(finish()));

    propertyIndex = 0;
    QuizOptionsDialog::instance()->propertyString = propertiesComboBox->itemText(0);

    tryingToStudy = false;
    tryingToTakeQuiz = false;
}

// Add properties to the properties combobox.
void SelectImagesDialog::addComboBoxItems()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Database.csv").c_str());
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning(this, "Invalid Filename", tr( "<b>Database file cannot be opened for reading</b>"));
        return;
    }
    
    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();
    QStringList attributeList = line.split( "," );
    
    for(int i = 2; i < attributeList.size(); ++i)
        propertiesComboBox->addItem(attributeList[i]);
    
    inFile.close();
}

// Add the Images to the list of images to chose from
void SelectImagesDialog::addListWidgetItems()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Database.csv").c_str());
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning(this, "Invalid Filename", tr("<b>Database file cannot be opened for reading</b>"));
        return;
    }
    
    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

    while(!in.atEnd())
    {
        line = in.readLine();
        QStringList attributeList = line.split(",");
        
        // check to see if that image has
        // already been added to the list
        if (!chooseFromList.contains(attributeList.at(1)))
        {
            new QListWidgetItem(tr("%1").arg(attributeList.at(1)), choseImagesListWidget);
            chooseFromList << attributeList.at(1);
        }
    }
    
    inFile.close();
}

// Called when the Add all button is clicked
// Moves all the Amino Acids to the right listbox
void SelectImagesDialog::addAll()
{
    int size = choseImagesListWidget->count();
    for (int i = 0; i < size; ++i)
    {
        choseImagesListWidget->setCurrentRow(0);
        QListWidgetItem * currentItem = choseImagesListWidget->currentItem();
        selectedImagesListWidget->addItem(currentItem->text());
        choseImagesListWidget->takeItem(choseImagesListWidget->row(currentItem));
    }
}

// Called when the add button is clicked.
// Moves the selected Amino Acid to the right listbox
void SelectImagesDialog::add()
{
    QList<QListWidgetItem *> selectedItems = choseImagesListWidget->selectedItems();
    for (int i = 0; i < selectedItems.size(); ++i)
    {
        QListWidgetItem* currentItem = selectedItems.at(i);
        selectedImagesListWidget->addItem(currentItem->text());
        choseImagesListWidget->takeItem(choseImagesListWidget->row(currentItem));
    }
}

// Called when the remove button is clicked.
// Moves the selected Amino Acid to the left listbox
void SelectImagesDialog::remove()
{
    QList<QListWidgetItem *> selectedItems = selectedImagesListWidget->selectedItems();
    for (int i = 0; i < selectedItems.size(); ++i)
    {
        QListWidgetItem * currentItem = selectedItems.at(i);
        choseImagesListWidget->addItem(currentItem->text());
        selectedImagesListWidget->takeItem(selectedImagesListWidget->row(currentItem));
    }
}


// Called when the remove all button is clicked.
// Moves the selected Amino Acid to the left listbox
void SelectImagesDialog::removeAll()
{
    int size = selectedImagesListWidget->count();
    for (int i = 0; i < size; ++i)
    {
        selectedImagesListWidget->setCurrentRow(0);
        QListWidgetItem* currentItem = selectedImagesListWidget->currentItem();
        choseImagesListWidget->addItem(currentItem->text());
        selectedImagesListWidget->takeItem(selectedImagesListWidget->row(currentItem));
    }
}

// Set the property type
void SelectImagesDialog::setPropertyType(int index)
{
    StudyOptionsDialog::instance()->setPropertyType(index);
    QuizOptionsDialog::instance()->setPropertyType(index, propertiesComboBox->itemText(index));
}

// Set the Answer mode
// Name and property
// No Name
// No property
void SelectImagesDialog::setAnswerMode()
{
    for (int i = 0; i < MAX_ANSWER_TYPES; ++i)
    {
        if (answerTypeRadioButton[i]->isChecked())
        {
            StudyOptionsDialog::instance()->setAnswerType(i);
            QuizOptionsDialog::instance()->setAnswerType(i);
            break;
        }
    }
}

// Called from the finish function
// If the user attempted to take a quiz/test
// or study with selecting any image
// Then he would be intimated about it and
// directed to the select images. So
// when he's done selecting images then
// we'd have to go back showing the user
// the quiz opition dialog or study option dialog
void SelectImagesDialog::conclude()
{
    // Check if they got here because they tried
    // to start a quiz/test or study session
    if (tryingToStudy || tryingToTakeQuiz)
    {
        // The whole point of getting here was
        // to chose images. Check to see
        // if that has been taken care off. 
        if (isChosenImages())
        {
            hide();

            // Depending on what the user tried to do
            // before they got here, show them the study
            // or quiz/test dialog.
            if (tryingToStudy)
            {
                StudyOptionsDialog::instance()->moveToCenter();
                StudyOptionsDialog::instance()->exec();
            }
            else
            {
                QuizOptionsDialog::instance()->moveToCenter();
                QuizOptionsDialog::instance()->exec();
            }
        }
        // If images were not selected
        // ask then if the use would like to or not.
        else
        {
            int yesno = QMessageBox::question( this, "No images Selected", "<b>There are no images selected for studying.\n\
                Would you like to select images now?</b>", QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes);
                
            // If not, then reset the flags and hide the dialog.
            if (yesno == QMessageBox::No)
            {
                tryingToStudy = false;
                tryingToTakeQuiz = false;
                hide();
            }
        }
    }
    else
      hide();
}

// Called when the finish button is clicked
// This method saves the state of the selections
// and send the necessary information to other dialogs.
void SelectImagesDialog::finish()
{
    chooseFromList.clear();
    chosenList.clear();

    propertyIndex = propertiesComboBox->currentIndex();

    // save answer mode
    for (int i = 0; i < MAX_ANSWER_TYPES; ++i)
        answerTypeChecked[i] = answerTypeRadioButton[i]->isChecked();

    // save the Images that were NOT selected
    for (int i = 0; i < choseImagesListWidget->count(); ++i)
    {
        choseImagesListWidget->setCurrentRow(i);
        QListWidgetItem* currentItem = choseImagesListWidget->currentItem();
        chooseFromList << currentItem->text();
    }

    // clear the list of images to study
    // for earlier quiz/test or study sessions
    StudyOptionsDialog::instance()->imagesToStudy.clear();
    QuizOptionsDialog::instance()->imagesToStudy.clear();

    // save the images that were selected for
    // for quiz/test or study. Also give this information
    // to the Quiz and Study dialogs.
    for (int i = 0; i < selectedImagesListWidget->count(); ++i)
    {
        selectedImagesListWidget->setCurrentRow(i);
        QListWidgetItem* currentItem = selectedImagesListWidget->currentItem();
        chosenList << currentItem->text();
        StudyOptionsDialog::instance()->imagesToStudy << currentItem->text();
        QuizOptionsDialog::instance()->imagesToStudy << currentItem->text();
    }

    conclude();
}

// Called when the close button is clicked
// Resets all the values to it's original state
void SelectImagesDialog::closeEvent(QCloseEvent *)
{
    choseImagesListWidget->clear();
    selectedImagesListWidget->clear();

    for (int i = 0; i < chooseFromList.size(); ++i)
        choseImagesListWidget->addItem(chooseFromList.at(i));

    for (int i = 0; i < chosenList.size(); ++i)
        selectedImagesListWidget->addItem(chosenList.at(i));

    propertiesComboBox->setCurrentIndex(propertyIndex);

    for (int i = 0; i < MAX_ANSWER_TYPES; ++i)
    {
        if (answerTypeChecked[i])
        {
            answerTypeRadioButton[i]->setChecked(true);
            break;
        }
    }

    conclude();
}

// Called when the Esc button is pressed. Do nothing, simply ignore.
void SelectImagesDialog::reject()
{
}

// Method is see if atleast one image was chosen
bool SelectImagesDialog::isChosenImages()
{
    if (0 == selectedImagesListWidget->count())
        return false;
    return true;
}

// Method to set the tryingToStudy flag
// This will be used later to see if the
// was attempting to study before getting here
void SelectImagesDialog::setTryingToStudy(bool flag)
{
    tryingToStudy = flag;
}

//Method to set the tryingToTakeQuiz flag
//This will be used later to see if the
//was attempting to Quiz before getting here
void SelectImagesDialog::setTryingToTakeQuiz(bool flag)
{
    tryingToTakeQuiz = flag;
}

SelectImagesDialog::~SelectImagesDialog()
{
}
