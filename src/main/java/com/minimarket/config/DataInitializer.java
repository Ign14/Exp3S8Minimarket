package com.minimarket.config;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Carga inicial de los tres roles del caso y un usuario de prueba por rol.
 * Las contrasenas se guardan con hash BCrypt. Credenciales solo para pruebas locales.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol admin = obtenerOCrear("ADMINISTRADOR");
        Rol cajero = obtenerOCrear("CAJERO");
        Rol cliente = obtenerOCrear("CLIENTE");

        crearSiNoExiste("admin", "admin@minimarket.cl", "admin123", Set.of(admin));
        crearSiNoExiste("cajero", "cajero@minimarket.cl", "cajero123", Set.of(cajero));
        crearSiNoExiste("cliente", "cliente@minimarket.cl", "cliente123", Set.of(cliente));
    }

    private Rol obtenerOCrear(String nombre) {
        return rolRepository.findByNombre(nombre).orElseGet(() -> rolRepository.save(new Rol(nombre)));
    }

    private void crearSiNoExiste(String username, String email, String pass, Set<Rol> roles) {
        if (usuarioRepository.findByUsername(username).isPresent()) return;
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(pass));
        u.setActivo(true);
        u.setRoles(roles);
        usuarioRepository.save(u);
    }
}
