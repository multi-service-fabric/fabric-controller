//
// 詳細部表示用の装置種別接頭辞(Leaf#/Spine#)を返す
// deviceType: leaf/leafs or spine/spines
//
function getDeviceTypeNumber(deviceType) {
    var fabricType = [MSF.Const.FabricType.Leaf, MSF.Const.FabricType.Spine];
    for (var i = 0; i < fabricType.length; i++) {
        if (deviceType.indexOf(fabricType[i]) != -1) {
            return proper(fabricType[i] + "#");
        }
    }
    MSF.console.warn("Unknown DeviceType: " + deviceType);
}

// 選択中/表示中のクラスタIDを取得
function getSelectedClusterId() {

    var clusterId = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
        // Map View
        clusterId = svg.getSelectedCluster();
    } else {
        // Fabric View, NodeView
        clusterId = MSF.main.can.viewClusterId;
    }
    return clusterId;
}

// 選択中/表示中の装置IDを取得
function getSelectedNodeId() {

    var nodeId = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        nodeId = getFabricSelectedSwitch();
    }
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
        nodeId = getNodeIdNavi();
    }
    return nodeId;
}

// 選択中/表示中の装置種別を取得
function getSelectedNodeType() {

    var type = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        type = getFabricSelectedSwitchType();
    }
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
        type = getDeviceTypesNavi();
    }
    return type;
}

// 選択中のPhysical IF IDを取得
function getSelectedIfId() {

    var ifId = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
        var tmp = nodeView.getSelectedIF();
        ifId = !isNaN(tmp) ? String(tmp) : null;
    }
    return ifId;
}

// 選択中/表示中のスライスIDを取得
function getSelectedSliceId() {

    var sliceL2Id = null;
    var sliceL3Id = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
        // Map View
        sliceL2Id = svg.getSelectedL2Slice();
        sliceL3Id = svg.getSelectedL3Slice();
    }
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        // Fabric View
        var focus = MSF.main.can.Focus.Slice;
        if (focus && focus.isLogical) {
            return focus.id;
        }
    }
    return sliceL2Id ? sliceL2Id : sliceL3Id;
}

// 選択中/表示中のスライス種別(l2vpn/l3vpn)を取得
function getSelectedSliceType() {

    var type = null;
    var sliceL2Id = null;
    var sliceL3Id = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
        // Map View
        sliceL2Id = svg.getSelectedL2Slice();
        sliceL3Id = svg.getSelectedL3Slice();
        if (sliceL2Id) {
            type = MSF.Const.SliceType.L2;
        }
        if (sliceL3Id) {
            type = MSF.Const.SliceType.L3;
        }
    }
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        // Fabric View,
        var focus = MSF.main.can.Focus.Slice;
        if (focus && focus.isLogical) {
            type = focus.sliceType;
        }
    }
    return type;
}

// Map 選択クラスタID取得
function getMapSelectedCluster() {
    return svg.getSelectedCluster();
}
// Map 選択クラスタ間リンクID取得
function getMapSelectedClusterLink() {
    return svg.getSelectedClusterLink();
}
// Map 選択L2スライスID取得
function getMapSelectedL2Slice() {
    return svg.getSelectedL2Slice();
}
// Map 選択L3スライスID取得
function getMapSelectedL3Slice() {
    return svg.getSelectedL3Slice();
}
// Fabric 選択スライスID取得
function getFabricSelectedSlice() {
    var sliceId = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Slice;
        if (focus && focus.isLogical) {
            sliceId = focus.id;
        }
    }
    return sliceId;
}
// Fabric 選択スライス種別取得
function getFabricSelectedSliceType() {
    var type = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Slice;
        if (focus && focus.isLogical) {
            type = focus.sliceType;
        }
    }
    return type;
}
// Fabric 選択スライス種別取得
function getFabricSelectedSliceLeafs() {
    var nodes = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Slice;
        if (focus) {
            nodes = focus.ClusterList[0].LeafDic;
        }
    }
    return nodes;
}
// Fabric 選択スライス種別取得
function getFabricSelectedSliceSpines() {
    var nodes = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Slice;
        if (focus) {
            nodes = focus.ClusterList[0].SpineDic;
        }
    }
    return nodes;
}
// Fabric 選択装置取得
function getFabricSelectedSwitch() {
    var nodeId = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Switch;
        nodeId = focus ? focus.id : null;
    }
    return nodeId;
}
// Fabric 選択装置種別取得
function getFabricSelectedSwitchType() {
    var type = null;
    if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
        var focus = MSF.main.can.Focus.Switch;
        if (!focus) {
            return type;
        }
        if (focus.isLeaf) {
            type = MSF.Const.FabricType.Leaf;
        } else {
            type = MSF.Const.FabricType.Spine;
        }
    }
    return type;
}

// Node 表示ノードID取得
function getNodeViewNode() {
    return nodeView.getViewNodeId();
}
// Node 表示ノード種別取得
function getNodeViewNodeType() {
    return nodeView.getViewDeviceType();
}

// Figure部表示状態の判定：Controllerモード表示
function isControllerMode() {
    return (MSF.main.mode == MSF.Const.Mode.Controller);
}
// Figure部表示状態の判定：Networkモード表示
function isNetworkMode() {
    return (MSF.main.mode == MSF.Const.Mode.Network);
}
// Figure部表示状態の判定：Networkモード Map表示
function isNetworkModeMap() {
    if (!isNetworkMode()) {
        return false;
    }
    if (MSF.main.networkmode != MSF.Const.NetworkMode.Map) {
        return false;
    }
    return true;
}
// Figure部表示状態の判定：Networkモード Fabric表示
function isNetworkModeFabric() {
    if (!isNetworkMode()) {
        return false;
    }
    if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) {
        return false;
    }
    return true;
}
// Figure部表示状態の判定：Networkモード Node表示
function isNetworkModeNode() {
    if (!isNetworkMode()) {
        return false;
    }
    if (MSF.main.networkmode != MSF.Const.NetworkMode.Equipment) {
        return false;
    }
    return true;
}
// Networkモード Map表示のオブジェクト選択状態の判定：Map 未選択
function isMapNotSelected() {
    if (!isNetworkModeMap()) {
        return false;
    }
    if (getMapSelectedCluster()) {
        return false;
    }
    if (getMapSelectedClusterLink()) {
        return false;
    }
    if (getMapSelectedL2Slice()) {
        return false;
    }
    if (getMapSelectedL3Slice()) {
        return false;
    }
    return true;
}
// Networkモード Map表示のオブジェクト選択状態の判定：Map クラスタ選択時
function isMapClusterSelected() {
    if (getMapSelectedCluster()) {
        return true;
    }
    return false;
}
// Networkモード Map表示のオブジェクト選択状態の判定：Map クラスタ間リンク選択時
function isMapClusterLinkSelected() {
    if (getMapSelectedClusterLink()) {
        return true;
    }
    return false;
}
// Networkモード Map表示のオブジェクト選択状態の判定：Map スライス選択時
function isMapSliceSelected() {
    if (getMapSelectedL2Slice()) {
        return true;
    }
    if (getMapSelectedL3Slice()) {
        return true;
    }
    return false;
}
// Networkモード Fabric表示のオブジェクト選択状態の判定： 未選択
function isFabricNotSelected() {
    if (!isNetworkModeFabric()) {
        return false;
    }
    if (MSF.main.can.Focus.Slice) {
        return false;
    }
    if (MSF.main.can.Focus.Switch) {
        return false;
    }
    return true;
}
// Networkモード Fabric表示のオブジェクト選択状態の判定： トポロジ選択時
function isFabricTopologySelected() {
    var slice = MSF.main.can.Focus.Slice;
    var sw = MSF.main.can.Focus.Switch;
    if (!slice) {
        return false;
    }
    if (slice.isLogical) {
        return false;
    }
    if (sw) {
        return false;
    }
    return true;
}
// Networkモード Fabric表示のオブジェクト選択状態の判定： トポロジ上SW選択時
function isFabricTopologySwSelected() {
    var slice = MSF.main.can.Focus.Slice;
    var sw = MSF.main.can.Focus.Switch;
    if (!slice) {
        return false;
    }
    if (slice.isLogical) {
        return false;
    }
    if (!sw) {
        return false;
    }
    return true;
}
// Networkモード Fabric表示のオブジェクト選択状態の判定： スライス選択時
function isFabricSliceSelected() {
    var slice = MSF.main.can.Focus.Slice;
    var sw = MSF.main.can.Focus.Switch;
    if (!slice) {
        return false;
    }
    if (!slice.isLogical) {
        return false;
    }
    if (sw) {
        return false;
    }
    return true;
}
// Networkモード Fabric表示のオブジェクト選択状態の判定： スライス上SW選択時
function isFabricSliceSwSelected() {
    var slice = MSF.main.can.Focus.Slice;
    var sw = MSF.main.can.Focus.Switch;
    if (!slice) {
        return false;
    }
    if (!slice.isLogical) {
        return false;
    }
    if (!sw) {
        return false;
    }
    return true;
}
// Networkモード Node表示のオブジェクト選択状態の判定： ポート選択時
function isNodePortSelected() {
    var ifId = nodeView.getSelectedIF();
    return (!isNaN(ifId));
}
