package clases;

public class Retiro {
    private int numRetiro;
    private int numMatricula;
    private String fecha;
    private String hora;
    private int estado; 
    private static int correlativo = 0;

    // Constructor 1: estándar para nuevos retiros
    public Retiro(int numMatricula, String fecha, String hora) {
        correlativo++;
        this.numRetiro    = 200000 + correlativo;
        this.numMatricula = numMatricula;
        this.fecha        = fecha;
        this.hora         = hora;
        this.estado       = 1; // Estado inicial: Activo
    }

    // Constructor 2: para reconstruir desde el archivo de texto
    public Retiro(int numRetiro, int numMatricula, String fecha, String hora, int estado) {
        this.numRetiro    = numRetiro;
        this.numMatricula = numMatricula;
        this.fecha        = fecha;
        this.hora         = hora;
        this.estado       = estado;
        int numero = numRetiro - 200000;
        if (numero > correlativo) { correlativo = numero; }
    }

    public int getNumRetiro()    { return numRetiro; }
    public int getNumMatricula() { return numMatricula; }
    public String getFecha()     { return fecha; }
    public String getHora()      { return hora; }
    public int getEstado()       { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}
