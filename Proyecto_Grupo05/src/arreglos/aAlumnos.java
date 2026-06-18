package arreglos;
import java.util.ArrayList;
import clases.Alumno;
import clases.Curso;

public class aAlumnos 
{
	//definición del ArrayList tipo Alumno
	private ArrayList<Alumno> alumnos=new ArrayList<Alumno>();
	
	public aAlumnos() 
	{

	}
	
	public void adicionarDesdeArchivo(Alumno a) {
	    alumnos.add(a);
	}
	
	public int longitud() 
	{
		return alumnos.size();
	}
	
	public Alumno obtener(int i) 
	{
		return alumnos.get(i);
	}
	
	public String adicionar(Alumno a) 
	{
		if(verificarDni(a.getDni())) 
		{
			return "Error: El DNI ya se encuentra registrado";
		}
		
		alumnos.add(a);
		return "Alumno registrado con código: "+ a.getCodigo();
	}
	
	public String eliminar(int i) 
	{
		if(i>=0 && i<longitud()) 
		{
			alumnos.remove(i);
			return "Alumno eliminado";
		}
		
		return "Alumno no encontrado";
	}
	
	public boolean verificarDni(String dni) 
	{
		for (int i=0; i<longitud(); i++)
		{
			if(obtener(i).getDni().equals(dni)) 
			{
				return true;
			}
				
		}
		return false;
	}
	
	public Alumno buscar(int codigo) 
	{
	    for (int i=0; i<longitud(); i++) 
	    {	Alumno alu=obtener(i);
	        if (alu.getCodigo()==codigo) 
	        {
	            return alu; 
	        }
	    }
	    return null; 
	}
	
}

