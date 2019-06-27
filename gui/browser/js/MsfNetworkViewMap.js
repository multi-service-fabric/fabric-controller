//
// ネットワークビュークラス.
//   initMultiClusterView():      初期化
//   addCluster(obj)              クラスタの登録
//   removeCluster(id)            クラスタの削除
//   addClusterLink(id)           クラスタ間リンク線の登録(両方)
//   removeClusterLink(id)        クラスタ間リンク線の削除(両方)
//   addClusterLinkLine(obj)      クラスタ間リンク線の登録(片方)
//   removeClusterLinkLine(id,id) クラスタ間リンク線の削除(片方)
//   addL2Slice(obj)              L2スライスの登録
//   removeL2Slice(id)            L2スライスの削除
//   addL3Slice(obj)              L3スライスの登録
//   removeL3Slice(id)            L3スライスの削除
//
//   getClusterById(id)              クラスタの取得
//   getClusterLinkById(id)          クラスタ間リンクの取得
//   getClusterLinkLineById(src,dst) クラスタ間リンク線の取得
//   getL2SliceById(id)              L2スライスの取得
//   getL3SliceById(id)              L3スライスの取得
//
//   getSelectedCluster()            選択中クラスタIDの取得
//   getSelectedClusterLink()        選択中クラスタ間リンクの取得
//   getSelectedClusterLinkLine()    選択中クラスタ間リンク線の取得
//   getSelectedL2Slice()            選択中L2スライスの取得
//   getSelectedL3Slice()            選択中L3スライスの取得
//
//   unSelectedAll()                 図形の選択解除
//

// Trafficの凡例
const trafficViewLegendX = 640;
const trafficViewLegendY = 30;
const trafficViewLegendW = 20;
const trafficViewLegendH = 12;
const trafficViewLegendVSpacer = 12;//16;
const trafficViewLegendHSpacer = 5;
const trafficViewLegendFontHeight = 18;//22;

(function() {
"use strict";

    //
    // コンストラクタ.
    //   w,h: キャンバスの幅・高さ
    //
    MSF.NetworkView = function(w, h) {

        this.clusters = [];
        this.clusterLinks = [];
        this.clusterLinkLines = [];
        this.sliceL2s = [];
        this.sliceL3s = [];

        // キャンバス全体の大きさ
        this.width = w;
        this.height = h;

        // キャンバス内で図形ドラッグ可能な領域
        this.dragArea = {
            minX: 8,
            minY: 2,
            maxX: w - 8,
            maxY: h - 8
        };

        // キャンバス描画 (htmlのdiv内に埋込SVGのDOM追加)
        this.svg = new SVG(document.querySelector("#multiClusterCanvas")).size(w, h);
        this.svg.attr({
            width: "100%",
            "max-width": w +"px",
            height: "100%",
            "max-height": h + "px"
        });
        this.svg.addClass("width-100-percent");

        // キャンバスの枠線
        this.baseArea = this.svg.rect(w, h).attr({fill: "#fcfcfc", stroke: "none"});

        // キャンバスクリック時、他図形の選択解除
        var svgCanvas = this;
        this.baseArea.click(function(){
            svgCanvas.unSelectedAll();
            MSF.main.updateDetailTable();
        });
        // キャンバスタッチ時、他図形の選択解除
        this.baseArea.touchstart(function(){
            svgCanvas.unSelectedAll();
            MSF.main.updateDetailTable();
        });

    }
    ;

    //
    // 初期化(マルチクラスタ表示モード).
    //
    MSF.NetworkView.prototype.initMultiClusterView = function() {
        // キャンバスに図形レイヤを追加(スライス用、線、マーカー、図)
        this.hides   = this.svg.group();
        this.slices  = this.svg.group();
        this.links   = this.svg.group();
        this.nodes   = this.svg.group();
        this.labels  = this.svg.group();
        // キャンバスに図形レイヤを追加(凡例)
        this.legend  = this.svg.group();
        this.drawColorLegend();
    }
    ;

    //
    // クラスタ追加.
    //   node: 追加対象のクラスタオブジェクト.
    //
    MSF.NetworkView.prototype.addCluster = function(node) {
        this.clusters.push(node);
    }
    ;

    //
    // クラスタ削除.
    //   id: 削除対象のクラスタID.
    //
    MSF.NetworkView.prototype.removeCluster = function(id) {
        // スライス(多角形)からクラスタ削除

        // クラスタリンク図形(線)の削除
        var links = this.clusterLinkLines;
        links.forEach(function(obj, i){
            if ((obj.local.id == id) || (obj.opposite.id == id)) {
                svg.removeClusterLinkLine(obj.local.id, obj.opposite.id);
            }
        });
        // クラスタ図形(六角形)の削除
        var node = this.getClusterById(id);
        if (node != null) {
            node.svgGroup.clear();
            node.svgGroup.remove();
            node = null;
        }
        // キャンバスのメンバから削除
        var cc = this.clusters;
        this.clusters = cc.filter(function(obj) {
            return obj.id != id;
        });
    }
    ;

    //
    // クラスタリンクの追加.
    //   lines: 追加対象のクラスタリンクオブジェクト.
    //
    MSF.NetworkView.prototype.addClusterLink = function(lines) {
        this.clusterLinks.push(lines);
        this.addClusterLinkLine(lines.local);
        this.addClusterLinkLine(lines.opposite);
    }
    ;

    //
    // クラスタリンクの削除.
    //   lines: 削除対象のクラスタリンクID.
    //
    MSF.NetworkView.prototype.removeClusterLink = function(id) {
        // クラスタリンク(線)の削除
        var links = svg.getClusterLinkById(id);
        if (links != null) {
            svg.removeClusterLinkLine(links.localId, links.oppositeId);
            svg.removeClusterLinkLine(links.oppositeId, links.localId);
            links = null;
        }
        // キャンバスのメンバから削除
        var cc = this.clusterLinks;
        this.clusterLinks = cc.filter(function(obj) {
            return (obj.id != id);
        });
    }
    ;

    //
    // クラスタリンク線の追加.
    //   line: 追加対象のクラスタリンク線オブジェクト.
    //
    MSF.NetworkView.prototype.addClusterLinkLine = function(line) {
        this.clusterLinkLines.push(line);
    }
    ;

    //
    // クラスタリンク線の削除.
    //   srcId: local側クラスタID
    //   dstId: opposite側クラスタID
    //
    MSF.NetworkView.prototype.removeClusterLinkLine = function(srcId, dstId) {
        // クラスタリンク図形(線)の削除
        var link = svg.getClusterLinkLineById(srcId, dstId);
        if (link != null) {
            link.line.remove();
            link.shadow.remove();
            link = null;
        }
        // キャンバスのメンバから削除
        var cc = this.clusterLinkLines;
        this.clusterLinkLines = cc.filter(function(obj) {
            return (!((obj.local.id == srcId) && (obj.opposite.id == dstId)));
        });
    }
    ;

    //
    // L2スライスの追加.
    //   slice: 追加対象のL2スライス・オブジェクト.
    //
    MSF.NetworkView.prototype.addL2Slice = function(slice) {
        this.sliceL2s.push(slice);
    }
    ;

    //
    // L2スライスの削除.
    //   id: 削除対象のスライスID
    //
    MSF.NetworkView.prototype.removeL2Slice = function(id) {
        // スライス図形(多角形)の削除
        var slice = svg.getL2SliceById(id);
        if (slice && slice.slice) {
            slice.slice.remove();
            slice.label.remove();
            slice.warn1.remove();
            slice.warn2.remove();
            slice = null;
        }
        // キャンバスのメンバから削除
        var sliceL2s = this.sliceL2s;
        for (var i = 0; i < sliceL2s.length; i++) {
            if (sliceL2s[i].id == id) {
                this.sliceL2s.splice(i, 1);
                return;
            }
        }
    }
    ;

    //
    // L3スライスの追加.
    //   slice: 追加対象のL2スライス・オブジェクト.
    //
    MSF.NetworkView.prototype.addL3Slice = function(slice) {
        this.sliceL3s.push(slice);
    }
    ;

    //
    // L3スライスの削除.
    //   id: 削除対象のスライスID.
    //
    MSF.NetworkView.prototype.removeL3Slice = function(id) {
        // スライス図形(多角形)の削除
        var slice = svg.getL3SliceById(id);
        if (slice && slice.slice) {
            slice.slice.remove();
            slice.label.remove();
            slice.warn1.remove();
            slice.warn2.remove();
            slice = null;
        }
        // キャンバスのメンバから削除
        var sliceL2s = this.sliceL2s;
        for (var i = 0; i < sliceL2s.length; i++) {
            if (sliceL2s[i].id == id) {
                this.sliceL2s.splice(i, 1);
                return;
            }
        }
    }
    ;

    //
    // 図形選択解除.
    //
    MSF.NetworkView.prototype.unSelectedAll = function() {
        // クラスタの選択解除
        this.clusters.forEach(function(obj, i){
            obj.selected(false);
        });

        // クラスタ間リンクの選択解除
        this.clusterLinkLines.forEach(function(obj, i) {
            obj.selected(false);
        });

        // L2スライスの選択解除
        this.sliceL2s.forEach(function(obj, i) {
            obj.selected(false);
        });

        // L3スライスの選択解除
        this.sliceL3s.forEach(function(obj, i) {
            obj.selected(false);
        });

    }
    ;

    //
    // クラスタの取得.
    //   id: クラスタID
    //
    MSF.NetworkView.prototype.getClusterById = function(id) {
        var result = null;
        this.clusters.forEach(function(obj, i) {
            if (obj.id == id) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // クラスタ間リンクの取得.
    //   id: クラスタ間リンクID
    //
    MSF.NetworkView.prototype.getClusterLinkById = function(id) {
        var result = null;
        this.clusterLinks.forEach(function(obj, i) {
            if (obj.id === id) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // クラスタ間リンク線の取得.
    //   src: ローカル側クラスタID
    //   dst: 対向側クラスタID
    //
    MSF.NetworkView.prototype.getClusterLinkLineById = function(src, dst) {
        var result = null;
        this.clusterLinkLines.forEach(function(obj, i) {
            if ((obj.local.id === src) && (obj.opposite.id === dst)) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // L2スライスの取得.
    //   id: スライスID
    //
    MSF.NetworkView.prototype.getL2SliceById = function(id) {
        var result = null;
        this.sliceL2s.forEach(function(obj, i) {
            if (obj.id === id) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // L3スライスの取得.
    //   id: スライスID
    //
    MSF.NetworkView.prototype.getL3SliceById = function(id) {
        var result = null;
        this.sliceL3s.forEach(function(obj, i) {
            if (obj.id === id) {
                result = obj;
            }
        });
        return result;
    }
    ;

    //
    // 選択中クラスタIDの取得.
    //   return: クラスタID
    //
    MSF.NetworkView.prototype.getSelectedCluster = function() {
        var result = null;
        this.clusters.forEach(function(obj, i) {
            if (obj.isSelected()) {
                result = obj.id;
            }
        });
        return result;
    }
    ;

    //
    // 選択中クラスタ間リンクの取得.
    //   return: クラスタ間リンクID
    //
    MSF.NetworkView.prototype.getSelectedClusterLink = function() {
        var result = null;
        var lineIds = this.getSelectedClusterLinkLine();
        if (lineIds != null) {
            var line = this.getClusterLinkLineById(lineIds.local, lineIds.opposite);
            if (line != null) {
                result = line.parentId;
            }
        }
        return result;
    }
    ;

    //
    // 選択中クラスタ間リンク線の取得.
    //   return: { local: クラスタID, opposite: クラスタID }
    //
    MSF.NetworkView.prototype.getSelectedClusterLinkLine = function() {
        var result = { "local": null, "opposite": null };
        this.clusterLinkLines.forEach(function(obj, i) {
            if (obj.isSelected()) {
                var id1 = obj.local.id;
                var id2 = obj.opposite.id;
                result = { "local": obj.local.id, "opposite": obj.opposite.id };
            }
        });
        return result;
    }
    ;

    //
    // 選択中L2スライスの取得.
    //
    MSF.NetworkView.prototype.getSelectedL2Slice = function() {
        var result = null;
        this.sliceL2s.forEach(function(obj, i) {
            if (obj.isSelected()) {
                result = obj.id;
            }
        });
        return result;
    }
    ;

    //
    // 選択中L3スライスの取得.
    //
    MSF.NetworkView.prototype.getSelectedL3Slice = function() {
        var result = null;
        this.sliceL3s.forEach(function(obj, i) {
            if (obj.isSelected()) {
                result = obj.id;
            }
        });
        return result;
    }
    ;

    //
    // クラスタ図形のドラッグ可能領域の表示.
    //
    MSF.NetworkView.prototype.showDraggableArea = function(isShow) {
        if (isShow) {
            this.baseArea.attr({stroke: "#a0b0f0", "stroke-dasharray": "3,3"});
        } else {
            this.baseArea.attr({stroke: "none"});
        }
    }
    ;
    //
    // 凡例描画
    //
    MSF.NetworkView.prototype.drawColorLegend = function() {
        var x   = trafficViewLegendX;
        var y   = trafficViewLegendY;
        var w   = trafficViewLegendW;
        var h   = trafficViewLegendH;
        var vsp = trafficViewLegendVSpacer;
        var hsp = trafficViewLegendHSpacer;
        var fh  = trafficViewLegendFontHeight;

        var view = this.legend;

        for (var i = 0; i < trafficColor.length; i++) {
            // 取得するIndexを反転させる
            var index = (trafficColor.length - 1 ) - i;
            // 凡例
            var rct = view.rect(w, h).dx(x).dy(y+vsp*i);
            rct.attr({"fill" : trafficColor[index]});
            // 凡例ラベル
            var lbl = view.plain(trafficBoundary[index]);
            lbl.font({anchor: "left"});
            lbl.dx(x+w+hsp).dy(y+vsp*i+fh);
            lbl.addClass(nodeViewCssLegendLabelClass);
        }
        // 0 ラベル
        var zLbl = view.plain("0");
        zLbl.font({anchor: "left"});
        zLbl.dx(x+w+hsp).dy(y+vsp*trafficColor.length+fh);
        zLbl.addClass(nodeViewCssLegendLabelClass);
        // 表示単位ラベル
        var pLbl = view.plain("[%]");
        pLbl.font({anchor: "left"});
        pLbl.dx(x+w+hsp*2).dy(y+vsp*(trafficColor.length+1)+fh);
        pLbl.addClass(nodeViewCssLegendLabelClass);
    }
    ;

})();
