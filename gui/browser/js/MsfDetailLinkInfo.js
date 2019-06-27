//
// MSF詳細エリアクラス(Link情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailLinkInfo = function() {
        // 更新対象ID
        this.TARGET_ID_INTER = "#detailTable5-1";
        this.TARGET_ID_INTRA = "#detailTable5-2";
        // カラム数
        this.COL_NUM_INTER = 7;
        this.COL_NUM_INTRA = 8;
        // ソート対象レコード
        this.SORT_COLS_INTER = [0, 7, 8];
        this.SORT_COLS_INTRA = [0, 8, 9, 10];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Inter-Cluster Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailLinkInfo.prototype.createRecordsInter = function (baseData, clusterId, clusterLinkId, deviceType, nodeId, ifId) {
        
        var records = [];
        var targetIfs = [];
        var clusterInfo;
        var i;
        if (!clusterLinkId && !clusterId) {
            for (i = 0; i < baseData.sw_clusters.length; i++) {
                var cId = baseData.sw_clusters[i].cluster_id;
                clusterInfo = baseData.clusterInfoDic[cId];
                targetIfs = this.getTargetClusterLink(clusterInfo);
                for (var j = 0; j < targetIfs.length; j++) {
                    records.push(this.getRecordInter(cId, targetIfs[j]));
                }
            }
        } else {
            if (clusterLinkId) {
                clusterId = clusterLinkId.slice(0, clusterLinkId.indexOf("-"));
            }
            clusterInfo = baseData.clusterInfoDic[clusterId];
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
            targetIfs = this.getTargetClusterLink(clusterInfo, clusterLinkId, deviceType, nodeId, ifId);
            for (i = 0; i < targetIfs.length; i++) {
                records.push(this.getRecordInter(clusterId, targetIfs[i]));
            }
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_INTER, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // Figure部で選択中のオブジェクトに該当するクラスタ間リンク情報を取り出す.
    // clusterInfo:   表示中のクラスタ情報
    // clusterLinkId: 選択中のクラスタ間リンクIF ID
    // deviceType:    選択中の装置種別
    // nodeId:        選択中の装置ID
    // ifId:          選択中のIFID
    //
    MSF.MsfDetailLinkInfo.prototype.getTargetClusterLink = function (clusterInfo, clusterLinkId, deviceType, nodeId, ifId) {
        
        var targetIfs = [];
        if (deviceType && deviceType == MSF.Const.FabricType.Spines) {
            return targetIfs;
        }
        var opposite = null;
        if (clusterLinkId) {
            opposite = clusterLinkId.slice(clusterLinkId.indexOf("-") + 1);
        }
        for (var i = 0; i < clusterInfo.cluster_link_ifs.length; i++) {
            var clusterLink = clusterInfo.cluster_link_ifs[i];
            if (opposite) {
                if (opposite == clusterLink.opposite_cluster_id) {
                    targetIfs.push(clusterLink);
                    return targetIfs;
                }
                continue;
            }
            if (!nodeId) {
                targetIfs.push(clusterLink);
                continue;
            }
            var link = clusterLink.physical_link || clusterLink.lag_link;
            if (nodeId != link.node_id) {
                continue;
            }
            if (!ifId) {
                targetIfs.push(clusterLink);
                continue;
            }
            var pIds = [];
            var ifInfo = clusterInfo.InterfacesInfo[MSF.Const.FabricType.Leafs][nodeId];
            if (link.physical_if_id) {
                pIds = getBaseIfIdDetail(ifInfo, "physical", link.physical_if_id);
            } else if (link.breakout_if_id) {
                pIds = getBaseIfIdDetail(ifInfo, "breakout", link.breakout_if_id);
            } else if (link.lag_if_id) {
                pIds = getBaseIfIdDetail(ifInfo, "lag", link.lag_if_id);
            }
            if (pIds.indexOf(ifId) == -1) {
                continue;
            }
            targetIfs.push(clusterLink);
        }
        return targetIfs;
    }
    ;
    //
    // 詳細情報テーブル（Intra-Cluster Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailLinkInfo.prototype.createRecordsIntra = function (baseData, clusterId, deviceType, nodeId, ifId) {
        
        var records = [];
        var clusterInfo = baseData.clusterInfoDic[clusterId];
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        var targetIntraData = this.getTargetInternalLink(clusterInfo, deviceType, nodeId, ifId);

        for (var i = 0; i < targetIntraData.length; i++) {
            records.push(this.getRecordIntra(clusterId, targetIntraData[i]));
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS_INTRA, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // Figure部で選択中のオブジェクトに該当する内部リンク情報を取り出す.
    // clusterInfo: 表示中のクラスタ情報
    // deviceType:  選択中の装置種別
    // nodeId:      選択中の装置ID
    // ifId:        選択中のIFID
    //
    MSF.MsfDetailLinkInfo.prototype.getTargetInternalLink = function (clusterInfo, deviceType, nodeId, ifId) {
        
        var targetData = [];
        
        for (var type in clusterInfo.NodesInfo) {
            if (deviceType && deviceType != type) {
                continue;
            }
            var nodes = clusterInfo.NodesInfo[type];
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                var internalLinks = node.internal_link_ifs;
                if (!internalLinks) {
                    continue;
                }
                if (nodeId && nodeId != node.node_id) {
                    continue;
                }
                var ifInfo = clusterInfo.InterfacesInfo[type][node.node_id];
                targetData = targetData.concat(this.selectTargetIfs(ifInfo, internalLinks, node, ifId, clusterInfo.NodesInfo));
            }
        }
        return targetData;
    }
    ;
    //
    // 装置単位で詳細テーブル表示用データを生成.
    // ifInfo:        クラスタ内のIF情報
    // internalLinks: 装置の内部リンク向けIF情報
    // node:          対象の装置情報
    // ifId:          NodeViewで選択中のIfId
    // nodesInfo:     クラスタ内の装置情報
    //
    MSF.MsfDetailLinkInfo.prototype.selectTargetIfs = function (ifInfo, internalLinks, node, ifId, nodesInfo) {
        
        var lagIfId;
        var breakoutIfId;
        var physicalIfIds;
        
        var targetData = [];
        for (var i = 0; i < internalLinks.length; i++) {
            var iLink = internalLinks[i];
            var iLinkId = iLink.internal_link_if_id;
            
            lagIfId = iLink.lag_if_id;
            breakoutIfId = iLink.breakout_if_id;
            var ifKey = "physical_ifs";
            var idKey = "physical_if_id";
            var targetId = iLink.physical_if_id;
            if (lagIfId) {
                physicalIfIds = getBaseIfIdDetail(ifInfo, "lag", lagIfId);
                ifKey = "lag_ifs";
                idKey = "lag_if_id";
                targetId = lagIfId;
            } else if (breakoutIfId) {
                physicalIfIds = getBaseIfIdDetail(ifInfo, "breakout", breakoutIfId);
                ifKey = "breakout_ifs";
                idKey = "breakout_if_id";
                targetId = breakoutIfId;
            } else {
                physicalIfIds = getBaseIfIdDetail(ifInfo, "breakout", iLink.physical_if_id);
            }
            if (ifId && physicalIfIds.indexOf(ifId) == -1) {
                continue;
            }
            
            for (var j = 0; j < node[ifKey].length; j++) {
                var pblIf = node[ifKey][j];
                if (pblIf[idKey] != targetId) {
                    continue;
                }
                targetData.push(this.createDetailData(iLinkId, node, idKey, targetId, pblIf.internal_options, nodesInfo));
            }
        }
        return targetData;
    }
    ;
    //
    // 詳細情報テーブル（内部リンク）のレコード生成用のデータを生成.
    // iLinkIfId:        内部リンクIF ID
    // node:             装置情報
    // idKey:            IF種別（ポート情報のプレフィックス用）
    // ifId:             IF ID
    // internalOptions:  内部リンク向けIF情報
    // nodesInfo:        クラスタに属する装置情報（対向の内部リンクIF ID抽出用）
    // return:           レコード生成用のデータ
    //
    MSF.MsfDetailLinkInfo.prototype.createDetailData = function (iLinkIfId, node, idKey, ifId, internalOptions, nodesInfo) {
        
        var recordData = [];
        var deviceType = MSF.Const.FabricType.Spines;
        var opDeviceType = MSF.Const.FabricType.Leafs;
        if (node.leaf_type) {
            deviceType = MSF.Const.FabricType.Leafs;
            opDeviceType = MSF.Const.FabricType.Spines;
        }
        recordData.push(deviceType);
        recordData.push(node.node_id);
        recordData.push(iLinkIfId);
        var fabricIfId = "port" + ifId;
        if (idKey == "lag_if_id") {
            fabricIfId = "LAG" + ifId;
        }
        recordData.push(fabricIfId);
        if (internalOptions == null) {
            // 対向装置ID
            recordData.push("-");
            // 対向装置内部リンクIFID
            recordData.push("-");
            // IPv4アドレス
            recordData.push("-");
            // トラヒック閾値
            recordData.push("-");
        } else {
            // 対向装置ID
            var oppositeNode = getDeviceTypeNumber(opDeviceType) + internalOptions.opposite_if.node_id;
            recordData.push(oppositeNode);
            // 対向装置内部リンクIFID
            var opDevType = MSF.Const.getFabricTypePlural(internalOptions.opposite_if.fabric_type);
            var opNodeId = internalOptions.opposite_if.node_id;
            var opIfType = internalOptions.opposite_if.if_type ? internalOptions.opposite_if.if_type : MSF.Const.RestIfType.LagIf;
            var opIfId = internalOptions.opposite_if.if_id || internalOptions.opposite_if.lag_if_id;
            var oppositeLinkIfId = this.getOppositeLinkIfId(nodesInfo, opDevType, opNodeId, opIfType, opIfId);
            recordData.push(oppositeLinkIfId);
            // IPv4アドレス
            var ipAddr = internalOptions.ipv4_address;
            recordData.push(ipAddr);
            // トラヒック閾値
            var threshold = (internalOptions.traffic_threshold != null) ? internalOptions.traffic_threshold : "-";
            recordData.push(threshold);
        }
        return recordData;
    }
    ;
    //
    // 対向情報から対向リンクIF IDを取得. (失敗時は"-"を返す)
    // nodesInfo:    装置情報
    // opDeviceType: 対向装置種別
    // opNodeId:     対向装置ID
    // opIfType:     対向IF種別
    // opIfId:       対向IF ID
    //
    MSF.MsfDetailLinkInfo.prototype.getOppositeLinkIfId = function (nodesInfo, opDeviceType, opNodeId, opIfType, opIfId) {
        
        // 対向の装置を特定
        var nodes = nodesInfo[opDeviceType] || [];
        var node = {};
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].node_id == opNodeId) {
                node = nodes[i];
                break;
            }
        }
        // ノード情報から対象のIF種別、IF IDを取り出すためのキーを設定
        var keyIfId = (opIfType == MSF.Const.RestIfType.PhysicalIf) ? "physical_if_id" : "breakout_if_id";
        if (opIfType == MSF.Const.RestIfType.LagIf) {
            keyIfId = "lag_if_id";
        }

        // 対向装置の内部リンク情報一覧から、IF種別、IF IDが一致する内部リンクIFIDを取り出す
        var opInternalLinkIfId = "-";
        var internalLinkIfs = node.internal_link_ifs;
        for (var i = 0; i < internalLinkIfs.length; i++) {
            if (internalLinkIfs[i][keyIfId] == opIfId) {
                opInternalLinkIfId = internalLinkIfs[i].internal_link_if_id;
                break;
            }
        }
        return opInternalLinkIfId;
    }
    ;
    //
    // 詳細情報テーブル（Inter-Cluster Info）レコード生成.
    // clusterId: クラスタID
    // clusterlinkif: レコード生成用データ
    // return: レコード1件分のカラム配列
    //
    MSF.MsfDetailLinkInfo.prototype.getRecordInter = function (clusterId, clusterlinkif) {

        var record = [];
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        var anyLink = clusterlinkif.physical_link || clusterlinkif.lag_link;
        record.push(getDeviceTypeNumber(MSF.Const.FabricType.Leafs) + anyLink.node_id);
        // 2: IF ID (cluster link if id)
        var linkIfId = clusterlinkif.cluster_link_if_id;
        record.push(linkIfId);
        // 3: 構成IF ID (physical_if_id/breakout_if_id/lag_if_id)
        var ifId = clusterlinkif.physical_link;
        var portPrefix = "port";
        if (ifId) {
            ifId = ifId.physical_if_id || ifId.breakout_if_id;
        } else {
            portPrefix = "LAG";
            ifId = clusterlinkif.lag_link.lag_if_id;
        }
        record.push(portPrefix + ifId);
        // 4: IGPコスト
        record.push(clusterlinkif.igp_cost);
        // 5: IPv4アドレス
        record.push(clusterlinkif.ipv4_address);
        // 6: トラヒック閾値
        record.push((clusterlinkif.traffic_threshold != null) ? clusterlinkif.traffic_threshold : "-");

        // ソート条件用カラム
        // 7: node_id 数値化して格納
        record.push(isNaN(anyLink.node_id) ? anyLink.node_id : parseInt(anyLink.node_id, 10));
        // 8: if_id 数値化して格納
        record.push(isNaN(linkIfId) ? linkIfId : parseInt(linkIfId, 10));

        return record;
    }
    ;
    //
    // 詳細情報テーブル（Intra-Cluster Info）レコード生成.
    // clusterId: クラスタID
    // intraDataList: レコード生成用データ配列 (createDetailData()で生成)
    // return: レコード1件分のカラム配列
    //
    MSF.MsfDetailLinkInfo.prototype.getRecordIntra = function (clusterId, intraDataList) {

        var record = [];
        var i = 0;
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        var deviceType = intraDataList[i++];
        var nodeId = intraDataList[i++];
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: IF ID (internal link if id)
        var ifId = intraDataList[i++];
        record.push(ifId);
        // 3: 構成IF ID (physical_if_id/breakout_if_id/lag_if_id)
        record.push(intraDataList[i++]);
        // 4: 対向装置ID
        record.push(intraDataList[i++]);
        // 5: 対向装置内部リンクIFID
        record.push(intraDataList[i++]);
        // 6: IPv4アドレス
        record.push(intraDataList[i++]);
        // 7: トラヒック閾値
        var traffic = record.push(intraDataList[i++]);
        record.push(traffic ? traffic : "-");

        // ソート条件用カラム
        // 8: deviceType
        record.push(deviceType);
        // 9: node_id 数値化して格納
        record.push(isNaN(nodeId) ? nodeId : parseInt(nodeId, 10));
        // 10: if_id 数値化して格納
        record.push(isNaN(ifId) ? ifId : parseInt(ifId, 10));

        return record;
    }
    ;
})();
