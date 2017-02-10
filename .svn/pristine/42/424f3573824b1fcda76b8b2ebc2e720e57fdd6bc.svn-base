CREATE TABLE property (
	name VARCHAR(50) NOT NULL,
	value TEXT,
	CONSTRAINT pk_property PRIMARY KEY(name)
);

CREATE TABLE person (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(46), -- SHA-256 base64 encoded
	salt VARCHAR(40), -- 30 bytes base64 encoded
	type VARCHAR(7) NOT NULL DEFAULT 'DRIVER',
	first_name VARCHAR(50) NOT NULL,
	middle_name VARCHAR(50),
	last_name VARCHAR(50) NOT NULL,
	locked BOOLEAN NOT NULL DEFAULT false,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_person PRIMARY KEY(id),
	CONSTRAINT u1_person_email UNIQUE(email),
	CONSTRAINT u2_person_uuid UNIQUE(uuid),
	CONSTRAINT fk1_person_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE person_id_seq;

CREATE TABLE role (
	id BIGINT NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT pk_role PRIMARY KEY(id),
	CONSTRAINT u1_role_name UNIQUE(name)
);
CREATE SEQUENCE role_id_seq;

CREATE TABLE person_role (
	person BIGINT NOT NULL,
	role BIGINT NOT NULL,
	CONSTRAINT pk_person_role PRIMARY KEY(person, role),
	CONSTRAINT fk1_person_role FOREIGN KEY(person) REFERENCES person(id) ON DELETE CASCADE,
	CONSTRAINT fk2_person_role FOREIGN KEY(role) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE driver (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	person BIGINT NOT NULL,
	dob DATE,
	ssn VARCHAR(11),
	phone VARCHAR(20),
	mobile VARCHAR(20),
	fax VARCHAR(20),
	address1 VARCHAR(50),
	address2 VARCHAR(50),
	city VARCHAR(50),
	state CHAR(2),
	postal_code VARCHAR(10),
	contact_name VARCHAR(100),
	contact_relationship VARCHAR(50),
	contact_phone VARCHAR(20),
	contact_mobile VARCHAR(20),
	available_date DATE,
	felony_conviction BOOLEAN,
	felony_conviction_date DATE,
	felony_conviction_explanation TEXT,
	dui_conviction BOOLEAN,
	dui_conviction_date DATE,
	dui_conviction_explanation TEXT,
	license_revoked BOOLEAN,
	license_revoked_date DATE,
	license_revoked_explanation TEXT,
	controlled_substance BOOLEAN,
	controlled_substance_date DATE,
	controlled_substance_explanation TEXT,
	highest_grade_completed VARCHAR(40),
	driver_school BOOLEAN,
	driver_school_name VARCHAR(100),
	eligible_employment BOOLEAN,
	not_eligible_explanation TEXT,
	no_additional_addresses BOOLEAN,
	hazmat_expiration DATE,
	mvr_date DATE,
	medical_review_date DATE,
	access_code CHAR(6),
	access_code_created_date TIMESTAMP,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_driver PRIMARY KEY(id),
	CONSTRAINT u1_driver UNIQUE(uuid),
	CONSTRAINT u2_driver UNIQUE(access_code),
	CONSTRAINT fk1_driver_person FOREIGN KEY(person) REFERENCES person(id) ON DELETE CASCADE,
	CONSTRAINT fk2_driver_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_driver_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE driver_id_seq;

CREATE TABLE truck (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	vin VARCHAR(100),
	year INTEGER,
	make VARCHAR(30),
	model VARCHAR(30),
	license VARCHAR(20),
	license_state CHAR(2),
	registration DATE,
	annual_inspection DATE,
	bobtail_insurance DATE,
	ifta BOOLEAN,
	quarterly_maintenance DATE,
	physical_damage_insurance DATE,
	lessor_number VARCHAR(100),
	lessor_name VARCHAR(100),
	lessor_address1 VARCHAR(50),
	lessor_address2 VARCHAR(50),
	lessor_city VARCHAR(50),
	lessor_state CHAR(2),
	lessor_postal_code VARCHAR(10),
	lessor_phone VARCHAR(20),
	lessor_mobile VARCHAR(20),
	lessor_gov_id VARCHAR(20),
	active BOOLEAN NOT NULL DEFAULT true,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_truck_id PRIMARY KEY(id),
	CONSTRAINT u1_truck UNIQUE(uuid),
	CONSTRAINT fk1_truck_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk2_truck_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE truck_id_seq;

CREATE TABLE company (
	id BIGINT NOT NULL,
	person BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	name VARCHAR(40) NOT NULL,
	company_number VARCHAR(100) NOT NULL,
	address1 VARCHAR(50),
	address2 VARCHAR(50),
	city VARCHAR(50),
	state CHAR(2),
	postal_code VARCHAR(10),
	phone VARCHAR(20),
	fax VARCHAR(20),
	website VARCHAR(100),
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_company_id PRIMARY KEY(id),
	CONSTRAINT u1_company_uuid UNIQUE(uuid),
	CONSTRAINT fk1_company_person FOREIGN KEY(person) REFERENCES person(id) ON DELETE CASCADE,
	CONSTRAINT fk2_company_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_company_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE company_id_seq;

CREATE TABLE company_driver (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	company BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	driver_number VARCHAR(100) NOT NULL,
	hire_date DATE,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_company_driver_id PRIMARY KEY(id),
	CONSTRAINT u1_company_driver_UUID UNIQUE(uuid),
	CONSTRAINT u2_company_driver UNIQUE(company, driver),
	CONSTRAINT u3_company_driver_driver_number UNIQUE(company, driver_number),
	CONSTRAINT fk1_company_driver_company FOREIGN KEY(company) REFERENCES company(id) ON DELETE CASCADE,
	CONSTRAINT fk2_company_driver_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk3_company_driver_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk4_company_driver_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE company_driver_id_seq;

CREATE TABLE company_truck (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	company BIGINT NOT NULL,
	truck BIGINT NOT NULL,
	truck_number VARCHAR(100) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_company_truck_id PRIMARY KEY(id),
	CONSTRAINT u1_company_truck UNIQUE(company, truck),
	CONSTRAINT u2_company_truck_truck_number UNIQUE(company, truck_number),
	CONSTRAINT fk1_company_truck_company FOREIGN KEY(company) REFERENCES company(id) ON DELETE CASCADE,
	CONSTRAINT fk2_company_truck_truck FOREIGN KEY(truck) REFERENCES truck(id) ON DELETE CASCADE,
	CONSTRAINT fk3_company_truck_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk4_company_truck_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE company_truck_id_seq;

CREATE TABLE driver_truck (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	driver BIGINT NOT NULL,
	truck BIGINT NOT NULL,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_driver_truck_id PRIMARY KEY(id),
	CONSTRAINT u1_driver_truck_uuid UNIQUE(uuid),
	CONSTRAINT u2_driver_truck_driver_truck UNIQUE(driver, truck),
	CONSTRAINT fk1_driver_truck_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_driver_truck_truck FOREIGN KEY(truck) REFERENCES truck(id) ON DELETE CASCADE,
	CONSTRAINT fk3_driver_truck_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk4_driver_truck_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE driver_truck_id_seq;

CREATE TABLE event (
	id BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	type VARCHAR(50) NOT NULL,
	event_date TIMESTAMP NOT NULL,
	ip VARCHAR(39), -- support IPv4 and IPv6
	subject BIGINT, -- this is the person acting on the object
	person BIGINT, -- this is the object being acted upon
	message TEXT,
	CONSTRAINT pk_event PRIMARY KEY(id),
	CONSTRAINT u1_event_uuid UNIQUE(uuid),
	CONSTRAINT fk1_event_subject FOREIGN KEY(subject) REFERENCES person(id) ON DELETE CASCADE,
	CONSTRAINT fk4_event_person FOREIGN KEY(person) REFERENCES person(id) ON DELETE CASCADE
);
CREATE SEQUENCE event_id_seq;

CREATE TABLE document (
	id BIGINT NOT NULL,
	person BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	type_code VARCHAR(50),
	filename VARCHAR(100),
	effective_date TIMESTAMP,
	expiration_date TIMESTAMP,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_document PRIMARY KEY(id),
	CONSTRAINT u1_document UNIQUE(UUID),
	CONSTRAINT fk1_document_person FOREIGN KEY(person) REFERENCES person(id) ON DELETE CASCADE,
	CONSTRAINT fk2_document_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_document_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE document_id_seq;

CREATE TABLE license (
	id BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	state CHAR(2) NOT NULL,
	number VARCHAR(20) NOT NULL,
	type VARCHAR(20),
	exp_date TIMESTAMP,
	current BOOLEAN NOT NULL DEFAULT false,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_license PRIMARY KEY(id),
	CONSTRAINT u1_license_uuid UNIQUE(UUID),
	CONSTRAINT u2_license_driver_state_number UNIQUE(driver, state, number),
	CONSTRAINT fk1_license_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_license_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_license_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE license_id_seq;

CREATE TABLE residence (
	id BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	address1 VARCHAR(50),
	address2 VARCHAR(50),
	city VARCHAR(50),
	state CHAR(2),
	postal_code VARCHAR(10),
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_residence PRIMARY KEY(id),
	CONSTRAINT u1_residence UNIQUE(UUID),
	CONSTRAINT fk1_residence_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_residence_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_residence_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE residence_id_seq;

CREATE TABLE accident (
	id BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	accident_date DATE NOT NULL,
	type VARCHAR(200) NOT NULL,
	nature VARCHAR(200) NOT NULL,
	at_fault BOOLEAN,
	fatalities BOOLEAN,
	injuries BOOLEAN,
	damages numeric(19,2),
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_accident PRIMARY KEY(id),
	CONSTRAINT u1_accident UNIQUE(UUID),
	CONSTRAINT fk1_accident_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_accident_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_accident_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE accident_id_seq;

CREATE TABLE traffic (
	id BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	traffic_date DATE NOT NULL,
	city VARCHAR(50),
	state CHAR(2),
	charge VARCHAR(200),
	penalty VARCHAR(200),
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_traffic PRIMARY KEY(id),
	CONSTRAINT u1_traffic UNIQUE(UUID),
	CONSTRAINT fk1_traffic_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_traffic_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_traffic_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE traffic_id_seq;

CREATE TABLE employment (
	id BIGINT NOT NULL,
	driver BIGINT NOT NULL,
	uuid VARCHAR(36) NOT NULL,
	name VARCHAR(200),
	supervisor VARCHAR(200),
	address VARCHAR(200),
	city VARCHAR(50),
	state CHAR(2),
	postal_code VARCHAR(10),
	phone VARCHAR(20),
	position VARCHAR(200),
	from_date DATE,
	to_date DATE,
	leaving VARCHAR(500),
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_employment PRIMARY KEY(id),
	CONSTRAINT u1_employment UNIQUE(UUID),
	CONSTRAINT fk1_employment_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE CASCADE,
	CONSTRAINT fk2_employment_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE SET NULL,
	CONSTRAINT fk3_employment_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE SET NULL
);
CREATE SEQUENCE employment_id_seq;

CREATE TABLE data_import(
	id BIGINT NOT NULL,
	import_type VARCHAR(50) NOT NULL,
	company BIGINT NOT NULL,
	import_data TEXT NOT NULL,
	overwrite BOOLEAN NOT NULL,
	import_log TEXT,
	start_time TIMESTAMP,
	end_time TIMESTAMP,
	success BOOLEAN,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_data_import PRIMARY KEY(id),
	CONSTRAINT fk1_data_import_company FOREIGN KEY(company) REFERENCES company(id) ON DELETE RESTRICT,
	CONSTRAINT fk2_data_import_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE RESTRICT,
	CONSTRAINT fk3_data_import_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE RESTRICT
);
CREATE SEQUENCE data_import_id_seq;

CREATE TABLE content_node (
	id BIGINT NOT NULL,
	name VARCHAR(50) NOT NULL,
	description TEXT NOT NULL,
	content TEXT NOT NULL,
	created_date TIMESTAMP NOT NULL,
	created_by BIGINT,
	last_modified_date TIMESTAMP NOT NULL,
	last_modified_by BIGINT,
	CONSTRAINT pk_content_node PRIMARY KEY(id),
	CONSTRAINT u1_content_node_name UNIQUE(name),
	CONSTRAINT fk1_content_node_created_by FOREIGN KEY(created_by) REFERENCES person(id) ON DELETE RESTRICT,
	CONSTRAINT fk2_content_node_last_modified_by FOREIGN KEY(last_modified_by) REFERENCES person(id) ON DELETE RESTRICT
);
CREATE SEQUENCE content_node_id_seq;

CREATE TABLE application_access (
	id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	company VARCHAR(100) NOT NULL,
	driver BIGINT NOT NULL,
	created_date TIMESTAMP NOT NULL,
	CONSTRAINT pk_application_access PRIMARY KEY(id),
	CONSTRAINT fk1_application_access_driver FOREIGN KEY(driver) REFERENCES driver(id) ON DELETE RESTRICT
);
CREATE SEQUENCE application_access_id_seq;
