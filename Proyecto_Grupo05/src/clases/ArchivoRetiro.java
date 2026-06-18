package clases;
import clases.Retiro;
import arreglos.aRetiro;
import java.io.*;
public class ArchivoRetiro {
    private static final String RUTA = "retiros.txt";
    // Guarda todos los retiros en el archivo txt
    // Formato por línea: numRetiro|numMatricula|fecha|hora|estado
    public static void guardar(aRetiro lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (int i = 0; i < lista.longitud(); i++) {
                Retiro r = lista.obtener(i);
                pw.println(
                    r.getNumRetiro()    + "|" +
                    r.getNumMatricula() + "|" +
                    r.getFecha()        + "|" +
                    r.getHora()         + "|" +
                    r.getEstado()
                );
            }
        } catch (IOException e) {
            System.out.println("Error al guardar retiros: " + e.getMessage());
        }
    }
    // Carga los retiros desde el archivo txt al iniciar el programa
    public static void cargar(aRetiro lista) {
        File archivo = new File(RUTA);
        if (!archivo.exists()) return; // Primera ejecución, no hay archivo aún
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    int numRetiro    = Integer.parseInt(partes[0]);
                    int numMatricula = Integer.parseInt(partes[1]);
                    String fecha     = partes[2];
                    String hora      = partes[3];
                    int estado       = Integer.parseInt(partes[4]);

                    lista.adicionarDesdeArchivo(
                        new Retiro(numRetiro, numMatricula, fecha, hora, estado)
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar retiros: " + e.getMessage());
        }
    }
}