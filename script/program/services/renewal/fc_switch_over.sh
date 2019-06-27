#!/bin/bash

SCRIPT_DIR=$(cd $(dirname $0); pwd)
nohup $SCRIPT_DIR/fc_switch_over_impl.sh >/dev/null 2>&1 &
