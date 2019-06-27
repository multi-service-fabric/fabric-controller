#!/bin/bash

# Library to backup DB
# Output the archive file as first argument to the specified directory.
#
# Reference variables
#  BACKUP_DB_HOST_ADDR
#  BACKUP_DB_HOST_USER
#  BACKUP_DB_HOST_PASS
#  BACKUP_DB_NAME
#  BACKUP_DB_USER
#  BACKUP_HOST_ADDR
#  BACKUP_HOST_USER
#  BACKUP_HOST_PASS
#  BACKUP_PATH
function dbBackup() {
  local retCode=$SUCCESS
  local outfile=$1

  checkArg BACKUP_DB_HOST_ADDR \
	BACKUP_DB_HOST_USER \
	BACKUP_DB_HOST_PASS \
	BACKUP_DB_NAME \
	BACKUP_DB_USER \
	BACKUP_HOST_ADDR \
	BACKUP_HOST_USER \
	BACKUP_HOST_PASS \
	BACKUP_PATH

  if [ $? = $SUCCESS ]; then
    log "There are no problems with setting values."
    expect -c "
      source lib/fc_lib.exp
      remote_cmd $BACKUP_DB_HOST_USER $BACKUP_DB_HOST_ADDR $BACKUP_DB_HOST_PASS \
  	\"pg_dump -U $BACKUP_DB_USER $BACKUP_DB_NAME > $outfile\"
    "
    if [ $? = $SUCCESS ]; then
      log "Executed to back up the DB."
      `sshpass -p $BACKUP_DB_HOST_PASS scp -o StrictHostKeyChecking=no $BACKUP_DB_HOST_USER@$BACKUP_DB_HOST_ADDR:$outfile .`

      if [ $? = $SUCCESS ]; then
        log "Executed to get the backup file."
        `sshpass -p $BACKUP_HOST_PASS scp -o StrictHostKeyChecking=no $outfile $BACKUP_HOST_USER@$BACKUP_HOST_ADDR:$BACKUP_PATH/$outfile`

        if [ $? = $SUCCESS ]; then
          log "Saved the backup file to the backup host($BACKUP_HOST_ADDR:$BACKUP_PATH/$outfile)."
          expect -c "
            source lib/fc_lib.exp
            remote_cmd $BACKUP_DB_HOST_USER $BACKUP_DB_HOST_ADDR $BACKUP_DB_HOST_PASS \
  	      \"rm $outfile\"
          "

          if [ $? = $SUCCESS ]; then
            log "Executed to delete the backup file in the DB server."
            `rm $outfile`
            log "Backup of the DB was successfully completed."
          else
            log "Failed to delete the backup the in the DB server."
            retCode=$FAIL
          fi
        else
          log "Failed to save the backup file to the backup host."
          retCode=$FAIL
        fi
      else
        log "Failed to get the backup file."
        retCode=$FAIL
      fi
    else
      log "Failed to back up the DB."
      retCode=$FAIL
    fi
  else
    log "There are some problems with setting values."
    retCode=$FAIL
  fi
  return $retCode
}
