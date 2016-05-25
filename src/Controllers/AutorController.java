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
public final class AutorController implements ActionListener, KeyListener {

    String dato = "";
    Principal pr;
    AutorDAO autordao = new AutorDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int idAutor = 0;
    AddAutor aa;

    public AutorController(Principal pr) {
        aa = new AddAutor(null, true);
        this.pr = pr;
        this.pr.btnAddAutor.addActionListener(this);
        aa.btnCreateAutor.addActionListener(this);
        aa.mnuUpdateAutor.addActionListener(this);
        aa.mnuDeleteAutor.addActionListener(this);
        aa.txtFindAutor.addKeyListener(this);
    }

    public void cargarAutor(JTable tbAutor, String dato) {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Autor> nombreIterator = autordao.getListAutor(dato).iterator();
        while (nombreIterator.hasNext()) {
            Autor a = nombreIterator.next();
            columna[0] = a.getIdAutor();
            columna[1] = a.getNombre();
            modelo.addRow(columna);
        }
        tbAutor.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbAutor.setRowSorter(ordenar);
        tbAutor.getColumnModel().getColumn(0).setMaxWidth(0);
        tbAutor.getColumnModel().getColumn(0).setMinWidth(0);
        tbAutor.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbAutor.setModel(modelo);
    }

    public void cargarCboAutor() {
        pr.cboAutor.removeAllItems();
        Iterator<Autor> autor = autordao.getListAutor("").iterator();
        pr.cboAutor.addItem("-- Seleccione --");
        while (autor.hasNext()) {
            Autor elemento = autor.next();
            pr.cboAutor.addItem(elemento.getNombre());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnAddAutor) {
            cargarAutor(aa.tbAutor, dato);
            aa.setLocationRelativeTo(null);
            aa.setVisible(true);
        }

        if (e.getSource() == aa.btnCreateAutor) {
            String autor = aa.txtNomAutor.getText();
            if (autor.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese elnombre del Autor..!");
                aa.txtNomAutor.requestFocus();
                return;
            }
            Autor a = new Autor();
            a.setNombre(autor);
            a.setIdAutor(idAutor);
            String rptaRegistro = autordao.create(a, opc);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarCboAutor();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AutorController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (opc.equals("C")) {
                    setAutor();
                    aa.dispose();
                } else {
                    cargarAutor(aa.tbAutor, dato);
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

        if (e.getSource() == aa.mnuUpdateAutor) {
            opc = "U";
            int fila = aa.tbAutor.getSelectedRow();
            if (fila >= 0) {
                idAutor = Integer.parseInt(aa.tbAutor.getValueAt(fila, 0).toString());
                aa.txtNomAutor.setText(aa.tbAutor.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == aa.mnuDeleteAutor) {
            int fila = aa.tbAutor.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    String rptaDelete = autordao.deleteAutor(Integer.parseInt(aa.tbAutor.getValueAt(fila, 0).toString()));
                    if (rptaDelete != null) {
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        cargarAutor(aa.tbAutor, "");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == aa.txtFindAutor) {
            dato = aa.txtFindAutor.getText();
            cargarAutor(aa.tbAutor, dato);
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
        aa.txtNomAutor.setText("");
    }

    public void setAutor() {
        Iterator<Autor> aut = autordao.getLastInsert().iterator();
        if (aut.hasNext()) {
            Autor elemento = aut.next();
            pr.cboAutor.setSelectedItem(elemento.getNombre());
        }

    }

}
