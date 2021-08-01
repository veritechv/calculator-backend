package org.challenge.calculator.webmodel;

import org.challenge.calculator.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserFactory {

    static public AppUser buildFromUser(User user){
        AppUser appUser = null;

        if(user!=null){
            List<String> roles = user.getRoles().stream().map(role -> role.getRoleName().name()).
                    collect(Collectors.toList());
            appUser = new AppUser(user.getId(), user.getUsername(), roles, user.getBalance(),
                    user.getStatus().name());
        }
        return appUser;
    }
}
