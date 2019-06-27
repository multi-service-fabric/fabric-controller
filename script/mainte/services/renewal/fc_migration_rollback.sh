#!/bin/bash

# Setting value (FC restore file)
#################################
BACKUP_HOST_ADDR=1.1.1.1
BACKUP_HOST_USER=username
BACKUP_HOST_PASS=password
ARCHIVE=/home/msfctrl/backup/FabricController_yyyymmdd_hhmmss.tar.gz
BACKUP_ARCHIVE_FILE_PATH=$ARCHIVE
RESTORE_HOST_ADDR=1.1.1.1
RESTORE_HOST_USER=username
RESTORE_HOST_PASS=password
RESTORE_PATH=/home/msfctrl/msf-controller
REPLACE_CONF=replace_conf_rollback
#################################

# Setting value (New DB deletion)
#################################
RESTORE_DB_HOST_ADDR=1.1.1.1
RESTORE_DB_HOST_USER=username
RESTORE_DB_HOST_PASS=password
RESTORE_DB_NAME=new_dbname
RESTORE_DB_USER=db_username
#################################

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/fc_restore_lib.sh
. ${SCRIPT_DIR}/lib/pacemaker_lib.sh

retCode=$SUCCESS

checkArg RESTORE_DB_HOST_ADDR \
      RESTORE_DB_HOST_USER \
      RESTORE_DB_HOST_PASS \
      RESTORE_DB_NAME \
      RESTORE_DB_USER

if [ $? = $SUCCESS ]; then

  getStandbyNode
  if [ $? = $SUCCESS ]; then
    log "Succeeded to acquire information of the Pacemaker."
    if [ -z $STANDBY_NODE ]; then
      log "There are no standby nodes."
      getOnlineNode
      if [ $? = $SUCCESS ]; then
        log "Succeeded to get online node information."
        if [ -n $ONLINE_HOST ]; then
          toStandby $ONLINE_HOST
          if [ $? = $SUCCESS ]; then
            log "Executed the process to change the node ($ONLINE_HOST) to the standby state."
            checkStandby $ONLINE_HOST 30
          else
            log "Failed to change the node to the standby state."
            retCode=$FAIL
          fi
        else
          log "Failed to change the node to the standby state."
          retCode=$FAIL
        fi
      else
        log "Failed to get online node information."
        retCode=$FAIL
      fi
    fi
    if [ $retCode = $FAIL ]; then
      exit $retCode
    fi

    # Restore controller file
    fcRestore
    if [ $? = $SUCCESS ]; then
      log "Succeeded to restore controller file."

      dbList=`expect -c "
        source lib/fc_lib.exp
        remote_cmd $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS \
          \"psql -U $RESTORE_DB_USER -l\"
        " | grep " $RESTORE_DB_NAME "`
      echo $dbList

      if [[ ! -z $dbList ]]; then
        log "Database has been already created."
        expect -c "
          source lib/fc_lib.exp
          remote_cmd $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS \
            \"dropdb -U $RESTORE_DB_USER $RESTORE_DB_NAME\"
        "
        if [ $? = $SUCCESS ]; then
          log "Succeeded to delete the DB."
        else
          log "Failed to delete the DB."
          retCode=$FAIL
        fi
      else
        log "Database hasn't been created yet."
      fi

      if [ $retCode = $FAIL ]; then
        exit $retCode
      fi

      getStandbyNode
      if [ $? = $SUCCESS ]; then
        log "Succeeded to acquire information of the Pacemaker."

        if [ ! -z $STANDBY_NODE ]; then
          toOnline $STANDBY_NODE

          if [ $? = $SUCCESS ]; then
            log "Executed the process to change the node ($STANDBY_NODE) to the online state."
            checkOnline $STANDBY_NODE 30

            if [ $? = $SUCCESS ]; then
              log "Changing the node to the online state has been completed normally."
              log "The rollback process was completed normally."
            else
              log "Failed to change the node to the online state."
              retCode=$FAIL
            fi
          else
            log "Failed to change the node to the online state."
            retCode=$FAIL
          fi
        else
          log "There are no standby nodes."
          retCode=$FAIL
        fi
      else
        log "Failed to acquire information of the Pacemaker."
        retCode=$FAIL
      fi
    else
      log "Failed to restore controller file."
      retCode=$FAIL
    fi
  else
    log "Failed to acquire information of the Pacemaker."
    retCode=$FAIL
  fi
else
  log "There are some problems with setting value."
  retCode=$FAIL
fi
exit $retCode

