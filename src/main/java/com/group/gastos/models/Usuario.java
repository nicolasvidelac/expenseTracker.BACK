package com.group.gastos.models;

import com.group.gastos.others.enums.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.GeneratedValue;
import java.util.Collection;
import java.util.Collections;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue
    private String id;
    private String username;
    private String nickname;
    private String password;
    private Float sueldo = 0F;
    private Boolean locked = false;
    private Boolean enabled = true;

    public Usuario(String username, String nickname, String password, Float sueldo, Boolean locked, Boolean enabled) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.sueldo = sueldo;
        this.locked = locked;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(RolesEnum.USER.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
