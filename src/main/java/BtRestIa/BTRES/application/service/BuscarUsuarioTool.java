package BtRestIa.BTRES.application.service;

import java.sql.*;

import org.springframework.stereotype.Component;

import dev.langchain4j.agent.tool.Tool;

@Component
public class BuscarUsuarioTool  {
    private final Connection connection;

    public BuscarUsuarioTool(Connection connection) {
        this.connection = connection;
    }

    @Tool 
    public String buscarUsuarioPorNombre(String nombre) {
        String query = "SELECT * FROM usuario WHERE usuario = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

               int id = resultSet.getInt("id");
                String token = resultSet.getString("token");
                String nombreUsuario = resultSet.getString("nombre");
                String email = resultSet.getString("email");
                String fechaCreacion = resultSet.getTimestamp("fecha_creacion").toString();   

               return String.format("Usuario encontrado: ID: %d, Token: %s, Nombre: %s, Email: %s, Fecha de Creación: %s", id, token, nombreUsuario, email, fechaCreacion);
            } else {
                return "Usuario no encontrado";
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al buscar el usuario: " + e.getMessage();
        }

    }
    
}
