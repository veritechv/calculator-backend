package org.challenge.calculator.model;

import org.apache.commons.collections.CollectionUtils;
import org.challenge.calculator.entity.Role;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.enums.RoleName;
import org.challenge.calculator.enums.UserStatus;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppUserFactory {

    static public AppUser buildFromUser(User user){
        AppUser appUser = null;

        if(user!=null){
            List<String> roles = user.getRoles().stream().map(role -> role.getRoleName().name()).
                    collect(Collectors.toList());
            appUser = new AppUser(user.getUsername(), roles, user.getBalance(),
                    user.getStatus().name());
        }
        return appUser;
    }

    /**
     * Creates a new User object based on appUser's information.
     * The password is not included.
     * @param appUser Object holding the information to create a User object.
     * @return a new User object
     */
    static public User buildFromAppUser(AppUser appUser){
        User user = null;

        if(appUser!=null){
            Set<Role> userRoles = new HashSet<>();

            if(CollectionUtils.isNotEmpty(appUser.getRoles())){
                userRoles = appUser.getRoles().stream().map(roleName -> {
                    return new Role(RoleName.valueOf(roleName), null);
                }).collect(Collectors.toSet());
            }

            user = new User(appUser.getUsername(), null, userRoles,
                    UserStatus.valueOf(appUser.getStatus()), appUser.getBalance());
        }
        return user;
    }

    static public Page<AppUser> buildFromPageUser(Page<User> pageToTransform){
        Page<AppUser> transformedPage = null;
        if(pageToTransform!=null){
            transformedPage = pageToTransform.map(AppUserFactory::buildFromUser);
        }
        return transformedPage;
    }
}
