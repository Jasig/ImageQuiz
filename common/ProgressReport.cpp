#include "ProgressReport.h"

#include "sysscale.h"

ProgressReport *ProgressReport::s_instance = 0;

ProgressReport::ProgressReport( QWidget *parent )
	: QDialog( parent )
{
    width = ppp(320);
    height = ppp(435);

	setGeometry(QRect(0, 0, width, height));
	setMinimumSize(width, height);
	setMaximumSize(width, height);
	setWindowTitle("Progress Report");

    QFont font("Times", 14, QFont::Bold);
    font.setPixelSize(ppp(14));

	report = new QTextEdit(this);
	report->setReadOnly(true);
	report->setFont(font);
    report->setGeometry(QRect(ppp(5), ppp(5), width - ppp(10), height - ppp(50)));

	okButton = new QPushButton("Ok", this);
    okButton->setGeometry(width / 2 + pppF(2.5), height - ppp(40), width / 2 - pppF(7.5), ppp(25));
	okButton->setDefault(true);
	okButton->setAutoDefault(true);
	QObject::connect(okButton, SIGNAL(clicked()), this, SLOT(hide()));

	printButton = new QPushButton("Print", this);
    printButton->setGeometry(QRect(ppp(5), height - ppp(40), width / 2 - pppF(7.5), ppp(25)));
	QObject::connect(printButton, SIGNAL(clicked()), this, SLOT(printToFile()));
}

void ProgressReport::printToFile()
{
	QTextDocument* document = report->document();
	QPrinter printer;

	QPrintDialog * dialog = new QPrintDialog(&printer, this);
	if (dialog->exec() == QDialog::Accepted)
	{
		document->print(&printer);
	}
}

void ProgressReport::generateIndividualReport( QString record )
{
	int i=0;
	QStringList details = record.split( "," );

	// If you open csv file in excel some times excel might pad ',' at the end to make
	// all the rows of uniform length. So we look for empty columns and delete them
	while (details.last().length() == 0)
		details.pop_back();

	report->append("Session Type: " + details[i] + "-" + details[i + 1]);
	
	i += 2;
	
	report->append("Date: " + details[i++]);
	report->append("Property Type: " + details[i++]);

#ifdef APP_AMINO
	report->append("Name Type: " + details[i++]);
	report->append("Image Type: " + details[i++]);
#endif
	
	for (; i < details.size() - 2; i += 2)
		report->append(details[i] + ": " + details[i + 1] + "%");
	
	report->append("Percentage Complete: " + details[i++].trimmed() + "%");
	report->append("Overall Percentage: " + details[i].trimmed() + "%");
	report->append("\n");
}

// Display MAX_REPORTS_DISPLAYED (25) most recent reports
void ProgressReport::generateReport(QStringList currentRecords, QString fname)
{
	filename = fname;
	int reportCounter = 0;
	report->clear();

	// First display reports from the current session
	int n = currentRecords.size();

	if (n != 0)
	{
		for (int i = n - 1; i >= 0 && reportCounter < MAX_REPORTS_DISPLAYED; --i)
		{
			generateIndividualReport(currentRecords[i]);
			reportCounter++;
		}
	}

	// Reports from stored sessions
	QFile inFile;
	inFile.setFileName(fname + ".csv");
	if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
	{
        QMessageBox::warning( this, "Invalid Filename", tr("Cannot open %1 for reading.").arg(fname + ".csv"));
		return;
	}

	QTextStream in;
	in.setDevice(&inFile);
    QString line = in.readLine(); // advanced options
#ifndef APP_AMINO
    line = in.readLine();
#endif
	while (!in.atEnd() && reportCounter < MAX_REPORTS_DISPLAYED)
	{
		line = in.readLine();
		if (!line.isEmpty())
		{
			generateIndividualReport(line);
			reportCounter++;
		}
		else
			break;
	}
	
	inFile.close();
	
	// Return the scroll bar to the top of the text box
	// So the most recent progress report is in view
	report->scrollToAnchor("Session");
}

// Return the only instance of the class
// parent: parent that calls the advanced options dialog
ProgressReport * ProgressReport::instance(QWidget * parent)
{
	if (!s_instance)
		s_instance = new ProgressReport(parent);
	return s_instance;
}

// Save the coordinates of the center of the main dialog
void ProgressReport::setCoordinates(int center_x, int center_y )
{
	x = center_x - width / 2;
	y = center_y - height / 2;
}

// Move this dialog the the center of the main dialog
void ProgressReport::moveToCenter()
{
	move(x, y);
}

ProgressReport::~ProgressReport(void)
{
}
