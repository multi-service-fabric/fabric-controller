#!/bin/bash

# Setting value
#################################
BACKUP_HOST_ADDR=1.1.1.1
BACKUP_HOST_USER=username
BACKUP_HOST_PASS=password
BACKUP_ARCHIVE_FILE_PATH=/tmp/backup/fcBackup_YYYYMMDDhhmmss.tar.gz
RESTORE_HOST_ADDR=1.1.1.1
RESTORE_HOST_USER=username
RESTORE_HOST_PASS=password
RESTORE_PATH=/tmp/restore
REPLACE_CONF=replace_conf
#################################

# Get a full path of a file for startup confirmation
SCRIPT_DIR=$(cd $(dirname $0); pwd)

# Load library for FC
. ${SCRIPT_DIR}/lib/fc_lib.sh
. ${SCRIPT_DIR}/lib/fc_restore_lib.sh

fcRestore
