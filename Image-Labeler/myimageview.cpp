#include "myimageview.h"
#include <QDebug>


myImageView::myImageView(QWidget *parent) :
    QLabel(parent)
{
    this->setMinimumSize(1,1);

}

void myImageView::setPixmap ( const QPixmap & p)
{
    pix = p;
    QLabel::setPixmap(pix.scaled(this->size(), Qt::KeepAspectRatio, Qt::SmoothTransformation));

}

int myImageView::heightForWidth( int width ) const
{
    return ((qreal)pix.height()*width)/pix.width();
}

QSize myImageView::sizeHint() const
{
    int w = this->width();
    return QSize( w, heightForWidth(w) );
}

void myImageView::resizeEvent(QResizeEvent * e)
{
    if(!pix.isNull()){
        QLabel::setPixmap(pix.scaled(this->size(), Qt::KeepAspectRatio, Qt::SmoothTransformation));
    }
}
