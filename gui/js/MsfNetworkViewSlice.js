//
// スライス
// MSF.MsfSlice
//   init():                     初期化
//   updateCluster(id):          クラスタの更新
//   delCluster(id):             クラスタの削除(toDo)
//   show()                      表示
//   hide()                      非表示
//   selected(true/false);       図の選択
//   isSelected()                選択中か否か
//   setStatus(status);
//

const sliceAreaErrorAttr    = {"fill-opacity": 0.1};
const sliceAreaNormalAttr   = {"fill-opacity": 0.1};
const sliceAreaSelectedAttr = {"fill-opacity": 0.2};
const sliceAreaHideAttr     = {"fill-opacity": 0.0};

const sliceLabelErrorAttr     = {"opacity": 1.0, "font-weight": "normal"};
const sliceLabelNormalAttr    = {"opacity": 1.0, "font-weight": "normal"};
const sliceLabelSelectedAttr  = {"opacity": 1.0, "font-weight": "bold"};
const sliceLabelHideAttr      = {"opacity": 0.0, "font-weight": "normal"};

const sliceIconShowAttr = {opacity: 1.0};
const sliceIconHideAttr = {opacity: 0.0};

const sliceLabelAttr = {
    family: 'Helvetica',  // フォントファミリー
    size: 12,             // フォントサイズ
    anchor: 'middle'      // 配置:中央寄せ
};
const sliceLabelOffsetX = 50;
const sliceLabelOffsetY = -40;
const sliceLabelHeight = 14;

// 警告アイコン(三角形)
const sliceWarnPoints1 = "m-20,15.2l17.6875,0l-8.84375,-15.0864l-8.84375,15.0864z";
const sliceWarnAttr1 = {
    "opacity": 0.0,       // 透明
    "fill": "#ffff00",    // 背景色:黄
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 1     // 線幅: 1px
};
// 警告アイコン(！)
const sliceWarnPoints2 = "m-10.1,13.1l-2.08088,0l0,-2.08088l2.08088,0l0,2.08088zm0,-3.64154l-2.08088,0l0,-3.64154l2.08088,0l0,3.64154z";
const sliceWarnAttr2 = {
    "opacity": 0.0,       // 透明
    "fill": "#000000",    // 背景色:黒
    "stroke": "#000000",  // 線色: 黒
    "stroke-width": 0     // 線幅: 1px
};

(function() {
"use strict";

    //
    // コンストラクタ.
    //   id: スライスID
    //   name: スライス名
    //   type: スライスタイプ(L2/L3)
    //   clusterIds: スライス内のCPが含まれるクラスタID群
    //   color: スライス表示色
    //
    MSF.MsfSlice = function(id, name, type, clusterIds, color) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.clusterIds = clusterIds;
        this.clusters = null;
        this.points = [];
        this.bnd = [];    // 境界点の配列
        this.ang = [];
        this.slice = null; // 図形：領域
        this.label = null; // 図形：ラベル
        this.warn1 = null; // 図形：警告アイコン△
        this.warn2 = null; // 図形：警告アイコン！
        // this.color = sliceColor[id % sliceColor.length];
        this.color = color;
        this.status = "normal";
        this.select = false;
        this.visibility = false;

        this.warn = false;

        var clusterSet = [];
        clusterIds.forEach(function(id, i){
            var cluster = svg.getClusterById(id);
            if (cluster != null) {
                clusterSet.push(cluster);
            }
        });
        this.clusters = clusterSet;

        // 境界線の描画
        this.calcBoundary();

        // ドラッグ時イベント
        var slice = this;
        this.clusters.forEach(function(node, i){
            //node.svgGroup.on("mouseup", function(){ slice.calcBoundary(); });
            node.svgGroup.on("dragend", function(){ slice.calcBoundary(); });
            node.svgData.hexgon.touchend(function(){ slice.calcBoundary(); });
        });

        //ネットワークビューに登録
        if (svg != null) {
            if (type == "L2") {
                svg.addL2Slice(slice);
            }else{
                svg.addL3Slice(slice);
            }
        }
    }
    ;
    // クラスタ削除時の再描画
    MSF.MsfSlice.prototype.deleteCluster = function(id){
        var clusters = this.clusters || [];
        for(var i = 0; i < clusters.length; i++) {
            var cluster = clusters[i];
            if (cluster.id == input) {
                clusters.splice(i, 1);
                break;
            }
        }
        // 境界線の描画
        this.calcBoundary();
    }
    ;
    //
    // スライスのクリック時動作.
    //
    MSF.MsfSlice.prototype.clickAction = function() {
        if (svg != null) {
            svg.unSelectedAll();
        }
        if (this.visibility) {
            // 選択状態
            this.selected(true);
        } else {
            // スライス非表示の場合は何もしない
        }
        MSF.main.updateDetailTable();
    }
    ;

    //
    // クラスタの更新.
    // スライス内のCPが含まれるクラスタを更新する.
    //
    MSF.MsfSlice.prototype.updateCluster = function(clusters) {
        // 再描画時に表示/選択状態を引き継ぐ
        var visibility = this.visibility;
        var isSelected = this.isSelected();
        this.hide();

        var clusterSet = [];
        clusters.forEach(function(id){
            var cluster = svg.getClusterById(id);
            if (cluster != null) {
                clusterSet.push(cluster);
            }
        });
        this.clusters = clusterSet;

        // ドラッグ時イベント
        var slice = this;
        var clusterIds = slice.clusterIds;
        this.clusters.forEach(function(node){
           if (clusterIds.indexOf(node.id) == -1) {
               //node.svgGroup.on("mouseup", function(){ slice.calcBoundary(); });
               node.svgGroup.on("dragend", function(){ slice.calcBoundary(); });
               node.svgData.hexgon.touchend(function(){ slice.calcBoundary(); });
           }
        });
        
        // 境界線の描画
        slice.clusterIds = clusters;
        this.calcBoundary();

        if (clusters.length !== 0) {
            // 描画更新前の状態に戻し
            if (visibility) {
                this.show();
            } else {
                this.hide();
            }
            if (isSelected) {
                this.selected(true);
            }
        } else {
            // クラスタIDが0件の場合、非表示のまま+選択解除
            this.selected(false);
        }
    }
    ;

    //
    // スライス図形の表示.
    //
    MSF.MsfSlice.prototype.show = function() {
        // 表示状態を保持(メニュー連動)
        this.visibility = true;
        // スライスにクラスタIDが存在しない場合は処理SKIP
        if (this.clusterIds.length === 0) return;
        // 図形未作成であれば処理SKIP
        if (!this.slice) return;
        // 通常レイヤに移動
        this.slice.addTo(svg.slices);
        this.label.addTo(svg.labels);
        this.warn1.addTo(svg.labels);
        this.warn2.addTo(svg.labels);
        // 表示
        this.slice.attr(sliceAreaNormalAttr);
        this.label.attr(sliceLabelNormalAttr);
        switch (this.status) {
        case "error":
            // 異常時：アイコン表示
            this.showIcon();
            break;
        case "normal":
        default:
            // 正常時：アイコン非表示
            this.hideIcon();
            break;
        }
        // クリック時イベント登録
        var slice = this;
        this.slice.click(function() { slice.clickAction(); });
        this.label.click(function() { slice.clickAction(); });
        this.warn1.click(function() { slice.clickAction(); });
        this.warn2.click(function() { slice.clickAction(); });
        // タッチイベント登録.
        this.slice.touchstart(function(evt) {
            evt.preventDefault();
            slice.clickAction();
        });
        this.label.touchstart(function(evt) {
            evt.preventDefault();
            slice.clickAction();
        });
    }
    ;

    //
    // スライス図形の非表示.
    //
    MSF.MsfSlice.prototype.hide = function() {
        // 表示状態を保持(メニュー連動)
        this.visibility = false;
        // スライスにクラスタIDが存在しない場合は処理SKIP
        if (this.clusterIds.length === 0) return;
        // 図形未作成であれば処理SKIP
        if (!this.slice) return;
        // 隠しレイヤに移動
        this.slice.addTo(svg.hides);
        this.label.addTo(svg.hides);
        this.warn1.addTo(svg.hides);
        this.warn2.addTo(svg.hides);
        // 非表示
        this.slice.attr(sliceAreaHideAttr);
        this.label.attr(sliceLabelHideAttr);
        this.hideIcon();
        // 選択解除
        this.select = false;
        // クリック時イベント解除
        //this.slice.off("click");
        this.slice.off();
        this.label.off("click");
        this.warn1.off("click");
        this.warn2.off("click");
        // タッチイベント解除
        this.slice.off("touchstart");
        this.label.off("touchstart");
    }
    ;

    //
    // スライス図形の警告アイコン表示.
    //
    MSF.MsfSlice.prototype.showIcon = function() {
        this.warn1 ? this.warn1.attr(sliceIconShowAttr) : null;
        this.warn2 ? this.warn2.attr(sliceIconShowAttr) : null;
    }
    ;

    //
    // スライス図形の警告アイコン非表示.
    //
    MSF.MsfSlice.prototype.hideIcon = function() {
        this.warn1 ? this.warn1.attr(sliceIconHideAttr) : null;
        this.warn2 ? this.warn2.attr(sliceIconHideAttr) : null;
    }
    ;

    //
    // スライス図形の選択表現.
    //
    MSF.MsfSlice.prototype.selected = function(isSelect) {
        if (isSelect) {
            // 選択時
            this.slice.attr(sliceAreaSelectedAttr);
            this.label.attr(sliceLabelSelectedAttr);
            this.select = true;
        } else {
            // 非選択時
            if (this.visibility) {
                this.slice ? this.slice.attr(sliceAreaNormalAttr) : null;
                this.label ? this.label.attr(sliceLabelNormalAttr) : null;
                this.select = false;
            }else{
                //this.hide();
            }
        }
    }
    ;

    //
    // スライス図形の選択有無.
    //
    MSF.MsfSlice.prototype.isSelected = function() {
        return this.select;
    }
    ;

    //
    // クラスタ群の外周点群の取得.
    //
    MSF.MsfSlice.prototype.getSlicePoints = function(nodes){
        var polygon = [];
        var cnt = 0;
        nodes.forEach(function(node, i) {
            var nodePoints = node.getPoints();
            nodePoints.forEach(function(p, j) {
                polygon.push({i: cnt, x: p.x, y: p.y });
                cnt = cnt + 1;
            });
        });
        return polygon;
    }
    ;

    //
    // 境界の取得.
    //
    // STEP1. 開始点座標を取得(y座標が最も小さい点)
    // STEP2. 2番目の点座標を取得(開始点との水平角度が最も小さい点)
    // STEP3. 次の点座標を取得(前線分[前々点～前点]との角度が最も小さい点)
    //
    // note:
    // この関数は初期化関数からまず呼び出され、
    // 関数内の呼び先から再帰的に呼ばれることで全点を走査して境界凸多角形を描画する。
    //
    MSF.MsfSlice.prototype.getBoundary = function(){
        if (this.bnd.length === 0) {
            this.getInitialPoint();
        } else if (this.bnd.length == 1) {
            this.getSecondPoint();
        } else {
            this.getNextPoint();
        }
    }
    ;

    //
    // 境界取得 STEP1.
    //
    MSF.MsfSlice.prototype.getInitialPoint = function(){
        var points = this.points;
        var firstPoint;
        points.forEach(function(d, i){
            if (firstPoint == undefined || points[firstPoint].y > d.y) {
                firstPoint = i;
            }
        });
        this.bnd[0] = firstPoint;
        this.getBoundary();
    }
    ;

    //
    // 境界取得 STEP2.
    //
    MSF.MsfSlice.prototype.getSecondPoint = function(){
        var points = this.points;
        var bnd = this.bnd;
        var rp = points.filter(function(d) {
            return bnd.indexOf(d.i) == -1;
        }); // 残りの点 remaining points.

        var firstPoint = bnd[0];
        var secondPoint;
        var lA; //最小角度 smallest angle
        rp.forEach(function(d, i) {
            var angle  = getAngle({x:0, y:points[firstPoint].y}, points[firstPoint], d);
            if(secondPoint == undefined || angle < lA){
                secondPoint = d.i;
                lA = angle;
            }
        });
        this.bnd[1] = secondPoint;
        this.getBoundary();
    }
    ;

    //
    // 境界取得 STEP3.
    //
    MSF.MsfSlice.prototype.getNextPoint = function(){
        var points = this.points;
        var bnd = this.bnd;
        if(bnd[0] == bnd[bnd.length - 1]) {
            // 3点未満なら終わり
            return false;
        }
        var p0 = points[bnd[bnd.length - 2]];
        var p1 = points[bnd[bnd.length - 1]];
        var p = getOuterPoint(p0, p1);

        var rp = points.filter(function(d){
            return bnd.indexOf(d.i) == -1 || d.i == bnd[0];
        }); // 残りの点 remaining points.

        var lA; //最小角度 smallest angle
        var l = bnd.length;
        var nextPoint;
        rp.forEach(function(d, i) {
            var angle  = getAngle(p0, p1, d);
            if(nextPoint == undefined || angle < lA){
                nextPoint = d.i;
                lA = angle;
            }
        });
        bnd[l] = nextPoint;
        if (bnd[0] != nextPoint) {
            this.getBoundary();
        } else {
            this.drawBoundary();
        }
    }
    ;

    //
    // 境界の描画(スライスの描画).
    //
    MSF.MsfSlice.prototype.drawBoundary = function(){
        var points = this.points;
        var bnd = this.bnd;
        // 座標群
        var ps = points[bnd[0]].x + ',' + points[bnd[0]].y;
        for (var i = 1; i < bnd.length; i++) {
            if (points[bnd[i]].x != undefined) {
                ps = ps + ' ' + points[bnd[i]].x + ',' + points[bnd[i]].y;
            }
        }
        // ラベルの属性 @see svg.js
        var textAttr = sliceLabelAttr;
        // ラベルの座標(スライスの中央)
        var textX = getCenterX(points);
        var textY = getCenterY(points);
        // スライスがクラスタ1個だけの場合(＝24角形の場合)
        // ラベル位置がクラスタのラベルと重なるので調整
        if(points.length == 24){
            textX = textX + sliceLabelOffsetX;
            textY = textY + sliceLabelOffsetY;
        }
        // 警告アイコン位置
        var warnPoints1, warnPoints2;
        if (this.slice == null) {
            // 新規描画
            this.slice = svg.slices.polygon(ps);
            this.slice.attr({fill:this.color});
            this.slice.attr(sliceAreaNormalAttr);
            this.label = svg.labels.text(this.name).font(textAttr).dx(textX).dy(textY);
            // 警告アイコン△
            warnPoints1 = getIconPosition(this.label, sliceWarnPoints1);
            warnPoints2 = getIconPosition(this.label, sliceWarnPoints2);
            this.warn1 = svg.labels.path(warnPoints1).attr(sliceWarnAttr1);
            this.warn2 = svg.labels.path(warnPoints2).attr(sliceWarnAttr2);
        } else {
            // 更新
            this.slice.attr({points:ps});
            this.label.dx(textX - this.label.x());
            this.label.dy(textY - this.label.y());
            // 警告アイコン△
            warnPoints1 = getIconPosition(this.label, sliceWarnPoints1);
            warnPoints2 = getIconPosition(this.label, sliceWarnPoints2);
            this.warn1.plot(warnPoints1);
            this.warn2.plot(warnPoints2);
        }
    }
    ;

    //
    // 境界領域の計算.
    //
    MSF.MsfSlice.prototype.calcBoundary = function(){
        // スライスにクラスタIDが存在しない場合は処理SKIP
        if (this.clusterIds.length === 0) return;
        this.points = [];
        this.bnd = [];
        this.points = this.getSlicePoints(this.clusters);
        this.getBoundary();
    }
    ;

    //
    // スライスの状態色の変更(スライス障害)
    //
    MSF.MsfSlice.prototype.setStatus = function(status) {
        // 状態をメンバ変数に格納
        this.status = status;
        // アイコンの表示/非表示
        if (this.visibility) {
            switch (status) {
            case 'error':
                // 障害時：アイコン表示
                this.showIcon();
                break;
            case 'normal':
            default:
                // 正常時：アイコン非表示
                this.hideIcon();
                break;
            }
        }
    }
    ;

    //
    // 警告アイコンの座標計算.
    //   obj:  <text>オブジェクト
    //   plots: 警告アイコンのpath座標
    //
    function getIconPosition(obj, plots) {
        var x = obj.x();
        var y = obj.y();
        var w = obj.length() / 2;
        return "m" + (x-w) + "," + y + plots;
    }

    //
    // 角度の取得.
    //
    function getAngle(p0, p1, p2) {
        var a1 = p1.x - p0.x;
        var b1 = p1.y - p0.y;
        var a2 = p2.x - p1.x;
        var b2 = p2.y - p1.y;
        var l1 = Math.sqrt(a1 * a1 + b1 * b1);
        var l2 = Math.sqrt(a2 * a2 + b2 * b2);
        return Math.acos((a1 * a2 + b1 * b2) / (l1 * l2));
    }

    function getOuterPoint(p0, p1) {
        var dx = p1.x - p0.x;
        var dy = p1.y - p0.y;

        if(dy === 0) return {
            x: (dx < 0 ? 0 : svg.width),
            y: p0.y
        };
        if(dx === 0) return {
            x: p0.x,
            y: (dy < 0 ? 0 : svg.height)
        };
        if(dy < 0 && 0 <= p0.x - p0.y * dx / dy <= svg.width) return {
            x: p0.x - p0.y * dx / dy,
            y: 0
        };
        if(dx < 0 && 0 <= p0.y - p0.x * dy / dx <= svg.height) return {
            x: 0,
            y: p0.y - p0.x * dy / dx
        };
        if(dx > 0 && 0 <= p0.y + (svg.width - p0.x) * dy / dx <= svg.height) return {
            x: svg.width,
            y: p0.y+(svg.width - p0.x) * dy / dx
        };
        if(dy > 0 && 0 <= p0.x + (svg.height - p0.y) * dx / dy <= svg.width) return {
            x: p0.x + (svg.height - p0.y) * dx / dy,
            y: svg.height
        };
    }

    //
    // X座標の重心点.
    //
    function getCenterX(points) {
        var cx = 0;
        points.forEach(function(d, i) {
            cx = cx + d.x;
        });
        cx = cx / points.length;
        return cx;
    }

    //
    // Y座標の重心点.
    //
    function getCenterY(points) {
        var cy = 0;
        points.forEach(function(d, i) {
            cy = cy + d.y;
        });
        cy = cy / points.length - sliceLabelHeight; // フォント高さ分だけ控除
        return cy;
    }

})();

