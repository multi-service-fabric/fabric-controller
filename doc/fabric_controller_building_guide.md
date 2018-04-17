# MSF Controller (FC) Building Guide
Version 1.0

March.28.2018

Copyright (c) 2018 NTT corp. All Rights Reserved.

## Table of Contents

- [*1. Overview*](#1-overview)

- [*2. Restrictions*](#2-restrictions)

- [*3. Preconditions*](#3-preconditions)

- [*4. List of software to be installed*](#4-list-of-software-to-be-installed)

  - [*4.1 List of software to be installed on target servers*](#41-list-of-software-to-be-installed-on-target-servers)

  - [*4.2 List of software to be installed on Ansible Server*](#42-list-of-software-to-be-installed-on-ansible-server)

- [*5. Installation procedures*](#5-installation-procedures)

  - [*5.1 Download of automatic installation file*](#51-download-of-automatic-installation-file)

  - [*5.2 Change of settings per installation environment and other preparations*](#52-change-of-settings-per-installation-environment-and-other-preparations)

    - [*5.2.1 Change of settings per installation environment*](#521-change-of-settings-per-installation-environment)

    - [*5.2.2 Other preparations*](#522-other-preparations)

    - [*5.2.3 Execution of the automatic installation file (Ansible Playbook)*](#523-execution-of-the-automatic-installation-file-ansible-playbook)

- [*6. Settings of MSF controller after installation*](#6-settings-of-msf-controller-after-installation)

  - [*6.1 MFC/FC settings*](#61-mfcfc-settings)

    - [*6.1.1 Preparation of initial data*](#611-preparation-of-initial-data)

     - [*6.1.1.1 Brief description of MFC/FC initial setting Config*](#6111-brief-description-of-mfcfc-initial-setting-config)

     - [*6.1.1.2 Contents of MFC/FC initial setting Config*](#6112-contents-of-mfcfc-initial-setting-config)

    - [*6.1.2 Settings during operation*](#612-settings-during-operation)

     - [*6.1.2.1 Brief description of MFC/FC system setting Config*](#6121-brief-description-of-mfcfc-system-setting-config)

     - [*6.1.2.2 Contents of MFC/FC system setting Config*](#6122-contents-of-mfcfc-system-setting-config)

    - [*6.1.3 MFC/FC simple startup confirmation*](#613-mfcfc-simple-startup-confirmation)

    - [*6.1.4 Status confirmation with MFC/FC startup/shutdown script*](#614-status-confirmation-with-mfcfc-startupshutdown-script)

    - [*6.1.5 MFC/FC status confirmation (normal/abnormal)*](#615-mfcfc-status-confirmation-normalabnormal)



## Revision History

  |Version  |Date           |Contents     |
  |:--------|:--------------|:------------|
  |1.0      |Mar.28.2018  |First edition|


## 1. Overview
This manual describes the installation procedures of MFC(Multi-Fabric Controller) and FC(Fabric Controller) which manages multiple cluster network and single cluster network,respectively.
In this manual, MSF controller (MFC) is referred to as "MFC", MSF controller (FC) is referred to as "FC" hereinafter.

## 2. Restrictions
- DB (PostgreSQL) client authentication settings of MFC and FC cannot be executed with this manual. Edit pg_hba.conf file of PostgreSQL based on the environment.
- Regarding the DB servers for MFC and FC, the OS security setting modifications (SELinux, Firewall) cannot be executed with procedures in this manual. Execute the OS security setting modifications based on the environment, if they are required for the connection between DB and MFC or FC.

## 3. Preconditions
Figure 3-1 shows the installation preconditions.

![](media/image1.png "Figure 3-1 Preconditions")  
**Figure 3-1 Preconditions**

- Ansible server is required along with target servers.
- Ansible Server needs to be connected to Internet.
- Installation target server group needs to be connected via ssh from Ansible Server with root user.
- OS of Ansible Server needs to be Linux. (i.e. yum command needs to be available.)\*1


\*1 Cent OS recommended  


## 4. List of software to be installed
### 4.1 List of software to be installed on target servers
Table 4-1 shows the list of software to be installed on each server.

**Table 4-1 List of software to be installed**

|No.|Server        |Software    |Version|
|:--|:-------------|:-----------|:------|
|1  |MFC or FC     |Oracle Java    |Oracle JDK 8 Update 162 |
|2  ||Jetty|9.3.11|
|3  ||Gson|2.7|
|4  ||Jersey|2.23.2|
|5  ||Hibernate|5.0.10|
|6  ||SLF4J|1.6.1|
|7  ||Apache Log4j|2.6.2|
|8  ||Apache Commons IO|2.5|
|9  ||Apache Commons Lang|2.6|
|10 ||Apache Commons Collections|3.2.2|
|11 ||JDBC|9.4.1209|
|12 ||Apache Maven|3.5.0|
|13 ||Hipster4j|1.0.1|
|14 ||chrony|Latest|
|15 ||sysstat|Latest|
|16 |DB           |PostgreSQL|9.3|

### 4.2 List of software to be installed on Ansible Server
Table 4-2 shows the list of software to be installed for your reference.

**Table 4-2 List of software to be installed on Ansible Server**

|No.|Server        |Software    |Version|
|:--|:-------------|:-----------|:------|
|1  |Ansible Server|Apache Maven|3.5.0|
|2  ||Oracle Java|Oracle JDK 8 Update 162|

## 5. Installation procedures
Overview of installation procedures is shown below.
1. Download of the automatic installation file (Ansible Playbook)
2. Change of settings per installation environment and preparations
3. Execution of automatic installation file (Ansible Playbook)

### 5.1 Download of automatic installation file
Download the automatic installation file from the following URL and set the file on Ansible Server.
~~~
https://github.com/multi-service-fabric/fabric-controller/raw/master/msf-controller-install.tar.gz
~~~

### 5.2 Change of settings per installation environment and other preparations
#### 5.2.1 Change of settings per installation environment
**<font color="Red">
The following work is executed on Ansible Server <br>
The working user is root
</font>**

(1) Decompression of automatic installation file and change to the working directory

~~~shell-session
# tar zxvf msf-controller-install.tar.gz
# cd msf-controller-install
~~~

(2) IP address settings of installation target server

~~~shell-session
# vi inventory/hosts
~~~
 The sample of inventory/hosts is shown below.

~~~bash
[ansible-server]
localhost ansible_connection=local

[msf-controller_mfc]
192.168.100.20          # Set the IP address of MFC

[msf-controller_fc]
192.168.100.40          # Set the IP address of FC (set IP address of one FC per line in case of installation of several FCs)
192.168.100.50

[mfc_db-server]
192.168.100.30          # Set the IP address of DB for MFC

[fc_db-server]
192.168.100.60          # Set the IP address of DB for FC (Set IP address of one DB for FC per line in case of installation of several DBs for FC)
192.168.100.70

[all:vars]
ansible_connection=paramiko
ansible_ssh_user=root
~~~

(3) Proxy settings of Ansible Server (skip this procedure if the Internet access does not require Proxy)

 ~~~shell-session
 # vi group_vars/ansible-server.yml
 ~~~
The sample of group_vars/ansible-server.yml is shown below.

~~~bash
proxy_user: "username"              # Modify this based on the environment
proxy_pass: "password"             # Modify this based on the environment
proxy_server: "proxyname.xxx.org"  # Modify this based on the environment
proxy_port: "8080"                 # Modify this based on the environment
~~~

(4) Settings of user information for MSF controller and NTP server to be created on MFC Server

~~~shell-session
# vi group_vars/msf-controller_mfc.yml
~~~
The sample of group_vars/msf-controller_mfc.yml is shown below.

~~~bash
msf_group_name: "msfctrl"      # Modify this as needed
msf_user_name: "msfctrl"       # Modify this as needed
msf_user_passwd: "msfctrl0123" # Modify this as needed

ntp_server: "aaa.bbb.ntp.org"  # Modify this based on the environment
~~~

(5) Settings of user information for MSF controller and NTP server on FC Server

~~~shell-session
# vi group_vars/msf-controller_fc.yml
~~~
- The settings are common for all FCs.

The sample of group_vars/msf-controller_fc.yml is shown below.

~~~bash
msf_group_name: "msfctrl"      # Modify this as needed
msf_user_name: "msfctrl"       # Modify this as needed
msf_user_passwd: "msfctrl0123" # Modify this as needed

ntp_server: "aaa.bbb.ntp.org"  # Modify this based on the environment
~~~

(6) Settings of user information for DB, port number for DB connection, DB name for MSF controller, and role to be created on DB for MFC (skip this procedure if change from default value is not necessary)

~~~shell-session
# vi group_vars/mfc_db-server.yml
~~~
The sample of group_vars/mfc_db-server.yml is shown below.

~~~bash
pg_group_name: "postgres"       # Modify this as needed
pg_user_name: "postgres"        # Modify this as needed
pg_user_passwd: "postgres0123"  # Modify this as needed

pg_port: "5432"                 # Modify this as needed

db_name: "msf_mfc"              # Modify this as needed

db_role_user: "msfctrl"         # Modify this as needed
db_role_pass: "msfctrl0123"     # Modify this as needed
~~~

(7) Settings of user information for DB, port number for DB connection, DB name for MSF controller, and role to be created on DB for FC (skip this procedure if change from default value is not necessary)

~~~shell-session
# vi group_vars/fc_db-server.yml
~~~
- The settings are common for all FCs.

The sample of group_vars/fc_db-server.yml is shown below.

~~~bash
pg_group_name: "postgres"       # Modify this as needed
pg_user_name: "postgres"        # Modify this as needed
pg_user_passwd: "postgres0123"  # Modify this as needed

pg_port: "5432"                 # Modify this as needed

db_name: "msf_fc"               # Modify this as needed

db_role_user: "msfctrl"         # Modify this as needed
db_role_pass: "msfctrl0123"     # Modify this as needed
~~~

#### 5.2.2 Other preparations
(1) Proxy settings for yum command on Ansible Server (skip this procedure if Internet access does not require Proxy)

~~~shell-session
# vi /etc/yum.conf
~~~
The sample of /etc/yum.conf is as follow.

~~~bash
proxy=http://proxyname.xxx.org:8080   # Modify this based on the environment
proxy_username=username               # Modify this based on the environment
proxy_password=password               # Modify this based on the environment
~~~

(2) Update of yum package

~~~shell-session
# yum -y update
~~~

(3) Install of ansible

~~~shell-session
# yum -y install ansible
~~~

(4) Key creation for ssh authentication

~~~shell-session
# ssh-keygen -t rsa -b 4096
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa):   # Enter
Enter passphrase (empty for no passphrase):                # Enter
Enter same passphrase again:                               # Enter
Your identification has been saved in /root/.ssh/id_rsa.
Your public key has been saved in /root/.ssh/id_rsa.pub.
~~~

- Press the Enter key with no input when you are asked the file name to be stored and passphrase for the ssh authentication key.

(5) Distribution of ssh public key to target servers.

~~~shell-session
# ssh-copy-id -i ~/.ssh/id_rsa.pub root@< server_address >
~~~
- Replace **< server_address >** with IP address of the installation target server.
- Install the ssh public key using the above command on all the installation target servers.

(6) Confirmation of ping communication with the installation target server

~~~shell-session
# ansible < server_address > -i inventory/hosts -m ping

paramiko: The authenticity of host 'server_address' can't be established.
The ssh-rsa key fingerprint is .
Are you sure you want to continue connecting (yes/no)?
yes
server_address | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
~~~
- Replace **< server_address >** with IP address of the installation target server.
- Execute the communication confirmation using the above command to all the installation target servers.
- Confirm the result of the command execution is "SUCCESS".
- Input "yes" in response to the following message for the first connection with the installation target server.

~~~shell-session
Are you sure you want to continue connecting (yes/no)?
~~~

#### 5.2.3 Execution of the automatic installation file (Ansible Playbook)
**<font color="Red">
Execute the following work in the working directory.
</font>**

1. Execution of the automatic installation file (Ansible Playbook)

~~~shell-session
# ansible-playbook -i inventory/ msf-controller_install.yml

 ~ Omit ~

PLAY RECAP *******************************************************************************
hostname                  : ok=5    changed=1    unreachable=0    failed=0
~~~

Confirm the result of the execution of the automatic installation file (Ansible Playbook) that is shown on the console is **"failed=0"**.



## 6. Settings of MSF controller after installation
The following sections describe the settings of MSF controller after installation.

### 6.1 MFC/FC settings

This section describes the settings of MFC/FC.

#### 6.1.1 Preparation of initial data

MFC/FC initial setting Config should be edited before the first startup of the MFC/FC server.

##### 6.1.1.1 Brief description of MFC/FC initial setting Config

MFC/FC initial setting Config is shown briefly in Table 6-1.

**Table 6-1 Brief description of MFC/FC initial setting Config**

  |No.  |Config file name|Config Name|Location|
  |:----|:---------------|:----------|:-------|
  |1    |mfc\_data.xml   |MFC initial configuration Config| In "*MFC/FC install directory*/conf/"|
  |2    |fc\_data.xml    |FC initial configuration Config | In "*MFC/FC install directory*/conf/"|

##### 6.1.1.2 Contents of MFC/FC initial setting Config

This section describes the parameters in MFC/FC initial setting Config.  
MFC/FC initial setting Config file is in xml format.  
An example of MFC/FC initial setting Config is shown below.  
Parameters in MFC/FC initial setting Config are summarized in Table 6-2.

**MFC initial setting Config**
~~~xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<dataConf xmlns="http://mfc.msf/common/config/type/data">
    <swClustersData>
        <maxSwClusterNum>5</maxSwClusterNum>
        <clusterStartAddress>1.1.1.1</clusterStartAddress>
        <swClusterData>
            <swCluster>
                <swClusterId>1</swClusterId>
                <inchannelStartAddress>1.1.1.1</inchannelStartAddress>
            </swCluster>
            <rrs>
                <rr>
                    <rrNodeId>1</rrNodeId>
                </rr>
            </rrs>
        </swClusterData>
    </swClustersData>
</dataConf>
~~~

**FC initial setting Config**
~~~xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<dataConf xmlns="http://fc.msf/common/config/type/data">
    <swClustersData>
        <swClusterData>
            <swCluster>
                <swClusterId>1</swClusterId>
                <maxLeafNum>45</maxLeafNum>
                <maxSpineNum>4</maxSpineNum>
                <maxRrNum>2</maxRrNum>
                <spineStartPos>1</spineStartPos>
                <leafStartPos>51</leafStartPos>
                <rrStartPos>101</rrStartPos>
                <fcStartPos>201</fcStartPos>
                <ecStartPos>211</ecStartPos>
                <emStartPos>221</emStartPos>
                <asNum>65000</asNum>
                <ospfArea>10</ospfArea>
                <inchannelStartAddress>1.1.1.1</inchannelStartAddress>
                <outchannelStartAddress>1.1.1.1</outchannelStartAddress>
                <aggregationStartAddress>1.1.1.1</aggregationStartAddress>
                <aggregationAddressPrefix>24</aggregationAddressPrefix>
            </swCluster>
            <rrs>
                <rr>
                    <rrNodeId>1</rrNodeId>
                    <rrRouterId>1.1.1.1</rrRouterId>
                </rr>
                <leafRr>
                    <leafRrSwClusterId>1</leafRrSwClusterId>
                    <leafRrNodeId>1</leafRrNodeId>
                    <leafRrRouterId>1.1.1.1</leafRrRouterId>
                </leafRr>
            </rrs>
        </swClusterData>
    </swClustersData>
</dataConf>
~~~

**Table 6-2 List of parameters in MFC/FC initial setting Config**

|Element |MFC |FC  |Type |Setting range |Number of elements |Description|
|:-------|:---|:---|:----|:--------------|:------------------|:----------|
|dataConf|M   |M    |-    |-              |1                  |Information of MFC/FC initial setting|
|&nbsp;&nbsp;maxSwClusterNum|M   |-    |integer|1 - 100|1               |Maximum number of clusters|
|&nbsp;&nbsp;clusterStartAddress|M   |-    |string|-   |1                  |inter-cluster link IF|
|&nbsp;&nbsp;swClustersData|M   |M    |-    |-              |1                  |Information of SW cluster which is managed by MFC/FC|
|&nbsp;&nbsp;&nbsp;&nbsp;swClusterData|M   |M    |-    |-              |1                  |Equivalent to information of a SW cluster per an element|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swCluster|M   |M     |-    |-              |1                  |Information of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swClusterId|M   |M    |integer|1 - 100|1 |ID of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maxLeafNum|-   |M    |integer|1 - 1000|1 |Maximum number of Leaves in SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maxSpineNum|-   |M    |integer|1 - 1000|1 |Maximum number of Spines in SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maxRrNum|M   |-    |integer|1 - 1000|1 |Maximum number of RRs in SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spineStartPos|-   |M    |integer|1 - 1000|1 |Starting offset value of management address which is assigned at the time of Spine addition.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;leafStartPos|-   |M    |integer|1 - 1000|1 |Starting offset value of management address which is assigned at the time of Leaf addition.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rrStartPos|-   |M    |integer|1 - 1000|1 |Starting offset of management address which is assigned to RR.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fcStartPos|-   |M    |integer|1 - 1000|1 |Starting offset of management address which is assigned to FC.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ecStartPos|-   |M    |integer|1 - 1000|1 |Starting offset of management address which is assigned to EC.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;emStartPos|-   |M    |integer|1 - 1000|1 |Starting offset of management address which is assigned to EM.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;asNum|M   |-    |integer|0 - 65535|1 |AS number of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ospfArea|-   |M    |integer|0 - 65535|1 |OSPF area number of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;inchannelStartAddress|M   |M    |string|-|1 |Inchannel starting IP address of SW cluster.<BR><BR>Initiating with starting IP address, an suitable IP address is calculated automatically within MSF controller and is assigned when interface (node) is added.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;outchannelStartAddress|-   |M    |string|-|1 |Outchannel starting IP address of SW cluster.<BR><BR>Initiating with starting IP address, an suitable IP address is calculated automatically within MSF controller and is assigned when interface (node) is added.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aggregationStartAddress|-   |M    |string|-|1 |Aggregation starting IP address.<BR><BR>Starting IP address for aggregation of paths to each node of SW cluster.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aggregationAddressPrefix|-   |M    |integer|1 - 32|1 |Aggregation prefix|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rrs|M   |M    |-|-|1 |RR information of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rr|O   |O    |-|-|0 and over |RR information|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rrNodeId|M   |M    |integer|1 - 65535|1|RR node ID of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rrRouterId|-   |M    |string|-|1|RR router ID of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;leafRr|-   |O    |-|-|0 and over |RR information of Leaf's peer|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;leafRrSwClusterId|-   |M    |integer|1 - 100|1|RR belonging cluster ID of Leaf's peer|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;leafRrNodeId|-   |M    |integer|1 - 65535|1|RR node ID of Leaf's peer|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;leafRrRouterId|-   |M    |string|-|1|RR router ID of Leaf's peer|

\* Legend: M: mandatory O: optional

#### 6.1.2 Settings during operation

This section describes the settings during MFC/FC operation. Edit MFC/FC system
setting Config while MFC/FC is not running. Note that any changes of MFC/FC
system setting Config while MFC/FC is running cannot be reflected in the
behavior of MFCFC.

##### 6.1.2.1 Brief description of MFC/FC system setting Config

The brief description of MFC/FC system setting Config is shown in Table 6-3.

**Table 6-3 Brief description of system setting Config**

|No.   |Config file name   |Name of Config             |Location|
|:-----|:------------------|:--------------------------|:-------|
|1     |mfc\_system.xml    |MFC system setting Config  |In "*MFC install directory*/conf/"|
|2     |fc\_system.xml     |FC system setting Config   |In "*FC install directory*/conf/"|


##### 6.1.2.2 Contents of MFC/FC system setting Config

This section describes the parameters in MFC/FC system setting Config.  
MFC/FC system setting Config file is in xml format.  
An example of MFC/FC system setting Config is shown below.  
Parameters in MFC/FC system setting Config are summarized in Table 6-4.  

**MFC system setting Config**
~~~xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<systemConf xmlns="http://mfc.msf/common/config/type/system">
    <controller>
        <managementIpAddress>0.0.0.0</managementIpAddress>
    </controller>
    <rest>
        <server>
            <listeningAddress>0.0.0.0</listeningAddress>
            <listeningPort>18080</listeningPort>
        </server>
        <client>
            <waitConnectionTimeout>30</waitConnectionTimeout>
            <requestTimeout>600</requestTimeout>
            <responseBufferSize>2</responseBufferSize>
        </client>
        <json>
            <isPrettyPrinting>true</isPrettyPrinting>
            <isSerializeNulls>false</isSerializeNulls>
        </json>
    </rest>
    <slice>
        <l2SlicesMagnificationNum>1</l2SlicesMagnificationNum>
        <l3SlicesMagnificationNum>1</l3SlicesMagnificationNum>
        <l2MaxSlicesNum>100</l2MaxSlicesNum>
        <l3MaxSlicesNum>100</l3MaxSlicesNum>
    </slice>
    <swClustersData>
        <swClusterData>
            <swCluster>
                <swClusterId>1</swClusterId>
                <fcControlAddress>0.0.0.0</fcControlAddress>
                <fcControlPort>18080</fcControlPort>
            </swCluster>
            <rrs>
                <peer_cluster>1</peer_cluster>
                <accommodated_clusters>1</accommodated_clusters>
            </rrs>
        </swClusterData>
    </swClustersData>
    <status>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
        <recvRestRequestUnitTime>3600</recvRestRequestUnitTime>
        <sendRestRequestUnitTime>3600</sendRestRequestUnitTime>
    </status>
    <failure>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
            <isPhysicalUnit>true</isPhysicalUnit>
            <isClusterUnit>true</isClusterUnit>
            <isSliceUnit>true</isSliceUnit>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
    </failure>
    <traffic>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
            <isPhysicalUnit>true</isPhysicalUnit>
            <isClusterUnit>true</isClusterUnit>
            <isSliceUnit>true</isSliceUnit>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
    </traffic>
</systemConf>
~~~

**FC system setting Config**
~~~xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<systemConf xmlns="http://fc.msf/common/config/type/system">
    <controller>
        <managementIpAddress>0.0.0.0</managementIpAddress>
    </controller>
    <rest>
        <server>
            <listeningAddress>0.0.0.0</listeningAddress>
            <listeningPort>18080</listeningPort>
        </server>
        <client>
            <waitConnectionTimeout>30</waitConnectionTimeout>
            <requestTimeout>600</requestTimeout>
            <responseBufferSize>2</responseBufferSize>
        </client>
        <json>
            <isPrettyPrinting>true</isPrettyPrinting>
            <isSerializeNulls>false</isSerializeNulls>
        </json>
    </rest>
    <slice>
        <l2SlicesMagnificationNum>1</l2SlicesMagnificationNum>
        <l3SlicesMagnificationNum>1</l3SlicesMagnificationNum>
        <l2MaxSlicesNum>100</l2MaxSlicesNum>
        <l3MaxSlicesNum>100</l3MaxSlicesNum>
    </slice>
    <swClustersData>
        <swClusterData>
            <swCluster>
                <swClusterId>1</swClusterId>
                <ecControlAddress>0.0.0.0</ecControlAddress>
                <ecControlPort>18080</ecControlPort>
            </swCluster>
        </swClusterData>
    </swClustersData>
    <status>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
        <recvRestRequestUnitTime>3600</recvRestRequestUnitTime>
        <sendRestRequestUnitTime>3600</sendRestRequestUnitTime>
    </status>
    <failure>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
            <isPhysicalUnit>true</isPhysicalUnit>
            <isClusterUnit>true</isClusterUnit>
            <isSliceUnit>true</isSliceUnit>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
    </failure>
    <traffic>
        <noticeDestInfo>
            <noticeAddress>1.1.1.1</noticeAddress>
            <noticePort>0</noticePort>
            <isPhysicalUnit>true</isPhysicalUnit>
            <isClusterUnit>true</isClusterUnit>
            <isSliceUnit>true</isSliceUnit>
        </noticeDestInfo>
        <noticeRetryNum>5</noticeRetryNum>
        <noticeTimeout>500</noticeTimeout>
        <execCycle>3600</execCycle>
    </traffic>
    <qos>
        <remark_menu>af1</remark_menu>
        <remark_menu>af2</remark_menu>
        <remark_menu>af3</remark_menu>
        <remark_menu>be</remark_menu>
        <remark_menu>packet_color</remark_menu>
    </qos>
</systemConf>
~~~

**Table 6-4 List of parameters in MFC/FC system setting Config**

|Element|MFC |FC  |Type |Setting range \* |Number of elements |Description|
|:------|:---|:---|:----|:--------------|:------------------|:----------|
|systemConf|M   |M    |-    |-              |1                  |Information of FC system setting|
|&nbsp;&nbsp;controller|M   |M    |-    |-              |1                  |Controller information|
|&nbsp;&nbsp;&nbsp;&nbsp;managementIpAddress|M   |M    |string    |-              |1                  |Management IP address of controller|
|&nbsp;&nbsp;rest|M   |M    |-    |-              |1                  |Information of REST behavior which operates on MFC/FC|
|&nbsp;&nbsp;&nbsp;&nbsp;server|M   |M    |-    |-              |1                  |Information of REST server behavior|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;listeningAddress|M   |M    |string|-              |1                  |Address for REST communication|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;listeningPort|M   |M    |integer|0 - 65535|1                  |REST listening port|
|&nbsp;&nbsp;&nbsp;&nbsp;client|M   |M    |-    |-              |1                  |Information of REST client behavior|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;waitConnectionTimeout|M   |M    |integer|0 and over |1                  |REST wait connection timeout (second)|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;requestTimeout|M   |M    |integer|0 and over |1                  |REST request timeout (second)|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;responseBufferSize|M   |M    |integer|1 and over |1                  |Buffer size of received response data for REST request(MB)|
|&nbsp;&nbsp;&nbsp;&nbsp;json|M   |M    |-    |-              |1                  |Information of JSON behavior which is sent and received in REST|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isPrettyPrinting|M   |M    |boolean|true/false|1                  |Whether JSON data that MFC/FC responds to REST request is formatted in such a way of line breaking and indenting or not (true/ false)|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isSerializeNulls|M   |M    |boolean|true/false|1                  |In case there is a null value in JSON data that MFC/FC respondes to REST request, whether parameter is described as null or not (true/ false)|
|&nbsp;&nbsp;slice|M   |M    |-    |-              |1                  |Information of slice setting|
|&nbsp;&nbsp;&nbsp;&nbsp;l2SlicesMagnificationNum|M   |M    |integer|1,2 |1                  |Parameter that controls maxmum value of assigned L2 slice ID.<BR>Assigned range of L2 slice ID is equal to l2MaxSlicesNum multiplied by l2SlicesMagnificationNum.<BR>Example 1: If l2MaxSliceNum is 100 and l2SliceMagnificationNum is 1, then assigned range of L2 slice ID is from 1 to 100.<BR>Example 2: If l2MaxSliceNum is 100 and l2SliceMagnificationNum is 2, then assigned range of L2 slice ID is from 1 to 200.|
|&nbsp;&nbsp;&nbsp;&nbsp;l3SlicesMagnificationNum|M   |M    |integer|1,2 |1                  |Parameter that controls maxmum value of assigned L3 slice ID.<BR>Assigned range of L3 slice ID is equal to l3MaxSlicesNum multiplied by l3SlicesMagnificationNum.<BR>Example 1: If l3MaxSliceNum is 100 and l3SliceMagnificationNum is 1, then assigned range of L3 slice ID is from 1 to 100.<BR>Example 2: If l3MaxSliceNum is 100 and l3SliceMagnificationNum is 2, then assigned range of L3 slice ID is from 1 to 200.|
|&nbsp;&nbsp;&nbsp;&nbsp;l2MaxSlicesNum|M   |M    |integer|1 - 1000 |1                  |Maximum L2 slice number|
|&nbsp;&nbsp;&nbsp;&nbsp;l3MaxSlicesNum|M   |M    |integer|1 - 1000 |1                  |Maximum L3 slice number|
|&nbsp;&nbsp;swClustresData|M   |M    |-    |-              |1                  |Information of SW cluster which is managed by MFC/FC|
|&nbsp;&nbsp;&nbsp;&nbsp;swClusterData|M   |M    |- |- |1                  |Equivalent to information of a SW cluster per an element|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swCluster|M   |M    |- |- |1                  |Information of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swClusterId|M   |M    |integer|1 - 100|1                  |ID of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fcControlAddress|M   |-    |string|-|1                  |FC connection address of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fcControlPort|M   |-    |integer|0 - 65535|1                  |FC connection port number of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ecControlAddress|-   |M    |string|-|1                  |EC connection address of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ecControlPort|-   |M    |integer|0 - 65535|1                  |EC connection port number of SW cluster|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rrs|M   |-    |- |- |1                  |RR information|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;peer_cluster|M   |-    |integer|1 - 100|1                  |Cluster ID of the cluster that accommodates the RR to be set as a peer by the Leafs in this cluster.|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;accommodated_clusters|M   |-    |integer|1 - 100|1 and over                  |Cluster IDs of the clusters that accommodate the Leafs to be set as a peer to the RR in this cluster.|
|&nbsp;&nbsp;status|M   |M    |-    |-              |1                  |Information of notification destination of system status|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeDestInfo|O   |O    |-    | - |0 and over|Information of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticeAddress|M   |M    |string |- |1|IP address of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticePort|M   |M    |integer |0 - 65535 |1|Port number of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeRetryNum|M   |M    |integer|0 and over|1|Number of notification retry (times)|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeTimeout|M   |M    |integer|0 and over|1|Interval of notification retry (millisecond)|
|&nbsp;&nbsp;&nbsp;&nbsp;recvRestRequestUnitTime|M   |M    |integer|0 and over|1|Unit time for counting the number of receving REST requests (second)|
|&nbsp;&nbsp;&nbsp;&nbsp;sendRestRequestUnitTime|M   |M    |integer|0 and over|1|Unit time for counting the number of sending REST requests (second)|
|&nbsp;&nbsp;failure|M   |M    |-    |-              |1                  |Information of notification destination of failure|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeDestInfo|O   |O    |-|-|0 and over|Information of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticeAddress|M   |M    |string|-|1|IP address of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticePort|M   |M    |integer|0 - 65535|1|Port number of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isPhysicalUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Physical unit|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isClusterUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Cluster unit|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isSliceUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Slice unit|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;failureLinkNum|-   |o    |integer|0 - 65535|1|Number of failed internal links.<BR><BR>In case of internal link failure, lower limit number of failed internal links to send notification.<BR><BR>Example: If failureLinkNum is set to 5, notification will not be sent until the number of failed internal links reaches 5.|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeRetryNum|M   |M    |integer|0 and over|1|Number of notification retry (times)|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeTimeout|M   |M    |integer|0 and over|1|Interval of notification retry (millisecond)|
|&nbsp;&nbsp;traffic|M   |M    |-    |-              |1                  |Information of notification destication of traffic|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeDestInfo|O   |O    |-|-|0 and over|Information of notification destication|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticeAddress|M   |M    |string|-|1|IP address of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;noticePort|M   |M    |integer|0 - 65535|1|Port number of notification destination|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isPhysicalUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Physical unit|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isClusterUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Cluster unit|
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;isSliceUnit|M   |M    |boolean|true/false|1|Notification unit:<BR>Slice unit|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeRetryNum|M   |M    |integer|0 and over|1|Number of notification retry (times)|
|&nbsp;&nbsp;&nbsp;&nbsp;noticeTimeout|M   |M    |integer|0 and over|1|Interval of notification retry (millisecond)|
|&nbsp;&nbsp;&nbsp;&nbsp;execCycle|-   |M    |integer|0 and over|1|Interval of acquiring traffic information (second)|
|&nbsp;&nbsp;qos|-   |M    |-    |-              |1                  |QoS|
|&nbsp;&nbsp;&nbsp;&nbsp;remark_menu|-   |M    |string|-              |1                  |Remark menu|


\* If the upper limit of setting range of an attribute is not described,
its upper limit is the maximum number of integer type (2147483647).

\* Legend: M: mandatory O: optional

#### 6.1.3 MFC/FC simple startup confirmation

This section gives the simple MFC/FC startup confirmation methods.

(1) Change MFC/FC Config (MFC/FC initial setting Config, MFC/FC system setting
    Config, and Hibernate Config) appropriately.

(2) Execute the following command after changing to the directory where
    MFC/FC startup/shutdown script resides.

**MFC startup**
~~~console
$ sh mfc_ctl.sh start
~~~

**FC startup**
~~~console
$ sh fc_ctl.sh start
~~~

Command prompts for both MFC/FC normal startup and MFC/FC startup failed are
described here.

In the case of MFC/FC normal startup, the command prompt is as follows.  
(Nothing is shown except for the prompt "$".)

~~~console
$
~~~

In the case of MFC/FC startup failed, the command prompt is as follows.

~~~console
ERROR. foo
$
~~~

\* "foo" explains either why MFC/FC can't startup correctly or what the
status of MFC/FC is.  
When MFC/FC cannot startup correctly, it would be likely that either the
Config setting or network setting is not correctly configured.

(3) Shutdown MFC/FC.  
    Change the working directory to the directory where MFC/FC startup/shutdown
    script resides and execute the following command.

**MFC shutdown**
~~~console
$ sh fc_ctl.sh stop
~~~

**FC shutdown**
~~~console
$ sh fc_ctl.sh stop
~~~

Command prompts for both MFC/FC normal shutdown and MFC/FC shutdown failed are
described here.

In the case of MFC/FC normal shutdown, the command prompt is as follows.

**MFC shutdown**
~~~console
INFO. MultiFabricController stopped.
$
~~~

**FC shutdown**
~~~console
INFO. FabricController stopped.
$
~~~

In the case of MFC/FC shutdown failed, the command prompt is as follows.

~~~console
ERROR. bar
$
~~~

\* "bar" explains either why MFC/FC cannot shutdown correctly or what the
status of MFC/FC is.

#### 6.1.4 Status confirmation with MFC/FC startup/shutdown script

This section gives the procedure for status confirmation with MFC/FC
startup/shutdown script.  
This procedure gives status confirmation of FC startup, whether a
process is running or not.  
Change the working directory to the directory where MFC/FC startup/shutdown
script resides and then execute the following command.

**MFC status**
~~~console
$ sh mfc_ctl.sh status
~~~

**FC status**
~~~console
$ sh fc_ctl.sh status
~~~

MFC/FC startup status can be determined based on the result of the above
command (standard output log). The standard output logs are summarized
in the Table 6-5 and Table 6-6.

**Table 6-5 MFC startup status**

|MFC startup status |Standard output log|Remarks|
|:-----------------|:------------------|:------|
|Running (normal)  |INFO. MultiFabricController\[pid=PID\] is running.|PID shows actual pid of MFC.|
|Running (abnormal: double startup)|WARN. MultiFabricController is running (two or more applications are running).|More than or equal to 2 MFCs are running. In order to get back normal condition, it is necessary to shutdown the MFC that started up by mistake.In this case, since it is not possible to shutdown MFC with MFC startup/shutdown script, kill command is required to shutdown the MFC.|
|Not running       |INFO. MultiFabricController is not running.|-|


**Table 6-6 FC startup status**

|FC startup status |Standard output log|Remarks|
|:-----------------|:------------------|:------|
|Running (normal)  |INFO. FabricController\[pid=PID\] is running.|PID shows actual pid of FC.|
|Running (abnormal: double startup)|WARN. FabricController is running (two or more applications are running).|More than or equal to 2 FCs are running. In order to get back normal condition, it is necessary to shutdown the FC that started up by mistake.In this case, since it is not possible to shutdown FC with FC startup/shutdown script, kill command is required to shutdown the FC.|
|Not running       |INFO. FabricController is not running.|-|

#### 6.1.5 MFC/FC status confirmation (normal/abnormal)

This section describes how to validate MFC/FC status.  
In order to validate MFC/FC status, the REST message in Table 6-7 needs to
be sent to management address of the MFC/FC.

**Table 6-7 REST request for status confirmation**

|method|URI                     |
|:-----|:-----------------------|
|GET   |/v1/internal/MSFcontroller/status|

MFC/FC status is validated by the response code of the REST message sent
above. However, if there is no response, either the MFC/FC isn't running or
there are transmission errors.  
Table 6-8 summarizes response codes.

**Table 6-8 List of response codes of status confirmation**

|Response code|Status of MFC/FC|
|:------------|:-----------|
|200          |normal      |
|500          |abnormal    |

When the response code is 200, relevant body field of its response
consists of the following information shown in Table 6-9.

**Table 6-9 Response body field of status confirmation**

|body parameter name|Brief description of body parameter|body parameter value| Brief description of body parameter|
|:------------------|:----------------------------------|--------------------|------------------------------------|
|service\_status    |Service status                     |start-up in progress|Start-up is in progress             |
|                   |                                   |running             |Running                             |
|                   |                                   |shutdown in progress|Shutdown is in progress             |
|                   |                                   |system switching    |System switching is in progress     |
|blockade\_status   |Maintenance blockade status        |blockade            |Under blockade                      |
|                   |                                   |none                |No blockade                         |
