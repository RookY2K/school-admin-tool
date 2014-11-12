package edu.uwm.owyh.model;

public class UserFactory {
//	public static Person getUser(String userName, String password, AccessLevel accessLevel ){
//		return Client.getClient(userName, password, accessLevel);
//	}
	
//	public static Person getUser(Entity userEntity){
//		if(userEntity.getProperty("username") != null){
//			return Client.getClient(userEntity);
//		}
//		
//		return ContactCard.getContactCard(userEntity);
//	}
	
//	public static Person getUser(String name, String phone, String address, String email){
//		return ContactCard.getContactCard(name, phone, address, email);
//	}
	
	public static Person getUser(){
		return ClientWrapper.getClientWrapper();
		
//		return ContactCard.getContactCard();
	}
}
