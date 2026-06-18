package clases;

public class Matricula {
    private int numMatricula;
    private int codAlumno;
    private int codCurso;
    private String fecha;
    private String hora;
    private int estado; 
    private static int correlativo = 0;

    // Constructor 1 para ingresar nuevas matrículas 
    public Matricula(int codAlumno, int codCurso, String fecha, String hora) {
        correlativo++;
        this.numMatricula = 100000 + correlativo;
        this.codAlumno = codAlumno;
        this.codCurso  = codCurso;
        this.fecha     = fecha;
        this.hora      = hora;
        this.estado    = 1; 
    }
    
    // Constructor para reconstruir objetos desde el archivo matricula.txt
    public Matricula(int numMatricula, int codAlumno, int codCurso, String fecha, String hora, int estado) {
        this.numMatricula = numMatricula;
        this.codAlumno    = codAlumno;
        this.codCurso     = codCurso;
        this.fecha        = fecha;
        this.hora         = hora;
        this.estado       = estado;
        int numero = numMatricula - 100000;
        if (numero > correlativo) { correlativo = numero; }
    }

    public int getNumMatricula() { return numMatricula; }
    public int getCodAlumno()    { return codAlumno; }
    public int getCodCurso()     { return codCurso; }
    public void setCodCurso(int codCurso) { this.codCurso = codCurso; }
    public String getFecha()     { return fecha; }
    public String getHora()      { return hora; }
    public int getEstado()       { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}