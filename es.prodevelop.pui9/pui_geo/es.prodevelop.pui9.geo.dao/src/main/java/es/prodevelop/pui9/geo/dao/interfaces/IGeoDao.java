package es.prodevelop.pui9.geo.dao.interfaces;

import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

public interface IGeoDao<TPK extends IGeoDto, T extends TPK> extends ITableDao<TPK, T> {

	Integer getSrid(String tableName);

	IDatabaseGeoHelper getSqlAdapter();

}
