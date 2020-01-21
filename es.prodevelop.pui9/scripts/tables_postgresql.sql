-- POSTGRESQL
CREATE EXTENSION IF NOT EXISTS UNACCENT;

-- PUI BASE
CREATE TABLE pui_language (
	isocode CHARACTER VARYING(2),
	name CHARACTER VARYING(100) NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_language PRIMARY KEY (isocode),
	CONSTRAINT ck_lang_def CHECK (isdefault in (0, 1))
);

CREATE TABLE pui_user (
	usr CHARACTER VARYING(100),
	name CHARACTER VARYING(200) NOT NULL,
	password CHARACTER VARYING(100),
	language CHARACTER VARYING(2),
	email CHARACTER VARYING(100),
	disabled INTEGER DEFAULT 0 NOT NULL,
	disabled_date TIMESTAMP,
	dateformat CHARACTER VARYING(10) DEFAULT 'dd/MM/yyyy' NOT NULL,
	reset_password_token CHARACTER VARYING(100),
	last_access_time TIMESTAMP,
	last_access_ip CHARACTER VARYING(50),
	CONSTRAINT pk_pui_user PRIMARY KEY (usr),
	CONSTRAINT ck_lang_dis CHECK (disabled in (0, 1)),
	CONSTRAINT fk_user_language FOREIGN KEY (language) REFERENCES pui_language(isocode) ON DELETE SET NULL,
	CONSTRAINT ck_dateformat CHECK (dateformat in ('yyyy/MM/dd', 'yyyy-MM-dd', 'dd/MM/yyyy', 'dd-MM-yyyy'))
);

CREATE TABLE pui_subsystem (
	subsystem CHARACTER VARYING(3),
	CONSTRAINT pk_pui_subsystem PRIMARY KEY (subsystem)
);

CREATE TABLE pui_subsystem_tra (
	subsystem CHARACTER VARYING(3),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_subsystem_tra PRIMARY KEY (subsystem, lang),
	CONSTRAINT fk_subsystem_tra_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE,
	CONSTRAINT fk_subsystem_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_subsystem_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_functionality (
	functionality CHARACTER VARYING(100),
	subsystem CHARACTER VARYING(3) NOT NULL,
	CONSTRAINT pk_pui_functionality PRIMARY KEY (functionality),
	CONSTRAINT fk_func_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE
);

CREATE TABLE pui_functionality_tra (
	functionality CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_functionality_tra PRIMARY KEY (functionality, lang),
	CONSTRAINT fk_func_tra_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE,
	CONSTRAINT fk_func_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_func_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile (
	profile CHARACTER VARYING(100),
	CONSTRAINT pk_pui_profile PRIMARY KEY (profile)
);

CREATE TABLE pui_profile_tra (
	profile CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_profile_tra PRIMARY KEY (profile, lang),
	CONSTRAINT fk_profile_tra_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_profile_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_profile_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile_functionality (
	profile CHARACTER VARYING(100),
	functionality CHARACTER VARYING(100),
	CONSTRAINT pk_pui_profile_functionality PRIMARY KEY (profile, functionality),
	CONSTRAINT fk_prof_func_prof FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_prof_func_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE
);

CREATE TABLE pui_user_profile (
	USR CHARACTER VARYING(100),
	profile CHARACTER VARYING(100),
	CONSTRAINT pk_pui_user_profile PRIMARY KEY (USR, profile),
	CONSTRAINT fk_user_profile_user FOREIGN KEY (USR) REFERENCES pui_user(USR) ON DELETE CASCADE,
	CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE
);

CREATE TABLE pui_model (
	model CHARACTER VARYING(100),
	entity CHARACTER VARYING(100),
	configuration TEXT,
	filter TEXT,
	CONSTRAINT pk_pui_grid PRIMARY KEY (model)
);

CREATE TABLE pui_user_model_filter (
	id INTEGER,
	usr CHARACTER VARYING(100) NOT NULL,
	model CHARACTER VARYING(100) NOT NULL,
	label CHARACTER VARYING(200) NOT NULL,
	filter TEXT NOT NULL,
	CONSTRAINT pk_pui_user_filter PRIMARY KEY (id),
	CONSTRAINT fk_user_model_filter_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_model_filter (
	id INTEGER,
	model CHARACTER VARYING(100) NOT NULL,
	label CHARACTER VARYING(200) NOT NULL,
	description CHARACTER VARYING(300),
	filter TEXT NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_grid_filter PRIMARY KEY (id),
	CONSTRAINT ck_model_filter_def CHECK (isdefault in (0, 1)),
	CONSTRAINT fk_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_user_model_config (
	id INTEGER,
	usr CHARACTER VARYING(100) NOT NULL,
	model CHARACTER VARYING(100) NOT NULL,
	configuration TEXT NOT NULL,
	type CHARACTER VARYING(50) NOT NULL,
	CONSTRAINT pk_pui_user_grid_config PRIMARY KEY (id),
	CONSTRAINT fk_user_config_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_config_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_menu (
	node INTEGER,
	parent INTEGER,
	model CHARACTER VARYING(100),
	component CHARACTER VARYING(100),
	functionality CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	icon_label CHARACTER VARYING(100),
	CONSTRAINT pk_pui_menu PRIMARY KEY (node),
	CONSTRAINT fk_menu_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_menu_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality),
	CONSTRAINT fk_menu_parent FOREIGN KEY (parent) REFERENCES pui_menu(node)
);

CREATE TABLE pui_variable (
	variable CHARACTER VARYING(50),
	value TEXT NOT NULL,
	description CHARACTER VARYING(500) NOT NULL,
	CONSTRAINT pk_pui_variable PRIMARY KEY (variable)
);

CREATE TABLE pui_elasticsearch_views (
    appname CHARACTER VARYING(100) NOT NULL DEFAULT 'DEFAULT',
    viewname CHARACTER VARYING(100) NOT NULL,
    identity_fields CHARACTER VARYING(100) NOT NULL,
    CONSTRAINT pk_elasticsearch_views PRIMARY KEY (appname, viewname)
);

CREATE TABLE pui_audit_type (
	type CHARACTER VARYING(50),
	description CHARACTER VARYING(200),
	CONSTRAINT pk_pui_activity_type PRIMARY KEY (type)
);

CREATE TABLE pui_audit (
	id INTEGER,
	model CHARACTER VARYING(100) NOT NULL,
	type CHARACTER VARYING(50) NOT NULL,
	pk CHARACTER VARYING(100),
	datetime TIMESTAMP NOT NULL,
	usr CHARACTER VARYING(100) NOT NULL,
	ip CHARACTER VARYING(100) NOT NULL DEFAULT '0.0.0.0',
	dto TEXT,
	CONSTRAINT pk_pui_audit PRIMARY KEY (id),
	CONSTRAINT fk_audit_type FOREIGN KEY (type) REFERENCES pui_audit_type(type)
);

create table pui_importexport (
	id INTEGER,
	model character varying(100) NOT NULL,
	usr character varying(100) NOT NULL,
	datetime TIMESTAMP NOT NULL,
	filename_csv character varying(100) NOT NULL,
	filename_json character varying(100) NOT NULL,
	executed INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_impexp PRIMARY KEY (id),
	CONSTRAINT fk_pui_impexp_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_pui_impexp_usr FOREIGN KEY (usr) REFERENCES pui_user(usr),
	CONSTRAINT ck_executed CHECK (executed in (0, 1))
);

-- PUI DOCUMENTS
CREATE TABLE pui_document_extension (
	extension CHARACTER VARYING(10),
	max_size INTEGER,
	CONSTRAINT pk_pui_doc_ext PRIMARY KEY (extension)
);

CREATE TABLE pui_document_model_extension (
	model CHARACTER VARYING(100),
	extension CHARACTER VARYING(10),
	CONSTRAINT pk_pui_doc_mod_ext PRIMARY KEY (model, extension),
	CONSTRAINT fk_pui_doc_mod_ext_mod FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE,
	CONSTRAINT fk_pui_doc_mod_ext_ext FOREIGN KEY (extension) REFERENCES pui_document_extension(extension) ON DELETE CASCADE
);

CREATE TABLE pui_document_role (
	role CHARACTER VARYING(100),
	CONSTRAINT pk_pui_document_role PRIMARY KEY (role)
);

CREATE TABLE pui_document_role_tra (
	role CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	description CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_document_role_tra PRIMARY KEY (role, lang),
	CONSTRAINT fk_doc_role_tra_role FOREIGN KEY (role) REFERENCES pui_document_role(role) ON DELETE CASCADE,
	CONSTRAINT fk_doc_role_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_doc_role_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_document (
	id INTEGER,
	model CHARACTER VARYING(100) NOT NULL,
	pk CHARACTER VARYING(100) NOT NULL,
	language CHARACTER VARYING(2),
	description CHARACTER VARYING(100) NOT NULL,
	filename CHARACTER VARYING(100) NOT NULL,
	filename_orig CHARACTER VARYING(100) NOT NULL,
	role CHARACTER VARYING(100) NOT NULL,
	thumbnails CHARACTER VARYING(100),
	datetime TIMESTAMP NOT NULL,
	CONSTRAINT pk_pui_document PRIMARY KEY (id),
	CONSTRAINT fk_doc_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_doc_doc_role FOREIGN KEY (role) REFERENCES pui_document_role(role),
	CONSTRAINT fk_doc_lang FOREIGN KEY (language) REFERENCES pui_language(isocode)
);

-- PUI DOCGEN
CREATE TABLE pui_docgen_model (
	model CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	CONSTRAINT pk_pui_docgen_view PRIMARY KEY (model),
	CONSTRAINT fk_doc_view_model FOREIGN KEY (model) REFERENCES pui_model(model)
);

CREATE TABLE pui_docgen_attribute (
	id CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	value CHARACTER VARYING(500) NOT NULL,
	CONSTRAINT pk_pui_docgen_attribute PRIMARY KEY (id)
);

CREATE TABLE pui_docgen_template (
	id INTEGER,
	name CHARACTER VARYING(100) NOT NULL,
	description CHARACTER VARYING(1000),
	main_model CHARACTER VARYING(100) NOT NULL,
	models CHARACTER VARYING(500),
	column_filename CHARACTER VARYING(100),
	filename CHARACTER VARYING(100) NOT NULL,
	mapping TEXT NOT NULL,
	filter TEXT,
	parameters TEXT,
	CONSTRAINT pk_pui_docgen_template PRIMARY KEY (id),
	CONSTRAINT fk_docgen_main_model FOREIGN KEY (main_model) REFERENCES pui_docgen_model(model)
);

-- NOTIFICATIONS
create table pui_user_fcm (
	token CHARACTER VARYING(300) NOT NULL,
	usr CHARACTER VARYING(100) NOT NULL,
	last_use TIMESTAMP NOT NULL,
	CONSTRAINT PK_user_fcm_token PRIMARY KEY (token),
	CONSTRAINT FK_user_fcm_token_user_fv FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE
);
CREATE INDEX pui_user_fcm_usr_IDX ON pui_user_fcm (usr);

-- PUI PUBLISH/AUDIT
CREATE TABLE pui_publish_audit_entity (
	id INTEGER,
	entity CHARACTER VARYING(100) NOT NULL,
	audit_insert INTEGER DEFAULT 0 NOT NULL,
	audit_update INTEGER DEFAULT 0 NOT NULL,
	audit_delete INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_publish_audit_entity PRIMARY KEY (id),
	CONSTRAINT uk_pub_aud_entity UNIQUE (entity),
	CONSTRAINT ck_pub_aud_val_ins CHECK (audit_insert in (0, 1)),
	CONSTRAINT ck_pub_aud_val_upd CHECK (audit_update in (0, 1)),
	CONSTRAINT ck_pub_aud_val_del CHECK (audit_delete in (0, 1))
);

CREATE TABLE pui_publish_audit_topic (
	id INTEGER,
	id_entity INTEGER NOT NULL,
	op_type CHARACTER VARYING(10) NOT NULL,
	cod_topic CHARACTER VARYING(100) NOT NULL,
	enabled INTEGER DEFAULT 1 NOT NULL,
	event_type INTEGER NOT NULL,
	CONSTRAINT pk_pui_publish_audit_topic PRIMARY KEY (id),
	CONSTRAINT ck_pub_aud_topic_enabled CHECK (enabled in (0, 1)),
	CONSTRAINT ck_pub_aud_topic_op CHECK (op_type in ('insert', 'update', 'delete')),
	CONSTRAINT fk_audit_topic_entity FOREIGN KEY (id_entity) REFERENCES pui_publish_audit_entity(id) ON DELETE CASCADE
);

CREATE TABLE pui_publish_audit_field (
	id INTEGER,
	id_topic INTEGER NOT NULL,
	field CHARACTER VARYING(100) NOT NULL,
	CONSTRAINT pk_pui_publish_audit_field PRIMARY KEY (id),
	CONSTRAINT uk_pub_aud_field UNIQUE (id_topic, field),
	CONSTRAINT fk_audit_field_topic FOREIGN KEY (id_topic) REFERENCES pui_publish_audit_topic(id) ON DELETE CASCADE
);

CREATE TABLE pui_publish_audit_field_value (
	id INTEGER,
	id_field INTEGER NOT NULL,
	old_value CHARACTER VARYING(200),
	new_value CHARACTER VARYING(200),
	CONSTRAINT pk_pui_pub_aud_field_value PRIMARY KEY (id),
	CONSTRAINT fk_audit_value_field FOREIGN KEY (id_field) REFERENCES pui_publish_audit_field(id) ON DELETE CASCADE
);
