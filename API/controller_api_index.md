## Controller API index

**Version 1.0**
**Copyright(c) 2017 Nippon Telegraph and Telephone Corporation**

### Common API
|group|Interface(API) description|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|
|Processing request|Getting list of operational state|000102|GET|/v1/operations|/v1/operations|
||Getting information of detailed operation state|000103|GET|/v1/operations/{operation_id}|/v1/operations/1234567890123|
|Controller status confirmation|Getting controller state|000201|GET|/v1/MSFcontroller/status|/v1/MSFcontroller/status|
|Controller log|Getting controller log|000301|GET|/v1/operations/log|/v1/operations/1234567890123|

### Underlay Management API
|group|Interface(API) description|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|
|Equipment-type information management|Registering equipment information|010101|POST|/v1/equipment-types|/v1/equipment-types|
||Getting equipment list in switch cluster|010102|GET|/v1/equipment-types|/v1/equipment-types|
||Getting equipment information|010103|GET|/v1/equipment-types/{equipment_type_id}|/v1/equipment-types/10|
||Deleting equipment information|010104|DELETE|/v1/equipment-types/{equipment_type_id}|/v1/equipment-types/{equipment_type_id}|
|Node information|Getting list of nodes|010301|GET|/v1/clusters/{cluster_id}/nodes|/v1/clusters/1/nodes|
|Leaf management|Adding Leaf- node|010401|POST|/v1/clusters/{cluster_id}/nodes/leafs|/v1/clusters/1/nodes/leafs|
||Getting list of Leaf-nodes|010402|GET|/v1/clusters/{cluster_id}/nodes/leafs|/v1/clusters/1/nodes/leafs|
||Getting information of Leaf-node|010403|GET|/v1/clusters/{cluster_id}/nodes/leafs/{node_id}|/v1/clusters/1/nodes/leafs/1|
||Deleting Leaf-node|010404|DELETE|/v1/clusters/{cluster_id}/nodes/leafs/{node_id}|/v1/clusters/1/nodes/leafs/1|
||Updating Leaf-node|010405|PUT|/v1/clusters/{cluster_id}/nodes/leafs/{node_id}|/v1/clusters/1/nodes/leafs/1|
|Spine management|Adding Spine-node|010501|POST|/v1/clusters/{cluster_id}/nodes/spines|/v1/clusters/1/nodes/spines|
||Getting list of Spine-nodes|010502|GET|/v1/clusters/{cluster_id}/nodes/spines|/v1/clusters/1/nodes/spines|
||Getting information of Spine-node|010503|GET|/v1/clusters/{cluster_id}/nodes/spines/{node_id}|/v1/clusters/1/nodes/spines/1|
||Deletting Spine-node|010504|DELETE|/v1/clusters/{cluster_id}/nodes/spines/{node_id}|/v1/clusters/1/nodes/spines/1|
|RR (BGP Route Reflector) management|Getting list of RR-node|010604|GET|/v1/clusters/{cluster_id}/nodes/rrs|/v1/clusters/1/nodes/rrs|
||Getting infromation of RR-node|010605|GET|/v1/clusters/{cluster_id}/nodes/rrs/{node_id}|/v1/clusters/1/nodes/rrs/1|
|Interface information|Gettinig list of interfaces|010701|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces|/v1/clusters/1/nodes/leafs/1/interfaces|
|Interface management (Physical interface)|Getting list of physical interfaces|010801|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/physical-ifs|
||Getting information of physical interface|010802|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/physical-ifs/1|
||Updating physical interface|010803|PUT|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/physical-ifs/1|
|Interface management (Breakout interface)|Creating or deleting breakout interface|010901|PATCH|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/breakout-ifs|
||Getting list of breakout interfaces|010902|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/breakout-ifs|
||Getting information of breakout interface|010903|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs/{breakout_if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/breakout-ifs/1-1|
|Interface management (Internal-link interface)|Getting list of internal-link interfaces|011001|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/internal-link-ifs|
||Getting information of internal-link interface|011002|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs/{internal_link_if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/internal-link-ifs/1|
|Interface management (Link aggregation interface)|Creating Link-aggregation interface|011101|POST|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/lag-ifs|
||Getting list of Link-aggregation interfaces|011102|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs|/v1/clusters/1/nodes/leafs/1/interfaces/lag-ifs|
||Getting information of Link-aggregation interface|011103|GET|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/lag-ifs/1|
||Deleting Link-aggregation interface|011105|DELETE|/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}|/v1/clusters/1/nodes/leafs/1/interfaces/lag-ifs/1|
|Edge point management|Creating edge-point|011401|POST|/v1/clusters/{cluster_id}/points/edge-points|/v1/clusters/1/points/edge-points|
||Getting list of edge-points|011402|GET|/v1/clusters/{cluster_id}/points/edge-points|/v1/clusters/1/points/edge-points|
||Getting infromation of edge-point|011403|GET|/v1/clusters/{cluster_id}/points/edge-points/{edge_point_id}|/v1/clusters/1/points/edge-points/1|
||Deleting edge-point|011404|DELETE|/v1/clusters/{cluster_id}/points/edge-points/{edge_point_id}|/v1/clusters/1/points/edge-points/1|

### Overlay Management API
|group|Interface(API) description|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|
|Slice|Creating Slice|020101|POST|/v1/slices/{slice_type}|/v1/slices/l2vpn/1<br>/v1/slices/l3vpn/100|
||Deleting Slice|020103|DELETE|/v1/slices/{slice_type}/{slice_id}|/v1/slices/l2vpn/1<br>/v1/slices/l3vpn/100|
||Getting information of Slice|020104|GET|/v1/slices/{slice_type}/{slice_id}|v1/slices/l2vpn/1<br>/v1/slices/l3vpn/100|
||Getting list of Slices|020105|GET|/v1/slices/{slice_type}|/v1/slices/l2vpn/1<br>/v1/slices/l3vpn/100|
|CP|Creating or deleting CP|020201|PATCH|/v1/slices/{slice_type}/{slice_id}/cps|//v1/slices/l2vpn/1/cps<br>/v1/slices/l3vpn/100/cps|
||Creating CP|020202|POST|/v1/slices/{slice_type}/{slice_id}/cps|/v1/slices/l2vpn/1/cps<br>/v1/slices/l3vpn/100/cps|
||Deleting CP|020204|DELETE|/v1/slices/{slice_type}/{slice_id}/cps/{cp_id}|/v1/slices/l2vpn/1/cps/1<br>/v1/slices/l3vpn/100/cps/10|
||Getting information of CP|020205|GET|/v1/slices/{slice_type}/{slice_id}/cps/{cp_id}|/v1/slices/l2vpn/1/cps/1<br>/v1/slices/l3vpn/100/cps/10|
||Getting lists of CP|020206|GET|/v1/slices/{slice_type}/{slice_id}/cps|/v1/slices/l2vpn/1/cps<br>/v1/slices/l3vpn/100/cps|
||Creating or deleting static route|020207|PATCH|/v1/slices/{slice_type}/{slice_id}/cps/{cp_id}|/v1/slices/l3vpn/100/cps/1|

### Traffic Information API
|group|Interface(API) description|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|
|Traffic information|Getting list of IF traffic|030102|GET|/v1/traffic/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces|/v1/traffic/clusters/1/nodes/1/interfaces|
||Getting IF traffic|030103|GET|/v1/traffic/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/{if_type}/{if_id}|/v1/traffic/clusters/1/nodes/1/interfaces/physical-ifs/1|
||Getting list of CP traffic|030104|GET|/v1/traffic/slices/{slice_type}/{slice_id}/cps|/v1/traffic/slices/l3vpn/1/cps|
||Getting CP traffic|030105|GET|/v1/traffic/slices/{slice_type}/{slice_id}/cps/{cp_id}|/v1/traffic/slices/l3vpn/1/cps/1|

### Fault detection API
|group|Interface(API) description|Identification ID|Method|URI|URI example|
|:----|:----|:----|:----|:----|:----|
|Failure detection|Getting list of failures|050101|GET|/v1/failures/failure_status|/v1/failures/failure_status|
