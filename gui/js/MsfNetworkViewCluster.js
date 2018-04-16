//
// クラスタクラス.
//   init():                     初期化
//   setStatus(status):          クラスタ物理障害 'error'|'warn'|'normal'
//   setChildStatus(status):     装置/IF物理障害 'error'|'normal'
//   setCpStatus(status,ids):    CP障害 'error'|'normal'
//   showCP(n):                  CP(丸印)の表示
//   hideCP():                   CP(丸印)の非表示
//   selected(true/false):       図の選択
//   isSelected()                選択中か否か
//

// 六角形の座標と属性
const hexgonWidth = 80;
const hexgonHeight = 80;
const hexgonPoints = "m1.83854,40l16.35491,-32.70982l43.6131,0l16.35491,32.70982l-16.35491,32.70982l-43.6131,0l-16.35491,-32.70982z";
const hexgonAttr = {
    "fill": "#ffffff",    // 背景色:白
    "fill-opacity": 1.0,  // 不透明
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 2     // 線幅: 2px
};
// 円の座標と属性 ※CPを表現する円
const circleAttr = {
    "opacity": 0.0,       // 透明
    "fill": "#ffffff",    // 背景色: 白
    "stroke": "#606060",  // 線色: 灰
    "stroke-width": 1     // 線幅: 1px
};
// 警告アイコン(三角形)
const warnPoints1 = "m46,76l34,0l-17,-29l-17,29z";
const warnAttr1 = {
    "opacity": 0.0,       // 透明
    "fill": "#ffff00",    // 背景色:黄
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 1     // 線幅: 1px
};
// 警告アイコン(！)
const warnPoints2 = "m65,72l-4,0l0,-4l4,0l0,4zm0,-7l-4,0l0,-7l4,0l0,7z";
const warnAttr2 = {
    "opacity": 0.0,       // 透明
    "fill": "#000000",    // 背景色:黒
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 0     // 線幅: 1px
};
// ラベルの属性 @see svg.js
const textAttr = {
    "family": "Helvetica",  // フォントファミリー
    "size": 12,             // フォントサイズ
    "anchor": "middle"      // 配置:中央寄せ
};
// 六角形の背景色
const ClusterHexColorNormal = {fill: "#ffffff"}; // 背景色:白
const ClusterHexColorWarn   = {fill: "#ffaa56"}; // 背景色:橙
const ClusterHexColorError  = {fill: "#ff0000"}; // 背景色:赤
// 六角形の文字色
const ClusterTxtColorNormal = {fill: "#000000"}; // 文字色:黒
const ClusterTxtColorWarn   = {fill: "#000000"}; // 文字色:黒
const ClusterTxtColorError  = {fill: "#ffffff"}; // 文字色:白
// 六角形の境界色
const ClusterStrokeSelected = {
    stroke: "#3f007f",  // 線色:紫
    "stroke-width": 4   // 線幅:4px
};
const ClusterStrokeUnSelected = {
    stroke: "#000000",  // 線色:黒
    "stroke-width": 2   // 線幅:2px
};
// 六角形の文字高さ
const ClusterTxtHeight = 16; // pixcel
// 六角形の外周点
const BoundaryVertexNum = 24;   // n角形として取得
const BoundaryVertexOffset = 5; // 半径の延長分(px)

// CP(5つの丸)の色
const ClusterCpColorNormal = {fill: "#ffffff"};
const ClusterCpColorError = {fill: "#ff0000"};
// CP(6以上)の色
const ClusterCpExColorNormal = {stroke: "#606060"};
const ClusterCpExColorError = {stroke: "#ff0000"};
// CP(1～5)大きさ・座標
const ClusterCpR1 = 6;   // 半径
const ClusterCpCx1 = 21;  // X座標
const ClusterCpCy1 = 76;  // Y座標
const ClusterCpDx1 = 8;   // X間隔
// CP(6以上)大きさ・座標
const ClusterCpR2 = 1;   // 半径
const ClusterCpCx2 = 61;  // X座標
const ClusterCpCy2 = 79;  // Y座標
const ClusterCpDx2 = 3;   // X間隔

(function() {
"use strict";

    //
    // コンストラクタ
    //   id: クラスタID
    //   x,y: 座標
    //
    MSF.MsfCluster = function(id, x, y) {
        this.id = id;
        this.x  = x;
        this.y  = y;
        this.width  = hexgonWidth;
        this.height  = hexgonHeight;
        this.status = null;
        this.select = false;
        this.isTouchDrag = false;

        this.svgData = null;
        this.label  = null;

        // 初期化
        this.init();
    }
    ;

    //
    // 初期化.
    // SVG図形描画とクリックイベント登録.
    //
    MSF.MsfCluster.prototype.init = function() {
        // CP(1～5)定数
        const r1  = ClusterCpR1;
        const cx1 = ClusterCpCx1;
        const cy1 = ClusterCpCy1;
        const dx1 = ClusterCpDx1;
        // CP(6以上)定数
        const r2  = ClusterCpR2;
        const cx2 = ClusterCpCx2;
        const cy2 = ClusterCpCy2;
        const dx2 = ClusterCpDx2;

        // ラベル座標:六角形の中央
        const textX = hexgonWidth / 2;
        const textY = hexgonHeight / 2 - ClusterTxtHeight;

        // クラスタ図形(SVG)の描画.
        this.svgGroup = svg.nodes.group().translate(this.x, this.y).draggy(svg.dragArea);
        // 六角形
        var hexgon = this.svgGroup.path(hexgonPoints).attr(hexgonAttr);
        // CP(1～5)
        var cp1 = this.svgGroup.circle(r1).attr(circleAttr).dx(cx1+dx1*0).dy(cy1);
        var cp2 = this.svgGroup.circle(r1).attr(circleAttr).dx(cx1+dx1*1).dy(cy1);
        var cp3 = this.svgGroup.circle(r1).attr(circleAttr).dx(cx1+dx1*2).dy(cy1);
        var cp4 = this.svgGroup.circle(r1).attr(circleAttr).dx(cx1+dx1*3).dy(cy1);
        var cp5 = this.svgGroup.circle(r1).attr(circleAttr).dx(cx1+dx1*4).dy(cy1);
        // CP(6以上…)
        var cp6 = this.svgGroup.circle(r2).attr(circleAttr).dx(cx2+dx2*0).dy(cy2);
        var cp7 = this.svgGroup.circle(r2).attr(circleAttr).dx(cx2+dx2*1).dy(cy2);
        var cp8 = this.svgGroup.circle(r2).attr(circleAttr).dx(cx2+dx2*2).dy(cy2);
        // 警告アイコン△
        var warn1  = this.svgGroup.path(warnPoints1).attr(warnAttr1);
        var warn2  = this.svgGroup.path(warnPoints2).attr(warnAttr2);
        // ラベル
        var label  = this.svgGroup.text("#"+this.id).font(textAttr).dx(textX).dy(textY);

        // 図形群をメンバ変数に格納.
        this.svgData = {
            "obj" : this.svgGroup,
            "hexgon" : hexgon,
            "cp1" : cp1,
            "cp2" : cp2,
            "cp3" : cp3,
            "cp4" : cp4,
            "cp5" : cp5,
            "cp6" : cp6,
            "cp7" : cp7,
            "cp8" : cp8,
            "warn1" : warn1,
            "warn2" : warn2,
            "label" : label
        };

        //ネットワークビューに登録
        var cluster = this;
        if (svg != null) {
            svg.addCluster(cluster);
        }

        // クリックイベント登録.
        this.svgGroup.click(function() {
            if (svg != null) svg.unSelectedAll();
            cluster.selected(true);
            MSF.main.updateDetailTable();
        });

        // 同タッチ対応
        hexgon.touchstart(function(evt) {
            evt.preventDefault();
            if (svg != null) svg.unSelectedAll();
            cluster.selected(true);
            MSF.main.updateDetailTable();
            // キャンバスドラッグ競合回避のドラッグ中フラグOn
            cluster.isTouchDrag = true;
        });
        hexgon.touchend(function(evt) {
            evt.preventDefault();
            // キャンバスドラッグ競合回避のドラッグ中フラグOff
            // イベント発火のタイムラグ対応で
            // クラスタ図形移動後、1秒後にキャンバス移動可能にする
            setTimeout(function(){ cluster.isTouchDrag = false; }, 1000);
        });

        // クラスタ図形のドラッグイベント登録
        this.setDragEvent(this.svgGroup);
    }
    ;

    // クラスタ図形のドラッグイベント登録.
    // ドラッグは移動可能領域を明示する.
    MSF.MsfCluster.prototype.setDragEvent = function(obj) {
        obj.on("dragstart", function(event) {
            if (svg != null) svg.showDraggableArea(true);
        });
        obj.on("dragend", function(event) {
            if (svg != null) svg.showDraggableArea(false);
        });
    }
    ;

    //
    // クラスタの状態色の変更(物理障害).
    //   status: 状態
    //   ※未使用。
    //   クラスタ障害は対象外。配下のノード・IF障害の場合に警告アイコンのみ表示とする。
    //
    MSF.MsfCluster.prototype.setStatus = function(status) {
        switch (status) {
        case "error":
            // 障害時：背景赤、文字白
            this.svgData["hexgon"].attr(ClusterHexColorError);
            this.svgData["label"].attr(ClusterTxtColorError);
            break;
        case "warn":
            // 警告時：背景橙、文字黒
            this.svgData["hexgon"].attr(ClusterHexColorWarn);
            this.svgData["label"].attr(ClusterTxtColorWarn);
            break;
        case "normal":
        default:
            // 正常時：背景白、文字黒
            this.svgData["hexgon"].attr(ClusterHexColorNormal);
            this.svgData["label"].attr(ClusterTxtColorNormal);
            break;
        }
    }
    ;

    //
    // クラスタ配下の状態色の変更(装置/IFの物理障害).
    //   status: 状態
    //
    MSF.MsfCluster.prototype.setChildStatus = function(status) {
        switch (status) {
        case "error":
            // 警告アイコンの表示
            this.svgData["warn1"].attr({opacity: 1.0});
            this.svgData["warn2"].attr({opacity: 1.0});
            break;
        case "normal":
        default:
            // 警告アイコンの非表示
            this.svgData["warn1"].attr({opacity: 0.0});
            this.svgData["warn2"].attr({opacity: 0.0});
            break;
        }
    }
    ;

    //
    // クラスタ配下のCP状態色の変更. (未使用)
    //   status: 状態
    //   ids: 赤くするCP図形の位置[1..6]
    //
    MSF.MsfCluster.prototype.setCpStatus = function(status, ids) {
        var svgData = this.svgData;
        ids.forEach(function(id, i){
            switch (status) {
            case "error":
                // 障害時：赤
                if (id == 1) svgData["cp1"].attr(ClusterCpColorError);
                if (id == 2) svgData["cp2"].attr(ClusterCpColorError);
                if (id == 3) svgData["cp3"].attr(ClusterCpColorError);
                if (id == 4) svgData["cp4"].attr(ClusterCpColorError);
                if (id == 5) svgData["cp5"].attr(ClusterCpColorError);
                if (id > 5){
                    svgData["cp6"].attr(ClusterCpExColorError);
                    svgData["cp7"].attr(ClusterCpExColorError);
                    svgData["cp8"].attr(ClusterCpExColorError);
                }
                break;
            case "normal":
            default:
                // 正常時：白
                if (id == 1) svgData["cp1"].attr(ClusterCpColorNormal);
                if (id == 2) svgData["cp2"].attr(ClusterCpColorNormal);
                if (id == 3) svgData["cp3"].attr(ClusterCpColorNormal);
                if (id == 4) svgData["cp4"].attr(ClusterCpColorNormal);
                if (id == 5) svgData["cp5"].attr(ClusterCpColorNormal);
                if (id > 5){
                    svgData["cp6"].attr(ClusterCpExColorNormal);
                    svgData["cp7"].attr(ClusterCpExColorNormal);
                    svgData["cp8"].attr(ClusterCpExColorNormal);
                }
                break;
            }
        });
    }
    ;

    //
    // クラスタ配下のCP表示
    //   n: CPの数
    //
    MSF.MsfCluster.prototype.showCP = function(n) {
        if (n === 0) this.hideCP();
        if (n > 0) this.svgData["cp1"].attr({opacity: 1.0});
        if (n > 1) this.svgData["cp2"].attr({opacity: 1.0});
        if (n > 2) this.svgData["cp3"].attr({opacity: 1.0});
        if (n > 3) this.svgData["cp4"].attr({opacity: 1.0});
        if (n > 4) this.svgData["cp5"].attr({opacity: 1.0});
        if (n > 5) {
            this.svgData["cp6"].attr({opacity: 1.0});
            this.svgData["cp7"].attr({opacity: 1.0});
            this.svgData["cp8"].attr({opacity: 1.0});
        }
    }
    ;

    //
    // クラスタ配下のCP非表示.
    //
    MSF.MsfCluster.prototype.hideCP = function() {
        this.svgData["cp1"].attr({opacity: 0.0});
        this.svgData["cp2"].attr({opacity: 0.0});
        this.svgData["cp3"].attr({opacity: 0.0});
        this.svgData["cp4"].attr({opacity: 0.0});
        this.svgData["cp5"].attr({opacity: 0.0});
        this.svgData["cp6"].attr({opacity: 0.0});
        this.svgData["cp7"].attr({opacity: 0.0});
        this.svgData["cp8"].attr({opacity: 0.0});
    }
    ;

    //
    // クラスタ図形の選択表現.
    //
    MSF.MsfCluster.prototype.selected = function(isSelect) {
        if (isSelect) {
            // 選択時
            this.svgData["hexgon"].attr(ClusterStrokeSelected);
            this.select = true;
        } else {
            // 非選択時
            this.svgData["hexgon"].attr(ClusterStrokeUnSelected);
            this.select = false;
        }
    }
    ;

    //
    // クラスタ図形の選択有無.
    //
    MSF.MsfCluster.prototype.isSelected = function() {
        return this.select;
    }
    ;

    //
    // 六角形の(拡張)外周点の取得.
    // 六角形より5px大きい24角形の頂点を返すことで角を丸く見せる.
    //
    MSF.MsfCluster.prototype.getPoints = function() {
        var hexPoints = [];
        var item_num = BoundaryVertexNum;
        var deg = 360.0 / item_num;
        var red = (deg * Math.PI / 180.0);
        var offset = BoundaryVertexOffset;
        var r = hexgonWidth/2 + offset;
        for (var i = 0; i < item_num; i++) {
            var x = Math.cos(red * i) * r + r;
            var y = Math.sin(red * i) * r + r;
            hexPoints.push({
                x: this.svgGroup.x() + x - offset,
                y: this.svgGroup.y() + y - offset
            });
        }
        return hexPoints;
    }
    ;

})();
