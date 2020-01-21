package es.prodevelop.pui9.elasticsearch;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;

/**
 * This class is an adaptation of ElasticSearch for PUI.<br>
 * <br>
 * This component connects to an external ElasticSearch Node, defined by some
 * PUI variables<br>
 * <br>
 * This component works as a Client of an external Node.<br>
 * <br>
 * It brings a set of common operations to perform with client, such as manage
 * indices and document
 */
@Component
public class PuiElasticSearchManager {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IPuiVariableService variableService;

	private RestHighLevelClient client;
	private boolean initialized;

	/**
	 * Initialize the ElasticSearch client
	 */
	private void connectClient() {
		try {
			String address = variableService.getVariable(PuiVariableValues.ELASTICSEARCH_NODE_ADDRESS.name());
			Integer httpPort = variableService.getVariable(Integer.class,
					PuiVariableValues.ELASTICSEARCH_NODE_PORT.name());

			client = new RestHighLevelClient(RestClient.builder(new HttpHost(address, httpPort)));

			if (isConnected()) {
				MainResponse resp = client.info(RequestOptions.DEFAULT);
				logger.info("Connected to Elastic Search:");
				logger.info("\tCluster name: " + resp.getClusterName());
				logger.info("\tCluster id: " + resp.getClusterUuid());
				logger.info("\tNode name: " + resp.getNodeName());
				logger.info("\tElasticSearch version: " + resp.getVersion().getNumber());
			} else {
				logger.info("Elastic Search is available but not connected to any node");
			}

			initialized = true;
		} catch (Exception e) {
			logger.info("Elastic Search is not available");
			initialized = false;
		}
	}

	@PreDestroy
	private void preDestroy() {
		closeClient();
	}

	private void closeClient() {
		if (client != null) {
			try {
				client.close();
				client = null;
			} catch (IOException e) {
				// do nothing
			}
		}
	}

	/**
	 * Get the {@link RestHighLevelClient} object of ElasticSearch
	 * 
	 * @return The {@link RestHighLevelClient} object
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	public RestHighLevelClient getClient() throws PuiElasticSearchNoNodesException {
		if (client == null) {
			connectClient();
		}

		if (isConnected()) {
			return client;
		} else {
			throw new PuiElasticSearchNoNodesException();
		}
	}

	private boolean isConnected() {
		try {
			return client.ping(RequestOptions.DEFAULT);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Change the active status of ElasticSearch
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		variableService.modifyVariable(PuiVariableValues.ELASTICSEARCH_ACTIVE.name(), String.valueOf(active));
	}

	/**
	 * Check if Elastic Search is active or not
	 * 
	 * @return active
	 */
	public boolean isActive() {
		try {
			getClient();
		} catch (PuiElasticSearchNoNodesException e) {
			// do nothing
		}

		return initialized && variableService.getVariable(Boolean.class, PuiVariableValues.ELASTICSEARCH_ACTIVE.name());
	}

}
