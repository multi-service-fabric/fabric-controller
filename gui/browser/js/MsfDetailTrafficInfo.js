//
// MSF詳細エリアクラス(トラヒック情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailTrafficInfo = function() {
        // 更新対象ID
        this.TARGET_ID_IF = "#detailTable8-1";
        this.TARGET_ID_CP_L2 = "#detailTable8-2";
        this.TARGET_ID_CP_L3 = "#detailTable8-3";
        // カラム数
        this.COL_NUM_IF = 6;
        this.COL_NUM_CP_L2 = 4;
        this.COL_NUM_CP_L3 = 4;
        // ソート対象レコード
        this.SORT_COLS_IF = [6, 7, 8, 2, 9];
        this.SORT_COLS_CP_L2 = [0, 1];
        this.SORT_COLS_CP_L3 = [0, 1];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（IF Traffic Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailTrafficInfo.prototype.createRecordsIf = function (baseData, clusterId, clusterLinkId, isClusterLink, deviceType, nodeId, ifId) {
    
        var records = [];
        var targetTraffics = {};
        var clusterInfo;
        if (isClusterLink) {
            clusterId = (clusterLinkId) ? clusterLinkId.slice(0, clusterLinkId.indexOf("-")) : clusterId;
            if (clusterId) {
                clusterInfo = baseData.clusterInfoDic[clusterId];
                targetTraffics[clusterId] = this.getTargetClusterLinkTraffics(clusterInfo, clusterInfo.if_traffics, clusterLinkId);
            } else {
                for (var i = 0; i < baseData.sw_clusters.length; i++) {
                    clusterId = baseData.sw_clusters[i].cluster_id;
                    clusterInfo = baseData.clusterInfoDic[clusterId];
                    targetTraffics[clusterId] = this.getTargetClusterLinkTraffics(clusterInfo, clusterInfo.if_traffics, clusterLinkId);
                }
            }
        } else {
            clusterInfo = baseData.clusterInfoDic[clusterId];
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
            targetTraffics[clusterId] = this.getTargetIfTraffics(clusterInfo, deviceType, nodeId, ifId);
        }
        
        for (var cId in targetTraffics) {
            for (var i = 0; i < targetTraffics[cId].length; i++) {
                var traffic = targetTraffics[cId][i];
                records.push(this.getRecordIf(cId, traffic));
            }
        }

        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_IF, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // 詳細情報出力対象のIFトラヒック情報を返す
    //
    MSF.MsfDetailTrafficInfo.prototype.getTargetIfTraffics = function (clusterInfo, deviceType, nodeId, ifId) {
        
        var targetTraffics = [];
        var ifTraffics = clusterInfo.if_traffics;
        
        for (var dType in ifTraffics) {
            if (deviceType && deviceType != dType) {
                continue;
            }
            var nodeTraffics = ifTraffics[dType];
            
            for (var nId in nodeTraffics) {
                if (nodeId && nodeId != nId) {
                    continue;
                }
                var ifInfo = clusterInfo.InterfacesInfo[dType][nId];
                var traffics = nodeTraffics[nId].if_traffics || [];
                for (var i = 0; i < traffics.length; i++) {
                    var traffic = traffics[i];
                    var physicalIf = getBaseIfIdDetail(ifInfo, traffic.traffic_value.if_type, traffic.traffic_value.if_id);
                    if (ifId && physicalIf.indexOf(ifId) == -1) {
                        continue;
                    }
                    targetTraffics.push(traffic);
                }
            }
        }
        return targetTraffics;
    }
    ;
    //
    // 詳細情報出力対象のIFトラヒック情報を返す(クラスタ間リンク選択時)
    //
    MSF.MsfDetailTrafficInfo.prototype.getTargetClusterLinkTraffics = function (clusterInfo, ifTraffics, clusterLinkId) {
        
        var targetIfs = [];
        var opposite = null;
        if (clusterLinkId) {
            opposite = clusterLinkId.slice(clusterLinkId.indexOf("-") + 1);
        }
        for (var i = 0; i < clusterInfo.cluster_link_ifs.length; i++) {
            var clusterLink = clusterInfo.cluster_link_ifs[i];
            if (opposite && opposite != clusterLink.opposite_cluster_id) {
                continue;
            }
            var nodeId = clusterLink.physical_link ? clusterLink.physical_link.node_id : clusterLink.lag_link.node_id;
            var ifType = MSF.Const.RestIfType.LagIf;
            var ifId;
            if (clusterLink.physical_link) {
                if (clusterLink.physical_link.physical_if_id) {
                    ifType = MSF.Const.RestIfType.PhysicalIf;
                    ifId = clusterLink.physical_link.physical_if_id;
                } else {
                    ifType = MSF.Const.RestIfType.BreakoutIf;
                    ifId = clusterLink.physical_link.breakout_if_id;
                }
            } else {
                ifId = clusterLink.lag_link.lag_if_id;
            }
            var targetIf = this.getClusterLinkIfTraffic(ifTraffics, nodeId, ifType, ifId);
            if (targetIf) {
                targetIfs.push(targetIf);
            }
        }
        return targetIfs;
    }
    ;
    //
    // クラスタ間リンクIFを構成するIFのトラヒック情報を返す
    //
    MSF.MsfDetailTrafficInfo.prototype.getClusterLinkIfTraffic = function (ifTraffics, nodeId, ifType, ifId) {
        
        var leafIfTraffics = ifTraffics[MSF.Const.FabricType.Leafs];
        for (var nId in leafIfTraffics) {
            if (nId != nodeId) {
                continue;
            }
            
            var traffics = (leafIfTraffics[nId]) ? leafIfTraffics[nId].if_traffics : [];
            for (var i = 0; i < traffics.length; i++) {
                var traffic = traffics[i];
                if (traffic.traffic_value.if_type != ifType) {
                    continue;
                }
                if (traffic.traffic_value.if_id != ifId) {
                    continue;
                }
                return traffic;
            }
        }
        return null;
    }
    ;
    //
    // 詳細情報テーブル（L2 CP Traffic Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailTrafficInfo.prototype.createRecordsCpL2 = function (baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId, isMap) {
    
        var records = [];
        var targetTraffics = [];
        // Map表示の場合はスライス名でCPをフィルタして表示
        if (isMap) {
            // sliceId :未選択の場合は全て
            targetTraffics = this.getTargetCpTrafficsSlice(baseData.l2cp_traffics, MSF.Const.SliceType.L2, sliceId);
        } else {
            // 選択装置のCPトラヒック情報(L2) (CPなのでLeafのみ) ※スライス選択
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
            targetTraffics = this.getTargetCpTrafficsL2(baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId);
        }

        for (var i = 0; i < targetTraffics.length; i++) {
            records.push(this.getRecordCpL2(targetTraffics[i]));
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_CP_L2, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // 詳細情報テーブル（L3 CP Traffic Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailTrafficInfo.prototype.createRecordsCpL3 = function (baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId, isMap) {
    
        var records = [];
        var targetTraffics = [];
        // Map表示の場合はスライス名でCPをフィルタして表示
        if (isMap) {
            // sliceId :未選択の場合は全て
            targetTraffics = this.getTargetCpTrafficsSlice(baseData.l3cp_traffics, MSF.Const.SliceType.L3, sliceId);
        } else {
            // 選択装置のCPトラヒック情報(L3) (CPなのでLeafのみ) ※スライス選択
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
            targetTraffics = this.getTargetCpTrafficsL3(baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId);
        }

        for (var i = 0; i < targetTraffics.length; i++) {
            records.push(this.getRecordCpL3(targetTraffics[i]));
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_CP_L3, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer));
        return records;
    }
    ;
    //
    // 詳細情報出力対象のCPトラヒック情報(L2)を返す
    //
    MSF.MsfDetailTrafficInfo.prototype.getTargetCpTrafficsL2 = function (baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId) {
        
        var targetTraffics = [];
        if (sliceType && sliceType != MSF.Const.SliceType.L2) {
            return targetTraffics;
        }
        if (deviceType && deviceType != MSF.Const.FabricType.Leafs) {
            return targetTraffics;
        }
        
        var cpInfoAll = baseData.CPInfo;
        var cpTraffics = baseData.l2cp_traffics;
        for ( var sId in cpTraffics) {
            if (sliceId && sliceId != sId) {
                continue;
            }
            var sliceCpTraffics = cpTraffics[sId].l2cp_traffics || [];
            for (var i = 0; i < sliceCpTraffics.length; i++) {
                var traffic = sliceCpTraffics[i];
                var cpInfo = cpInfoAll[MSF.Const.SliceType.L2 + sId][traffic.cp_id];
                // クラスタ内のCPトラヒック情報でなければ除外
                if (clusterId && cpInfo.cluster_id != clusterId) {
                    continue;
                }
                // 選択装置のCPトラヒック情報でなければ除外
                var relLeafId = getNodeIdForCpId(baseData, MSF.Const.SliceType.L2, sId, traffic.cp_id);
                if (nodeId &&  nodeId != relLeafId) {
                    continue;
                }
                // 選択スライスに所属する装置のCPトラヒック情報でなければ除外
                if (leafs && !leafs[relLeafId]) {
                    continue;
                }
                targetTraffics.push(traffic);
            }
        }
        return targetTraffics;
    }
    ;
    //
    // 詳細情報出力対象のCPトラヒック情報(L3)を返す
    //
    MSF.MsfDetailTrafficInfo.prototype.getTargetCpTrafficsL3 = function (baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId) {
        
        var targetTraffics = [];
        if (sliceType && sliceType != MSF.Const.SliceType.L3) {
            return targetTraffics;
        }
        if (deviceType && deviceType != MSF.Const.FabricType.Leafs) {
            return targetTraffics;
        }
        
        var cpInfoAll = baseData.CPInfo;
        var cpTraffics = baseData.l3cp_traffics;
        for ( var sId in cpTraffics) {
            if (sliceId && sliceId != sId) {
                continue;
            }
            var sliceCpTraffics = cpTraffics[sId].l3cp_traffics || [];
            for (var i = 0; i < sliceCpTraffics.length; i++) {
                var traffic = sliceCpTraffics[i];
                var cpInfo = cpInfoAll[MSF.Const.SliceType.L3 + sId][traffic.cp_id];
                // クラスタ内のCPトラヒック情報でなければ除外
                if (clusterId && cpInfo.cluster_id != clusterId) {
                    continue;
                }
                // 選択装置のCPトラヒック情報でなければ除外
                var relLeafId = getNodeIdForCpId(baseData, MSF.Const.SliceType.L3, sId, traffic.cp_id);
                if (nodeId &&  nodeId != relLeafId) {
                    continue;
                }
                // 選択スライスに所属する装置のCPトラヒック情報でなければ除外
                if (leafs && !leafs[relLeafId]) {
                    continue;
                }
                targetTraffics.push(traffic);
            }
        }
        return targetTraffics;
    }
    ;
    //
    // 詳細情報出力対象のCPトラヒック情報を返す(スライス選択時)
    //
    MSF.MsfDetailTrafficInfo.prototype.getTargetCpTrafficsSlice = function (cpTraffics, sliceType, sliceId) {
        
        var targetTraffics = [];
//        if (sliceType && sliceType != MSF.Const.SliceType.L3) {
//            return targetTraffics;
//        }
        
        for (var sId in cpTraffics) {
            if (sliceId && sliceId != sId) {
                continue;
            }
            var sliceCpTraffics = [];
            if (sliceType == MSF.Const.SliceType.L2)
                sliceCpTraffics = cpTraffics[sId].l2cp_traffics;
            if (sliceType == MSF.Const.SliceType.L3)
                sliceCpTraffics = cpTraffics[sId].l3cp_traffics;

            for (var i = 0; i < sliceCpTraffics.length; i++) {
                var traffic = sliceCpTraffics[i];
                targetTraffics.push(traffic);
            }
        }
        return targetTraffics;
    }
    ;
    //
    // 詳細情報テーブル（IF Traffic Info）レコード生成.
    //
    MSF.MsfDetailTrafficInfo.prototype.getRecordIf = function (clusterId, traffic) {

        var val = traffic.traffic_value;
        var record = [];
        // 0: ClusterID
        record.push(clusterId);
        // 1: 装置ID
        var deviceType = traffic.fabric_type;
        var nodeId = traffic.node_id;
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: IF 種別
        record.push(val.if_type);
        // 3: IF ID
        var ifId = val.if_id;
        record.push(ifId);
        // 4: 送信レート
        record.push(getTrafficRate(val.send_rate));
        // 5: 受信レート
        record.push(getTrafficRate(val.receive_rate));
    
        // ソート用の列を追加
        // 6: ClusterID
        record.push(isNaN(clusterId) ? clusterId : parseInt(clusterId, 10));
        // 7: deviceType
        record.push(deviceType);
        // 8: node_id ※可能であれば数値化して格納
        record.push(isNaN(nodeId) ? nodeId : parseInt(nodeId, 10));
        // 9: if_id ※可能であれば数値化して格納
        record.push(isNaN(ifId) ? ifId : parseInt(ifId, 10));
    
        return record;
    }
    ;
    //
    // 詳細情報テーブル（L2CP Traffic Info）レコード生成.
    //
    MSF.MsfDetailTrafficInfo.prototype.getRecordCpL2 = function (traffic) {

        var val = traffic.traffic_value;
        var record = [];
        // 0: スライスID
        var sliceId = traffic.slice_id;
        record.push(sliceId);
        // 1: CP ID
        var cpId = traffic.cp_id;
        record.push(cpId);
        // 2: 送信レート
        record.push(getTrafficRate(val.send_rate));
        // 3: 受信レート
        record.push(getTrafficRate(val.receive_rate));
    
        // ソート用の列を追加
        // 4: slice_id 数値化して格納
        record.push(isNaN(sliceId) ? sliceId : parseInt(sliceId, 10));
        // 5: cp_id 数値化して格納
        record.push(isNaN(cpId) ? cpId : parseInt(cpId, 10));
        return record;
    }
    ;
    //
    // 詳細情報テーブル（L3CP Traffic Info）レコード生成.
    //
    MSF.MsfDetailTrafficInfo.prototype.getRecordCpL3 = function (traffic) {

        var val = traffic.traffic_value;
        var record = [];
        // 0: スライスID
        var sliceId = traffic.slice_id;
        record.push(sliceId);
        // 1: CP ID
        var cpId = traffic.cp_id;
        record.push(cpId);
        // 2: 送信レート
        record.push(getTrafficRate(val.send_rate));
        // 3: 受信レート
        record.push(getTrafficRate(val.receive_rate));
    
        // ソート用の列を追加
        // 4: slice_id 数値化して格納
        record.push(isNaN(sliceId) ? sliceId : parseInt(sliceId, 10));
        // 5: cp_id 数値化して格納
        record.push(isNaN(cpId) ? cpId : parseInt(cpId, 10));
        return record;
    }
    ;
})();
