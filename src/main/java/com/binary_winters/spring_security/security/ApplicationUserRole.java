package com.binary_winters.spring_security.security;

import static com.binary_winters.spring_security.security.ApplicationUserPermission.COURSE_READ;
import static com.binary_winters.spring_security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.binary_winters.spring_security.security.ApplicationUserPermission.STUDENT_READ;
import static com.binary_winters.spring_security.security.ApplicationUserPermission.STUDENT_WRITE;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    // I'm adding a role to the list of permissions.
    // Due to UserDetails has this property: Collection<? extends GrantedAuthority> getAuthorities();
    // this method will be useful to set the list returned for it to the User.
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
