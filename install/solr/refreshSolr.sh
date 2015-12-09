#!/bin/bash 


# ---------------------------------------------------------------------- */
# UPDATE SOLR HTTP AND EMBEDDED COLLECTIONS FOR NIXMASH SPRING
# ---------------------------------------------------------------------- */

# NOTES ------------------------------------------------------------- */

# 1) Solr Version 4.10.4
# 2) Configure local Http Solr Server: 
#
#    Example used is http://solr/nixmashspring. Can be http://localhost:8983/solr/collection1 or whatever
#
# 3) This script will populate the Http Index from the docs subfolder and copy to embedded server
#    
#    A) Be sure Collection1/conf/schema.xml contains a doctype field, or
#    copy schema.xml from project nixmash_spring/install/solr
#
#    <field name="doctype" type="text_general" indexed="true" stored="true"/>
#
#    B) Use "collection1" as name of embedded Solr server collection as shown below
#
#
# ------------------------------------------------------------------ */

# VARIABLES ------------------------------------------------------------- */

# 1) HTTP_COLLECTION - File Path of Http Default Collection1
# 2) HTTP_URL - Url of Http Default Collection
# 3) EMBEDDED_COLLECTION - File Path of Embedded Collection1
#
# ------------------------------------------------------------------ */

HTTP_COLLECTION=/ubuntuland/utils/solr-4.10.4/home/collections/nixmashspring
HTTP_URL=http://solr/nixmashspring/update?commit=true
EMBEDDED_COLLECTION=/home/daveburke/web/solr-4.10.4/collection1

# Clear existing index for Collection1 ----------------------------- */

curl $HTTP_URL/update?commit=true  -H "Content-Type: text/xml" --data-binary '<delete><query>*:*</query></delete>'

# Populate Collection -------------------------------------------- */

java -Dtype=text/csv -Durl=$HTTP_URL -jar post.jar docs/*.csv
java -Dtype=application/json -Durl=$HTTP_URL -jar post.jar docs/*.json
java -Durl=$HTTP_URL -jar post.jar docs/*.xml

# Delete Embedded Collection and Copy from Http --------------------- */

rm -rf $EMBEDDED_COLLECTION/data
cp -r $HTTP_COLLECTION/data/ $EMBEDDED_COLLECTION/data/


