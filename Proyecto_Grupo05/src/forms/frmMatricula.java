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

public class frmMatricula extends JInternalFrame {

    // Listas de datos compartidas con el sistema
    private aAlumnos   listaAlumnos;
    private aCurso     listaCursos;
    private aMatricula listaMatriculas;

    // Componentes del formulario
    private JTextField txtNumMatricula;
    private JTextField txtCodAlumno;
    private JTextField txtNombreAlumno;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JComboBox<String> cboCurso;

    // Tabla y su modelo de datos
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Botones de acción
    private JButton btnBuscar;
    private JButton btnAdicionar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnGrabar;
    private JButton btnLimpiar;

    // Componentes del autocompletado
    private JPopupMenu     popupSugerencias;
    private JList<String>  listaSugerencias;
    private DefaultListModel<String> modeloSugerencias;
    private aRetiro listaRetiros;
    // Bandera para evitar que actualizaciones programáticas disparen el DocumentListener
    private boolean ignorarCambioTexto = false;

    // Constructor: inicializa la ventana y carga los datos persistidos
    public frmMatricula(aAlumnos listaAlumnos, aCurso listaCursos, aMatricula listaMatriculas) {
        super("Matrícula", true, true, true, true);
        this.listaAlumnos    = listaAlumnos;
        this.listaCursos     = listaCursos;
        this.listaMatriculas = listaMatriculas;
        this.listaRetiros    = new aRetiro(); 

        setBounds(50, 50, 850, 530);
        setLayout(new BorderLayout());

        // Construcción de los paneles de la interfaz
        add(construirFormulario(), BorderLayout.NORTH);
        add(construirTabla(),      BorderLayout.CENTER);
        add(construirBotones(),    BorderLayout.SOUTH);

        inicializarAutocompletado();
        
        // Carga de datos desde archivos persistidos
        listaMatriculas.limpiar();
        ArchivoMatricula.cargar(listaMatriculas);
        
        this.listaRetiros.limpiar();
        clases.ArchivoRetiro.cargar(listaRetiros);
        
        // Sincronización de estados y refresco inicial de la tabla
        sincronizarEstadosDesdeMatriculas();
        refrescarTabla();

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

    // Filtra alumnos en estado Registrado (0) según el texto ingresado y actualiza el popup
    private void actualizarSugerencias() {
        if (ignorarCambioTexto) return;

        String texto = txtCodAlumno.getText().trim();
        modeloSugerencias.clear();

        if (texto.isEmpty()) {
            popupSugerencias.setVisible(false);
            return;
        }

        // Recorre la lista y agrega solo alumnos registrados cuyo código coincida
        for (int i = 0; i < listaAlumnos.longitud(); i++) {
            Alumno a = listaAlumnos.obtener(i);
            // Solo alumnos en estado Registrado pueden matricularse
            if (a.getEstado() != 0) continue;
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
            new Dimension(txtCodAlumno.getWidth() + 20, 150)
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

        // Formato del ítem: "2020100001  –  Nombres Apellidos"
        String codigoStr = seleccion.split("–")[0].trim();

        ignorarCambioTexto = true;         
        txtCodAlumno.setText(codigoStr);
        ignorarCambioTexto = false;

        popupSugerencias.setVisible(false);
        buscarAlumno();                     
    }

    // Construye el panel superior con los campos del formulario de matrícula
    private JPanel construirFormulario() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Fila 0: N° Matrícula | Fecha | Hora
        gbc.gridy = 0;
        gbc.gridx = 0; pnlForm.add(new JLabel("N° Matrícula:"), gbc);
        txtNumMatricula = new JTextField(10);
        txtNumMatricula.setEditable(false);
        gbc.gridx = 1; pnlForm.add(txtNumMatricula, gbc);

        gbc.gridx = 2; pnlForm.add(new JLabel("Fecha:"), gbc);
        txtFecha = new JTextField(10);
        txtFecha.setEditable(false);
        gbc.gridx = 3; pnlForm.add(txtFecha, gbc);

        gbc.gridx = 4; pnlForm.add(new JLabel("Hora:"), gbc);
        txtHora = new JTextField(10);
        txtHora.setEditable(false);
        gbc.gridx = 5; pnlForm.add(txtHora, gbc);

        // Fila 1: Cód. Alumno | Buscar | Nombre
        gbc.gridy = 1;
        gbc.gridx = 0; pnlForm.add(new JLabel("Cód. Alumno:"), gbc);
        txtCodAlumno = new JTextField(10);
        gbc.gridx = 1; pnlForm.add(txtCodAlumno, gbc);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarAlumno());
        btnBuscar.setFocusPainted(false);
        gbc.gridx = 2; pnlForm.add(btnBuscar, gbc);

        gbc.gridx = 3; pnlForm.add(new JLabel("Nombre:"), gbc);
        txtNombreAlumno = new JTextField(22);
        txtNombreAlumno.setEditable(false);
        gbc.gridx = 4; gbc.gridwidth = 2;
        pnlForm.add(txtNombreAlumno, gbc);
        gbc.gridwidth = 1;

        // Fila 2: Curso
        gbc.gridy = 2;
        gbc.gridx = 0; pnlForm.add(new JLabel("Curso:"), gbc);
        cboCurso = new JComboBox<>();
        cargarComboCursos();
        gbc.gridx = 1; gbc.gridwidth = 5;
        pnlForm.add(cboCurso, gbc);
        gbc.gridwidth = 1;

        return pnlForm;
    }

    // Construye la tabla con su modelo de datos de solo lectura
    private JScrollPane construirTabla() {
        String[] columnas = {
            "N° Matrícula", "Cód. Alumno", "Alumno", "Curso", "Fecha", "Hora", "Estado"
        };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarFilaSeleccionada());
        return new JScrollPane(tabla);
    }

    // Construye el panel inferior con los botones de acción y sus listeners
    private JPanel construirBotones() {
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setFocusPainted(false);
        btnModificar = new JButton("Modificar Curso");
        btnModificar.setFocusPainted(false);
        btnEliminar  = new JButton("Eliminar");
        btnEliminar.setFocusPainted(false);
        btnGrabar    = new JButton("Grabar");
        btnGrabar.setFocusPainted(false);
        btnLimpiar   = new JButton("Limpiar");
        btnLimpiar.setFocusPainted(false);

        pnlBotones.add(btnAdicionar);
        pnlBotones.add(btnModificar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnGrabar);
        pnlBotones.add(btnLimpiar);

        btnAdicionar.addActionListener(e -> adicionar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e  -> eliminar());
        btnGrabar.addActionListener(e    -> grabar());
        btnLimpiar.addActionListener(e   -> limpiar());

        return pnlBotones;
    }

    // Busca un alumno por código y muestra su nombre y estado en el formulario
    private void buscarAlumno() {
        String texto = txtCodAlumno.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código del alumno.");
            return;
        }
        try {
            int    cod = Integer.parseInt(texto);
            Alumno a   = listaAlumnos.buscar(cod);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "Alumno no encontrado.");
                txtNombreAlumno.setText("");
                return;
            }
            String estadoStr;
            switch (a.getEstado()) {
                case 0:  estadoStr = "Registrado";  break;
                case 1:  estadoStr = "Matriculado"; break;
                case 2:  estadoStr = "Retirado";    break;
                default: estadoStr = "Desconocido"; break;
            }
            txtNombreAlumno.setText(
                a.getNombres() + " " + a.getApellidos() + "  [" + estadoStr + "]"
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero.");
            txtNombreAlumno.setText("");
        }
    }

    // Carga los cursos disponibles en el combo desplegable
    private void cargarComboCursos() {
        cboCurso.removeAllItems();
        for (int i = 0; i < listaCursos.longitud(); i++) {
            Curso c = listaCursos.obtener(i);
            cboCurso.addItem(c.getCodCurso() + " - " + c.getAsignatura());
        }
    }

    // Registra una nueva matrícula para el alumno indicado
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
            JOptionPane.showMessageDialog(this, "Alumno no registrado en el sistema.");
            return;
        }
        if (alumno.getEstado() != 0) {
            String estadoActual;
            switch (alumno.getEstado()) {
                case 1:  estadoActual = "Matriculado"; break;
                case 2:  estadoActual = "Retirado";    break;
                default: estadoActual = "Desconocido"; break;
            }
            JOptionPane.showMessageDialog(this,
                "Solo se puede matricular un alumno en estado 'Registrado'.\n" +
                "Estado actual: " + estadoActual);
            return;
        }
        if (listaMatriculas.alumnoYaMatriculado(alumno)) {
            JOptionPane.showMessageDialog(this, "Este alumno ya tiene una matrícula activa.");
            return;
        }

        // FASE 3: Registrar la matrícula y actualizar el estado del alumno
        int    codCurso = obtenerCodCursoSeleccionado();
        String fecha    = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora     = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Matricula m = new Matricula(cod, codCurso, fecha, hora);
        listaMatriculas.adicionar(m);
        alumno.setEstado(1);

        // Persistir cambios y refrescar la vista
        ArchivoMatricula.guardar(listaMatriculas);
        ArchivoAlumno.guardar(listaAlumnos);    
        JOptionPane.showMessageDialog(this,
            "Matrícula N° " + m.getNumMatricula() + " registrada.\n" +
            "Alumno: " + alumno.getNombres() + " " + alumno.getApellidos() + "\n" +
            "Código alumno: " + cod);
        limpiar();
        refrescarTabla();
    }

    // Modifica el curso asignado a la matrícula seleccionada
    private void modificar() {
        // FASE 1: Validar selección en la tabla
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una matrícula de la tabla.");
            return;
        }

        int numMat = (int) modeloTabla.getValueAt(fila, 0);
        Matricula m = listaMatriculas.buscar(numMat);
        if (m == null) return;

        // FASE 2: Validar que el alumno pueda ser modificado
        Alumno alumno = listaAlumnos.buscar(m.getCodAlumno());
        if (alumno == null || alumno.getEstado() == 0) {
            JOptionPane.showMessageDialog(this, "No se puede modificar la matrícula de un alumno no activo.");
            return;
        }

        // FASE 3: Actualizar el curso y persistir el cambio
        m.setCodCurso(obtenerCodCursoSeleccionado());
        ArchivoMatricula.guardar(listaMatriculas);
        JOptionPane.showMessageDialog(this, "Curso actualizado correctamente.");
        limpiar();
        refrescarTabla();
    } 

    // Elimina la matrícula seleccionada y revierte el estado del alumno a Registrado
    private void eliminar() {
        // FASE 1: Validar selección en la tabla
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione la matrícula a eliminar.");
            return;
        }

        int       numMat = (int) modeloTabla.getValueAt(fila, 0);
        Matricula m      = listaMatriculas.buscar(numMat);
        if (m == null) return;

        // FASE 2: Validar regla de negocio (no eliminar si el alumno está retirado)
        Alumno alumno = listaAlumnos.buscar(m.getCodAlumno());
        if (alumno != null && alumno.getEstado() == 2) {
            JOptionPane.showMessageDialog(this,
                "No se puede eliminar: el alumno de esta matrícula está en estado 'Retirado'.",
                "Eliminación bloqueada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // FASE 3: Confirmar y ejecutar la eliminación
        Object[] opciones = {"Eliminar", "Cancelar"};
        int conf = JOptionPane.showOptionDialog(this,
            "¿Eliminar definitivamente la matrícula N° " + numMat + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);

        if (conf == JOptionPane.YES_OPTION) {
            int idx = listaMatriculas.buscarIndice(numMat);
            if (idx >= 0) listaMatriculas.eliminar(idx);
            if (alumno != null) alumno.setEstado(0);

            ArchivoMatricula.guardar(listaMatriculas);
            ArchivoAlumno.guardar(listaAlumnos);
            JOptionPane.showMessageDialog(this,
                "Matrícula eliminada. El alumno vuelve a estado 'Registrado'.");
            limpiar();
            refrescarTabla();
        }
    }

    // Persiste manualmente la lista de matrículas en el archivo
    private void grabar() {
        ArchivoMatricula.guardar(listaMatriculas);
        JOptionPane.showMessageDialog(this, "Datos guardados correctamente.");
    }

    // Limpia todos los campos del formulario y deselecciona la tabla
    private void limpiar() {
        txtNumMatricula.setText("");
        ignorarCambioTexto = true;
        txtCodAlumno.setText("");
        ignorarCambioTexto = false;
        txtNombreAlumno.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        if (cboCurso.getItemCount() > 0) cboCurso.setSelectedIndex(0);
        popupSugerencias.setVisible(false);
        tabla.clearSelection();
    }

    // Carga los datos de la fila seleccionada en los campos del formulario
    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        txtNumMatricula.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));

        ignorarCambioTexto = true;
        txtCodAlumno.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        ignorarCambioTexto = false;

        txtNombreAlumno.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        txtHora.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));

        // Sincroniza el combo de cursos con el curso de la fila seleccionada
        String cursoEnTabla = String.valueOf(modeloTabla.getValueAt(fila, 3));
        for (int i = 0; i < cboCurso.getItemCount(); i++) {
            if (cboCurso.getItemAt(i).contains(cursoEnTabla)) {
                cboCurso.setSelectedIndex(i);
                break;
            }
        }
    }

    // Recarga la tabla con los datos actuales de la lista de matrículas
    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Matricula m = listaMatriculas.obtener(i);
            Alumno    a = listaAlumnos.buscar(m.getCodAlumno());
            Curso     c = listaCursos.buscar(m.getCodCurso());

            String nomAlumno = (a != null)
                ? a.getNombres() + " " + a.getApellidos()
                : "Cód: " + m.getCodAlumno();
            String nomCurso  = (c != null) ? c.getAsignatura() : "Cód: " + m.getCodCurso();

            // Determina el estado visible de la matrícula según el estado del alumno
            String estadoMat;
            if (a == null)              estadoMat = "Desconocido";
            else if (a.getEstado() == 2) estadoMat = "Retirado";
            else                         estadoMat = "Activa";

            modeloTabla.addRow(new Object[]{
                m.getNumMatricula(), m.getCodAlumno(), nomAlumno,
                nomCurso, m.getFecha(), m.getHora(), estadoMat
            });
        }
    }

    // Obtiene el código del curso actualmente seleccionado en el combo
    private int obtenerCodCursoSeleccionado() {
        String item = (String) cboCurso.getSelectedItem();
        return Integer.parseInt(item.split(" - ")[0].trim());
    }
    
    // Sincroniza el estado en memoria de cada alumno cruzando matrículas y retiros persistidos
    private void sincronizarEstadosDesdeMatriculas() {
        // FASE 1: Resetear todos los alumnos a estado Registrado (0)
        for (int i = 0; i < listaAlumnos.longitud(); i++) {
            listaAlumnos.obtener(i).setEstado(0);
        }
        
        // FASE 2: Marcar como Matriculado (1) a quienes tienen matrícula registrada
        for (int i = 0; i < listaMatriculas.longitud(); i++) {
            Alumno a = listaAlumnos.buscar(listaMatriculas.obtener(i).getCodAlumno());
            if (a != null) a.setEstado(1);
        }
        
        // FASE 3: Marcar como Retirado (2) a quienes tienen un retiro activo
        for (int i = 0; i < listaRetiros.longitud(); i++) {
            // Usamos tu método .obtener(i) de la clase aRetiro
            Retiro ret = listaRetiros.obtener(i); 
            
            // Buscamos la matrícula correspondiente al retiro
            Matricula mat = listaMatriculas.buscar(ret.getNumMatricula());
            
            if (mat != null) {
                // Si la matrícula existe, encontramos al alumno y cambiamos su estado a 2
                Alumno a = listaAlumnos.buscar(mat.getCodAlumno());
                if (a != null) {
                    a.setEstado(2); // Ponemos el estado correcto en memoria
                }
            }
        }
    }
}