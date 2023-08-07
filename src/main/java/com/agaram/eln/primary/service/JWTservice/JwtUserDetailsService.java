package com.agaram.eln.primary.service.JWTservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.jwt.UserDTO;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private LSuserMasterRepository userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
    private LSSiteMasterRepository lSSiteMasterRepository;
	@Autowired
	private LSMultisitesRepositery LSMultisitesRepositery;

//	@Override
//	public UserDetails loadUserByUsernameaandpassword(String username,String password) throws UsernameNotFoundException{
//		String passworden = (AESEncryption.encrypt(password));
////		LSuserMaster user = userDao.findByusername(username,passworden);
//		LSuserMaster user = userDao.findByUsernameAndPassword(username,passworden);
////		String Password = AESEncryption.decrypt(user.getPassword());
////		user.setPassword(AESEncryption.encrypt(password));
//		if (user == null) {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//				new ArrayList<>());
//	}
//	
	public LSuserMaster save(UserDTO user) {
		LSuserMaster newUser = new LSuserMaster();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userDao.save(newUser);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		int sitecodestartindex = username.lastIndexOf("[");
		String usernamevalue = username.substring(0,sitecodestartindex);
		String sitecodevalue = username.substring(sitecodestartindex+1, username.length()-1);
		
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(sitecodevalue));
		
		LSuserMaster user = new LSuserMaster();
		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objsite);
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> userobj = userDao.findByUsernameIgnoreCaseAndUsercodeInAndUserretirestatusNot(usernamevalue, usercode,1);
		if (!userobj.isEmpty()) {
		    userobj.forEach(items -> {
		                items.setLssitemaster(objsite);
		    });
		    user = userobj.get(0);
		}
//kumaresan multisite purpose 
//			user = userDao.findByUsernameIgnoreCaseAndLssitemaster(usernamevalue, objsite);

		
		try {
			if (user == null) {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(user!=null) {
		String Tokenuser = user.getUsername() +"["+user.getLssitemaster().getSitecode()+"]";
		if(user.getPassword() == null) {
			user.setPassword("admin");
		}
		return new org.springframework.security.core.userdetails.User(Tokenuser, user.getPassword(),
				new ArrayList<>());
	}

	return null;
	}

	
}
