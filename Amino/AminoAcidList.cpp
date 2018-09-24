#include "AminoAcidList.h"

AminoAcidList *AminoAcidList::s_instance = 0;

AminoAcidList::AminoAcidList(void)
{
}

AminoAcidList* AminoAcidList::instance()
{
	if (!s_instance)
		s_instance = new AminoAcidList;
	return s_instance;
}

AminoAcidList::~AminoAcidList(void)
{
}
