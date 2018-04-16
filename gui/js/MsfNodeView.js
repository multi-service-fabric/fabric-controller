//
// ノードビュークラス.
//
//   init(modelKey)               初期化
//   drawPanelImage(view, info)   前面図・背面図の描画
//   drawColorLegend(view)        色凡例の描画
//   setSlotType(id, type)        IF種類での色替え
//   setIfStatus(status,ids):     IF障害 'error'|'normal'
//
//   selectedFront(isSelect)      前面図の選択
//   selectedRear(isSelect)       背面図の選択
//   unSelectedAll()              図選択の解除
//
//   getViewClusterID()           表示中クラスタIDの取得
//   getViewDeviceType()        表示中装置種別の取得
//   getViewNodeID()              表示中装置IDの取得
//   getSelectedIF()              選択中IFの取得
//

// HTL(DOM)のid、CSSのclass
const nodeViewDivId = "#nodeCanvas";
const nodeViewCssSvgClass = "width-100-percent";
const nodeViewCssIfLabelClass = "ifLabel";
const nodeViewCssLegendLabelClass = "legendLabel";

// 座標系
const nodeViewOffsetX = 20;
const nodeViewOffsetY = 100;
// IFの凡例
const nodeViewLegendX = 640;
const nodeViewLegendY = 30;
const nodeViewLegendW = 20;
const nodeViewLegendH = 12;
const nodeViewLegendVSpacer = 16;
const nodeViewLegendHSpacer = 5;
const nodeViewLegendFontHeight = 22;

// SVG背景
const nodeViewBgRectAttr = {
    fill : '#fcfcfc',
    stroke : 'none'
};
// ノード(四角形)
const nodeViewNodeRectAttr = {
    "stroke" : "#000000",
    "stroke-width" : 0.5,
    "fill": "#ffffff",
    "fill-opacity": 0.0
};
// ノード(正常)選択時
const nodeViewNodeRectSelectAttr = {
    "stroke" : "#3f007f",
    "stroke-width" : 4
};
// ノード(正常)非選択時
const nodeViewNodeRectUnSelectAttr = {
    "stroke" : "#000000",
    "stroke-width" : 0.5
};
// ノード(異常)選択時
const nodeViewNodeRectSelectErrAttr = {
    "stroke" : "#ff0000",
    "stroke-width" : 4
};
// ノード(異常)非選択時
const nodeViewNodeRectUnSelectErrAttr = {
    "stroke" : "#ff0000",
    "stroke-width" : 0.5
};

// インタフェース(四角形)
const nodeViewIfRectAttr = {
    "stroke" : "#000000",
    "stroke-width" : 0.5,
    "fill": interfaceColor["unused"],
    "cursor": "pointer"
};
const nodeViewIfSelectAttr = {
    "stroke": "#3f007f",  // 線色:紫
    "stroke-width": 3     // 線幅:4px
};
const nodeViewIfUnSelectAttr = {
    "stroke": "#000000",
    "stroke-width": 0.5
};

// 警告アイコン(三角形)
const nodeViewWarnAttr1 = {
    "opacity": 0.0,
    "fill": "#ffff00",
    "stroke": "#000000",
    "stroke-width": 1
};
// 警告アイコン！(path)
const nodeViewWarnAttr2 = {
    "opacity": 0.0,
    "fill": "#000000",
    "stroke": "#000000",
    "stroke-width": 0
};
// 凡例(物理IF)
const nodeViewPhysicalIfAttr = {
    "stroke" : "#000000",
    "stroke-width" : 0.5,
    "fill" : interfaceColor["physical"]
};
// 凡例(breakoutIF)
const nodeViewBreakoutIfAttr = {
    "stroke": "#000000",
    "stroke-width": 0.5,
    "fill": interfaceColor["breakout"]
};
// 凡例(LagIF)
const nodeViewLagIfAttr = {
    "stroke": "#000000",
    "stroke-width": 0.5,
    "fill": interfaceColor["lag"]
};
// 凡例(未使用IF)
const nodeViewUnusedIfAttr = {
    "stroke" : "#000000",
    "stroke-width" : 0.5,
    "fill" : interfaceColor["unused"]
};


(function() {
"use strict";

    //
    // コンストラクタ.
    //   w,h: キャンバスの幅・高さ
    //
    MSF.NodeView = function(w, h) {

        // メンバ変数
        this.nodeView = null; // SVGキャンバス
        this.nodes = null;    // 図形レイヤ(svg group)
        this.front = null;    // 前面図(svg rect)
        this.rear  = null;    // 背面図(svg rect)
        this.slot = null;     // IFスロット(svg rect)
        this.warn1 = null;    // IFスロット警告アイコン△(svg path)
        this.warn2 = null;    // IFスロット警告アイコン！(svg path)
        this.error = false;   // ノード物理障害有無
        this.select = "none"; // ノード図形選択有無

        // キャンバス描画 (htmlのdiv内に埋込SVGのDOM追加)
        this.nodeView = new SVG(document.querySelector(nodeViewDivId)).size(w, h);
        this.nodeView.attr({
            "width"     : "100%",
            "max-width" : w +"px",
            "height"    : "100%",
            "max-height": h + "px"
        });
        this.nodeView.addClass(nodeViewCssSvgClass);
        // キャンバスの枠線
        var rect = this.nodeView.rect(w, h).attr(nodeViewBgRectAttr);

        // キャンバスクリック時、他図形の選択解除
        var svgCanvas = this;
        rect.click(function(){
            svgCanvas.unSelectedAll();
            // 詳細部更新
            MSF.main.updateDetailTable();
        });
        // 同タッチイベント
        rect.touchstart(function(evt){
            evt.preventDefault();
            svgCanvas.unSelectedAll();
            // 詳細部更新
            MSF.main.updateDetailTable();
        });

        // キャンバスに図形レイヤを追加
        this.nodes = this.nodeView.group();
    }
    ;

    //
    // 初期化(ノード表示モード).
    //
    MSF.NodeView.prototype.init = function(modelKey, clusterId, deviceType, nodeId) {
        this.clusterId = clusterId;
        this.deviceType = deviceType;
        this.nodeId = nodeId;
        
        var view = this.nodes;
        var info = MSF.ModelInfo[modelKey];

        // 表示のクリア
        view.clear();

        var frontGroup = view.group();
        var rearGroup  = view.group();
        var slotGroup  = view.group();

        // 凡例表示
        this.drawColorLegend(view);

        // イメージ表示
        this.front = this.drawPanelImage(frontGroup, info.view.front);
        this.rear  = this.drawPanelImage(rearGroup, info.view.rear);

        // IFスロット
        var slotArray = {};
        var nView = this;
        info.view.front.slot.forEach(function(obj, i){
            var x = obj.x + nodeViewOffsetX;
            var y = obj.y + nodeViewOffsetY;
            var w = obj.w;
            var h = obj.h;
            slotArray[obj.id] = slotGroup.rect(w, h);
            slotArray[obj.id].dx(x).dy(y);
            slotArray[obj.id].attr(nodeViewIfRectAttr);
            // クリックイベントの追加
            slotArray[obj.id].click(function(){
                nView.selectedIfSlot(true, obj.id);
                MSF.main.updateDetailTable();
            });
            //タッチイベントの追加
            slotArray[obj.id].on("touchstart", function(evt) {
                // ページ遷移イベントを抑止
                evt.preventDefault();
                nView.selectedIfSlot(true, obj.id);
                // 詳細部更新
                MSF.main.updateDetailTable();
            });
        });
        this.slot = slotArray;

        // IFスロットラベル
        var labelArray = {};
        info.view.front.label.forEach(function(obj, i){
            var x = obj.x + nodeViewOffsetX;
            var y = obj.y + nodeViewOffsetY;
            labelArray[obj.id] = frontGroup.plain(obj.id);
            labelArray[obj.id].font({anchor: "middle"});
            labelArray[obj.id].dx(x).dy(y);
            labelArray[obj.id].addClass(nodeViewCssIfLabelClass);
        });

        // IF警告アイコン
        var warn1Array = {};
        var warn2Array = {};
        info.view.front.slot.forEach(function(obj, i){
            // 警告アイコン(三角形)
            // IFスロット右下座標(x+w,y+h)からアイコンサイズ(13,10)分戻した位置が基準座標
            var x1 = obj.x + obj.w - 13 + 0  + nodeViewOffsetX;
            var y1 = obj.y + obj.h - 10 + 11 + nodeViewOffsetY;
            var warnPoints1 = " l 13.1,0 l -6.6,-11.2 l -6.6,11.2z";
            warnPoints1 = "m" + x1 + "," + y1 + warnPoints1;

            // 警告アイコン(！)
            // IFスロット右下座標(x+w,y+h)からアイコンサイズ(13,10)分戻した位置が基準座標
            var x2 = obj.x + obj.w -13 + 7.3 + nodeViewOffsetX;
            var y2 = obj.y + obj.h -10 + 9.6 + nodeViewOffsetY;
            var warnPoints2 = " l -1.5,0 l 0,-1.5 l 1.5,0 l 0,1.5zm0,-2.7 l -1.5,0 l 0,-2.7 l 1.5,0 l 0,2.7z";
            warnPoints2 = "m" + x2 + "," + y2 + warnPoints2;
            warn1Array[obj.id] = slotGroup.path(warnPoints1).attr(nodeViewWarnAttr1);
            warn2Array[obj.id] = slotGroup.path(warnPoints2).attr(nodeViewWarnAttr2);
            // クリックイベントの追加
            warn1Array[obj.id].click(function(){
                nView.selectedIfSlot(true, obj.id);
                // 詳細部更新
                MSF.main.updateDetailTable();
            });
            warn2Array[obj.id].click(function(){
                nView.selectedIfSlot(true, obj.id);
                // 詳細部更新
                MSF.main.updateDetailTable();
            });
            // タッチイベントは追加しない
            // マウスより当たり判定が広いので、IFスロットだけタッチ対応で十分.
        });
        this.warn1 = warn1Array;
        this.warn2 = warn2Array;

        // 前面パネルにクリックイベント登録
        var svgCanvas = this;
        frontGroup.click(function(){
            svgCanvas.unSelectedAll();
            svgCanvas.selectedFront(true);
            // 詳細部更新
            MSF.main.updateDetailTable();
        });
        // 前面パネルにタッチイベント登録
        this.front.on("touchstart", function(evt){
            // ページ遷移イベントを抑止
            evt.preventDefault();
            svgCanvas.unSelectedAll();
            svgCanvas.selectedFront(true);
        });
        // 背面パネルにクリックイベント登録
        rearGroup.click(function(){
            svgCanvas.unSelectedAll();
            svgCanvas.selectedRear(true);
            // 詳細部更新
            MSF.main.updateDetailTable();
        });
        // 背面パネルにタッチイベント登録
        this.rear.on("touchstart", function(evt){
            // ページ遷移イベントを抑止
            evt.preventDefault();
            svgCanvas.unSelectedAll();
            svgCanvas.selectedRear(true);
        });
    }
    ;

    //
    // 前面図・背面図の描画.
    //
    MSF.NodeView.prototype.drawPanelImage = function(view, info){
        var x, y, w, h, txt, url, pos;

        // パネルキャプション
        x = info.caption.x + nodeViewOffsetX;
        y = info.caption.y + nodeViewOffsetY;
        txt = info.caption.title;
        pos = info.caption.anchor;
        view.plain(txt).font({anchor: pos}).dx(x).dy(y);

        // パネル背景画像
        x = info.image.x + nodeViewOffsetX;
        y = info.image.y + nodeViewOffsetY;
        w = info.image.w;
        h = info.image.h;
        url = info.image.url;
        view.image(url, w, h).dx(x).dy(y);

        // 外枠
        x = info.body.x + nodeViewOffsetX;
        y = info.body.y + nodeViewOffsetY;
        w = info.body.w;
        h = info.body.h;
        return view.rect(w, h).dx(x).dy(y).attr(nodeViewNodeRectAttr);
    }
    ;

    //
    // インターフェース色凡例の描画.
    //   1. 物理IF
    //   2. breakoutIF
    //   3. LagIF
    //   4. 未使用IF
    //
    MSF.NodeView.prototype.drawColorLegend = function(view){
        var x = nodeViewLegendX;
        var y = nodeViewLegendY;
        var w = nodeViewLegendW;
        var h = nodeViewLegendH;
        var vsp = nodeViewLegendVSpacer;
        var hsp = nodeViewLegendHSpacer;
        var fh = nodeViewLegendFontHeight;

        // 凡例
        var if1 = view.rect(w, h).dx(x).dy(y+vsp*0);
        var if2 = view.rect(w, h).dx(x).dy(y+vsp*1);
        var if3 = view.rect(w, h).dx(x).dy(y+vsp*2);
        var if4 = view.rect(w, h).dx(x).dy(y+vsp*3);

        if1.attr(nodeViewPhysicalIfAttr);
        if2.attr(nodeViewBreakoutIfAttr);
        if3.attr(nodeViewLagIfAttr);
        if4.attr(nodeViewUnusedIfAttr);

        // 凡例ラベル
        var lbl1 = view.plain("Physocal IF");
        var lbl2 = view.plain("breakout IF");
        var lbl3 = view.plain("Lag IF");
        var lbl4 = view.plain("unused");

        lbl1.font({anchor: "left"});
        lbl2.font({anchor: "left"});
        lbl3.font({anchor: "left"});
        lbl4.font({anchor: "left"});

        lbl1.dx(x+w+hsp).dy(y+vsp*0+fh);
        lbl2.dx(x+w+hsp).dy(y+vsp*1+fh);
        lbl3.dx(x+w+hsp).dy(y+vsp*2+fh);
        lbl4.dx(x+w+hsp).dy(y+vsp*3+fh);

        lbl1.addClass(nodeViewCssLegendLabelClass);
        lbl2.addClass(nodeViewCssLegendLabelClass);
        lbl3.addClass(nodeViewCssLegendLabelClass);
        lbl4.addClass(nodeViewCssLegendLabelClass);
    }
    ;

    //
    // インタフェース種類の設定.
    //
    MSF.NodeView.prototype.setSlotType = function(id, type) {
        var color;
        if (interfaceColor[type] != null) {
            // 定義色を取得
            color = interfaceColor[type];
        } else {
            color = interfaceColor["unused"];
        }
        if (this.slot[id] != null) {
            // 背景色を変更
            this.slot[id].attr({fill: color});
        }
    }
    ;

    //
    // インタフェース障害状態の設定.
    //
    MSF.NodeView.prototype.setIfStatus = function(status, ids) {
        var warn1 = this.warn1;
        var warn2 = this.warn2;
        ids.forEach(function(id, i){
            if (warn1[id] != null) {
                switch (status) {
                case "error":
                    // 障害時：アイコン表示
                    warn1[id].attr({opacity: 1.0});
                    warn2[id].attr({opacity: 1.0});
                    break;
                case "normal":
                default:
                    // 正常時：アイコン非表示
                    warn1[id].attr({opacity: 0.0});
                    warn2[id].attr({opacity: 0.0});
                    break;
                }
            }
        });
    }
    ;

    //
    // インタフェース障害状態の設定クリア.
    //
    MSF.NodeView.prototype.clearIfStatus = function() {
        var warn1 = this.warn1;
        var warn2 = this.warn2;

        for (var id in this.slot) {
            if (warn1[id] != null) {
                warn1[id].attr({opacity: 0.0});
                warn2[id].attr({opacity: 0.0});
            }
        }
    }
    ;

    //
    // 前面図の選択.
    //
    MSF.NodeView.prototype.selectedFront = function(isSelect) {
        if (isSelect) {
            // 選択時
            if (this.error) {
                this.front.attr(nodeViewNodeRectSelectErrAttr);
            } else {
                this.front.attr(nodeViewNodeRectSelectAttr);
            }
            this.select = "front";
        } else {
            // 非選択時
            if (this.error) {
                this.front.attr(nodeViewNodeRectUnSelectErrAttr);
            } else {
                this.front.attr(nodeViewNodeRectUnSelectAttr);
            }
        }
    }
    ;

    //
    // 背面図の選択.
    //
    MSF.NodeView.prototype.selectedRear = function(isSelect) {
        if (isSelect) {
            // 選択時
            if (this.error) {
                this.rear.attr(nodeViewNodeRectSelectErrAttr);
            } else {
                this.rear.attr(nodeViewNodeRectSelectAttr);
            }
            this.select = "rear";
        } else {
            // 非選択時
            if (this.error) {
                this.rear.attr(nodeViewNodeRectUnSelectErrAttr);
            } else {
                this.rear.attr(nodeViewNodeRectUnSelectAttr);
            }
        }
    }
    ;

    //
    // インターフェースの選択.
    //
    MSF.NodeView.prototype.selectedIfSlot = function(isSelect, ifNo) {
        this.unSelectedAll();
        if (isSelect) {
            // 選択時
            this.slot[ifNo].attr(nodeViewIfSelectAttr);
            this.select = ifNo;
            console.log("IF ["+ifNo+"] selected.");
        } else {
            // 非選択時
            this.slot[ifNo].attr(nodeViewIfUnSelectAttr);
        }
    }
    ;

    //
    // ノード物理障害.
    //
    MSF.NodeView.prototype.setError = function(isError) {
        this.error = isError;
        if (isError) {
            if (this.select == "front") {
                this.front.attr(nodeViewNodeRectSelectErrAttr);
            } else {
                this.front.attr(nodeViewNodeRectUnSelectErrAttr);
            }
            if (this.select == "rear") {
                this.rear.attr(nodeViewNodeRectSelectErrAttr);
            } else {
                this.rear.attr(nodeViewNodeRectUnSelectErrAttr);
            }
        } else {
            if (this.select == "front") {
                this.front.attr(nodeViewNodeRectSelectAttr);
            } else {
                this.front.attr(nodeViewNodeRectUnSelectAttr);
            }
            if (this.select == "rear") {
                this.rear.attr(nodeViewNodeRectSelectAttr);
            } else {
                this.rear.attr(nodeViewNodeRectUnSelectAttr);
            }
        }
    }
    ;

    //
    // 図形選択解除.
    //
    MSF.NodeView.prototype.unSelectedAll = function() {
        if (this.front == null) {
            return;
        }
        this.selectedFront(false);
        this.selectedRear(false);
        var obj = this.slot;
        Object.keys(obj).forEach(function (k) {
            obj[k].attr(nodeViewIfUnSelectAttr);
        });
        this.select = "none";
    }
    ;

    //
    // 選択中クラスタIDの取得.
    //   return: クラスタID
    //
    MSF.NodeView.prototype.getViewClusterId = function() {
        return this.clusterId;
    }
    ;

    //
    // 選択中装置種別の取得.
    //   return: 装置種別ID(MSF.Const.FabricType.Spines/Leafs)
    //
    MSF.NodeView.prototype.getViewDeviceType = function() {
        return this.deviceType;
    }
    ;

    //
    // 選択中装置IDの取得.
    //   return: 装置ID
    //
    MSF.NodeView.prototype.getViewNodeId = function() {
        return this.nodeId;
    }
    ;

    //
    // 選択中IFの取得.
    //   return: IF ID
    //
    MSF.NodeView.prototype.getSelectedIF = function() {
        return this.select;
    }
    ;

})();
