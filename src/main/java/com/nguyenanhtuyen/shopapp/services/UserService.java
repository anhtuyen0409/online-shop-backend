package com.nguyenanhtuyen.shopapp.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.dtos.UserDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.Role;
import com.nguyenanhtuyen.shopapp.models.User;
import com.nguyenanhtuyen.shopapp.repositories.RoleRepository;
import com.nguyenanhtuyen.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;

	@Override
	public User createUser(UserDTO userDTO) throws DataNotFoundException {
		
		String phoneNumber = userDTO.getPhoneNumber();
		
		//kiểm tra sdt đã tồn tại chưa?
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number already exists");
		}
		
		//convert userDTO => user
		User newUser = User.builder()
				.fullName(userDTO.getFullName())
				.phoneNumber(userDTO.getPhoneNumber())
				.password(userDTO.getPassword())
				.address(userDTO.getAddress())
				.dateOfBirth(userDTO.getDateOfBirth())
				.facebookAccountId(userDTO.getFacebookAccountId())
				.googleAccountId(userDTO.getGoogleAccountId())
				.build();
		Role role = roleRepository.findById(userDTO.getRoleId())
				.orElseThrow(() -> new DataNotFoundException("Role not found"));
		newUser.setRole(role);
		
		//kiểm tra nếu không có accountId (facebook, google) thì yêu cầu mật khẩu
		if(userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAccountId() == 0) {
			String password = userDTO.getPassword();
		}
		
		return userRepository.save(newUser);
	}

	@Override
	public String login(String phoneNumber, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
