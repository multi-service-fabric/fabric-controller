
/* Drop Tables */

DROP TABLE IF EXISTS async_requests;
DROP TABLE IF EXISTS cluster_link_ifs;
DROP TABLE IF EXISTS cp_filter_info;
DROP TABLE IF EXISTS l2_cps;
DROP TABLE IF EXISTS l3_cps;
DROP TABLE IF EXISTS edge_points;
DROP TABLE IF EXISTS internal_link_ifs;
DROP TABLE IF EXISTS breakout_ifs;
DROP TABLE IF EXISTS cp_ids;
DROP TABLE IF EXISTS lag_if_filter_info;
DROP TABLE IF EXISTS lag_ifs;
DROP TABLE IF EXISTS leaf_nodes;
DROP TABLE IF EXISTS physical_if_filter_info;
DROP TABLE IF EXISTS physical_ifs;
DROP TABLE IF EXISTS nodes;
DROP TABLE IF EXISTS equipments;
DROP TABLE IF EXISTS esi_ids;
DROP TABLE IF EXISTS vlan_ifs;
DROP TABLE IF EXISTS irb_instances;
DROP TABLE IF EXISTS l2_slices;
DROP TABLE IF EXISTS l3_slices;
DROP TABLE IF EXISTS lag_if_ids;
DROP TABLE IF EXISTS node_operation_info;
DROP TABLE IF EXISTS slice_ids;
DROP TABLE IF EXISTS sw_clusters;
DROP TABLE IF EXISTS system_status;
DROP TABLE IF EXISTS vlan_if_ids;
DROP TABLE IF EXISTS vrf_ids;




/* Create Tables */

CREATE TABLE async_requests
(
	operation_id text NOT NULL,
	occurred_time timestamp with time zone NOT NULL,
	last_update_time timestamp with time zone NOT NULL,
	status int NOT NULL,
	sub_status text,
	request_uri text,
	request_method varchar(6),
	request_body text,
	response_status_code int,
	response_body text,
	notification_ip_address text,
	notification_port_number int,
	PRIMARY KEY (operation_id)
) WITHOUT OIDS;


CREATE TABLE breakout_ifs
(
	breakout_if_info_id bigserial NOT NULL,
	node_info_id bigint NOT NULL,
	breakout_if_id text NOT NULL,
	PRIMARY KEY (breakout_if_info_id),
	CONSTRAINT breakout_composite_id UNIQUE (node_info_id, breakout_if_id)
) WITHOUT OIDS;


CREATE TABLE cluster_link_ifs
(
	cluster_link_if_id bigint NOT NULL,
	physical_if_info_id bigint,
	lag_if_id int,
	breakout_if_info_id bigint,
	traffic_threshold double precision,
	PRIMARY KEY (cluster_link_if_id)
) WITHOUT OIDS;


CREATE TABLE cp_filter_info
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (slice_id, cp_id, term_id)
) WITHOUT OIDS;


CREATE TABLE cp_ids
(
	layer_type int NOT NULL,
	slice_id text NOT NULL,
	next_id int DEFAULT 1 NOT NULL,
	PRIMARY KEY (layer_type, slice_id)
) WITHOUT OIDS;


CREATE TABLE edge_points
(
	edge_point_id int NOT NULL,
	physical_if_info_id bigint,
	lag_if_id int,
	breakout_if_info_id bigint,
	traffic_threshold double precision,
	PRIMARY KEY (edge_point_id)
) WITHOUT OIDS;


CREATE TABLE equipments
(
	equipment_type_id int NOT NULL,
	PRIMARY KEY (equipment_type_id)
) WITHOUT OIDS;


CREATE TABLE esi_ids
(
	sw_cluster_id_1 int NOT NULL,
	sw_cluster_id_2 int NOT NULL,
	next_id int NOT NULL,
	PRIMARY KEY (sw_cluster_id_1, sw_cluster_id_2)
) WITHOUT OIDS;


CREATE TABLE internal_link_ifs
(
	internal_link_if_id int NOT NULL,
	physical_if_info_id bigint,
	lag_if_id int,
	breakout_if_info_id bigint,
	traffic_threshold double precision,
	opposite_internal_link_if_id int,
	igp_cost int NOT NULL,
	PRIMARY KEY (internal_link_if_id)
) WITHOUT OIDS;


CREATE TABLE irb_instances
(
	irb_instance_id bigserial NOT NULL,
	node_info_id int NOT NULL,
	vlan_id int NOT NULL,
	vni int NOT NULL,
	slice_id text NOT NULL,
	PRIMARY KEY (irb_instance_id),
	CONSTRAINT irb_composite_id UNIQUE (node_info_id, vlan_id)
) WITHOUT OIDS;


CREATE TABLE l2_cps
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	esi text,
	clag_id int,
	vlan_if_id int NOT NULL,
	edge_point_id int NOT NULL,
	node_info_id int NOT NULL,
	traffic_threshold double precision,
	PRIMARY KEY (slice_id, cp_id)
) WITHOUT OIDS;


CREATE TABLE l2_slices
(
	slice_id text NOT NULL,
	vrf_id int UNIQUE,
	vni int UNIQUE,
	remark_menu text,
	irb_type int,
	l3vni int,
	l3vni_vlan_id int,
	PRIMARY KEY (slice_id)
) WITHOUT OIDS;


CREATE TABLE l3_cps
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	vlan_if_id int NOT NULL,
	edge_point_id int NOT NULL,
	node_info_id int NOT NULL,
	traffic_threshold double precision,
	PRIMARY KEY (slice_id, cp_id)
) WITHOUT OIDS;


CREATE TABLE l3_slices
(
	slice_id text NOT NULL,
	vrf_id int NOT NULL UNIQUE,
	plane int NOT NULL,
	remark_menu text,
	PRIMARY KEY (slice_id)
) WITHOUT OIDS;


CREATE TABLE lag_ifs
(
	lag_if_id int NOT NULL,
	node_info_id bigint NOT NULL,
	PRIMARY KEY (lag_if_id)
) WITHOUT OIDS;


CREATE TABLE lag_if_filter_info
(
	lag_if_id int NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (lag_if_id, term_id)
) WITHOUT OIDS;


CREATE TABLE lag_if_ids
(
	next_id int DEFAULT 801 NOT NULL,
	PRIMARY KEY (next_id)
) WITHOUT OIDS;


CREATE TABLE leaf_nodes
(
	node_info_id bigint NOT NULL,
	leaf_type int NOT NULL,
	PRIMARY KEY (node_info_id)
) WITHOUT OIDS;


CREATE TABLE nodes
(
	node_info_id bigserial NOT NULL,
	equipment_type_id int NOT NULL,
	node_type int NOT NULL,
	node_id int NOT NULL,
	ec_node_id int NOT NULL,
	is_priority_node_group_member boolean DEFAULT 'false' NOT NULL,
	PRIMARY KEY (node_info_id),
	CONSTRAINT device_id UNIQUE (node_type, node_id)
) WITHOUT OIDS;


CREATE TABLE node_operation_info
(
	node_operation_status int NOT NULL,
	PRIMARY KEY (node_operation_status)
) WITHOUT OIDS;


CREATE TABLE physical_ifs
(
	physical_if_info_id bigserial NOT NULL,
	node_info_id bigint NOT NULL,
	physical_if_id text NOT NULL,
	PRIMARY KEY (physical_if_info_id),
	CONSTRAINT physical_composite_id UNIQUE (node_info_id, physical_if_id)
) WITHOUT OIDS;


CREATE TABLE physical_if_filter_info
(
	physical_if_info_id bigint NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (physical_if_info_id, term_id)
) WITHOUT OIDS;


CREATE TABLE slice_ids
(
	layer_type int NOT NULL,
	next_id int DEFAULT 1 NOT NULL,
	PRIMARY KEY (layer_type)
) WITHOUT OIDS;


CREATE TABLE sw_clusters
(
	sw_cluster_id int NOT NULL,
	max_leaf_num int NOT NULL,
	max_spine_num int NOT NULL,
	max_rr_num int NOT NULL,
	ec_control_address text NOT NULL,
	ec_control_port int NOT NULL,
	as_num int NOT NULL,
	ospf_area int NOT NULL,
	spine_start_pos int NOT NULL,
	leaf_start_pos int NOT NULL,
	rr_start_pos int NOT NULL,
	fc_start_pos int NOT NULL,
	ec_start_pos int NOT NULL,
	em_start_pos int NOT NULL,
	inchannel_start_address varchar(15) NOT NULL,
	outchannel_start_address varchar(15) NOT NULL,
	route_aggregation_start_address varchar(15) NOT NULL,
	route_aggregation_address_prefix int NOT NULL,
	internal_link_normal_igp_cost int NOT NULL,
	PRIMARY KEY (sw_cluster_id)
) WITHOUT OIDS;


CREATE TABLE system_status
(
	system_id int NOT NULL,
	service_status int NOT NULL,
	blockade_status int NOT NULL,
	PRIMARY KEY (system_id)
) WITHOUT OIDS;


CREATE TABLE vlan_ifs
(
	node_info_id int NOT NULL,
	vlan_if_id int NOT NULL,
	irb_instance_id bigint,
	PRIMARY KEY (node_info_id, vlan_if_id)
) WITHOUT OIDS;


CREATE TABLE vlan_if_ids
(
	node_info_id int NOT NULL,
	next_id int NOT NULL,
	PRIMARY KEY (node_info_id)
) WITHOUT OIDS;


CREATE TABLE vrf_ids
(
	layer_type int NOT NULL,
	next_id int DEFAULT 1 NOT NULL,
	PRIMARY KEY (layer_type)
) WITHOUT OIDS;



/* Create Foreign Keys */

ALTER TABLE cluster_link_ifs
	ADD FOREIGN KEY (breakout_if_info_id)
	REFERENCES breakout_ifs (breakout_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE edge_points
	ADD FOREIGN KEY (breakout_if_info_id)
	REFERENCES breakout_ifs (breakout_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE internal_link_ifs
	ADD FOREIGN KEY (breakout_if_info_id)
	REFERENCES breakout_ifs (breakout_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l2_cps
	ADD FOREIGN KEY (edge_point_id)
	REFERENCES edge_points (edge_point_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l3_cps
	ADD FOREIGN KEY (edge_point_id)
	REFERENCES edge_points (edge_point_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE nodes
	ADD FOREIGN KEY (equipment_type_id)
	REFERENCES equipments (equipment_type_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE internal_link_ifs
	ADD FOREIGN KEY (opposite_internal_link_if_id)
	REFERENCES internal_link_ifs (internal_link_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE vlan_ifs
	ADD FOREIGN KEY (irb_instance_id)
	REFERENCES irb_instances (irb_instance_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE cp_filter_info
	ADD FOREIGN KEY (slice_id, cp_id)
	REFERENCES l2_cps (slice_id, cp_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE irb_instances
	ADD FOREIGN KEY (slice_id)
	REFERENCES l2_slices (slice_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l2_cps
	ADD FOREIGN KEY (slice_id)
	REFERENCES l2_slices (slice_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l3_cps
	ADD FOREIGN KEY (slice_id)
	REFERENCES l3_slices (slice_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE cluster_link_ifs
	ADD FOREIGN KEY (lag_if_id)
	REFERENCES lag_ifs (lag_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE edge_points
	ADD FOREIGN KEY (lag_if_id)
	REFERENCES lag_ifs (lag_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE internal_link_ifs
	ADD FOREIGN KEY (lag_if_id)
	REFERENCES lag_ifs (lag_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE lag_if_filter_info
	ADD FOREIGN KEY (lag_if_id)
	REFERENCES lag_ifs (lag_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE breakout_ifs
	ADD FOREIGN KEY (node_info_id)
	REFERENCES nodes (node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE lag_ifs
	ADD FOREIGN KEY (node_info_id)
	REFERENCES nodes (node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE leaf_nodes
	ADD FOREIGN KEY (node_info_id)
	REFERENCES nodes (node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE physical_ifs
	ADD FOREIGN KEY (node_info_id)
	REFERENCES nodes (node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE cluster_link_ifs
	ADD FOREIGN KEY (physical_if_info_id)
	REFERENCES physical_ifs (physical_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE edge_points
	ADD FOREIGN KEY (physical_if_info_id)
	REFERENCES physical_ifs (physical_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE internal_link_ifs
	ADD FOREIGN KEY (physical_if_info_id)
	REFERENCES physical_ifs (physical_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE physical_if_filter_info
	ADD FOREIGN KEY (physical_if_info_id)
	REFERENCES physical_ifs (physical_if_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l2_cps
	ADD FOREIGN KEY (vlan_if_id, node_info_id)
	REFERENCES vlan_ifs (vlan_if_id, node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l3_cps
	ADD FOREIGN KEY (vlan_if_id, node_info_id)
	REFERENCES vlan_ifs (vlan_if_id, node_info_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



