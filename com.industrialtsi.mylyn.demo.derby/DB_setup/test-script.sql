connect 'jdbc:derby:C:\DerbyDatabases\MyDB;create=true';

drop table issues;
DROP table comments;
DROP TABLE attachments;
DROP TABLE priority;
DROP TABLE products;
DROP TABLE status;



CREATE TABLE issues (
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

CREATE TABLE comments (
	cmt_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	cmt_bug_id integer NOT NULL,
    cmt_desc CLOB,
    cmt_text CLOB,
	cmt_author VARCHAR(512),
	cmt_author_name VARCHAR(512),
    cmt_date timestamp NOT NULL default CURRENT_TIMESTAMP
);

CREATE TABLE attachments (
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


CREATE TABLE priority (
    priority_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	priority VARCHAR(2),
    PRIMARY KEY  (priority_id)
);


CREATE TABLE products (
    product_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	product VARCHAR(512),
    PRIMARY KEY  (product_id)
);

CREATE TABLE status (
    status_id integer NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1),
	sort INTEGER not NULL,
	status VARCHAR(12),
    PRIMARY KEY  (status_id)
);


insert into priority (priority) values ( 'P1' ), ( 'P2' ), ( 'P3' ), ( 'P4' ), ( 'P5' );
insert into products (product) values ( 'Product 1' ), ( 'Product 2' ), ( 'Product 3' ), ( 'Product 4' ), ( 'Product 5' ), ( 'UNKNOWN' );
insert into status (sort,status) values ( 1, 'NEW' ), (2, 'OPEN' ), ( 3, 'CLOSED' );
