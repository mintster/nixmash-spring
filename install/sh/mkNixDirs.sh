#!/bin/bash

# ---------------------------------------------------------------------- */
# Create NixMash Spring Website Directories - 9/19/2016
#
#		nixmashspring/
#		nixmashspring/files
#		nixmashspring/files/flashcards
#		nixmashspring/files/demo
#		nixmashspring/files/demo/images
#		nixmashspring/files/posts
#		nixmashspring/files/posts/images
#		nixmashspring/files/users
#		nixmashspring/files/users/icons
#		nixmashspring/files/users/avatars
#		nixmashspring/x
#		nixmashspring/x/html
#		nixmashspring/x/pics
#		nixmashspring/x/posts
#		nixmashspring/x/posts/2016
#		nixmashspring/x/css
#
# ---------------------------------------------------------------------- */

# Variables ------------------------------------------------------------- */

# $1 - root folder of website



if [ "$1" != "" ]; then
    mkdir -p $1/files/{users/{icons,avatars},posts/images,demo/images,flashcards}
    mkdir -p $1/x/{html,pics,posts/2016,css}
else
     echo "Enter site root path with command"
      echo "DO NOT INCLUDE FINAL SLASH"
      echo "Example: $ mkNixDirs.sh /sites/mysite"
fi
