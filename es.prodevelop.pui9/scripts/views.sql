-- VIEWS
CREATE VIEW v_pui_language AS
SELECT
	l.isocode,
	l.name,
	l.isdefault
FROM pui_language l;

CREATE VIEW v_pui_variable AS
SELECT
	v.variable,
	v.value,
	v.description
FROM pui_variable v;

CREATE VIEW v_pui_audit AS
SELECT
	a.id,
	a.datetime,
	a.usr,
	a.ip,
	a.type,
	a.model,
	a.pk,
	a.dto
FROM pui_audit a;

CREATE VIEW v_pui_login AS
SELECT
	a.ID,
	a.USR,
	a.DATETIME,
	a.ip
FROM PUI_AUDIT a
WHERE a.TYPE = 'signin';

CREATE VIEW v_pui_subsystem AS
SELECT
	s.SUBSYSTEM,
	s_tra.NAME,
	s_tra.lang
FROM PUI_SUBSYSTEM s
LEFT JOIN PUI_SUBSYSTEM_TRA s_tra ON
	s_tra.SUBSYSTEM = s.SUBSYSTEM;

CREATE VIEW v_pui_functionality AS
SELECT
	f.functionality,
	f_tra.name,
	f.subsystem,
	s_tra.name as subsystem_name,
	s_tra.lang
FROM pui_functionality f
LEFT JOIN pui_subsystem s ON
	s.subsystem = f.subsystem
LEFT JOIN pui_functionality_tra f_tra ON
	f_tra.functionality = f.functionality
LEFT JOIN pui_subsystem_tra s_tra ON
	s_tra.subsystem = s.subsystem AND
	s_tra.lang = f_tra.lang;

CREATE VIEW v_pui_profile AS
SELECT
	p.profile,
	p_tra.name,
	p_tra.lang
FROM pui_profile p
LEFT JOIN pui_profile_tra p_tra ON
	p_tra.profile = p.profile;

CREATE VIEW v_pui_user AS
SELECT
	u.usr,
	u.name,
	u.email,
	u.language,
	u.dateformat,
	u.disabled,
	u.disabled_date,
	u.last_access_time,
	u.last_access_ip
FROM pui_user u;

CREATE VIEW v_pui_profile_functionality AS
SELECT
	pf.profile,
	p_tra.name as profile_name,
	pf.functionality,
	p_tra.lang
FROM pui_profile_functionality pf
LEFT JOIN pui_profile p ON
	p.profile = pf.profile
LEFT JOIN pui_profile_tra p_tra ON
	p_tra.profile = p.profile;

CREATE VIEW v_pui_user_functionality AS
SELECT
	up.usr,
	pf.functionality,
	f_tra.name AS functionality_name,
	pf.profile,
	p_tra.name AS profile_name
FROM pui_user_profile up
INNER JOIN pui_profile_functionality pf ON
	pf.profile = up.profile
LEFT JOIN pui_profile p ON
	p.profile = pf.profile
LEFT JOIN pui_functionality f ON
	f.functionality = pf.functionality
LEFT JOIN pui_profile_tra p_tra ON
	p_tra.profile = p.profile
LEFT JOIN pui_functionality_tra f_tra ON
	f_tra.functionality = f.functionality AND
	f_tra.lang = p_tra.lang
WHERE f_tra.lang = 'es';

CREATE VIEW v_pui_docgen_template AS
SELECT
	dt.id,
	dt.name,
	dt.description, 
	dt.main_model,
	dt.models,
	dt.column_filename,
	dm.label,
	dt.filename
FROM pui_docgen_template dt
LEFT JOIN pui_docgen_model dm ON
	dm.model = dt.main_model;
