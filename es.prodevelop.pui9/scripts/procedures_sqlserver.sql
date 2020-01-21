-- procedures
CREATE PROCEDURE ENABLECONSTRAINTS AS
BEGIN
	declare @tname varchar(128), @tschema varchar(128)
	declare tables cursor for
		select TABLE_SCHEMA, TABLE_NAME
		from INFORMATION_SCHEMA.TABLES
		where table_type = 'BASE TABLE'

	open tables

	fetch next from tables
	into @tschema, @tname

	while @@FETCH_STATUS = 0
	begin
		execute ('alter table [' + @tschema + '].[' + @tname + '] check constraint all')
		fetch next from tables into @tschema, @tname
	end

	close tables
	deallocate tables
END
GO

CREATE PROCEDURE DISABLECONSTRAINTS AS
BEGIN
	declare @tname varchar(128), @tschema varchar(128)

	declare tables cursor for
		select TABLE_SCHEMA, TABLE_NAME
		from INFORMATION_SCHEMA.TABLES
		where table_type = 'BASE TABLE'

	open tables

	fetch next from tables
	into @tschema, @tname

	while @@FETCH_STATUS = 0
	begin
		execute ('alter table [' + @tschema + '].[' + @tname + '] nocheck constraint all')
		fetch next from tables into @tschema, @tname
	end

	close tables
	deallocate tables
END
GO
