package forms;

import arreglos.aAlumnos;
import arreglos.aCurso;
import arreglos.aMatricula;
import arreglos.aRetiro;
import clases.Alumno;
import clases.Curso;
import clases.Matricula;
import clases.Retiro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmConsulta extends JInternalFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Referencias a las listas compartidas con el resto del sistema.
    private aAlumnos   listaAlumnos;
    private aCurso     listaCursos;
    private aMatricula listaMatriculas;
    private aRetiro    listaRetiros;

    // Componentes del área de búsqueda.
    private JComboBox<String> cboTipoConsulta;
    private JLabel            lblCriterio;
    private JTextField        txtCriterio;
    private JButton           btnConsultar;
    private JButton           btnLimpiar;
    private JPanel pnlResultado;

    // Campos de resultado para los datos del alumno.
    private JTextField txtResCodAlumno, txtResDni, txtResNombres, txtResApellidos;
    private JTextField txtResCarrera, txtResEdad, txtResCelular, txtResEstado;
    private JTextField txtResCodCurso, txtResAsignatura, txtResCiclo;
    private JTextField txtResCreditos, txtResHoras;
    private JTextField txtResNumMatricula, txtResFechaMatricula, txtResHoraMatricula;
    private JTextField txtResNumRetiro, txtResFechaRetiro, txtResHoraRetiro;

    // Constructor: inicializa las listas, construye la interfaz y la hace visible.
    public frmConsulta(aAlumnos listaAlumnos, aCurso listaCursos,
                       aMatricula listaMatriculas, aRetiro listaRetiros) {
        super("Consultas", true, true, true, true);
        this.listaAlumnos    = listaAlumnos;
        this.listaCursos     = listaCursos;
        this.listaMatriculas = listaMatriculas;
        this.listaRetiros    = listaRetiros;

        // Dimensiones del formulario ampliadas para que todos los sub-paneles
        setBounds(60, 60, 690, 650);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        construirSelectorConsulta(contentPane);
        construirPanelBusqueda(contentPane);
        construirPanelResultado(contentPane);

        actualizarLabelCriterio();

        setVisible(true);
    }

    // Construcción del selector de tipo de consulta mediante JComboBox.
    private void construirSelectorConsulta(JPanel contentPane) {
        JLabel lblTipo = new JLabel("Tipo de consulta:");
        lblTipo.setBounds(10, 15, 125, 20);
        contentPane.add(lblTipo);

        cboTipoConsulta = new JComboBox<>(new String[]{
            "Alumno por código",
            "Curso por código",
            "Matrícula por número",
            "Retiro por número"
        });
        cboTipoConsulta.setBounds(140, 12, 220, 24);
        cboTipoConsulta.addActionListener(e -> {
            actualizarLabelCriterio();
            limpiarResultado();
        });
        contentPane.add(cboTipoConsulta);
    }

    // Construcción del área de búsqueda: etiqueta dinámica, campo de entrada y botones.
    private void construirPanelBusqueda(JPanel contentPane) {
        lblCriterio = new JLabel("Cód. Alumno:");
        lblCriterio.setBounds(10, 52, 125, 20);
        contentPane.add(lblCriterio);

        txtCriterio = new JTextField();
        txtCriterio.setBounds(140, 50, 160, 22);
        contentPane.add(txtCriterio);

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(310, 49, 110, 24);
        btnConsultar.addActionListener(this);
        contentPane.add(btnConsultar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(430, 49, 90, 24);
        btnLimpiar.addActionListener(this);
        contentPane.add(btnLimpiar);
    }

    // Construcción del panel principal de resultados y sus cuatro sub-paneles.
    private void construirPanelResultado(JPanel contentPane) {
        pnlResultado = new JPanel(null);
        pnlResultado.setBorder(new TitledBorder("Resultado"));
        pnlResultado.setBounds(10, 88, 654, 510);
        contentPane.add(pnlResultado);

        construirSubPanelAlumno();
        construirSubPanelCurso();
        construirSubPanelMatricula();
        construirSubPanelRetiro();
    }

    // Sub-panel: datos completos del alumno.
    private void construirSubPanelAlumno() {
        JPanel pnl = new JPanel(null);
        pnl.setBorder(new TitledBorder("Datos del Alumno"));
        pnl.setBounds(8, 15, 636, 165);
        pnl.setName("pnlAlumno");
        pnl.setVisible(false);
        pnlResultado.add(pnl);

        pnl.add(lbl("Código:", 10, 30));
        txtResCodAlumno = campo(pnl, 90, 27, 120, 20);
        pnl.add(lbl("DNI:", 230, 30));
        txtResDni = campo(pnl, 265, 27, 140, 20);

        pnl.add(lbl("Nombres:", 10, 60));
        txtResNombres = campo(pnl, 90, 57, 150, 20);
        pnl.add(lbl("Apellidos:", 255, 60));
        txtResApellidos = campo(pnl, 325, 57, 200, 20);

        pnl.add(lbl("Edad:", 10, 90));
        txtResEdad = campo(pnl, 90, 87, 60, 20);
        pnl.add(lbl("Celular:", 165, 90));
        txtResCelular = campo(pnl, 225, 87, 110, 20);
        pnl.add(lbl("Estado:", 350, 90));
        txtResEstado = campo(pnl, 410, 87, 120, 20);

        pnl.add(lbl("Carrera:", 10, 120));
        txtResCarrera = campo(pnl, 90, 117, 435, 20);
    }

    // Sub-panel: datos completos del curso.
    private void construirSubPanelCurso() {
        JPanel pnl = new JPanel(null);
        pnl.setBorder(new TitledBorder("Datos del Curso"));
        pnl.setBounds(8, 188, 636, 110);
        pnl.setName("pnlCurso");
        pnl.setVisible(false);
        pnlResultado.add(pnl);

        pnl.add(lbl("Código:", 10, 30));
        txtResCodCurso = campo(pnl, 80, 27, 80, 20);
        pnl.add(lbl("Asignatura:", 175, 30));
        txtResAsignatura = campo(pnl, 255, 27, 270, 20);

        pnl.add(lbl("Ciclo:", 10, 63));
        txtResCiclo = campo(pnl, 80, 60, 90, 20);
        pnl.add(lbl("Créditos:", 185, 63));
        txtResCreditos = campo(pnl, 255, 60, 60, 20);
        pnl.add(lbl("Horas:", 330, 63));
        txtResHoras = campo(pnl, 390, 60, 60, 20);
    }

    // Sub-panel: datos de la matrícula (número, fecha y hora).
    private void construirSubPanelMatricula() {
        JPanel pnl = new JPanel(null);
        pnl.setBorder(new TitledBorder("Datos de la Matrícula"));
        pnl.setBounds(8, 306, 636, 65);
        pnl.setName("pnlMatricula");
        pnl.setVisible(false);
        pnlResultado.add(pnl);

        pnl.add(lbl("N° Matrícula:", 10, 30));
        txtResNumMatricula = campo(pnl, 105, 27, 100, 20);
        pnl.add(lbl("Fecha:", 225, 30));
        txtResFechaMatricula = campo(pnl, 272, 27, 110, 20);
        pnl.add(lbl("Hora:", 400, 30));
        txtResHoraMatricula = campo(pnl, 440, 27, 100, 20);
    }

    // Sub-panel: datos del retiro (número, fecha y hora).
    private void construirSubPanelRetiro() {
        JPanel pnl = new JPanel(null);
        pnl.setBorder(new TitledBorder("Datos del Retiro"));
        pnl.setBounds(8, 379, 636, 65);
        pnl.setName("pnlRetiro");
        pnl.setVisible(false);
        pnlResultado.add(pnl);

        pnl.add(lbl("N° Retiro:", 10, 30));
        txtResNumRetiro = campo(pnl, 95, 27, 100, 20);
        pnl.add(lbl("Fecha:", 215, 30));
        txtResFechaRetiro = campo(pnl, 262, 27, 110, 20);
        pnl.add(lbl("Hora:", 392, 30));
        txtResHoraRetiro = campo(pnl, 432, 27, 100, 20);
    }

    // Métodos auxiliares para crear etiquetas y campos de texto no editables.
    private JLabel lbl(String texto, int x, int y) {
        JLabel l = new JLabel(texto);
        l.setBounds(x, y, texto.length() * 7 + 10, 18);
        return l;
    }

    private JTextField campo(JPanel contenedor, int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBounds(x, y, w, h);
        contenedor.add(tf);
        return tf;
    }

    // Actualiza la etiqueta del campo de criterio según el tipo de consulta seleccionado.
    private void actualizarLabelCriterio() {
        switch (cboTipoConsulta.getSelectedIndex()) {
            case 0: lblCriterio.setText("Cód. Alumno:");  break;
            case 1: lblCriterio.setText("Cód. Curso:");   break;
            case 2: lblCriterio.setText("N° Matrícula:"); break;
            case 3: lblCriterio.setText("N° Retiro:");    break;
        }
        txtCriterio.setText("");
        txtCriterio.requestFocus();
    }

    // Despacho de eventos hacia los métodos de acción correspondientes.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConsultar) realizarConsulta();
        if (e.getSource() == btnLimpiar)   limpiarResultado();
    }

    // Validación del criterio ingresado y enrutamiento al método de consulta correspondiente.
    private void realizarConsulta() {
        String criterio = txtCriterio.getText().trim();

        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un valor en el campo de búsqueda.",
                "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtCriterio.requestFocus();
            return;
        }

        int valor;
        try {
            valor = Integer.parseInt(criterio);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El valor ingresado debe ser un número entero.",
                "Dato inválido", JOptionPane.ERROR_MESSAGE);
            txtCriterio.requestFocus();
            return;
        }

        limpiarResultado();

        switch (cboTipoConsulta.getSelectedIndex()) {
            case 0: consultarPorAlumno(valor);    break;
            case 1: consultarPorCurso(valor);     break;
            case 2: consultarPorMatricula(valor); break;
            case 3: consultarPorRetiro(valor);    break;
        }
    }

    // Consulta por código de alumno.
    private void consultarPorAlumno(int codAlumno) {
        Alumno a = listaAlumnos.buscar(codAlumno);
        if (a == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró ningún alumno con código " + codAlumno + ".",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mostrarAlumno(a);

        Matricula m = buscarMatriculaActivaDeAlumno(codAlumno);
        if (m != null) {
            Curso c = listaCursos.buscar(m.getCodCurso());
            if (c != null) mostrarCurso(c);
            mostrarMatricula(m);
            mostrarPanelSegunTipo("alumno_con_matricula");
        } else {
            txtResAsignatura.setText("(Sin matrícula activa)");
            mostrarPanelSegunTipo("alumno_sin_matricula");
        }
    }

    // Consulta por código de curso.
    private void consultarPorCurso(int codCurso) {
        Curso c = listaCursos.buscar(codCurso);
        if (c == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró ningún curso con código " + codCurso + ".",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        mostrarCurso(c);
        mostrarPanelSegunTipo("solo_curso");
    }

    // Consulta por número de matrícula.
    private void consultarPorMatricula(int numMatricula) {
        Matricula m = listaMatriculas.buscar(numMatricula);
        if (m == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró ninguna matrícula con número " + numMatricula + ".",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mostrarMatricula(m);
        Alumno a = listaAlumnos.buscar(m.getCodAlumno());
        Curso  c = listaCursos.buscar(m.getCodCurso());
        if (a != null) mostrarAlumno(a);
        if (c != null) mostrarCurso(c);
        mostrarPanelSegunTipo("matricula_completa");
    }

    // Consulta por número de retiro.
    private void consultarPorRetiro(int numRetiro) {
        Retiro r = listaRetiros.buscar(numRetiro);
        if (r == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró ningún retiro con número " + numRetiro + ".",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Matricula m = listaMatriculas.buscar(r.getNumMatricula());
        Alumno a = (m != null) ? listaAlumnos.buscar(m.getCodAlumno()) : null;
        Curso  c = (m != null) ? listaCursos.buscar(m.getCodCurso())   : null;

        mostrarRetiro(r);
        if (m != null) mostrarMatricula(m);
        if (a != null) mostrarAlumno(a);
        if (c != null) mostrarCurso(c);
        mostrarPanelSegunTipo("retiro_completo");
    }

    // Presentación de los datos del alumno en los campos de resultado correspondientes.
    private void mostrarAlumno(Alumno a) {
        txtResCodAlumno.setText(String.valueOf(a.getCodigo()));
        txtResDni.setText(a.getDni());
        txtResNombres.setText(a.getNombres());
        txtResApellidos.setText(a.getApellidos());
        txtResCarrera.setText(a.getCarrera());
        txtResEdad.setText(String.valueOf(a.getEdad()));
        txtResCelular.setText(String.valueOf(a.getCelular()));

        // Conversión del estado numérico a texto descriptivo, igual al resto del sistema.
        String estadoStr;
        switch (a.getEstado()) {
            case 0:  estadoStr = "Registrado";  break;
            case 1:  estadoStr = "Matriculado"; break;
            case 2:  estadoStr = "Retirado";    break;
            default: estadoStr = "Desconocido"; break;
        }
        txtResEstado.setText(estadoStr);
    }

    // Presentación de los datos del curso en los campos de resultado correspondientes.
    private void mostrarCurso(Curso c) {
        txtResCodCurso.setText(String.valueOf(c.getCodCurso()));
        txtResAsignatura.setText(c.getAsignatura());
        txtResCreditos.setText(String.valueOf(c.getCreditos()));
        txtResHoras.setText(String.valueOf(c.getHoras()));

        // Conversión del ciclo numérico a texto descriptivo, igual al usado en frmCursos.
        String[] nombresCiclos = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto"};
        int ciclo = c.getCiclo();
        txtResCiclo.setText((ciclo >= 0 && ciclo < nombresCiclos.length)
            ? nombresCiclos[ciclo] : String.valueOf(ciclo));
    }

    // Presentación de los datos de la matrícula en los campos de resultado correspondientes.
    private void mostrarMatricula(Matricula m) {
        txtResNumMatricula.setText(String.valueOf(m.getNumMatricula()));
        txtResFechaMatricula.setText(m.getFecha());
        txtResHoraMatricula.setText(m.getHora());
    }

    // Presentación de los datos del retiro en los campos de resultado correspondientes.
    private void mostrarRetiro(Retiro r) {
        txtResNumRetiro.setText(String.valueOf(r.getNumRetiro()));
        txtResFechaRetiro.setText(r.getFecha());
        txtResHoraRetiro.setText(r.getHora());
    }

    // Control de visibilidad de los sub-paneles según el tipo de consulta ejecutada.
    private void mostrarPanelSegunTipo(String modo) {
        JPanel pnlAlumno    = getPanelPorNombre("pnlAlumno");
        JPanel pnlCurso     = getPanelPorNombre("pnlCurso");
        JPanel pnlMatricula = getPanelPorNombre("pnlMatricula");
        JPanel pnlRetiro    = getPanelPorNombre("pnlRetiro");

        // Oculta todos los sub-paneles antes de mostrar solo los pertinentes.
        if (pnlAlumno    != null) pnlAlumno.setVisible(false);
        if (pnlCurso     != null) pnlCurso.setVisible(false);
        if (pnlMatricula != null) pnlMatricula.setVisible(false);
        if (pnlRetiro    != null) pnlRetiro.setVisible(false);

        switch (modo) {
            case "alumno_con_matricula":
                if (pnlAlumno    != null) pnlAlumno.setVisible(true);
                if (pnlCurso     != null) pnlCurso.setVisible(true);
                if (pnlMatricula != null) pnlMatricula.setVisible(true);
                break;
            case "alumno_sin_matricula":
                if (pnlAlumno != null) pnlAlumno.setVisible(true);
                if (pnlCurso  != null) pnlCurso.setVisible(true);
                break;
            case "solo_curso":
                if (pnlCurso != null) pnlCurso.setVisible(true);
                break;
            case "matricula_completa":
                if (pnlAlumno    != null) pnlAlumno.setVisible(true);
                if (pnlCurso     != null) pnlCurso.setVisible(true);
                if (pnlMatricula != null) pnlMatricula.setVisible(true);
                break;
            case "retiro_completo":
                if (pnlAlumno    != null) pnlAlumno.setVisible(true);
                if (pnlCurso     != null) pnlCurso.setVisible(true);
                if (pnlMatricula != null) pnlMatricula.setVisible(true);
                if (pnlRetiro    != null) pnlRetiro.setVisible(true);
                break;
        }

        pnlResultado.revalidate();
        pnlResultado.repaint();
    }

    // Localiza un sub-panel dentro de pnlResultado mediante su nombre asignado.
    private JPanel getPanelPorNombre(String nombre) {
        for (Component comp : pnlResultado.getComponents()) {
            if (comp instanceof JPanel && nombre.equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    // Búsqueda de la matrícula activa de un alumno dentro de listaMatriculas.
    private Matricula buscarMatriculaActivaDeAlumno(int codAlumno) {
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Matricula m = listaMatriculas.obtener(i);
            if (m.getCodAlumno() == codAlumno) {
                Alumno a = listaAlumnos.buscar(m.getCodAlumno());
                if (a != null && a.getEstado() == 1) {
                    return m;
                }
            }
        }
        return null;
    }

    // Limpieza de todos los campos de resultado y ocultamiento de los sub-paneles.
    private void limpiarResultado() {
        txtResCodAlumno.setText("");  txtResDni.setText("");
        txtResNombres.setText("");    txtResApellidos.setText("");
        txtResCarrera.setText("");    txtResEdad.setText("");
        txtResCelular.setText("");    txtResEstado.setText("");

        txtResCodCurso.setText("");   txtResAsignatura.setText("");
        txtResCiclo.setText("");      txtResCreditos.setText("");
        txtResHoras.setText("");

        txtResNumMatricula.setText("");
        txtResFechaMatricula.setText("");
        txtResHoraMatricula.setText("");

        txtResNumRetiro.setText("");
        txtResFechaRetiro.setText("");
        txtResHoraRetiro.setText("");

        mostrarPanelSegunTipo("ninguno");

        txtCriterio.setText("");
        txtCriterio.requestFocus();
    }
}
