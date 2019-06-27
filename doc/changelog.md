# Change Log
## fabric_controller_file_update_manual.md
### [1.0] - 2019.06.27

## fabric_controller_auto_installation_manual.md
### [1.2] - 2019.06.27
#### Added
- Config : "lagIfIdStartPos"
  - 6.1.2.2 Contents of FC system setting Config
  - Table 6-4 List of parameters in FC system setting Config
- Function : Extension functions
  - 6.1.3 Settings during operation (Extension functions)
  - 6.1.3.1 Priority routes control management function
  - 6.1.3.2 Silent failure detection function
  - 6.1.3.3 Node OS upgrade function
  - 6.1.3.4 Controller status notify function
- 6.1.4 FC simple startup confirmation
  - In the case of FC shutdown failed (FC process exists), ...
- 6.1.7 FC status confirmation (normal/abnormal) by getting the controller renewal state

#### Changed
- Config : "swClusterId", "l3VniVlanIdStartPos", "l3VniVlanIdEndPos"
  - Table 6-4 List of parameters in FC system setting Config (change Description)
- MFC is not supported on this version.
  - [OLD] MFC/FC
  - [NEW] FC
- 6.1.5 MFC/FC status confirmation (normal/abnormal) by getting
  - [OLD] FC status confirmation (normal/abnormal)
  - [NEW] FC status confirmation (normal/abnormal) by getting
- Table 6-18 Response body field of getting controller state
  - [OLD] Maintenance blockade status
  - [NEW] Blockade status

### [1.1] - 2019.02.15
#### Added
- Config : "internalLinkNormalIgpCost"
  - 6.1.1.2 Contents of MFC/FC initial setting Config
  - Table 6-2 List of parameters in MFC/FC initial setting Config
- Config : "IRB", "l3VniVlanIdStartPos", "l3VniVlanIdEndPos"
  - 6.1.2.2 Contents of MFC/FC system setting Config
  - Table 6-4 List of parameters in MFC/FC system setting Config

#### Changed
- MFC is not supported on this version.
- 6.1.3 MFC/FC simple startup confirmation 
  - [OLD] (1) Change MFC/FC Config(MFC/FC initial setting Config, MFC/FC system setting Config, and Hibernate Config) 
  - [NEW] (1) Change MFC/FC Configs appropriately.

### [1.0] - 2018.03.28

## fabric_controller_building_guide.md
### [1.2] - 2019.06.27
#### Added
- Config : "lagIfIdStartPos"
  - 6.1.2.2 Contents of FC system setting Config
  - Table 6-4 List of parameters in FC system setting Config
- Function : Extension functions
  - 6.1.3 Settings during operation (Extension functions)
  - 6.1.3.1 Priority routes control management function
  - 6.1.3.2 Silent failure detection function
  - 6.1.3.3 Node OS upgrade function
  - 6.1.3.4 Controller status notify function
- 6.1.4 FC simple startup confirmation
  - In the case of FC shutdown failed (FC process exists), ...
- 6.1.7 FC status confirmation (normal/abnormal) by getting the controller renewal state

#### Changed
- Config : "swClusterId", "l3VniVlanIdStartPos", "l3VniVlanIdEndPos"
  - Table 6-4 List of parameters in FC system setting Config (change Description)
- MFC is not supported on this version.
  - [OLD] MFC/FC
  - [NEW] FC
- 6.1.5 MFC/FC status confirmation (normal/abnormal) by getting
  - [OLD] FC status confirmation (normal/abnormal)
  - [NEW] FC status confirmation (normal/abnormal) by getting
- Table 6-18 Response body field of getting controller state
  - [OLD] Maintenance blockade status
  - [NEW] Blockade status

### [1.1] - 2019.02.15
#### Added
- Config : "internalLinkNormalIgpCost"
  - 6.1.1.2 Contents of MFC/FC initial setting Config
  - Table 6-2 List of parameters in MFC/FC initial setting Config
- Config : "IRB", "l3VniVlanIdStartPos", "l3VniVlanIdEndPos"
  - 6.1.2.2 Contents of MFC/FC system setting Config
  - Table 6-4 List of parameters in MFC/FC system setting Config

#### Changed
- MFC is not supported on this version.
- 6.1.3 MFC/FC simple startup confirmation 
  - [OLD] (1) Change MFC/FC Config(MFC/FC initial setting Config, MFC/FC system setting Config, and Hibernate Config) 
  - [NEW] (1) Change MFC/FC Configs appropriately.

### [1.0] - 2018.03.28

## fabric_controller_user_guide_for_startup_stop.md
### [2.2] - 2019.06.27
#### Added
- 3.3. FC status confirmation (normal/abnormal) by getting the controller renewal state

#### Changed
- 3.2. FC status confirmation (normal/abnormal) by getting controller state
  - [OLD] FC status confirmation (normal/abnormal)
  - [NEW] FC status confirmation (normal/abnormal) by getting controller state
  - Table 6-18 Response body field of getting controller state
    - [OLD] Maintenance blockade status
    - [NEW] Blockade status

### [2.1] - 2010.02.15
#### Changed
- 1. Startup
  - [OLD] (1) Change MFC/FC Config(MFC/FC initial setting Config, MFC/FC system setting Config, and Hibernate Config) 
  - [NEW] (1) Change MFC/FC Configs appropriately.

### [2.0] - 2018.04.12
