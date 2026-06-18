package forms;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import arreglos.aAlumnos;
import clases.Alumno;
import clases.ArchivoAlumno;

public class frmAlumnos extends javax.swing.JInternalFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Componentes de la interfaz
    private JPanel contentPane;
    private JTextField txtCodigo, txtDni, txtNombres, txtApellidos, txtCarrera, txtEdad, txtCelular;
    private JButton btnLimpiar, btnAgregar, btnBuscar, btnEliminar, btnModificar;
    private JTable tbAlumnos;

    // Lista compartida de alumnos accesible desde otras formas
    public static aAlumnos misAlumnos = new aAlumnos();

    // Constructor: inicializa la ventana, construye los componentes y carga el listado
    public frmAlumnos() {
        super("Mantenimiento de Alumnos",true,true,true,true);
        setBounds(100, 100, 674, 556);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Etiquetas y campos del formulario
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(33, 36, 60, 12);
        contentPane.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(105, 33, 96, 18);
        contentPane.add(txtCodigo);

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setBounds(242, 33, 44, 12);
        contentPane.add(lblDni);

        txtDni = new JTextField();
        txtDni.setBounds(314, 30, 96, 18);
        contentPane.add(txtDni);

        JLabel lblNombres = new JLabel("Nombres:");
        lblNombres.setBounds(33, 73, 60, 12);
        contentPane.add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setBounds(105, 70, 96, 18);
        contentPane.add(txtNombres);

        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setBounds(242, 70, 60, 12);
        contentPane.add(lblApellidos);

        txtApellidos = new JTextField();
        txtApellidos.setBounds(314, 67, 96, 18);
        contentPane.add(txtApellidos);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(33, 112, 44, 12);
        contentPane.add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setBounds(105, 109, 96, 18);
        contentPane.add(txtEdad);

        JLabel lblCelular = new JLabel("Celular:");
        lblCelular.setBounds(242, 108, 44, 12);
        contentPane.add(lblCelular);

        txtCelular = new JTextField();
        txtCelular.setBounds(313, 106, 96, 18);
        contentPane.add(txtCelular);

        JLabel lblCarrera = new JLabel("Carrera:");
        lblCarrera.setBounds(33, 162, 60, 12);
        contentPane.add(lblCarrera);

        txtCarrera = new JTextField();
        txtCarrera.setBounds(105, 159, 220, 18);
        contentPane.add(txtCarrera);

        // Botones de acción con sus listeners
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(this);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBounds(513, 31, 93, 20);
        contentPane.add(btnLimpiar);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(this);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBounds(513, 68, 93, 20);
        contentPane.add(btnAgregar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBounds(513, 104, 93, 20);
        contentPane.add(btnBuscar);

        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(this);
        btnModificar.setFocusPainted(false);
        btnModificar.setBounds(513, 145, 93, 20);
        contentPane.add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(this);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBounds(513, 184, 93, 20);
        contentPane.add(btnEliminar);

        // Tabla de listado de alumnos
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(33, 252, 584, 257);
        contentPane.add(scrollPane);

        tbAlumnos = new JTable();
        tbAlumnos.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[]{"Código", "DNI", "Nombres", "Apellidos", "Edad", "Carrera", "Celular", "Estado"}
        ));
        scrollPane.setViewportView(tbAlumnos);

        // Carga inicial del listado
        listado();
    }

    // Muestra todos los alumnos en la tabla
    public void listado() {
        DefaultTableModel modelo = (DefaultTableModel) tbAlumnos.getModel();
        modelo.setRowCount(0);
        for (int i = 0; i < misAlumnos.longitud(); i++) {
            Alumno a = misAlumnos.obtener(i);
            // Convierte el estado numérico a texto para que sea legible en la tabla
            String estadoStr;
            switch (a.getEstado()) {
                case 0:  estadoStr = "Registrado";  break;
                case 1:  estadoStr = "Matriculado"; break;
                case 2:  estadoStr = "Retirado";    break;
                default: estadoStr = "Desconocido"; break;
            }
            modelo.addRow(new Object[]{
                a.getCodigo(), a.getDni(), a.getNombres(), a.getApellidos(),
                a.getEdad(), a.getCarrera(), a.getCelular(), estadoStr
            });
        }
    }

    // Despacha las acciones de los botones y persiste los cambios cuando corresponde
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar)  actionPerformedBtnAgregar(e);
        ArchivoAlumno.guardar(misAlumnos);
        if (e.getSource() == btnBuscar)   actionPerformedBtnBuscar(e);
        if (e.getSource() == btnModificar)actionPerformedBtnModificar(e);
        ArchivoAlumno.guardar(misAlumnos);
        if (e.getSource() == btnEliminar) actionPerformedBtnEliminar(e);
        ArchivoAlumno.guardar(misAlumnos);
        if (e.getSource() == btnLimpiar)  limpiar();
    }

    // Crea un nuevo alumno con los datos del formulario y lo agrega a la lista
    protected void actionPerformedBtnAgregar(ActionEvent e) {
        try {
            Alumno a = new Alumno(
                txtDni.getText().trim(),
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtCarrera.getText().trim(),
                Integer.parseInt(txtCelular.getText().trim()),
                Integer.parseInt(txtEdad.getText().trim()),
                0 // Estado inicial siempre Registrado
            );
            String mensaje = misAlumnos.adicionar(a);
            JOptionPane.showMessageDialog(null, mensaje);
            if (mensaje.startsWith("Alumno registrado")) {
                listado();
                limpiar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Edad y celular deben ser valores numéricos.");
        }
    }

    // Busca un alumno por código y carga sus datos en el formulario
    protected void actionPerformedBtnBuscar(ActionEvent e) {
        try {
            // El código del alumno es del formato 2020100001
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            Alumno a = misAlumnos.buscar(codigo);
            if (a != null) {
                txtDni.setText(a.getDni());
                txtNombres.setText(a.getNombres());
                txtApellidos.setText(a.getApellidos());
                txtCarrera.setText(a.getCarrera());
                txtEdad.setText(String.valueOf(a.getEdad()));
                txtCelular.setText(String.valueOf(a.getCelular()));
            } else {
                JOptionPane.showMessageDialog(null, "Alumno no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingrese un código numérico válido.");
        }
    }

    // Modifica los datos del alumno encontrado con los valores actuales del formulario
    protected void actionPerformedBtnModificar(ActionEvent e) {
        try {
            // FASE 1: Validar que exista un alumno seleccionado
            if (txtCodigo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Primero busque un alumno para modificarlo.");
                return;
            }
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String nombreCompleto = txtNombres.getText().trim() + " " + txtApellidos.getText().trim();

            // FASE 2: Confirmar la operación con el usuario
            Object[] opciones = {"Guardar", "Cancelar"};
            int resp = JOptionPane.showOptionDialog(null,
                "¿Guardar cambios del alumno " + nombreCompleto + "?",
                "Confirmar modificación", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (resp != JOptionPane.YES_OPTION) return;

            // FASE 3: Actualizar los campos del alumno en la lista
            for (int i = 0; i < misAlumnos.longitud(); i++) {
                if (misAlumnos.obtener(i).getCodigo() == codigo) {
                    misAlumnos.obtener(i).setNombres(txtNombres.getText().trim());
                    misAlumnos.obtener(i).setApellidos(txtApellidos.getText().trim());
                    misAlumnos.obtener(i).setCarrera(txtCarrera.getText().trim());
                    misAlumnos.obtener(i).setEdad(Integer.parseInt(txtEdad.getText().trim()));
                    misAlumnos.obtener(i).setCelular(Integer.parseInt(txtCelular.getText().trim()));
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, "Registro modificado con éxito.");
            listado();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en los datos. Verifique los campos numéricos.");
        }
    }

    // Elimina un alumno de la lista previa validación de su estado
    protected void actionPerformedBtnEliminar(ActionEvent e) {
        try {
            // FASE 1: Solicitar el código del alumno a eliminar
            String input = JOptionPane.showInputDialog(this,
                "Ingrese el código del alumno a eliminar:", "Eliminar Alumno",
                JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) return;

            int codigo = Integer.parseInt(input.trim());
            Alumno alumno = misAlumnos.buscar(codigo);

            // FASE 2: Validar existencia y estado del alumno
            if (alumno == null) {
                JOptionPane.showMessageDialog(this, "Código " + codigo + " no registrado.");
                return;
            }
            if (alumno.getEstado() == 1) {
                JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el alumno está Matriculado.",
                    "Eliminación bloqueada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (alumno.getEstado() == 2) {
                JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el alumno está Retirado.",
                    "Eliminación bloqueada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // FASE 3: Confirmar y ejecutar la eliminación
            Object[] opciones = {"Eliminar", "Cancelar"};
            int resp = JOptionPane.showOptionDialog(this,
                "¿Eliminar a " + alumno.getNombres() + " " + alumno.getApellidos() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (resp != JOptionPane.YES_OPTION) return;

            for (int i = 0; i < misAlumnos.longitud(); i++) {
                if (misAlumnos.obtener(i).getCodigo() == codigo) {
                    String msg = misAlumnos.eliminar(i);
                    JOptionPane.showMessageDialog(this, msg);
                    listado();
                    limpiar();
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código debe ser un valor numérico entero.");
        }
    }

    // Limpia todos los campos del formulario
    public void limpiar() {
        txtCodigo.setText("");
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEdad.setText("");
        txtCelular.setText("");
        txtCarrera.setText("");
    }
}