package com.in.services;

import com.in.dtos.LoginDTO;
import com.in.dtos.UserDTO;

public interface UserDataService {
    UserDTO saveUser(UserDTO userEntityDTO);
    Boolean checkEmailExists(String email);
    LoginDTO getUserLoginDetails(String userId);
    boolean updatePassword(String email, String password);

}
