#!/bin/sh
# --------------------------------------------------
# Monster Trading Card Game LoreMaster Endpoint
# --------------------------------------------------

echo "Consult the LoreMaster"
curl -i -X GET http://localhost:10001/loremaster --header "Authorization: Bearer kienboec-mtcgToken"
echo .
curl -i -X GET http://localhost:10001/loremaster --header "Authorization: Bearer altenhof-mtcgToken"
echo .
echo .
