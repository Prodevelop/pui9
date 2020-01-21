-- procedures
create PROCEDURE enableConstraints AS
	sen VARCHAR2(1000);
	CURSOR curPK IS
		select 'alter table '||uc.table_name||' modify constraint '||uc.constraint_name||' enable' as sentence
		from user_constraints uc
		where uc.constraint_type = 'P';
	CURSOR curUQ IS
		select 'alter table '||uc.table_name||' modify constraint '||uc.constraint_name||' enable' as sentence
		from user_constraints uc
		where uc.constraint_type = 'U';
	CURSOR curFK IS
		select 'alter table '||uc.table_name||' modify constraint '||uc.constraint_name||' enable' as sentence
		from user_constraints uc
		where uc.constraint_type = 'R';
	CURSOR curOT IS
		select 'alter table '||uc.table_name||' modify constraint '||uc.constraint_name||' enable' as sentence
		from user_constraints uc
		where uc.constraint_type not in ('P', 'U', 'R');
BEGIN
	OPEN curPK;
	LOOP
		FETCH curPK INTO sen;
		EXIT WHEN curPK%NOTFOUND;
		EXECUTE IMMEDIATE sen;
	END LOOP;
	CLOSE curPK;

	OPEN curUQ;
	LOOP
		FETCH curUQ INTO sen;
		EXIT WHEN curUQ%NOTFOUND;
		EXECUTE IMMEDIATE sen;
	END LOOP;
	CLOSE curUQ;

	OPEN curFK;
	LOOP
		FETCH curFK INTO sen;
		EXIT WHEN curFK%NOTFOUND;
		EXECUTE IMMEDIATE sen;
	END LOOP;
	CLOSE curFK;

	OPEN curOT;
	LOOP
		FETCH curOT INTO sen;
		EXIT WHEN curOT%NOTFOUND;
		EXECUTE IMMEDIATE sen;
	END LOOP;
	CLOSE curOT;
END;
/

create PROCEDURE disableConstraints AS
	sen VARCHAR2(1000);
	CURSOR cur IS
		select 'alter table '||table_name||' disable constraint '||constraint_name as sentence
		from user_constraints
		where constraint_type in ('P', 'U', 'R');
BEGIN
	OPEN cur;
	LOOP
		FETCH cur INTO sen;
		EXIT WHEN cur%NOTFOUND;
		EXECUTE IMMEDIATE sen;
	END LOOP;
	CLOSE cur;
END;
/
