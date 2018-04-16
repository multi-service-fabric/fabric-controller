//
// グローバルネームスペース
//
var MSF = MSF || {};
MSF.console = console;

//SVGキャンバス
var svg;
var nodeView;
var controllerView;

// スライス色定義
var sliceColor = [
    "#33CCCC",
    "#6666FF",
    "#FF3399",
    "#FFCC00",
    "#00CC00",
    "#00CCFF",
    "#9966FF",
    "#FF0066",
    "#FFFF00",
    "#00FF00",
    "#0099FF",
    "#CC66FF",
    "#FF5050",
    "#CCFF33",
    "#00FF99",
    "#0066FF",
    "#FF00FF",
    "#FF6600",
    "#99FF33",
    "#00FFCC",
    "#3366FF",
    "#FF33CC",
    "#FF9933",
    "#66FF33"
];

// トラヒック色定義
var trafficColor = [
    "#ABC8FF",  // ～0.25
    "#75D0F4",  // ～0.5
    "#4AD5DB",  // ～1
    "#48D5B9",  // ～2
    "#67D194",  // ～4
    "#8BCB72",  // ～8
    "#AEC158",  // ～16
    "#CEB44C",  // ～32
    "#E8A551",  // ～64
    "#FB9664"   // ～128+
];
// traffic: Gbps
var trafficBoundary = [0.25, 0.5, 1, 2, 4, 8, 16, 32, 64, 128];

//インタフェース色定義
var interfaceColor = {
    "physical": "#8064A2",
    "breakout": "#9BBB59",
    "lag":      "#4BACC6",
    "unused":   "#a6a6a6"
};

//
// コンフィグ
//
MSF.Conf = {
    System: {
        // デバック設定
        Debug: {
//              // サーバーに接続しない
//              NOT_CONNECT_TO_SERVER: true,
//              // 初期データとしてダミーデータを使用する
//              USE_DUMMY_DATA: true,
//              // 障害通知ダイアログ抑止
//              NOT_NOTIFY_FAILURE_DIALOG: true,
//              // CPU使用率超過通知ダイアログ抑止
//              NOT_NOTIFY_CPU_THRESHOLD_DIALOG: true
        },
        Lang: {
            lng: "en"
//          lng: "ja"
//          lng: "zh_CN"
//          lng: "zh_TW"
        }
    },
    //
    // MSFMainクラス設定
    //
    MSFMain: {
        // ポーリング設定
        // INTERVAL：インターバル：ミリ秒単位
        // TIMEOUT ：タイムアウト値：秒単位
        Polling: {
            // メインポーリング処理
            MainPolling: {
                INTERVAL: 5000
            },
            // SWクラスタ増設処理
            AddSwCluster: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // SWクラスタ減設処理
            DeleteSwCluster: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // Leaf追加処理
            AddLeaf: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // Leaf削除処理
            DeleteLeaf: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // Leaf変更処理
            ModifyLeaf: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // Spine追加処理
            AddSpine: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // Spine削除処理
            DeleteSpine: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // breakoutIF登録・削除処理
            BreakoutIFs: {
                INTERVAL: 3000,
                TIMEOUT: 45
            },
            // LagIF生成処理
            AddLagIF: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // LagIF削除処理
            DeleteLagIF: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // クラスタ間リンクIF新設処理
            AddClusterLinkIfs: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // クラスタ間リンクIF減設処理
            DeleteClusterLinkIfs: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // スライス変更処理
            ModifySlice: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // CP生成処理
            AddCP: {
                INTERVAL: 3000,
                TIMEOUT: 75
            },
            // CP変更処理
            ModifyCP: {
                INTERVAL: 3000,
                TIMEOUT: 15
            },
            // CP削除処理
            DeleteCP: {
                INTERVAL: 3000,
                TIMEOUT: 15
            }
        }

    },
    //
    // Rest通信クラス設定
    //
    Rest: {
        MFC: {
            HOST: "http://localhost:18090",
            // GET処理におけるタイムアウト値。0指定はタイムアウトなし
            GET_TIMEOUT: 60000,
            // 機種情報の有効期限(秒)
            MODEL_INFO_VALID_PERIOD: 300,
            METRIC: 10,
            MTU: 1514,
            NTP_SERVER_ADDRESS: "192.168.111.100",
            PASSW0RD: "nslab1",
            USERNAME: "nslab",
            PLANE: 1,
            ROLE: "master",
            SNMP_COMMUNITY: "ntt-msf",
            // オペレーション完了通知先アドレス
            NOTIFY_ADDRESS: "192.168.56.150",
            // オペレーション完了通知先ポート
            NOTIFY_PORT: "10000",
            // シングルクラスタ構成
            FC_SINGLE: false
        }
    },
    //
    // 詳細部設定
    //
    Detail: {
        // IFトラヒック、CPトラヒック詳細情報
        Traffic: {
            // 送受信の単位をGbpsではなくMbpsで表示する
            UNIT_MBPS: true,
            // 送受信の小数以下桁数。(0の場合は取得した生値を表示。桁丸めなし)
            DECIMAL_PLACES: 2
        }
    },
    //
    // MsfLogクラス設定
    //
    MsfLog: {
        // ログ保持期限(秒) ※０以下の指定は期限なし
        LOG_MAX_SECONDS: 3600,
        // ログ保持件数 ※０以下の指定は期限なし
        LOG_MAX_COUNT: 100,
        // ログ追加時の自動スクロール
        AUTO_SCROLL: true
    },
    //
    // MsfCanvasFrameクラス設定
    //
    MsfCanvasFrame: {
        //
        // キャンバス
        //
        Canvas: {
            // 最大スケーリング倍率
            SCALE_MIN: 0.5,
            // 最小スケーリング倍率
            SCALE_MAX: 3.0,
            // スケーリングステップ
            SCALE_STEP: 0.1,
            // デフォルト倍率
            SCALE_DEFAULT: 1.0
        }
    },
    //
    // MsfCanvasクラス設定
    //
    MsfCanvas: {
        //
        // スライス
        //
        Slice: {
            // 一度に表示するスライスの数（物理デバイスは含まない）
            MAX_SLICE_COUNT: 1,
            MARGIN: 25,  // キャンバスとスライス、および、スライス同士の隙間（上下左右）
            // 論理モード時
            Logical_l2: {
                //FILL_COLOR: "#EFF7FF", /* lavenderから色相-30して、彩度を6にした色 */
                FILL_COLOR: "aliceblue",
                STROKE_COLOR: "#007FFF", /*FILL_COLORの彩度を100%に*/
                RADIUS: 40,
                STROKE_WIDTH: 1,
                // 影設定
                SHADOW_COLOR: "lightgrey",
                SHADOW_BLUR: 15,
                SHADOW_OFFSET_X: 7,
                SHADOW_OFFSET_Y: 7,
                // 選択時
                Active: {
                    STROKE_WIDTH: 3
                },
                // フォント関連
                Font: {
                    NAME: "20px Century Gothic Italic ",
                    FILL_COLOR: "#007FFF",
                    STROKE_COLOR: "white",
                    STROKE_WIDTH: 8
                }
            },
            Logical_l3: {
                //FILL_COLOR: "#F5EFFF", /* lavenderから色相+30して、彩度を6にした色 */
                FILL_COLOR: "aliceblue",
                STROKE_COLOR: "#5D00FF", /*FILL_COLORの彩度を100%に*/
                RADIUS: 40,
                STROKE_WIDTH: 1,
                // 影設定
                SHADOW_COLOR: "lightgrey",
                SHADOW_BLUR: 15,
                SHADOW_OFFSET_X: 7,
                SHADOW_OFFSET_Y: 7,
                // 選択時
                Active: {
                    STROKE_WIDTH: 3
                },
                // フォント関連
                Font: {
                    NAME: "20px Century Gothic Italic ",
                    FILL_COLOR: "#5D00FF",
                    STROKE_COLOR: "white",
                    STROKE_WIDTH: 8
                }
            },
            // 物理モード時
            Physical: {
                FILL_COLOR: "white",
                STROKE_COLOR: "grey",
                RADIUS: 10,
                STROKE_WIDTH: 1,
                // 影設定
                SHADOW_COLOR: "lightgrey",
                SHADOW_BLUR: 15,
                SHADOW_OFFSET_X: 7,
                SHADOW_OFFSET_Y: 7,
                // 選択時
                Active: {
                    STROKE_WIDTH: 3
                },
                // フォント関連
                Font: {
                    NAME: "20px Century Gothic Italic ",
                    FILL_COLOR: "black",
                    STROKE_COLOR: "white",
                    STROKE_WIDTH: 8
                }
            }
        },
        //
        // クラスター
        //
        Cluster: {
            HEIGHT: 38,
            DEPTH: 220,
            MARGIN_X: 80,  // スライスとクラスタの隙間（左右）
            MARGIN_Y: 15,  // スライスとクラスタの隙間（上下）
            CLEARANCE: 40, // クラスタ同士の隙間
            PADDING: 10,   // クラスタとポートの隙間（左右）
            // クラスター奥からのDepth軸座標
            SPINE_DEPTH: 0,
            LEAF_DEPTH: 150,
            MINIMUM_PORT_COUNT: 4,
            // CP接続線
            ConnectionPoint:{
                // 髭の長さ
                LENGTH: 18,
                // 表示する髭の最大数
                MAX_CP_DISP_COUNT:10,
                // 髭の色
                STROKE_COLOR: "dimgray",
                // 髭の太さ
                STROKE_WIDTH: 1,
                // エラー時の表面色
                ERROR_FILL_COLOR: "orangered"
            },
            // 論理モード時
            Logical: {
                FILL_COLOR: "lavender",
                STROKE_COLOR: "blue",
                ERROR_STROKE_COLOR: "red",
                // 選択時の色
                Active: {
                    STROKE_WIDTH: 2
                },
                // 透過表現関連
                Trans: {
                    LINE_DASH_STYLE: [4, 4]
                },
                // ポートの矩形
                Port: {
                    NAME: "LineCard",
                    WIDTH: 50,
                    HEIGHT: 20,
                    STROKE_COLOR: "blue",
                    STROKE_WIDTH: 1,
                    // フォント関連
                    Font: {
                        NAME: "12px Century Gothic",
                        FILL_COLOR: "blue",
                        STROKE_COLOR: "lightgrey",
                        STROKE_WIDTH: 0,
                        Active: {
                            FILL_COLOR: "blue"
                        }
                    },
                    // 選択時の色
                    Active: {
                        STROKE_COLOR: "blue",
                        LINE_DASH_STYLE: [4, 4],
                        STROKE_WIDTH: 2
                    }
                }
            },
            // 物理モード時
            Physical: {
                FILL_COLOR: "whitesmoke",
                STROKE_COLOR: "grey",
                ERROR_STROKE_COLOR: "red",
                // 選択時の色
                Active: {
                    STROKE_WIDTH: 2
                },
                // 透過表現関連
                Trans: {
                    LINE_DASH_STYLE: [4, 4]
                },
                // ポートの矩形
                Port: {
                    NAME: "LineCard",
                    WIDTH: 50,
                    HEIGHT: 20,
                    STROKE_COLOR: "grey",
                    STROKE_WIDTH: 1,
                    // フォント関連
                    Font: {
                        NAME: "12px Century Gothic",
                        FILL_COLOR: "grey",
                        STROKE_COLOR: "lightgrey",
                        STROKE_WIDTH: 0,
                        Active: {
                            FILL_COLOR: "grey"
                        }
                    },
                    // 選択時の色
                    Active: {
                        STROKE_COLOR: "grey",
                        LINE_DASH_STYLE: [4, 4],
                        STROKE_WIDTH: 2
                    }
                }
            }
        },
        //
        // デバイス
        //
        Device: {
            HEIGHT: 30,
            DEPTH: 200,
            MARGIN_X: 60,  // スライスとデバイスの隙間（左右）
            MARGIN_Y: 15,  // スライスとデバイスの隙間（上下）
            CLEARANCE: 0,  // デバイス同士の隙間
            PADDING: 0,    // デバイスとスイッチの隙間（左右）
            // クラスター奥からのDepth軸座標
            SPINE_DEPTH: 0,
            LEAF_DEPTH: 150,
            MINIMUM_PORT_COUNT: 4,
            // CP接続線
            ConnectionPoint:{
                // 髭の長さ
                LENGTH: 15,
                // 表示する髭の最大数
                MAX_CP_DISP_COUNT:10,
                // 髭の色
                STROKE_COLOR: "dimgray",
                // 髭の太さ
                STROKE_WIDTH: 1,
                // エラー時の表面色
                ERROR_FILL_COLOR: "orangered"
            }
        },
        //
        // スイッチ
        //
        Switch: {
            WIDTH: 50,
            HEIGHT: 10,
            DEPTH: 38,
            MARGIN: 18,
            // 論理モード時
            Logical: {
                FILL_COLOR: "lavender",
                STROKE_COLOR: "blue",
                LINE_DASH_STYLE: [],
                STROKE_WIDTH: 1,
                // 選択時の色
                Active: {
                    FILL_COLOR: "lavender",
                    STROKE_COLOR: "blue",
                    LINE_DASH_STYLE: [4, 4],
                    STROKE_WIDTH: 3
                },
                // フォント関連
                Font: {
                    NAME: "11px Century Gothic",
                    FILL_COLOR: "blue",
                    STROKE_COLOR: "lightgrey",
                    STROKE_WIDTH: 0,
                    Active: {
                        FILL_COLOR: "blue"
                    }
                }
            },
            // 物理モード時
            Physical: {
                FILL_COLOR: "whitesmoke",
                STROKE_COLOR: "grey",
                LINE_DASH_STYLE: [],
                STROKE_WIDTH: 1,
                // 選択時の色
                Active: {
                    FILL_COLOR: "whitesmoke",
                    STROKE_COLOR: "grey",
                    LINE_DASH_STYLE: [4, 4],
                    STROKE_WIDTH: 3
                },
                // フォント関連
                Font: {
                    NAME: "11px Century Gothic",
                    FILL_COLOR: "grey",
                    STROKE_COLOR: "lightgrey",
                    STROKE_WIDTH: 0,
                    Active: {
                        FILL_COLOR: "grey"
                    }
                }
            }
        },
        //
        // 接続線
        //
        Line: {
            STROKE_COLOR: "darkgrey",
            STROKE_WIDTH: 1,
            // 選択時の色
            Active: {
                STROKE_COLOR: "red",
                STROKE_WIDTH: 2
            }
        },
        //
        // トラヒック
        //
        Traffic: {
            // 取得間隔(分) トラヒック値の取得間隔を指定
            TRAFFIC_INTERVAL: 10,
            // 取得期間(分) トラヒック値の取得期間を指定
            // 画面上で入力した取得開始日時からここで指定した時間分過去に遡った期間になります
            TRAFFIC_SPAN: 10,
            // 描画更新時間 30～ ミリ秒
            DRAWING_UPDATE_TIME: 30,
            // 描画出力（1～100）％ 描画更新時間が30の場合は200％まで可能
            DRAWING_POWER: 24,
            // 描画領域
            DrawArea: {
                HEIGHT: 100
            },
            STROKE_WIDTH: 1,
            // 線上クリック判定の精度0.1～
            HIT_ACCURACY: 4,
            // 選択時
            Active: {
//                 STROKE_WIDTH: 5,
            },
            // トラヒック速度に対応する線種定義(可変)
            // 速度の低い順に定義する
            // 定義した最後の速度が破線の流動速度の最大となる（それ以上早くならない）
            SpeedList2: [{
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 0,
                STROKE_SPEED: 800,
                // 破線設定
                DASH_ON: 8,
                DASH_OFF: 2
//                 DASH_ON: 1,
//                 DASH_OFF: 0,
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 1,
                STROKE_SPEED: 750,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }],

            // 速度の低い順に定義する
            // 定義した最後の速度が破線の流動速度の最大となる（それ以上早くならない）
            SpeedList: [
            {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: -1,
                STROKE_COLOR: "transparent",
                STROKE_WIDTH: 1,
                STROKE_SPEED: 0,
                // 破線設定
                DASH_ON: 1,
                DASH_OFF: 0
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 0,
                STROKE_COLOR: "transparent",
                STROKE_WIDTH: 1,
                STROKE_SPEED: 0,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 0.001,
                //STROKE_COLOR: "#FFF25D",
                STROKE_COLOR: "66FF00",
                STROKE_WIDTH: 0.5,
                STROKE_SPEED: 50,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 0.100,
                STROKE_COLOR: "#FFF25D",
                //STROKE_COLOR: "#66FF00",
                STROKE_WIDTH: 0.5,
                STROKE_SPEED: 100,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 2,
                STROKE_COLOR: "#BBFF00",
                STROKE_WIDTH: 1,
                STROKE_SPEED: 150,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 5,
                STROKE_COLOR: "#FFBB00",
                STROKE_WIDTH: 1,
                STROKE_SPEED: 180,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 30,
                STROKE_COLOR: "#FF6600",
                STROKE_WIDTH: 2,
                STROKE_SPEED: 300,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 40,
                STROKE_COLOR: "#FF3300",
                STROKE_WIDTH: 2.5,
                STROKE_SPEED: 230,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }, {
                // トラヒック速度 単位：GigaBPS
                TRAFFIC_FROM: 50,
                STROKE_COLOR: "#FF0000",
                STROKE_WIDTH: 5,
                STROKE_SPEED: 500,
                // 破線設定
                DASH_ON: 12,
                DASH_OFF: 6
            }],
            // 線上矢印のサイズ
            Arrow: {
                WIDTH: 8,
                HEIGHT: 4,
                VISIBLE: false
            }
        }
    },
    //
    // コントローラ状態取得設定
    //
    ControllerInfo: {
        // CPU使用率
        CPU_USAGE: 80
    },

    // 十字キーの利用
    SCROLL_ENABLE: false,

    // 十字キーの移動サイズ
    SCROLL_SIZE: {
        TOP: -100,
        LEFT: 100,
        BOTTOM: 100,
        RIGHT: -100
    }
};
