package clases;

import clases.Matricula;
import arreglos.aMatricula;

import java.io.*;

public class ArchivoMatricula {

    private static final String RUTA = "matriculas.txt";

    // Guarda todas las matrículas en el archivo txt
    public static void guardar(aMatricula lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (int i = 0; i < lista.longitud(); i++) {
                Matricula m = lista.obtener(i);
                pw.println(
                    m.getNumMatricula() + "|" +
                    m.getCodAlumno()    + "|" +
                    m.getCodCurso()     + "|" +
                    m.getFecha()        + "|" +
                    m.getHora()         + "|" +
                    m.getEstado() 
                );
            }
        } catch (IOException e) {
            System.out.println("Error al guardar matrículas: " + e.getMessage());
        }
    }

    // Carga las matrículas desde el archivo txt al iniciar el programa
    public static void cargar(aMatricula lista) {
        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                
                if (partes.length == 6) {
                    int numMatricula = Integer.parseInt(partes[0]);
                    int codAlumno    = Integer.parseInt(partes[1]);
                    int codCurso     = Integer.parseInt(partes[2]);
                    String fecha     = partes[3];
                    String hora      = partes[4];
                    int estado       = Integer.parseInt(partes[5]); 
                    
                    // Se envía el sexto parámetro al constructor corregido
                    lista.adicionarDesdeArchivo(
                        new Matricula(numMatricula, codAlumno, codCurso, fecha, hora, estado)
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar matrículas: " + e.getMessage());
        }
    }
}