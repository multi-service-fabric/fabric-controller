//
// ログ管理クラス
//
(function() {
"use strict";
    //
    // コンストラクタ
    //   logTable:ログを表示するtable要素
    //
    MSF.MsfLog = function(logTable) {
        
        // ログを表示するtable要素
        this.logTable = logTable;

        // 保持するログの一覧
        this.logList = [];

        // 使用済みの最大のログID
        this.maxLogID = 0;
    }
    ;

    //
    // ログ追加
    //   type     : ログの種別（MSF.Const.LogTypeを指定）
    //   operation: 操作内容（任意文字列）
    //   message  : メッセージ（任意文字列）
    //   戻り値   : なし
    //
    MSF.MsfLog.prototype.appendLog = function(type, operation, message) {

        // ログ生成
        var log = this._getNewLog(type, operation, message, new Date());
        if (log === null) return;

        // 内部リストに追加
        this.logList.push(log);

        // GUIへの反映
        this.reflectLogToScreen();
    }
    ;

    //
    // ログ生成
    //   type     : ログの種別（MSF.Const.LogTypeを指定）
    //   operation: 操作内容（任意文字列）
    //   message  : メッセージ（任意文字列）
    //   dateTime : ログの発生日時（Dateオブジェクト）
    //   戻り値   : 生成したログオブジェクト。引数が異常な場合は null 
    //
    MSF.MsfLog.prototype._getNewLog = function(type, operation, message, dateTime) {
        var instance = {};

        // 引数を検証
        if ($.inArray(type, MSF.Const.AllLogType) < 0) {
            MSF.console.error("typeの指定が異常です(function = [_getNewLog], type = [{type}])".format({type:type}));
            return null;
        }
        if ((dateTime instanceof Date) === false){
            MSF.console.error("dateTimeの指定が異常です(function = [_getNewLog], dateTime = [{dateTime}])".format({dateTime:dateTime}));
            return null;
        }

        // 属性を設定
        instance.id = this._getNewLogID();
        instance.type = type;
        instance.operation = operation;
        instance.message = message;
        instance.dateTime = dateTime;

        return instance;
    }
    ;

    //
    // ログＩＤ払い出し
    //   戻り値: 新しく払い出されたログID
    // 
    MSF.MsfLog.prototype._getNewLogID = function() {
        this.maxLogID += 1;
        return this.maxLogID;
    }
    ;
    
    //
    // ログの画面への反映
    //   ※ 内部データは古い順、表示データは新しい順に格納している前提
    //   戻り値   : なし
    //
    MSF.MsfLog.prototype.reflectLogToScreen = function() {

        if (!logTable) {
            MSF.console.error("logTableの要素がありません(function = [reflectLogToScreen])");
            return;
        }

        // 古いログの削除(内部データ)
        var maxCount = MSF.Conf.MsfLog.LOG_MAX_COUNT;
        var maxSeconds = MSF.Conf.MsfLog.LOG_MAX_SECONDS;
        var currentTime = new Date();
        var tmpLog;
        do {
            // 件数チェック
            if (maxCount > 0 && this.logList.length > maxCount) {
                this.logList.shift();
                continue;
            }
            if (this.logList.length === 0) {
                break;
            }
            
            // 期限チェック
            tmpLog = this.logList[0];
            if (maxSeconds > 0 && currentTime.getTime() - tmpLog.dateTime.getTime() > maxSeconds * 1000) {
                this.logList.shift();
                continue;
            }
            break;
        } while(true);

        // logTableのrow要素一覧を取得
        var showLogMax = -1;
        var firstRow = null;
        var logRowList = $(logTable).find("tr").each(function (idx,item) {

            // trにひもづくログIDを取得
            var tmpID = $(item).attr("data-logid");
            if (!tmpID) return;
            tmpID = parseInt(tmpID, 10);

            // 古いログを削除(表示要素)
            if (this.logList.length === 0 || tmpID < this.logList[0].id)
                $(item).remove();

            // 最大のログIDを保持
            if (showLogMax < tmpID) {
                showLogMax = tmpID;
            }
        }.bind(this));

        // 新しいログを追加
        var logAppended = false;
        for (var i = 0; i < this.logList.length; i++){
            tmpLog = this.logList[i];
            if (tmpLog.id > showLogMax) {
                this._appendLogToScreen(tmpLog);
                logAppended = true;
            }
        }

        // ログ追加時は、追加ログが見えるようにスクロール
        if(logAppended === true && MSF.Conf.MsfLog.AUTO_SCROLL === true)
            $(logTable).parent().scrollTop(0);
    }
    ;

    
    //
    // ログの画面への追加
    //   newLog : 新しく追加するログ
    //   戻り値 : なし
    //
    MSF.MsfLog.prototype._appendLogToScreen = function(newLog) {

        if (!logTable) {
            MSF.console.error("logTableの要素がありません(function = [_appendLogToScreen])");
            return;
        }

        // logTableの最初(１行目)のrow要素を取得
        var firstRow = null;
        var rows = $(logTable).find("tr");
        if (rows.length > 0) {
            firstRow = rows[0];
        }

        // 新しい行を生成して追加
        var newRow = this._createLogItem(newLog);
        if (firstRow !== null) {
            newRow.insertBefore(firstRow);
        } else {
            newRow.appendTo(logTable);
        }
    }
    ;

    //
    // ログを表すtr要素生成
    //   newLog : ログオブジェクト
    //   戻り値 : 生成したtr要素のjQueryオブジェクト
    //
    MSF.MsfLog.prototype._createLogItem = function(newLog) {

        // 時刻はh:mm:ss形式 ※０詰めがちょっとトリッキー
        var timeString = newLog.dateTime.getHours() + ":" +
                         ("0" + newLog.dateTime.getMinutes()).slice(-2) + ":" +
                         ("0" + newLog.dateTime.getSeconds()).slice(-2);
        
        var html = '<tr class="' + newLog.type + '" data-logid="' + newLog.id + '">' + 
                   '<td class="logIcon"></td>' + 
                   '<td class="logTime">' + timeString + '</td>' + 
                   '<td class="logOperation">' + newLog.operation + '</td>' + 
                   '<td class="logMessage">' + newLog.message + '</td>' + 
                   '</tr>';

        return $(html);
    }
    ;
})();
