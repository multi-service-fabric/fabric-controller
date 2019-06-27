//
// MSF詳細エリアクラス(CP情報)
//
(function() {
"use strict";
    //
    // コンストラクタ
    //
    MSF.MsfDetailSliceInfo = function() {
        // 更新対象ID
        this.TARGET_ID_L2 = "#detailTable6-1";
        this.TARGET_ID_L3 = "#detailTable6-2";
        // カラム数
        this.COL_NUM_L2 = 14;
        this.COL_NUM_L3 = 17;
        // ソート対象レコード
        this.SORT_COLS = [0, 1, 2];
        // ソート条件 (未指定:昇順)
        this.SORT_ORDER = [];
    }
    ;
    //
    // 詳細情報テーブル（L2 Slice Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID (undefined)
    //
    MSF.MsfDetailSliceInfo.prototype.createRecordsL2 = function (baseData, sliceId, clusterId, deviceType, nodeId, leafs) {
    
        var records = [];
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        var targetCps = this.getTargetL2(baseData, sliceId, clusterId, leafs, deviceType, nodeId);
        for (var i = 0; i < targetCps.length; i++) {
            records.push(this.getRecordL2(targetCps[i]));
        }

        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報テーブル（L3 Slice Info）レコード生成.
    // baseData:   レコード生成に使用する基礎データ(ポーリング取得)
    // clusterId: 出力対象クラスタID
    //
    MSF.MsfDetailSliceInfo.prototype.createRecordsL3 = function (baseData, sliceId, clusterId, deviceType, nodeId, leafs) {
    
        var records = [];
        deviceType = deviceType ? MSF.Const.getFabricTypePlural(deviceType) : deviceType;
        var targetCps = this.getTargetL3(baseData, sliceId, clusterId, leafs, deviceType, nodeId);
        for (var i = 0; i < targetCps.length; i++) {
            records.push(this.getRecordL3(targetCps[i]));
        }

        // 生成したレコードをソート 
        var comparer = new MSF.ArrayComparer(this.SORT_COLS, this.SORT_ORDER);
        records.sort(comparer.compare.bind(comparer)); 
        return records;
    }
    ;
    //
    // 詳細情報テーブル（L2 Slice Info）レコード生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getRecordL2 = function (cp) {

        var record = [];
        // 0: スライスID
        record.push(cp.slice_id);
        // 1: edge-point ID
        record.push(cp.edge_point_id);
        // 2: CP ID
        record.push(cp.cp_id);
        // 3: VLAN ID
        record.push(cp.vlan_id);
        // 4: portモード
        record.push(cp.port_mode);
        var qos = cp.qos || {};
        // 5: remarkメニュー
        var remarkMenu = qos.remark_menu || "-";
        record.push((qos.remark) ? remarkMenu : "-");
        // 6: 流入シェーピングレート
        var inShapingRate = qos.ingress_shaping_rate ? alignmentRate(qos.ingress_shaping_rate, 1) : "-";
        record.push((qos.shaping) ? inShapingRate : "-");
        // 7: 流出シェーピングレート
        var egShapingRate = qos.egress_shaping_rate ? alignmentRate(qos.egress_shaping_rate, 1) : "-";
        record.push((qos.shaping) ? egShapingRate : "-");
        // 8: Egressキュー割合メニュー
        record.push(this.getEgressQueueMenu(qos.egress_queue_menu, qos.egress_queue_capability));
        // 9: マルチホームペアCP ID
        record.push((cp.pair_cp_id != null) ? cp.pair_cp_id : "-");
        var irb = cp.irb || {};
        //10: IRBインタフェースのIPアドレス
        record.push((irb.irb_ipv4_address != null) ? irb.irb_ipv4_address : "-");
        //11: 仮想ゲートウェイのIPアドレス
        record.push((irb.vga_ipv4_address != null) ? irb.vga_ipv4_address : "-");
        //12: ネットワークアドレスプレフィックス
        record.push((irb.ipv4_address_prefix != null) ? irb.ipv4_address_prefix : "-");
        //13: トラヒック閾値
        record.push((cp.traffic_threshold != null) ? cp.traffic_threshold : "-");

        return record;
    }
    ;
    //
    // 詳細情報テーブル（L3 Slice Info）レコード生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getRecordL3 = function (cp) {

        var record = [];
        // 0: スライスID
        record.push(cp.slice_id);
        // 1: edge-point ID
        record.push(cp.edge_point_id);
        // 2: CP ID
        record.push(cp.cp_id);
        // 3: VLAN ID
        record.push(cp.vlan_id);
        // 4: IPv4アドレス
        record.push((cp.ipv4_address != null) ? cp.ipv4_address : "-");
        // 5: IPv4プレフィックス
        record.push((cp.ipv4_prefix != null) ? cp.ipv4_prefix : "-");
        // 6: IPv6アドレス
        record.push((cp.ipv6_address != null) ? cp.ipv6_address : "-");
        // 7: IPv6プレフィックス
        record.push((cp.ipv6_prefix != null) ? cp.ipv6_prefix : "-");
        // 8: サポートプロトコル
        record.push((cp.support_protocols.length > 0) ? cp.support_protocols.join(",") : "-");
        // 9: BGP
        record.push((cp.bgp != null) ? this.getBgp(cp.bgp) : "-");
        // 10: 静的経路
        record.push((cp.static_routes != null) ? this.getStaticRoutes(cp.static_routes) : "-");
        // 11: VRRP
        record.push((cp.vrrp != null) ? this.getVrrp(cp.vrrp) : "-");
        var qos = cp.qos || {};
        // 12: remarkメニュー
        var remarkMenu = qos.remark_menu || "-";
        record.push((qos.remark) ? remarkMenu : "-");
        // 13: 流入シェーピングレート
        var inShapingRate = qos.ingress_shaping_rate ? alignmentRate(qos.ingress_shaping_rate, 1) : "-";
        record.push((qos.shaping) ? inShapingRate : "-");
        // 14; 流出シェーピングレート
        var egShapingRate = qos.egress_shaping_rate ? alignmentRate(qos.egress_shaping_rate, 1) : "-";
        record.push((qos.shaping) ? egShapingRate : "-");
        // 15: Egressキュー割合メニュー
        record.push(this.getEgressQueueMenu(qos.egress_queue_menu, qos.egress_queue_capability));
        // 16: トラヒック閾値
        record.push((cp.traffic_threshold != null) ? cp.traffic_threshold : "-");

        return record;
    }
    ;
    //
    // Egressキュー割合メニュー表示文言生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getEgressQueueMenu = function (egressQueueMenu, egressQueueCapability) {
        
        var egressQueue = "-";
        if (egressQueueMenu != null) {
            egressQueue = egressQueueMenu;
        }
        if (egressQueueCapability != null && egressQueueCapability.length > 0) {
            var capability = egressQueueCapability.join(",");
            egressQueue = egressQueue + "(" + egressQueueCapability + ")";
        }
        return egressQueue;
    }
    ;
    //
    // BGP表示文言生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getBgp = function (cpInfoBgp) {
        var bgp = "";
        var tmp;
        // bgp.role
        bgp = bgp + "role:" + cpInfoBgp.role + ", ";
        // bgp.neighbor_as
        bgp = bgp + "neighbor_as:" + cpInfoBgp.neighbor_as + ", ";
        // bgp.neighbor_ipv4_address
        tmp = (cpInfoBgp.neighbor_ipv4_address != null) ? cpInfoBgp.neighbor_ipv4_address : "-";
        bgp = bgp + "neighbor_ipv4_address:" + tmp + ", ";
        // bgp.neighbor_ipv6_address
        tmp = (cpInfoBgp.neighbor_ipv6_address != null) ? cpInfoBgp.neighbor_ipv6_address : "-";
        bgp = bgp + "neighbor_ipv6_address:" + tmp;
        return bgp;
    }
    ;
    //
    // 静的経路表示文言生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getStaticRoutes = function (cpInfoStaticRoutes) {
        var routesList = [];
        for (var i = 0; i < cpInfoStaticRoutes.length; i++) {
            var sr = cpInfoStaticRoutes[i];
            routesList.push("addr_type:" + sr.addr_type + ", address:" + sr.address + ", prefix:" + sr.prefix + ", next_hop:" + sr.next_hop);
        }
        var routes = (routesList.length != 0) ? routesList.join("<br>") : "-";
        return routes;
    }
    ;
    //
    // Vrrp表示文言生成.
    //
    MSF.MsfDetailSliceInfo.prototype.getVrrp = function (cpInfoVrrp) {
        var vrrp = "";
        vrrp = vrrp + "group_id:" + cpInfoVrrp.group_id + ", ";
        vrrp = vrrp + "role:" + cpInfoVrrp.role + ", ";
        var tmp = (cpInfoVrrp.virtual_ipv4_address != null) ? cpInfoVrrp.virtual_ipv4_address : "-";
        vrrp = vrrp + "virtual_ipv4_address:" + tmp;
        return vrrp;
    }
    ;
    //
    // 詳細情報出力対象のCP情報(L2)を返す
    //
    MSF.MsfDetailSliceInfo.prototype.getTargetL2 = function (baseData, sliceId, clusterId, leafs, deviceType, nodeId) {
        var l2cps = baseData.l2_cps;
        var clusterInfo = clusterId ? baseData.clusterInfoDic[clusterId] : {};
        var target = [];
        if (deviceType && deviceType != MSF.Const.FabricType.Leafs) {
            return target;
        }
        for (var sId in l2cps) {
            var l2cp = l2cps[sId];
            for (var i = 0; i < l2cp.length; i++) {
                var cp = l2cp[i];
                if (sliceId && sliceId != cp.slice_id) {
                    continue;
                }
                if (clusterId && clusterId != cp.cluster_id) {
                    continue;
                }
                if (leafs) {
                    // edge-point特定: base_if > leaf_node_id
                    var ep = clusterInfo.EdgepointDic[cp.edge_point_id];
                    if (!ep) {
                        continue;
                    }
                    if (nodeId && nodeId != ep.base_if.leaf_node_id) {
                        continue;
                    }
                    if (!leafs[ep.base_if.leaf_node_id]) {
                        continue;
                    }
                }
                target.push(cp);
            }
        }
        return target;
    }
    ;
    //
    // 詳細情報出力対象のCP情報(L3)を返す
    //
    MSF.MsfDetailSliceInfo.prototype.getTargetL3 = function (baseData, sliceId, clusterId, leafs, deviceType, nodeId) {
        var l3cps = baseData.l3_cps;
        var clusterInfo = clusterId ? baseData.clusterInfoDic[clusterId] : {};
        var target = [];
        if (deviceType && deviceType != MSF.Const.FabricType.Leafs) {
            return target;
        }
        for (var sId in l3cps) {
            var l3cp = l3cps[sId];
            for (var i = 0; i < l3cp.length; i++) {
                var cp = l3cp[i];
                if (sliceId && sliceId != cp.slice_id) {
                    continue;
                }
                if (clusterId && clusterId != cp.cluster_id) {
                    continue;
                }
                if (leafs) {
                    // edge-point特定: base_if > leaf_node_id
                    var ep = clusterInfo.EdgepointDic[cp.edge_point_id];
                    if (!ep) {
                        continue;
                    }
                    if (nodeId && nodeId != ep.base_if.leaf_node_id) {
                        continue;
                    }
                    if (!leafs[ep.base_if.leaf_node_id]) {
                        continue;
                    }
                }
                target.push(cp);
            }
        }
        return target;
    }
    ;
})();
