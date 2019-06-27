//
// MSF詳細エリアクラス(空き物理IF情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    //MSF.MsfDetail
    MSF.MsfDetailEmptyResources = function() {
        // 更新対象ID
        this.TARGET_ID = "#detailTable2";
        // カラム数
        this.COL_NUM = 4;
        // ソート対象レコード
        this.SORT_COLS = [0, 4, 5, 6];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Empty Resource）レコード生成.
    // baseData        : レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId       : 選択中コントローラIDの配列
    // spines          : スライスに属するSpine情報 (スライス選択時)
    // leafs           : スライスに属するLeaf情報 (スライス選択時)
    // deviceType      : 装置種別
    // nodeIds         : nodeIds (配列/undefined)
    // ifId            : ifId (undefined)
    //
    MSF.MsfDetailEmptyResources.prototype.createRecords = function (baseData, clusterId, spines, leafs, deviceType, nodeId, ifId) {

        var records = [];
        var ifInfo = baseData.clusterInfoDic[clusterId].InterfacesInfo || {};

        var targetIfs = {};
        if (spines != null && leafs != null) {
            targetIfs = this.getPhysicalIfsSlice(ifInfo, spines, leafs);
        } else {
            deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : null;
            targetIfs = this.getPhysicalIfs(ifInfo, deviceType, nodeId, ifId);
        }
        
        // 装置指定に従い絞り込み
        for (var type in targetIfs) {
            for (var nId in targetIfs[type]) {
                for (var iId in targetIfs[type][nId].physical_ifs) {
                    var pIf = targetIfs[type][nId].physical_ifs[iId];
                    records.push(this.getRecord(clusterId, type, nId, pIf));
                }
            }
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;

    //
    // 詳細情報テーブル（Empty Resource）レコード生成.
    // clusterId       : クラスタID
    // deviceType      : 装置種別
    // nodeId          : 装置ID
    // pIf             : 物理IF情報
    //
    MSF.MsfDetailEmptyResources.prototype.getRecord = function (clusterId, deviceType, nodeId, pIf) {

        var PORT = "port";
        var BLANK = "Blank";
        
        // 表示用のレコードを生成
        var record = [];
        // 0: クラスタID
        record.push(clusterId);
        // 1 :装置ID
        record.push(getDeviceTypeNumber(deviceType) + nodeId);
        // 2: ID
        var pIfId = pIf.physical_if_id;
        record.push(PORT + pIfId);
        // 3: IF SLOT ("Blank"固定)
        record.push(BLANK);
        
        // ソート条件用カラム
        // 4: 装置種別
        record.push(deviceType);
        // 5: 装置ID 可能ならば数値化
        record.push((!isNaN(nodeId)) ? parseInt(nodeId, 10) : nodeId);
        // 6: IF ID 可能ならば数値化
        record.push((!isNaN(pIfId)) ? parseInt(pIfId, 10) : pIfId);

        return record;
    }
    ;

    //
    // 選択中スライスに所属する装置の空き物理IF情報を返す
    // ifInfo     : InterfacesInfo
    // spines     : Spine装置配列
    // leafs      : Leaf装置配列
    //
    MSF.MsfDetailEmptyResources.prototype.getPhysicalIfsSlice = function (ifInfo, spines, leafs) {
    
        var targetIfs = {};
        // nodeIf, ifID条件でフィルタ
        for (var type in ifInfo) {
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                // 装置ID指定条件の判定
                if (type == MSF.Const.FabricType.Spines) {
                    if (!spines[nId]) {
                        continue;
                    }
                } else {
                    if (!leafs[nId]) {
                        continue;
                    }
                }
                targetIfs[type][nId] = {physical_ifs : []};
                var node = nodes[nId];
                for (var i = 0; i < node.physical_ifs.length; i++) {
                    var pIf = node.physical_ifs[i];
                    if (pIf.breakout) {
                        continue;
                    }
                    if (pIf.speed) {
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
    // 指定された条件に一致する物理IF情報を返す
    // ifInfo      : InterfacesInfo
    // nodeType    : 装置種別
    // nodeId      : 装置ID
    // ifId        : IF ID
    //
    MSF.MsfDetailEmptyResources.prototype.getPhysicalIfs = function (ifInfo, nodeType, nodeId, ifId) {
    
        var targetIfs = {};
        // nodeId, ifId条件でフィルタ
        for (var type in ifInfo) {
            // 装置種別指定条件の判定
            if (nodeType && nodeType != type) {
                continue;
            }
            targetIfs[type] = {};
            var nodes = ifInfo[type];
            for (var nId in nodes) {
                // 装置ID指定条件の判定
                if (nodeId && nodeId != nId) {
                    continue;
                }
                targetIfs[type][nId] = {physical_ifs : []};
                var node = nodes[nId];
                for (var i = 0; i < node.physical_ifs.length; i++) {
                    var pIf = node.physical_ifs[i];
                    // IF ID指定条件の判定
                    if (ifId && ifId != pIf.physical_if_id) {
                        continue;
                    }
                    if (pIf.breakout) {
                        continue;
                    }
                    if (pIf.speed) {
                        continue;
                    }
                    targetIfs[type][nId].physical_ifs.push(pIf);
                }
            }
        }
        return targetIfs;
    }
    ;
})();
