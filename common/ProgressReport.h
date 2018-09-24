#include <QDialog>
#include <QPushButton>
#include <QFile>
#include <QTextEdit>
#include <QMessageBox>
#include <QTextStream>
#include <QDir>
#include <QDateTime>
#include <QPrinter>
#include <QPrintDialog>

#define MAX_REPORTS 200
#define MAX_REPORTS_DISPLAYED 25

class ProgressReport:
   public QDialog
{
   Q_OBJECT
   
   static ProgressReport *s_instance;

   int width;
   int height;

   int x;
   int y;
   QTextEdit *report;
   QPushButton *okButton;
   QPushButton *printButton;
   QString filename;

   ProgressReport( QWidget *parent = 0 );
   void generateIndividualReport( QString );
   ~ProgressReport(void);

public:
   static ProgressReport* instance( QWidget *parent=0 );
   void setCoordinates( int, int );
   void moveToCenter();
   void generateReport( QStringList, QString );
   public slots:
   void printToFile();
};


