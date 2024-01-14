
package proyectonavidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class GestorEventos {

    private static final String URL = "jdbc:mysql://localhost:3306/zonasolidaria";
    private static final String USUARIO = "zonasolidaria";
    private static final String CONTRASENA = "zonasolidaria";

    public static void registrarEvento(String nombre, String descripcion, String fecha, String tipoevento, String organizador, String ubicacion) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                // Preparar la consulta utilizando PreparedStatement
                String query = "INSERT INTO eventos (NombreEvento, Descripcion, Fecha, Tipoevento, OrganizadorID, UbicacionID) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
                    // Establecer los parámetros utilizando setXXX según el tipo de datos en la base de datos
                    preparedStatement.setString(1, nombre);
                    preparedStatement.setString(2, descripcion);
                    preparedStatement.setDate(3, fecha);
                    preparedStatement.setString(4, tipoevento);
                    preparedStatement.setString(5, organizador);
                    preparedStatement.setString(6, ubicacion);

                    // Ejecutar la consulta de inserción
                    int filasAfectadas = preparedStatement.executeUpdate();

                    if (filasAfectadas > 0) {
                        System.out.println("Evento registrado correctamente en la base de datos");
                    } else {
                        System.out.println("No se pudo registrar el evento en la base de datos");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
