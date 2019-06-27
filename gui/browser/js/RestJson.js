// 定数定義

// REST発行時のslice_type
MSF.Json = {
    // Leaf追加
    P010401: {
        PARM: {
            cluster_id: ""
        },

        JSON: {
            node_id: '',
            equipment_type_id: '',
            leaf_type: 'BL',
            host_name: '',
            mac_address: '',
            username: '',
            password: '',
            provisioning: '',
            vpn_type: '',
            irb_type: '',
            plane: MSF.Conf.Rest.MFC ? MSF.Conf.Rest.MFC.PLANE : MSF.Conf.Rest.FC[0].PLANE,
            snmp_community: '',
            ntp_server_address: '',
            breakout: {
                local: {
                    breakout_ifs: [
                        {
                            breakout_if_ids: ['','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        }
                    ]
                },
                opposite: [
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    }
                ]
            },
            internal_links: {
                physical_links: [
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    }
                ],
                lag_links: [
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    }
                ]
            },
            management_if_address: '',
            management_if_prefix: ''
        }
    },
    // Spine追加
    P010501: {
        PARM: {
            cluster_id: ""
        },

        JSON: {
            node_id: '',
            equipment_type_id: '',
            host_name: '',
            mac_address: '',
            username: '',
            password: '',
            provisioning: '',
            snmp_community: '',
            ntp_server_address: '',
            breakout: {
                local: {
                    breakout_ifs: [
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        },
                        {
                            breakout_if_ids: ['','','','','','','','','',''],
                            base_if: {
                                physical_if_id: ''
                            },
                            division_number: '',
                            breakout_if_speed: ''
                        }
                    ]
                },
                opposite: [
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        breakout_ifs: [
                            {
                                breakout_if_ids: ['','','','','','','','','',''],
                                base_if: {
                                    physical_if_id: ''
                                },
                                division_number: '',
                                breakout_if_speed: ''
                            }
                        ]
                    }
                ]
            },
            internal_links: {
                physical_links: [
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        internal_link_if: {
                            local: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            },
                            opposite: {
                                physical_if: {
                                    physical_if_id: '',
                                    physical_if_speed: ''
                                },
                                breakout_if: {
                                    breakout_if_id: ''
                                }
                            }
                        }
                    }
                ],
                lag_links: [
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    },
                    {
                        opposite_node_id: '',
                        local_traffic_threshold: null,
                        opposite_traffic_threshold: null,
                        member_ifs: [
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            },
                            {
                                local: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                },
                                opposite: {
                                    physical_if: {
                                        physical_if_id: '',
                                        physical_if_speed: ''
                                    },
                                    breakout_if: {
                                        breakout_if_id: ''
                                    }
                                }
                            }
                        ]
                    }
                ]
            },
            management_if_address: '',
            management_if_prefix: ''
        }
    },
    // breakout登録
    P010901_add: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'add',
                    path: '',
                    value: {
                        base_if: {
                            physical_if_id: ''
                        },
                        division_number: '',
                        breakout_if_speed: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        base_if: {
                            physical_if_id: ''
                        },
                        division_number: '',
                        breakout_if_speed: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        base_if: {
                            physical_if_id: ''
                        },
                        division_number: '',
                        breakout_if_speed: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        base_if: {
                            physical_if_id: ''
                        },
                        division_number: '',
                        breakout_if_speed: ''
                    }
                }
            ]
        }
    },
    // breakout削除
    P010901_delete: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                }
            ]
        }
    },
    // IF閉塞状態変更
    P011501: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: "",
            if_type:"physical-ifs",
            if_id:""
        },

        JSON: {
            blockade_status: 'down'
        }
    },
    //CP生成_l2
    P020201_add_l2: {
        PARM: {
            slice_type: "",
            slice_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        pair_cp_id: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        irb: {
                            irb_ipv4_address:'',
                            vga_ipv4_address:'',
                            ipv4_address_prefix:''
                        },
                        port_mode: '',
                        traffic_threshold: ''
                    }
                }
            ]
        }
    },
    //CP生成_l3
    P020201_add_l3: {
        PARM: {
            slice_type: "",
            slice_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        cluster_id: '',
                        edge_point_id: '',
                        vlan_id: '',
                        mtu: '',
                        qos: {
                            ingress_shaping_rate: '',
                            egress_shaping_rate: '',
                            egress_queue_menu: ''
                        },
                        ipv4_address: '',
                        ipv6_address: '',
                        ipv4_prefix: '',
                        ipv6_prefix: '',
                        bgp: {
                            role: '',
                            neighbor_as: '',
                            neighbor_ipv4_address: '',
                            neighbor_ipv6_address: ''
                        },
                        static_routes: [
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            },
                            {
                                addr_type: '',
                                address: '',
                                prefix: '',
                                next_hop: ''
                            }
                        ],
                        vrrp: {
                            group_id: '',
                            role: '',
                            virtual_ipv4_address: ''
                        },
                        traffic_threshold: ''
                    }
                }
            ]
        }
    },
    //L2スライス追加
    P020101_l2: {
        PARM: {
            slice_type: ''
        },

        JSON: {
            slice_id: '',
            remark_menu: '',
            irb_type: ''
        }
    },
    //CP削除
    P020201_delete: {
        PARM: {
            slice_type: "",
            slice_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                }
            ]
        }
    },
    // CP変更
    P020203: {
        PARM: {
            slice_type: "",
            slice_id: "",
            cp_id: ""
        },

        JSON: {
            action: MSF.Const.ActionType.Update,
            update_option: {
                qos_update_option: {
                    ingress_shaping_rate: '',
                    egress_shaping_rate: '',
                    egress_queue_menu: ''
                }
            }

        }
    },
    // 内部リンクIF優先度変更
    P060101: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: "",
            internal_link_if_id: ""
        },

        JSON: {
            action: 'update',
            update_option: {
                igp_cost: ''
            }
        }
    },
    // 優先装置グループ装置追加
    P060201_add: {
        PARM: {
            cluster_id: "",
        },

        JSON: {
            op: 'add',
            noname: [
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                }
            ]
        }
    },
    // 優先装置グループ装置削除
    P060201_delete: {
        PARM: {
            cluster_id: "",
        },

        JSON: {
            op: 'remove',
            noname: [
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                },
                {
                    path: ''
                }
            ]
        }
    },
    // 物理IFフィルタ追加
    P070104_add: {
        PARM: {
            cluster_id: "",
            fabric_type: "leafs",
            node_id: "",
            physical_if_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                }
            ]
        }
    },
    // 物理IFフィルタ削除
    P070104_delete: {
        PARM: {
            cluster_id: "",
            fabric_type: "leafs",
            node_id: "",
            physical_if_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                }
            ]
        }
    },
    // LagIFフィルタ追加
    P070107_add: {
        PARM: {
            cluster_id: "",
            fabric_type: "leafs",
            node_id: "",
            lag_if_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                },
                {
                    op: 'add',
                    path: '',
                    value: {
                        action: 'discard',
                        direction: 'in',
                        source_mac_address: '',
                        dest_mac_address: '',
                        source_ip_address: '',
                        dest_ip_address: '',
                        protocol: '',
                        source_port: '',
                        dest_port: '',
                    }
                }
            ]
        }
    },
    // LagIFフィルタ削除
    P070107_delete: {
        PARM: {
            cluster_id: "",
            fabric_type: "leafs",
            node_id: "",
            lag_if_id: ""
        },

        JSON: {
            noname: [
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                },
                {
                    op: 'remove',
                    path: ''
                }
            ]
        }
    },
    // 装置OSアップグレード
    P080201: {
        PARM: {
        },

        JSON: {
            reservation_time: '',
            cluster_id: '',
            nodes: [
                {
                    fabric_type: 'leaf',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: 'true',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: 'true'
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                },
                {
                    fabric_type: '',
                    node_id: '',
                    equipment_type_id: '',
                    os_upgrade: {
                        upgrade_script_path: '',
                        ztp_flag: '',
                        upgrade_complete_msg: '',
                        upgrade_error_msgs: ['','','','','']
                    },
                    operator_check: ''
                }
            ]
        }
    },
    // 装置迂回
    P080101: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: ""
        },

        JSON: {
            action: MSF.Const.ActionType.Update,
            update_option: {
                detoured: 'true'
            }
        }
    },
    //LagIF情報変更
    P011104: {
        PARM: {
            cluster_id: "",
            fabric_type: "",
            node_id: "",
            lag_if_id: ""
        },

        JSON: {
            action: "",
            physical_if_ids: ['','','','','','','','','',''],
            breakout_if_ids: ['','','','','','','','','','']
        }
    },
    //LagIF情報変更(減速）
    P011104_delete: {
        PRAM: {
            cluster_id: "",
            fabric_type: "",
            node_id: "",
            lag_if_id:""
        },
        
        JSON: {
            action: "del_if",
            physical_if_ids: ['','','','','',''],
            breakout_if_ids: ['','','','','','']
        }
    },
};

