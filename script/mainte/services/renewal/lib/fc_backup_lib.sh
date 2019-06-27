#!/bin/bash

# Library to back up the FC.
#
# Reference variables
#  BACKUP_TARGET_HOST_ADDR \
#  BACKUP_TARGET_HOST_USER \
#  BACKUP_TARGET_HOST_PASS \
#  BACKUP_TARGET_PATH \
#  BACKUP_DESTINATION_HOST_ADDR \
#  BACKUP_DESTINATION_HOST_USER \
#  BACKUP_DESTINATION_HOST_PASS \
#  BACKUP_DESTINATION_PATH
function fcBackup() {
  local retCode=$SUCCESS
  local archive=$1

  checkArg BACKUP_TARGET_HOST_ADDR \
	BACKUP_TARGET_HOST_USER \
	BACKUP_TARGET_HOST_PASS \
	BACKUP_TARGET_PATH \
	BACKUP_DESTINATION_HOST_ADDR \
	BACKUP_DESTINATION_HOST_USER \
	BACKUP_DESTINATION_HOST_PASS \
	BACKUP_DESTINATION_PATH

  if [ $? = $SUCCESS ]; then
    log "There are no problems with the setting values."

  if [[ ! ${BACKUP_DESTINATION_PATH} =~ /$ ]] ;then
    BACKUP_DESTINATION_PATH=${BACKUP_DESTINATION_PATH}/
  fi

    local tmpDir=TMP.$$.$(date +%s)
    `mkdir $tmpDir`

    if [ $? = $SUCCESS ]; then
      `sshpass -p $BACKUP_TARGET_HOST_PASS scp -o StrictHostKeyChecking=no -r $BACKUP_TARGET_HOST_USER@$BACKUP_TARGET_HOST_ADDR:$BACKUP_TARGET_PATH/* $tmpDir`

      if [ $? = $SUCCESS ]; then
        `ls $tmpDir/bin`

        if [ $? = $SUCCESS ]; then
          log "Succeeded to execute SCP command."
          pushd "$tmpDir"
          `tar cfz $archive *`

          if [ $? = $SUCCESS ]; then
            log "Succeeded to archive($archive)."
            `sshpass -p $BACKUP_DESTINATION_HOST_PASS scp -o StrictHostKeyChecking=no $archive $BACKUP_DESTINATION_HOST_USER@$BACKUP_DESTINATION_HOST_ADDR:$BACKUP_DESTINATION_PATH`

            if [ $? = $SUCCESS ]; then
              log "Succeeded to backup($BACKUP_DESTINATION_HOST_ADDR:$BACKUP_DESTINATION_PATH$archive)."
              popd
              rm -rf $tmpDir
              log "Backup of the controller was successfully completed."
            else
              log "Failed to back up the controller."
              retCode=$FAIL
            fi
          else
            log "Failed to archive."
            retCode=$FAIL
          fi
        else
          log "Failed to back up the controller."
          retCode=$FAIL
        fi
      else
        log "Failed to back up the controller."
        retCode=$FAIL
      fi

      `ls $tmpDir > /dev/null 2>&1`
      if [ $? = $SUCCESS ]; then
        rm -rf $tmpDir
        if [ $? = $SUCCESS ]; then
          log "Deleted the temporary folder."
        else
          log "Failed to delete the temporary folder."
          retCode=$FAIL
        fi
      fi
    else
      log "Failed to create a temporary folder."
      retCode=$FAIL
    fi

  else
    log "There are some problems with the setting values."
    retCode=$FAIL
  fi
  return $retCode
}
