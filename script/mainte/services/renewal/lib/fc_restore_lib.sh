#!/bin/bash

# Library to restore the FC from the backup.
#
# Reference variables
#   BACKUP_HOST_ADDR
#   BACKUP_HOST_USER
#   BACKUP_HOST_PASS
#   BACKUP_ARCHIVE_FILE_PATH
#   RESTORE_HOST_ADDR
#   RESTORE_HOST_USER
#   RESTORE_HOST_PASS
#   RESTORE_PATH
#   REPLACE_CONF
function fcRestore() {
  local retCode=$SUCCESS

  checkArg BACKUP_HOST_ADDR \
	BACKUP_HOST_USER \
	BACKUP_HOST_PASS \
	BACKUP_ARCHIVE_FILE_PATH \
	RESTORE_HOST_ADDR \
	RESTORE_HOST_USER \
	RESTORE_HOST_PASS \
	RESTORE_PATH \
	REPLACE_CONF

  if [ $? = $SUCCESS ]; then
    log "There are no problems with setting values."

  if [[ $RESTORE_PATH =~ /$ ]] ;then
    RESTORE_PATH=${RESTORE_PATH%/}
  fi


    local tmpDir=TMP.$$.$(date +%s)
    `mkdir $tmpDir`

    if [ $? = $SUCCESS ]; then
      log "Succeeded to create a temporary folder."
      `sshpass -p $BACKUP_HOST_PASS scp -o StrictHostKeyChecking=no $BACKUP_HOST_USER@$BACKUP_HOST_ADDR:$BACKUP_ARCHIVE_FILE_PATH $tmpDir`

      if [ $? = $SUCCESS ]; then
        log "Succeeded to the SCP command."

        local archive=`basename $BACKUP_ARCHIVE_FILE_PATH`
        pushd "$tmpDir"
        `tar xfz $archive`
        local tmpRet=$?
        popd

        if [ $tmpRet = $SUCCESS ]; then
          log "Succeeded to extract files from the backup file($archive)."
          pushd "$tmpDir/conf"
          replaceConf ../../ $REPLACE_CONF
          tmpRet=$?
          popd

          if [ $tmpRet = $SUCCESS ]; then
            log "Succeeded to modify the config file."
            local fcrBackupPath=$RESTORE_PATH.`date +%Y%m%d%H%M%S`

            expect -c "
              source lib/fc_lib.exp
              set result [open_conn $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS]
              send_cmd \"mv $RESTORE_PATH $fcrBackupPath\"
              send_cmd \"mkdir $RESTORE_PATH\"
              close_conn
            "
            if [ $? = $SUCCESS ]; then
              log "Succeeded to save the FC directory."
              `sshpass -p $RESTORE_HOST_PASS scp -o StrictHostKeyChecking=no -r $tmpDir/* $RESTORE_HOST_USER@$RESTORE_HOST_ADDR:$RESTORE_PATH`

              if [ $? = $SUCCESS ]; then
                log "Succeeded to restore($RESTORE_HOST_ADDR:$RESTORE_PATH)."
                rm -rf $tmpDir

                log "Restore of the controller was successfully completed."
              else
                log "Failed to restore the controller."
                retCode=$FAIL
              fi
            else
              log "Failed to save the FC directory."
              retCode=$FAIL
            fi
          else
            log "Failed to modify the config file."
            retCode=$FAIL
          fi
        else
          log "Failed to extract files from the backup file."
          retCode=$FAIL
        fi
      else
        log "Failed to transfer the controller file."
        retCode=$FAIL
      fi
    else
      log "Failed to create a temporary folder."
      retCode=$FAIL
    fi
  else
    log "There are some problems with setting values."
    retCode=$FAIL
  fi
  return $retCode
}
