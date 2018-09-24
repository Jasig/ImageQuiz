#include <QString>
#include <QStringList>

class AminoAcid
{
private:
   int id;
   QStringList nameList;
   QStringList propertyList;

public:
   AminoAcid( void );

   void setId( int );
   void appendName( QString );
   void appendProperty( QString );

   int getId();
   QString getNameAt( int );
   QString getPropertyAt( int );

   ~AminoAcid( void );
};
