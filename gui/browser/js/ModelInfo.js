
//
// 機種情報
// GS: General Spine
// IL: IP VPN Leaf
// EL: Ethernet Leaf
// BL: Border Leaf
//
MSF.ModelInfo = {
    "QFX5100-48S(GS)": {
        "equipment_type": {
            "platform": "QFX5100-48S(GS)",
            "os": "JUNOS",
            "firmware": "14.1X53-D35",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>:<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5100",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ILeaf_1_0_ztp_init.conf.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ILeaf_1_0_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "52",
                    "if_slot": "0/0/52",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "53",
                    "if_slot": "0/0/53",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5100-48S_front.png" },
                "caption": { x: 5, y:20, title: "QFX5100-48S_GS front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  17,  y: 38,  w: 20,  h: 20 },
                    { id:  1,  x:  17,  y: 63,  w: 20,  h: 20 },
                    { id:  2,  x:  40,  y: 38,  w: 20,  h: 20 },
                    { id:  3,  x:  40,  y: 63,  w: 20,  h: 20 },
                    { id:  4,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  5,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  6,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  7,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  8,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id:  9,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id: 10,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id: 11,  x: 132,  y: 63,  w: 20,  h: 20 },

                    { id: 12,  x: 158,  y: 38,  w: 20,  h: 20 },
                    { id: 13,  x: 158,  y: 63,  w: 20,  h: 20 },
                    { id: 14,  x: 181,  y: 38,  w: 20,  h: 20 },
                    { id: 15,  x: 181,  y: 63,  w: 20,  h: 20 },
                    { id: 16,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 17,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 18,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 19,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 20,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 21,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 22,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 23,  x: 273,  y: 63,  w: 20,  h: 20 },

                    { id: 24,  x: 299,  y: 38,  w: 20,  h: 20 },
                    { id: 25,  x: 299,  y: 63,  w: 20,  h: 20 },
                    { id: 26,  x: 322,  y: 38,  w: 20,  h: 20 },
                    { id: 27,  x: 322,  y: 63,  w: 20,  h: 20 },
                    { id: 28,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 29,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 30,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 31,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 32,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 33,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 34,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 35,  x: 414,  y: 63,  w: 20,  h: 20 },

                    { id: 36,  x: 440,  y: 38,  w: 20,  h: 20 },
                    { id: 37,  x: 440,  y: 63,  w: 20,  h: 20 },
                    { id: 38,  x: 463,  y: 38,  w: 20,  h: 20 },
                    { id: 39,  x: 463,  y: 63,  w: 20,  h: 20 },
                    { id: 40,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 41,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 42,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 43,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 44,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 45,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 46,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 47,  x: 555,  y: 63,  w: 20,  h: 20 },

                    { id: 48,  x: 599,  y: 38,  w: 27,  h: 20 },
                    { id: 49,  x: 599,  y: 63,  w: 27,  h: 20 },
                    { id: 50,  x: 629,  y: 38,  w: 27,  h: 20 },
                    { id: 51,  x: 629,  y: 63,  w: 27,  h: 20 },
                    { id: 52,  x: 659,  y: 38,  w: 27,  h: 20 },
                    { id: 53,  x: 659,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  28,  y: 45  },
                    { id:  1,  x:  28,  y: 108 },
                    { id:  2,  x:  51,  y: 45  },
                    { id:  3,  x:  51,  y: 108 },
                    { id:  4,  x:  74,  y: 45  },
                    { id:  5,  x:  74,  y: 108 },
                    { id:  6,  x:  97,  y: 45  },
                    { id:  7,  x:  97,  y: 108 },
                    { id:  8,  x: 120,  y: 45  },
                    { id:  9,  x: 120,  y: 108 },
                    { id: 10,  x: 143,  y: 45  },
                    { id: 11,  x: 143,  y: 108 },

                    { id: 12,  x: 169,  y: 45  },
                    { id: 13,  x: 169,  y: 108 },
                    { id: 14,  x: 192,  y: 45  },
                    { id: 15,  x: 192,  y: 108 },
                    { id: 16,  x: 215,  y: 45  },
                    { id: 17,  x: 215,  y: 108 },
                    { id: 18,  x: 238,  y: 45  },
                    { id: 19,  x: 238,  y: 108 },
                    { id: 20,  x: 261,  y: 45  },
                    { id: 21,  x: 261,  y: 108 },
                    { id: 22,  x: 284,  y: 45  },
                    { id: 23,  x: 284,  y: 108 },

                    { id: 24,  x: 310,  y: 45  },
                    { id: 25,  x: 310,  y: 108 },
                    { id: 26,  x: 333,  y: 45  },
                    { id: 27,  x: 333,  y: 108 },
                    { id: 28,  x: 356,  y: 45  },
                    { id: 29,  x: 356,  y: 108 },
                    { id: 30,  x: 379,  y: 45  },
                    { id: 31,  x: 379,  y: 108 },
                    { id: 32,  x: 402,  y: 45  },
                    { id: 33,  x: 402,  y: 108 },
                    { id: 34,  x: 425,  y: 45  },
                    { id: 35,  x: 425,  y: 108 },

                    { id: 36,  x: 451,  y: 45  },
                    { id: 37,  x: 451,  y: 108 },
                    { id: 38,  x: 474,  y: 45  },
                    { id: 39,  x: 474,  y: 108 },
                    { id: 40,  x: 497,  y: 45  },
                    { id: 41,  x: 497,  y: 108 },
                    { id: 42,  x: 520,  y: 45  },
                    { id: 43,  x: 520,  y: 108 },
                    { id: 44,  x: 543,  y: 45  },
                    { id: 45,  x: 543,  y: 108 },
                    { id: 46,  x: 566,  y: 45  },
                    { id: 47,  x: 566,  y: 108 },

                    { id: 48,  x: 612,  y: 45  },
                    { id: 49,  x: 612,  y: 108 },
                    { id: 50,  x: 642,  y: 45  },
                    { id: 51,  x: 642,  y: 108 },
                    { id: 52,  x: 672,  y: 45  },
                    { id: 53,  x: 672,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5100-48S_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5100-48S_GS rear", anchor: "start" }
            }
        }
    },

    "QFX5100-48S(IL)": {
        "equipment_type": {
            "platform": "QFX5100-48S(IL)",
            "os": "JUNOS",
            "firmware": "14.1X53-D35",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5100",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ILeaf_1_0_ztp_init.conf.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ILeaf_1_0_ztp_init"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "52",
                    "if_slot": "0/0/52",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "53",
                    "if_slot": "0/0/53",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5100-48S_front.png" },
                "caption": { x: 5, y:20, title: "QFX5100-48S_IL front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  17,  y: 38,  w: 20,  h: 20 },
                    { id:  1,  x:  17,  y: 63,  w: 20,  h: 20 },
                    { id:  2,  x:  40,  y: 38,  w: 20,  h: 20 },
                    { id:  3,  x:  40,  y: 63,  w: 20,  h: 20 },
                    { id:  4,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  5,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  6,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  7,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  8,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id:  9,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id: 10,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id: 11,  x: 132,  y: 63,  w: 20,  h: 20 },

                    { id: 12,  x: 158,  y: 38,  w: 20,  h: 20 },
                    { id: 13,  x: 158,  y: 63,  w: 20,  h: 20 },
                    { id: 14,  x: 181,  y: 38,  w: 20,  h: 20 },
                    { id: 15,  x: 181,  y: 63,  w: 20,  h: 20 },
                    { id: 16,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 17,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 18,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 19,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 20,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 21,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 22,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 23,  x: 273,  y: 63,  w: 20,  h: 20 },

                    { id: 24,  x: 299,  y: 38,  w: 20,  h: 20 },
                    { id: 25,  x: 299,  y: 63,  w: 20,  h: 20 },
                    { id: 26,  x: 322,  y: 38,  w: 20,  h: 20 },
                    { id: 27,  x: 322,  y: 63,  w: 20,  h: 20 },
                    { id: 28,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 29,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 30,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 31,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 32,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 33,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 34,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 35,  x: 414,  y: 63,  w: 20,  h: 20 },

                    { id: 36,  x: 440,  y: 38,  w: 20,  h: 20 },
                    { id: 37,  x: 440,  y: 63,  w: 20,  h: 20 },
                    { id: 38,  x: 463,  y: 38,  w: 20,  h: 20 },
                    { id: 39,  x: 463,  y: 63,  w: 20,  h: 20 },
                    { id: 40,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 41,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 42,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 43,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 44,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 45,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 46,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 47,  x: 555,  y: 63,  w: 20,  h: 20 },

                    { id: 48,  x: 599,  y: 38,  w: 27,  h: 20 },
                    { id: 49,  x: 599,  y: 63,  w: 27,  h: 20 },
                    { id: 50,  x: 629,  y: 38,  w: 27,  h: 20 },
                    { id: 51,  x: 629,  y: 63,  w: 27,  h: 20 },
                    { id: 52,  x: 659,  y: 38,  w: 27,  h: 20 },
                    { id: 53,  x: 659,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  28,  y: 45  },
                    { id:  1,  x:  28,  y: 108 },
                    { id:  2,  x:  51,  y: 45  },
                    { id:  3,  x:  51,  y: 108 },
                    { id:  4,  x:  74,  y: 45  },
                    { id:  5,  x:  74,  y: 108 },
                    { id:  6,  x:  97,  y: 45  },
                    { id:  7,  x:  97,  y: 108 },
                    { id:  8,  x: 120,  y: 45  },
                    { id:  9,  x: 120,  y: 108 },
                    { id: 10,  x: 143,  y: 45  },
                    { id: 11,  x: 143,  y: 108 },

                    { id: 12,  x: 169,  y: 45  },
                    { id: 13,  x: 169,  y: 108 },
                    { id: 14,  x: 192,  y: 45  },
                    { id: 15,  x: 192,  y: 108 },
                    { id: 16,  x: 215,  y: 45  },
                    { id: 17,  x: 215,  y: 108 },
                    { id: 18,  x: 238,  y: 45  },
                    { id: 19,  x: 238,  y: 108 },
                    { id: 20,  x: 261,  y: 45  },
                    { id: 21,  x: 261,  y: 108 },
                    { id: 22,  x: 284,  y: 45  },
                    { id: 23,  x: 284,  y: 108 },

                    { id: 24,  x: 310,  y: 45  },
                    { id: 25,  x: 310,  y: 108 },
                    { id: 26,  x: 333,  y: 45  },
                    { id: 27,  x: 333,  y: 108 },
                    { id: 28,  x: 356,  y: 45  },
                    { id: 29,  x: 356,  y: 108 },
                    { id: 30,  x: 379,  y: 45  },
                    { id: 31,  x: 379,  y: 108 },
                    { id: 32,  x: 402,  y: 45  },
                    { id: 33,  x: 402,  y: 108 },
                    { id: 34,  x: 425,  y: 45  },
                    { id: 35,  x: 425,  y: 108 },

                    { id: 36,  x: 451,  y: 45  },
                    { id: 37,  x: 451,  y: 108 },
                    { id: 38,  x: 474,  y: 45  },
                    { id: 39,  x: 474,  y: 108 },
                    { id: 40,  x: 497,  y: 45  },
                    { id: 41,  x: 497,  y: 108 },
                    { id: 42,  x: 520,  y: 45  },
                    { id: 43,  x: 520,  y: 108 },
                    { id: 44,  x: 543,  y: 45  },
                    { id: 45,  x: 543,  y: 108 },
                    { id: 46,  x: 566,  y: 45  },
                    { id: 47,  x: 566,  y: 108 },

                    { id: 48,  x: 612,  y: 45  },
                    { id: 49,  x: 612,  y: 108 },
                    { id: 50,  x: 642,  y: 45  },
                    { id: 51,  x: 642,  y: 108 },
                    { id: 52,  x: 672,  y: 45  },
                    { id: 53,  x: 672,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5100-48S_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5100-48S_IL rear", anchor: "start" }
            }
        }
    },

    "QFX5100-48S(EL)": {
        "equipment_type": {
            "platform": "QFX5100-48S(EL)",
            "os": "JUNOS",
            "firmware": "14.1X53-D35",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": false
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5100",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ELeaf_1_0_ztp_init.conf.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5100_ELeaf_1_0_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "52",
                    "if_slot": "0/0/52",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "53",
                    "if_slot": "0/0/53",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5100-48S_front.png" },
                "caption": { x: 5, y:20, title: "QFX5100-48S_EL front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  17,  y: 38,  w: 20,  h: 20 },
                    { id:  1,  x:  17,  y: 63,  w: 20,  h: 20 },
                    { id:  2,  x:  40,  y: 38,  w: 20,  h: 20 },
                    { id:  3,  x:  40,  y: 63,  w: 20,  h: 20 },
                    { id:  4,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  5,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  6,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  7,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  8,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id:  9,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id: 10,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id: 11,  x: 132,  y: 63,  w: 20,  h: 20 },

                    { id: 12,  x: 158,  y: 38,  w: 20,  h: 20 },
                    { id: 13,  x: 158,  y: 63,  w: 20,  h: 20 },
                    { id: 14,  x: 181,  y: 38,  w: 20,  h: 20 },
                    { id: 15,  x: 181,  y: 63,  w: 20,  h: 20 },
                    { id: 16,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 17,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 18,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 19,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 20,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 21,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 22,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 23,  x: 273,  y: 63,  w: 20,  h: 20 },

                    { id: 24,  x: 299,  y: 38,  w: 20,  h: 20 },
                    { id: 25,  x: 299,  y: 63,  w: 20,  h: 20 },
                    { id: 26,  x: 322,  y: 38,  w: 20,  h: 20 },
                    { id: 27,  x: 322,  y: 63,  w: 20,  h: 20 },
                    { id: 28,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 29,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 30,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 31,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 32,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 33,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 34,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 35,  x: 414,  y: 63,  w: 20,  h: 20 },

                    { id: 36,  x: 440,  y: 38,  w: 20,  h: 20 },
                    { id: 37,  x: 440,  y: 63,  w: 20,  h: 20 },
                    { id: 38,  x: 463,  y: 38,  w: 20,  h: 20 },
                    { id: 39,  x: 463,  y: 63,  w: 20,  h: 20 },
                    { id: 40,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 41,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 42,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 43,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 44,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 45,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 46,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 47,  x: 555,  y: 63,  w: 20,  h: 20 },

                    { id: 48,  x: 599,  y: 38,  w: 27,  h: 20 },
                    { id: 49,  x: 599,  y: 63,  w: 27,  h: 20 },
                    { id: 50,  x: 629,  y: 38,  w: 27,  h: 20 },
                    { id: 51,  x: 629,  y: 63,  w: 27,  h: 20 },
                    { id: 52,  x: 659,  y: 38,  w: 27,  h: 20 },
                    { id: 53,  x: 659,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  28,  y: 45  },
                    { id:  1,  x:  28,  y: 108 },
                    { id:  2,  x:  51,  y: 45  },
                    { id:  3,  x:  51,  y: 108 },
                    { id:  4,  x:  74,  y: 45  },
                    { id:  5,  x:  74,  y: 108 },
                    { id:  6,  x:  97,  y: 45  },
                    { id:  7,  x:  97,  y: 108 },
                    { id:  8,  x: 120,  y: 45  },
                    { id:  9,  x: 120,  y: 108 },
                    { id: 10,  x: 143,  y: 45  },
                    { id: 11,  x: 143,  y: 108 },

                    { id: 12,  x: 169,  y: 45  },
                    { id: 13,  x: 169,  y: 108 },
                    { id: 14,  x: 192,  y: 45  },
                    { id: 15,  x: 192,  y: 108 },
                    { id: 16,  x: 215,  y: 45  },
                    { id: 17,  x: 215,  y: 108 },
                    { id: 18,  x: 238,  y: 45  },
                    { id: 19,  x: 238,  y: 108 },
                    { id: 20,  x: 261,  y: 45  },
                    { id: 21,  x: 261,  y: 108 },
                    { id: 22,  x: 284,  y: 45  },
                    { id: 23,  x: 284,  y: 108 },

                    { id: 24,  x: 310,  y: 45  },
                    { id: 25,  x: 310,  y: 108 },
                    { id: 26,  x: 333,  y: 45  },
                    { id: 27,  x: 333,  y: 108 },
                    { id: 28,  x: 356,  y: 45  },
                    { id: 29,  x: 356,  y: 108 },
                    { id: 30,  x: 379,  y: 45  },
                    { id: 31,  x: 379,  y: 108 },
                    { id: 32,  x: 402,  y: 45  },
                    { id: 33,  x: 402,  y: 108 },
                    { id: 34,  x: 425,  y: 45  },
                    { id: 35,  x: 425,  y: 108 },

                    { id: 36,  x: 451,  y: 45  },
                    { id: 37,  x: 451,  y: 108 },
                    { id: 38,  x: 474,  y: 45  },
                    { id: 39,  x: 474,  y: 108 },
                    { id: 40,  x: 497,  y: 45  },
                    { id: 41,  x: 497,  y: 108 },
                    { id: 42,  x: 520,  y: 45  },
                    { id: 43,  x: 520,  y: 108 },
                    { id: 44,  x: 543,  y: 45  },
                    { id: 45,  x: 543,  y: 108 },
                    { id: 46,  x: 566,  y: 45  },
                    { id: 47,  x: 566,  y: 108 },

                    { id: 48,  x: 612,  y: 45  },
                    { id: 49,  x: 612,  y: 108 },
                    { id: 50,  x: 642,  y: 45  },
                    { id: 51,  x: 642,  y: 108 },
                    { id: 52,  x: 672,  y: 45  },
                    { id: 53,  x: 672,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5100-48S_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5100-48S_EL rear", anchor: "start" }
            }
        }
    },

    "QFX5100-48S(BL)": {
        "equipment_type": {
            "platform": "QFX5100-48S(BL)",
            "os": "JUNOS",
            "firmware": "14.1X53-D35",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5100",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5100_BLeaf_1_0_ztp_init.conf.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5100_BLeaf_1_0_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "52",
                    "if_slot": "0/0/52",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "53",
                    "if_slot": "0/0/53",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5100-48S_front.png" },
                "caption": { x: 5, y:20, title: "QFX5100-48S_BL front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  17,  y: 38,  w: 20,  h: 20 },
                    { id:  1,  x:  17,  y: 63,  w: 20,  h: 20 },
                    { id:  2,  x:  40,  y: 38,  w: 20,  h: 20 },
                    { id:  3,  x:  40,  y: 63,  w: 20,  h: 20 },
                    { id:  4,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  5,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  6,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  7,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  8,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id:  9,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id: 10,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id: 11,  x: 132,  y: 63,  w: 20,  h: 20 },

                    { id: 12,  x: 158,  y: 38,  w: 20,  h: 20 },
                    { id: 13,  x: 158,  y: 63,  w: 20,  h: 20 },
                    { id: 14,  x: 181,  y: 38,  w: 20,  h: 20 },
                    { id: 15,  x: 181,  y: 63,  w: 20,  h: 20 },
                    { id: 16,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 17,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 18,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 19,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 20,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 21,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 22,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 23,  x: 273,  y: 63,  w: 20,  h: 20 },

                    { id: 24,  x: 299,  y: 38,  w: 20,  h: 20 },
                    { id: 25,  x: 299,  y: 63,  w: 20,  h: 20 },
                    { id: 26,  x: 322,  y: 38,  w: 20,  h: 20 },
                    { id: 27,  x: 322,  y: 63,  w: 20,  h: 20 },
                    { id: 28,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 29,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 30,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 31,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 32,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 33,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 34,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 35,  x: 414,  y: 63,  w: 20,  h: 20 },

                    { id: 36,  x: 440,  y: 38,  w: 20,  h: 20 },
                    { id: 37,  x: 440,  y: 63,  w: 20,  h: 20 },
                    { id: 38,  x: 463,  y: 38,  w: 20,  h: 20 },
                    { id: 39,  x: 463,  y: 63,  w: 20,  h: 20 },
                    { id: 40,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 41,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 42,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 43,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 44,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 45,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 46,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 47,  x: 555,  y: 63,  w: 20,  h: 20 },

                    { id: 48,  x: 599,  y: 38,  w: 27,  h: 20 },
                    { id: 49,  x: 599,  y: 63,  w: 27,  h: 20 },
                    { id: 50,  x: 629,  y: 38,  w: 27,  h: 20 },
                    { id: 51,  x: 629,  y: 63,  w: 27,  h: 20 },
                    { id: 52,  x: 659,  y: 38,  w: 27,  h: 20 },
                    { id: 53,  x: 659,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  28,  y: 45  },
                    { id:  1,  x:  28,  y: 108 },
                    { id:  2,  x:  51,  y: 45  },
                    { id:  3,  x:  51,  y: 108 },
                    { id:  4,  x:  74,  y: 45  },
                    { id:  5,  x:  74,  y: 108 },
                    { id:  6,  x:  97,  y: 45  },
                    { id:  7,  x:  97,  y: 108 },
                    { id:  8,  x: 120,  y: 45  },
                    { id:  9,  x: 120,  y: 108 },
                    { id: 10,  x: 143,  y: 45  },
                    { id: 11,  x: 143,  y: 108 },

                    { id: 12,  x: 169,  y: 45  },
                    { id: 13,  x: 169,  y: 108 },
                    { id: 14,  x: 192,  y: 45  },
                    { id: 15,  x: 192,  y: 108 },
                    { id: 16,  x: 215,  y: 45  },
                    { id: 17,  x: 215,  y: 108 },
                    { id: 18,  x: 238,  y: 45  },
                    { id: 19,  x: 238,  y: 108 },
                    { id: 20,  x: 261,  y: 45  },
                    { id: 21,  x: 261,  y: 108 },
                    { id: 22,  x: 284,  y: 45  },
                    { id: 23,  x: 284,  y: 108 },

                    { id: 24,  x: 310,  y: 45  },
                    { id: 25,  x: 310,  y: 108 },
                    { id: 26,  x: 333,  y: 45  },
                    { id: 27,  x: 333,  y: 108 },
                    { id: 28,  x: 356,  y: 45  },
                    { id: 29,  x: 356,  y: 108 },
                    { id: 30,  x: 379,  y: 45  },
                    { id: 31,  x: 379,  y: 108 },
                    { id: 32,  x: 402,  y: 45  },
                    { id: 33,  x: 402,  y: 108 },
                    { id: 34,  x: 425,  y: 45  },
                    { id: 35,  x: 425,  y: 108 },

                    { id: 36,  x: 451,  y: 45  },
                    { id: 37,  x: 451,  y: 108 },
                    { id: 38,  x: 474,  y: 45  },
                    { id: 39,  x: 474,  y: 108 },
                    { id: 40,  x: 497,  y: 45  },
                    { id: 41,  x: 497,  y: 108 },
                    { id: 42,  x: 520,  y: 45  },
                    { id: 43,  x: 520,  y: 108 },
                    { id: 44,  x: 543,  y: 45  },
                    { id: 45,  x: 543,  y: 108 },
                    { id: 46,  x: 566,  y: 45  },
                    { id: 47,  x: 566,  y: 108 },

                    { id: 48,  x: 612,  y: 45  },
                    { id: 49,  x: 612,  y: 108 },
                    { id: 50,  x: 642,  y: 45  },
                    { id: 51,  x: 642,  y: 108 },
                    { id: 52,  x: 672,  y: 45  },
                    { id: 53,  x: 672,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5100-48S_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5100-48S_BL rear", anchor: "start" }
            }
        }
    },

    "QFX5100-24Q(GS)": {
        "equipment_type": {
            "platform": "QFX5100-24Q(GS)",
            "os": "JUNOS",
            "firmware": "14.1X53-D35.3",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>:<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5100",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5100_Spine_1_0_ztp_init.conf.Line1-BlockZ.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5100_Spine_1_0_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "40g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "xe-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["40g"]
                }, {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5100-24Q_front.png" },
                "caption": { x: 5, y:20, title: "QFX5100-24Q_GS front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  23,  y: 38,  w: 27,  h: 20 },
                    { id:  1,  x:  23,  y: 63,  w: 27,  h: 20 },
                    { id:  2,  x:  53,  y: 38,  w: 27,  h: 20 },
                    { id:  3,  x:  53,  y: 63,  w: 27,  h: 20 },
                    { id:  4,  x:  83,  y: 38,  w: 27,  h: 20 },
                    { id:  5,  x:  83,  y: 63,  w: 27,  h: 20 },

                    { id:  6,  x: 123,  y: 38,  w: 27,  h: 20 },
                    { id:  7,  x: 123,  y: 63,  w: 27,  h: 20 },
                    { id:  8,  x: 153,  y: 38,  w: 27,  h: 20 },
                    { id:  9,  x: 153,  y: 63,  w: 27,  h: 20 },
                    { id: 10,  x: 183,  y: 38,  w: 27,  h: 20 },
                    { id: 11,  x: 183,  y: 63,  w: 27,  h: 20 },

                    { id: 12,  x: 223,  y: 38,  w: 27,  h: 20 },
                    { id: 13,  x: 223,  y: 63,  w: 27,  h: 20 },
                    { id: 14,  x: 253,  y: 38,  w: 27,  h: 20 },
                    { id: 15,  x: 253,  y: 63,  w: 27,  h: 20 },
                    { id: 16,  x: 283,  y: 38,  w: 27,  h: 20 },
                    { id: 17,  x: 283,  y: 63,  w: 27,  h: 20 },

                    { id: 18,  x: 323,  y: 38,  w: 27,  h: 20 },
                    { id: 19,  x: 323,  y: 63,  w: 27,  h: 20 },
                    { id: 20,  x: 353,  y: 38,  w: 27,  h: 20 },
                    { id: 21,  x: 353,  y: 63,  w: 27,  h: 20 },
                    { id: 22,  x: 383,  y: 38,  w: 27,  h: 20 },
                    { id: 23,  x: 383,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  36,  y: 45  },
                    { id:  1,  x:  36,  y: 108 },
                    { id:  2,  x:  66,  y: 45  },
                    { id:  3,  x:  66,  y: 108 },
                    { id:  4,  x:  96,  y: 45  },
                    { id:  5,  x:  96,  y: 108 },
                    { id:  6,  x: 136,  y: 45  },
                    { id:  7,  x: 136,  y: 108 },
                    { id:  8,  x: 166,  y: 45  },
                    { id:  9,  x: 166,  y: 108 },
                    { id: 10,  x: 196,  y: 45  },
                    { id: 11,  x: 196,  y: 108 },

                    { id: 12,  x: 236,  y: 45  },
                    { id: 13,  x: 236,  y: 108 },
                    { id: 14,  x: 266,  y: 45  },
                    { id: 15,  x: 266,  y: 108 },
                    { id: 16,  x: 296,  y: 45  },
                    { id: 17,  x: 296,  y: 108 },
                    { id: 18,  x: 336,  y: 45  },
                    { id: 19,  x: 336,  y: 108 },
                    { id: 20,  x: 366,  y: 45  },
                    { id: 21,  x: 366,  y: 108 },
                    { id: 22,  x: 396,  y: 45  },
                    { id: 23,  x: 396,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5100-48S_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5100-24Q_GS rear", anchor: "start" }
            }
        }
    },

    "QFX5110-48S(EL)_IRB": {
        "equipment_type": {
            "platform": "QFX5110-48S(EL)_IRB",
            "os": "JUNOS",
            "firmware": "17.4R1",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": false
                },
                "qos": {
                    "remark": false,
                    "remark_capability": ["packet_color"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                },
                "irb": {
                    "asymmetric": true,
                    "symmetric": false
                },
                "traffic": {
                    "same_vlan_number_traffic_total_value_flag": false,
                    "vlan_traffic_capability": "MIB",
                    "vlan_traffic_counter_name_mib_oid": "1.3.6.1.4.1.263 6.3.5.2.1.7",
                    "vlan_traffic_counter_value_mib_oid": "1.3.6.1.4.1.263 6.3.5.2.1.5",
                    "cli_exec_path": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5110",
                "config_template": "/var/lib/tftpboot/initial-config/juniper/qfx5110_ELeaf_IRB_0_7_ztp_init.conf.template",
                "initial_config": "/var/lib/tftpboot/initial-config/juniper/qfx5110_ELeaf_IRB_0_7_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/qfx5110_front.png" },
                "caption": { x: 5, y:20, title: "QFX5110-48S(EL)_IRB front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  1,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  2,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  3,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  4,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id:  5,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id:  6,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id:  7,  x: 132,  y: 63,  w: 20,  h: 20 },
                    { id:  8,  x: 155,  y: 38,  w: 20,  h: 20 },
                    { id:  9,  x: 155,  y: 63,  w: 20,  h: 20 },
                    { id: 10,  x: 178,  y: 38,  w: 20,  h: 20 },
                    { id: 11,  x: 178,  y: 63,  w: 20,  h: 20 },

                    { id: 12,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 13,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 14,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 15,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 16,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 17,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 18,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 19,  x: 273,  y: 63,  w: 20,  h: 20 },
                    { id: 20,  x: 296,  y: 38,  w: 20,  h: 20 },
                    { id: 21,  x: 296,  y: 63,  w: 20,  h: 20 },
                    { id: 22,  x: 319,  y: 38,  w: 20,  h: 20 },
                    { id: 23,  x: 319,  y: 63,  w: 20,  h: 20 },

                    { id: 24,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 25,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 26,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 27,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 28,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 29,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 30,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 31,  x: 414,  y: 63,  w: 20,  h: 20 },
                    { id: 32,  x: 437,  y: 38,  w: 20,  h: 20 },
                    { id: 33,  x: 437,  y: 63,  w: 20,  h: 20 },
                    { id: 34,  x: 460,  y: 38,  w: 20,  h: 20 },
                    { id: 35,  x: 460,  y: 63,  w: 20,  h: 20 },

                    { id: 36,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 37,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 38,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 39,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 40,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 41,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 42,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 43,  x: 555,  y: 63,  w: 20,  h: 20 },
                    { id: 44,  x: 578,  y: 38,  w: 20,  h: 20 },
                    { id: 45,  x: 578,  y: 63,  w: 20,  h: 20 },
                    { id: 46,  x: 601,  y: 38,  w: 20,  h: 20 },
                    { id: 47,  x: 601,  y: 63,  w: 20,  h: 20 },

                    { id: 48,  x: 627,  y: 38,  w: 30,  h: 20 },
                    { id: 49,  x: 627,  y: 63,  w: 30,  h: 20 },
                    { id: 50,  x: 660,  y: 38,  w: 30,  h: 20 },
                    { id: 51,  x: 660,  y: 63,  w: 30,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  73,  y: 45  },
                    { id:  1,  x:  73,  y: 108 },
                    { id:  2,  x:  96,  y: 45  },
                    { id:  3,  x:  96,  y: 108 },
                    { id:  4,  x: 119,  y: 45  },
                    { id:  5,  x: 119,  y: 108 },
                    { id:  6,  x: 142,  y: 45  },
                    { id:  7,  x: 142,  y: 108 },
                    { id:  8,  x: 165,  y: 45  },
                    { id:  9,  x: 165,  y: 108 },
                    { id: 10,  x: 188,  y: 45  },
                    { id: 11,  x: 188,  y: 108 },

                    { id: 12,  x: 214,  y: 45  },
                    { id: 13,  x: 214,  y: 108 },
                    { id: 14,  x: 237,  y: 45  },
                    { id: 15,  x: 237,  y: 108 },
                    { id: 16,  x: 260,  y: 45  },
                    { id: 17,  x: 260,  y: 108 },
                    { id: 18,  x: 283,  y: 45  },
                    { id: 19,  x: 283,  y: 108 },
                    { id: 20,  x: 306,  y: 45  },
                    { id: 21,  x: 306,  y: 108 },
                    { id: 22,  x: 329,  y: 45  },
                    { id: 23,  x: 329,  y: 108 },

                    { id: 24,  x: 355,  y: 45  },
                    { id: 25,  x: 355,  y: 108 },
                    { id: 26,  x: 378,  y: 45  },
                    { id: 27,  x: 378,  y: 108 },
                    { id: 28,  x: 401,  y: 45  },
                    { id: 29,  x: 401,  y: 108 },
                    { id: 30,  x: 424,  y: 45  },
                    { id: 31,  x: 424,  y: 108 },
                    { id: 32,  x: 447,  y: 45  },
                    { id: 33,  x: 447,  y: 108 },
                    { id: 34,  x: 470,  y: 45  },
                    { id: 35,  x: 470,  y: 108 },

                    { id: 36,  x: 496,  y: 45  },
                    { id: 37,  x: 496,  y: 108 },
                    { id: 38,  x: 519,  y: 45  },
                    { id: 39,  x: 519,  y: 108 },
                    { id: 40,  x: 542,  y: 45  },
                    { id: 41,  x: 542,  y: 108 },
                    { id: 42,  x: 565,  y: 45  },
                    { id: 43,  x: 565,  y: 108 },
                    { id: 44,  x: 588,  y: 45  },
                    { id: 45,  x: 588,  y: 108 },
                    { id: 46,  x: 611,  y: 45  },
                    { id: 47,  x: 611,  y: 108 },

                    { id: 48,  x: 642,  y: 45  },
                    { id: 49,  x: 642,  y: 108 },
                    { id: 50,  x: 675,  y: 45  },
                    { id: 51,  x: 675,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/qfx5110_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5110-48S(EL)_IRB rear", anchor: "start" }
            }
        }
    },

    "QFX5200-32C(GS)": {
        "equipment_type": {
            "platform": "QFX5200-32C(GS)",
            "os": "JUNOS",
            "firmware": "15.1X53-D30.5",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>:<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5200",
                "config_template": "/var/www/html/initial-config/juniper/qfx5200_Spine_0_8_ztp_init.conf.Line2-BlockY1.template",
                "initial_config": "/var/www/html/initial-config/juniper/qfx5200_Spine_0_8_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "40g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "xe-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": [ "40g", "100g" ]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5200-32C_front.png" },
                "caption": { x: 5, y:20, title: "QFX5200-32C(GS) front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 100,  y: 38,  w: 27,  h: 20 },
                    { id:  1,  x: 100,  y: 63,  w: 27,  h: 20 },
                    { id:  2,  x: 130,  y: 38,  w: 27,  h: 20 },
                    { id:  3,  x: 130,  y: 63,  w: 27,  h: 20 },
                    { id:  4,  x: 160,  y: 38,  w: 27,  h: 20 },
                    { id:  5,  x: 160,  y: 63,  w: 27,  h: 20 },
                    { id:  6,  x: 190,  y: 38,  w: 27,  h: 20 },
                    { id:  7,  x: 190,  y: 63,  w: 27,  h: 20 },

                    { id:  8,  x: 230,  y: 38,  w: 27,  h: 20 },
                    { id:  9,  x: 230,  y: 63,  w: 27,  h: 20 },
                    { id: 10,  x: 260,  y: 38,  w: 27,  h: 20 },
                    { id: 11,  x: 260,  y: 63,  w: 27,  h: 20 },
                    { id: 12,  x: 290,  y: 38,  w: 27,  h: 20 },
                    { id: 13,  x: 290,  y: 63,  w: 27,  h: 20 },
                    { id: 14,  x: 320,  y: 38,  w: 27,  h: 20 },
                    { id: 15,  x: 320,  y: 63,  w: 27,  h: 20 },

                    { id: 16,  x: 360,  y: 38,  w: 27,  h: 20 },
                    { id: 17,  x: 360,  y: 63,  w: 27,  h: 20 },
                    { id: 18,  x: 390,  y: 38,  w: 27,  h: 20 },
                    { id: 19,  x: 390,  y: 63,  w: 27,  h: 20 },
                    { id: 20,  x: 420,  y: 38,  w: 27,  h: 20 },
                    { id: 21,  x: 420,  y: 63,  w: 27,  h: 20 },
                    { id: 22,  x: 450,  y: 38,  w: 27,  h: 20 },
                    { id: 23,  x: 450,  y: 63,  w: 27,  h: 20 },

                    { id: 24,  x: 490,  y: 38,  w: 27,  h: 20 },
                    { id: 25,  x: 490,  y: 63,  w: 27,  h: 20 },
                    { id: 26,  x: 520,  y: 38,  w: 27,  h: 20 },
                    { id: 27,  x: 520,  y: 63,  w: 27,  h: 20 },
                    { id: 28,  x: 550,  y: 38,  w: 27,  h: 20 },
                    { id: 29,  x: 550,  y: 63,  w: 27,  h: 20 },
                    { id: 30,  x: 580,  y: 38,  w: 27,  h: 20 },
                    { id: 31,  x: 580,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 113,  y: 45  },
                    { id:  1,  x: 113,  y: 108 },
                    { id:  2,  x: 143,  y: 45  },
                    { id:  3,  x: 143,  y: 108 },
                    { id:  4,  x: 173,  y: 45  },
                    { id:  5,  x: 173,  y: 108 },
                    { id:  6,  x: 203,  y: 45  },
                    { id:  7,  x: 203,  y: 108 },

                    { id:  8,  x: 243,  y: 45  },
                    { id:  9,  x: 243,  y: 108 },
                    { id: 10,  x: 273,  y: 45  },
                    { id: 11,  x: 273,  y: 108 },
                    { id: 12,  x: 303,  y: 45  },
                    { id: 13,  x: 303,  y: 108 },
                    { id: 14,  x: 333,  y: 45  },
                    { id: 15,  x: 333,  y: 108 },

                    { id: 16,  x: 373,  y: 45  },
                    { id: 17,  x: 373,  y: 108 },
                    { id: 18,  x: 403,  y: 45  },
                    { id: 19,  x: 403,  y: 108 },
                    { id: 20,  x: 433,  y: 45  },
                    { id: 21,  x: 433,  y: 108 },
                    { id: 22,  x: 463,  y: 45  },
                    { id: 23,  x: 463,  y: 108 },

                    { id: 24,  x: 503,  y: 45  },
                    { id: 25,  x: 503,  y: 108 },
                    { id: 26,  x: 533,  y: 45  },
                    { id: 27,  x: 533,  y: 108 },
                    { id: 28,  x: 563,  y: 45  },
                    { id: 29,  x: 563,  y: 108 },
                    { id: 30,  x: 593,  y: 45  },
                    { id: 31,  x: 593,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5200-32C_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5200-32C(GS) rear", anchor: "start" }
            }
        }
    },

    "QFX5200-32C(IL)": {
        "equipment_type": {
            "platform": "QFX5200-32C(IL)",
            "os": "JUNOS",
            "firmware": "15.1X53-D30.5",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>:<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5200",
                "config_template": "/var/www/html/initial-config/juniper/qfx5200_ILeaf_0_8_ztp_init.conf.template",
                "initial_config": "/var/www/html/initial-config/juniper/qfx5200_ILeaf_0_8_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "40g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "xe-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": [ "40g", "100g" ]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5200-32C_front.png" },
                "caption": { x: 5, y:20, title: "QFX5200-32C(IL) front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 100,  y: 38,  w: 27,  h: 20 },
                    { id:  1,  x: 100,  y: 63,  w: 27,  h: 20 },
                    { id:  2,  x: 130,  y: 38,  w: 27,  h: 20 },
                    { id:  3,  x: 130,  y: 63,  w: 27,  h: 20 },
                    { id:  4,  x: 160,  y: 38,  w: 27,  h: 20 },
                    { id:  5,  x: 160,  y: 63,  w: 27,  h: 20 },
                    { id:  6,  x: 190,  y: 38,  w: 27,  h: 20 },
                    { id:  7,  x: 190,  y: 63,  w: 27,  h: 20 },

                    { id:  8,  x: 230,  y: 38,  w: 27,  h: 20 },
                    { id:  9,  x: 230,  y: 63,  w: 27,  h: 20 },
                    { id: 10,  x: 260,  y: 38,  w: 27,  h: 20 },
                    { id: 11,  x: 260,  y: 63,  w: 27,  h: 20 },
                    { id: 12,  x: 290,  y: 38,  w: 27,  h: 20 },
                    { id: 13,  x: 290,  y: 63,  w: 27,  h: 20 },
                    { id: 14,  x: 320,  y: 38,  w: 27,  h: 20 },
                    { id: 15,  x: 320,  y: 63,  w: 27,  h: 20 },

                    { id: 16,  x: 360,  y: 38,  w: 27,  h: 20 },
                    { id: 17,  x: 360,  y: 63,  w: 27,  h: 20 },
                    { id: 18,  x: 390,  y: 38,  w: 27,  h: 20 },
                    { id: 19,  x: 390,  y: 63,  w: 27,  h: 20 },
                    { id: 20,  x: 420,  y: 38,  w: 27,  h: 20 },
                    { id: 21,  x: 420,  y: 63,  w: 27,  h: 20 },
                    { id: 22,  x: 450,  y: 38,  w: 27,  h: 20 },
                    { id: 23,  x: 450,  y: 63,  w: 27,  h: 20 },

                    { id: 24,  x: 490,  y: 38,  w: 27,  h: 20 },
                    { id: 25,  x: 490,  y: 63,  w: 27,  h: 20 },
                    { id: 26,  x: 520,  y: 38,  w: 27,  h: 20 },
                    { id: 27,  x: 520,  y: 63,  w: 27,  h: 20 },
                    { id: 28,  x: 550,  y: 38,  w: 27,  h: 20 },
                    { id: 29,  x: 550,  y: 63,  w: 27,  h: 20 },
                    { id: 30,  x: 580,  y: 38,  w: 27,  h: 20 },
                    { id: 31,  x: 580,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 113,  y: 45  },
                    { id:  1,  x: 113,  y: 108 },
                    { id:  2,  x: 143,  y: 45  },
                    { id:  3,  x: 143,  y: 108 },
                    { id:  4,  x: 173,  y: 45  },
                    { id:  5,  x: 173,  y: 108 },
                    { id:  6,  x: 203,  y: 45  },
                    { id:  7,  x: 203,  y: 108 },

                    { id:  8,  x: 243,  y: 45  },
                    { id:  9,  x: 243,  y: 108 },
                    { id: 10,  x: 273,  y: 45  },
                    { id: 11,  x: 273,  y: 108 },
                    { id: 12,  x: 303,  y: 45  },
                    { id: 13,  x: 303,  y: 108 },
                    { id: 14,  x: 333,  y: 45  },
                    { id: 15,  x: 333,  y: 108 },

                    { id: 16,  x: 373,  y: 45  },
                    { id: 17,  x: 373,  y: 108 },
                    { id: 18,  x: 403,  y: 45  },
                    { id: 19,  x: 403,  y: 108 },
                    { id: 20,  x: 433,  y: 45  },
                    { id: 21,  x: 433,  y: 108 },
                    { id: 22,  x: 463,  y: 45  },
                    { id: 23,  x: 463,  y: 108 },

                    { id: 24,  x: 503,  y: 45  },
                    { id: 25,  x: 503,  y: 108 },
                    { id: 26,  x: 533,  y: 45  },
                    { id: 27,  x: 533,  y: 108 },
                    { id: 28,  x: 563,  y: 45  },
                    { id: 29,  x: 563,  y: 108 },
                    { id: 30,  x: 593,  y: 45  },
                    { id: 31,  x: 593,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5200-32C_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5200-32C(IL) rear", anchor: "start" }
            }
        }
    },

    "QFX5200-32C(BL)": {
        "equipment_type": {
            "platform": "QFX5200-32C(BL)",
            "os": "JUNOS",
            "firmware": "15.1X53-D30.5",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>:<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"],
                    "remark_default": "packet_color",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5200",
                "config_template": "/var/www/html/initial-config/juniper/qfx5200_BLeaf_0_8_ztp_init.conf.template",
                "initial_config": "/var/www/html/initial-config/juniper/qfx5200_BLeaf_0_8_ztp_init.conf"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "40g",
                        "port_prefix": "et-"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "xe-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": [ "40g", "100g" ]
                }, {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": [ "40g", "100g" ]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/QFX5200-32C_front.png" },
                "caption": { x: 5, y:20, title: "QFX5200-32C(BL) front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 100,  y: 38,  w: 27,  h: 20 },
                    { id:  1,  x: 100,  y: 63,  w: 27,  h: 20 },
                    { id:  2,  x: 130,  y: 38,  w: 27,  h: 20 },
                    { id:  3,  x: 130,  y: 63,  w: 27,  h: 20 },
                    { id:  4,  x: 160,  y: 38,  w: 27,  h: 20 },
                    { id:  5,  x: 160,  y: 63,  w: 27,  h: 20 },
                    { id:  6,  x: 190,  y: 38,  w: 27,  h: 20 },
                    { id:  7,  x: 190,  y: 63,  w: 27,  h: 20 },

                    { id:  8,  x: 230,  y: 38,  w: 27,  h: 20 },
                    { id:  9,  x: 230,  y: 63,  w: 27,  h: 20 },
                    { id: 10,  x: 260,  y: 38,  w: 27,  h: 20 },
                    { id: 11,  x: 260,  y: 63,  w: 27,  h: 20 },
                    { id: 12,  x: 290,  y: 38,  w: 27,  h: 20 },
                    { id: 13,  x: 290,  y: 63,  w: 27,  h: 20 },
                    { id: 14,  x: 320,  y: 38,  w: 27,  h: 20 },
                    { id: 15,  x: 320,  y: 63,  w: 27,  h: 20 },

                    { id: 16,  x: 360,  y: 38,  w: 27,  h: 20 },
                    { id: 17,  x: 360,  y: 63,  w: 27,  h: 20 },
                    { id: 18,  x: 390,  y: 38,  w: 27,  h: 20 },
                    { id: 19,  x: 390,  y: 63,  w: 27,  h: 20 },
                    { id: 20,  x: 420,  y: 38,  w: 27,  h: 20 },
                    { id: 21,  x: 420,  y: 63,  w: 27,  h: 20 },
                    { id: 22,  x: 450,  y: 38,  w: 27,  h: 20 },
                    { id: 23,  x: 450,  y: 63,  w: 27,  h: 20 },

                    { id: 24,  x: 490,  y: 38,  w: 27,  h: 20 },
                    { id: 25,  x: 490,  y: 63,  w: 27,  h: 20 },
                    { id: 26,  x: 520,  y: 38,  w: 27,  h: 20 },
                    { id: 27,  x: 520,  y: 63,  w: 27,  h: 20 },
                    { id: 28,  x: 550,  y: 38,  w: 27,  h: 20 },
                    { id: 29,  x: 550,  y: 63,  w: 27,  h: 20 },
                    { id: 30,  x: 580,  y: 38,  w: 27,  h: 20 },
                    { id: 31,  x: 580,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 113,  y: 45  },
                    { id:  1,  x: 113,  y: 108 },
                    { id:  2,  x: 143,  y: 45  },
                    { id:  3,  x: 143,  y: 108 },
                    { id:  4,  x: 173,  y: 45  },
                    { id:  5,  x: 173,  y: 108 },
                    { id:  6,  x: 203,  y: 45  },
                    { id:  7,  x: 203,  y: 108 },

                    { id:  8,  x: 243,  y: 45  },
                    { id:  9,  x: 243,  y: 108 },
                    { id: 10,  x: 273,  y: 45  },
                    { id: 11,  x: 273,  y: 108 },
                    { id: 12,  x: 303,  y: 45  },
                    { id: 13,  x: 303,  y: 108 },
                    { id: 14,  x: 333,  y: 45  },
                    { id: 15,  x: 333,  y: 108 },

                    { id: 16,  x: 373,  y: 45  },
                    { id: 17,  x: 373,  y: 108 },
                    { id: 18,  x: 403,  y: 45  },
                    { id: 19,  x: 403,  y: 108 },
                    { id: 20,  x: 433,  y: 45  },
                    { id: 21,  x: 433,  y: 108 },
                    { id: 22,  x: 463,  y: 45  },
                    { id: 23,  x: 463,  y: 108 },

                    { id: 24,  x: 503,  y: 45  },
                    { id: 25,  x: 503,  y: 108 },
                    { id: 26,  x: 533,  y: 45  },
                    { id: 27,  x: 533,  y: 108 },
                    { id: 28,  x: 563,  y: 45  },
                    { id: 29,  x: 563,  y: 108 },
                    { id: 30,  x: 593,  y: 45  },
                    { id: 31,  x: 593,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/QFX5200-32C_rear.png" },
                "caption": { x: 5, y:150, title: "QFX5200-32C(BL) rear", anchor: "start" }
            }
        }
    },

    "AS5812(GS)": {
        "equipment_type": {
            "platform": "AS5812(GS)",
            "os": "Beluganos",
            "firmware": "0.2.0",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "-",
                "config_template": "-",
                "initial_config": "-"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.4.99999.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.4.99999.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "-",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "10g",
                       "port_prefix": "eth"
                    }
                ],
                "lag_prefix": "bond",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["1g", "10g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/AS5812_front.png" },
                "caption": { x: 5, y:20, title: "AS5812(GS) front", anchor: "start" },
                "slot": [
                    { id:  1, x:  17, y: 38, w: 20, h: 20 },
                    { id:  2, x:  17, y: 63, w: 20, h: 20 },
                    { id:  3, x:  42, y: 38, w: 20, h: 20 },
                    { id:  4, x:  42, y: 63, w: 20, h: 20 },
                    { id:  5, x:  67, y: 38, w: 20, h: 20 },
                    { id:  6, x:  67, y: 63, w: 20, h: 20 },
                    { id:  7, x:  92, y: 38, w: 20, h: 20 },
                    { id:  8, x:  92, y: 63, w: 20, h: 20 },
                    { id:  9, x: 117, y: 38, w: 20, h: 20 },
                    { id: 10, x: 117, y: 63, w: 20, h: 20 },
                    { id: 11, x: 142, y: 38, w: 20, h: 20 },
                    { id: 12, x: 142, y: 63, w: 20, h: 20 },
                    { id: 13, x: 167, y: 38, w: 20, h: 20 },
                    { id: 14, x: 167, y: 63, w: 20, h: 20 },
                    { id: 15, x: 192, y: 38, w: 20, h: 20 },
                    { id: 16, x: 192, y: 63, w: 20, h: 20 },

                    { id: 17, x: 222, y: 38, w: 20, h: 20 },
                    { id: 18, x: 222, y: 63, w: 20, h: 20 },
                    { id: 19, x: 247, y: 38, w: 20, h: 20 },
                    { id: 20, x: 247, y: 63, w: 20, h: 20 },
                    { id: 21, x: 272, y: 38, w: 20, h: 20 },
                    { id: 22, x: 272, y: 63, w: 20, h: 20 },
                    { id: 23, x: 297, y: 38, w: 20, h: 20 },
                    { id: 24, x: 297, y: 63, w: 20, h: 20 },
                    { id: 25, x: 322, y: 38, w: 20, h: 20 },
                    { id: 26, x: 322, y: 63, w: 20, h: 20 },
                    { id: 27, x: 347, y: 38, w: 20, h: 20 },
                    { id: 28, x: 347, y: 63, w: 20, h: 20 },
                    { id: 29, x: 372, y: 38, w: 20, h: 20 },
                    { id: 30, x: 372, y: 63, w: 20, h: 20 },
                    { id: 31, x: 397, y: 38, w: 20, h: 20 },
                    { id: 32, x: 397, y: 63, w: 20, h: 20 },

                    { id: 33, x: 427, y: 38, w: 20, h: 20 },
                    { id: 34, x: 427, y: 63, w: 20, h: 20 },
                    { id: 35, x: 452, y: 38, w: 20, h: 20 },
                    { id: 36, x: 452, y: 63, w: 20, h: 20 },
                    { id: 37, x: 477, y: 38, w: 20, h: 20 },
                    { id: 38, x: 477, y: 63, w: 20, h: 20 },
                    { id: 39, x: 502, y: 38, w: 20, h: 20 },
                    { id: 40, x: 502, y: 63, w: 20, h: 20 },
                    { id: 41, x: 527, y: 38, w: 20, h: 20 },
                    { id: 42, x: 527, y: 63, w: 20, h: 20 },
                    { id: 43, x: 552, y: 38, w: 20, h: 20 },
                    { id: 44, x: 552, y: 63, w: 20, h: 20 },
                    { id: 45, x: 577, y: 38, w: 20, h: 20 },
                    { id: 46, x: 577, y: 63, w: 20, h: 20 },
                    { id: 47, x: 602, y: 38, w: 20, h: 20 },
                    { id: 48, x: 602, y: 63, w: 20, h: 20 }
                ],
                "label": [
                    { id:  1, x:  27, y:  45 },
                    { id:  2, x:  27, y: 108 },
                    { id:  3, x:  52, y:  45 },
                    { id:  4, x:  52, y: 108 },
                    { id:  5, x:  77, y:  45 },
                    { id:  6, x:  77, y: 108 },
                    { id:  7, x: 102, y:  45 },
                    { id:  8, x: 102, y: 108 },
                    { id:  9, x: 127, y:  45 },
                    { id: 10, x: 127, y: 108 },
                    { id: 11, x: 152, y:  45 },
                    { id: 12, x: 152, y: 108 },
                    { id: 13, x: 177, y:  45 },
                    { id: 14, x: 177, y: 108 },
                    { id: 15, x: 202, y:  45 },
                    { id: 16, x: 202, y: 108 },

                    { id: 17, x: 232, y:  45 },
                    { id: 18, x: 232, y: 108 },
                    { id: 19, x: 257, y:  45 },
                    { id: 20, x: 257, y: 108 },
                    { id: 21, x: 282, y:  45 },
                    { id: 22, x: 282, y: 108 },
                    { id: 23, x: 307, y:  45 },
                    { id: 24, x: 307, y: 108 },
                    { id: 25, x: 332, y:  45 },
                    { id: 26, x: 332, y: 108 },
                    { id: 27, x: 357, y:  45 },
                    { id: 28, x: 357, y: 108 },
                    { id: 29, x: 382, y:  45 },
                    { id: 30, x: 382, y: 108 },
                    { id: 31, x: 407, y:  45 },
                    { id: 32, x: 407, y: 108 },

                    { id: 33, x: 437, y:  45 },
                    { id: 34, x: 437, y: 108 },
                    { id: 35, x: 462, y:  45 },
                    { id: 36, x: 462, y: 108 },
                    { id: 37, x: 487, y:  45 },
                    { id: 38, x: 487, y: 108 },
                    { id: 39, x: 512, y:  45 },
                    { id: 40, x: 512, y: 108 },
                    { id: 41, x: 537, y:  45 },
                    { id: 42, x: 537, y: 108 },
                    { id: 43, x: 562, y:  45 },
                    { id: 44, x: 562, y: 108 },
                    { id: 45, x: 587, y:  45 },
                    { id: 46, x: 587, y: 108 },
                    { id: 47, x: 612, y:  45 },
                    { id: 48, x: 612, y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/AS5812_rear.png" },
                "caption": { x: 5, y:150, title: "AS5812(GS) rear", anchor: "start" }
            }
        }
    },

    "AS5812(EL)": {
        "equipment_type": {
            "platform": "AS5812(EL)",
            "os": "OcNOS",
            "firmware": "1.3.0V.158a",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": false
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "-",
                "config_template": "-",
                "initial_config": "-"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "-",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "10g",
                       "port_prefix": "xe"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "xe"
                    }
                ],
                "lag_prefix": "po",
                "unit_connector": "-"
            },
            "slots": [
                {
                    "if_id": "1",
                    "if_slot": "1",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "2",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "3",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "4",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "5",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "6",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "7",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "8",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "9",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "10",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "11",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "12",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "13",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "14",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "15",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "16",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "17",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "18",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "19",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "20",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "21",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "22",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "23",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "24",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "25",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "26",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "27",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "28",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "29",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "30",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "31",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "32",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "33",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "34",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "35",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "36",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "37",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "38",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "39",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "40",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "41",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "42",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "43",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "44",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "45",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "46",
                    "if_slot": " 46",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "47",
                    "speed_capabilities": ["10g", "1g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "48",
                    "speed_capabilities": ["10g", "1g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/AS5812_front.png" },
                "caption": { x: 5, y:20, title: "AS5812_EL front", anchor: "start" },
                "slot": [
                    { id:  1,  x:  17,  y: 38,  w: 20,  h: 20 },
                    { id:  2,  x:  17,  y: 63,  w: 20,  h: 20 },
                    { id:  3,  x:  40,  y: 38,  w: 20,  h: 20 },
                    { id:  4,  x:  40,  y: 63,  w: 20,  h: 20 },
                    { id:  5,  x:  63,  y: 38,  w: 20,  h: 20 },
                    { id:  6,  x:  63,  y: 63,  w: 20,  h: 20 },
                    { id:  7,  x:  86,  y: 38,  w: 20,  h: 20 },
                    { id:  8,  x:  86,  y: 63,  w: 20,  h: 20 },
                    { id:  9,  x: 109,  y: 38,  w: 20,  h: 20 },
                    { id: 10,  x: 109,  y: 63,  w: 20,  h: 20 },
                    { id: 11,  x: 132,  y: 38,  w: 20,  h: 20 },
                    { id: 12,  x: 132,  y: 63,  w: 20,  h: 20 },

                    { id: 13,  x: 158,  y: 38,  w: 20,  h: 20 },
                    { id: 14,  x: 158,  y: 63,  w: 20,  h: 20 },
                    { id: 15,  x: 181,  y: 38,  w: 20,  h: 20 },
                    { id: 16,  x: 181,  y: 63,  w: 20,  h: 20 },
                    { id: 17,  x: 204,  y: 38,  w: 20,  h: 20 },
                    { id: 18,  x: 204,  y: 63,  w: 20,  h: 20 },
                    { id: 19,  x: 227,  y: 38,  w: 20,  h: 20 },
                    { id: 20,  x: 227,  y: 63,  w: 20,  h: 20 },
                    { id: 21,  x: 250,  y: 38,  w: 20,  h: 20 },
                    { id: 22,  x: 250,  y: 63,  w: 20,  h: 20 },
                    { id: 23,  x: 273,  y: 38,  w: 20,  h: 20 },
                    { id: 24,  x: 273,  y: 63,  w: 20,  h: 20 },

                    { id: 25,  x: 299,  y: 38,  w: 20,  h: 20 },
                    { id: 26,  x: 299,  y: 63,  w: 20,  h: 20 },
                    { id: 27,  x: 322,  y: 38,  w: 20,  h: 20 },
                    { id: 28,  x: 322,  y: 63,  w: 20,  h: 20 },
                    { id: 29,  x: 345,  y: 38,  w: 20,  h: 20 },
                    { id: 30,  x: 345,  y: 63,  w: 20,  h: 20 },
                    { id: 31,  x: 368,  y: 38,  w: 20,  h: 20 },
                    { id: 32,  x: 368,  y: 63,  w: 20,  h: 20 },
                    { id: 33,  x: 391,  y: 38,  w: 20,  h: 20 },
                    { id: 34,  x: 391,  y: 63,  w: 20,  h: 20 },
                    { id: 35,  x: 414,  y: 38,  w: 20,  h: 20 },
                    { id: 36,  x: 414,  y: 63,  w: 20,  h: 20 },

                    { id: 37,  x: 440,  y: 38,  w: 20,  h: 20 },
                    { id: 38,  x: 440,  y: 63,  w: 20,  h: 20 },
                    { id: 39,  x: 463,  y: 38,  w: 20,  h: 20 },
                    { id: 40,  x: 463,  y: 63,  w: 20,  h: 20 },
                    { id: 41,  x: 486,  y: 38,  w: 20,  h: 20 },
                    { id: 42,  x: 486,  y: 63,  w: 20,  h: 20 },
                    { id: 43,  x: 509,  y: 38,  w: 20,  h: 20 },
                    { id: 44,  x: 509,  y: 63,  w: 20,  h: 20 },
                    { id: 45,  x: 532,  y: 38,  w: 20,  h: 20 },
                    { id: 46,  x: 532,  y: 63,  w: 20,  h: 20 },
                    { id: 47,  x: 555,  y: 38,  w: 20,  h: 20 },
                    { id: 48,  x: 555,  y: 63,  w: 20,  h: 20 }
                ],
                "label": [
                    { id:  1,  x:  28,  y: 45  },
                    { id:  2,  x:  28,  y: 108 },
                    { id:  3,  x:  51,  y: 45  },
                    { id:  4,  x:  51,  y: 108 },
                    { id:  5,  x:  74,  y: 45  },
                    { id:  6,  x:  74,  y: 108 },
                    { id:  7,  x:  97,  y: 45  },
                    { id:  8,  x:  97,  y: 108 },
                    { id:  9,  x: 120,  y: 45  },
                    { id: 10,  x: 120,  y: 108 },
                    { id: 11,  x: 143,  y: 45  },
                    { id: 12,  x: 143,  y: 108 },

                    { id: 13,  x: 169,  y: 45  },
                    { id: 14,  x: 169,  y: 108 },
                    { id: 15,  x: 192,  y: 45  },
                    { id: 16,  x: 192,  y: 108 },
                    { id: 17,  x: 215,  y: 45  },
                    { id: 18,  x: 215,  y: 108 },
                    { id: 19,  x: 238,  y: 45  },
                    { id: 20,  x: 238,  y: 108 },
                    { id: 21,  x: 261,  y: 45  },
                    { id: 22,  x: 261,  y: 108 },
                    { id: 23,  x: 284,  y: 45  },
                    { id: 24,  x: 284,  y: 108 },

                    { id: 25,  x: 310,  y: 45  },
                    { id: 26,  x: 310,  y: 108 },
                    { id: 27,  x: 333,  y: 45  },
                    { id: 28,  x: 333,  y: 108 },
                    { id: 29,  x: 356,  y: 45  },
                    { id: 30,  x: 356,  y: 108 },
                    { id: 31,  x: 379,  y: 45  },
                    { id: 32,  x: 379,  y: 108 },
                    { id: 33,  x: 402,  y: 45  },
                    { id: 34,  x: 402,  y: 108 },
                    { id: 35,  x: 425,  y: 45  },
                    { id: 36,  x: 425,  y: 108 },

                    { id: 37,  x: 451,  y: 45  },
                    { id: 38,  x: 451,  y: 108 },
                    { id: 39,  x: 474,  y: 45  },
                    { id: 40,  x: 474,  y: 108 },
                    { id: 41,  x: 497,  y: 45  },
                    { id: 42,  x: 497,  y: 108 },
                    { id: 43,  x: 520,  y: 45  },
                    { id: 44,  x: 520,  y: 108 },
                    { id: 45,  x: 543,  y: 45  },
                    { id: 46,  x: 543,  y: 108 },
                    { id: 47,  x: 566,  y: 45  },
                    { id: 48,  x: 566,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/AS5812_rear.png" },
                "caption": { x: 5, y:150, title: "AS5812_EL rear", anchor: "start" }
            }
        }
    },

    "AS5812(EL)_Cumulus": {
        "equipment_type": {
            "platform": "AS5812(EL)_Cumulus",
            "os": "Cumulus",
            "firmware": "3.6.0",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": false
                },
                "qos": {
                    "remark": true,
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"],
                    "remark_default": "af1",
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                },
                "irb": {
                    "asymmetric": false,
                    "symmetric": true
                },
                "traffic": {
                    "same_vlan_number_traffic_total_value_flag": true,
                    "vlan_traffic_capability": "CLI",
                    "vlan_traffic_counter_name_mib_oid": null,
                    "vlan_traffic_counter_value_mib_oid": null,
                    "cli_exec_path": "/usr/ec_main/bin/CumulusVlanTraffic.sh"
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.cumulus",
                "config_template": "/var/lib/tftpboot/cumulus/EL3/config/config_template:/var/lib/tftpboot/cumulus/ztp_sh_template",
                "initial_config": "/var/lib/tftpboot/cumulus/EL3/config:/var/lib/tftpboot/cumulus"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "ZTP DHCP: Script returned success",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "et-"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "xe-"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/40",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/41",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/42",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/43",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "44",
                    "if_slot": "0/0/44",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "45",
                    "if_slot": "0/0/45",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "46",
                    "if_slot": "0/0/46",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "47",
                    "if_slot": "0/0/47",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "48",
                    "if_slot": "0/0/48",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "49",
                    "if_slot": "0/0/49",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "50",
                    "if_slot": "0/0/50",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "51",
                    "if_slot": "0/0/51",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "52",
                    "if_slot": "0/0/52",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "53",
                    "if_slot": "0/0/53",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "54",
                    "if_slot": "0/0/54",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/AS5812_front.png" },
                "caption": { x: 5, y:20, title: "AS5812(EL)_Cumulus front", anchor: "start" },
                "slot": [
                    { id:  1, x:  17, y: 38, w: 20, h: 20 },
                    { id:  2, x:  17, y: 61, w: 20, h: 20 },
                    { id:  3, x:  40, y: 38, w: 20, h: 20 },
                    { id:  4, x:  40, y: 61, w: 20, h: 20 },
                    { id:  5, x:  63, y: 38, w: 20, h: 20 },
                    { id:  6, x:  63, y: 61, w: 20, h: 20 },
                    { id:  7, x:  86, y: 38, w: 20, h: 20 },
                    { id:  8, x:  86, y: 61, w: 20, h: 20 },
                    { id:  9, x: 109, y: 38, w: 20, h: 20 },
                    { id: 10, x: 109, y: 61, w: 20, h: 20 },
                    { id: 11, x: 132, y: 38, w: 20, h: 20 },
                    { id: 12, x: 132, y: 61, w: 20, h: 20 },
                    { id: 13, x: 155, y: 38, w: 20, h: 20 },
                    { id: 14, x: 155, y: 61, w: 20, h: 20 },
                    { id: 15, x: 178, y: 38, w: 20, h: 20 },
                    { id: 16, x: 178, y: 61, w: 20, h: 20 },

                    { id: 17, x: 206, y: 38, w: 20, h: 20 },
                    { id: 18, x: 206, y: 61, w: 20, h: 20 },
                    { id: 19, x: 229, y: 38, w: 20, h: 20 },
                    { id: 20, x: 229, y: 61, w: 20, h: 20 },
                    { id: 21, x: 252, y: 38, w: 20, h: 20 },
                    { id: 22, x: 252, y: 61, w: 20, h: 20 },
                    { id: 23, x: 275, y: 38, w: 20, h: 20 },
                    { id: 24, x: 275, y: 61, w: 20, h: 20 },
                    { id: 25, x: 298, y: 38, w: 20, h: 20 },
                    { id: 26, x: 298, y: 61, w: 20, h: 20 },
                    { id: 27, x: 321, y: 38, w: 20, h: 20 },
                    { id: 28, x: 321, y: 61, w: 20, h: 20 },
                    { id: 29, x: 344, y: 38, w: 20, h: 20 },
                    { id: 30, x: 344, y: 61, w: 20, h: 20 },
                    { id: 31, x: 367, y: 38, w: 20, h: 20 },
                    { id: 32, x: 367, y: 61, w: 20, h: 20 },

                    { id: 33, x: 395, y: 38, w: 20, h: 20 },
                    { id: 34, x: 395, y: 61, w: 20, h: 20 },
                    { id: 35, x: 418, y: 38, w: 20, h: 20 },
                    { id: 36, x: 418, y: 61, w: 20, h: 20 },
                    { id: 37, x: 441, y: 38, w: 20, h: 20 },
                    { id: 38, x: 441, y: 61, w: 20, h: 20 },
                    { id: 39, x: 464, y: 38, w: 20, h: 20 },
                    { id: 40, x: 464, y: 61, w: 20, h: 20 },
                    { id: 41, x: 487, y: 38, w: 20, h: 20 },
                    { id: 42, x: 487, y: 61, w: 20, h: 20 },
                    { id: 43, x: 510, y: 38, w: 20, h: 20 },
                    { id: 44, x: 510, y: 61, w: 20, h: 20 },
                    { id: 45, x: 533, y: 38, w: 20, h: 20 },
                    { id: 46, x: 533, y: 61, w: 20, h: 20 },
                    { id: 47, x: 556, y: 38, w: 20, h: 20 },
                    { id: 48, x: 556, y: 61, w: 20, h: 20 },

                    { id: 49, x: 584, y: 32, w: 28, h: 17 },
                    { id: 50, x: 584, y: 52, w: 28, h: 17 },
                    { id: 51, x: 615, y: 32, w: 28, h: 17 },
                    { id: 52, x: 615, y: 52, w: 28, h: 17 },
                    { id: 53, x: 584, y: 82, w: 28, h: 17 },
                    { id: 54, x: 615, y: 82, w: 28, h: 17 }
                ],
                "label": [
                    { id:  1, x:  27, y:  45 },
                    { id:  2, x:  27, y: 108 },
                    { id:  3, x:  50, y:  45 },
                    { id:  4, x:  50, y: 108 },
                    { id:  5, x:  73, y:  45 },
                    { id:  6, x:  73, y: 108 },
                    { id:  7, x:  96, y:  45 },
                    { id:  8, x:  96, y: 108 },
                    { id:  9, x: 119, y:  45 },
                    { id: 10, x: 119, y: 108 },
                    { id: 11, x: 142, y:  45 },
                    { id: 12, x: 142, y: 108 },
                    { id: 13, x: 165, y:  45 },
                    { id: 14, x: 165, y: 108 },
                    { id: 15, x: 188, y:  45 },
                    { id: 16, x: 188, y: 108 },

                    { id: 17, x: 216, y:  45 },
                    { id: 18, x: 216, y: 108 },
                    { id: 19, x: 239, y:  45 },
                    { id: 20, x: 239, y: 108 },
                    { id: 21, x: 262, y:  45 },
                    { id: 22, x: 262, y: 108 },
                    { id: 23, x: 285, y:  45 },
                    { id: 24, x: 285, y: 108 },
                    { id: 25, x: 308, y:  45 },
                    { id: 26, x: 308, y: 108 },
                    { id: 27, x: 331, y:  45 },
                    { id: 28, x: 331, y: 108 },
                    { id: 29, x: 354, y:  45 },
                    { id: 30, x: 354, y: 108 },
                    { id: 31, x: 377, y:  45 },
                    { id: 32, x: 377, y: 108 },

                    { id: 33, x: 405, y:  45 },
                    { id: 34, x: 405, y: 108 },
                    { id: 35, x: 428, y:  45 },
                    { id: 36, x: 428, y: 108 },
                    { id: 37, x: 451, y:  45 },
                    { id: 38, x: 451, y: 108 },
                    { id: 39, x: 474, y:  45 },
                    { id: 40, x: 474, y: 108 },
                    { id: 41, x: 497, y:  45 },
                    { id: 42, x: 497, y: 108 },
                    { id: 43, x: 520, y:  45 },
                    { id: 44, x: 520, y: 108 },
                    { id: 45, x: 543, y:  45 },
                    { id: 46, x: 543, y: 108 },
                    { id: 47, x: 566, y:  45 },
                    { id: 48, x: 566, y: 108 },

                    { id: 49, x: 598, y:  43 },
                    { id: 50, x: 591, y:  90 },
                    { id: 51, x: 629, y:  43 },
                    { id: 52, x: 622, y:  90 },
                    { id: 53, x: 605, y:  94 },
                    { id: 54, x: 636, y:  94 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/AS5812_rear.png" },
                "caption": { x: 5, y:150, title: "AS5812(EL)_Cumulus rear", anchor: "start" }
            }
        }
    },

    "AS7712(GS)": {
        "equipment_type": {
            "platform": "AS7712(GS)",
            "os": "OcNOS",
            "firmware": "1.3.1.131",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "-",
                "config_template": "-",
                "initial_config": "-"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "-",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "ce"
                    }
                ],
                "lag_prefix": "po",
                "unit_connector": "-"
            },
            "slots": [
                {
                    "if_id": "1",
                    "if_slot": "1",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "2",
                    "if_slot": "2",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "3",
                    "if_slot": "3",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "4",
                    "if_slot": "4",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "5",
                    "if_slot": "5",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "6",
                    "if_slot": "6",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "7",
                    "if_slot": "7",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "8",
                    "if_slot": "8",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "9",
                    "if_slot": "9",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "10",
                    "if_slot": "10",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "11",
                    "if_slot": "11",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "12",
                    "if_slot": "12",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "13",
                    "if_slot": "13",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "14",
                    "if_slot": "14",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "15",
                    "if_slot": "15",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "16",
                    "if_slot": "16",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "17",
                    "if_slot": "17",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "18",
                    "if_slot": "18",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "19",
                    "if_slot": "19",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "20",
                    "if_slot": "20",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "21",
                    "if_slot": "21",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "22",
                    "if_slot": "22",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "23",
                    "if_slot": "23",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "24",
                    "if_slot": "24",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "25",
                    "if_slot": "25",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "26",
                    "if_slot": "26",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "27",
                    "if_slot": "27",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "28",
                    "if_slot": "28",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "29",
                    "if_slot": "29",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "30",
                    "if_slot": "30",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "31",
                    "if_slot": "31",
                    "speed_capabilities": ["10g"]
                }, {
                    "if_id": "32",
                    "if_slot": "32",
                    "speed_capabilities": ["10g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/AS7712_front.png" },
                "caption": { x: 5, y:20, title: "AS7712_GS front", anchor: "start" },
                "slot": [
                    { id:  1,  x: 70,  y: 38,  w: 27,  h: 20 },
                    { id:  2,  x: 70,  y: 63,  w: 27,  h: 20 },
                    { id:  3,  x: 100,  y: 38,  w: 27,  h: 20 },
                    { id:  4,  x: 100,  y: 63,  w: 27,  h: 20 },
                    { id:  5,  x: 130,  y: 38,  w: 27,  h: 20 },
                    { id:  6,  x: 130,  y: 63,  w: 27,  h: 20 },
                    { id:  7,  x: 160,  y: 38,  w: 27,  h: 20 },
                    { id:  8,  x: 160,  y: 63,  w: 27,  h: 20 },

                    { id:  9,  x: 200,  y: 38,  w: 27,  h: 20 },
                    { id: 10,  x: 200,  y: 63,  w: 27,  h: 20 },
                    { id: 11,  x: 230,  y: 38,  w: 27,  h: 20 },
                    { id: 12,  x: 230,  y: 63,  w: 27,  h: 20 },
                    { id: 13,  x: 260,  y: 38,  w: 27,  h: 20 },
                    { id: 14,  x: 260,  y: 63,  w: 27,  h: 20 },
                    { id: 15,  x: 290,  y: 38,  w: 27,  h: 20 },
                    { id: 16,  x: 290,  y: 63,  w: 27,  h: 20 },

                    { id: 17,  x: 330,  y: 38,  w: 27,  h: 20 },
                    { id: 18,  x: 330,  y: 63,  w: 27,  h: 20 },
                    { id: 19,  x: 360,  y: 38,  w: 27,  h: 20 },
                    { id: 20,  x: 360,  y: 63,  w: 27,  h: 20 },
                    { id: 21,  x: 390,  y: 38,  w: 27,  h: 20 },
                    { id: 22,  x: 390,  y: 63,  w: 27,  h: 20 },
                    { id: 23,  x: 420,  y: 38,  w: 27,  h: 20 },
                    { id: 24,  x: 420,  y: 63,  w: 27,  h: 20 },

                    { id: 25,  x: 460,  y: 38,  w: 27,  h: 20 },
                    { id: 26,  x: 460,  y: 63,  w: 27,  h: 20 },
                    { id: 27,  x: 490,  y: 38,  w: 27,  h: 20 },
                    { id: 28,  x: 490,  y: 63,  w: 27,  h: 20 },
                    { id: 29,  x: 520,  y: 38,  w: 27,  h: 20 },
                    { id: 30,  x: 520,  y: 63,  w: 27,  h: 20 },
                    { id: 31,  x: 550,  y: 38,  w: 27,  h: 20 },
                    { id: 32,  x: 550,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  1,  x: 83,  y: 45  },
                    { id:  2,  x: 83,  y: 108 },
                    { id:  3,  x: 113,  y: 45  },
                    { id:  4,  x: 113,  y: 108 },
                    { id:  5,  x: 143,  y: 45  },
                    { id:  6,  x: 143,  y: 108 },
                    { id:  7,  x: 173,  y: 45  },
                    { id:  8,  x: 173,  y: 108 },

                    { id:  9,  x: 213,  y: 45  },
                    { id: 10,  x: 213,  y: 108 },
                    { id: 11,  x: 233,  y: 45  },
                    { id: 12,  x: 233,  y: 108 },
                    { id: 13,  x: 273,  y: 45  },
                    { id: 14,  x: 273,  y: 108 },
                    { id: 15,  x: 303,  y: 45  },
                    { id: 16,  x: 303,  y: 108 },

                    { id: 17,  x: 343,  y: 45  },
                    { id: 18,  x: 343,  y: 108 },
                    { id: 19,  x: 373,  y: 45  },
                    { id: 20,  x: 373,  y: 108 },
                    { id: 21,  x: 403,  y: 45  },
                    { id: 22,  x: 403,  y: 108 },
                    { id: 23,  x: 433,  y: 45  },
                    { id: 24,  x: 433,  y: 108 },

                    { id: 25,  x: 473,  y: 45  },
                    { id: 26,  x: 473,  y: 108 },
                    { id: 27,  x: 503,  y: 45  },
                    { id: 28,  x: 503,  y: 108 },
                    { id: 29,  x: 533,  y: 45  },
                    { id: 30,  x: 533,  y: 108 },
                    { id: 31,  x: 563,  y: 45  },
                    { id: 32,  x: 563,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/AS7712_rear.png" },
                "caption": { x: 5, y:150, title: "AS7712_GS rear", anchor: "start" }
            }
        }
    },

    "NCS5001(IL)": {
        "equipment_type": {
            "platform": "NCS5001(IL)",
            "os": "CiscoIOSXRSoftware",
            "firmware": "Version6.1.2",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.ncs5001",
                "config_template": "/var/www/html/initial-config/cisco/ncs5001_ILeaf_0.8_ztp_init.sh.template",
                "initial_config": "/var/www/html/initial-config/cisco/ncs5001_ILeaf_0.8_ztp_init.sh"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "snmptrap_if_name_oid": null,
                "max_repetitions": 75
            },
            "boot_complete_msg": "%OS-SYSLOG-6-LOG_INFO : ZTP is complete.",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "100g",
                       "port_prefix": "HundredGigE"
                    },
                    {
                       "speed": "40g",
                       "port_prefix": "HundredGigE"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "TenGigE"
                    },
                    {
                       "speed": "1g",
                       "port_prefix": "GigabitEthernet"
                    }
                ],
                "lag_prefix": "Bundle-Ether",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/1/0",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/1/1",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/1/2",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/1/3",
                    "speed_capabilities": ["40g", "100g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/NCS5001_front.png" },
                "caption": { x: 5, y: 20, title: "NCS5001_IL front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 65,  y: 38,  w: 23,  h: 20 },
                    { id:  1,  x: 65,  y: 63,  w: 23,  h: 20 },
                    { id:  2,  x: 92,  y: 38,  w: 23,  h: 20 },
                    { id:  3,  x: 92,  y: 63,  w: 23,  h: 20 },
                    { id:  4,  x: 119,  y: 38,  w: 23,  h: 20 },
                    { id:  5,  x: 119,  y: 63,  w: 23,  h: 20 },
                    { id:  6,  x: 146,  y: 38,  w: 23,  h: 20 },
                    { id:  7,  x: 146,  y: 63,  w: 23,  h: 20 },
                    { id:  8,  x: 173,  y: 38,  w: 23,  h: 20 },
                    { id:  9,  x: 173,  y: 63,  w: 23,  h: 20 },
                    { id: 10,  x: 200,  y: 38,  w: 23,  h: 20 },
                    { id: 11,  x: 200,  y: 63,  w: 23,  h: 20 },
                    { id: 12,  x: 227,  y: 38,  w: 23,  h: 20 },
                    { id: 13,  x: 227,  y: 63,  w: 23,  h: 20 },
                    { id: 14,  x: 254,  y: 38,  w: 23,  h: 20 },
                    { id: 15,  x: 254,  y: 63,  w: 23,  h: 20 },
                    { id: 16,  x: 281,  y: 38,  w: 23,  h: 20 },
                    { id: 17,  x: 281,  y: 63,  w: 23,  h: 20 },
                    { id: 18,  x: 308,  y: 38,  w: 23,  h: 20 },
                    { id: 19,  x: 308,  y: 63,  w: 23,  h: 20 },

                    { id: 20,  x: 345,  y: 38,  w: 23,  h: 20 },
                    { id: 21,  x: 345,  y: 63,  w: 23,  h: 20 },
                    { id: 22,  x: 372,  y: 38,  w: 23,  h: 20 },
                    { id: 23,  x: 372,  y: 63,  w: 23,  h: 20 },
                    { id: 24,  x: 399,  y: 38,  w: 23,  h: 20 },
                    { id: 25,  x: 399,  y: 63,  w: 23,  h: 20 },
                    { id: 26,  x: 426,  y: 38,  w: 23,  h: 20 },
                    { id: 27,  x: 426,  y: 63,  w: 23,  h: 20 },
                    { id: 28,  x: 453,  y: 38,  w: 23,  h: 20 },
                    { id: 29,  x: 453,  y: 63,  w: 23,  h: 20 },
                    { id: 30,  x: 480,  y: 38,  w: 23,  h: 20 },
                    { id: 31,  x: 480,  y: 63,  w: 23,  h: 20 },
                    { id: 32,  x: 507,  y: 38,  w: 23,  h: 20 },
                    { id: 33,  x: 507,  y: 63,  w: 23,  h: 20 },
                    { id: 34,  x: 534,  y: 38,  w: 23,  h: 20 },
                    { id: 35,  x: 534,  y: 63,  w: 23,  h: 20 },
                    { id: 36,  x: 561,  y: 38,  w: 23,  h: 20 },
                    { id: 37,  x: 561,  y: 63,  w: 23,  h: 20 },
                    { id: 38,  x: 588,  y: 38,  w: 23,  h: 20 },
                    { id: 39,  x: 588,  y: 63,  w: 23,  h: 20 },

                    { id: 40,  x: 625,  y: 38,  w: 27,  h: 20 },
                    { id: 41,  x: 625,  y: 63,  w: 27,  h: 20 },
                    { id: 42,  x: 660,  y: 38,  w: 27,  h: 20 },
                    { id: 43,  x: 660,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 79,   y: 45  },
                    { id:  1,  x: 79,   y: 108 },
                    { id:  2,  x: 106,  y: 45  },
                    { id:  3,  x: 106,  y: 108 },
                    { id:  4,  x: 133,  y: 45  },
                    { id:  5,  x: 133,  y: 108 },
                    { id:  6,  x: 160,  y: 45  },
                    { id:  7,  x: 160,  y: 108 },
                    { id:  8,  x: 187,  y: 45  },
                    { id:  9,  x: 187,  y: 108 },
                    { id: 10,  x: 214,  y: 45  },
                    { id: 11,  x: 214,  y: 108 },
                    { id: 12,  x: 241,  y: 45  },
                    { id: 13,  x: 241,  y: 108 },
                    { id: 14,  x: 268,  y: 45  },
                    { id: 15,  x: 268,  y: 108 },
                    { id: 16,  x: 295,  y: 45  },
                    { id: 17,  x: 295,  y: 108 },
                    { id: 18,  x: 322,  y: 45  },
                    { id: 19,  x: 322,  y: 108 },

                    { id: 20,  x: 359,  y: 45  },
                    { id: 21,  x: 359,  y: 108 },
                    { id: 22,  x: 386,  y: 45  },
                    { id: 23,  x: 386,  y: 108 },
                    { id: 24,  x: 413,  y: 45  },
                    { id: 25,  x: 413,  y: 108 },
                    { id: 26,  x: 440,  y: 45  },
                    { id: 27,  x: 440,  y: 108 },
                    { id: 28,  x: 467,  y: 45  },
                    { id: 29,  x: 467,  y: 108 },
                    { id: 30,  x: 494,  y: 45  },
                    { id: 31,  x: 494,  y: 108 },
                    { id: 32,  x: 521,  y: 45  },
                    { id: 33,  x: 521,  y: 108 },
                    { id: 34,  x: 548,  y: 45  },
                    { id: 35,  x: 548,  y: 108 },
                    { id: 36,  x: 575,  y: 45  },
                    { id: 37,  x: 575,  y: 108 },
                    { id: 38,  x: 602,  y: 45  },
                    { id: 39,  x: 602,  y: 108 },

                    { id: 40,  x: 639,  y: 45  },
                    { id: 41,  x: 639,  y: 108 },
                    { id: 42,  x: 674,  y: 45  },
                    { id: 43,  x: 674,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/NCS5001_rear.png" },
                "caption": { x: 5, y:150, title: "NCS5001_IL rear", anchor: "start" }
            }
        }
    },

    "NCS5011(GS)": {
        "equipment_type": {
            "platform": "NCS5011(GS)",
            "os": "CiscoIOSXRSoftware",
            "firmware": "Version 6.1.2",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": "<PORTPREFIX><IFSLOTNAME>/<BREAKOUTIFSUFFIX>",
            "breakout_if_name_suffix_list": "0:1:2:3",
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.ncs5011",
                "config_template": "/var/www/html/initial-config/cisco/ncs5011_Spine_0.8_ztp_init.sh.template",
                "initial_config": "/var/www/html/initial-config/cisco/ncs5011_Spine_0.8_ztp_init.sh"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "max_repetitions": 100
            },
            "boot_complete_msg": "%OS-SYSLOG-6-LOG_INFO : ZTP is complete.",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "100g",
                       "port_prefix": "HundredGigE"
                    },
                    {
                       "speed": "40g",
                       "port_prefix": "HundredGigE"
                    },
                    {
                       "speed": "10g",
                       "port_prefix": "TenGigE"
                    }
                ],
                "lag_prefix": "Bundle-Ether",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0/0",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/0/1",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/0/2",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/0/3",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/0/4",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/0/5",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/0/6",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/0/7",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/0/8",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/0/9",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/0/10",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/0/11",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/0/12",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/0/13",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/0/14",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/0/15",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/0/16",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/0/17",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/0/18",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/0/19",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/0/20",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/0/21",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/0/22",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/0/23",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/0/24",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/0/25",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/0/26",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/0/27",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/0/28",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/0/29",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/0/30",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/0/31",
                    "speed_capabilities": ["40g", "100g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/NCS5011_front.png" },
                "caption": { x: 5, y:20, title: "NCS5011_GS front", anchor: "start" },
                "slot": [
                    { id:  0,  x:  70,  y: 38,  w: 27,  h: 20 },
                    { id:  1,  x:  70,  y: 63,  w: 27,  h: 20 },
                    { id:  2,  x: 108,  y: 38,  w: 27,  h: 20 },
                    { id:  3,  x: 108,  y: 63,  w: 27,  h: 20 },
                    { id:  4,  x: 146,  y: 38,  w: 27,  h: 20 },
                    { id:  5,  x: 146,  y: 63,  w: 27,  h: 20 },
                    { id:  6,  x: 184,  y: 38,  w: 27,  h: 20 },
                    { id:  7,  x: 184,  y: 63,  w: 27,  h: 20 },
                    { id:  8,  x: 222,  y: 38,  w: 27,  h: 20 },
                    { id:  9,  x: 222,  y: 63,  w: 27,  h: 20 },
                    { id: 10,  x: 260,  y: 38,  w: 27,  h: 20 },
                    { id: 11,  x: 260,  y: 63,  w: 27,  h: 20 },
                    { id: 12,  x: 298,  y: 38,  w: 27,  h: 20 },
                    { id: 13,  x: 298,  y: 63,  w: 27,  h: 20 },
                    { id: 14,  x: 336,  y: 38,  w: 27,  h: 20 },
                    { id: 15,  x: 336,  y: 63,  w: 27,  h: 20 },
                    { id: 16,  x: 374,  y: 38,  w: 27,  h: 20 },
                    { id: 17,  x: 374,  y: 63,  w: 27,  h: 20 },
                    { id: 18,  x: 412,  y: 38,  w: 27,  h: 20 },
                    { id: 19,  x: 412,  y: 63,  w: 27,  h: 20 },
                    { id: 20,  x: 450,  y: 38,  w: 27,  h: 20 },
                    { id: 21,  x: 450,  y: 63,  w: 27,  h: 20 },
                    { id: 22,  x: 488,  y: 38,  w: 27,  h: 20 },
                    { id: 23,  x: 488,  y: 63,  w: 27,  h: 20 },
                    { id: 24,  x: 526,  y: 38,  w: 27,  h: 20 },
                    { id: 25,  x: 526,  y: 63,  w: 27,  h: 20 },
                    { id: 26,  x: 564,  y: 38,  w: 27,  h: 20 },
                    { id: 27,  x: 564,  y: 63,  w: 27,  h: 20 },
                    { id: 28,  x: 602,  y: 38,  w: 27,  h: 20 },
                    { id: 29,  x: 602,  y: 63,  w: 27,  h: 20 },
                    { id: 30,  x: 640,  y: 38,  w: 27,  h: 20 },
                    { id: 31,  x: 640,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x:  83,  y: 45  },
                    { id:  1,  x:  83,  y: 108 },
                    { id:  2,  x: 121,  y: 45  },
                    { id:  3,  x: 121,  y: 108 },
                    { id:  4,  x: 159,  y: 45  },
                    { id:  5,  x: 159,  y: 108 },
                    { id:  6,  x: 197,  y: 45  },
                    { id:  7,  x: 197,  y: 108 },
                    { id:  8,  x: 235,  y: 45  },
                    { id:  9,  x: 235,  y: 108 },
                    { id: 10,  x: 273,  y: 45  },
                    { id: 11,  x: 273,  y: 108 },
                    { id: 12,  x: 311,  y: 45  },
                    { id: 13,  x: 311,  y: 108 },
                    { id: 14,  x: 349,  y: 45  },
                    { id: 15,  x: 349,  y: 108 },
                    { id: 16,  x: 387,  y: 45  },
                    { id: 17,  x: 387,  y: 108 },
                    { id: 18,  x: 425,  y: 45  },
                    { id: 19,  x: 425,  y: 108 },
                    { id: 20,  x: 463,  y: 45  },
                    { id: 21,  x: 463,  y: 108 },
                    { id: 22,  x: 501,  y: 45  },
                    { id: 23,  x: 501,  y: 108 },
                    { id: 24,  x: 539,  y: 45  },
                    { id: 25,  x: 539,  y: 108 },
                    { id: 26,  x: 577,  y: 45  },
                    { id: 27,  x: 577,  y: 108 },
                    { id: 28,  x: 615,  y: 45  },
                    { id: 29,  x: 615,  y: 108 },
                    { id: 30,  x: 653,  y: 45  },
                    { id: 31,  x: 653,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/NCS5001_rear.png" },
                "caption": { x: 5, y:150, title: "NCS5011_GS rear", anchor: "start" }
            }
        }
    },

    "NCS5501-SE(IL)": {
        "equipment_type": {
            "platform": "NCS5501-SE(IL)",
            "os": "iox-lnx-049",
            "firmware": "Version6.3.1",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true, 
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"], 
                    "remark_default": "packet_color",
                    "shaping": true,
                    "egress_queue_capability": ["pt1", "pt2", "pt3"], 
                    "egress_queue_default": "pt1"
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.ncs5501",
                "config_template": "/var/www/html/initial-config/cisco/ncs5501_ILeaf_0.8_ztp_init.sh.template",
                "initial_config": "/var/www/html/initial-config/cisco/ncs5501_ILeaf_0.8_ztp_init.sh"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "max_repetitions": 75
            },
            "boot_complete_msg": "%PLATFORM-DPA-6-INFO : NPU #0 Initialization Completed",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "HundredGigE"
                    },
                    {
                        "speed": "40g",
                        "port_prefix": "FortyGigE"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "TenGigE"
                    },
                    {
                        "speed": "1g",
                        "port_prefix": "GigabitEthernet"
                    }
                ],
                "lag_prefix": "Bundle-Ether",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/1/0",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/1/1",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/1/2",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/1/3",
                    "speed_capabilities": ["40g", "100g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/NCS5501SE_front.png" },
                "caption": { x: 5, y: 20, title: "NCS5501_IL front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 75,  y: 38,  w: 23,  h: 20 },
                    { id:  1,  x: 75,  y: 63,  w: 23,  h: 20 },
                    { id:  2,  x: 102,  y: 38,  w: 23,  h: 20 },
                    { id:  3,  x: 102,  y: 63,  w: 23,  h: 20 },
                    { id:  4,  x: 129,  y: 38,  w: 23,  h: 20 },
                    { id:  5,  x: 129,  y: 63,  w: 23,  h: 20 },
                    { id:  6,  x: 156,  y: 38,  w: 23,  h: 20 },
                    { id:  7,  x: 156,  y: 63,  w: 23,  h: 20 },
                    { id:  8,  x: 183,  y: 38,  w: 23,  h: 20 },
                    { id:  9,  x: 183,  y: 63,  w: 23,  h: 20 },
                    { id: 10,  x: 210,  y: 38,  w: 23,  h: 20 },
                    { id: 11,  x: 210,  y: 63,  w: 23,  h: 20 },
                    { id: 12,  x: 237,  y: 38,  w: 23,  h: 20 },
                    { id: 13,  x: 237,  y: 63,  w: 23,  h: 20 },
                    { id: 14,  x: 264,  y: 38,  w: 23,  h: 20 },
                    { id: 15,  x: 264,  y: 63,  w: 23,  h: 20 },
                    { id: 16,  x: 291,  y: 38,  w: 23,  h: 20 },
                    { id: 17,  x: 291,  y: 63,  w: 23,  h: 20 },
                    { id: 18,  x: 318,  y: 38,  w: 23,  h: 20 },
                    { id: 19,  x: 318,  y: 63,  w: 23,  h: 20 },

                    { id: 20,  x: 353,  y: 38,  w: 23,  h: 20 },
                    { id: 21,  x: 353,  y: 63,  w: 23,  h: 20 },
                    { id: 22,  x: 380,  y: 38,  w: 23,  h: 20 },
                    { id: 23,  x: 380,  y: 63,  w: 23,  h: 20 },
                    { id: 24,  x: 407,  y: 38,  w: 23,  h: 20 },
                    { id: 25,  x: 407,  y: 63,  w: 23,  h: 20 },
                    { id: 26,  x: 434,  y: 38,  w: 23,  h: 20 },
                    { id: 27,  x: 434,  y: 63,  w: 23,  h: 20 },
                    { id: 28,  x: 461,  y: 38,  w: 23,  h: 20 },
                    { id: 29,  x: 461,  y: 63,  w: 23,  h: 20 },
                    { id: 30,  x: 488,  y: 38,  w: 23,  h: 20 },
                    { id: 31,  x: 488,  y: 63,  w: 23,  h: 20 },
                    { id: 32,  x: 515,  y: 38,  w: 23,  h: 20 },
                    { id: 33,  x: 515,  y: 63,  w: 23,  h: 20 },
                    { id: 34,  x: 542,  y: 38,  w: 23,  h: 20 },
                    { id: 35,  x: 542,  y: 63,  w: 23,  h: 20 },
                    { id: 36,  x: 569,  y: 38,  w: 23,  h: 20 },
                    { id: 37,  x: 569,  y: 63,  w: 23,  h: 20 },
                    { id: 38,  x: 596,  y: 38,  w: 23,  h: 20 },
                    { id: 39,  x: 596,  y: 63,  w: 23,  h: 20 },

                    { id: 40,  x: 631,  y: 38,  w: 27,  h: 20 },
                    { id: 41,  x: 631,  y: 63,  w: 27,  h: 20 },
                    { id: 42,  x: 661,  y: 38,  w: 27,  h: 20 },
                    { id: 43,  x: 661,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 89,   y: 45  },
                    { id:  1,  x: 89,   y: 108 },
                    { id:  2,  x: 116,  y: 45  },
                    { id:  3,  x: 116,  y: 108 },
                    { id:  4,  x: 143,  y: 45  },
                    { id:  5,  x: 143,  y: 108 },
                    { id:  6,  x: 170,  y: 45  },
                    { id:  7,  x: 170,  y: 108 },
                    { id:  8,  x: 197,  y: 45  },
                    { id:  9,  x: 197,  y: 108 },
                    { id: 10,  x: 224,  y: 45  },
                    { id: 11,  x: 224,  y: 108 },
                    { id: 12,  x: 251,  y: 45  },
                    { id: 13,  x: 251,  y: 108 },
                    { id: 14,  x: 278,  y: 45  },
                    { id: 15,  x: 278,  y: 108 },
                    { id: 16,  x: 305,  y: 45  },
                    { id: 17,  x: 305,  y: 108 },
                    { id: 18,  x: 332,  y: 45  },
                    { id: 19,  x: 332,  y: 108 },

                    { id: 20,  x: 367,  y: 45  },
                    { id: 21,  x: 367,  y: 108 },
                    { id: 22,  x: 394,  y: 45  },
                    { id: 23,  x: 394,  y: 108 },
                    { id: 24,  x: 421,  y: 45  },
                    { id: 25,  x: 421,  y: 108 },
                    { id: 26,  x: 448,  y: 45  },
                    { id: 27,  x: 448,  y: 108 },
                    { id: 28,  x: 475,  y: 45  },
                    { id: 29,  x: 475,  y: 108 },
                    { id: 30,  x: 502,  y: 45  },
                    { id: 31,  x: 502,  y: 108 },
                    { id: 32,  x: 529,  y: 45  },
                    { id: 33,  x: 529,  y: 108 },
                    { id: 34,  x: 556,  y: 45  },
                    { id: 35,  x: 556,  y: 108 },
                    { id: 36,  x: 583,  y: 45  },
                    { id: 37,  x: 583,  y: 108 },
                    { id: 38,  x: 610,  y: 45  },
                    { id: 39,  x: 610,  y: 108 },

                    { id: 40,  x: 645,  y: 45  },
                    { id: 41,  x: 645,  y: 108 },
                    { id: 42,  x: 675,  y: 45  },
                    { id: 43,  x: 675,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/NCS5501SE_rear.png" },
                "caption": { x: 5, y:150, title: "NCS5501_IL rear", anchor: "start" }
            }
        }
    },

    "NCS5501-SE(BL)": {
        "equipment_type": {
            "platform": "NCS5501-SE(BL)",
            "os": "iox-lnx-049",
            "firmware": "Version6.3.1",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": true, 
                    "remark_capability": ["packet_color", "af3", "af2", "af1", "be"], 
                    "remark_default": "packet_color",
                    "shaping": true,
                    "egress_queue_capability": ["pt1", "pt2", "pt3"], 
                    "egress_queue_default": "pt3"
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.ncs5501",
                "config_template": "/var/www/html/initial-config/cisco/ncs5501_BLeaf_0.8_ztp_init.sh.template",
                "initial_config": "/var/www/html/initial-config/cisco/ncs5501_BLeaf_0.8_ztp_init.sh"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.2.2.1.2",
                "max_repetitions": 75
            },
            "boot_complete_msg": "%PLATFORM-DPA-6-INFO : NPU #0 Initialization Completed",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "100g",
                        "port_prefix": "HundredGigE"
                    },
                    {
                        "speed": "40g",
                        "port_prefix": "FortyGigE"
                    },
                    {
                        "speed": "10g",
                        "port_prefix": "TenGigE"
                    },
                    {
                        "speed": "1g",
                        "port_prefix": "GigabitEthernet"
                    }
                ],
                "lag_prefix": "Bundle-Ether",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0/0/0",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/0/0/1",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/0/2",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/0/3",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/0/4",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/0/5",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/0/6",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/0/7",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/0/8",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/0/9",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/0/10",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/0/11",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/0/12",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/0/13",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/0/14",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/0/15",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/0/16",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/0/17",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/0/18",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/0/19",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/0/20",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/0/21",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/0/22",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/0/23",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/0/24",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/0/25",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/0/26",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/0/27",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/0/28",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/0/29",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/0/30",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/0/31",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/0/32",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "33",
                    "if_slot": "0/0/0/33",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "34",
                    "if_slot": "0/0/0/34",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "35",
                    "if_slot": "0/0/0/35",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "36",
                    "if_slot": "0/0/0/36",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "37",
                    "if_slot": "0/0/0/37",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "38",
                    "if_slot": "0/0/0/38",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "39",
                    "if_slot": "0/0/0/39",
                    "speed_capabilities": ["1g", "10g"]
                },
                {
                    "if_id": "40",
                    "if_slot": "0/0/1/0",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "41",
                    "if_slot": "0/0/1/1",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "42",
                    "if_slot": "0/0/1/2",
                    "speed_capabilities": ["40g", "100g"]
                },
                {
                    "if_id": "43",
                    "if_slot": "0/0/1/3",
                    "speed_capabilities": ["40g", "100g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/NCS5501SE_front.png" },
                "caption": { x: 5, y: 20, title: "NCS5501_BL front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 75,  y: 38,  w: 23,  h: 20 },
                    { id:  1,  x: 75,  y: 63,  w: 23,  h: 20 },
                    { id:  2,  x: 102,  y: 38,  w: 23,  h: 20 },
                    { id:  3,  x: 102,  y: 63,  w: 23,  h: 20 },
                    { id:  4,  x: 129,  y: 38,  w: 23,  h: 20 },
                    { id:  5,  x: 129,  y: 63,  w: 23,  h: 20 },
                    { id:  6,  x: 156,  y: 38,  w: 23,  h: 20 },
                    { id:  7,  x: 156,  y: 63,  w: 23,  h: 20 },
                    { id:  8,  x: 183,  y: 38,  w: 23,  h: 20 },
                    { id:  9,  x: 183,  y: 63,  w: 23,  h: 20 },
                    { id: 10,  x: 210,  y: 38,  w: 23,  h: 20 },
                    { id: 11,  x: 210,  y: 63,  w: 23,  h: 20 },
                    { id: 12,  x: 237,  y: 38,  w: 23,  h: 20 },
                    { id: 13,  x: 237,  y: 63,  w: 23,  h: 20 },
                    { id: 14,  x: 264,  y: 38,  w: 23,  h: 20 },
                    { id: 15,  x: 264,  y: 63,  w: 23,  h: 20 },
                    { id: 16,  x: 291,  y: 38,  w: 23,  h: 20 },
                    { id: 17,  x: 291,  y: 63,  w: 23,  h: 20 },
                    { id: 18,  x: 318,  y: 38,  w: 23,  h: 20 },
                    { id: 19,  x: 318,  y: 63,  w: 23,  h: 20 },

                    { id: 20,  x: 353,  y: 38,  w: 23,  h: 20 },
                    { id: 21,  x: 353,  y: 63,  w: 23,  h: 20 },
                    { id: 22,  x: 380,  y: 38,  w: 23,  h: 20 },
                    { id: 23,  x: 380,  y: 63,  w: 23,  h: 20 },
                    { id: 24,  x: 407,  y: 38,  w: 23,  h: 20 },
                    { id: 25,  x: 407,  y: 63,  w: 23,  h: 20 },
                    { id: 26,  x: 434,  y: 38,  w: 23,  h: 20 },
                    { id: 27,  x: 434,  y: 63,  w: 23,  h: 20 },
                    { id: 28,  x: 461,  y: 38,  w: 23,  h: 20 },
                    { id: 29,  x: 461,  y: 63,  w: 23,  h: 20 },
                    { id: 30,  x: 488,  y: 38,  w: 23,  h: 20 },
                    { id: 31,  x: 488,  y: 63,  w: 23,  h: 20 },
                    { id: 32,  x: 515,  y: 38,  w: 23,  h: 20 },
                    { id: 33,  x: 515,  y: 63,  w: 23,  h: 20 },
                    { id: 34,  x: 542,  y: 38,  w: 23,  h: 20 },
                    { id: 35,  x: 542,  y: 63,  w: 23,  h: 20 },
                    { id: 36,  x: 569,  y: 38,  w: 23,  h: 20 },
                    { id: 37,  x: 569,  y: 63,  w: 23,  h: 20 },
                    { id: 38,  x: 596,  y: 38,  w: 23,  h: 20 },
                    { id: 39,  x: 596,  y: 63,  w: 23,  h: 20 },

                    { id: 40,  x: 631,  y: 38,  w: 27,  h: 20 },
                    { id: 41,  x: 631,  y: 63,  w: 27,  h: 20 },
                    { id: 42,  x: 661,  y: 38,  w: 27,  h: 20 },
                    { id: 43,  x: 661,  y: 63,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 89,   y: 45  },
                    { id:  1,  x: 89,   y: 108 },
                    { id:  2,  x: 116,  y: 45  },
                    { id:  3,  x: 116,  y: 108 },
                    { id:  4,  x: 143,  y: 45  },
                    { id:  5,  x: 143,  y: 108 },
                    { id:  6,  x: 170,  y: 45  },
                    { id:  7,  x: 170,  y: 108 },
                    { id:  8,  x: 197,  y: 45  },
                    { id:  9,  x: 197,  y: 108 },
                    { id: 10,  x: 224,  y: 45  },
                    { id: 11,  x: 224,  y: 108 },
                    { id: 12,  x: 251,  y: 45  },
                    { id: 13,  x: 251,  y: 108 },
                    { id: 14,  x: 278,  y: 45  },
                    { id: 15,  x: 278,  y: 108 },
                    { id: 16,  x: 305,  y: 45  },
                    { id: 17,  x: 305,  y: 108 },
                    { id: 18,  x: 332,  y: 45  },
                    { id: 19,  x: 332,  y: 108 },

                    { id: 20,  x: 367,  y: 45  },
                    { id: 21,  x: 367,  y: 108 },
                    { id: 22,  x: 394,  y: 45  },
                    { id: 23,  x: 394,  y: 108 },
                    { id: 24,  x: 421,  y: 45  },
                    { id: 25,  x: 421,  y: 108 },
                    { id: 26,  x: 448,  y: 45  },
                    { id: 27,  x: 448,  y: 108 },
                    { id: 28,  x: 475,  y: 45  },
                    { id: 29,  x: 475,  y: 108 },
                    { id: 30,  x: 502,  y: 45  },
                    { id: 31,  x: 502,  y: 108 },
                    { id: 32,  x: 529,  y: 45  },
                    { id: 33,  x: 529,  y: 108 },
                    { id: 34,  x: 556,  y: 45  },
                    { id: 35,  x: 556,  y: 108 },
                    { id: 36,  x: 583,  y: 45  },
                    { id: 37,  x: 583,  y: 108 },
                    { id: 38,  x: 610,  y: 45  },
                    { id: 39,  x: 610,  y: 108 },

                    { id: 40,  x: 645,  y: 45  },
                    { id: 41,  x: 645,  y: 108 },
                    { id: 42,  x: 675,  y: 45  },
                    { id: 43,  x: 675,  y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/NCS5501SE_rear.png" },
                "caption": { x: 5, y:150, title: "NCS5501_BL rear", anchor: "start" }
            }
        }
    },

    "MX240(BL)": {
        "equipment_type": {
            "platform": "MX240(BL)",
            "os": "JUNOS",
            "firmware": "15.1R5.5",
            "router_type": "core-router",
            "physical_if_name_syntax": "<PORTPREFIX><NODEID>/<SLOT>",
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": false,
                    "l3": true
                },
                "qos": {
                    "remark": false, 
                    "remark_capability": null, 
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null, 
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.qfx5200",
                "config_template": "/initial-config/juniper/ztp.conf.qfx5200-32c_GS",
                "initial_config": "/initial-config/juniper/ztp.conf.qfx5200-32c_GS"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "UI_COMMIT_COMPLETED: commit complete",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                        "speed": "10g",
                        "port_prefix": "xe-"
                    }, {
                        "speed": "1g",
                        "port_prefix": "ge-"
                    }
                ],
                "lag_prefix": "ae",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "0",
                    "if_slot": "0/0",
                    "speed_capabilities": ["10g"]
                },
                {
                    "if_id": "1",
                    "if_slot": "0/1",
                    "speed_capabilities": ["10g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "1/0",
                    "speed_capabilities": ["10g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "1/1",
                    "speed_capabilities": ["10g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "2/0",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "2/1",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "2/2",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "2/3",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "2/4",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "2/5",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "2/6",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "2/7",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "2/8",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "2/9",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "3/0",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "3/1",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "3/2",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "3/3",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "3/4",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "3/5",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "3/6",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "3/7",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "3/8",
                    "speed_capabilities": ["1g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "3/9",
                    "speed_capabilities": ["1g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 0, w: 700, h: 340 },
                "image": { x: 0, y: 0, w: 700, h: 340, url: "img/device/MX240_front.png" },
                "caption": { x: 5, y: 5, title: "MX240_BL front", anchor: "start" },
                "slot": [
                    { id:  0,  x: 153,  y: 133,  w: 27,  h: 20 },
                    { id:  1,  x: 153,  y: 157,  w: 27,  h: 20 },
                    { id:  2,  x: 183,  y: 133,  w: 27,  h: 20 },
                    { id:  3,  x: 183,  y: 157,  w: 27,  h: 20 },
                    { id:  4,  x: 213,  y: 133,  w: 27,  h: 20 },
                    { id:  5,  x: 213,  y: 157,  w: 27,  h: 20 },

                    { id:  6,  x: 253,  y: 133,  w: 27,  h: 20 },
                    { id:  7,  x: 253,  y: 157,  w: 27,  h: 20 },
                    { id:  8,  x: 283,  y: 133,  w: 27,  h: 20 },
                    { id:  9,  x: 283,  y: 157,  w: 27,  h: 20 },
                    { id: 10,  x: 313,  y: 133,  w: 27,  h: 20 },
                    { id: 11,  x: 313,  y: 157,  w: 27,  h: 20 },

                    { id: 12,  x: 423,  y: 133,  w: 27,  h: 20 },
                    { id: 13,  x: 423,  y: 157,  w: 27,  h: 20 },
                    { id: 14,  x: 453,  y: 133,  w: 27,  h: 20 },
                    { id: 15,  x: 453,  y: 157,  w: 27,  h: 20 },
                    { id: 16,  x: 483,  y: 133,  w: 27,  h: 20 },
                    { id: 17,  x: 483,  y: 157,  w: 27,  h: 20 },

                    { id: 18,  x: 523,  y: 133,  w: 27,  h: 20 },
                    { id: 19,  x: 523,  y: 157,  w: 27,  h: 20 },
                    { id: 20,  x: 553,  y: 133,  w: 27,  h: 20 },
                    { id: 21,  x: 553,  y: 157,  w: 27,  h: 20 },
                    { id: 22,  x: 583,  y: 133,  w: 27,  h: 20 },
                    { id: 23,  x: 583,  y: 157,  w: 27,  h: 20 }
                ],
                "label": [
                    { id:  0,  x: 166,  y: 140 },
                    { id:  1,  x: 166,  y: 202 },
                    { id:  2,  x: 196,  y: 140 },
                    { id:  3,  x: 196,  y: 202 },
                    { id:  4,  x: 226,  y: 140 },
                    { id:  5,  x: 226,  y: 202 },
                    { id:  6,  x: 266,  y: 140 },
                    { id:  7,  x: 266,  y: 202 },
                    { id:  8,  x: 296,  y: 140 },
                    { id:  9,  x: 296,  y: 202 },
                    { id: 10,  x: 326,  y: 140 },
                    { id: 11,  x: 326,  y: 202 },

                    { id: 12,  x: 436,  y: 140 },
                    { id: 13,  x: 436,  y: 202 },
                    { id: 14,  x: 466,  y: 140 },
                    { id: 15,  x: 466,  y: 202 },
                    { id: 16,  x: 496,  y: 140 },
                    { id: 17,  x: 496,  y: 202 },
                    { id: 18,  x: 536,  y: 140 },
                    { id: 19,  x: 536,  y: 202 },
                    { id: 20,  x: 566,  y: 140 },
                    { id: 21,  x: 566,  y: 202 },
                    { id: 22,  x: 596,  y: 140 },
                    { id: 23,  x: 596,  y: 202 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 363, w: 700, h: 340 },
                "image": { x: 0, y: 363, w: 700, h: 340, url: "img/device/MX240_rear.png" },
                "caption": { x: 5, y: 368, title: "MX240_BL rear", anchor: "start" }
            }
        }
    },

    "S6000(GS)": {
        "equipment_type": {
            "platform": "S6000(GS)",
            "os": "Cumulus",
            "firmware": "3.6.0",
            "router_type": "normal",
            "physical_if_name_syntax": null,
            "breakout_if_name_syntax": null,
            "breakout_if_name_suffix_list": null,
            "capability": {
                "vpn": {
                    "l2": true,
                    "l3": true
                },
                "qos": {
                    "remark": false,
                    "remark_capability": null,
                    "remark_default": null,
                    "shaping": false,
                    "egress_queue_capability": null,
                    "egress_queue_default": null
                }
            },
            "dhcp": {
                "dhcp_template": "/root/setup/dhcp_template/dhcpd.conf.cumulus",
                "config_template": "/var/lib/tftpboot/cumulus/GS/config_template:/var/lib/tftpboot/cumulus/ztp_sh_gs_template",
                "initial_config": "/var/lib/tftpboot/cumulus/GS:/var/lib/tftpboot/cumulus"
            },
            "snmp": {
                "if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "snmptrap_if_name_oid": "1.3.6.1.2.1.31.1.1.1.1",
                "max_repetitions": 100
            },
            "boot_complete_msg": "ZTP DHCP: Script returned success",
            "boot_error_msgs": null,
            "if_definitions": {
                "ports": [
                    {
                       "speed": "40g",
                       "port_prefix": "swp"
                    }
                ],
                "lag_prefix": "bond",
                "unit_connector": "."
            },
            "slots": [
                {
                    "if_id": "1",
                    "if_slot": "0/0/1",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "2",
                    "if_slot": "0/0/2",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "3",
                    "if_slot": "0/0/3",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "4",
                    "if_slot": "0/0/4",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "5",
                    "if_slot": "0/0/5",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "6",
                    "if_slot": "0/0/6",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "7",
                    "if_slot": "0/0/7",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "8",
                    "if_slot": "0/0/8",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "9",
                    "if_slot": "0/0/9",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "10",
                    "if_slot": "0/0/10",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "11",
                    "if_slot": "0/0/11",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "12",
                    "if_slot": "0/0/12",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "13",
                    "if_slot": "0/0/13",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "14",
                    "if_slot": "0/0/14",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "15",
                    "if_slot": "0/0/15",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "16",
                    "if_slot": "0/0/16",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "17",
                    "if_slot": "0/0/17",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "18",
                    "if_slot": "0/0/18",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "19",
                    "if_slot": "0/0/19",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "20",
                    "if_slot": "0/0/20",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "21",
                    "if_slot": "0/0/21",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "22",
                    "if_slot": "0/0/22",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "23",
                    "if_slot": "0/0/23",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "24",
                    "if_slot": "0/0/24",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "25",
                    "if_slot": "0/0/25",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "26",
                    "if_slot": "0/0/26",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "27",
                    "if_slot": "0/0/27",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "28",
                    "if_slot": "0/0/28",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "29",
                    "if_slot": "0/0/29",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "30",
                    "if_slot": "0/0/30",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "31",
                    "if_slot": "0/0/31",
                    "speed_capabilities": ["40g"]
                },
                {
                    "if_id": "32",
                    "if_slot": "0/0/32",
                    "speed_capabilities": ["40g"]
                }
            ]
        },
        "view": {
            "front": {
                "body": { x: 0, y: 20, w: 700, h: 82 },
                "image": { x: 0, y: 20, w: 700, h: 82, url: "img/device/S6000_front.png" },
                "caption": { x: 5, y:20, title: "S6000(GS) front", anchor: "start" },
                "slot": [
                    { id:  1, x:  20, y: 38, w: 30, h: 20 },
                    { id:  2, x:  20, y: 63, w: 30, h: 20 },
                    { id:  3, x:  54, y: 38, w: 30, h: 20 },
                    { id:  4, x:  54, y: 63, w: 30, h: 20 },

                    { id:  5, x:  96, y: 38, w: 30, h: 20 },
                    { id:  6, x:  96, y: 63, w: 30, h: 20 },
                    { id:  7, x: 130, y: 38, w: 30, h: 20 },
                    { id:  8, x: 130, y: 63, w: 30, h: 20 },

                    { id:  9, x: 172, y: 38, w: 30, h: 20 },
                    { id: 10, x: 172, y: 63, w: 30, h: 20 },
                    { id: 11, x: 206, y: 38, w: 30, h: 20 },
                    { id: 12, x: 206, y: 63, w: 30, h: 20 },

                    { id: 13, x: 248, y: 38, w: 30, h: 20 },
                    { id: 14, x: 248, y: 63, w: 30, h: 20 },
                    { id: 15, x: 282, y: 38, w: 30, h: 20 },
                    { id: 16, x: 282, y: 63, w: 30, h: 20 },

                    { id: 17, x: 324, y: 38, w: 30, h: 20 },
                    { id: 18, x: 324, y: 63, w: 30, h: 20 },
                    { id: 19, x: 358, y: 38, w: 30, h: 20 },
                    { id: 20, x: 358, y: 63, w: 30, h: 20 },

                    { id: 21, x: 400, y: 38, w: 30, h: 20 },
                    { id: 22, x: 400, y: 63, w: 30, h: 20 },
                    { id: 23, x: 434, y: 38, w: 30, h: 20 },
                    { id: 24, x: 434, y: 63, w: 30, h: 20 },

                    { id: 25, x: 476, y: 38, w: 30, h: 20 },
                    { id: 26, x: 476, y: 63, w: 30, h: 20 },
                    { id: 27, x: 510, y: 38, w: 30, h: 20 },
                    { id: 28, x: 510, y: 63, w: 30, h: 20 },

                    { id: 29, x: 552, y: 38, w: 30, h: 20 },
                    { id: 30, x: 552, y: 63, w: 30, h: 20 },
                    { id: 31, x: 586, y: 38, w: 30, h: 20 },
                    { id: 32, x: 586, y: 63, w: 30, h: 20 }
                ],
                "label": [
                    { id:  1, x:  35, y:  45 },
                    { id:  2, x:  35, y: 108 },
                    { id:  3, x:  69, y:  45 },
                    { id:  4, x:  69, y: 108 },

                    { id:  5, x: 111, y:  45 },
                    { id:  6, x: 111, y: 108 },
                    { id:  7, x: 145, y:  45 },
                    { id:  8, x: 145, y: 108 },

                    { id:  9, x: 187, y:  45 },
                    { id: 10, x: 187, y: 108 },
                    { id: 11, x: 221, y:  45 },
                    { id: 12, x: 221, y: 108 },

                    { id: 13, x: 263, y:  45 },
                    { id: 14, x: 263, y: 108 },
                    { id: 15, x: 297, y:  45 },
                    { id: 16, x: 297, y: 108 },

                    { id: 17, x: 339, y:  45 },
                    { id: 18, x: 339, y: 108 },
                    { id: 19, x: 373, y:  45 },
                    { id: 20, x: 373, y: 108 },

                    { id: 21, x: 415, y:  45 },
                    { id: 22, x: 415, y: 108 },
                    { id: 23, x: 449, y:  45 },
                    { id: 24, x: 449, y: 108 },

                    { id: 25, x: 491, y:  45 },
                    { id: 26, x: 491, y: 108 },
                    { id: 27, x: 525, y:  45 },
                    { id: 28, x: 525, y: 108 },

                    { id: 29, x: 567, y:  45 },
                    { id: 30, x: 567, y: 108 },
                    { id: 31, x: 601, y:  45 },
                    { id: 32, x: 601, y: 108 }
                ]
            },
            "rear": {
                "body": { x: 0, y: 150, w: 700, h: 82 },
                "image": { x: 0, y: 150, w: 700, h: 82, url: "img/device/S6000_rear.png" },
                "caption": { x: 5, y:150, title: "S6000(GS) rear", anchor: "start" }
            }
        }
    }
};
