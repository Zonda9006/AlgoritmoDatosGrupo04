package clases;

public class Alumno 
{
	private int codigo, edad, celular, estado;
	private String dni, nombres, apellidos, carrera;
	private static int correlativo=0;
	
	public Alumno(String dni, String nombres, String apellidos, String carrera, int celular, int edad, int estado) 
	{
		correlativo++;
		this.codigo= 2020100000 + correlativo;
		this.dni=dni;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.celular=celular;
		this.edad=edad;
		this.carrera=carrera;
		this.estado=estado;
	}
	
	// Constructor que ejecuta la persistencia de datos
	public Alumno(int codigo, String dni, String nombres, String apellidos, String carrera, int celular, int edad, int estado) {
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.carrera = carrera;
        this.celular = celular;
        this.edad = edad;
        this.estado = estado;
        
        int numeroCorrelativoPuro = codigo % 100000; 
        if (numeroCorrelativoPuro > correlativo) {
            correlativo = numeroCorrelativoPuro;
        }
	}
	
	//getter y setter

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public int getCelular() {
		return celular;
	}

	public void setCelular(int celular) {
		this.celular = celular;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	public int getCodigo() {
		return codigo;
	}


	
	
}
