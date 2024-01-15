
package proyectonavidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;


public class GestorEventos {

    private static final String URL = "jdbc:mysql://localhost:3306/zonasolidaria";
    private static final String USUARIO = "zonasolidaria";
    private static final String CONTRASENA = "zonasolidaria";

    public static void registrarEvento(String nombre, String descripcion, String fecha, String tipoevento, String organizador, String ubicacion) {
    
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Validar datos antes de realizar la inserción
            if (nombre == null || descripcion == null || fecha == null || tipoevento == null || organizador == null || ubicacion == null) {
                System.out.println("Por favor, complete todos los campos antes de registrar el evento");
                return;
            }

            // Crear la consulta de inserción
            String query = "INSERT INTO Eventos (NombreEvento, Descripcion, Fecha, TipoEvento, OrganizadorID, UbicacionID) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
                // Establecer los parámetros utilizando setXXX según el tipo de datos en la base de datos
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setString(3, fecha);
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
        } catch (SQLException e) {
            System.err.println("Error al registrar el evento en la base de datos:");
            e.printStackTrace();
        }

    }
    
    public static void mostrarEventos(DefaultTableModel modeloTabla, String municipioFiltro) {
        String[] nombresColumnas = {"Nombre", "Descripcion", "Fecha", "Organizador"};
        String[] registros = new String[4];

        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(nombresColumnas);

        String sql = "SELECT NombreEvento, Descripcion, Fecha, OrganizadorID FROM eventos WHERE UbicacionID = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            // Usar PreparedStatement para permitir la inserción segura de parámetros
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                // Establecer el valor para el marcador de posición utilizando el texto del JTextField
                preparedStatement.setString(1, municipioFiltro);

                // Ejecutar la consulta
                ResultSet resultado = preparedStatement.executeQuery();

                // Procesar el resultado
                while (resultado.next()) {
                    // ... procesar el resultado
                    registros[0] = resultado.getString("NombreEvento");
                    registros[1] = resultado.getString("Descripcion");
                    registros[2] = resultado.getString("Fecha");
                    registros[3] = resultado.getString("OrganizadorID");

                    modeloTabla.addRow(registros);
                }

                // Cerrar recursos
                resultado.close();
            }

            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

    

