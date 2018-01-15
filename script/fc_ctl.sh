#!/bin/sh

# Location of config file (user variable constant)
# Specify the absolute path to the config directory 
# when the config files other than the files placed under the installation directory /conf is used.
# Installation directory /conf is used if it is unspecified (empty string).
readonly ANOTHER_CONF_DIR=""

# FC startup main class
readonly MAIN_CLASS="msf.fc.FabricController"

# FC application name
readonly APP_NAME="FabricController"

# Startup confirmation file
readonly PID_FILE="fc.output"

# Acquiring absolute path of the startup confirmation file
SCRIPT_DIR=$(cd $(dirname $0); pwd)
STARTUP_RESULT=${SCRIPT_DIR}/${PID_FILE}

. ${SCRIPT_DIR}/mfcfc_ctl.sh
