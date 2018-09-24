#include "AminoAcid.h"

AminoAcid::AminoAcid( void )
{
}

void AminoAcid::setId( int aaId )
{
	id = aaId;
}

void AminoAcid::appendName( QString name )
{
	nameList.append( name );
}

void AminoAcid::appendProperty( QString prperty )
{
	propertyList.append( prperty );
}

int AminoAcid::getId()
{
	return id;
}

QString AminoAcid::getNameAt(int index )
{
	return nameList.at( index );
}

QString AminoAcid::getPropertyAt( int index )
{
	return propertyList.at( index );
}

AminoAcid::~AminoAcid( void )
{
}
