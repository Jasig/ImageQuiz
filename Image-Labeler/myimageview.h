#ifndef MYIMAGEVIEW_H
#define MYIMAGEVIEW_H

#include <QLabel>
#include <QPixmap>
#include <QResizeEvent>

class myImageView : public QLabel
{
    Q_OBJECT
public:
    explicit myImageView(QWidget *parent = 0);
    virtual int heightForWidth( int width ) const;
    virtual QSize sizeHint() const;
signals:

public slots:
    void setPixmap ( const QPixmap & );
    void resizeEvent(QResizeEvent *);
private:
    QPixmap pix;
};

#endif // MYIMAGEVIEW_H
