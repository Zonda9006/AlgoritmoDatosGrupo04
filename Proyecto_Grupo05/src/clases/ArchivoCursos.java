package clases;

import arreglos.aCurso;
import java.io.*;

public class ArchivoCursos {

    private static final String RUTA = "cursos.txt";

    // Formato por línea: codCurso|asignatura|ciclo|creditos|horas
    public static void guardar(aCurso lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (int i = 0; i < lista.longitud(); i++) {
                Curso c = lista.obtener(i);
                pw.println(
                    c.getCodCurso()   + "|" +
                    c.getAsignatura() + "|" +
                    c.getCiclo()      + "|" +
                    c.getCreditos()   + "|" +
                    c.getHoras()
                );
            }
        } catch (IOException e) {
            System.out.println("Error al guardar cursos: " + e.getMessage());
        }
    }

    public static void cargar(aCurso lista) {
        File archivo = new File(RUTA);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    int    codCurso   = Integer.parseInt(partes[0]);
                    String asignatura = partes[1];
                    int    ciclo      = Integer.parseInt(partes[2]);
                    int    creditos   = Integer.parseInt(partes[3]);
                    int    horas      = Integer.parseInt(partes[4]);
                    lista.adicionarDesdeArchivo(
                        new Curso(codCurso, asignatura, ciclo, creditos, horas)
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar cursos: " + e.getMessage());
        }
    }
}
