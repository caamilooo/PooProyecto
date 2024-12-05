package vista;

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

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String patente = patenteField.getText();
                String marca = marcaField.getText();
                String modelo = modeloField.getText();
                int nroAsientos = Integer.parseInt(nroAsientosField.getText());
                String rut = rutComboBox.getSelectedItem().toString();
                String nombre = nombreComboBox.getSelectedItem().toString();

                if (patente.isEmpty()){
                    JOptionPane.showMessageDialog(null, "La patente es obligatoria");
                    return;
                }
                if (marca.isEmpty()){
                    JOptionPane.showMessageDialog(null, "La marca es obligatoria");
                }
                if (modelo.isEmpty()){
                    JOptionPane.showMessageDialog(null, "El modelo es obligatorio");
                }
                if (nroAsientos.isEmpty()){
                    JOptionPane.showMessageDialog(null, "El numero de asientos es obligatorio");
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

    public static void main(String[] args) {
        GUICreacionDeBus dialog = new GUICreacionDeBus();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
