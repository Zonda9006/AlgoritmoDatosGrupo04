package forms;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import arreglos.aAlumnos;
import arreglos.aCurso;
import arreglos.aMatricula;
import arreglos.aRetiro;
import clases.ArchivoAlumno;
import clases.ArchivoCursos;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;

public class frmPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Componentes de la interfaz principal
    private JPanel contentPane;
    private JMenuBar menuBar;
    private JMenu mnMantenimiento;
    private JMenu mnRegistro;
    private JMenu mnConsulta;
    private JMenu mnReporte;
    private JMenuItem mntmAlumno;
    private JMenuItem mntmCurso;
    private JMenuItem mntmReporteGeneral;
    private final JDesktopPane escritorio = new JDesktopPane();
    private JMenuItem mntmMatricula;
    private JMenuItem mntmRetiro;
    private JMenuItem mntmConsulta;

    // Referencia persistente a la ventana de matrícula para sincronizar con frmRetiro
    private frmMatricula ventanaMatricula;

    // Listas compartidas: apuntan a los mismos objetos estáticos de frmAlumnos y frmCursos
    // para que Mantenimiento y Registro trabajen sobre la misma información en memoria
    private aAlumnos listaAlumnos    = frmAlumnos.misAlumnos;
    private aCurso listaCursos     = frmCursos.misCursos;
    private aMatricula listaMatriculas = new aMatricula();
    private aRetiro listaRetiros    = new aRetiro();

    // Punto de entrada de la aplicación
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frmPrincipal frame = new frmPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Constructor: configura la ventana principal, el menú y carga los datos persistidos
    public frmPrincipal() {
        setTitle("REGISTRO Y MATRÍCULA DE ALUMNOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1036, 780);

        // Construcción de la barra de menú y sus ítems
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnMantenimiento = new JMenu("Mantenimiento");
        menuBar.add(mnMantenimiento);

        mntmAlumno = new JMenuItem("Alumno");
        mntmAlumno.addActionListener(this);
        mnMantenimiento.add(mntmAlumno);

        mntmCurso = new JMenuItem("Curso");
        mntmCurso.addActionListener(this);
        mnMantenimiento.add(mntmCurso);

        mnRegistro = new JMenu("Registro");
        menuBar.add(mnRegistro);

        mntmMatricula = new JMenuItem("Matricula");
        mntmMatricula.addActionListener(this);
        mnRegistro.add(mntmMatricula);

        mntmRetiro = new JMenuItem("Retiro");
        mntmRetiro.addActionListener(this);
        mnRegistro.add(mntmRetiro);

        mnConsulta = new JMenu("Consulta");
        menuBar.add(mnConsulta);
        mntmConsulta = new JMenuItem("Consulta General");
        mntmConsulta.addActionListener(this);
        mnConsulta.add(mntmConsulta);

        mnReporte = new JMenu("Reporte");
        menuBar.add(mnReporte);

        mntmReporteGeneral = new JMenuItem("General"); 
        mntmReporteGeneral.addActionListener(this);
        mnReporte.add(mntmReporteGeneral);

        // Configuración del panel principal y el escritorio MDI
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        escritorio.setBounds(0, 0, 1022, 721);
        contentPane.add(escritorio);
        
        // Carga de datos persistidos al iniciar la aplicación
        ArchivoAlumno.cargar(frmAlumnos.misAlumnos);
        ArchivoCursos.cargar(frmCursos.misCursos);
    }

    // Despacha las acciones del menú al método correspondiente
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mntmAlumno) {
            actionPerformedMntmAlumno(e);
        }
        if (e.getSource() == mntmCurso) {
            actionPerformedMntmCurso(e);
        }
        
        if (e.getSource() == mntmReporteGeneral) {
            actionPerformedMntmReporteGeneral(e);
        }
        
        if (e.getSource() == mntmMatricula) {
            actionPerformedMntmMatricula(e);
        }
        
        if (e.getSource() == mntmRetiro) {
            actionPerformedMntmRetiro(e);
        }
        
        if (e.getSource() == mntmConsulta) {
            actionPerformedMntmConsulta(e);
        }
    }
	

	// Abre la ventana de mantenimiento de cursos
    protected void actionPerformedMntmCurso(ActionEvent e) {
        frmCursos f = new frmCursos();
        escritorio.add(f);
        f.show();
    }

    // Abre la ventana de mantenimiento de alumnos centrada en el escritorio
    protected void actionPerformedMntmAlumno(ActionEvent e) {
        frmAlumnos f = new frmAlumnos();
        escritorio.add(f);
        int x = (contentPane.getWidth() - f.getWidth()) / 2;
        int y = (contentPane.getHeight() - f.getHeight()) / 2;
        f.setLocation(x, y);
        f.show();
        f.toFront();
    }

    // Abre la ventana de reporte general centrada en el escritorio
    protected void actionPerformedMntmReporteGeneral(ActionEvent e) {
        frmReporte f = new frmReporte();
        escritorio.add(f);
        int x = (contentPane.getWidth() - f.getWidth()) / 2;
        int y = (contentPane.getHeight() - f.getHeight()) / 2;
        f.setLocation(x, y);
        f.setVisible(true);
        f.toFront();
    }
    
    // Abre la ventana de matrícula y guarda la referencia para uso de frmRetiro
    protected void actionPerformedMntmMatricula(ActionEvent e) {
        ventanaMatricula = new frmMatricula(listaAlumnos, listaCursos, listaMatriculas);
        escritorio.add(ventanaMatricula);
        ventanaMatricula.toFront();
    }
    
    // Abre la ventana de retiro pasando la referencia de la ventana de matrícula
    protected void actionPerformedMntmRetiro(ActionEvent e) {
        frmRetiro f = new frmRetiro(listaAlumnos, listaCursos, listaMatriculas, listaRetiros, ventanaMatricula);
        escritorio.add(f);
        f.toFront();
    }
    
    private void actionPerformedMntmConsulta(ActionEvent e) {
  		// Abre el formulario de consultas pasando todas la slistas compartidas
      	  frmConsulta f = new frmConsulta(listaAlumnos, listaCursos, listaMatriculas, listaRetiros);
            escritorio.add(f);
            int x = (contentPane.getWidth() - f.getWidth()) / 2;
            int y = (contentPane.getHeight() - f.getHeight()) / 2;
            f.setLocation(x, y);
            
            f.show(); 
            f.toFront();
        }
    
}