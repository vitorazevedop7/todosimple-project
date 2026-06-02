package com.vitorazevedo.todosimple.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vitorazevedo.todosimple.models.enums.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity(Long id, String username, String password,
            Set<ProfileEnum>  profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = profileEnums.stream().map(x -> new SimpleGrantedAuthority(x.getDescription()))
                                        .collect(Collectors.toList());
    }



    // CODE REVIEW [CODE SMELL]: Todos os métodos de status de conta retornam true hardcoded.
    // Isso impede qualquer mecanismo futuro de bloqueio, desativação ou expiração de contas.
    // Sugestão: adicionar campo 'boolean active' à entidade User e usar aqui:
    //   public boolean isEnabled() { return this.active; }
    //   public boolean isAccountNonLocked() { return this.active; }
    // Isso habilita funcionalidades como suspensão por admin, verificação de e-mail, etc.
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
        return true;
    }
    
    public boolean hasRole(ProfileEnum profileEnum) {
        return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }

}
