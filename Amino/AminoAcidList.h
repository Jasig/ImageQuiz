#include <QList>
#include "AminoAcid.h"

class AminoAcidList
{
   AminoAcidList(void);
   static AminoAcidList *s_instance;

public:
   static AminoAcidList* instance();
   QList< AminoAcid > aminoAcidList;
   ~AminoAcidList(void);
};
