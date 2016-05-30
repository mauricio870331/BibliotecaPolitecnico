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
public final class AreaController extends MouseAdapter implements ActionListener, KeyListener {

    String dato = "";
    Principal pr;
    AreaDAO areadao = new AreaDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int idArea = 0;
    AddArea aa;

    public AreaController(Principal pr) {
        aa = new AddArea(null, true);
        this.pr = pr;
        this.pr.btnAddArea.addActionListener(this);
        aa.btnCreateArea.addActionListener(this);
        aa.mnuUpdateArea.addActionListener(this);
        aa.mnuDeleteArea.addActionListener(this);
        aa.txtFindArea.addKeyListener(this);
        aa.btnCancelaArea.addActionListener(this);
        aa.tbArea.addMouseListener(this);
    }

    public void cargarArea(JTable tbArea, String dato) {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Area> nombreIterator = areadao.getListArea(dato).iterator();
        while (nombreIterator.hasNext()) {
            Area a = nombreIterator.next();
            columna[0] = a.getIdArea();
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

    public void cargarCboArea() {
        pr.cboArea.removeAllItems();
        Iterator<Area> area = areadao.getListArea("").iterator();
        pr.cboArea.addItem("-- Seleccione --");
        while (area.hasNext()) {
            Area elemento = area.next();
            pr.cboArea.addItem(elemento.getNombre());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnAddArea) {
            cargarArea(aa.tbArea, dato);
            aa.setLocationRelativeTo(null);
            aa.setVisible(true);
        }
        
        if (e.getSource() == aa.btnCancelaArea) {
            limpiarForm();            
        }

        if (e.getSource() == aa.btnCreateArea) {
            String area = aa.txtNomArea.getText();
            if (area.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese elnombre del Autor..!");
                aa.txtNomArea.requestFocus();
                return;
            }
            if (areadao.existArea(area)) {
                JOptionPane.showMessageDialog(null, "El area: "+area+" coincide con una existente\nNo es necesario crearla otra vez.\nSi lo desea puede actualizarlo..!");
                aa.txtNomArea.setText("");
                aa.txtNomArea.requestFocus();
                return;
            }
            Area a = new Area();
            a.setNombre(area);
            a.setIdArea(idArea);
            String rptaRegistro = areadao.create(a, opc);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarCboArea();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (opc.equals("C")) {
                    setArea();
                    aa.dispose();
                } else {
                    cargarArea(aa.tbArea, dato);
                }
                opc = "C";
                limpiarForm();
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
            }

        }

        if (e.getSource() == pr.btnCancelar) {

            limpiarForm();
        }

        if (e.getSource() == aa.mnuUpdateArea) {
            opc = "U";
            int fila = aa.tbArea.getSelectedRow();
            if (fila >= 0) {
                idArea = Integer.parseInt(aa.tbArea.getValueAt(fila, 0).toString());
                aa.txtNomArea.setText(aa.tbArea.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == aa.mnuDeleteArea) {
            int fila = aa.tbArea.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    String rptaDelete = areadao.deleteArea(Integer.parseInt(aa.tbArea.getValueAt(fila, 0).toString()));
                    if (rptaDelete != null) {
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        cargarArea(aa.tbArea, "");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == aa.txtFindArea) {
            dato = aa.txtFindArea.getText();
            cargarArea(aa.tbArea, dato);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void limpiarForm() {
        aa.txtNomArea.setText("");
        opc="C";       
    }

    public void setArea() {
        Iterator<Area> ar = areadao.getLastInsert().iterator();
        if (ar.hasNext()) {
            Area elemento = ar.next();
            pr.cboArea.setSelectedItem(elemento.getNombre());
        }

    }
     public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {           
            int fila = aa.tbArea.getSelectedRow();
            if (fila >= 0) {
                pr.cboArea.setSelectedItem(aa.tbArea.getValueAt(fila, 1).toString());
                aa.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }
    }

}
