//
// クラスタ間リンククラス
// MSF.MsfClusterLink
//   init():                     初期化
//   setColor(src, dst, color):  線色の変更
//
// クラスタ間リンク線クラス
// MSF.MsfClusterLinkLine
//   init():                     初期化
//   update():                   座標更新
//   setColor(color):            線色の変更
//   selected(true/false):       図の選択
//   isSelected()                選択中か否か
//
// グローバル関数
// trafficDemoOn()
// trafficUpdateDemo()
// trafficDemoOff()
//
// trafficAnimationStart()       トラヒック送信アニメーション
//

// 線分の属性
const ClusterLinkLineAttr = {
    stroke: "#c0c0c0",
    "stroke-width": 2,
    "stroke-dasharray": "none"
};
// マウス選択用の見えない太い線の属性
var ClusterLinkShadowAttr = {
    stroke: "#c0c0c0",
    "stroke-width": 30,
    opacity: 0.0
};
// 線の選択・非選択時の属性
const ClusterLinkLineSelected = {"stroke-width": 6};
const ClusterLinkLineUnSelected = {"stroke-width": 2};
const ClusterLinkLineNoAnimation = {
    stroke: "#c0c0c0",
    "stroke-dasharray": "none"
};
// トラヒックが流れるアニメーションの速度(ミリ秒)
const trafficAnimSpeed = 80;
// トラヒックが流れるアニメーション用の一点鎖線表現
const trafficAnimDashArray = [
    {"stroke-dasharray": "1,0.5,0,0.5,9,0,0,0"},
    {"stroke-dasharray": "2,0.5,0,0.5,8,0,0,0"},
    {"stroke-dasharray": "3,0.5,0,0.5,7,0,0,0"},
    {"stroke-dasharray": "4,0.5,0,0.5,6,0,0,0"},
    {"stroke-dasharray": "5,0.5,0,0.5,5,0,0,0"},
    {"stroke-dasharray": "6,0.5,0,0.5,4,0,0,0"},
    {"stroke-dasharray": "7,0.5,0,0.5,3,0,0,0"},
    {"stroke-dasharray": "8,0.5,0,0.5,2,0,0,0"},
    {"stroke-dasharray": "9,0.5,0,0.5,1,0,0,0"},
    {"stroke-dasharray": "10,0.5,0,0.5,0,0,0,0"}
];
// 一点鎖線選択要のカウンタ
var trafficAnimCnt = 0;
// トラヒック表示状態
var isTrafficDemoOn = false;
var isTrafficOn = false;

(function() {
"use strict";

    //
    // コンストラクタ
    //   id: クラスタリンクID
    //   localId: 自身のクラスタID
    //   oppositeId: 対向のクラスタID
    //
    MSF.MsfClusterLink = function(id, localId, oppositeId) {
        // クラスタオブジェクトの取得
        var localCluster = svg.getClusterById(localId);
        var oppositeCluster = svg.getClusterById(oppositeId);
        // クラスタ間リンク線の生成
        var local = new MSF.MsfClusterLinkLine(id, localCluster, oppositeCluster);
        // var opposite = new MSF.MsfClusterLinkLine(id, oppositeCluster, localCluster);

        // 初期化.
        local.init();
        // opposite.init();

        // ドラッグイベント登録.
        localCluster.svgData.obj.on("dragmove", function(){local.update();});
        // localCluster.svgData.obj.on("dragmove", function(){opposite.update();});
        oppositeCluster.svgData.obj.on("dragmove", function(){local.update();});
        // oppositeCluster.svgData.obj.on("dragmove", function(){opposite.update();});

        // クリックイベント登録.
        local.shadow.click(function() {
            if (svg != null) svg.unSelectedAll();
            // 選択状態
            local.selected(true);
            MSF.main.updateDetailTable();
        });
        // タッチイベント登録.
        local.shadow.touchstart(function(evt) {
            evt.preventDefault();
            if (svg != null) svg.unSelectedAll();
            // 選択状態
            local.selected(true);
            MSF.main.updateDetailTable();

        });

        //ネットワークビューに登録
        // var links = this;
        if (svg != null) {
            // svg.addClusterLink(links);
            svg.addClusterLinkLine(local);
        }
    }
    ;

    //
    // クラスタ間リンク色の更新(色はトラヒック量を表現).
    //   localId: 自身のクラスタID
    //   oppositeId: 対向のクラスタID
    //   strokeColor: 線色
    //
    MSF.MsfClusterLink.prototype.setColor = function(localId, oppositeId, strokeColor) {
        if (localId == this.localId) {
            this.local.setColor(strokeColor);
        } else {
            this.opposite.setColor(strokeColor);
        }
    }
    ;

})();

//
// クラスタ間リンク線クラス
//
(function() {
"use strict";

    //
    // コンストラクタ.
    //   parentId: クラスタ間リンクID
    //   local: 自身のクラスタオブジェクト
    //   opposite: 対向のクラスタオブジェクト
    //
    MSF.MsfClusterLinkLine = function(parentId, local, opposite) {
        this.parentId = parentId;
        this.local = local;
        this.opposite = opposite;
        this.line = null;
        this.shadow = null;
        this.select = false;
    }
    ;

    //
    // 初期化.
    // 線図形の描画.自身から対向向けの半分の線分を描画
    //
    MSF.MsfClusterLinkLine.prototype.init = function() {
        // 自身側の座標
        var x0 = this.local.svgData.obj.transform().x + this.local.width / 2;
        var y0 = this.local.svgData.obj.transform().y + this.local.height / 2;
        // 対向側の座標
        var x1 = this.opposite.svgData.obj.transform().x + this.local.width / 2;
        var y1 = this.opposite.svgData.obj.transform().y + this.local.height / 2;
        // 中心側の座標
        var x2 = (x0 + x1) / 2;
        var y2 = (y0 + y1) / 2;
        // 線分の描画
        this.line = svg.links.line(x0, y0, x2, y2).attr(ClusterLinkLineAttr);
        // マウス選択用に見えない太い線を用意
        this.shadow = svg.links.line(x0, y0, x2, y2).attr(ClusterLinkShadowAttr);
    }
    ;

    //
    // 更新.
    // 線図形の座標更新. クラスタ図形(六角形)ドラッグイベントでコールされる
    //
    MSF.MsfClusterLinkLine.prototype.update = function() {
        // 自身側の座標
        var x0 = this.local.svgData.obj.transform().x + this.local.width / 2;
        var y0 = this.local.svgData.obj.transform().y + this.local.height / 2;
        // 対向側の座標
        var x1 = this.opposite.svgData.obj.transform().x + this.local.width / 2;
        var y1 = this.opposite.svgData.obj.transform().y + this.local.height / 2;
        // 中心側の座標
        var x2 = (x0 + x1) / 2;
        var y2 = (y0 + y1) / 2;
        // 座標の更新
        this.line.plot(x0, y0, x2, y2);
        this.shadow.plot(x0, y0, x2, y2);
    }
    ;

    //
    // クラスタ間リンク色の更新(色はトラヒック量を表現).
    //   strokeColor: 線色
    //
    MSF.MsfClusterLinkLine.prototype.setColor = function(strokeColor) {
        this.line.attr({stroke: strokeColor});
    }
    ;

    //
    // クラスタ間リンクの選択.
    //
    MSF.MsfClusterLinkLine.prototype.selected = function(isSelect) {
        if (isSelect) {
            this.line.attr(ClusterLinkLineSelected);
            this.select = true;
        } else {
            this.line.attr(ClusterLinkLineUnSelected);
            this.select = false;
        }
    }
    ;

    //
    // クラスタ間リンクの選択有無.
    //
    MSF.MsfClusterLinkLine.prototype.isSelected = function() {
        return this.select;
    }
    ;

})();

// トラヒックアニメーションの開始
function trafficAnimationOn(){
    isTrafficOn = true;
    if (svg != null) {
        trafficcAnimationInit();
    }
}
//トラヒック初期化
function trafficcAnimationInit(){
    if (isTrafficOn) {
        var clusterLinkLines = svg.clusterLinkLines;
        clusterLinkLines.forEach(function(obj, i) {
            obj.setColor(getTrafficColor());
        });
    }else{
        trafficAnimationOff();
    }
}

//
// トラヒックアニメーションの設定
//
function trafficAnimationUpdate(srcId, dstId, traffic, type){
    if (svg != null) {
       var clusterLinkLine = svg.getClusterLinkLineById(srcId, dstId);
       if (clusterLinkLine != null) {
            clusterLinkLine.setColor(getTrafficColor(traffic, type));
       }
    }
}

//
// トラヒックアニメーションの停止
//
function trafficAnimationOff(){
    isTrafficOn = false;
    if (svg != null) {
       var clusterLinkLines = svg.clusterLinkLines;
       clusterLinkLines.forEach(function(obj, i) {
           obj.line.attr(ClusterLinkLineNoAnimation);
       });
    }
}

//
// トラヒックが流れているアニメーション.
// 一点鎖線のパターンを時間変化させて送信トラヒックが流れるイメージを表現.
//
function trafficAnimationStart(){
    if (isTrafficOn) {
        trafficAnimCnt = trafficAnimCnt > trafficAnimDashArray.length ? 0 : trafficAnimCnt + 1;
        var clusterLinkLines = svg.clusterLinkLines;
        if (clusterLinkLines != null) {
            clusterLinkLines.forEach(function(obj, i) {
                obj.line.attr(trafficAnimDashArray[trafficAnimCnt]);
            });
        }
    }
    setTimeout(trafficAnimationStart, trafficAnimSpeed);
}
