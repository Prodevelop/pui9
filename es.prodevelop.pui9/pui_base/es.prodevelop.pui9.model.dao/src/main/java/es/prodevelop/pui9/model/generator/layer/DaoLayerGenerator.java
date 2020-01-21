package es.prodevelop.pui9.model.generator.layer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;
import es.prodevelop.pui9.model.generator.layers.AbstractDaoLayerGenerator;

/**
 * This class represents the DAO Layer Generator for JDBC approach, to generate
 * all the files for the PUI Server.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DaoLayerGenerator extends AbstractDaoLayerGenerator {

	public static final String subpackageTableDaoName = ".model.dao";
	public static final String subpackageTableDaoInterfacesName = subpackageTableDaoName + ".interfaces";
	public static final String subpackageViewDaoName = ".model.views.dao";
	public static final String subpackageViewDaoInterfacesName = subpackageViewDaoName + ".interfaces";

	@Override
	protected List<TemplateFileInfo> getTableDaoTemplates() {
		TemplateFileInfo idao = new TemplateFileInfo("dao/ITableDao.ftl",
				"I" + DynamicClassGenerator.filenameWildcard + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageTableDaoInterfacesName);
		TemplateFileInfo dao = new TemplateFileInfo("dao/TableDao.ftl",
				DynamicClassGenerator.filenameWildcard + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageTableDaoName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idao);
		list.add(dao);

		return list;
	}

	@Override
	protected List<TemplateFileInfo> getViewDaoTemplates() {
		TemplateFileInfo idao = new TemplateFileInfo("dao/IViewDao.ftl",
				"I" + DynamicClassGenerator.filenameWildcard + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageViewDaoInterfacesName);
		TemplateFileInfo dao = new TemplateFileInfo("dao/ViewDao.ftl",
				DynamicClassGenerator.filenameWildcard + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageViewDaoName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idao);
		list.add(dao);

		return list;
	}

	@Override
	protected boolean classPassFilter(Class<?> clazz) {
		return !clazz.isInterface();
	}

	@Override
	protected void registerBeanDefinition(Class<?> clazz) {
		String className = clazz.getName().replace(".", "/") + JavaFileObject.Kind.CLASS.extension;
		URL url = PuiDynamicClassLoader.getInstance().getResource(className);
		UrlResource ur = new UrlResource(url);

		RootBeanDefinition rbd = new RootBeanDefinition(clazz);
		rbd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		rbd.setSource(ur);
		rbd.setResource(ur);

		definitionRegistry.registerBeanDefinition(StringUtils.uncapitalize(clazz.getSimpleName()), rbd);
	}

}
