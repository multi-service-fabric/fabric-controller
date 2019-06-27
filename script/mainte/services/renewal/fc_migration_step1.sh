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
REPLACE_CONF=replace_conf_step1
#################################

# Setting value (DB backup)
#################################
BACKUP_DB_HOST_ADDR=1.1.1.1
BACKUP_DB_HOST_USER=username
BACKUP_DB_HOST_PASS=password
BACKUP_DB_NAME=old_dbname
BACKUP_DB_USER=db_username
BACKUP_PATH=$BACKUP_DESTINATION_PATH
#################################

# Setting value (DB restore)
#################################
RESTORE_DB_HOST_ADDR=$BACKUP_DB_HOST_ADDR
RESTORE_DB_HOST_USER=$BACKUP_DB_HOST_USER
RESTORE_DB_HOST_PASS=$BACKUP_DB_HOST_PASS
RESTORE_DB_NAME=new_dbname
RESTORE_DB_USER=$BACKUP_DB_USER
ALTER_SQL=fc_migration_MSF2018BtoMSF2018B.sql
#ALTER_SQL=fc_migration_MSF2017toMSF2018B.sql
#ALTER_SQL=fc_migration_MSF2018AtoMSF2018B.sql
#################################

ARCHIVE=FabricController_`date +%Y%m%d_%H%M%S`.tar.gz
BACKUP_SQL=dbBackup_`date +%Y%m%d%H%M%S`.sql
SET_RENEWAL_SQL=fc_set_renewal.sql

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/fc_backup_lib.sh
. ${SCRIPT_DIR}/lib/fc_restore_lib.sh
. ${SCRIPT_DIR}/lib/db_backup_lib.sh
. ${SCRIPT_DIR}/lib/db_restore_lib.sh
. ${SCRIPT_DIR}/lib/pacemaker_lib.sh

retCode=$SUCCESS

getOnlineNode
if [ $? = $SUCCESS ]; then
  log "Online node information acquisition succeeded."
  if [ ! -z $ONLINE_HOST ]; then
    toStandby $ONLINE_HOST
    if [ $? = $SUCCESS ]; then
      log "Executed the process to change the node ($ONLINE_HOST) to the standby state."
      checkStandby $ONLINE_HOST 30
      if [ $? = $SUCCESS ]; then
        log "Change the node to the standby state was successfully completed."
        # Backup of the controller file
        fcBackup $ARCHIVE
        if [ $? = $SUCCESS ]; then
          log "Backup of the controller file was successfully completed."
          # Restore of the controller file
          fcRestore
          if [ $? = $SUCCESS ]; then
            log "Succeeded to restore the controller file."
            # DB backup
            dbBackup $BACKUP_SQL
            if [ $? = $SUCCESS ]; then
              log "Succeeded to back up the DB."
              # DB restore
              dbRestore $BACKUP_SQL
              if [ $? = $SUCCESS ]; then
                log "Succeeded to restore the DB."
                `sshpass -p $RESTORE_DB_HOST_PASS scp -o StrictHostKeyChecking=no $ALTER_SQL $RESTORE_DB_HOST_USER@$RESTORE_DB_HOST_ADDR:.`
                if [ $? = $SUCCESS ]; then
                  log "Succeeded to transfer the DB-update SQL."
                  expect -c "
                    source lib/fc_lib.exp
                    set result [open_conn $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS]
                    send_cmd \"psql -U $RESTORE_DB_USER $RESTORE_DB_NAME < $ALTER_SQL\"
                    send_cmd \"rm -f $ALTER_SQL\"
                    close_conn
                  "
                  if [ $? = $SUCCESS ]; then
                    log "Succeeded to update the DB schema."
                    `sshpass -p $RESTORE_DB_HOST_PASS scp -o StrictHostKeyChecking=no $SET_RENEWAL_SQL $RESTORE_DB_HOST_USER@$RESTORE_DB_HOST_ADDR:.`
                    if [ $? = $SUCCESS ]; then
                      log "Succeeded to transfer the renewal status update SQL."
                      expect -c "
                        source lib/fc_lib.exp
                        set result [open_conn $RESTORE_DB_HOST_USER $RESTORE_DB_HOST_ADDR $RESTORE_DB_HOST_PASS]
                        send_cmd \"psql -U $RESTORE_DB_USER $RESTORE_DB_NAME < $SET_RENEWAL_SQL\"
                        send_cmd \"rm -f $SET_RENEWAL_SQL\"
                        close_conn
                      "
                      if [ $? = $SUCCESS ]; then
                        log "Succeeded to update DB to the renewal status."
                        toOnline $ONLINE_HOST

                        if [ $? = $SUCCESS ]; then
                          log "Executed the process to change the node ($ONLINE_HOST) to online."
                          checkOnline $ONLINE_HOST 30

                          if [ $? = $SUCCESS ]; then
                            log "FC migration script Step 1 completed normally."
                          else
                            log "Failed to change the node to online."
                            retCode=$FAIL
                          fi
                        else
                          log "Failed to change the node to online."
                          retCode=$FAIL
                        fi
                      else
                        log "Failed to update DB to the renewal status."
                        retCode=$FAIL
                      fi
                    else
                      log "Failed to transfer the renewal status update SQL."
                      retCode=$FAIL
                    fi
                  else
                    log "Faild to update the DB schema."
                    retCode=$FAIL
                  fi
                else
                  log "Failed to transfer the DB-update SQL."
                  retCode=$FAIL
                fi
              else
                log "Failed to restore the DB."
                retCode=$FAIL
              fi
            else
              log "Failed to back up the DB."
              retCode=$FAIL
            fi
          else
            log "Failed to restore the controller file."
            retCode=$FAIL
          fi
        else
          log "Failed to back up the controller file."
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
    log "There are no nodes online with FC not running."
    retCode=$FAIL
  fi
else
  log "Online node information acquisition failed."
  retCode=$FAIL
fi
exit $retCode
