package BtRestIa.BTRES.application.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ai.model.function.Tool;

import java.util.Map;

@Component
public class BuscarUsuarioTool  { 

    private final JdbcTemplate jdbcTemplate;

    public BuscarUsuarioTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Functio(name = "buscarUsuarioPorNombre", description = "Busca un usuario por su nombre en la base de datos")



    @Override
    public String getName() {
        return "buscarUsuarioPorNombre";
    }

    @Override
    public String getDescription() {
        return "Busca un usuario por su nombre en la base de datos";
    }

    @Override
    public Object execute(Map<String, Object> params) {
        String nombre = (String) params.get("nombre");
        return buscarUsuario(nombre);
    }

    private String buscarUsuario(String nombre) {


        String query = "SELECT * FROM usuario WHERE usuario = ?";
       
        try {
            return jdbcTemplate.query(
                query,
                (rs, rowNum) -> String.format(
                    "Usuario encontrado: ID: %d, Token: %s, Nombre: %s, Email: %s, Fecha de Creación: %s",
                    rs.getInt("id"),
                    rs.getString("token"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getTimestamp("fecha_creacion").toString()
                ),
                nombre
            ).stream().findFirst().orElse("Usuario no encontrado");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al buscar el usuario: " + e.getMessage();
        }
    }
}