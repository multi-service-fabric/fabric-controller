#!/bin/bash

# Execute the crm_mon command, check FC runnnig status.
function getOnlineNode() {
  local retCode=$SUCCESS

  crmMon=`expect -c "
    source lib/fc_lib.exp
    remote_cmd $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS \
      \"sudo crm_mon -A1 \"
    "`
  if [ $? = $SUCCESS ]; then
    nodeStatus=`echo "$crmMon" | grep prmFC | grep heartbeat | awk '{print $3}'`
    ONLINE_GRP_FC_HOST=`echo "$crmMon" | grep prmFC | grep heartbeat | awk '{print $4}' | sed -e "s/[\r\n]\+//g"`

    if [ -z $nodeStatus ] || [ -z $ONLINE_GRP_FC_HOST ]; then
      log "There is no FC running."
      retCode=$FAIL
    else
      if [ $nodeStatus = "Started" ]; then
        local online=`echo "$crmMon" | grep Online: | sed -e "s/Online: \[\(.*\)\]/\1/"`
        local host1=`echo "$online" | awk '{print $1}' | sed -e "s/[\r\n]\+//g"`
        local host2=`echo "$online" | awk '{print $2}' | sed -e "s/[\r\n]\+//g"`
        if [ -z $host1 ] || [ -z $host2 ]; then
          log "There is no two online host."
          retCode=$FAIL
        else
          if [ $ONLINE_GRP_FC_HOST = $host1 ]; then
            ONLINE_HOST=$host2
          else
            ONLINE_HOST=$host1
          fi
        fi
      else
        log "There is no FC running."
        retCode=$FAIL
      fi
    fi
  else
    log "Failed to acquire crm_mon."
    retCode=$FAIL
  fi
  return $retCode
}

# connect to host by $RESTORE_HOST_USER、$RESTORE_HOST_ADDR、$RESTORE_HOST_PASS, execute crm_mon, acquire the standby host.
function getStandbyNode() {
  local retCode=$SUCCESS

  crmMon=`expect -c "
    source lib/fc_lib.exp
    remote_cmd $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS \
      \"sudo crm_mon -A1 \"
    "`
  if [ $? = $SUCCESS ]; then
    STANDBY_NODE=`echo "$crmMon" | awk 'match($0, /^Node\s(.*):\sstandby/)' | sed -e 's/Node //' | sed -e 's/: standby//' | sed -e "s/[\r\n]\+//g"`
    if [ -z $STANDBY_NODE ]; then
      log "There are no standby nodes"
    fi
  else
    log "Failed to acquire crm_mon."
    retCode=$FAIL
  fi
  return $retCode
}

# Change the node as the first argument to the standby state.
function toStandby() {
  changeState $1 standby
  return $?
}

# Change the node as the first argument to the online state.
function toOnline() {
  changeState $1 online
  return $?
}

# Change the node as the first argument to the state as the second argument.
function changeState() {
  local node=$1
  local state=$2
  local retCode=$SUCCESS
  expect -c "
    source lib/fc_lib.exp
    remote_cmd $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS \
    \"sudo crm node $state $node \"
  "
  if [ $? != $SUCCESS ]; then
    retCode=$FAIL
  fi
  return $retCode
}

# Check to change the node as the first argument to the standby state.
# The second argument specifies a number of seconds to continue the check process.
function checkStandby() {
  local retCode=$SUCCESS
  local targetNode=$1
  SECONDS=0

  while :
  do
    if [ $SECONDS -ge $2 ]; then
      retCode=$TIMEOUT
      log "A timeout occurred."
      break
    fi
    crmMon=`expect -c "
      source lib/fc_lib.exp
      remote_cmd $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS \
        \"sudo crm_mon -A1 \"
      "`
    if [ $? = $SUCCESS ]; then
      nodeStatus=`echo "$crmMon" | grep "Node $targetNode: standby"`

      if [[ ! -z $nodeStatus ]]; then
        break
      fi
    fi
    sleep 5
    log "Recheck to change the node to the standby state."
  done
  return $retCode
}

# Check to change the node as the first argument to the online state.
# The second argument specifies a number of seconds to continue the check process.
function checkOnline() {
  local retCode=$SUCCESS
  local targetNode=`echo $1 | sed -e "s/[\r\n]\+//g"`
  SECONDS=0

  while :
  do
    if [ $SECONDS -ge $2 ]; then
      retCode=$TIMEOUT
      log "A timeout occurred."
      break
    fi
    crmMon=`expect -c "
      source lib/fc_lib.exp
      remote_cmd $RESTORE_HOST_USER $RESTORE_HOST_ADDR $RESTORE_HOST_PASS \
        \"sudo crm_mon -A1 \"
      "`
    if [ $? = $SUCCESS ]; then
      local nodeStatus=`echo "$crmMon" | grep "Online: \[" | grep "$targetNode"`

      if [[ ! -z $nodeStatus ]]; then
        break
      fi
    fi
    sleep 5
    log "Recheck to change the node to the online state."
  done
  return $retCode
}
