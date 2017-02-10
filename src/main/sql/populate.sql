-- passwords are 'password1'
INSERT INTO person (id, uuid, email, password, salt, type, first_name, last_name, created_date, last_modified_date) VALUES
(nextval('person_id_seq'), '7a7ed2e4-b567-4cc1-9247-9366b87b1592', 'jonk@transtaff.com', 'FW8F2r6PILFuUHDShsodRBuuxYj+cFYjSHXTpbAQ06I=', 'dCoY8zNjn1UsBNoZbVNtE0qoptZrS+4t4B2vFjbx', 'ADMIN', 'Jon', 'Kesler', now(), now()),
(nextval('person_id_seq'), '626a1256-0927-4c37-a52a-8fb52bccba68', 'testdriver@transtaff.com', 'FW8F2r6PILFuUHDShsodRBuuxYj+cFYjSHXTpbAQ06I=', 'dCoY8zNjn1UsBNoZbVNtE0qoptZrS+4t4B2vFjbx', 'DRIVER', 'Test', 'Driver', now(), now()),
(nextval('person_id_seq'), '94bd1dac-415a-4371-b7df-e43a686a1602', 'testcompany@transtaff.com', 'FW8F2r6PILFuUHDShsodRBuuxYj+cFYjSHXTpbAQ06I=', 'dCoY8zNjn1UsBNoZbVNtE0qoptZrS+4t4B2vFjbx', 'COMPANY', 'Test', 'Company', now(), now());

INSERT INTO company (id, person, uuid, name, company_number, created_date, last_modified_date) VALUES
(nextval('company_id_seq'), (select id from person where email = 'testcompany@transtaff.com'), '69bd8c6e-f043-4d67-826a-b6854896de80', 'Test Company', 'TEST1', now(), now());

INSERT INTO driver(id, uuid, person, created_date, last_modified_date) VALUES
(nextval('driver_id_seq'), '25433181-aa1a-4afb-a3f7-2fbc959a7f5e', (select id from person where email = 'testdriver@transtaff.com'), now(), now());

INSERT INTO content_node (id, name, description, content, created_date, last_modified_date) VALUES
(nextval('content_node_id_seq'), 'HOME', 'Home page content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'SIGNUP', 'Sign up page content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'FEATURES', 'Features page content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'FAQ', 'FAQ page content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'CONTACT_US', 'Contact us page content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'ADMIN_DASHBOARD_MESSAGE', 'Admin dashboard message content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'DRIVER_DASHBOARD_MESSAGE', 'Driver dashboard message content', 'TODO: Content needed', now(), now()),
(nextval('content_node_id_seq'), 'COMPANY_DASHBOARD_MESSAGE', 'Company dashboard message content', 'TODO: Content needed', now(), now());
