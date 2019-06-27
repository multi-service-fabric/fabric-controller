#!/bin/bash

# Library to restore the DB from the backup file.
#
# Reference variables
#  RESTORE_DB_HOST_ADDR
#  RESTORE_DB_HOST_USER
#  RESTORE_DB_HOST_PASS
#  RESTORE_DB_NAME
#  RESTORE_DB_USER
#  BACKUP_HOST_ADDR
#  BACKUP_HOST_USER
#  BACKUP_HOST_PASS
#  BACKUP_PATH
function dbRestore() {
  local retCode=$SUCCESS
  local outfile=$1

  checkArg RESTORE_DB_HOST_ADDR \
	RESTORE_DB_HOST_USER \
	RESTORE_DB_HOST_PASS \
	RESTORE_DB_NAME \
	RESTORE_DB_USER \
	BACKUP_HOST_ADDR \
	BACKUP_HOST_USER \
	BACKUP_HOST_PASS \
	BACKUP_PATH

  if [ $? = $SUCCESS ]; then
    log "There are no problems with setting values."
    `sshpass -p $BACKUP_HOST_PASS scp -o StrictHostKeyChecking=no $BACKUP_HOST_USER@$BACKUP_HOST_ADDR:$BACKUP_PATH/$outfile .`

    if [ $? = $SUCCESS ]; then
      log "Executed to get a backup file."
      `sshpass -p $RESTORE_DB_HOST_PASS scp -o StrictHostKeyChecking=no $outfile $RESTORE_DB_HOST_USER@$RESTORE_DB_HOST_ADDR:.`

      if [ $? = $SUCCESS ]; then
        log "Copy the backup file to the DB host."
        expect -c "
          source lib/fc_lib.exp
          set result [open_conn $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS]
          send_cmd \"dropdb -U $RESTORE_DB_USER --if-exist $RESTORE_DB_NAME\"
          send_cmd \"createdb -U $RESTORE_DB_USER $RESTORE_DB_NAME\"
          send_cmd \"psql -U $RESTORE_DB_USER $RESTORE_DB_NAME < $outfile\"
          close_conn
        "

        if [ $? = $SUCCESS ]; then
          log "Executed to restore the DB."
          expect -c "
            source lib/fc_lib.exp
            remote_cmd $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS \
              \"rm $outfile\"
          "
          `rm $outfile`

          log "DB Restore was completed($RESTORE_DB_HOST_ADDR:$RESTORE_DB_NAME)."
        else
          log "Failed to restore the DB."
          retCode=$FAIL
        fi
      else
        log "Failed to copy the backup file."
        retCode=$FAIL
      fi
    else
      log "Failed to get the backup file."
      retCode=$FAIL
    fi
  else
    log "There are some problems with setting values."
    retCode=$FAIL
  fi
  return $retCode
}
