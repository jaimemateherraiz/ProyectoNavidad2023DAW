
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
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void mostrarEventos() {
        
        DefaultTableModel meventos = new DefaultTableModel();
        meventos.addColumn("NOMBRE");
        meventos.addColumn("DESCRIPCIÓN");
        meventos.addColumn("FECHA");
        meventos.addColumn("TIPO");
        meventos.addColumn("ORGANIZADOR");
        meventos.addColumn("UBICACIÓN");
        
        
        
        
        
        
        
        
        /*try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Ahora establezco la conexión
            try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                // Preparo una petición a la base de datos
                Statement peticion = conexion.createStatement();
                // A continuación le pedimos algo a una base de datos 
                ResultSet resultado = peticion.executeQuery("SELECT NombreEvento, Descripcion, Fecha, Tipoevento, Organizador FROM eventos WHERE Ubicacion = Ubicacion;");

                // Mientras el resultado tenga líneas, agrega los nombres al modelo de lista
                while (resultado.next()) {
                    System.out.println(resultado.getString(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    
    
    
    
    
   
}
