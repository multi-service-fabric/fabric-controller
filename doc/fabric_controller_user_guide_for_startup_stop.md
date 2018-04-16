# Fabric Controller User Guide for Startup/Stop
Version 2.0

April 12, 2018

NTT Confidential

Copyright (c) 2018 NTT corp. All Rights Reserved.

---

FC server startup/shutdown is executed by FC startup/shutdown script.

## 1. Startup
(1) Change FC Config (FC initial setting Config, FC system setting
    Config, and Hibernate Config) appropriately.

(2) Execute the following command after changing to the directory where
    FC startup/shutdown script resides.

~~~console
$ sh fc_ctl.sh start
~~~

Command prompts for both FC normal startup and FC startup failed are
described here.

In the case of FC normal startup, the command prompt is as follows.
(Nothing is shown except for the prompt “\$”.)

~~~console
$
~~~

In the case of FC startup failed, the command prompt is as follows.

~~~console
ERROR. foo
$
~~~

\* “foo” explains either why FC can’t startup correctly or what the
status of FC is.
When FC cannot startup correctly, it would be likely that either the
Config setting or network setting is not correctly configured.

## 2. Stop
(1) Shutdown FC.
    Change the working directory to the directory where FC startup/shutdown
    script resides and execute the following command.

~~~console
$ sh fc_ctl.sh stop
~~~

Command prompts for both FC normal shutdown and FC shutdown failed are
described here.

In the case of FC normal shutdown, the command prompt is as follows.

~~~console
INFO. FabricController stopped.
$
~~~

In the case of FC shutdown failed, the command prompt is as follows.

~~~console
ERROR. bar
$
~~~

\* “bar” explains either why FC cannot shutdown correctly or what the
status of FC is.

In the case of FC shutdown failed (FC process exists), execute the following command. This command sends SIGKILL signal to FC process. If the command is excecuted, the DB server may be illegal.
~~~console
$ sh fc_ctl.sh forcestop
~~~

## 3. Status confirmation
### 3.1. Status confirmation with FC startup/shutdown script

This section gives the procedure for status confirmation with FC
starup/shutdown script.
This procedure gives status confirmation of FC startup, whether a
process is running or not.
Change the working directory to the directory where FC startup/shutdown
script resides and then execute the following command.

~~~console
$ sh fc_ctl.sh status
~~~

FC startup status can be determined based on the result of the above
command (standard output log). The standard output logs are summarized
in the Table 3-1.

**Table 3-1 FC startup status**

|FC startup status |Standard output log|Remarks|
|:-----------------|:------------------|:------|
|Running (normal)  |INFO. FabricController\[pid=PID\] is running.|PID shows actual pid of FC.|
|Running (abnormal: double startup)|WARN. FabricController is running (two or more applications are running).|More than or equal to 2 FCs are running. In order to get back normal condition, it is necessary to shutdown the FC that started up by mistake.In this case, since it is not possible to shutdown FC with FC startup/shutdown script, kill command is required to shutdown the FC.|
|Not running       |INFO. FabricController is not running.|-|

### 3.3.2 FC status confirmation (normal/abnormal)

This section describes how to validate FC status.
In order to validate FC status, the REST message in Table 3-2 needs to
be sent to management address of the FC.

**Table 3-2 REST request for status confirmation**

|method|URI                     |
|:-----|:-----------------------|
|GET   |/v1/internal/MSFcontroller/status|

FC status is validated by the response code of the REST message sent
above. However, if there is no response, either the FC isn’t running or
there are transmission errors.
Table 3-3 summarizes response codes.

**Table 3-3 List of response codes of status confirmation**

|Response code|Status of FC|
|:------------|:-----------|
|200          |normal      |
|500          |abnormal    |

When the response code is 200, relevant body field of its response
consists of the following information shown in Table 3-4.

**Table 3-4 Response body field of status confirmation**

|body parameter name|Brief description of body parameter|body parameter value| Brief description of body parameter|
|:------------------|:----------------------------------|--------------------|------------------------------------|
|service\_status    |Service status                     |start-up in progress|Start-up is in progress             |
|                   |                                   |running             |Running                             |
|                   |                                   |shutdown in progress|Shutdown is in progress             |
|                   |                                   |system switching    |System switching is in progress     |
|blockade\_status   |Maintenance blockade status        |blockade            |Under blockade                      |
|                   |                                   |none                |No blockade                         |
