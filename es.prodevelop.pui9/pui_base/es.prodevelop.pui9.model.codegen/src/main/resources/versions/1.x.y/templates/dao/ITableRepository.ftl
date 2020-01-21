<#compress>
package ${config.server.daoJavaPackage}.model.repository;

import org.springframework.stereotype.Repository;

import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName};
import ${config.server.dtoJavaPackage}.model.dto.interfaces.I${config.selectedTable.javaName}Pk;

import es.prodevelop.pui9.model.repository.interfaces.IRepository;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;

/**
 * @generated
 */
@Repository
public interface I${config.selectedTable.javaName}Repository extends IRepository<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}>, ITableDao<I${config.selectedTable.javaName}Pk, I${config.selectedTable.javaName}> {
<#list config.selectedTable.columns as column>
	/** 
	 * @generated
	 */
	java.util.List<I${config.selectedTable.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException;
</#list>
}
</#compress>
