-- VIEWS SQL SERVER
CREATE VIEW v_pui_document AS
SELECT
	d.id,
	d.model,
	d.pk,
	d.description,
	d.language,
	d.filename,
	d.filename_orig,
	d.role,
	dr_tra.description as role_description,
	d.thumbnails,
	case
		when (select count(1) from pui_variable v where v.variable = 'DOCUMENTS_BASE_URL' and v.value <> '-') = 1 then
			(select v.value from pui_variable v where v.variable = 'DOCUMENTS_BASE_URL')
			+ d.model + '/'
			+ d.pk + '/'
			+ d.filename
		else null
	end as url,
	d.datetime,
	dr_tra.lang
FROM pui_document d
LEFT JOIN pui_document_role dr ON
	dr.role = d.role
LEFT JOIN pui_document_role_tra dr_tra ON
	dr_tra.role = dr.role;

CREATE VIEW v_pui_docgen_model AS
SELECT
	dm.model,
	m.entity,
	dm.label + ' (' + m.entity + ')' AS label
FROM pui_docgen_model dm
LEFT JOIN pui_model m ON
	m.model = dm.model;
