//
// MSF詳細エリアクラス
//
(function () {
    "use strict";

    MSF.MSFjsonParam = {};
    //機種情報登録
    MSF.MSFjsonParam.P010101 = [
    {
        UrlParam: false,
        Name: "equipment_type",
        Caption: "Params.P010101.equipment_type",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "equipment_type_id",
                Caption: "Params.P010101.equipment_type_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "platform",
                Caption: "Params.P010101.platform",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "os",
                Caption: "Params.P010101.os",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "firmware",
                Caption: "Params.P010101.firmware",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "router_type",
                Caption: "Params.P010101.router_type",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "physical_if_name_syntax",
                Caption: "Params.P010101.physical_if_name_syntax",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "breakout_if_name_syntax",
                Caption: "Params.P010101.breakout_if_name_syntax",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "breakout_if_name_suffix_list",
                Caption: "Params.P010101.breakout_if_name_suffix_list",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "capability",
                Caption: "Params.P010101.capability",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "vpn",
                        Caption: "Params.P010101.vpn",
                        repeatCount: 1,
                        Type: "label",
                        Visible: false,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "l2",
                                Caption: "Params.P010101.l2",
                                repeatCount: 1,
                                Type: "checkbox",
                                Visible: false,
                                Readonly: false,
                                Value: false,
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "l3",
                                Caption: "Params.P010101.l3",
                                repeatCount: 1,
                                Type: "checkbox",
                                Visible: false,
                                Readonly: false,
                                Value: false,
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    },
                    {
                        UrlParam: false,
                        Name: "qos",
                        Caption: "Params.P010101.qos",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "remark",
                                Caption: "Params.P010101.remark",
                                repeatCount: 1,
                                Type: "checkbox",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "remark_capability",
                                Caption: "Params.P010101.remark_capability",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "remark_default",
                                Caption: "Params.P010101.remark_default",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "shaping",
                                Caption: "Params.P010101.shaping",
                                repeatCount: 1,
                                Type: "checkbox",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "egress_queue_capability",
                                Caption: "Params.P010101.egress_queue_capability",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "egress_queue_default",
                                Caption: "Params.P010101.egress_queue_default",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            }
                        ]
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "dhcp",
                Caption: "Params.P010101.dhcp",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "dhcp_template",
                        Caption: "Params.P010101.dhcp_template",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "config_template",
                        Caption: "Params.P010101.config_template",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "initial_config",
                        Caption: "Params.P010101.initial_config",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 1,
                        children: []
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "snmp",
                Caption: "Params.P010101.snmp",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "if_name_oid",
                        Caption: "Params.P010101.if_name_oid",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "snmptrap_if_name_oid",
                        Caption: "Params.P010101.snmptrap_if_name_oid",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "max_repetitions",
                        Caption: "Params.P010101.max_repetitions",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 1,
                        children: []
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "boot_complete_msg",
                Caption: "Params.P010101.boot_complete_msg",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "boot_error_msgs",
                Caption: "Params.P010101.boot_error_msgs",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "if_definitions",
                Caption: "Params.P010101.if_definitions",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "ports",
                        Caption: "Params.P010101.ports",
                        repeatCount: 5,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "speed",
                                Caption: "Params.P010101.speed",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "port_prefix",
                                Caption: "Params.P010101.port_prefix",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    },
                    {
                        UrlParam: false,
                        Name: "lag_prefix",
                        Caption: "Params.P010101.lag_prefix",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "unit_connector",
                        Caption: "Params.P010101.unit_connector",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 1,
                        children: []
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "slots",
                Caption: "Params.P010101.slots",
                repeatCount: 5,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "if_id",
                        Caption: "Params.P010101.if_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "if_slot",
                        Caption: "Params.P010101.if_slot",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "speed_capabilities",
                        Caption: "Params.P010101.speed_capabilities",
                        repeatCount: 5,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    }
                ]
            }
        ]
    }
    ];

    //機種情報削除
    MSF.MSFjsonParam.P010104 = [
    {
        UrlParam: true,
        Name: "equipment_type_id",
        Caption: "Params.P010104.equipment_type_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //SWクラスタ増設
    MSF.MSFjsonParam.P010201 = [
    {
        UrlParam: false,
        Name: "cluster",
        Caption: "Params.P010201.cluster",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: [
            {
                UrlParam: false,
                Name: "cluster_id",
                Caption: "Params.P010201.cluster_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    }
    ];

    //SWクラスタ減設
    MSF.MSFjsonParam.P010204 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010204.cluster_id",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //Leaf追加
    MSF.MSFjsonParam.P010401 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010401.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "node_id",
        Caption: "Params.P010401.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "equipment_type_id",
        Caption: "Params.P010401.equipment_type_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "leaf_type",
        Caption: "Params.P010401.leaf_type",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "host_name",
        Caption: "Params.P010401.host_name",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "mac_address",
        Caption: "Params.P010401.mac_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "username",
        Caption: "Params.P010401.username",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.USERNAME,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "password",
        Caption: "Params.P010401.password",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.PASSW0RD,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "provisioning",
        Caption: "Params.P010401.provisioning",
        repeatCount: 1,
        Type: "checkbox",
        Visible: true,
        Readonly: false,
        Value: false,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "vpn_type",
        Caption: "Params.P010401.vpn_type",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "plane",
        Caption: "Params.P010401.plane",
        repeatCount: 1,
        Type: "number",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.PLANE,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "snmp_community",
        Caption: "Params.P010401.snmp_community",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: MSF.Conf.Rest.MFC.SNMP_COMMUNITY,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ntp_server_address",
        Caption: "Params.P010401.ntp_server_address",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: MSF.Conf.Rest.MFC.NTP_SERVER_ADDRESS,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "breakout",
        Caption: "Params.P010401.breakout",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "local",
                Caption: "Params.P010401.breakout_local",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "breakout_ifs",
                        Caption: "Params.P010401.breakout_local_breakout_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "breakout_if_ids",
                                Caption: "Params.P010401.breakout_local_breakout_ifs_breakout_if_ids",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "base_if",
                                Caption: "Params.P010401.breakout_local_breakout_ifs_base_if",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if_id",
                                        Caption: "Params.P010401.breakout_local_breakout_ifs_base_if_physical_if_id",
                                        repeatCount: 1,
                                        Type: "text",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 1,
                                        children: []
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "division_number",
                                Caption: "Params.P010401.breakout_local_breakout_ifs_division_number",
                                repeatCount: 1,
                                Type: "number",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "breakout_if_speed",
                                Caption: "Params.P010401.breakout_local_breakout_ifs_breakout_if_speed",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "opposite",
                Caption: "Params.P010401.breakout_opposite",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010401.breakout_opposite_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "breakout_ifs",
                        Caption: "Params.P010401.breakout_opposite_breakout_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "breakout_if_ids",
                                Caption: "Params.P010401.breakout_opposite_breakout_ifs_breakout_if_ids",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "base_if",
                                Caption: "Params.P010401.breakout_opposite_breakout_ifs_base_if",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if_id",
                                        Caption: "Params.P010401.breakout_opposite_breakout_ifs_base_if_physical_if_id",
                                        repeatCount: 1,
                                        Type: "text",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 1,
                                        children: []
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "division_number",
                                Caption: "Params.P010401.breakout_opposite_breakout_ifs_division_number",
                                repeatCount: 1,
                                Type: "number",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "breakout_if_speed",
                                Caption: "Params.P010401.breakout_opposite_breakout_ifs_breakout_if_speed",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        UrlParam: false,
        Name: "internal_links",
        Caption: "Params.P010401.internal_links",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "physical_links",
                Caption: "Params.P010401.internal_links_physical_links",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010401.internal_links_physical_links_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "local_traffic_threshold",
                        Caption: "Params.P010401.internal_links_physical_links_local_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "opposite_traffic_threshold",
                        Caption: "Params.P010401.internal_links_physical_links_opposite_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "internal_link_if",
                        Caption: "Params.P010401.internal_links_physical_links_internal_link_if",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "local",
                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_local_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "opposite",
                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010401.internal_links_physical_links_internal_link_if_opposite_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "lag_links",
                Caption: "Params.P010401.internal_links_lag_links",
                repeatCount: 50,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010401.internal_links_lag_links_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "local_traffic_threshold",
                        Caption: "Params.P010401.internal_links_lag_links_local_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "opposite_traffic_threshold",
                        Caption: "Params.P010401.internal_links_lag_links_opposite_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "member_ifs",
                        Caption: "Params.P010401.internal_links_lag_links_member_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "local",
                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_local",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010401.internal_links_lag_links_member_ifs_local_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_local_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_local_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010401.internal_links_lag_links_member_ifs_local_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_local_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "opposite",
                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010401.internal_links_lag_links_member_ifs_opposite_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        UrlParam: false,
        Name: "management_if_address",
        Caption: "Params.P010401.management_if_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "management_if_prefix",
        Caption: "Params.P010401.management_if_prefix",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //Leaf削除
    MSF.MSFjsonParam.P010404 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010404.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010404.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //Leaf変更
    MSF.MSFjsonParam.P010405 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010405.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010405.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "action",
        Caption: "Params.P010405.action",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: MSF.Const.ActionType.Change,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "leaf_type_option",
        Caption: "Params.P010405.leaf_type_option",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "leaf_type",
                Caption: "Params.P010405.leaf_type",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "BL or IL or EL",
                NewLine: 1,
                children: []
            }
        ]
    }
    ];

    //Spine追加
    MSF.MSFjsonParam.P010501 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010501.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "node_id",
        Caption: "Params.P010501.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "equipment_type_id",
        Caption: "Params.P010501.equipment_type_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "host_name",
        Caption: "Params.P010501.host_name",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "mac_address",
        Caption: "Params.P010501.mac_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "username",
        Caption: "Params.P010501.username",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.USERNAME,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "password",
        Caption: "Params.P010501.password",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.PASSW0RD,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "provisioning",
        Caption: "Params.P010501.provisioning",
        repeatCount: 1,
        Type: "checkbox",
        Visible: true,
        Readonly: false,
        Value: false,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "snmp_community",
        Caption: "Params.P010501.snmp_community",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: MSF.Conf.Rest.MFC.SNMP_COMMUNITY,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ntp_server_address",
        Caption: "Params.P010501.ntp_server_address",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: MSF.Conf.Rest.MFC.NTP_SERVER_ADDRESS,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "breakout",
        Caption: "Params.P010501.breakout",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "local",
                Caption: "Params.P010501.breakout_local",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "breakout_ifs",
                        Caption: "Params.P010501.breakout_local_breakout_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "breakout_if_ids",
                                Caption: "Params.P010501.breakout_local_breakout_ifs_breakout_if_ids",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "base_if",
                                Caption: "Params.P010501.breakout_local_breakout_ifs_base_if",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if_id",
                                        Caption: "Params.P010501.breakout_local_breakout_ifs_base_if_physical_if_id",
                                        repeatCount: 1,
                                        Type: "text",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 1,
                                        children: []
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "division_number",
                                Caption: "Params.P010501.breakout_local_breakout_ifs_division_number",
                                repeatCount: 1,
                                Type: "number",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "breakout_if_speed",
                                Caption: "Params.P010501.breakout_local_breakout_ifs_breakout_if_speed",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "opposite",
                Caption: "Params.P010501.breakout_opposite",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010501.breakout_opposite_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "breakout_ifs",
                        Caption: "Params.P010501.breakout_opposite_breakout_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "breakout_if_ids",
                                Caption: "Params.P010501.breakout_opposite_breakout_ifs_breakout_if_ids",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "base_if",
                                Caption: "Params.P010501.breakout_opposite_breakout_ifs_base_if",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if_id",
                                        Caption: "Params.P010501.breakout_opposite_breakout_ifs_base_if_physical_if_id",
                                        repeatCount: 1,
                                        Type: "text",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 1,
                                        children: []
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "division_number",
                                Caption: "Params.P010501.breakout_opposite_breakout_ifs_division_number",
                                repeatCount: 1,
                                Type: "number",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            },
                            {
                                UrlParam: false,
                                Name: "breakout_if_speed",
                                Caption: "Params.P010501.breakout_opposite_breakout_ifs_breakout_if_speed",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 1,
                                children: []
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        UrlParam: false,
        Name: "internal_links",
        Caption: "Params.P010501.internal_links",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "physical_links",
                Caption: "Params.P010501.internal_links_physical_links",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010501.internal_links_physical_links_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "local_traffic_threshold",
                        Caption: "Params.P010501.internal_links_physical_links_local_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "opposite_traffic_threshold",
                        Caption: "Params.P010501.internal_links_physical_links_opposite_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "internal_link_if",
                        Caption: "Params.P010501.internal_links_physical_links_internal_link_if",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "local",
                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_local_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "opposite",
                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010501.internal_links_physical_links_internal_link_if_opposite_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                UrlParam: false,
                Name: "lag_links",
                Caption: "Params.P010501.internal_links_lag_links",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "opposite_node_id",
                        Caption: "Params.P010501.internal_links_lag_links_opposite_node_id",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "local_traffic_threshold",
                        Caption: "Params.P010501.internal_links_lag_links_local_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "opposite_traffic_threshold",
                        Caption: "Params.P010501.internal_links_lag_links_opposite_traffic_threshold",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "member_ifs",
                        Caption: "Params.P010501.internal_links_lag_links_member_ifs",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "local",
                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_local",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010501.internal_links_lag_links_member_ifs_local_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_local_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_local_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010501.internal_links_lag_links_member_ifs_local_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_local_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                UrlParam: false,
                                Name: "opposite",
                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite",
                                repeatCount: 1,
                                Type: "label",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: [
                                    {
                                        UrlParam: false,
                                        Name: "physical_if",
                                        Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite_physical_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_id",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 0,
                                                children: []
                                            },
                                            {
                                                UrlParam: false,
                                                Name: "physical_if_speed",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_speed",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    },
                                    {
                                        UrlParam: false,
                                        Name: "breakout_if",
                                        Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite_breakout_if",
                                        repeatCount: 1,
                                        Type: "label",
                                        Visible: true,
                                        Readonly: false,
                                        Value: "",
                                        Candidate: "",
                                        NewLine: 0,
                                        children: [
                                            {
                                                UrlParam: false,
                                                Name: "breakout_if_id",
                                                Caption: "Params.P010501.internal_links_lag_links_member_ifs_opposite_breakout_if_breakout_if_id",
                                                repeatCount: 1,
                                                Type: "text",
                                                Visible: true,
                                                Readonly: false,
                                                Value: "",
                                                Candidate: "",
                                                NewLine: 1,
                                                children: []
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        UrlParam: false,
        Name: "management_if_address",
        Caption: "Params.P010501.management_if_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "management_if_prefix",
        Caption: "Params.P010501.management_if_prefix",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //Spine削除
    MSF.MSFjsonParam.P010504 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010504.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010504.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //物理IF情報変更
    MSF.MSFjsonParam.P010803 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010803.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: true,
        Name: "fabric_type",
        Caption: "Params.P010803.fabric_type",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010803.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "if_id",
        Caption: "Params.P010803.if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "action",
        Caption: "Params.P010803.action",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "speed_set or speed_delete",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "speed",
        Caption: "Params.P010803.speed",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //breakoutIF登録
    MSF.MSFjsonParam.P010901_add = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010901_add.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "fabric_type",
        Caption: "Params.P010901_add.fabric_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010901_add.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "",
        Caption: "Params.P010901_add.noname",
        repeatCount: 5,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "op",
                Caption: "Params.P010901_add.op",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: true,
                Value: MSF.Const.OP.ADD,
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "path",
                Caption: "Params.P010901_add.path",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "value",
                Caption: "Params.P010901_add.value",
                repeatCount: 1,
                Type: "label",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: [
                    {
                        UrlParam: false,
                        Name: "base_if",
                        Caption: "Params.P010901_add.base_if",
                        repeatCount: 1,
                        Type: "label",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: [
                            {
                                UrlParam: false,
                                Name: "physical_if_id",
                                Caption: "Params.P010901_add.physical_if_id",
                                repeatCount: 1,
                                Type: "text",
                                Visible: true,
                                Readonly: false,
                                Value: "",
                                Candidate: "",
                                NewLine: 0,
                                children: []
                            }
                        ]
                    },
                    {
                        UrlParam: false,
                        Name: "division_number",
                        Caption: "Params.P010901_add.division_number",
                        repeatCount: 1,
                        Type: "number",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    },
                    {
                        UrlParam: false,
                        Name: "breakout_if_speed",
                        Caption: "Params.P010901_add.breakout_if_speed",
                        repeatCount: 1,
                        Type: "text",
                        Visible: true,
                        Readonly: false,
                        Value: "",
                        Candidate: "",
                        NewLine: 0,
                        children: []
                    }
                ]
            }
        ]
    }
    ];

    //breakoutIF削除
    MSF.MSFjsonParam.P010901_delete = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P010901_delete.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "fabric_type",
        Caption: "Params.P010901_delete.fabric_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P010901_delete.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "",
        Caption: "Params.P010901_delete.noname",
        repeatCount: 5,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "op",
                Caption: "Params.P010901_delete.op",
                repeatCount: 1,
                Type: "text",
                Visible: false,
                Readonly: false,
                Value: MSF.Const.OP.REMOVE,
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "path",
                Caption: "Params.P010901_delete.path",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    }
    ];


    //LagIF生成
    MSF.MSFjsonParam.P011101 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011101.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "fabric_type",
        Caption: "Params.P011101.fabric_type",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P011101.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "physical_if_ids",
        Caption: "Params.P011101.physical_if_ids",
        repeatCount: 10,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "breakout_if_ids",
        Caption: "Params.P011101.breakout_if_ids",
        repeatCount: 10,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //LagIF削除
    MSF.MSFjsonParam.P011105 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011105.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "fabric_type",
        Caption: "Params.P011105.fabric_type",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "node_id",
        Caption: "Params.P011105.node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "lag_if_id",
        Caption: "Params.P011105.lag_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //クラスタ間リンクIF新設_physicalIF
    MSF.MSFjsonParam.P011201_physicalIF = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011201_physicalIF.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "cluster_link_if_id",
        Caption: "Params.P011201_physicalIF.cluster_link_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "opposite_cluster_id",
        Caption: "Params.P011201_physicalIF.opposite_cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "physical_link",
        Caption: "Params.P011201_physicalIF.physical_link",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "node_id",
                Caption: "Params.P011201_physicalIF.node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "physical_if_id",
                Caption: "Params.P011201_physicalIF.physical_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_node_id",
                Caption: "Params.P011201_physicalIF.opposite_node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_if_id",
                Caption: "Params.P011201_physicalIF.opposite_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_breakout_if_id",
                Caption: "Params.P011201_physicalIF.opposite_breakout_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "igp_cost",
        Caption: "Params.P011201_physicalIF.igp_cost",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "port_status",
        Caption: "Params.P011201_physicalIF.port_status",
        repeatCount: 1,
        Type: "checkbox",
        Visible: true,
        Readonly: false,
        Value: true,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv4_address",
        Caption: "Params.P011201_physicalIF.ipv4_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "traffic_threshold",
        Caption: "Params.P011201_physicalIF.traffic_threshold",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //クラスタ間リンクIF新設_breakoutIF
    MSF.MSFjsonParam.P011201_breakoutIF = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011201_breakoutIF.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "cluster_link_if_id",
        Caption: "Params.P011201_breakoutIF.cluster_link_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "opposite_cluster_id",
        Caption: "Params.P011201_breakoutIF.opposite_cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "physical_link",
        Caption: "Params.P011201_breakoutIF.physical_link",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "node_id",
                Caption: "Params.P011201_breakoutIF.node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "breakout_if_id",
                Caption: "Params.P011201_breakoutIF.breakout_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_node_id",
                Caption: "Params.P011201_breakoutIF.opposite_node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_if_id",
                Caption: "Params.P011201_breakoutIF.opposite_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_breakout_if_id",
                Caption: "Params.P011201_breakoutIF.opposite_breakout_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "igp_cost",
        Caption: "Params.P011201_breakoutIF.igp_cost",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "port_status",
        Caption: "Params.P011201_breakoutIF.port_status",
        repeatCount: 1,
        Type: "checkbox",
        Visible: true,
        Readonly: false,
        Value: true,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv4_address",
        Caption: "Params.P011201_breakoutIF.ipv4_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "traffic_threshold",
        Caption: "Params.P011201_breakoutIF.traffic_threshold",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //クラスタ間リンクIF新設_lagIF
    MSF.MSFjsonParam.P011201_lagIF = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011201_lagIF.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "cluster_link_if_id",
        Caption: "Params.P011201_lagIF.cluster_link_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "opposite_cluster_id",
        Caption: "Params.P011201_lagIF.opposite_cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "lag_link",
        Caption: "Params.P011201_lagIF.lag_link",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "node_id",
                Caption: "Params.P011201_lagIF.node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "lag_if_id",
                Caption: "Params.P011201_lagIF.lag_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_node_id",
                Caption: "Params.P011201_lagIF.opposite_node_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "opposite_lag_if_id",
                Caption: "Params.P011201_lagIF.opposite_lag_if_id",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "igp_cost",
        Caption: "Params.P011201_lagIF.igp_cost",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "port_status",
        Caption: "Params.P011201_lagIF.port_status",
        repeatCount: 1,
        Type: "checkbox",
        Visible: true,
        Readonly: false,
        Value: true,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv4_address",
        Caption: "Params.P011201_lagIF.ipv4_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "traffic_threshold",
        Caption: "Params.P011201_lagIF.traffic_threshold",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //クラスタ間リンクIF減設
    MSF.MSFjsonParam.P011204 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011204.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "cluster_link_if_id",
        Caption: "Params.P011204.cluster_link_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //edge-point登録
    MSF.MSFjsonParam.P011401 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011401.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "leaf_node_id",
        Caption: "Params.P011401.leaf_node_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "lag_if_id",
        Caption: "Params.P011401.lag_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "physical_if_id",
        Caption: "Params.P011401.physical_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "breakout_if_id",
        Caption: "Params.P011401.breakout_if_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "traffic_threshold",
        Caption: "Params.P011401.traffic_threshold",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //edge-point削除
    MSF.MSFjsonParam.P011404 = [
    {
        UrlParam: true,
        Name: "cluster_id",
        Caption: "Params.P011404.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "edge_point_id",
        Caption: "Params.P011404.edge_point_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //スライス生成(L2)
    MSF.MSFjsonParam.P020101_l2 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020101_l2.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "slice_id",
        Caption: "Params.P020101_l2.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "remark_menu",
        Caption: "Params.P020101_l2.remark_menu",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "vrf_id",
        Caption: "Params.P020101_l2.vrf_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //スライス生成(L3)
    MSF.MSFjsonParam.P020101_l3 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020101_l3.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "slice_id",
        Caption: "Params.P020101_l3.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "plane",
        Caption: "Params.P020101_l3.plane",
        repeatCount: 1,
        Type: "number",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.PLANE,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "remark_menu",
        Caption: "Params.P020101_l3.remark_menu",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "vrf_id",
        Caption: "Params.P020101_l3.vrf_id",
        repeatCount: 1,
        Type: "text",
        Visible: false,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //スライス変更
    MSF.MSFjsonParam.P020102 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020102.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "slice_id",
        Caption: "Params.P020102.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "action",
        Caption: "Params.P020102.action",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: MSF.Const.ActionType.UpdateRemark,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "remark_menu",
        Caption: "Params.P020102.remark_menu",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //スライス削除
    MSF.MSFjsonParam.P020103 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020103.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "slice_id",
        Caption: "Params.P020103.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //CP生成(L2)
    MSF.MSFjsonParam.P020201_add_l2 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020201_add_l2.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "slice_id",
        Caption: "Params.P020201_add_l2.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "cluster_id",
        Caption: "Params.P020201_add_l2.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "edge_point_id",
        Caption: "Params.P020201_add_l2.edge_point_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "vlan_id",
        Caption: "Params.P020201_add_l2.vlan_id",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "cp_id",
        Caption: "Params.P020201_add_l2.cp_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "pair_cp_id",
        Caption: "Params.P020201_add_l2.pair_cp_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "qos",
        Caption: "Params.P020201_add_l2.qos",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "ingress_shaping_rate",
                Caption: "Params.P020201_add_l2.qos_ingress_shaping_rate",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "egress_shaping_rate",
                Caption: "Params.P020201_add_l2.qos_egress_shaping_rate",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "egress_queue_menu",
                Caption: "Params.P020201_add_l2.qos_egress_queue_menu",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "port_mode",
        Caption: "Params.P020201_add_l2.port_mode",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

    //CP生成(L3)
    MSF.MSFjsonParam.P020201_add_l3 = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020201_add_l3.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "slice_id",
        Caption: "Params.P020201_add_l3.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    },
    {
        UrlParam: false,
        Name: "cluster_id",
        Caption: "Params.P020201_add_l3.cluster_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "edge_point_id",
        Caption: "Params.P020201_add_l3.edge_point_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "vlan_id",
        Caption: "Params.P020201_add_l3.vlan_id",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "mtu",
        Caption: "Params.P020201_add_l3.mtu",
        repeatCount: 1,
        Type: "number",
        Visible: false,
        Readonly: false,
        Value: MSF.Conf.Rest.MFC.MTU,
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "cp_id",
        Caption: "Params.P020201_add_l3.cp_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "qos",
        Caption: "Params.P020201_add_l3.qos",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "ingress_shaping_rate",
                Caption: "Params.P020201_add_l3.qos_ingress_shaping_rate",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "egress_shaping_rate",
                Caption: "Params.P020201_add_l3.qos_egress_shaping_rate",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "egress_queue_menu",
                Caption: "Params.P020201_add_l3.qos_egress_queue_menu",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "ipv4_address",
        Caption: "Params.P020201_add_l3.ipv4_address",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv4_prefix",
        Caption: "Params.P020201_add_l3.ipv4_prefix",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv6_address",
        Caption: "Params.P020201_add_l3.ipv6_addr",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "ipv6_prefix",
        Caption: "Params.P020201_add_l3.ipv6_prefix",
        repeatCount: 1,
        Type: "number",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: false,
        Name: "bgp",
        Caption: "Params.P020201_add_l3.bgp",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "role",
                Caption: "Params.P020201_add_l3.bgp_role",
                repeatCount: 1,
                Type: "text",
                Visible: false,
                Readonly: false,
                Value: MSF.Conf.Rest.MFC.ROLE,
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "neighbor_as",
                Caption: "Params.P020201_add_l3.bgp_neighbor_as",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "neighbor_ipv4_address",
                Caption: "Params.P020201_add_l3.bgp_neighbor_ipv4_addr",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "neighbor_ipv6_address",
                Caption: "Params.P020201_add_l3.bgp_neighbor_ipv6_addr",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "static_routes",
        Caption: "Params.P020201_add_l3.static_routes",
        repeatCount: 10,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "addr_type",
                Caption: "Params.P020201_add_l3.static_routes_addr_type",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "ipv4 or ipv6",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "address",
                Caption: "Params.P020201_add_l3.static_routes_addr",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "prefix",
                Caption: "Params.P020201_add_l3.static_routes_prefix",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "next_hop",
                Caption: "Params.P020201_add_l3.static_routes_next_hop",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "vrrp",
        Caption: "Params.P020201_add_l3.vrrp",
        repeatCount: 1,
        Type: "label",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: [
            {
                UrlParam: false,
                Name: "group_id",
                Caption: "Params.P020201_add_l3.vrrp_group_id",
                repeatCount: 1,
                Type: "number",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "role",
                Caption: "Params.P020201_add_l3.vrrp_role",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "master or slave",
                NewLine: 0,
                children: []
            },
            {
                UrlParam: false,
                Name: "virtual_ipv4_address",
                Caption: "Params.P020201_add_l3.vrrp_virtual_ipv4_addr",
                repeatCount: 1,
                Type: "text",
                Visible: true,
                Readonly: false,
                Value: "",
                Candidate: "",
                NewLine: 0,
                children: []
            }
        ]
    },
    {
        UrlParam: false,
        Name: "traffic_threshold",
        Caption: "Params.P020201_add_l3.traffic_threshold",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    }
    ];

    //CP削除
    MSF.MSFjsonParam.P020201_delete = [
    {
        UrlParam: true,
        Name: "slice_type",
        Caption: "Params.P020201_delete.slice_type",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "slice_id",
        Caption: "Params.P020201_delete.slice_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: true,
        Value: "",
        Candidate: "",
        NewLine: 0,
        children: []
    },
    {
        UrlParam: true,
        Name: "cp_id",
        Caption: "Params.P020201_delete.cp_id",
        repeatCount: 1,
        Type: "text",
        Visible: true,
        Readonly: false,
        Value: "",
        Candidate: "",
        NewLine: 1,
        children: []
    }
    ];

})();
