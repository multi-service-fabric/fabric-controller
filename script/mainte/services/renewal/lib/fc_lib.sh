#!/bin/bash

# Return code
SUCCESS=0
FAIL=1
TIMEOUT=9

# Check if all arguments are defined.
function checkArg() {
  local i
  for i in `seq 1 ${#}`
  do
    if [ ! -n "${!1}" ]; then
      log "$1 is undefined."
      return $FAIL
    fi
    shift
  done
  return $SUCCESS
}

# Get a full path of a file for startup confirmation
SCRIPT_DIR_=$(cd $(dirname $0); pwd)

# "script name".log.
LOGFILE="${SCRIPT_DIR_}/${0##*/}.log"

# Output log to LOGFILE and console.
function log() {
  local message="$(date '+%Y-%m-%d %H:%M:%S') : $@"
  echo -e "\e[31m$message\e[m"
  echo -e "$message" | rotatelogs -n 3 ${LOGFILE} 1M
}

# Execute the command as the first argument, output a result to the file as the second argument.
function outputFile() {
  {
  eval $1
  } > $2
}

# First argument: Directory of replace_conf, the second argument: replace_conf
# Modify the config file according to the contents of the file.
function replaceConf() {
  local confDir=$1
  local replaceConfFile=$2
  local lineNo=0
  local tmpFile1=$(mktemp)
  local tmpFile2=$(mktemp)

  local retCode=$SUCCESS

  if [ -f $confDir$replaceConfFile ]; then
    while read line
    do
      lineNo=$((lineNo += 1))
      ary=(`echo $line`)
      if [ ${ary[0]} = "XML" ]; then
        outputFile "xmlstarlet fo ${ary[3]}" $tmpFile1
        outputFile "xmlstarlet edit --update ${ary[1]} --value ${ary[2]} ${ary[3]}" $tmpFile2
        diffResult=`diff $tmpFile1 $tmpFile2`
        if [ -z "$diffResult" ]; then
          log "$replaceConfFile $lineNo Line : convert error. Confirm $replaceConfFile file."
          retCode=$FAIL
        else
          log "$replaceConfFile $lineNo Line : convert ok."
          `rm ${ary[3]}`
          `mv $tmpFile2 ${ary[3]}`
        fi
      elif [ ${ary[0]} = "PRP" ]; then
        key=`echo ${ary[1]} | sed 's/"//g'`
        val=`echo ${ary[2]} | sed 's/"//g'`
        awk -v pat="^$key[ \t]*=" -v value="$key=$val" '{ if ($0 ~ pat) print value; else print $0; }' "${ary[3]}" > $tmpFile1
        diffResult=`diff $tmpFile1 ${ary[3]}`
        if [ -z "$diffResult" ]; then
          log "$replaceConfFile $lineNo Line : convert error. Confirm $replaceConfFile file."
          retCode=$FAIL
        else 
          log "$replaceConfFile $lineNo Line : convert ok."
          `rm ${ary[3]}`
          `mv $tmpFile1 ${ary[3]}`
        fi
      fi
    done < $confDir$replaceConfFile
  else
   log "$confDir$replaceConfFile file is not found."
   retCode=$FAIL
  fi

  `rm -f $tmpFile1 $tmpFile2`
  return $retCode
}

