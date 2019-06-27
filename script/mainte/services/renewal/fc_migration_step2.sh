#!/bin/bash

# Setting value (FC backup)
#################################
BACKUP_TARGET_HOST_ADDR=1.1.1.1
BACKUP_TARGET_HOST_USER=username
BACKUP_TARGET_HOST_PASS=password
BACKUP_TARGET_PATH=/home/msfctrl/msf-controller
BACKUP_DESTINATION_HOST_ADDR=1.1.1.1
BACKUP_DESTINATION_HOST_USER=username
BACKUP_DESTINATION_HOST_PASS=password
BACKUP_DESTINATION_PATH=/home/msfctrl/backup
#################################

# Setting value (FC restore)
#################################
BACKUP_HOST_ADDR=$BACKUP_DESTINATION_HOST_ADDR
BACKUP_HOST_USER=$BACKUP_DESTINATION_HOST_USER
BACKUP_HOST_PASS=$BACKUP_DESTINATION_HOST_PASS
BACKUP_ARCHIVE_FILE_PATH=/home/msfctrl/release/msf-controller-new.tar.gz
RESTORE_HOST_ADDR=$BACKUP_TARGET_HOST_ADDR
RESTORE_HOST_USER=$BACKUP_TARGET_HOST_USER
RESTORE_HOST_PASS=$BACKUP_TARGET_HOST_PASS
RESTORE_PATH=$BACKUP_TARGET_PATH
REPLACE_CONF=replace_conf_step2
#################################

ARCHIVE=FabricController_`date +%Y%m%d_%H%M%S`.tar.gz

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/fc_backup_lib.sh
. ${SCRIPT_DIR}/lib/fc_restore_lib.sh
. ${SCRIPT_DIR}/lib/pacemaker_lib.sh

retCode=$SUCCESS

getStandbyNode
if [ $? = $SUCCESS ]; then
  log "Succeeded to acquire information of the Pacemaker."
  if [ -z $STANDBY_NODE ]; then
    log "There are no standby nodes."
    getOnlineNode
    if [ $? = $SUCCESS ]; then
      log "Succeeded to acquire online host information."
      if [ -n ONLINE_HOST ]; then
        toStandby $ONLINE_HOST
        if [ $? = $SUCCESS ]; then
          log "Executed the process to change the node ($ONLINE_HOST) to the standby state."
          checkStandby $ONLINE_HOST 30
          if [ $? = $SUCCESS ]; then
            log "Succeeded to change the node to the standby state."
            STANDBY_NODE=$ONLINE_HOST
          else
            log "Failed to change the node to the standby state."
            retCode=$FAIL
          fi
        else
          log "Failed to change the node to the standby state."
          retCode=$FAIL
        fi
      else
        log "Failed to change the node to the standby state."
        retCode=$FAIL
      fi
    else
      log "Failed to acquire online host information."
      retCode=$FAIL
    fi
  fi

  if [ $retCode = $FAIL ]; then
    exit $retCode
  fi

  # Backup controller file
  fcBackup $ARCHIVE
  if [ $? = $SUCCESS ]; then
    log "Succeeded to backup controller file."
    # Restore controller file
    fcRestore
    if [ $? = $SUCCESS ]; then
      log "Succeeded to restore controller file."
      toOnline $STANDBY_NODE
      if [ $? = $SUCCESS ]; then
        log "Executed the process to change the node to the online state."
        checkOnline $STANDBY_NODE 30
        if [ $? = $SUCCESS ]; then
          log "FC migration script Step 2 completed normally."
        else
          log "Failed to change the node to the online state."
          retCode=$FAIL
        fi
      else
        log "Failed to change the node to the online state."
        retCode=$FAIL
      fi
    else
      log "Failed to restore controller file."
      retCode=$FAIL
    fi
  else
    log "Failed to backup controller file."
    retCode=$FAIL
  fi
else
  log "Failed to acquire information of the Pacemaker."
  retCode=$FAIL
fi
exit $retCode
