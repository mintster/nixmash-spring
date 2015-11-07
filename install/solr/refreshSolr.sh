#!/bin/bash 


# ---------------------------------------------------------------------- */
# UPDATE SOLR HTTP AND EMBEDDED COLLECTIONS FOR NIXMASH SPRING
# ---------------------------------------------------------------------- */

# NOTES ------------------------------------------------------------- */

# 1) Solr Version 4.10.4
# 2) Start in Solr Server root w/ Default Collection: 
#
#    $ bin/solr start -e default 
#
# 3) Be sure Collection1/conf/schema.xml contains a doctype field, or
#    copy schema.xml from project nixmash_spring/install/solr
#
#    <field name="doctype" type="text_general" indexed="true" stored="true"/>
#
# ------------------------------------------------------------------ */

# VARIABLES ------------------------------------------------------------- */

# 1) HTTP_COLLECTION - File Path of Http Default Collection1
# 2) HTTP_URL - Url of Http Default Collection
# 3) EMBEDDED_COLLECTION - File Path of Embedded Collection1
#
# ------------------------------------------------------------------ */

HTTP_COLLECTION=/ubuntuland/utils/solr-4.10.4/example/solr/collection1
HTTP_URL=http://localhost:8983/solr/collection1
EMBEDDED_COLLECTION=/home/daveburke/web/solr-4.10.4/collection1

# Clear existing index for Collection1 ----------------------------- */

curl $HTTP_URL/update?commit=true  -H "Content-Type: text/xml" --data-binary '<delete><query>*:*</query></delete>'

# Populate Collection -------------------------------------------- */

java -Dtype=text/csv -jar post.jar docs/*.csv
java -Dtype=application/json -jar post.jar docs/*.json
java -jar post.jar docs/*.xml

# Delete Embedded Collection and Copy from Http --------------------- */

rm -rf $EMBEDDED_COLLECTION/data
cp -r $HTTP_COLLECTION/data/ $EMBEDDED_COLLECTION/data/


