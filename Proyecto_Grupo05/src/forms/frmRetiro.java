package forms;

import arreglos.aAlumnos;
import arreglos.aCurso;
import arreglos.aMatricula;
import arreglos.aRetiro;
import clases.Alumno;
import clases.ArchivoAlumno;
import clases.Curso;
import clases.Matricula;
import clases.Retiro;
import clases.ArchivoRetiro;
import clases.ArchivoMatricula;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class frmRetiro extends JInternalFrame {

    // Listas de datos compartidas con el sistema
    private aAlumnos    listaAlumnos;
    private aCurso      listaCursos;
    private aMatricula  listaMatriculas;
    private aRetiro     listaRetiros;

    // Componentes del formulario
    private JTextField txtNumRetiro, txtCodAlumno, txtInfoMatricula, txtFecha, txtHora;
    // Tabla y su modelo de datos
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    // Botones de acción
    private JButton btnBuscar, btnAdicionar, btnEliminar, btnGrabar, btnLimpiar;
    // Referencia a la ventana de matrícula para sincronizar su tabla
    private frmMatricula ventanaMatricula;

    // Componentes del autocompletado
    private JPopupMenu            popupSugerencias;
    private JList<String>         listaSugerencias;
    private DefaultListModel<String> modeloSugerencias;
    // Bandera para evitar que actualizaciones programáticas disparen el DocumentListener
    private boolean ignorarCambioTexto = false;

    // Constructor: inicializa la ventana, construye la interfaz y carga los datos persistidos
    public frmRetiro(aAlumnos listaAlumnos, aCurso listaCursos,
            aMatricula listaMatriculas, aRetiro listaRetiros,
            frmMatricula ventanaMatricula) {
		super("Retiro de Alumnos", true, true, true, true);
		this.listaAlumnos    = listaAlumnos;
		this.listaCursos     = listaCursos;
		this.listaMatriculas = listaMatriculas;
		this.listaRetiros    = listaRetiros;
	    this.ventanaMatricula = ventanaMatricula;
		
		setBounds(80, 80, 800, 520);
		setLayout(new BorderLayout());
		
		// Construcción del panel de formulario
		JPanel pnlForm = new JPanel(new GridBagLayout());
		pnlForm.setBorder(BorderFactory.createTitledBorder("Datos de Retiro"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 8, 4, 8);
		gbc.anchor = GridBagConstraints.WEST;
		
		// Fila 0: N° Retiro | Fecha | Hora
		gbc.gridx = 0; gbc.gridy = 0;
		pnlForm.add(new JLabel("N° Retiro:"), gbc);
		txtNumRetiro = new JTextField(10); txtNumRetiro.setEditable(false);
		gbc.gridx = 1; pnlForm.add(txtNumRetiro, gbc);
		
		gbc.gridx = 2; gbc.gridy = 0;
		pnlForm.add(new JLabel("Fecha:"), gbc);
		txtFecha = new JTextField(10); txtFecha.setEditable(false);
		gbc.gridx = 3; pnlForm.add(txtFecha, gbc);
		
		gbc.gridx = 4; gbc.gridy = 0;
		pnlForm.add(new JLabel("Hora:"), gbc);
		txtHora = new JTextField(10); txtHora.setEditable(false);
		gbc.gridx = 5; pnlForm.add(txtHora, gbc);
		
		// Fila 1: Cód. Alumno | Buscar | Alumno / Curso
		gbc.gridx = 0; gbc.gridy = 1;
		pnlForm.add(new JLabel("Cód. Alumno:"), gbc);
		txtCodAlumno = new JTextField(10);
		gbc.gridx = 1; pnlForm.add(txtCodAlumno, gbc);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> buscarAlumno());
		gbc.gridx = 2; pnlForm.add(btnBuscar, gbc);
		
		gbc.gridx = 3; gbc.gridy = 1;
		pnlForm.add(new JLabel("Alumno / Curso:"), gbc);
		txtInfoMatricula = new JTextField(35); txtInfoMatricula.setEditable(false);
		gbc.gridx = 4; gbc.gridwidth = 2; pnlForm.add(txtInfoMatricula, gbc);
		gbc.gridwidth = 1;
		
		add(pnlForm, BorderLayout.NORTH);
		
		// Construcción de la tabla de retiros
		String[] cols = {"N° Retiro", "N° Matrícula", "Alumno", "Curso", "Fecha", "Hora"};
		modeloTabla = new DefaultTableModel(cols, 0) {
		   @Override
		   public boolean isCellEditable(int r, int c) { return false; }
		};
		tabla = new JTable(modeloTabla);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.getSelectionModel().addListSelectionListener(e -> cargarFilaSeleccionada());
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		
		// Construcción del panel de botones y registro de listeners
		JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setFocusPainted(false);
		btnEliminar  = new JButton("Eliminar");
		btnEliminar.setFocusPainted(false);
		btnGrabar    = new JButton("Grabar");
		btnGrabar.setFocusPainted(false);
		btnLimpiar   = new JButton("Limpiar");
		btnLimpiar.setFocusPainted(false);
		
		pnlBotones.add(btnAdicionar);
		pnlBotones.add(btnEliminar);
		pnlBotones.add(btnGrabar);
		pnlBotones.add(btnLimpiar);
		add(pnlBotones, BorderLayout.SOUTH);
		
		btnAdicionar.addActionListener(e -> adicionar());
		btnEliminar.addActionListener(e  -> eliminar());
		btnGrabar.addActionListener(e    -> grabar());
		btnLimpiar.addActionListener(e   -> limpiar());
		
		
		inicializarAutocompletado();
		
		// Carga de matrículas desde archivo si la lista viene vacía
		if (this.listaMatriculas.longitud() == 0) {
		   ArchivoMatricula.cargar(this.listaMatriculas);
		}
		
		// Carga de retiros, sincronización de estados y refresco inicial
		listaRetiros.limpiar();
		ArchivoRetiro.cargar(listaRetiros);
		sincronizarEstadosDesdeRetiros();
		refrescarTabla();
		estadoInicial();
		
		setVisible(true);
		}
    

    // Inicializa el popup de autocompletado y sus listeners sobre txtCodAlumno
    private void inicializarAutocompletado() {
        modeloSugerencias = new DefaultListModel<>();
        listaSugerencias  = new JList<>(modeloSugerencias);
        listaSugerencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSugerencias.setFont(txtCodAlumno.getFont());
        listaSugerencias.setVisibleRowCount(6);

        // Listener de clic sobre la lista de sugerencias
        listaSugerencias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                aplicarSeleccion();
            }
        });

        // Navegación y confirmación con teclado sobre el campo de código
        txtCodAlumno.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!popupSugerencias.isVisible()) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        int sig = listaSugerencias.getSelectedIndex() + 1;
                        if (sig < modeloSugerencias.getSize()) {
                            listaSugerencias.setSelectedIndex(sig);
                            listaSugerencias.ensureIndexIsVisible(sig);
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_UP:
                        int ant = listaSugerencias.getSelectedIndex() - 1;
                        if (ant >= 0) {
                            listaSugerencias.setSelectedIndex(ant);
                            listaSugerencias.ensureIndexIsVisible(ant);
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ENTER:
                        if (listaSugerencias.getSelectedIndex() >= 0) {
                            aplicarSeleccion();
                            e.consume();
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        popupSugerencias.setVisible(false);
                        e.consume();
                        break;
                }
            }
        });

        // Actualiza sugerencias en cada cambio de texto del campo
        txtCodAlumno.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { actualizarSugerencias(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizarSugerencias(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarSugerencias(); }
        });

        // Cierra el popup al perder el foco, con retardo para permitir clics
        txtCodAlumno.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!listaSugerencias.isFocusOwner()) {
                        popupSugerencias.setVisible(false);
                    }
                });
            }
        });

        // Configuración visual del popup
        JScrollPane scroll = new JScrollPane(listaSugerencias);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        popupSugerencias = new JPopupMenu();
        popupSugerencias.setFocusable(false); // mantiene el foco en txtCodAlumno
        popupSugerencias.add(scroll);
        popupSugerencias.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    // Filtra alumnos en estado Matriculado (1) según el texto ingresado y actualiza el popup
    private void actualizarSugerencias() {
        if (ignorarCambioTexto) return;

        String texto = txtCodAlumno.getText().trim();
        modeloSugerencias.clear();

        if (texto.isEmpty()) {
            popupSugerencias.setVisible(false);
            return;
        }

        // Recorre la lista y agrega solo alumnos matriculados cuyo código coincida
        for (int i = 0; i < listaAlumnos.longitud(); i++) {
            Alumno a = listaAlumnos.obtener(i);
            // Solo alumnos Matriculados (1) pueden retirarse
            if (a.getEstado() != 1) continue;
            if (String.valueOf(a.getCodigo()).contains(texto)) {
                modeloSugerencias.addElement(
                    a.getCodigo() + "  –  " + a.getNombres() + " " + a.getApellidos()
                );
            }
        }

        if (modeloSugerencias.isEmpty()) {
            popupSugerencias.setVisible(false);
            return;
        }

        // Selecciona automáticamente el primer ítem y muestra el popup
        listaSugerencias.setSelectedIndex(0);

        popupSugerencias.setPreferredSize(
            new Dimension(txtCodAlumno.getWidth(), 130)
        );
        if (!popupSugerencias.isVisible()) {
            popupSugerencias.show(txtCodAlumno, 0, txtCodAlumno.getHeight());
        } else {
            popupSugerencias.revalidate();
            popupSugerencias.repaint();
        }
    }

    // Extrae el código del ítem seleccionado, lo escribe en el campo y dispara la búsqueda
    private void aplicarSeleccion() {
        String seleccion = listaSugerencias.getSelectedValue();
        if (seleccion == null) return;

        String codigoStr = seleccion.split("–")[0].trim();

        ignorarCambioTexto = true;
        txtCodAlumno.setText(codigoStr);
        ignorarCambioTexto = false;

        popupSugerencias.setVisible(false);
        buscarAlumno(); 
    }

    // Habilita el campo de código y le otorga el foco para una nueva operación
    private void estadoInicial() {
    	txtCodAlumno.setEditable(true);
        txtCodAlumno.setBackground(Color.WHITE);
        txtCodAlumno.requestFocusInWindow();
    }

    // Busca un alumno por código y muestra su información y curso en el formulario
    private void buscarAlumno() {
        String texto = txtCodAlumno.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código del alumno.");
            return;
        }
        int cod;
        try {
            cod = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero.");
            txtInfoMatricula.setText("");
            return;
        }

        Alumno alumno = listaAlumnos.buscar(cod);
        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "Alumno no encontrado.");
            txtInfoMatricula.setText("");
            return;
        }

        // Determina el texto del estado del alumno
        String estadoStr;
        switch (alumno.getEstado()) {
            case 0:  estadoStr = "Registrado";  break;
            case 1:  estadoStr = "Matriculado"; break;
            case 2:  estadoStr = "Retirado";    break;
            default: estadoStr = "Desconocido"; break;
        }

        // Busca el curso asociado a la matrícula del alumno
        String cursoStr = "Sin curso";
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Matricula mat = listaMatriculas.obtener(i);
            if (mat.getCodAlumno() == cod) {
                Curso c = listaCursos.buscar(mat.getCodCurso());
                if (c != null) cursoStr = c.getAsignatura();
                break;
            }
        }

        txtInfoMatricula.setText(
            alumno.getNombres() + " " + alumno.getApellidos() +
            " | " + cursoStr + "  [" + estadoStr + "]"
        );
    }

    // Registra un nuevo retiro para el alumno indicado
    private void adicionar() {
        // FASE 1: Validar entrada del formulario
        String texto = txtCodAlumno.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código del alumno y presione Buscar.");
            return;
        }

        int cod;
        try {
            cod = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero válido.");
            return;
        }

        // FASE 2: Validar reglas de negocio
        Alumno alumno = listaAlumnos.buscar(cod);
        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "El alumno no se encuentra registrado en el sistema.");
            return;
        }
        if (alumno.getEstado() != 1) {
            JOptionPane.showMessageDialog(this,
                "El alumno debe estar en estado 'Matriculado' para efectuar un retiro.");
            return;
        }

        // Busca la matrícula activa del alumno
        Matricula m = null;
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Matricula mat = listaMatriculas.obtener(i);
            if (mat.getCodAlumno() == cod) { m = mat; break; }
        }
        if (m == null) {
            JOptionPane.showMessageDialog(this,
                "No se puede procesar: el alumno no cuenta con una matrícula registrada.");
            return;
        }

        // FASE 3: Registrar el retiro y actualizar el estado del alumno
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora  = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Retiro r = new Retiro(m.getNumMatricula(), fecha, hora);
        listaRetiros.adicionar(r);
        alumno.setEstado(2);

        // Persistir cambios y refrescar ambas vistas
        ArchivoRetiro.guardar(listaRetiros);
        ArchivoAlumno.guardar(listaAlumnos);    
        JOptionPane.showMessageDialog(this, "Retiro N° " + r.getNumRetiro() + " registrado con éxito.");
        if (ventanaMatricula != null) ventanaMatricula.refrescarTabla(); 
        limpiar();
        refrescarTabla();
    }

    // Elimina el retiro seleccionado y revierte el estado del alumno a Matriculado
    private void eliminar() {
        // FASE 1: Validar selección en la tabla
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un retiro de la tabla.");
            return;
        }

        int numRet = (int) modeloTabla.getValueAt(fila, 0);
        Retiro ret = listaRetiros.buscar(numRet);
        if (ret == null) return;

        // FASE 2: Validar que el alumno esté en estado Retirado
        Matricula m   = listaMatriculas.buscar(ret.getNumMatricula());
        Alumno alumno = (m != null) ? listaAlumnos.buscar(m.getCodAlumno()) : null;

        if (alumno == null || alumno.getEstado() != 2) {
            JOptionPane.showMessageDialog(this,
                "Solo se puede eliminar un retiro cuando el alumno está en estado 'Retirado'.");
            return;
        }

        // FASE 3: Confirmar y ejecutar la eliminación
        Object[] opciones = {"Eliminar", "Cancelar"};
        int conf = JOptionPane.showOptionDialog(this,
            "¿Eliminar definitivamente el Retiro N° " + numRet + "?\nEl alumno volverá a estar Matriculado.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);

        if (conf == JOptionPane.YES_OPTION) {
            int idx = listaRetiros.buscarIndice(numRet);
            if (idx >= 0) listaRetiros.eliminar(idx);
            alumno.setEstado(1);

            // Persistir cambios y refrescar ambas vistas
            ArchivoRetiro.guardar(listaRetiros);
            ArchivoAlumno.guardar(listaAlumnos);
            JOptionPane.showMessageDialog(this, "Retiro eliminado con éxito.");
            if (ventanaMatricula != null) ventanaMatricula.refrescarTabla();
            limpiar();
            refrescarTabla();
        }
    }

    // Persiste manualmente la lista de retiros y el estado de los alumnos
    private void grabar() {
        ArchivoRetiro.guardar(listaRetiros);
        ArchivoAlumno.guardar(listaAlumnos);    
        JOptionPane.showMessageDialog(this, "Historial de retiros guardado correctamente.");
    }

    // Limpia todos los campos del formulario y deselecciona la tabla
    private void limpiar() {
        txtNumRetiro.setText("");
        ignorarCambioTexto = true;
        txtCodAlumno.setText("");
        ignorarCambioTexto = false;
        txtInfoMatricula.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        popupSugerencias.setVisible(false);
        tabla.clearSelection();
        estadoInicial();
    }

    // Carga los datos de la fila seleccionada en los campos del formulario
    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        txtNumRetiro.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        txtHora.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
        txtInfoMatricula.setText(
            modeloTabla.getValueAt(fila, 2) + " | " + modeloTabla.getValueAt(fila, 3)
        );

        // Deshabilita el campo de código al seleccionar un registro existente
        txtCodAlumno.setEditable(false);
        txtCodAlumno.setBackground(new Color(230, 230, 230));

        ignorarCambioTexto = true;
        txtCodAlumno.setText("");
        ignorarCambioTexto = false;

        popupSugerencias.setVisible(false);
    }

    // Recarga la tabla con los datos actuales de la lista de retiros
    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < listaRetiros.longitud(); i++) {
            Retiro    r = listaRetiros.obtener(i);
            Matricula m = listaMatriculas.buscar(r.getNumMatricula());

            // Resuelve el nombre del alumno y el curso desde la matrícula asociada
            String nomAlumno = "?", nomCurso = "?";
            if (m != null) {
                Alumno a = listaAlumnos.buscar(m.getCodAlumno());
                Curso  c = listaCursos.buscar(m.getCodCurso());
                if (a != null) nomAlumno = a.getNombres() + " " + a.getApellidos();
                if (c != null) nomCurso  = c.getAsignatura();
            } else {
                nomAlumno = "Matrícula N° " + r.getNumMatricula();
            }

            modeloTabla.addRow(new Object[]{
                r.getNumRetiro(), r.getNumMatricula(), nomAlumno, nomCurso, r.getFecha(), r.getHora()
            });
        }
    }
    
    // Ajusta a Retirado (2) a los alumnos que tienen un retiro activo en el archivo
    private void sincronizarEstadosDesdeRetiros() {
        for (int i = 0; i < listaRetiros.longitud(); i++) {
            Matricula m = listaMatriculas.buscar(listaRetiros.obtener(i).getNumMatricula());
            if (m != null) {
                Alumno a = listaAlumnos.buscar(m.getCodAlumno());
                if (a != null) a.setEstado(2);
            }
        }
    }
    
    // Resetea todos los alumnos a Registrado (0) y marca como Matriculado (1) a quienes tienen matrícula
    private void sincronizarEstadosDesdeMatriculas() {
        for (int i = 0; i < listaAlumnos.longitud(); i++) {
            listaAlumnos.obtener(i).setEstado(0);
        }
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Alumno a = listaAlumnos.buscar(listaMatriculas.obtener(i).getCodAlumno());
            if (a != null) a.setEstado(1);
        }
    }
}