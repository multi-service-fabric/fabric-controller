//
// MSFキャンバスフレームクラス
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfCanvasFrame = function(clipFrame, dragFrame, msfCanvas, conf) {
        // オブジェクト保持
        this.clipFrame = clipFrame;
        this.dragFrame = dragFrame;
        this.msfCanvas = msfCanvas;
        this.canvasSize = {
            width: this.clipFrame.clientWidth,
            height: this.clipFrame.clientHeight
        };
        this.Conf = conf.MsfCanvasFrame;
        this.msfCanvas.scale = this.Conf.Canvas.SCALE_DEFAULT;
        this.supportTouch = 'ontouchend'in document;
        this.isResizeExec = false;
        this.isMousewheelExec = false;
        // ドラック情報
        this.dragInfo = {
            clickPoint: null,
            prevLocation: null,
            dragArea: null
        };
    }
    ;
    //
    // 初期化処理
    //
    MSF.MsfCanvasFrame.prototype.init = function() {
        this.setClipFrameSize();
        this.setCanvasBounds();
        this.msfCanvas.init();
        this.dragFrame.style.left = "0px";
        this.dragFrame.style.top = "0px";
    }
    ;
    //
    // デバイスモード表示
    //
    MSF.MsfCanvasFrame.prototype.showDeviceMode = function() {
        this.msfCanvas.DispMode = this.msfCanvas.DeviceMode;
        this.msfCanvas.syncFocusedDevice();
        this.msfCanvas.PhysicalSliceInfo.isVisible = true;
        this.updateSelectedSlice();
        $("#filterArea").css("visibility","visible");
    }
    ;
    //
    // クラスターモード表示
    //
    MSF.MsfCanvasFrame.prototype.showClusterMode = function() {
        this.msfCanvas.DispMode = this.msfCanvas.ClusterMode;
        this.msfCanvas.syncFocusedDevice();
        this.msfCanvas.PhysicalSliceInfo.isVisible = true;
        this.updateSelectedSlice();
        $("#filterArea").css("visibility","visible");
    }
    ;
    //
    // 選択スライス更新処理
    //
    MSF.MsfCanvasFrame.prototype.updateSelectedSlice = function(checkedSliceIDList) {
        // レイアウト計算
        this.msfCanvas.calcLayout();
        // フォーカス更新
        this.msfCanvas.updateFocus();
        // 描画処理
        this.msfCanvas.Draw();
    }
    ;
    //
    // レイアウト計算処理
    //
    MSF.MsfCanvasFrame.prototype.calcLayout = function(inf) {
        var size = this.msfCanvas.calcLayout();
        // キャンバスサイズ設定
        this.setCanvasBounds(size);
    }
    ;
    //
    // クリップフレームのサイズ計算処理
    // ウィンドウサイズからメニュー等他の部品を引いた残りをフレームとする
    //
    MSF.MsfCanvasFrame.prototype.setClipFrameSize = function() {
        // MSFMainが担当するようになった
    }
    ;
    //
    // ユーザーがText等を選択できるか設定する処理
    //
    MSF.MsfCanvasFrame.prototype.setUserSelect = function(isSelect) {
        var act = isSelect ? "auto" : "none";
        var body = $("body");
        body.css("user-select", act);
        body.css("-webkit-user-select", act);
        body.css("-moz-user-select", act);
        body.css("-ms-user-select", act);
        body.css("-o-user-select", act);
    }
    ;
    //
    // スケール設定処理
    // scaleDirection 1:拡大　-1:縮小
    //
    MSF.MsfCanvasFrame.prototype.SetScale = function(scaleDirection) {
        var scale = this.msfCanvas.scale;
        var moveStep = 30;
        if (scaleDirection) {
            if (scaleDirection > 0) {
                scale += this.Conf.Canvas.SCALE_STEP;
            } else {
                scale -= this.Conf.Canvas.SCALE_STEP;
            }
            scale = Math.round(scale * 10) / 10;
        }
        // スケーリング上限下限設定
        if (scale < this.Conf.Canvas.SCALE_MIN) {
            var left = parseInt(this.dragFrame.style.left, 10);
            var top = parseInt(this.dragFrame.style.top, 10);
            // 縮小限界にきたらロケーション0,0に向かって移動
            if (left < 0) {
                left = Math.min(left + moveStep, 0);
                this.dragFrame.style.left = left + "px";
            }
            if (top < 0) {
                top = Math.min(top + moveStep, 0);
                this.dragFrame.style.top = top + "px";
            }
        } else if (this.Conf.Canvas.SCALE_MAX < scale) {
            // 拡大限界は特に処理なし
        } else {
            this.msfCanvas.scale = scale;
        }
    }
    ;
    //
    // キャンバスの位置、サイズ、移動範囲設定処理
    //
    MSF.MsfCanvasFrame.prototype.setCanvasBounds = function(size) {
        // ドキュメント基準に変換
        var clip = this.clipFrame.getBoundingClientRect();
        clip.x = clip.left + window.pageXOffset;
        clip.y = clip.top + window.pageYOffset;
        if (size) {
            this.canvasSize = size;
        } else {
            size = this.canvasSize;
        }
        // キャンバスのスケーリングサイズ計算
        // 計算結果設定 ただしキャンバスを格納しているDivより小さくしない
        var width = Math.max(size.width * this.msfCanvas.scale, clip.width);
        var height = Math.max(size.height * this.msfCanvas.scale, clip.height);
        this.dragFrame.style.width = width + "px";
        this.dragFrame.style.height = height + "px";
        this.msfCanvas.mainCanvas.width = width;
        this.msfCanvas.mainCanvas.height = height;
        this.msfCanvas.animationCanvas.width = width;
        this.msfCanvas.animationCanvas.height = height;
        // 拡大かつスクロールしている場合からの縮小時にキャンバスの端がDivに入らないようにキャンバス自体の位置調整
        //var offsetX = parseInt(this.dragFrame.style.left, 10);
        //var right = clip.width - (offsetX + width);
        //if (right > 0) {
        //    this.dragFrame.style.left = (offsetX + right) + "px";
        //}
        //var offsetY = parseInt(this.dragFrame.style.top, 10);
        //var bottom = clip.height - (offsetY + height);
        //if (bottom > 0) {
        //    this.dragFrame.style.top = (offsetY + bottom) + "px";
        //}
        // キャンバスがきっちり収まるドラックエリアの座標を計算する
        // ドラックエリア設定
        //this.dragInfo.dragArea = Point(clip.width - width, clip.height - height);
    }
    ;
    //
    // タッチスタート処理
    //
    MSF.MsfCanvasFrame.prototype.touchStart = function(event) {
    }
    ;
    //
    // タッチエンド処理
    //
    MSF.MsfCanvasFrame.prototype.touchEnd = function(event) {
        this.dragInfo.clickPoint = null;
        // ユーザー選択設定
        this.setUserSelect(true);

        if (event.changedTouches.length > 0 ){
            var touch = event.changedTouches[0];
            var pt = Point(touch.pageX, touch.pageY);
            // キャンバスクリック処理実行
            this.canvasClick(pt);
        }
    }
    ;
    //
    // ジェスチャー処理
    //
    MSF.MsfCanvasFrame.prototype.gestureChange = function(event) {
        // todo:拡縮スケール計算処理
        console.info(event.scale);
    }
    ;
    //
    // タッチムーブ処理
    //
    MSF.MsfCanvasFrame.prototype.touchMove = function(event) {
        // 一本指でムーブした場合
        //if (event.targetTouches.length == 1) {
        //    var touch = event.targetTouches[0];
        //    var pt = Point(touch.pageX, touch.pageY);
        //    // キャンバスムーブ処理
        //    this.canvasMove(pt);
        //}
    }
    ;

    //
    // ドラック処理
    //
    MSF.MsfCanvasFrame.prototype.Drag = function(event) {
        // マウスの左ボタン押下時
        //if (event.buttons == 1) {
        //    var pt = Point(event.pageX, event.pageY);
        //    // キャンバスムーブ処理
        //    this.canvasMove(pt);
        //}
    }
    ;

    //
    // クリップフレームクリック処理
    //
    MSF.MsfCanvasFrame.prototype.clipFrameClick = function(event) {
        var pt = Point(event.pageX, event.pageY);
        // キャンバスクリック処理実行
        this.canvasClick(pt);
    }
    ;
    //
    // キャンバスクリック処理
    //
    MSF.MsfCanvasFrame.prototype.canvasClick = function(point) {
        var pt = point;
        // ドラックフレームからの相対座標に変換
        // 対象オブジェクトからのオフセット座標を取得する
        var rect = $(this.dragFrame).offset();
        // ページオフセットの相対座標計算
        pt.x -= rect.left - window.pageXOffset;
        pt.y -= rect.top - window.pageYOffset;
        // 描画処理実行
        this.msfCanvas.Draw(pt);
    }
    ;
    //
    // ウィンドウリサイズイベント
    //
    MSF.MsfCanvasFrame.prototype.resize = function() {
        if (this.isResizeExec !== false) {
            clearTimeout(this.isResizeExec);
        }
        this.isResizeExec = setTimeout(function() {
            // クリップフレームサイズ計算
            this.setClipFrameSize();
            // キャンバス位置、サイズ計算
            this.setCanvasBounds();
            // 描画
            this.msfCanvas.Draw();
            this.isResizeExec = false;
        }
        .bind(this), 200);
    }
    ;
    //
    // マウスホイールイベント
    //
    MSF.MsfCanvasFrame.prototype.mousewheel = function(eo, delta, deltaX, deltaY) {
        $("#status").text("scale:" + this.msfCanvas.scale.toFixed(1));
        if (this.isMousewheelExec !== false) {
            clearTimeout(this.isMousewheelExec);
        }
        this.isMousewheelExec = setTimeout(function() {
            // スケーリング
            this.SetScale(delta);
            // キャンバス位置、サイズ計算
            this.setCanvasBounds();
            // 描画処理実行
            this.msfCanvas.Draw();
            this.isMousewheelExec = false;
        }
        .bind(this), 100);
    }
    ;
    //
    // 座標表現
    //
    function Point(x, y) {
        return {
            x: x,
            y: y
        };
    }
})();
