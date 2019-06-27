#!/bin/bash

# Return code
SUCCESS=0
FAIL=1

function sby() {
  local retCode=$SUCCESS
  local crmMon=`sudo crm_mon -A1`

  local nodeState=`echo "$crmMon" | grep vipCheck | grep heartbeat | awk '{print $3}'`
  local actNode=`echo "$crmMon" | grep vipCheck | grep heartbeat | awk '{print $4}'`

  if [ -z $nodeState ] || [ -z $actNode ]; then
#    echo "STATUS = $nodeState : HOST = $actNode"
#    echo "There is no FC running."
    retCode=$FAIL
  else
    if [ $nodeState = "Started" ]; then
      local host1=`echo "$crmMon" | grep Online: | sed -E 's/\[|\]//g' | awk '{print $2}'`
      local host2=`echo "$crmMon" | grep Online: | sed -E 's/\[|\]//g' | awk '{print $3}'`
      if [ -z $host1 ] || [ -z $host2 ]; then
#        echo "There are no two hosts online"
        retCode=$FAIL
      else
        # local SBY=""
        if [ $actNode = $host1 ]; then
          sbyNode=$host2
        else
          sbyNode=$host1
        fi
        # actNode_IP=`echo "$crmMon" | awk '/Node '${actNode}'/,/ is UP/' | grep "ringnumber" | head -n 1 | awk '{print $4}'`
        local sbyIp=`echo "$crmMon" | awk '/Node '${sbyNode}'/,/ is UP/' | grep "ringnumber" | head -n 1 | awk '{print $4}'`
        echo $sbyIp
      fi
    else
#      echo "STATUS = $nodeState : HOST = $actNode"
#      echo "There is no FC running."
      retCode=$FAIL
    fi
  fi
  return $retCode
}

sby
