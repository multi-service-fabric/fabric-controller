//
// 権限グループ(機能グループ)
//   id  : 機能グループ固有の番号
//   name: "data-function-group" 属性に設定する際の名前
//

MSF.FunctionGroup = {
    // デバイス可視化（物理ポート情報）
    DisplayPhysicalDevice: {
        id: 0,
        name: "display_physical_device"
    },

    // デバイス可視化（論理ポート情報）
    DisplayLogicalDevice: {
        id: 1,
        name: "display_logical_device"
    },

    // クラスタ可視化
    DisplayCluster: {
        id: 2,
        name: "display_cluster"
    },

    // デバイス制御（機種情報制御）
    ControlModelInfo: {
        id: 3,
        name: "control_model"
    },

    // デバイス制御（装置制御）
    ControlDevice: {
        id: 4,
        name: "control_device"
    },

    // デバイス制御（IF制御）
    ControlInterface: {
        id: 5,
        name: "control_interface"
    },

    // スライス可視化
    DisplaySlice: {
        id: 6,
        name: "display_slice"
    },

    // スライス制御
    ControlSlice: {
        id: 7,
        name: "control_slice"
    },

    // トラヒック情報管理
    DisplayTraffic: {
        id: 8,
        name: "display_traffic"
    },

    // RESTのログ情報参照
    RestMonitor: {
        id: 9,
        name: "rest_monitor"
    },

    // ポーリング制御
    PollingControl: {
        id: 10,
        name: "polling_control"
    },

    // 手動ロールバック機能
    Rollback: {
        id: 11,
        name: "rollback"
    },

    // デバイス制御（クラスタ制御）
    ControlCluster: {
        id: 12,
        name: "control_cluster"
    },

    // DB参照
    DbMonitor: {
        id: 13,
        name: "db_monitor"
    },

    // コントローラ情報管理
    DisplayControllerInfo: {
        id: 14,
        name: "display_controller_info"
    },

    // 障害発生制御
    DisplayFailure: {
        id: 15,
        name: "display_failure"
    },
    // 障害発生制御(デバッグ用)
    DebugFailure: {
        id: 16,
        name: "debug_failure"
    },

    // 装置OSアップグレード
    MaintenanceSwitch: {
        id: 17,
        name: "maintenance_switch"
    },

    // 装置迂回
    ConstructionSwitch: {
        id: 18,
        name: "construction_switch"
    },

    // 権限グループ配列の終端
    EndOfList: {
        id: 19,
        name: "end_of_list"
    }
};


//
// アカウント種別
//
MSF.AccountType = {

    FirstB_Admininistrator: {
        id: 0,
        note: "First-B 管理者",
        name: "FirstB Administrator",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            MSF.FunctionGroup.ControlModelInfo,
            MSF.FunctionGroup.ControlDevice,
            MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            MSF.FunctionGroup.MaintenanceSwitch,
            MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    FirstB_MonitoringStaff: {
        id: 1,
        note: "First-B 監視担当",
        name: "FirstB Monitoring Staff",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            //MSF.FunctionGroup.ControlModelInfo,
            //MSF.FunctionGroup.ControlDevice,
            //MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            //MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            //MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            //MSF.FunctionGroup.MaintenanceSwitch,
            //MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    FirstB_DeviceMaintainer: {
        id: 2,
        note: "First-B 設備保守",
        name: "FirstB Device Maintainer",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            MSF.FunctionGroup.ControlModelInfo,
            MSF.FunctionGroup.ControlDevice,
            MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            //MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            MSF.FunctionGroup.MaintenanceSwitch,
            MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    FirstB_ServiceOrderStaff: {
        id: 3,
        note: "First-B SO担当",
        name: "FirstB SO Staff",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            //MSF.FunctionGroup.ControlModelInfo,
            //MSF.FunctionGroup.ControlDevice,
            MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            //MSF.FunctionGroup.ControlCluster,
            //MSF.FunctionGroup.DisplayControllerInfo,
            //MSF.FunctionGroup.DisplayFailure,
            //MSF.FunctionGroup.MaintenanceSwitch,
            //MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    MiddleB_Administrator: {
        id: 4,
        note: "Middle-B 管理者",
        name: "MiddleB Administrator",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            //MSF.FunctionGroup.ControlModelInfo,
            //MSF.FunctionGroup.ControlDevice,
            //MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            //MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            //MSF.FunctionGroup.MaintenanceSwitch,
            //MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    MiddleB_MonitoringStaff: {
        id: 5,
        note: "Middle-B 監視担当",
        name: "MiddleB Monitoring Staff",
        availableFunctions: [
            //MSF.FunctionGroup.DisplayPhysicalDevice,
            //MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            //MSF.FunctionGroup.ControlModelInfo,
            //MSF.FunctionGroup.ControlDevice,
            //MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            //MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            //MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            //MSF.FunctionGroup.MaintenanceSwitch,
            //MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    MiddleB_ServiceOrderStaff: {
        id: 6,
        note: "Middle-B SO担当",
        name: "MiddleB SO Staff",
        availableFunctions: [
            //MSF.FunctionGroup.DisplayPhysicalDevice,
            //MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            //MSF.FunctionGroup.ControlModelInfo,
            //MSF.FunctionGroup.ControlDevice,
            //MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            //MSF.FunctionGroup.ControlCluster,
            //MSF.FunctionGroup.DisplayControllerInfo,
            //MSF.FunctionGroup.DisplayFailure,
            //MSF.FunctionGroup.MaintenanceSwitch,
            //MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    },

    Developer: {
        id: 7,
        note: "開発者",
        name: "Developer",
        availableFunctions: [
            MSF.FunctionGroup.DisplayPhysicalDevice,
            MSF.FunctionGroup.DisplayLogicalDevice,
            MSF.FunctionGroup.DisplayCluster,
            MSF.FunctionGroup.ControlModelInfo,
            MSF.FunctionGroup.ControlDevice,
            MSF.FunctionGroup.ControlInterface,
            MSF.FunctionGroup.DisplaySlice,
            MSF.FunctionGroup.ControlSlice,
            MSF.FunctionGroup.DisplayTraffic,
            MSF.FunctionGroup.RestMonitor,
            MSF.FunctionGroup.PollingControl,
            MSF.FunctionGroup.Rollback,
            MSF.FunctionGroup.DbMonitor,
            MSF.FunctionGroup.ControlCluster,
            MSF.FunctionGroup.DisplayControllerInfo,
            MSF.FunctionGroup.DisplayFailure,
            MSF.FunctionGroup.DebugFailure,
            MSF.FunctionGroup.MaintenanceSwitch,
            MSF.FunctionGroup.ConstructionSwitch,
            MSF.FunctionGroup.EndOfList
        ]
    }
};

// インデックスアクセス時に使用する。
MSF.AccountTypeList = [
    MSF.AccountType.FirstB_Admininistrator,
    MSF.AccountType.FirstB_MonitoringStaff,
    MSF.AccountType.FirstB_DeviceMaintainer,
    MSF.AccountType.FirstB_ServiceOrderStaff,
    MSF.AccountType.MiddleB_Administrator,
    MSF.AccountType.MiddleB_MonitoringStaff,
    MSF.AccountType.MiddleB_ServiceOrderStaff,
    MSF.AccountType.Developer
];

//
// アカウント一覧
//
MSF.AccountList = [
    {
        user: "a",
        pass: "a",
        authority: MSF.AccountType.FirstB_Admininistrator.id
    },
    {
        user: "user1",
        pass: "user1",
        authority: MSF.AccountType.FirstB_MonitoringStaff.id
    },
    {
        user: "user2",
        pass: "user2",
        authority: MSF.AccountType.FirstB_DeviceMaintainer.id
    },
    {
        user: "user3",
        pass: "user3",
        authority: MSF.AccountType.FirstB_ServiceOrderStaff.id
    },
    {
        user: "user4",
        pass: "user4",
        authority: MSF.AccountType.MiddleB_Administrator.id
    },
    {
        user: "user5",
        pass: "user5",
        authority: MSF.AccountType.MiddleB_MonitoringStaff.id
    },
    {
        user: "user6",
        pass: "user6",
        authority: MSF.AccountType.MiddleB_ServiceOrderStaff.id
    },
    {
        user: "debug_",
        pass: "debug_",
        authority: MSF.AccountType.Developer.id
    }
];
