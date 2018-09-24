#include "StudyDialog.h"
#include "sysfolders.h"
#include "sysscale.h"

#include <QDebug>

StudyDialog * StudyDialog::s_instance = nullptr;

StudyDialog::StudyDialog(QWidget * parent)
    : QDialog(parent)
{
    setWindowTitle("Study Session");

    initialPalette = palette();

    imageLabel = new QLabel(this); // Label to hold image, text msgs
    imageLabel->setBackgroundRole(QPalette::Base);
    imageLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);

    font.setPixelSize(ppp(25));
    imageLabel->setFont(font);
    imageLabel->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);

    captionLabel = new QLabel(this); // Label to hold caption below the image
    captionLabel->setBackgroundRole(QPalette::Base);
    captionLabel->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    captionLabel->setFont(font);
    captionLabel->setAlignment(Qt::AlignHCenter | Qt::AlignVCenter);
    setFocusPolicy(Qt::StrongFocus); // Keyboard focus to get keystrokes

    shortcut = new QShortcut(QKeySequence("Ctrl+E"), this, SLOT(close()));

    timer = new QTimer(this);
    timer->setSingleShot(true);
    connect(timer, SIGNAL(timeout()), this, SLOT(updateLabel()));
    timerSet = false;
    interval = 1000;
    fixationInterval = 500;
    nameOption1 = 0;
    nameOption2 = 0;
    stopOption = 0;
    interactionOption = 0;
    backgroundColor = Qt::black;
    firstTime = true;
}

// Method to change the background color
// depending type of image being displayed.
// Black every time excpet for structural
// formula images
void StudyDialog::setBackgroundColor(int color)
{
    backgroundColor = color; // Setting a variable for setting the font color later.
    QPalette pal;

#ifndef APP_AMINO
    backgroundColor = Qt::white;
#endif

    // either black or white
    switch (backgroundColor)
    {
        case Qt::black:
            pal.setColor(QPalette::Window, Qt::black);
            break;
        case Qt::white:
            pal.setColor(QPalette::Window, Qt::white);
            break;
    }

    setPalette(pal);
}

// Method to set the font color depending on the
// background color. Black on white and vice versa.
QString StudyDialog::changeFontColor(QString caption)
{
    QString paddedString;
    switch (backgroundColor)
    {
        case Qt::black:
            paddedString = "<font color='white'>" + caption + "</font>";
            break;
        case Qt::white:
            paddedString = "<font color='black'>" + caption + "</font>";
            break;
    }

    qDebug() << "setting label caption to " << paddedString;

    return paddedString;
}

// Method to organize the Study dialog.
void StudyDialog::organizeDialog()
{
    QSize dialogSize = size();

    imageLabel->setGeometry(0, 0, dialogSize.width(), dialogSize.height() - ppp(100));
    captionLabel->setGeometry(0, dialogSize.height() - ppp(100), dialogSize.width(), ppp(100));

    // If interaction option = 0 i.e., using keyboard
    // start with the first image. Other if the option
    // is Auto image display is ask the user to press
    // a key to start
    if (interactionOption == 0) // Use arrow keys
    {
        setBackgroundColor(backgroundColor);
        loadImage(filenameList.at(indexList[++currentImageIndex]));

        captionLabel->setText(changeFontColor(captionList.at(indexList[currentImageIndex])));
    }
    else  // Auto image display
    {
        imageLabel->setText("<b>Press any key to start.</b>");
        captionLabel->setText("");
    }
}

// Initialize the variables
void StudyDialog::initialize()
{
    setPalette( initialPalette );
    currentImageIndex = -1;
    intervalCounter = 2;
    timer->stop();
    timerSet = false;
    organizeDialog();
}

// Set size to full screen
void StudyDialog::setSize(QSize size)
{
    setMaximumSize(size);
    setMinimumSize(size);
    move(0, 0);
}

// End the study session
void StudyDialog::close()
{
    timerSet = false;
    timer->stop();
    int choice = QMessageBox::question( this, "Quit",
        "Are you sure?",
        QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes
    );

    if (choice == QMessageBox::Yes)
        hide();
    else
    {
        // If Auto image display keep going
        if (interactionOption == 1)
        {
            timerSet = true;
            timer->start();
        }
    }
}

// Called when the X is clicked
void StudyDialog::closeEvent(QCloseEvent * e)
{
    close();
    e->ignore();
}

// Load the image
void StudyDialog::loadImage( QString fname )
{
    fname = (ResourceFolder() + "Images/").c_str() + fname + ".jpg";
    qDebug() << "loading image " << fname;
    image.load(fname);

    // Scale the image down to size if the width or
    // height is greater than that size of the label
    // Simply Display the image otherwise.
    if (imageLabel->width() < image.width() || imageLabel->height() < image.height())
        imageLabel->setPixmap(QPixmap::fromImage(image.scaled(imageLabel->size(), Qt::KeepAspectRatio)));
    else
        imageLabel->setPixmap(QPixmap::fromImage(image));
}

// Update image labels
void StudyDialog::updateLabel()
{
    setBackgroundColor(backgroundColor);

    if (currentImageIndex < filenameList.size() - 1)
    {
        switch (intervalCounter % 3)
        {
            // case 0 - Image and caption below image or only Image.
            // case 1 - Image name after the image.
            // case 2 - Fixation inteveral.
            case 0:
                loadImage(filenameList.at(indexList[++currentImageIndex]));

                // nameOption2 == 0   Show Image with Name
                // nameOption2 == 1   Show Image only
                if (nameOption2 == 0)
                {
                    intervalCounter = 2;
                    captionLabel->setVisible(true);
                    captionLabel->setText(changeFontColor(captionList.at(indexList[currentImageIndex])));
                }
                else
                {
                    intervalCounter = 1;
                    currentImageIndex--;
                }
                // Don't wait for key press, just keep going
                timer->start(interval);
                break;
            case 1:
                captionLabel->setVisible(false);
                currentImageIndex++;
                imageLabel->setText(changeFontColor(captionList.at(indexList[currentImageIndex])));

                // Don't wait for key press, just keep going
                if (stopOption != 1)
                {
                    intervalCounter = 2;
                    timer->start(1000);
                }
                else // Waiting for key press
                {
                    while (timer->isActive()) // wait for timer to expire
                        ;
                    timerSet = false;
                    intervalCounter = 2;
                }
                break;
            case 2:
                captionLabel->setVisible(false);
                imageLabel->setText(changeFontColor("+"));
                intervalCounter = 0;
                timer->start(fixationInterval);
                break;
        }
    }
    else
    {
        timer->stop();
        timerSet = false;
        hide();
    }
}

// Listening to the keyboard
void StudyDialog::keyPressEvent(QKeyEvent * e)
{
#ifdef __APPLE__
#define CTRL_KEY "\u2318"
#else
#define CTRL_KEY "Ctrl"
#endif

    int key = e->key();
    if (key == Qt::Key_Escape) // Esc key terminates session
    {
        e->ignore();
        close();
    }

    setBackgroundColor(backgroundColor);

    switch (interactionOption)
    {
        case 0: // Wait until key press
            switch (key)
            {
                case Qt::Key_Left:
                    if (currentImageIndex != 0)
                        currentImageIndex--;
                    break;
                case Qt::Key_Right:
                    if (currentImageIndex < filenameList.size() - 1)
                        currentImageIndex++;
                    else
                    {
                        QMessageBox::information(this, "Message",
                            QString("No more images.\nPress ") + QString::fromUtf8(CTRL_KEY) + QString("+E or Esc to exit."),
                            QMessageBox::Ok, QMessageBox::Ok
                        );
                    }
                    break;
            }

            loadImage(filenameList.at(indexList[currentImageIndex]));
            captionLabel->setText(changeFontColor(captionList.at(indexList[currentImageIndex])));

            break;
        case 1: // Auto Image Display
            if (!timerSet)
            {
                updateLabel();
                timerSet = true;
            }
            break;
    }
}

// Method is set either auto image display or
// using arrow keys to browse images
void StudyDialog::setInteractionOption( int index )
{
    interactionOption = index;

    // interactionOption == 0 -> Use Arrow keys
    // interactionOption == 1 -> Auto Image Display

    if (interactionOption == 0)
        captionLabel->setVisible(!index);
}

// When interaction option is 0, i.e. Use arrow keys
// nameOption1 = 0 -> Show Image with name
// nameOption1 = 1 -> Show image only
void StudyDialog::setNameOption1( int index )
{
    nameOption1 = index;
    captionLabel->setVisible(!index);
}

// When interaction option is 1, i.e. Auto image display
// nameOption2 = 0 -> Show name with image
// nameOption2 = 1 -> Show name after image
void StudyDialog::setNameOption2(int index)
{
    nameOption2 = index;
    captionLabel->setVisible(!index);
}

// When interaction option is 1, i.e. Auto image display
// and nameOption2 = 1 -> Show name after image
// you can either stop and wait for a keypress or just keep going.
void StudyDialog::setStopOption(int checkedState)
{
    stopOption = checkedState;
}

void StudyDialog::setInterval(int value)
{
    interval = value;
}

void StudyDialog::setFixationInterval(int value)
{
    fixationInterval = value;
}

// Singleton design pattern. Only one instance of the
// StudyDialog class
StudyDialog * StudyDialog::instance(QWidget * parent)
{
    if (!s_instance)
        s_instance = new StudyDialog(parent);
    return s_instance;
}

StudyDialog::~StudyDialog()
{
}

