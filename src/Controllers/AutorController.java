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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio
 */
public final class AutorController extends MouseAdapter implements ActionListener, KeyListener {

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
        aa.btnCancelarAutor.addActionListener(this);
        aa.tbAutor.addMouseListener(this);
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

        if (e.getSource() == aa.btnCancelarAutor) {
            limpiarForm();
        }

        if (e.getSource() == aa.btnCreateAutor) {
            String autor = aa.txtNomAutor.getText();
            if (autor.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese elnombre del Autor..!");
                aa.txtNomAutor.requestFocus();
                return;
            }
            if (autordao.existAutor(autor)) {
                JOptionPane.showMessageDialog(null, "El autor: " + autor + " coincide con uno existente\nNo es necesario crearlo otra vez.\nSi lo desea puede actualizarlo..!");
                aa.txtNomAutor.setText("");
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
        aa.txtNomAutor.setText("");
        opc = "C";

    }

    public void setAutor() {
        Iterator<Autor> aut = autordao.getLastInsert().iterator();
        if (aut.hasNext()) {
            Autor elemento = aut.next();
            pr.cboAutor.setSelectedItem(elemento.getNombre());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {           
            int fila = aa.tbAutor.getSelectedRow();
            if (fila >= 0) {
                pr.cboAutor.setSelectedItem(aa.tbAutor.getValueAt(fila, 1).toString());
                aa.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }
    

}
