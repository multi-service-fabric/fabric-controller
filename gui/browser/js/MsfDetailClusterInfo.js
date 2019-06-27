//
// MSF詳細エリアクラス(クラスタ情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailClusterInfo = function() {
        // 更新対象ID
        this.TARGET_ID = "#detailTable1";
        // カラム数
        this.COL_NUM = 4;
        // ソート対象レコード
        this.SORT_COLS = [4, 5];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Controller Info）レコード生成.
    // baseData :      レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId :     対象クラスタID 未指定:全件クラスタ対象
    // return :        詳細部への表示レコード情報
    //
    MSF.MsfDetailClusterInfo.prototype.createRecords = function (baseData, clusterId) {
        
        var records = [];
        var clusters = baseData.sw_clusters || [];

        for (var i = 0; i < clusters.length; i++) {
            var cluster = clusters[i];
            if (!clusterId) {
                // clusterID未指定の場合は全件表示
                records.push(this.getRecord(cluster));
            } else if (clusterId == cluster.cluster_id) {
                // clusterId指定時は対象のみ表示
                records.push(this.getRecord(cluster));
                break;
            }
        }
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報テーブル（Cluster Info）レコード生成.
    // updateIfTrafficTable()内で使用.
    // cluster: クラスタ情報
    // return:  1レコード分の情報
    //
    MSF.MsfDetailClusterInfo.prototype.getRecord = function (cluster) {

        var record = [];
        // 0: クラスタID
        record.push(cluster.cluster_id);
        // 1: AS番号
        record.push(cluster.as_number);
        // 2: サポートするUNI接続プロトコル情報: L2対応可否
        record.push(cluster.uni_support_protocols.L2);
        // 3: サポートするUNI接続プロトコル情報: L3対応可否
        var l3_protocol = cluster.uni_support_protocols.L3 ? cluster.uni_support_protocols.L3_protocols.join(",") : "-";
        record.push(l3_protocol);

        // ソート条件用カラム
        // 4: ソートキー(1番目:cluster_id) 可能であれば数値化して格納
        record.push(isNaN(cluster.cluster_id) ? cluster.cluster_id : parseInt(cluster.cluster_id, 10));
        // 5: ソートキー(2番目:as_number) 可能であれば数値化して格納
        record.push(isNaN(cluster.as_number) ? cluster.as_number : parseInt(cluster.as_number, 10));
        return record;
    }
    ;
})();

