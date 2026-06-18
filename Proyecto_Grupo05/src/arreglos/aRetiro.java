package arreglos;

import java.util.ArrayList;
import clases.Retiro;

public class aRetiro {
    private ArrayList<Retiro> retiros = new ArrayList<>();

    public int longitud() { return retiros.size(); }
    public Retiro obtener(int i) { return retiros.get(i); }
    public void adicionar(Retiro r) { retiros.add(r); }
    public void eliminar(int i) { retiros.remove(i); }
    public void adicionarDesdeArchivo(Retiro r) { retiros.add(r); }

    public Retiro buscar(int numRetiro) {
        for (int i = 0; i < longitud(); i++) {
            if (obtener(i).getNumRetiro() == numRetiro)
                return obtener(i);
        }
        return null;
    }

    public int buscarIndice(int numRetiro) {
        for (int i = 0; i < longitud(); i++) {
            if (obtener(i).getNumRetiro() == numRetiro)
                return i;
        }
        return -1;
    }
    
    public void limpiar() {
        retiros.clear();
    }
}