package vista;

import controlador.ControladorEmpresas;
import excepciones.SVPExepction;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class GUIListarVentasEmpresas extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox rutComboBox;
    private JComboBox nombreComboBox;
    private JTable tabla;
    private JLabel rutJLabel;
    private JLabel nombreJLabel;
    private JLabel Titulo;
    private JScrollPane scrollPane;

    public GUIListarVentasEmpresas() throws SVPExepction {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();

        if(empresas == null || empresas.length == 0) {
            throw new SVPExepction("No se encuentran empresas registradas en el sistema");
        }

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

            String rutEmpresa = rutComboBox.getSelectedItem().toString();
            String [][] ventasEmpresas = ControladorEmpresas.getInstance().listVentasEmpresa(Rut.of(rutEmpresa));

            String [] columnas = {"FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO"};

            if(ventasEmpresas == null || ventasEmpresas.length == 0){
                tabla.setModel(new DefaultTableModel(new Object[0][0], columnas));
                JOptionPane.showMessageDialog(this, "La empresa no tiene ventas registradas en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            tabla.setModel(new DefaultTableModel(ventasEmpresas, columnas));
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

    public static void display(){
        GUIListarVentasEmpresas dialog = new GUIListarVentasEmpresas();
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
