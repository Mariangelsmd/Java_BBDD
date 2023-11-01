package JavaBBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maria
 */
public class Conectar {

    // Propiedades de la conexión para no hardcodear
    private static Connection conn;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/mountains";

    //CONSTRUCTOR DE CONEXIÓN VACÍO ********************************************
    public Conectar() {
        // Establecemos la conexión en null
        conn = null;
        try {
            // Intentamos hacer conexión con la base de datos almacenandolo en conn
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url, user, password);

            // Si hemos tenido éxito se imprime mensaje
            if (conn != null) {
                System.out.println("Conectado a la base de datos.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            // En el caso de error se imprime mensaje
            System.out.println("Error al conectar.\nVuelva a intentarlo.\n" + e);
        }
    }

    // MÉTODO DE DESCONEXIÓN ***************************************************
    public void desconectar() {
        conn = null;
    }

    // MÉTODO PARA ELIMINAR UNA TABLA ******************************************
    public void eliminarTabla(String tabla) {
        try (Statement st = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS " + tabla;
            st.executeUpdate(sql);
            System.out.println("\nEliminada la tabla " + tabla);
        } catch (SQLException ex) {
            Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // MÉTODO PARA ELIMINAR UN ELEMENTO DE UNA TABLA ***************************
    public void eliminarElemento(String tabla, int id) {
        try (Statement st = conn.createStatement()) {
            String sql = "DELETE FROM " + tabla + " WHERE id = " + id + ";";
            st.executeUpdate(sql);
            System.out.println("\nEliminado el elemento con \"id\" " + id);
        } catch (SQLException ex) {
            Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // MÉTODO PARA CREAR UNA TABLA *********************************************
    public void crearTabla(String tabla, int columnas, String[] campos, String[] tipoDeDatos) throws SQLException {
        Statement st = null;
        // String de los valores con id añadido por defecto
        String valores = "id INT AUTO_INCREMENT PRIMARY KEY, ";
        // Creamos la tabla
        try {
            st = conn.createStatement();
            // Creamos un bucle para poder insertar los datos introducidos por el usuario y no hardcodear
            for (int i = 0; i < columnas; i++) {
                if (campos.length - 1 == i) {
                    valores += campos[i] + " " + tipoDeDatos[i];
                } else {
                    valores += campos[i] + " " + tipoDeDatos[i] + ", ";
                }
            }
            // Completamos la query con le nombre de la tabla y los valores
            String sql = "CREATE TABLE  " + tabla + " (" + valores + ");";
            //Ejecutamos la query
            st.executeUpdate(sql);
            System.out.println("\nCreada la tabla " + tabla);
        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos.");
        } finally {
            // Finalmente cerramos la conexión si esta está abierta
            if (st != null) {
                st.close();
            }
        }
    }

    //  MÉTODO PARA CONSULTAR COLUMNAS DE UNA TABLA ****************************
    public ArrayList<String> consultarNombresColumnas(String tabla) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> nombresColumnas = new ArrayList<>();
        try {
            st = conn.createStatement();
            // Query para obtener el nombre de las columnas
            rs = st.executeQuery("SHOW COLUMNS FROM " + tabla);

            // Almacenamos los nombres de las columnas en un ArrayList para hacer la consulta después
            while (rs.next()) {
                nombresColumnas.add(rs.getString("Field"));
            }
            rs.close();

        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos. Puede que la tabla no exista.");

        } finally {
            if (st != null) {
                st.close();
            }

        }
        return nombresColumnas;
    }

    //  MÉTODO PARA CONSULTAR EL TIPO DE LAS COLUMNAS DE UNA TABLA *************
    public ArrayList<String> consultarTipoColumnas(String tabla) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> nombresColumnas = new ArrayList<>();
        try {
            st = conn.createStatement();
            // Query para obtener el nombre de las columnas
            rs = st.executeQuery("SHOW COLUMNS FROM " + tabla);

            // Almacenamos los nombres de las columnas en un ArrayList para hacer la consulta después
            while (rs.next()) {
                nombresColumnas.add(rs.getString("Type"));
            }
            rs.close();

        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos. Puede que la tabla no exista.");

        } finally {
            if (st != null) {
                st.close();
            }

        }
        return nombresColumnas;
    }

    // MÉTODO PARA CONSULTAR UNA TABLA *****************************************
    public void consultaGeneral(String tabla) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            // Query para obtener el nombre de las columnas
            ArrayList<String> nombresColumnas = new ArrayList<>();
            nombresColumnas = consultarNombresColumnas(tabla);
            // Realizamos la consulta
            String sql = "SELECT * FROM " + tabla + ";";
            rs = st.executeQuery(sql);
            int contador = 0;
            while (rs.next()) {
                // Accedemos a las columnas por su nombre
                for (int i = 0; i < nombresColumnas.size(); i++) {
                    String nomColumna = nombresColumnas.get(i);
                    String contenido = rs.getString(nomColumna);
                    System.out.print("- " + nomColumna + ": " + contenido + "  ");
                }
                System.out.println(""); // Salto de línea
                contador++;
            }
            if (contador == 0) {
                // En el caso de que no haya contenido imprimiremos lo siguiente
                System.out.println("*** LA TABLA ESTÁ VACÍA ***\n");
            } else {
                // Cuando se termina la lectura se indica
                System.out.println("\n*** FIN DEL CONTENIDO ***");
            }
            rs.close();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // MÉTODO PARA CONSULTAR UN ELEMENTO ***************************************
    public void consultaPorId(String tabla, int id) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            // Query para obtener el nombre de las columnas
            ArrayList<String> nombresColumnas = new ArrayList<>();
            nombresColumnas = consultarNombresColumnas(tabla);
            // Realizamos la consulta
            String sql = "SELECT * FROM " + tabla + " WHERE id = " + id + ";";
            rs = st.executeQuery(sql);
            int contador = 0;
            while (rs.next()) {
                // Accedemos a las columnas por su nombre
                for (int i = 0; i < nombresColumnas.size(); i++) {
                    String nomColumna = nombresColumnas.get(i);
                    String contenido = rs.getString(nomColumna);
                    System.out.print("- " + nomColumna + ": " + contenido + "  ");
                }
                System.out.println(""); // Salto de línea
                contador++;
            }
            if (contador == 0) {
                // En el caso de que no haya contenido imprimiremos lo siguiente
                System.out.println("*** NO HAY NINGÚN ELEMENTO CON ESE ID ***\n");
            } else {
                // Cuando se termina la lectura se indica
                System.out.println("\n*** FIN DEL CONTENIDO ***");
            }
            rs.close();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // MÉTODO PARA EDITAR UNA TABLA CON INT ************************************
    public void editarNum(String tabla, String columna, int id, int dato) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            // Realizamos la consulta
            String sql = "UPDATE " + tabla + " SET " + columna + " = '" + dato + "' WHERE id = " + id;
            // Ejecutamos la consulta de actualización
            int filasAfectadas = st.executeUpdate(sql);
            if (filasAfectadas > 0) {
                System.out.println(columna + " actualizado.");
            } else {
                System.out.println("No se encontró ninguna fila para actualizar.");
            }
        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos.");
        } finally {
            // Finalmente cerramos la conexión si esta está abierta
            if (st != null) {
                st.close();
            }
        }
    }

    // MÉTODO PARA EDITAR UNA TABLA CON STRING *********************************
    public void editarString(String tabla, String columna, int id, String dato) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            // Realizamos la consulta
            String sql = "UPDATE " + tabla + " SET " + columna + " = '" + dato + "' WHERE id = " + id;
            // Ejecutamos la consulta de actualización
            int filasAfectadas = st.executeUpdate(sql);
            if (filasAfectadas > 0) {
                System.out.println(columna + " actualizado.");
            } else {
                System.out.println("No se encontró ninguna fila para actualizar.");
            }
        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos.");
        } finally {
            // Finalmente cerramos la conexión si esta está abierta
            if (st != null) {
                st.close();
            }
        }
    }

    // MÉTODO PARA INSERTAR EN UNA TABLA ***************************************
    public void anadir(String tabla, int columnas, String[] campos, String[] valores) throws SQLException {
        // Definimos variables para leer campos y valores
        String camposLeidos = "";
        String valoresLeidos = "";
        Statement st = null;
        ResultSet rs = null;
        // Comenzamos la consulta
        try {
            st = conn.createStatement();
            // Bucle para leer los arrays de campos y valores
            for (int i = 0; i < columnas; i++) {
                // If para que en el último elemento no incluya la coma
                if (campos.length - 1 == i) {
                    camposLeidos += campos[i];
                    valoresLeidos += "\"" + valores[i] + "\"";
                } else {
                    camposLeidos += campos[i] + ", ";
                    valoresLeidos += "\"" + valores[i] + "\", ";
                }
            }
            // Completamos la query
            String sql = "INSERT INTO " + tabla + " (" + camposLeidos + ") VALUES (" + valoresLeidos + ");";
            // Ejecutamos update y solicitamos keys generadas
            int num = st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("\nNúmero de elementos añadidos: " + num);
            rs = st.getGeneratedKeys();
            rs.next();
            // Solicitamos id obtenido
            int id = rs.getInt(1);
            System.out.println("\nAñadido elemento con \"id\": " + id);
            rs.close();
        } catch (SQLSyntaxErrorException exception) {
            System.out.println(""); //Hacer un espacio en blanco
            // Excepción para capturar errores de síntais
            System.err.println("Error de síntasis en los datos introducidos.");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
}
