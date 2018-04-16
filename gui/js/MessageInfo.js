//
// メッセージクラス
// 設計書の画面表示メッセージ一覧と対応している
//
(function() {
    //
    // 処理別メッセージ一覧
    // メッセージ一覧.xlsxの処理別メッセージ一覧シートからマクロ生成したものを貼り付けて管理する（予定）
    //
    MSF.MessageInfo = {
//        Login: {
//            login:{
//                AllError: "Username or password is incorrect.",
//                Output: "ログイン画面",
//            },
//        },
        Polling: {
            getSwClusters: {
                operationName: "GET &lt;clusters list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getNodes: {
                operationName: "GET &lt;node list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getL2Slices: {
                operationName: "GET &lt;slice list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getL3Slices: {
                operationName: "GET &lt;slice list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getEdgePoints: {
                operationName: "GET &lt;edge point list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getSliceIDListForCP: {
                operationName: "GET &lt;CP list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getCPList: {
                operationName: "GET &lt;CP list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getEquipments: {
                operationName: "GET &lt;equipment model information list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getInterfaceList: {
                operationName: "GET &lt;interface list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getClusterLinkIfs: {
                operationName: "GET &lt;cluster link if list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getIfTraffics: {
                operationName: "GET &lt;if traffic list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getCpTraffics: {
                operationName: "GET &lt;cp traffic list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getInterfaces: {
                operationName: "GET &lt;if list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getFailureStatus: {
                operationName: "GET &lt;failure status list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            getSystemStatus: {
                operationName: "GET &lt;status list&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            }
        },
        Traffic: {
            getPresent: {
                operationName: "GET &lt;traffic&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            },
            // 未使用
            getPast: {
                operationName: "GET &lt;traffic&gt;",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "{ResponseErrorMessage}",
                Output: "ログエリア"
            }
        },
        AddModel: {
            /* 未使用
            getConfig: {
                NotSpecified: "Equipment model name is not specified.",
                NotFound: "No match for specified equipment model name.",
                Output: "機種情報登録パネル"
            },
            */
            "add": {
                // ID追加
                Success: "ADD &lt;equipment model information&gt; has been completed. (Equipment type ID = {equipment_type_id})",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;equipment model information&gt; failed. {ResponseErrorMessage}",
                Output: "機種情報登録パネル"
            }
        },
        DeleteModel: {
            searchModel: {
                NotFound: "No match for specified feature.",
                Output: "機種選択パネル"
            },
            selectModel: {
                NotSelected: "Equipment model is not specified.",
                Output: "機種選択パネル"
            },
            "delete": {
                Success: "DELETE &lt;equipment model information&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "DELETE &lt;equipment model information&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        AddSwCluster: {
            "add": {
                // ID追加
                Success: "REQUEST &lt;ADD sw cluster&gt; has been completed. (Cluster = #{cluster_id})",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;sw cluster&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "ADD &lt;sw cluster&gt; has been completed. (Cluster = #{cluster_id})",
                ConnectError: "ADD &lt;sw cluster&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "ADD &lt;sw cluster&gt; has not been confirmed. Connection timeout.",
                ResponseError: "ADD &lt;sw cluster&gt; has not been confirmed. (Cluster = #{cluster_id}) {ResponseErrorMessage}",
                CheckFailed: "ADD &lt;sw cluster&gt; failed. (Leaf = #{node_id}) {ResponseErrorMessage}",
                CheckCanceled: "ADD &lt;sw cluster&gt; was canceled by controller. (Cluster = #{cluster_id})",
                CheckTimeout: "ADD &lt;sw cluster&gt; has not been confirmed. Timeout. (Cluster = #{cluster_id})",
                Output: "登録系共通パネル"
            }
        },
        DeleteSwCluster: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE sw cluster&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE sw cluster&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE sw cluster&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;sw cluster&gt; has been completed. (Cluster = #{cluster_id})",
                ConnectError: "DELETE &lt;sw cluster&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;sw cluster&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;sw cluster&gt; has not been confirmed. (Cluster = #{cluster_id}) {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;sw cluster&gt; failed. (Cluster = #{cluster_id}) {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;sw cluster&gt; was canceled by controller. (Cluster = #{cluster_id})",
                CheckTimeout: "DELETE &lt;sw cluster&gt; has not been confirmed. Timeout. (Cluster = #{cluster_id})",
                Output: "登録系共通パネル"
            }
        },
        AddLeaf: {
            searchModel: {
                NotFound: "No match for specified feature.",
                Output: "機種選択パネル"
            },
            selectModel: {
                NotSelected: "Equipment model is not specified.",
                Output: "機種選択パネル"
            },
            "add": {
                // ID追加
                Success: "ADD &lt;leaf&gt; has been started async process.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;leaf&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        DeleteLeaf: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE leaf&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE leaf&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE leaf&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;leaf&gt; has been completed. (Leaf = #{node_id})",
                ConnectError: "DELETE &lt;leaf&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;leaf&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;leaf&gt; has not been confirmed. (Leaf = #{node_id}) {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;leaf&gt; failed. (Leaf = #{node_id}) {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;leaf&gt; was canceled by controller. (Leaf = #{node_id})",
                CheckTimeout: "DELETE &lt;leaf&gt; has not been confirmed. Timeout. (Leaf = #{node_id})",
                Output: "登録系共通パネル"
            }
        },
        ModifyLeaf: {
            "modify": {
                Success: "MODIFY &lt;leaf&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "MODIFY &lt;leaf&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "MODIFY &lt;leaf&gt; has been completed. (Leaf = #{node_id})",
                ConnectError: "MODIFY &lt;leaf&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "MODIFY &lt;leaf&gt; has not been confirmed. Connection timeout.",
                ResponseError: "MODIFY &lt;leaf&gt; has not been confirmed. (Leaf = #{node_id}) {ResponseErrorMessage}",
                CheckFailed: "MODIFY &lt;leaf&gt; failed. (Leaf = #{node_id}) {ResponseErrorMessage}",
                CheckCanceled: "MODIFY &lt;leaf&gt; was canceled by controller. (Leaf = #{node_id})",
                CheckTimeout: "MODIFY &lt;leaf&gt; has not been confirmed. Timeout. (Leaf = #{node_id})",
                Output: "登録系共通パネル"
            }
        },
        AddSpine: {
            searchModel: {
                NotFound: "No match for specified feature.",
                Output: "機種選択パネル"
            },
            selectModel: {
                NotSelected: "Equipment model is not specified.",
                Output: "機種選択パネル"
            },
            "add": {
                // ID追加
                Success: "ADD &lt;spine&gt; has been started async process.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;spine&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        DeleteSpine: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE spine&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE spine&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE spine&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;spine&gt; has been completed. (Spine = #{node_id})",
                ConnectError: "DELETE &lt;spine&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;spine&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;spine&gt; has not been confirmed. (Spine = #{node_id}) {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;spine&gt; failed. (Spine = #{node_id}) {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;spine&gt; was canceled by controller. (Spine = #{node_id})",
                CheckTimeout: "DELETE &lt;spine&gt; has not been confirmed. Timeout. (Spine = #{node_id})",
                Output: "登録系共通パネル"
            }
        },
        ModifyPhysicalIF: {
            "modify": {
                Success: "MODIFY &lt;physical IF&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "MODIFY &lt;physical IF&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        AddBreakoutIF: {
            "add": {
                ConnectError: "REQUEST &lt;ADD breakout IF&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;ADD breakout IF&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;ADD breakout IF&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "ADD &lt;breakout IF&gt; has been completed. (BreakoutIF ID = {breakout_if_ids})",
                ConnectError: "ADD &lt;breakout IF&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "ADD &lt;breakout IF&gt; has not been confirmed. Connection timeout.",
                ResponseError: "ADD &lt;breakout IF&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "ADD &lt;breakout IF&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "ADD &lt;breakout IF&gt; was canceled by controller.",
                CheckTimeout: "ADD &lt;breakout IF&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        DeleteBreakoutIF: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE breakout IF&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE breakout IF&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE breakout IF&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;breakout IF&gt; has been completed.",
                ConnectError: "DELETE &lt;breakout IF&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;breakout IF&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;breakout IF&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;breakout IF&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;breakout IF&gt; was canceled by controller.",
                CheckTimeout: "DELETE &lt;breakout IF&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        AddLagIF: {
            "add": {
                ConnectError: "REQUEST &lt;ADD lag IF&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;ADD lag IF&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;ADD lag IF&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "ADD &lt;lag IF&gt; has been completed. (LagIF ID = {lag_if_id})",
                ConnectError: "ADD &lt;lag IF&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "ADD &lt;lag IF&gt; has not been confirmed. Connection timeout.",
                ResponseError: "ADD &lt;lag IF&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "ADD &lt;lag IF&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "ADD &lt;lag IF&gt; was canceled by controller.",
                CheckTimeout: "ADD &lt;lag IF&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        DeleteLagIF: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE lag IF&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE lag IF&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE lag IF&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;lag IF&gt; has been completed. (LagIF ID = {lag_if_id})",
                ConnectError: "DELETE &lt;lag IF&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;lag IF&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;lag IF&gt; has not been confirmed. (LagIF ID = {lag_if_id}) {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;lag IF&gt; failed. (LagIF ID = {lag_if_id}) {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;lag IF&gt; was canceled by controller. (LagIF ID = {lag_if_id})",
                CheckTimeout: "DELETE &lt;lag IF&gt; has not been confirmed. Timeout. (LagIF ID = {lag_if_id})",
                Output: "登録系共通パネル"
            }
        },
        AddClusterLinkIfs:{
            "add": {
                Success: "ADD &lt;cluster link ifs&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;cluster link ifs&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "ADD &lt;cluster link ifs&gt; has been completed.",
                ConnectError: "ADD &lt;cluster link ifs&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "ADD &lt;cluster link ifs&gt; has not been confirmed. Connection timeout.",
                ResponseError: "ADD &lt;cluster link ifs&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "ADD &lt;cluster link ifs&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "ADD &lt;cluster link ifs&gt; was canceled by controller.",
                CheckTimeout: "ADD &lt;cluster link ifs&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        DeleteClusterLinkIfs: {
            "delete": {
                Success: "DELETE &lt;cluster link ifs&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "DELETE &lt;cluster link ifs&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;cluster link ifs&gt; has been completed.",
                ConnectError: "DELETE &lt;cluster link ifs&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;cluster link ifs&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;cluster link ifs&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;cluster link ifs&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;cluster link ifs&gt; was canceled by controller.",
                CheckTimeout: "DELETE &lt;cluster link ifs&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        AddEdgePoint: {
            "add": {
                // ID追加
                Success: "ADD &lt;edge point&gt; has been completed. (EdgePoint ID = {edge_point_id})",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "ADD &lt;edge point&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        DeleteEdgePoint: {
            "delete": {
                Success: "DELETE &lt;edge point&gt; has been completed.",
                ConnectError: "Connection refused.",
                ConnectTimeout: "Connection timeout.",
                ResponseError: "DELETE &lt;edge point&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        AddSlice: {
            "add": {
                Success: "ADD &lt;slice&gt; has been completed. (Slice ID = {slice_id})",
                ConnectError: "ADD &lt;slice&gt; failed. Connection refused.",
                ConnectTimeout: "ADD &lt;slice&gt; failed. Connection timeout.",
                ResponseError: "ADD &lt;slice&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        ModifySlice: {
            "modify": {
                Success: "MODIFY &lt;slice&gt; has been completed.",
                ConnectError: "MODIFY &lt;slice&gt; failed. Connection refused.",
                ConnectTimeout: "MODIFY &lt;slice&gt; failed. Connection timeout.",
                ResponseError: "MODIFY &lt;slice&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "MODIFY &lt;slice&gt; has been completed.",
                ConnectError: "MODIFY &lt;slice&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "MODIFY &lt;slice&gt; has not been confirmed. Connection timeout.",
                ResponseError: "MODIFY &lt;slice&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "MODIFY &lt;slice&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "MODIFY &lt;slice&gt; was canceled by controller.",
                CheckTimeout: "MODIFY &lt;slice&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        DeleteSlice: {
            "delete": {
                Success: "DELETE &lt;slice&gt; has been completed.",
                ConnectError: "DELETE &lt;slice&gt; failed. Connection refused.",
                ConnectTimeout: "DELETE &lt;slice&gt; failed. Connection timeout.",
                ResponseError: "DELETE &lt;slice&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            }
        },
        AddCP: {
            "add": {
                ConnectError: "REQUEST &lt;ADD CP&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;ADD CP&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;ADD CP&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "ADD &lt;CP&gt; has been completed.(CP ID = {cp_ids})",
                ConnectError: "ADD &lt;CP&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "ADD &lt;CP&gt; has not been confirmed. Connection timeout.",
                ResponseError: "ADD &lt;CP&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "ADD &lt;CP&gt; failed. (CP ID = {failure_path_list}) {ResponseErrorMessage}",
                CheckCanceled: "ADD &lt;CP&gt; was canceled by controller.",
                CheckTimeout: "ADD &lt;CP&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        ModifyCP: {
            "modify": {
                Success: "MODIFY &lt;CP&gt; has been completed.",
                ConnectError: "MODIFY &lt;CP&gt; failed. Connection refused.",
                ConnectTimeout: "MODIFY &lt;CP&gt; failed. Connection timeout.",
                ResponseError: "MODIFY &lt;CP&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "MODIFY &lt;CP&gt; has been completed.",
                ConnectError: "MODIFY &lt;CP&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "MODIFY &lt;CP&gt; has not been confirmed. Connection timeout.",
                ResponseError: "MODIFY &lt;CP&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "MODIFY &lt;CP&gt; failed. {ResponseErrorMessage}",
                CheckCanceled: "MODIFY &lt;CP&gt; was canceled by controller.",
                CheckTimeout: "MODIFY &lt;CP&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        },
        DeleteCP: {
            "delete": {
                ConnectError: "REQUEST &lt;DELETE CP&gt; failed. Connection refused.",
                ConnectTimeout: "REQUEST &lt;DELETE CP&gt; failed. Connection timeout.",
                ResponseError: "REQUEST &lt;DELETE CP&gt; failed. {ResponseErrorMessage}",
                Output: "登録系共通パネル"
            },
            "check": {
                Success: "DELETE &lt;CP&gt; has been completed.",
                ConnectError: "DELETE &lt;CP&gt; has not been confirmed. Connection refused.",
                ConnectTimeout: "DELETE &lt;CP&gt; has not been confirmed. Connection timeout.",
                ResponseError: "DELETE &lt;CP&gt; has not been confirmed. {ResponseErrorMessage}",
                CheckFailed: "DELETE &lt;CP&gt; failed. (CP ID = {failure_path_list}) {ResponseErrorMessage}",
                CheckCanceled: "DELETE &lt;CP&gt; was canceled by controller.",
                CheckTimeout: "DELETE &lt;CP&gt; has not been confirmed. Timeout.",
                Output: "登録系共通パネル"
            }
        }
    }
    ;

    //
    // 応答エラーメッセージ一覧
    // メッセージ一覧.xlsxの(別紙)応答エラーメッセージ一覧シートからマクロ生成したものを貼り付けて管理する
    //
    MSF.MessageDic = {
        "500": {
            "000001": "System status error.",
            "000002": "Operation canceled.",
            "020001": "Database connection error.",
            "020002": "Database operation error.",
            "020004": "Database transaction error.",
            "030003": "Related resource not found.",
            "030004": "Register information error.",
            "030005": "Update information error.",
            "030006": "Delete information error.",
            "030007": "Transition status error.",
            "040001": "File read error.",
            "040002": "File write error.",
            "040003": "Execute file error.",
            "040004": "File delete error.",
            "050001": "EC connection error.",
            "050002": "EC control error.",
            "050004": "EC control timeout.",
            "060001": "FC connection error.",
            "060002": "FC control error.",
            "060003": "FC control timeout.",
            "300001": "Cluster control error.",
            "900001": "FC, EC control error. (EM control completed.)",
            "900002": "FC control error. (EC, EM control completed.)",
            "900003": "EC control error. (FC, EM control completed.)",
            "900004": "FC control error. (EC control completed.)",
            "900005": "MFC control error. (FC, EC, EM control completed.)",
            "900006": "MFC control error. (FC, EC control completed.)",
            "900007": "MFC control error. (FC control completed.)",
            "990001": "Undefined error.",
            "990002": "System check status error."
        },
        "400": {
            "010001": "Parameter format error.",
            "010002": "Parameter value error.",
            "010003": "Parameter value out of range."
        },
        "409": {
            "020003": "Exclusive control error.",
            "030002": "Target resource already exist."
        },
        "404": {
            "030001": "Target resource not found."
        }
    }
    ;
})();
