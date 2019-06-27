#!/bin/bash

# Setting value
#################################
BACKUP_DB_HOST_ADDR=1.1.1.1
BACKUP_DB_HOST_USER=username
BACKUP_DB_HOST_PASS=password
BACKUP_DB_NAME=dbname
BACKUP_DB_USER=username
BACKUP_HOST_ADDR=1.1.1.1
BACKUP_HOST_USER=username
BACKUP_HOST_PASS=password
BACKUP_PATH=/tmp/backup/
#################################

outFile=dbBackup_`date +%Y%m%d%H%M%S`.sql

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries for FC
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/db_backup_lib.sh


dbBackup $outFile
