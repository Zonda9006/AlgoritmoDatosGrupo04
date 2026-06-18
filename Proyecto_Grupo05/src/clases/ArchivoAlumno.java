package clases;

import arreglos.aAlumnos;
import java.io.*;

public class ArchivoAlumno {

    private static final String RUTA = "alumnos.txt";

    // Guarda todos los alumnos en el archivo txt
    // Formato por línea: codigo|dni|nombres|apellidos|carrera|celular|edad|estado
    public static void guardar(aAlumnos lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (int i = 0; i < lista.longitud(); i++) {
                Alumno a = lista.obtener(i);
                pw.println(
                    a.getCodigo()    + "|" +
                    a.getDni()       + "|" +
                    a.getNombres()   + "|" +
                    a.getApellidos() + "|" +
                    a.getCarrera()   + "|" +
                    a.getCelular()   + "|" +
                    a.getEdad()      + "|" +
                    a.getEstado()
                );
            }
        } catch (IOException e) {
            System.out.println("Error al guardar alumnos: " + e.getMessage());
        }
    }

    // Carga los alumnos desde el archivo txt al iniciar el programa
    // Formato esperado: 8 partes separadas por |
    public static void cargar(aAlumnos lista) {
        File archivo = new File(RUTA);
        if (!archivo.exists()) return; // Primera ejecución, no hay archivo aún
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 8) {
                    int    codigo    = Integer.parseInt(partes[0]);
                    String dni       = partes[1];
                    String nombres   = partes[2];
                    String apellidos = partes[3];
                    String carrera   = partes[4];
                    int    celular   = Integer.parseInt(partes[5]);
                    int    edad      = Integer.parseInt(partes[6]);
                    int    estado    = Integer.parseInt(partes[7]);
                    lista.adicionarDesdeArchivo(
                        new Alumno(codigo, dni, nombres, apellidos, carrera, celular, edad, estado)
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar alumnos: " + e.getMessage());
        }
    }
}
