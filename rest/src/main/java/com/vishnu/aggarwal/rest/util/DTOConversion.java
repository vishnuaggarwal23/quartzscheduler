package com.vishnu.aggarwal.rest.util;

/*
Created by vishnu on 14/8/18 2:45 PM
*/

import com.vishnu.aggarwal.core.dto.AuthorityDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.User;

import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class DTOConversion {

    public static UserAuthenticationDTO convertFromUserDTO(UserDTO userDTO, Boolean isAuthenticated, String xAuthToken) throws NullPointerException, IllegalArgumentException {
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO();
        userAuthenticationDTO.setIsAuthenticated(isAuthenticated);
        userAuthenticationDTO.setXAuthToken(xAuthToken);
        userAuthenticationDTO.setUser(userDTO);
        return userAuthenticationDTO;
    }

    public static UserDTO convertFromUser(User user) throws NullPointerException, IllegalArgumentException {
        UserDTO userDTO = new UserDTO();
        userDTO.setAccountEnabled(user.getAccountEnabled());
        userDTO.setAccountExpired(user.getAccountExpired());
        userDTO.setAccountLocked(user.getAccountLocked());
        userDTO.setCredentialsExpired(user.getCredentialsExpired());
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setIsDeleted(user.getIsDeleted());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoles(user.getUserAuthorities().stream().filter(userAuthority -> nonNull(userAuthority) && (userAuthority.getStatus() == ACTIVE)).map(userAuthority -> new AuthorityDTO(userAuthority.getAuthority().getId(), userAuthority.getAuthority().getAuthority(), userAuthority.getStatus())).collect(toList()));
        return userDTO;
    }
}
