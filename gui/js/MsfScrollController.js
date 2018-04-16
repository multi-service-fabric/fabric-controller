//
// 十字キーパネルクラス
//

// 十字キーパネルの座標
var dragStartX;
var dragStartY;
// タッチデバイスでドラッグか否か
var isTouchDrag = false;
// アニメーション時間(ms)
var crossKeySpeed = 100;

// 十字キーパネルのドラッグ領域(上)
const draggableOffsetTop = parseInt($("#navi1").css("height"),10) +
    parseInt($("#navi1").css("top"), 10) +
    parseInt($("#navi1").css("margin-bottom"), 10) + 4;
// 十字キーパネルのドラッグ領域(左)
const draggableOffsetLeft = 2;

$(document).ready(function(){

    // Viewのドラッグイベント登録
    addViewDragEvent("#multiClusterView", "#multiClusterCanvas");
    addViewDragEvent("#figure", "#dragFrame");
    addViewDragEvent("#nodeView", "#nodeCanvas");
    addViewDragEvent("#controllerView", "#controllerCanvas");

    // 十字キーの利用
    if (MSF.Conf.SCROLL_ENABLE) {
        $("#crossKeyPanel").css("visibility", "visible");
        // 十字キーパネルの初期位置設定
        var h = parseInt($("#crossKeyPanel").css("height"), 10);
        $("#crossKeyPanel").css("top", (parseInt($("main").height(), 10) - 5 - h) + "px");

        addCrossKeyEvent();
    }else{
        $("#crossKeyPanel").css("visibility", "hidden");
    }
});

//
// 十字キーのイベント登録
//
function addCrossKeyEvent() {

    // 上ボタン押下時
    $("#crossKeyTop").click(function(evt) {
        var panel = getCurFigureMap();
        var dy = MSF.Conf.SCROLL_SIZE.TOP;
        var y = parseInt(panel.css("top"), 10);
        panel.animate({top: (y+dy)+"px"}, crossKeySpeed);
    });

    // 右ボタン押下時
    $("#crossKeyRight").click(function(evt) {
        var panel = getCurFigureMap();
        var dx = MSF.Conf.SCROLL_SIZE.RIGHT;
        var x = parseInt(panel.css("left"), 10);
        panel.animate({left: (x+dx)+"px"}, crossKeySpeed);
    });

    // 下ボタン押下時
    $("#crossKeyBottom").click(function(evt) {
        var panel = getCurFigureMap();
        var dy = MSF.Conf.SCROLL_SIZE.BOTTOM;
        var y = parseInt(panel.css("top"), 10);
        panel.animate({top: (y+dy)+"px"}, crossKeySpeed);
    });

    // 左ボタン押下時
    $("#crossKeyLeft").click(function(evt) {
        var panel = getCurFigureMap();
        var dx = MSF.Conf.SCROLL_SIZE.LEFT;
        var x = parseInt(panel.css("left"), 10);
        panel.animate({left: (x+dx)+"px"}, crossKeySpeed);
    });

    // ○ボタン押下時
    $("#crossKeyCenter").click(function(evt) {
        var panel = getCurFigureMap();
        panel.animate({top:0, left:0}, crossKeySpeed);
    });

    // 十字キーパネルのドラッグ開始時、座標を保持
    $("#crossKeyPanel").on("dragstart", function(evt) {
        dragStartX = evt.clientX - parseInt($("#crossKeyPanel").css("left"), 10);
        dragStartY = evt.clientY - parseInt($("#crossKeyPanel").css("top"), 10);
    });

    // 十字キーパネルのドラッグ終了時、指定座標にパネル移動
    $("#crossKeyPanel").on("dragend", function(evt) {

        var leftPos = evt.clientX - dragStartX;
        var topPos = evt.clientY - dragStartY;
        var w = parseInt($("#crossKeyPanel").css("width"), 10);
        var h = parseInt($("#crossKeyPanel").css("height"), 10);
        var limitRight = parseInt($("main").width(), 10) - parseInt($("#detail").width(), 10) - 12 - w;
        var limitBottom = parseInt($("main").height(), 10) - 5 - h;

        // 領域外にドラッグした場合は、領域内に制限
        if (leftPos < draggableOffsetLeft) {
            leftPos = draggableOffsetLeft;
        }
        if (leftPos > limitRight) {
            leftPos = limitRight;
        }
        if (topPos < draggableOffsetTop) {
            topPos = draggableOffsetTop;
        }
        if (topPos > limitBottom) {
            topPos = limitBottom;
        }

        // パネルの座標変更
        $("#crossKeyPanel").css("left", leftPos + "px");
        $("#crossKeyPanel").css("top", topPos + "px");
    });

    // ダブルクリック時、半透明/非透明の切り替え
    $(".crossKeyNone").dblclick(function(evt) {
       var opacity = $("#crossKeyPanel").css("opacity");
       if (opacity > 0.4) {
           $("#crossKeyPanel").animate({opacity: 0.1}, crossKeySpeed);
       }else{
           $("#crossKeyPanel").animate({opacity: 0.7}, crossKeySpeed);
       }
    });
}


//
// Viewのドラッグイベント登録
//
function addViewDragEvent(dragArea, moveTarget) {

    // キャンバスのドラッグ開始時、座標を保持
    $(dragArea).on("dragstart", function(evt) {
        dragStartX = evt.clientX - parseInt($(moveTarget).css("left"), 10);
        dragStartY = evt.clientY - parseInt($(moveTarget).css("top"), 10);
        $(dragArea).attr({cursor: "move"});
        evt.originalEvent.dataTransfer.setData("text", "move");
        evt.originalEvent.dataTransfer.effectAllowed = "move";
    });

    // キャンバスのドラッグ終了時、指定座標にパネル移動
    $(dragArea).on("dragend", function(evt) {
        var leftPos = evt.clientX - dragStartX;
        var topPos = evt.clientY - dragStartY;
        $(moveTarget).animate({top: topPos + "px", left: leftPos + "px"}, crossKeySpeed);
    });

    $(dragArea).parent().on("dragover", function(evt) {
        evt.preventDefault();
        evt.originalEvent.dataTransfer.dropEffect = "move";
    });
    // 同タッチ対応
    $(dragArea).bind({
        "touchstart": function(evt) {
            // ページ遷移イベントを抑止
            evt.preventDefault();
            // ドラッグ開始座標を保持
            var x = event.changedTouches[0].pageX;
            var y = event.changedTouches[0].pageY;
            dragStartX = x - parseInt($(moveTarget).css("left"), 10);
            dragStartY = y - parseInt($(moveTarget).css("top"), 10);
            this.dragged = false;
        },
        "touchmove": function(evt) {
            // ページ遷移イベントを抑止
            evt.preventDefault();
            if (enableCanvasDrag(dragArea)) {
                this.dragged = true;
            } else {
                this.dragged = false;
                console.log("cancelMove at touchstart");
            }
            if (!this.touched) return;
        },
        "touchend": function(evt) {
            // ページ遷移イベントを抑止
            evt.preventDefault();
            if (!this.dragged) return;
            this.touched = false;
            // マルチクラスタViewの場合、クラスタドラッグとの処理競合回避
            if (!enableCanvasDrag(dragArea)) {
                console.log("cancelMove at touchend");
                return;
            }
            var x = event.changedTouches[0].pageX;
            var y = event.changedTouches[0].pageY;
            var leftPos = x - dragStartX;
            var topPos = y - dragStartY;
            $(moveTarget).animate({top: topPos + "px", left: leftPos + "px"}, crossKeySpeed);
        }
    });

}

// キャンバスの移動可否を返す.
function enableCanvasDrag(dragArea) {
    // マルチクラスタView以外は、移動可能
    if (dragArea != "#multiClusterView") return true;

    // マルチクラスタViewの場合
    // クラスタ選択していなければ、移動不可
    var cId = svg.getSelectedCluster();
    if (cId == null) return true;

    // クラスタ図形選択中の場合
    // 選択中クラスタがドラッグ中ではないので、移動可能
    if (!svg.getClusterById(cId).isTouchDrag) return true;

    // 選択中クラスタがドラッグ中なのでキャンバスドラッグ禁止
    return false;
}

// 表示中の図領域のスクロール領域を返す
function getCurFigureMap(){
    if ($("#multiClusterView").is(":visible")) {
        return $("#multiClusterCanvas");
    }
    if ($("#figure").is(":visible")) {
        return $("#dragFrame");
    }
    if ($("#nodeView").is(":visible")) {
        return $("#nodeCanvas");
    }
    if ($("#controllerView").is(":visible")) {
        return $("#controllerCanvas");
    }
}
// TODO: 画面リサイズ時に十字キーパネルの位置調整
