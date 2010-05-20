CREATE SCHEMA tasks AUTHORIZATION mylyn;

DROP TABLE tasks.issues;
DROP TABLE tasks.comments;
DROP TABLE tasks.attachments;
DROP TABLE tasks.priority;
DROP TABLE tasks.products;
DROP TABLE tasks.status;



CREATE TABLE tasks.issues (
  bug_ID integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  bug_product varchar(512) NOT NULL default 'UNKNOWN',
  bug_owner varchar(512) NOT NULL default 'nn',
  bug_summary CLOB NOT NULL default '',
  bug_priority varchar(2) NOT NULL default 'P3',
  bug_status varchar(12) NOT NULL default 'NEW',
  bug_created timestamp NOT NULL default CURRENT_TIMESTAMP,
  bug_scheduled timestamp,
  bug_closed timestamp,
  bug_due timestamp,
  bug_time_estimated integer NOT NULL default 60,
  bug_time_actual integer NOT NULL default 0,
  bug_notes CLOB NOT NULL default '',
  PRIMARY KEY  (bug_ID)
);

CREATE TABLE tasks.comments (
	cmt_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	cmt_bug_id integer NOT NULL,
    cmt_desc CLOB,
    cmt_text CLOB,
	cmt_author VARCHAR(512),
	cmt_author_name VARCHAR(512),
    cmt_date timestamp NOT NULL default CURRENT_TIMESTAMP
);

CREATE TABLE tasks.attachments (
  att_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
  att_desc varchar(255) NOT NULL,
  att_date timestamp NOT NULL default CURRENT_TIMESTAMP,
  att_url varchar(255),
  att_ctype varchar(255) NOT NULL,
  att_filename varchar(512) NOT NULL,
  att_size integer NOT NULL,
  att_task varchar(255) NOT NULL,
  att_blob blob,
  att_name varchar(255) NOT NULL default 'unknown',
  PRIMARY KEY  (att_id)
);


CREATE TABLE tasks.priority (
    priority_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	priority VARCHAR(2),
    PRIMARY KEY  (priority_id, priority)
);


CREATE TABLE tasks.products (
    product_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	product VARCHAR(512),
    PRIMARY KEY  (product_id, product)
);

CREATE TABLE tasks.status (
    status_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	sort INTEGER not NULL,
	status VARCHAR(12),
    PRIMARY KEY  (status_id, status)
);


insert into tasks.priority (priority) values ( 'P1' ), ( 'P2' ), ( 'P3' ), ( 'P4' ), ( 'P5' );
insert into tasks.products (product) values ( 'Product 1' ), ( 'Product 2' ), ( 'Product 3' ), ( 'Product 4' ), ( 'Product 5' ), ( 'UNKNOWN' );
insert into tasks.status (sort,status) values ( 1, 'NEW' ), (2, 'OPEN' ), ( 3, 'CLOSED' );



insert into tasks.issues ( bug_product, bug_owner, bug_summary, bug_priority)
values
	( 'Product 1', 'Maarten', 'Test issue 1', 'P1'),
	( 'Product 1', 'Maarten', 'Another Test issue', 'P1'),
	( 'Product 1', 'Maarten', 'Yet more Test issue 1', 'P2'),
	( 'Product 2', 'Maarten', 'So many Test issue 1', 'P2'),
	( 'Product 2', 'Wim', 'Test issue', 'P3'),
	( 'Product 3', 'Wim', 'Test issue', 'P3')
;

insert into tasks.attachments (  att_desc, att_url, att_ctype, att_filename, att_size, att_task, att_name )
values 
	( 'BLOB based', null, 'text/plain', 'demo.txt', 0, '1', 'Maarten')
;
