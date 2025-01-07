package com.in.services.serviceimpls;

import com.in.dtos.LoginDTO;
import com.in.dtos.UserDTO;
import com.in.entities.UserEntity;
import com.in.exceptions.ResourceNotFoundException;
import com.in.repositories.UserDataRepository;
import com.in.services.UserDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserDataServiceImpl implements UserDataService {
    private final ModelMapper modelMapper;
    private final UserDataRepository userDataRepository;

    @Override
    public UserDTO saveUser(UserDTO userEntityDTO) {
        UserEntity userEntity = modelMapper.map(userEntityDTO, UserEntity.class);
        UserEntity savedUser = userDataRepository.save(userEntity);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public Boolean checkEmailExists(String email) {
        return userDataRepository.existsByEmail(email);
    }

    @Override
    public LoginDTO getUserLoginDetails(String userId) {
        LoginDTO savedUser = userDataRepository.findNameAndPasswordByUserId(userId);
        if(savedUser == null){
            throw new ResourceNotFoundException("User not found with userId: "+userId);
        }
        return savedUser;
    }

    @Override
    public boolean updatePassword(String email, String password) {
        int rowsUpdated = userDataRepository.updatePasswordByEmail(email, password);
        return rowsUpdated != 0;
    }

}
