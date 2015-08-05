package com.alks.dao;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.alks.service.aws.BaseAWS;
import com.alks.service.config.MessageUtils;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.AttributeEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.providers.EncryptionMaterialsProvider;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.providers.SymmetricStaticProvider;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * The base for any database class that encrypts data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class BaseEncryptDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(BaseEncryptDAO.class);

	static{
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	} 
	
	/**
	 * Constructor for BaseEncryptDAO setting credentials and settings for
	 * the database requests that are encrypted
	 *  
	 */
	public BaseEncryptDAO(){
		BaseAWS baseAWS = new BaseAWS();
		client = new AmazonDynamoDBClient(baseAWS.getCredentials());
		String keyFilePass = MessageUtils.getMessage("security.encryption.jceks.file.pass");
		String aesKeyPass = MessageUtils.getMessage("security.encryption.jceks.aes.pass");
		String shaKeyPass = MessageUtils.getMessage("security.encryption.jceks.sha.pass");
		
		try{

			  InputStream keystoreStream = this.getClass().getClassLoader()
                    .getResourceAsStream("alks.jck");

		 	  KeyStore keystore = KeyStore.getInstance("JCEKS");
		 	  keystore.load(keystoreStream, keyFilePass.toCharArray());
		 	  if (!keystore.containsAlias("jceksAES")) {
		 		  throw new RuntimeException("Alias for key not found");
		 	  }
		 	  Key aesKey = keystore.getKey("jceksAES", aesKeyPass.toCharArray());
		 	  SecretKeySpec cek = new SecretKeySpec(aesKey.getEncoded(), "AES");

		 	  if (!keystore.containsAlias("jceksHmacSHA256")) {
		 		  throw new RuntimeException("Alias for key not found");
		 	  }
		 	  Key shaKey = keystore.getKey("jceksHmacSHA256", shaKeyPass.toCharArray());
		 	  
		 	  SecretKeySpec sks = new SecretKeySpec(shaKey.getEncoded(),"HmacSHA256");
		 	  
		 	  
			EncryptionMaterialsProvider provider = new SymmetricStaticProvider(cek, sks); 
					
			Region regionEast1 = Region.getRegion(Regions.US_EAST_1);
			client.setRegion(regionEast1);
			
			mapper = new DynamoDBMapper(client,DynamoDBMapperConfig.DEFAULT,new AttributeEncryptor(provider));
			dynamoDB = new DynamoDB(client);
			
		}catch(Exception e){
			logger.error(e);
		}
	}
	
}