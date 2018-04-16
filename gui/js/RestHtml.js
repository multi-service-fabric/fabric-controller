// 定数定義
MSF.Html = {
        // Leaf追加
        P010401: {
            PARM: "" +
                "<span data-i18n='[html]Params.P010401.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n",

            BODY: "" +
                "<span data-i18n='[html]Params.P010401.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.equipment_type_id'>equipment_type_id</span>:" +
                "<input type='text' name='data.equipment_type_id' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.leaf_type'>leaf_type</span>:" +
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.leaf_type' value='BL' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.BL'>BL</span>&nbsp;&nbsp;&nbsp;"+
                "    <input type='radio' name='data.leaf_type' value='IL' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.IL'>IL</span>&nbsp;&nbsp;&nbsp;"+
                "    <input type='radio' name='data.leaf_type' value='EL' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.EL'>EL</span>"+
                "</span><br />" +
                "<span data-i18n='[html]Params.P010401.host_name'>host_name</span>:" +
                "<input type='text' name='data.host_name' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.mac_address'>mac_address</span>:" +
                "<input type='text' name='data.mac_address' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.username'>username</span>:" +
                "<input type='text' name='data.username' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.password'>password</span>:" +
                "<input type='text' name='data.password' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.provisioning'>provisioning</span>:" +
                "<input type='checkbox' name='data.provisioning' class='panel_details panel_select_checkbox' /><br />" +
                "<span data-i18n='[html]Params.P010401.vpn_type'>vpn_type</span>:" +
                "<input type='text' name='data.vpn_type' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.snmp_community'>snmp_community</span>:" +
                "<input type='text' name='data.snmp_community' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010401.ntp_server_address'>ntp_server_address</span>:" +
                "<input type='text' name='data.ntp_server_address' class='panel_details' /><br />" +
                "<br />" +
                "" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010401.breakout'>breakout</span>:" +
                "        <span data-i18n='[html]Params.P010401.breakout_local'>local</span>" +
                "    </legend>" +
                "    <fieldset>" +
                "        <legend class='countIf_start'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs'>breakout_ifs</span>" +
                "            <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "            <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "        </legend>" +
                "        <div class='collection' data-field='data.breakout.local.breakout_ifs'>" +
                "            <div class='initHideItem'>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                <span class='countIf'></span>" +
                "                <span class='itemSep2'></span>" +
                "                <fieldset>" +
                "                    <legend class='countIfId_start'>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_breakout_if_ids'>breakout_if_ids</span>" +
                "                        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                    </legend>" +
                "                    <div class='collection' data-field='breakout_ifs.breakout_if_ids'>" +
                "                        <div class='initHideItem'>" +
                "                            <span>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_breakout_if_id'>breakout_if_id</span>" +
                "                                <span class='countIfId'></span>:" +
                "                                <input type='text' name='breakout_if_ids.' class='panel_details' />" +
                "                            </span>" +
                "                        </div>" +
                "                    </div>" +
                "                </fieldset>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_base_if'>base_if</span>:" +
                "                <span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_base_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='breakout_ifs.base_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_division_number'>division_number</span>:" +
                "                <input type='number' name='breakout_ifs.division_number' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_local_breakout_ifs_breakout_if_speed'>breakout_if_speed</span>:" +
                "                <input type='text' name='breakout_ifs.breakout_if_speed' class='panel_details' /><br />" +
                "            </div>" +
                "        </div>" +
                "    </fieldset>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010401.breakout'>breakout</span>:" +
                "        <span data-i18n='[html]Params.P010401.breakout_opposite'>opposite</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.breakout.opposite'>" +
                "        <div>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_opposite_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='opposite.opposite_node_id' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend class='countIf_start'>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs'>breakout_ifs</span>" +
                "                    <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                    <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                </legend>" +
                "                <div class='collection' data-field='opposite.breakout_ifs'>" +
                "                    <div class='initHideItem'>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                        <span class='countIf'></span>" +
                "                        <span class='itemSep2'></span>" +
                "                        <fieldset>" +
                "                            <legend class='countIfId_start'>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_breakout_if_ids'>breakout_if_ids</span>" +
                "                                <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                                <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                            </legend>" +
                "                            <div class='collection' data-field='breakout_ifs.breakout_if_ids'>" +
                "                                <div class='initHideItem'>" +
                "                                    <span>" +
                "                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_breakout_if_id'>breakout_if_id</span>:"+
                "                                        <span class='countIfId'></span>" +
                "                                        <input type='text' name='breakout_if_ids.' class='panel_details' />" +
                "                                    </span>" +
                "                                </div>" +
                "                            </div>" +
                "                        </fieldset>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span  data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_base_if'>base_if</span>:" +
                "                        <span  data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_base_if_physical_if_id'>physical_if_id</span>:" +
                "                        <input type='text' name='breakout_ifs.base_if.physical_if_id' class='panel_details' /><br />" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span  data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_division_number'>division_number</span>:" +
                "                        <input type='number' name='breakout_ifs.division_number' class='panel_details number' /><br />" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span  data-i18n='[html]Params.P010401.breakout_opposite_breakout_ifs_breakout_if_speed'>breakout_if_speed</span>:" +
                "                        <input type='text' name='breakout_ifs.breakout_if_speed' class='panel_details' /><br />" +
                "                    </div>" +
                "                </div>" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<fieldset class='countIf_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010401.internal_links'>internal_links</span>:" +
                "        <span data-i18n='[html]Params.P010401.internal_links_physical_links'>physical_links</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    " +
                "    <div class='collection' data-field='data.internal_links.physical_links'>" +
                "        <div class='initHideItem'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "            <span class='countIf'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='physical_links.opposite_node_id' class='panel_details' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_local_traffic_threshold'>local_traffic_threshold</span>:" +
                "            <input type='number' name='physical_links.local_traffic_threshold' class='panel_details number' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_opposite_traffic_threshold'>opposite_traffic_threshold</span>:" +
                "            <input type='number' name='physical_links.opposite_traffic_threshold' class='panel_details number' /><br />" +
                "            " +
                "            <fieldset>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if'>internal_link_if</span>:" +
                "                    <span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_local'>local</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_local_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "            </fieldset>" +
                "            <fieldset>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if'>internal_link_if</span>:" +
                "                    <span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_opposite'>opposite</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_physical_links_internal_link_if_opposite_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.breakout_if.breakout_if_id' class='panel_details' /> <br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "" +
                "<fieldset class='countIf_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010401.internal_links'>internal_links</span>:" +
                "        <span data-i18n='[html]Params.P010401.internal_links_lag_links'>lag_links</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    " +
                "    <div class='collection' data-field='data.internal_links.lag_links'>" +
                "        <div class='initHideItem'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "            <span class='countIf'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='lag_links.opposite_node_id' class='panel_details' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_local_traffic_threshold'>local_traffic_threshold</span>:" +
                "            <input type='number' name='lag_links.local_traffic_threshold' class='panel_details number' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_opposite_traffic_threshold'>opposite_traffic_threshold</span>:" +
                "            <input type='number' name='lag_links.opposite_traffic_threshold' class='panel_details number' /><br />" +
                "            " +
                "            <fieldset class='countIfId_start'>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs'>member_ifs</span>:" +
                "                    <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                    <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                </legend>" +
                "                <div class='collection' data-field='lag_links.member_ifs'>" +
                "                    <div class='initHideItem'>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                        <span class='countIfId'></span>" +
                "                        <span class='itemSep2'></span>" +
                "                        <fieldset>" +
                "                            <legend>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_local'>local</span>" +
                "                            </legend>" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_local_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                            <input type='text' name='member_ifs.local.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_local_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                            <input type='text' name='member_ifs.local.physical_if.physical_if_speed' class='panel_details' /> <br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_local_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                            <input type='text' name='member_ifs.local.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "                        </fieldset>" +
                "                        <fieldset>" +
                "                            <legend>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_opposite'>opposite</span>" +
                "                            </legend>" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                            <input type='text' name='member_ifs.opposite.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                            <input type='text' name='member_ifs.opposite.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010401.internal_links_lag_links_member_ifs_opposite_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                            <input type='text' name='member_ifs.opposite.breakout_if.breakout_if_id' class='panel_details' /> <br />" +
                "                        </fieldset>" +
                "                    </div>" +
                "                </div>" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<span data-i18n='[html]Params.P010401.management_if_address'>management_if_address</span>:" +
                "<input type='text' name='data.management_if_address' class='panel_details'><br />" +
                "<span data-i18n='[html]Params.P010401.management_if_prefix'>management_if_prefix</span>:" +
                "<input type='number' name='data.management_if_prefix' class='panel_details number'><br />" +
                "<br />" +
                "<br />" +
                "<br />"
        },


        // Spine追加
        P010501: {
            PARM: "" +
                "<span data-i18n='[html]Params.P010501.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n",

            BODY: "" +
                "<span data-i18n='[html]Params.P010501.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.equipment_type_id'>equipment_type_id</span>:" +
                "<input type='text' name='data.equipment_type_id' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.host_name'>host_name</span>:" +
                "<input type='text' name='data.host_name' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.mac_address'>mac_address</span>:" +
                "<input type='text' name='data.mac_address' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.username'>username</span>:" +
                "<input type='text' name='data.username' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.password'>password</span>:" +
                "<input type='text' name='data.password' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.provisioning'>provisioning</span>:" +
                "<input type='checkbox' name='data.provisioning' class='panel_details panel_select_checkbox' /><br />" +
                "<span data-i18n='[html]Params.P010501.snmp_community'>snmp_community</span>:" +
                "<input type='text' name='data.snmp_community' class='panel_details' /><br />" +
                "<span data-i18n='[html]Params.P010501.ntp_server_address'>ntp_server_address</span>:" +
                "<input type='text' name='data.ntp_server_address' class='panel_details' /><br />" +
                "<br />" +
                "" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010501.breakout'>breakout</span>:" +
                "        <span data-i18n='[html]Params.P010501.breakout_local'>breakout_local</span>" +
                "    </legend>" +
                "    <fieldset class='countIf_start'>" +
                "        <legend>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs'>breakout_ifs</span>" +
                "            <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "            <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "        </legend>" +
                "        <div class='collection' data-field='data.breakout.local.breakout_ifs'>" +
                "            <div class='initHideItem'>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                <span class='countIf'></span>" +
                "                <span class='itemSep2'></span>" +
                "                <fieldset class='countIfId_start'>" +
                "                    <legend>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_breakout_if_ids'>breakout_if_ids</span>" +
                "                        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                    </legend>" +
                "                    <div class='collection' data-field='breakout_ifs.breakout_if_ids'>" +
                "                        <div class='initHideItem'>" +
                "                            <span>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_breakout_if_id'>breakout_if_id</span>:" +
                "                                <span class='countIfId'></span>:" +
                "                                <input type='text' name='breakout_if_ids.' class='panel_details' />" +
                "                            </span>" +
                "                        </div>" +
                "                    </div>" +
                "                </fieldset>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_base_if'>base_if</span>:" +
                "                <span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_base_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='breakout_ifs.base_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_division_number'>division_number</span>:" +
                "                <input type='number' name='breakout_ifs.division_number' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_local_breakout_ifs_breakout_if_speed'>breakout_if_speed</span>:" +
                "                <input type='text' name='breakout_ifs.breakout_if_speed' class='panel_details' /><br />" +
                "            </div>" +
                "        </div>" +
                "    </fieldset>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010501.breakout'>breakout</span>:" +
                "        <span data-i18n='[html]Params.P010501.breakout_opposite'>opposite</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    " +
                "    <div class='collection' data-field='data.breakout.opposite'>" +
                "        <div class='initHideItem'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='opposite.opposite_node_id' class='panel_details' /><br />" +
                "            <fieldset class='countIf_start'>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs'>breakout_ifs</span>:" +
                "                    <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                    <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                </legend>" +
                "                <div class='collection' data-field='opposite.breakout_ifs'>" +
                "                    <div class='initHideItem'>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                        <span class='countIf'></span>" +
                "                        <span class='itemSep2'></span>" +
                "                        <fieldset class='countIfId_start'>" +
                "                            <legend>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_breakout_if_ids'>breakout_if_ids</span>:" +
                "                                <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                                <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                            </legend>" +
                "                            <div class='collection' data-field='breakout_ifs.breakout_if_ids'>" +
                "                                <div class='initHideItem'>" +
                "                                    <span>" +
                "                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_breakout_if_id'>breakout_if_id</span>" +
                "                                        <span class='countIfId'></span>:" +
                "                                        <input type='text' name='breakout_if_ids.' class='panel_details' />" +
                "                                    </span>" +
                "                                </div>" +
                "                            </div>" +
                "                        </fieldset>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_base_if'>base_if</span>:" +
                "                        <span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_base_if_physical_if_id'>physical_if_id</span>:" +
                "                        <input type='text' name='breakout_ifs.base_if.physical_if_id' class='panel_details' /><br />" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_division_number'>division_number</span>:" +
                "                        <input type='number' name='breakout_ifs.division_number' class='panel_details number' /><br />" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.breakout_opposite_breakout_ifs_breakout_if_speed'>breakout_if_speed</span>:" +
                "                        <input type='text' name='breakout_ifs.breakout_if_speed' class='panel_details' /><br />" +
                "                    </div>" +
                "                </div>" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010501.internal_links'>internal_links</span>:" +
                "        <span data-i18n='[html]Params.P010501.internal_links_physical_links'>physical_links</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    " +
                "    <div class='collection' data-field='data.internal_links.physical_links'>" +
                "        <div class='initHideItem'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='physical_links.opposite_node_id' class='panel_details' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_local_traffic_threshold'>local_traffic_threshold</span>:" +
                "            <input type='number' name='physical_links.local_traffic_threshold' class='panel_details number' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_opposite_traffic_threshold'>opposite_traffic_threshold</span>:" +
                "            <input type='number' name='physical_links.opposite_traffic_threshold' class='panel_details number' /><br />" +
                "            " +
                "            <fieldset>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if'>internal_link_if</span>:" +
                "                    <span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_local'>local</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_local_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.physical_if.physical_if_speed' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_local_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.local.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "            </fieldset>" +
                "            <fieldset>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if'>internal_link_if</span>:" +
                "                    <span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_opposite'>opposite</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_opposite_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_physical_links_internal_link_if_opposite_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                <input type='text' name='physical_links.internal_link_if.opposite.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010501.internal_links'>internal_links</span>:" +
                "        <span data-i18n='[html]Params.P010501.internal_links_lag_links'>lag_links</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    " +
                "    <div class='collection' data-field='data.internal_links.lag_links'>" +
                "        <div class='initHideItem'>" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_opposite_node_id'>opposite_node_id</span>:" +
                "            <input type='text' name='lag_links.opposite_node_id' class='panel_details' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_local_traffic_threshold'>local_traffic_threshold</span>:" +
                "            <input type='number' name='lag_links.local_traffic_threshold' class='panel_details number' /><br />" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_opposite_traffic_threshold'>opposite_traffic_threshold</span>:" +
                "            <input type='number' name='lag_links.opposite_traffic_threshold' class='panel_details number' /> <br />" +
                "            " +
                "            <fieldset class='countIf_start'>" +
                "                <legend>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs'>member_ifs</span>:" +
                "                    <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                    <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                </legend>" +
                "                " +
                "                <div class='collection' data-field='lag_links.member_ifs'>" +
                "                    <div class='initHideItem'>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                        <span class='countIf'></span>" +
                "                        <span class='itemSep2'></span>" +
                "                        <fieldset>" +
                "                            <legend>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_local'>local</span>" +
                "                            </legend>" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_local_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                            <input type='text' name='member_ifs.local.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_local_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                            <input type='text' name='member_ifs.local.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_local_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                            <input type='text' name='member_ifs.local.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "                        </fieldset>" +
                "                        <fieldset>" +
                "                            <legend>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_opposite'>opposite</span>" +
                "                            </legend>" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_id'>physical_if_id</span>:" +
                "                            <input type='text' name='member_ifs.opposite.physical_if.physical_if_id' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_opposite_physical_if_physical_if_speed'>physical_if_speed</span>:" +
                "                            <input type='text' name='member_ifs.opposite.physical_if.physical_if_speed' class='panel_details' /><br />" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010501.internal_links_lag_links_member_ifs_opposite_breakout_if_breakout_if_id'>breakout_if_id</span>:" +
                "                            <input type='text' name='member_ifs.opposite.breakout_if.breakout_if_id' class='panel_details' /><br />" +
                "                        </fieldset>" +
                "                    </div>" +
                "                </div>" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br />" +
                "" +
                "<span data-i18n='[html]Params.P010501.management_if_address'>management_if_address</span>:" +
                "<input type='text' name='data.management_if_address' class='panel_details'><br />" +
                "<span data-i18n='[html]Params.P010501.management_if_prefix'>management_if_prefix</span>:" +
                "<input type='number' name='data.management_if_prefix' class='panel_details number'><br />" +
                "<br /><br /><br />"
        },

        // breakout登録
        P010901_add: {
            PARM: "" +
                "<span data-i18n='[html]Params.P010901_add.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P010901_add.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P010901_add.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n",
             BODY: "" +
                "<fieldset class='countIf_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010901_add.base_if'>base_if</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countIf'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P010901_add.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P010901_add.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend>" +
                "                    <span data-i18n='[html]Params.P010901_add.value'>value</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010901_add.base_if'>base_if</span>:" +
                "                <span data-i18n='[html]Params.P010901_add.physical_if_id'>physical_if_id</span>:" +
                "                <input type='text' name='noname.value.base_if.physical_if_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010901_add.division_number'>division_number</span>:" +
                "                <input type='number' name='noname.value.division_number' class='panel_details number' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P010901_add.breakout_if_speed'>breakout_if_speed</span>:" +
                "                <input type='text' name='noname.value.breakout_if_speed' class='panel_details' /> <br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // breakout削除
        P010901_delete: {
            PARM: "" +
                "<span data-i18n='[html]Params.P010901_delete.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P010901_delete.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P010901_delete.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n",
            BODY: "" +
                "<fieldset class='countIf_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P010901_delete.noname'>breakout_ifs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countIf'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "                <span data-i18n='[html]Params.P010901_delete.op'>op</span>:" +
                "                <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "                <span data-i18n='[html]Params.P010901_delete.path'>path</span>:" +
                "                <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // CP生成
        P020201_add_l2: {
            PARM: "" +
                "<span data-i18n='[html]Params.P020201_add_l2.slice_type'>slice_type</span>:" +
                "<input type='text' name='data.slice_type' id='data.slice_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P020201_add_l2.slice_id'>slice_id</span>:" +
                "<input type='text' name='data.slice_id' id='data.slice_id' class='panel_details panel_readonly' readonly/><br />\n",
             BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P020201_add_l2.noname'>CPs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P020201_add_l2.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P020201_add_l2.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend>" +
                "                    <span data-i18n='[html]Params.P020201_add_l2.value'>value</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.cluster_id'>cluster_id</span>:" +
                "                <input type='text' name='noname.value.cluster_id' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.edge_point_id'>edge_point_id</span>:" +
                "                <input type='text' name='noname.value.edge_point_id' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.vlan_id'>vlan_id</span>:" +
                "                <input type='number' name='noname.value.vlan_id' class='panel_details number' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.pair_cp_id'>pair_cp_id</span>:" +
                "                <input type='text' name='noname.value.pair_cp_id' class='panel_details' /> <br /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.qos'>qos</span>:<br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.ingress_shaping_rate'>ingress_shaping_rate</span>:" +
                "                <input type='number' name='noname.value.qos.ingress_shaping_rate' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.egress_shaping_rate'>egress_shaping_rate</span>:" +
                "                <input type='number' name='noname.value.qos.egress_shaping_rate' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.egress_queue_menu'>egress_queue_menu</span>:" +
                "                <input type='text' name='noname.value.qos.egress_queue_menu' class='panel_details' /><br /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.esi'>esi</span>:" +
                "                <input type='text' name='noname.value.esi' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.lacp_system_id'>lacp_system_id</span>:" +
                "                <input type='text' name='noname.value.lacp_system_id' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.port_mode'>port_mode</span>:" +
                "                <input type='text' name='noname.value.port_mode' class='panel_details' /> <br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // CP生成_L3
        P020201_add_l3: {
            PARM: "" +
                "<span data-i18n='[html]Params.P020201_add_l3.slice_type' >slice_type</span>:" +
                "<input type='text' name='data.slice_type' id='data.slice_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P020201_add_l3.slice_id'>slice_id</span>:" +
                "<input type='text' name='data.slice_id' id='data.slice_id' class='panel_details panel_readonly' readonly/><br />\n",
             BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P020201_add_l3.noname'>CPs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P020201_add_l3.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P020201_add_l3.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend>" +
                "                    <span data-i18n='[html]Params.P020201_add_l3.value'>value</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.cluster_id'>cluster_id</span>:" +
                "                <input type='text' name='noname.value.cluster_id' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.edge_point_id'>edge_point_id</span>:" +
                "                <input type='text' name='noname.value.edge_point_id' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.vlan_id'>vlan_id</span>:" +
                "                <input type='number' name='noname.value.vlan_id' class='panel_details number' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.mtu'>mtu</span>:" +
                "                <input type='number' name='noname.value.mtu' class='panel_details number' /> <br /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.qos'>qos</span>:<br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.ingress_shaping_rate'>ingress_shaping_rate</span>:" +
                "                <input type='number' name='noname.value.qos.ingress_shaping_rate' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.egress_shaping_rate'>egress_shaping_rate</span>:" +
                "                <input type='number' name='noname.value.qos.egress_shaping_rate' class='panel_details number' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.egress_queue_menu'>egress_queue_menu</span>:" +
                "                <input type='text' name='noname.value.qos.egress_queue_menu' class='panel_details' /><br /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.ipv4_address'>ipv4_address</span>:" +
                "                <input type='text' name='noname.value.ipv4_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.ipv6_address'>ipv6_address</span>:" +
                "                <input type='text' name='noname.value.ipv6_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.ipv4_prefix'>ipv4_prefix</span>:" +
                "                <input type='number' name='noname.value.ipv4_prefix' class='panel_details number' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.ipv6_prefix'>ipv6_prefix</span>:" +
                "                <input type='number' name='noname.value.ipv6_prefix' class='panel_details number' /> <br /><br />" +
                "                <span id=bgp style='display:none'>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.bgp'>bgp</span>:<br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.bgp_role'>role</span>:" +
                "                    <input type='text' name='noname.value.bgp.role' class='panel_details' /> <br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.bgp_neighbor_as'>neighbor_as</span>:" +
                "                    <input type='number' name='noname.value.bgp.neighbor_as' class='panel_details number' /> <br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.bgp_neighbor_ipv4_address'>neighbor_ipv4_address</span>:" +
                "                    <input type='text' name='noname.value.bgp.neighbor_ipv4_address' class='panel_details' /> <br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.bgp_neighbor_ipv6_address'>neighbor_ipv6_address</span>:" +
                "                    <input type='text' name='noname.value.bgp.neighbor_ipv6_address' class='panel_details' /> <br /><br />" +
                "                </span>" +
                "                <span id=static_routes style='display:none'>" +
                "                <fieldset class='countIf_start'>" +
                "                    <legend>" +
                "                        &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.static_routes'>static_routes</span>" +
                "                        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                    </legend>" +
                "                    <div class='collection' data-field='noname.value.static_routes'>" +
                "                        <div class='initHideItem'>" +
                "                            <span class='itemSep1'></span>" +
                "                            <span class='countIf'></span>" +
                "                            <span class='itemSep2'></span>" +
                "                            <br />" +
                "                            <span>" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.static_routes_addr_type'>addr_type</span>:" +
                "                                <input type='text' name='static_routes.addr_type' class='panel_details' /> <br />" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.static_routes_address'>address</span>:" +
                "                                <input type='text' name='static_routes.address' class='panel_details' /> <br />" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.static_routes_prefix'>prefix</span>:" +
                "                                <input type='number' name='static_routes.prefix' class='panel_details number' /> <br />" +
                "                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.static_routes_next_hop'>next_hop</span>:" +
                "                                <input type='text' name='static_routes.next_hop' class='panel_details' /> <br />" +
                "                            </span>" +
                "                        </div>" +
                "                    </div>" +
                "                </fieldset>" +
                "                </span>" +
                "                <br />" +
                "                <span id=vrrp style='display:none'>" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.vrrp'>vrrp</span>:<br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.vrrp_group_id'>group_id</span>:" +
                "                    <input type='number' name='noname.value.vrrp.group_id' class='panel_details number' /> <br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.vrrp_role'>role</span>:" +
                "                    <input type='text' name='noname.value.vrrp.role' class='panel_details' /> <br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.vrrp_virtual_ipv4_address'>virtual_ipv4_address</span>:" +
                "                    <input type='text' name='noname.value.vrrp.virtual_ipv4_address' class='panel_details' /> <br /><br />" +
                "                </span>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l3.traffic_threshold'>traffic_threshold</span>:" +
                "                <input type='number' name='noname.value.traffic_threshold' class='panel_details number' /> <br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // CP削除
        P020201_delete: {
            PARM: "" +
                "<span data-i18n='[html]Params.P020201_delete.slice_type'>slice_type</span>:" +
                "<input type='text' name='data.slice_type' id='data.slice_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P020201_delete.slice_id'>slice_id</span>:" +
                "<input type='text' name='data.slice_id' id='data.slice_id' class='panel_details panel_readonly' readonly/><br />\n",
             BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P020201_delete.noname'>CPs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P020201_delete.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P020201_delete.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // CP変更
        P020203: {
            PARM: "" +
                "<span data-i18n='[html]Params.P020203.slice_type'>slice_type</span>:" +
                "<input type='text' name='data.slice_type' id='data.slice_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P020203.slice_id'>slice_id</span>:" +
                "<input type='text' name='data.slice_id' id='data.slice_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P020203.cp_id'>cp_id</span>:" +
                "<input type='text' name='data.cp_id' id='data.cp_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P020203.action'>action</span>:" +
                "<input type='text' name='data.action' class='panel_details panel_readonly' readonly/><br />" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P020203.update_option'>update_option</span>:<br />" +
                "    </legend>" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020203.qos_update_option'>qos_update_option</span>:<br />" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020203.ingress_shaping_rate'>ingress_shaping_rate</span>:" +
                "    <input type='number' name='data.update_option.qos_update_option.ingress_shaping_rate' class='panel_details number' /><br />" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020203.egress_shaping_rate'>egress_shaping_rate</span>:" +
                "    <input type='number' name='data.update_option.qos_update_option.egress_shaping_rate' class='panel_details number' /><br />" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020203.egress_queue_menu'>egress_queue_menu</span>:" +
                "    <input type='text' name='data.update_option.qos_update_option.egress_queue_menu' class='panel_details' /><br />" +
                "</fieldset>" +
                "<br /><br /><br />"
        }
};

