#!/bin/bash

sbyIp=$1

if [ -z $sbyIp ]; then
  exit 1
else
  tmpFile=tmp.$$.$(date +%s)
  expect -c "
    source ./lib/fc_lib.exp
    remote_cmd $sbyIp \
      \"df -k > $tmpFile\"
  " > /dev/null
  scp $sbyIp:$tmpFile ./ > /dev/null

  expect -c "
    source ./lib/fc_lib.exp
    remote_cmd $sbyIp \
      \"rm -f $tmpFile\"
  " > /dev/null
  cat $tmpFile

  rm -f $tmpFile
fi
