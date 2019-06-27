//
// MSF詳細エリアクラス
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetail = function() {
        // フィルターのIDの最大を取得
        this.filterCount = 10;
        // フィルターアクション登録
        for (var i = 0; i < this.filterCount; i++) {
            var id = "#filter" + (i + 1);
            this.registFilterAction(id);
            // 最初は詳細情報は非表示
            $(id + "_detail").css("display", "none");
        }
        // 各詳細テーブルクラス
        this.opCluster = new MSF.MsfDetailClusterInfo();
        this.opEmpty = new MSF.MsfDetailEmptyResources();
        this.opPhysical = new MSF.MsfDetailPhysicalInfo();
        this.opLogical = new MSF.MsfDetailLocigalInfo();
        this.opLink = new MSF.MsfDetailLinkInfo();
        this.opSlice = new MSF.MsfDetailSliceInfo();
        this.opPlatform = new MSF.MsfDetailPlatformInfo();
        this.opTraffic = new MSF.MsfDetailTrafficInfo();
        this.opFailure = new MSF.MsfDetailFailureInfo();
        this.opController = new MSF.MsfDetailControllerInfo();
    }
    ;
    //
    // フィルターアクション登録処理
    // 前提条件
    // ・filterSelectというスタイル定義がある
    // ・フィルターDivにfilter1～のIDが振ってある
    // ・詳細Divにfilter1_detail～のIDが振ってある
    //
    MSF.MsfDetail.prototype.registFilterAction = function(targetId) {
        var filterSelect = "filterSelect";
        //
        // フィルタークリック処理
        //
        $(targetId).click(function() {
            $(this).toggleClass(filterSelect);
            var isSelect = this.className.indexOf(filterSelect) >= 0;
            var id = "#" + this.id + "_detail";
            $(id).css("display", isSelect ? "block" : "none");
        });
    }
    ;
    //
    // テーブル作成処理
    //   targetId    : レコード生成対象のtable要素のid
    //   pairList    : レコード群
    //   columnCount : カラム数(省略時はtableDataの配列の要素数でカラム数を決定)
    //   戻り値 : なし
    //
    MSF.MsfDetail.prototype.makeDetailTable = function(targetId, pairList, columnCount) {
        this.clearAllRow(targetId);
        this.addRow(targetId, pairList, columnCount);
    }
    ;
    //
    // テーブル作成　行追加処理
    //   targetId    : レコード生成対象のtable要素のid
    //   tableData   : レコード群
    //   columnCount : カラム数(省略時はtableDataの配列の要素数でカラム数を決定)
    //   戻り値 : なし
    //
    MSF.MsfDetail.prototype.addRow = function(targetId, tableData, columnCount) {
        var word = "";
        for (var rowIdx in tableData) {
            word += "<tr>";
            var rows = tableData[rowIdx];
            if (!columnCount) {
                columnCount = rows.length;
            }
            for (var col=0; col < columnCount; col++) {
                var cell = rows[col];
                word += "<td class='detail'>" + cell + "</td>";
            }
            word += "</tr>";
        }
        $(targetId).append(word);
    }
    ;
    //
    // テーブル作成　行削除処理
    //
    MSF.MsfDetail.prototype.clearAllRow = function(targetId) {
        // ヘッダ以外の全行を削除
        $(targetId).find("tr:gt(0)").remove();
    }
    ;

    //
    // 表示されている画面が詳細画面の表示対象か判定
    //
    MSF.MsfDetail.prototype.isSelectMode = function(targetId, mode, comparison) {
        var recordList = [];
        if (mode == comparison) {
            // 空のレコードでテーブルを作成
            this.makeDetailTable(targetId, recordList);
            return true;
        }
        return false;
    }
    ;

    //
    // 詳細情報テーブル更新処理（全）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateAllDetailTable = function (baseData) {
        // クラスタ
        this.updateClusterTable(baseData);
        // 空き物理IF
        this.updateEmptyTable(baseData);
        // Physical
        this.updatePhysicalTable(baseData);
        this.updateBreakoutTable(baseData);
        this.updateLagTable(baseData);
        // EdgePoint
        this.updateLogicalTable(baseData);
        // リンク
        this.updateInterLinkTable(baseData);
        this.updateIntraLinkTable(baseData);
        // CP
        this.updateSliceL2Table(baseData);
        this.updateSliceL3Table(baseData);
        // Platform
        this.updatePlatformTable(baseData);
        // 障害
        this.updateFailureNodeTable(baseData);
        this.updateFailureIfTable(baseData);
        this.updateFailureSliceTable(baseData);

        // トラヒック
        this.updateTrafficIfTable(baseData);
        this.updateTrafficCpTableL2(baseData);
        this.updateTrafficCpTableL3(baseData);

        // コントローラ
        this.updateControllerTable(baseData);
        return;
    };
    //
    // 詳細情報テーブル更新処理（1_クラスタ情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateClusterTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ間リンク選択時
        // Map: スライス選択時
        // Controller Mode
        if (!isMapClusterLinkSelected() && !isMapSliceSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            // レコード生成
            records = this.opCluster.createRecords(baseData, clusterId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opCluster.TARGET_ID, records, this.opCluster.COL_NUM);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（2_空き物理IF情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateEmptyTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Controller Mode
        if (!isNetworkModeMap() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var spines = null;
            var leafs = null;
            if (isFabricSliceSelected()) {
                spines = getFabricSelectedSliceSpines();
                leafs = getFabricSelectedSliceLeafs();
            }
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            
            // レコード生成
            records = this.opEmpty.createRecords(baseData, clusterId, spines, leafs, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opEmpty.TARGET_ID, records, this.opEmpty.COL_NUM);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（3-1_Physical情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updatePhysicalTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Controller Mode
        if (!isNetworkModeMap() && !isControllerMode()) {

            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            if (isFabricSliceSelected() || isFabricSliceSwSelected()) {
                var sliceType = getFabricSelectedSliceType();
                var sliceId = getFabricSelectedSlice();
                var spines = getFabricSelectedSliceSpines();
                var leafs = getFabricSelectedSliceLeafs();
                // レコード生成
                records = this.opPhysical.createRecordsPhysicalSlice(baseData, clusterId, sliceType, sliceId, spines, leafs, deviceType, nodeId);
            } else {
                var ifId = getSelectedIfId();
                // レコード生成
                records = this.opPhysical.createRecordsPhysical(baseData, clusterId, deviceType, nodeId, ifId);
            }
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opPhysical.TARGET_ID_PHYSICAL, records, this.opPhysical.COL_NUM_PHYSICAL);
    }
    ;
    //
    // 詳細情報テーブル更新処理（3-2_Breakout情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateBreakoutTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Fabric: スライス選択時
        // Controller Mode
        if (!isNetworkModeMap() && !isFabricSliceSelected() && !isControllerMode()) {

            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            // レコード生成
            records = this.opPhysical.createRecordsBreakout(baseData, clusterId, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opPhysical.TARGET_ID_BREAKOUT, records, this.opPhysical.COL_NUM_BREAKOUT);
    }
    ;
    //
    // 詳細情報テーブル更新処理（3-3_Lag情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateLagTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Fabric: スライス選択時
        // Controller Mode
        if (!isNetworkModeMap() && !isFabricSliceSelected() && !isControllerMode()) {

            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            
            // レコード生成
            records = this.opPhysical.createRecordsLag(baseData, clusterId, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opPhysical.TARGET_ID_LAG, records, this.opPhysical.COL_NUM_LAG);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（4_edgepoint情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateLogicalTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: 未選択
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Controller Mode
        if (!isMapNotSelected() && !isMapClusterSelected() && !isMapClusterLinkSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var sliceType = getSelectedSliceType();
            var sliceId = getSelectedSliceId();
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            if (sliceType && deviceType != MSF.Const.FabricType.Spine) {
                var leafs = getFabricSelectedSliceLeafs();
                // レコード生成
                records = this.opLogical.createRecordsSlice(baseData, sliceType, sliceId, clusterId, leafs, nodeId);
            } else {
                // レコード生成
                records = this.opLogical.createRecords(baseData, clusterId, deviceType, nodeId, ifId);
            }
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opLogical.TARGET_ID, records, this.opLogical.COL_NUM);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（5-1_クラスタ間リンクIF情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateInterLinkTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: スライス選択時
        // Fabric: スライス選択時
        // Controller Mode
        if (!isMapSliceSelected() && !isFabricSliceSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterLinkId = getMapSelectedClusterLink();
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            
            // レコード生成
            records = this.opLink.createRecordsInter(baseData, clusterId, clusterLinkId, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opLink.TARGET_ID_INTER, records, this.opLink.COL_NUM_INTER);
    }
    ;
    //
    // 詳細情報テーブル更新処理（5-2_内部リンクIF情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateIntraLinkTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Fabric: スライス選択時
        // Controller Mode
        if (!isNetworkModeMap() && !isFabricSliceSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            
            // レコード生成
            records = this.opLink.createRecordsIntra(baseData, clusterId, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opLink.TARGET_ID_INTRA, records, this.opLink.COL_NUM_INTRA);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（6-1_L2CP情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateSliceL2Table = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Fabric: トポロジ選択時
        // Fabric: トポロジ状のSW選択時
        // Node: すべて
        // Controller Mode
        if (!isMapClusterSelected() && !isMapClusterLinkSelected() &&
            !isFabricTopologySelected() && !isFabricTopologySwSelected() &&
            !isNetworkModeNode() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var sliceId = getSelectedSliceId();
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var leafs  = getFabricSelectedSliceLeafs();
            
            // レコード生成
            records = this.opSlice.createRecordsL2(baseData, sliceId, clusterId, deviceType, nodeId, leafs);
        }
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opSlice.TARGET_ID_L2, records, this.opSlice.COL_NUM_L2);
    }
    ;
    //
    // 詳細情報テーブル更新処理（6-2_L3CP情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateSliceL3Table = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Fabric: トポロジ選択時
        // Fabric: トポロジ状のSW選択時
        // Node: すべて
        // Controller Mode
        if (!isMapClusterSelected() && !isMapClusterLinkSelected() &&
            !isFabricTopologySelected() && !isFabricTopologySwSelected() &&
            !isNetworkModeNode() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var sliceId = getSelectedSliceId();
            var clusterId = getSelectedClusterId();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var leafs  = getFabricSelectedSliceLeafs();
            
            // レコード生成
            records = this.opSlice.createRecordsL3(baseData, sliceId, clusterId, deviceType, nodeId, leafs);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opSlice.TARGET_ID_L3, records, this.opSlice.COL_NUM_L3);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（7_装置増設状態）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updatePlatformTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: すべて
        // Node: ポート選択時
        // Controller Mode
        if (!isNetworkModeMap() && !isNodePortSelected() && !isControllerMode()) {

            var spines = {};
            var leafs = {};
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            if (isNetworkModeFabric()) {
                var selNodeType = getSelectedNodeType();
                // 装置未選択
                if (!selNodeType) {
                    spines = getFabricSelectedSliceSpines();
                    leafs = getFabricSelectedSliceLeafs();
                }
                // 装置選択
                if (selNodeType == MSF.Const.FabricType.Leaf) {
                    leafs[getSelectedNodeId()] = {};
                } else if (selNodeType == MSF.Const.FabricType.Spine) {
                    spines[getSelectedNodeId()] = {};
                }
            } else {
                if (getNodeViewNodeType() == MSF.Const.FabricType.Leafs) {
                    leafs[getNodeViewNode()] = {};
                } else {
                    spines[getNodeViewNode()] = {};
                }
            }
            
            // レコード生成
            records = this.opPlatform.createRecords(baseData, clusterId, spines, leafs);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opPlatform.TARGET_ID, records, this.opPlatform.COL_NUM);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（8-1_トラヒック情報(IF)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateTrafficIfTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: スライス選択時
        // Fabric: スライス選択時
        // Controller Mode
        if (!isMapSliceSelected() && !isFabricSliceSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var clusterLinkId = getMapSelectedClusterLink();
            var isClusterLink = isNetworkModeMap();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            
            // レコード生成
            records = this.opTraffic.createRecordsIf(baseData, clusterId, clusterLinkId, isClusterLink, deviceType, nodeId, ifId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opTraffic.TARGET_ID_IF, records, this.opTraffic.COL_NUM_IF);
    }
    ;
    //
    // 詳細情報テーブル更新処理（8-2_L2トラヒック情報(CP)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateTrafficCpTableL2 = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Node: すべて
        // Controller Mode
        if (!isMapClusterSelected() && !isMapClusterLinkSelected() && !isNetworkModeNode() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var sliceType = getSelectedSliceType();
            var sliceId = getSelectedSliceId();
            var leafs = getFabricSelectedSliceLeafs();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var isMap = isNetworkModeMap();
            
            // レコード生成
            records = this.opTraffic.createRecordsCpL2(baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId, isMap);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opTraffic.TARGET_ID_CP_L2, records, this.opTraffic.COL_NUM_CP_L2);
    }
    ;
    //
    // 詳細情報テーブル更新処理（8-3_L3トラヒック情報(CP)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateTrafficCpTableL3 = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Node: すべて
        // Controller Mode
        if (!isMapClusterSelected() && !isMapClusterLinkSelected() && !isNetworkModeNode() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var sliceType = getSelectedSliceType();
            var sliceId = getSelectedSliceId();
            var leafs = getFabricSelectedSliceLeafs();
            var deviceType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var isMap = isNetworkModeMap();
            
            // レコード生成
            records = this.opTraffic.createRecordsCpL3(baseData, sliceType, sliceId, clusterId, leafs, deviceType, nodeId, isMap);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opTraffic.TARGET_ID_CP_L3, records, this.opTraffic.COL_NUM_CP_L3);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（9-1_障害情報(物理単位:装置)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateFailureNodeTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ間リンク選択時
        // Map: スライス選択時
        // Fabric: スライス自体選択時
        // Node: ポート選択時
        // Controller Mode
        if (!isMapClusterLinkSelected() && !isMapSliceSelected() && !isFabricSliceSelected() && !isNodePortSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var nodeType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            
            // レコード生成
            records = this.opFailure.createRecordsNode(baseData, clusterId, nodeType, nodeId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opFailure.TARGET_ID_NODE, records, this.opFailure.COL_NUM_NODE);
    }
    ;
    //
    // 詳細情報テーブル更新処理（9-2_障害情報(物理単位:IF)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateFailureIfTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: スライス選択時
        // Fabric: スライス自体選択時
        // Controller Mode
        if (!isMapSliceSelected() && !isFabricSliceSelected() && !isControllerMode()) {
            
            // 選択中/表示中クラスタID
            var clusterId = getSelectedClusterId();
            var nodeType = getSelectedNodeType();
            var nodeId = getSelectedNodeId();
            var ifId = getSelectedIfId();
            var clusterLinkId = getMapSelectedClusterLink();
            // レコード生成
            records = this.opFailure.createRecordsIf(baseData, clusterId, nodeType, nodeId, ifId, clusterLinkId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opFailure.TARGET_ID_IF, records, this.opFailure.COL_NUM_IF);
    }
    ;
    //
    // 詳細情報テーブル更新処理（9-3_障害情報(物理単位:スライス)）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateFailureSliceTable = function (baseData) {
    
        var records = [];
        
        // 以下条件以外の場合は表示対象
        // Map: クラスタ選択時
        // Map: クラスタ間リンク選択時
        // Fabric: 未選択時
        // Fabric: トポロジ選択時
        // Fabric: トポロジ上SW選択時
        // Fabric: スライス上SW選択時
        // Node: すべて
        // Controller Mode
        if (!isMapClusterSelected() &&!isMapClusterLinkSelected() &&
            !isFabricNotSelected() && !isFabricTopologySelected() && !isFabricTopologySwSelected() && !isFabricSliceSwSelected() &&
            !isNetworkModeNode() && !isControllerMode()) {
            
            var sliceType = getSelectedSliceType();
            var sliceId = getSelectedSliceId();
            
            // レコード生成
            records = this.opFailure.createRecordsSlice(baseData, sliceType, sliceId);
        }
        
        // 画面のテーブルへ反映
        this.makeDetailTable(this.opFailure.TARGET_ID_SLICE, records, this.opFailure.COL_NUM_SLICE);
    }
    ;
    
    //
    // 詳細情報テーブル更新処理（10_コントローラ情報）
    //   baseData       : レコード生成に使用する基礎データ(ポーリング取得)
    //   戻り値         : なし
    //
    MSF.MsfDetail.prototype.updateControllerTable = function (baseData) {
    
        var records = [];
        var controllers = [];
        
        // コントローラ選択中(表示中)のコントローラを取得
        if (isControllerMode()) {
            controllers = controllerView.getSelectedControllers();
            if (controllers.length == 0) {
                // 未選択の場合は全コントローラを対象
                controllers = controllerView.getAllControllers();
            }
        }
        
        // レコード生成
        records = this.opController.createRecords(baseData, controllers);

        // 画面のテーブルへ反映
        this.makeDetailTable(this.opController.TARGET_ID, records, this.opController.COL_NUM);
    }
    ;
})();
