//
// コントローラクラス.
//   init():                     初期化
//   setStatus(status):          クラスタ物理障害 'error'|'warn'|'normal'
//   selected(true/false):       図の選択
//   isSelected()                選択中か否か
//

// コントローラ(四角形)
const controllerRectAttr = {
    "fill": "#ffffff",    // 背景色:白
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 1     // 線幅: 1px
};
// 警告アイコン(三角形)...未使用
const controllerWarnPoints1 = "m-23,7.2l17.6875,0l-8.84375,-15.0864l-8.84375,15.0864z";
const controllerWarnAttr1 = {
    "opacity": 0.0,       // 透明
    "fill": "#ffff00",    // 背景色:黄
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 1     // 線幅: 1px
};
// 警告アイコン(！)...未使用
const controllerWarnPoints2 = "m-13,5.1l-2.1,0l0,-2.1l2.1,0l0,2.1zm0,-3.64154l-2.1,0l0,-3.64154l2.1,0l0,3.64154z";
const controllerWarnAttr2 = {
    "opacity": 0.0,       // 透明
    "fill": "#000000",    // 背景色:黒
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 0     // 線幅: 1px
};
// ラベルの属性 @see svg.js
const controllerTextAttr = {
    "family": "Helvetica",  // フォントファミリー
    "size": 12,             // フォントサイズ
    "anchor": "middle"      // 配置:中央寄せ
};
// 四角形の境界色
const controllerStrokeSelected = {
    stroke: "#3f007f",  // 線色:紫
    "stroke-width": 4   // 線幅:4px
};
const controllerStrokeUnSelected = {
    stroke: "#000000",  // 線色:黒
    "stroke-width": 1   // 線幅:2px
};
// ラベルの文字高さ
const controllerTxtHeight = 4; // pixcel

(function() {
"use strict";

    //
    // コンストラクタ
    //   id: 識別子(装置名)
    //   x,y,w,h: 座標,大きさ
    //
    MSF.Controller = function(id, x, y, w, h) {
        this.id = id;
        this.x  = x;
        this.y  = y;
        this.width  = w;
        this.height  = h;
        this.status = null;
        this.select = false;

        this.svgGroup = null;

        // 初期化
        this.init();
    }
    ;

    //
    // 初期化.
    // SVG図形描画とクリックイベント登録.
    //
    MSF.Controller.prototype.init = function() {

        // ラベル座標:四角形の中央
        const textX = this.width / 2;
        const textY = this.height / 2 + controllerTxtHeight;

        // コントローラ図形(SVG)の描画.
        this.svgGroup = controllerView.nodes.group().translate(this.x, this.y);
        // 四角形
        var rect = this.svgGroup.rect(this.width, this.height).attr(controllerRectAttr);
        // 警告アイコン△
        var points1 = "m" + this.width + "," + this.height + controllerWarnPoints1;
        var points2 = "m" + this.width + "," + this.height + controllerWarnPoints2;
        var warn1  = this.svgGroup.path(points1).attr(controllerWarnAttr1);
        var warn2  = this.svgGroup.path(points2).attr(controllerWarnAttr2);
        // ラベル
        var label  = this.svgGroup.plain(this.id).font(controllerTextAttr).dx(textX).dy(textY);

        // 図形群をメンバ変数に格納.
        this.svgData = {
            "obj" : this.svgGroup,
            "rect" : rect,
            "warn1" : warn1,
            "warn2" : warn2,
            "label" : label
        };

        // コントローラビューに登録
        var controller = this;

        // クリックイベント登録.
        this.svgGroup.click(function() { controller.clickAction(); });
        // タッチイベント登録.
        rect.touchstart(function(evt) {
            evt.preventDefault();
            controller.clickAction();
        });
        label.touchstart(function(evt) {
            evt.preventDefault();
            controller.clickAction();
        });
    }
    ;

    //
    // ノードのクリック時動作.
    //
    MSF.Controller.prototype.clickAction = function() {
        if (!this.select) {
            this.selected(true);
        } else {
            this.selected(false);
        }
        //console.log("controller["+ controller.id +"] selected");
        MSF.main.updateDetailControllerTable();
    }
    ;

    //
    // コントローラ状態色の変更(未使用).
    //   status: 状態
    //
    MSF.Controller.prototype.setStatus = function(status) {
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
    // コントローラ図形の選択表現.
    //
    MSF.Controller.prototype.selected = function(isSelect) {
        if (isSelect) {
            // 選択時
            this.svgData["rect"].attr(controllerStrokeSelected);
            this.select = true;
        } else {
            // 非選択時
            this.svgData["rect"].attr(controllerStrokeUnSelected);
            this.select = false;
        }
    }
    ;

    //
    // コントローラ図形の選択有無.
    //
    MSF.Controller.prototype.isSelected = function() {
        return this.select;
    }
    ;

})();
