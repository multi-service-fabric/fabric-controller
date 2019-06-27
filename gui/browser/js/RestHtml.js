// 定数定義
MSF.Html = {
        // Leaf追加(L2)
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

                "<span data-i18n='[html]Params.P010401.irb_type'>irb_type</span>:" +
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.irb_type' value='asymmetric' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.asymmetric'>asymmetric</span>"+
                "    <input type='radio' name='data.irb_type' value='symmetric' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.symmetric'>symmetric</span>"+
                "    <input type='radio' name='data.irb_type' value='none' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P010401.none'>none</span>"+
                "</span><br />" +

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

        // Leaf追加(L3)
        P010401_l3: {
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

        // L2スライス生成
        P020101_l2: {
            PARM: "" +
                "<span data-i18n='[html]Params.P020101_l2.slice_type'>slice_type</span>:" +
                "<input type='text' name='data.slice_type' id='data.slice_type' class='panel_details panel_readonly' readonly/><br />\n",
             BODY: "" +
                "<span data-i18n='[html]Params.P020101_l2.slice_id'>slice_id</span>:" +
                "<input type='text' name='data.slice_id' class='panel_details'><br />" +
                "<span data-i18n='[html]Params.P020101_l2.remark_menu'>remark_menu</span>:" +
                "<input type='text' name='data.remark_menu' class='panel_details'><br />" +
                "<span data-i18n='[html]Params.P020101_l2.irb_type'>irb_type</span>:" +

                //ラジオボタンでの実装
                /*
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.irb_type' value='asymmetric' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P020101_l2.asymmetric'>asymmetric</span>"+
                "    <input type='radio' name='data.irb_type' value='symmetric' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P020101_l2.symmetric'>symmetric</span>"+
                "    <input type='radio' name='data.irb_type' value='none' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P020101_l2.none'>none</span>"+
                "</span><br />" +
                */

                "<span class='panel_details'>" +
                "    <span id='sliceTypeSelectSection'>" +
                "        <table>" +
                "            <tr>" +
                "                <td id='irbTypeList' class='radioGroup'>" +
                "                    <input id='irbTypeAsymmetric' type='radio' name='data.irb_type' value='asymmetric' class='panel_select_redio' />" +
                "                    <label for='irbTypeAsymmetric' data-i18n='[html]Params.P020101_l2.asymmetric'>asymmetric</label>" +
                "                    <input id='irbTypeSymmetric' type='radio' name='data.irb_type' value='symmetric' class='panel_select_redio' />" +
                "                    <label for='irbTypeSymmetric' data-i18n='[html]Params.P020101_l2.symmetric'>symmetric</label>" +
                "                    <input id='irbTypeNone' type='radio' name='data.irb_type' value='none' class='panel_select_redio' />" +
                "                    <label for='irbTypeNone' data-i18n='[html]Params.P020101_l2.none'>none</label>" +
                "                </td>" +
                "            </tr>" +
                "        </table>" +
                "    </span>" +
                "</span>" +

                "<br /><br /><br />"
        },

        // IF閉塞状態変更
        P011501: {
            PARM: "" +
                "<span data-i18n='[html]Params.P011501.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011501.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011501.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011501.if_type'>if_type</span>:" +
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.if_type' value='physical-ifs' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P011501.physical-ifs'>physical-ifs</span>"+
                "    <input type='radio' name='data.if_type' value='lag-ifs' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P011501.lag-ifs'>lag-ifs</span>"+
                "    <input type='radio' name='data.if_type' value='breakout-ifs' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P011501.breakout-ifs'>breakout-ifs</span>"+
                "</span><br />" +
                "<span data-i18n='[html]Params.P011501.if_id'>if_id</span>:" +
                "<input type='text' name='data.if_id' id='data.if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<br />" +
                "<span data-i18n='[html]Params.P011501.blockade_status'>blockade_status</span>:" +
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.blockade_status' value='down' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P011501.down'>down</span>"+
                "    <input type='radio' name='data.blockade_status' value='up' class='panel_select_radio' />" +
                "    <span data-i18n='[html]Params.P011501.up'>up</span>"+
                "</span><br />" +
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
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.irb'>irb</span>:<br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.irb_ipv4_address'>irb_ipv4_address</span>:" +
                "                <input type='text' name='noname.value.irb.irb_ipv4_address' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.vga_ipv4_address'>vga_ipv4_address</span>:" +
                "                <input type='text' name='noname.value.irb.vga_ipv4_address' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.ipv4_address_prefix'>ipv4_address_prefix</span>:" +
                "                <input type='number' name='noname.value.irb.ipv4_address_prefix' class='panel_details' /><br /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.port_mode'>port_mode</span>:" +
                "                <input type='text' name='noname.value.port_mode' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P020201_add_l2.traffic_threshold'>traffic_threshold</span>:" +
                "                <input type='number' name='noname.value.traffic_threshold' class='panel_details number' /> <br />" +
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
        },

        // 内部リンクIF優先度変更
        P060101: {
            PARM: "" +
                "<span data-i18n='[html]Params.P060101.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P060101.fabric_type'>fabric_type</span>:" +
                "<span class='panel_details'>" +
                "    <input type='radio' name='data.fabric_type' value='leafs' class='panel_select_radio'>" +
                "    <span data-i18n='[html]Params.P060101.leafs'>leafs</span>" +
                "    &nbsp;&nbsp;&nbsp;" +
                "    <input type='radio' name='data.fabric_type' value='spines' class='panel_select_radio'>" +
                "    <span data-i18n='[html]Params.P060101.spines'>spines</span>" +
                "</span><br />" +
                "<span data-i18n='[html]Params.P060101.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details' /><br />\n" +
                "<span data-i18n='[html]Params.P060101.internal_link_if_id'>internal_link_if_id</span>:" +
                "<input type='text' name='data.internal_link_if_id' id='data.internal_link_if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P060101.update_option'>update_option</span>:<br />" +
                "    </legend>" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P060101.igp_cost'>igp_cost</span>:" +
                "    <input type='number' name='data.update_option.igp_cost' class='panel_details number' /><br />" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 優先装置グループ装置追加
        P060201_add: {
            PARM: "" +
                "<span data-i18n='[html]Params.P060201_add.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P060201_add.op'>op</span>:" +
                "<input type='text' name='data.op' class='panel_details panel_readonly' readonly/><br />" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P060201_add.nodes'>nodes</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "<form>"+
                "            <span data-i18n='[html]Params.P060201_add.path'>path</span>:<br />" +
                "            &nbsp;<span data-i18n='[html]Params.P060201_add.fabric_type'>fabric_type</span>:" +
                "            <span class='panel_details'>" +
                "                <input type='radio' name='noname.fabric_type' value='leafs' class='panel_select_radio' checked='checked'>" +
                "                <span data-i18n='[html]Params.P060201_add.leafs'>leafs</span>" +
                "                &nbsp;&nbsp;&nbsp;" +
                "                <input type='radio' name='noname.fabric_type' value='spines' class='panel_select_radio'>" +
                "                <span data-i18n='[html]Params.P060201_add.spines'>spines</span>" +
                "            </span><br />" +
                "</form>"+
                "            &nbsp;<span data-i18n='[html]Params.P060201_add.node_id'>node_id</span>:" +
                "            <input type='number' name='noname.node_id' class='panel_details' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 優先装置グループ装置削除
        P060201_delete: {
            PARM: "" +
                "<span data-i18n='[html]Params.P060201_delete.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P060201_delete.op'>op</span>:" +
                "<input type='text' name='data.op' class='panel_details panel_readonly' readonly/><br />" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P060201_delete.nodes'>nodes</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "<form>"+
                "            <span data-i18n='[html]Params.P060201_delete.path'>path</span>:<br />" +
                "            &nbsp;<span data-i18n='[html]Params.P060201_delete.fabric_type'>fabric_type</span>:" +
                "            <span class='panel_details'>" +
                "                <input type='radio' name='noname.fabric_type" + "" + "' value='leafs' class='panel_select_radio' checked>" +
                "                <span data-i18n='[html]Params.P060201_delete.leafs'>leafs</span>" +
                "                &nbsp;&nbsp;&nbsp;" +
                "                <input type='radio' name='noname.fabric_type" + "" + "' value='spines' class='panel_select_radio'>" +
                "                <span data-i18n='[html]Params.P060201_delete.spines'>spines</span>" +
                "            </span><br />" +
                "</form>"+
                "            &nbsp;<span data-i18n='[html]Params.P060201_delete.node_id'>node_id</span>:" +
                "            <input type='number' name='noname.node_id' class='panel_details' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 物理IFフィルタ追加
        P070104_add: {
            PARM: "" +
                "<span data-i18n='[html]Params.P070104_add.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_add.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_add.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_add.physical_if_id'>physical_if_id</span>:" +
                "<input type='text' name='data.physical_if_id' id='data.physical_if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P070104_add.noname'>physical_if_ACLs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P070104_add.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P070104_add.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend>" +
                "                    <span data-i18n='[html]Params.P070104_add.value'>value</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.source_mac_address'>source_mac_address</span>:" +
                "                <input type='text' name='noname.value.source_mac_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.dest_mac_address'>dest_mac_address</span>:" +
                "                <input type='text' name='noname.value.dest_mac_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.source_ip_address'>source_ip_address</span>:" +
                "                <input type='text' name='noname.value.source_ip_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.dest_ip_address'>dest_ip_address</span>:" +
                "                <input type='text' name='noname.value.dest_ip_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.protocol'>protocol</span>:" +
                "                <input type='text' name='noname.value.protocol' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.source_port'>source_port</span>:" +
                "                <input type='text' name='noname.value.source_port' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070104_add.dest_port'>dest_port</span>:" +
                "                <input type='text' name='noname.value.dest_port' class='panel_details' /> <br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 物理IFフィルタ削除
        P070104_delete: {
            PARM: "" +
                "<span data-i18n='[html]Params.P070104_delete.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_delete.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_delete.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070104_delete.physical_if_id'>physical_if_id</span>:" +
                "<input type='text' name='data.physical_if_id' id='data.physical_if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P070104_delete.noname'>physical_if_ACLs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P070104_delete.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P070104_delete.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // LagIFフィルタ追加
        P070107_add: {
            PARM: "" +
                "<span data-i18n='[html]Params.P070107_add.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_add.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_add.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_add.lag_if_id'>lag_if_id</span>:" +
                "<input type='text' name='data.lag_if_id' id='data.lag_if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P070107_add.noname'>lag_if_ACLs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P070107_add.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P070107_add.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "            <fieldset>" +
                "                <legend>" +
                "                    <span data-i18n='[html]Params.P070107_add.value'>value</span>" +
                "                </legend>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.source_mac_address'>source_mac_address</span>:" +
                "                <input type='text' name='noname.value.source_mac_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.dest_mac_address'>dest_mac_address</span>:" +
                "                <input type='text' name='noname.value.dest_mac_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.source_ip_address'>source_ip_address</span>:" +
                "                <input type='text' name='noname.value.source_ip_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.dest_ip_address'>dest_ip_address</span>:" +
                "                <input type='text' name='noname.value.dest_ip_address' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.protocol'>protocol</span>:" +
                "                <input type='text' name='noname.value.protocol' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.source_port'>source_port</span>:" +
                "                <input type='text' name='noname.value.source_port' class='panel_details' /> <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P070107_add.dest_port'>dest_port</span>:" +
                "                <input type='text' name='noname.value.dest_port' class='panel_details' /> <br />" +
                "            </fieldset>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // LagIFフィルタ削除
        P070107_delete: {
            PARM: "" +
                "<span data-i18n='[html]Params.P070107_delete.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_delete.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_delete.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P070107_delete.lag_if_id'>lag_if_id</span>:" +
                "<input type='text' name='data.lag_if_id' id='data.lag_if_id' class='panel_details' /><br />\n",
            BODY: "" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P070107_delete.noname'>lag_if_ACLs</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.noname'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countNode'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P070107_delete.op'>op</span>:" +
                "            <input type='text' name='noname.op' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P070107_delete.path'>path</span>:" +
                "            <input type='text' name='noname.path' class='panel_details' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 装置OSアップグレード
        P080201: {
            PARM: "" +
                "\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P080201.reservation_time'>reservation_time</span>:" +
                "<input type='text' name='data.reservation_time' class='panel_details' placeholder='YYYYMMDD_hhmmss' /><br />" +
                "<span data-i18n='[html]Params.P080201.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' class='panel_details panel_readonly' readonly/><br />" +
                "<br />" +
                "<fieldset>" +
                "    <legend class='countIf_start'>" +
                "        <span data-i18n='[html]Params.P080201.nodes'>nodes</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.nodes'>" +
                "        <div class='initHideItem'>" +
                "            <form>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span class='itemSep1'></span>" +
                "                <span class='countIf'></span>" +
                "                <span class='itemSep2'></span>" +
                "                <br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.fabric_type'>fabric_type</span>:" +
                "                <span class='panel_details'>" +
                "                    <input type='radio' name='nodes.fabric_type' value=leaf class='panel_select_radio' checked='checked' />" +
                "                    <span data-i18n='[html]Params.P080201.leaf'>leaf</span>&nbsp;&nbsp;&nbsp;"+
                "                    <input type='radio' name='nodes.fabric_type' value=spine class='panel_select_radio' />" +
                "                    <span data-i18n='[html]Params.P080201.spine'>spine</span>&nbsp;&nbsp;&nbsp;"+
                "                </span><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.node_id'>node_id</span>:" +
                "                <input type='text' name='nodes.node_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.equipment_type_id'>equipment_type_id</span>:" +
                "                <input type='text' name='nodes.equipment_type_id' class='panel_details' /><br />" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.os_upgrade'>os_upgrade</span>:<br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.upgrade_script_path'>upgrade_script_path</span>:" +
                "                    <input type='text' name='nodes.os_upgrade.upgrade_script_path' class='panel_details' /><br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.ztp_flag'>ztp_flag</span>:" +
                "                    <span class='panel_details'>" +
                "                        <input type='radio' name='nodes.os_upgrade.ztp_flag' value=true class='panel_select_radio' checked='checked' />" +
                "                        <span data-i18n='[html]Params.P080201.true'>true</span>&nbsp;&nbsp;&nbsp;"+
                "                        <input type='radio' name='nodes.os_upgrade.ztp_flag' value=false class='panel_select_radio' />" +
                "                        <span data-i18n='[html]Params.P080201.false'>false</span>&nbsp;&nbsp;&nbsp;"+
                "                    </span><br />" +
                "                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.upgrade_complete_msg'>upgrade_complete_msg</span>:" +
                "                    <input type='text' name='nodes.os_upgrade.upgrade_complete_msg' class='panel_details' /><br />" +
                "                    <fieldset>" +
                "                        <legend class='countIfId_start'>" +
                "                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.upgrade_error_msgs'>upgrade_error_msgs</span>" +
                "                            <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "                            <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "                        </legend>" +
                "                        <div class='collection' data-field='nodes.os_upgrade.upgrade_error_msgs'>" +
                "                            <div class='initHideItem'>" +
                "                                <span>" +
                "                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.upgrade_error_msg'>upgrade_error_msg</span>" +
                "                                    <span class='countIfId'></span>:" +
                "                                    <input type='text' name='upgrade_error_msgs.' class='panel_details' />" +
                "                                </span>" +
                "                            </div>" +
                "                        </div>" +
                "                    </fieldset>" +
                "                &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080201.operator_check'>operator_check</span>:" +
                "                <span class='panel_details'>" +
                "                    <input type='radio' name='nodes.operator_check' value=true class='panel_select_radio' checked='checked' />" +
                "                    <span data-i18n='[html]Params.P080201.true'>true</span>&nbsp;&nbsp;&nbsp;"+
                "                    <input type='radio' name='nodes.operator_check' value=false class='panel_select_radio' />" +
                "                    <span data-i18n='[html]Params.P080201.false'>false</span>&nbsp;&nbsp;&nbsp;"+
                "                </span><br />" +
                "            </form>" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        },

        // 装置迂回
        P080101: {
            PARM: "" +
                "<span data-i18n='[html]Params.P080101.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P080101.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P080101.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P080101.action'>action</span>:" +
                "<input type='text' name='data.action' class='panel_details panel_readonly' readonly/><br />" +
                "<fieldset>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P080101.update_option'>update_option</span>:<br />" +
                "    </legend>" +
                "    &nbsp;&nbsp;&nbsp;&nbsp;<span data-i18n='[html]Params.P080101.detoured'>detoured</span>:" +
                "    <span class='panel_details'>" +
                "        <input type='radio' name='data.update_option.detoured' value=true class='panel_select_radio' checked=true />" +
                "        <span data-i18n='[html]Params.P080101.true'>true</span>&nbsp;&nbsp;&nbsp;"+
                "        <input type='radio' name='data.update_option.detoured' value=false class='panel_select_radio' />" +
                "        <span data-i18n='[html]Params.P080101.false'>false</span>&nbsp;&nbsp;&nbsp;"+
                "    </span><br />" +
                "</fieldset>" +
                "<br /><br /><br />"
        },
        
         //LagIF情報変更
        P011104: {
            PARM: "" +
                "<span data-i18n='[html]Params.P011104.cluster_id'>cluster_id</span>:" +
                "<input type='text' name='data.cluster_id' id='data.cluster_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011104.fabric_type'>fabric_type</span>:" +
                "<input type='text' name='data.fabric_type' id='data.fabric_type' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011104.node_id'>node_id</span>:" +
                "<input type='text' name='data.node_id' id='data.node_id' class='panel_details panel_readonly' readonly/><br />\n" +
                "<span data-i18n='[html]Params.P011104.lag_if_id'>lag_if_id</span>:" +
                "<input type='text' name='data.lag_if_id' id='data.lag_if_id' class='panel_details'/><br />\n",
            BODY: "" +
                "<span data-i18n='[html]Params.P011104.action'>action</span>:" +
                "<input type='text' name='data.action' id='data.action' class='panel_details panel_readonly' readonly/><br />\n" +
                "<fieldset class='countNode_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P011104.physical_if_ids'>physical_if</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "        <span class='showItem' onclick='javascript:showAllChildItem($(this));'>▼</span>" +
                "        <span class='showItem2' onclick='javascript:hideAllChildItem($(this));'>▲</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.physical_if_ids'>" +
                "         <div class='initHideItem'>" +
                "             <span class='itemSep1'></span>" +
                "             <span class='countNode'></span>" +
                "             <span class='itemSep2'></span>" +
                "             <br />" +
                "             <span data-i18n='[html]Params.P011104.id'>id</span>:" +
                "             <input type='text' name='physical_if_ids.' class='panel_details panel_readonly' readonly/><br />" +
                "             <span [html]Params.P011104.op>op</span>:" +
                "             <input type='checkbox' class='panel_details panel_select_checkbox' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<fieldset class='countIf_start'>" +
                "    <legend>" +
                "        <span data-i18n='[html]Params.P011104.breakout_if_ids'>breakout_if</span>" +
                "        <span class='showItem' onclick='javascript:showChildItem($(this));'>▽</span>" +
                "        <span class='hideItem' onclick='javascript:hideChildItem($(this));'>△</span>" +
                "        <span class='showItem' onclick='javascript:showAllChildItem($(this));'>▼</span>" +
                "        <span class='showItem2' onclick='javascript:hideAllChildItem($(this));'>▲</span>" +
                "    </legend>" +
                "    <div class='collection' data-field='data.breakout_if_ids'>" +
                "        <div class='initHideItem'>" +
                "            <span class='itemSep1'></span>" +
                "            <span class='countIf'></span>" +
                "            <span class='itemSep2'></span>" +
                "            <br />" +
                "            <span data-i18n='[html]Params.P011104.id'>id</span>:" +
                "            <input type='text' name='breakout_if_ids.' class='panel_details panel_readonly' readonly/><br />" +
                "            <span data-i18n='[html]Params.P011104.op'>op</span>:" +
                "            <input type='checkbox' name='breakout_if_ops.' value=true class='panel_details panel_select_checkbox' /><br />" +
                "        </div>" +
                "    </div>" +
                "</fieldset>" +
                "<br /><br /><br />"
        } 
};

