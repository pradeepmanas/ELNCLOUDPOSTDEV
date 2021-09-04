package com.agaram.eln.primary.commonfunction;


import com.agaram.eln.config.AESEncryption;

public class commonfunction {

	public static boolean checkuseronmanualaudit(String myKey,String auditPassword) {
		
		String encryptedpassword = AESEncryption.decrypt(myKey);
		
		String getPasscode = AESEncryption.decrypt(encryptedpassword.split("_", 0)[0]);
		
		if(auditPassword.equals(getPasscode)) {
			return true;
		}
		
		return false;
	}
}