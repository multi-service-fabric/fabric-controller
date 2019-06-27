//
// MSF詳細エリアクラス(コントローラ状態)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailControllerInfo = function() {
        // 更新対象ID
        this.TARGET_ID = "#detailTable10";
        // カラム数
        this.COL_NUM = 4;
        // ソート対象レコード
        this.SORT_COLS = [0];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（Controller Info）レコード生成.
    // baseData       :レコード生成に使用する基礎データ(ポーリング取得)
    // controllers    : 表示対象コントローラIDの配列
    // 戻り値         : 詳細エリア出力レコードの配列
    //
    MSF.MsfDetailControllerInfo.prototype.createRecords = function (baseData, controllers) {
        var recordList = [];
        for (var i = 0; i < controllers.length; i++) {
            var ctrl = controllers[i];
            // コントローラ情報から該当情報を取出し
            var target = this.getControllerInformation(baseData.controller_informations, ctrl);
            if (target == null) {
                // 対象コントローラ情報なし
                continue;
            }

            // レコード作成
            var record = this.getRecord(target);
            recordList.push(record);
        }
        
        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        recordList.sort(comparer.compareController.bind(comparer));
        return recordList;
    }
    ;

    //
    // 詳細情報テーブル（Controller Info）レコード生成.
    //
    MSF.MsfDetailControllerInfo.prototype.getRecord = function (info) {

        var id = info.controller_type.toUpperCase();
        if (id != MSF.Const.ControllerType.MFC) {
            // MFC以外はコントローラIDを表示
            id = id + "#" + info.cluster_id;
        }
        var cpuRate = alignmentRate(info.os.cpu.use_rate, 1);
        var memRate = this.controllerInfoMemColumn(info.os.memory.used, info.os.memory.free);
        var disk = this.controllerInfoDiskColumn(info.os.disk.devices);
        
        var record = [];
        // 0: コントローラID
        record.push(id);
        // 1: CPU使用率
        record.push(cpuRate);
        // 2: メモリ使用率
        record.push(memRate);
        // 3: ディスク使用量
        record.push(disk);
        
        return record;
    }
    ;

    //
    // コントローラの取得情報配列から該当情報を取出す
    //
    MSF.MsfDetailControllerInfo.prototype.getControllerInformation = function (informations, ctrl) {

        if (!informations || informations.length == 0) {
            return null;
        }
        // コントローラ種別, クラスタIDを取出し
        var controller_type = this.getControllerType(ctrl);
        var cluster_id = this.getClusterId(ctrl);
        for (var i = 0; i < informations.length; i++) {
            var info = informations[i];
            if (controller_type != info.controller_type) {
                continue;
            }
            if (controller_type == "mfc") {
                return info;
            }
            if (cluster_id != info.cluster_id) {
                continue;
            }
            return info;
        }
        return null;
    }
    ;

    //
    // コントローラID(FC#x)からコントローラ種別の小文字を取得
    //
    MSF.MsfDetailControllerInfo.prototype.getControllerType = function (controller) {
        var indexKey = "#";
        var index = controller.indexOf(indexKey);
        if (index == -1) {
            // #がつかない文字列の場合はMFCとして返す
            return "mfc";
        }
        var controllerType = controller.slice(0, index);
        return controllerType.toLowerCase();
    }
    ;

    //
    // コントローラID(FC#x)からクラスタIDを取得
    //
    MSF.MsfDetailControllerInfo.prototype.getClusterId = function (controller) {
        var indexKey = "#";
        var index = controller.indexOf(indexKey);
        if (index == -1) {
            // MFCの場合は空文字を返す
            return "";
        }
        var clusterId = controller.slice(index + 1);
        return clusterId;
    }
    ;

    //
    // ControllerInfo: メモリ使用率カラム文字列生成
    // used   : メモリ使用量
    // free   : メモリ空き容量
    // return : メモリ使用率の1カラム分の出力文字列
    //
    MSF.MsfDetailControllerInfo.prototype.controllerInfoMemColumn = function (used, free) {
        if (used === -1 || free === -1) {
            // メモリ使用量または空きメモリ容量が取得できていない場合
            return used + " (-%)";
        }
        var memRate = Math.floor(used / (used + free) * 100);
        return used + " (" + memRate + "%)";
    }
    ;

    //
    // ControllerInfo: ディスク使用量カラム文字列生成
    // devices : デバイス単位の情報配列
    // return  : ディスク使用量の1カラム分の出力文字列
    //
    MSF.MsfDetailControllerInfo.prototype.controllerInfoDiskColumn = function (devices) {

        var column = "";
        if (devices.length === 0) {
            return "-";
        }
        var rate = 0;
        var device;
        for (var i = 0; i < devices.length; i++) {
            device = devices[i];
            rate = Math.floor((device.used / device.size) * 100);
            column = column + "<span class=\"detailDisk1\">" + device.size + "</span>";
            column = column + "<span class=\"detailDisk2\">(" + rate + "%)</span>";
            column = column + "<span class=\"detailDisk3\">" + device.mounted_on + "</span>";
            column = column + "<br/>";
        }
        return column;
    }
    ;
})();


