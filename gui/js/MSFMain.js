//
// MSFMainクラス
//
(function() {
"use strict";

    //
    // コンストラクタ
    //
    MSF.MSFMain = function() {

        // デバッグ用フラグ
        this.execPolling = true;

        // ポーリングステータス
        this.isPolling = false;
        // トラヒックのポーリングステータス
        this.isTrafficPolling = false;
        // コントローラ状態のポーリングステータス
        this.isControllerPolling = false;
        // 初回のポーリング(基礎データ取得)が完了しているか否か
        this.isFirstPollingDone = false;
        // フォーマットメソッドをストリングクラスに登録
        this.addStringFormatMethod();
        // キャンバス
        this.can = new MSF.MsfCanvas(window.mainCanvas,window.animationCanvas,MSF.Conf.MsfCanvas);
        this.can.getSelectedSliceIdList = this.getSelectedSliceIdList;
        // キャンバスフレーム
        this.mf = new MSF.MsfCanvasFrame(window.clipFrame,window.dragFrame,this.can,MSF.Conf);

        // マルチクラスタ表示オブジェクト {CLUSTER_ID->MsfClusterオブジェクト}
        this.mcv = {};
        this.mcvIds = [];
        // クラスタリンク表示オブジェクト {CLUSTER_LINK_ID->MsfClusterLinkオブジェクト}
        this.mclIds = [];
        // スライス表示オブジェクト {SLICE_ID->MsfSliceオブジェクト}
        this.msv = {};
        this.msvIds = [];

        this.spineLeafConnect = {};
        // 前回通知情報
        this.lastFailureStatus = {};
        this.lastFailureSliceStatus = {};
        this.lastSystemStatus = {};
        // メニュー用スライス選択状態管理辞書
        this.menuSliceDic = {};

        // 詳細情報
        this.dt = new MSF.MsfDetail();

        // データ管理オブジェクト　ライフタイム：ポーリングの通信成功後～次のポーリング開始まで
        this.db = this.getDBStruct();

        // Rest通信 (MFC)
        this.rest = new MSF.MsfRest(this.can, MSF.Conf.Rest.MFC, this.db);
        this.rest.reflectServerStatus = this.reflectServerStatus;
        this.rest.showMessage = this.showMessage;

        // データ登録用オブジェクト
        this.jsonform = new MSF.MSFjsonForm(this.rest);

        // ログ出力
        this.log = new MSF.MsfLog($("#logTable"));

        // タブ表示ステータス
        this.mode = MSF.Const.Mode.Network;

        // 階層表示ステータス
        this.networkmode = MSF.Const.NetworkMode.Map;

        // クラスタID
        this.clusters = [];

    }
    ;

    //
    //  データ管理オブジェクト構造体取得
    //
    MSF.MSFMain.prototype.getDBStruct = function() {
        return {
            // ----------------------------
            // RESTで受信したデータそのもの
            // ----------------------------
            sw_clusters: [],        // All
            equipment_types: [],    // All
            l2_slices: [],          // All
            l3_slices: [],          // All
            // cluster_link_ifs: [],   // All
            l2_cps: {},             // All {SliceId->CP情報リスト}
            l3_cps: {},             // All {SliceId->CP情報リスト}

            cp_traffics: {},        // All
            failure_status: {},     // All

            // --------------------
            // 上記を加工したデータ
            // --------------------
            // CPIDで引ける辞書。中身は{sliceType+sliceID->{CPId->CP情報}}
            // ※ CP情報はCP情報取得結果のRest参照
            CPInfo:{},              // All
            // エッジポイントIDで引ける辞書。中身はedge_point Rest結果の参照
            // EdgepointDic: {},       // Cluster
            // 機種IDで引ける辞書。中身はRest結果の参照
            EquipmentTypeDic: {},   // All
            // 画面表示用の障害情報辞書。// All
            FailureInfoDic: {},
            FailureInfoSliceDic: {},
            // --------------------
            // ステータス情報
            // --------------------
            // 機種情報の有効期限
            EquipmentTypeExpireDate: null,
            SpineLeafTraffic: {},

            // 2017年度版追加
            // クラスタIDからクラスタごとの情報を参照する辞書。
            clusterInfoDic: {},      // Cluster
            controller_informations: []
        };
    }
    ;
    // クラスタごと情報管理オブジェクト構造体取得
    MSF.MSFMain.prototype.getClusterDbStruct = function() {
        return {
                    NodesInfo: {},          // Cluster
                    l2_edge_points: [],     // Cluster
                    l3_edge_points: [],     // Cluster
                    cluster_link_ifs: [],   // Cluster
                    EdgepointDic: {},       // Cluster
                    if_traffics: {
                        leafs: {},
                        spines: {}
                    },                      // Cluster(fc):Node
                    InterfacesInfo: {
                        leafs: {},
                        spines: {}
                    },                      // Cluster:Node
                    failure_status: {}
                };
    }

    //
    // 初期化処理
    //
    MSF.MSFMain.prototype.init = function() {

        // ログインアカウント情報初期化
        this.initAccount();

        // キャンバスフレーム初期化
        this.mf.init();

        // コントローラViewキャンバス初期化
        controllerView = new MSF.ControllerView(150*12,480);
        controllerView.init([]);

        // メニュー用スライス選択状態管理辞書
        this.menuSliceDic[MSF.Const.SliceType.L2] = [];
        this.menuSliceDic[MSF.Const.SliceType.L3] = [];
        // 詳細部スライス単位設定
        if (MSF.Conf.Detail.Traffic.UNIT_MBPS) {
            $(".traffic_bps").text("[Mbps]");
        }

        // 初回表示データ取得
        this.Polling();

        // ポーリングインターバルイベント(REST＆Figure部更新処理)
        setInterval(function() {
            this.Polling();
        }
        .bind(this), MSF.Conf.MSFMain.Polling.MainPolling.INTERVAL);

        // ポーリングインターバルイベント(詳細部の更新チェック)
        setInterval(function() {
            this.detailUpdateCheck();
        }.bind(this), 200);

    }
    ;
    //
    // CPU使用率超過通知ダイアログ表示
    //
    MSF.MSFMain.prototype.noticeSystemStatus = function() {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_CPU_THRESHOLD_DIALOG) {
            // CPU使用率超過通知OFFなら処理SKIP
            return null;
        }
        if (!this.isControllerPolling) {
            // 「Controller Info」未選択の場合、処理SKIP
            return null;
        }

        var statusList= {};
        var pMessageList = [];
        // 前回の超過情報を取得
        var lastStatusList = this.lastSystemStatus;
        this.db.controller_informations.forEach(function(information) {
            if (!information.os || !information.os.cpu) {
                return;
            }

            // 「controller_type + "#" + cluster_id」となる辞書型のキーを作成(MFCはcontroller_typeのみ)
            var type = information.controller_type.toUpperCase();
            var id = (information.cluster_id) ? "#" + information.cluster_id : "";
            var key = type + id;

            if (MSF.Conf.ControllerInfo.CPU_USAGE >= information.os.cpu.use_rate) {
                return;
            }

            // 今回CPU使用率を超過したコントローラの通知フラグをtrueにする
            statusList[key] = true;

            // 前回通知済みの超過情報は通知しない
            if (lastStatusList[key]) {
                return;
            }

            // CPU使用率が閾値を超過した場合、コントローラの種別とクラスタIDを取り出し
            pMessageList.push(key);
        });
        // 今回の超過情報を保存
        this.lastSystemStatus = statusList;

        if (pMessageList.length === 0) {
            return null;
        }

        // CPU使用率超過の通知
        var outMessage = "Usage rate exceeded!!<br>&nbsp;&nbsp;";
        var message = "The CPU usage rate of the Controller \"" + pMessageList.join() + "\" exceeded " + MSF.Conf.ControllerInfo.CPU_USAGE + "%.<br>";
        var msg1 = i18nx("Main.errMessage.UsageRate", outMessage);
        var msg2 = i18nx("Main.errMessage.UsageDetail", message, pMessageList.join(), MSF.Conf.ControllerInfo.CPU_USAGE);
        outMessage = msg1 + msg2;

        return outMessage;
    }
    ;

    //
    // アカウント情報表示
    //
    MSF.MSFMain.prototype.initAccount = function() {

        // ログインユーザ設定
        var loginTmp = location.search.match(/user=(.*?)(&|$)/);
        var loginUser = "";
        if (loginTmp !== null && loginTmp.length >= 2)
            loginUser = loginTmp[1];
        if(!loginUser) loginUser = "";

        // ログイン権限取得
        loginTmp = location.search.match(/authority=(.*?)(&|$)/);
        var loginType = "0";
        if (loginTmp !== null && loginTmp.length >= 2)
            loginType = loginTmp[1];
        if(!loginType) loginType = "0";
        loginType = Number(loginType);

        // 画面上に設定
       $("#loginUser").text(loginUser + "  <" + MSF.AccountTypeList[loginType].name + ">");

        // ログイン権限に応じたGUIの非表示化
        this.initAuthority(loginType);

    }
    ;

    //
    // ログイン権限に応じたGUIの非表示化
    //   loginTypeID : ログインしたアカウント種別のID
    //
    MSF.MSFMain.prototype.initAuthority = function(loginType) {

        // 権限に応じたGUIの非表示化(削除)
        // ※ data-function-groupが設定されているタグは、
        //    該当機能グループが使用可能なユーザの場合だけ表示
        var availableFuncList = MSF.AccountTypeList[loginType].availableFunctions;

        $("*[data-function-group]").each(function() {

            // 属性で指定された機能グループを自分のアカウントが利用可能として持っているかをチェック
            var attrVal = $(this).attr("data-function-group");
            var fGrpList = attrVal.split(" ");
            // タグに対応する機能グループでループ
            for (var i = 0; i < fGrpList.length; i++) {
                var funcName = fGrpList[i].trim().toLowerCase();
                // 自分の権限にあるか？
                for (var j = 0; j< availableFuncList.length; j++){
                    // 自分の権限にあれば、非表示化しなくてＯＫ
                    if(availableFuncList[j].name == funcName) return;
                }
            }

            // アカウントの利用可能機能一覧と、タグの機能グループに合致が無ければ非表示
            $(this).remove();
        });

        // 障害通知、CPU使用率超過通知の権限有効化
        var disableFailureDialog = true;
        var disableCpuDialog = true;
        
        // 権限に応じたメニューバーの有効化
        for (var j = 0; j< availableFuncList.length; j++){
            // 装置メニュー
            if (availableFuncList[j].name == "display_physical_device") {
                $("#menuBlank1").remove();
                $("#menuEquipmentRoot").css("visibility", "visible");
                $("#menuNode").css("visibility", "visible");
            }
            // スライスメニュー
            if (availableFuncList[j].name == "display_slice") {
                $("#menuBlank3").remove();
                $("#menuBlank4").remove();
                $("#menuL2SliceRoot").css("visibility", "visible");
                $("#menuL2Slice").css("visibility", "visible");
                $("#menuL3SliceRoot").css("visibility", "visible");
                $("#menuL3Slice").css("visibility", "visible");
            }
            // 障害通知有効化
            if (availableFuncList[j].name == "display_failure") {
                disableFailureDialog = false;
            }
            // CPU使用率超過通知有効化
            if (availableFuncList[j].name == "display_controller_info") {
                disableCpuDialog = false;
            }
        }
        if (!MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG = disableFailureDialog;
        }
        if (!MSF.Conf.System.Debug.NOT_NOTIFY_CPU_THRESHOLD_DIALOG) {
            MSF.Conf.System.Debug.NOT_NOTIFY_CPU_THRESHOLD_DIALOG = disableCpuDialog;
        }

        // デバッグモードON
        if(loginType == MSF.AccountType.Developer.id) {
            this.debug_ = true;
            this.rest.debug_ = true;
        }
    }
    ;

    //
    // スライス/CP情報取得処理
    //
    MSF.MSFMain.prototype.slicePromise = function() {

        return this.rest.getCPListAll(MSF.MessageInfo.Polling.getCPList)
        .then(function(response) {
            // メニューバー用スライス管理辞書を更新
            this.updateMenuSliceDic();
        }.bind(this));
    }
    ;
    // スライス図形、CP数の描画処理
    // メニュー連携用辞書の更新
    MSF.MSFMain.prototype.updateMenuSliceDic = function() {

        var sliceParam = {};
        sliceParam[MSF.Const.SliceType.L3] = {
            "sliceDicList" : this.menuSliceDic[MSF.Const.SliceType.L3],
            "svgSliceType" : "L3"
        };
        sliceParam[MSF.Const.SliceType.L2] = {
            "sliceDicList" : this.menuSliceDic[MSF.Const.SliceType.L2],
            "svgSliceType" : "L2"
        };

        // メニュー管理辞書のスライスのメニューを無効化し、後処理で有効化し直す
        var i = 0;
        for (var sliceType in sliceParam) {
            var dicList = sliceParam[sliceType].sliceDicList;
            for (i = 0; i < dicList.length; i++) {
                dicList[i].isActive = false;
            }
        }
        
        var cpKeys = [];
        var cpCounterL2 = {};
        var cpCounterL3 = {};
        for(var cpKey in this.db.CPInfo){

            var sliceTypeKey = (cpKey.indexOf(MSF.Const.SliceType.L3) === 0) ? MSF.Const.SliceType.L3 : MSF.Const.SliceType.L2;

            var clusterIds = [];
            var sliceInfo = this.db.CPInfo[cpKey];
            for (var cpId in sliceInfo) {
                var cp = sliceInfo[cpId];
                // スライスに所属するクラスタをグループ化
                if (clusterIds.indexOf(cp.cluster_id) == -1) {
                    clusterIds.push(cp.cluster_id);
                }
                // CP数をカウント
                var clusterId = cp.cluster_id;
                var cpCounter = (sliceTypeKey == MSF.Const.SliceType.L3) ? cpCounterL3 : cpCounterL2;
                if (!cpCounter[clusterId]) {
                    cpCounter[clusterId] = {};
                }
                var sId = (cp.slice_id);
                if (!cpCounter[clusterId][sId]) {
                    cpCounter[clusterId][sId] = 1;
                } else {
                    cpCounter[clusterId][sId] = cpCounter[clusterId][sId] + 1;
                }
            }

            // メニューバー スライス更新
            var sliceId = cpKey.slice(MSF.Const.SliceType.L2.length);
            var targetSliceDic = getTargetSliceInfo(sliceParam[sliceTypeKey].sliceDicList, sliceId);
            if (targetSliceDic == null) {
                targetSliceDic = {};
                targetSliceDic.id = sliceId;
                targetSliceDic.cpKey = cpKey;
                targetSliceDic.isChecked = false;
                targetSliceDic.color = getSliceViewColor();
                targetSliceDic.cpCountCluster = { L2: {}, L3: {} };
                sliceParam[sliceTypeKey].sliceDicList.push(targetSliceDic);
            }
            // 所属クラスタID, CP数を更新
            targetSliceDic.clusterIds = clusterIds;
            targetSliceDic.cpCount = Object.keys(sliceInfo).length;
            targetSliceDic.cpCountCluster.L2 = cpCounterL2;
            targetSliceDic.cpCountCluster.L3 = cpCounterL3;
            // コントローラから取得したスライスをメニュー有効化する
            targetSliceDic.isActive = true;

            // スライスSVG Canvas更新
            // スライス未作成であればスライス作成
            var sli;
            if (!this.msv[cpKey]) {
                targetSliceDic = getTargetSliceInfo(this.menuSliceDic[sliceTypeKey], sliceId);
                var color = targetSliceDic.color;
                //slice.id = cp.slice_id;
                sli = new MSF.MsfSlice(targetSliceDic.id, targetSliceDic.id, sliceParam[sliceTypeKey].svgSliceType, clusterIds, color);
                sli.hide();
                this.msv[cpKey] = sli;
            } else if (!isArrayEquals(clusterIds, this.msv[cpKey].clusterIds)) {
                // スライスに所属するクラスタが変更されていればスライス描画を更新
                sli = this.msv[cpKey];
                sli.updateCluster(clusterIds);
            }
            cpKeys.push(cpKey);
        }

        // スライスIDでソート
        var comparer = new MSF.ArrayComparer();
        comparer.ObjArraySort(this.menuSliceDic[MSF.Const.SliceType.L2], "id");
        comparer.ObjArraySort(this.menuSliceDic[MSF.Const.SliceType.L3], "id");

        // メニューバー：スライス更新
        updateSliceMenu(this.can.viewClusterId);

        // メニュー無効なスライスのチェック状態を初期化する
        for (var sliceType in sliceParam) {
            var dicList = sliceParam[sliceType].sliceDicList;
            for (i = 0; i < dicList.length; i++) {
                if (!dicList[i].isActive) dicList[i].isChecked = false;
            }
        }

        // スライス削除
        for (var targetId in this.msv) {
            var target = this.msv[targetId];
            if (cpKeys.indexOf(targetId) == -1) {
                if (target.type == "L3") {
                    svg.removeL3Slice(target.id);
                } else {
                    svg.removeL2Slice(target.id);
                }
                delete this.msv[targetId];
            }
        }

        // SVGキャンバスにSVG図形(CP)を表示
        for (i = 0; i < this.db.sw_clusters.length; i++) {
            var clusterId = this.db.sw_clusters[i].cluster_id;
            var n = 0;
            for (var id in cpCounterL2[clusterId]) {
                n = n + cpCounterL2[clusterId][id];
            }
            for (var id in cpCounterL3[clusterId]) {
                n = n + cpCounterL3[clusterId][id];
            }
            this.mcv[clusterId].hideCP();
            this.mcv[clusterId].showCP(n);
        }
    }
    ;

    //
    // クラスタ図形の描画処理
    // SVGキャンバスにSVG図形(クラスタ)を表示
    //
    MSF.MSFMain.prototype.clusterDraw = function(clusters) {
        var i;

        // 初回のみ円周形に並べて表示
        if (!this.isFirstPollingDone) {
            // 初回取得のリトライ時はクリアして再描画
            for (i = 0; i < this.mcvIds.length; i++) {
                svg.removeCluster(this.mcvIds[i]);
            }
            this.mcvIds = [];
            this.mcv = {};
            this.circleDraw(this.clusters);
            return;
        }

        // 2回目以降に追加されたクラスタは右上に表示
        var addIds = diffArray(this.clusters, this.mcvIds);
        for (i = 0; i < addIds.length; i++) {
            var clusterId = addIds[i];
            var x = 550;
            var y = 50 + (i * 75);

            var cluster = new MSF.MsfCluster(clusterId, x, y);
            this.mcvIds.push(clusterId);
            this.mcv[clusterId] = cluster;
        }

        // 削除されたクラスタのSVG図形を削除
        var delIds = diffArray(this.mcvIds, this.clusters);
        for (i = 0; i < delIds.length; i++) {
            var delId = delIds[i];
            svg.removeCluster(delId);
            this.mcvIds.splice( i, 1 ) ;
            delete this.mcv[delId];
        }
    };

    //
    // クラスタ図形の円形描画処理
    //
    MSF.MSFMain.prototype.circleDraw = function(clusters) {
        var item_num = clusters.length;
        var deg = 360.0/item_num;
        var red = (deg*Math.PI/180.0);
        //var circle_r = $("div.item").width() * 2.5;
        var circle_r = 70 * 2.5;
        for (var i = 0; i < clusters.length; i++) {
            var id = clusters[i];
            var x = Math.cos(red * i) * circle_r + circle_r + 50;
            var y = Math.sin(red * i) * circle_r + circle_r + 50;
            var cluster = new MSF.MsfCluster(id, x, y);
            this.mcvIds.push(id);
            this.mcv[id] = cluster;
        }
    }
    ;

    //
    // クラスタ間リンク図形の描画処理
    // SVGキャンバスにSVG図形(クラスタ)を表示
    //
    MSF.MSFMain.prototype.clusterLinkDraw = function(clusters) {

        var latestLinkLines = [];
        for (var cindex in this.db.sw_clusters) {
            var clusterId = this.db.sw_clusters[cindex].cluster_id;
            var clusterLinkIfIfs = this.db.clusterInfoDic[clusterId].cluster_link_ifs;

            for (var index in clusterLinkIfIfs) {
                var clusterLinkIf = clusterLinkIfIfs[index];
                if (!clusterLinkIf.port_status) {
                    // ポート状態「閉塞」は処理SKIP
                    MSF.console.info("clusterLinkIf svg draw skip. info: " + JSON.stringify(clusterLinkIf));
                    continue;
                }
                var opClusterId = clusterLinkIf.opposite_cluster_id;
                //opClusterMap[clusterId].push(opClusterId);
                var id = clusterId+"-"+opClusterId;

                if (this.mclIds.indexOf(id) == -1) {
                    var link = svg.getClusterLinkLineById(clusterId, opClusterId);
                    if (link == null) {
                        new MSF.MsfClusterLink(id, clusterId, opClusterId);
                        this.mclIds[id] = {local:clusterId, opposite:opClusterId};
                    }
                }
                latestLinkLines.push(id);
            }
        }

        // 削除されたクラスタ間リンクのSVG図形を削除
        var delIdsDic = diffDicArray(this.mclIds, latestLinkLines);
        for (var key in delIdsDic) {
            var delId = delIdsDic[key];
            svg.removeClusterLinkLine(delId.local, delId.opposite);
            delete this.mclIds[key];
        }
    }
    ;

    // SVG障害情報更新
    MSF.MSFMain.prototype.updateFailureStatus = function() {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            // 障害通知OFFなら処理SKIP
            return;
        }

        var failureInfo = this.db.FailureInfoDic;
        setClusterFailure(failureInfo);
        
        var failureSliceInfo = this.db.FailureInfoSliceDic;
        setL2SliceFailure(failureSliceInfo[MSF.Const.SliceType.L2] || {});
        setL3SliceFailure(failureSliceInfo[MSF.Const.SliceType.L3] || {});
    }
    ;

    //
    // ポーリング処理
    //
    MSF.MSFMain.prototype.Polling = function() {

        // ポーリングが終わってない場合は次回に持越し
        if (this.isPolling) return;
        // デバッグ用にポーリングの停止が指定されていた場合は実行しない(未定義の場合は実行する)
        if (this.execPolling===false) return;

        this.isPolling = true;

        // ログリフレッシュ(古いログの削除)
        this.log.reflectLogToScreen();

        // 2017年度版のPolling処理
        var mainPromise = Promise.resolve(this.rest.getClusterList(MSF.MessageInfo.Polling.getSwClusters))
        .catch(function(reason) {
            if (MSF.Conf.System.Debug.NOT_CONNECT_TO_SERVER) {
                // デバックで通信しない設定の場合はRejectしないでthenルートに流す
                return reason;
            }
            // Promise.allの例外処理
            //   例外処理は各処理で実装済みの前提なので、ここではなにもしない
            //   rejectされたPromiseを返すことで、次のcatchへ
            this.isPolling = false;

            return Promise.reject(reason);

        }.bind(this))
        .then(function(response) {
            // スライス、クラスタ情報を取得
            this.slicePromise();
            // クラスタ図形描画
            this.clusterMapDraw();

            // クラスタ情報初期化
            this.clusters.forEach(function(cluster) {
                if (!this.db.clusterInfoDic[cluster]) {
                    this.db.clusterInfoDic[cluster] = MSF.main.getClusterDbStruct();
                }
            }, this);

            // クラスタ一覧取得完了 -> クラスタ共通情報＋クラスタごとの情報を取得
            var pList = [];

            // クラスタIDをパラメータに使用するデータの取得
            for (var index in this.clusters) {
                var clusterId = this.clusters[index];
                MSF.console.info("clusterId: " + clusterId);

                // 装置情報一覧取得
                pList.push(this.rest.getNodesInfo(MSF.MessageInfo.Polling.getNodes, clusterId));
                // クラスタ間リンクIF情報一覧取得
                if (!MSF.Conf.Rest.MFC.FC_SINGLE) {
                    pList.push(this.rest.getClusterLinkIfDetailList(MSF.MessageInfo.Polling.getClusterLinkIfs, clusterId));
                }
                // EdgePoint情報一覧取得
                pList.push(this.rest.getEdgepointList(MSF.MessageInfo.Polling.getEdgePoints, clusterId));
            }
            // 障害情報一覧取得
            pList.push(this.rest.getFailureStatusList(MSF.MessageInfo.Polling.getFailureStatus));
            return Promise.all(pList);

        }.bind(this))
        .then(function(response) {

            // Spine-Leafの対向関係を保持
            this.updateSpineLeafDic();
            // 障害情報の辞書を更新
            this.updateFailureInfoDic();
            // 装置メニュー更新
            var clusterId = this.can.viewClusterId;
            if (clusterId) {
                updateNodeMenu(clusterId);
            }

            // 障害情報描画
            this.updateFailureStatus();

            // 画面表示用データが揃った時点でClusterメニューを更新
            updateClusterMenu(this.clusters);

            // SVGキャンバスにSVG図形(クラスタ間リンク)を表示
            this.clusterLinkDraw();

            // 全IF情報一覧取得
            this.rest.getInterfaceListAll(MSF.MessageInfo.Polling.getInterfaceList);

            // 選択スライス更新処理
            this.mf.updateSelectedSlice();
            // キャンバスにデータセット
            this.can.setInfo(this.db);

            // レイアウト計算
            this.mf.calcLayout();

            // 描画処理
            this.can.Draw();

            // 各種初期化処理(基礎データが初めて取得できた際に実行する)
            if (this.isFirstPollingDone == false ) {
                this.isFirstPollingDone = true;
            }
        }.bind(this))
        .then(function(response) {
            // 表示中情報の更新
            this.updateViewModeInfo();
            this.dt.updateAllDetailTable(this.db);
        }.bind(this))
        ;

        // 詳細情報テーブル更新処理
        // ※ REST呼び出しを行うので、メイン処理のthenと並行実行
        var promiseDetail = mainPromise.then(function(response) {
            return this.UpdateDetailTable();
        }.bind(this))
        ;

        // Traffic表示更新処理
        // ※ REST呼び出しを行うので、メイン処理のthenと並行実行
        var promiseTraffic = mainPromise.then(function(response) {
            var promise = response;
            var promiseList = [];
            if (this.isTrafficPolling){
                // トラヒック情報取得
                promiseList.push(this.rest.getTraffic(MSF.MessageInfo.Traffic.getPresent));
                Promise.all(promiseList)
                .then(function(response) {
                    // SpineLeaf間のトラヒック情報更新
                    this.updateSpineLeafTraffic();
                    // トラヒック情報のMapView, FabricNWへの反映
                    this.updateTrafficMapView();
                    // 描画処理
                    this.can.Draw();
                    return response;
                }.bind(this))
                .catch(function(response) {
                    // SpineLeaf間のトラヒック情報更新
                    this.updateSpineLeafTraffic();
                    // トラヒック情報のMapView, FabricNWへの反映
                    this.updateTrafficMapView();
                    // 描画処理
                    this.can.Draw();
                    return response;
                }.bind(this))
                ;
            }
            return promise;

        }.bind(this))
        ;
        // コントローラ状態更新処理
        // ※ REST呼び出しを行うので、メイン処理のthenと並行実行
        var promiseController = mainPromise.then(function(response) {
            var promise = response;
            if (this.isControllerPolling){
                promise = Promise.resolve(this.rest.getStatusList(MSF.MessageInfo.Polling.getSystemStatus))
                .then(function() {
                    this.updateDetailControllerTable();
                }.bind(this))
                ;
            }
            return promise;
        }.bind(this))
        ;
        var lstPromise = [mainPromise, promiseTraffic, promiseController];

        // すべての並行処理の終了を待って、ポーリングのフラグを落とす
        Promise.all(lstPromise)
        .then(function(response) {
            // ダイアログ描画
            this.noticeStatus(this.noticeSystemStatus(), this.noticeFailueStatus())

            MSF.console.info("Polling done.");
            this.isPolling = false;
        }.bind(this))
        .catch(function(reason) {
            MSF.console.trace("Polling done.");
            if (reason) {
                MSF.console.warn(JSON.stringify(reason, null, 4));
            }
            this.isPolling = false;
        }.bind(this))
        ;
    }
    ;

    // Spine-Leaf接続情報トラヒック更新
    MSF.MSFMain.prototype.updateSpineLeafTraffic = function() {
        var makeKey = MSF.Const.makeKey;
        var spineAllDic = this.db.spineLeafConnect;

        var trafficAllDic = {};
        for (var clusterId in spineAllDic) {
            var clusterSpines = spineAllDic[clusterId];
            trafficAllDic[clusterId] = {};

            for (var spineId in clusterSpines) {
                var ifInfoList = clusterSpines[spineId];

                ifInfoList.forEach(function(ifInfo) {
                    var leafId = ifInfo.leafNodeId;
                    var key = makeKey(spineId, leafId);
                    trafficAllDic[clusterId][key] = {};
                    var spineSendRate = this.getTrafficSendRate(clusterId, "spines", spineId, ifInfo.spineIfType, ifInfo.spineIfId);
                    var leafSendRate = this.getTrafficSendRate(clusterId, "leafs", leafId, ifInfo.leafIfType, ifInfo.leafIfId);
                    trafficAllDic[clusterId][key].send_rate = spineSendRate;
                    trafficAllDic[clusterId][key].receive_rate = leafSendRate;
                }.bind(this));
            }
        }
        this.db.SpineLeafTraffic = trafficAllDic;
    }
    ;
    MSF.MSFMain.prototype.getTrafficSendRate = function(clusterId, fabricType, nodeId, ifType, ifId) {
        var trafficInfo = this.db.clusterInfoDic[clusterId].if_traffics[fabricType][nodeId];
        if (!trafficInfo) {
            return;
        }
        for (var i = 0; i < trafficInfo.if_traffics.length; i++) {
            var trafficValue = trafficInfo.if_traffics[i].traffic_value;
            if (trafficValue.if_type != ifType) {
                continue;
            }
            if (trafficValue.if_id != ifId) {
                continue;
            }
            return trafficValue.send_rate;
        }
    }
    ;
    // Spine-Leaf接続情報更新
    MSF.MSFMain.prototype.updateSpineLeafDic = function() {
        var clusters = this.db.clusterInfoDic;

        var spineAllDic = {};
        for (var clusterId in clusters) {
            spineAllDic[clusterId] = {};
            var spineClusterDic = spineAllDic[clusterId];
            var clusterInfo = clusters[clusterId].NodesInfo;
            var internalLinkIfs = clusterInfo.internal_link_ifs;

            if (!clusterInfo.spines) {
                continue;
            }

            clusterInfo.spines.forEach(function(spine) {
                spineClusterDic[spine.node_id] = [];
                var spineNodeDicList = spineClusterDic[spine.node_id];
                // IF数でループ
                spine.physical_ifs.forEach(function(physicalIf) {
                    if (!physicalIf.internal_options) {
                        return;
                    }
                    // 対向LeafID
                    var leafNodeId = physicalIf.internal_options.opposite_if.node_id;
                    var leafIfType = physicalIf.internal_options.opposite_if.if_type;
                    var leafIfId = physicalIf.internal_options.opposite_if.if_id;
                    // Spine IF情報
                    var ifType = "physical-if";
                    var ifId = physicalIf.physical_if_id;

                    spineNodeDicList.push(this.getConnectLineInfoStruct(ifType, ifId, leafNodeId, leafIfType, leafIfId));
                }.bind(this));
                spine.breakout_ifs.forEach(function(breakoutIf) {
                    if (!breakoutIf.internal_options) {
                        return;
                    }
                    // 対向LeafID
                    var leafNodeId = breakoutIf.internal_options.opposite_if.node_id;
                    var leafIfType = breakoutIf.internal_options.opposite_if.if_type;
                    var leafIfId = breakoutIf.internal_options.opposite_if.if_id;
                    // Spine IF情報
                    var ifType = "breakout-if";
                    var ifId = breakoutIf.breakout_if_id;

                    spineNodeDicList.push(this.getConnectLineInfoStruct(ifType, ifId, leafNodeId, leafIfType, leafIfId));
                }.bind(this));
                spine.lag_ifs.forEach(function(lagIf) {
                    if (!lagIf.internal_options) {
                        return;
                    }
                    // 対向LeafID
                    var leafNodeId = lagIf.internal_options.opposite_if.node_id;
                    var leafIfType = "lag-if";
                    var leafIfId = lagIf.internal_options.opposite_if.lag_if_id;
                    // Spine IF情報
                    var ifType = "lag-if";
                    var ifId = lagIf.lag_if_id;

                    spineNodeDicList.push(this.getConnectLineInfoStruct(ifType, ifId, leafNodeId, leafIfType, leafIfId));
                }.bind(this));
            }.bind(this));
        }
        this.db.spineLeafConnect = spineAllDic;
    }
    ;
    //
    // 障害情報辞書の生成
    //
    MSF.MSFMain.prototype.updateFailureInfoDic = function () {
        
        var physicalUnit = this.db.failure_status.physical_unit;

        var failureInfoDic = {};
        var downNodes = {};
        var downIfs = {};
        
        // 辞書用データ初期化
        var clusterInfos = this.db.clusterInfoDic;
        for (var clusterId in clusterInfos) {
            failureInfoDic[clusterId] = {};
            downNodes[clusterId] = { spines: [], leafs: [] };
            downIfs[clusterId] = { spines: {}, leafs: {} };
            var clusterInfo = clusterInfos[clusterId];
            if (clusterInfo.NodesInfo.spines) {
                clusterInfo.NodesInfo.spines.forEach(function(node) {
                    downIfs[clusterId].spines[node.node_id] = { physical: [], breakout: [], lag: [] };
                });
            }
            if (clusterInfo.NodesInfo.leafs) {
                clusterInfo.NodesInfo.leafs.forEach(function(node) {
                    downIfs[clusterId].leafs[node.node_id] = { physical: [], breakout: [], lag: [] };
                });
            }
        }

        // 物理単位障害情報のうちdownのものを装置毎にまとめる
        if (physicalUnit) {
            var nodeList = physicalUnit.nodes || [];
            nodeList.forEach(function(node) {
                if (node.failure_status == "down") {
                    var fabricType = (MSF.Const.FabricType.Leaf == node.fabric_type) ? MSF.Const.FabricType.Leafs : MSF.Const.FabricType.Spines;
                    downNodes[node.cluster_id][fabricType].push(node.node_id);
                }
            });
            
            var ifList = physicalUnit.ifs || [];
            ifList.forEach(function(i) {
                if (i.failure_status == "down") {
                    var fabricType = (MSF.Const.FabricType.Leaf == i.fabric_type) ? MSF.Const.FabricType.Leafs : MSF.Const.FabricType.Spines;
                    var keyIfType;
                    if (i.if_type == MSF.Const.RestIfType.PhysicalIf) {
                        keyIfType = "physical";
                    } else if (i.if_type == MSF.Const.RestIfType.BreakoutIf) {
                        keyIfType = "breakout";
                    } else if (i.if_type == MSF.Const.RestIfType.LagIf) {
                        keyIfType = "lag";
                    }
                    downIfs[i.cluster_id][fabricType][i.node_id] = downIfs[i.cluster_id][fabricType][i.node_id] || {};
                    downIfs[i.cluster_id][fabricType][i.node_id][keyIfType] = downIfs[i.cluster_id][fabricType][i.node_id][keyIfType] || [];
                    downIfs[i.cluster_id][fabricType][i.node_id][keyIfType].push(i.if_id);
                }
            });
        }

        // 障害アイコン判定に使用する障害発生フラグ
        for (var clusterId in downIfs) {
            var clusterDownIfs = downIfs[clusterId];
            var isClusterStatus = false;
            // IF障害またはNode障害が1件でもあればCluster障害アイコン付与対象
            for (var type in clusterDownIfs) {
                var nodes = downNodes[clusterId][type];
                if (nodes.length > 0) {
                    isClusterStatus = true;
                    break;
                }
                
                var allIfs = clusterDownIfs[type];
                for (var nodeId in allIfs) {
                    var nodeIfs = allIfs[nodeId];
                    for (var type in nodeIfs) {
                        var ifs = nodeIfs[type];
                        if (ifs.length > 0) {
                            isClusterStatus = true;
                            break;
                        }
                    }
                }
            }
    
            failureInfoDic[clusterId].isFailure = isClusterStatus;
            failureInfoDic[clusterId].nodes = downNodes[clusterId];
            failureInfoDic[clusterId].ifs = downIfs[clusterId];
        }
        this.db.FailureInfoDic = failureInfoDic;

        var sliceUnit = this.db.failure_status.slice_unit;
        var failureInfoSliceDic = {};
        if (sliceUnit) {
            for (var i = 0; i < sliceUnit.slices.length; i++) {
                var slice = sliceUnit.slices[i];
                var sliceType = slice.slice_type;
                var sliceId = slice.slice_id;
                if (!failureInfoSliceDic[sliceType]) {
                    failureInfoSliceDic[sliceType] = {};
                }
                failureInfoSliceDic[sliceType][sliceId] = slice;
                failureInfoSliceDic[sliceType][sliceId].isFailure = (slice.failure_status == "down") ? true : false;
            }
        }
        this.db.FailureInfoSliceDic = failureInfoSliceDic;
    }
    ;
    MSF.MSFMain.prototype.getConnectLineInfoStruct = function(spineIfType, spineIfId, leafNodeId, leafIfType, leafIfId) {
        return {
            spineIfType: spineIfType,
            spineIfId: spineIfId,
            leafNodeId: leafNodeId,
            leafIfType: leafIfType,
            leafIfId: leafIfId
        };
    }
    ;
    MSF.MSFMain.prototype.clusterMapDraw = function() {

        var clusterIdList = [];
        for(var index in this.db.sw_clusters){
            clusterIdList.push(this.db.sw_clusters[index].cluster_id);
        }
        this.clusters = clusterIdList;

        // コントローラ表示モードを更新
        var lastIds = controllerView.ids;
        var latestIds = this.clusters;
        var addIds = diffArray(latestIds, lastIds);
        var delIds = diffArray(lastIds, latestIds);

        // 更新前の選択状態を保持
        var selectedCluster = controllerView.getSelectedControllers();
        addIds.forEach(function(id) {
            controllerView.addCluster(id);
        });
        delIds.forEach(function(id) {
            controllerView.delCluster(id);
        });
        // 更新前の選択状態に戻し
        controllerView.multiSelected(controllerView);

        // クラスタ一覧取得完了->クラスタ図形をSVGキャンバスに表示
        this.clusterDraw(this.clusters);
    }
    ;
    // MapView状のトラヒックアニメーション更新
    MSF.MSFMain.prototype.updateTrafficMapView = function() {
        var clusterInfoDic = this.db.clusterInfoDic;
        for(var clusterId in clusterInfoDic) {
            var clusterInfo = clusterInfoDic[clusterId];
            var clusterLinkIfs = clusterInfo.cluster_link_ifs;
            clusterLinkIfs.forEach(function(clusterLinkIf) {
                var opClusterId = clusterLinkIf.opposite_cluster_id;
                // 構成するIFを取得(物理orLAG)
                var nodeId;
                var ifType;
                var ifId;
                if (clusterLinkIf.physical_link) {
                    nodeId = clusterLinkIf.physical_link.node_id;
                    ifType = clusterLinkIf.physical_link.physical_if_id ? "physical-if" : "breakout-if";
                    ifId = clusterLinkIf.physical_link.physical_if_id ? clusterLinkIf.physical_link.physical_if_id : clusterLinkIf.physical_link.breakout_if_id;
                } else if (clusterLinkIf.lag_link) {
                    nodeId = clusterLinkIf.lag_link.node_id;
                    ifType = "lag-if";
                    ifId = clusterLinkIf.lag_link.lag_if_id;
                }

                var ifTraffic = clusterInfo.if_traffics.leafs[nodeId];
                if (!ifTraffic) {
                    return;
                }
                for (var i = 0; i < ifTraffic.if_traffics.length; i++) {
                    var traffic = ifTraffic.if_traffics[i].traffic_value;
                    if (traffic.if_type != ifType) {
                        continue;
                    }
                    if (traffic.if_id != ifId) {
                        continue;
                    }
                    var sendRate = traffic.send_rate;
                    trafficAnimationUpdate(clusterId, opClusterId, sendRate);
                    return;
                }
            });
        }
    }
    ;
    // 障害通知ダイアログ表示
    MSF.MSFMain.prototype.noticeFailueStatus = function() {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            // 障害通知OFFなら処理SKIP
            return null;
        }
        // SliceUnitの障害通知
        var sMessageList = [];
        var lastFailureSliceStatus = this.lastFailureSliceStatus;
        var newFailureSliceStatus = this.db.FailureInfoSliceDic;
        var l2SliceFailure = [];
        var key;
        for (var sliceId in newFailureSliceStatus[MSF.Const.SliceType.L2]) {
            if (!newFailureSliceStatus[MSF.Const.SliceType.L2][sliceId].isFailure) {
                continue;
            }
            key = newFailureSliceStatus[MSF.Const.SliceType.L2][sliceId].slice_type + sliceId;
            if (!lastFailureSliceStatus[MSF.Const.SliceType.L2] || lastFailureSliceStatus[MSF.Const.SliceType.L2].indexOf(key) == -1) {
                sMessageList.push("SliceId: " + key);
            }
            l2SliceFailure.push(key);
        }
        var l3SliceFailure = [];
        for (var sliceId in newFailureSliceStatus[MSF.Const.SliceType.L3]) {
            if (!newFailureSliceStatus[MSF.Const.SliceType.L3][sliceId].isFailure) {
                continue;
            }
            key = newFailureSliceStatus[MSF.Const.SliceType.L3][sliceId].slice_type + sliceId;
            if (!lastFailureSliceStatus[MSF.Const.SliceType.L3] || lastFailureSliceStatus[MSF.Const.SliceType.L3].indexOf(key) == -1) {
                sMessageList.push("SliceId: " + key);
            }
            l3SliceFailure.push(key);
        }
        // 今回情報を保存
        lastFailureSliceStatus[MSF.Const.SliceType.L2] = l2SliceFailure;
        lastFailureSliceStatus[MSF.Const.SliceType.L3] = l3SliceFailure;

        // PhysicalUnit障害通知
        var pMessageList = [];
        var lastFailureStatus = this.lastFailureStatus;
        var newFailureStatus = this.db.FailureInfoDic;
        var nodeType;
        var clusters = this.db.sw_clusters;
        for (var i = 0; i < clusters.length; i++) {
            // 最新の障害情報がなければ通知対象外
            var clusterId = clusters[i].cluster_id;
            var lastClusterFailure = lastFailureStatus[clusterId] || {};
            var newClustertFailure = newFailureStatus[clusterId];
            if (!newClustertFailure) {
                continue;
            }
            // Node障害
            for (nodeType in newClustertFailure.nodes) {
                // 装置種別ごとに障害情報を取出し
                var lastNodeList =  lastClusterFailure.nodes || {};
                lastNodeList = lastNodeList[nodeType] || [];
                var newNodeList =  newClustertFailure.nodes[nodeType];
                for (var ii = 0; ii < newNodeList.length; ii++) {
                    // 装置IDで新規障害情報を前回情報と比較
                    var node = newNodeList[ii];
                    if (lastNodeList.indexOf(node) == -1) {
                        pMessageList.push("#" + clusterId + " > " +nodeType+"#"+node);
                    }
                }
            }
            // IF障害
            for (nodeType in newClustertFailure.ifs) {
                // 装置種別ごとに障害情報を取出し
                var lastIfList = lastClusterFailure.ifs || {};
                lastIfList = lastIfList[nodeType] || {};
                var newIfList = newClustertFailure.ifs[nodeType];
                for (var nodeId in newIfList) {
                    // 装置IDごとに障害情報を取出し
                    var lastIfs = lastIfList[nodeId] || {};
                    var newIfs = newIfList[nodeId];
                    for (var ifType in newIfs) {
                        // 装置種別ごとに障害情報を取出し
                        var lastInfs = lastIfs[ifType] || [];
                        var newInfs = newIfs[ifType];
                        for (var j = 0; j < newInfs.length; j++) {
                            // IFIDで新規障害情報を前回情報と比較
                            var ifId = newInfs[j];
                            if (lastInfs.indexOf(ifId) == -1) {
                                pMessageList.push("#" + clusterId + " > " +nodeType+"#"+nodeId+" > "+ifType+"#"+ifId);
                            }
                        }
                    }
                }
            }
        }

        // ディープコピーで前回情報を保存
        this.lastFailureSliceStatus = JSON.parse(JSON.stringify(lastFailureSliceStatus));
        this.lastFailureStatus = JSON.parse(JSON.stringify(this.db.FailureInfoDic));
        
        if (sMessageList.length == 0 && pMessageList.length == 0) {
            return null;
        }

        var messageCount = 0;
        var outMessage = i18nx("Main.errMessage.Failure", "Failure occurred!!<br>");
        var lastMessage = "";

        if (sMessageList.length > 0) {
            outMessage = outMessage + "&nbsp;&nbsp;[" + i18nx("Main.errMessage.SliceUnit", "SliceUnit") + "]";
            outMessage =  outMessage + "<ul>";
            for(var i = 0; i < sMessageList.length; i++){
                if (messageCount >= 5) {
                    lastMessage = "and more...";
                    break;
                }
                outMessage = outMessage + "<li>"+sMessageList[i] + "</li>";
                messageCount++;
            }
            outMessage =  outMessage + "</ul>";
            outMessage =  outMessage + lastMessage;
        }

        if (pMessageList.length > 0 && messageCount < 5) {
            outMessage = outMessage + "&nbsp;&nbsp;[" + i18nx("Main.errMessage.PhysicalUnit", "PhysicalUnit") + "]";
            outMessage =  outMessage + "<ul>";
            for(var i = 0; i < pMessageList.length; i++){
                if (messageCount >= 5) {
                    lastMessage = "&nbsp;&nbsp;" + i18nx("Main.errMessage.AndMore", "and more...");
                    break;
                }
                outMessage = outMessage + "<li>"+pMessageList[i] + "</li>";
                messageCount++;
            }
            outMessage =  outMessage + "</ul>";
            outMessage =  outMessage + lastMessage;
        }

        return outMessage;
    }
    ;
    // ダイアログ描画
    MSF.MSFMain.prototype.noticeStatus = function(controllerMessage, failureMessage) {
        var title = i18nx("Main.errMessage.Attention", "Attention!!");
        var outMessageList = [];
        if (controllerMessage) {
            outMessageList.push(controllerMessage);
        }

        if (failureMessage) {
            outMessageList.push(failureMessage);
        }

        if(outMessageList.length === 0){
            return;
        }
        this.showInformationPanel(outMessageList.join("<br>"), title);
    }
    ;
    //
    // 詳細情報部の更新
    //   戻り値 : 詳細情報部の更新を管理するPromiseオブジェクト
    //
    MSF.MSFMain.prototype.UpdateDetailTable = function () {

        var action1;
        if (this.db.equipment_types &&
            this.db.equipment_types.length > 0 &&
            this.db.EquipmentTypeExpireDate !== null &&
            this.db.EquipmentTypeExpireDate > new Date()) {
            // 機種情報がすでに存在し、有効期限内の場合は新しくとりに行かない
            action1 = Promise.resolve();
        } else {
            action1 = this.rest.getEquipmentTypeListAll(MSF.MessageInfo.Polling.getEquipments);
        }
        action1 = action1.then(function(result) {
            this.dt.updateAllDetailTable(this.db);
        }.bind(this));

        return Promise.all([action1]);
    }
    ;
    //
    // 表示中情報の更新
    //
    MSF.MSFMain.prototype.updateViewModeInfo = function () {

        var clusterId;
        var deviceType;
        var nodeId;
        var equipmentTypeId;

        // MapView 更新 (NetworkMode)
        // ポーリング内で処理

        // ClusterView 更新 (NetworkMode)
        // ポーリングインターバルイベント処理中で実施

        // EquipmentView 更新 (NetworkMode)
        if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            clusterId = this.can.viewClusterId;
            deviceType = getDeviceTypesNavi();
            nodeId = getNodeIdNavi();
            // 装置内IFの状態更新
            updateNodeIF(clusterId, deviceType, nodeId);
            // 障害状態の更新
            failureNodeIf(clusterId, deviceType, nodeId);
        }

        // Controller 更新 (ControllerMode)
        // ポーリング内で処理
    }
    ;
    //
    // 詳細情報部の更新チェック
    //
    MSF.MSFMain.prototype.detailUpdateCheck = function () {

        var needUpdate = false;
        // 各々の現在の選択状態を一意に表す文字列
        var current1;
        var current2;

        // スライス選択状態チェック
        current1 = (this.can.Focus.Slice === null ? null : this.can.Focus.Slice.id);
        current2 = (this.can.Focus.Slice === null ? null : this.can.Focus.Slice.sliceType);
        if (this.focusedSliceId_ != current1 || this.focusSliceType_ != current2){
            //MSF.console.log("Slice Focus changed !");
            this.focusedSliceId_ = current1;
            this.focusedSliceType_ = current2;
            needUpdate = true;
        }

        // 装置選択状態チェック
        current1 = (this.can.Focus.Switch === null ? null : this.can.Focus.Switch.isLeaf);
        current2 = (this.can.Focus.Switch === null ? null : this.can.Focus.Switch.id);
        if (this.focusedSwIsLeaf_ != current1 || this.focusedSwId_ != current2){
            //MSF.console.log("Switch Focus changed !");
            this.focusedSwIsLeaf_ = current1;
            this.focusedSwId_ = current2;
            needUpdate = true;
        }

        // クラスタ選択状態チェック
        current1 = (this.can.Focus.Cluster === null ? null : this.can.Focus.Cluster.id);
        if (this.focusedClusterId_ != current1){
            //MSF.console.log("Cluster Focus changed !");
            this.focusedClusterId_ = current1;
            needUpdate = true;
        }

        // ポート選択状態チェック
        current1 = (this.can.Focus.Port === null ? null : this.can.Focus.Port.isLeaf);
        current2 = (this.can.Focus.Port === null ? null : this.can.Focus.Port.id);
        if (this.focusedPortIsLeaf_ != current1 || this.focusedPortId_ != current2){
            //MSF.console.log("Port Focus changed !");
            this.focusedPortIsLeaf_ = current1;
            this.focusedPortId_ = current2;
            needUpdate = true;
        }

        // 必要に応じて更新処理（REST発行はしない）
        if (needUpdate) {
            this.dt.updateAllDetailTable(this.db, needUpdate);
        }
    }
    ;
    //
    // REST結果の画面反映処理。controllerStatusの表示/非表示を切り替える
    //   connectResult : true=接続成功 false=接続失敗
    //   戻り値        : controllerStatusを表示中かどうか
    //
    MSF.MSFMain.prototype.reflectServerStatus = function(connectResult) {

        var isVisible = $("#controllerStatus").is(':visible');

        if (connectResult === false) {
            // 接続失敗

            if (isVisible){
                // すでにエラー表示中なので、何もしない
            } else {
                // エラー表示する
                $("#controllerStatus").show();
            }
        } else {
            // 接続成功
            if (isVisible){
                // エラー表示中なので非表示に戻す
                $("#controllerStatus").hide();
            } else {
                // エラー非表示なので何もしない
            }
        }

        return isVisible;
    }
    ;

    //
    // メッセージ表示処理
    //   Output : 出力先
    //   operationName : ログエリア用 オペレーション名
    //   message : 出力メッセージ
    //
    MSF.MSFMain.prototype.showMessage = function(Output, operationName, message) {
        switch(Output){
            case "ログエリア":
                // サーバ応答が異常だった場合
                MSF.main.log.appendLog(MSF.Const.LogType.Fail, operationName, message);
                break;
            case "登録系共通パネル":
                $("#panelAlert").html(message);
                $("#panelAlert").fadeIn(250);
                break;
            case "機種情報登録パネル":
                $("#addModelAlert").html(message);
                $("#addModelAlert").fadeIn(250);
                break;
            case "メッセージボックス":
                // メッセージパネル表示
                this.showInformationPanel(message);
                break;
            default:
                break;
        }
    }
    ;

    //
    // フィルターアクション登録処理
    //
    MSF.MSFMain.prototype.addStringFormatMethod = function() {
        // 存在チェック
        if (String.prototype.format == undefined) {
            //
            // フォーマット関数
            //
            String.prototype.format = function(arg) {
                // 置換ファンク
                var rep_fn = undefined;
                // オブジェクトの場合
                if (typeof arg == "object") {
                    rep_fn = function(m, k) {
                        return arg[k];
                    };
                }// 複数引数だった場合
                else {
                    var args = arguments;
                    rep_fn = function(m, k) {
                        return args[parseInt(k, 10)];
                    };
                }
                return this.replace(/\{(\w+)\}/g, rep_fn);
            };
        }
    }
    ;
    //
    // ポート速度一覧生成（機種情報選択パネル）
    //   戻り値 : 機種情報一覧が存在しない場合は false 以外は true
    //
    MSF.MSFMain.prototype.initPortSpeedList = function() {

        // 現在のリストを削除
        $("#portSpeedList").children().remove();

        // ポート速度Hash（KEYがポート速度、VALUEがtrue）
        var speedListHash = {};

        // 機種情報一覧から全機種でループ
        for (var i = 0; i < this.db.equipment_types.length; i++) {
            // 全スロットでループして、対応速度を全て取得
            var equipmentType = this.db.equipment_types[i];
            if (!equipmentType.slots) continue;
            for (var j = 0; j < equipmentType.slots.length; j++) {
                // 対応速度一覧をループ
                if (!equipmentType.slots[j].speed_capabilities) continue;
                for (var k = 0; k < equipmentType.slots[j].speed_capabilities.length; k++) {
                    var speed = equipmentType.slots[j].speed_capabilities[k];
                    // 未設定(異常)は無視
                    if(!speed) continue;
                    // Hashに追加
                    speedListHash[speed] = true;
                }
            }
        }

        // ポート速度一覧をリスト化
        var speedList = [];
        for (var speed in speedListHash) speedList.push(speed);

        // ソート(数値部分でソート)
        // null、undefinedなどはリストに存在しないので考慮しない
        speedList.sort(function (item1, item2) {

            // 最初の数値部分を取得("10G"、"100G"などを数値として比較したいため)
            var tmpArray;
            tmpArray = item1.match(/^[a-zA-Z-+]*([0-9]*)/);
            item1 = 0;
            if (tmpArray && tmpArray.length > 1 && tmpArray[1] !== "") {
                item1 = parseInt(tmpArray[1], 10);
            }
            tmpArray = item2.match(/^[a-zA-Z-+]*([0-9]*)/);
            item2 = 0;
            if (tmpArray && tmpArray.length > 1 && tmpArray[1] !== "") {
                item2 = parseInt(tmpArray[1], 10);
            }

            // 比較
            if (item1 < item2) {
                return -1;
            } else if(item1 > item2) {
                return 1;
            } else {
                return 0;
            }
        });

        // HTML生成
        for (var i = 0; i < speedList.length; i++) {
            var speed = speedList[i];
            var id = "portSpeed" + i;
            var html = '<input id="' + id + '" type="checkbox" class="portSpeed" data-port-speed="' + speed + '" />' +
                       '<label for="' + id + '">' + speed + '</label>';

            $("#portSpeedList").append($(html));
        }
        return (speedList.length > 0);
    }
    ;

    //
    // 機種一覧生成（機種情報選択パネル）
    //   戻り値 : 条件に合致する機種の数
    //
    MSF.MSFMain.prototype.initModelList = function() {

        // ポート速度Hash（KEYがポート速度）
        var speedListHash = {};
        // パネルで選択されたポート速度(取得条件)一覧を取得
        $("#portSpeedList input[type='checkbox']:checked").each(function(){
            speedListHash[$(this).attr("data-port-speed")] = false;
        });

        // 現在のリストを削除
        $("#modelList").children().remove();

        // 機種情報一覧から全機種でループ
        var id = 0;
        for (var i = 0; i < this.db.equipment_types.length; i++) {
            // vpnTypeが不一致の場合は、スキップ
            var equipmentType = this.db.equipment_types[i];
            if (!equipmentType.capability) continue;
            if ($("#vpnTypeL2").prop("checked") === true && equipmentType.capability.vpn.l2 !== true) continue;
            if ($("#vpnTypeL3").prop("checked") === true && equipmentType.capability.vpn.l3 !== true) continue;

            // ポート速度の不適合Hashを生成(KEY:ポート速度 VAL:true)
            var unmatchSpeed = {};
            for (var key in speedListHash) unmatchSpeed[key] = true;

            // 全スロットでループして、ポート速度の適合状況をチェック
            // ただし、ポート速度が未指定の場合はポート速度のチェックをそもそもしない
            if (Object.keys(unmatchSpeed).length > 0) {
                for (var j = 0; j < equipmentType.slots.length; j++) {
                    // 対応速度一覧をループ
                    if (!equipmentType.slots[j].speed_capabilities) continue;
                    for (var k = 0; k < equipmentType.slots[j].speed_capabilities.length; k++) {
                        var speed = equipmentType.slots[j].speed_capabilities[k];
                        // 未設定(異常)は無視
                        if (!speed) continue;
                        // 不適合Hashから削除
                        delete unmatchSpeed[speed];
                        if (Object.keys(unmatchSpeed).length === 0) break;
                    }
                    if(Object.keys(unmatchSpeed).length === 0) break;
                }
            }
            // 不適合の場合は次の機種へ
            if (Object.keys(unmatchSpeed).length > 0) continue;

            // HTMLを生成
            var elementID = "model" + i;
            var modelID = equipmentType.equipment_type_id;
            var platform = equipmentType.platform;
            var checked = ((id == 0) ? " checked" : "");
            var html = '<input id="' + elementID + '" type="radio" name="model" class="model" value="' + modelID + '"' + checked + ' />' +
                       '<label for="' + elementID + '">' + platform + '</label>';
            id = id + 1;
            $("#modelList").append($(html));
        }

        return id;
    }
    ;

    //
    // 機種一覧生成（機種情報選択パネル 追加用）
    //   戻り値 : なし
    //
    MSF.MSFMain.prototype.initConfigModelList = function() {

        // 現在のリストを削除
        $("#configModelList").children().remove();

        // 機種情報一覧のconfigから全機種でループ
        var id = 0;
        for (var modelKey in MSF.ModelInfo) {

            var equipmentType = MSF.ModelInfo[modelKey].equipment_type;

            // HTMLを生成
            var elementID = "configModel" + id;
            var platform = equipmentType.platform;
            var checked = ((id == 0) ? " checked" : "");
            var html = '<input id="' + elementID + '" type="radio" name="addModel" class="model" value="' + modelKey + '"' + checked + ' />' +
                       '<label for="' + elementID + '">' + platform + '</label>';
            id = id + 1;
            $("#configModelList").append($(html));
        }

        return id;
    }
    ;

    //
    // スライス一覧選択状態取得
    //   vpnType   : L2 or L3（MSF.Const.SliceTypeを指定）
    //   clusterId : ClusterMode表示中であれば選択中のClusterIDを指定。
    //   戻り値  : 現在選択されているスライスIDの一覧配列（順序は不定）
    //             未選択の場合、(権限などにより)スライスメニューが存在しない場合は空の配列を返す。
    //
    MSF.MSFMain.prototype.getSelectedSliceIdList = function(vpnType, clusterId) {

        // vpnTypeを判別
        var sliceDicList = MSF.main.menuSliceDic[vpnType];

        // スライスメニューの要素一覧を取得
        var checkedIDList = [];
        for (var index in sliceDicList) {
            var status = sliceDicList[index];

            // 削除済みデータは対象外
            if (!status.isActive) {
                continue;
            }

            // 選択中ClusterIDに該当しなければ対象外
            // クラスタに属していないスライスは対象
            if (status.clusterIds.length !== 0) {
                if (clusterId !== undefined && status.clusterIds.indexOf(clusterId) == -1) {
                    continue;
                }
            }

            if (status.isChecked) {
                checkedIDList.push(status.id);
            }
        }

        return checkedIDList;
    }
    ;

    //
    // モーダルウィンドウ表示
    //   panelId                : モーダルウィンドウとして表示するdiv要素のID
    //   closeAtBackgroundClick : パネル以外の背景クリック時にパネルを閉じるかどうか？(boolean)
    //
    MSF.MSFMain.prototype.showModalPanel = function(panelId, closeAtBackgroundClick) {

        // サイズ計算
        var panelWidth = $("#"+panelId).outerWidth();
        var panelHeight = $("#"+panelId).outerHeight();

        // 背景を隠すレイヤーを表示
        if ($("#maskLayer").is(':visible') === false) {
            $("#maskLayer").css({ "display": "block", opacity: 0 });
            $("#maskLayer").fadeTo(250, 0.4);
        }

        // 背景クリック時にパネルを閉じるように設定
        $("#maskLayer").unbind("click");
        if (closeAtBackgroundClick === true) {
            $("#maskLayer").click(this.closeModalPanel);
        }

        // パネル自体を表示
        $("#"+panelId).css("display", "block");
        $("#"+panelId).css("left", "50%");
        $("#"+panelId).css("margin-left", -(panelWidth / 2) + "px");
        $("#"+panelId).css("top", "calc(50% - " + (panelHeight / 2) + "px)");
        // fade効果を出すために透明度を元に戻す
        $("#"+panelId).css("opacity",0);
        $("#"+panelId).fadeTo(250, 1);
        // パネルを表示する際先頭に移動する
        $(".panelBody").scrollTop(0);
        // 折りたたみ項目の初期状態は非表示
        $("div.initHideItem.POJO").hide();
        $("span.hideItem").css({color: "#c0c0c0", cursor: "default"});
    }
    ;

    //
    // モーダルウィンドウ非表示
    //
    MSF.MSFMain.prototype.closeModalPanel = function(hideMaskLayer) {
        if (hideMaskLayer == null) hideMaskLayer = true;
        if (hideMaskLayer) $("#maskLayer").fadeOut(250);
        $(".overlayPanel").css("display", "none");
    }
    ;

    //
    // メッセージパネル表示
    // ほかのモーダルパネルを表示していない状態で使用してください。２枚重ね不可。
    //   message: 表示するメッセージ（htmlテキストで指定可能。brタグも使用可）
    //   title: メッセージのタイトル。省略時は「Information」
    //
    MSF.MSFMain.prototype.showInformationPanel= function(message, title) {

        if (!title) title = "Information";

        $("#informationTitle").html(title);
        $("#informationMessage").html(message);

        this.showModalPanel("infomationPanel", true);
    }
    ;

    //
    // ログアウト
    //
    MSF.MSFMain.prototype.logout = function() {
        window.location.href = "./Login.html";
    }
    ;

    //
    // 機種選択パネル表示
    //   messageInfo: 呼び出し元によって中身が変わるメッセージクラス
    //
    MSF.MSFMain.prototype.showModelSelectPanel = function(messageInfo) {

        // 機種一覧(検索結果)を一度隠して、条件一覧を表示
        $("#capabilitySelectSection").show();
        $("#modelSelectSection").hide();
        // エラーメッセージを非表示
        $("#deviceSearchAlert").hide();

        // ポート速度一覧を取得して、ラジオボタンを生成
        var bRet = this.initPortSpeedList();
        if (!bRet) return;

        // 機種一覧(検索結果)の表示処理
        $('#deviceSearch').unbind('click');
        $("#deviceSearch").click(function () {
            // エラーメッセージを非表示
            $("#deviceSearchAlert").hide();

            // 機種情報検索
            var modelCount = this.initModelList();
            if (modelCount === 0) {
                $("#deviceSearchAlert").html(messageInfo.NotFound);
                $("#deviceSearchAlert").show();
                return;
            }

            //機種一覧表示
            $("#capabilitySelectSection").hide();
            $("#modelSelectSection").fadeIn(250);
        }.bind(this));

        // 機種一覧(検索結果)から、条件選択に戻る
        $('#modelSelectBack').unbind('click');
        $("#modelSelectBack").click(function () {
            $("#modelSelectSection").hide();
            $("#capabilitySelectSection").fadeIn(250);
        });

        // パネルを表示
        this.showModalPanel("modelInfoSelectPanel", false);
    }
    ;
    //
    // 機種選択パネル表示
    //   戻り値: なし
    //
    MSF.MSFMain.prototype.showAddModelSelectPanel = function() {

        // 機種情報一覧をconfig->画面に反映
        this.initConfigModelList();

        // エラーメッセージを非表示
        $("#addModelAlert").hide();

        // パネルを表示
        this.showModalPanel("addModelInfoSelectPanel", false);
    }
    ;
    //
    // RESTログダウンロード画面
    //
    MSF.MSFMain.prototype.restLogDownload = function() {

        // ダウンロード用の要素を生成
        var count = 1;
        $("#debugPanel").children().remove();
        $("#debugPanel").append('<div id="restList_" style="overflow:auto;max-height:100%;"></div>');

        var blob, html;
        for (var key in this.rest.restResponseList) {
            blob = new Blob([ this.rest.restResponseList[key] ], { "type" : "text/plain" });
            window.URL = window.URL || window.webkitURL;
            html = '<a target="_blank" href="' + window.URL.createObjectURL(blob) + '" ' +
                   'download="' + count + '.txt">' + key + '</a><br>';
            count = count + 1;
            $("#restList_").append(html);
        }
        this.showModalPanel("debugPanel",true);
    }
    ;
    //
    // ポーリングのON/OFF切替
    //
    MSF.MSFMain.prototype.switchExecutePolling = function() {
        this.execPolling = $("#execPolling").prop("checked");
    }
    ;
    //
    // 障害発生のON/OFF切替
    //
    MSF.MSFMain.prototype.switchFailureOccurred = function() {
        // デバッグ用
    }
    ;
    //
    // DB表示画面
    //
    MSF.MSFMain.prototype.showDb = function() {

        var textVal = JSON.stringify(this.db, null, 4);
        var copyFrom = document.createElement("textarea");
        copyFrom.textContent = textVal;
        var bodyElm = document.getElementsByTagName("body")[0];
        bodyElm.appendChild(copyFrom);
        copyFrom.select();
        var retVal = document.execCommand('copy');
        bodyElm.removeChild(copyFrom);
    }
    ;
    //
    // Map Viewでの図形選択/選択解除イベント処理(詳細更新)
    // isTraffic: IF/CPトラヒック詳細部更新ON/OFF
    //
    MSF.MSFMain.prototype.updateDetailTable = function() {
       this.dt.updateAllDetailTable(this.db);
    }
    ;
    //
    // ControllerModeでの図形選択/選択解除イベント処理(詳細更新)
    //
    MSF.MSFMain.prototype.updateDetailControllerTable = function() {
        this.dt.updateControllerTable(this.db);
    }
    ;
})();
//
// エントリポイント
//
$(function() {
"use strict";

    // MSFクラス生成＆初期化
    MSF.main = new MSF.MSFMain();
    if (MSF.Conf.System.Debug.USE_DUMMY_DATA) {
        MSF.main.execPolling = false;
    }
    MSF.main.init();
    // マルチクラスタ表示領域の生成
    svg = new MSF.NetworkView(800, 480);
    svg.initMultiClusterView();
    nodeView = new MSF.NodeView(800,480);

    trafficAnimationStart();
    // --------------------------------------------------------------------
    // テスト実装
    if (MSF.Conf.System.Debug.USE_DUMMY_DATA) {
        MSF.main.db = dummyDB || MSF.main.db;

        // クラスタ更新
        MSF.main.clusterMapDraw();

        // スライス/CP更新
        MSF.main.updateMenuSliceDic();

        // その他更新
        // Spine-Leafの対向関係を保持
        MSF.main.updateSpineLeafDic();
        // 障害情報の辞書を更新
        MSF.main.updateFailureInfoDic();
        // 障害情報描画
        MSF.main.updateFailureStatus();
        // 画面表示用データが揃った時点でClusterメニューを更新
        updateClusterMenu(MSF.main.clusters);
        // SVGキャンバスにSVG図形(クラスタ間リンク)を表示
        MSF.main.clusterLinkDraw();
        // キャンバスにデータセット
        MSF.main.can.setInfo(MSF.main.db);
        // レイアウト計算
        MSF.main.mf.calcLayout();
        // 描画処理
        MSF.main.can.Draw();

        setTimeout(function(){
            MSF.main.log.appendLog(MSF.Const.LogType.Success, "get edge-point", "Request Succeeded");
        }, 5000);
        setTimeout(function(){
            MSF.main.log.appendLog(MSF.Const.LogType.Success, "get edge-point", "Request Succeeded");
        }, 10000);
        setTimeout(function(){
            MSF.main.log.appendLog(MSF.Const.LogType.Success, "get nodes", "Request Succeeded");
        }, 15000);
        setTimeout(function(){
            MSF.main.log.appendLog(MSF.Const.LogType.Fail, "set edge-point", "Request Failed. Regist information error.");
        }, 20000);
        setTimeout(function(){
            MSF.main.log.appendLog(MSF.Const.LogType.Success, "get equipment-types", "Request Succeeded");
        }, 25000);
        setTimeout(function() {
            // 障害情報ダイアログ描画
            MSF.main.noticeStatus(MSF.main.noticeSystemStatus(), MSF.main.noticeFailueStatus());
            
        }, 30000);
    }
    // --------------------------------------------------------------------



    // イベント登録
    // ウィンドウリサイズイベント
    $(window).resize(MSF.main.mf.resize.bind(MSF.main.mf));
    if (MSF.main.mf.supportTouch) {
        // documentイベント(タッチムーブ)
        $("#clipFrame").on({
            touchstart:MSF.main.mf.touchStart.bind(MSF.main.mf),
            touchmove: MSF.main.mf.touchMove.bind(MSF.main.mf),
            touchend: MSF.main.mf.touchEnd.bind(MSF.main.mf),
            touchcancel: MSF.main.mf.touchEnd.bind(MSF.main.mf),
            gesturechange: MSF.main.mf.gestureChange.bind(MSF.main.mf)
        });
    } else {
        $("#clipFrame").on({
            // clipFrameクリックイベント
            click: MSF.main.mf.clipFrameClick.bind(MSF.main.mf)
        });
        $(document).on({
            // documentイベント(マウスムーブ)
            //mousemove: MSF.main.mf.Drag.bind(MSF.main.mf),
            mouseup: function(event) {
                MSF.main.mf.dragInfo.clickPoint = null;
                // ユーザー選択設定
                MSF.main.mf.setUserSelect(true);
            }
        });
    }
    // マウスホイールイベント
    $("#clipFrame").mousewheel(MSF.main.mf.mousewheel.bind(MSF.main.mf));

    // モーダルパネルのクローズ
    $(".panelCloseButton").click(MSF.main.closeModalPanel);
    $(".panelCancelButton").click(MSF.main.closeModalPanel);
    $("#informationClose").click(MSF.main.closeModalPanel);

    // ログアウト
    $("#logout").click(MSF.main.logout);

    //
    // ■デバッグ用機能
    //
    // デバッグ用画面表示
    $("#restLogDownload").click(MSF.main.restLogDownload.bind(MSF.main));
    // ポーリングON/OFF切替
    $("#execPolling").click(MSF.main.switchExecutePolling.bind(MSF.main));
    // DB表示
    $("#dbMonitor").click(MSF.main.showDb.bind(MSF.main));
    // 障害発生ON/OFF切替
    $("#failureOccurred").click(MSF.main.switchFailureOccurred.bind(MSF.main));

    /* メニューをリセット */
    $("*").click(function(event) {
        if ($(event.target).closest("#controlMenu input").length == 0 &&
            $(event.target).closest("#controlMenu label").length == 0) {
            $("#controlMenu .menuItem[type='radio']").each(function() {
                if ($(this).prop("checked") === true &&
                    $(this).is($(event.target)) === false) {
                    $(this).prop("checked", false).change();
                }
            });
        }
    });

    /* メニューグループをアコーディオン式に表示 */
    $(".menuTitle").click(function(event) {
        $(event.target).next().slideToggle(300);
    });

    /* メニューをアコーディオン式に表示(1) */
    $(".menuItem").click(function(event) {
        $(event.target).next().next().slideToggle(300);
    });
    /* メニューをアコーディオン式に表示(2) もう一度クリックで元に戻す */
    $(".menuItem").change(function(event) {
        /* radioボタン用の処理 */
        $(".menuItem[type='radio'][name=" + $(event.target).prop("name") + "]").each(function() {
            if ($(this).prop("checked") === false) {
                $(this).next().next().slideUp(300);
            }
        });
    });

    /* アコーディオン内のメニューをアコーディオン式に表示(1) */
    $(".menuItemAcc").click(function(event) {
        $(event.target).next().next().slideToggle(300);
    });
    /* アコーディオン内のメニューをアコーディオン式に表示(2) もう一度クリックで元に戻す */
    $(".menuItemAcc").change(function(event) {
        /* radioボタン用の処理 */
        $(".menuItemAcc[type='radio'][name=" + $(event.target).prop("name") + "]").each(function() {
            if ($(this).prop("checked") === false) {
                $(this).next().next().slideUp(300);
            }
        });
    });

    /* トラヒックメニュークリックイベント */
    $("#traffic").click(function(event){
        var hit = false;
        if ($("#traffic").prop("checked") === true){
            hit = true;
        }
        MSF.main.isTrafficPolling = hit;
        if (hit){
            // MapViewトラヒックアニメーション開始
            trafficAnimationOn();
            // トラヒックラインアニメーション開始
            MSF.main.can.startAnimation();
        }else{
            // MapViewトラヒックアニメーション停止
            trafficAnimationOff();
            // トラヒックラインアニメーション停止
            MSF.main.can.stopAnimation();
        }
        MSF.main.mf.updateSelectedSlice();
    });

    /* コントローラメニュークリックイベント */
    $("#controller").click(function(event){
        var hit = false;
        if ($("#controller").prop("checked")){
            hit = true;
        }
        MSF.main.isControllerPolling = hit;
    });

    // デバイス表示モードを初期値として表示
    MSF.main.mf.showDeviceMode();

    //
    //Network Modeボタンクリックイベント
    //
    $('#tab1').on("click", function () {
        MSF.main.mode = MSF.Const.Mode.Network;
    });

    //
    //Controller Modeボタンクリックイベント
    //
    $('#tab2').on("click", function () {
        MSF.main.mode = MSF.Const.Mode.Controller;
        
        // 詳細部更新処理
        MSF.main.updateDetailControllerTable();
    });

    //
    //Network Modeボタンクリックイベント
    //
    $('#menuMap').on("click", function () {
        MSF.main.networkmode = MSF.Const.NetworkMode.Map;
    });

    //
    //Fabric Networkボタンクリックイベント
    //
    $('#menuCluster').on("click", function () {
        MSF.main.networkmode = MSF.Const.NetworkMode.Cluster;
    });

    //
    //Equipmentボタンクリックイベント
    //
    $('#menuNode').on("click", function () {
        MSF.main.networkmode = MSF.Const.NetworkMode.Equipment;
    });

});
