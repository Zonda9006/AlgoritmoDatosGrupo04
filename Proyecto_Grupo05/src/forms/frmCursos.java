package forms;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import arreglos.aAlumnos;
import arreglos.aCurso;
import clases.Alumno;
import clases.ArchivoCursos;
import clases.Curso;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class frmCursos extends JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JLabel lblAsignatura;
	private JLabel lblCiclo;
	private JLabel lblCreditos;
	private JLabel lblHoras;
	private JTextField txtCodigo;
	private JTextField txtAsignatura;
	private JTextField txtCreditos;
	private JTextField txtHoras;
	private JButton btnLimpiar;
	private JButton btnAgregar;
	private JButton btnBuscar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JScrollPane scrollPane;
	private JTable tbCursos;
	private JComboBox cboCiclo;

	static aCurso misCursos=new aCurso();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmCursos frame = new frmCursos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public frmCursos() {
		super("MANTENIMIENTO DE CURSOS", true, true, true, true); 
		setBounds(100, 100, 746, 573);
		getContentPane().setLayout(null);
		JPanel contentPane = new JPanel();
	    contentPane.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);
		
		lblCodigo = new JLabel("Código:");
		lblCodigo.setBounds(31, 44, 72, 12);
		contentPane.add(lblCodigo);
		
		lblAsignatura = new JLabel("Asignatura:");
		lblAsignatura.setBounds(31, 77, 72, 12);
		contentPane.add(lblAsignatura);
		
		lblCiclo = new JLabel("Ciclo:");
		lblCiclo.setBounds(31, 117, 72, 12);
		contentPane.add(lblCiclo);
		
		lblCreditos = new JLabel("Créditos:");
		lblCreditos.setBounds(31, 156, 72, 12);
		contentPane.add(lblCreditos);
		
		lblHoras = new JLabel("Horas:");
		lblHoras.setBounds(31, 189, 72, 12);
		contentPane.add(lblHoras);
		
		txtCodigo = new JTextField();
		txtCodigo.setBounds(109, 41, 141, 18);
		contentPane.add(txtCodigo);
		txtCodigo.setColumns(10);
		
		txtAsignatura = new JTextField();
		txtAsignatura.setBounds(109, 74, 141, 18);
		contentPane.add(txtAsignatura);
		txtAsignatura.setColumns(10);
		
		txtCreditos = new JTextField();
		txtCreditos.setBounds(109, 153, 141, 18);
		contentPane.add(txtCreditos);
		txtCreditos.setColumns(10);
		
		txtHoras = new JTextField();
		txtHoras.setBounds(109, 189, 141, 18);
		contentPane.add(txtHoras);
		txtHoras.setColumns(10);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(this);
		btnLimpiar.setFocusPainted(false);
		btnLimpiar.setBounds(528, 40, 97, 20);
		contentPane.add(btnLimpiar);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(this);
		btnAgregar.setFocusPainted(false);
		btnAgregar.setBounds(528, 73, 97, 20);
		contentPane.add(btnAgregar);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(this);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBounds(528, 113, 97, 20);
		contentPane.add(btnBuscar);
		
		btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(this);
		btnModificar.setFocusPainted(false);
		btnModificar.setBounds(528, 152, 97, 20);
		contentPane.add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(this);
		btnEliminar.setFocusPainted(false);
		btnEliminar.setBounds(528, 185, 97, 20);
		contentPane.add(btnEliminar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 258, 640, 252);
		contentPane.add(scrollPane);
		
		tbCursos = new JTable();
		tbCursos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Código", "Asignatura", "Ciclo", "Créditos", "Horas"
			}
		));
		scrollPane.setViewportView(tbCursos);
		
		cboCiclo = new JComboBox();
		cboCiclo.setBounds(109, 113, 141, 20);
		cboCiclo.setModel(new DefaultComboBoxModel(new String[] {"Seleccione", "Primero", "Segundo", "Tercero", "Cuarto", "Quinto"}));
		contentPane.add(cboCiclo);
		
		listado();
	}
	
	//Método Listar para listar todos los elementos de ArrayList almacenados en tbCursos
		public void listado() 
		{
			DefaultTableModel modelo=(DefaultTableModel)tbCursos.getModel();
			modelo.setRowCount(0); 
			
			//Definir un arreglo de textos auxiliares para traducir el entero del ciclo
		    String[] nombresCiclos = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto"};
			
			for(int i=0; i<misCursos.longitud();i++) 
			{ 
				// Recuperar al alumno por su ubicación
				Curso c=misCursos.obtener(i);
				
				int numCiclo = c.getCiclo();
		        String textoCiclo = nombresCiclos[numCiclo];
				
				// agregar una fila al modelo, el cual contiene una
				//colección de objetos por el c (Curso)
				modelo.addRow(new Object[] {c.getCodCurso(), c.getAsignatura(), textoCiclo, c.getCreditos(),
						c.getHoras()});
				
			}
		}
		
		//Método limpiar
		public void limpiar() 
		{
			txtCodigo.setText("");     
		    txtAsignatura.setText("");  
		    cboCiclo.setSelectedIndex(0); 
		    txtCreditos.setText("");  
		    txtHoras.setText("");       
		    txtCodigo.requestFocus();
		}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnModificar) {
			actionPerformedBtnModificar(e);
		}
		ArchivoCursos.guardar(misCursos);
		if (e.getSource() == btnBuscar) {
			actionPerformedBtnBuscar(e);
		}
		if (e.getSource() == btnAgregar) {
			actionPerformedBtnAgregar(e);
		}
		ArchivoCursos.guardar(misCursos);
		if (e.getSource() == btnEliminar) {
			actionPerformedBtnEliminar(e);
		}
		ArchivoCursos.guardar(misCursos);
		if (e.getSource() == btnLimpiar) {
			actionPerformedBtnLimpiar(e);
		}
	}
	
	private void actionPerformedBtnEliminar(ActionEvent e) {
	
	    int resp = JOptionPane.showConfirmDialog(this, 
	            "¿Está seguro de eliminar el curso seleccionado?", 
	            "Confirmar", JOptionPane.YES_NO_OPTION);
	  
	    if (resp == JOptionPane.YES_OPTION) {
	        int fila = tbCursos.getSelectedRow();
	        
	        int codigo = Integer.parseInt(tbCursos.getValueAt(fila, 0).toString());
	        
	        for (int i = 0; i < misCursos.longitud(); i++) {
	            if (misCursos.obtener(i).getCodCurso() == codigo) {
	                misCursos.eliminar(i); 
	                break;
	            }
	        }
	        
	        listado();
	        limpiar();
	    }
	}

	protected void actionPerformedBtnLimpiar(ActionEvent e) 
	{
		limpiar();
	}
	
	protected void actionPerformedBtnAgregar(ActionEvent e) 
	{
		try 
		{
			if(txtCodigo.getText().trim().isEmpty() || 
		            txtAsignatura.getText().trim().isEmpty() ||
		            txtCreditos.getText().trim().isEmpty() || 
		            txtHoras.getText().trim().isEmpty()) 
			{
				JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de registrar.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
	            return;
			}
			
			if (cboCiclo.getSelectedIndex()==0) 
			{
				JOptionPane.showMessageDialog(this, "Debe seleccionar un ciclo válido.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
	            return;
			}
			
			int codigo=Integer.parseInt(txtCodigo.getText().trim());
			if (codigo<1000||codigo>9999) 
			{
				JOptionPane.showMessageDialog(this, "El código del curso debe ser un número entero de exactamente 4 dígitos", "Código Inválido", JOptionPane.WARNING_MESSAGE);
	            return;
			}
			
			//4. Verificar duplicación de código 
			if (misCursos.buscar(codigo)!= null) {
	            JOptionPane.showMessageDialog(this, "El código " + codigo + " ya se encuentra registrado en otro curso.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
			
			//5. obtener asignatura, creditos y horas
			String asignatura=txtAsignatura.getText().trim();
			int creditos=Integer.parseInt(txtCreditos.getText().trim());
			int horas=Integer.parseInt(txtHoras.getText().trim());
			
			//6. Ajuste del JComboBox para el ciclo
	        int ciclo=cboCiclo.getSelectedIndex()-1;
	        
	        //7.Crear el objeto Curso e insertarlo
	        Curso nuevoCurso=new Curso(codigo, asignatura, ciclo, creditos, horas);
	        misCursos.adicionar(nuevoCurso);
	        
	        //8. Mensaje de confirmación 
	        JOptionPane.showMessageDialog(this, "Curso adicionado y ordenado correctamente.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            
	        listado();           
	        limpiar();
		}
		
		catch (NumberFormatException ex)
		{
			JOptionPane.showMessageDialog(this, "Los campos Código, Créditos y Horas deben ser valores numéricos enteros."+"\nAsignatura debe ser String y Código un número de 4 dígitos", "Error de Tipo de Datos", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void actionPerformedBtnBuscar(ActionEvent e) 
	{
		try {
	        // 1. Validar que el campo Código no esté vacío
	        if (txtCodigo.getText().trim().isEmpty()) 
	        {
	            JOptionPane.showMessageDialog(this, "Por favor, ingrese el código del curso que desea buscar.", "Código Requerido", JOptionPane.WARNING_MESSAGE);
	            txtCodigo.requestFocus();
	            return;
	        }

	        // 2. Capturar el código y validar que sea de 4 dígitos
	        int codigo=Integer.parseInt(txtCodigo.getText().trim());
	        if (codigo<1000||codigo>9999) 
	        {
	            JOptionPane.showMessageDialog(this, "El código a buscar debe ser un número entero de exactamente 4 dígitos.", "Código Inválido", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // 3. Llamar al método buscar de la clase global 'misCursos'
	        Curso c=misCursos.buscar(codigo);

	        // 4. Verificar si el curso fue encontrado
	        if (c!=null) 
	        {
	            txtAsignatura.setText(c.getAsignatura());
	            txtCreditos.setText(String.valueOf(c.getCreditos()));
	            txtHoras.setText(String.valueOf(c.getHoras()));
	            cboCiclo.setSelectedIndex(c.getCiclo()+1);
	            
	            JOptionPane.showMessageDialog(this,"Curso encontrado con éxito.","Búsqueda Exitosa",JOptionPane.INFORMATION_MESSAGE);
	        } 
	        
	        else 
	        {
	        
	            JOptionPane.showMessageDialog(this,"El código "+codigo+" no se encuentra registrado.","Sin Resultados",JOptionPane.WARNING_MESSAGE);
	            limpiar();
	        }

	    } 
		
		catch (NumberFormatException ex) 
		{
	        JOptionPane.showMessageDialog(this, "El código de búsqueda debe ser un valor numérico entero.","Error de Datos",JOptionPane.ERROR_MESSAGE);
	        limpiar();
	    }
	}
	
	protected void actionPerformedBtnModificar(ActionEvent e) 
	{
		try {
	        //1. Validar que el campo código no esté vacío
	        if (txtCodigo.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Por favor, ingrese o busque el código del curso que desea modificar.", "Código Requerido", JOptionPane.WARNING_MESSAGE);
	            txtCodigo.requestFocus();
	            return;
	        }

	        // 2. Capturar el código 
	        int codigo=Integer.parseInt(txtCodigo.getText().trim());

	        // 3. Buscar el curso en misCursos
	        Curso c = misCursos.buscar(codigo);

	        // 4. Verificar si el curso existe
	        if (c == null) 
	        {
	            JOptionPane.showMessageDialog(this, "El código " + codigo + " no existe. Primero debe buscar un curso válido para modificarlo.", "Error", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // 5. Validar que los NUEVOS datos ingresados en las demás cajas no estén vacíos
	        if (txtAsignatura.getText().trim().isEmpty() ||
	            txtCreditos.getText().trim().isEmpty() || 
	            txtHoras.getText().trim().isEmpty()) 
	        {
	            JOptionPane.showMessageDialog(this, "No puede dejar los campos de Asignatura, Créditos o Horas vacíos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // 6. validar que se haya seleccionado un ciclo real en el JComboBox
	        if (cboCiclo.getSelectedIndex()==0) 
	        {
	            JOptionPane.showMessageDialog(this, "Debe seleccionar un ciclo válido.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        
	        
	        // 7. Recuperamos el nombre de la asignatura que está guardada actualmente en el objeto
	        String asignaturaActual=c.getAsignatura();
	        
	        // 8. Creamos un arreglo con los textos exactos que quieres para los botones
	        Object[] opciones={"Guardar","Cancelar"};
	        
	        // 9. Lanzamos la ventana de consulta en pantalla
	        int respuesta = JOptionPane.showOptionDialog
	        		(
			            this, 
			            "¿Desea guardar los cambios de la asignatura "+ asignaturaActual+"?", 
			            "Confirmar modificación", 
			            JOptionPane.YES_NO_OPTION, 
			            JOptionPane.QUESTION_MESSAGE, 
			            null, 
			            opciones, 
			            opciones[0]
	        		);

	        // 10. Si el usuario presiona "Cancelar" o cierra la ventana, detenemos el método de inmediato
	        if (respuesta != JOptionPane.YES_OPTION) {
	            return; 
	        }
	        
	        

	        // 11. Obtener Y modificar los atributos usando los SETTERS de la clase Cursos
	        c.setAsignatura(txtAsignatura.getText().trim());
	        
	        // 12. Ajuste matemático para el JComboBox (-1)
	        c.setCiclo(cboCiclo.getSelectedIndex() - 1);
	        c.setCreditos(Integer.parseInt(txtCreditos.getText().trim()));
	        c.setHoras(Integer.parseInt(txtHoras.getText().trim()));

	        //13. Confirmar los cambios al usuario, refrescar la JTable y limpiar la pantalla
	        JOptionPane.showMessageDialog(this, "El curso con código " + codigo + " ha sido modificado correctamente.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
	        
	        listado();           
	        limpiar(); 

	    } 
		
		catch (NumberFormatException ex) 
		{
	        JOptionPane.showMessageDialog(this, "Los campos Créditos y Horas deben ser valores numéricos enteros.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
