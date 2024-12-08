package vista;

import controlador.ControladorEmpresas;
import excepciones.SVPExepction;
import utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;

public class GUICreacionDeBus extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField patenteField;
    private JTextField marcaField;
    private JTextField modeloField;
    private JTextField nroAsientosField;
    private JComboBox rutComboBox;
    private JComboBox nombreComboBox;
    private JLabel patenteLabel;
    private JLabel marcaLabel;
    private JLabel modeloLabel;
    private JLabel nroAsientosLabel;
    private JLabel rutLabel;
    private JLabel nombreLabel;

    public GUICreacionDeBus() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();
        String [] rutEmpresas = new String[empresas.length];
        String [] nombreEmpresas = new String[empresas.length];

        for(int i = 0; i < empresas.length; i++){
            rutEmpresas[i] = empresas[i][0];
            nombreEmpresas[i] = empresas[i][1];
        }

        nombreComboBox.setModel(new DefaultComboBoxModel(nombreEmpresas));
        rutComboBox.setModel(new DefaultComboBoxModel(rutEmpresas));

        rutComboBox.addActionListener(e -> {
            int index = rutComboBox.getSelectedIndex();
            if (index >= 0 && index < nombreEmpresas.length) {
                nombreComboBox.setSelectedIndex(index);
            }
        });

        nombreComboBox.addActionListener(e -> {
            int index = nombreComboBox.getSelectedIndex();
            if (index >= 0 && index < nombreEmpresas.length) {
                rutComboBox.setSelectedIndex(index);
            }
        });


        buttonOK.addActionListener(e -> {
            String patente = patenteField.getText().trim();
            String marca = marcaField.getText().trim();
            String modelo = modeloField.getText().trim();
            String nroAsientosString = nroAsientosField.getText().trim();

            if(patente.isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese una patente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!patente.matches("^([a-zA-Z]{4})([\\-| ]?)([0-9]{2})?$")) {
                JOptionPane.showMessageDialog(this, "Patente no valida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(marca.isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese una marca", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!marca.matches("^[a-zA-Z ]+$")){
                JOptionPane.showMessageDialog(this, "Marca no valida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(modelo.isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese un modelo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!modelo.matches("^[a-zA-Z0-9 ]+$")){
                JOptionPane.showMessageDialog(this, "Modelo no valida", "Error", JOptionPane.ERROR_MESSAGE);
            }

            int nroAsientos;
            try {
                nroAsientos = Integer.parseInt(nroAsientosString);
                if (nroAsientos <= 0) {
                    JOptionPane.showMessageDialog(this, "El numero de asientos debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un numero de asientos valido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            String rutEmpresaAux = rutComboBox.getSelectedItem().toString();
            try {
                ControladorEmpresas.getInstance().createBus(patente, modelo, marca, nroAsientos, Rut.of(rutEmpresaAux));
                JOptionPane.showMessageDialog(this, "Bus guardado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SVPExepction exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        });

        rutComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String rutEmpresas = rutComboBox.getSelectedItem().toString();

                for (int i = 0; i< empresas.length; i++){
                    if(rutEmpresas.equals(empresas[i][0])){
                        nombreComboBox.setSelectedIndex(i);
                    }
                }
            }
        });

        nombreComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String nombreEmpresas = nombreComboBox.getSelectedItem().toString();

                for (int i = 0; i< empresas.length; i++){
                    if(nombreEmpresas.equals(empresas[i][0])){
                        rutComboBox.setSelectedIndex(i);
                    }
                }
            }
        });


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        GUICreacionDeBus dialog = new GUICreacionDeBus();
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
