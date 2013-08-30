package xdi2.core.impl.json.mongodb;


import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import xdi2.core.GraphFactory;
import xdi2.core.impl.json.AbstractJSONGraphFactory;
import xdi2.core.impl.json.JSONStore;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * GraphFactory that creates JSON graphs in MongoDB.
 * 
 * @author markus
 */
public class MongoDBJSONGraphFactory extends AbstractJSONGraphFactory implements GraphFactory {

	public static final String DEFAULT_HOST = "localhost";
	public static final Integer DEFAULT_PORT = null;

	private String host;
	private Integer port;

	public MongoDBJSONGraphFactory() { 

		super();

		this.host = DEFAULT_HOST;
		this.port = DEFAULT_PORT;
	}

	@Override
	protected JSONStore openJSONStore(String identifier) throws IOException {

		// create mongo client

		MongoClient mongoClient;

		if (this.getPort() != null) {

			mongoClient = new MongoClient(this.getHost(), this.getPort());
		} else {

			mongoClient = new MongoClient(this.getHost());
		}

		// open DB

		String dbName = identifier == null ? "" : new String(Base64.encodeBase64(identifier.getBytes("UTF-8")), "UTF-8") + ".";
		dbName = "woot";

		DB db = mongoClient.getDB(dbName);

		// open store

		JSONStore jsonStore;

		jsonStore = new MongoDBJSONStore(mongoClient, db);
		jsonStore.init();

		// done

		return jsonStore;
	}

	public String getHost() {

		return this.host;
	}

	public void setHost(String host) {

		this.host = host;
	}

	public Integer getPort() {

		return this.port;
	}

	public void setPort(Integer port) {

		this.port = port;
	}
}