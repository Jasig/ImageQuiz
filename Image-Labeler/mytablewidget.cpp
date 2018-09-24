#include "mytablewidget.h"

#include <QDebug>

#include "csvutil.hpp"


myTableWidget::myTableWidget(QWidget *parent) :
    QTableWidget(parent)
{

    this->setSelectionMode( QAbstractItemView::SingleSelection );
    this->installEventFilter(this);


    connect(this,SIGNAL(currentCellChanged(int,int,int,int)),
            this, SLOT(curCellChanged(int,int)));

    connect(this,SIGNAL(cellChanged(int,int)),
            this,SLOT(textChanged(int,int)));

    connect((QObject*)this->horizontalHeader(), SIGNAL(sectionDoubleClicked ( int )),
            this, SLOT(labelsDblClicked(int)));
}


void myTableWidget::curCellChanged(int row, int)
{
    emit imageChanged(QString (this->item(row, 0)->text()));
}

void myTableWidget::refreshImage()
{
    if (this->currentRow() != -1)
        emit imageChanged(QString(this->item(this->currentRow(), 0)->text()));
    else
        emit imageChanged(QString(""));
}


void myTableWidget::textChanged(int row, int column)
{
    QString s = this->item(row,column)->text();
    QString parsed = csv_parse_field(s);
    if (s != parsed)
        this->item(row, column)->setText(parsed);

    QStringList slist = tableData->at(row);

    if (column == 0) // emiting signal that filename should be changed
        emit renameImageRequested(slist.at(0), parsed);

    slist.replace(column, parsed);
    tableData->replace(row, slist);
}


void myTableWidget::labelsDblClicked(int index)
{
    emit renameFieldRequested(index);
}


void myTableWidget::setData(QStringList* labels, QList<QStringList> *data)
{
    columnLabels = labels;
    tableData = data;

    this->setRowCount(data->count());
    this->setColumnCount(labels->count());
    this->setHorizontalHeaderLabels(*labels);

    for (int row = 0; row < data->count(); ++row)
    {
        for (int col = 0; col < labels->count(); ++col)
        {
            QTableWidgetItem *item = new QTableWidgetItem(data->at(row).at(col));
            this->setItem(row,col,item);
        }
    }
}

bool myTableWidget::eventFilter(QObject* object, QEvent* event)
{
    if (event->type() != QEvent::KeyPress)
        return QWidget::eventFilter(object, event);

    // transforms QEvent into QKeyEvent
    QKeyEvent* pKeyEvent=static_cast<QKeyEvent*>(event);
    switch(pKeyEvent->key())
    {
        case Qt::Key_Return:
        {
            // Return key pressed - record update
            if (this->currentRow() < this->rowCount() - 1)
            {
                if (this->columnCount() > 1)
                    this->setCurrentCell(this->currentRow() + 1, 1);
                else
                    this->setCurrentCell(this->currentRow() + 1, 0);
            }

            break;
        }

        case Qt::Key_Delete:
        {
            // Delete key pressed - record delete
            if (this->selectedItems().count()) // not empty selection
                this->selectedItems().at(0)->setText("");

            break;
        }
        case Qt::Key_Backspace:
        {
            if (this->selectedItems().count()) // not empty selection
            {
                this->selectedItems().at(0)->setText("");

                QModelIndex index = this->model()->index(this->currentRow(), this->currentColumn(), QModelIndex());
                this->edit(index);
            }
            break;
        }


    }
    return false;
}
