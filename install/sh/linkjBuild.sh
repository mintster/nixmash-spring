#!/bin/bash 

# ---------------------------------------------------------------------- */
# Create path to NixMash Spring File Storage Path - 4/21/2016
# 
# -- Used to point to physical storage location from IDE Build folder
# -- Creates Soft Link in classes /main/static folder to physical location
#
# ---------------------------------------------------------------------- */

# Variables ------------------------------------------------------------- */

# $BUILDPATH - /static folder in IDE build directory for soft link
# $STORAGEPATH - physical location of file storage area
# $LINKNAME - name of soft link to storage area. Same name as storage root

# Set Variables ------------------------------------------------- */

BUILDPATH="/ubuntuland/projects/nixmash-spring/mvc/build/classes/main/static"
STORAGEPATH="/ubuntuland/sites/nixmashspring/files"
LINKNAME="files"


if [ "$1" != "" ]; then
     if [[ "$1" = "--create" ]]
	then
	   ln -s $STORAGEPATH $BUILDPATH 
	   echo "SOFTLINK TO PHYSICAL FILES LOCATION CREATED"
	elif [[ "$1" = "--rm" ]]
	then
	   rm $BUILDPATH/$LINKNAME
	   echo "SOFTLINK TO PHYSICAL FILES LOCATION REMOVED"
     fi
else
     echo "Use --create to create soft link, --rm to remove"
fi



