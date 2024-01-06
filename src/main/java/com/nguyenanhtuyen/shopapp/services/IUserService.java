package com.nguyenanhtuyen.shopapp.services;

import com.nguyenanhtuyen.shopapp.dtos.UserDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.User;

public interface IUserService {

	User createUser(UserDTO userDTO) throws DataNotFoundException;
	
	String login(String phoneNumber, String password) throws Exception;
	
}
