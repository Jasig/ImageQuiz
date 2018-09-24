#include <QDialog>
#include <QMainWindow>
#include <QPushButton>
#include <QLabel>
#include <QComboBox>
#include <QDialogButtonBox>
#include <QGroupBox>
#include <QDialogButtonBox>
#include <QCheckBox>
#include <QListWidget>
#include <QFile>
#include <QTextStream>
#include <QMessageBox>

class QuizOptionsDialog: public QDialog
{
    Q_OBJECT

    static QuizOptionsDialog * s_instance;

    int width;
    int height;

    QGroupBox *modeSelectGroupBox;
    QComboBox *modeSelectComboBox;
    QGroupBox *speedGroupBox;
    QSlider *speedSlider;
    QPushButton *continueButton;
    QPushButton *cancelButton;
    QLabel *label1;
    QLabel *label2;
    QLabel *label3;
    QLabel *label4;
    int speed;

    int propertyType;

#if APP_AMINO
    QString imageType;
    QString answer;
    int nameType;
    QHash<QString, QString> hash;
#else
    int answerType;
#endif

    void createFilenameList();
    void createHashMap();
    void createIndexList();
    void randomize();
    void setOptions();
    bool isNotAbleToCreateNegativeQuestions();

    int x;
    int y;
    int option;
    int tempOption;
    int interval;
    bool quiz;

    QuizOptionsDialog(QWidget * parent = 0);
    ~QuizOptionsDialog();

public:
    static QuizOptionsDialog * instance(QWidget *parent = 0);

    QStringList imagesToStudy;
    QString propertyString;
    void addItemToComboBox(QString);
    void clearComboBox();
    void setMode(bool);

#ifdef APP_AMINO
    void setImageType(QString, int);
    void setNameType(int);
    void setPropertyType(int);
#else
    void setPropertyType(int, QString);
    void setAnswerType(int);
#endif
    void setCoordinates(int, int);
    void moveToCenter();
    void resetOptions();

public slots:
    void continueClicked();
    void cancelClicked();
    void setValue( int value );
    void setQuizOption(int option);
};
