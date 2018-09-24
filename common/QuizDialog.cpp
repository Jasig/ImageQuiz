// Quiz and Test are exactly the same.
// Test has no answer confirmation.

#include "QuizDialog.h"
#include <ProgressReport.h>
#include <sysfolders.h>
#include <sysscale.h>

#include <QDebug>

#define MAX 8
#define MAX_SEARCH_LENGTH 3
#define MAX_MODES 4
#define MAX_PROPERTY_TYPES 6
#define MAX_NAME_TYPES 4

// Messages for correct answer
const char *message[ MAX ] = {
    "Correct!",
    "Affirmative!",
    "Right!",
    "That's it!",
    "Absolutely!",
    "Way to go!",
    "Good!",
    "Precisely!"
};

// For the purpose of progress report
const char *modeTypes[ MAX_MODES ] = {
    "Image Naming with Prompt",
    "Image Naming without Prompt",
    "Image Comparison",
    "Image Verification"
};

#ifdef APP_AMINO
// For the purpose of progress report
const char *propertyTypes[ MAX_PROPERTY_TYPES ] = {
    "Acidic / Basic / Amide / Neutral",
    "Charged / Uncharged",
    "Polar / Non-polar",
    "Hydrophobic / Hydrophilic",
    "Aromatic / Hydroxyl / Thiol / Aliphatic",
    "No Properties"
};

// For the purpose of progress report
const char *nameTypes[ MAX_NAME_TYPES ] = {
    "Full Name",
    "Single Letter Name",
    "Three Letter Name",
    "No Name"
};
#endif

QuizDialog * QuizDialog::s_instance = nullptr;

QuizDialog::QuizDialog(QWidget * parent)
    : QDialog(parent)
{
    initialPalette = palette();
    setWindowFlags(Qt::CustomizeWindowHint | Qt::Dialog | Qt::WindowTitleHint);
    setWindowTitle("Quiz Session");
    font.setPixelSize(ppp(25));

    imageLabel1 = new QLabel(this); // Label to hold image or left comparison image
    imageLabel1->setBackgroundRole(QPalette::Base);
    imageLabel1->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    imageLabel1->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    imageLabel1->setFont(font);

    imageLabel2 = new QLabel(this); // Label to hold right comparison image
    imageLabel2->setBackgroundRole(QPalette::Base);
    imageLabel2->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    imageLabel2->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    imageLabel2->setFont(font);
    imageLabel2->setVisible(false);

    // Label to hold answer when Quiz/Test mode is
    // Image Naming with Option
    answerLabel = new QLabel(this);
    answerLabel->setBackgroundRole(QPalette::Base);
    answerLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    answerLabel->setFont(font);
    answerLabel->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    answerLabel->setVisible(true);

    incorrectAnswerLabel = new QLabel(this);
    incorrectAnswerLabel->setBackgroundRole(QPalette::Base);
    incorrectAnswerLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    incorrectAnswerLabel->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    incorrectAnswerLabel->setVisible(false);

    font.setPixelSize(ppp(15));

    // Label to hold "Answer :" or "Same (y/n)" to the left of the textbox
    enterAnswerHereLabel = new QLabel(this);
    enterAnswerHereLabel->setBackgroundRole(QPalette::Base);
    enterAnswerHereLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    enterAnswerHereLabel->setAlignment(Qt::AlignRight | Qt::AlignVCenter);
    enterAnswerHereLabel->setFont(font);
    enterAnswerHereLabel->setVisible(false);

    // Textbox to enter the answer
    answerLineEdit = new QLineEdit(this);
    answerLineEdit->setFont(font);
    answerLineEdit->setVisible(false);

    QObject::connect(answerLineEdit, SIGNAL(returnPressed()), this, SLOT(answerEntered()));

    okButton = new QPushButton("Ok", this);
    okButton->setFont(font);
    okButton->setVisible(false);

    QObject::connect(okButton, SIGNAL(clicked()), this, SLOT(answerEntered()));

    cancelButton = new QPushButton("Cancel", this);
    cancelButton->setFont(font);
    cancelButton->setVisible(false);

    QObject::connect(cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));

    setFocusPolicy(Qt::StrongFocus);  // Keyboard focus to get keystrokes

    interval = 1000;
    fixationInterval = 500;
    numberOfImages = INT_MAX;
    quizOption = 0;
    viewProgressAfterQuiz = false;
    viewProgressAfterTest = false;
    firstAttempt = true;
    quizInProgress = false;
    spellingSensitivity = 1.0;
    messageIndex = 0;

#ifndef APP_AMINO
    backgroundColor = Qt::white;
#endif
}

// Initialize progress report variables
void QuizDialog::initProgressReportVariables()
{
    images.clear();
    attempts.clear();
    rightAnswers.clear();

    int n = answerList.size() > numberOfImages ? numberOfImages:answerList.size();
    for (int i = 0; i < n; ++i)
    {
        QString image = answerList[indexList[i]].split('|').at(0);
        int index = images.indexOf(image);
        if (index == -1)
        {
            images << image;
            attempts << 0.0;
            rightAnswers << 0.0;
        }
    }
}

// Update progress report variables
void QuizDialog::updateProgressReportVariables(bool correct)
{
    // quizOption == 2 => Image Comparison
    // For image comparison the first image is selected
    // from the list of images chosen for the Quiz/Test
    // The second image is selected randomnly  Therefore
    // we add it to the list of chosen amino acids.
    if (quizOption == 2)
    {
        attempts[images.indexOf(image1)] += 0.5;
        int index = images.indexOf(image2);
        if (index == -1)
        {
            images << image2;
            attempts << 0.0;
            rightAnswers << 0.0;
        }
        attempts[images.indexOf(image2)] += 0.5;
    }
    else
        attempts[images.indexOf(image1)] += 1.0;

    // If Image comparison and the answer is correct
    // award 0.5 to each image otherwise 1.0
    if (correct)
    {
        if (quizOption == 2)
        {
            rightAnswers[images.indexOf(image1)] += 0.5;
            rightAnswers[images.indexOf(image2)] += 0.5;
        }
        else
            rightAnswers[images.indexOf(image1)] += 1.0;
    }
}

// Save progress report
void QuizDialog::saveProgress()
{
    double totalRight = 0.0;        // number of right answers
    double totalAttempts = 0.0;     // number of attempts

    QString record="";
    if (quiz)
        record.append("Quiz");
    else
        record.append("Test");

    record.append(",");
    record.append(modeTypes[quizOption]);
    record.append(",");
    record.append(QDateTime::currentDateTime().toString());
    record.append(",");

#ifdef APP_AMINO
    record.append(propertyTypes[propertyType]);
    record.append(",");
    record.append(nameTypes[nameType]);
    record.append(",");
    record.append(imageType);
#else
    record.append(propertyString);
#endif

    for (int i = 0; i < images.size(); ++i)
    {
        record.append("," );
        record.append(images[i]);
        record.append(",");
        double percentage = 0.0;

        if (attempts[i] != 0.0)
        {
            percentage = ((double)rightAnswers[i] / (double)attempts[i]) * 100;
            totalRight += rightAnswers[i];
            totalAttempts += attempts[i];
        }

        record.append(QString::number(percentage, 'f', 2));
    }

    record.append(",");
    double total = answerList.size() < numberOfImages ? answerList.size() : numberOfImages;
    double overallPercentage = (totalRight / total) * 100;
    double percentageComplete = (totalAttempts / total) * 100;
    record.append(QString::number(percentageComplete, 'f', 2));
    record.append(",");
    record.append(QString::number(overallPercentage, 'f', 2));
    record.append("\n");
    records.append(record);

    hide();

    if ((quiz && viewProgressAfterQuiz) || (!quiz && viewProgressAfterTest))
    {
        qDebug() << "generating report '" << UserDataFolder().c_str() << "' for user " << username;
        ProgressReport::instance()->moveToCenter();
        ProgressReport::instance()->generateReport(records, username);
        ProgressReport::instance()->exec();
    }
}

// Maximize dialog and place widgets as required
void QuizDialog::organizeDialog()
{
    QSize dialogSize = size();

    // If the quiz/test option is NOT "Image Naming with prompt"
    // textbox, buttons etc. are to displayed in the middle
    // of the screen.
    int h = quizOption > 0 ? dialogSize.height() / 2 : dialogSize.height() - ppp(95);
    int midway = dialogSize.width() / 2;

    enterAnswerHereLabel->setGeometry(midway - ppp(280 - 200 - 10), h, ppp(200), ppp(30));
    answerLineEdit->setGeometry(midway - ppp(280), h, ppp(410), ppp(30));
    answerLineEdit->setFocus();
    okButton->setGeometry(midway + ppp(130 + 10), h, ppp(110), ppp(30));
    cancelButton->setGeometry(midway + ppp(130 + 10 + 110 + 10), h, ppp(110), ppp(30));

    // Only in Image Comparison would we need 2 images
    if (quizOption != 2)
    {
        imageLabel1->setGeometry(0, 0, dialogSize.width(), dialogSize.height() - ppp(100));
        imageLabel2->setVisible(false);
    }
    else
    {
        imageLabel1->setGeometry(ppp(1), ppp(1), dialogSize.width() / 2 - ppp(2), dialogSize.height() - ppp(2));
        imageLabel2->setVisible(true);
        imageLabel2->setGeometry(dialogSize.width() / 2 + ppp(1), ppp(2), dialogSize.width() / 2 - ppp(2), dialogSize.height() - ppp(2));
    }

    answerLabel->setGeometry(0, dialogSize.height() - ppp(50), dialogSize.width(), ppp(50));
    incorrectAnswerLabel->setGeometry(QRect(0, 0, dialogSize.width(), dialogSize.height()));
    initProgressReportVariables();
    quizInProgress = true;

    updateLabel();
}

// Initialize Yes/No list
void QuizDialog::initYNList()
{
    ynListIndex = 0;     // Yes/No list index
    int n = filenameList.size()<numberOfImages?filenameList.size():numberOfImages;
    int i = 0;

    // Initialize half the list to 1
    // and the rest to 0 then randomize

    for (; i < n / 2; ++i)
        ynList << 1;

    for (; i < n; ++i)
        ynList << 0;

    for (i = 1; i < n; ++i)
    {
        int index1 = rand() % n;
        int index2 = rand() % n;
        while (index1 == index2)
            index2 = rand() % n;
        int temp = ynList[index1];
        ynList[index1] = ynList[index2];
        ynList[index2] = temp;
    }
}

// Initialize variables before starting Quiz/Test session
void QuizDialog::initialize( bool flag )
{
    setPalette(initialPalette);
    initYNList();
    currentImageIndex = -1;
    intervalCounter = 2;
    firstAttempt = true;
    incorrectAnswer = false;
    quiz = flag;
    answerLabel->setText("");
    organizeDialog();
}

void QuizDialog::setSize(QSize size)
{
   setMaximumSize(size);
   setMinimumSize(size);
}

// Find the number of matching characters in the given answer
int QuizDialog::getNumberOfMatchingCharacters(QString correctAnswer, QString attempt)
{
    int i = 0;
    int j = 0;
    int count = 0;
    int correctAnswerLength = correctAnswer.length();  // length of the correct answer
    int attemptLength = attempt.length();              // length of the attempted answer

    if (correctAnswerLength == 0 || attemptLength == 0)
        return 0;

    // Find first pair of matching characters
    while (i < correctAnswerLength)
    {
        if (correctAnswer.at(i) == attempt.at(j))
            break;

        j++;

        if (j == attemptLength || j == MAX_SEARCH_LENGTH)   // MAX_SEARCH_LENGTH=3
        {
            i++;
            j = 0;
        }
    }

    // Get the matching substring begining at i, j
    if (i < correctAnswerLength)
    {
        int p = i + 1;
        int q = j + 1;
        while (p < correctAnswerLength && q < attemptLength )
        {
            if (correctAnswer.at(p) == attempt.at(q))
            {
                p++;
                q++;
            }
            else
                break;
        }

        // Add to list for tester to see.
        // Make recursive call with rest of words.
        if (p > i)
        {
            count = p - i;
            if (p < correctAnswerLength && q < attemptLength)
            {
                correctAnswer.remove(0, p);
                attempt.remove(0, q);
                count += getNumberOfMatchingCharacters(correctAnswer, attempt);
            }
        }
    }

    return count;
}

// Method that returns if the answer is correct or not
// depending on the spell check senisitivity. Spelling
// sensitivity is added to each individual word in the
// answer and not the answer as a whole.
bool QuizDialog::isCorrectAnswer(QString correctAnswer, QString attempt)
{
    // Looking for an exact match
    if (correctAnswer == attempt)
        return true;

    QStringList correctList = correctAnswer.simplified().split(' ');      // List that contains words in the answer
    QStringList attemptList = attempt.simplified().split(' ');            // List that contains the words in the attempt

    int a = attemptList.size();

    if (correctList.size() != a)
        return false;

    for (int i = 0; i < a; ++i)
    {
        float n = getNumberOfMatchingCharacters( correctList.at(i), attemptList.at(i));
        float maxLength = correctList.at(i).length() > attemptList.at(i).length()?
            correctList.at(i).length():attemptList.at(i).length();
        if (n / maxLength < spellingSensitivity)
            return false;
    }
    return true;
}

// Method if cancel is clicked
void QuizDialog::cancelClicked()
{
    enterAnswerHereLabel->setVisible(false);
    answerLineEdit->setVisible(false);
    okButton->setVisible(false);
    cancelButton->setVisible(false);

    int yesNo = QMessageBox::question(this, "Quit", "Are you sure?", QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes);
    if (yesNo == QMessageBox::Yes)
        saveProgress();
    else
        getAnswer();
}

// Listening to the keyboard
void QuizDialog::keyPressEvent(QKeyEvent * e)
{
    if (incorrectAnswer)
    {
        incorrectAnswer = true;

        // The right answer
        QString image1 = answerList.at(indexList[currentImageIndex]).split('|').at(1);

        // If image comparison, then answer
        // string has name of both images.
        if (quizOption == 2)      // Image Comparison
        {
            QString image2 = answerList.at(indexList[randomIndex]).split('|').at(1);
            image1 = "Left: " + image1 + "<br>" + "Right: " + image2;
        }

        // Press y to see the correct answer before repeating the question.
        // Press n to go on to the next question without repeating the question.
        // Press r to repeat the question without seeing the correct answer.
        int key = e->key();
        switch( key )
        {
            case Qt::Key_Y:
                currentImageIndex--;
                firstAttempt = false;
                font.setPixelSize(ppp(25));
                incorrectAnswerLabel->setFont(font);
                incorrectAnswerLabel->setText(changeFontColor(image1));
                if (quizOption == 2)
                    QTimer::singleShot(1000, this, SLOT(updateLabel()));
                else
                    QTimer::singleShot(1500, this, SLOT(updateLabel()));
                break;
            case Qt::Key_N:
                firstAttempt = true;
                updateLabel();
                break;
            case Qt::Key_R:
                currentImageIndex--;
                firstAttempt = false;
                updateLabel();
                break;
        }
    }
}

// Method to ask show the text that asks the user
// what he wants to do once he gets the answer wrong.
// if the visibility is true otherwise toggle
void QuizDialog::setVisibleIncorrectAnswerLabel(bool visibility)
{
    imageLabel1->setVisible(!visibility);

#ifdef APP_AMINO
    answerLabel->setText(" ");
#endif

    if (quizOption == 2)
        imageLabel2->setVisible(!visibility);

    incorrectAnswerLabel->setVisible(visibility);

    if (visibility)
    {
        font.setPixelSize(ppp(15));
        incorrectAnswerLabel->setFont(font);
        incorrectAnswerLabel->setText(changeFontColor(
            "Your answer is incorrect.<br>\
            Press \"y\" to see the correct answer before repeating the question.<br>\
            Press \"n\" to go on to the next question without repeating the question.<br>\
            Press \"r\" to repeat the question without seeing the correct answer."
            )
        );
    }

    incorrectAnswer = visibility;
}

// Method if Ok button is
// clicked or Return is hit
void QuizDialog::answerEntered()
{
    enterAnswerHereLabel->setVisible(false);
    answerLabel->setText(" ");
    answerLineEdit->setVisible(false);
    okButton->setVisible(false);
    cancelButton->setVisible(false);

#ifndef APP_AMINO
    imageLabel1->setText(" ");
#endif

    // Find out if the answer is correct or not
    bool correct = isCorrectAnswer(
        answer1.toLower().trimmed(),
        answerLineEdit->text().toLower().trimmed()
    );

    // Save report only for the first attempt
    if (firstAttempt)
        updateProgressReportVariables( correct );

    // Notify the user of right or wrong answer
    // only in quiz mode.
    if (quiz)
    {
        if (correct)
        {
            int tempIndex = rand() % MAX;
            while (messageIndex == tempIndex )
                tempIndex = rand() % MAX;

            messageIndex = tempIndex;
            QMessageBox::information(this, "Correct Answer", message[messageIndex], QMessageBox::Ok, QMessageBox::Ok);
            firstAttempt = true;
        }
        else
        {
            setVisibleIncorrectAnswerLabel(true);
            return;
        }
    }

    updateLabel();
}

// Ask the user to enter the answer
void QuizDialog::getAnswer()
{
    enterAnswerHereLabel->setVisible(true);
    answerLineEdit->setVisible(true);
    debugString += answer1;

    answerLineEdit->setText("");
    answerLineEdit->selectAll();
    answerLineEdit->setFocus();
    okButton->setVisible(true);
    cancelButton->setVisible(true);
    enterAnswerHereLabel->setText(changeFontColor("Answer:"));

#ifndef APP_AMINO
    imageLabel1->setVisible(true);
    if (quizOption == 0)
       answerLabel->setVisible(true);
#endif

    if (quizOption > 1)
        enterAnswerHereLabel->setText(changeFontColor("Same?(y/n)"));

    if (quizOption != 0)
    {
        imageLabel1->setText(" ");
        imageLabel2->setText(" ");
        answerLabel->setText(" ");
        intervalCounter = 2;
    }
    else
        intervalCounter = 0;
}

void QuizDialog::setBackgroundColor(int color)
{
    int blackColor = Qt::white;

#ifdef APP_AMINO
    blackColor = Qt::black;
#endif

    backgroundColor = color;
    QPalette pal;
    switch (color)
    {
        case Qt::black:
            pal.setColor(QPalette::Window, blackColor);
            break;
        case Qt::white:
            pal.setColor(QPalette::Window, Qt::white);
            break;
    }

    setPalette(pal);
}

QString QuizDialog::changeFontColor(QString caption)
{
    QString paddedString;
    QString blackColorString = "<font color='black'>";

#ifdef APP_AMINO
    blackColorString = "<font color='white'>";
#endif

    switch (backgroundColor)
    {
        case Qt::black:
            paddedString = blackColorString + caption + "</font>";
            break;
        case Qt::white:
            paddedString = "<font color='black'>" + caption + "</font>";
            break;
    }
    return paddedString;
}

void QuizDialog::loadImage(int index)
{
    fname = (ResourceFolder() + "Images/").c_str();
    debugString += QString("%1").arg(filenameList.at(indexList[index])) + "|";
    fname = fname + QString("%1").arg(filenameList.at(indexList[index]));
    fname = fname + ".jpg";

    qDebug() << "loading image " << fname;
    image.load(fname);

    if (!image.load(fname))
    {
        QMessageBox::critical(this, "Error loading image",
            "<b>A problem occured while trying to load the image. Terminating session.</b>",
            QMessageBox::Ok, QMessageBox::Ok);

        hide();
        return;
    }
}

// Method specifically for Image Comparison and Verification
// that would return a random index depending on whether we
// want the answer to be yes or no.
int QuizDialog::generateRandomIndex()
{
    int x;
    int n = filenameList.size();
    bool yes = ynList[ynListIndex];
    int i = 0;
    if (yes)
    {
        for (; i < n; ++i)
        {
            answer2 = answerList.at(indexList[i]).split('|').at(1);
            if (i != currentImageIndex && answer1 == answer2 )
                break;
        }

        // If the for loop does not end that mean
        // an appropriate index was found else the
        // the only possible index is itself.
        if (i != n)
            x = i;
        else
            x = currentImageIndex;

        answer1 = "y";
    }
    else
    {
        x = rand() % n;
        while (currentImageIndex == x)
            x = rand() % n;

        answer2 = answerList.at(indexList[x]).split('|').at(1);
        while (answer1 == answer2)
        {
            x = rand() % n;
            while (currentImageIndex == x)
                x = rand() % n;
            answer2 = answerList.at(indexList[x]).split('|').at(1);
        }

        answer1 = "n";
    }

    ynListIndex++;
    return x;
}

void QuizDialog::updateLabel()
{
    setVisibleIncorrectAnswerLabel( false );
    int n = filenameList.size();
    if ((currentImageIndex < n - 1 && currentImageIndex < numberOfImages - 1) || intervalCounter == 1)
    {
        setBackgroundColor( backgroundColor );

        //intervalCounter 0 ->  Show image(s)
        //intervalCounter 1 ->  This is specifically for Image Verification
        //                      Here the user is shown the answer for a second
        //                      and the asked to verify y or n
        //intervalCounter 2 ->  Fixation interval
        switch (intervalCounter)
        {
            case 0:
                debugString = QString("%1").arg( currentImageIndex+2 ) + "|";
                loadImage(++currentImageIndex);

                if (imageLabel1->width() < image.width() || imageLabel1->height() < image.height())
                    imageLabel1->setPixmap(QPixmap::fromImage(image.scaled(imageLabel1->size(), Qt::KeepAspectRatio)));
                else
                    imageLabel1->setPixmap(QPixmap::fromImage(image));

                if (firstAttempt)
                {
                    answer1 = answerList.at(indexList[ currentImageIndex ] ).split('|').at(1);
                    image1 = answerList.at(indexList[currentImageIndex]).split('|').at(0);
                    debugString += image1 + "|";
                }
                answerLabel->setText(changeFontColor(answer1));
                if (quizOption == 2)
                {
                    if (firstAttempt)
                        randomIndex = generateRandomIndex();

                    loadImage(randomIndex);
                    answer2 = answerList.at(indexList[randomIndex]).split('|').at(1);
                    image2 = answerList.at(indexList[randomIndex]).split('|').at(0);

                    if (imageLabel2->width() < image.width() || imageLabel2->height() < image.height())
                        imageLabel2->setPixmap(QPixmap::fromImage(image.scaled(imageLabel2->size(), Qt::KeepAspectRatio)));
                else
                    imageLabel2->setPixmap(QPixmap::fromImage(image));
                }

                // If not Image Verification, wait for a while
                // and then ask the user to answer.
                if (quizOption != 3)
                    QTimer::singleShot(interval, this, SLOT(getAnswer()));
                else
                {
                    intervalCounter = 1;
                    QTimer::singleShot(interval, this, SLOT(updateLabel()));
                }
                break;
            case 1:
                if (firstAttempt)
                    randomIndex = generateRandomIndex();
                image2 = answerList.at(indexList[randomIndex]).split('|').at(0);
                answer2 = answerList.at(indexList[randomIndex]).split('|').at(1);
                debugString += image2 + "|";
                imageLabel1->setText(changeFontColor(answer2));
                QTimer::singleShot(1000, this, SLOT(getAnswer()));
                break;
            case 2:
                imageLabel1->setText(changeFontColor("+"));
                imageLabel2->setText(changeFontColor("+"));
                intervalCounter = 0;
                QTimer::singleShot(fixationInterval, this, SLOT(updateLabel()));
                break;
        }
    }
    else
    {
        timerSet = false;
        saveProgress();
    }
}

void QuizDialog::setInterval(int value)
{
    interval = value;
}

void QuizDialog::setFixationInterval(int value)
{
    fixationInterval = value;
}

void QuizDialog::setSpellingSensitivity(int value)
{
    switch(value)
    {
        case 0:
            spellingSensitivity = 1.0f;
            break;
        case 1:
            spellingSensitivity = 0.9f;
            break;
        case 2:
            spellingSensitivity = 0.8f;
            break;
        case 3:
            spellingSensitivity = 0.7f;
            break;
    }
}

void QuizDialog::setNumberOfImages(int value)
{
    switch (value)
    {
        case 0:
            numberOfImages = INT_MAX;
            break;
        case 1:
            numberOfImages = 15;
            break;
        case 2:
            numberOfImages = 20;
            break;
        case 3:
            numberOfImages = 25;
            break;
        case 4:
            numberOfImages = 30;
            break;
        case 5:
            numberOfImages = 35;
            break;
        case 6:
            numberOfImages = 40;
            break;
        case 7:
            numberOfImages = 45;
            break;
    }
}

void QuizDialog::setQuizOption(int option)
{
    quizOption = option;
    switch (quizOption)
    {
        case 0:
            answerLabel->setVisible( true );
            break;
        default:
            answerLabel->setVisible( false );
            break;
    }
}

void QuizDialog::closeEvent(QCloseEvent * e)
{
    e->ignore();
}

void QuizDialog::reject()
{
}

void QuizDialog::setUsername(QString uname)
{
    qDebug() << "set username -> " << uname;
    username = uname;
}

#ifdef APP_AMINO
void QuizDialog::setNameType(int nType)
{
    nameType = nType;
}

void QuizDialog::setImageType(QString iType)
{
    imageType = iType;
}

void QuizDialog::setPropertyType(int pType)
{
    propertyType = pType;
}

#else
void QuizDialog::setPropertyType(int pType, QString pString)
{
   propertyType = pType;
   propertyString = pString;
}
#endif

void QuizDialog::setQuizProgressReport(bool flag)
{
    viewProgressAfterQuiz = flag;
}

void QuizDialog::setTestProgressReport(bool flag)
{
    viewProgressAfterTest = flag;
}

QStringList QuizDialog::getCurrentRecords()
{
    return records;
}

QuizDialog* QuizDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new QuizDialog(parent);
    return s_instance;
}

QuizDialog::~QuizDialog()
{
}
