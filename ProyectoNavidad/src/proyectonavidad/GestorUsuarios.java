package proyectonavidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;


public class GestorUsuarios {

    private static final String URL = "jdbc:mysql://localhost:3306/zonasolidaria";
    private static final String USUARIO = "zonasolidaria";
    private static final String CONTRASENA = "zonasolidaria";

    public static void registrarUsuario(String nombre, String apellidos, String correo, String telefono, String municipio, String tipoUsuario) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Validar datos antes de realizar la inserción
            if (nombre == null || apellidos == null || correo == null || telefono == null || municipio == null || tipoUsuario == null ||
            nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || telefono.isEmpty() || municipio.isEmpty() || tipoUsuario.isEmpty()) {
            System.out.println("Por favor, complete todos los campos antes de registrar el usuario");
            return;
            }
            // Crear la consulta de inserción
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
        } catch (SQLException e) {
            System.err.println("Error al registrar el evento en la base de datos:");
            e.printStackTrace();
        }

    }
        
    public static void listaDonantes(DefaultListModel<String> listModel) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Ahora establezco la conexión
            try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                // Preparo una petición a la base de datos
                Statement peticion = conexion.createStatement();
                // A continuación le pedimos algo a una base de datos 
                ResultSet resultado = peticion.executeQuery("SELECT Nombre, Apellidos FROM usuarios WHERE TipoUsuario = 'Donante';");
                // Mientras el resultado tenga líneas, agrega los nombres y apellidos al modelo de lista
                while (resultado.next()) {
                    
                    System.out.println(resultado.getString("Nombre") + " | " + resultado.getString("Apellidos"));
                    String nombreCompleto = resultado.getString("Nombre") + " " + resultado.getString("Apellidos");
                    listModel.addElement(nombreCompleto);
                }
            }
        }   catch (Exception e) {
                e.printStackTrace();
            }
        }

}


