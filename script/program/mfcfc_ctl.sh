##################################################################
# JAR name
readonly JAR="FabricController.jar"

# Execution command
readonly EXE_NAME="java -jar ${JAR}"

# Status code
# "1" is redundantly defined because there are two meanings depending on the options.
readonly STATUS_OK=0
readonly STATUS_NOT_RUNNING=1
readonly STATUS_STARTUP_FAILED=1
readonly STATUS_ALREADY_RUNNING=2
readonly STATUS_INVALID_OPTION=3
readonly STATUS_END_FAILURE=4

# SLEEP time (second)
readonly SLEEP_TIME=1

# Startup PID check timeout (second)
readonly PID_CHECK_TIMEOUT=10

# Stop timeout (minute)
readonly FINALIZE_TIMEOUT=1

# A function that acquires the number of running processes (process_num) and PID (pid) of running process
# The function returns 0 if the number of running processes is not 1 (no or multiple running processes).
function get_process_info(){

  # Set the number of processes
  # pgrep returns the process number of the specified files if it exists.
  process_num=`pgrep -c -F $STARTUP_RESULT 2>/dev/null `
  # Put the default value 0 if empty
  : ${process_num:=0}

  # PID is set to pid only when the corresponding process exists.
  if [ $process_num -eq 1 ]; then
    pid=`pgrep -F "${STARTUP_RESULT}"`
  else
    pid=0
  fi
}


function start(){

  # Confirm the number of processes to prevent the double startup
  get_process_info

  if [ $process_num -eq 0 ] ; then
    # In case that the application is not running

    # Acquire the exclusive lock (read) for this file
    # Script lock status 0: unlocked, 1: locked
    lock=0
    exec {lock_fd}< ${0}
    flock --nonblock ${lock_fd} || lock=1

    if [ ${lock} = 1 ]; then
      # Abnormal termination if the file lock fails
      echo "ERROR. Couldn't lock this script."
      exit $STATUS_STARTUP_FAILED
    fi

    # Delete the startup confirmation file (output at normal startup) if it exists
    if [ -e ${STARTUP_RESULT} ]; then
      rm -f $STARTUP_RESULT
    fi


    # Move to the script directory
    cd $(dirname ${0})
    if [ ! -e ../lib/${JAR} ] ; then
      echo "ERROR. Application file not found."
      exit $STATUS_STARTUP_FAILED
    fi

    # Start the Java process with the location of the application file (jar) as the root
    java -cp ../lib/${JAR} ${MAIN_CLASS} ${ANOTHER_CONF_DIR} &

    # Sleep to wait for the startup of the background executed process
    sleep $SLEEP_TIME

    # Sleep to wait for the startup while the Java process exists and the startup confirmation file has not been output
    # - Terminate abnormally due to a Java process startup error when the PID can not be confirmed
    # - Exit from standby after confirming the output of the startup confirmation file
    count=0
    while true
    do
      # Confirm existence of startup confirmation file
      # The file is outputted when the startup process is completed normally in the Java process
      if [ -e $STARTUP_RESULT ] ; then
        get_process_info

        if [ $process_num -eq 0 ] ; then
          echo "ERROR. ${APP_NAME} failed in start."
          exit $STATUS_STARTUP_FAILED
        fi

        break
      fi

      if [ $count -gt 50 ] ; then
          echo "ERROR. ${APP_NAME} process does not created."
          exit $STATUS_STARTUP_FAILED
      fi
      let count++

      sleep $SLEEP_TIME
    done

    # Confirm just to make sure whether the PID confirmed by the PS command matches the PID output to the startup confirmation file
    pid_no=`cat ${STARTUP_RESULT}`

    # Variable for checking the content of startup confirmation file
    parm=0

    # Confirm whether startup confirmation file is empty
    if [ ! -z ${pid_no} ]; then
      parm=${pid_no}
    else
      echo "ERROR. The contents of the ${STARTUP_RESULT} are empty."
      exit $STATUS_STARTUP_FAILED
    fi

    # Confirm whether the character string of the startup confirmation file is numeric
    #### Add 1 to parm, then save the return value in ret
    expr $parm + 1 > /dev/null 2>&1
    ret=$?
    if [ $ret -ge 2  ] ; then
      echo "ERROR. The contents of the ${STARTUP_RESULT} is not a number."
      exit $STATUS_STARTUP_FAILED
    fi

    if [ $pid_no -ne $pid ] ; then
      echo "ERROR. ${APP_NAME} PID does not match."
      exit $STATUS_STARTUP_FAILED
    fi

    exit $STATUS_OK

  elif [ $process_num -eq 1 ] ; then
    # In case that one application is running
    echo "WARN. ${APP_NAME}[pid=${pid}] is already running."
    exit $STATUS_ALREADY_RUNNING

  else
    # In case that multiple applications are running
    echo "ERROR. ${APP_NAME} is already running(two or more applications are running)."
    exit $STATUS_ALREADY_RUNNING
  fi
}

function status(){
  # Check process execution status
  get_process_info

  if [ $process_num -eq 1 ]; then
    # In case that only one process is running
    echo "INFO. ${APP_NAME}[pid=${pid}] is running."
    exit $STATUS_OK

  elif [ $process_num -gt 1 ]; then
    # In case that multiple prosesses are running
    # Respond as running to leave it to execution status check on the Java process side (status confirmation of RA)
    echo "WARN. ${APP_NAME} is running(two or more applications are running)."
    exit $STATUS_OK

  else
    # In case of no pid
    echo "INFO. ${APP_NAME} is not running."
    exit $STATUS_NOT_RUNNING
  fi
}

function stop_forcestop(){
  # After checking the process execution status, 
  # stop the process with SIGTERM for stop and with SIGKILL for forcestop 

  get_process_info

  if [ $process_num -eq 1 ] ; then
    # If the number of processes is 1 or more, specify the PID and perform the stop processing
    kill_mode="-TERM"
    if [ ${1} = "forcestop" ]; then
      kill_mode="-KILL"
    fi

    kill $kill_mode $pid
    kill_result=$?

  elif [ $process_num -eq 0 ] ; then
    # If the number of processes is 0, stop processing is not performed, and it is assumed that the stop succeeds normally
    kill_result=0

  else
    # When two or more processes are running, terminate abnormally because the stop object is unclear
    echo "ERROR. ${APP_NAME} failed in stop(two or more applications are running)."
    exit $STATUS_END_FAILURE
  fi

  # In case that sending a signal with the kill command fails
  if [ ${kill_result} -ne 0 ] ; then
    echo "ERROR. ${APP_NAME} failed in stop."
    exit $STATUS_END_FAILURE
  fi

  # Monitor the stop of Java process notifying signal until termination of TIMEOUT
  end_time=`date -d "${FINALIZE_TIMEOUT} minutes" "+%s"`
  while [ $end_time -gt `date "+%s"` ]
  do

    # Stop is completed when the number of running processes becomes 0
    get_process_info
    if [ $process_num -eq 0 ] ; then
      echo "INFO. ${APP_NAME} stopped."
      exit $STATUS_OK
    fi

    sleep $SLEEP_TIME

  done

  # In case that TIMEOUT expires before stop
  echo "ERROR. ${APP_NAME} failed in stop."
  exit $STATUS_END_FAILURE
}

function invalid_option(){
  echo "Usage:"
  echo "  $(basename ${0}) stop"
  echo "  $(basename ${0}) forcestop"

  exit $STATUS_INVALID_OPTION
}


# option branch
case ${1} in
  start)
    start
    ;;
  status)
    status
    ;;
  stop | forcestop)
    stop_forcestop ${1}
    ;;
  *)
    invalid_option
    ;;
esac
