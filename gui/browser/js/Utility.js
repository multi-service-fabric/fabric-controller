//
// ユーティリティ.
//
//   getTrafficColor(traffic)                   トラヒック量(Gbps)からトラヒック表示色を取得する
//   getSliceViewColor()                        スライス表示色を取得
//   getTargetSliceInfo(sliceDicList, sliceId)  メニュー連携用辞書のリストからスライスIDをキーに1件取り出す
//   diffArray(lastArray, latestArray)          配列の差分取得処理(第一引数にのみ存在する要素を配列で返す)
//   diffDicArray(lastArray, latestArray)       配列の差分取得処理(第一引数(連想配列)にのみ存在する要素を返す)
//   isArrayEquals(a, b)                        配列の差分取得処理(第一引数(連想配列)にのみ存在する要素を返す)
//   proper(text)                               先頭文字を大文字に変換する
//   getTrafficRate(rate)                       トラヒック単位変換
//   alignmentRate(rate, decimal)               小数点桁合わせ処理
//   increase_brightness(hex, percent)          RGB配色の明度を変更する
//   getBaseIfId(ifList, ifType, ifId)          BreakoutIf,LagIfのbaseIf(PhysicalIf)を返す
//   getBaseIfIdDetail(ifInfo, ifType, ifId)    BreakoutIf,LagIfのPhysicalIfIdを返す
//   getSpeed(ifInfo, ifType, ifId)             Ifのspeedを返す
//   getNodeIdForCpId(cpInfo, type, id, cpId)   スライス種別、スライスIDから装置IDを特定して返す
//   getClusterIdNavi()                         ナビゲーションからクラスタIDを取得
//   getNodeIdNavi()                            ナビゲーションから装置IDを取得
//   getDeviceType()                            ナビゲーションから装置種別(s)を取得
//

//
// トラヒック量(Gbps)からトラヒック表示色を取得する
// ※トラヒック量(Gbps)⇒トラヒック量(%)の変換も行う
// traffic: Gbps⇒%
//
function getTrafficColor(traffic, type) {
    if (traffic == undefined) {
        return "#A9A9A9";  // 未取得
    }
    var speed = "";
    // 速度の存在チェック
    if (traffic.send_speed != undefined) {
        speed = traffic.send_speed;
    } else if (traffic.receive_speed != undefined) {
        speed = traffic.receive_speed;
    } else if (traffic.speed != undefined) {
        speed = traffic.speed;
    } else {
        return "#A9A9A9";  // 未取得
    }
    // トラフィック量(%)算出
    var parTraffic;
    if (MSF.Const.TrafficType.Send.indexOf(type) != -1) {            // 送信
        parTraffic = getParTraffic(traffic.send_rate, speed);
    } else if (MSF.Const.TrafficType.Receive.indexOf(type) != -1) {  // 受信
        parTraffic = getParTraffic(traffic.receive_rate, speed);
    } else {
        return "#A9A9A9";  // 未取得
    }

    for (var i = 0; i < trafficColor.length; i++) {
        if (parTraffic <= trafficBoundary[i]) {
            return trafficColor[i];
        }
    }
    return trafficColor.slice(-1)[0];
}

//
// トラヒック量(%)の計算を行う
//
function getParTraffic(rate, speed) {

    var traffic = 100;

    if (speed == undefined) {
        // 速度未指定は帯域FULLで使用しているものとする
    } else if (speed == "") {
        // 速度未指定は帯域FULLで使用しているものとする
    } else {

        // 速度から数字を抽出 [g]を削除する
        var iSpeed = "";
        if (!isNaN(speed)) {
            iSpeed = speed;
        } else {
            iSpeed = parseInt(speed.substring(0, speed.length - 1), 10);
        }

        if (!isNaN(iSpeed)) {

            // トラヒック量(%)の計算
            // rateの単位は[g]なので、そのまま計算
            traffic = 100 * rate / iSpeed;
            if (traffic > 100)
                traffic = 100;
        }
    }

    return traffic;
}

//
// スライス表示色を取得
// 呼び出しごとに取得位置をずらして取得する
//
function getSliceViewColor() {
    // 先頭から取出し、最後へ格納
    var ret = sliceColor.shift();
    sliceColor.push(ret);
    return ret;
}

//
// メニュー連携用辞書のリストからスライスIDをキーに1件取り出す
// sliceDicList: メニュー連携用辞書のリスト
// sliceId: スライスID (接頭辞はつかない)
//
function getTargetSliceInfo(sliceDicList, sliceId) {

    for (var key in sliceDicList) {
        if (sliceId == sliceDicList[key].id) {
            return sliceDicList[key];
        }
    }
}

//
// 配列の差分取得処理(第一引数にのみ存在する要素を配列で返す)
//
function diffArray(lastArray, latestArray) {
    // lastArray にのみ存在する値を配列で返却
    var ret = [];
    for (var i = 0; i < lastArray.length; i++) {
        if (latestArray.indexOf(lastArray[i]) == -1) {
            ret.push(lastArray[i]);
        }
    }
    return ret;
}

//
// 配列の差分取得処理(第一引数(連想配列)にのみ存在する要素を返す)
//
function diffDicArray(lastArray, latestArray) {
    // lastArray にのみ存在する値を配列で返却
    var ret = [];
    for (var key in lastArray) {
        if (latestArray.indexOf(key) == -1) {
            ret[key] = lastArray[key];
        }
    }
    return ret;
}

//
// 配列の差分取得処理(第一引数(連想配列)にのみ存在する要素を返す)
//
function isArrayEquals(a, b) {
    if (!Array.isArray(a))    return false;
    if (!Array.isArray(b))    return false;
    if (a.length != b.length) return false;
    for (var i = 0, n = a.length; i < n; ++i) {
        if (a[i] !== b[i]) return false;
    }
    return true;
}


//
// 先頭文字を大文字に変換する
//
function proper(text) {

    if (!text) {
        return text;
    }
    if (text.length === 0) {
        return text;
    }

    return text.charAt(0).toUpperCase() + text.slice(1);
}

//
// トラヒック単位変換
// rete: トラヒック量(Gpbs)
//
function getTrafficRate(rate) {

    // 数値として扱えないならそのまま返す
    if (isNaN(rate)) return;

    // Gbps -> Mbps変換
    if (MSF.Conf.Detail.Traffic.UNIT_MBPS) {
        rate = rate * 1024;
    }

    // 少数以下桁合わせ処理
    var decimal = MSF.Conf.Detail.Traffic.DECIMAL_PLACES;
    if (!decimal) {
        return rate;
    }
    rate = alignmentRate(rate, decimal);
    return rate;
}

//
// 桁合わせ処理
//
function alignmentRate(rate, decimal) {

    var con = Math.pow(10, decimal);
    rate = Math.round(rate * con) / con;
    rate = rate.toLocaleString('en', { maximumFractionDigits: decimal });

    // 桁合わせ処理後、小数点以下の桁揃え再確認
    var index = rate.indexOf(".");
    var charLength = rate.length;
    var diff = charLength - (index + 1);
    if (index == -1) {
        // 整数値(小数点なし)の場合、小数点以下0埋め
        rate = rate + ".";
        for (var i = 0; i < decimal; i++) {
            rate = rate + "0";
        }
    } else if (diff < decimal) {
        // 小数以下桁数不足：0埋め
        for (; diff < decimal; diff++) {
            rate = rate + "0";
        }
    }
    return rate;
}

//
// RGBの明度を変更する
//
function increase_brightness(hex, percent) {
    // strip the leading # if it's there
    hex = hex.replace(/^\s*#|\s*$/g, "");

    // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
    if (hex.length == 3) {
        hex = hex.replace(/(.)/g, "$1$1");
    }

    var r = parseInt(hex.substr(0, 2), 16),
        g = parseInt(hex.substr(2, 2), 16),
        b = parseInt(hex.substr(4, 2), 16);

    return "#" +
       ((0|(1<<8) + r + (256 - r) * percent / 100).toString(16)).substr(1) +
       ((0|(1<<8) + g + (256 - g) * percent / 100).toString(16)).substr(1) +
       ((0|(1<<8) + b + (256 - b) * percent / 100).toString(16)).substr(1);
}

//
// BreakoutIf,LagIfのbaseIf(PhysicalIf)を返す
//
function getBaseIfId(ifList, ifType, ifId) {

    var key;
    if (MSF.Const.RestIfType.PhysicalIf.indexOf(ifType) != -1) {
        return ifId;
    }
    if (MSF.Const.RestIfType.BreakoutIf.indexOf(ifType) != -1) {
        key = "breakout_if_id";
    }
    if (MSF.Const.RestIfType.LagIf.indexOf(ifType) != -1) {
        key = "lag_if_id";
    }
    for (var i = 0; i < ifList.length; i++) {
        var info = ifList[i];
        if (info[key] == ifId) {
            var baseIf = info.base_if || info;
            var physicalIfId = baseIf.physical_if_id;
            return physicalIfId;
        }
    }
}

//
// BreakoutIf,LagIfのbaseIf(PhysicalIf)を返す
// LagIfがBreakoutIfで構成されていれば、breakoutIfのbaseIfを返す
//
function getBaseIfIdDetail(ifInfo, ifType, ifId) {

    if (ifInfo == null) {
        return [];
    }
    var key;
    if (MSF.Const.RestIfType.PhysicalIf.indexOf(ifType) != -1) {
        return [ifId];
    }
    if (MSF.Const.RestIfType.BreakoutIf.indexOf(ifType) != -1) {
        typeKey = "breakout_ifs";
        idKey = "breakout_if_id";
    }
    if (MSF.Const.RestIfType.LagIf.indexOf(ifType) != -1) {
        typeKey = "lag_ifs";
        idKey = "lag_if_id";
    }
    for (var i = 0; i < ifInfo[typeKey].length; i++) {
        var info = ifInfo[typeKey][i];
        if (info[idKey] != ifId) {
            continue;
        }
        var baseIf = info.base_if;
        if (baseIf) {
            return [baseIf.physical_if_id];
        }
        // LagIfの場合
        if (info.physical_if_ids.length > 0) {
            return info.physical_if_ids;
        }
        // breakoutIFで構成されている場合、breakoutIfを構成する物理IFIDを取得
        var physicalIfIds = [];
        for (var j = 0; j < info.breakout_if_ids.length; j++) {
            physicalIfIds = physicalIfIds.concat(getBaseIfIdDetail(ifInfo, "breakout", info.breakout_if_ids[i]));
        }
        return physicalIfIds;
    }
    return [];
}

//
//IFのspeedを返す
//
function getSpeed(ifInfo, ifType, ifId) {

  var speed = null;

  if (ifInfo != null) {

      if (MSF.Const.RestIfType.PhysicalIf.indexOf(ifType) != -1) {
          typeKey = "physical_ifs";
          idKey = "physical_if_id";
      }
      if (MSF.Const.RestIfType.BreakoutIf.indexOf(ifType) != -1) {
          typeKey = "breakout_ifs";
          idKey = "breakout_if_id";
      }
      if (MSF.Const.RestIfType.LagIf.indexOf(ifType) != -1) {
          typeKey = "lag_ifs";
          idKey = "lag_if_id";
      }
      for (var i = 0; i < ifInfo[typeKey].length; i++) {
          var info = ifInfo[typeKey][i];
          if (info[idKey] != ifId) {
              continue;
          }
          if (info.speed != null)
              speed = info.speed;
      }
  }

  return speed;
}

//
// スライス種別、スライスIDから装置IDを特定して返す
//
function getNodeIdForCpId(baseData, sliceType, sliceId, cpId) {

    var info = baseData.CPInfo[sliceType + sliceId] || {};
    var cp = info[cpId];
    if (!cp) {
        return null;
    }
    var clusterId = cp.cluster_id;
    var edgePointInfo = baseData.clusterInfoDic[clusterId].EdgepointDic;
    var edgePoint = edgePointInfo[cp.edge_point_id];
    if (!edgePoint) {
        return null;
    }
    return edgePoint.base_if.leaf_node_id;
}

//
// ナビゲーションからクラスタIDを取得
//
function getClusterIdNavi() {
    var indexKey = "#";
    var clusterName = $("#navi2").text().replace(/> /, '');
    var index = clusterName.indexOf(indexKey);
    var clusterId = clusterName.slice(index+1);
    return clusterId;
}

//
// ナビゲーションから装置IDを取得
//
function getNodeIdNavi() {
    var indexKey = "#";
    var nodeName = $("#navi3").text().replace(/> /, '');
    var index = nodeName.indexOf(indexKey);
    var nodeId = nodeName.slice(index+1);
    return nodeId;
}

//
// ナビゲーションから装置種別(s)を取得
//
function getDeviceTypesNavi() {
    var indexKey = "#";
    var nodeName = $("#navi3").text().replace(/> /, '');
    var index = nodeName.indexOf(indexKey);
    var deviceType = nodeName.slice(0, index);
    return MSF.Const.DeviceType[deviceType] + "s";
}

//
// ナビゲーションから装置種別(sなし)を取得
//
function getDeviceTypeNavi() {
    var indexKey = "#";
    var nodeName = $("#navi3").text().replace(/> /, '');
    var index = nodeName.indexOf(indexKey);
    var deviceType = nodeName.slice(0, index);
    return deviceType;
}

//
// JSON判定
//
function isJSON(arg) {
    arg = (typeof arg === "function") ? arg() : arg;
    if (typeof arg  !== "string") {
        return false;
    }
    try {
        arg = (!JSON) ? eval("(" + arg + ")") : JSON.parse(arg);
        return true;
    } catch (e) {
        return false;
    }
}

//
//
// ifs    : InterfacesInfo.deviceType.nodeId
// ifType : 物理, Brakout, Lag
// id     : ifId
// return : 物理IFIDを配列で返す(Lagの場合は複数件数を返却)
//
function getPhysicalIfIdsForAnyIf(ifs, ifType, id) {
    var ret = [];
    var physicalIfId;
    if (ifType.indexOf("lag") != -1) {
        return getPhysicalIfsForLag(ifs.lag_ifs, ifs.breakout_ifs, id);
    }
    if (ifType.indexOf("breakout") != -1) {
        physicalIfId = getPhysicalForBreakout(ifs.breakout_ifs, id);
    } else {
        physicalIfId = id;
    }
    // 物理IF or それ以外はそのままidを返す
    ret.push(physicalIfId);
    return ret;
}

//
// breakoutIfIdが属する物理IFIDを取得する
// ifs : InterfacesInfo.deviceType.nodeId.breakout_ifs
// id : bId
//
function getPhysicalForBreakout(breakoutIfs, bId) {
    for (var i = 0; i < breakoutIfs.length; i++) {
        var breakoutIf = breakoutIfs[i];
        // bIFでループし、bIfIdとトラヒックのIFIDが同じ
        if (breakoutIf.breakout_if_id == bId) {
            return breakoutIf.base_if.physical_if_id;
        }
    }
}

//
// lagIfIdが属する物理IFIDを取得する
// lagIfs       : InterfacesInfo.deviceType.nodeId.lag_ifs
// breakoutIfs  : InterfacesInfo.deviceType.nodeId.breakout_ifs
// id : lId
//
function getPhysicalIfsForLag(lagIfs, breakoutIfs, lId) {
    for (var i = 0; i < lagIfs.length; i++) {
        var lagIf = lagIfs[i];
        if (lagIf.lag_if_id == lId) {
            if (lagIf.physical_if_ids.length > 0) {
                return lagIf.physical_if_ids;
            }
            var ifs = [];
            for (var ii = 0; ii < lagIf.breakout_if_ids.length; ii++) {
                var bId = lagIf.breakout_if_ids[ii];
                ifs.push(getPhysicalForBreakout(breakoutIfs, bId));
            }
            return ifs;
        }
    }
}

