package vista;

import controlador.ControladorEmpresas;
import excepciones.SVPExepction;
import utilidades.Rut;

import javax.swing.*;

public class GUICreaEmpresa extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField RUTText;
    private JTextField NomText;
    private JTextField URLText;
    private JLabel RUTLabel;
    private JLabel NombreLabel;
    private JLabel UrlLabel;
    private JLabel TituloLabel;
    private JPanel panelDatos;
    private JPanel CreacionPanel;

    public GUICreaEmpresa() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);




        buttonOK.addActionListener(e -> {
            String rutString = RUTText.getText().trim();
            String nombre = NomText.getText().trim();
            String url = URLText.getText().trim();

            Rut rut =Rut.of(rutString);

            if (rutString.isEmpty()){
                JOptionPane.showMessageDialog(this, "El R.U.T es obligatorio");
                return;
            }

            if (!Rut.esValido(rutString)) {
                JOptionPane.showMessageDialog(this, "R.U.T no valido.");
                return;
            }

            if (nombre.isEmpty()){
                JOptionPane.showMessageDialog(this, "El Nombre es obligatorio");
                return;
            }

            if (url.isEmpty()){
                JOptionPane.showMessageDialog(this, "El URL es obligatorio");
                return;
            }

            if (!urlvalido(url)) {
                JOptionPane.showMessageDialog(this, "URL no valido.");
                return;
            }

            try {
                ControladorEmpresas.getInstance().createEmpresa(rut, nombre, url);
                JOptionPane.showMessageDialog(this, "Empresa guardada exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (SVPExepction exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }


            RUTText.setText("");
            NomText.setText("");
            URLText.setText("");

            dispose();
        });

        buttonCancel.addActionListener(e -> dispose());
        setLocationRelativeTo(null);
        }

        private boolean urlvalido(String url) {
            return url.matches("^(http|https)://[a-zA-Z0-9-_.]+\\.[a-zA-Z]+.*$");
        }

    public static void main(String[] args) {
        GUICreaEmpresa dialog = new GUICreaEmpresa();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
