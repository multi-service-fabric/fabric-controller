//
// MSF詳細エリアクラス(Physical情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailPhysicalInfo = function() {
        // 更新対象ID
        this.TARGET_ID_PHYSICAL = "#detailTable3-1";
        this.TARGET_ID_BREAKOUT = "#detailTable3-2";
        this.TARGET_ID_LAG = "#detailTable3-3";
        // カラム数
        this.COL_NUM_PHYSICAL = 8;
        this.COL_NUM_BREAKOUT = 9;
        this.COL_NUM_LAG = 11;
        // ソート対象レコード
        this.SORT_COLS_PHYSICAL = [0, 8, 9, 10, 3];
        this.SORT_COLS_BREAKOUT = [0, 9, 10, 11, 3];
        this.SORT_COLS_LAG = [0, 11, 12, 13, 3];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Physical IF Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    //
    MSF.MsfDetailPhysicalInfo.prototype.createRecordsPhysical = function (baseData, clusterId, deviceType, nodeId, ifId) {
        
        var records = [];
        var targetIfs = {};
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo || {};
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        targetIfs = this.getTargetPhysicalIf(ifInfo, deviceType, nodeId, ifId);
        
        for (var type in targetIfs) {
            for (var nId in targetIfs[type]) {
                for (var iId in targetIfs[type][nId].physical_ifs) {
                    var pIf = targetIfs[type][nId].physical_ifs[iId];
                    // 使用状態の取得 （LAGの場合のみ"/"区切りでLagIfIdを付与）
                    var uses = this.usesPhysicalIfStatus(ifInfo, baseData.clusterInfoDic[clusterId], type, nId, pIf.physical_if_id);
                    records.push(this.getRecordPhysical(clusterId, type, nId, pIf, uses));
                }
            }
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_PHYSICAL, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    
    //
    // 詳細情報テーブル（Physical IF Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    //
    MSF.MsfDetailPhysicalInfo.prototype.createRecordsPhysicalSlice = function (baseData, clusterId, sliceType, sliceId, spines, leafs, deviceType, nodeId) {
        
        var records = [];
        var targetIfs = {};
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo || {};
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        if (deviceType && nodeId) {
            targetIfs = this.getTargetPhysicalIf(ifInfo, deviceType, nodeId);
        } else {
            targetIfs = this.getTargetPhysicalIfSlice(ifInfo, spines, leafs);
        }
        
        for (var type in targetIfs) {
            for (var nId in targetIfs[type]) {
                for (var iId in targetIfs[type][nId].physical_ifs) {
                    var pIf = targetIfs[type][nId].physical_ifs[iId];
                    // 使用状態の取得 （LAGの場合のみ"/"区切りでLagIfIdを付与）
                    var uses = this.usesPhysicalIfStatus(ifInfo, baseData.clusterInfoDic[clusterId], type, nId, pIf.physical_if_id);
                    // 該当スライスにCPが設定されていないEdgePointはSKIP
                    if (uses == MSF.Const.UsesStatus.EdgePoint &&
                        this.isOtherSliceEdgePoint(baseData.clusterInfoDic[clusterId], baseData.l2_cps, baseData.l3_cps, sliceType, sliceId, nId, pIf.physical_if_id)) {
                        continue;
                    }
                    records.push(this.getRecordPhysical(clusterId, type, nId, pIf, uses));
                }
            }
        }

        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_PHYSICAL, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報出力対象の物理IF情報を返す
    //
    MSF.MsfDetailPhysicalInfo.prototype.getTargetPhysicalIf = function (ifInfo, deviceType, nodeId, ifId) {

        var targetIfs = {};
        // nodeId, ifId条件でフィルタ
        for (var type in ifInfo) {
            // 装置種別指定条件の判定
            if (deviceType && deviceType != type) {
                continue;
            }
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                targetIfs[type][nId] = {physical_ifs: []};
                var node = nodes[nId];
                for (var i = 0; i < node.physical_ifs.length; i++) {
                    var pIf = node.physical_ifs[i];
                    if (ifId && ifId != pIf.physical_if_id) {
                        continue;
                    }
                    // 速度未設定かつbreakout未使用であれば対象外
                    if (!pIf.speed && !pIf.breakout) {
                        continue;
                    }
                    targetIfs[type][nId].physical_ifs.push(pIf);
                }
            }
        }
        return targetIfs;
    }
    ;
    //
    // 詳細情報出力対象の物理IF情報を返す(スライス選択時)
    //
    MSF.MsfDetailPhysicalInfo.prototype.getTargetPhysicalIfSlice = function (ifInfo, spines, leafs) {

        var targetIfs = {};
        // nodeId, ifId条件でフィルタ
        for (var type in ifInfo) {
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (type == MSF.Const.FabricType.Leafs && !leafs[nId]) {
                    continue;
                }
                if (type == MSF.Const.FabricType.Spines && !spines[nId]) {
                    continue;
                }
                targetIfs[type][nId] = {physical_ifs: []};
                var node = nodes[nId];
                for (var i = 0; i < node.physical_ifs.length; i++) {
                    var pIf = node.physical_ifs[i];
                    // 速度未設定かつbreakout未使用であれば対象外
                    if (!pIf.speed && !pIf.breakout) {
                        continue;
                    }
                    targetIfs[type][nId].physical_ifs.push(pIf);
                }
            }
        }
        return targetIfs;
    }
    ;
    //
    // 詳細情報テーブル（Breakout IF Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailPhysicalInfo.prototype.createRecordsBreakout = function (baseData, clusterId, deviceType, nodeId, ifId) {
        
        var records = [];
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo || {};
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;

        var targetIfs = this.getTargetBreakoutIf(ifInfo, deviceType, nodeId, ifId);
        // 装置指定に従い絞り込み
        for (var type in targetIfs) {
            for (var nId in targetIfs[type]) {
                for (var iId in targetIfs[type][nId].breakout_ifs) {
                    var bIf = targetIfs[type][nId].breakout_ifs[iId];
                    var uses = this.usesBreakoutIfStatus(ifInfo, baseData.clusterInfoDic[clusterId], type, nId, bIf.breakout_if_id);
                    records.push(this.getRecordBreakout(clusterId, type, nId, bIf, uses));
                }
            }
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_BREAKOUT, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報出力対象のBreakoutIF情報を返す
    //
    MSF.MsfDetailPhysicalInfo.prototype.getTargetBreakoutIf = function (ifInfo, deviceType, nodeId, ifId) {

        var targetIfs = {};
        // nodeId, ifId条件でフィルタ
        for (var type in ifInfo) {
            // 装置種別指定条件の判定
            if (deviceType && deviceType != type) {
                continue;
            }
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                targetIfs[type][nId] = {breakout_ifs: []};
                var node = nodes[nId];
                for (var i = 0; i < node.breakout_ifs.length; i++) {
                    var bIf = node.breakout_ifs[i];
                    if (ifId && getBaseIfIdDetail(node, "breakout", bIf.breakout_if_id).indexOf(ifId) == -1) {
                        continue;
                    }
                    targetIfs[type][nId].breakout_ifs.push(bIf);
                }
            }
        }
        return targetIfs;
    }
    ;
    //
    // 詳細情報テーブル（Lag IF Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID
    //
    MSF.MsfDetailPhysicalInfo.prototype.createRecordsLag = function (baseData, clusterId, deviceType, nodeId, ifId) {
        
        var records = [];
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo || {};
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;

        var targetIfs =  this.getTargetLagIf(ifInfo, deviceType, nodeId, ifId);
        // 装置指定に従い絞り込み
        for (var type in targetIfs) {
            for (var nId in targetIfs[type]) {
                for (var iId in targetIfs[type][nId].lag_ifs) {
                    var lIf = targetIfs[type][nId].lag_ifs[iId];
                    var uses = this.usesLagIfStatus(ifInfo, baseData.clusterInfoDic[clusterId], type, nId, lIf.lag_if_id);
                    records.push(this.getRecordLag(clusterId, type, nId, lIf, uses));
                }
            }
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_LAG, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報出力対象のLagIF情報を返す
    //
    MSF.MsfDetailPhysicalInfo.prototype.getTargetLagIf = function (ifInfo, deviceType, nodeId, ifId) {

        var targetIfs = {};
        // nodeId, ifId条件でフィルタ
        for (var type in ifInfo) {
            // 装置種別指定条件の判定
            if (deviceType && deviceType != type) {
                continue;
            }
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                targetIfs[type][nId] = {lag_ifs: []};
                var node = nodes[nId];
                for (var i = 0; i < node.lag_ifs.length; i++) {
                    var lIf = node.lag_ifs[i];
                    if (ifId && lIf.physical_if_ids.indexOf(ifId) == -1) {
                        continue;
                    }
                    targetIfs[type][nId].lag_ifs.push(lIf);
                }
            }
        }
        return targetIfs;
    }
    ;
    
    //
    // 詳細情報テーブル（Physical Info）レコード生成.
    //
    MSF.MsfDetailPhysicalInfo.prototype.getRecordPhysical = function (clusterId, deviceType, nodeId, pIf, uses) {

        var record = [];
        var lagId = null;
        var sepIndex = uses.indexOf("/");
        if (sepIndex != -1) {
            lagId = uses.slice(sepIndex + 1);
            uses = uses.slice(0, sepIndex);
        }
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: ID
        record.push("port" + pIf.physical_if_id);
        // 3: IF SLOT
        var ifName = pIf.if_name || "-";
        if (lagId) {
            // Lag設定ありの場合
            ifName = ifName + "(LAG" + lagId + ")";
        }
        record.push(ifName);

        // 4: remarkケーパビリティリスト
        record.push((pIf.qos.remark_capability && pIf.qos.remark_capability.length > 0) ? pIf.qos.remark_capability.join(",") : "-");
        // 5: shapingケーパビリティ
        record.push(pIf.qos.shaping ? "available" : "-");
        // 6: egress queueケーパビリティリスト
        record.push((pIf.qos.egress_queue_capability && pIf.qos.egress_queue_capability.length > 0) ? pIf.qos.egress_queue_capability.join(",") : "-");
        // 7: 使用状態
        record.push(uses);

        // ソート条件用カラム
        // 8: deviceType
        record.push(deviceType);
        // 9: node_id ※可能であれば数値化して格納
        record.push(isNaN(nodeId) ? nodeId : parseInt(nodeId, 10));
        // 10: if_id ※可能であれば数値化して格納
        record.push(isNaN(pIf.physical_if_id) ? pIf.physical_if_id : parseInt(pIf.physical_if_id, 10));

        return record;
    }
    ;
    
    //
    // 詳細情報テーブル（Breakout Info）レコード生成.
    //
    MSF.MsfDetailPhysicalInfo.prototype.getRecordBreakout = function (clusterId, deviceType, nodeId, bIf, uses) {

        var record = [];
        var lagId = null;
        var sepIndex = uses.indexOf("/");
        if (sepIndex != -1) {
            lagId = uses.slice(sepIndex + 1);
            uses = uses.slice(0, sepIndex);
        }
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: ID
        record.push("port" + bIf.breakout_if_id);
        // 3: IF名
        record.push(bIf.if_name);
        // 4: IF速度
        record.push(bIf.speed);

        // 4: remarkケーパビリティリスト
        record.push((bIf.qos.remark_capability && bIf.qos.remark_capability.length > 0) ? bIf.qos.remark_capability.join(",") : "-");
        // 5: shapingケーパビリティ
        record.push(bIf.qos.shaping ? "available" : "-");
        // 6: egress queueケーパビリティリスト
        record.push((bIf.qos.egress_queue_capability && bIf.qos.egress_queue_capability.length > 0) ? bIf.qos.egress_queue_capability.join(",") : "-");
        // 7: 使用状態
        record.push(uses);

        // ソート条件用カラム
        // 8: deviceType
        record.push(deviceType);
        // 9: node_id ※可能であれば数値化して格納
        record.push(isNaN(nodeId) ? nodeId : parseInt(nodeId, 10));
        // 10: if_id ※可能であれば数値化して格納
        record.push(isNaN(bIf.breakout_if_id) ? bIf.breakout_if_id : parseInt(bIf.breakout_if_id, 10));

        return record;
    }
    ;
    
    //
    // 詳細情報テーブル（Lag Info）レコード生成.
    //
    MSF.MsfDetailPhysicalInfo.prototype.getRecordLag = function (clusterId, deviceType, nodeId, lIf, uses) {

        var record = [];
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: ID
        record.push("LAG" + lIf.lag_if_id);
        // 3: IF名
        record.push(lIf.if_name);
        // 4: IF速度
        record.push(lIf.speed);
        // 5: 最少リンク数
        record.push(lIf.minimum_links);
        // 6: メンバーリンクIF名
        record.push(lIf.physical_if_ids.concat(lIf.breakout_if_ids).join(","));

        // 7: remarkケーパビリティリスト
        record.push((lIf.qos.remark_capability && lIf.qos.remark_capability.length > 0) ? lIf.qos.remark_capability.join(",") : "-");
        // 8: shapingケーパビリティ
        record.push(lIf.qos.shaping ? "available" : "-");
        // 9: egress queueケーパビリティリスト
        record.push((lIf.qos.egress_queue_capability && lIf.qos.egress_queue_capability.length > 0) ? lIf.qos.egress_queue_capability.join(",") : "-");
        // 10: 使用状態
        record.push(uses);

        // ソート条件用カラム
        // 11: deviceType
        record.push(deviceType);
        // 12: node_id ※可能であれば数値化して格納
        record.push(isNaN(nodeId) ? nodeId : parseInt(nodeId, 10));
        // 13: if_id ※可能であれば数値化して格納
        record.push(isNaN(lIf.lag_if_id) ? lIf.lag_if_id : parseInt(lIf.lag_if_id, 10));

        return record;
    }
    ;

    // 該当スライスにCPが設定されていないEdgePoint
    // true: 該当スライスでない
    MSF.MsfDetailPhysicalInfo.prototype.isOtherSliceEdgePoint = function (clusterInfo, l2Cps, l3Cps, sliceType, sliceId, nodeId, pIfId) {
        
        for (var epId in clusterInfo.EdgepointDic) {
            var ep = clusterInfo.EdgepointDic[epId];
            if (ep.base_if.leaf_node_id != nodeId) {
                continue;
            }
            if (ep.base_if.physical_if_id && ep.base_if.physical_if_id != pIfId) {
                continue;
            }
            var cps;
            if (sliceType == MSF.Const.VpnType.L2) {
                cps = l2Cps;
            } else {
                cps = l3Cps;
            }
            for (var i = 0; i < cps.length; i++) {
                cp = cps[i];
                if (cp.edge_point_id != ep.edge_point_id) {
                    continue;
                }
                // 該当スライスのCPでない場合
                if (cp.slice_id != sliceId) {
                    return true;
                }
                // 該当スライスのCP
                return false;
            }
        }
        // 該当CPなし
        return false;
    }
    ;
    //
    // PhysicalIFの使用状態を取得
    // LAGのみ/を区切り文字としてLagIfIdを付与する
    //
    MSF.MsfDetailPhysicalInfo.prototype.usesPhysicalIfStatus = function(ifInfo, clusterInfo, deviceType, nodeId, pIfId) {

        // BreakoutIF > LagIF > 内部リンク > クラスタ間リンク > EdgePointの順で使用IFを探索
        var i;
        for (var type in ifInfo) {
            // var nodes = ifInfo[type];
            if (deviceType && deviceType != type) {
                continue;
            }
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                var node = nodes[nId];
                for (i = 0; i < node.breakout_ifs.length; i++) {
                    if (node.breakout_ifs[i].base_if.physical_if_id == pIfId) {
                        return MSF.Const.UsesStatus.Breakout;
                    }
                }
                for (i = 0; i < node.lag_ifs.length; i++) {
                    if (node.lag_ifs[i].physical_if_ids.indexOf(pIfId) != -1) {
                        return MSF.Const.UsesStatus.Lag + "/" + node.lag_ifs[i].lag_if_id;
                    }
                }
                for (i = 0; i < node.internal_link_ifs.length; i++) {
                    if (node.internal_link_ifs[i].physical_if_id && node.internal_link_ifs[i].physical_if_id == pIfId) {
                        return MSF.Const.UsesStatus.InternalLink;
                    }
                }
            }
        }

        // クラスタ間リンク
        for (i = 0; i < clusterInfo.cluster_link_ifs.length; i++) {
            var clusterLink = clusterInfo.cluster_link_ifs[i];
            if (!clusterLink.physical_link) {
                continue;
            }
            if (clusterLink.physical_link.node_id != nodeId) {
                continue;
            }
            if (!clusterLink.physical_link.physical_if_id || clusterLink.physical_link.physical_if_id != pIfId) {
                continue;
            }
            return MSF.Const.UsesStatus.ClusterLink;
        }

        // EdgePoint
        if (deviceType == MSF.Const.FabricType.Leafs) {
            for (var epId in clusterInfo.EdgepointDic) {
                var ep = clusterInfo.EdgepointDic[epId];
                if (ep.base_if.leaf_node_id != nodeId) {
                    continue;
                }
                if (ep.base_if.physical_if_id && ep.base_if.physical_if_id == pIfId) {
                    return MSF.Const.UsesStatus.EdgePoint;
                }
            }
        }
        return MSF.Const.UsesStatus.UnUsed;
    }
    ;

    //
    // BreakoutIFの使用状態を取得
    // LAGのみ/を区切り文字としてLagIfIdを付与する
    //
    MSF.MsfDetailPhysicalInfo.prototype.usesBreakoutIfStatus = function(ifInfo, clusterInfo, deviceType, nodeId, bIfId) {

        // BreakoutIF > LagIF > 内部リンク > クラスタ間リンク > EdgePointの順で使用IFを探索
        var i;
        for (var type in ifInfo) {
            // var nodes = ifInfo[type];
            if (deviceType && deviceType != type) {
                continue;
            }
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                var node = nodes[nId];
                for (i = 0; i < node.lag_ifs.length; i++) {
                    if (node.lag_ifs[i].breakout_if_ids.indexOf(bIfId) != -1) {
                        return MSF.Const.UsesStatus.Lag + "/" + node.lag_ifs[i].lag_if_id;
                    }
                }
                for (i = 0; i < node.internal_link_ifs.length; i++) {
                    if (node.internal_link_ifs[i].breakout_if_id && node.internal_link_ifs[i].breakout_if_id == bIfId) {
                        return MSF.Const.UsesStatus.InternalLink;
                    }
                }
            }
        }

        // クラスタ間リンク
        for (i = 0; i < clusterInfo.cluster_link_ifs.length; i++) {
            var clusterLink = clusterInfo.cluster_link_ifs[i];
            if (!clusterLink.physical_link) {
                continue;
            }
            if (clusterLink.physical_link.node_id != nodeId) {
                continue;
            }
            if (clusterLink.physical_link.breakout_if_id && clusterLink.physical_link.breakout_if_id == bIfId) {
                return MSF.Const.UsesStatus.ClusterLink;
            }
        }

        // EdgePoint
        if (deviceType == MSF.Const.FabricType.Leafs) {
            for (var epId in clusterInfo.EdgepointDic) {
                var ep = clusterInfo.EdgepointDic[epId];
                if (ep.base_if.leaf_node_id != nodeId) {
                    continue;
                }
                if (ep.base_if.breakout_if_id && ep.base_if.breakout_if_id == bIfId) {
                    return MSF.Const.UsesStatus.EdgePoint;
                }
            }
        }
        return MSF.Const.UsesStatus.UnUsed;
    }
    ;

    //
    // LagIFの使用状態を取得
    //
    MSF.MsfDetailPhysicalInfo.prototype.usesLagIfStatus = function(ifInfo, clusterInfo, deviceType, nodeId, lIfId) {

        // BreakoutIF > LagIF > 内部リンク > クラスタ間リンク > EdgePointの順で使用IFを探索
        var i;
        for (var type in ifInfo) {
            // var nodes = ifInfo[type];
            if (deviceType && deviceType != type) {
                continue;
            }
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                var node = nodes[nId];
                for (i = 0; i < node.internal_link_ifs.length; i++) {
                    if (node.internal_link_ifs[i].lag_if_id && node.internal_link_ifs[i].lag_if_id == lIfId) {
                        return MSF.Const.UsesStatus.InternalLink;
                    }
                }
            }
        }

        // クラスタ間リンク
        for (i = 0; i < clusterInfo.cluster_link_ifs.length; i++) {
            var clusterLink = clusterInfo.cluster_link_ifs[i];
            if (!clusterLink.lag_link) {
                continue;
            }
            if (clusterLink.lag_link.node_id != nodeId) {
                continue;
            }
            if (clusterLink.lag_link.lag_if_id && clusterLink.lag_link.lag_if_id == lIfId) {
                return MSF.Const.UsesStatus.ClusterLink;
            }
        }

        // EdgePoint
        if (deviceType == MSF.Const.FabricType.Leafs) {
            for (var epId in clusterInfo.EdgepointDic) {
                var ep = clusterInfo.EdgepointDic[epId];
                if (ep.base_if.leaf_node_id != nodeId) {
                    continue;
                }
                if (ep.base_if.lag_if_id && ep.base_if.lag_if_id == lIfId) {
                    return MSF.Const.UsesStatus.EdgePoint;
                }
            }
        }
        return MSF.Const.UsesStatus.UnUsed;
    }
    ;
})();
