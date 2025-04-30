package BtRestIa.BTRES.application.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

@Component
public class BuscarUsuarioTool {

    private final JdbcTemplate jdbcTemplate;

    public BuscarUsuarioTool(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(name = "buscarUsuarioPorNombre", description = "Busca un usuario por su nombre en la base de datos")
    public String buscarUsuarioPorNombre(
            @ToolParam(description = "Nombre del usuario a buscar", required = true) String nombre) {

        String query = "SELECT * FROM usuario WHERE nombre = ?";

        try {
            return jdbcTemplate.query(
                    query,
                    (rs, rowNum) -> String.format(
                            "{\"ID\": \"%d\", \"Token\": \"%s\", \"Nombre\": \"%s\", \"Email\": \"%s\", \"Fecha de Creación\": \"%s\"}",
                            rs.getInt("id"),
                            rs.getString("token"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getTimestamp("fecha_creacion").toString()),
                    nombre).stream().findFirst().orElse("Usuario no encontrado");
        } catch (Exception e) {

            return "Error al buscar el usuario: " + e.getMessage();
        }
    }
}
