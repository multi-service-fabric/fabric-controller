//
// MSF入力エリアクラス
//
(function () {
    "use strict";
    //
    // コンストラクタ
    MSF.MSFjsonForm = function (rest) {
        this.rest = rest;
    }
    ;

    //生成したjson dataの変換
    MSF.MSFjsonForm.prototype.jsonSet = function (genparm, genjson, processID) {

        var obj = {
            parmData:[],
            jsonData:[]
        };

        obj.parmData = {};
        obj.parmData = (new Function("return " + genparm))();

        obj.jsonData = {};
        obj.jsonData = (new Function("return " + genjson))();

        return obj;
    }
    ;

    //
    // HTML生成 & 引継ぎデータセット
    //   processID : 画面ID
    //   transfer  : 引継ぎオブジェクト
    //                 vpnType         : VPNタイプ
    //                 sliceID         : スライスID
    //                 sliceType       : スライスタイプ
    //                 equipmentTypeID : 機種ID
    //                 nodeID          : 装置ID(Leaf,Spine共通)
    //                 leafNodeID      : Leaf装置ID(edge-point登録)
    //   modifyJsonParam
    //             : config定義されたjsonParamを加工するメソッド。未指定の場合は加工しません。
    //                 引数  ：config定義のjsonParamのコピー
    //                 戻り値：なし（引数のjsonParamを直接変更してください）
    //
    MSF.MSFjsonForm.prototype.htmlGeneration = function (processID, transfer, modifyJsonParam) {

        var dispData = "";
        var dispHtml = "";
        var dispHtmlParm = "";
        var jsonParm = {};
        var title = "";
        var genParm = "";                   //json data 生成用(URLリクエストパラメータ)
        var genJson = "";                   //json data 生成用
        var genchildJson = "";              //json data 生成用(子object)
        var svName = new Array(10);         //key name save

        //メッセージエリアクリア
        $("#panelAlert").hide();

        // 前回分の要素を削除
        $("#panelBody").children().remove();

        var param_object = document.getElementById("restPanelTitle");
        while (param_object.firstChild) param_object.removeChild(param_object.firstChild);

        var prm = this.getParam(processID, transfer);
        jsonParm = prm.jsonParm;
        title = prm.title;

        var thisParm = false;
        var thisName = "";
        var thisCaption = "";
        var thisRepeatCount = 1;
        var thisType = "";
        var thisVisible = true;
        var thisReadOnly = false;
        var thisValue = null;
        var thisCandidate = null;
        var thisNewLine = 0;
        var thischild = 0;
        var sRepeat = 0;                            //繰り返しの判定用
        var svRepeatCount = 0;                      //繰り返しの判定用
        var svchild = 0;
        var svlabel = 0;

        this.clr(0, svName);

        if (processID == "P010401" || 
            processID == "P010501" || 
            processID == "P010901_add" || 
            processID == "P010901_delete" || 
            processID == "P020203" || 
            processID == "P020201_add_l2" || 
            processID == "P020201_add_l3" || 
            processID == "P020201_delete") {
            genParm = JSON.stringify(MSF.Json[processID].PARM);
            genJson = JSON.stringify(MSF.Json[processID].JSON);

            dispHtml = MSF.Html[processID].BODY;
            dispHtmlParm = MSF.Html[processID].PARM;
            if (modifyJsonParam !== undefined){
                dispHtml = modifyJsonParam(dispHtml);
            }
        } else {
            var getcollection = function (o, n) {

                n = n ? n : [];
                if (typeof o != "object") {

                    var count = this.indexofcount(n, "children");
                    thischild = count;
                    var captionCount = this.indexofcount(n, "Caption");

                    dispData += n + " : " + o + " : " + count;
                    if (captionCount !== 0) {
                        var sp = this.spaceadd(o, count);
                        dispData += " [" + sp + "]";
                    }
                    dispData += "\n";
                    var w = String(n).split(",");
                           switch (w[w.length - 1]) {
                           case "UrlParam":
                               thisParm = o;
                               break;
                           case "Name":
                               if (thisParm === true) {
                                   thisName = o;
                               } else {
                                   this.clr(count, svName);
                                   thisName = "";
                                   for (i = 0; i < count; i++) {
                                       thisName += svName[i] + ".";
                                   }
                                   thisName += o;
                                   svName[count] = o;
                               }
                               break;
                           case "Caption":
                               if (thisParm === true) {
                                   thisCaption = o;
                               } else {
                                   if (sRepeat > 0) {
                                       thisCaption = o;
                                   } else {
                                       thisCaption = this.spaceadd(o, count);
                                   }
                               }
                               break;
                           case "repeatCount":
                               if (thisParm === true) {
                                   thisRepeatCount = 1;
                               } else {
                                   thisRepeatCount = o;
                               }
                               break;
                           case "Type":
                               thisType = o;
                               break;
                           case "Visible":
                               thisVisible = o;
                               break;
                           case "Readonly":
                               thisReadOnly = o;
                               break;
                           case "Value":
                               thisValue = o;
                               break;
                           case "Candidate":
                               // 候補表示
                               thisCandidate = o;
                               break;
                           case "NewLine":
                               thisNewLine = o;
                               var text;
                               if (thisParm === true) {
                                   //HTML生成
                                   text = this.textadd(thisName, thisCaption, thisRepeatCount, sRepeat, thisNewLine, thisParm, thisVisible, thisReadOnly, thisValue, thisType, thisCandidate);
                                   dispHtmlParm += text.str + "\n";
                                   genParm += text.genParm;
                                   genJson += text.genJson;
                                   genchildJson += text.genchildJson;
                               } else {
                                   if (svlabel > count) {
                                       if (sRepeat === 0) {
                                           genJson += " }, \n";
                                       }
                                   }
                                   svlabel = count;

                                   if (thisRepeatCount !== 1) {
                                       if (thisType == "label") {
                                           sRepeat += 1;
                                           svchild = count;
                                           svRepeatCount = thisRepeatCount;
                                       }
                                   }
                                   if (sRepeat > 0) {
                                       if (svchild <= count) {
                                           svchild = count;
                                       }
                                       if (svchild > count) {
                                           dispHtml += "</span>\n</li>\n</ul>\n";
                                           sRepeat -= 1;
                                           genJson += " [ \n";
                                               for (i = 0; i < svRepeatCount; i++) {
                                                   genJson += genchildJson + " }, \n";
                                               }
                                           genJson += "] , \n";
                                           genchildJson = "";
                                           svRepeatCount = 0;
                                       }
                                   }
                                   //HTML生成
                                   switch (thisType) {
                                   case "label":
                                       var label = this.labeladd(thisName, thisCaption, thisRepeatCount, thisNewLine, thisVisible);
                                       dispHtml += label.str + "\n";
                                       genJson += label.genJson;
                                       genchildJson += label.genchildJson;
                                       break;
                                   case "checkbox":
                                       var checkbox = this.checkboxadd(thisName, thisCaption, thisRepeatCount, sRepeat, thisNewLine, thisVisible, thisReadOnly, thisValue);
                                       dispHtml += checkbox.str + "\n";
                                       genJson += checkbox.genJson;
                                       genchildJson += checkbox.genchildJson;
                                       break;
                                   default:
                                       text = this.textadd(thisName, thisCaption, thisRepeatCount, sRepeat, thisNewLine, thisParm, thisVisible, thisReadOnly, thisValue, thisType, thisCandidate);
                                       dispHtml += text.str + "\n";
                                       genParm += text.genParm;
                                       genJson += text.genJson;
                                       genchildJson += text.genchildJson;
                                       break;
                                   }
                               }
                               break;
                           default:
                               break;
                           }
                } else {
                    for (var i in o) getcollection(o[i], n.concat(i));
                }
            }.bind(this);

            getcollection(jsonParm);

            //最後の編集
            if (sRepeat > 0) {
                dispHtml += "</span>\n</li>\n</ul>\n";
                sRepeat = 0;
                genJson += " [ \n";
                for (i = 0; i < svRepeatCount; i++) {
                    genJson += genchildJson + " }, \n";
                }
                genJson += "] , \n";
                svRepeatCount = 0;

                // 中カッコの数を無理やり合わせましょう。。。
                var kakkoStartCount = this.indexofcount(genJson, "{");
                var kakkoEndCount = this.indexofcount(genJson, "}");
                while(kakkoStartCount > kakkoEndCount) {
                    genJson = genJson + " }\n";
                    kakkoEndCount = kakkoEndCount + 1;
                }

                // 全体を囲みましょう。
                genJson = "{\n" + genJson + " }";
            } else {
                if (thischild === 0) {
                    genJson = "{\n" + genJson + " }";
                } else {
                    genJson = "{\n" + genJson + " }\n}";
                }
            }
            genParm = "{\n" + genParm + " }";
        }

        this.divadd(dispHtmlParm, "params", title);
        this.divadd(dispHtml, "details");
        
        var jsonobj = this.jsonSet(genParm, genJson, processID);

        //初期データの設定
        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)

        // 選択値から取得
        this.username = MSF.Conf.Rest.MFC.USERNAME;                     // 固定値
        this.password = MSF.Conf.Rest.MFC.PASSW0RD;                     // 固定値
        this.ntp_server_address = MSF.Conf.Rest.MFC.NTP_SERVER_ADDRESS; // 固定値
        this.snmp_community = MSF.Conf.Rest.MFC.SNMP_COMMUNITY;         // 固定値

        this.cluster_id = "";                                       // 選択値 クラスタID
        if (transfer.clusterID) this.cluster_id = transfer.clusterID;

        this.fabric_type = MSF.Const.FabricType.Leafs;              // 選択値 Const
        if (transfer.fabricType) this.fabric_type = transfer.fabricType;

        this.slice_id = "";                                         // 選択値 スライスID
        if (transfer.sliceID) this.slice_id = transfer.sliceID;

        this.equipment_type_id = "";                                // 選択値 機種ID
        if (transfer.equipmentTypeID) this.equipment_type_id = transfer.equipmentTypeID;

        this.leaf_node_id = "";                                     // 選択値 LeafID
        if (transfer.leafNodeID) this.leaf_node_id = transfer.leafNodeID;

        this.node_id = "";                                          // 選択値 装置ID(Leaf,Spine共通))
        if (transfer.nodeID) this.node_id = transfer.nodeID;

        this.slice_type = "";                                       // 選択値 スライスタイプ
        if (transfer.sliceType) this.slice_type = transfer.sliceType;

        var vpn_type = "";                                          // 選択値 VPNタイプ
        if (transfer.vpnType) vpn_type = transfer.vpnType;

        var linkID = "";                                            // 選択値 クラスタ間リンクID
        if (transfer.linkID) linkID = transfer.linkID;

        // cluster_id
        if (jsonobj.parmData.cluster_id !== undefined) {
            jsonobj.parmData.cluster_id = this.cluster_id;
        }
        //equipment_type_id
        if (jsonobj.parmData.equipment_type_id !== undefined) {
            jsonobj.parmData.equipment_type_id = this.equipment_type_id;
        }
        //fabric_type
        if (jsonobj.parmData.fabric_type !== undefined) {
            jsonobj.parmData.fabric_type = this.fabric_type;
        }
        //leaf_node_id
        if (jsonobj.parmData.leaf_node_id !== undefined) {
            jsonobj.parmData.leaf_node_id = this.leaf_node_id;
        }
        //node_id
        if (jsonobj.parmData.node_id !== undefined) {
            jsonobj.parmData.node_id = this.node_id;
        }
        //slice_id
        if (jsonobj.parmData.slice_id !== undefined) {
            jsonobj.parmData.slice_id = this.slice_id;
        }
        //slice_type
        if (jsonobj.parmData.slice_type !== undefined) {
            jsonobj.parmData.slice_type = this.slice_type;
        }
        //vpn_type
        if (jsonobj.parmData.vpn_type !== undefined) {
            jsonobj.parmData.vpn_type = vpn_type;
        }
        //linkID
        if (jsonobj.parmData.cluster_link_if_id !== undefined) {
            jsonobj.parmData.cluster_link_if_id = linkID;
        }
        $("#params").jsForm({
            data: jsonobj.parmData
        });
        
        if (processID == "P010401" || processID == "P010501") {
            jsonobj.jsonData.username = this.username;
            jsonobj.jsonData.password = this.password;
            jsonobj.jsonData.snmp_community = this.snmp_community;
            jsonobj.jsonData.ntp_server_address = this.ntp_server_address;
        }

        //equipment_type_id
        if (jsonobj.jsonData.equipment_type_id !== undefined) {
            jsonobj.jsonData.equipment_type_id = this.equipment_type_id;
        }
        //fabric_type
        if (jsonobj.jsonData.fabric_type !== undefined) {
            jsonobj.jsonData.fabric_type = this.fabric_type;
        }
        //leaf_node_id
        if (jsonobj.jsonData.leaf_node_id !== undefined) {
            jsonobj.jsonData.leaf_node_id = this.leaf_node_id;
        }
        //node_id
        if (jsonobj.jsonData.node_id !== undefined) {
            jsonobj.jsonData.node_id = this.node_id;
        }
        //slice_id
        if (jsonobj.jsonData.slice_id !== undefined) {
            jsonobj.jsonData.slice_id = this.slice_id;
        }
        //slice_type
        if (jsonobj.jsonData.slice_type !== undefined) {
            jsonobj.jsonData.slice_type = this.slice_type;
        }
        //vpn_type
        if (jsonobj.jsonData.vpn_type !== undefined) {
            jsonobj.jsonData.vpn_type = vpn_type;
        }
        $("#details").jsForm({
            data: jsonobj.jsonData
        });
        
        // 国際化
        $("#panelBody").i18n();

    }
    ;

    //Jsonパラメータ取得
    //Paramater
    //processID :ID
    //transfer  :引継データ
    MSF.MSFjsonForm.prototype.getParam = function (processID, transfer) {
        var pm = {
            jsonParm: {},
            title: ""
        };

        //パラメータオブジェクト
        var jsParm = MSF.MSFjsonParam;

        switch (processID) {
            case "P010101":
                pm.jsonParm = jsParm.P010101;
                pm.title = i18nx("Form.pmtitle.P010101", "Device Add");
                break;
            case "P010104":
                pm.jsonParm = jsParm.P010104;
                pm.title = i18nx("Form.pmtitle.P010104", "Device Delete");
                break;
            case "P010201":
                pm.jsonParm = jsParm.P010201;
                pm.title = i18nx("Form.pmtitle.P010201", "SW Cluster Add");
                break;
            case "P010204":
                pm.jsonParm = jsParm.P010204;
                pm.title = i18nx("Form.pmtitle.P010204", "SW Cluster Delete");
                break;
            case "P010401":
                pm.jsonParm = jsParm.P010401;
                pm.title = i18nx("Form.pmtitle.P010401", "Leaf Add");
                break;
            case "P010404":
                pm.jsonParm = jsParm.P010404;
                pm.title = i18nx("Form.pmtitle.P010404", "Leaf Delete");
                break;
            case "P010405":
                pm.jsonParm = jsParm.P010405;
                pm.title = i18nx("Form.pmtitle.P010405", "Leaf Modify");
                break;
            case "P010501":
                pm.jsonParm = jsParm.P010501;
                pm.title = i18nx("Form.pmtitle.P010501", "Spine Add");
                break;
            case "P010504":
                pm.jsonParm = jsParm.P010504;
                pm.title = i18nx("Form.pmtitle.P010504", "Spine Delete");
                break;
            case "P010803":
                pm.jsonParm = jsParm.P010803;
                pm.title = i18nx("Form.pmtitle.P010803", "physicalIF Modify");
                break;
            case "P010901_add":
                pm.jsonParm = jsParm.P010901_add;
                pm.title = i18nx("Form.pmtitle.P010901_add", "Breakout IFs Add");
                break;
            case "P010901_delete":
                pm.jsonParm = jsParm.P010901_delete;
                pm.title = i18nx("Form.pmtitle.P010901_delete", "Breakout IFs Delete");
                break;
            case "P011101":
                pm.jsonParm = jsParm.P011101;
                pm.title = i18nx("Form.pmtitle.P011101", "LagIF Add");
                break;
            case "P011105":
                pm.jsonParm = jsParm.P011105;
                pm.title = i18nx("Form.pmtitle.P011105", "LagIF Delete");
                break;
            case "P011201":
                if (transfer.ifType == undefined) {
                    MSF.console.debug("IF type error " + transfer.ifType);
                    return false;
                } else {
                    if (transfer.ifType == MSF.Const.IfType.PhysicalIf) {
                        pm.jsonParm = jsParm.P011201_physicalIF;
                    } else if (transfer.ifType == MSF.Const.IfType.BreakoutIf) {
                        pm.jsonParm = jsParm.P011201_breakoutIF;
                    } else {
                        pm.jsonParm = jsParm.P011201_lagIF;
                    }
                }
                pm.title = i18nx("Form.pmtitle.P011201", "ClusterLinkIfs Add");
                break;
            case "P011204":
                pm.jsonParm = jsParm.P011204;
                pm.title = i18nx("Form.pmtitle.P011204", "ClusterLinkIfs Delete");
                break;
            case "P011401":
                pm.jsonParm = jsParm.P011401;
                pm.title = i18nx("Form.pmtitle.P011401", "Edge-Point Add");
                break;
            case "P011404":
                pm.jsonParm = jsParm.P011404;
                pm.title = i18nx("Form.pmtitle.P011404", "Edge-Point Delete");
                break;
            case "P020101":
                if (transfer.vpnType == undefined) {
                    MSF.console.debug("vpn type error " + transfer.vpnType);
                    return false;
                } else {
                    if (transfer.vpnType == MSF.Const.VpnType.L2) {
                        pm.jsonParm = jsParm.P020101_l2;
                    } else {
                        pm.jsonParm = jsParm.P020101_l3;
                    }
                }
                pm.title = i18nx("Form.pmtitle.P020101", "Slice Add");
                break;
            case "P020102":
                pm.jsonParm = jsParm.P020102;
                pm.title = i18nx("Form.pmtitle.P020102", "Slice Modify");
                break;
            case "P020103":
                pm.jsonParm = jsParm.P020103;
                pm.title = i18nx("Form.pmtitle.P020103", "Slice Delete");
                break;
            case "P020201_add_l2":
            case "P020201_add_l3":
                if (transfer.vpnType == undefined) {
                    MSF.console.debug("vpn type error " + transfer.vpnType);
                    return false;
                } else {
                    if (transfer.vpnType == MSF.Const.VpnType.L2) {
                        pm.jsonParm = jsParm.P020201_add_l2;
                    } else {
                        pm.jsonParm = jsParm.P020201_add_l3;
                    }
                }
                pm.title = i18nx("Form.pmtitle.P020201_add", "CP Add");
                break;
            case "P020201_delete":
                pm.jsonParm = jsParm.P020201_delete;
                pm.title =  i18nx("Form.pmtitle.P020201_delete", "CP Delete");
                break;
            case "P020203":
                pm.jsonParm = jsParm.P020203;
                pm.title =  i18nx("Form.pmtitle.P020203", "CP Modify");
                break;
            default:
                break;
        }

        return pm;
    }
    ;

    //対象文字列数取得
    //Paramater
    //str      :文字列
    //pattern  :対象文字列
    MSF.MSFjsonForm.prototype.indexofcount = function (str, pattern) {
        var count = 0;
        var pos = str.indexOf(pattern);

        while (pos !== -1) {
            count++;
            pos = str.indexOf(pattern, pos + 1);
        }

        return count;
    }
    ;

    //Space文字列追加
    //Paramater
    //str      :文字列
    //i        :Space文字数
    MSF.MSFjsonForm.prototype.spaceadd = function (str, i) {
        var strA = str;
        for (var ii = 0; ii < i; ii++) {
            strA = "&nbsp;&nbsp;" + strA;
        }
        return strA;
    }
    ;

    MSF.MSFjsonForm.prototype.divadd = function (html, name, title) {
        if (title !== undefined) {
            //panelTitleの設定
            var info = document.getElementById("restPanelTitle");
            var h1Node = document.createElement("h1");
            var textNode = document.createTextNode(title);
            h1Node.appendChild(textNode);
            info.appendChild(h1Node);
        }

        var parent_object;
        parent_object = document.getElementById("panelBody");
        parent_object.innerHTML += '<div id="' + name + '" class="panel_div"></div>';

        parent_object = document.getElementById(name);
        parent_object.innerHTML += html;
    }
    ;

    //HTML & Json Data生成[text]
    //Paramater
    //name        :フィールド名
    //caption     :見出し
    //repeatCount :繰り返し数
    //repeat      :繰り返し判定
    //newline     :改行数
    //parm        :パラメータ判定(true:URLパラメータ)
    MSF.MSFjsonForm.prototype.textadd = function (name, caption, repeatCount, repeat, newline, parm, visible, readonly, value, type, candidate) {
        var obj = {
          str:"",
          genParm:"",
          genJson:"",
          genchildJson:""
        };

        var cls = "";
        var inputtype = "text";
        if (type == "number") {
            cls = " number";
            inputtype = "number";
        }
        // 候補文字の設定
        var placeholder = "";
        if (candidate !== "") {
            placeholder = 'placeholder="' + candidate + '" ';
        }
        if (parm === true) {
            if (visible === true) {
                if (readonly === true) {
                    obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='data." + name + "' id='data." + name + "' class='panel_details panel_readonly" + cls + "' " + placeholder + "readonly/><br />";
                } else {
                    obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='data." + name + "' class='panel_details" + cls + "' " + placeholder + "/><br />";
                }
                for (var i = 0; i < newline; i++) {
                    obj.str += "<br />";
                }
            } else {
                obj.str += "<input type='" + inputtype + "' name='data." + name + "' class='panel_details_none" + cls + "' " + placeholder + "/>";
                //for (var i = 0; i < newline; i++) {
                //    obj.str += "<br />";
                //}
            }
            obj.genParm += ' "' + name + '" : "' + value + '", \n';
        } else {
            if (repeatCount !== 1) {
                var w = name.split(".");
                if (repeat > 0) {
                    obj.str = "<ul class='collection panel_label' data-field='" + w[w.length - 2] + "." + w[w.length - 1] + "'>\n<li>\n<span class='field panel_label'>\n";
                    obj.genchildJson += ' "' + w[w.length - 1] + '" : [ \n';
                    for (i = 0; i < repeatCount ; i++) {
                        obj.genchildJson += ' "' + value + '", ';
                    }
                    obj.genchildJson += '], \n';
                } else {
                    obj.str = "<ul class='collection panel_label' data-field='data." + name + "'>\n<li>\n<span class='field panel_label'>\n";
                    obj.genJson += ' "' + w[w.length - 1] + '" : [ \n';
                    for (i = 0; i < repeatCount ; i++) {
                        obj.genJson += ' "' + value + '", ';
                    }
                    obj.genJson += '], \n';
                }
                if (visible === true) {
                    if (readonly === true) {
                        obj.str += i18nx(caption, name) + ": <input type='" + inputtype + "' name='" + w[w.length - 1] + ".' class='panel_details panel_readonly" + cls + "' readonly " + placeholder + "/><br />";
                    } else {
                        obj.str += i18nx(caption, name) + ": <input type='" + inputtype + "' name='" + w[w.length - 1] + ".' class='panel_details" + cls + "' " + placeholder + "/><br />";
                    }
                    for (var i = 0; i < newline; i++) {
                        obj.str += "<br />";
                    }
                } else {
                    obj.str += "<input type='" + inputtype + "' name='" + w[w.length - 1] + ".' class='panel_details_none" + cls + "' " + placeholder + "/>";
                }
                obj.str += "</span>\n</li>\n</ul>";
            } else {
                var w = name.split(".");
                if (repeat > 0) {
                    if (visible === true) {
                        if (readonly === true) {
                            obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details panel_readonly" + cls + "' readonly " + placeholder + "/><br />";
                        } else {
                            obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details" + cls + "' " + placeholder + "/><br />";
                        }
                        for (var i = 0; i < newline; i++) {
                            obj.str += "<br />";
                        }
                    } else {
                        obj.str += "<input type='" + inputtype + "' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details_none" + cls + "' " + placeholder + "/>";
                        //for (var i = 0; i < newline; i++) {
                        //    obj.str += "<br />";
                        //}
                    }
                    obj.genchildJson += ' "' + w[w.length - 1] + '" : "' + value + '", \n';
                } else {
                    if (visible === true) {
                        if (readonly === true) {
                            obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='data." + name + "' class='panel_details panel_readonly" + cls + "' readonly " + placeholder + "/><br />";
                        } else {
                            obj.str += i18nx(caption, name) + ":<input type='" + inputtype + "' name='data." + name + "' class='panel_details" + cls + "' " + placeholder + "/><br />";
                        }
                        for (var i = 0; i < newline; i++) {
                            obj.str += "<br />";
                        }
                    } else {
                        obj.str += "<input type='" + inputtype + "' name='data." + name + "' class='panel_details_none" + cls + "' " + placeholder + "/>";
                        //for (var i = 0; i < newline; i++) {
                        //    obj.str += "<br />";
                        //}
                    }
                    obj.genJson += ' "' + w[w.length - 1] + '" : "' + value + '", \n';
                }
            }
        }

        return obj;
    }
    ;

    //HTML & Json Data生成[label]
    //Paramater
    //name        :フィールド名
    //caption     :見出し
    //repeatCount :繰り返し数
    //newline     :改行数
    MSF.MSFjsonForm.prototype.labeladd = function (name, caption, repeatCount, newline, visible) {
        var obj = {
          str:"",
          genJson:"",
          genchildJson:""
        };
        var w = name.split(".");
        if (repeatCount !== 1) {
            if (visible === true) {
                obj.str = i18nx(caption, name) + "<br />";
            }
            obj.str += "<ul class='collection panel_label' data-field='data." + name + "'>\n<li>\n<span class='field panel_label'>";
            obj.genJson += ' "' + w[w.length - 1] + '" : \n';
            obj.genchildJson += ' { ';
        } else {
            if (visible === true) {
                obj.str = i18nx(caption, name) + "<br />";
                for (var i = 0; i < newline; i++) {
                    obj.str += "<br />";
                }
            }
            obj.genJson += ' "' + w[w.length - 1] + '" : { \n';
        }

        return obj;
    }
    ;

    //HTML & Json Data生成[checkbox]
    //Paramater
    //name        :フィールド名
    //caption     :見出し
    //repeatCount :繰り返し数
    //repeat      :繰り返し判定
    //newline     :改行数
    MSF.MSFjsonForm.prototype.checkboxadd = function (name, caption, repeatCount, repeat, newline, visible, readonly, value) {
        var obj = {
          str:"",
          genJson:"",
          genchildJson:""
        };
        if (repeatCount !== 1) {
            var w = name.split(".");
            if (repeat > 0) {
                obj.str = "<ul class='collection panel_label' data-field='" + w[w.length - 2] + "." + w[w.length - 1] + "'>\n<li>\n<span class='field panel_label'>\n";
            } else {
                obj.str = "<ul class='collection panel_label' data-field='data." + name + "'>\n<li>\n<span class='field panel_label'>\n";
            }
            if (visible === true) {
                if (readonly === true) {
                    obj.str += i18nx(caption, name) + ": <input type='checkbox' name='" + w[w.length - 1] + ".' class='panel_details panel_select_checkbox panel_readonly' readonly/><br />";
                    obj.str += " readonly";
                } else {
                    obj.str += i18nx(caption, name) + ": <input type='checkbox' name='" + w[w.length - 1] + ".' class='panel_details panel_select_checkbox'/><br />";
                }
                for (var i = 0; i < newline; i++) {
                    obj.str += "<br />";
                }
            } else {
                obj.str += "<input type='checkbox' name='" + w[w.length - 1] + ".' class='panel_details_none'/>";
                //for (var i = 0; i < newline; i++) {
                //    obj.str += "<br />";
                //}
            }
            obj.str += "</span>\n</li>\n</ul>";
            obj.genJson += ' "' + w[w.length - 1] + '" : [ ';
            for (i = 0; i < repeatCount ; i++) {
                obj.genJson += ' ' + value + ', ';
            }
            obj.genJson += '], \n';
        } else {
            var w = name.split(".");
            if (repeat > 0) {
                if (visible === true) {
                    if (readonly === true) {
                        obj.str += i18nx(caption, name) + ": <input type='checkbox' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details panel_select_checkbox panel_readonly' readonly/><br />";
                    } else {
                        obj.str += i18nx(caption, name) + ": <input type='checkbox' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details panel_select_checkbox'/><br />";
                    }
                    for (var i = 0; i < newline; i++) {
                        obj.str += "<br />";
                    }
                } else {
                    obj.str += "<input type='checkbox' name='" + w[w.length - 2] + "." + w[w.length - 1] + "' class='panel_details_none'/>";
                    //for (var i = 0; i < newline; i++) {
                    //    obj.str += "<br />";
                    //}
                }
                obj.genchildJson += ' "' + w[w.length - 1] + '" : ' + value + ', \n';
            } else {
                if (visible === true) {
                    if (readonly === true) {
                        obj.str += i18nx(caption, name) + ": <input type='checkbox' name='data." + name + "' class='panel_details panel_select_checkbox panel_readonly' readonly/><br />";
                    } else {
                        obj.str += i18nx(caption, name) + ": <input type='checkbox' name='data." + name + "' class='panel_details panel_select_checkbox'/><br />";
                    }
                    for (var i = 0; i < newline; i++) {
                        obj.str += "<br />";
                    }
                } else {
                    obj.str += "<input type='checkbox' name='data." + name + "' class='panel_details_none'/>";
                }
                obj.genJson += ' "' + w[w.length - 1] + '" : ' + value + ', \n';
            }
        }

        return obj;
    }
    ;

    MSF.MSFjsonForm.prototype.clr = function (s, svName) {
        for (var i = s; i < svName.length - 1; i++) {
            svName[i] = "";
        }
    }
    ;

    //
    // 通信処理(共通) url method 取得
    //
    MSF.MSFjsonForm.prototype.urlMethod = function (id) {
        var obj = {
          id: id,
          method:"",
          url:"",
          msg:"",
          sts: 0,
          messageInfo:null,
          AsyncMessageInfo:null,
          PollingConf:null
        };

        switch (id) {
            //機種情報登録
            case "P010101":
                obj.method = "POST";
                obj.url = "/v1/equipment-types";
                obj.msg = "Model information registration";
                obj.sts = 201;
                obj.messageInfo = MSF.MessageInfo.AddModel.add;
                break;
                //機種情報削除
            case "P010104":
                obj.method = "DELETE";
                obj.url = "/v1/equipment-types/{equipment_type_id}";
                obj.msg = "Model information deletion";
                obj.sts = 204;
                obj.messageInfo = MSF.MessageInfo.DeleteModel.delete;
                break;
                //SWクラスタ増設
            case "P010201":
                obj.method = "POST";
                obj.url = this.rest.optionParamAdd("/v1/clusters");
                obj.msg = "SW cluster addition";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddSwCluster.add;
                obj.AsyncMessageInfo = MSF.MessageInfo.AddSwCluster.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddSwCluster;
                break;
                //SWクラスタ減設
            case "P010204":
                obj.method = "DELETE";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}");
                obj.msg = "SW cluster deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteSwCluster.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteSwCluster.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteSwCluster;
                break;
                //Leaf追加
            case "P010401":
                obj.method = "POST";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/leafs");
                obj.msg = "Leaf addition";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddLeaf.add;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddLeaf;
                break;
                //Leaf削除
            case "P010404":
                obj.method = "DELETE";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/leafs/{node_id}");
                obj.msg = "Leaf deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteLeaf.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteLeaf.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteLeaf;
                break;
              //Leaf変更
            case "P010405":
                obj.method = "PUT";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/leafs/{node_id}");
                obj.msg = "Leaf change";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.ModifyLeaf.modify;
                obj.AsyncMessageInfo = MSF.MessageInfo.ModifyLeaf.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.ModifyLeaf;
                break;
                //Spine追加
            case "P010501":
                obj.method = "POST";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/spines");
                obj.msg = "Spine addition";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddSpine.add;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddSpine;
                break;
                //Spine削除
            case "P010504":
                obj.method = "DELETE";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/spines/{node_id}");
                obj.msg = "Spine deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteSpine.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteSpine.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteSpine;
                break;
                //物理IF情報変更
            case "P010803":
                obj.method = "PUT";
                obj.url = "/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{if_id}";
                obj.msg = "Physical IF information change";
                obj.sts = 200;
                obj.messageInfo = MSF.MessageInfo.ModifyPhysicalIF.modify;
                break;
                //breakoutIF登録
            case "P010901_add":
                obj.method = "PATCH";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs");
                obj.msg = "breakout IF addition";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddBreakoutIF.add;
                obj.AsyncMessageInfo = MSF.MessageInfo.AddBreakoutIF.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.BreakoutIFs;
                break;
                //breakoutIF削除
            case "P010901_delete":
                obj.method = "PATCH";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs");
                obj.msg = "breakout IF deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteBreakoutIF.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteBreakoutIF.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.BreakoutIFs;
                break;
                //LagIF生成
            case "P011101":
                obj.method = "POST";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs");
                obj.msg = "LagIF generation";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddLagIF.add;
                obj.AsyncMessageInfo = MSF.MessageInfo.AddLagIF.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddLagIF;
                break;
                //LagIF削除
            case "P011105":
                obj.method = "DELETE";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}");
                obj.msg = "LagIF deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteLagIF.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteLagIF.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteLagIF;
                break;
                //クラスタ間リンクIF新設
            case "P011201":
                obj.method = "POST";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/interfaces/cluster-link-ifs");
                obj.msg = "cluster-link-ifs generation";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddClusterLinkIfs.add;
                obj.AsyncMessageInfo = MSF.MessageInfo.AddClusterLinkIfs.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddClusterLinkIfs;
                break;
                //クラスタ間リンクIF減設
            case "P011204":
                obj.method = "DELETE";
                obj.url = this.rest.optionParamAdd("/v1/clusters/{cluster_id}/interfaces/cluster-link-ifs/{cluster_link_if_id}");
                obj.msg = "cluster-link-ifs deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteClusterLinkIfs.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteClusterLinkIfs.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteClusterLinkIfs;
                break;
                //edge-point登録
            case "P011401":
                obj.method = "POST";
                obj.url = "/v1/clusters/{cluster_id}/points/edge-points";
                obj.msg = "edge-point registration";
                obj.sts = 201;
                obj.messageInfo = MSF.MessageInfo.AddEdgePoint.add;
                break;
                //edge-point削除
            case "P011404":
                obj.method = "DELETE";
                obj.url = "/v1/clusters/{cluster_id}/points/edge-points/{edge_point_id}";
                obj.msg = "edge-point deletion";
                obj.sts = 204;
                obj.messageInfo = MSF.MessageInfo.DeleteEdgePoint.delete;
                break;
                //スライス変更
            case "P020102":
                obj.method = "PUT";
                obj.url = this.rest.optionParamAdd("/v1/slices/{slice_type}/{slice_id}");
                obj.msg = "slice change";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.ModifySlice.modify;
                obj.AsyncMessageInfo = MSF.MessageInfo.ModifySlice.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.ModifySlice;
                break;
                //CP生成
            case "P020201_add":
                obj.method = "PATCH";
                obj.url = this.rest.optionParamAdd("/v1/slices/{slice_type}/{slice_id}/cps");
                obj.msg = "CP addition";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.AddCP.add;
                obj.AsyncMessageInfo = MSF.MessageInfo.AddCP.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.AddCP;
                break;
                //CP削除
            case "P020201_delete":
                obj.method = "PATCH";
                obj.url = this.rest.optionParamAdd("/v1/slices/{slice_type}/{slice_id}/cps");
                obj.msg = "CP deletion";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.DeleteCP.delete;
                obj.AsyncMessageInfo = MSF.MessageInfo.DeleteCP.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.DeleteCP;
                break;
                //CP変更
            case "P020203":
                obj.method = "PUT";
                obj.url = this.rest.optionParamAdd("/v1/slices/{slice_type}/{slice_id}/cps/{cp_id}");
                obj.msg = "CP change";
                obj.sts = 202;
                obj.messageInfo = MSF.MessageInfo.ModifyCP.modify;
                obj.AsyncMessageInfo = MSF.MessageInfo.ModifyCP.check;
                obj.PollingConf = MSF.Conf.MSFMain.Polling.ModifyCP;
                break;
            default:
                berak;
        }

        return obj;

    }
    ;
    //
    // 登録・削除処理(共通)
    //
    MSF.MSFjsonForm.prototype.condition = function (pm, dt) {
        var info = {};
        info.pm = pm;
        info.body = dt;

        // メッセージに埋め込む変数
        info.msgParam = pm;

        var id = $(':text[id="paneldeploy_id"]').val();

        // url method 取得
        var parm = this.urlMethod(id);

        // 登録・削除処理実行
        return this.rest.condition(parm.messageInfo, info, parm.url, parm.method)
        .then(function (response) {

            // 後処理
            this.afterProcess(parm.id, response);

            if (parm.AsyncMessageInfo) {
                // 非同期スライス更新OPID
                var operation_id = response.data.operation_id;
                // 非同期ポーリング登録
                this.registAsyncPolling(parm.AsyncMessageInfo, operation_id, response.msgParam, parm.PollingConf, function(){
                    // パネルを閉じる処理
                    MSF.main.closeModalPanel(false);
                }.bind(this))
                ;
            } else {
                // Waitパネル非表示
                this.closeWaitPanel();
                if (response.msgInfo.Success) {
                    // パネルを閉じる処理
                    MSF.main.closeModalPanel(false);
                    // メッセージ表示
                    MSF.main.showMessage("メッセージボックス", "", response.successMessage);
                } else {
                    // パネルを閉じる処理
                    MSF.main.closeModalPanel(true);
                }
            }
        }.bind(this))
        .catch(function(reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("登録・削除処理(共通) 最終キャッチ", reason);
        }.bind(this))
        ;
    }
    ;

    //
    // 後処理処理
    //
    MSF.MSFjsonForm.prototype.afterProcess = function (id, response) {
        var data = response.data;

        // フォールスルー(fall through)を行っているのでcaseの順番に注意
        switch(id) {
                //Leaf追加
            case "P010401":
                //Spine追加
            case "P010501":
                if (!data.node_id) {
                    data.node_id = JSON.parse(response.ajax.data).node_id;
                }
                //機種情報登録
            case "P010101":
                //edge-point登録
            case "P011401":
                response.successMessage = response.msgInfo.Success.format(data);
                break;
        }
    }
    ;

    //
    // SLICE追加処理
    //
    MSF.MSFjsonForm.prototype.addSLICE = function (pm, dt) {

        var info = {
            pm: pm,
            body: dt
        };

        // SLICE追加実行
        this.rest.createSLICE(MSF.MessageInfo.AddSlice.add, info)
        .then(function (response) {
            var slice_id = response.data.slice_id || info.body.slice_id;
            if ( !slice_id) {
                slice_id = dt.slice_id;
            }

            // SLICE追加待合せ後の正常系処理
            info.pm.slice_id = slice_id;
            info.body = {
                action: "activate",
            };
            info.msgParam = {slice_id: slice_id };

            // Waitパネル非表示
            this.closeWaitPanel();

            // SLICE生成待合せ後の正常系処理
            if (response.msgInfo.Success) {
                // パネルを閉じる処理
                MSF.main.closeModalPanel(false);
                // メッセージ表示
                response.successMessage = response.msgInfo.Success.format(info.msgParam);
                MSF.main.showMessage("メッセージボックス", "", response.successMessage);
            } else {
                // パネルを閉じる処理
                MSF.main.closeModalPanel(true);
            }
        }.bind(this))
        .catch(function (reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("SLICE追加処理 最終キャッチ", reason);
        }.bind(this));
    }
    ;

    //
    // SLICE削除処理
    //
    MSF.MSFjsonForm.prototype.delSLICE = function (pm) {

        var info = {
            pm: pm,
            body: {}
        };

        // SLICE削除実行
        this.rest.deleteSLICE(MSF.MessageInfo.DeleteSlice.delete, info)
        .then(function (response) {

            // Waitパネル非表示
            this.closeWaitPanel();
            // SLICE削除待合せ後の正常系処理
            if (response.msgInfo.Success) {
                // パネルを閉じる処理
                MSF.main.closeModalPanel(false);
                // メッセージ表示
                MSF.main.showMessage("メッセージボックス", "", response.successMessage);
            } else {
                // パネルを閉じる処理
                MSF.main.closeModalPanel(true);
            }
        }.bind(this))
        .catch(function (reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("SLICE削除処理 最終キャッチ", reason);
        }.bind(this));
    }
    ;

    //
    // CP追加処理
    //
    MSF.MSFjsonForm.prototype.addCP = function(pm, dtList) {
        var lstCre = [];
        for (var i = 0; i < dtList.length; i++) {
            var info = {
                pm: pm,
                body: dtList[i],
            };
            //CP生成のプロミス配列生成処理 繰り返し処理になる
            //lstCre.push(this.rest.createCP(MSF.MessageInfo.AddCP.add, info));
            lstCre.push(info);
        }
        // // CP生成パラレル実行
        // return Promise.all(lstCre)
        // CP生成シリアル実行
         this.serialAsyncOperateCP(MSF.MessageInfo.AddCP.add, lstCre, "createCP")
        .then(function(response) {
            // 並列処理のエラーチェック
            var errCPParamList = [];
            var isError = false;
            for (var i = 0; i < response.length; i++) {
                if (response[i].isError) {
                    isError = true;
                }
            }
            // CP生成が一つでも失敗していたら異常
            if (isError) {
                //
                // 異常系処理
                //
                MSF.console.debug("CP生成異常 ロールバック実行します", response);
                // パラメータ作成
                var lstAct = [];
                for (var i = 0; i < response.length; i++) {
                    var info = {};
                    info.pm = {
                        slice_type: pm.slice_type,
                        slice_id: pm.slice_id,
                        cp_id: null,
                    };
                    // エラーがあった場合正常に追加されたCPを削除しないといけないため、正常CPをリストアップ
                    if (!response[i].isError) {
                        info.pm.cp_id = response[i].data.cp_id;
                        // // CP削除処理 自前でエラー処理するフラグ指定
                        // lstAct.push(this.rest.deleteCP(MSF.MessageInfo.DeleteCP.delete, info, true));
                        // シリアル実行リストに追加
                        lstAct.push(info);
                    }
                }

                var backupResponse = response;
                // // CP削除パラレル実行
                // return Promise.all(lstAct)
                // CP削除シリアル実行
                return this.serialAsyncOperateCP(MSF.MessageInfo.DeleteCP.delete, lstAct, "deleteCP")
                .then(function(response) {
                    for (var i = 0; i < response.length; i++) {
                        if (response[i].isError) {
                            MSF.console.debug("CP生成異常 ロールバック異常（CP削除処理）", response[i]);
                        }
                    }

                    // backupResponseとresponseでマッチング掛ければロールバック異常のメッセージも表示できる

                    // 並列処理のエラーチェック
                    this.isParallelError(backupResponse);
                    return Promise.reject(response);
                }.bind(this))
                ;
            } else {
            
                this.closeWaitPanel();
                MSF.main.closeModalPanel(true);
                // 非同期スライス更新OPID
                //var operation_id = response.data.operation_id;
                // 非同期ポーリング登録
                //this.registAsyncPolling(MSF.MessageInfo.AddCP.check, operation_id, response.msgParam, MSF.Conf.MSFMain.Polling.AddCP, function() {
                // CP一括有効化パラレル待合せ後の正常系処理
                // パネルを閉じる処理
                //MSF.main.closeModalPanel(false);
            }
        }.bind(this))
        .catch(function(reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("CP追加処理 最終キャッチ", reason);
        }
        .bind(this));
    }
    ;

    //
    // CP削除処理
    //
    MSF.MSFjsonForm.prototype.delCP = function (pm) {
        var actionList = [];
        for (var i = 0; i < pm.length; i++) {
            var info = {};
            info.pm = pm[i];
            // CP削除処理
//             actionList.push(this.rest.deleteCP(MSF.MessageInfo.DeleteCP.delete, info, true));
            // CP無効化予約シリアル実行用リスト
            actionList.push(info);
        }

        // CP削除パラレル実行
//         Promise.all(actionList)
        // CP削除シリアル実行
         this.serialAsyncOperateCP(MSF.MessageInfo.DeleteCP.delete, actionList, "deleteCP")
        .then(function (response) {
            // 並列処理のエラーチェック
            if (this.isParallelError(response)) {
                return Promise.reject(response);
            }
            // CP削除後の正常系処理
            var Success = response[0].msgInfo.Success;
            // Waitパネル非表示
            this.closeWaitPanel();
            if (Success) {
                var cp_ids = [];
                for (var i = 0; i < response.length; i++) {
                    var res = response[i];
                    cp_ids.push(res.ajax.urlParam.cp_id);
                }
                // パネルを閉じる処理
                MSF.main.closeModalPanel(false);
                // メッセージ表示
                MSF.main.showMessage("メッセージボックス", "", Success.format({cp_ids:cp_ids.join(",")}));
            } else {
                // パネルを閉じる処理
                MSF.main.closeModalPanel(true);
            }
        }.bind(this))
        .catch(function (reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("CP削除処理 最終キャッチ", reason);
        }.bind(this));
    }
    ;

    //
    // 並列処理のエラーチェック
    //
    MSF.MSFjsonForm.prototype.isParallelError = function (response) {
        //戻り値のチェック
        var isError = false;
        var ErrorPrefix = "";
        var msglst = "";
        var output = "";

        // メッセージ作成
        for (var i = 0; i < response.length; i++) {
            var res = response[i];
            msglst += res.errorMessage + "<br>";
            if (res.isError) {
                ErrorPrefix = res.msgInfo.ErrorPrefix;
                output = res.msgInfo.Output;
                isError = true;
            }
        }
        if (isError) {
            var msgWord = ErrorPrefix + "<br>" + msglst;
            // メッセージ表示
            MSF.main.showMessage(output, "", msgWord);
        }

        return isError;
    }
    ;
    //
    // CP変更非同期シリアル処理
    //
    MSF.MSFjsonForm.prototype.serialAsyncOperateCP = function (messageInfo, paramList, command) {

        var param = {
            messageInfo: messageInfo,
            count: 0,
            paramList: paramList,
            execList: [],
            command:command,
        };

        // ループ処理の完了を受け取るPromise
        return new Promise(function(resolve, reject) {
            if ( paramList.length > 0) {
                // 初回実行
                this.serialAsyncOperateCPLoop(param, resolve, reject);
            } else {
                resolve();
            }
        }.bind(this))
        ;
    }
    ;

    //
    // CP変更非同期シリアルLoop処理
    //
    MSF.MSFjsonForm.prototype.serialAsyncOperateCPLoop = function (param, resolve, reject) {
        var pm = param;
        // 非同期処理なのでPromiseを利用
        var info = pm.paramList[pm.count];
        var action;

        // コマンドで処理振分け
        if (pm.command == "deleteCP") {
            action = MSF.main.rest.deleteCP(pm.messageInfo, info, true);
        }else if (pm.command == "createCP") {
            action = MSF.main.rest.createCP(pm.messageInfo, info)
        }

        // アクション実行
        action
        .then(function(response) {
            pm.execList.push(response);
            pm.count++;
            if (response.isError){
                // エラーのため未実行となった処理のメッセージ作成
                for (var i = pm.count; i < pm.paramList.length; i++) {
                    var res = {};
                    res.ajax = {};
                    res.ajax.urlParam = pm.paramList[i].pm;
                    // エラーメッセージ作成用の変数をかき集める
                    var msgParam = {};
                    for(var key in pm.paramList[i].pm)
                        msgParam[key] = pm.paramList[i].pm[key];
                    msgParam.edge_point_id = pm.paramList[i].body.edge_point_id;
                    // 未実行エラーメッセージ作成
                    res.errorMessage = response.msgInfo.UnexecutedWithError.format(msgParam);
                    res.isError = true;
                    res.msgInfo = response.msgInfo;
                    pm.execList.push(res);
                }
                resolve(pm.execList);
            } else {
                // ループを抜けるかどうかの判定
                if (pm.count < pm.paramList.length) {
                    // 再帰的に実行
                    this.serialAsyncOperateCPLoop(pm, resolve, reject);
                } else {
                    // 抜ける
                    resolve(pm.execList);
                }
            }
        }.bind(this))
        .catch(function (reason) {
            pm.execList.push(reason);
            MSF.console.debug("CP変更非同期シリアル処理 キャッチ", reason);
            reject(pm.execList);
        }.bind(this));
    }
    ;

    //
    // 非同期ポーリング登録処理
    //
    MSF.MSFjsonForm.prototype.registAsyncPolling = function (messageInfo, operation_id, msgParam, conf, doneAction) {
        // 現在日時取得
        var alertTime = new Date();
        // タイムアウトさせたい時間にタイマーをセット。
        alertTime.setSeconds( alertTime.getSeconds() + conf.TIMEOUT );

        // 非同期オペレーション詳細取得
        this.getOperationDetail(messageInfo, operation_id, alertTime, msgParam, conf, doneAction);
    }
    ;
    //
    // 非同期ポーリング処理
    //
    MSF.MSFjsonForm.prototype.asyncPolling = function (messageInfo, operation_id, alertTime, msgParam, conf, doneAction) {
        // ポーリング用のタイマーを設定
        setTimeout(function() {
            // 非同期オペレーション詳細取得
            this.getOperationDetail(messageInfo, operation_id, alertTime, msgParam, conf, doneAction);
        }
        .bind(this), conf.INTERVAL);
    }
    ;
    //
    // 非同期オペレーション詳細取得処理
    //
    MSF.MSFjsonForm.prototype.getOperationDetail = function (messageInfo, operation_id, alertTime, msgParam, conf, doneAction) {
        // オペレーション詳細情報取得
        this.rest.getOperationDetail(messageInfo, operation_id, msgParam)
        .then(function(response) {
            var op = response.data;
            var dt = new Date();
            var isTimeout = alertTime < dt;
            response.msgParam = msgParam;
            // (未実行、実行中)でまだタイムアウトしていない場合
            if ((op.status == "unexecuted" || op.status == "executing") && !isTimeout) {
                // 再帰呼出し
                this.asyncPolling(messageInfo, operation_id, alertTime, msgParam, conf, doneAction);
            } else {
                var Output = response.msgInfo.Output;
                var message = "";
                if (op.status == "completed") {
                    // 完了時
                    // 出力メッセージ作成
                    if (response.msgInfo.Success) {
//                      var body = op.response.body.replace(/\\/g, "\\");
                        if (!op.response.body) {
                            op.response.body = "{}";
                        }
                        var body = JSON.parse(op.response.body);
                        body = $.extend(true, body, msgParam);
                        // キーワードが含まれていれば置換する
                        message = response.msgInfo.Success.format(body);
                        Output = "メッセージボックス";
                    }
                    // 成功アクション呼出し
                    doneAction();
                } else if (op.status == "failed") {
                    // 失敗時
                    // 出力メッセージ作成
                    var status = op.response.status_code;
                    var error_code;

                    // エラーメッセージを生成
                    var resWord = null;
                    if (response.textStatus=="parsererror") {
                        // 応答のJSONボディをパースできなかった場合(構文エラー)
                        resWord = "Response Parse Error (status=[{0}])".format(status);
                    } else {
                        // エラーコードとRestのステータスから動的メッセージ取得
                        if (!op.response.body) {
                            op.response.body = "{}";
                        }
                        var body = JSON.parse(op.response.body);
                        if (status && body) {
                            error_code = body.error_code;
                            resWord = MSF.MessageDic[status][error_code];
                        }
                    }

                    // メッセージがなかった場合
                    resWord = resWord ||  "Undefined Error (error=[{0}]、status=[{1}])".format(error_code, status);
                    response.msgParam.ResponseErrorMessage = resWord;
                    var req_body = JSON.parse(op.request.body);
                    var path = {
                        failure_path_list: []
                    };
                    var isSetPath = false;
                    if (body.target_clusters) {
                        if (body.target_clusters.length > 1) {
                            // PATCHメソッドにより複数クラスタにREST送信した場合
                            path.failure_path_list = this.setFailurePath(op, req_body);
                            isSetPath = true;
                        }
                    }
                    // 1つのクラスタにREST送信した場合
                    if(!isSetPath && req_body instanceof Array){
                        // PATCHメソッドの場合
                        path.failure_path_list = this.setFailurePath(op, req_body);
                    }

                    msgParam = $.extend(true, msgParam, path);
                    message = response.msgInfo.CheckFailed.format(msgParam);
                } else if (op.status == "canceled") {
                    // システム依存の実行取消時
                    // 出力メッセージ作成
                    message = response.msgInfo.CheckCanceled.format(msgParam);
                } else if (isTimeout) {
                    // タイムアウト時
                    // 出力メッセージ作成
                    message = response.msgInfo.CheckTimeout.format(msgParam);
                } else {
                    MSF.console.error("非同期ポーリング処理で不明の処理状態 [" + op.status + "]");
                    // Waitパネル非表示
                    this.closeWaitPanel();
                }
                if (message) {
                    // Waitパネル非表示
                    this.closeWaitPanel();
                    // メッセージ表示
                    MSF.main.showMessage(Output, "", message);
                }
            }
        }.bind(this))
        .catch(function (reason) {
            // Waitパネル非表示
            this.closeWaitPanel();
            MSF.console.debug("非同期ポーリング処理 最終キャッチ", reason);
        }.bind(this))
        ;
    }
    ;
    //
    // REST失敗pathを設定
    //
    MSF.MSFjsonForm.prototype.setFailurePath = function (op, req_body) {

        var failure_path_list = [];
        for(var i = 0; req_body.length > i; i++){
            var target_path = req_body[i].path;
            if(this.isFailurePath(op, target_path)){
                failure_path_list.push(req_body[i].path.slice(1));
            }
        }
        return failure_path_list;
    }
    ;
    //
    // REST失敗pathか判別
    //
    MSF.MSFjsonForm.prototype.isFailurePath = function (op, path) {

        // ロールバック対象が1つもない場合、trueを返す
        if(!op.rollbacks){
            return true;
        } else {
            for(var i = 0; op.rollbacks.target_clusters.length > i; i++){
                var rollback_path = JSON.parse(op.rollbacks.target_clusters[i].request.body);
                for(var j = 0; rollback_path.length > j; j++){
                    if (path === rollback_path[j].path) {
                        // ロールバックが実行されている場合、falseを返す
                        return false;
                    }
                }
            }
        }
        return true;
    }
    ;
    //
    // JSON.stringify 時　未入力項目を除く
    //
    MSF.MSFjsonForm.prototype.replacer = function (key, value) {

        // ルートキーは削除しません
        if (key == "") return value;

        // ゴミは文字列化しません
        if (this.isValidValue(value)) return value;

        return undefined;
    }
    ;
    //
    // Waitパネル表示
    //   processingMsgID: 処理中であるメッセージを表す要素のID。処理中であることを示すために表示します。
    //
    MSF.MSFjsonForm.prototype.showWaitPanel = function (processingMsgID) {
        $(".overlayPanel").find("*").prop("disabled", true);
        $("#"+processingMsgID).fadeIn(250);
    }
    ;
    //
    // Waitパネル非表示
    //
    MSF.MSFjsonForm.prototype.closeWaitPanel = function () {
        $(".overlayPanel").find("*").prop("disabled", false);
        $(".processing").fadeOut(250);
    }
    ;

    //
    // 該当のオブジェクトがJSON化する必要があるまともなオブジェクトかどうかを判定
    //
    MSF.MSFjsonForm.prototype.isValidValue = function (value) {

        // 空文字列、null はゴミです。
        if (value == null) return false;
        if (value === "") return false;

        // 上記以外で、オブジェクトじゃないもの(値)は、まともなデータです
        if ((value instanceof Object) == false) return true;

        // オブジェクトは再帰的に聞きましょう
        // Arrayの場合
        if (value instanceof Array) {
            for (var i=0; i<value.length; i++) {
                // 中にひとつでもゴミでないものがいれば、まともなデータです
                if (this.isValidValue(value[i])) return true;
            }
            // ひとつも無かったのなら･･･ゴミです。
            return false;
        } else {
            for (var key in value) {
                // 中にひとつでもゴミでないものがいれば、まともなデータです
                if (this.isValidValue(value[key])) return true;
            }
            // ひとつも無かったのなら･･･ゴミです。
            return false;
        }
    }
    ;

})();

$(function () {
    "use strict";
    //
    //機種情報登録ボタンクリックイベント
    //
    $("#modelInfoAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // 機種情報選択パネルの遷移先を設定（既存の遷移先を削除してから）
        $("#addModelDeploy").unbind("click");
        $("#addModelDeploy").click(function () {

            // エラーメッセージを非表示
            $("#addModelAlert").hide();
            try {
                // 処理中パネルを表示
                MSF.main.jsonform.showWaitPanel("addModelProcessing");

                // 選択された機種(削除対象)のKEYを取得
                var modelKey = $("input[name='addModel']:checked").val();
                if (!modelKey) return;

                // RESTインタフェースのIDを設定
                document.getElementById('paneldeploy_id').defaultValue = "P010101";

                var pm = { cluster_id: MSF.Conf.Rest.CLUSTER_ID };
                var info = MSF.ModelInfo[modelKey];

                MSF.main.jsonform.condition(pm, info)
                .then(function() {
                    // 機種情報をいじったら、機種情報一覧は取り直し(有効期限を現在日時に変更)
                    MSF.main.db.EquipmentTypeExpireDate = new Date();
                });
            } catch(e) {
                // Waitパネル非表示
                MSF.main.jsonform.closeWaitPanel();
                MSF.console.error(e);
            }
        });

        // 機種情報選択パネル(追加用)を表示
        MSF.main.showAddModelSelectPanel();
    });

    //
    //機種情報削除ボタンクリックイベント
    //
    $("#modelInfoDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // 機種情報選択パネルの遷移先を設定（既存の遷移先を削除してから）
        $("#modelSelect").unbind("click");
        $("#modelSelect").click(function () {

            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // 機種情報選択パネルの選択内容の取得
            var modelVal = $("input[name='model']:checked").val();

            // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
            var transfer = {};
            transfer.equipmentTypeID = modelVal;                        // 機種ID

            MSF.main.jsonform.htmlGeneration("P010104", transfer);
            document.getElementById("paneldeploy_id").defaultValue = "P010104";

            MSF.main.showModalPanel("restCommonPanel");
        });

        // 機種情報選択パネルを表示
        MSF.main.showModelSelectPanel(MSF.MessageInfo.DeleteModel.searchModel);
    });

    //
    //SWクラスタ増設ボタンクリックイベント
    //
    $("#clusterAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Map) return;

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};                                              // 無し

        MSF.main.jsonform.htmlGeneration("P010201", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010201";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //SWクラスタ減設ボタンクリックイベント
    //
    $("#clusterDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Map) return;

        // クラスタ未選択の場合は、何もしない
        var select = false;
        for (var id in MSF.main.mcv) {
            if (MSF.main.mcv[id].select) {
                select = true;
                break;
            }
        }
        if (!select) return;

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = id;                                        // クラスタID

        MSF.main.jsonform.htmlGeneration("P010204", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010204";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //LEAF追加ボタンクリックイベント
    //
    $("#leafAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 現在表示しているファブリックネットワークのクラスタIDを取得
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 機種情報選択パネルの遷移先を設定
        $("#modelSelect").unbind("click");
        $("#modelSelect").click(function () {
            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // 機種選択パネルの選択内容の取得
            var vpnType;
            var result1 = $("#vpnType").is(":checked");
            if ($("input[name='vpnType']:checked").val() == MSF.Const.SliceType.L2) {
                vpnType = MSF.Const.VpnType.L2;
            } else {
                vpnType = MSF.Const.VpnType.L3;
            }
            var modelVal = $("input[name='model']:checked").val();

            // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
            var transfer = {};
            transfer.clusterID = clusterId;                              // クラスタID
            transfer.vpnType = vpnType;                                 // VPNタイプ
            transfer.equipmentTypeID = modelVal;                        // 機種ID

            MSF.main.jsonform.htmlGeneration("P010401", transfer);
            document.getElementById("paneldeploy_id").defaultValue = "P010401";
            MSF.main.showModalPanel("restCommonPanel");
        });

        // 機種情報選択パネルを表示
        MSF.main.showModelSelectPanel(MSF.MessageInfo.AddLeaf.searchModel);
    });

    //
    //LEAF削除ボタンクリックイベント
    //
    $("#leafDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 装置未選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch) return;

        // SPINE図選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch.isLeaf) return;

        // 現在表示しているファブリックネットワークのクラスタIDを取得
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = MSF.main.can.Focus.Switch.id;                 // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P010404", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010404";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //LEAF変更ボタンクリックイベント
    //
    $("#leafModify").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 装置未選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch) return;

        // SPINE図選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch.isLeaf) return;

        // 現在表示しているファブリックネットワークのクラスタIDを取得
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = MSF.main.can.Focus.Switch.id;                 // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P010405", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010405";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //SPINE追加ボタンクリックイベント
    //
    $("#spineAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 現在表示しているファブリックネットワークのクラスタIDを取得
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 機種情報選択パネルの遷移先を設定
        $("#modelSelect").unbind("click");
        $("#modelSelect").click(function () {

            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // 機種選択パネルの選択内容の取得
            var modelVal = $("input[name='model']:checked").val();

            // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
            var transfer = {};
            transfer.clusterID = clusterId;                              // クラスタID
            transfer.equipmentTypeID = modelVal;                        // 機種ID

            MSF.main.jsonform.htmlGeneration("P010501", transfer);
            document.getElementById("paneldeploy_id").defaultValue = "P010501";

            MSF.main.showModalPanel("restCommonPanel");
        });

        // 機種情報選択パネルを表示
        MSF.main.showModelSelectPanel(MSF.MessageInfo.AddSpine.searchModel);
    });

    //
    //SPINE削除ボタンクリックイベント
    //
    $("#spineDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 装置未選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch) return;

        // LEAF図選択の場合は、何もしない
        if (MSF.main.can.Focus.Switch.isLeaf) return;

        // 現在表示しているファブリックネットワークのクラスタIDを取得
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = MSF.main.can.Focus.Switch.id;                 // Spine装置ID

        MSF.main.jsonform.htmlGeneration("P010504", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010504";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //物理IF変更ボタンクリックイベント
    //
    $("#physicalIFModify").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var nodeId;
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // 装置未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch) return;
            //spine装置選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch.isLeaf) return;
            nodeId = MSF.main.can.Focus.Switch.id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            //spine装置選択の場合は、何もしない
            if (getDeviceTypesNavi() == "spines") return;
            nodeId = getNodeIdNavi();
        } else {
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = nodeId;                                       // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P010803", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010803";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //breakoutIF登録ボタンクリックイベント
    //
    $("#breakoutIFAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var nodeId;
        var fabricType;
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // 装置未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch) return;
            nodeId = MSF.main.can.Focus.Switch.id;
            // 装置種別の取得
            if (MSF.main.can.Focus.Switch.isLeaf) {
                fabricType = MSF.Const.FabricType.Leafs
            } else {
                fabricType = MSF.Const.FabricType.Spines
            }
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            nodeId = getNodeIdNavi();
        } else {
            // ファブリックネットワーク表示およびノード表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = nodeId;                                       // 装置ID
        transfer.fabricType = fabricType;                               // 装置種別

        MSF.main.jsonform.htmlGeneration("P010901_add", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010901_add";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //breakoutIF削除ボタンクリックイベント
    //
    $("#breakoutIFDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var nodeId;
        var fabricType;
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // 装置未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch) return;
            nodeId = MSF.main.can.Focus.Switch.id;
            // 装置種別の取得
            if (MSF.main.can.Focus.Switch.isLeaf) {
                fabricType = MSF.Const.FabricType.Leafs
            } else {
                fabricType = MSF.Const.FabricType.Spines
            }
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            nodeId = getNodeIdNavi();
        } else {
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = nodeId;                                       // 装置ID
        transfer.fabricType = fabricType;                               // 装置種別

        MSF.main.jsonform.htmlGeneration("P010901_delete", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P010901_delete";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //LAG IF追加ボタンクリックイベント
    //
    $("#lagIFAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var nodeId;
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // 装置未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch) return;
            //spine装置選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch.isLeaf) return;
            nodeId = MSF.main.can.Focus.Switch.id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            nodeId = getNodeIdNavi();
        } else {
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = nodeId;                                       // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P011101", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P011101";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //LAG IF削除ボタンクリックイベント
    //
    $("#lagIFDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var nodeId;
        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // 装置未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch) return;
            //spine装置選択の場合は、何もしない
            if (!MSF.main.can.Focus.Switch.isLeaf) return;
            nodeId = MSF.main.can.Focus.Switch.id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Equipment) {
            nodeId = getNodeIdNavi();
        } else {
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.nodeID = nodeId;                                       // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P011105", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P011105";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //クラスタ間リンクIF新設ボタンクリックイベント
    //
    $("#clusterAddLink").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Map) return;

        // クラスタ未選択の場合は、何もしない
        var select = false;
        for (var id in MSF.main.mcv) {
            if (MSF.main.mcv[id].select) {
                select = true;
                break;
            }
        }
        if (!select) return;

        // IFType選択パネルの遷移先を設定
        $("#selectIF").unbind("click");
        $("#selectIF").click(function () {

            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // IFType選択パネルの選択内容の取得
            var ifType;
            var result1 = $("#ifType").is(":checked");
            if ($("input[name='ifType']:checked").val() == MSF.Const.IfType.PhysicalIf) {
                ifType = MSF.Const.IfType.PhysicalIf;
            } else if ($("input[name='ifType']:checked").val() == MSF.Const.IfType.BreakoutIf) {
                ifType = MSF.Const.IfType.BreakoutIf;
            } else {
                ifType = MSF.Const.IfType.LagIf;
            }

            // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
            var transfer = {};
            transfer.ifType = ifType;                                       // 制御種別
            transfer.clusterID = id;                                        // クラスタID

            MSF.main.jsonform.htmlGeneration("P011201", transfer);
            document.getElementById("paneldeploy_id").defaultValue = "P011201";

            MSF.main.showModalPanel("restCommonPanel");
        });

        // IFType選択パネルを表示
        MSF.main.showModalPanel("ifTypeSelectPanel");
    });

    //
    //クラスタ間リンクIF減設ボタンクリックイベント
    //
    $("#clusterDeleteLink").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Map) return;

        // クラスタ間リンク未選択の場合は、何もしない
        var isSelectClusterLink = false;
        var clusterId;
        var oppositeId;
        for (var i = 0; i < svg.clusterLinkLines.length; i++) {
            if (svg.clusterLinkLines[i].select) {
                isSelectClusterLink = true;
                clusterId = svg.clusterLinkLines[i].local.id;
                oppositeId = svg.clusterLinkLines[i].opposite.id;
                break;
            }
        }
        if (isSelectClusterLink == false) return;

        var linkIf = MSF.main.db.clusterInfoDic[clusterId].cluster_link_ifs.filter(function(linkIfs) {
            if (linkIfs.opposite_cluster_id == oppositeId) return true;
        });
        var linkId = linkIf.pop().cluster_link_if_id;

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                        // クラスタID
        transfer.linkID = linkId;                                              // クラスタ間リンクID

        MSF.main.jsonform.htmlGeneration("P011204", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P011204";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //Edge-Point追加ボタンクリックイベント
    //
    $("#edgePointAdd").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // 装置未選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch) return;

        //spine装置選択の場合は、何もしない
        if (!MSF.main.can.Focus.Switch.isLeaf) return;

        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID
        transfer.leafNodeID = MSF.main.can.Focus.Switch.id;             // Leaf装置ID

        MSF.main.jsonform.htmlGeneration("P011401", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P011401";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //Edge-Point削除ボタンクリックイベント
    //
    $("#edgePointDelete").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // ファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        var clusterName = $("#navi2").text();
        var clusterId = clusterName.replace(/> Cluster#/, "");

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.clusterID = clusterId;                                 // クラスタID

        MSF.main.jsonform.htmlGeneration("P011404", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P011404";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //SLICE追加ボタンクリックイベント
    //
    $("#sliceControlCreate").click(function () {

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
        if (MSF.main.networkmode != MSF.Const.NetworkMode.Map &&
            MSF.main.networkmode != MSF.Const.NetworkMode.Cluster) return;

        // スライスタイプ選択パネルの遷移先を設定
        $("#selectSlice").unbind("click");
        $("#selectSlice").click(function () {

            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // スライスタイプ選択パネルの選択内容の取得
            var vpnType;
            var sliceType;
            var result1 = $("#sliceVpnType").is(":checked");
            if ($("input[name='sliceVpnType']:checked").val() == MSF.Const.SliceType.L2) {
                vpnType = MSF.Const.VpnType.L2;
                sliceType = MSF.Const.SliceType.L2;
            } else {
                vpnType = MSF.Const.VpnType.L3;
                sliceType = MSF.Const.SliceType.L3;
            }

            // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
            var transfer = {};
            transfer.vpnType = vpnType;                                 // VPNタイプは必要
            transfer.sliceType = sliceType;                             // スライスタイプ

            MSF.main.jsonform.htmlGeneration("P020101", transfer);
            document.getElementById("paneldeploy_id").defaultValue = "P020101";

            MSF.main.showModalPanel("restCommonPanel");
        });

        // スライスタイプ選択パネルを表示
        MSF.main.showModalPanel("sliceSelectPanel");
    });

    //
    //SLICE変更ボタンクリックイベント
    //
    $("#sliceControlModify").click(function () {
        var sliceType;
        var sliceID;

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示およびファブリックネットワーク表示時の表示対象判定
        if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
            var isSelect = false;
            for (var id in MSF.main.msv) {
                if (MSF.main.msv[id].select) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect) return;
            if (MSF.main.msv[id].type == "L2") {
                sliceType = MSF.Const.SliceType.L2;
            } else {
                sliceType = MSF.Const.SliceType.L3;
            }
            sliceID = MSF.main.msv[id].id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // スライス未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice) return;
            // トポロジ図選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice.isLogical) return;
            sliceType =MSF.main.can.Focus.Slice.sliceType;
            sliceID = MSF.main.can.Focus.Slice.id;
        } else {
            // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.sliceType = sliceType;                                 // スライスタイプ
        transfer.sliceID = sliceID;                                     // スライスID

        MSF.main.jsonform.htmlGeneration("P020102", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P020102";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //SLICE削除ボタンクリックイベント
    //
    $("#sliceControlDelete").click(function () {
        var sliceType;
        var sliceID;

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示およびファブリックネットワーク表示時の表示対象判定
        if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
            var isSelect = false;
            for (var id in MSF.main.msv) {
                if (MSF.main.msv[id].select) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect) return;
            if (MSF.main.msv[id].type == "L2") {
                sliceType = MSF.Const.SliceType.L2;
            } else {
                sliceType = MSF.Const.SliceType.L3;
            }
            sliceID = MSF.main.msv[id].id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // スライス未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice) return;
            // トポロジ図選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice.isLogical) return;
            sliceType =MSF.main.can.Focus.Slice.sliceType;
            sliceID = MSF.main.can.Focus.Slice.id;
        } else {
            // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.sliceType = sliceType;                                 // スライスタイプ
        transfer.sliceID = sliceID;                                     // スライスID

        MSF.main.jsonform.htmlGeneration("P020103", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P020103";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //CP追加ボタンクリックイベント(PATCH版)
    //
    $("#sliceControlCreateCP").click(function () {
        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        var sliceType;
        var sliceID;
        var vpnType;
        var clusterId;
        var isSelect = false;

        if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
            // マルチクラスタ表示時の表示対象判定
            var slice;
            var selectedSlice = svg.getSelectedL2Slice();
            if (selectedSlice) {
                slice =svg.getL2SliceById(selectedSlice);
            } else {
                selectedSlice = svg.getSelectedL3Slice();
                slice =svg.getL3SliceById(selectedSlice);
            }
            
            if (slice) {
                isSelect = true;
                sliceType = MSF.Const.getSliceTypeForVpnType(slice.type);
                vpnType = MSF.Const.VpnType[slice.type];
                sliceID = slice.id;
            } else {
                // スライス未選択の場合、L2を初期値として使用
                sliceType = MSF.Const.SliceType.L2;
                vpnType = MSF.Const.VpnType.L2;
                sliceID = "";
            }
            
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // ファブリックネットワーク表示時の表示対象判定
            var slice = MSF.main.can.Focus.Slice;
            if (slice && slice.isLogical) {
                isSelect = true;
                sliceType =slice.sliceType;
                sliceID = slice.id;
                vpnType = slice.vpnType;
            } else {
                // スライス未選択の場合、L2を初期値として使用
                sliceType = MSF.Const.SliceType.L2;
                sliceID = "";
                vpnType = MSF.Const.VpnType.L2;
            }
            clusterId = MSF.main.can.viewClusterId;
        } else {
            // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.vpnType = vpnType;                                     // VPNタイプは必要
        transfer.sliceID = sliceID;                                     // スライスID
        transfer.sliceType = sliceType;                                 // スライスタイプ
        transfer.clusterID = clusterId;                                 // クラスタID

        // CP追加対象のスライス未選択のとき、スライス情報入力フォームを表示
        $('input[name="cpSlice"]:radio').val([sliceType]);
        $("#cpSliceIdVal").val(sliceID);
        if (isSelect) {
            $('#cpSliceTypeForm, #cpSliceIdForm').hide();
        } else {
            $('#cpSliceTypeForm, #cpSliceIdForm').show();
        }
        
        // CP生成オプションパネルの遷移先を設定
        $("#setCPOption").unbind("click");
        $("#setCPOption").click(function (transferData) {

            // スライスIDが未入力の場合は画面遷移なし
            var sliceId = $("#cpSliceIdVal").val().trim();
            if (!sliceId) {
                return;
            }

            // maskLayerをそのままにモーダルパネルだけを閉じる
            MSF.main.closeModalPanel(false);

            // スライスタイプ入力値を取得
            transferData.sliceType = $("input[name='cpSlice']:checked").val();
            transferData.sliceID = $("#cpSliceIdVal").val();
            var processId;
            // VPNタイプを再取得(スライス未選択状態対応)
            if (transferData.sliceType == MSF.Const.SliceType.L2) {
                transferData.vpnType = MSF.Const.VpnType.L2;
                processId = "P020201_add_l2";
            } else {
                transferData.vpnType = MSF.Const.VpnType.L3;
                processId = "P020201_add_l3";
            }

            MSF.main.jsonform.htmlGeneration(processId, transferData, function(option, jsonParam) {
                var result = jsonParam;
                // L3の時だけ加工する
                if (transferData.vpnType == MSF.Const.VpnType.L3) {
                    // 必要のないプロトコルを拾い出し
                    var removeTargetKeys = {};
                    $("#protocolList input").each(function (idx, item) {
                        if ($(item).prop("checked"))
                            removeTargetKeys[$(item).attr("data-protocol-key")] = true;
                    });
                    var pattern;
                    if(removeTargetKeys["bgp"]){
                        pattern = new RegExp("id=bgp style='display:none'");
                        result = result.replace(pattern, "id=bgp style='display:'" );
                    }
                    if(removeTargetKeys["static_routes"]){
                        pattern = new RegExp("id=static_routes style='display:none'");
                        result = result.replace(pattern, "id=static_routes style='display:'" );
                    }
                    if(removeTargetKeys["vrrp"]){
                        pattern = new RegExp("id=vrrp style='display:none'");
                        result = result.replace(pattern, "id=vrrp style='display:'" );
                    }
                }
                return result;
            }.bind(this, transferData));
            document.getElementById("paneldeploy_id").defaultValue = "P020201_add";

            MSF.main.showModalPanel("restCommonPanel");
        }.bind(MSF.main.jsonform, transfer));

        // L2のCPの場合は一部要素を隠す
        if (transfer.vpnType == MSF.Const.VpnType.L2 ) {
            $(".L3CPOnly").hide();
        } else {
            $(".L3CPOnly").show();
        }

        // CP生成オプションパネルを開く
        MSF.main.showModalPanel("cpCreateOptionPanel");
        // L2スライス選択時、CP生成オプションパネルに遷移せず、リクエスト画面に遷移する
        if(isSelect && transfer.vpnType == MSF.Const.VpnType.L2){
            document.getElementById("setCPOption").click();
        }
    });

    // スライス選択ラジオ変更イベント
    $('input[name="cpSlice"]:radio' ).change( function() {
        var val = $(this).val();
        if(val == MSF.Const.SliceType.L2){
            $(".L3CPOnly").hide();
        } else {
            $(".L3CPOnly").show();
        }
    });

    //
    //CP削除ボタンクリックイベント
    //
    $("#sliceControlDeleteCP").click(function () {
        var sliceType;
        var sliceID;

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示およびファブリックネットワーク表示時の表示対象判定
        if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
            var isSelect = false;
            for(var id in MSF.main.msv){
                if (MSF.main.msv[id].select) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect) return;
            if (MSF.main.msv[id].type == "L2") {
                sliceType = MSF.Const.SliceType.L2;
            } else {
                sliceType = MSF.Const.SliceType.L3;
            }
            sliceID = MSF.main.msv[id].id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // スライス未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice) return;
            // トポロジ図選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice.isLogical) return;
            sliceType =MSF.main.can.Focus.Slice.sliceType;
            sliceID = MSF.main.can.Focus.Slice.id;
        } else {
            // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.sliceID = sliceID;                                     // スライスID
        transfer.sliceType = sliceType;                                 // スライスタイプ

        MSF.main.jsonform.htmlGeneration("P020201_delete", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P020201_delete";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //CP変更ボタンクリックイベント
    //
    $("#sliceControlModifyCP").click(function () {
        var sliceType;
        var sliceID;

        // Network Mode表示以外の場合は、何もしない
        if (MSF.main.mode != MSF.Const.Mode.Network) return;

        // マルチクラスタ表示およびファブリックネットワーク表示時の表示対象判定
        if (MSF.main.networkmode == MSF.Const.NetworkMode.Map) {
            var isSelect = false;
            for(var id in MSF.main.msv){
                if (MSF.main.msv[id].select) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect) return;
            if (MSF.main.msv[id].type == "L2") {
                sliceType = MSF.Const.SliceType.L2;
            } else {
                sliceType = MSF.Const.SliceType.L3;
            }
            sliceID = MSF.main.msv[id].id;
        } else if (MSF.main.networkmode == MSF.Const.NetworkMode.Cluster) {
            // スライス未選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice) return;
            // トポロジ図選択の場合は、何もしない
            if (!MSF.main.can.Focus.Slice.isLogical) return;
            sliceType =MSF.main.can.Focus.Slice.sliceType;
            sliceID = MSF.main.can.Focus.Slice.id;
        } else {
            // マルチクラスタ表示およびファブリックネットワーク表示以外の場合は、何もしない
            return;
        }

        // 引継ぎデータ(vpnType, sliceID, sliceType, equipmentTypeID, nodeID, leafNodeID)
        var transfer = {};
        transfer.sliceID = sliceID;                                     // スライスID
        transfer.sliceType = sliceType;                                 // スライスタイプ

        MSF.main.jsonform.htmlGeneration("P020203", transfer);
        document.getElementById("paneldeploy_id").defaultValue = "P020203";

        MSF.main.showModalPanel("restCommonPanel");
    });

    //
    //DEPLOYボタンクリックイベント
    //
    $("#paneldeploy").on("click", function () {
        var frm = MSF.main.jsonform;

        // エラー表示をクリア
        $("#panelAlert").hide();

        // Waitパネル表示
        frm.showWaitPanel("restCommonProcessing");
        try {
            switch ($(':text[id="paneldeploy_id"]').val()) {
                case "P020101":
                    //SLICE追加
                    var pmjson = $("#params").jsForm("get");
                    var infojsn = $("#details").jsForm("get");

                    var pm = JSON.parse(JSON.stringify(pmjson, frm.replacer.bind(frm), " "));
                    var info = JSON.parse(JSON.stringify(infojsn, frm.replacer.bind(frm), " "));
                    frm.addSLICE(pm, info);

                    break;
                case "P020103":
                    //SLICE削除
                    var pmjson = $("#params").jsForm("get");
                    var pm = JSON.parse(JSON.stringify(pmjson, frm.replacer.bind(frm), " "));

                    frm.delSLICE(pm);

                    break;
                case "P010401":     // Leaf追加
                case "P010501":     // Spine追加
                    var pm = {};
                    var info = {};

                    // 空の要素をキーのみ生成するため、置き換えは実施しない。
                    // 2017/09/01 AT富岡様より不具合の連絡

                    var pm = JSON.parse(JSON.stringify($("#params").jsForm("get"))); //, frm.replacer.bind(frm), " "));

                    var info = JSON.parse(JSON.stringify($("#details").jsForm("get"))); //, frm.replacer.bind(frm), " "));

                    // 未入力データを整形する
                    var new_info = request_format_P010401(info);

                    frm.condition(pm, new_info);

                    break;
                case "P011101":     // LagIf生成

                    var pm = JSON.parse(JSON.stringify($("#params").jsForm("get"), frm.replacer.bind(frm), " "));
                    var info = JSON.parse(JSON.stringify($("#details").jsForm("get"), frm.replacer.bind(frm), " "));

                    // 必須要素:○ null許容:× 空配列許容:○
                    if (!info.breakout_if_ids) {
                        info.breakout_if_ids = [];
                    }
                    if (!info.physical_if_ids) {
                        info.physical_if_ids = [];
                    }

                    frm.condition(pm, info);
                    break;
                case "P010901_add":        // BreakoutIF追加
                case "P010901_delete":     // BreakoutIF削除
                case "P020201_add":        // CP生成
                case "P020201_delete":     // CP削除
                    //Breakout登録・削除
                    var pm = {};
                    var info = {};
                    
                    var pm = JSON.parse(JSON.stringify($("#params").jsForm("get"), frm.replacer.bind(frm), " "));
                    var info = JSON.parse(JSON.stringify($("#details").jsForm("get"), frm.replacer.bind(frm), " "));
                    // ダミーキーから無名オブジェクトを取出し
                    var nonameInfo = [];
                    
                    // pathが設定されているもののみ取出し
                    for (var i = 0; i < info.noname.length; i++) {
                        var path = info.noname[i].path;
                        if (!path) {
                            continue;
                        }
                        if (path.indexOf("/") !== 0) {
                            info.noname[i].path = "/" + path;
                        }
                        nonameInfo.push(info.noname[i]);
                    }

                    frm.condition(pm, nonameInfo)
                    .then(function() {
                        // 機種情報をいじったら、機種情報一覧は取り直し(有効期限を現在日時に変更)
                        if ($(':text[id="paneldeploy_id"]').val()=="P010104" || $(':text[id="paneldeploy_id"]').val()=="P010101")
                            MSF.main.db.EquipmentTypeExpireDate = new Date();
                    });
                    break;
                case "P020203":     // CP変更

                    var pm = JSON.parse(JSON.stringify($("#params").jsForm("get"), frm.replacer.bind(frm), " "));
                    var info = JSON.parse(JSON.stringify($("#details").jsForm("get"), frm.replacer.bind(frm), " "));

                    if (!info.update_option) {
                        info.update_option = {
                            qos_update_option : {}
                        };
                    }

                    frm.condition(pm, info);
                    break;
                default:
                    //その他
                    var pm = {};
                    var info = {};

                    var pm = JSON.parse(JSON.stringify($("#params").jsForm("get"), frm.replacer.bind(frm), " "));

                    var info = JSON.parse(JSON.stringify($("#details").jsForm("get"), frm.replacer.bind(frm), " "));

                    frm.condition(pm, info)
                    .then(function() {
                        // 機種情報をいじったら、機種情報一覧は取り直し(有効期限を現在日時に変更)
                        if ($(':text[id="paneldeploy_id"]').val()=="P010104" || $(':text[id="paneldeploy_id"]').val()=="P010101")
                            MSF.main.db.EquipmentTypeExpireDate = new Date();
                    });
                    break;
            }
        } catch(e) {
            // Waitパネル非表示
            frm.closeWaitPanel();
            MSF.console.error(e);
        }
    });

    //
    // LEAF追加:null許容フィールドチェック
    //
    var request_format_P010401 = function(info) {

        // breakout.local.breakout_ifsが0件 のとき, breakout.local=null
        if (info.breakout.local.breakout_ifs.length == 0) {
            info.breakout.local = null;
        }

        // breakout.oppositeが0件 のとき, breakout.opposite=null
        if (info.breakout.opposite.length == 0) {
            info.breakout.opposite = null;
        }

        if (info.breakout.local == null && info.breakout.opposite == null) {
            info.breakout = null;
        }

        // internal_links.physical_linksが0件のとき、internal_links.physical_links=null
        if (info.internal_links.physical_links.length == 0) {
            info.internal_links.physical_links = null;
        } else {
            for(var i in info.internal_links.physical_links){

                // 物理IF、breakoutIFのどちらを使用しているか判別
                var physical_links = info.internal_links.physical_links[i].internal_link_if;
                info.internal_links.physical_links[i].internal_link_if = checkInterface(physical_links);
            }
        }

        // internal_links.lag_linksが0件のとき、internal_links.lag_links=null
        if (info.internal_links.lag_links.length == 0) {
            info.internal_links.lag_links = null;
        } else {
            for(var i in info.internal_links.lag_links){
                for(var j in info.internal_links.lag_links[i].member_ifs){
                    // 物理IF、breakoutIFのどちらを使用しているか判別
                    var lag_links_if = info.internal_links.lag_links[i].member_ifs[j];
                    info.internal_links.lag_links[i].member_ifs[j] = checkInterface(lag_links_if);
                }
            }
        }

        if (info.internal_links.physical_links == null && info.internal_links.lag_links == null) {
            info.internal_links = null;
        }
        
        if (info.management_if_address == "") {
            info.management_if_address = null;
        }
        return info;
    }
    ;
    
    //
    // 物理IFかbreakoutIFかを判別
    //
    var checkInterface = function(data) {
        if (data.local.physical_if.physical_if_id == "") {
            data.local.physical_if = null;
        }
        if (data.local.breakout_if.breakout_if_id == "") {
            data.local.breakout_if = null;
        }
        if (data.opposite.physical_if.physical_if_id == "") {
            data.opposite.physical_if = null;
        }
        if (data.opposite.breakout_if.breakout_if_id == "") {
            data.opposite.breakout_if = null;
        }
        return data;
    }
});

// 繰り返し項目を開く
// (RestHtml.js内のhtmlから呼ばれる)
function showChildItem(obj) {
    var target = obj.parent("legend").parent("fieldset").children("div").children("div.POJO:hidden");
    if (target.length <= 1) {
        obj.css({color: "#c0c0c0", cursor: "default"});
    }
    obj.next().css({color: "#000000", cursor: "pointer"});
    target.first().slideDown();
}
// 繰り返し項目を閉じる
// (RestHtml.js内のhtmlから呼ばれる)
function hideChildItem(obj) {
    var target = obj.parent("legend").parent("fieldset").children("div").children("div.POJO:visible");
    if (target.length <= 1) {
        obj.css({color: "#c0c0c0", cursor: "default"});
    }
    obj.prev().css({color: "#000000", cursor: "pointer"});
    target.last().slideUp();
}
