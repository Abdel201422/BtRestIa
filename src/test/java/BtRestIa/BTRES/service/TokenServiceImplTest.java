package BtRestIa.BTRES.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import BtRestIa.BTRES.application.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    UsuarioRepository usuarioRepo;
    @InjectMocks
    TokenServiceImpl service;

    @Test
    void validateUsuarioToken_tokenExistente_devuelveUsuario() {
        Usuario u = new Usuario();
        u.setToken("abc");
        when(usuarioRepo.findByToken("abc")).thenReturn(Optional.of(u));

        Usuario resultado = service.validateUsuarioToken("abc");
        assertSame(u, resultado);
    }

    @Test
    void validateUsuarioToken_tokenInexistente_lanzaNotFound() {
        when(usuarioRepo.findByToken("nope")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.validateUsuarioToken("nope"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Usuario no encontrado"));
    }

    @Test
    void generateTokenForUsuario_asignaYGuardaNuevoToken() {
        Usuario u = new Usuario();
        u.setToken("old");
        when(usuarioRepo.save(u)).thenReturn(u);

        String newToken = service.generateTokenForUsuario(u);
        assertNotEquals("old", newToken);
        assertEquals(newToken, u.getToken());
        verify(usuarioRepo).save(u);
    }
}
