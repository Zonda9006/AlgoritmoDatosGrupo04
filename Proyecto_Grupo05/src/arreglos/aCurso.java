package arreglos;
import java.util.ArrayList;

import clases.Alumno;
import clases.Curso;

public class aCurso 
{
	//definición del ArrayList tipo Curso
		private ArrayList<Curso> cursos=new ArrayList<Curso>();
		
		public aCurso()
		{
			
		}
		
		public void adicionarDesdeArchivo(Curso a) {
		    cursos.add(a);
		}
		
		public void adicionar(Curso c) 
		{
			cursos.add(c);
			ordenarPorCodigo();
		}
		
		public int longitud() 
		{
			return cursos.size();
		}
		
		public Curso obtener(int i)
		{
			return cursos.get(i);
		}
		
		public void eliminar(int i) 
		{
			cursos.remove(i);
		}
		
		private void ordenarPorCodigo() 
		{
			for(int i=0; i<longitud()-1;i++) 
			{
				for(int j=0; j<longitud()-i-1;j++) 
				{
					if (cursos.get(j).getCodCurso() > cursos.get(j + 1).getCodCurso()) 
					{
						Curso temp=cursos.get(j);
						cursos.set(j,cursos.get(j+1));
						cursos.set(j+1,temp);
					}
				}
			}
		}
		
		
		public Curso buscar(int codCurso) 
		{
		    for (int i=0; i<longitud(); i++) 
		    {
		        if (obtener(i).getCodCurso()==codCurso) 
		        {
		            return obtener(i); 
		        }
		    }
		    return null; 
		}
}
