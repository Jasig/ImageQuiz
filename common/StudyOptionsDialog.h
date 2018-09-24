#include <QDialog>
#include <QGroupBox>
#include <QComboBox>
#include <QPushButton>
#include <QLabel>
#include <QSlider>
#include <QTextStream>
#include <QFile>
#include <QMessageBox>
#include <QHash>
#include <QCheckBox>

class StudyOptionsDialog: public QDialog
{
    Q_OBJECT
    static StudyOptionsDialog *s_instance;

    int width;
    int height;

    QGroupBox *modeSelectGroupBox;
    QComboBox *orderComboBox;
    QComboBox *interactionComboBox;
    QComboBox *nameComboBox1;
    QComboBox *nameComboBox2;
    QCheckBox *stopCheckBox;

    QGroupBox *speedGroupBox;

    QSlider *speedSlider;
    QPushButton *testOptionsContinueButton;
    QPushButton *testOptionsCancelButton;
    QLabel *label1;
    QLabel *label2;
    QLabel *label3;
    QLabel *label4;
    int speed;

    QPushButton *continueButton;
    QPushButton *cancelButton;

    QString imageType;
    QString caption;
    int nameType;
    int propertyType;
    int answerType;
    int orderOption;
    int interactionOption;
    int name1Option;
    int name2Option;
    bool stopOption;
    QHash<QString, QString> hash;

    void createFilenameList();
    void createHashMap();
    void createIndexList();
    void randomize();
    void setOptions();
    void resetOptions();

    int x;
    int y;

    StudyOptionsDialog(QWidget * parent = 0);
    ~StudyOptionsDialog();

public:
    static StudyOptionsDialog * instance(QWidget * parent = 0);
    QStringList imagesToStudy;
    void setImageType(QString, int);
    void setNameType(int);
    void setPropertyType(int);
    void setAnswerType(int);
    void setBackgroundColor(int);
    void setCoordinates(int, int);
    void moveToCenter();

public slots:
    void setInterval( int );
    void continueClicked();
    void cancelClicked();
    void setInteractionOption( int );
    void setNameOption2( int );
};
