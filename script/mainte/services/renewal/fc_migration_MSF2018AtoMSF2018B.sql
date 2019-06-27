--
--
-- DDL for updating the DB schema definition version of FC from "2018A" to "2018B".
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
-- Async_requests
--
ALTER TABLE async_requests
	ADD reservation_time timestamp with time zone;
ALTER TABLE async_requests
	ADD start_time timestamp with time zone;

--
-- breakout_ifs
--
-- -> NO change

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
-- cp_filter_info
--
-- -> NO change

--
-- cp_ids
--
-- -> NO change

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
-- -> NO change

--
-- esi_ids
--
-- -> NO change

--
-- internal_link_ifs
--
ALTER TABLE internal_link_ifs
        RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE internal_link_ifs
        ALTER lag_if_info_id TYPE bigint;
ALTER TABLE internal_link_ifs
        ADD old_igp_cost int;

--
-- irb_instances
--
ALTER TABLE irb_instances
	ALTER node_info_id TYPE bigint;

--
-- l2_cps 
--
ALTER TABLE l2_cps
	ALTER node_info_id TYPE bigint;

--
-- l2_slices
--
ALTER TABLE l2_slices
	ADD q_in_q_enable boolean ;
UPDATE l2_slices
	SET q_in_q_enable = false;
ALTER TABLE l2_slices
	ALTER q_in_q_enable SET NOT NULL;

--
-- l3_cps
--
ALTER TABLE l3_cps
	ALTER node_info_id TYPE bigint;

--
-- l3_slices
--
-- -> NO change


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
-- lag_if_filter_info
--
ALTER TABLE lag_if_filter_info
	RENAME lag_if_id TO lag_if_info_id;
ALTER TABLE lag_if_filter_info
	ALTER lag_if_info_id TYPE bigint;

--
-- lag_if_ids
--
DROP TABLE lag_if_ids;

--
-- leaf_nodes
--
-- -> NO change

--
-- nodes
--
ALTER TABLE nodes
	ALTER is_priority_node_group_member DROP DEFAULT;
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
-- node_operation_info
--
-- -> NO change

--
-- physical_ifs
--
-- -> NO change

--
-- physical_if_filter_info
--
-- -> NO change

--
-- slice_ids
--
-- -> NO change

--
-- sw_clusters
--
-- -> NO change

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

--
-- vlan_if_ids
--
ALTER TABLE vlan_if_ids
        ALTER node_info_id TYPE bigint;

--
-- vrf_ids
--
-- -> NO change

--
-- ############### Create new tables ###############
--
-- Nothing

--
-- ############### modify referential constraints. ###############
--
-- Nothing


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

