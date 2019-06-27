#!/bin/bash

# Return code
SUCCESS=0
FAIL=1
TIMEOUT=9

# The name of log file is "<executed script name>.log".
LOGFILE="../logs/${0##*/}.log"

# It outputs logs to LOGFILE and console.
# Console log is output in red.
# Rotate log files by 1M and keep 3 generations (xxx.log, xxx.log.1, xxx.log.2).
function log() {
  message="$(date '+%Y-%m-%d %H:%M:%S') : $@"
  echo -e "\e[31m$message\e[m"
  echo -e "$message" | rotatelogs -n 3 ${LOGFILE} 1M
}


retCode=$SUCCESS

crmMon=`sudo crm_mon -A1`

nodeStatus=`echo "$crmMon" | grep prmFC | grep heartbeat | awk '{print $3}'`
actNode=`echo "$crmMon" | grep prmFC | grep heartbeat | awk '{print $4}' | sed -e "s/[\r\n]\+//g"`

if [ -z $nodeStatus ] || [ -z $actNode ]; then
  log "There is no FC running."
  retCode=$FAIL
else
  if [ $nodeStatus = "Started" ]; then
    onlineNode=`echo "$crmMon" | grep Online: | sed -e "s/Online: \[\(.*\)\]/\1/"`
    host1=`echo "$onlineNode" | awk '{print $1}'`
    host2=`echo "$onlineNode" | awk '{print $2}'`
    if [ -z $host1 ] || [ -z $host2 ]; then
      log "There is no FC to switch over."
      retCode=$FAIL
    else
      log "Do switch over."
      `sudo crm_resource -M -r grpFC`
      log "The flag to prohibit movement is set to FC."
      log "It is necessary to execute [% sudo crm_resource -U -r grpFC] to reset the flag to prohibit movement."

      SECONDS=0
      while :
      do
        if [ $SECONDS -ge 60 ]; then
          retCode=$TIMEOUT
          log "Time out."
          break
        fi
        crmMon=`sudo crm_mon -A1`
        if [ $? = $SUCCESS ]; then
          newActNode=`echo "$crmMon" | grep prmFC | grep heartbeat | awk '{print $4}' | sed -e "s/[\r\n]\+//g"`

          if [[ ! -z $newActNode ]] && [[ $newActNode != $actNode ]]; then
            log "Switch over $newActNode."
            break
          fi
        fi
        sleep 5
        log "Recheck system switching."
      done
    fi
  fi
fi

exit $retCode

