package com.alks.service.config;
 
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.alks.service.delegate.AccountMaster;
import com.alks.service.delegate.GenerateKeyDelegate;
import com.alks.service.impl.ADGroupsServiceImpl;
 
@Path("/getkey")
@Consumes({"application/json"})
public class GetKeyService {
	@GET
	@Produces("application/xml")
	public String getkeyUsage() {

		String result = "AccessKey/SecretKey";
		return "<keyservice>" + "<input>" +"system id/password"+ "</input>" + "<output>" + result + "</output>" + "</keyservice>";
	}
 
	@Path("{userid}/{password}")
	@GET
	@Produces("application/xml")
	public String getADGroups(@PathParam("userid") String userid, @PathParam("password") String password) {
		List<String> adgroups = ADGroupsServiceImpl.authenticate(userid, password);
		return "<keyservice>" + "<input>" +"system id/password"+ "</input>" + "<output>" + adgroups + "</output>" + "</keyservice>";

	}

	@Path("{userid}/{password}/{adgroup}")
	@GET
	@Produces("application/xml")
	public String getKeys(@PathParam("userid") String userid, @PathParam("password") String password, @PathParam("adgroup") String adgroup) {
		List<String> adgroups = ADGroupsServiceImpl.authenticate(userid, password);
		Map<String,List<String>> accounts = AccountMaster.getAccounts(adgroups);
		
		if(adgroups.contains(adgroup)){
		
		return  "<keyservice>" + "<input>" +"system id/password"+ " <adgroup> " + adgroup + " </adgroup> </input>" +"<output>" + "<adgroups>" + adgroups + "</adgroups>" 
				+	"<accounts>" + accounts +"</accounts>"
				+"</output>" +"</keyservice>";
		}else{
		return 	 "<keyservice>" + "<input>" +"system id/password"+ " <adgroup> " + adgroup + " </adgroup> </input>" +"<output>" + "<adgroups>" + adgroups + "</adgroups>" 
				+	"<error> No permissions to the AD group provided </error>"
				+"</output>" +"</keyservice>";
		}

	}

	@Path("{userid}/{password}/{account}/{accountuser}/{role}")
	@GET
	@Produces("application/xml")
	public String getKeys(@PathParam("userid") String userid, @PathParam("password") String password,  
			@PathParam("account") String account,@PathParam("accountuser") String accountuser,@PathParam("role") String role) {
		List<String> adgroups = ADGroupsServiceImpl.authenticate(userid, password);
		String returnString = null;
		account = account + "/"+accountuser;
		if(adgroups==null){
			returnString = "Invalid Credentails";
		}else if(adgroups.size()==0){
			returnString = "No permissions, please check with administrator";			
		}else{
			Map<String,List<String>> accounts = AccountMaster.getAccountsByAcctNo(adgroups);
			if(accounts==null || accounts.size()==0){
				returnString = "No permissions, please check with administrator";							
			}else{
				ArrayList<String> acctRoles = new ArrayList(accounts.get(account));
				if(acctRoles==null || acctRoles.size()==0){
					returnString = "No permissions for account #" + account + " , please check with administrator";												
				}else{
					if(acctRoles.contains(role)){
						String keys[] = GenerateKeyDelegate.getKeys(userid.substring(0,userid.indexOf("@")), account, role, "10");
						returnString =  "<key>"+ "<accesskey>" + keys[0] +"</accesskey>" +"<secretkey>"+ keys[1] +"</secretkey>"+"<sessionToken>"+ keys[2] +"</sessionToken>"+"</key>";			
					}else{
						returnString = "No "+role +" permission for account #" + account + " , please check with administrator";												
					}
				}
			}
		}
		
		String rs =  "<keyservice>" + "<input>" +"system id/password" + "<accountno>" + account + "</accountno>" 
				+	"<role>" + role +"</role>"
				+ "</input>" +"<output>" 
				+ returnString
				+ "</output>" +"</keyservice>";
		
		return rs;
	}

}