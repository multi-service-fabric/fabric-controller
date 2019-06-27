#!/bin/bash
#
# Compare old and new config files, and if the keys in the 
# old file are different from the ones in the new file, 
# output replace_conf to apply these changes.
# This tool uses xmlstalet and java.
#

# Setting value
#################################
old="oldfc"
new="newfc"
out="re.out"
#################################

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries for FC
. ${SCRIPT_DIR}/lib/fc_lib.sh

outFlag=0

# arg1：Old XML
# arg2：New XML
# arg3：Output file
function makeDiff {
  local oldXml=$1
  local newXml=$2
  local output=$3

  local tmpFile1=$(mktemp)

  log "Compare：old = $oldXml new = $newXml"
  if [ -e $newXml ]; then
    `java -jar lib/xpath.jar $oldXml > $tmpFile1`
    while read line
    do
      local value_=`xmlstarlet sel -t -v $line $oldXml 2> /dev/null | tr '\n' '_'`
      if [[ ! "${value_}" =~ ^_ ]]; then
        local valueOld=`xmlstarlet sel -t -v $line $oldXml 2> /dev/null`
        local valueNew=`xmlstarlet sel -t -v $line $newXml 2> /dev/null`
        if [[ ! -z $valueNew ]]; then
          if [[ "$valueOld" != "$valueNew" ]]; then
            echo "XML \"$line\" \"$valueOld\" $newXml" >> $output
            outFlag=1
          fi
        fi
      fi
    done < $tmpFile1
  else
    log "$newXml does not exist."
  fi
}

for file in `find $old/conf -type f`; do
  if [ ${file##*.} = "xml" ]; then
    makeDiff $file ${file/$old/$new} $out
  fi
done

if [ $outFlag = 0 ]; then
  log "There are no differences in the configuration files."
fi
