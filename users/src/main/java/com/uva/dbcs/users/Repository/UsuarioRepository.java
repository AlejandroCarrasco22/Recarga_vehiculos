package com.uva.dbcs.users.Repository;

import com.uva.dbcs.users.Model.Usuario;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    ArrayList<Usuario> findByEnabled(boolean activo);

    ArrayList<Usuario> findByEmail(String email);

    ArrayList<Usuario> findByPassword(String password);
}
