-- languages
INSERT INTO pui_language VALUES ('es', 'Español', '1');
INSERT INTO pui_language VALUES ('en', 'English', '0');
INSERT INTO pui_language VALUES ('fr', 'Français', '0');
INSERT INTO pui_language VALUES ('ca', 'Català', '0');

-- models
INSERT INTO pui_model VALUES ('puiaudit', 'v_pui_audit', '{"order":[{"column":"datetime","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('login', 'v_pui_login', '{"order":[{"column":"id","direction":"desc"}]}', null);
INSERT INTO pui_model VALUES ('puidocgenattribute', 'pui_docgen_attribute', '{"order":[{"column":"label","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puidocgentemplate', 'v_pui_docgen_template', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puidocgenmodel', 'v_pui_docgen_model', '{"order":[{"column":"label","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puidocument', 'v_pui_document', '{"order":[{"column":"id","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puifunctionality', 'v_pui_functionality', '{"order":[{"column":"functionality","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puilanguage', 'v_pui_language', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiprofile', 'v_pui_profile', '{"order":[{"column":"profile","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puisubsystem', 'v_pui_subsystem', '{"order":[{"column":"subsystem","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiuser', 'v_pui_user', '{"order":[{"column":"usr","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puivariable', 'v_pui_variable', '{"order":[{"column":"variable","direction":"asc"}]}', null);

-- profiles
INSERT INTO pui_profile VALUES ('ADMIN_PUI');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'es', 1, 'Administración');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'en', 1, 'Administration');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'fr', 1, 'Administration');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'ca', 1, 'Administració');

INSERT INTO pui_profile VALUES ('ANONYMOUS');
INSERT INTO pui_profile_tra VALUES ('ANONYMOUS', 'es', 1, 'Anónimo');
INSERT INTO pui_profile_tra VALUES ('ANONYMOUS', 'en', 1, 'Anonymous');
INSERT INTO pui_profile_tra VALUES ('ANONYMOUS', 'fr', 1, 'Anonymous');
INSERT INTO pui_profile_tra VALUES ('ANONYMOUS', 'ca', 1, 'Anònim');

-- subsystems
INSERT INTO pui_subsystem VALUES ('PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'es', 1, 'Administración de PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'en', 1, 'PUI Administration');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'fr', 1, 'Administration de PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'ca', 1, 'Administració de PUI');

-- common functionalities
INSERT INTO pui_functionality values ('READ_PUI_AUDIT', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'es', 1, 'Visualización de auditoría');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'en', 1, 'View PUI Activity');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'fr', 1, 'View PUI Activity');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'ca', 1, 'Visualització d''auditoria');

INSERT INTO pui_functionality values ('READ_PUI_FUNCTIONALITY', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'es', 1, 'Visualización de funcionalidades');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'en', 1, 'View funcionalities');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'fr', 1, 'View funcionalities');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'ca', 1, 'Visualització de funcionalitats');

INSERT INTO pui_functionality values ('READ_PUI_LANGUAGE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'es', 1, 'Visualización de Idiomas');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'en', 1, 'View languages');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'fr', 1, 'View languages');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'ca', 1, 'Visualització d''idiomes');

INSERT INTO pui_functionality values ('WRITE_PUI_LANGUAGE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'es', 1, 'Edición de Idiomas');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'en', 1, 'Manage languages');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'fr', 1, 'Manage languages');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'ca', 1, 'Edició d''idiomes');

INSERT INTO pui_functionality values ('READ_PUI_PROFILE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'es', 1, 'Visualización de perfiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'en', 1, 'View profiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'fr', 1, 'View profiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'ca', 1, 'Visualització de perfils');

INSERT INTO pui_functionality values ('WRITE_PUI_PROFILE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'es', 1, 'Edición de perfiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'en', 1, 'Manage profiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'fr', 1, 'Manage profiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'ca', 1, 'Edició de perfils');

INSERT INTO pui_functionality values ('READ_PUI_USER', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'es', 1, 'Visualización de usuarios');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'en', 1, 'View application users');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'fr', 1, 'View application users');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'ca', 1, 'Visualització d''usuaris');

INSERT INTO pui_functionality values ('WRITE_PUI_USER', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'es', 1, 'Edición de usuarios');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'en', 1, 'Manage application users');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'fr', 1, 'Manage application users');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'ca', 1, 'Edició d''usuaris');

INSERT INTO pui_functionality values ('CHANGE_USER_PASSWORDS', 'PUI');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'es', 1, 'Modificar contraseñas de los usuarios');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'en', 1, 'Modify users passwords');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'fr', 1, 'Modifier les mots de passe des utilisateurs');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'ca', 1, 'Modificar contrasenyes dels usuaris');

INSERT INTO pui_functionality values ('READ_PUI_VARIABLE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'es', 1, 'Visualización de variables');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'en', 1, 'View variable');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'fr', 1, 'View variable');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'ca', 1, 'Visualització de variables');

INSERT INTO pui_functionality values ('WRITE_PUI_VARIABLE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'es', 1, 'Edición de variables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'en', 1, 'Manage variable');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'fr', 1, 'Manage variable');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'ca', 1, 'Edició de variables');

INSERT INTO pui_functionality values ('READ_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'es', 1, 'Visualización para tablas generadas automáticamente');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'en', 1, 'View autogen tables');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'fr', 1, 'View autogen tables');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'ca', 1, 'Visualització de taules generades automàticament');

INSERT INTO pui_functionality values ('WRITE_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'es', 1, 'Edición de tablas generadas automáticamente');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'en', 1, 'Manage autogen tables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'fr', 1, 'Manage autogen tables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'ca', 1, 'Edició de taules generades automàticament');

INSERT INTO pui_functionality values ('GEN_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'es', 1, 'Generación automática de tablas');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'en', 1, 'Generate autogen tables');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'fr', 1, 'Generate autogen tables');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'ca', 1, 'Generació automàtica de taules');

INSERT INTO pui_functionality values ('LIST_LOGIN', 'PUI');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'es', 1, 'Visualización de logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'en', 1, 'View PUI Logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'fr', 1, 'View PUI Logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'ca', 1, 'Visualització de logins');

INSERT INTO pui_functionality values ('LIST_PUI_SESSIONS', 'PUI');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'es', 1, 'Listar sesiones abiertas');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'en', 1, 'List opened sessions');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'fr', 1, 'Liste des sessions ouvertes');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'ca', 1, 'Llistar sessions obertes');

INSERT INTO pui_functionality values ('KILL_PUI_SESSIONS', 'PUI');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'es', 1, 'Eliminar sesiones abiertas');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'en', 1, 'Remove opened sessions');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'fr', 1, 'Supprimer les sessions ouvertes');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'ca', 1, 'Eliminar sessions obertes');

INSERT INTO pui_functionality values ('EXECUTE_IMPORT_EXPORT','PUI');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'ca', 1, 'Acció de Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'en', 1, 'Action Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'fr', 1, 'Action Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'es', 1, 'Acción de Import/Export');

INSERT INTO pui_functionality values ('EXECUTE_COPY','PUI');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','es', 1, 'Acción de Copiar Registro');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','en', 1, 'Action Copy Registry');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','fr', 1, 'Action copier un document');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','ca', 1, 'Acció de Copiar Registre');

-- elasticsearch functionalities
INSERT INTO pui_functionality values ('READ_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'es', 1, 'Consultas ElasticSearch');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'en', 1, 'ElasticSearch requests');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'fr', 1, 'ElasticSearch demandes');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'ca', 1, 'Consultes ElasticSearch');

INSERT INTO pui_functionality values ('WRITE_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'es', 1, 'Modificaciones ElasticSearch');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'en', 1, 'ElasticSearch modifications');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'fr', 1, 'ElasticSearch modifications');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'ca', 1, 'Modificacions ElasticSearch');

INSERT INTO pui_functionality values ('ADMIN_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'es', 1, 'Administrar ElasticSearch');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'en', 1, 'Manage modifications');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'fr', 1, 'ElasticSearch admin');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'ca', 1, 'Administrar ElasticSearch');

-- docgen functionalities
INSERT INTO pui_functionality values ('WRITE_PUI_DOCGEN', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'es', 1, 'Edición de Generación de Documentos');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'en', 1, 'Manage Document Generation');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'fr', 1, 'Manage Document Generation');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'ca', 1, 'Edició de Generació de Documents');

INSERT INTO pui_functionality values ('GEN_PUI_DOCGEN', 'PUI');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'es', 1, 'Generación de Documentos');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'en', 1, 'Document Generation');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'fr', 1, 'Document Generation');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'ca', 1, 'Generació de Documents');

-- documents functionalities
INSERT INTO pui_functionality values ('READ_PUI_DOCUMENT', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'es', 1, 'Visualización de documentos adjuntos');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'en', 1, 'View documents');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'fr', 1, 'View documents');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'ca', 1, 'Visualització de documents adjunts');

INSERT INTO pui_functionality values ('WRITE_PUI_DOCUMENT', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'es', 1, 'Edición documentos');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'en', 1, 'Manage documents');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'fr', 1, 'Manage documents');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'ca', 1, 'Edició de documents');

-- menu
INSERT INTO pui_menu VALUES (1000, null, null, null, null, 'menu.accounts', 'fal fa-users');
INSERT INTO pui_menu VALUES (1001, 1000, 'puiuser', null, 'WRITE_PUI_USER', 'menu.puiuser', null);
INSERT INTO pui_menu VALUES (1002, 1000, 'puiprofile', null, 'READ_PUI_PROFILE', 'menu.puiprofile', null);
INSERT INTO pui_menu VALUES (1003, 1000, 'puifunctionality', null, 'READ_PUI_FUNCTIONALITY', 'menu.puifunctionality', null);
INSERT INTO pui_menu VALUES (2000, null, null, null, null, 'menu.configuration', 'fal fa-sliders-h');
INSERT INTO pui_menu VALUES (2001, 2000, 'puilanguage', null, 'WRITE_PUI_LANGUAGE', 'menu.puilanguage', null);
INSERT INTO pui_menu VALUES (2002, 2000, 'puivariable', null, 'WRITE_PUI_VARIABLE', 'menu.puivariable', null);
INSERT INTO pui_menu VALUES (2003, 2000, null, null, null, 'menu.elasticsearch', null);
INSERT INTO pui_menu VALUES (2004, 2000, 'login', null, 'LIST_LOGIN', 'menu.puilogin', null);
INSERT INTO pui_menu VALUES (2005, 2000, null, 'session', 'LIST_PUI_SESSIONS', 'menu.puisession', null);
INSERT INTO pui_menu VALUES (2006, 2000, 'puidocgentemplate', null, 'GEN_PUI_DOCGEN', 'menu.puidocgen', null);

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_AUDIT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_FUNCTIONALITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_LANGUAGE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_LANGUAGE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_PROFILE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_PROFILE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_USER');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_USER');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'CHANGE_USER_PASSWORDS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_VARIABLE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_VARIABLE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'GEN_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'LIST_LOGIN');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'LIST_PUI_SESSIONS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'KILL_PUI_SESSIONS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_DOCGEN');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'GEN_PUI_DOCGEN');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_DOCUMENT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_DOCUMENT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'EXECUTE_IMPORT_EXPORT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'EXECUTE_COPY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_ELASTICSEARCH');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_ELASTICSEARCH');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'ADMIN_PUI');

-- users
INSERT INTO pui_user VALUES ('admin', 'Admin', '$2a$10$z7T1x/t8rhJgPLA/0DijL.4XQXeruaRkYweDx3h.OrlvS0M04vHAm', 'es', 'admin@prodevelop.es', 0, null); -- admin
INSERT INTO pui_user VALUES ('anonymous', 'Anonymous', '$2a$10$QxVszsreux9jwGHPP5Sh/.wqVyWcIBP69O.AjeA4qc62Ya4RY7lKS', 'es', 'anonymous@prodevelop.es', 0, null); -- anonymous

-- users-profiles
INSERT INTO pui_user_profile VALUES ('admin', 'ADMIN_PUI');
INSERT INTO pui_user_profile VALUES ('anonymous', 'ANONYMOUS');

-- document extensions
INSERT INTO pui_document_extension VALUES ('*', null);

-- document roles
INSERT INTO pui_document_role VALUES ('attachment');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'es', 1, 'Rol para adjuntos');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'en', 1, 'An attachment role');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'fr', 1, 'Un rôle d''attachement');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'ca', 1, 'Rol per a adjunts');

-- variables
INSERT INTO pui_variable values ('APPLICATION_LEGAL_TEXT', '-', 'Texto legal de la aplicación');
INSERT INTO pui_variable values ('BASE_CLIENT_URL','http://localhost/admin','URL base de la web cliente');
INSERT INTO pui_variable values ('DEVELOPMENT_ENVIRONMENT', 'true', 'indica si estamos en el entorno de desarrollo-preproducción o producción (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_PATH', '/var/www/html/app/documents', 'ruta absoluta donde se almacenan los documentos adjuntos');
INSERT INTO pui_variable values ('DOCUMENTS_BASE_URL', '-', 'URL base para obtener ficheros adjuntos a través del Apache. Ejemplo: ''http://localhost/appdocs/''. Si no se quiere, poner valor ''-''');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_GENERATE', 'false', 'indica si se deben generar thumbnails (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_VALUES', '150x150,240x180,640x480', 'lista separada por comas de los tamaños de los thumbnails para documentos adjuntos de tipo imagen');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_CROP', 'true', 'indica si las imágenes se deben cortar en caso de tener que reducir su tamaño (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_SHOW_ROLE', 'true', 'indica si se quiere seleccionar el rol en los documentos (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_SHOW_LANGUAGE', 'true', 'indica si se quiere seleccionar el idioma en los documentos (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_CLEAN_ENABLED', 'false', 'habilita el proceso de limpieza de Documentos no existentes (true/false)');
INSERT INTO pui_variable values ('DOCGEN_PATH', '/var/www/html/app/docgen', 'ruta absoluta donde se almacenan las plantillas para generación de documentos');
INSERT INTO pui_variable values ('DOCGEN_TO_PDF', 'true', 'indica si los documentos generados deben convertirse a PDF (true/false)');
INSERT INTO pui_variable values ('FCM_ENABLED', 'true', 'indica si las notificaciones push están habilitadas o no (true/false)');
insert into pui_variable values ('FCM_TOPIC_PREFIX', 'DESA_', 'Prefijo para los topics. Se recomienda que contenga el nombre del entorno de despliegue (DESA_/PRE_/PRO_)');
INSERT INTO pui_variable values ('ELASTICSEARCH_ACTIVE', 'true', 'Si Elastic Search está activado para la aplicación o no (true/false)');
INSERT INTO pui_variable values ('ELASTICSEARCH_CLUSTER_NAME', 'elasticsearch_desa', 'Nombre del cluster de Elastic Search');
INSERT INTO pui_variable values ('ELASTICSEARCH_NODE_ADDRESS', 'svlelastic01.prodevelop.es', 'Servidor donde se encuentra Elastic Search');
INSERT INTO pui_variable values ('ELASTICSEARCH_NODE_PORT', '9300', 'Puerto de conexión de Elastic Search');
INSERT INTO pui_variable values ('ELASTICSEARCH_INDEX_PREFIX', 'app_index_prefix', 'Prefijo para los índices de la aplicación');
INSERT INTO pui_variable values ('GRIDFILTER_SHOW_SQL_BTN', 'false' , 'true/false, indica si se debe mostrar el botón SQL en la definición de filtros de usuario');
INSERT INTO pui_variable values ('GRIDFILTER_SHOW_SUBGROUP_BTN', 'false', 'true/false, indica si se debe mostrar el botón añadir subgrupo en la definición de filtros de usuario');
INSERT INTO pui_variable values ('IMPORTEXPORT_PATH', '/var/www/html/app/importexport', 'ruta absoluta donde se almacenan los ficheros de import/export');
INSERT INTO pui_variable values ('LOGIN_ALLOW_ANONYMOUS', 'true', 'permitir sesiones de usuarios anónimos (usuario anonymous)');
INSERT INTO pui_variable values ('MAIL_SMTP_HOST', 'smtp.office365.com', 'Host SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_PORT', '587', 'Puerto SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_USER', 'no-reply@prodevelop.es', 'Usuario SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_PASS', 'k0BO.Xe902ef46ET', 'Contraseña del usuario SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_AUTH', 'true', 'Usar autenticación en el servidor SMTP (true/false)');
INSERT INTO pui_variable values ('MAIL_SMTP_STARTTLS_ENABLE', 'true', 'Usar seguridad TLS en el servidor SMTP (true/false)');
INSERT INTO pui_variable values ('MAIL_FROM', 'no-reply@prodevelop.es', 'Email por defecto para el FROM. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('PASSWORD_CHANGE_MAIL_SUBJECT_LABEL_ID', 'requestResetPasswordSubject', 'Etiqueta de traducción del título del email de cambio de contraseña');
INSERT INTO pui_variable values ('PASSWORD_PATTERN', '', 'descripción del patrón. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('SESSION_JWT_SECRET', 'YIokUXGTm64fMDR5QpAGtJxF4CUbs9pE', 'clave secreta para cifrar los tokens JWT');
INSERT INTO pui_variable values ('SESSION_TIMEOUT', '30', 'tiempo en minutos para invalidar la sesión de usuario');

-- activity type
INSERT INTO pui_audit_type VALUES ('insert', 'a new record was inserted');
INSERT INTO pui_audit_type VALUES ('update', 'a record was updated');
INSERT INTO pui_audit_type VALUES ('patch', 'a record was updated partially');
INSERT INTO pui_audit_type VALUES ('delete', 'a record was deleted');
INSERT INTO pui_audit_type VALUES ('list', 'PUI list action was executed');
INSERT INTO pui_audit_type VALUES ('get', 'an existing record was retrieved');
INSERT INTO pui_audit_type VALUES ('template', 'a new record was requested');
INSERT INTO pui_audit_type VALUES ('signin', 'user was logged');
INSERT INTO pui_audit_type VALUES ('signout', 'user closed the sesion');
INSERT INTO pui_audit_type VALUES ('upload_document', 'a new document has been inserted');

