//
// MSF詳細エリアクラス(Platform情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailPlatformInfo = function() {
        // 更新対象ID
        this.TARGET_ID = "#detailTable7";
        // カラム数
        this.COL_NUM = 4;
        // ソート対象レコード
        this.SORT_COLS = [0, 4, 5];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Platform Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailPlatformInfo.prototype.createRecords = function (baseData, clusterId, spines, leafs) {
    
        var spineRecords = [];
        var leafRecords = [];
        var targetSpines = this.getTargetSpines(baseData.clusterInfoDic[clusterId].NodesInfo.spines, spines);
        var targetLeafs = this.getTargetLeafs(baseData.clusterInfoDic[clusterId].NodesInfo.leafs, leafs);

        var i;
        for (i = 0; i < targetSpines.length; i++) {
            spineRecords.push(this.getRecord(clusterId, targetSpines[i], MSF.Const.FabricType.Spine, baseData.equipment_types));
        }
        for (i = 0; i < targetLeafs.length; i++) {
            leafRecords.push(this.getRecord(clusterId, targetLeafs[i], MSF.Const.FabricType.Leaf, baseData.equipment_types));
        }
        var records = spineRecords.concat(leafRecords);
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 対象Spineリスト取得
    //
    MSF.MsfDetailPlatformInfo.prototype.getTargetSpines = function (spinesInfo, spines) {
        
        var target = [];
        for (var spineId in spinesInfo) {
            if (!spines) {
                target.push(spinesInfo[spineId]);
            } else if (spines[spinesInfo[spineId].node_id]){
                target.push(spinesInfo[spineId]);
            }
        }
        return target;
    }
    ;
    //
    // 対象Leafリスト取得
    //
    MSF.MsfDetailPlatformInfo.prototype.getTargetLeafs = function (leafsInfo, leafs) {
        
        var target = [];
        for (var leafId in leafsInfo) {
            if (!leafs) {
                target.push(leafsInfo[leafId]);
            } else if (leafs[leafsInfo[leafId].node_id]) {
                target.push(leafsInfo[leafId]);
            }
        }
        return target;
    }
    ;
    //
    // 詳細情報テーブル（Platform Info）レコード生成.
    //
    MSF.MsfDetailPlatformInfo.prototype.getRecord = function (clusterId, device, type, equipments) {

        var record = [];
        // 0: クラスタID
        record.push(clusterId);
        // 1: 装置ID
        record.push(getDeviceTypeNumber(type) + device.node_id);
        // 2: プラットフォーム
        record.push(this.getPlatform(equipments, device.equipment_type_id));
        // 3: 増設状態
        record.push(device.provisioning_status);

        // ソート条件用カラム
        // 4: 装置種別deviceType
        record.push(type);
        // 5: 装置ID 数値化して格納
        record.push(isNaN(device.node_id) ? device.node_id : parseInt(device.node_id, 10));

        return record;
    }
    ;
    // プラットフォーム取得
    MSF.MsfDetailPlatformInfo.prototype.getPlatform = function (equipments, id) {
        
        var platform = "-";
        for (var i = 0; equipments.length; i++) {
            if (equipments[i].equipment_type_id == id) {
                platform = equipments[i].platform;
                break;
            }
        }
        return platform;
    }
    ;
})();
