package arreglos;
import java.util.ArrayList;
import clases.Alumno;
import clases.Matricula;

public class aMatricula {
    private ArrayList<Matricula> matriculas = new ArrayList<>();

    public int longitud() { return matriculas.size(); }
    public Matricula obtener(int i) { return matriculas.get(i); }
    public void adicionar(Matricula m) { matriculas.add(m); }
    public void eliminar(int i) { matriculas.remove(i); }
    public void adicionarDesdeArchivo(Matricula m) { matriculas.add(m); }

    public Matricula buscar(int numMatricula) {
        for (int i = 0; i < longitud(); i++) {
            if (obtener(i).getNumMatricula() == numMatricula)
                return obtener(i);
        }
        return null;
    }

    public boolean alumnoYaMatriculado(Alumno alumno) {
        if (alumno == null) return false;
        return alumno.getEstado() == 1 || alumno.getEstado() == 2;
    }

    public int buscarIndice(int numMatricula) {
        for (int i = 0; i < longitud(); i++) {
            if (obtener(i).getNumMatricula() == numMatricula)
                return i;
        }
        return -1;
    }
    
    public void limpiar() {
        matriculas.clear();
    }
}