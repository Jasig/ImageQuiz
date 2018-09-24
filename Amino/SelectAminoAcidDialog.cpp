#include "SelectAminoAcidDialog.h"
#include "AminoAcidList.h"
#include <StudyOptionsDialog.h>
#include <QuizOptionsDialog.h>
#include "quizapp.h"
#include <sysfolders.h>
#include <sysscale.h>

#include <QDebug>

//Text for property type labels
const char *propertyTypeLabel[MAX_PROPERTY_TYPES] = {
    "Acidic / Basic / Amide / Neutral",
    "Charged / Uncharged",
    "Polar / Nonpolar",
    "Hydrophobic / Hydrophilic",
    "Aromatic / Hydroxyl / Thiol / Aliphatic",
    "No Properties"
};

//Text for image type labels
const char *imageTypeLabel[MAX_IMAGE_TYPES] = {
    "Space Filling",
    "Ball && Stick",
    "Stick",
    "Wireframe",
    "Structural Formula"
};

//Text for name type labels
const char *nameTypeLabel[MAX_NAME_TYPES] = {
    "Full Name",
    "Single Letter Name",
    "Three Letter Name",
    "No Name"
};

SelectAminoAcidDialog * SelectAminoAcidDialog::s_instance = nullptr;

SelectAminoAcidDialog * SelectAminoAcidDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new SelectAminoAcidDialog(parent);
    return s_instance;
}

SelectAminoAcidDialog::SelectAminoAcidDialog(QWidget * parent)
    : QDialog(parent)
{
    int width = ppp(770);
    int height = ppp(385);

    setWindowTitle("Select Amino Acids");
    setGeometry(QRect(ppp(100), ppp(100), width, height));
    setMaximumSize(width, height);
    setMinimumSize(width, height);

    aAGroupBox = new QGroupBox(this);
    aAGroupBox->setGeometry(QRect(ppp(10), ppp(10), ppp(410), ppp(330)));
    aAGroupBox->setTitle("Select Amino Acids");

    choseAAListWidget = new QListWidget(aAGroupBox);
    choseAAListWidget->setGeometry(QRect(ppp(10), ppp(30), ppp(130), ppp(285)));
    choseAAListWidget->setSelectionMode(QAbstractItemView::ExtendedSelection);
    choseAAListWidget->setSortingEnabled(true);
    addListWidgetItems();

    selectedAAListWidget = new QListWidget(aAGroupBox);
    selectedAAListWidget->setGeometry(QRect(ppp(270), ppp(30), ppp(130), ppp(285)));
    selectedAAListWidget->setSelectionMode(QAbstractItemView::ExtendedSelection);
    selectedAAListWidget->setSortingEnabled(true);

    addAllButton = new QPushButton("Add All->", aAGroupBox );
    addAllButton->setGeometry(QRect(ppp(150), ppp(85), ppp(110), ppp(25)));
    QObject::connect(addAllButton, SIGNAL(clicked()), this, SLOT(addAll()));

    addButton = new QPushButton("Add->", aAGroupBox);
    addButton->setGeometry(QRect(ppp(150), ppp(120), ppp(110), ppp(25)));
    QObject::connect(addButton, SIGNAL(clicked()), this, SLOT(add()));

    removeButton = new QPushButton("<-Remove", aAGroupBox);
    removeButton->setGeometry(QRect(ppp(150), ppp(155), ppp(110), ppp(25)));
    QObject::connect(removeButton, SIGNAL(clicked()), this, SLOT(remove()));

    removeAllButton = new QPushButton("<-Remove All", aAGroupBox);
    removeAllButton->setGeometry(QRect(ppp(150), ppp(190), ppp(110), ppp(25)));
    QObject::connect(removeAllButton, SIGNAL(clicked()), this, SLOT(removeAll()));

    propertiesGroupBox = new QGroupBox( this );
    propertiesGroupBox->setGeometry(QRect(ppp(430), ppp(10), ppp(330), ppp(165)));
    propertiesGroupBox->setTitle("Select Properties");

    for (int x = ppp(10), y = ppp(35), width = ppp(300), height = ppp(15), n = 0; n < MAX_PROPERTY_TYPES; y += ppp(20), ++n)
    {
        propertyTypeRadioButton[n] = new QRadioButton(propertyTypeLabel[n], propertiesGroupBox);
        propertyTypeRadioButton[n]->setGeometry(x, y, width, height);
        QObject::connect(propertyTypeRadioButton[n], SIGNAL(clicked()), this, SLOT(noProperty()));
    }

    propertyTypeRadioButton[0]->setChecked(true);

    imageTypeGroupBox = new QGroupBox( this );
    imageTypeGroupBox->setGeometry(QRect(ppp(430), ppp(185), ppp(160), ppp(156)));
    imageTypeGroupBox->setTitle("Image Type");

    for (int x = ppp(10), y = ppp(35), width = ppp(145), height = ppp(15), n = 0; n < MAX_IMAGE_TYPES; y += ppp(20), ++n)
    {
        imageTypeRadioButton[n] = new QRadioButton(imageTypeLabel[n], imageTypeGroupBox);
        imageTypeRadioButton[n]->setGeometry(x, y, width, height);
    }

    imageTypeRadioButton[0]->setChecked(true);

    nameTypeGroupBox = new QGroupBox(this);
    nameTypeGroupBox->setGeometry(QRect(ppp(600), ppp(185), ppp(160), ppp(156)));
    nameTypeGroupBox->setTitle("Name Type");

    for (int x = ppp(10), y = ppp(35), width = ppp(145), height = ppp(15), n = 0; n < MAX_NAME_TYPES; y += ppp(20), ++n)
    {
        nameTypeRadioButton[n] = new QRadioButton(nameTypeLabel[n], nameTypeGroupBox);
        nameTypeRadioButton[n]->setGeometry(x, y, width, height);
        QObject::connect(nameTypeRadioButton[n], SIGNAL(clicked()), this, SLOT(noName()));
    }

    nameTypeRadioButton[0]->setChecked(true);

    cancelButton = new QPushButton("Cancel", this);
    cancelButton->setGeometry(QRect(ppp(320), ppp(350), ppp(100), ppp(25)));
    QObject::connect(cancelButton, SIGNAL(clicked()), this, SLOT(close()));


    finishButton = new QPushButton("Finish", this);
    finishButton->setGeometry(QRect(ppp(430), ppp(350), ppp(330), ppp(25)));
    finishButton->setDefault(true);
    finishButton->setAutoDefault(true);
    QObject::connect(finishButton, SIGNAL(clicked()), this, SLOT(finish()));

    propertyTypeChecked[0] = true;
    for (int i = 1; i < MAX_PROPERTY_TYPES; ++i)
        propertyTypeChecked[i] = false;

    imageTypeChecked[0] = true;
    for (int i = 1; i < MAX_IMAGE_TYPES; ++i)
        imageTypeChecked[i] = false;

    nameTypeChecked[0] = true;
    for (int i = 0; i < MAX_NAME_TYPES; ++i)
        nameTypeChecked[i] = false;

    tryingToStudy = false;
    tryingToTakeQuiz = false;
}

// Method that adds the amino acids to
// the list of Amino Acids to chose from
void SelectAminoAcidDialog::addListWidgetItems()
{
    QFile inFile;
    inFile.setFileName((ResourceFolder() + "Properties.csv").c_str());
    qDebug() << "opening Properties file " << inFile.fileName();

    if (!inFile.open( QIODevice::ReadOnly | QIODevice::Text))
        QMessageBox::warning(this, "Invalid Filename", tr("Cannot be opened for reading"));

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();

    while(!in.atEnd())
    {
        line = in.readLine();
        QStringList attributeList = line.split(",");
        new QListWidgetItem(tr("%1").arg(attributeList.at(0)), choseAAListWidget);
        chooseFromList << attributeList.at(0);
    }

    inFile.close();
}

// Method called when the Add all button is clicked
// Moves all the Amino Acids to the right listbox
void SelectAminoAcidDialog::addAll()
{
    int size = choseAAListWidget->count();
    for (int i = 0; i < size; ++i)
    {
        choseAAListWidget->setCurrentRow(0);
        QListWidgetItem * currentItem = choseAAListWidget->currentItem();
        selectedAAListWidget->addItem(currentItem->text());
        choseAAListWidget->takeItem(choseAAListWidget->row(currentItem));
    }
}

// Method called when the add button is clicked.
// Moves the selected Amino Acid to the right listbox
void SelectAminoAcidDialog::add()
{
    QList<QListWidgetItem *> selectedItems = choseAAListWidget->selectedItems();
    for (int i = 0; i < selectedItems.size(); ++i)
    {
        QListWidgetItem * currentItem = selectedItems.at(i);
        selectedAAListWidget->addItem(currentItem->text());
        choseAAListWidget->takeItem(choseAAListWidget->row(currentItem));
    }
}

// Method called when the remove button is clicked.
// Moves the selected Amino Acid to the left listbox
void SelectAminoAcidDialog::remove()
{
    QList<QListWidgetItem *> selectedItems = selectedAAListWidget->selectedItems();
    for (int i = 0; i < selectedItems.size(); ++i)
    {
        QListWidgetItem * currentItem = selectedItems.at(i);
        choseAAListWidget->addItem(currentItem->text());
        selectedAAListWidget->takeItem(selectedAAListWidget->row(currentItem));
    }
}

// Method called when the remove all button is clicked.
// Moves the selected Amino Acid to the left listbox
void SelectAminoAcidDialog::removeAll()
{
    int size = selectedAAListWidget->count();
    for (int i = 0; i < size; ++i)
    {
        selectedAAListWidget->setCurrentRow(0);
        QListWidgetItem * currentItem = selectedAAListWidget->currentItem();
        choseAAListWidget->addItem(currentItem->text());
        selectedAAListWidget->takeItem(selectedAAListWidget->row(currentItem));
    }
}

// Method called when the No property option is checked.
// This mean that the no name option must be disabled
void SelectAminoAcidDialog::noProperty()
{
    if (propertyTypeRadioButton[ MAX_PROPERTY_TYPES - 1]->isChecked())
        nameTypeRadioButton[MAX_NAME_TYPES - 1]->setEnabled(false);
    else
        nameTypeRadioButton[MAX_NAME_TYPES - 1]->setEnabled(true);
}

// Method called when the No name option is checked.
// This mean that the no property option must be disabled
void SelectAminoAcidDialog::noName()
{
    if (nameTypeRadioButton[MAX_NAME_TYPES - 1]->isChecked())
        propertyTypeRadioButton[MAX_PROPERTY_TYPES - 1]->setEnabled(false);
    else
        propertyTypeRadioButton[MAX_PROPERTY_TYPES - 1]->setEnabled(true);
}

// Method called from the finish function
// If the user attempted to take a quiz/test
// or study with selecting any amino acids
// Then he would be intimated about it and
// directed to the select amino acids. So
// when he's done selected amino acids then
// we'd have to go back showing the user
// the quiz opition dialog or study option dialog
void SelectAminoAcidDialog::conclude()
{
    // Check if they got here because they tried
    // to start a quiz/test or study session
    if (tryingToStudy || tryingToTakeQuiz)
    {
        // The whole point of getting here was
        // to chose amino acids. Check to see
        // if that has been taken care off.
        if (isChosenAminoAcids())
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
        else
        {
            // if Amino acids were not selected
            // ask then if the use would like to or not
            int yesno = QMessageBox::question( this, "No Amino Acids Selected",
                "There are no Amino Acids selected for studying.\n "
                "Would you like to select Amino Acids now? ",
                QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes
            );

            // if not, then reset the flags
            // and hide the dialog
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

// Method called when the finish button is clicked
// This method saves the state of the selections
// and send the necessary information to other dialogs.
void SelectAminoAcidDialog::finish()
{
    chooseFromList.clear();
    chosenList.clear();

    // save property selection
    for (int i = 0; i < MAX_PROPERTY_TYPES; ++i)
        propertyTypeChecked[i] = propertyTypeRadioButton[i]->isChecked();

    // save property type selection
    for (int i = 0; i < MAX_IMAGE_TYPES; ++i)
        imageTypeChecked[i] = imageTypeRadioButton[i]->isChecked();

    // save image type selection
    for (int i = 0; i < MAX_NAME_TYPES; ++i)
      nameTypeChecked[i] = nameTypeRadioButton[i]->isChecked();

    // save the amino acids that were NOT selected
    for (int i = 0; i < choseAAListWidget->count(); ++i)
    {
        choseAAListWidget->setCurrentRow(i);
        QListWidgetItem * currentItem = choseAAListWidget->currentItem();
        chooseFromList << currentItem->text();
    }

    // clear the list of amino acids to study
    // for earlier quiz/test or study sessions
    StudyOptionsDialog::instance()->imagesToStudy.clear();
    QuizOptionsDialog::instance()->imagesToStudy.clear();

    // save the amino acids that were selected for
    // for quiz/test or study. Also give this information
    // to the Quiz and Study dialogs.
    for (int i = 0; i < selectedAAListWidget->count(); ++i)
    {
        selectedAAListWidget->setCurrentRow(i);
        QListWidgetItem * currentItem = selectedAAListWidget->currentItem();
        chosenList << currentItem->text();
        StudyOptionsDialog::instance()->imagesToStudy << currentItem->text();
        QuizOptionsDialog::instance()->imagesToStudy << currentItem->text();
    }

    // Tell the Quiz and Study dialogs
    // the type of Image, Name and property
    setImageType();
    setNameType();
    setPropertyType();
    conclude();
}

// Method called when the close button is clicked
// Resets all the values to it's original state
void SelectAminoAcidDialog::closeEvent(QCloseEvent* e)
{
    for (int i = 0; i < MAX_PROPERTY_TYPES; ++i)
        propertyTypeRadioButton[i]->setChecked(propertyTypeChecked[i]);

    for (int i = 0; i < MAX_IMAGE_TYPES; ++i)
        imageTypeRadioButton[i]->setChecked(imageTypeChecked[i]);

    for (int i = 0; i < MAX_NAME_TYPES; ++i)
        nameTypeRadioButton[i]->setChecked(nameTypeChecked[i]);

    choseAAListWidget->clear();
    selectedAAListWidget->clear();

    for (int i = 0; i < chooseFromList.size(); ++i)
        choseAAListWidget->addItem(chooseFromList.at(i));

    for (int i = 0; i < chosenList.size(); ++i)
        selectedAAListWidget->addItem(chosenList.at(i));

    conclude();
}

// Method called when the Esc button is
// pressed. Do nothing, simply ignore
void SelectAminoAcidDialog::reject()
{
}

// Method is see if atleast one
// Amino acid was chosen
bool SelectAminoAcidDialog::isChosenAminoAcids()
{
    if (0 == selectedAAListWidget->count())
        return false;
    return true;
}

// Method to set the tryingToStudy flag
// This will be used later to see if the
// was attempting to study before getting here
void SelectAminoAcidDialog::setTryingToStudy(bool flag)
{
    tryingToStudy = flag;
}


// Method to set the tryingToTakeQuiz flag
// This will be used later to see if the
// was attempting to Quiz before getting here
void SelectAminoAcidDialog::setTryingToTakeQuiz(bool flag)
{
    tryingToTakeQuiz = flag;
}

// Method to set the Image type in the
// Quiz/Test or Study sessions. The bg
// color is also set->White for structured
// formula. Black otherwise
void SelectAminoAcidDialog::setImageType()
{
    for (int i = 0; i < MAX_IMAGE_TYPES; ++i)
    {
        if (imageTypeChecked[i])
        {
            if (i != 4)
            {
                StudyOptionsDialog::instance()->setImageType(imageTypeLabel[i], Qt::black);
                QuizOptionsDialog::instance()->setImageType(imageTypeLabel[i], Qt::black);
            }
            else
            {
                StudyOptionsDialog::instance()->setImageType(imageTypeLabel[i], Qt::white);
                QuizOptionsDialog::instance()->setImageType(imageTypeLabel[i], Qt::white);
            }

            break;
        }
    }
    return;
}

// Method to set the Name type in the
// Quiz/Test or Study sessions
void SelectAminoAcidDialog::setNameType()
{
    for (int i = 0; i < MAX_NAME_TYPES; ++i)
    {
        if (nameTypeChecked[i])
        {
            StudyOptionsDialog::instance()->setNameType(i);
            QuizOptionsDialog::instance()->setNameType(i);
            break;
        }
    }

    return;
}

// Method to set the Property type in the
// Quiz/Test or Study sessions
void SelectAminoAcidDialog::setPropertyType()
{
    for (int i = 0; i < MAX_PROPERTY_TYPES; ++i)
    {
        if (propertyTypeChecked[i])
        {
            StudyOptionsDialog::instance()->setPropertyType(i);
            QuizOptionsDialog::instance()->setPropertyType(i);
            break;
        }
    }
    return;
}

SelectAminoAcidDialog::~SelectAminoAcidDialog()
{
}
