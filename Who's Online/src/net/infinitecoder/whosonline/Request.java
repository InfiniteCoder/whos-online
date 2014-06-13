package net.infinitecoder.whosonline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

public class Request {
	
	public static boolean isValidated;
	private static int validated = -1;
	
	public static String secretKey() {
		return Manager.getSecretKey();
	}
	
	public static String createServer() {
		return sendRequest("create_server", "secret=" + UUID.randomUUID().toString());
	}
	
	public static String sendValidateRequest() {
		return sendRequest("validate", "secret=" + secretKey());
	}
	
	public static boolean sendIsValidatedRequest() {
		if(validated == -1) {
			String s = sendRequest("validated", "secret=" + secretKey());
			isValidated = !s.equals("false");
			validated = 0;
		}
		return isValidated;
	}
	
	public static String sendRequest(String url, String...strings) {
		url = "http://www.infinitecoder.net/Minecraft/util/" + url + ".php";
		if(strings != null) {
			for(int i=0;i<strings.length;i++) {
				String first = "";
				if(i == 0)
					first = "?";
				else 
					first = "&";
				
				url += first + strings[i];
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line;
			String total = "";
			while((line=reader.readLine())!=null) {
				total += line;
			}
			reader.close();
			return total;
		} catch(Exception e) {
			return "ERROR!";
		}
	}
	
	public static String sendChangeRequest(String serverName, String serverAddress) {
		sendRequest("update_server_info", "secret=" + secretKey(), "name=" + serverName.replaceAll("_", "+"), "ip=" + serverAddress);
		return "Updated successfully";
	}
	
	public static Object[] sendLoginRequest(Player player) {
		sendRequest("create", "username=" + player.getName(), "secret=" + secretKey(), "uuid=" + player.getUniqueId().toString(), "ip=" + player.getAddress().getHostName());
		if(isValidated) {
			return new Object[]{Level.INFO, sendRequest("send", "secret=" + secretKey(), "username=" + player.getName(), "type=login")};
		} else {
			return new Object[]{Level.SEVERE, "Server not validated!"};
		}
	}
	
	public static Object[] sendLogoutRequest(Player player) {
		if(isValidated) {
			return new Object[]{Level.INFO, sendRequest("send", "secret=" + secretKey(), "username=" + player.getName(), "type=logout")};
		} else {
			return new Object[]{Level.SEVERE, "Server not validated!"};
		}
	}
	
}
