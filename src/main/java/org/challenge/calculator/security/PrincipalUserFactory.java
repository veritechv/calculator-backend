package org.challenge.calculator.security;

import org.challenge.calculator.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PrincipalUserFactory {
    public static PrincipalUser build(User user) {

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                user.getRoles().stream().map(role->{return role.getRoleName().name();}).collect(Collectors.joining(",")));

        return new PrincipalUser(user.getUsername(), user.getPassword(), authorities);
    }
}
