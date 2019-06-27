#!/bin/bash

# Get a full path of a file for startup confirmation.
SCRIPT_DIR=$(cd $(dirname $0); pwd)

. ${SCRIPT_DIR}/lib/fc_lib.sh

#################################
HOST=1.1.1.1
PORT=80
#################################

TIMEOUT=30
SECONDS=0

while :
do
  if [ $SECONDS -ge $TIMEOUT ]; then
    log "There are asynchronous operations being executed."
    break;
  fi
  ret=`curl -s $HOST:$PORT/v1/operations?format=detail-list`

  if [ $? = $SUCCESS ]; then
    statusValue=`echo $ret | python -mjson.tool | grep "\"status\":" | sed -r 's/^ *//;s/.*: *"//;s/",?//'`
    if [[ ! `echo $statusValue | grep 'executing'` ]]; then
      log "There are no asynchronous operations being executed."
      break
    fi
  else
    log "The operation to acquire operation list failed."
    break;
  fi
  sleep 10
  log "Retry"
done
