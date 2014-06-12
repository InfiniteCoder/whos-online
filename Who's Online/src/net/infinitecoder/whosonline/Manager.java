package net.infinitecoder.whosonline;

import java.util.logging.Level;

public class Manager {
	
	private static String secretKey;
	
	public static void init(String secretKey) {
		Manager.secretKey = secretKey;
		Request.sendIsValidatedRequest();
		if(Request.isValidated) {
			WhosOnline.getInstance().logger.log(Level.INFO, "[Who's Online] Server is correctly validated!");
		} else {
			WhosOnline.getInstance().logger.log(Level.SEVERE, "[Who's Online] Server not validated!");
		}
	}
	
	public static void init() {
		Manager.secretKey = Request.createServer();
		System.out.println(secretKey);
		Request.sendValidateRequest();
		Request.sendIsValidatedRequest();
		if(Request.isValidated) {
			WhosOnline.getInstance().logger.log(Level.INFO, "[Who's Online] Server is correctly validated!");
		} else {
			WhosOnline.getInstance().logger.log(Level.SEVERE, "[Who's Online] Something is wrong! Server is not validated, and we just created the server!");
		}
	}
	
	public static String getSecretKey() {
		return secretKey;
	}
	
}
