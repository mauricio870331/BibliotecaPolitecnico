/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.*;
import App.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio
 */
public final class EditorialController implements ActionListener, KeyListener {

    int idToUpdate = 0;
    int id_rol;
    int rol = 3;
    int id_gym;
    int pagina = 1;
    String dato = "";
    Principal pr;
    EditorialDAO editorialdao = new EditorialDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int countAction = 0;
    JFileChooser FileChooser = new JFileChooser();
    String foto = "";
    boolean clientes = true;
    boolean admin = false;
    boolean secretaria = false;
    int idEditorial = 0;
    AddEditorial ae;

    public EditorialController(Principal pr) {
        ae = new AddEditorial(null, true);
        this.pr = pr;
        this.pr.btnAddEditorial.addActionListener(this);
        ae.btnCreateEditorial.addActionListener(this);
        ae.mnuUpdateEditorial.addActionListener(this);
        ae.mnuDeleteEditorial.addActionListener(this);
        ae.txtFindEditorial.addKeyListener(this);
    }

    public void cargarEditorial(JTable tbEditorial, String dato) {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Editorial> nombreIterator = editorialdao.getListEditorial(dato).iterator();
        while (nombreIterator.hasNext()) {
            Editorial e = nombreIterator.next();
            columna[0] = e.getIdEditorial();
            columna[1] = e.getNombre();
            modelo.addRow(columna);
        }
        tbEditorial.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbEditorial.setRowSorter(ordenar);
        tbEditorial.getColumnModel().getColumn(0).setMaxWidth(0);
        tbEditorial.getColumnModel().getColumn(0).setMinWidth(0);
        tbEditorial.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbEditorial.setModel(modelo);
    }

    public void cargarCboEditorial() {
        pr.cboEditorial.removeAllItems();
        Iterator<Editorial> editorial = editorialdao.getListEditorial("").iterator();
        pr.cboEditorial.addItem("-- Seleccione --");
        while (editorial.hasNext()) {
            Editorial elemento = editorial.next();
            pr.cboEditorial.addItem(elemento.getNombre());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnAddEditorial) {
            cargarEditorial(ae.tbEditorial, dato);
            ae.setLocationRelativeTo(null);
            ae.setVisible(true);
        }

        if (e.getSource() == ae.btnCreateEditorial) {
            String editorial = ae.txtNomEditorial.getText();
            if (editorial.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre de la Editorial..!");
                ae.txtNomEditorial.requestFocus();
                return;
            }
            Editorial ed = new Editorial();
            ed.setNombre(editorial);
            ed.setIdEditorial(idEditorial);
            String rptaRegistro = editorialdao.create(ed, opc);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarCboEditorial();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (opc.equals("C")) {
                    setEditorial();
                    ae.dispose();
                } else {
                    cargarEditorial(ae.tbEditorial, dato);
                }
                opc = "C";
                limpiarForm();
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
            }

        }

//        if (e.getSource() == pr.btnRegistrar) {
//            SimpleDateFormat Año = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar calendario = Calendar.getInstance();
//            String FchaHoy = Año.format(m);
//            int hora, minutos, segundos;
//            hora = calendario.get(Calendar.HOUR_OF_DAY);
//            minutos = calendario.get(Calendar.MINUTE);
//            segundos = calendario.get(Calendar.SECOND);
//            //String Fechacompleta = FchaHoy + " " + hora + ":" + minutos + ":" + segundos;
////            String Horacomp = hora + ":" + minutos + ":" + segundos;
////            String tipo_doc = (String) pr.cboTipoDocAdmin.getSelectedItem();
//            String idAutor = (String) pr.cboAutor.getSelectedItem();
//            int autor = 0;
//            if (!idAutor.equals("-- Seleccione --")) {
//                String[] idGymSeparated = idAutor.split("-");
//                autor = Integer.parseInt(idGymSeparated[0].trim());
//            }
//            String idEditorial = (String) pr.cboEditorial.getSelectedItem();
//            int editorial = 0;
//            if (!idEditorial.equals("-- Seleccione --")) {
//                String[] idRolSeparated = idEditorial.split("-");
//                editorial = Integer.parseInt(idRolSeparated[0].trim());
//            }
//            String idArea = (String) pr.cboArea.getSelectedItem();
//            int area = 0;
//            if (!idArea.equals("-- Seleccione --")) {
//                String[] idRolSeparated = idArea.split("-");
//                area = Integer.parseInt(idRolSeparated[0].trim());
//            }
////            String documento = pr.txtDoc.getText();
////            String nombres = pr.txtNombres.getText();
////            String apellidos = pr.txtApellidos.getText();
////            String direccion = pr.txtDireccion.getText();
////            String telefonos = pr.txtTelefonos.getText();
////            String user = pr.txtUser.getText();
////            String pass = new String(pr.txtPass.getPassword());
//            int creadoPor = 0;
//            if (!pr.lblIdAdminLogin.getText().equals("")) {
//                creadoPor = Integer.parseInt(pr.lblIdAdminLogin.getText());
//            }
//
////            String rptaRegistro = admDao.create(documento, tipo_doc, direccion, telefonos, nombres, apellidos, user, pass, FchaHoy, creadoPor, opc, idToUpdate, gym, foto, rolU);
////            if (rptaRegistro != null) {
////                JOptionPane.showMessageDialog(null, rptaRegistro);
////                cargarAdmin(pr.tbAdmin, dato, rolU);
////                if (opc.equals("C")) {
////                    this.pr.pnCreateAdmin.setVisible(false);                
////                }
////                opc = "C";
////                limpiarForm();
////            } else if (opc.equals("C")) {
////                JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
////            } else {
////                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
////            }
//        }
        if (e.getSource() == pr.btnCancelar) {
            limpiarForm();
        }

        if (e.getSource() == ae.mnuUpdateEditorial) {
            opc = "U";
            int fila = ae.tbEditorial.getSelectedRow();
            if (fila >= 0) {
                idEditorial = Integer.parseInt(ae.tbEditorial.getValueAt(fila, 0).toString());
                ae.txtNomEditorial.setText(ae.tbEditorial.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == ae.mnuDeleteEditorial) {
            int fila = ae.tbEditorial.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    String rptaDelete = editorialdao.deleteEditorial(Integer.parseInt(ae.tbEditorial.getValueAt(fila, 0).toString()));
                    if (rptaDelete != null) {
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        cargarEditorial(ae.tbEditorial, "");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == ae.txtFindEditorial) {
            dato = ae.txtFindEditorial.getText();
            cargarEditorial(ae.tbEditorial, dato);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void limpiarForm() {
//        pr.cboArea.setSelectedItem("-- Seleccione --");
//        pr.cboAutor.setSelectedItem("-- Seleccione --");
//        pr.cboEditorial.setSelectedItem("-- Seleccione --");
//        pr.txttitulo.setText("");
        ae.txtNomEditorial.setText("");
    }

    public void setEditorial() {
        Iterator<Editorial> ed = editorialdao.getLastInsert().iterator();
        if (ed.hasNext()) {
            Editorial elemento = ed.next();
            pr.cboEditorial.setSelectedItem(elemento.getNombre());
        }

    }

}
