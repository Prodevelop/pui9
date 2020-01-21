package es.prodevelop.pui9.model.generator.layer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;

import es.prodevelop.pui9.model.generator.layers.AbstractControllerLayerGenerator;

/**
 * This class represents the Controller Layer Generator, to generate all the
 * files for the PUI Server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class ControllerLayerGenerator extends AbstractControllerLayerGenerator {

	@Autowired
	private PuiRequestMappingHandlerMapping requestMapping;

	@Override
	protected void postCreateBean(Object bean) {
		requestMapping.registerNewMapping(bean);
	}

}
