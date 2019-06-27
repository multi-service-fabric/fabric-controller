// 定数定義

// REST発行時のslice_type
MSF.Const = {
    SliceType: {
        L2: "l2vpn",
        L3: "l3vpn"
    },
    VpnType: {
        L2: "l2",
        L3: "l3"
    },
    LogType: {
        Success: "logSuccess",
        Fail: "logFail"
    },
    AllLogType: [],
    DeviceType: {
        Spine: "spine",
        Leaf: "leaf"
    },
    FabricType: {
        Spine: "spine",
        Leaf: "leaf",
        Spines: "spines",
        Leafs: "leafs"
    },
    SupportProtocol: {
        BGP: "bgp",
        OSPF: "ospf",
        STATIC: "static",
        VRRP: "vrrp"
    },
    ActionType: {
        Change: "chg_leaf_type",
        Update: "update",
        UpdateRemark: "update_remark_menu"
    },
    IfType: {
        PhysicalIf: "physical_IF",
        BreakoutIf: "breakout_IF",
        LagIf: "lag_IF"
    },
    RestIfType: {
        PhysicalIf: "physical-if",
        BreakoutIf: "breakout-if",
        LagIf: "lag-if"
    },
    OP: {
        ADD: "add",
        REMOVE: "remove"
    },
    UsesStatus: {
        Breakout: "breakout IF",
        Lag: "Lag IF",
        InternalLink: "Intra-Cluster Link",
        ClusterLink: "Inter-Cluster Link",
        EdgePoint: "edge point",
        UnUsed: "-"
    },
    ControllerType: {
        MFC: "MFC",
        FC: "FC",
        EC: "EC",
        EM: "EM"
    },
    getVpnTypeForSliceType: function (sliceType) {
        return sliceType.substring(0,2);
    },
    getSliceTypeForVpnType: function (vpnType) {
        return MSF.Const.SliceType[vpnType];
    },
    getFabricTypePlural: function (fabricType) {
        if (fabricType.indexOf(MSF.Const.FabricType.Spine) != -1) {
            return MSF.Const.FabricType.Spines;
        }
        return MSF.Const.FabricType.Leafs;
    },
    getFabricTypeSingular: function (fabricType) {
        if (fabricType.indexOf(MSF.Const.FabricType.Spine) != -1) {
            return MSF.Const.FabricType.Spine;
        }
        return MSF.Const.FabricType.Leaf;
    },
    // 
    // キー作成 引数で指定した項目を一定のフォーマットで加工して返します
    // 引数：可変長
    // 
    makeKey: function(){
        var word = "";
        for (var i = 0; i < arguments.length; i++) {
            var val = arguments[i];
            word += "[" + val + "]";
        }
        return word;
    },
    Mode: {
        Network: "Network Mode",
        Controller: "Controller Mode"
    },
    NetworkMode: {
        Map: "Map",
        Cluster: "Cluster",
        Equipment: "Equipment"
    },
    IrbType: {
        Asymmetric: "asymmetric",
        Symmetric: "symmetric",
        None: "none"
    },
    LagIfUpdateAction:{
        Add_If: "add_if",
        Del_If: "del_if"
    },
    TrafficType:{
        Send: "send",
        Receive: "receive"
    }
};

MSF.Const.AllLogType = [
    MSF.Const.LogType.Success,
    MSF.Const.LogType.Fail
];
