#!/bin/bash

# Setting value
#################################
BACKUP_TARGET_HOST_ADDR=1.1.1.1
BACKUP_TARGET_HOST_USER=username
BACKUP_TARGET_HOST_PASS=password
BACKUP_TARGET_PATH=/target/fc
BACKUP_DESTINATION_HOST_ADDR=1.1.1.1
BACKUP_DESTINATION_HOST_USER=username
BACKUP_DESTINATION_HOST_PASS=password
BACKUP_DESTINATION_PATH=/tmp/backup
#################################

outFile=fcBackup_`date +%Y%m%d_%H%M%S`.tar.gz

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load libraries for FC
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/fc_backup_lib.sh

fcBackup $outFile
