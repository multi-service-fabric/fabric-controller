//
// MSF詳細エリアクラス(障害情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailFailureInfo = function() {
        // 更新対象ID
        this.TARGET_ID_NODE = "#detailTable9-1";
        this.TARGET_ID_IF = "#detailTable9-2";
        this.TARGET_ID_SLICE = "#detailTable9-3";
        // カラム数
        this.COL_NUM_NODE = 3;
        this.COL_NUM_IF = 4;
        this.COL_NUM_SLICE = 5;
        // ソート対象レコード
        this.SORT_COLS_NODE = [3, 4, 5];
        this.SORT_COLS_IF = [4, 5, 6, 7, 8];
        this.SORT_COLS_SLICE = [0];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Node Failure Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID
    // deviceType: 装置種別
    // nodeId: 装置ID
    //
    MSF.MsfDetailFailureInfo.prototype.createRecordsNode = function (baseData, clusterId, deviceType, nodeId) {
        
        var records = [];
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        var targetNodes = this.getTargetNodeFailure(baseData.failure_status.physical_unit, clusterId, deviceType, nodeId);
        for (var i = 0; i < targetNodes.length; i++) {
            records.push(this.getRecordPhysical(targetNodes[i], false));
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_NODE, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // 詳細情報テーブル（IF Failure Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID
    // deviceType: 装置種別
    // nodeId: 装置ID
    // physicalIfId: 物理IF ID
    // clusterLinkId: クラスタ間リンクIF ID(クラスタ間リンクIF選択時)
    //
    MSF.MsfDetailFailureInfo.prototype.createRecordsIf = function (baseData, clusterId, deviceType, nodeId, physicalIfId, clusterLinkId) {

        // 画面上の選択オブジェクトでフィルタ
        var physicalUnit = baseData.failure_status.physical_unit;
        var ifFilter = { pIds:[], bIds:[], lIds:[] };
        if (clusterLinkId) {
            // クラスタ間リンクIFから各条件を取り出す
            this.getRelClusterLinkIfs(baseData, clusterLinkId, ifFilter);
            clusterId = ifFilter.clusterid;
            deviceType = MSF.Const.FabricType.Leafs;
            nodeId = ifFilter.nodeId;
        } else {
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
            ifFilter = this.getRelIfFilter(baseData, clusterId, deviceType, nodeId, physicalIfId);
        }
        var targetIfs = this.getTargetIfFailure(physicalUnit, clusterId, deviceType, nodeId, ifFilter);
        var records = [];
        for (var i = 0; i < targetIfs.length; i++) {
            records.push(this.getRecordPhysical(targetIfs[i], true));
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_IF, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // physicalIfIdまたはクラスタ間リンクIDから関連IFIDを取得
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId : クラスタID
    // deviceType : 装置種別
    // nodeId : 装置ID
    // optIfId: physicalIfId
    //
    MSF.MsfDetailFailureInfo.prototype.getRelIfFilter = function(baseData, clusterId, deviceType, nodeId, optIfId) {
        
        if (!optIfId) {
            return null;
        }
        var clusterInfo = baseData.clusterInfoDic[clusterId];
        var clusterLinkInfo = clusterInfo.cluster_link_ifs;
        var interfaceInfo = clusterInfo.InterfacesInfo[deviceType][nodeId] || [];
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        var ifFilter = {};
        ifFilter.pIds = this.getRelPhysicalIfs(clusterLinkInfo, interfaceInfo, optIfId);
        ifFilter.bIds = this.getRelBreakoutIfs(clusterLinkInfo, interfaceInfo, optIfId);
        ifFilter.lIds = this.getRelLagIfs(clusterLinkInfo, interfaceInfo, optIfId);
        return ifFilter;
    }
    ;
    //
    // 指定された条件に関連するPhysicalIfの配列を返す
    //
    MSF.MsfDetailFailureInfo.prototype.getRelPhysicalIfs = function (clusterLinks, interfaceInfo, optIfId) {
        var ifs = [];
        if (optIfId) {
            ifs.push(optIfId);
        }
        return ifs;
    }
    ;
    //
    // 指定された条件に関連するBreakoutIfの配列を返す
    //
    MSF.MsfDetailFailureInfo.prototype.getRelBreakoutIfs = function (clusterLinks, interfaceInfo, optIfId) {
        var ifs = [];
        if (optIfId) {
            // 物理IFからbreakoutIFIDを特定
            for (var i = 0; i < interfaceInfo.breakout_ifs.length; i++) {
                var breakoutIf = interfaceInfo.breakout_ifs[i];
                if (breakoutIf.base_if.physical_if_id == optIfId) {
                    ifs.push(breakoutIf.breakout_if_id);
                }
            }
        }
        return ifs;
    }
    ;
    //
    // 指定された条件に関連するLagIfの配列を返す
    //
    MSF.MsfDetailFailureInfo.prototype.getRelLagIfs = function (clusterLinks, interfaceInfo, optIfId) {
        var ifs = [];
        if (optIfId) {
            // 物理IFからlagIFIDを特定
            for (var i = 0; i < interfaceInfo.lag_ifs.length; i++) {
                var lagIf = interfaceInfo.lag_ifs[i];
                if (lagIf.physical_if_ids.indexOf(optIfId) != -1) {
                    ifs.push(lagIf.lag_if_id);
                }
                for (var j = 0; j < lagIf.breakout_if_ids.length; j++) {
                    if (this.isTargetlagIf(interfaceInfo.breakout_ifs, lagIf.breakout_if_ids[j], optIfId)) {
                        ifs.push(lagIf.lag_if_id);
                    }
                }
            }
        }
        return ifs;
    }
    ;
    //
    // LagIfを構成するIFが指定されたIF IDと一致するか判定する
    //
    MSF.MsfDetailFailureInfo.prototype.isTargetlagIf = function (breakoutIfs, breakoutIfId, physicalIfId) {
        for (var i = 0; i < breakoutIfs.length; i++) {
            if (breakoutIfs[i].breakout_if_id != breakoutIfId) {
                continue;
            }
            if (breakoutIfs[i].base_if.physical_if_id == physicalIfId) {
                return true;
            }
        }
        return false;
    }
    ;
    //
    // クラスタ間リンクIFに関連するフィルタ条件(ifFilter)を生成
    //
    MSF.MsfDetailFailureInfo.prototype.getRelClusterLinkIfs = function (baseData, clusterLinkId, ifFilter) {

        var clusterId = clusterLinkId.slice(0, clusterLinkId.indexOf("-"));
        var clusterInfo = baseData.clusterInfoDic[clusterId];
        var clusterLinks = clusterInfo.cluster_link_ifs;
        
        ifFilter.clusterId = clusterId;
        var oppositeClusterId = clusterLinkId.slice(clusterLinkId.indexOf("-") + 1);
        var targetLink = {};
        for (var i = 0; i < clusterLinks.length; i++) {
            if (clusterLinks[i].opposite_cluster_id == oppositeClusterId) {
                targetLink = clusterLinks[i];
                break;
            }
        }
        
        var physicalLink = targetLink.physical_link;
        if (physicalLink) {
            ifFilter.nodeId = physicalLink.node_id;
            if (physicalLink.physical_if_id) {
                ifFilter.pIds.push(physicalLink.physical_if_id);
            }
            if (physicalLink.breakout_if_id) {
                ifFilter.bIds.push(physicalLink.breakout_if_id);
            }
        } else {
            ifFilter.nodeId = targetLink.lag_link.node_id;
            ifFilter.lIds.push(targetLink.lag_link.lag_if_id);
        }
    }
    ;
    //
    // 詳細情報テーブル（Slice Failure Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID
    //
    MSF.MsfDetailFailureInfo.prototype.createRecordsSlice = function (baseData, sliceType, sliceId) {

        // スライスIDでフィルタ (未指定の場合は全件)
        var target = this.getTargetSliceFailure(baseData.failure_status.slice_unit, sliceType, sliceId);
        var records = [];
        for (var i = 0; i < target.length; i++) {
            records.push(this.getRecordSlice(target[i]));
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_SLICE, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // 詳細情報テーブル（IF/Node Failure Info）レコード生成.
    // failure: 障害情報 (装置情報またはIF情報)
    // isIfs  : IF情報の場合true
    //
    MSF.MsfDetailFailureInfo.prototype.getRecordPhysical = function (failure, isIfs) {

        var record = [];
        // if0-node0: クラスタID
        record.push(failure.cluster_id);
        // if1-node1: 装置ID
        record.push(getDeviceTypeNumber(failure.fabric_type) + failure.node_id);

        var portPrefix = "";
        if (isIfs) {
            // if2: IF ID
            portPrefix = (failure.if_type == MSF.Const.RestIfType.LagIf) ? "LAG" : "port";
            record.push(portPrefix + failure.if_id);
        }
        // if3-node2: 障害状態
        record.push(failure.failure_status);

        // ソート用の列を追加
        // if4-node3: cluster id ※可能であれば数値化して格納
        record.push(isNaN(failure.cluster_id) ? failure.cluster_id : parseInt(failure.cluster_id, 10));
        // if5-node4: node type
        record.push(failure.fabric_type);
        // if6-node5: node id ※可能であれば数値化して格納
        record.push(isNaN(failure.node_id) ? failure.node_id : parseInt(failure.node_id, 10));
        if (isIfs) {
            // if7: ポート種別prefix
            record.push(portPrefix);
            // if8: if id ※可能であれば数値化して格納
            record.push(isNaN(failure.if_id) ? failure.if_id : parseInt(failure.if_id, 10));
        }
        return record;
    }
    ;
    //
    // 詳細情報テーブル（Slice Failure Info）レコード生成.
    //
    MSF.MsfDetailFailureInfo.prototype.getRecordSlice = function (failure) {

        var record = [];
        // レコード生成前に到達可能性ステータスをソート
        failure.reachable_statuses.sort(this.compareReachableStatus);
        // 0: スライスID
        record.push(this.getSliceId(failure));
        // 1: スライス障害状態
        record.push(failure.failure_status);
        // 2: CP ID(1)
        record.push(this.getCpIds(failure));
        // 3: CP ID(2)
        record.push(this.getOppositeCpIds(failure));
        // 4: reachable
        record.push(this.getReachables(failure));
        
        return record;
    }
    ;
    //
    // 到達可能性ステータスリストをソート
    //   第1キー：cp_id 第2キー:opposite_id
    //
    MSF.MsfDetailFailureInfo.prototype.compareReachableStatus = function (a, b) {
        
        // 第1キー：cp_id
        var aKey1 = a.cp_id;
        var bKey1 = b.cp_id;
        if (aKey1 < bKey1) {
            return -1;
        }
        if (aKey1 > bKey1) {
            return 1;
        }
        // 第2キー：opposite_id
        var aKey2 = a.opposite_id;
        var bKey2 = b.opposite_id;
        if (aKey2 < bKey2) {
            return -1;
        }
        if (aKey2 > bKey2) {
            return 1;
        }
        return 0;
    }
    ;
    //
    // 対象の障害情報(装置単位)を取得.
    // physicalUnit : IF障害情報リスト
    // optClusterId : クラスタID
    // optNodeType : 装置種別
    // optNodeId : 装置ID
    // return : 表示対象の障害情報配列
    //
    MSF.MsfDetailFailureInfo.prototype.getTargetNodeFailure = function (physicalUnit, optClusterId, optNodeType, optNodeId) {
        
        var target = [];
        var nodes = physicalUnit.nodes || [];
        for (var i = 0; i < nodes.length; i++) {
            var failure = nodes[i];
            // 障害状態 up は対象外
            if (failure.failure_status == "up") {
                continue;
            }
            // クラスタIDでフィルタ
            if (optClusterId && optClusterId != failure.cluster_id) {
                continue;
            }
            // 装置種別でフィルタ
            if (optNodeType && optNodeType != MSF.Const.getFabricTypePlural(failure.fabric_type)) {
                continue;
            }
            // 装置IDでフィルタ
            if (optNodeId && optNodeId != failure.node_id) {
                continue;
            }
            target.push(failure);
        }
        return target;
    }
    ;
    //
    // 対象の障害情報(IF単位)を取得.
    // physicalUnit : IF障害情報リスト
    // optClusterId : クラスタID
    // optNodeType : 装置種別
    // optNodeId : 装置ID
    // ifFilter : 選択physicalIfIdの関連IF情報
    // return : 表示対象の障害情報配列
    //
    MSF.MsfDetailFailureInfo.prototype.getTargetIfFailure = function (physicalUnit, optClusterId, optNodeType, optNodeId, ifFilter) {
        
        var target = [];
        var ifs = physicalUnit.ifs || [];
        for (var i = 0; i < ifs.length; i++) {
            var failure = ifs[i];
            // 障害状態 up は対象外
            if (failure.failure_status == "up") {
                continue;
            }
            // クラスタIDでフィルタ
            if (optClusterId && optClusterId != failure.cluster_id) {
                continue;
            }
            // 装置種別でフィルタ
            if (optNodeType && optNodeType != MSF.Const.getFabricTypePlural(failure.fabric_type)) {
                continue;
            }
            // 装置IDでフィルタ
            if (optNodeId && optNodeId != failure.node_id) {
                continue;
            }
            // IF種別/IDでフィルタ
            if (ifFilter) {
                if (failure.if_type == "physical-if") {
                    if (ifFilter.pIds.indexOf(failure.if_id) == -1) {
                        continue;
                    }
                } else if (failure.if_type == "breakout-if") {
                    if (ifFilter.bIds.indexOf(failure.if_id) == -1) {
                        continue;
                    }
                } else if (ifFilter && failure.if_type == "lag-if") {
                    if (ifFilter.lIds.indexOf(failure.if_id) == -1) {
                        continue;
                    }
                }
            }
            target.push(failure);
        }
        return target;
    }
    ;
    //
    // 対象の障害情報(スライス単位)を取得.
    // sliceUnit : スライス単位障害情報
    // optSliceType : スライス種別
    // optSliceId:  スライスID
    // return : 表示対象の障害情報配列
    //
    MSF.MsfDetailFailureInfo.prototype.getTargetSliceFailure = function (sliceUnit, optSliceType, optSliceId) {
        
        // 全てのスライス単位(スライス)の障害障害
        // 選択スライスのスライス単位(スライス)の障害障害
        var target = [];
        var slices = sliceUnit.slices || [];
        for (var i = 0; i < slices.length; i++) {
            var failure = slices[i];
            // 障害状態 up は対象外
            if (failure.failure_status == "up") {
                continue;
            }
            if (optSliceType && optSliceType != failure.slice_type) {
                continue;
            }
            if (optSliceId && optSliceId != failure.slice_id) {
                continue;
            }
            target.push(failure);
        }
        return target;
    }
    ;
    //
    // 障害テーブル表示用のスライスIDを取得.
    // failure : スライス単位障害情報
    // return : 表示用スライスID
    //
    MSF.MsfDetailFailureInfo.prototype.getSliceId = function (failure) {
        var id = failure.slice_type + failure.slice_id;
        return id;
    }
    ;
    //
    // 障害テーブル表示用のCP IDを取得.
    // failure : スライス単位障害情報
    // return : 表示用CP ID(1)
    //
    MSF.MsfDetailFailureInfo.prototype.getCpIds = function (failure) {
        var cpIds = [];
        var reachableStatuses = failure.reachable_statuses || [];
        for (var i = 0; i < reachableStatuses.length; i++) {
            var rs = reachableStatuses[i];
            if (rs.opposite_type == "cp") {
                cpIds.push(rs.cp_id);
            }
        }
        if (cpIds.length == 0) {
            return "-";
        }
        return cpIds.join("<br>");
    }
    ;
    //
    // 障害テーブル表示用のCP ID (opposite)を取得.
    // failure : スライス単位障害情報
    // return : 表示用CP ID(2)
    //
    MSF.MsfDetailFailureInfo.prototype.getOppositeCpIds = function (failure) {
        var cpIds = [];
        var reachableStatuses = failure.reachable_statuses || [];
        for (var i = 0; i < reachableStatuses.length; i++) {
            var rs = reachableStatuses[i];
            if (rs.opposite_type == "cp") {
                cpIds.push(rs.opposite_id);
            }
        }
        if (cpIds.length == 0) {
            return "-";
        }
        return cpIds.join("<br>");
    }
    ;
    //
    // 障害テーブル表示用のReachableを取得.
    // failure : スライス単位障害情報
    // return : 表示用reachable
    //
    MSF.MsfDetailFailureInfo.prototype.getReachables = function (failure) {
        var reachables = [];
        var reachableStatuses = failure.reachable_statuses || [];
        for (var i = 0; i < reachableStatuses.length; i++) {
            var rs = reachableStatuses[i];
            if (rs.opposite_type == "cp") {
                reachables.push(rs.reachable_status);
            }
        }
        if (reachables.length == 0) {
            return "-";
        }
        return reachables.join("<br>");
    }
    ;
})();
