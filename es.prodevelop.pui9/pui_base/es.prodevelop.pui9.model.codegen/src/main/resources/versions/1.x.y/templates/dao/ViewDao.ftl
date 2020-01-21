<#compress>
package ${config.server.daoJavaPackage}.model.views.dao;

import es.prodevelop.pui9.model.dao.AbstractViewDao;

import ${config.server.daoJavaPackage}.model.views.dao.interfaces.I${config.selectedView.javaName}Dao;
import ${config.server.dtoJavaPackage}.model.views.dto.interfaces.I${config.selectedView.javaName};

import es.prodevelop.pui9.exceptions.PuiDaoFindException;

import org.springframework.stereotype.Repository;

/**
 * @generated
 */
@Repository
public class ${config.selectedView.javaName}Dao extends AbstractViewDao<I${config.selectedView.javaName}> implements I${config.selectedView.javaName}Dao {
<#list config.selectedView.columns as column>
	/** 
	 * @generated
	 */
	@Override
	public java.util.List<I${config.selectedView.javaName}> findBy${column.javaName?cap_first}(${column.javaTypeString} ${column.javaName}) throws PuiDaoFindException {
		return super.findByColumn(I${config.selectedView.javaName}<#if column.pk>Pk</#if>.${column.dbName?upper_case}_FIELD, ${column.javaName});
	}
</#list>
}
</#compress>
