package com.autobots.automanager.controles;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.modelos.LoginRequest;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/auth")
public class AuthControle {

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorioCredencialUsuarioSenha;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private ProvedorJwt provedorJwt;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> loginPorUsuario(@RequestBody LoginRequest request) {
        if (request == null || request.getNomeUsuario() == null || request.getSenha() == null ||
            request.getNomeUsuario().trim().isEmpty() || request.getSenha().trim().isEmpty()) {
            return new ResponseEntity<>("Dados inválidos", HttpStatus.BAD_REQUEST);
        }

        String nomeUsuarioEntrada = request.getNomeUsuario().trim();
        CredencialUsuarioSenha credencialEncontrada = null;
        Usuario usuarioEncontrado = null;

        for (Usuario usuario : repositorioUsuario.findAll()) {
            CredencialUsuarioSenha cred = usuario.obterCredencialUsuarioSenha();
            if (cred != null && cred.getNomeUsuario() != null && cred.getNomeUsuario().trim().equals(nomeUsuarioEntrada)) {
                credencialEncontrada = cred;
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null || credencialEncontrada == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.UNAUTHORIZED);
        }

        boolean senhaValida;
        String senhaArmazenada = credencialEncontrada.getSenha();
        if (senhaArmazenada != null && senhaArmazenada.startsWith("$2")) {
            senhaValida = encoder.matches(request.getSenha(), senhaArmazenada);
        } else {
            senhaValida = senhaArmazenada.equals(request.getSenha());
        }
        if (!senhaValida) {
            return new ResponseEntity<>("Senha incorreta", HttpStatus.UNAUTHORIZED);
        }

        String subject = credencialEncontrada.getNomeUsuario();
        String token = provedorJwt.proverJwt(subject);
        System.out.println("Token gerado para usuario: " + subject);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("token", token);
        resposta.put("type", "Bearer");
        resposta.put("expiresIn", provedorJwt.getDuracao());
        resposta.put("issuedAt", new Date());
        resposta.put("usuarioId", usuarioEncontrado.getId());
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}
