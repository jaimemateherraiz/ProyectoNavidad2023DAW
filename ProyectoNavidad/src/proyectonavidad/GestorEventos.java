package proyectonavidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            // Obtener el ID del organizador usando su nombre
            String queryOrganizador = "SELECT UsuarioID FROM Usuarios WHERE Nombre = ?";
            try (PreparedStatement preparedStatementOrganizador = conexion.prepareStatement(queryOrganizador)) {
                preparedStatementOrganizador.setString(1, organizador);
                ResultSet resultadoOrganizador = preparedStatementOrganizador.executeQuery();

                if (resultadoOrganizador.next()) {
                    int idOrganizador = resultadoOrganizador.getInt("UsuarioID");

                    // Obtener el ID de la ubicación usando el nombre del municipio
                    String queryUbicacion = "SELECT UbicacionID FROM Ubicacion WHERE Municipio = ?";
                    try (PreparedStatement preparedStatementUbicacion = conexion.prepareStatement(queryUbicacion)) {
                        preparedStatementUbicacion.setString(1, ubicacion);
                        ResultSet resultadoUbicacion = preparedStatementUbicacion.executeQuery();

                        if (resultadoUbicacion.next()) {
                            int idUbicacion = resultadoUbicacion.getInt("UbicacionID");
                            
                            // Crear la consulta de inserción
                            String query = "INSERT INTO Eventos (NombreEvento, Descripcion, Fecha, TipoEvento, OrganizadorID, UbicacionID) VALUES (?, ?, ?, ?, ?, ?)";

                            try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
                                // Establecer los parámetros utilizando setXXX según el tipo de datos en la base de datos
                                preparedStatement.setString(1, nombre);
                                preparedStatement.setString(2, descripcion);
                                preparedStatement.setString(3, fecha);
                                preparedStatement.setString(4, tipoevento);
                                preparedStatement.setInt(5, idOrganizador);
                                preparedStatement.setInt(6, idUbicacion);

                                // Ejecutar la consulta de inserción
                                int filasAfectadas = preparedStatement.executeUpdate();

                                if (filasAfectadas > 0) {
                                    System.out.println("Evento registrado correctamente en la base de datos");
                                } else {
                                    System.out.println("No se pudo registrar el evento en la base de datos");
                                }
                            }
                        } else {
                            System.out.println("No se encontró la ubicación con el nombre proporcionado");
                        }
                    }
                } else {
                    System.out.println("No se encontró el organizador con el nombre proporcionado");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el evento en la base de datos:");
            e.printStackTrace();
        }
    }

    
    public static void mostrarEventos(DefaultTableModel modeloTabla, String municipioFiltro) {
        String[] nombresColumnas = {"ID","Nombre", "Descripcion", "Fecha", "Organizador", "Municipio"};
        String[] registros = new String[6];

        modeloTabla.setRowCount(0);
        modeloTabla.setColumnIdentifiers(nombresColumnas);

        String sql = "SELECT e.EventoID, e.NombreEvento, e.Descripcion, e.Fecha, u.Nombre AS NombreOrganizador, u.Apellidos AS ApellidosOrganizador, ub.Municipio AS MunicipioUbicacion" +
                     " FROM eventos e" +
                     " JOIN usuarios u ON e.OrganizadorID = u.UsuarioID" +
                     " JOIN ubicacion ub ON e.UbicacionID = ub.UbicacionID" +
                     " WHERE ub.Municipio = ?";

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
                    registros[0] = String.valueOf(resultado.getInt("EventoID")); // Convertir el ID a String
                    registros[1] = resultado.getString("NombreEvento");
                    registros[2] = resultado.getString("Descripcion");
                    registros[3] = resultado.getString("Fecha");
                    registros[4] = resultado.getString("NombreOrganizador") + " " + resultado.getString("ApellidosOrganizador");
                    registros[5] = resultado.getString("MunicipioUbicacion");

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

    public void eliminarEvento(int idEvento) {
    
        String query = "DELETE FROM eventos WHERE EventoID = ?";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setInt(1, idEvento);

            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Evento eliminado correctamente de la base de datos");
            } else {
                System.out.println("No se encontró el evento con el ID proporcionado");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar el evento de la base de datos:");
            e.printStackTrace();
        }
    }

}

    

