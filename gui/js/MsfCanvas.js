//
// MSFキャンバスクラス
//
(function() {
    "use strict";
    //
    // コンストラクタ
    //
    MSF.MsfCanvas = function(mainCanvas, animationCanvas, conf) {
        this.scale = 1.0;
        // mainとtransキャンバスはマウス座標を一致させる為、ドラック位置とスケーリングは同期させる
        this.mainCanvas = mainCanvas;
        this.animationCanvas = animationCanvas;
        this.DisplyArea = [];
        // データ管理オブジェクトのルート配列（選択中のスライスリスト）
        this.SliceList = [];
        // 物理スライス管理オブジェクト ずっと保持する。　ポーリング時はクラスター配下が新規オブジェクトに置き換わる
        this.PhysicalSliceInfo = null;
        // 全スライスのオリジナル辞書キーはsliceType+id バリューはSliceInfoオブジェクトの参照
        this.SliceListDic = {};
        // 通信成功時に取得した全トラヒック情報の辞書　次の通信成功時まで保持する
        // キーはSliceType＋SliceId　値はTrafficInfo構造体のリスト
        this.TrafficListDic = {};
        // トラヒックの破線アニメーション管理オブジェクトリスト
        this.moveLineInfoList = [];
        // 現在フォーカスされているオブジェクト
        this.Focus = {
            Slice: null,
            Cluster: null,
            Port: null,
            Switch: null,
            TrafficSlice: null,
            TrafficLineList: null
        };
        this.SpeedRate = 0;
        // 45固定
        this.deg = 45;
        // 疑似３D 　カメラのX方向Y方向の角度からスライド比率計算
        this.slide = GetSlideRatio(this.deg, this.deg);
        // コンフィグ
        this.Conf = conf;
        // 定数定義
        this.DeviceMode = "DeviceMode";
        this.ClusterMode = "ClusterMode";
        this.DispMode = "";
        this.dummySlice = this.getSliceStruct("", "");
        this.dummySlice.isVisible = false;
        this.animationTimer = null;
        // 描画対象のクラスタID
        this.viewClusterID = "";
        // 障害情報(setInfo()内で更新
        this.failureStatus = {};
        this.failureStatusSlice = {};
    }
    ;
    //
    // スライス一覧選択状態取得
    //   sliceType : L2 or L3（MSF.Const.SliceTypeを指定）
    //   戻り値  : 現在選択されているスライスIDの一覧配列（順序は不定）
    //             未選択の場合、(権限などにより)スライスメニューが存在しない場合は空の配列を返す。
    //
    MSF.MsfCanvas.prototype.getSelectedSliceIdList = function() {// 抽象メソッド。実装に依存。
    }
    ;
    //
    // 初期化処理
    //
    MSF.MsfCanvas.prototype.init = function() {
        // 物理スライス
        this.PhysicalSliceInfo = this.getSliceStruct("", MSF.Const.SliceType.L2);
        this.PhysicalSliceInfo.isLogical = false;
        this.PhysicalSliceInfo.name = "";
        this.PhysicalSliceInfo.isVisible = false;


        var conf = this.Conf.Traffic;
        // 一番最後のトラヒックリミット値を描画の最高速度とする
        var sl = conf.SpeedList;
        var sp = sl[sl.length - 1];
        this.SpeedRate = conf.DRAWING_POWER / sp.TRAFFIC_FROM;
        // トラヒックアニメーション管理オブジェクトリスト
        for (var i = 0; i < conf.SpeedList2.length; i++) {
            sp = conf.SpeedList2[i];
            var ml = this.getMoveLineInfoStruct();
            ml.load = sp.STROKE_SPEED;
            ml.dash_on = sp.DASH_ON;
            ml.dash_off = sp.DASH_OFF;
            ml.len = ml.dash_on + ml.dash_off;
            ml.rate = ml.len / ml.load;
            this.SetMoveLineDash2(ml);
            this.moveLineInfoList.push(ml);
        }
    }
    ;
    //
    // トラヒックラインアニメーション停止処理
    //
    MSF.MsfCanvas.prototype.stopAnimation = function() {
        clearInterval(this.animationTimer);
        this.animationTimer = null;
    }
    ;
    //
    // トラヒックラインアニメーション処理
    //
    MSF.MsfCanvas.prototype.startAnimation = function() {
        this.TrafficListDic = {};
        this.PhysicalSliceInfo.TrafficList = [];
        for (var key in this.SliceListDic) {
            var si = this.SliceListDic[key];
            si.TrafficList = [];
        }
        if (this.animationTimer !== null) {
//             MSF.console.debug("アニメーションタイマーが二重に呼ばれました。ID=[" + this.animationTimer + "]" + " ※pastのshowからpresentクリックの場合は問題なし");
            return;
        }
        // アニメーション描画用のタイマーを設定
        this.animationTimer = setInterval(function() {
            for (var i = 0; i < this.moveLineInfoList.length; i++) {
                var ml = this.moveLineInfoList[i];
                if (ml.load > 0) {
                    // スピードが０以外のものだけ計算
                    this.SetMoveLineDash2(ml);
                }
            }
            // トラヒック描画
            this.DrawTraffic(null);
        }
        .bind(this), this.Conf.Traffic.DRAWING_UPDATE_TIME);
    }
    ;
    //
    // スライス情報構造体取得
    //
    MSF.MsfCanvas.prototype.getSliceStruct = function(id, sliceType) {
        return {
            id: id,
            name: id,
            isLogical: true,
            sliceType: sliceType,
            vpnType: MSF.Const.getVpnTypeForSliceType(sliceType),
            DispMode: "",
            isVisible: true,
            rect: Rect(),
            // トラヒック用データ管理オブジェクトのルート配列
            TrafficList: [],
            // トラヒック用の描画原点
            TarfficBasePos: Point(),
            ClusterList: [this.getClusterStruct()]
        };
    }
    ;
    //
    // クラスター情報構造体取得
    //
    MSF.MsfCanvas.prototype.getClusterStruct = function() {
        return {
            // 今回はクラスターの配列は１個のみ
            id: MSF.Conf.Rest.CLUSTER_ID,
            rect: Rect(),
            depth: 0,
            EpList: [],
            SpineList: [],
            LeafList: [],
            SpineDic: {},
            LeafDic: {},
            LineDic: {},
            isError: false
        };
    }
    ;
    //
    // スイッチ情報構造体取得
    //
    MSF.MsfCanvas.prototype.getSwitchStruct = function(id) {
        return {
            id: id,
            isLeaf: true,
            rect: Rect(),
            depth: 0,
            caption: "#" + id,
            status: "",
            CPDic: {l2vpn:[]}
        };
    }
    ;
    //
    // 接続線情報構造体取得
    //
    MSF.MsfCanvas.prototype.getLineInfoStruct = function(SpineId, LeafId) {
        return {
            Alert: false,
            Spine: SpineId,
            Leaf: LeafId
        };
    }
    ;
    //
    // 流動線データ定義構造体取得
    //
    MSF.MsfCanvas.prototype.getMoveLineInfoStruct = function() {
        return {
            dash_on: 5,
            dash_off: 10,
            len_count: 0,
            // トラヒックが全然取得できなかった場合を考慮して-1を初期値とする
            // 取得した結果合算が0なら0になる。-1を表現するかはコンフィグに依存する
            speed: -1,
            trafficColor: "red",
            lineWidth: 1,
            strokeSpeed: 0,
            dashList: [0, 0, 0, 0]
        };
    }
    ;
    //
    // ソートされたスイッチリストを返します処理
    //
    MSF.MsfCanvas.prototype.getSortedSwitchList = function(switchDic) {
        var lst = [];
        var keyList = Object.keys(switchDic);

        keyList.sort(function(a, b) {
            return a - b;
        });

        for (var i = 0; i < keyList.length; i++) {
            var key = keyList[i];
            var sw = switchDic[key];
            lst.push(sw);
        }

        return lst;
    }
    ;
    //
    // 情報設定処理
    //
    MSF.MsfCanvas.prototype.setInfo = function(db) {
        // 表示対象クラスタ
        var clusterId = this.viewClusterId;
        if (!clusterId) {
            // FabricNW Viewを表示していないので処理SKIP
            return;
        }
        
        var sliceListDic = {};
        // Spineをキーにぶら下がるLeafを列挙した辞書
        var dicLineInfoSpine = {};
        // Leafをキーにぶら下がるSpineを列挙した辞書
        var dicLineInfoLeaf = {};
        var nd = db.clusterInfoDic[clusterId].NodesInfo;
        // 物理スライス（トポロジ）はスライス情報構造体のクラスターリスト以外を常に保持する　所属するトラヒックデータは別タイミングで初期化
        var si = this.PhysicalSliceInfo;
        // クラスター情報構造体取得
        var pcl = this.getClusterStruct();
        // クラスターリスト初期化
        si.ClusterList = [pcl];

        // 物理情報
        for (var ii = 0; ii < si.ClusterList.length; ii++) {
            var cl = si.ClusterList[ii];
            // Spine設定
            this.setPhysicalSliceInfo(nd.spines, cl.SpineDic, dicLineInfoSpine, cl.LineDic, false);
            cl.SpineList = this.getSortedSwitchList(cl.SpineDic);

            // Leaf設定
            this.setPhysicalSliceInfo(nd.leafs, cl.LeafDic, dicLineInfoLeaf, cl.LineDic, true);
            cl.LeafList = this.getSortedSwitchList(cl.LeafDic);
        }
        // L2スライス情報設定
        this.setSliceInfo(db, db.l2_slices, sliceListDic, MSF.Const.SliceType.L2, pcl, dicLineInfoLeaf, clusterId);
        // L3スライス情報設定
        this.setSliceInfo(db, db.l3_slices, sliceListDic, MSF.Const.SliceType.L3, pcl, dicLineInfoLeaf, clusterId);

        // コミット
        this.SliceListDic = sliceListDic;

        // 障害情報設定
        this.failureStatus = db.FailureInfoDic;
        this.failureStatusSlice = db.FailureInfoSliceDic;

        var focusDevice = this.Focus.Switch || this.Focus.Port;
        if (focusDevice && this.Focus.Slice.isLogical) {
            // 装置未選択状態
            var sliceKey = this.Focus.Slice.sliceType + this.Focus.Slice.id;
            // SliceListDicにトポロジ情報はないが、トポロジ選択時はこのコードまで来ない
            var slice = this.SliceListDic[sliceKey];
            var sw = null;
            if (focusDevice.isLeaf) {
                sw = slice.ClusterList[0].LeafDic[focusDevice.id];
            } else {
                sw = slice.ClusterList[0].SpineDic[focusDevice.id];
            }

            if (!sw) {
                this.Focus.Switch = null;
                this.Focus.Port = null;
            }
        }
    }
    ;
    //
    // 物理スライス情報設定
    //
    MSF.MsfCanvas.prototype.setPhysicalSliceInfo = function(nodeSwitchList, switchDic, dicLineInfo, LineDic, isLeaf) {
        var makeKey = MSF.Const.makeKey;
        var IF_TYPE_KEY = ["lag_ifs", "physical_ifs", "breakout_ifs"];
        var IF_ID_KEY = ["lag_if_id", "physical_if_id", "breakout_if_id"];
        
        // Spine設定
        for (var iii = 0; iii < nodeSwitchList.length; iii++) {
            // Restから来たスイッチリスト
            var ndsw = nodeSwitchList[iii];

            // 対応するスイッチインスタンス作成
            var sw = this.getSwitchStruct(ndsw.node_id);
            sw.status = ndsw.provisioning_status;
            sw.isLeaf = isLeaf;
            // クラスターに所属しているスイッチ連想配列にノードIDで追加
            switchDic[ndsw.node_id] = sw;
            // 接続線情報設定
            // 初回はインスタンス作成
            var lineInfo = dicLineInfo[ndsw.node_id] = dicLineInfo[ndsw.node_id] || {};
            // 各IF数でループ
            for (var ifIndex in IF_TYPE_KEY) {
                var ifTypeKey = IF_TYPE_KEY[ifIndex];
                var ifIdKey = IF_ID_KEY[ifIndex];
                for (var i = 0; i < ndsw[ifTypeKey].length; i++) {
                    // 各IF一覧情報の内部リンク向けIF用情報がある場合はSpineないしLeafと接続しているとみなす
                    var internal_options = ndsw[ifTypeKey][i].internal_options;
                    if (internal_options) {
                        var opposite_if = internal_options.opposite_if;
                        // 一時辞書にLeaf、Spineそれぞれにぶら下がるノードIDのキーを追加
                        lineInfo[opposite_if.node_id] = {};
                        // 接続線情報のキーはSpineID＋LeafIDとする
                        if (opposite_if.fabric_type == MSF.Const.DeviceType.Spine) {
                            var key = makeKey(opposite_if.node_id, ndsw.node_id);
                            // クラスターに所属している接続線連想配列に追加
                            LineDic[key] = this.getLineInfoStruct(opposite_if.node_id, ndsw.node_id);
                        }
                    }
                }
            }

        }
    }
    ;
    //
    // スライス情報設定
    //
    MSF.MsfCanvas.prototype.setSliceInfo = function(db, DB_slices, sliceList, sliceType, PhysicalClusterInfo, dicLineInfoLeaf, clusterId) {
        if (!DB_slices) {
            return;
        }
        for (var i = 0; i < DB_slices.length; i++) {
            var slice = DB_slices[i];
            var key = sliceType + slice.slice_id;
            // スライスが有効でない場合は対象外とする
            if (!slice.isActive) {
//                 MSF.console.debug("有効でないスライスがあります[" + key + "]");
//                continue;
            }
            var si = this.getSliceStruct(slice.slice_id, sliceType);
            var cl = si.ClusterList[0];
            var CpList = slice[si.vpnType + "_cp_ids"];
            this.setSliceSwitch(db, key, CpList, PhysicalClusterInfo, cl, dicLineInfoLeaf, clusterId);
            cl.SpineList = this.getSortedSwitchList(cl.SpineDic);
            cl.LeafList = this.getSortedSwitchList(cl.LeafDic);

            sliceList[key] = si;
        }
    }
    ;
    //
    // スライス上のスイッチ情報設定
    //
    MSF.MsfCanvas.prototype.setSliceSwitch = function(db, sliceKey, cp_ids, PhysicalClusterInfo, clusterInfo, dicLineInfoLeaf, clusterId) {
        var cl = clusterInfo;
        var pcl = PhysicalClusterInfo;
        var makeKey = MSF.Const.makeKey;
        for (var i = 0; i < cp_ids.length; i++) {
            var CPid = cp_ids[i];
            var SIids = db.CPInfo[sliceKey];
            if  (!SIids) {
//                 MSF.console.debug("「020205_CP一覧情報取得」で取得したデータに「020105_スライス情報一覧取得」で取得したスライス[" + sliceKey + "]の情報がありません")
                continue;
            }
            var CPobj = SIids[CPid];
            if  (!CPobj) {
//                 MSF.console.debug("「020205_CP一覧情報取得」で取得したデータに「020105_スライス情報一覧取得」で取得したCP[" + sliceKey + "," + CPid + "]の情報がありません")
                continue;
            }
            if (CPobj.cluster_id != clusterId) {
                continue;
            }

            var ep = db.clusterInfoDic[clusterId].EdgepointDic[CPobj.edge_point_id];
            if (!ep) {
//                 MSF.console.debug("「011102_edge-point情報一覧取得」で取得したデータに「020205_CP一覧情報取得」で取得したエッジポイントの情報がありません", ep);
                continue;
            }
            var node_id = ep.base_if.leaf_node_id;
            var leaf = pcl.LeafDic[node_id];
            if (leaf) {
                cl.LeafDic[node_id] = leaf;
                // リーフにスライス毎に所属するCPリストを保持させる
                var CPList = leaf.CPDic[sliceKey] = leaf.CPDic[sliceKey] || [];
                CPList.push(CPobj);
                leaf.CPDic.l2vpn.push(CPobj);

                var spineList = dicLineInfoLeaf[node_id];
                for (var key in spineList) {
                    var spine = pcl.SpineDic[key];
                    if (spine) {
                        // Leafに接続されているSpine設定
                        cl.SpineDic[key] = spine;
                        // 接続線設定
                        var key2 = makeKey(key, node_id);
                        cl.LineDic[key2] = pcl.LineDic[key2];
                    }
                }
            }
        }
    }
    ;
    //
    // レイアウト計算処理
    //
    MSF.MsfCanvas.prototype.calcLayout = function() {
        var maxWidth = 0;
        var maxHeight = 0;
        var offsetY = this.Conf.Slice.MARGIN;
        this.SliceList = [];
        //
        // スライス選択処理
        //
        var selectSlice_ = function(sliceType, clusterId) {
            //
            var sidList = this.getSelectedSliceIdList(sliceType, clusterId);
            // スライスリスト
            for (var i = 0; i < sidList.length; i++) {
                var id = sidList[i];
                if (id) {
                    var si = this.SliceListDic[sliceType + id];
                    if (si) {
                        this.SliceList.push(si);
                    }
                }
            }
        }
        .bind(this);

        var clusterId = this.viewClusterId;
        selectSlice_(MSF.Const.SliceType.L2, clusterId);
        selectSlice_(MSF.Const.SliceType.L3, clusterId);
        // スライス４つ固定で下から積み上げるイメージにするための処理
        var cnt = this.Conf.Slice.MAX_SLICE_COUNT - this.SliceList.length;
        for (var i = 0; i < cnt; i++) {
            this.SliceList.push(this.dummySlice);
        }
        // 非表示スライス、スライスキーでソート
        this.SliceList.sort(function(siA, siB) {
            if (siA.isVisible < siB.isVisible) {
                return -1;
            } else if (siA.isVisible == siB.isVisible) {
                var sliceKeyA = siA.sliceType + siA.id;
                var sliceKeyB = siB.sliceType + siB.id;
                if (sliceKeyA < sliceKeyB) {
                    return -1;
                } else if (sliceKeyA == sliceKeyB) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        });
        // 物理スライス（トポロジ）は一番最後に追加
        this.SliceList.push(this.PhysicalSliceInfo);
        // スライスリスト
        for (var sl = 0; sl < this.SliceList.length; sl++) {
            var si = this.SliceList[sl];
            var cl = si.ClusterList[0];
            si.DispMode = this.DispMode;
            var conf = si.DispMode == this.ClusterMode ? this.Conf.Cluster : this.Conf.Device;
            var slide = GetSlide(conf.DEPTH);
            // クラスタの高さを元にスライスの縦幅を決定
            si.rect.height = slide.y + conf.MARGIN_Y + conf.HEIGHT + conf.MARGIN_Y;
            // クラスターレイアウト処理
            var allClusterWidth = this.LayoutCluster(si, conf);
            // クラスターのサイズを元にスライスの横幅を決定
            si.rect.width = conf.MARGIN_X + allClusterWidth + conf.MARGIN_X;
            // スライスの位置 横位置固定、縦は規定サイズの積み重ね計算(横にはひとつ。縦には複数)
            si.rect.x = this.Conf.Slice.MARGIN;
            si.rect.y = offsetY;
            // トラヒック用原点座標
            si.TarfficBasePos.x = si.rect.x + cl.rect.x + slide.x;
            si.TarfficBasePos.y = si.rect.y + cl.rect.y - slide.y;
            offsetY += this.Conf.Slice.MARGIN + si.rect.height;
            // スライスの横幅 = 横幅 + 疑似３Ｄでシアーした奥行き分(※)
            // ※ 45度シアーなので、横=縦
            var sliceWidth = si.rect.width + si.rect.height;
            // キャンバスサイズ計算
            //    横：スライスの幅＋マージン
            //    縦：（計算済み）
            maxWidth = Math.max(maxWidth, si.rect.x + sliceWidth + this.Conf.Slice.MARGIN);
            maxHeight = offsetY;
        }
        return Size(maxWidth, maxHeight);
    }
    ;
    //
    // クラスターレイアウト処理
    //
    MSF.MsfCanvas.prototype.LayoutCluster = function(sliceInfo, conf) {
        var si = sliceInfo;
        var allClusterWidth = 0;
        var pcl = this.PhysicalSliceInfo.ClusterList[0];

        var offsetX = conf.MARGIN_X + conf.MARGIN_Y;
        for (var i = 0; i < si.ClusterList.length; i++) {
            var cl = si.ClusterList[i];
            // 横幅計算 一番長いスイッチ配列を取得
            var cnt = Math.max(pcl.SpineList.length, pcl.LeafList.length);
            cnt = Math.max(cnt, conf.MINIMUM_PORT_COUNT);
            cnt = Math.max(cnt, 1);
            // スイッチ数からクラスターサイズ計算
            cl.rect.width = (this.Conf.Switch.WIDTH + this.Conf.Switch.MARGIN) * cnt;
            // 左右で２つ
            cl.rect.width = cl.rect.width + conf.PADDING * 2;

            // Spineの中央配置
            this.LayoutSpineCenter(cl.rect.width, pcl.SpineList);
            // Spineスイッチレイアウト
//             this.LayoutSwitch(0, pcl.SpineList);
            // Leafスイッチレイアウト
            this.LayoutSwitch(conf.DEPTH, pcl.LeafList);


            cl.rect.height = conf.HEIGHT;
            cl.depth = conf.DEPTH;
            // 位置 横 クラスターの数分 縦固定
            cl.rect.x = offsetX;
            cl.rect.y = sliceInfo.rect.height - conf.MARGIN_Y;
            // 少し隙間を開けて、次のクラスタの位置決め
            offsetX += cl.rect.width + conf.CLEARANCE;
            // 全クラスタの横サイズと隙間の合計を導出
            if (i > 0) {
                allClusterWidth += conf.CLEARANCE;
            }
            allClusterWidth += cl.rect.width;
        }
        // 全クラスターの横サイズ
        return allClusterWidth;
    }
    ;
    //
    // Spineの中央配置処理
    //
    MSF.MsfCanvas.prototype.LayoutSpineCenter = function(width, SpineList) {
        var switchOffset = this.Conf.Switch.WIDTH + this.Conf.Switch.MARGIN;
        var switchLength = switchOffset * SpineList.length;
        var centerOffset = width / 2 - switchLength / 2;
        var idx = 0;
        for (var i = 0; i < SpineList.length; i++) {
            var sw = SpineList[i];
            // サイズ
            sw.rect.width = this.Conf.Switch.WIDTH;
            sw.rect.height = this.Conf.Switch.HEIGHT;
            sw.depth = this.Conf.Switch.DEPTH;
            // 位置
            sw.rect.x = centerOffset + (switchOffset * idx);
            sw.rect.y = 0;
            idx++;
        }
        return idx;
    }
    ;
    //
    // スイッチレイアウト処理（横一列分）
    //
    MSF.MsfCanvas.prototype.LayoutSwitch = function(height, SwitchList) {
        var switchOffset = this.Conf.Switch.WIDTH + this.Conf.Switch.MARGIN;
        for (var i = 0; i < SwitchList.length; i++) {
            var sw = SwitchList[i];
            // サイズ
            sw.rect.width = this.Conf.Switch.WIDTH;
            sw.rect.height = this.Conf.Switch.HEIGHT;
            sw.depth = this.Conf.Switch.DEPTH;
            // 位置
            sw.rect.x = switchOffset * i;
            sw.rect.y = height;
        }
    }
    ;
    //
    // フォーカスクリア処理
    //
    MSF.MsfCanvas.prototype.clearFocus = function() {
        this.Focus.Slice = null;
        this.Focus.Cluster = null;
        this.Focus.Switch = null;
        this.Focus.Port = null;
    }
    ;
    //
    // フォーカス更新処理
    //
    MSF.MsfCanvas.prototype.updateFocus = function() {
        if (this.Focus.Slice) {
            // フォーカスしていたスライスが表示対象でなくなった場合はフォーカスクリアする
            var hit = false;
            for (var i = 0; i < this.SliceList.length; i++) {
                var si = this.SliceList[i];
                if (si.isVisible && si.sliceType == this.Focus.Slice.sliceType && si.id == this.Focus.Slice.id) {
                    this.Focus.Slice = si;
                    hit = true;
                    break;
                }
            }
            if (!hit) {
                this.clearFocus();
                this.Focus.TrafficLineList = null;
                this.Focus.TrafficSlice = null;
            }
        }
    }
    ;
    //
    // DisplayMode切替時に選択していたSiwtch、Portの選択を引き継ぐ
    //
    MSF.MsfCanvas.prototype.syncFocusedDevice = function() {

        if (this.DispMode == this.DeviceMode && this.Focus.Port !== null) {
            // 現状DeviceModeの場合は、選択Portと同じSwitchを選択する
            this.Focus.Switch = this.Focus.Port;
            this.Focus.Port = null;
        } else if (this.DispMode == this.ClusterMode && this.Focus.Switch !== null) {
            // 現状ClusterModeの場合は、選択Switchと同じPortを選択する
            this.Focus.Port = this.Focus.Switch;
            this.Focus.Switch = null;
        }
    }
    ;
    //
    // 描画処理
    //
    MSF.MsfCanvas.prototype.Draw = function(mPos) {
    
        // 表示対象クラスタ未選択(MapView)であれば処理必要なし
        var clusterId = this.viewClusterId;
        if (!clusterId) {
            return;
        }

        // マウスでクリックされた場合はフォーカスターゲットを初期化
        if (mPos) {
            this.clearFocus();
            // マウス座標のスケーリング
            var sPos = Point(mPos.x / this.scale, mPos.y / this.scale);
        }
        // コンテキスト初期化
        var ctx = this.mainCanvas.getContext("2d");
        ctx.clearRect(0, 0, this.mainCanvas.width, this.mainCanvas.height);

        // デバッグ時のみtrue
        if (false) {
            // 【デバッグ用】グリッド線を描画
            ctx.save();
            ctx.strokeStyle = "lightgrey";
            ctx.lineWidth = 0.5;
            var xMax = 1000;
            var yMax = 1000;
            // 縦線
            for (var x = 0; x <= xMax; x += 50) {
                if (x === 0)
                    continue;
                ctx.beginPath();
                ctx.moveTo(x, 0);
                ctx.lineTo(x, yMax);
                ctx.stroke();
            }
            // 縦線
            for (var y = 0; y <= yMax; y += 50) {
                if (y === 0)
                    continue;
                ctx.beginPath();
                ctx.moveTo(0, y);
                ctx.lineTo(xMax, y);
                ctx.stroke();
            }
            ctx.restore();
        }
        ctx.save();
        try {
            ctx.beginPath();
            // スケーリング
            ctx.scale(this.scale, this.scale);
            // スライスリスト
            for (var i = 0; i < this.SliceList.length; i++) {
                var si = this.SliceList[i];
                if (!si.isVisible) {
                    continue;
                }
                // スライス判定
                if (sPos) {
                    //
                    // クリック判定用外接矩形計算
                    var rec = Rect();
                    rec.x = si.rect.x;
                    rec.y = si.rect.y;
                    rec.width = si.rect.width + si.rect.height;
                    rec.height = si.rect.height;
                    if (this.hitTestCube(null, sPos, rec, Point(si.rect.height, si.rect.height) )) {
                        this.Focus.Slice = si;
                    }
                }
                ctx.save();
                ctx.beginPath();
                // スライス描画の原点は左上とする
                ctx.translate(si.rect.x, si.rect.y);

                if (sPos) {
                    // マウス座標の原点同期
                    var tPos = Point(sPos.x - si.rect.x, sPos.y - si.rect.y);
                }
                // スライス描画
                this.DrawSlice(ctx, si, tPos, this.Conf.Slice);
                ctx.restore();
            }

        } finally {
            ctx.restore();
        }
        // トラヒック情報描画
        this.DrawTraffic(sPos);
    }
    ;
    //
    // スライス描画処理
    //
    MSF.MsfCanvas.prototype.DrawSlice = function(ctx, sliceInfo, mPos, conf) {
        var si = sliceInfo;
        // スライスフレーム描画
        this.DrawSliceFrame(ctx, si, mPos, conf);
        // クラスターリスト描画
        for (var i = 0; i < si.ClusterList.length; i++) {
            var cl = si.ClusterList[i];
            ctx.save();
            ctx.beginPath();
            // 原点を立方体底辺左手前とする
            ctx.translate(cl.rect.x, cl.rect.y);

            if (mPos) {
                // マウス座標の原点同期
                var tPos = Point(mPos.x - cl.rect.x, mPos.y - cl.rect.y);
            }
            // デバイスモード描画
            this.DrawDevice(ctx, si, cl, tPos);

            ctx.restore();
        }
    }
    ;
    //
    // デバイス描画処理
    //
    MSF.MsfCanvas.prototype.DrawDevice = function(ctx, sliceInfo, clusterInfo, mPos) {
        var si = sliceInfo;
        var cl = clusterInfo;
        // スライド量計算
        var slide = GetSlide(cl.depth);
        ctx.save();
        //
        // マウスクリックからの呼び出しの場合
        if (mPos) {
            //
            // クリック判定用外接矩形計算
            var rec = Rect();
            rec.x = 0;
            rec.y = -cl.rect.height - slide.y;
            rec.width = cl.rect.width + slide.x;
            rec.height = cl.rect.height + slide.y;
            // ヒットテスト実行
            if (this.hitTestCube(null, mPos, rec, slide)) {
                this.Focus.Cluster = cl;
            }
        }
        ctx.beginPath();
        // デバイス内の原点を立方体底辺左奥とする
        ctx.translate(slide.x, -slide.y);

        
        if (mPos) {
            // マウス座標の原点同期
            var tPos = Point(mPos.x - slide.x, mPos.y + slide.y);
        }
        // スイッチ描画(Spine)
        this.DrawSwitchList(ctx, si, cl, cl.SpineList, tPos);
        // スイッチ描画(Leaf)
        this.DrawSwitchList(ctx, si, cl, cl.LeafList, tPos);
        // 非アクティブ接続線描画
        var key, line, spine, leaf;
        for (key in cl.LineDic) {
            line = cl.LineDic[key];
            if (line) {
                spine = cl.SpineDic[line.Spine];
                leaf = cl.LeafDic[line.Leaf];
                this.DrawConnectionLine(ctx, spine, leaf, false, this.Conf.Line);
            }
        }
        // アクティブ接続線描画
        //for (key in cl.LineDic) {
        //    line = cl.LineDic[key];
        //    if (line) {
        //        spine = cl.SpineDic[line.Spine];
        //        leaf = cl.LeafDic[line.Leaf];
        //        this.DrawConnectionLine(ctx, spine, leaf, true, this.Conf.Line);
        //    }
        //}
        ctx.restore();
    }
    ;
    //
    // スイッチリスト描画処理
    //
    MSF.MsfCanvas.prototype.DrawSwitchList = function(ctx, sliceInfo, clusterInfo, SwitchList, mPos) {
        var si = sliceInfo;
        var cl = clusterInfo;
        for (var i = 0; i < SwitchList.length; i++) {
            var sw = SwitchList[i];
            ctx.save();
            ctx.beginPath();
            var slide = GetSlide(sw.rect.y);
            // 原点を立方体底辺左手前とする
            ctx.translate(sw.rect.x - slide.x, slide.y);
            if (mPos) {
                // マウス座標の原点同期
                var tPos = Point(mPos.x - (sw.rect.x - slide.x), mPos.y - slide.y);
            }
            // スイッチ描画
            this.DrawSwitch(ctx, si, cl, sw, tPos, this.Conf.Switch);
            ctx.restore();
        }
    }
    ;
    //
    // スライスフレーム描画処理
    //
    MSF.MsfCanvas.prototype.DrawSliceFrame = function(ctx, sliceInfo, mPos, conf) {
        var si = sliceInfo;
        // スライド量計算
        var slide = GetSlide(si.rect.height);
        // 原点は左上
        ctx.save();
        ctx.beginPath();
        if (si.isLogical) {
            // L2 または L3 の設定を選択
            conf = conf["Logical_" + si.vpnType];
            sliceInfo = getTargetSliceInfo(MSF.main.menuSliceDic[si.sliceType], si.id);
            conf.STROKE_COLOR = sliceInfo.color;
            conf.Font.FILL_COLOR = sliceInfo.color;
            conf.FILL_COLOR = increase_brightness(sliceInfo.color, 90);
        } else {
            conf = conf.Physical;
        }

        if (this.Focus.Slice !== null && si.sliceType == this.Focus.Slice.sliceType && this.Focus.Slice.id == si.id) {
            ctx.lineWidth = conf.Active.STROKE_WIDTH;
        } else {
            ctx.lineWidth = conf.STROKE_WIDTH;
        }
        // 影を設定
        ctx.shadowColor = conf.SHADOW_COLOR;
        ctx.shadowBlur = conf.SHADOW_BLUR;
        ctx.shadowOffsetX = conf.SHADOW_OFFSET_X;
        ctx.shadowOffsetY = conf.SHADOW_OFFSET_Y;
        // 丸角平行四辺形描画処理
        this.DrawParallelogramRound(ctx, Size(si.rect.width, si.rect.height), conf);
        // スライス名描画処理
        ctx.shadowColor = "transparent";
        this.DrawSliceName(ctx, si, conf.Font);

        // スライス障害アイコン描画処理
        if (si.isLogical && this.isSliceFailure(si.sliceType, si.id)) {
            this.DrawSliceFailureIcon(ctx);
        }
        ctx.restore();
    }
    ;
    //
    // ポートリスト描画処理
    //
    MSF.MsfCanvas.prototype.DrawPortList = function(ctx, sliceInfo, clusterInfo, SwitchList, mPos, conf) {
        var si = sliceInfo;
        var cl = clusterInfo;
        for (var i = 0; i < SwitchList.length; i++) {
            var sw = SwitchList[i];
            var active = false;
            ctx.save();
            // 原点を各ポート底辺左とする　オフセット込み
            ctx.translate(sw.rect.x + this.Conf.Cluster.PADDING, 0);
            if (mPos) {
                var tPos = Point(mPos.x - (sw.rect.x + this.Conf.Cluster.PADDING), mPos.y);
                var rec = Rect(0, 0, conf.WIDTH, -conf.HEIGHT);
                active = containRectangle(tPos, rec);
                if (active) {
                    this.Focus.Port = sw;
                }
            }
            active = false;
            if (this.Focus.Slice !== null &&
                si.sliceType == this.Focus.Slice.sliceType &&
                this.Focus.Slice.id == si.id) {
                if (this.Focus.Cluster !== null && this.Focus.Cluster.id == cl.id) {
                    active = (this.Focus.Port !== null &&
                        this.Focus.Port.id == sw.id &&
                        this.Focus.Port.isLeaf == sw.isLeaf);
                }
            }
            // ポート描画
            this.DrawPort(ctx, active, conf);
            var size = Size(conf.WIDTH, conf.HEIGHT);
            //
            // CP髭描画
            this.DrawCpBeard(ctx, si, sw, size, this.Conf.Cluster.ConnectionPoint);

            // ポート名描画
            this.DrawPortName(ctx, sw.caption, size, active, conf.Font);
            ctx.restore();
        }
    }
    ;
    //
    // ポート描画処理
    //
    MSF.MsfCanvas.prototype.DrawPort = function(ctx, active, conf) {
        if (active) {
            ctx.strokeStyle = conf.Active.STROKE_COLOR;
            ctx.lineWidth = conf.Active.STROKE_WIDTH;
        } else {
            ctx.strokeStyle = conf.STROKE_COLOR;
            ctx.lineWidth = conf.STROKE_WIDTH;
        }
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(0, -conf.HEIGHT);
        ctx.lineTo(conf.WIDTH, -conf.HEIGHT);
        ctx.lineTo(conf.WIDTH, 0);
        if (active) {
            ctx.closePath();
        }
        ctx.stroke();
    }
    ;
    //
    // スイッチ描画処理
    //
    MSF.MsfCanvas.prototype.DrawSwitch = function(ctx, sliceInfo, clusterInfo, switchInfo, mPos, conf) {
        var si = sliceInfo;
        var cl = clusterInfo;
        var sw = switchInfo;
        // スライド量計算
        var slide = GetSlide(sw.depth);
        ctx.save();
        //
        // マウスクリックからの呼び出しの場合
        if (mPos) {
            //
            // クリック判定用外接矩形計算
            var rec = Rect();
            rec.x = 0;
            rec.y = -sw.rect.height - slide.y;
            rec.width = sw.rect.width + slide.x;
            rec.height = sw.rect.height + slide.y;
            // ヒットテスト実行
            if (this.hitTestCube(null, mPos, rec, slide)) {
                this.Focus.Switch = sw;
            }
        }
        var active = false;
        if (this.Focus.Slice !== null && si.sliceType == this.Focus.Slice.sliceType && this.Focus.Slice.id == si.id) {
            if (this.Focus.Cluster !== null && this.Focus.Cluster.id == cl.id) {
                active = (this.Focus.Switch !== null && this.Focus.Switch.id == sw.id && this.Focus.Switch.isLeaf == sw.isLeaf);
            }
        }
        var swConf;
        if (si.isLogical) {
            swConf = conf.Logical;
        } else {
            swConf = conf.Physical;
        }
        
        var failure = false;
        // Node障害発生判定 (キューブ枠線を赤色で着色)
        if (!si.isLogical) {
            failure = this.isNodeFailure(sw);
        }
        //
        // キューブ描画
        this.DrawCube(ctx, active, sw.rect.width, sw.rect.height, slide, swConf, failure);
        
        //
        // 文字列描画処理
        this.DrawSwitchTypeName(ctx, sw, slide, swConf.Font);
        //
        // CP髭描画
        this.DrawCpBeard(ctx, si, sw, Size(sw.rect.width, sw.rect.height),  this.Conf.Device.ConnectionPoint);
        
        // アイコン描画判定
        var isDrawSwitchIcon = false;
        if (sw.status == "before-setting" ||
            sw.status == "ztp-complete" ||
            sw.status == "node-resetting-complete") {
            isDrawSwitchIcon = true;
        }
        //
        // アイコン描画
        if (!si.isLogical && isDrawSwitchIcon) {
            this.DrawSwitchIcon(ctx, sw, slide, swConf);
        }
        // IF障害発生判定 (警告アイコン配置)
        if (!si.isLogical && this.isIfFailure(sw)) {
            this.DrawSwitchFailureIcon(ctx, sw, slide, swConf);
        }
        ctx.restore();
    }
    ;
    //
    // CP髭描画処理
    //
    MSF.MsfCanvas.prototype.DrawCpBeard = function(ctx, sliceInfo, switchInfo, size, conf) {
        var si = sliceInfo;
        var sw = switchInfo;
        // Spineとリストなしは除外
        var sliceKey = si.sliceType + si.id;
        var CPList = sw.CPDic[sliceKey];
        if (!sw.isLeaf || !CPList) {
            return;
        }

        // 上限設定 コンフィグで０以下の場合は上限なし
        var countOffset = 1;
        var count = Math.min(CPList.length, conf.MAX_CP_DISP_COUNT) + countOffset;
        if (conf.MAX_CP_DISP_COUNT < 0 ){
            count = CPList.length + countOffset;
        }

        var width = size.width / count;
        // CPの何れかにエラーがあった場合は表面に異常色を塗る
        var isError = false;

        ctx.beginPath();
        for (var i = countOffset; i< count; i++) {
            // 斜めの髭描画
            ctx.moveTo(width * i, - size.height / 2);
            ctx.lineTo(width * i - conf.LENGTH, conf.LENGTH - size.height / 2);
        }
        ctx.strokeStyle = conf.STROKE_COLOR;
        ctx.lineWidth = conf.STROKE_WIDTH;
        if (isError) {
            ctx.fillStyle = conf.ERROR_FILL_COLOR;
            // 枠分縮小
            var deflate = 2;
            // 表面塗り
            ctx.rect(0 + deflate, 0 - deflate, size.width-deflate * 2, - size.height + deflate * 2);
            ctx.fill();
        }
        ctx.stroke();
    }
    ;
    //
    // CP髭描画処理
    //
    MSF.MsfCanvas.prototype.DrawTrafficCpBeard = function(ctx, sliceInfo, switchInfo, size, conf) {
        var si = sliceInfo;
        var sw = switchInfo;
        // Spineとリストなしは除外
        var sliceKey = si.sliceType + si.id;
        var CPList = sw.CPDic[sliceKey];
        if (!sw.isLeaf || !CPList) {
            return;
        }

        // 上限設定 コンフィグで０以下の場合は上限なし
        var countOffset = 1;
        var count = Math.min(CPList.length, conf.MAX_CP_DISP_COUNT) + countOffset;
        if (conf.MAX_CP_DISP_COUNT < 0 ){
            count = CPList.length + countOffset;
        }

        var width = size.width / count;

        ctx.beginPath();
        for (var i = countOffset; i< count; i++) {
            var trafficValue = CPList[i-countOffset];

            // 斜めの髭描画(上り)
            ctx.beginPath();
            var x1 = width * i;
            var y1 = - size.height / 2;
            var x2 = width * i - conf.LENGTH;
            var y2 = conf.LENGTH - size.height / 2;
            ctx.moveTo(x1, y1);
            ctx.strokeStyle = getTrafficColor(trafficValue.send_rate);
            ctx.lineWidth = "2";//conf.STROKE_WIDTH;
            ctx.setLineDash(this.moveLineInfoList[0].dashList);
            ctx.lineTo((x1+x2)/2, (y1+y2)/2);
            ctx.stroke();

            // 斜めの髭描画(下り)
            ctx.beginPath();
            ctx.moveTo(x2, y2);
            ctx.strokeStyle = getTrafficColor(trafficValue.receive_rate);
            ctx.setLineDash(this.moveLineInfoList[0].dashList);
            ctx.lineWidth = "2";//conf.STROKE_WIDTH;
            ctx.lineTo((x1+x2)/2, (y1+y2)/2);
            ctx.stroke();
        }
        ctx.stroke();
    }
    ;
    //
    // キューブ描画処理
    //
    MSF.MsfCanvas.prototype.DrawCube = function(ctx, active, width, height, slide, conf, failure) {
        ctx.lineCap = "round";
        if (active) {
            //             conf = conf.Active;
            ctx.strokeStyle = conf.Active.STROKE_COLOR;
            ctx.fillStyle = conf.Active.FILL_COLOR;
            //         ctx.setLineDash(conf.LINE_DASH_STYLE);
            ctx.lineWidth = conf.Active.STROKE_WIDTH;
        } else {
            ctx.strokeStyle = conf.STROKE_COLOR;
            ctx.fillStyle = conf.FILL_COLOR;
            ctx.lineWidth = conf.STROKE_WIDTH;
        }
        if (failure) {
            ctx.strokeStyle = "red";
        }
        // フォーカスターゲット描画
        // 右側面
        ctx.save();
        ctx.beginPath();
        ctx.translate(width, -height);
        // 平行四辺形描画
        this.DrawParallelogram(ctx, slide.x, height, "up");
        ctx.restore();
        // 前面
        ctx.save();
        ctx.beginPath();
        ctx.translate(0, -height);
        ctx.rect(0, 0, width, height);
        ctx.fill();
        ctx.stroke();
        ctx.restore();
        // 上面
        ctx.save();
        ctx.beginPath();
        ctx.translate(slide.x, -height - slide.y);
        if (active) {
            // 平行四辺形描画
            this.DrawParallelogram(ctx, width, slide.y, "left", {
                stroke: false
            });
            ctx.beginPath();
            ctx.moveTo(width, 0);
            ctx.lineTo(0, 0);
            ctx.lineTo(-slide.x, slide.y);
            ctx.stroke();
        } else {
            // 平行四辺形描画
            this.DrawParallelogram(ctx, width, slide.y, "left");
        }
        ctx.restore();
    }
    ;
    //
    // Spine-Leaf接続線描画処理
    //
    MSF.MsfCanvas.prototype.DrawConnectionLine = function(ctx, Spine, Leaf, Alert, conf) {
        var st = Point();
        var ed = Point();
        if (Spine) {
            var A = Spine.rect;
            // 接続開始点
            st.x = A.x;
            st.x += A.width / 2;
            st.y = A.y;
        }
        if (Leaf) {
            var B = Leaf.rect;
            // スイッチのY座標はDepthなのでスライド量を計算する
            var slide = GetSlide(B.y - Leaf.depth);
            // 接続終了点 スイッチのX座標からスライド量を引き、幅の中心分足す
            ed.x = B.x - slide.x + B.width / 2;
            // スライド量から高さを引く
            ed.y = slide.y - B.height;
        }
        // どちらかが不明の場合の処理 縦線をちょこっと引いておく
        if (!Spine) {
            st.x = ed.x;
            st.y = ed.y - B.height;
        }
        if (!Leaf) {
            ed.x = st.x;
            ed.y = st.y + A.height;
        }
        ctx.beginPath();
        ctx.moveTo(st.x, st.y);
        if (Alert) {
            ctx.strokeStyle = conf.Active.STROKE_COLOR;
            ctx.lineWidth = conf.Active.STROKE_WIDTH;
        } else {
            ctx.strokeStyle = conf.STROKE_COLOR;
            ctx.lineWidth = conf.STROKE_WIDTH;
        }
        ctx.lineTo(ed.x, ed.y);
        ctx.stroke();
    }
    ;
    //
    // Spine-Leaf接続線描画処理
    //
    MSF.MsfCanvas.prototype.DrawTrafficConnectionLine = function(ctx, Spine, Leaf, Alert, conf, traffic) {
        var st = Point();
        var ed = Point();
        var md = Point(); // 中間点
        if (Spine) {
            var A = Spine.rect;
            // 接続開始点
            st.x = A.x;
            st.x += A.width / 2;
            st.y = A.y;
        }
        if (Leaf) {
            var B = Leaf.rect;
            // スイッチのY座標はDepthなのでスライド量を計算する
            var slide = GetSlide(B.y - Leaf.depth);
            // 接続終了点 スイッチのX座標からスライド量を引き、幅の中心分足す
            ed.x = B.x - slide.x + B.width / 2;
            // スライド量から高さを引く
            ed.y = slide.y - B.height;
        }
        if (true) {
            md.x = (st.x + ed.x) / 2;
            md.y = (st.y + ed.y) / 2;
        }
        // どちらかが不明の場合の処理 縦線をちょこっと引いておく
        if (!Spine) {
            st.x = ed.x;
            st.y = ed.y - B.height;
        }
        if (!Leaf) {
            ed.x = st.x;
            ed.y = st.y + A.height;
        }
        
        // トラヒック値取得,設定
        ctx.beginPath();
        ctx.moveTo(st.x, st.y);
        ctx.strokeStyle = getTrafficColor(traffic.send_rate);
        //ctx.lineWidth = conf.Active.STROKE_WIDTH;
        ctx.lineWidth = 2;
        ctx.setLineDash(this.moveLineInfoList[0].dashList);
        ctx.lineTo(md.x, md.y);
        ctx.stroke();
        
        ctx.beginPath();
        ctx.moveTo(ed.x, ed.y);
        ctx.strokeStyle = getTrafficColor(traffic.receive_rate);
        //ctx.lineWidth = conf.Active.STROKE_WIDTH;
        ctx.lineWidth = 2;
        ctx.setLineDash(this.moveLineInfoList[0].dashList);
        ctx.lineTo(md.x, md.y);
        ctx.stroke();
        
    }
    ;
    //
    // 丸角平行四辺形描画処理
    //
    MSF.MsfCanvas.prototype.DrawParallelogramRound = function(ctx, size, conf) {
        var x = size.height;
        var y = 0;
        var width = size.width;
        var height = size.height;
        var radius = conf.RADIUS;
        ctx.save();
        ctx.beginPath();
        // 45度固定
        ctx.transform(1, 0, -1, 1, 0, 0);
        ctx.translate(x, y);
        ctx.strokeStyle = conf.STROKE_COLOR;
        ctx.fillStyle = conf.FILL_COLOR;
        // 上辺の直線から描画を開始
        // コーナーの曲線を考慮して円の半径分右よりから開始する
        ctx.moveTo(radius, 0);
        // 上辺の終点、コーナーの曲線を考慮して円の半径分左よりになる
        ctx.lineTo(width - radius, 0);
        // 右上コーナーの描画：
        //   - arcのx,y座標は円の中心
        //   - startAngle, endAngleはラジアンの単位,
        //   - Math.PIで180度,Math.PI*2でちょうど一周
        //   - 最後のパラメタは反時計回りに描画するかどうか、
        //     この場合時計回りに描画するのでfalse
        ctx.arc(width - radius, radius, radius, Math.PI * 1.5, 0, false);
        //  あと上記と同様、時計回りに描画を行っていく
        ctx.lineTo(width, height - radius);
        ctx.arc(width - radius, height - radius, radius, 0, Math.PI * 0.5, false);
        ctx.lineTo(radius, height);
        ctx.arc(radius, height - radius, radius, Math.PI * 0.5, Math.PI, false);
        ctx.lineTo(0, radius);
        ctx.arc(radius, radius, radius, Math.PI, Math.PI * 1.5, false);
        ctx.closePath();
        ctx.fill();
        // 線は影なし(無条件)
        ctx.shadowColor = "transparent";
        ctx.stroke();
        ctx.restore();
    }
    ;
    //
    // 平行四辺形描画処理
    //
    MSF.MsfCanvas.prototype.DrawParallelogram = function(ctx, width, height, slideDirection, option) {
        var stroke = true;
        if (option) {
            var dash = option.dash;
            if (typeof (option.stroke) !== "undefined") {
                stroke = option.stroke;
            }
        }
        ctx.save();
        ctx.beginPath();
        // 45度固定
        if (slideDirection == "left") {
            ctx.transform(1, 0, -1, 1, 0, 0);
        }
        if (slideDirection == "up") {
            ctx.transform(1, -1, 0, 1, 0, 0);
        }
        ctx.rect(0, 0, width, height);
        ctx.fill();
        if (dash) {
            ctx.setLineDash(dash);
        }
        if (stroke) {
            ctx.stroke();
        }
        ctx.restore();
    }
    ;
    //
    // スライス名描画処理
    //
    MSF.MsfCanvas.prototype.DrawSliceName = function(ctx, sliceInfo, conf) {
        var si = sliceInfo;
        ctx.save();
        ctx.beginPath();
        ctx.lineWidth = conf.STROKE_WIDTH;
        ctx.strokeStyle = conf.STROKE_COLOR;
        ctx.font = conf.NAME;
        ctx.fillStyle = conf.FILL_COLOR;
        // 文字描画位置計算
        var tx = si.rect.height / 2;
        var ty = si.rect.height / 2;
        ctx.textAlign = "center";
        ctx.translate(tx, ty);
        ctx.rotate(Rad(-this.deg));
        ctx.textBaseline = "middle";
        var offsetY = -3;
        ctx.strokeText(si.name, 0, offsetY);
        ctx.fillText(si.name, 0, offsetY);
        ctx.restore();
    }
    ;
    //
    // スイッチタイプ名描画処理
    //
    MSF.MsfCanvas.prototype.DrawSwitchTypeName = function(ctx, switchInfo, slide, conf) {
        var sw = switchInfo;
        // 文字列描画処理
        ctx.font = conf.NAME;
        // 文字描画位置計算
        ctx.textAlign = "center";
        var tx = (sw.rect.width + slide.x) / 2;
        var ty = -sw.rect.height + (-slide.y / 2) + 3;
        if (this.Focus.Switch == sw) {
            ctx.fillStyle = conf.Active.FILL_COLOR;
            ctx.fillText(sw.caption, tx, ty);
        } else {
            ctx.lineWidth = conf.STROKE_WIDTH;
            ctx.strokeStyle = conf.STROKE_COLOR;
            ctx.fillStyle = conf.FILL_COLOR;
            //ctx.strokeText(sw.caption, tx, ty);
            ctx.fillText(sw.caption, tx, ty);
        }
    }
    ;
    //
    // スイッチアイコン描画処理
    //
    MSF.MsfCanvas.prototype.DrawSwitchIcon = function(ctx, sw, slide, conf) {
        ctx.beginPath();
        ctx.arc(10, -27, 8, 0, Math.PI * 2, false);
        ctx.fillStyle = "white";
        ctx.fill();
        ctx.lineWidth = 1.5;
        ctx.strokeStyle = "grey";
        //"#4DA6FF";//"#0080FF";
        ctx.stroke();
        ctx.font = "bold 15px helvetica";
        //helvetica";//arial black";
        ctx.fillStyle = "grey";
        //"#4DA6FF";
        ctx.fillText("i", 10, -22);
    }
    ;
    //
    // スイッチ障害アイコン描画処理
    //
    MSF.MsfCanvas.prototype.DrawSwitchFailureIcon = function(ctx, sw, slide, conf) {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            // 障害通知OFFなら処理SKIP
            return;
        }
        ctx.beginPath();
        ctx.moveTo(50, -11);
        ctx.lineTo(40, 7);
        ctx.lineTo(60, 7);
        ctx.closePath();

        ctx.fillStyle = "yellow";
        ctx.fill();
        ctx.lineWidth = 1.5;
        ctx.strokeStyle = "grey";
        //"#4DA6FF";//"#0080FF";
        ctx.stroke();
        ctx.font = "bold 12px helvetica";
        //helvetica";//arial black";
        ctx.fillStyle = "grey";
        //"#4DA6FF";
        ctx.fillText("!", 50, 5);
    }
    ;
    //
    // スライス障害アイコン描画処理
    //
    MSF.MsfCanvas.prototype.DrawSliceFailureIcon = function(ctx) {
        ctx.beginPath();
        ctx.moveTo(25, 129);
        ctx.lineTo(15, 147);
        ctx.lineTo(35, 147);
        ctx.closePath();

        ctx.fillStyle = "yellow";
        ctx.fill();
        ctx.lineWidth = 1.5;
        ctx.strokeStyle = "grey";
        //"#4DA6FF";//"#0080FF";
        ctx.stroke();
        ctx.font = "bold 12px helvetica";
        //helvetica";//arial black";
        ctx.fillStyle = "grey";
        //"#4DA6FF";
        ctx.fillText("!", 23, 145);
    }
    ;
    //
    // ポート名描画処理
    //
    MSF.MsfCanvas.prototype.DrawPortName = function(ctx, word, size, focus, conf) {
        // 文字列描画処理
        ctx.font = conf.NAME;
        // 文字描画位置計算
        ctx.textAlign = "center";
        var tx = size.width / 2;
        var ty = -size.height - 4;
        if (focus) {
            ctx.fillStyle = conf.Active.FILL_COLOR;
            ctx.fillText(word, tx, ty);
        } else {
            ctx.lineWidth = conf.STROKE_WIDTH;
            ctx.strokeStyle = conf.STROKE_COLOR;
            ctx.fillStyle = conf.FILL_COLOR;
            //ctx.strokeText(word, tx, ty);
            ctx.fillText(word, tx, ty);
        }
    }
    ;
    //
    // トラヒック描画処理
    //
    MSF.MsfCanvas.prototype.DrawTraffic = function(mPos) {
        // コンテキスト初期化
        var ctx = this.animationCanvas.getContext("2d");
        ctx.save();
        try {
            ctx.beginPath();
            // 透過レイヤーのスケーリング
            ctx.scale(this.scale, this.scale);

            var conf = this.Conf.Traffic;
            // 描画領域クリア
            ctx.clearRect(0, 0, this.animationCanvas.width/this.scale, this.animationCanvas.height/this.scale);
            if (!this.animationTimer) {
                return;
            }
            // 選択スライスリスト
            for (var i = 0; i < this.SliceList.length; i++) {
                var si = this.SliceList[i];
                if (!si.isVisible) {
                    continue;
                }
                ctx.save();
                ctx.beginPath();
                // スライス描画の原点は左上とする
                ctx.translate(si.rect.x, si.rect.y);
                
                // スライスのトラヒック描画
                this.DrawSliceTraffic(ctx, mPos, si, conf);
                ctx.restore();
            }

        } finally {
            ctx.restore();
        }
    }
    ;
    //
    // スライスのトラヒック描画処理
    //
    MSF.MsfCanvas.prototype.DrawSliceTraffic = function(ctx, mPos, sliceInfo, conf) {
        var si = sliceInfo;

        // 全トラヒック描画
        var TrafficList = si.TrafficList;

        // クラスターリスト描画
        for (var i = 0; i < si.ClusterList.length; i++) {
            var cl = si.ClusterList[i];
            ctx.save();
            ctx.beginPath();
            // 原点を立方体底辺左手前とする
            ctx.translate(cl.rect.x, cl.rect.y);

            if (si.isLogical) {
                // 物理描画
                this.DrawTrafficSlice(ctx, si, cl);
            } else {
                // スライス描画
                this.DrawTrafficDevice(ctx, si, cl);
            }
            ctx.restore();
        }
    }
    ;
    //
    // デバイストラヒック描画処理
    //
    MSF.MsfCanvas.prototype.DrawTrafficDevice = function(ctx, sliceInfo, clusterInfo) {
        var si = sliceInfo;
        var cl = clusterInfo;
        // スライド量計算
        var slide = GetSlide(cl.depth);
        ctx.save();

        ctx.beginPath();
        // デバイス内の原点を立方体底辺左奥とする
        ctx.translate(slide.x, -slide.y);

        for (var key in cl.LineDic) {
            var line = cl.LineDic[key];
            if (line) {
                var spine = cl.SpineDic[line.Spine];
                var leaf = cl.LeafDic[line.Leaf];
                var clusterTraffic = MSF.main.db.SpineLeafTraffic[this.viewClusterId] || {};
                var traffic = clusterTraffic[key] || {};
                this.DrawTrafficConnectionLine(ctx, spine, leaf, true, this.Conf.Line, traffic);
            }
        }
        ctx.restore();
    }
    ;
    //
    // デバイストラヒック描画処理
    //
    MSF.MsfCanvas.prototype.DrawTrafficSlice = function(ctx, sliceInfo, clusterInfo) {
        var si = sliceInfo;
        var cl = clusterInfo;
        // スライド量計算
        var slide = GetSlide(cl.depth);
        ctx.save();

        ctx.beginPath();
        // デバイス内の原点を立方体底辺左奥とする
        ctx.translate(slide.x, -slide.y);

        var swList = cl.SpineList.concat(cl.LeafList);
        for (var i = 0; i < swList.length; i++) {
            var sw = swList[i];
            ctx.save();
            ctx.beginPath();
            slide = GetSlide(sw.rect.y);
            // 原点を立方体底辺左手前とする
            ctx.translate(sw.rect.x - slide.x, slide.y);

            // スイッチの髭描画
            this.DrawTrafficCpBeard(ctx, si, sw, Size(sw.rect.width, sw.rect.height),  this.Conf.Device.ConnectionPoint);
            ctx.restore();
        }

        ctx.restore();
    }
    ;
    //
    // 破線設定処理（線が動いているように見せる処理）
    //
    MSF.MsfCanvas.prototype.SetMoveLineDash = function(moveLineInfo) {
        var ml = moveLineInfo;
        var len = ml.dash_on + ml.dash_off;
        var rate = ml.strokeSpeed * this.SpeedRate / 10;
        var step = parseInt((ml.len_count * rate) % len, 10);
        ml.dashList[0] = Math.max(ml.dash_on + step - len, 0);
        ml.dashList[1] = Math.min(ml.dash_off, step);
        ml.dashList[2] = Math.min(ml.dash_on, len - step);
        ml.dashList[3] = Math.max(ml.dash_off - step, 0);
        ml.len_count++;
        if (ml.len_count >= len * 1000) {
            ml.len_count = 0;
        }
    }
    ;
    //
    // 破線設定処理（線が動いているように見せる処理）キャンバス管理バージョン
    //
    MSF.MsfCanvas.prototype.SetMoveLineDash2 = function(moveLineInfo) {
        var ml = moveLineInfo;
        var step = 0;
        if (ml.load > 0) {
            var count = new Date().getTime() % ml.load;
            step = parseInt((count * ml.rate) % ml.len, 10);
        }
        ml.dashList[0] = Math.max(ml.dash_on + step - ml.len, 0);
        ml.dashList[1] = Math.min(ml.dash_off, step);
        ml.dashList[2] = Math.min(ml.dash_on, ml.len - step);
        ml.dashList[3] = Math.max(ml.dash_off - step, 0);
    }
    ;
    //
    // キューブ用ヒットテスト
    // ctx : デバック用 基本はnullを指定する
    // 判定エリアを確認したいときにctxを指定する
    //
    MSF.MsfCanvas.prototype.hitTestCube = function(ctx, point, rect, slide) {

        // デバック用
        if (ctx) {
            ctx.beginPath();
            ctx.rect(rect.x, rect.y, rect.width, rect.height);
            ctx.stroke();
        }

        // スイッチの外接矩形にヒットするか
        if (containRectangle(point, rect)) {
            //
            // クリック判定用外接矩形計算
            // 外接矩形から左上の３D表現部を計算
            var triA = [];
            triA.push(Point(rect.x, rect.y));
            triA.push(Point(rect.x + slide.x, rect.y));
            triA.push(Point(rect.x, rect.y + slide.y));

            // 外接矩形から左上の３D表現部を除外する
            if (containTriangle(ctx, point, triA)) {
                return false;
            }

            //
            // 外接矩形から右下の３D表現部を計算
            var triB = [];
            triB.push(Point(rect.x + rect.width, rect.y + rect.height));
            triB.push(Point(rect.x + rect.width - slide.x, rect.y + rect.height));
            triB.push(Point(rect.x + rect.width, rect.y + rect.height - slide.y));

            // 外接矩形から右下の３D表現部を除外する
            if (containTriangle(ctx, point, triB)) {
                return false;
            }
            return true;
        }
        return false;
    }
    ;
    //
    // 矩形の当たり判定
    //
    function containRectangle(point, rect) {
        var hit = true;
        if (rect.width > 0) {
            hit &= (rect.x <= point.x);
            hit &= (rect.x + rect.width >= point.x);
        } else {
            hit &= (rect.x >= point.x);
            hit &= (rect.x + rect.width <= point.x);
        }
        if (rect.height > 0) {
            hit &= (rect.y <= point.y);
            hit &= (rect.y + rect.height >= point.y);
        } else {
            hit &= (rect.y >= point.y);
            hit &= (rect.y + rect.height <= point.y);
        }
        return hit;
    }
    //
    // 三角形と点の当たり判定
    // ctx : デバック用 基本はnullを指定する
    // 判定エリアを確認したいときにctxを指定する
    //
    function containTriangle(ctx, point, triList) {
        var P = point;
        var A = triList[0];
        var B = triList[1];
        var C = triList[2];

        // デバック用
        if (ctx) {
            ctx.beginPath();
            ctx.moveTo(A.x,A.y);
            ctx.lineTo(B.x,B.y);
            ctx.lineTo(C.x,C.y);
            ctx.closePath();
            ctx.stroke();
        }

        //線上は外とみなします。
        //ABCが三角形かどうかのチェックは省略...
        var AB = Sub_vector(B, A);
        var BP = Sub_vector(P, B);
        var BC = Sub_vector(C, B);
        var CP = Sub_vector(P, C);
        var CA = Sub_vector(A, C);
        var AP = Sub_vector(P, A);
        //外積    Z成分だけ計算すればよいです
        var c1 = AB.x * BP.y - AB.y * BP.x;
        var c2 = BC.x * CP.y - BC.y * CP.x;
        var c3 = CA.x * AP.y - CA.y * AP.x;
        if ((c1 > 0 && c2 > 0 && c3 > 0) || (c1 < 0 && c2 < 0 && c3 < 0)) {
            //三角形の内側に点がある
            return true;
        }
        //三角形の外側に点がある
        return false;
    }
    //
    // 疑似３D描画用のスライド比率計算
    //
    function GetSlideRatio(degX, degY) {
        var tanX = Math.tan(Rad(degX / 2));
        var tanY = Math.tan(Rad(degY / 2));
        return Point(tanX, tanY);
    }
    //
    // スライド量計算
    //
    function GetSlide(len) {
        var slideX = len * MSF.main.mf.msfCanvas.slide.x;
        var slideY = len * MSF.main.mf.msfCanvas.slide.y;
        return Point(slideX, slideY);
    }
    //
    // 矩形表現
    //
    function Rect(x, y, width, height) {
        return {
            x: x,
            y: y,
            width: width,
            height: height
        };
    }
    //
    // サイズ表現
    //
    function Size(width, height) {
        return {
            width: width,
            height: height
        };
    }
    //
    // 座標表現
    //
    function Point(x, y) {
        return {
            x: x,
            y: y
        };
    }
    //
    //ベクトル引き算(a-b)
    //
    function Sub_vector(a, b) {
        return {
            x: a.x - b.x,
            y: a.y - b.y
        };
    }
    //
    // 角度からラジアン計算
    //
    function Rad(angle) {
        return angle * Math.PI / 180;
    }
    //
    // 符号判定
    //
    function sign(n) {
        return n ? n < 0 ? -1 : 1 : 0;
    }
    //
    // ベジエ曲線上の任意の点を取得する
    //
    function getPointOnBezier(points, t) {
        var b0 = Math.pow(1 - t, 3);
        var b1 = t * Math.pow(1 - t, 2) * 3;
        var b2 = (1 - t) * Math.pow(t, 2) * 3;
        var b3 = Math.pow(t, 3);
        var pt = Point();
        pt.x = points[0].x * b0 + points[1].x * b1 + points[2].x * b2 + points[3].x * b3;
        pt.y = points[0].y * b0 + points[1].y * b1 + points[2].y * b2 + points[3].y * b3;
        return pt;
    }
    //
    // ベジエ曲線上の任意の点の接線の傾きを取得する
    //
    function getSlopeOfTangentLine(points, t) {
        var bd0 = 3 * Math.pow(1 - t, 2);
        var bd1 = 6 * (1 - t) * t;
        var bd2 = 3 * Math.pow(t, 2);
        var x = bd0 * (points[1].x - points[0].x) + bd1 * (points[2].x - points[1].x) + bd2 * (points[3].x - points[2].x);
        var y = bd0 * (points[1].y - points[0].y) + bd1 * (points[2].y - points[1].y) + bd2 * (points[3].y - points[2].y);
        return y / x;
    }
    //
    // ベジエ曲線を直線に分割する
    //
    function calculateDividingPoints(bezierInfo) {
        var bi = bezierInfo;
        var points = [];
        var i;
        for (i = 0.1; i <= 1.0; i += 0.1) {
            divideCurveRecursively(bi, points, i - 0.1, i);
        }
        points.push(bi.points[bi.points.length - 1]);
        bi.dividingPoints = points;
    }
    //
    // ベジエ曲線上の任意の点の接線の傾きを取得する
    //
    function divideCurveRecursively(bi, points, t1, t2) {
        var prevD = getSlopeOfTangentLine(bi.points, t1);
        var currentD = getSlopeOfTangentLine(bi.points, t2);
        var dDiff = Math.abs(Math.abs(prevD) - Math.abs(currentD));
        var mDiff = t2 - t1;
        var middleT = t1 + mDiff / 2;
        if (mDiff > 0.05 && (sign(prevD) !== sign(currentD) || dDiff > 0.2)) {
            divideCurveRecursively(bi, points, t1, middleT);
            divideCurveRecursively(bi, points, middleT, t2);
        } else {
            points.push(getPointOnBezier(bi.points, t1));
        }
    }
    //
    // 任意の点が２点間の線上に存在するかをチェックする
    //
    function onStraightLine(pt, bezierInfo, HIT_ACCURACY) {
        var bi = bezierInfo;
        if (!bi.dividingPoints) {
            return false;
        }
        for (var i = 1; i < bi.dividingPoints.length; i++) {
            var p1 = bi.dividingPoints[i - 1];
            var p2 = bi.dividingPoints[i];
            var trueDistance = calculateDistance(p1, p2);
            var testDistance1 = calculateDistance(p1, pt);
            var testDistance2 = calculateDistance(pt, p2);
            if (Math.abs(trueDistance - (testDistance1 + testDistance2)) < HIT_ACCURACY) {
                return true;
            }
        }
        return false;
    }
    //
    // ２点間の距離を計算する
    //
    function calculateDistance(pt1, pt2) {
        var c = Math.pow(pt1.x - pt2.x, 2) + Math.pow(pt1.y - pt2.y, 2);
        return Math.sqrt(c);
    }

    //
    // スライスの障害発生有無を判定する
    // スライス配下の装置/IFが1件以上の障害であれば障害(true)とする
    //
    MSF.MsfCanvas.prototype.isSliceFailure = function(sliceType, sliceId) {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            // 障害通知OFFなら処理SKIP
            return false;
        }
        
        var failureStatus = this.failureStatusSlice[sliceType];
        if (!failureStatus || !failureStatus[sliceId]) {
            // データ未取得
            return false;
        }
        if (failureStatus[sliceId].failure_status == "down") {
            return true;
        }
        return false;
    }
    ;

    //
    // 装置の障害発生有無を判定する
    //
    MSF.MsfCanvas.prototype.isNodeFailure = function(sw) {
        if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
            // 障害通知OFFなら処理SKIP
            return false;
        }
        var clusterId = this.viewClusterId;
        var failureStatus = this.failureStatus[clusterId];

        if (!failureStatus) {
            // データ未取得
            return false;
        }
        var fabricType;
        if (sw.isLeaf) {
            fabricType = MSF.Const.FabricType.Leafs;
        } else {
            fabricType = MSF.Const.FabricType.Spines;
        }
        var nodes = failureStatus.nodes[fabricType];
        if (nodes && nodes.indexOf(sw.id) != -1) {
            return true;
        }
        return false;
    }
    ;

    //
    // 装置配下IFの障害発生有無を判定する
    //
    MSF.MsfCanvas.prototype.isIfFailure = function(sw) {
        var clusterId = this.viewClusterId;
        var failureStatus = this.failureStatus[clusterId];

        if (!failureStatus) {
            // データ未取得
            return false;
        }
        var fabricType;
        if (sw.isLeaf) {
            fabricType = MSF.Const.FabricType.Leafs;
        } else {
            fabricType = MSF.Const.FabricType.Spines;
        }

        var allIfs = failureStatus.ifs[fabricType][sw.id];
        for (var ifType in allIfs) {
            var ifs = allIfs[ifType];
            if (ifs.length > 0) {
                return true;
            }
        }
        return false;
    }
    ;
    
    //
    // ファブリックNWモードで表示するクラスタのIDを設定
    //
    MSF.MsfCanvas.prototype.setViewClusterId = function(clusterId) {
        this.viewClusterId = clusterId;
    }
    ;
})();
