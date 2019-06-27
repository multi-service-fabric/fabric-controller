--
--
-- DDL for updating the DB schema definition version of FC from "2017" to "2018B"
--
--

BEGIN;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- ############### Modify existing tables ###############
--

--
-- async_requests
--
ALTER TABLE async_requests
	ADD reservation_time timestamp with time zone;
ALTER TABLE async_requests
	ADD start_time timestamp with time zone;
ALTER TABLE async_requests
	ALTER notification_ip_address DROP NOT NULL;
ALTER TABLE async_requests
	ALTER notification_port_number DROP NOT NULL;

--
-- breakout_ifs
--
-- -> No change

--
-- cluster_link_ifs
--
ALTER TABLE cluster_link_ifs
	RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE cluster_link_ifs
	ALTER lag_if_info_id TYPE bigint;
ALTER TABLE cluster_link_ifs
	ADD igp_cost int;
UPDATE cluster_link_ifs
	SET igp_cost = 100;
ALTER TABLE cluster_link_ifs
	ALTER igp_cost SET NOT NULL;
ALTER TABLE cluster_link_ifs
        ADD ipv4_addr text;
UPDATE cluster_link_ifs
	SET ipv4_addr = '0.0.0.0';
ALTER TABLE cluster_link_ifs
        ALTER ipv4_addr SET NOT NULL;

--
-- cp_ids
--
-- -> No change

--
-- edge_points
--
ALTER TABLE edge_points
	RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE edge_points
	ALTER lag_if_info_id TYPE bigint;

--
-- equipments
--
-- -> No change

--
-- esi_ids
--
-- -> No change

--
-- internal_link_ifs
--
ALTER TABLE internal_link_ifs
        RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE internal_link_ifs
        ALTER lag_if_info_id TYPE bigint;
ALTER TABLE internal_link_ifs
        ADD igp_cost int;
UPDATE internal_link_ifs
	SET igp_cost = 100;
ALTER TABLE internal_link_ifs
        ALTER igp_cost SET NOT NULL;
ALTER TABLE internal_link_ifs
        ADD old_igp_cost int;

--
-- l2_cps
--
ALTER TABLE l2_cps
	ADD clag_id int;
ALTER TABLE l2_cps
	ALTER node_info_id TYPE bigint;
ALTER TABLE l2_cps
	ADD traffic_threshold double precision;

--
-- l2_slices
--
ALTER TABLE l2_slices
	ALTER vrf_id DROP NOT NULL;
ALTER TABLE l2_slices
	ADD vni int UNIQUE;
ALTER TABLE l2_slices
	ADD irb_type int;
ALTER TABLE l2_slices
	ADD l3vni int;
ALTER TABLE l2_slices
	ADD l3vni_vlan_id int;
ALTER TABLE l2_slices
	ADD q_in_q_enable boolean ;
UPDATE l2_slices
	SET q_in_q_enable = false;
ALTER TABLE l2_slices
	ALTER q_in_q_enable SET NOT NULL;

UPDATE l2_slices A
	SET vni = (
		SELECT vrf_id FROM l2_slices
		WHERE slice_id = A.slice_id
	);
UPDATE l2_slices
	SET vrf_id = null;


--
-- l3_cps
--
ALTER TABLE l3_cps
	ALTER node_info_id TYPE bigint;

--
-- l3_slices
--
-- -> No change

--
-- lag_ifs
--
ALTER TABLE lag_ifs
	RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE lag_ifs
	ALTER lag_if_info_id TYPE bigint;

CREATE SEQUENCE lag_ifs_lag_if_info_id_seq;
ALTER TABLE lag_ifs
	ALTER lag_if_info_id SET DEFAULT nextval('lag_ifs_lag_if_info_id_seq');
ALTER SEQUENCE lag_ifs_lag_if_info_id_seq
	OWNED BY lag_ifs.lag_if_info_id;

--
-- lag_if_ids
--
DROP TABLE lag_if_ids;

--
-- leaf_nodes
--
-- -> No change

--
-- nodes
--
ALTER TABLE nodes
	ADD is_priority_node_group_member boolean;
UPDATE nodes
	SET is_priority_node_group_member = false;
ALTER TABLE nodes
	ALTER is_priority_node_group_member SET NOT NULL;
ALTER TABLE nodes
        ADD detoured boolean;
UPDATE nodes
	SET detoured = false;
ALTER TABLE nodes
	ALTER detoured SET NOT NULL;

UPDATE nodes
	SET ec_node_id = ec_node_id + 900
	WHERE node_type = 2;
UPDATE nodes
	SET ec_node_id = ec_node_id + 1800
	WHERE node_type = 3;

--
-- physical_ifs
--
-- -> No change

--
-- slice_ids
--
-- -> No change

--
-- sw_clusters
--
ALTER TABLE sw_clusters
	ADD internal_link_normal_igp_cost int NOT NULL;

--
-- system_status
--
ALTER TABLE system_status
	ADD renewal_status int;
UPDATE system_status
	SET renewal_status = 0;
ALTER TABLE system_status
	ALTER renewal_status SET NOT NULL;


--
-- vlan_ifs
--
ALTER TABLE vlan_ifs
	ALTER node_info_id TYPE bigint;
ALTER TABLE vlan_ifs
	ADD irb_instance_id bigint;

--
-- vlan_if_ids
--
ALTER TABLE vlan_if_ids
        ALTER node_info_id TYPE bigint;

--
-- vrf_ids
--
-- -> No change

--
-- Create new tables.
--

--
-- cp_filter_info
--
CREATE TABLE cp_filter_info
(
	slice_id text NOT NULL,
	cp_id text NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (slice_id, cp_id, term_id)
) WITHOUT OIDS;


--
-- irb_instances
--
CREATE TABLE irb_instances
(
	irb_instance_id bigserial NOT NULL,
	node_info_id bigint NOT NULL,
	vlan_id int NOT NULL,
	vni int NOT NULL,
	slice_id text NOT NULL,
	PRIMARY KEY (irb_instance_id),
	CONSTRAINT irb_composite_id UNIQUE (node_info_id, vlan_id)
) WITHOUT OIDS;


--
-- lag_if_filter_info
--
CREATE TABLE lag_if_filter_info
(
	lag_if_info_id bigint NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (lag_if_info_id, term_id)
) WITHOUT OIDS;

--
-- node_operation_info
--
CREATE TABLE node_operation_info
(
	node_operation_status int NOT NULL,
	PRIMARY KEY (node_operation_status)
) WITHOUT OIDS;

--
-- physical_if_filter_info
--
CREATE TABLE physical_if_filter_info
(
	physical_if_info_id bigint NOT NULL,
	term_id text NOT NULL,
	PRIMARY KEY (physical_if_info_id, term_id)
) WITHOUT OIDS;


--
-- ############### create referential constraints. ###############
--

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

ALTER TABLE lag_if_filter_info
	ADD FOREIGN KEY (lag_if_info_id)
	REFERENCES lag_ifs (lag_if_info_id)
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


--
-- Create and convert the LagIF ID.
--
ALTER TABLE lag_ifs
        ADD lag_if_id int;

UPDATE lag_ifs A
	SET lag_if_id = (
        	SELECT lag_if_info_id
                FROM lag_ifs
		WHERE lag_if_info_id = A.lag_if_info_id
        );
ALTER TABLE lag_ifs
        ALTER lag_if_id SET NOT NULL;
UPDATE lag_ifs A
	SET lag_if_id = (
		SELECT node_id
		FROM nodes
		WHERE node_info_id = (
			SELECT node_info_id
			FROM lag_ifs
			WHERE lag_if_info_id = (
				SELECT lag_if_info_id
				FROM internal_link_ifs
				WHERE opposite_internal_link_if_id = (
					SELECT internal_link_if_id
					FROM internal_link_ifs
					WHERE lag_if_info_id = A.lag_if_info_id
				)
			)
		)
	)
	WHERE lag_if_id <= 800;

COMMIT;

