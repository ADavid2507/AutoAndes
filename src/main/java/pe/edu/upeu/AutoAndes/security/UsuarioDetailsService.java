package pe.edu.upeu.AutoAndes.security;

import pe.edu.upeu.AutoAndes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarios;

    public UserDetails loadUserByUsername(String username) {
        var u = usuarios.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Credenciales inválidas"));

        var permisos = u.getRoles().stream().flatMap(r -> PermisosPorRol.de(r.getNombre()).stream()).distinct()
                .map(p -> new SimpleGrantedAuthority(p.name())).toList();
        return new User(u.getUsername(), u.getPassword(), Boolean.TRUE.equals(u.getActivo()), true, true, true, permisos);
    }
}
