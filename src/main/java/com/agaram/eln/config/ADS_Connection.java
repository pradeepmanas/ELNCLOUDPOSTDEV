package com.agaram.eln.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import org.apache.log4j.Logger;

public class ADS_Connection {

	static final Logger logger = Logger.getLogger(ADS_Connection.class.getName());
	static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	static final String SECURITY_AUTHENTICATION = "simple";

	public static boolean CheckLDAPConnection(String Url, String user_name, String user_password) {
		try {
			Hashtable<String, String> env1 = new Hashtable<>();
			env1.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			env1.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
			env1.put(Context.SECURITY_PRINCIPAL, user_name);
			env1.put(Context.SECURITY_CREDENTIALS, user_password);
			env1.put(Context.PROVIDER_URL, Url);
			try {
				// Connect with ldap
				new InitialLdapContext(env1, null);

				// Connection succeeded
				return true;
			} catch (AuthenticationException e) {
				logger.error("CheckLDAPConnection()-->" + e.getMessage());
				// Connection failed
				return false;
			}
		} catch (Exception ex) {

		}
		return false;
	}

	public static Map<String, List<Map<String, String>>> importADSGroups(Map<String, Object> objCredentials) {
		Map<String, List<Map<String, String>>> rtnListMap = new HashMap<>();

		List<Map<String, String>> lstGroups = new ArrayList<>();
		Map<String, String> errMsg = new HashMap<>();
		try {
			DirContext context = new InitialDirContext(setLDAPCredential(objCredentials));
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			@SuppressWarnings("rawtypes")
			NamingEnumeration answer = context.search("", "(objectclass=group)", searchControls);
			while (answer.hasMore()) {
				SearchResult rslt = (SearchResult) answer.next();
				Attributes attrs = rslt.getAttributes();
				Map<String, String> objAttrs = new HashMap<>();
				objAttrs.put("GroupName", attrs.get("cn").toString().split(":")[1].trim());
				objAttrs.put("sDistinguishedName", attrs.get("distinguishedName").toString().split(":")[1].trim());
				lstGroups.add(objAttrs);
			}
			rtnListMap.put("ADSGroups", lstGroups);
			context.close();

		} catch (AuthenticationNotSupportedException ex) {
			errMsg.put("error", "The authentication is not supported by the server");
			lstGroups.add(errMsg);
			rtnListMap.put("error", lstGroups);
			logger.error("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
			errMsg.put("error", "Incorrect username or password");
			lstGroups.add(errMsg);
			rtnListMap.put("error", lstGroups);
			logger.error("Incorrect username or password");
		} catch (NamingException ex) {
			errMsg.put("error", "Unknown Host...Check Domainname");
			lstGroups.add(errMsg);
			rtnListMap.put("error", lstGroups);
			logger.error("Unknown Host...check Domainname");
		} catch (Exception ex) {
			errMsg.put("error", "Error in creating Context");
			lstGroups.add(errMsg);
			rtnListMap.put("error", lstGroups);
			logger.error("Error in creating Context");
		}

		return rtnListMap;
	}

	public static Hashtable<String, String> setLDAPCredential(Map<String, Object> objCredentials) {

		Hashtable<String, String> envObject = new Hashtable<>();
		envObject.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		envObject.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
		envObject.put(Context.SECURITY_PRINCIPAL, (String) objCredentials.get("sServerUserName"));

		envObject.put(Context.SECURITY_CREDENTIALS, (String) objCredentials.get("sPassword"));
		envObject.put(Context.PROVIDER_URL, "ldap://" + objCredentials.get("sDomainName") + ":3268");

		return envObject;

	}

	public static Map<String, List<Map<String, String>>> importADSUsersByGroup(Map<String, Object> objCredentials) {
		Map<String, List<Map<String, String>>> rtnListMap = new HashMap<>();
		List<Map<String, String>> lstUsers = new ArrayList<>();
		Map<String, String> errMsg = new HashMap<>();
		try {
			DirContext context = new InitialDirContext(setLDAPCredential(objCredentials));
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String sDistinguishedName = (String) objCredentials.get("sDistinguishedName");
			@SuppressWarnings("rawtypes")
			NamingEnumeration answer = context.search("",
					"(&(objectCategory=user)(memberOf=" + sDistinguishedName + "))", searchControls);
			while (answer.hasMore()) {
				SearchResult rslt = (SearchResult) answer.next();
				Attributes attrs = rslt.getAttributes();
				Map<String, String> objAttrs = new HashMap<>();
				objAttrs.put("UserName", attrs.get("cn").toString().split(":")[1].trim());
				objAttrs.put("sDistinguishedName", attrs.get("distinguishedName").toString().split(":")[1].trim());
				objAttrs.put("sImport", "0");
				objAttrs.put("sApprove", "0");
				Attribute userPrincipalName = attrs.get("userPrincipalName");
				if (userPrincipalName == null)
					objAttrs.put("DomainUserID", attrs.get("cn").toString().split(":")[1].trim());
				else
					objAttrs.put("DomainUserID",
							(attrs.get("userPrincipalName").toString().split(":")[1]).split("@")[0].trim());
				lstUsers.add(objAttrs);
			}
			rtnListMap.put("ADSUsersByGroup", lstUsers);
			context.close();

		} catch (AuthenticationNotSupportedException ex) {
			errMsg.put("error", "The authentication is not supported by the server");
			lstUsers.add(errMsg);
			rtnListMap.put("error", lstUsers);
			logger.error("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
			errMsg.put("error", "Incorrect username or password");
			lstUsers.add(errMsg);
			rtnListMap.put("error", lstUsers);
			logger.error("Incorrect username or password");
		} catch (NamingException ex) {
			errMsg.put("error", "Unknown Host...Check Domainname");
			lstUsers.add(errMsg);
			rtnListMap.put("error", lstUsers);
			logger.error("Unknown Host...check Domainname");
		} catch (Exception ex) {
			errMsg.put("error", "Error in creating Context");
			lstUsers.add(errMsg);
			rtnListMap.put("error", lstUsers);
			logger.error("Error in creating Context");
		}
		return rtnListMap;
	}

	public static Boolean CheckDomainLDAPConnection(Map<String, Object> objCredentials) {

		boolean isConnect = false;
		Map<String, Object> errMsg = new HashMap<>();
		try {
			DirContext context = new InitialDirContext(setLDAPCredential(objCredentials));
			isConnect = true;
			context.close();

		} catch (AuthenticationNotSupportedException ex) {
			errMsg.put("error", "The authentication is not supported by the server");
			logger.error("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
			errMsg.put("error", "Incorrect username or password");
			logger.error("Incorrect username or password");
		} catch (NamingException ex) {
			errMsg.put("error", "Unknown Host...Check Domainname");
			logger.error("Unknown Host...check Domainname");
		} catch (Exception ex) {
			errMsg.put("error", "Error in creating Context");
			logger.error("Error in creating Context");
		}
		errMsg.put("connect", isConnect);

		return isConnect;
	}

	public static Map<String, Object> CheckDomainLDAPConnection4Msg(Map<String, Object> objCredentials) {

		boolean isConnect = false;
		Map<String, Object> map = new HashMap<>();
		try {
			DirContext context = new InitialDirContext(setLDAPCredential(objCredentials));
			isConnect = true;
			context.close();

		} catch (AuthenticationNotSupportedException ex) {
			map.put("Msg", "The authentication is not supported by the server");
			logger.error("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
			map.put("Msg", "Incorrect username or password");
			logger.error("Incorrect username or password");
		} catch (NamingException ex) {
			map.put("Msg", "Unknown Host...Check Domainname");
			logger.error("Unknown Host...check Domainname");
		} catch (Exception ex) {
			map.put("Msg", "Error in creating Context");
			logger.error("Error in creating Context");
		}
		map.put("connect", isConnect);

		return map;
	}
}
