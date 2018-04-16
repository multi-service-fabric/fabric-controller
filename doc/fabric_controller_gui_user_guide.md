# GUI User Guide
Version 1.0

March.28.2018

NTT Confidential

Copyright (c) 2018 NTT corp. All Rights Reserved.

## Table of Contents
- [*1. Overview*](#1-overview)

  - [*1.1 Introduction*](#11-introduction)

  - [*1.2 Conditions of GUI tool operation*](#12-conditions-of-gui-tool-operation)

  - [*1.3 Preparation of GUI tool environment*](#13-preparation-of-gui-tool-environment)

    - [*1.3.1 Config Editing*](#131-config-editing)

    - [*1.3.2 Web server publishing*](#132-web-server-publishing)

- [*2. Login*](#2-login)

  - [*2.1 Login screen*](#21-login-screen)

  - [*2.2 Login user*](#22-login-user)

- [*3. Main screen*](#3-main-screen)

  - [*3.1 Screen layout*](#31-screen-layout)

  - [*3.2 Network display mode*](#32-network-display-mode)

    - [*3.2.1 Multi-cluster display mode*](#321-multi-cluster-display-mode)

    - [*3.2.2 Fabric network display mode*](#322-fabric-network-display-mode)

    - [*3.2.3 Node display mode*](#323-node-display-mode)

  - [*3.3 Controller display mode*](#33-controller-display-mode)

    - [*3.3.1 Controller display mode(MFC, FC, EC, EM)*](#331-controller-display-modemfc-fc-ec-em)

- [*4. Monitoring menu*](#4-monitoring-menu)

  - [*4.1 Traffic display*](#41-traffic-display)

  - [*4.2 Controller status display*](#42-controller-status-display)

- [*5. Service setting menu*](#5-service-setting-menu)

  - [*5.1 Slice*](#51-slice)

    - [*5.1.1 Slice addition*](#511-slice-addition)

    - [*5.1.2 Slice modification*](#512-slice-modification)

    - [*5.1.3 Slice deletion*](#513-slice-deletion)

  - [*5.2 CP*](#52-cp)

    - [*5.2.1 CP addition*](#521-cp-addition)

    - [*5.2.2 CP modification*](#522-cp-modification)

    - [*5.2.3 CP deletion*](#523-cp-deletion)

- [*6. Infrastructure setting menu*](#6-infrastructure-setting-menu)

  - [*6.1 Cluster*](#61-cluster)

    - [*6.1.1 Cluster addition*](#611-cluster-addition)

    - [*6.1.2 Cluster deletion*](#612-cluster-deletion)

    - [*6.1.3 Inter-cluster link IF addition*](#613-inter-cluster-link-if-addition)

    - [*6.1.4 Inter-cluster link IF deletion*](#614-inter-cluster-link-if-deletion)

  - [*6.2 Device information*](#62-device-information)

    - [*6.2.1 Device information registration*](#621-device-information-registration)

    - [*6.2.2 Device information deletion*](#622-device-information-deletion)

  - [*6.3 Leaf*](#63-leaf)

    - [*6.3.1 Leaf addition*](#631-leaf-addition)

    - [*6.3.2 Leaf modification*](#632-leaf-modification)

    - [*6.3.3 Leaf deletion*](#633-leaf-deletion)

  - [*6.4 Spine*](#64-spine)

    - [*6.4.1 Spine addition*](#641-spine-addition)

    - [*6.4.2 Spine deletion*](#642-spine-deletion)

  - [*6.5 Interface*](#65-interface)

    - [*6.5.1 Physical IF modification*](#651-physical-if-modification)

    - [*6.5.2 breakoutIF addition*](#652-breakoutif-addition)

    - [*6.5.3 breakoutIF deletion*](#653-breakoutif-deletion)

    - [*6.5.4 LagIF addition*](#654-lagif-addition)

    - [*6.5.5 LagIF deletion*](#655-lagif-deletion)

  - [*6.6 edge-point*](#66-edge-point)

    - [*6.6.1 edge-point registration*](#661-edge-point-registration)

    - [*6.6.2 edge-point deletion*](#662-edge-point-deletion)

- [*7. Detailed information display*](#7-detailed-information-display)

  - [*7.1 Cluster information*](#71-cluster-information)

  - [*7.2 Empty physical IF information*](#72-empty-physical-if-information)

  - [*7.3 IF information*](#73-if-information)

    - [*7.3.1 Physical IF information*](#731-physical-if-information)

    - [*7.3.2 breakoutIF information*](#732-breakoutif-information)

    - [*7.3.3 LagIF information*](#733-lagif-information)

  - [*7.4 Logical port information*](#74-logical-port-information)

  - [*7.5 Link information*](#75-link-information)

    - [*7.5.1 Inter-cluster link IF information*](#751-inter-cluster-link-if-information)

    - [*7.5.2 Intra-cluster link IF information*](#752-intra-cluster-link-if-information)

  - [*7.6 Slice information*](#76-slice-information)

    - [*7.6.1 L2Slice information*](#761-l2slice-information)

    - [*7.6.2 L3Slice information*](#762-l3slice-information)

  - [*7.7 Node information*](#77-node-information)

  - [*7.8 Traffic information*](#78-traffic-information)

    - [*7.8.1 IF traffic information*](#781-if-traffic-information)

    - [*7.8.2 CP traffic information*](#782-cp-traffic-information)

  - [*7.9 Failure information*](#79-failure-information)

    - [*7.9.1 Node failure information*](#791-node-failure-information)

    - [*7.9.2 IF failure information*](#792-if-failure-information)

    - [*7.9.3 Slice failure information*](#793-slice-failure-information)

  - [*7.10 Controller information*](#710-controller-information)

- [*8. Notification*](#8-notification)

  - [*8.1 Failure notification*](#81-failure-notification)

  - [*8.2 CPU use rate threshold excess notification*](#82-cpu-use-rate-threshold-excess-notification)


## Revision History

  |Version  |Date           |Contents     |
  |:--------|:--------------|:------------|
  |1.0      |Mar.28.2018      |First edition|

## 1. Overview

### 1.1 Introduction
This manual describes the operation procedures of MSF controller with MSF controller GUI tool (hereinafter it is referred to as "GUI tool").

GUI tool provides the Web screen for information acquisition and settings via Northbound IF of MFC and FC (if no MFC).

![](media/1.abstract/1-1.png "Overview")  
**Figure 1-1 Overview**

### 1.2 Conditions of GUI tool operation
Set the GUI tool on the arbitrary Web server and access to it through Web browser.<br>
The GUI tool runs on Google Chrome ver. 64 or later.

### 1.3 Preparation of GUI tool environment

Set the GUI tool on Web server and prepare the environment for GUI tool operation through Web browser on MSF GUI client terminal.

The following is a sample screenshot of the GUI tool on the Web.

![](media/1.abstract/1-2.png "MSF GUI and MFC configuration")  
**Figure 1-2 MSF GUI and MFC configuration**

The procedures to publish the GUI tool on the web are as follows.
1. Config Editing
2. Web server publishing

#### 1.3.1 Config Editing
Edit the config of GUI tool to connect the tool with MFC.
1. Decompress tar file "msf-controllerGUI.tar.gz" of GUI tool to the arbitrary location.
2. Open "js/Config.js" with text editor.
3. Modify the REST communication class setting "HOST:" of "Config.js" to MFC management server address:port.

**Config.js**
```
Rest: {
    MFC: {
        HOST: "http://192.168.56.150:20000",
    ~ Omit ~    
    },
```

#### 1.3.2 Web server publishing
This section gives the procedures of GUI tool publishing.
1. Deploy a packages prepared in the step [1.3.1 Config Editing](#131-config-editing) in the publishing folder on the Web server.
2. Start Web server (e.g. Apache httpd).

## 2. Login
### 2.1 Login screen
Log in by entering your user name and password.

![](media/2.login/2-1.png "Login screen")  
**Figure 2-1 Login screen**

**Table 2-1 Login screen**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |Username    |Enter the user name.|
|2    |Password    |Enter the password.|
|3    |Login button  |Check username and password. Transit to the Main screen if authenticated.|

### 2.2 Login user
Usernames and passwords that are available for login are as follows.

**Table 2-2 Login user type**

|No.  |User name and password                     |User type(displayed on Web browser screen)|
|----:|:------------------------------------------|:-----------------------------------------|
|1    |User name: "a"<br>Password: "a"            |FirstB Administrator|
|2    |User name: "user1"<br>Password: "user1"    |FirstB Monitoring Staff|
|3    |User name: "user2"<br>Password: "user2"    |FirstB Device Maintainer|
|4    |User name: "user3"<br>Password: "user3"    |FirstB SO Staff|
|5    |User name: "user4"<br>Password: "user4"    |MiddleB Administrator|
|6    |User name: "user5"<br>Password: "user5"    |MiddleB Monitoring Staff|
|7    |User name: "user6"<br>Password: "user6"    |MiddleB SO Staff|
|8    |User name: "debug\_"<br>Password: "debug\_"|Developer|  
\* It is assumed that FirstB indicates telecommunication infrastructure provider.  
\* It is assumed that MiddleB indicates ISP provider.

## 3. Main screen
Main screen is a screen displayed after login.

### 3.1 Screen layout
![](media/3.main/3-1.png "Login screen")  
**Figure 3-1 Main screen**

**Table 3-1 Main screen**

|No.  |Item        |Description|
|----:|:-------------|:------------|
|1    |Side menu |Select the menu to be operated.<br> Displayed menu is different depending on the authority of your account.|
|2    |Tab          |Switch the display of Figure area between Network display mode and Controller display mode.|
|3    |Menu bar   |Menu bar is displayed when Figure area is network display mode. Switch the screen and select the Slice to be displayed.|
|4    |Login information     |It displays the information of logged in user. <br> When Logout button is pressed, the Main screen is closed to transit to the login screen.|
|5    |Figure area      |It displays the figure that describes the network topology and the node status. It displays the multi-fabric network structure when logged in.|
|6    |Detailed screen       | The information on the selected figure in Figure area is displayed. The Detailed screen is described in [7. Detailed information display](#7-detailed-information-display).|

### 3.2 Network display mode
When Network display mode is selected with tab, the Figure area switches to the network display screen.

![](media/3.main/3-2.png "Tab (network display mode)")  
**Figure 3-2 Tab (network display mode)**

Network display screen switches between the following 3 types of View displays by menu bar operation.

- Multi-cluster display mode
- Fabric network display mode
- Node display mode

#### 3.2.1 Multi-cluster display mode
It displays the multi-cluster network topology.
Each object indicates the abstracted fabric network,respectively.

![](media/3.main/3-3.png "Multi-cluster display screen")  
**Figure 3-3 Multi-cluster display screen**

**Table 3-2 Multi-cluster display screen**

|No.  |Item               |Description|
|----:|:-----------------|:------------|
|1    |Map menu     |It displays the multi-fabric network topology.|
|2    |Cluster menu   |It displays the number of registered clusters. <br> The screen is transit to Fabric network display screen of the specified cluster by selecting the submenu.|
|3    |Equipment menu       |It is not available for Multi-cluster display screen|
|4    |L2 Slice menu |It displays the number of currently registered L2 Slices.<br>Submenu displays the icon indicating Slice display color, Slice name and the number of CPs.|
|5    |L3 Slice menu |It displays the number of currently registered L3 Slices.<br>Submenu displays the icon indicating Slice display color, Slice name and the number of CPs.|
|6    |Cluster          |Cluster is shown with a hexagonal figure.<br>Exclamation-triangle icon is displayed when there is failure information in the cluster.|
|7    |CP                |CP is shown with a circle under the hexagon. The circles will be omitted for the sixth CP and over.|
|8    |Inter-cluster link   |Inter-cluster link is shown with lines connected between the hexagons.|
|9    |Slice          |Slice information is shown with polygon surrounding the cluster figure when you select the display-on with L2/L3 Slice menu. <br>Only the Slices that one or more CPs belong to can be displayed on this screen.<br>Exclamation-triangle icon is displayed when there is Slice failure information. The maximum number of Slices that can be displayed is 5 for L2/L3 respectively. If the Slices belongs to multiple clusters, the Slice ID is shown at the center of the figure. In the case of the Slice with a single Slice, it displays the Slice ID in the upper right of the figure.|

#### 3.2.2 Fabric network display mode
It displays the fabric network topology.

![](media/3.main/3-4.png "Fabric network display screen")  
**Figure 3-4 Fabric network display screen**

**Table 3-3 Fabric network display screen**

|No.  |Item              |Description|
|----:|:-----------------|:------------|
|1    |Map menu     |The screen is transit to Multi-cluster display screen by selecting this menu.|
|2    |Cluster menu   |It displays the number of currently registered clusters.<br>The screen is transit to Fabric network display screen of the specified cluster by selecting this submenu.|
|3    |Equipment menu       |It displays the number of currently registered nodes.<br>The screen is transit to Node display screen of the specified node by selecting this menu.|
|4    |L2 Slice menu |It displays the number of currently registered L2 Slices.<br>The number after the menu name shows the number of L2Slices on the cluster that the node belongs to.|
|5    |L3 Slice menu |It displays the number of currently registered L3 Slices.<br>The number after the menu name shows the number of L3Slices on the cluster that the node belongs to.|
|6    |Slice          |Slice information is displayed.<br> Only Leaf that has CP belongs to the selected Slice is displayed.<br> Exclamation-triangle icon is displayed when there is Slice failure information.|
|7    |Topology          |It displays the network topology. Info-circle icon is displayed for the node when the node is not running.<br>The node is shown with red frame border when there is node failure information.<br>Exclamation-triangle icon is displayed for the node when there is IF failure information.|

#### 3.2.3 Node display mode
It displays the front view and the rear view of node.
It displays both the front view and the rear view of switch used in fabric network.

![](media/3.main/3-5.png "Node display screen")  
**Figure 3-5 Node display screen**

The interfaces are shown with 4 colors for physical IF, breakout IF, Lag IF and unused physical IF.<br>
Exclamation-triangle icon is displayed when there is IF failure information.

**Table3-4 Node display screen**

|No.  |Item               |Description|
|----:|:-----------------|:------------|
|1    |Map menu     |The screen is transit to Multi-cluster display screen by selecting the menu.|
|2    |Cluster menu   |It displays the number of currently registered clusters.<br>The screen is transit to Fabric network display screen of the specified cluster by selecting the submenu.|
|3    |Equipment menu       |It displays the number of currently registered nodes.<br>The screen is transit to Node display screen of the specified node by selecting the submenu.|
|4    |L2 Slice menu |It cannot be selected on the Node display screen. The number after the menu name shows the number of L2Slices on the cluster that the node belongs to.|
|5    |L3 Slice menu |It cannot be selected on the Node display screen. The number after the menu name shows the number of L3Slices on the cluster that the node belongs to.|
|6    |Front view            |It displays the front view of the node.<br> Red frame border is shown for the Front view when there is a node failure.|
|7    |Rear view            |It displays the rear view of the node.|
|8    |Port     |It displays the port of physical IF.|

### 3.3 Controller display mode
Figure area transits to Controller display screen when the Controller display mode tab is selected.

![](media/3.main/3-6.png "Tab (Controller display mode)")  
**Figure 3-6 Tab (Controller display mode)**

#### 3.3.1 Controller display mode(MFC, FC, EC, EM)
The controllers (MFC, FC, EC, EM) are shown in the figure below.

![](media/3.main/3-7.png "Controller display screen")  
**Figure 3-7 Controller display screen**

**Table 3-5 Controller display screen**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |Controller |It displays the controllers (MFC, FC, EC, EM) as squares.|

## 4. Monitoring menu
The following monitoring menus can be selected from the side menu of Main screen.

- [Traffic display](#41-traffic-display)
- [Controller status display](#42-controller-status-display)

### 4.1 Traffic display
When you press the Show Traffic menu button, the button turns blue and the periodic collection of traffic information from MFC starts.<br>
When you press the same button again, the traffic information collection stops.

![](media/4.monitor/4-1.png "Show Traffic menu button")  
**Figure 4-1 Show Traffic menu button**

On the Multi-cluster screen, the inter-cluster link lines are shown with animation and the color of the lines changes based on the traffic volume during the traffic information collection.<br>

![](media/4.monitor/4-2.png "Multi-cluster display screen (during traffic information collection)")  
**Figure 4-2 Multi-cluster display screen (during traffic information collection)**

On the Fabric network screen, the intra-cluster link lines of the topology layer (lines between Leafs and Spines) are shown with animation and the color of the lines changes based on the IF traffic volume during traffic information collection. CP lines of the Slice layer (outbound Leaf line) are also shown with animation and the color of the lines changes based on the CP traffic volume.

![](media/4.monitor/4-3.png "Fabric network display screen (during traffic information collection)")  
**Figure 4-3 Fabric network display screen (during traffic information collection)**

### 4.2 Controller status display
When you press the Controller Info menu button, the button turns blue and the periodic collection of controller status from MFC starts.<br>
When you press the same button again, the controller status collection stops.

![](media/4.monitor/4-4.png "Controller Info menu button")  
**Figure 4-4 Controller Info menu button**


![](media/4.monitor/4-5.png "Controller status display screen (during controller status collection)")  
**Figure 4-5 Controller status display screen (during controller status collection)**

## 5. Service setting menu
The following service setting menus can be selected from the side menu of Main screen.

- [Slice addition](#511-slice-addition)
- [Slice modification](#512-slice-modification)
- [Slice deletion](#513-slice-deletion)
- [CP addition](#521-cp-addition)
- [CP modification](#522-cp-modification)
- [CP deletion](#523-cp-deletion)

### 5.1 Slice

#### 5.1.1 Slice addition
Add a Slice.<br>
When you press the Slice add menu button, Select Slice Type dialog box opens.

![](media/5.service/5-1.png "Slice add menu button")  
**Figure 5-1 Slice add menu button**

When you select the "Slice Type" (L2 Slice / L3 Slice) on the Select Slice Type dialog box and press the select button, Slice Add dialog box opens.

![](media/5.service/5-2.png "Select Slice Type dialog box ")  
**Figure 5-2 Select Slice Type dialog box**

When you enter the parameters on the Slice Add dialog box and press the deploy button, Slice addition request is sent to MFC.

![](media/5.service/5-3.png "Slice Add dialog box")  
**Figure 5-3 Slice Add dialog box**


#### 5.1.2 Slice modification
Modify the remark menu value of the Slice.<br>
When you select a Slice and press the Slice modify menu button, Slice Modify dialog box opens.

![](media/5.service/5-4.png "Slice modify menu button")  
**Figure 5-4 Slice modify menu button**

When you press the deploy button on the Slice Modify dialog box, Slice modification request is sent to MFC.

![](media/5.service/5-5.png "Slice Modify dialog box")  
**Figure 5-5 Slice Modify dialog box**

#### 5.1.3 Slice deletion
Delete a Slice.<br>
When you select a Slice on Multi-cluster display screen or Fabric network display screen and press the Slice delete menu button, Slice Delete dialog box opens.

![](media/5.service/5-6.png "Slice delete menu button")  
**Figure 5-6 Slice delete menu button**

When you press the deploy button on the Slice Delete dialog box, Slice deletion request is sent to MFC.

![](media/5.service/5-7.png "Slice Delete dialog box")  
**Figure 5-7 Slice Delete dialog box**

### 5.2 CP
#### 5.2.1 CP addition
Add a CP.<br>
When you press the CP add menu button, Create CP Option dialog box opens.
When you input the parameters and press the deploy button on the Create CP Option dialog box, CP add request is sent to MFC.  
\*When you press the up/down-pointing triangle on the dialog box, the CP add parameter input field opens or closes. Multiple items can be inputted.

![](media/5.service/5-8.png "CP add menu button")  
**Figure 5-8 CP add menu button**

When you select the option on the Create CP Option dialog box and press the select button, CP Add dialog box opens.

![](media/5.service/5-9.png "Create CP Option dialog box (Slice TypeL2)")  
**Figure 5-9 Create CP Option dialog box (Slice TypeL2)**

![](media/5.service/5-10.png "Create CP Option dialog box (Slice TypeL3)")  
**Figure 5-10 Create CP Option dialog box (Slice TypeL3)**


![](media/5.service/5-11.png "CP Add dialog box (L2CP)")  
**Figure 5-11 CP Add dialog box (e.g. L2CP)**

#### 5.2.2 CP modification
Modify the QoS option of a CP.<br>
When you select a Slice and press the CP modify menu button, CP Modify dialog box opens.

![](media/5.service/5-12.png "CP modify menu button")  
**Figure 5-12 CP modify menu button**

When you enter the parameters on the CP Modify dialog box and press the deploy button, CP modification request is sent to MFC.

![](media/5.service/5-13.png "CP Modify dialog box")  
**Figure 5-13 CP Modify dialog box**

#### 5.2.3 CP deletion
Delete a CP.<br>
When you select a Slice and press the CP delete menu button, CP Delete dialog box opens.

![](media/5.service/5-14.png "CP delete menu button")  
**Figure 5-14 CP delete menu button**

When you enter the parameters on the CP Delete dialog box and press the deploy button, CP deletion request is sent to MFC.

![](media/5.service/5-15.png "CP Delete dialog box")  
**Figure 5-15 CP Delete dialog box**

## 6. Infrastructure setting menu
The following service setting menus can be selected from the side menu of Main screen.

- [Cluster addition](#611-cluster-addition)
- [Cluster deletion](#612-cluster-deletion)
- [Inter-cluster link IF addition](#613-inter-cluster-link-if-addition)
- [Inter-cluster link IF deletion](#614-inter-cluster-link-if-deletion)
- [Device information registration](#621-device-information-registration)
- [Device information deletion](#622-device-information-deletion)
- [Leaf addition](#631-leaf-addition)
- [Leaf modification](#632-leaf-modification)
- [Leaf deletion](#633-leaf-deletion)
- [Spine addition](#641-spine-addition)
- [Spine deletion](#642-spine-deletion)
- [Physical IF modification](#651-physical-if-modification)
- [breakoutIF addition](#652-breakoutif-addition)
- [breakoutIF deletion](#653-breakoutif-deletion)
- [LagIF addition](#654-lagif-addition)
- [LagIF deletion](#655-lagif-deletion)
- [edge-point registration](#661-edge-point-registration)
- [edge-point deletion](#662-edge-point-deletion)

### 6.1 Cluster
#### 6.1.1 Cluster addition
Add a cluster.<br>
When you press the Cluster add menu button, SW Cluster Add dialog box opens.

![](media/6.infrastructure/6-1.png "Cluster add menu button")  
**Figure 6-1 Cluster add menu button**

When you enter the parameters on the SW Cluster Add dialog box and press the deploy button, SW cluster addition request is sent to MFC.

![](media/6.infrastructure/6-2.png "SW Cluster Add dialog box")  
**Figure 6-2 SW Cluster Add dialog box**

#### 6.1.2 Cluster deletion
Delete a cluster.<br>
When you select a cluster on the Multi-cluster display screen and press the Cluster delete menu button, SW Cluster Delete dialog box opens.

![](media/6.infrastructure/6-3.png "Cluster delete menu button")  
**Figure 6-3 Cluster delete menu button**

When you press the deploy button on the SW Cluster Delete dialog box, SW cluster deletion request is sent to MFC.

![](media/6.infrastructure/6-4.png "SW Cluster Delete dialog box")  
**Figure 6-4 SW Cluster Delete dialog box**

#### 6.1.3 Inter-cluster link IF addition
Add an inter-cluster link IF.<br>
When you select a cluster and press the Cluster add link menu button, Select IF Type dialog box opens.

![](media/6.infrastructure/6-5.png "Cluster add link menu button")  
**Figure 6-5 Cluster add link menu button**

When you select the IF Type and press the select button on the Select IF Type dialog box, Inter-Cluster Link Add dialog box opens.

![](media/6.infrastructure/6-6.png "Select IF Type dialog box")  
**Figure 6-6 Select IF Type dialog box**

When you press the deploy button on the Inter-Cluster Link Add dialog box, inter-cluster link IF addition request is sent to MFC.

![](media/6.infrastructure/6-7.png "Inter-Cluster Link Add dialog box (PhysicalIF)")  
**Figure 6-7 Inter-Cluster Link Add dialog box (PhysicalIF)**

![](media/6.infrastructure/6-8.png "Inter-Cluster Link Add dialog box (breakoutIF)")  
**Figure 6-8 Inter-Cluster Link Add dialog box (breakoutIF)**

![](media/6.infrastructure/6-9.png "Inter-Cluster Link Add dialog box (LagIF)")  
**Figure 6-9 Inter-Cluster Link Add dialog box (LagIF)**

#### 6.1.4 Inter-cluster link IF deletion
Delete an inter-cluster link IF.<br>
When you select an inter-cluster link IF and press the Cluster delete link menu button, ClusterLinkIfs Delete dialog box opens.

![](media/6.infrastructure/6-10.png "Cluster delete link menu button")  
**Figure 6-10 Cluster delete link menu button**

When you press the deploy button on the ClusterLinkIfs Delete dialog box, inter-cluster link IF deletion request is sent to MFC.

![](media/6.infrastructure/6-11.png "ClusterLinkIfs Delete dialog box")  
**Figure 6-11 ClusterLinkIfs Delete dialog box**

### 6.2 Device information
#### 6.2.1 Device information registration
Register the device information.<br>
When you press the Device add menu button, Device Info Add dialog box opens.

![](media/6.infrastructure/6-12.png "Device add menu button")  
**Figure 6-12 Device add menu button**

When you select the device information to be registered on the Device Info Add dialog box and press the deploy button, the device information registration request is sent to MFC.

![](media/6.infrastructure/6-13.png "Device Info Add dialog box")  
**Figure 6-13 Device Info Add dialog box**

#### 6.2.2 Device information deletion
Delete the registered device information.<br>
When you press the Device delete menu button, Select Device (Select capability conditions) dialog box opens.

![](media/6.infrastructure/6-14.png "Device delete menu button")  
**Figure 6-14 Device delete menu button**

When you designate the parameters on the Select Device (Select capability conditions) dialog box and press the device search button, Select Device (Select device) dialog box opens.

![](media/6.infrastructure/6-15.png "Select Device dialog box")  
**Figure 6-15 Select Device (Select capability conditions) dialog box**

When you select the device information on the Select Device (Select device) dialog box and press the select button, Device Info Delete dialog box opens.

![](media/6.infrastructure/6-16.png "device information selection dialog box")  
**Figure 6-16 Select Device (Select device) dialog box**

When you press the deploy button on the Device Info Delete dialog box, device information deletion request is sent to MFC.

![](media/6.infrastructure/6-17.png "Device Info Delete dialog box")  
**Figure 6-17 Device Info Delete dialog box**

### 6.3 Leaf
#### 6.3.1 Leaf addition
Add a Leaf.<br>
When you press the Leaf add menu button on the Fabric network display screen, Select Device dialog box opens.

![](media/6.infrastructure/6-18.png "Leaf add menu button")  
**Figure 6-18 Leaf add menu button**

When you designate the parameters on the Select Device (Select capability conditions) dialog box and press the device search button, Select Device (Select device) dialog box opens.

![](media/6.infrastructure/6-19.png "Select Device (Select capability conditions) dialog box")  
**Figure 6-19 Select Device (Select capability conditions) dialog box**

When you select the device information on the Select Device (Select device) dialog box and press the select button, Leaf Add dialog box opens.

![](media/6.infrastructure/6-20.png "Device information selection dialog box")  
**Figure 6-20 Select Device (Select device) dialog box**

When you enter the parameters on the Leaf Add dialog box and press the deploy button, Leaf addition request is sent to MFC.  
\*When you press the up/down-pointing triangle icon on the screen, the entry field for variable number of parameters opens in order to enter multiple parameters.

![](media/6.infrastructure/6-21.png "Leaf Add dialog box")  
**Figure 6-21 Leaf Add dialog box**

#### 6.3.2 Leaf modification
Modify a Leaf.<br>
When you select a Leaf on the Fabric network display screen and press the Leaf modify menu button, Leaf Modify dialog box opens.

![](media/6.infrastructure/6-22.png "Leaf modify menu button")  
**Figure 6-22 Leaf modify menu button**

When you enter the parameters on the Leaf Modify dialog box and press the deploy button, Leaf modification request is sent to MFC.

![](media/6.infrastructure/6-23.png "Leaf Modify dialog box")  
**Figure 6-23 Leaf Modify dialog box**

#### 6.3.3 Leaf deletion
Delete a Leaf.<br>
When you select a Leaf on the Fabric network display screen and press the Leaf delete menu button, Leaf Delete dialog box opens.

![](media/6.infrastructure/6-24.png "Leaf delete menu button")  
**Figure 6-24 Leaf delete menu button**

When you press the deploy button on the Leaf Delete dialog box, Leaf deletion request is sent to MFC.

![](media/6.infrastructure/6-25.png "Leaf Delete dialog box")  
**Figure 6-25 Leaf Delete dialog box**

### 6.4 Spine
#### 6.4.1 Spine Addition
Add a Spine.<br>
When you press the Spine add menu button on the Fabric network display screen, Select Device (Select capability conditions) dialog box opens.

![](media/6.infrastructure/6-26.png "Spine add menu button")  
**Figure 6-26 Spine add menu button**

When you designate the parameters on the Select Device (Select capability conditions) dialog box and press the device search button, Select Device (Select device) dialog box opens.

![](media/6.infrastructure/6-27.png "Select Device dialog box")  
**Figure 6-27 Select Device (Select capability conditions) dialog box**

When you select the device information on the Select Device (Select device) dialog box and press the select button, Spine Add dialog box opens.

![](media/6.infrastructure/6-28.png "device information selection dialog box")  
**Figure 6-28 Select Device (Select device) dialog box**

When you enter the parameters on the Spine Add dialog box and press the deploy button, Spine addition request is sent to MFC.  
\*When you press the up/down-pointing triangle icon on the screen, the entry field for variable number of parameters opens in order to enter multiple parameters.

![](media/6.infrastructure/6-29.png "Spine Add dialog box")  
**Figure 6-29 Spine Add dialog box**

#### 6.4.2 Spine deletion
Delete a Spine.<br>
When you select a Spine on the Fabric network display screen and press the Spine delete menu button, Spine Delete dialog box opens.

![](media/6.infrastructure/6-30.png "Spine delete menu button")  
**Figure 6-30 Spine delete menu button**

When you press the deploy button on the Spine Delete dialog box, Spine deletion request is sent to MFC.

![](media/6.infrastructure/6-31.png "Spine Delete dialog box")  
**Figure 6-31 Spine Delete dialog box**

### 6.5 Interface
#### 6.5.1 Physical IF modification
Modify the physical IF information.<br>
When you select a node (Leaf/Spine) on the Fabric network display screen and press the Physical IF modify menu button, physicalIF Modify dialog box opens.

![](media/6.infrastructure/6-32.png "Physical IF modify menu button")  
**Figure 6-32 Physical IF modify menu button**

When you enter the parameters on the physicalIF Modify dialog box and press the deploy button, physical IF modification request is sent to MFC.

![](media/6.infrastructure/6-33.png "physicalIF Modify dialog box")  
**Figure 6-33 physicalIF Modify dialog box**

#### 6.5.2 breakoutIF addition
Add the breakoutIF information.<br>
When you select a node (Leaf/Spine) on the Fabric network display screen and press the BreakoutIF add menu button, Breakout IFs Add dialog box opens.

![](media/6.infrastructure/6-34.png "BreakoutIF add menu button")  
**Figure 6-34 BreakoutIF add menu button**

When you enter the parameters on the Breakout IFs Add dialog box and press the deploy button, breakoutIF addition request is sent to MFC.

![](media/6.infrastructure/6-35.png "Breakout IFs Add dialog box")  
**Figure 6-35 Breakout IFs Add dialog box**

#### 6.5.3 breakoutIF deletion
Delete the breakoutIF information.<br>
When you select a node (Leaf/Spine) on the Fabric network display screen and press the BreakoutIF delete menu button, Breakout IFs Delete dialog box opens.

![](media/6.infrastructure/6-36.png "BreakoutIF delete menu button")  
**Figure 6-36 BreakoutIF delete menu button**

When you enter the parameters on the Breakout IFs Delete dialog box and press the deploy button, breakoutIF deletion request is sent to MFC.

![](media/6.infrastructure/6-37.png "Breakout IFs Delete dialog box")  
**Figure 6-37 Breakout IFs Delete dialog box**

#### 6.5.4 LagIF addition
Add the LagIF information.<br>
When you select a node (Leaf/Spine) on the Fabric network display screen and press the Lag IF add menu button, LagIF Add dialog box opens.

![](media/6.infrastructure/6-38.png "Lag IF add menu button")  
**Figure 6-38 Lag IF add menu button**

When you enter the parameters on the LagIF Add dialog box and press the deploy button, LagIF addition request is sent to MFC.

![](media/6.infrastructure/6-39.png "LagIF Add dialog box")  
**Figure 6-39 LagIF Add dialog box**

#### 6.5.5 LagIF deletion
Delete the LagIF information.<br>
When you select a node (Leaf/Spine) and press the Lag IF delete menu button, LagIF Delete dialog box opens.

![](media/6.infrastructure/6-40.png "Lag IF delete menu button")  
**Figure 6-40 Lag IF delete menu button**

When you enter the parameters on the LagIF Delete dialog box and press deploy button, LagIF deletion request is sent to MFC.

![](media/6.infrastructure/6-41.png "LagIF Delete dialog box")  
**Figure 6-41 LagIF Delete dialog box**

### 6.6 edge-point
#### 6.6.1 edge-point registration
Register an edge-point.<br>
When you select a node (Leaf) on the Fabric network display screen and press the Edge Point add menu button, Edge-Point Add dialog box opens.

![](media/6.infrastructure/6-42.png "Edge Point add menu button")  
**Figure 6-42 Edge Point add menu button**

When you enter the parameters on the Edge-Point Add dialog box and press the deploy button, edge-point registration request is sent to MFC.

![](media/6.infrastructure/6-43.png "Edge-Point Add dialog box")  
**Figure 6-43 Edge-Point Add dialog box**

#### 6.6.2 edge-point deletion
Delete an edge-point.<br>
When you select a node (Leaf) on the Fabric network display screen and press the Edge Point delete menu button, Edge-Point Delete dialog box opens.

![](media/6.infrastructure/6-44.png "Edge Point delete menu button")  
**Figure 6-44 Edge Point delete menu button**

When you enter the parameters on the Edge-Point Delete dialog box and press the deploy button, edge-point deletion request is sent to MFC.

![](media/6.infrastructure/6-45.png "Edge-Point Delete dialog box")  
**Figure 6-45 Edge-Point Delete dialog box**

## 7. Detailed information display
It displays the detailed information based on the information selected at the Figure area on the Main screen.

- [Cluster information](#71-cluster-information)
- [Empty physical IF information](#72-empty-physical-if-information)
- [IF information](#73-if-information)
- [Logical port information](#74-logical-port-information)
- [Link information](#75-link-information)
- [Slice information](#76-slice-information)
- [Node information](#77-node-information)
- [Traffic information](#78-traffic-information)
- [Failure information](#79-failure-information)
- [Controller information](#710-controller-information)

### 7.1 Cluster information
Select the Network display mode tab.<br>
Press the Cluster toggle button on the upper part of the detailed screen.<br>
Cluster information will be displayed based on the selected information.

![](media/7.detail/7-1.png "List of cluster information")  
**Figure 7-1 List of cluster information**

**Table 7-1 List of cluster information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID.|
|2    |as number  |It displays the AS number of the cluster.|
|3    |L2 enable  |It displays the availability of the L2CP supported by the cluster.<br> true/false|
|4    |L3 support |It displays the L3CP protocols supported by the cluster.<br> "bgp": BGP (Border Gateway Protocol) <br> "static": static route <br> "vrrp": VRRP (Virtual Router Redundancy Protocol)|

**Table 7-2 Display condition of cluster information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:--------------------|:------------|
|Multi-cluster display screen            |Not selected              |It displays the information of all clusters.|
|                                |If a cluster is selected         |It displays the information of the selected cluster.|
|                                |If an Inter-cluster link is selected  |(No display)|
|                                |If a Slice is selected         |(No display)|
|Fabric network display screen   |Not selected              |It displays the cluster information that the displayed Fabric network belongs to.|
|                                |If a topology is selected       |Same as above|
|                                |If a node on the topology is selected|Same as above|
|                                |If a Slice is selected         |Same as above|
|                                |If a node on the Slice is selected  |Same as above|
|Node display screen                   |Not selected               |It displays the information of the cluster that the displayed node belongs to.|
|                                |If a node is selected             |Same as above|
|                                |If a port is selected           |Same as above|

### 7.2 Empty physical IF information
Select the Network display mode tab.<br>
Press the Empty toggle button on the upper part of the detailed screen.<br>
List of physical IFs whose speed is unset will be displayed based on the selected information.

![](media/7.detail/7-2.png "List of empty physical IF information")  
**Figure 7-2 List of empty physical IF information**

**Table 7-3 List of empty physical IF information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id  |It displays the cluster ID of the cluster that the node who has the physical IF belongs to.|
|2    |node id     |It displays the node ID of the node who has the physical IF.|
|3    |if id       |It displays the IF ID of the physical IF.|
|4    |if slot     |Blank fixed.|

**Table 7-4 Display condition of empty physical IF information list**

|Screen                            |Condition of selection              |displayed content|
|:-------------------------------|:--------------------|:------------|
|Multi-cluster display screen            |Not selected              |(No display)|
|                                |If a cluster is selected         |(No display)|
|                                |If an inter-cluster link is selected  |(No display)|
|                                |If a Slice is selected         |(No display)|
|Fabric network display screen   |Not selected              |It displays the ports of all nodes in the cluster.|
|                                |If a topology is selected       |Same as above|
|                                |If a node on the topology is selected|It displays only ports of the selected node.|
|                                |If a Slice is selected         |It displays only ports of the node that belongs to(has one or more CPs on) the selected Slice.|
|                                |If a node on the Slice is selected |It displays only ports of the selected node.|
|Node display screen                   |Not selected              |It displays the ports of the displayed node.|
|                               |If a node is selected             |It displays the ports of the selected node.
|                               |If a port is selected           |It displays the selected port information|

### 7.3 IF information
Select the Network display mode tab.<br>
Press the Physical toggle button on the upper part of the detailed screen.<br>
List of physical IF information, breakoutIF information and LagIF information will be displayed based on the selected information.
#### 7.3.1 Physical IF information

![](media/7.detail/7-3.png "List of physical IF information")  
**Figure 7-3 List of physical IF information**

**Table 7-5 List of physical IF information**

|No.  |Item                   |Description|
|----:|:----------------------|:------------|
|1    |cluster id             |It displays the cluster ID of the cluster that the node who has the physical IF belongs to.|
|2    |node id                |It displays the node ID of the node who has the physical IF.|
|3    |if id                  |It displays the IF ID of the physical IF.|
|4    |if slot                |It displays the SLOT number of the physical IF.|
|5    |remark capability      |It displays the selectable remark menu list.|
|6    |shaping capability     |It displays the availability of the shaping.<br>available / -|
|7    |egress queue capability|It displays the selectable Egress queue rate menu list.|
|8    |uses                   |It displays the usage information of the physical IF.<br>breakout IF: Split into breakoutIFs. <br>LagIF: Used as member link of LAG<br>Intra-Cluster Link: Used for intra-cluster link<br>Inter-Cluster Link: Used for inter-cluster link<br>edge point: Used for edge-point<br>-: None of the above applies|

**Table 7-6 Display condition of physical IF information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays the ports of all nodes in the cluster.|
|                                |If a topology is selected |Same as above|
|                                |If a node on the topology is selected|It displays only ports of the selected node.|
|                                |If a Slice is selected|It displays only ports of the node that belongs to(has one or more CPs on) the selected Slice.|
|                                |If a node on the Slice is selected|It displays only ports of the selected node.|
|Node display screen                   |Not selected       |It displays the ports of the displayed node.|
|                               |If a node is selected |It displays the ports of the selected node.
|                               |If a port is selected|It displays the information of the selected port.|


#### 7.3.2 breakoutIF information

![](media/7.detail/7-4.png "List of breakoutIF information")  
**Figure 7-4 List of breakoutIF information**

**Table 7-7 List of breakoutIF information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node who has the breakoutIF belongs to.|
|2    |node id |It displays the node ID of the node who has breakoutIF.|
|3    |if id |It displays the IF ID of breakoutIF.|
|4    |if name|It displays the IF name of breakoutIF.|
|5    |speed|It displays the IF speed of breakoutIF.|
|6    |remark capability|It displays the selectable remark menu list.|
|7    |shaping capability |It displays the availability of the shaping.<br> available / -|
|8    |egress queue capability| It displays the selectable Egress queue rate menu list.|
|9    |uses |It displays the usage information of breakoutIF.<br>LagIF: Used as member link of LAG<br>Intra-Cluster Link: Used for intra-cluster link<br>Inter-Cluster Link: Used for inter-cluster link<br>edge point: Used for edge-point<br>-: None of the above applies|

**Table 7-8 Display condition of breakoutIF information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected |(No display)|
|Fabric network display screen   |Not selected       |It displays the ports of all nodes in the cluster.|
|                                |If a topology is selected |Same as above|
|                                |If a node on the topology is selected|It displays only ports of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays only ports of the selected node.|
|Node display screen                   |Not selected       |It displays the port of the displayed node.|
|                                |If a node is selected |It displays the ports of the selected node.
|                                |If a port is selected|It displays the information of the selected port.|

#### 7.3.3 LagIF information

![](media/7.detail/7-5.png "List of LagIF information")  
**Figure 7-5 List of LagIF information**

**Table 7-9 List of LagIF information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node who has LagIF belongs to.|
|2    |node id |It displays the node ID of the node who has LagIF.|
|3    |if id |It displays the IF ID of LagIF.|
|4    |if name|It displays the IF name of LagIF.|
|5    |speed|It displays the IF speed of LagIF.|
|6    |min links|It displays the minimum number of the normal links, for that a LAG  is not considered as in a failure.|
|7    |member|It displays the IF ID of IF that is the member link of LAG.|
|8    |remark capability|It displays the selectable remark menu list.|
|9    |shaping capability |It displays the availability of the shaping.<br> available / -|
|10   |egress queue capability|It displays the selectable Egress queue rate menu list.|
|11   |uses |It displays the LagIF usage information.<br>Intra-Cluster Link: Used for intra-cluster link<br>Inter-Cluster Link: Used for inter-cluster link<br>edge point: Used for edge-point.<br>-: None of the above applies|

**Table 7-10 Display condition of LagIF information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays the ports of all nodes in the cluster.|
|                                |If a topology is selected |Same as above |
|                                |If a node on the topology is selected|It displays only ports of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays only ports of the selected node.|
|Node display screen                   |Not selected       |It displays the ports of the displayed node.|
|                                |If a node is selected |It displays the ports of the selected node.
|                                |If a port is selected|It displays the information of the selected port.|

### 7.4 Logical port information
Select the Network display mode tab.<br>
Press the Logical toggle button on the upper part of the detailed screen.<br>
List of edge-points will be displayed based on the selected information.

![](media/7.detail/7-6.png "List of logical port information")  
**Figure 7-6 List of logical port information**

**Table 7-11 List of logical port information**    

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node who has edge-point belongs to.|
|2    |node id |It displays the node ID of the node who has edge-point.|
|3    |if id |It displays the IF ID who has edge-point.|
|4    |edge point id|It displays the edge-point ID.|
|5    |remark capability|It displays the selectable remark menu list.|
|6    |shaping capability |It displays the availability of the shaping.<br> available / -|
|7   |egress queue capability|It displays the selectable Egress queue rate menu list.|

**Table 7-12 Display condition of logical port information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |It displays the edge-points used for the selected Slice.|
|Fabric network display screen   |Not selected       |(No display)|
|                                |If a topology is selected |It displays the edge-points of all nodes in the cluster.|
|                                |If a node on the topology is selected|It displays the edge-point of the selected node.|
|                                |If a Slice is selected|It displays the edge-points used for the selected Slice among the edge-points of all nodes in the cluster.|
|                                |If a node on the Slice is selected| It displays the edge-points used for the selected Slice among the edge-points of the selected node.|
|node display screen                   |Not selected       |It displays the edge-point of the displayed node.|
|                                |If a node is selected |It displays the edge-point of the selected node.|
|                                |If a port is selected|It displays the edge-point of the selected port.|

### 7.5 Link information
Select the Network display mode tab.<br>
Press the Link toggle button on the upper part of the detailed screen.<br>
List of inter-cluster link IF information and intra-cluster link IF information will be displayed based on the selected information.
#### 7.5.1 Inter-cluster link IF information

![](media/7.detail/7-7.png "List of inter-cluster link IF information")  
**Figure 7-7 List of inter-cluster link IF information**

**Table 7-13 List of inter-cluster link IF**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node who has the inter-cluster link IF belongs to.|
|2    |node id |It displays the node ID of the node who has the Inter-cluster link IF.|
|3    |link id |It displays the IF ID of the inter-cluster link IF.|
|4    |igp cost|It displays the IGP cost of the inter-cluster link IF.|
|5    |ipv4 addr|It displays the IPv4 address of the inter-cluster link IF.|
|6    |threshold|It displays the traffic threshold value of the inter-cluster link IF.|

**Table 7-14 Display condition of inter-cluster link IF information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |It displays all inter-cluster link IFs.|
|                                |If a cluster is selected |It displays the inter-cluster link IFs in the selected cluster. |
|                                |If an inter-cluster link is selected|It displays the inter-cluster link IFs in the selected cluster.|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays all inter-cluster link IFs in the cluster.|
|                                |If a topology is selected |Same as above|
|                                |If a node on the topology is selected|It displays the inter-cluster link IFs of the selected node.|
|                                |IF Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays the inter-cluster link IFs of the selected node.|
|Node display screen                   |Not selected       |It displays the inter-cluster link IFs of the displayed node.|
|                                |If a node is selected |It displays the inter-cluster link IFs of the selected node.
|                                |If a port is selected|It displays the inter-cluster link IF of the selected port.|

#### 7.5.2 Intra-cluster link IF information

![](media/7.detail/7-8.png "List of intra-cluster link IF information")  
**Figure 7-8 List of intra-cluster link IF information**

**Table 7-15 List of intra-cluster link IF information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node who has the intra-cluster link IF belongs to.|
|2    |node id |It displays the node ID of the node who has the intra-cluster link IF.|
|3    |link if id |It displays the link IF ID of the intra-cluster link IF.|
|4    |if id |It displays the IF ID of the IF that accommodates the intra-cluster link IF. |
|5    |opposite node id|It displays the node ID of the opposite node of the intra-cluster link IF.|
|6    |opposite link if id|It displays the intra-cluster link IF ID of the opposite node of the intra-cluster link IF.|
|7    |ipv4 addr|It displays the IPv4 address of the intra-cluster link IF.|
|8    |threshold|It displays the traffic threshold value of the intra-cluster link IF [Gbps].|

**Table 7-16 Display condition of intra-cluster link IF information list**

|Screen                            |Condition of selection       |displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays all intra-cluster link IFs in the cluster.|
|                                |If a topology is selected |Same as above|
|                                |If a node on the topology is selected|It displays the intra-cluster link IFs of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays the intra-cluster link IFs of the selected node.|
|Node display screen                   |Not selected       |It displays the intra-cluster link IFs of the displayed node.|
|                                |If a node is selected |It displays the intra-cluster link IFs of the selected node.|
|                                |If a port is selected|It displays the intra-cluster link IF of the selected port.|

### 7.6 Slice information
Select the Network display mode tab.<br>
Press the Slice toggle button on the upper part of the detailed screen.<br>
List of L2Slice information and L3Slice information will be displayed based on the selected information.

#### 7.6.1 L2Slice information

![](media/7.detail/7-9.png "List of L2Slice information")  
**Figure 7-9 List of L2Slice information**

**Table 7-17 L2Slice information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |slice id |It displays the SliceID.|
|2    |edge point id |It displays the edge-point ID of the edge-point.|
|3    |cp id |It displays the CP ID of the CP existing on the edge-point.|
|4    |vlan id |I displays the VLAN ID of the CP.<br>1 - 4096|
|5    |port mode|It displays the port mode of the CP.<br> "access": Access mode <br> "trunk": Trunk mode|
|6    |remark menu|It displays the CP remark menu if remark function is available.|
|7    |ingress shaping|it displays the inflow shaping rate of the CP [Gbps] if the shaping function is available.|
|8    |egress shaping|It displays the outflow shaping rate of the CP [Gbps] if the shaping function is available.|
|9    |egress queue|It displays the Egress queue rate menu.|
|10   |pair cp id|It displays the CP ID of the pair CP in multi-home configuration.|

**Table 7-18 Display condition of L2Slice information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |It displays all CPs of the selected Slice.|
|Fabric network display screen   |Not selected       |It displays all CPs in the cluster.|
|                                |If a topology is selected |(No display)|
|                                |If a node on the topology is selected|(No display)|
|                                |If a Slice is selected|It displays the CPs in the cluster among the all CPs of the selected Slice.|
|                                |If a node on the Slice is selected|It displays the CPs on the selected node among the all CPs of the selected Slice.|
|Node display screen                   |Not selected       |(No display)|
|                                |If a node is selected |(No display)|
|                                |If a port is selected|(No display)|

#### 7.6.2 L3Slice information

![](media/7.detail/7-10.png "List of L3Slice information")  
**Figure 7-10 L3Slice information**

**Table 7-19 List of L3Slice information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1   |slice id |It displays the SliceID.|
|2   |edge point id |It displays the edge-point ID of the edge-point.|
|3   |cp id |It displays the CP ID of the CP existing on the edge-point.|
|4   |vlan id |It displays the VLAN ID of the CP.<br>0 - 4096|
|5   |ipv4 addr |It displays the accommodated node IF address (IPv4) of the CP.|
|6   |ipv4 prefix |It displays the accommodated node IF prefix (IPv4) of the CP.<br>0 - 31|
|7   |ipv6 addr |It displays the accommodated node IF address (IPv6) of the CP.|
|8   |ipv6 prefix |It displays the accommodated node IF prefix (IPv6) of the CP.<br> 0 - 64|
|9   |support protocols |It displays the UNI connection protocol information supported by L3CP.<br>"bgp": BGP (Border Gateway Protocol)<br> "static": Static route<br> "vrrp": VRRP (Virtual Router Redundancy Protocol)|
|10   |bgp |It displays the BGP information of the L3CP.|
|11   |static route |It displays the static route information of the L3CP.|
|12   |vrrp |It displays the VRRP information of the L3CP.|
|13   |remark menu |It displays the remark menu of the CP if remark function is available.|
|14   |ingress shaping |It displays the inflow shaping rate of the CP [Gbps] if the shaping function is available.|
|15   |egress shaping |It displays the outflow shaping rate of the CP [Gbps] if the shaping function is available.|
|16   |egress queue |It displays the Egress queue rate menu of the CP.|
|17   |threshold |It displays the traffic threshold value of L3CP [Gbps].|

**Table 7-20 Display condition of L3Slice information list**

|Screen                             |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |It displays all CPs of the selected Slice.|
|Fabric network display screen   |Not selected       |It displays all CPs in the cluster.|
|                                |If a topology is selected |(No display)|
|                                |If a node on the topology is selected|(No display)|
|                                |If a Slice is selected|It displays the CPs in the cluster among the all CPs of the selected Slice.|
|                                |If a node on the Slice is selected| It displays the CPs on the selected node among the all CPs of the selected Slice.|
|Node display screen                   |Not selected       |(No display)|
|                                |If a node is selected |(No display)|
|                                |If a port is selected|(No display)|



### 7.7 Node information
Select the Network display mode tab.<br>
Press the Platform toggle button on the upper part of the detailed screen.<br>
Node addition (provisioning) status will be displayed based on the selected information.

![](media/7.detail/7-11.png "List of node information")  
**Figure 7-11 List of node information**

**Table 7-21 List of node information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node belongs to.
|2    |node id |It displays the node ID of the node.
|3    |platform |It displays the platform of the node.
|4    |status |It displays the node addition status.<br>"in-service": In service<br>"before-setting": Waiting for start of the node<br>"ztp-complete": ZTP completed<br>"node-resetting-complete": Infrastructure setting completed<br>"failure-setting": Out of order (Failure of addition)<br>"failure-node-resetting": Failure of infrastructure setting<br>"failure-service-setting": Failure of service setting<br>"failure-other": Out of order (others)<br>"failure-recover-node": Out of order (failure of addition for recovery)

**Table 7-22 Display condition of node information list**

|Screen                             |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays the information of all nodes in the cluster.|
|                                |If a topology is selected |It displays the information of all nodes in the cluster.|
|                                |If a node on the topology is selected| It displays the information of the selected node.|
|                                |If a Slice is selected|It displays the information of the nodes that belongs to the selected Slice (or has one or more CPs on the selected Slice).|
|                                |If a node on the Slice is selected|It displays the information of the selected node.|
|Node display screen                   |Not selected       |It displays the information of the displayed node.|
|                                |If a node is selected |It displays the information of the selected node.
|                                |If a port is selected|(No display)|

### 7.8 Traffic information
Select the Network display mode tab.<br>
Press the Traffic toggle button on the upper part of the detailed screen.<br>
List of IF traffic information and CP traffic information will be displayed based on the selected information.

#### 7.8.1 IF traffic information

![](media/7.detail/7-12.png "List of IF traffic information")  
**Figure 7-12 List of IF traffic information**

**Table 7-23 List of IF traffic information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id  |It displays the cluster ID of the cluster that the node belongs to.|
|2    |node id    |It displays the node ID of the node.|
|3    |if type    |It displays the type of IF.<br>"physical-if": Physical IF<br>"breakout-if": breakoutIF<br>"lag-if": LagIF|
|4    |if id      |It displays the IF ID of the IF.|
|5    |send[unit]  |It displays the traffic sending rate of the IF. <br>[Unit] is the unit (Gbps/Mbps) specified in Config.|
|6    |receive[unit] |It displays the traffic receiving rate of the IF.<br>[Unit] is the unit (Gbps/Mbps) specified in Config.|

**Table 7-24 Display condition of IF traffic information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |It displays the traffic information of the inter-cluster link IFs of the selected cluster.|
|                                |If the inter-cluster link is selected|It displays the traffic information of the selected inter-cluster link IF.|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays all IF traffic information of the nodes in the cluster.|
|                                |If a topology is selected |It displays all IF traffic information of the nodes in the cluster.|
|                                |If a node on the topology is selected|It displays the IF traffic information of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|(No display)|
|Node display screen                   |Not selected       |It displays the IF traffic information of the displayed node.|
|                                |If a node is selected |It displays the IF traffic information of the selected node.|
|                                |If a port is selected|It displays the IF traffic information of the selected port.|


#### 7.8.2 CP traffic information

![](media/7.detail/7-13.png "List of CP traffic information")  
**Figure 7-13 List of CP traffic information**

**Table 7-25 List of CP Traffic information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |slice id |It displays the Slice ID of the Slice.|
|2    |cp id |It displays the CP ID of the CP.|
|3    |send[unit] |It displays the traffic sending rate of the CP.<br>[Unit] is the unit (Gbps/Mbps) specified by Confing.|
|4    |receive[unit] |It displays the traffic receiving rate of the CP.<br>[Unit] is the unit (Gbps/Mbps) specified by CP.|

**Table 7-26 Display condition of CP traffic information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |It displays the traffic information of all CPs of the selected Slice.|
|Fabric network display screen   |Not selected       |It displays all CP traffic information of the nodes in the cluster.|
|                                |If a topology is selected |It displays all CP traffic information in the cluster.|
|                                |If a node on the topology is selected|It displays the CP traffic information of the selected node.|
|                                |If a Slice is selected|It displays the traffic information of the CP in the cluster of the selected Slice.|
|                                |If a node on the Slice is selected|It displays the traffic information of the CP that belongs to the Slice of the selected node.|
|Node display screen                   |Not selected       |It displays the CP traffic information of the displayed node.|
|                                |If a node is selected |It displays the CP traffic information of the selected node.|
|                                |If a port is selected|(No display)|

### 7.9 Failure information
Select the Network display mode tab.<br>
Press the Failure toggle button on the upper part of the detailed screen.<br>
Node failure information, IF failure information and Slice failure information will be displayed based on the selected information.
#### 7.9.1 Node failure information

![](media/7.detail/7-14.png "List of node failure information")  
**Figure 7-14 List of node failure information**

**Table 7-27 List of node failure information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node belongs to.|
|2    |node id |It displays the node ID of the node.|
|3    |status |It displays the node status.<br>up/down|

**Table 7-28 Display condition of node failure information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |It displays the node failure information of the nodes that belongs to the selected cluster.|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays the node failure information in the cluster.|
|                                |If a topology is selected |It displays the node failure information in the cluster.|
|                                |If a node on the topology is selected|It displays the node failure information of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays the node failure information of the selected node.|
|Node display screen                   |Not selected       |It displays the node failure information of the displayed node.|
|                                |If a node is selected |It displays the node failure information of the selected node.|
|                                |If a port is selected|(No display)|

#### 7.9.2 IF failure information

![](media/7.detail/7-15.png "List of IF failure information")  
**Figure 7-15 List of IF failure information**

**Table 7-29 List of IF failure information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |cluster id |It displays the cluster ID of the cluster that the node belongs to.|
|2    |node id |It displays the node ID of the failure node.|
|3    |if id |It displays the IF ID of the failure IF.|
|4    |status |It displays the failure status.|

**Table 7-30 Display condition of IF failure information list**

|Screen                             |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |(No display)|
|                                |If a cluster is selected |It displays the IF failure information of the nodes that belongs to the selected cluster.|
|                                |If an inter-cluster link is selected|It displays the IF failure information of the selected inter-cluster link IF.|
|                                |If a Slice is selected  |(No display)|
|Fabric network display screen   |Not selected       |It displays the IF failure information of the nodes in the cluster.|
|                                |If a topology is selected |It displays the IF failure information of the nodes in the cluster.|
|                                |If a node on the topology is selected|It displays the IF failure information of the selected node.|
|                                |If a Slice is selected|(No display)|
|                                |If a node on the Slice is selected|It displays the IF failure information of the selected node.|
|Node display screen                   |Not selected       |It displays the IF failure information of the displayed node.|
|                                |If a node is selected |It displays the IF failure information of the selected node.|
|                                |If a port is selected|It displays the IF failure information of the selected port.|

#### 7.9.3 Slice failure information

![](media/7.detail/7-16.png "List of Slice failure information")  
**Figure 7-16 List of Slice failure information**

**Table 7-31 List of Slice failure information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |slice id    |It displays the SliceID.|
|2    |status |It displays the Slice failure status.<br>up/down|
|3    |cp id(1) |It displays the CP ID (one side point).|
|4    |cp id(2) |It displays the CP ID (one side point) on the opposite side.<br>up/down|
|5    |reachable |It displays the reachability.<br>reachable/unreachable|

**Table 7-32 Display condition of Slice failure information list**

|Screen                            |Condition of selection       |Displayed content|
|:-------------------------------|:-------------|:------------|
|Multi-cluster display screen            |Not selected       |It displays all Slice failure information.|
|                                |If a cluster is selected |(No display)|
|                                |If an inter-cluster link is selected|(No display)|
|                                |If a Slice is selected  |It displays the Slice failure information of the selected Slice.|
|Fabric network display screen   |Not selected       |(No display)|
|                                |If a topology is selected |(No display)|
|                                |If a node on the topology is selected|(No display)|
|                                |If a Slice is selected|It displays the Slice failure information of the selected Slice.|
|                                |If a node on the Slice is selected|(No display)|
|Node display screen                   |Not selected       |(No display)|
|                                |If a node is selected |(No display)|
|                                |If a port is selected|(No display)|

### 7.10 Controller information
Select the Controller display mode tab.<br>
Press the Controller toggle button on the upper part of the detailed screen.<br>
It displays the controller information based on the selected information.

![](media/7.detail/7-17.png "List of controller information")  
**Figure 7-17 List of controller information**

**Table 7-33 List of controller information**

|No.  |Item        |Description|
|----:|:-----------|:------------|
|1    |id          |It displays the controller ID.|
|2    |cpu(%)      |It displays the CPU use rate.|
|3    |mem(KB)     |It displays the memory usage [KB] and the memory use rate.|
|4    |disk(KB)    |It displays the disk usage [KB], the disk use rate and the mount point.|

**Table 7-34 Display condition of controller information list**

|Screen                     |Condition of selection       |Displayed content|
|:------------------------|:-------------|:------------|
|Controller status display screen   |Not selected       |It displays all controllers information.|
|                         |If controllers are selected (Multiple selections available*) |It displays the selected controllers information.|
\*When you click the controller, it turns to be selected status (shown with purple frame border).  
\*When you click the selected controller again, the selected status is cancelled.

![](media/7.detail/7-18.png "Selected status/unselected status of controller")  
**Figure 7-18 Selected status/unselected status of controller**

## 8. Notification
### 8.1 Failure notification
It acquires the failure information from MSF controller periodically and displays the notification dialog when a new failure is detected.

![](media/8.notification/8-1.png "Failure notification dialog")  
**Figure 8-1 Failure notification dialog**

### 8.2 CPU use rate threshold excess notification
When the controller status acquisition is On, it displays the notification dialog if CPU use rate of each controller excesses the threshold value specified in Config.

![](media/8.notification/8-2.png "Controller Info menu button")  
**Figure 8-2 Controller Info menu button**

![](media/8.notification/8-3.png "CPU use rate threshold excess notification dialog")  
**Figure 8-3 CPU use rate threshold excess notification dialog**
