package com.doemedicamentos.services;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Usuario;
import com.doemedicamentos.repositories.UsuarioRepository;
import com.doemedicamentos.security.DetalhesUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Usuario salvarUsuario(Usuario usuario){
        Usuario userOBJ = usuarioRepository.findByEmail(usuario.getEmail());

        if(userOBJ != null){
            throw new RuntimeException("Esse email já existe");
        }

        String senha = usuario.getSenha();
        String encode = bCryptPasswordEncoder.encode(senha);
        usuario.setSenha(encode);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorid(Integer id){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        return usuarioOptional;
    }
    public Iterable<Usuario> buscarUsuarios(){
        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null){
            throw new UsernameNotFoundException(email);
        }
        DetalhesUsuario usuarioDetails = new DetalhesUsuario(usuario.getId(), usuario.getEmail(), usuario.getSenha());
        return usuarioDetails;
    }
}
