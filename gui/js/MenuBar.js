// タブ選択状態のCSSクラス名
const cssTabOn = "tabOn";
// タブ切替アニメーション時間(ms)
const tabSpeed = 300;

// タブのdiv id
const tabNetworkMode = "#tab1";
const tabCtrllerMode = "#tab2";
// ビューのdiv id
const divNetworkMode = "#networkView";
const divCtrllerMode = "#controllerView";
const divViewMultiCls = "#multiClusterView";
const divViewFablicNW = "#figure";
const divViewNodeView = "#nodeView";
// ナビゲーション「Map > Cluster > Equipment」のdiv id
const divNaviMultiCls = "#navi1";
const divNaviFablicNW = "#navi2";
const divNaviNodeView = "#navi3";
// メニューバーのid
const ulMenuBar = "#top-menu";
const divMenuBar = "#menubar";
const divDumyBar = "#dummyBar";
// 詳細画面表示 ＜|＞ > ボタンのid
const btnSlideR = "#slideRight";
const btnSlideL = "#slideLeft";
// 詳細画面表示 ＜|＞ > ボタンのCSS名
const cssSlideDisable = "disableSlide";
// 詳細画面のid
const divDetailArea = "#detail";
// 詳細画面の幅分だけ、メニューバー幅を調整するためのダミー領域
const divNaviSpacer = "#naviSpacer";

// メニュー(prmenu.js)の初期化パラメータ
const prmenuProps = {
    "fontsize": "0.98em",
    "height": "32",
    "case": "capitalize",
    "linkbgcolor": "#DCE6F2",
    "linktextcolor": "#618dc3",
    "linktextweight": "400",
    "hoverdark": false
};

// メニューアイテムの最大数
const prmenuCountMax = 5;

// 詳細画面の最小幅 (px)
const sizeSlideMimimunWidth = 5;
// 詳細画面の標準幅 (300px)
const sizeSlideDefaultWidth = parseInt($("#detailArea").css("width"), 10);
// 詳細画面の最大表示時の左マージン(px)
const sizeSlideLeftOffset = 0 +
    parseInt($("#menu").css("width"), 10) +
    parseInt($("#menu").css("margin-right"), 10) +
    parseInt($("#menu").css("margin-left" ), 10) +
    parseInt($("body" ).css("margin-left" ), 10);



$(document).ready(function() {

    // タブ1押下時：Network Mode
    $(tabNetworkMode).click(function() {
        // タブ表示切り替え
        $(tabNetworkMode).addClass(cssTabOn);
        $(tabCtrllerMode).removeClass(cssTabOn);
        // Controller Mode 非表示
        $(divCtrllerMode).hide("slide", {direction: "right"}, tabSpeed);
        $(divDumyBar).hide("slide", {direction: "right"}, tabSpeed);
        // Network Mode 表示
        $(divNetworkMode).show("slide", {direction: "left"}, tabSpeed);
        $(divMenuBar).show("slide", {direction: "left"}, tabSpeed);
        $(divNaviMultiCls).show();
        $(divNaviFablicNW).show();
        $(divNaviNodeView).show();
        $("#naviSpacerBg").fadeIn(tabSpeed*2);
        showFigure();
    });

    // タブ2押下時：Controller Mode
    $(tabCtrllerMode).click(function() {
        // タブ表示切り替え
        $(tabCtrllerMode).addClass(cssTabOn);
        $(tabNetworkMode).removeClass(cssTabOn);
        // Network Mode 非表示
        $(divNaviMultiCls).hide();
        $(divNaviFablicNW).hide();
        $(divNaviNodeView).hide();
        $("#naviSpacerBg").hide();
        $(divMenuBar).hide("slide", {direction: "left"}, tabSpeed);
        $(divNetworkMode).hide("slide", {direction: "left"}, tabSpeed);
        // Controller Mode 表示
        $(divDumyBar).show("slide", {direction: "right"}, tabSpeed);
        $(divCtrllerMode).show("slide", {direction: "right"}, tabSpeed);

        // コントローラ表示切替
        showControllerView();
        showFigure(true);
    });

    // タブの初期化：デフォルト「Network Mode」選択
    $(tabNetworkMode).addClass(cssTabOn);

    // メニューの初期化. Jquery.prmenu.js 利用
    $(ulMenuBar).prmenu(prmenuProps);

    // 画面表示の初期化：デフォルト「NetworkMode/MultiCluster」表示
    $(divNetworkMode).show();
    $(divViewMultiCls).show();
    $(divViewFablicNW).hide();
    $(divViewNodeView).hide();
    $(divCtrllerMode).hide();

    // 詳細画面スライドパネルを閉じる
    $(btnSlideR).click(function(event) {
        console.log(btnSlideR);
        var detailWidth = $(divDetailArea).css("width");
        switch (Math.round(parseFloat(detailWidth))) {
        case sizeSlideMimimunWidth:
            // 最小表示中(何もしない)
            break;
        case sizeSlideDefaultWidth:
            // 標準サイズ→最小表示
            $(divDetailArea).animate({"width": sizeSlideMimimunWidth + "px"}, "fast");
            $(divNaviSpacer).hide();
            $(divMenuBar).css("width", "100%");
            $(divDumyBar).css("width", "100%");
            $(btnSlideR).addClass(cssSlideDisable);
            $(btnSlideL).removeClass(cssSlideDisable);
            break;
        default:
            // 最大サイズ→標準サイズ
            $(divDetailArea).animate({"width": sizeSlideDefaultWidth + "px"}, "fast");
            $(btnSlideR).removeClass(cssSlideDisable);
            $(btnSlideL).removeClass(cssSlideDisable);
            break;
        }
    });

    // 詳細画面スライドパネルを開く
    $(btnSlideL).click(function(event) {
        console.log(btnSlideL);
        var detailWidth = $(divDetailArea).css("width");
        switch (Math.round(parseFloat(detailWidth))) {
        case sizeSlideMimimunWidth:
            // 標準サイズ←最小サイズ
            $(divDetailArea).animate({"width": sizeSlideDefaultWidth + "px"}, "fast");
            $(divNaviSpacer).show();
            $(divMenuBar).css("width", "100%");
            $(divDumyBar).css("width", "100%");
            $(btnSlideR).removeClass(cssSlideDisable);
            $(btnSlideL).removeClass(cssSlideDisable);
            break;
        case sizeSlideDefaultWidth:
            // 最大サイズ←標準サイズ
            var w = parseInt($(window).width(), 10);
            $(divDetailArea).animate({"width": (w - sizeSlideLeftOffset) + "px"}, "fast");
            $(btnSlideR).removeClass(cssSlideDisable);
            $(btnSlideL).addClass(cssSlideDisable);
            break;
        default:
            // 最大表示中(何もしない)
            break;
        }
    });

    // メニューバーダイアログのOKボタン
    $("#menubarCommonOk").click(function() {

        // L2 or L3 Sliceのとき選択状態を連携する
        var isL2Slice = $("#SliceCountL2_dialog").css("display");
        var isL3Slice = $("#SliceCountL3_dialog").css("display");
        if (isL2Slice != "none" || isL3Slice != "none") {
            var type = (isL2Slice!="none") ? "L2" : "L3";
            selectSliceListSort(type);
        }
        $("#maskLayer").fadeOut(250);
        $(".overlayPanel").css("display", "none");
    });

});

function selectSliceListSort(type) {
    var checkedIds = [];
    var uncheckedIds = [];
    $("#menu"+type+"SliceAll li").each(function() {
        var name = $(this).attr("id");
        var isVisible = $("#selectedSlice"+name+"_dialog").css("display");
        if (isVisible != "none") {
            checkedIds.push(name);
        } else {
            uncheckedIds.push(name);
        }
    });
    checkedIds.forEach(function(name) {
        var id = name.slice(5);
        if (type == "L2") {
            showL2slice(id, name, type, true);
        } else {
            showL3slice(id, name, type, true);
        }
    });
    uncheckedIds.forEach(function(name) {
        var id = name.slice(5);
        if (type == "L2") {
            showL2slice(id, name, type, false);
        } else {
            showL3slice(id, name, type, false);
        }
    });

    // 選択されているメニューで昇順ソート
    var clusterId = MSF.main.can.viewClusterId;
    updateSliceMenu(clusterId);
}

// NetworkMode/MultiCluster画面を表示.
// メニューバーからcallされる.
function showMapView() {
    $(divNetworkMode).show();
    $(divCtrllerMode).hide();
    $(divViewMultiCls).show();
    $(divViewFablicNW).hide();
    $(divViewNodeView).hide();
    // メニュー
    $("#menuEquipmentRoot").addClass("menuDisabled");
    $("#menuEquipment").addClass("menuDisabled");
    $("#menuL2SliceRoot").removeClass("menuDisabled");
    $("#menuL3SliceRoot").removeClass("menuDisabled");
    $("#menuL2Slice").removeClass("menuDisabled");
    $("#menuL3Slice").removeClass("menuDisabled");
    // ナビゲーション
    $(divNaviFablicNW).html("");
    $(divNaviNodeView).html("");
    $(divNaviMultiCls).show();
    $(divNaviFablicNW).hide();
    $(divNaviNodeView).hide();
    removeNodeMenu();

    // スライスメニュー更新 全スライス表示
    updateSliceMenu();

    // 各Viewの各種選択状態をクリア
    svg.unSelectedAll();
    nodeView.unSelectedAll();
    controllerView.unSelectedAll();

    // FabricNWViewの選択状態をクリア
    MSF.main.can.setViewClusterId();
    MSF.main.can.clearFocus();

    // 詳細部更新処理
    MSF.main.updateDetailTable();
}

// NetworkMode/FabricNetwork画面を表示.
// メニューバーからcallされる.
function showFabricView(id) {
    $(divNetworkMode).show();
    $(divCtrllerMode).hide();
    $(divViewMultiCls).hide();
    $(divViewFablicNW).show();
    $(divViewNodeView).hide();
    // メニュー
    $("#menuEquipmentRoot").removeClass("menuDisabled");
    $("#menuEquipment").removeClass("menuDisabled");
    $("#menuL2SliceRoot").removeClass("menuDisabled");
    $("#menuL3SliceRoot").removeClass("menuDisabled");
    $("#menuL2Slice").removeClass("menuDisabled");
    $("#menuL3Slice").removeClass("menuDisabled");
    // ナビゲーション
    $(divNaviFablicNW).html("&gt; Cluster#" + id);
    $(divNaviNodeView).html("");
    $(divNaviMultiCls).show();
    $(divNaviFablicNW).show();
    $(divNaviNodeView).hide();

    // 装置選択メニュー更新
    removeNodeMenu();
    updateNodeMenu(id);

    // スライスメニュー更新
    updateSliceMenu(id);

    // 各Viewの各種選択状態をクリア
    svg.unSelectedAll();
    nodeView.unSelectedAll();
    controllerView.unSelectedAll();

    // ファブリックNW描画
    MSF.main.can.setViewClusterId(id);
    MSF.main.can.setInfo(MSF.main.db);
    // レイアウト計算処理:キャンバスサイズ設定初期化
    MSF.main.mf.setCanvasBounds();
    // レイアウト計算
    MSF.main.mf.calcLayout();
    // 描画処理
    MSF.main.can.Draw();

    $("#menubarCommonOk").click();
    // 詳細部更新処理
    MSF.main.updateDetailTable();
}

// NetworkMode/Node画面を表示.
// メニューバーからcallされる.
function showNodeView(clusterId, deviceType, nodeId, equipmentTypeId) {
    $(divNetworkMode).show();
    $(divCtrllerMode).hide();
    $(divViewMultiCls).hide();
    $(divViewFablicNW).hide();
    $(divViewNodeView).show();
    // メニュー
    $("#menuEquipmentRoot").removeClass("menuDisabled");
    $("#menuEquipment").removeClass("menuDisabled");
    $("#menuL2SliceRoot").addClass("menuDisabled");
    $("#menuL3SliceRoot").addClass("menuDisabled");
    $("#menuL2Slice").addClass("menuDisabled");
    $("#menuL3Slice").addClass("menuDisabled");

    var equipmentTypeDic = MSF.main.db.EquipmentTypeDic;
    var equipmentType = equipmentTypeDic[equipmentTypeId];
    if (!equipmentType) {
        MSF.console.warn("equipmentType Not Found. equipmentTypeId: " + equipmentTypeId);
        return;
    }

    // 選択された装置(Spine#1, Leaf#1...)から装置を特定
    var info = MSF.ModelInfo[equipmentType.platform];
    if (info) {
       nodeView.init(equipmentType.platform, clusterId, deviceType, nodeId);
    } else {
       nodeView.init("QFX5100-48S(EL)", clusterId, deviceType, nodeId);
    }

    // 装置内IFの状態更新
    updateNodeIF(clusterId, deviceType, nodeId);
    // 障害状態の更新
    failureNodeIf(clusterId, deviceType, nodeId);

    // ナビゲーション

    var name = (deviceType == MSF.Const.FabricType.Spines) ? "Spine" : "Leaf";
    name = name + "#" + nodeId;
    $(divNaviNodeView).html("&gt; " + name);
    $(divNaviMultiCls).show();
    $(divNaviFablicNW).show();
    $(divNaviNodeView).show();

    // 各Viewの各種選択状態をクリア
    svg.unSelectedAll();
    nodeView.unSelectedAll();
    controllerView.unSelectedAll();
    
    // FabricNWViewの選択状態をクリア
    MSF.main.can.clearFocus();
    
    $("#menubarCommonOk").click();
    // 詳細部更新処理
    MSF.main.updateDetailTable();
}

function updateNodeIF(clusterId, deviceType, nodeId) {
    var clusterInfo = MSF.main.db.clusterInfoDic[clusterId];
    var interfaceInfo = clusterInfo.InterfacesInfo;
    var nodes = interfaceInfo[deviceType];
    var ifs = nodes[nodeId];

    var physicalIfs = ifs.physical_ifs;
    physicalIfs.forEach(function(p) {
        var type = "physical";
        if (!p.speed) {
            // 速度未設定は"unused"
            type = "unused";
        }
        var ifId = p.physical_if_id;
        nodeView.setSlotType(ifId, type);
    });
    var breakoutIfs = ifs.breakout_ifs;
    breakoutIfs.forEach(function(b) {
        var ifId = b.base_if.physical_if_id;
        nodeView.setSlotType(ifId, "breakout");
    });
    var lagIfs = ifs.lag_ifs;
    lagIfs.forEach(function(l) {
        var ifIds = [];
        if (l.physical_if_ids.length > 0) {
            ifIds = l.physical_if_ids;
        // } else {
        // BreakoutIFでLAGが構成されていた場合は表示不可
        }

        ifIds.forEach(function(ifId) {
            nodeView.setSlotType(ifId, "lag");
        });
    });
}

function failureNodeIf(clusterId, deviceType, nodeId) {
    if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
        // 障害通知OFFなら処理SKIP
        return;
    }
    var failureStatus = MSF.main.db.FailureInfoDic[clusterId];
    if (!failureStatus) {
        return;
    }
    var nodes = failureStatus.nodes[deviceType];
    if (nodes.indexOf(nodeId) == -1) {
        nodeView.setError(false);
    } else {
        nodeView.setError(true);
    }

    var allIfs = failureStatus.ifs[deviceType][nodeId];
    if (!allIfs) {
        return;
    }

    var clusterInfo = MSF.main.db.clusterInfoDic[clusterId];
    var interfaceInfo = clusterInfo.InterfacesInfo;
    var nodes = interfaceInfo[deviceType];
    var ifs = nodes[nodeId];
    
    nodeView.clearIfStatus();
    for (var ifType in allIfs) {
        if (ifType == "lag") {
            // Lagのメンバリンクの物理IFの障害情報で設定するためSKIP
            continue;
        }
        var ifsType = allIfs[ifType];
        ifsType.forEach(function(id) {
            var physicalIf = getBaseIfId(ifs[ifType+"_ifs"], ifType, id);
            physicalIf ? nodeView.setIfStatus("error", [physicalIf]) : null;
        });
    }
}

// コントローラ画面を表示
// メニューバーからcallされる
function showControllerView() {
    // コントローラ画面への表示切替時に必要な処理有ればここに追加。
    svg.unSelectedAll();
    nodeView.unSelectedAll();
    controllerView.unSelectedAll();
}

// MapViewでのスライス障害判定
function setL2SliceFailure(failureInfo) {
    if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
        // 障害通知OFFなら処理SKIP
        return false;
    }

    // 全クラスタの障害を解除後、設定
    svg.sliceL2s.forEach(function(slice) {
        slice.setStatus("normal");
        if (!failureInfo[slice.id]) {
            // 障害情報未取得：処理SKIP
            return;
        }
        if (failureInfo[slice.id].isFailure) {
            slice.setStatus("error");
        }
    });
}

// MapViewでのスライス障害判定
function setL3SliceFailure(failureInfo) {
    if (MSF.Conf.System.Debug.NOT_NOTIFY_FAILURE_DIALOG) {
        // 障害通知OFFなら処理SKIP
        return false;
    }

    svg.sliceL3s.forEach(function(slice) {
        slice.setStatus("normal");
        if (!failureInfo[slice.id]) {
            // 障害情報未取得：処理SKIP
            return;
        }
        if (failureInfo[slice.id].isFailure) {
            slice.setStatus("error");
        }
    });
}

// MapViewでのクラスタ障害判定
function setClusterFailure(failureInfo) {

    // 全クラスタの障害を解除後、設定
    svg.clusters.forEach(function(cluster) {
        cluster.setChildStatus("normal");

        if (!failureInfo[cluster.id]) {
            // 障害情報未取得：処理SKIP
            return;
        }
        if (failureInfo[cluster.id].isFailure) {
            cluster.setChildStatus("error");
        }
    });
}

// L2スライス表示(メニューからcallされる).
function showL2slice(id, name, vpnType, isShow) {
    var sliceType = MSF.Const.SliceType.L2;
    var target = svg.getL2SliceById(String(id));
    if (isShow == null) {
        var isVisible = $("#selectedSlice"+name+"_dialog").css("display");
        isShow = (isVisible == "none") ? true : false;
    }
    showSlice(id, name, vpnType, sliceType, target, isShow);
}

// L3スライス表示(メニューからcallされる).
function showL3slice(id, name, vpnType, isShow) {
    var sliceType = MSF.Const.SliceType.L3;
    var target = svg.getL3SliceById(String(id));
    if (isShow == null) {
        var isVisible = $("#selectedSlice"+name+"_dialog").css("display");
        isShow = (isVisible == "none") ? true : false;
    }
    showSlice(id, name, vpnType, sliceType, target, isShow);
}
// L2/L3スライス表示.
function showSlice(id, name, vpnType, sliceType, target, isShow) {

    var sliceDic = getTargetSliceInfo(MSF.main.menuSliceDic[sliceType], id);
    if (isShow) {
        if (target != null) {
            target.show();
        }
        $("#selectedSlice"+name+"_menu").show();
        $("#selectedSlice"+name+"_dialog").show();

        // メニュー管理辞書連動
        sliceDic.isChecked = true;

    } else {
        if (target != null) {
            target.hide();
        }
        $("#selectedSlice"+name+"_menu").hide();
        $("#selectedSlice"+name+"_dialog").hide();
        
        // メニュー管理辞書連動
        sliceDic.isChecked = false;
    }
    
    MSF.main.mf.updateSelectedSlice();
}


// L2スライス一覧表示ダイアログ中のメニュー選択時処理
function selectL2slice(id, name, vpnType) {
    var count = 0;
    //var target = svg.getL2SliceById(String(id));
    var sliceType = MSF.Const.SliceType.L2;
    selectSlice(id, name, vpnType, sliceType);
}
// L3スライス一覧表示ダイアログ中のメニュー選択時処理
function selectL3slice(id, name, vpnType) {
    var count = 0;
    //var target = svg.getL3SliceById(String(id));
    var sliceType = MSF.Const.SliceType.L3;
    selectSlice(id, name, vpnType, sliceType);
}
// L2/L3選択ダイアログからの選択制御(L2/L3選択ダイアログメニューからcallされる).
function selectSlice(id, name, vpnType, sliceType) {
    var count = 0;
    var isVisible = $("#selectedSlice"+name+"_dialog").css("display");
    if (isVisible == "none") {
        $("#selectedSlice"+name+"_dialog").show();
    } else {
        $("#selectedSlice"+name+"_dialog").hide();
    }
    
    var checkedIds = [];
    var isOver = false;
    $("#menu" + vpnType + "SliceAll li").each(function() {
        var id = $(this).attr("id");
        var isVisible = $("#selectedSlice"+id+"_dialog").css("display");
        if (isVisible != "none") {
            checkedIds.push(id);
            if (++count > prmenuCountMax) {
                isOver = true;
                return false;
            }
        }
    });
    // 上限超過時は選択されたスライスの選択をOFF
    if (isOver) {
        var msg = i18nx("Main.menuBar.selectMsg", "Selectable Max");
        $("#menubarPanelMessage").text(msg + " ("+prmenuCountMax+")");
        checkedIds.some(function(v, i){
            if (v == name) checkedIds.splice(i, 1);
        });
        $("#selectedSlice"+name+"_dialog").hide();
    } else {
        $("#menubarPanelMessage").text("");
    }
}


// @see MSF.MsfCanvasFrame.prototype.updateSelectedSlice = function(checkedSliceIDList) {
// @see MSF.MSFMain.prototype.addSliceMenu = function(sliceType, sliceID) {
function addSliceMenu(id, name, type, color, cpCnt, isDialogUpdate) {
    var ulMenubar;
    var ulDialog;
    var counterType;
    var span1, span2, span3, cp, cpSpan, anchor;
    // 丸枠線の色(半透明)
    var red   = parseInt(color.substring(1, 3), 16);
    var green = parseInt(color.substring(3, 5), 16);
    var blue  = parseInt(color.substring(5, 7), 16);
    var rgba  = "rgba(" + red + "," + green + "," + blue + ",0.8)";

    if (type == "L2") {
        ulMenubar = $("#menuL2Slice");
        ulDialog = $("#menuL2SliceAll");
        counterType = "l2slice";
    } else {
        ulMenubar = $("#menuL3Slice");
        ulDialog = $("#menuL3SliceAll");
        counterType = "l3slice";
    }

    // スライス内のCP数情報
    cpCnt = cpCnt == undefined ? 0 : cpCnt;
    cpSpan = '<span class="cpNumber" style="border-color: ' + rgba + ';">' + cpCnt + "</span>";

    // メニューバーにアイテムを追加
    var curSize = ulMenubar.find("li").length;
    if (curSize < prmenuCountMax) {
        // 上限未満の場合、アイテム追加
        span1 = getSliceIconImage(color) + "&nbsp;";
        span2 = id;
        span3 = ' <span id="selectedSlice' + name + '_menu" style="display:none">&#x2714;</span>';
        // スライス内のCP数表示
        cp = '<span  class="cpCountMenu">' + cpSpan + '</span>';
        anchor = '<a class="menubarItem" href="javascript:show'+type+'slice(\''+id+'\',\''+name+'\',\''+type+'\');">' + span1 + span2 + span3 + cp + '</a>';
        $("<li style='white-space:nowrap;vertical-align: text-bottom;'>" + anchor + "</li>").appendTo(ulMenubar);
    } else if (curSize == prmenuCountMax) {
        // 上限の場合、ダイアログを開くを追加
        anchor = '<a class="menubarItem" href="javascript:showMenubarModalPanel(' + "'" + counterType + "'" + ');">...</a>';
        $("<li>"+anchor+"</li>").appendTo(ulMenubar);
    } else {
        // 何もしない
    }

    // ダイアログにアイテムを追加 (上限なし)
    if (isDialogUpdate) {
        span1 = getSliceIconImage(color) + "&nbsp;";
        span2 = id;
        span3 = '<span id="selectedSlice' + name + '_dialog" style="display:none">&#x2714;</span>';
        // スライス内のCP数表示
        cp = '<span  class="cpCountDialog">' + cpSpan + '</span>';
        anchor = '<a href="javascript:select'+type+'slice(\''+id+'\',\''+name+'\',\''+type+'\');">' + span1 + span2 + span3 + cp + "</a>";
        $("<li id=\""+name+"\" class=\"check\">" + anchor + "</li>").appendTo(ulDialog);
    }

    // リソース数(スライス数)を更新
    setMenuCount(counterType, ulDialog.find("li").length);
}

function removeNodeMenu() {
    $("#menuNode").empty();
    $("#menuNodeAll").empty();
    setMenuCount("node", 0);
}

// 装置メニュー追加
function addNodeMenu(name, clusterId, deviceType, nodeId, equipmentTypeId) {
    var ulMenubar = $("#menuNode");
    var ulDialog = $("#menuNodeAll");
    var anchor;

    // メニューバーにアイテムを追加
    var curSize = ulDialog.find("li").length;
    if (curSize < prmenuCountMax) {
        // 上限未満の場合、アイテム追加
        anchor = '<a class="menubarItem" href="javascript:showNodeView(';
        anchor = anchor + "'" + clusterId + "',";
        anchor = anchor + "'" + deviceType + "',";
        anchor = anchor + "'" + nodeId + "',";
        anchor = anchor + "'" + equipmentTypeId + "'";
        anchor = anchor + ');">' + name + "</a>";
        $("<li>"+anchor+"</li>").appendTo(ulMenubar);
    } else if (curSize == prmenuCountMax) {
        // 上限の場合、ダイアログを開くを追加
        anchor = '<a class="menubarItem" href="javascript:showMenubarModalPanel(' + "'node'" + ');">...</a>';
        $("<li>"+anchor+"</li>").appendTo(ulMenubar);
    } else {
        // 何もしない
    }

    // ダイアログにアイテムを追加 (上限なし)
    anchor = '<a href="javascript:showNodeView(';
    anchor = anchor + "'" + clusterId + "',";
    anchor = anchor + "'" + deviceType + "',";
    anchor = anchor + "'" + nodeId + "',";
    anchor = anchor + "'" + equipmentTypeId + "'";
    anchor = anchor + ');">' + name + "</a>";
    $("<li>"+anchor+"</li>").appendTo(ulDialog);

    // リソース数(装置数)を更新
    setMenuCount("node", ulDialog.find("li").length);
}

// スライスメニュー更新
function updateSliceMenu(clusterId) {
    var menuSliceDic = MSF.main.menuSliceDic;
    var sliceTypes = MSF.Const.VpnType;

    var isDialogVisible = $("#menubarCommonPanel").css("display");
    for (var vpnType in sliceTypes) {
        // リソースカウントを0リセット
        setMenuCount((vpnType == "L2") ? "l2slice" : "l3slice", 0);
        $("#menu" + vpnType + "Slice").empty();
        var isDialogUpdate = false;
        if (isDialogVisible == "none") {
            isDialogUpdate = true;
            $("#menu" + vpnType + "SliceAll").empty();
        }
        
        // 指定されたクラスタIDに所属するスライスで更新
        var sliceDicList = menuSliceDic[MSF.Const.SliceType[vpnType]];
        sliceDicList.sort(function(a, b) {
            if (a.isChecked == b.isChecked) {
                if (isNaN(a) || isNaN(b)) {
                    return (a.id > b.id) ? 1 : -1;
                } else {
                    return (parseInt(a.id, 10) > parseInt(b.id, 10)) ? 1 : -1;
                }
            }
            if (a.isChecked) {
                return -1;
            }
            if (b.isChecked) {
                return 1;
            }
        });

        for (var i = 0; i < sliceDicList.length; i++) {
            var sliceDic = sliceDicList[i];
            // 指定クラスタIDに属さないスライスは表示対象外
            // クラスタID未指定(Map View)、どのクラスタにも属していないスライスは表示対象
            if (clusterId != null && sliceDic.clusterIds.length !== 0 && sliceDic.clusterIds.indexOf(clusterId) == -1) {
                continue;
            }
            // 削除済みスライスは表示対象外
            if (!sliceDic.isActive) {
                continue;
            }
            var cpCount = sliceDic.cpCount;
            if (clusterId != null) {
                if (sliceDic.cpCountCluster[vpnType][clusterId] != null) {
                    cpCount = sliceDic.cpCountCluster[vpnType][clusterId][sliceDic.id];
                } else {
                    cpCount = 0;
                }
            }
            addSliceMenu(sliceDic.id, sliceDic.cpKey, vpnType, sliceDic.color, cpCount, isDialogUpdate);
            // 選択状態引き継ぎ
            if (sliceDic.isChecked) {
                $("#selectedSlice"+sliceDic.cpKey+"_menu").show();
                if (isDialogUpdate) {
                    $("#selectedSlice"+sliceDic.cpKey+"_dialog").show();
                }
            }
        }
    }
}

// 装置メニュー更新
function updateNodeMenu(clusterId) {

    var nodes = MSF.main.db.clusterInfoDic[clusterId].NodesInfo;
    var spines = nodes.spines;
    spines.sort(function(a, b) {
        return (parseInt(a.node_id, 10) > parseInt(b.node_id, 10)) ? 1 : -1;
    });
    var leafs = nodes.leafs;
    leafs.sort(function(a, b) {
        return (parseInt(a.node_id, 10) > parseInt(b.node_id, 10)) ? 1 : -1;
    });

    // 装置メニュー全削除
    removeNodeMenu();

    // Spine装置メニュー
    var nodeId, deviceType, node, name, equipmentTypeId;
    for (nodeId in nodes.spines) {
        deviceType = MSF.Const.FabricType.Spines;
        node = nodes.spines[nodeId];
        name = "Spine#" + node.node_id;
        equipmentTypeId = node.equipment_type_id;
        addNodeMenu(name, clusterId, deviceType, node.node_id, equipmentTypeId);
    }

    // Leaf装置メニュー
    for (nodeId in nodes.leafs) {
        deviceType = MSF.Const.FabricType.Leafs;
        node = nodes.leafs[nodeId];
        name = "Leaf#" + node.node_id;
        equipmentTypeId = node.equipment_type_id;
        addNodeMenu(name, clusterId, deviceType, node.node_id, equipmentTypeId);
    }
}


// クラスタメニュー追加
function addClusterMenu(clusterId) {
    var ulMenubar = $("#menuCluster");
    var ulDialog = $("#menuClusterAll");
    var anchor;

    // メニューバーにアイテムを追加
    var curSize = ulDialog.find("li").length;
    if (curSize < prmenuCountMax) {
        // 上限未満の場合、アイテム追加
        anchor = '<a class="menubarItem" href="javascript:showFabricView(';
        anchor = anchor + "'" + clusterId + "'";
        anchor = anchor + ');">Cluster#' + clusterId + "</a>";
        $("<li value=\"" + clusterId + "\">"+anchor+"</li>").appendTo(ulMenubar);
    } else if (curSize == prmenuCountMax) {
        // 上限の場合、ダイアログを開くを追加
        anchor = '<a class="menubarItem" href="javascript:showMenubarModalPanel('+"'cluster'"+');">...</a>';
        $("<li>"+anchor+"</li>").appendTo(ulMenubar);
    } else {
        // 何もしない
    }

    // ダイアログにアイテムを追加 (上限なし)
    anchor = '<a href="javascript:showFabricView(';
    anchor = anchor + "'" + clusterId + "'";
    anchor = anchor + ');">Cluster#' + clusterId + "</a>";
    $("<li value=\"" + clusterId + "\">"+anchor+"</li>").appendTo(ulDialog);

    // リソース数(スライス数)を更新
    setMenuCount("cluster", ulDialog.find("li").length);
}

// クラスタメニュー削除
function removeClusterMenu(clusterId) {
    $("#menuCluster li").eq(clusterId).remove();
    $("#menuClusterAll li").eq(clusterId).remove();
}

// クラスタメニュー更新
// クラスタ一覧からメニューを作成する
function updateClusterMenu(sw_clusters) {

    var ulMenubar = $("#menuCluster");
    var ulDialog = $("#menuClusterAll");

    ulMenubar.empty();
    ulDialog.empty();
    setMenuCount("cluster", 0);
    
    for (var i = 0; i < sw_clusters.length; i++) {
        var clusterId = sw_clusters[i];
        // クラスタメニュー更新(追加)
        addClusterMenu(clusterId);
    }
}


//
// リソース数の更新.
//   type: cluster/node/l2slice/l3slice 更新対象のspam要素
//   cnt: 更新値
//
function setMenuCount(type, cnt) {
    var spanId = null;
    switch(type) {
        case "cluster":
            $("#ClusterCount_menu").text(cnt);
            $("#ClusterCount_dialog").text(cnt);
            break;
        case "node":
            $("#NodeCount_menu").text(cnt);
            $("#NodeCount_dialog").text(cnt);
            break;
        case "l2slice":
            $("#SliceCountL2_menu").text(cnt);
            $("#SliceCountL2_dialog").text(cnt);
            break;
        case "l3slice":
            $("#SliceCountL3_menu").text(cnt);
            $("#SliceCountL3_dialog").text(cnt);
            break;
        default:
            return;
            break;
    }
}

//
// モーダルウィンドウ表示
//   panelId                : モーダルウィンドウとして表示するdiv要素のID
//   closeAtBackgroundClick : パネル以外の背景クリック時にパネルを閉じるかどうか？(boolean)
//
function showMenubarModalPanel(type) {

    var title;
    var list;

    $("#menuClusterAll").hide();
    $("#menuNodeAll").hide();
    $("#menuL2SliceAll").hide();
    $("#menuL3SliceAll").hide();

    $("#ClusterCount_dialog").hide();
    $("#NodeCount_dialog").hide();
    $("#SliceCountL2_dialog").hide();
    $("#SliceCountL3_dialog").hide();

    switch(type) {
        case "cluster":
            $("#menubarPanelTitle").html(i18nx("Main.menuBar.cluster", "Cluster"));
            $("#menuClusterAll").show();
            $("#ClusterCount_dialog").show();
            break;
        case "node":
            $("#menubarPanelTitle").html(i18nx("Main.menuBar.equipment", "Equipment"));
            $("#menuNodeAll").show();
            $("#NodeCount_dialog").show();
            break;
        case "l2slice":
            $("#menubarPanelTitle").html(i18nx("Main.menuBar.l2slice", "L2 Slice"));
            $("#menuL2SliceAll").show();
            $("#SliceCountL2_dialog").show();
            break;
        case "l3slice":
            $("#menubarPanelTitle").html(i18nx("Main.menuBar.l3slice", "L3 Slice"));
            $("#menuL3SliceAll").show();
            $("#SliceCountL3_dialog").show();
            break;
        default:
            return;
            break;
    }

    // サイズ計算
    var panelWidth = $("#menubarCommonPanel").outerWidth();

    // 背景を隠すレイヤーを表示
    if ($("#maskLayer").is(":visible") === false) {
        $("#maskLayer").css({ "display": "block", opacity: 0 });
        $("#maskLayer").fadeTo(250, 0.4);
    }

    // 背景クリック時にパネルを閉じるように設定
    $("#maskLayer").unbind("click");
    $("#maskLayer").click(function() {
        $("#maskLayer").fadeOut(250);
        $(".overlayPanel").css("display", "none");
    });

    // パネル自体を表示
    $("#menubarCommonPanel").css("display", "block");
    $("#menubarCommonPanel").css("left", "50%");
    $("#menubarCommonPanel").css("margin-left", -(panelWidth / 2) + "px");
    // fade効果を出すために透明度を元に戻す
    $("#menubarCommonPanel").css("opacity", 0);
    $("#menubarCommonPanel").fadeTo(250, 1);
    // パネルを表示する際先頭に移動する
    $(".panelBody").scrollTop(0);
}
//スライスメニュー項目に表示する雲のSVG画像アイコン.
function getSliceIconImage(color) {
    var str = '<svg width="14" height="14" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">';
    str = str + '<path stroke="none" fill="' + color + '" ';
    str = str + ' d="m11.35175,8.37536c-0.04256,-1.79611 -1.51102,-3.23894 -3.31672,-3.23986c-1.20717,0.00092 -2.26149,0.64613 -2.84082,1.60986c-0.27227,-0.14964 -0.58574,-0.23704 -0.91979,-0.23704c-1.04288,0.00092 -1.89129,0.82689 -1.92927,1.86063c-1.09505,0.35968 -1.88763,1.38425 -1.88854,2.60195c0,1.51559 1.22958,2.74564 2.74564,2.74564l0,-0.00092l7.2645,0c1.51559,0 2.74564,-1.23005 2.74564,-2.74564c-0.00183,-1.20717 -0.78068,-2.22717 -1.86063,-2.59463z"/>';
    str = str + '</svg>';
    return str;
}// Networkモードでの表示モードを記憶
var tmp_filters = [];

// 表示モードの選択で詳細部の表示を更新する
function showFigure(isController) {
    hideFigure();
    var filters = [];
    if (isController) {
        // コントローラ表示
        filters = ["#filter10"];
    } else {
        // ネットワーク表示
        filters = ["#filter1", "#filter2", "#filter3", "#filter4", "#filter5", "#filter6", "#filter7", "#filter8", "#filter9"];
    }
    filters.forEach(function(id) {
        $(id).show();
    });
}
// 展開状態の詳細テーブルを閉じる
function hideFigure() {
    var filters = ["#filter1", "#filter2", "#filter3", "#filter4", "#filter5", "#filter6", "#filter7", "#filter8", "#filter9", "#filter10"];

    filters.forEach(function(id) {
        if ($(id).hasClass("filterSelect")) {
            $(id).click();
        }
        $(id).hide();
    });
}
