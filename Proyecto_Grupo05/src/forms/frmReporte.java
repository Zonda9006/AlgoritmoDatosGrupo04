package forms;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import clases.Alumno;
import clases.Curso;

public class frmReporte extends JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Componentes de la interfaz
	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lblTotal;
	private JButton btnAlumnos;
	private JButton btnCursos;
	
	// Punto de entrada de la aplicación
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmReporte frame = new frmReporte();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor: configura la ventana y construye los componentes de la interfaz
	public frmReporte() {
	    setTitle("REPORTES");
	    setBounds(100, 100, 549, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Título y botones de selección de tipo de reporte
		JLabel lblTitulo = new JLabel("TIPO DE REPORTES");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(23, 24, 187, 20);
		contentPane.add(lblTitulo);

		btnAlumnos = new JButton("Alumnos");
		btnAlumnos.addActionListener(this);
		btnAlumnos.setBounds(23, 57, 100, 23);
		contentPane.add(btnAlumnos);

		btnCursos = new JButton("Cursos");
		btnCursos.addActionListener(this);
		btnCursos.setBounds(23, 91, 100, 23);
		contentPane.add(btnCursos);

		// Área de visualización del reporte generado
		JLabel lblReporte = new JLabel("REPORTE GENERADO");
		lblReporte.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblReporte.setBounds(180, 136, 170, 20);
		contentPane.add(lblReporte);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 186, 489, 269);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		// Etiqueta y contador de registros reportados
		JLabel lblTextoTotal = new JLabel("Total Reportados:");
		lblTextoTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTextoTotal.setBounds(23, 472, 128, 20);
		contentPane.add(lblTextoTotal);

		lblTotal = new JLabel("0");
		lblTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotal.setBounds(161, 472, 100, 20);
		contentPane.add(lblTotal);
	}

	// Despacha las acciones de los botones al método correspondiente
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAlumnos) {
			actionPerformedBtnAlumnos(e);
		}
		if (e.getSource() == btnCursos) {
			actionPerformedBtnCursos(e);
		}
	}

	// Genera el reporte de alumnos y lo muestra en el área de texto
	protected void actionPerformedBtnAlumnos(ActionEvent e) {
		textArea.setText("");
		textArea.append("CODIGO\tDNI\tNOMBRES\tAPELLIDOS\n\n");
		for (int i = 0; i < frmAlumnos.misAlumnos.longitud(); i++) {
			Alumno a = frmAlumnos.misAlumnos.obtener(i);
			textArea.append(
					a.getCodigo() + "\t" +
					a.getDni() + "\t" +
					a.getNombres() + "\t" +
					a.getApellidos() + "\n");
		}
		lblTotal.setText(String.valueOf(frmAlumnos.misAlumnos.longitud()));
	}

	// Genera el reporte de cursos y lo muestra en el área de texto
	protected void actionPerformedBtnCursos(ActionEvent e) {
		textArea.setText("");
		textArea.append("CODIGO\tASIGNATURA\tCREDITOS\tHORAS\n\n");
		for (int i = 0; i < frmCursos.misCursos.longitud(); i++) {
			Curso c = frmCursos.misCursos.obtener(i);
			textArea.append(
					c.getCodCurso() + "\t" +
					c.getAsignatura() + "\t" +
					c.getCreditos() + "\t" +
					c.getHoras() + "\n");
		}
		lblTotal.setText(
				String.valueOf(frmCursos.misCursos.longitud()));
	}
}