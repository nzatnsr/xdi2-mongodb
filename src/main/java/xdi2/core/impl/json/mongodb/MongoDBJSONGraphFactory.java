package xdi2.core.impl.json.mongodb;

import java.util.UUID;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xdi2.core.GraphFactory;
import xdi2.core.impl.json.AbstractJSONGraphFactory;
import xdi2.core.impl.json.JSONStore;

/**
 * GraphFactory that creates JSON graphs in MongoDB.
 * 
 * @author markus
 */
public class MongoDBJSONGraphFactory extends AbstractJSONGraphFactory implements GraphFactory {

	private static final Logger log = LoggerFactory.getLogger(MongoDBJSONGraphFactory.class);

	public static final String  DEFAULT_HOST = "localhost";
	public static final Integer DEFAULT_PORT = null;

	private String  host;
	private Integer port;
	private Boolean mockFlag;
	private Boolean hashIdentifierFlag;

	public MongoDBJSONGraphFactory() { 

		super();

		this.host = DEFAULT_HOST;
		this.port = DEFAULT_PORT;
		this.mockFlag = Boolean.FALSE;
		this.hashIdentifierFlag = Boolean.FALSE;
	}

	@Override
	public boolean supportsPersistence() {

		return true;
	}

	@Override
	protected JSONStore openJSONStore(String identifier) throws IOException {

		if (identifier == null) {
			identifier = UUID.randomUUID().toString();
		}

		if (log.isTraceEnabled()) {
			log.trace("openJSONStore for identifier " + identifier);
		}

		if (Boolean.TRUE.equals(this.getHashIdentifierFlag())) {
			identifier = hashIdentifier(identifier);
		}

		MongoDBStore dbStore = MongoDBStore.getMongoDBStore(this.getHost(), this.getPort(), this.getMockFlag());

		JSONStore jsonStore = new MongoDBJSONStore(dbStore, identifier);
		if (jsonStore != null) {
			jsonStore.init();
		} else {
			log.error("openJSONStore for identifier " + identifier + " failed");
		}

		return jsonStore;
	}

	public static String hashIdentifier(String identifier) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return new String(Base64.encodeBase64URLSafe(digest.digest(identifier.getBytes("UTF-8"))));
		} catch (Exception ex) {
			log.error("hashIdentifier " + identifier + "failed", ex);
		}
		return identifier;
	}

	/*
	 * Getters and setters
	 */

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

	public Boolean getMockFlag() {
		return this.mockFlag;
	}

	public void setMockFlag(Boolean mockFlag) {
		this.mockFlag = mockFlag;
	}

	public Boolean getHashIdentifierFlag() {
		return this.hashIdentifierFlag;
	}

	public void setHashIdentifierFlag(Boolean hashIdentifierFlag) {
		this.hashIdentifierFlag = hashIdentifierFlag;
	}
}
