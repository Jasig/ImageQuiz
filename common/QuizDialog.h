#include <QFile>
#include <QLabel>
#include <QDialog>
#include <QPixmap>
#include <QMessageBox>
#include <QKeyEvent>
#include <QTimer>
#include <QPixmap>
#include <QFont>
#include <QTimer>
#include <QInputDialog>
#include <QLineEdit>
#include <QTextStream>
#include <QDateTime>

class QuizDialog: public QDialog
{
    Q_OBJECT

    static QuizDialog * s_instance;
    QLabel * imageLabel1;
    QLabel * imageLabel2;
    QLabel * answerLabel;
    QLabel * enterAnswerHereLabel;
    QLabel * incorrectAnswerLabel;
    QLineEdit * answerLineEdit;
    QPushButton * cancelButton;
    QPushButton * okButton;
    QuizDialog(QWidget * parent = 0);
    bool timerSet;

    int quizOption;
    int interval;
    int fixationInterval;
    int intervalCounter;
    float spellingSensitivity;
    int currentImageIndex;
    int randomIndex;
    QString fname;
    QImage image;
    QFont font;
    bool firstAttempt;               // First attempt to answer correctly or not
    bool quizInProgress;             // Quiz/Test in progress or not
    bool quiz;                       // Quiz or Test mode
    int numberOfImages;
    bool viewProgressAfterQuiz;
    bool viewProgressAfterTest;
    bool incorrectAnswer;
    QString answer1, answer2;
    QString username;
    int propertyType;
    int nameType;
    QString imageType;
    QStringList images;
    QString image1, image2;
    QList<double> attempts;
    QList<double> rightAnswers;
    QStringList records;
    QList<bool> ynList;
    int ynListIndex;
    QString debugString;

#ifndef APP_AMINO
    QString propertyString;
#endif

    QPalette initialPalette;
    int backgroundColor;
    QString changeFontColor(QString);
    int messageIndex;
    bool isCorrectAnswer(QString, QString);
    int getNumberOfMatchingCharacters(QString, QString);
    void initProgressReportVariables();
    void updateProgressReportVariables(bool);
    void saveProgress();
    void loadImage(int);
    void initYNList();
    int generateRandomIndex();
    void setVisibleIncorrectAnswerLabel(bool);
   
protected:
    void keyPressEvent(QKeyEvent *);	// Handle keystroke events

public:
    QStringList filenameList;
    QStringList answerList;
    QStringList imagePropertyList;
    QList<int> indexList;

    ~QuizDialog(void);
    static QuizDialog * instance(QWidget * parent = 0);
    void initialize(bool);
    void organizeDialog();
    void setSize(QSize);
    void setInterval(int);
    void setFixationInterval(int);
    void setSpellingSensitivity(int);
    void setNumberOfImages(int);
    void setQuizOption(int);
    void setBackgroundColor(int color);
    void setUsername(QString);

#ifdef APP_AMINO
    void setNameType(int);
    void setImageType(QString );
    void setPropertyType(int);
#else
    void setPropertyType(int, QString);
#endif

    void setQuizProgressReport(bool);
    void setTestProgressReport(bool);
    QStringList getCurrentRecords();

public slots:
    void reject();
    void getAnswer();
    void updateLabel();
    void closeEvent(QCloseEvent *);
    void cancelClicked();
    void answerEntered();
};
