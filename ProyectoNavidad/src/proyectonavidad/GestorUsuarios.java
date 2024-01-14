package proyectonavidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GestorUsuarios {

    private static final String URL = "jdbc:mysql://localhost:3306/zonasolidaria";
    private static final String USUARIO = "zonasolidaria";
    private static final String CONTRASENA = "zonasolidaria";

    public static void registrarUsuario(String nombre, String apellidos, String correo, String telefono, String municipio, String tipoUsuario) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                // Preparar la consulta utilizando PreparedStatement
                String query = "INSERT INTO usuarios (Nombre, Apellidos, CorreoElectronico, TelefonoMovil, Municipio, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
                    // Establecer los parámetros utilizando setXXX según el tipo de datos en la base de datos
                    preparedStatement.setString(1, nombre);
                    preparedStatement.setString(2, apellidos);
                    preparedStatement.setString(3, correo);
                    preparedStatement.setString(4, telefono);
                    preparedStatement.setString(5, municipio);
                    preparedStatement.setString(6, tipoUsuario);

                    // Ejecutar la consulta de inserción
                    int filasAfectadas = preparedStatement.executeUpdate();

                    if (filasAfectadas > 0) {
                        System.out.println("Usuario registrado correctamente en la base de datos");
                    } else {
                        System.out.println("No se pudo registrar el usuario en la base de datos");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

