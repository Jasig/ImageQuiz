#ifndef MYTABLEWIDGET_H
#define MYTABLEWIDGET_H

#include <QTableWidget>
#include <QEvent>
#include <QKeyEvent>



class myTableWidget : public QTableWidget
{
    Q_OBJECT
public:
    myTableWidget(QWidget *parent = 0);
    void setData(QStringList *labels, QList<QStringList> *data);
    bool eventFilter(QObject* object, QEvent* event);
    void refreshImage();


private:
    QStringList         *columnLabels;
    QList<QStringList>  *tableData;

signals:
    void imageChanged(QString name);
    void renameFieldRequested(int index);
    void renameImageRequested(QString oldname, QString newname);

public slots:

private slots:
    void curCellChanged(int row, int);
    void textChanged(int row, int column);
    void labelsDblClicked(int index);


};

#endif // MYTABLEWIDGET_H
