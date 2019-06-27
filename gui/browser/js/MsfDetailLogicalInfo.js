//
// MSF詳細エリアクラス(Logical情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailLocigalInfo = function() {
        // 更新対象ID
        this.TARGET_ID = "#detailTable4";
        // カラム数
        this.COL_NUM = 7;
        // ソート対象レコード
        this.SORT_COLS = [0, 1, 7, 8];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Logical Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailLocigalInfo.prototype.createRecords = function (baseData, clusterId, deviceType, nodeId, ifId) {

        var records = [];
        var edgePointDic = baseData.clusterInfoDic[clusterId].EdgepointDic;
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo;
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : null;
        var targetEps = this.getTargetEdgePoint(edgePointDic, ifInfo, deviceType, nodeId, ifId);
        
        for (var i = 0; i < targetEps.length; i++) {
            records.push(this.getRecord(clusterId, targetEps[i]));
        }

        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細レコード出力対象のEdgePoint情報の配列を返す
    //
    MSF.MsfDetailLocigalInfo.prototype.getTargetEdgePoint = function (edgePointDic, ifInfo, deviceType, nodeId, ifId) {

        var targetEps = [];
        if (deviceType && deviceType == MSF.Const.FabricType.Spines) {
            return targetEps;
        }
        for (var epId in edgePointDic) {
            var ep = edgePointDic[epId];
            if (nodeId && nodeId != ep.base_if.leaf_node_id) {
                continue;
            }
            if (ifId) {
                var epIfId = ep.base_if.physical_if_id;
                if (epIfId && ifId != epIfId) {
                    continue;
                }
                epIfId = ep.base_if.breakout_if_id;
                if (epIfId && getBaseIfIdDetail(ifInfo[MSF.Const.FabricType.Leafs][nodeId], "breakout", epIfId).indexOf(ifId)) {
                    continue;
                }
                epIfId = ep.base_if.lag_if_id;
                if (epIfId && getBaseIfIdDetail(ifInfo[MSF.Const.FabricType.Leafs][nodeId], "lag", epIfId).indexOf(ifId)) {
                    continue;
                }
            }
            targetEps.push(ep);
        }
        
        return targetEps;
    }
    ;
    //
    // 詳細レコード出力対象のEdgePoint情報の配列を返す(スライス条件)
    //
    MSF.MsfDetailLocigalInfo.prototype.getTargetEdgePointSlice = function (baseData, sliceType, sliceId, clusterId, leafs, nodeId) {

        var targetEps = [];
        // 重複データ除外のための辞書 {cluster_id : [edgepoint_id,,]}
        var duplicateCheckEps = {};
        
        // 指定スライスに所属するCPから対象のEdgePointを取り出す
        var cpInfo = baseData.CPInfo[sliceType + sliceId] || {};
        for (var cpId in cpInfo) {
            var cp = cpInfo[cpId];
            if (clusterId && clusterId != cp.cluster_id) {
                continue;
            }
            
            var edgePointDic = baseData.clusterInfoDic[cp.cluster_id].EdgepointDic;
            var ep = edgePointDic[cp.edge_point_id];
            // 出力情報のクラスタIDが不足しているため、あらかじめ埋め込む
            ep.clusterId = cp.cluster_id;
            var leafNodeId = ep.base_if.leaf_node_id;
            if (leafs && !leafs[leafNodeId]) {
                continue;
            }
            if (nodeId && nodeId != leafNodeId) {
                continue;
            }
            
            // 対象EdgePointとして取出し済みであればSKIP
            if (duplicateCheckEps[cp.cluster_id]) {
                if (duplicateCheckEps[cp.cluster_id].indexOf(cp.edge_point_id) != -1) {
                    continue;
                }
            } else {
                duplicateCheckEps[cp.cluster_id] = [];
            }
            duplicateCheckEps[cp.cluster_id].push(cp.edge_point_id);
            targetEps.push(ep);
        }
        return targetEps;
    }
    ;
    //
    // 詳細情報テーブル（Logical Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailLocigalInfo.prototype.createRecordsSlice = function (baseData, sliceType, sliceId, clusterId, leafs, nodeId) {

        var records = [];
        var targetEps = this.getTargetEdgePointSlice(baseData, sliceType, sliceId, clusterId, leafs, nodeId);

        for (var i = 0; i < targetEps.length; i++) {
            records.push(this.getRecord(targetEps[i].clusterId, targetEps[i]));
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報テーブル（Logical Info）レコード生成.
    //
    MSF.MsfDetailLocigalInfo.prototype.getRecord = function (clusterId, edgePoint) {

        var record = [];
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        record.push(getDeviceTypeNumber(MSF.Const.DeviceType.Leaf) + edgePoint.base_if.leaf_node_id);
        // 2: IF ID
        var ifId;
        if (edgePoint.base_if.physical_if_id) {
            ifId = "port" + edgePoint.base_if.physical_if_id;
        } else if (edgePoint.base_if.breakout_if_id) {
            ifId = "port" + edgePoint.base_if.breakout_if_id;
        } else if (edgePoint.base_if.lag_if_id) {
            ifId = "LAG" + edgePoint.base_if.lag_if_id;
        }
        record.push(ifId);
        // 3: edge-point ID
        record.push(edgePoint.edge_point_id);
        // 4: remarkケーパビリティリスト
        record.push((edgePoint.qos.remark) ? this.getCapability(edgePoint.qos.remark_capability) : "-");
        // 5: shapingケーパビリティ
        record.push(edgePoint.qos.shaping ? "available" : "-");
        // 6: egress queueケーパビリティリスト
        record.push((edgePoint.qos.egress_queue_capability != null) ? this.getCapability(edgePoint.qos.egress_queue_capability) : "-");

        // ソート条件用カラム
        // 7: if id
        record.push(ifId);
        // 8: 数値化して格納
        record.push(!isNaN(edgePoint.edge_point_id) ? parseInt(edgePoint.edge_point_id, 10) : edgePoint.edge_point_id);

        return record;
    }
    ;
    //
    // 詳細情報テーブル（Logical Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailLocigalInfo.prototype.getCapability = function (strList) {
        
        if (strList == null) {
            return "-";
        }
        if (strList.length == 0) {
            return "-";
        }
        return strList.join(", ");
    }
    ;
})();
