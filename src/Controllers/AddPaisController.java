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
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public final class AddPaisController implements ActionListener, KeyListener {

    String dato = "";
    Principal pr;
    PaisDAO paisdao = new PaisDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int idPais = 0;
    AddPais aa;

    public AddPaisController(Principal pr) {
        aa = new AddPais(null, true);
        this.pr = pr;
        this.pr.btnSelectCiudad.addActionListener(this);
        aa.btnCreatePais.addActionListener(this);
        aa.mnuUpdateArea.addActionListener(this);
        aa.mnuDeleteArea.addActionListener(this);
        aa.txtFindPaises.addKeyListener(this);
        aa.btnCancelarPais.addActionListener(this);
    }

    public void cargarPais(JTable tbArea, String dato) {
        String Titulos[] = {"Id", "Descripción"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Pais> nombreIterator = paisdao.getListPais(dato).iterator();
        while (nombreIterator.hasNext()) {
            Pais a = nombreIterator.next();
            columna[0] = a.getIdPais();
            columna[1] = a.getNombre();
            modelo.addRow(columna);
        }
        tbArea.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbArea.setRowSorter(ordenar);
        tbArea.getColumnModel().getColumn(0).setMaxWidth(0);
        tbArea.getColumnModel().getColumn(0).setMinWidth(0);
        tbArea.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbArea.setModel(modelo);
    }

    public void cargarTxtPaises() {
        TextAutoCompleter ta = new TextAutoCompleter(pr.txtPais);
        Iterator<Pais> pais = paisdao.getListPais("").iterator();
        while (pais.hasNext()) {
            Pais elemento = pais.next();
            ta.addItem(elemento.getNombre());
        }
       
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnSelectCiudad) {
            cargarPais(aa.tbPaises, dato);
            aa.setLocationRelativeTo(null);
            aa.setVisible(true);
        }

        if (e.getSource() == aa.btnCancelarPais) {
            limpiarForm();
            opc = "C";
        }

        if (e.getSource() == aa.btnCreatePais) {
            String area = aa.txtNomPais.getText();
            if (area.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese elnombre de la ciudad..!");
                aa.txtNomPais.requestFocus();
                return;
            }
            Pais a = new Pais();
            a.setNombre(area);
            a.setIdPais(idPais);
            String rptaRegistro = paisdao.create(a, opc);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarTxtPaises();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AddPaisController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (opc.equals("C")) {
                    setArea();
                    aa.dispose();
                } else {
                    cargarPais(aa.tbPaises, dato);
                }
                opc = "C";
                limpiarForm();
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
            }

        }

        if (e.getSource() == aa.mnuUpdateArea) {
            opc = "U";
            int fila = aa.tbPaises.getSelectedRow();
            if (fila >= 0) {
                idPais = Integer.parseInt(aa.tbPaises.getValueAt(fila, 0).toString());
                aa.txtNomPais.setText(aa.tbPaises.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == aa.mnuDeleteArea) {
            int fila = aa.tbPaises.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    String rptaDelete = paisdao.deleteArea(Integer.parseInt(aa.tbPaises.getValueAt(fila, 0).toString()));
                    if (rptaDelete != null) {
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        cargarPais(aa.tbPaises, "");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == aa.txtFindPaises) {
            dato = aa.txtFindPaises.getText();
            cargarPais(aa.tbPaises, dato);
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
        aa.txtNomPais.setText("");
    }

    public void setArea() {
        Iterator<Pais> ar = paisdao.getLastInsert().iterator();
        if (ar.hasNext()) {
            Pais elemento = ar.next();
            pr.txtPais.setText(elemento.getNombre());
        }

    }

}
