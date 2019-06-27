#!/bin/bash

# Setting value
#################################
RESTORE_DB_HOST_ADDR=1.1.1.1
RESTORE_DB_HOST_USER=username
RESTORE_DB_HOST_PASS=password
RESTORE_DB_NAME=dbname
RESTORE_DB_USER=username
BACKUP_HOST_ADDR=1.1.1.1
BACKUP_HOST_USER=username
BACKUP_HOST_PASS=password
BACKUP_PATH=/tmp/backup
OUT_FILE=dbBackup.sql
#################################

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries for FC
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/db_restore_lib.sh

dbRestore ${OUT_FILE}
