package org.challenge.calculator.model;

import org.challenge.calculator.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
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

    static public Page<AppUser> buildFromPageUser(Page<User> pageToTransform){
        Page<AppUser> transformedPage = null;
        if(pageToTransform!=null){
            transformedPage = pageToTransform.map(AppUserFactory::buildFromUser);
        }
        return transformedPage;
    }
}
