//
// ArrayComparerクラス
// ※オブジェクトのArrayをソートする際に使用する便利クラスです
//

(function() {
"use strict";

    //
    // コンストラクタ
    //   sortKeys   : 各要素(レコード)のどの要素をキーにソートするか？
    //   sortOrders : キーでソートする際の昇順／降順の指定(省略時は昇順)
    //
    MSF.ArrayComparer = function(sortKeys, sortOrders) {
        this.sortKeys = sortKeys;
        this.sortOrders = sortOrders;
    }
    ;

    //
    // 定数定義：ソート順
    //
    MSF.ArrayComparer.Order = {
        // 昇順
        ASC: "Ascending",
        // 降順
        DESC: "Descending"
    };
 
    //
    // ソート用の関数
    //   Array.prototype.sort()に引数として渡してください
    //
    MSF.ArrayComparer.prototype.compare = function(item1, item2) {

        // 比較が出来るキーが見つかるまでループ
        for (var i = 0; i < this.sortKeys.length; i++) {
            var key = this.sortKeys[i];
            var attribute1 = item1[key];
            var attribute2 = item2[key];
            var order = this.sortOrders[i];

            // 順序不明時は昇順でソート
            if (order != MSF.ArrayComparer.Order.DESC) {
                order = MSF.ArrayComparer.Order.ASC;
            }

            // null等を置換
            if (attribute1 === null) attribute1 = "";
            if (attribute2 === null) attribute2 = "";

            // 同じ値なら次のキーで比較
            if ( attribute1 == attribute2 ) {
                continue;
            }

            // 異なる値なら比較結果を返す
            if ( attribute1 < attribute2 && order == MSF.ArrayComparer.Order.ASC ||
                 attribute1 > attribute2 && order == MSF.ArrayComparer.Order.DESC) {
                return -1;
            } else {
                return 1;
            }
        }

        return 0;
    }
    ;
 
    //
    // ソート用の関数(Controllerでソート)
    //   Array.prototype.sort()に引数として渡してください
    //
    MSF.ArrayComparer.prototype.compareController = function(item1, item2) {

        // 比較が出来るキーが見つかるまでループ
        for (var i = 0; i < this.sortKeys.length; i++) {
            var key = this.sortKeys[i];
            var attribute1 = item1[key];
            var attribute2 = item2[key];

            // 同じ値なら次のキーで比較
            if (attribute1 == attribute2) {
                continue;
            }

            var num1 = Number(attribute1.slice(attribute1.indexOf("#") + 1));
            var num2 = Number(attribute2.slice(attribute2.indexOf("#") + 1));

            // 異なる値なら比較結果を返す
            if (attribute1.indexOf("MFC") !== -1) {
                return -1;
            }
            if (attribute1.indexOf("FC") !== -1) {
                if (attribute2.indexOf("MFC") !== -1) {
                    return 1;
                }
                if (attribute2.indexOf("FC") !== -1) {
                    return num1 - num2;
                }
                return -1;
            }
            if (attribute1.indexOf("EC") !== -1) {
                if (attribute2.indexOf("FC") !== -1) {
                    return 1;
                }
                if (attribute2.indexOf("EC") !== -1) {
                    return num1 - num2;
                }
                return -1;
            }
            if (attribute1.indexOf("EM") !== -1) {
                if (attribute2.indexOf("EM") !== -1) {
                    return num1 - num2;
                }
                return 1;
            }
        }

        return 0;
    }
    ;

    //
    // 連想配列ソート用の関数
    //
    MSF.ArrayComparer.prototype.ObjArraySort = function(ary, key, order) {
        var reverse = 1;
        if (order && order.toLowerCase() == "desc") {
            reverse = -1;
        }
        ary.sort(function(a, b) {
            if (a[key] < b[key]) {
                return -1 * reverse;
            } else if (a[key] == b[key]) {
                return 0;
            } else {
                return 1 * reverse;
            }
        });
    }
    ;

})();
