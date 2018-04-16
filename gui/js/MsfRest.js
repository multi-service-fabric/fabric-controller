//
// MSFRest通信クラス
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfRest = function(msfCanvas, conf, db) {
        this.can = msfCanvas;
        this.cluster_id = conf.CLUSTER_ID;
        this.host = conf.HOST;
        this.OperationDic = {};
        this.db = db;
        this.getTimeout = conf.GET_TIMEOUT;
        //
        // 以下デバッグ用設定
        //
        this.debug_ = false;
        // URLごとの応答データを保持して、あとでダウンロードできるようにする。
        // URL⇒応答データ(テキスト)

        this.restResponseList = {};
    }
    ;

    //
    // 任意のタイミングで参照DBをセットしなおす
    //
    MSF.MsfRest.prototype.setDB = function(db) {
        this.db = db;
    }
    ;

    //
    // オペレーション完了通知先情報のオプションパラメータを追加
    //
    MSF.MsfRest.prototype.optionParamAdd = function (url) {
        var address = MSF.Conf.Rest.MFC.NOTIFY_ADDRESS;
        var port = MSF.Conf.Rest.MFC.NOTIFY_PORT;
        return url + "?notification_address=" + address + "&notification_port=" + port;
    }
    ;

    //
    // Rest通信のための構造体を返します
    // messageInfo： 設計書の画面表示メッセージ一覧と対応するMSF.MessageInfoのオブジェクト
    // data：GETのオプションパラメータ または POST等のJsonリクエストBODY
    //
    MSF.MsfRest.prototype.getRestStruct = function(type, url, urlParam, messageInfo, data) {
        var restStruct = {
            ajax:{
                // URL作成
                url:this.host + url.format(urlParam),
                // URLパラメータの参照用保持
                urlParam: urlParam,
                // GET、POST等
                type: type,
                // Json等
                // 200の成功応答の場合はJSON形式の戻りではなくnull戻りなので、以下の指定はしない
                // jQueryが自動的に判断してくれる。
                //dataType: "json",
                // GETのオプションパラメータ または POST等のJsonリクエストBODY
                data: data,
                // MIMEタイプ
                contentType: "application/json; charset=utf-8"
            },
            // エラー時にtrue
            isError:false,
            // エラー時に使うため保持
            msgInfo: messageInfo,
            // エラー時のメッセージに変数がある場合に指定
            msgParam: {},
            // ResponseのBodyが入るJson形式
            data:null,
            // ログ表示用成功メッセージ
            successMessage:null,
            // ログ表示用エラーメッセージ
            errorMessage:null,
            // エラーの場合でエラー処理したあとリジェクトしたくない場合trueにする
            isNotReject: false,
            XMLHttpRequest:null,
            textStatus:null,
            errorThrown:null
        };

        if (this.getTimeout &&
            isNaN(this.getTimeout) === false &&
            restStruct.ajax.type == "GET") {
            restStruct.ajax.timeout = this.getTimeout;
        }

        return restStruct;
    }
    ;
    //
    // 通信処理（GET、Json）
    //
    MSF.MsfRest.prototype.Rest = function(rest) {
        var response = rest;
        MSF.console.info("Rest: [rest.ajax.url]: " + rest.ajax.url);
        MSF.console.info("Rest: [rest.ajax.type]: " + rest.ajax.type);
        MSF.console.info("Rest: [rest.ajax.urlParam]: \n" + JSON.stringify(rest.ajax.urlParam, null, 4));
        MSF.console.info("Rest: [rest.ajax.data]: \n" + JSON.stringify(isJSON(rest.ajax.data) ? JSON.parse(rest.ajax.data) : rest.ajax.data, null, 4));

        // 非同期オブジェクト設定
        return new Promise(function(resolve, reject) {
            if (MSF.Conf.System.Debug.NOT_CONNECT_TO_SERVER) {
                reject(response);
            } else {
                // リクエストを保持(デバッグ用)
                if (this.debug_) {
                    if (rest.ajax.type!="GET") {
                        this.restResponseList["["+rest.ajax.type+"]"+response.ajax.url] = rest.ajax.data;
                    }
                }
                // 通信開始
                $.ajax(rest.ajax)
                .done(function(responseData, textStatus, XMLHttpRequest) {
                    // 非同期通信成功
                    response.data = responseData;
                    response.textStatus = textStatus;
                    response.XMLHttpRequest = XMLHttpRequest;
                    // 応答を保持(デバッグ用)
                    if (this.debug_){ 
                        if (rest.ajax.type == "GET") {
                            this.restResponseList["["+response.ajax.type+"]"+response.ajax.url] = XMLHttpRequest.responseText;
                        }
                    }
                    resolve(response);
                }.bind(this))
                .fail(function(XMLHttpRequest, textStatus, errorThrown) {
                    // 通信失敗
                    response.XMLHttpRequest = XMLHttpRequest;
                    response.textStatus = textStatus;
                    response.errorThrown = errorThrown;
                    response.isError = true;
                    reject(response);
                });
            }
        }
        .bind(this))
        .then(function(response) {
            this.reflectServerStatus(true);
            // 出力メッセージ作成
            if ( response.msgInfo.Success){
                response.successMessage = response.msgInfo.Success.format(response.msgParam);
            }

            if ( response.msgInfo.SuccessWithError){
                response.errorMessage = response.msgInfo.SuccessWithError.format(response.msgParam);
            }
            return response;
        }.bind(this))
        .catch(function(response) {
            //
            var isShowingMessage = false;
            if ( response.textStatus=="error" && response.XMLHttpRequest.readyState === 0 ) {
                // サーバに接続できない状態
                isShowingMessage = this.reflectServerStatus(false);
                // ポーリングでない場合
                if (!response.msgInfo.operationName){
                    isShowingMessage = false;
                }
                // 出力メッセージ作成
                response.errorMessage = response.msgInfo.ConnectError.format(response.msgParam);
            } else if ( response.textStatus=="timeout" ) {
                // サーバが込み合っている状態
                isShowingMessage = this.reflectServerStatus(false);
                // ポーリングでない場合
                if (!response.msgInfo.operationName) {
                    isShowingMessage = false;
                }
                response.errorMessage = response.msgInfo.ConnectTimeout.format(response.msgParam);
            } else {
                // サーバがエラーを返した状態
                this.reflectServerStatus(true);
                var status = response.XMLHttpRequest.status;

                // エラーメッセージを生成
                var resWord = null;
                if (response.textStatus=="parsererror") {
                    // 応答のJSONボディをパースできなかった場合(構文エラー)
                    resWord = "Response Parse Error (status=[{0}])".format(status);
                } else {
                    // エラーコードとRestのステータスから動的メッセージ取得
                    var responseJSON = response.XMLHttpRequest.responseJSON;
                    var error_code = "";
                    if (status && responseJSON ){
                        error_code = responseJSON.error_code;
                        resWord = MSF.MessageDic[status][error_code];
                    }
                }

                // メッセージがなかった場合
                resWord = resWord ||  "Undefined Error (error=[{0}]、status=[{1}])".format(error_code, status);
                response.msgParam.ResponseErrorMessage = resWord;
                // 出力メッセージ作成
                response.errorMessage = response.msgInfo.ResponseError.format(response.msgParam);
            }
            if (response.isNotReject) {
                // thenに返す指定があり呼び出し側が自前でエラー処理する場合
                return Promise.resolve(response);
            } else {
                if (!isShowingMessage) {
                    // メッセージ表示
                    this.showMessage(response.msgInfo.Output, response.msgInfo.operationName, response.errorMessage);
                }
                return Promise.reject(response);
            }
        }
        .bind(this))
        ;
    }
    ;

    //
    // サーバ状態反映
    //   connectResult : true=接続成功 false=接続失敗
    //   戻り値        : なし
    //
    MSF.MsfRest.prototype.reflectServerStatus = function(connectResult) {
        // 抽象メソッド。実装に依存。
    }
    ;

    //
    // メッセージ表示処理
    //   Output : 出力先
    //   operationName : ログエリア用 オペレーション名
    //   message : 出力メッセージ
    //
    MSF.MsfRest.prototype.showMessage = function(Output, operationName, message) {
        // 抽象メソッド。実装に依存。
    }
    ;

    //
    // オペレーション詳細情報取得処理
    //
    MSF.MsfRest.prototype.getOperationDetail = function(messageInfo, operation_id, msgParam) {
        var pm = {operation_id:operation_id};
        var url = "/v1/operations/{operation_id}";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, pm, messageInfo);
        rs.msgParam = msgParam;

        return this.Rest(rs);
    }
    ;

    //
    // 状態取得処理
    //
    MSF.MsfRest.prototype.getStatusList = function(messageInfo) {
        var param = {};
        var url = "/v1/MSFcontroller/status";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            MSF.console.info("getStatusInfo Done. response:" + response.data);
            var information = response.data.informations || [];
            this.db.controller_informations = information;
            return response;
        }.bind(this))
        ;
        
        return promise;
    }
    ;

    //
    // SWクラスタ情報一覧取得処理
    //
    MSF.MsfRest.prototype.getClusterList = function(messageInfo) {
        var param = {};
        var url = "/v1/clusters";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, {format:"detail-list", 'user-type':"operator"});
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            MSF.console.info("getSwClustersInfo Done. response:" + response.data);
            this.db.sw_clusters = response.data.clusters;
            return response;
        }.bind(this))
        ;
        
        return promise;
    }
    ;

    //
    // 装置情報一覧取得処理(システム管理者向け)
    //
    MSF.MsfRest.prototype.getNodesInfo = function(messageInfo, clusterId) {

        MSF.console.info("getNodesInfo Start. clusterId: " + clusterId);
        var pm = {cluster_id: clusterId};
        var url = "/v1/clusters/{cluster_id}/nodes";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, pm, messageInfo, {format:"detail-list", 'user-type':"operator"});

        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            MSF.console.info("getNodesInfo Done. clusterId: " + clusterId);
            this.db.clusterInfoDic[clusterId].NodesInfo = response.data;

            return response;
        }.bind(this))
        ;

        return promise;
    }
    ;

    //
    // L2スライス情報一覧取得処理
    //
    MSF.MsfRest.prototype.getL2SliceList = function(messageInfo) {
        MSF.console.info("getL2SliceList Start.");
        return this.getSliceList(messageInfo, MSF.Const.SliceType.L2, "detail-list")
        .then(function(response) {
            // DBに設定
            MSF.console.info("getL2SliceList Done.");
            // MFCからの応答値を設定
            this.db.l2_slices = response.data.l2_slices;
            return response;
        }.bind(this));
    }
    ;
    //
    // L3スライス情報一覧取得処理
    //
    MSF.MsfRest.prototype.getL3SliceList = function(messageInfo) {
        MSF.console.info("getL3SliceList Start.");
        return this.getSliceList(messageInfo, MSF.Const.SliceType.L3, "detail-list")
        .then(function(response) {
            // DBに設定
            MSF.console.info("getL3SliceList Done.");
            // MFCからの応答値を設定
            this.db.l3_slices = response.data.l3_slices;
            return response;
        }.bind(this));
    }
    ;
    //
    // スライス情報一覧取得処理
    //
    MSF.MsfRest.prototype.getSliceList = function(messageInfo, slice_type, format) {
        var param = {slice_type:slice_type};
        var url = "/v1/slices/{slice_type}";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, {format:format});
        return this.Rest(rs);
    }
    ;

    //
    // 全CP一覧情報取得処理
    // スライス一覧取得後に、CP一覧を取得する
    //
    MSF.MsfRest.prototype.getCPListAll = function(messageInfo) {
        MSF.console.info("getCPListAll Start.");
        return Promise.all([
            this.getL2SliceList(MSF.MessageInfo.Polling.getSliceIDListForCP),
            this.getL3SliceList(MSF.MessageInfo.Polling.getSliceIDListForCP)
        ])
        .then(function(response) {
            MSF.console.info("getCPListAll Done.");
            var promiseList = [];

            // L2CP一覧取得
            var i, id, lst;
            lst = response[0].data.l2_slices;
            for (i = 0; i < lst.length; i++) {
                id = lst[i].slice_id;
                promiseList.push(this.getCPList(messageInfo, MSF.Const.SliceType.L2, id, "detail-list"));
            }
            // L3CP一覧取得
            lst = response[1].data.l3_slices;
            for (i = 0; i < lst.length; i++) {
                id = lst[i].slice_id;
                promiseList.push(this.getCPList(messageInfo, MSF.Const.SliceType.L3, id, "detail-list"));
            }

            return Promise.all(promiseList);

        }.bind(this))
        .then(function(response) {
        
            var cpInfo = {};
            var sliceId;
            var cpKey;
            
            var l2_slices = this.db.l2_slices;
            var l2_cps = this.db.l2_cps;
            for( var i=0; i < l2_slices.length; i++){
                sliceId = l2_slices[i].slice_id;
                cpKey = MSF.Const.SliceType.L2 + sliceId;
                cpInfo[cpKey] = {};
                // [スライスタイプ＋ID][cpid]=CP情報の辞書作成
                var cps = l2_cps[sliceId];
                for (var ii = 0; ii < cps.length; ii++) {
                    var cp = cps[ii];
                    cpInfo[cpKey][cp.cp_id] = cp;
                }
            }

            
            var l3_slices = this.db.l3_slices;
            var l3_cps = this.db.l3_cps;
            for(i=0; i < l3_slices.length; i++){
                sliceId = l3_slices[i].slice_id;
                cpKey = MSF.Const.SliceType.L3 + sliceId;
                cpInfo[cpKey] = {};
                // [スライスタイプ＋ID][cpid]=CP情報の辞書作成
                var cps = l3_cps[sliceId];
                for (var ii = 0; ii < cps.length; ii++) {
                    var cp = cps[ii];
                    cpInfo[cpKey][cp.cp_id] = cp;
                }
            }

            this.db.CPInfo = cpInfo;
        }.bind(this));
    }
    ;

    //
    // CP一覧情報取得処理
    //
    MSF.MsfRest.prototype.getCPList = function(messageInfo, slice_type, slice_id, format) {
        MSF.console.info("getCPList Start. slice_type:" + slice_type);
        var sliceKey = slice_type + slice_id;
        var param = {
            slice_type:slice_type,
            slice_id:slice_id
        };
        var url = "/v1/slices/{slice_type}/{slice_id}/cps/";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, {format:format});
        return this.Rest(rs)
        .then(function(response) {
            MSF.console.info("getCPList Done. slice_type:" + slice_type);
            if (slice_type == MSF.Const.SliceType.L2) {
                this.db.l2_cps[slice_id] = response.data.l2_cps;
            }
            if (slice_type == MSF.Const.SliceType.L3) {
                this.db.l3_cps[slice_id] = response.data.l3_cps;
            }
            //this.db.l2_cps = response.data.l2_cps;
            //this.db.l3_cps = response.data.l3_cps;
            return response;
        }.bind(this))
    }
    ;

    //
    // IF/CPトラヒック情報取得処理 (Connect to MFC)
    //
    MSF.MsfRest.prototype.getTraffic = function(messageInfo) {
        var promiseList = [];
        // IFトラヒック情報一覧取得
        promiseList.push(this.getIfTrafficInfoAll(messageInfo));
        
        // CPトラヒック情報一覧取得
        promiseList.push(this.getCPTrafficInfoAll(messageInfo));
        
        // パラレル非同期実行
        return Promise.all(promiseList)
        .then(function(response){
            return response;
        }.bind(this));
    }
    ;

    //
    // 全IFトラヒック情報取得処理
    //
    MSF.MsfRest.prototype.getIfTrafficInfoAll = function (messageInfo) {
        var promiseList = [];

        for (var index in this.db.sw_clusters) {
            var clusterId = this.db.sw_clusters[index].cluster_id;
            var nodeId, node;
            for (nodeId in this.db.clusterInfoDic[clusterId].NodesInfo.leafs) {
                node = this.db.clusterInfoDic[clusterId].NodesInfo.leafs[nodeId];
                promiseList.push(this.getIfTrafficInfo(messageInfo, clusterId, MSF.Const.FabricType.Leafs, node.node_id));
            }
            for (nodeId in this.db.clusterInfoDic[clusterId].NodesInfo.spines) {
                node = this.db.clusterInfoDic[clusterId].NodesInfo.spines[nodeId];
                promiseList.push(this.getIfTrafficInfo(messageInfo, clusterId, MSF.Const.FabricType.Spines, node.node_id));
            }
        }

        return Promise.all(promiseList);
    }
    ;
    

    // IFトラヒック情報一覧取得処理(FC)
    MSF.MsfRest.prototype.getIfTrafficInfo = function (messageInfo, clusterId, fabricType, nodeId) {
        MSF.console.info("getIfTrafficInfo Start. cluster_id: " + clusterId + " fabric_type: " + fabricType + " node_id: " + nodeId);
        var param = {
            cluster_id:clusterId,
            fabric_type:fabricType,
            node_id:nodeId
        };
        var url = "/v1/traffic/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces";
        var rs = this.getRestStruct("GET", url, param, messageInfo);

        return this.Rest(rs)
        .then(function(response) {
            // DBに設定
            this.db.clusterInfoDic[clusterId].if_traffics[fabricType][nodeId] = response.data;
        }.bind(this));
    };

    //
    // 全CPトラヒック一覧情報取得処理
    // スライス一覧取得後に、CPトラヒック一覧を取得する
    //
    MSF.MsfRest.prototype.getCPTrafficInfoAll = function(messageInfo) {
        return Promise.all([
            this.getL3SliceList(MSF.MessageInfo.Polling.getSliceIDListForCP)
        ])
        .then(function(response) {
            var promiseList = [];

            // L3CP一覧取得
            var lst = response[0].data.l3_slices;
            for (var i = 0; i < lst.length; i++) {
                var id = lst[i].slice_id;
                promiseList.push(this.getCpTrafficInfo(messageInfo, MSF.Const.SliceType.L3, id));
            }

            return Promise.all(promiseList);

        }.bind(this));
    }
    ;

    // CPトラヒック情報一覧取得処理
    MSF.MsfRest.prototype.getCpTrafficInfo = function (messageInfo, sliceType, sliceId) {
        MSF.console.info("getCpTrafficInfo Start. slice_type: " + sliceType + " slice_id: " + sliceId);
        var param = {
            slice_type:sliceType,
            slice_id:sliceId
        };
        var url = "/v1/traffic/slices/{slice_type}/{slice_id}/cps";
        var rs = this.getRestStruct("GET", url, param, messageInfo);

        return this.Rest(rs)
        .then(function(response) {
            // DBに設定
            this.db.cp_traffics[sliceId] = response.data;

            var cpTraffics = response.data.cp_traffics;
            if (!cpTraffics) {
                return;
            }
            cpTraffics.forEach(function(cpTraffic) {
                var sliceKey = sliceType + cpTraffic.slice_id;
                var cp = this.db.CPInfo[sliceKey][cpTraffic.cp_id];
                if (!cp) {
                    MSF.console.info("対象のCPが存在しない. sliceKey:" + sliceKey + "  cpId:" + cpTraffic.cp_id);
                    return;
                }
                cp.receive_rate = cpTraffic.traffic_value.receive_rate;
                cp.send_rate = cpTraffic.traffic_value.send_rate;
            }.bind(this));
        }.bind(this));
    };

    //
    //登録・削除処理
    //
    MSF.MsfRest.prototype.condition = function (messageInfo, info, url, method) {
        // Rest通信のための構造体取得
        var rs = this.getRestStruct(method, url, info.pm, messageInfo, JSON.stringify(info.body));
        rs.msgParam = info.msgParam;

        return this.Rest(rs)
        .then(function(response){
            MSF.console.debug("共通登録・削除", response);
            return response;
        })
        ;
    }
    ;
    //
    // SLICE生成処理
    //
    MSF.MsfRest.prototype.createSLICE = function (messageInfo, info) {
        var url = "/v1/slices/{slice_type}";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("POST", url, info.pm, messageInfo, JSON.stringify(info.body));

        return this.Rest(rs)
        .then(function(response){
            MSF.console.debug("020101_スライス生成", response);
            return response;
        })
        ;
    }
    ;
    //
    // SLICE削除処理
    //
    MSF.MsfRest.prototype.deleteSLICE = function (messageInfo, info) {
        var url = "/v1/slices/{slice_type}/{slice_id}";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("DELETE", url, info.pm, messageInfo);

        return this.Rest(rs)
        .then(function(response){
            MSF.console.debug("020103_スライス削除", info, response);
            return response;
        })
        ;
    }
    ;
    //
    // CP生成処理
    //
    MSF.MsfRest.prototype.createCP = function(messageInfo, info) {
        var url = this.optionParamAdd("/v1/slices/{slice_type}/{slice_id}/cps");
        // Rest通信のための構造体取得
        var rs = this.getRestStruct("POST", url, info.pm, messageInfo, JSON.stringify(info.body));
        rs.msgParam = {
            edge_point_id: info.body.edge_point_id
        };
        // エラーはPromise.allで処理する
        rs.isNotReject = true;
        // 通信開始
        return this.Rest(rs)
        .then(function(response){
            MSF.console.debug("020201_CP生成", info, response);
            return response;
        })
        ;
    }
    ;
    //
    // CP削除処理
    //
    MSF.MsfRest.prototype.deleteCP = function (messageInfo, info, isNotReject) {
        var url = this.optionParamAdd("/v1/slices/{slice_type}/{slice_id}/cps/{cp_id}");
        // Rest通信のための構造体取得
        var rs = this.getRestStruct("DELETE", url, info.pm, messageInfo);
        rs.msgParam = {
            cp_id: info.pm.cp_id
        };
        if (isNotReject){
            // エラーはPromise.allで処理する
            rs.isNotReject = isNotReject;
        }
        return this.Rest(rs)
        .then(function(response){
            MSF.console.debug("020203_CP削除", info, response);
            return response;
        })
        ;
    }
    ;

    //
    // エッジポイント情報一覧取得処理(システム管理者向け詳細一覧)
    //
    MSF.MsfRest.prototype.getEdgepointList = function(messageInfo, clusterId) {
        var param = {cluster_id:clusterId};
        var url = "/v1/clusters/{cluster_id}/points/edge-points";

        var query = {};
        query["format"]="detail-list";
        query["user-type"]="operator";

        // Rest通信のための構造体取得

        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            var edgepointDic = {};
            var l2_edge_points = [];
            var l3_edge_points = [];

            var lst = response.data.edge_points;
            // 辞書作成
            for (var i = 0; i < lst.length; i++) {
                var ep = lst[i];
                if (ep.support_protocols.L2) {
                    l2_edge_points.push(ep);
                } else if (ep.support_protocols.L3){
                    l3_edge_points.push(ep);
                } else {
                    MSF.console.warn(ep);
                }
                edgepointDic[ep.edge_point_id] = ep;
            }

            // DBに設定
            this.db.clusterInfoDic[clusterId].EdgepointDic = edgepointDic;
            this.db.clusterInfoDic[clusterId].l2_edge_points = l2_edge_points;
            this.db.clusterInfoDic[clusterId].l3_edge_points = l3_edge_points;

            return response;
        }.bind(this));

        return promise;
    }
    ;

    //
    // 機種情報一覧取得処理（詳細一覧）
    //
    MSF.MsfRest.prototype.getEquipmentTypeListAll = function(messageInfo) {
        var param = null;
        var url = "/v1/equipment-types";

        var query = {};
        query["format"]="detail-list";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);

        return this.Rest(rs)
        .then(function(response) {

            this.db.equipment_types = response.data.equipment_types;

            // 辞書作成
            var equipmentTypeDic = {};
            var lst = response.data.equipment_types;
            for (var i = 0; i < lst.length; i++) {
                var equipmentType = lst[i];
                equipmentTypeDic[equipmentType.equipment_type_id] = equipmentType;
            }
            this.db.EquipmentTypeDic = equipmentTypeDic;

            // 有効期限設定
            var now = new Date();
            var period = MSF.Conf.Rest.MFC.MODEL_INFO_VALID_PERIOD;
            this.db.EquipmentTypeExpireDate = new Date(now.getTime() + period * 1000);

            return response;
        }.bind(this));
    }
    ;

    //
    // SWクラスタ情報一覧取得（スライス利用者向け詳細一覧）
    //
    MSF.MsfRest.prototype.getSwClusterSliceDetailList = function(messageInfo) {
        var param = null;
        var url = "/v1/clusters";

        var query = {};
        query["format"]="detail-list";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            this.db.sw_clusters = response.data;
            return response;
        }.bind(this))
        ;

        return promise;
    }
    ;

    //
    // SWクラスタ情報一覧取得（システム利用者向け詳細一覧）
    //
    MSF.MsfRest.prototype.getSwClusterSystemDetailList = function(messageInfo) {
        var param = null;
        var url = "/v1/clusters";

        var query = {};
        query["format"]="detail-list";
        query["user-type"]="operator";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            this.db.sw_clusters = response.data;
            return response;
        }.bind(this))
        ;

        return promise;
    }
    ;

    //
    // クラスタ間リンクIF情報一覧取得（詳細一覧）
    //
    MSF.MsfRest.prototype.getClusterLinkIfDetailList = function(messageInfo, clusterId) {
        var param = {cluster_id:clusterId};
        var url = "/v1/clusters/{cluster_id}/interfaces/cluster-link-ifs";

        var query = {};
        query["format"]="detail-list";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            this.db.clusterInfoDic[clusterId].cluster_link_ifs = response.data.cluster_link_if_ids;
            return response;
        }.bind(this));

        return promise;
    }
    ;

    //
    // クラスタ間リンクIF情報取得
    //
    MSF.MsfRest.prototype.getClusterLinkIf = function(messageInfo, cluster_link_if_id) {
        var param = {
            cluster_id:this.cluster_id,
            cluster_link_if_id:cluster_link_if_id
        };

        var url = "/v1/clusters/{cluster_id}/interfaces/cluster-link-ifs/{cluster_link_if_id}";
        var query = {};

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);

        return this.Rest(rs);
    }
    ;

    //
    // 全IF情報一覧取得処理 (2017)
    //
    MSF.MsfRest.prototype.getInterfaceListAll = function(messageInfo) {
        var promiseList = [];

        for (var index in this.db.sw_clusters) {
            var clusterId = this.db.sw_clusters[index].cluster_id;
            var nodeId, node;
            for (nodeId in this.db.clusterInfoDic[clusterId].NodesInfo.leafs) {
                node = this.db.clusterInfoDic[clusterId].NodesInfo.leafs[nodeId];
                MSF.console.info("getInterfaceList[Leaf]. clusterId:" + clusterId + "  nodeId:" + node.node_id);
                promiseList.push(this.getInterfaceList(messageInfo, clusterId, MSF.Const.FabricType.Leafs, node.node_id));
            }
            for (nodeId in this.db.clusterInfoDic[clusterId].NodesInfo.spines) {
                node = this.db.clusterInfoDic[clusterId].NodesInfo.spines[nodeId];
                MSF.console.info("getInterfaceList[Spine]. clusterId:" + clusterId + "  nodeId:" + node.node_id);
                promiseList.push(this.getInterfaceList(messageInfo, clusterId, MSF.Const.FabricType.Spines, node.node_id));
            }
        }

        return Promise.all(promiseList);
    }
    ;

    //
    // IF情報一覧取得処理 (2017)
    //
    MSF.MsfRest.prototype.getInterfaceList = function(messageInfo, cluster_id, fabric_type, node_id) {
        var param = {
            cluster_id:cluster_id,
            fabric_type:fabric_type,
            node_id:node_id
        };
        var url = "/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces";

        var query = {};
        query["format"]="detail-list";

        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定
            var interfaces = {};
            interfaces.nodeId = node_id;
            interfaces.interface = response.data;
            //this.db.clusterInfoDic[cluster_id].InterfacesInfo[fabric_type].push(interfaces);
            this.db.clusterInfoDic[cluster_id].InterfacesInfo[fabric_type][node_id] = response.data;
            return response;
        }.bind(this))
        ;

        return promise;
    }
    ;

    //
    // 障害情報一覧取得処理
    //
    MSF.MsfRest.prototype.getFailureStatusList = function(messageInfo) {
        var param = {};
        var url = "/v1/failures/failure_status";
        var query = {};
        
        // Rest通信のための構造体取得
        var rs = this.getRestStruct("GET", url, param, messageInfo, query);
        var promise = this.Rest(rs)
        .then(function(response) {
            // DBに設定 (null許容値は空データで登録)
            var physicalUnit = response.data.physical_unit || {};
            physicalUnit.nodes = physicalUnit.nodes || [];
            physicalUnit.ifs = physicalUnit.ifs || [];
            
            var sliceUnit = response.data.slice_unit || {};
            sliceUnit.slices = sliceUnit.slices || [];
            
            this.db.failure_status.physical_unit = physicalUnit;
            this.db.failure_status.slice_unit = sliceUnit;
            
            return response;
        }.bind(this))
        ;
        return promise;
    }
    ;
})();
