
/* Drop Tables */

DROP TABLE IF EXISTS async_requests_for_lower_rollback;
DROP TABLE IF EXISTS async_requests_for_lower;
DROP TABLE IF EXISTS async_requests;
DROP TABLE IF EXISTS lag_links;
DROP TABLE IF EXISTS physical_links;
DROP TABLE IF EXISTS cluster_link_ifs;
DROP TABLE IF EXISTS cluster_link_if_ids;
DROP TABLE IF EXISTS cp_ids;
DROP TABLE IF EXISTS equipments;
DROP TABLE IF EXISTS esi_ids;
DROP TABLE IF EXISTS l2_cps;
DROP TABLE IF EXISTS l2_slices;
DROP TABLE IF EXISTS l3_cps;
DROP TABLE IF EXISTS l3_slices;
DROP TABLE IF EXISTS slice_ids;
DROP TABLE IF EXISTS sw_clusters;
DROP TABLE IF EXISTS system_status;
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
	notification_ip_address text NOT NULL,
	notification_port_number int NOT NULL,
	PRIMARY KEY (operation_id)
) WITHOUT OIDS;


CREATE TABLE async_requests_for_lower
(
	cluster_id int NOT NULL,
	request_operation_id text NOT NULL,
	operation_id text NOT NULL,
	occurred_time timestamp with time zone NOT NULL,
	last_update_time timestamp with time zone NOT NULL,
	request_uri text NOT NULL,
	request_method varchar(6) NOT NULL,
	request_body text NOT NULL,
	response_status_code int,
	response_body text,
	PRIMARY KEY (cluster_id, request_operation_id)
) WITHOUT OIDS;


CREATE TABLE async_requests_for_lower_rollback
(
	cluster_id int NOT NULL,
	request_operation_id text NOT NULL,
	rollback_operation_id text NOT NULL,
	occurred_time timestamp with time zone NOT NULL,
	last_update_time timestamp with time zone NOT NULL,
	request_uri text NOT NULL,
	request_method varchar(6) NOT NULL,
	request_body text NOT NULL,
	response_status_code int,
	response_body text,
	PRIMARY KEY (cluster_id, request_operation_id)
) WITHOUT OIDS;


CREATE TABLE cluster_link_ifs
(
	cluster_link_if_id int NOT NULL,
	sw_cluster_id int NOT NULL,
	opposite_sw_cluster_id int NOT NULL,
	igp_cost int NOT NULL,
	port_status boolean NOT NULL,
	ipv4_addr text NOT NULL,
	traffic_threshold double precision,
	PRIMARY KEY (cluster_link_if_id)
) WITHOUT OIDS;


CREATE TABLE cluster_link_if_ids
(
	next_id int NOT NULL,
	PRIMARY KEY (next_id)
) WITHOUT OIDS;


CREATE TABLE cp_ids
(
	layer_type int NOT NULL,
	slice_id text NOT NULL,
	next_id int DEFAULT 1 NOT NULL,
	PRIMARY KEY (layer_type, slice_id)
) WITHOUT OIDS;


CREATE TABLE equipments
(
	equipment_type_info_id int NOT NULL,
	PRIMARY KEY (equipment_type_info_id)
) WITHOUT OIDS;


CREATE TABLE esi_ids
(
	sw_cluster_id_1 int NOT NULL,
	sw_cluster_id_2 int NOT NULL,
	next_id int NOT NULL,
	PRIMARY KEY (sw_cluster_id_1, sw_cluster_id_2)
) WITHOUT OIDS;


CREATE TABLE l2_cps
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	esi text,
	sw_cluster_id int NOT NULL,
	PRIMARY KEY (slice_id, cp_id)
) WITHOUT OIDS;


CREATE TABLE l2_slices
(
	slice_id text NOT NULL,
	vrf_id int NOT NULL UNIQUE,
	remark_menu text,
	PRIMARY KEY (slice_id)
) WITHOUT OIDS;


CREATE TABLE l3_cps
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	sw_cluster_id int NOT NULL,
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


CREATE TABLE lag_links
(
	cluster_link_if_id int NOT NULL,
	node_id int NOT NULL,
	lag_if_id int NOT NULL,
	opposite_node_id int NOT NULL,
	opposite_lag_if_id int NOT NULL,
	PRIMARY KEY (cluster_link_if_id)
) WITHOUT OIDS;


CREATE TABLE physical_links
(
	cluster_link_if_id int NOT NULL,
	node_id int NOT NULL,
	physical_if_id text,
	breakout_if_id text,
	opposite_node_id int NOT NULL,
	opposite_physical_if_id text,
	opposite_breakout_if_id text,
	PRIMARY KEY (cluster_link_if_id)
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
	cluster_status int NOT NULL,
	PRIMARY KEY (sw_cluster_id)
) WITHOUT OIDS;


CREATE TABLE system_status
(
	system_id int NOT NULL,
	service_status int NOT NULL,
	blockade_status int NOT NULL,
	PRIMARY KEY (system_id)
) WITHOUT OIDS;


CREATE TABLE vrf_ids
(
	layer_type int NOT NULL,
	next_id int DEFAULT 1 NOT NULL,
	PRIMARY KEY (layer_type)
) WITHOUT OIDS;



/* Create Foreign Keys */

ALTER TABLE async_requests_for_lower
	ADD FOREIGN KEY (operation_id)
	REFERENCES async_requests (operation_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE async_requests_for_lower_rollback
	ADD FOREIGN KEY (cluster_id, request_operation_id)
	REFERENCES async_requests_for_lower (cluster_id, request_operation_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE lag_links
	ADD FOREIGN KEY (cluster_link_if_id)
	REFERENCES cluster_link_ifs (cluster_link_if_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE physical_links
	ADD FOREIGN KEY (cluster_link_if_id)
	REFERENCES cluster_link_ifs (cluster_link_if_id)
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
	ADD FOREIGN KEY (sw_cluster_id)
	REFERENCES sw_clusters (sw_cluster_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l2_cps
	ADD FOREIGN KEY (sw_cluster_id)
	REFERENCES sw_clusters (sw_cluster_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE l3_cps
	ADD FOREIGN KEY (sw_cluster_id)
	REFERENCES sw_clusters (sw_cluster_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



