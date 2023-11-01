package JavaBBDD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author maria
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    @SuppressWarnings({"CallToPrintStackTrace", "null"})
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here

        // CREACIÓN DE VARIABLES PARA EL PROGRAMA ******************************
        @SuppressWarnings("UnusedAssignment")
        int op = 0;
        String tabla;
        int columnas;
        int id;
        Scanner sc = new Scanner(System.in, "ISO-8859-1");

        // CONEXIÓN CON LA BASE DE DATOS PARA PODER REALIZAR QUERYS ************
        Conectar conectar = new Conectar();

        // INICIO DEL PROGRAMA PARA HACER QUERYS *******************************
        System.out.println("\nBIENVENIDO AL PROGRAMA PARA MODIFICAR LA BASE DE DATOS \"MOUNTAINS\"\n__________________________________________________________________");
        try {
            do {
                // Introducimos el código en un try catch para tener en cuenta todo tipo de errores como, por ejemplo, un fallo en el tipo de dato introducido

                System.out.println("\n¿Qué desea realizar?\n");
                System.out.println("1. Crear una nueva tabla\n2. Añadir un nuevo elemento a la tabla\n3. Consultar los datos de toda la tabla\n4. Consultar los datos de "
                        + "un sólo elemento de la tabla\n5. Editar los datos de la tabla\n6. Borrar un elemento de la tabla\n7. Borrar una tabla\n0. Salir.\n");
                System.out.print("Indique el número de la opción deseada aquí: ");
                op = sc.nextInt();
                switch (op) {

                    // CREACIÓN DE LA TABLA ************************************
                    case 1:
                        System.out.print("\nHa seleccionado crear una nueva tabla.\n\nIndique el nombre de la tabla a crear: ");
                        tabla = sc.next();
                        System.out.print("\n(Recuerde que el campo \"id\" se creará por defecto así que no lo incluya).\nIndique el número de columnas que desea que tenga "
                                + "su tabla: ");
                        columnas = sc.nextInt();
                        String[] campos = new String[columnas];
                        System.out.println("\nIntroduzca por orden los nombres de las diferentes columnas de la tabla.");
                        for (int i = 0; i < columnas; i++) {
                            System.out.print("Introduca el nombre de la comumna " + (i + 1) + ": ");
                            campos[i] = sc.next();
                        }
                        System.out.print("\nLas columnas introducidas más \"id\" son:\nid, ");
                        for (int i = 0; i < columnas; i++) {
                            // Este if es para que no muestre la coma final en el último campo
                            if (campos.length - 1 == i) {
                                System.out.print(campos[i] + "\n");
                            } else {
                                System.out.print(campos[i] + ", ");
                            }
                        }
                        System.out.println("\n(Recuerde que los valores más comunes son \"int\" y \"VARCHAR (numeroDeCaracteres)\".\nIntroduzca el tipo de dato para "
                                + "cada una de las columnas sin contar \"id.\"");
                        String[] tipoDatos = new String[columnas];
                        for (int i = 0; i < columnas; i++) {
                            System.out.print("Introduzca el tipo de dato de la columna " + (i + 1) + ": ");
                            tipoDatos[i] = sc.next();
                        }
                        conectar.crearTabla(tabla, columnas, campos, tipoDatos);
                        break;

                    // AÑADIR UN NUEVO ELEMENTO A LA TABLA *********************
                    case 2:
                        System.out.print("\nHa seleccionado añadir un nuevo elemento a una tabla.\n\nIndique el nombre de la tabla donde desea insertar el elemento: ");
                        tabla = sc.next();
                        System.out.print("\nIndique el número de columnas en las que desea insertar información: ");
                        columnas = sc.nextInt();
                        String[] camposEditar = new String[columnas];
                        System.out.println("\nIntroduzca por orden los nombres de las diferentes columnas en las que desea añadir información.");
                        for (int i = 0; i < columnas; i++) {
                            System.out.print("Introduca el nombre de la comumna " + (i + 1) + ": ");
                            camposEditar[i] = sc.next();
                        }
                        System.out.print("\nLas columnas introducidas son: ");
                        for (int i = 0; i < columnas; i++) {
                            // Este if es para que no muestre la coma final en el último campo
                            if (camposEditar.length - 1 == i) {
                                System.out.print(camposEditar[i] + "\n");
                            } else {
                                System.out.print(camposEditar[i] + ", ");
                            }
                        }
                        System.out.println("\nIntroduzca los valores para cada columna.");
                        String[] valores = new String[columnas];
                        for (int i = 0; i < columnas; i++) {
                            System.out.print("Introduzca el valor de la columna " + (i + 1) + ": ");
                            valores[i] = sc.next();
                        }
                        conectar.anadir(tabla, columnas, camposEditar, valores);
                        break;

                    // REALIZAR UNA CONSULTA GENERAL ***************************
                    case 3:
                        System.out.print("\nHa seleccionado realizar una consulta de una tabla.\n\nIndique el nombre de la tabla de la que desea realizar la consulta: ");
                        tabla = sc.next();
                        System.out.println("\nEl contenido de la tabla es el siguiente: \n");
                        conectar.consultaGeneral(tabla);
                        break;

                    // REALIZAR UNA CONSULTA ESPECÍFICA ************************
                    case 4:
                        System.out.print("\nHa seleccionado realizar una consulta de un elemento específico.\n\nIndique el nombre de la tabla de la que desea realizar la "
                                + "consulta: ");
                        tabla = sc.next();
                        System.out.print("\nIndique el \"id\" del elemento del que desea realizar la consulta: ");
                        id = sc.nextInt();
                        System.out.println("\nEl contenido de la tabla es el siguiente: \n");
                        conectar.consultaPorId(tabla, id);
                        break;

                    // EDITAR DATOS DE UNA TABLA *******************************    
                    case 5:
                        System.out.print("\nHa seleccionado editar los datos de la tabla.\n\nIndique el nombre de la tabla de la que desea hacer la edición: ");
                        tabla = sc.next();
                        // Se pregunta cuantos elementos se quieren editar
                        System.out.print("\n¿Cuántas entradas de la tabla quiere editar? ");
                        int editar = sc.nextInt();
                        // Mostramos los nombres y tipos de las columnas para que sea más fácil para el usuario
                        System.out.println("\nLas columnas en esta tabla son las siguientes: ");
                        ArrayList<String> nombresColumnas = new ArrayList<>();
                        nombresColumnas = conectar.consultarNombresColumnas(tabla);
                        ArrayList<String> tipoColumnas = new ArrayList<>();
                        tipoColumnas = conectar.consultarTipoColumnas(tabla);
                        for (int i = 1; i < nombresColumnas.size(); i++) {
                            System.out.print("- " + nombresColumnas.get(i) + "-> " + tipoColumnas.get(i) + "  ");
                        }
                        System.out.println(""); //Espacio en blanco
                        // Solicitamos id y datos a editar
                        for (int i = 0; i < editar; i++) {
                            System.out.print("\nIndique el número de id del elemento " + (i + 1) + " a editar: ");
                            id = sc.nextInt();
                            System.out.println("\nIntroduzca por orden los nuevos datos del elemento respetando los tipos de dato de la columna.");
                            // Para que el usuario introduzca los datos según el tipo lanzaremos una query para cada edición de dato
                            for (int j = 1; j < nombresColumnas.size(); j++) {
                                System.out.print("\nIntroduzca el dato de la columna " + j + ": ");
                                String nombreColumnaEdicion = nombresColumnas.get(j);
                                if (sc.hasNextInt()) {
                                    int datoNum = sc.nextInt();
                                    conectar.editarNum(tabla, nombreColumnaEdicion, id, datoNum);
                                } else if (sc.hasNext()) {
                                    String datoString = sc.next();
                                    conectar.editarString(tabla, nombreColumnaEdicion, id, datoString);
                                }
                            }
                        }
                        break;
                    // BORRAR UN ELEMENTO DE LA TABLA **************************
                    case 6:
                        System.out.print("\nHa seleccionado eliminar un elemento de una tabla.\n\nIndique el nombre de la tabla en la que desea eliminar el elemento: ");
                        tabla = sc.next();
                        System.out.print("\nIndique el número \"id\" del elemento a eliminar: ");
                        id = sc.nextInt();
                        conectar.eliminarElemento(tabla, id);
                        break;

                    // BORRAR UNA TABLA ****************************************
                    case 7:
                        System.out.print("\nHa seleccionado eliminar una tabla.\n\nIndique el nombre de la tabla a eliminar: ");
                        tabla = sc.next();
                        conectar.eliminarTabla(tabla);
                        break;

                    default:
                        System.out.println("\nOpción no válida.");
                }
            } while (op != 0);
        } catch (InputMismatchException ex) {
            System.err.println("Debe introducir un número entero");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conectar != null) {
                conectar.desconectar();
            }
        }
    }

}
