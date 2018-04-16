//
// コントローラビュークラス.
//
//   init(ids[])                  初期化
//   unSelectedAll()              図選択の解除
//   getControllerById(type, id)  図の取得
//   multiSelected(ids)           図の複数選択
//   getSelectedControllers()     選択中図形の取得
//   addCluster(id)               クラスタの追加
//   delCluster(id)               クラスタの削除
//

const controllerMfcPrefix = "MFC";
const controllerFcPrefix = "FC#";
const controllerEcPrefix = "EC#";
const controllerEmPrefix = "EM#";

const controllerViewDivId = "#controllerCanvas";
const controllerViewCssSvgClass = "width-100-percent";
// SVG背景
const controllerViewBgRectAttr = {
    fill : "#fcfcfc",
    stroke : "none"
};
// 四角形の座標と属性(MFC)
const mfcRectX = 150;
const mfcRectY = 40;
const mfcRectWidth = 240;
const mfcRectHeight = 40;
// 四角形の座標と属性(FC)
const fcRectX = 40;
const fcRectY = 150;
const fcRectWidth = 120;
const fcRectHeight = mfcRectHeight;
// 四角形の座標と属性(EC)
const ecRectX = fcRectX;
const ecRectY = 250;
const ecRectWidth = fcRectWidth;
const ecRectHeight = fcRectHeight;
// 四角形の座標と属性(EM)
const emRectX = fcRectX;
const emRectY = 350;
const emRectWidth = fcRectWidth;
const emRectHeight = fcRectHeight;
// 四角形の間隔
const fcRectOffsetX = 150;
// 平行四辺形の座標
const nwPathX = 30;
const nwPathY = 480;
const nwPathHeight = 30;
// 平行四辺形の座標点(開始点以外)
const nwPathPoints = " l 30,-30 l 110,0 l -30,30 l -110,0z";
const nwPathAttr = {
    stroke: "none",
    fill: "#e0e0e0"
};
// コントローラ間の線属性
const controllerLineAttr = {
    stroke: "#c0c0c0",
    fill: "none"
};
// クラスタ枠の座標と属性
const controllerClusterX = 30;
const controllerClusterY = 125;
const controllerClusterWidth = 140;
const controllerClusterHeight = 370;
const controllerClusterAttr = {
    stroke: "#e0e0e0",
    "stroke-width": 2,
    "stroke-dasharray": "5,3",
    fill: "none",
    rx : "5",
    ry : "5"
};
// クラスタアイコンの座標と属性
const controllerHexgonPoint = "m40,0l-8.4,14.6l-16.8,0l-8.4,-14.6l8.4,-14.6l16.8,0l8.4,14.6z";
const controllerHexgonAttr = {
    stroke: "#e0e0e0",
    fill: "#ffffff"
};
// クラスタアイコンの座標と属性
const controllerLabelX = 53;
const controllerLabelY = 129;
const controllerLabelAttr = {
    stroke: "none",
    fill: "#c0c0c0",
    family: 'Helvetica',  // フォントファミリー
    size: 9,             // フォントサイズ
    anchor: 'middle'      // 配置:中央寄せ
};

(function() {
"use strict";

    //
    // コンストラクタ.
    //   w,h: キャンバスの幅・高さ
    //
    MSF.ControllerView = function(w, h) {

        // メンバ変数
        this.canvas = null; // SVGキャンバス
        this.lines = null;    // 図形レイヤ(svg group)
        this.backs = null;    // 図形レイヤ(svg group)
        this.nodes = null;    // 図形レイヤ(svg group)
        this.controllers = [];
        this.ids = [];

        this.warn1 = {};    // 警告アイコン△(svg path)
        this.warn2 = {};    // 警告アイコン！(svg path)
        this.select = "none"; // ノード図形選択有無

        // キャンバス描画 (htmlのdiv内に埋込SVGのDOM追加)
        this.canvas = new SVG(document.querySelector(controllerViewDivId)).size(w, h);
        this.canvas.attr({
            "width"     : "100%",
            "max-width" : w +"px",
            "height"    : "100%",
            "max-height": h + "px"
        });
        this.canvas.addClass(controllerViewCssSvgClass);
        // キャンバスの枠線
        var rect = this.canvas.rect(w, h).attr(controllerViewBgRectAttr);

        // キャンバスクリック時、他図形の選択解除
        var svgCanvas = this;
        rect.click(function() {
            svgCanvas.unSelectedAll();
            MSF.main.updateDetailControllerTable();
        });
        // 同タッチイベント
        rect.touchstart(function(evt) {
            evt.preventDefault();
            svgCanvas.unSelectedAll();
            MSF.main.updateDetailControllerTable();
        });

        // キャンバスに図形レイヤ<g>を追加
        this.lines = this.canvas.group();
        this.backs = this.canvas.group();
        this.nodes = this.canvas.group();
    }
    ;

    //
    // 初期化.
    //
    MSF.ControllerView.prototype.init = function(ids) {
        var view = this.nodes;
        var id, x, y, w, h;
        var x1, y1, x2, y2;
        var points;

        // 表示のクリア
        this.lines.clear();
        this.backs.clear();
        this.nodes.clear();
        this.controllers = [];
        this.ids = ids;
        var controller = this;

        // MFC
        if (!MSF.Conf.Rest.MFC.FC_SINGLE) {
            id = controllerMfcPrefix;
            x = mfcRectX;
            y = mfcRectY;
            w = mfcRectWidth;
            h = mfcRectHeight;
            controller.controllers.push(new MSF.Controller(id, x, y, w, h));
        }

        ids.forEach(function(obj, i) {

            // FC
            id = controllerFcPrefix + obj;
            x = fcRectX + fcRectOffsetX * i;
            y = fcRectY;
            w = fcRectWidth;
            h = fcRectHeight;
            controller.controllers.push(new MSF.Controller(id, x, y, w, h));

            // EC
            id = controllerEcPrefix + obj;
            x = ecRectX + fcRectOffsetX * i;
            y = ecRectY;
            w = ecRectWidth;
            h = ecRectHeight;
            controller.controllers.push(new MSF.Controller(id, x, y, w, h));

            // EM
            id = controllerEmPrefix + obj;
            x = emRectX + fcRectOffsetX * i;
            y = emRectY;
            w = emRectWidth;
            h = emRectHeight;
            controller.controllers.push(new MSF.Controller(id, x, y, w, h));

            // NW
            x = nwPathX + fcRectOffsetX * i;
            y = nwPathY;
            points = "m" + x + "," + y + nwPathPoints;
            controller.nodes.path(points).attr(nwPathAttr);

            // 線：MFC-FC
            if (!MSF.Conf.Rest.MFC.FC_SINGLE) {
                x1 = mfcRectX + mfcRectWidth / 2;
                y1 = mfcRectY + mfcRectHeight;
                x2 = fcRectX + fcRectOffsetX * i + fcRectWidth / 2;
                y2 = fcRectY;
                controller.lines.line(x1, y1, x2, y2).attr(controllerLineAttr);
            }

            // 線：FC-EC
            x1 = fcRectX + fcRectOffsetX * i + fcRectWidth / 2;
            y1 = fcRectY + fcRectHeight;
            x2 = x1;
            y2 = ecRectY;
            controller.lines.line(x1, y1, x2, y2).attr(controllerLineAttr);

            // 線：EC-NW
            x1 = fcRectX + fcRectOffsetX * i + fcRectWidth / 2;
            y1 = ecRectY + ecRectHeight;
            x2 = x1;
            y2 = nwPathY - nwPathHeight;
            controller.lines.line(x1, y1, x2, y2).attr(controllerLineAttr);

            // クラスタ
            x = controllerClusterX + fcRectOffsetX * i;
            y = controllerClusterY;
            w = controllerClusterWidth;
            h = controllerClusterHeight;
            controller.backs.rect(w, h).dx(x).dy(y).attr(controllerClusterAttr);

            // クラスタアイコン
            x = controllerClusterX + fcRectOffsetX * i;
            y = controllerClusterY;
            points = "m" + x + "," + y + controllerHexgonPoint;
            controller.backs.path(points).attr(controllerHexgonAttr);

            // クラスタアイコン・ラベル
            x = controllerLabelX + fcRectOffsetX * i;
            y = controllerLabelY;
            controller.backs.plain("#"+obj).font(controllerLabelAttr).dx(x).dy(y);

        });
    }
    ;

    //
    // 図形選択解除.
    //
    MSF.ControllerView.prototype.unSelectedAll = function() {
        this.controllers.forEach(function(obj, i) {
            obj.selected(false);
        });
    }
    ;

    //
    // 図の取得.
    //   type: コントローラ種別(MFC/FC/EC/EM)
    //   id: クラスタID
    //
    MSF.ControllerView.prototype.getControllerById = function(type, id) {
        var result = null;

        // ID検索キーワード
        var key = "unknown";
        switch(type) {
            case "MFC":
                key = controllerMfcPrefix;
                break;
            case "FC":
                key = controllerFcPrefix + id;
                break;
            case "EC":
                key = controllerEcPrefix + id;
                break;
            case "EM":
                key = controllerEmPrefix + id;
                break;
            default:
                break;
        }
        // 探索
        this.controllers.forEach(function(obj, i) {
            if (obj.id == key) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // 複数コントローラ選択.
    //   ids: クラスタIDの配列
    //
    MSF.ControllerView.prototype.multiSelected = function(ids) {
        this.controllers.forEach(function(obj, i) {
            for (var j = 0; j < ids.length; j++) {
                if (obj.id == ids[j]) {
                    obj.selected(true);
                }
            }
        });
    }
    ;

    //
    // 選択中コントローラIDの取得.
    //   return: コントローラIDの配列
    //
    MSF.ControllerView.prototype.getSelectedControllers = function() {
        var result = [];
        this.controllers.forEach(function(obj, i) {
            if (obj.isSelected()) {
                result.push(obj.id);
            }
        });
        return result;
    }
    ;

    //
    // 全コントローラIDの取得.
    //   return: コントローラIDの配列
    //
    MSF.ControllerView.prototype.getAllControllers = function() {
        var result = [];
        this.controllers.forEach(function(obj, i) {
            result.push(obj.id);
        });
        return result;
    }
    ;

    //
    // クラスタの有無.
    //   id: クラスタID
    //   return: true/false
    //
    MSF.ControllerView.prototype.isExistsCluster = function(id) {
        var result = false;
        this.ids.forEach(function(obj, i) {
            if (obj === id) {
                result = true;
            }
        });
        return result;
    }
    ;

    //
    // クラスタの追加.
    //   id: クラスタID
    //
    MSF.ControllerView.prototype.addCluster = function(id) {
        if (this.isExistsCluster(id)) {
            // 既に登録済み。何もしない。
            return;
        }
        // 現在のクラスタID群にID追加
        var newIds = [];
        newIds = this.ids;
        newIds.push(id);
        // ソートして再描画
        newIds.sort(function(a, b) {
            return (parseInt(a, 10) > parseInt(b, 10)) ? 1 : -1;
        });
        this.init(newIds);
    }
    ;

    //
    // クラスタの削除.
    //   id: クラスタID
    //
    MSF.ControllerView.prototype.delCluster = function(id) {
        if (!this.isExistsCluster(id)) {
            // 未存在。何もしない。
            return;
        }
        // 現在のクラスタID群からID削除
        var newIds = [];
        this.ids.forEach(function(obj, i) {
            if (obj !== id) {
                newIds.push(obj);
            }
        });
        // 再描画
        this.init(newIds);
    }
    ;

})();
