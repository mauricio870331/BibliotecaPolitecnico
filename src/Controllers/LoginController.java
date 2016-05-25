/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.AddComment;
import App.ConfirmarRegistro;
import App.Login;
import App.Principal;
import Model.Conexion;
import Model.Libro;
import Model.LibroDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public final class LoginController implements ActionListener, KeyListener {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;
    int pagina = 1;
    Login lg;
    Principal pr;
    LibroDAO librodao;
    ConfirmarRegistro cr;
    AddComment ac;
    AutorController autorController;
    AreaController areaController;
    EditorialController editorialController;
    LibroController librocontroller;
    String pass = "";
    Date date = new Date();//para capturar la fecha actual
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfY = new SimpleDateFormat("yyyy");
    SimpleDateFormat dfM = new SimpleDateFormat("MM");
    SimpleDateFormat dfD = new SimpleDateFormat("dd");
    DateFormat format = DateFormat.getDateInstance();
    int id = 0;
//     añoActual = dfY.format(date);

    public LoginController(Login lg, Principal pr, ConfirmarRegistro cr, AddComment ac) {
        conexion = new Conexion();
        cn = conexion.getConexion();
        this.lg = lg;
        this.librodao = new LibroDAO();
        this.autorController = null;
        this.areaController = null;
        this.editorialController = null;
        this.librocontroller = null;
        this.pr = pr;
        this.cr = cr;
        this.ac = ac;
        this.lg.btnIngresar.addActionListener(this);
        this.lg.btnSalir.addActionListener(this);
        this.lg.txtPass.addKeyListener(this);      
        ocultarCapas();
    }

    public void ocultarCapas() {
//        this.pr.lblIdArea.setVisible(false);
//        this.pr.lblIdAutor.setVisible(false);
//        this.pr.lblIdEditorial.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lg.btnIngresar) {
//            String fechaActual = df.format(date);
            String vence = "2016-06-30";
            Date f = null;
            try {
                f = df.parse(vence);
            } catch (ParseException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String añoActual = dfY.format(date);
//            String añoSelc = dfY.format(f);
            String mesActual = dfM.format(date);
            String mesSelect = dfM.format(f);
//            String diaActual = dfD.format(date);
//            String diasel = dfD.format(f);
            pass = new String(lg.txtPass.getPassword());
            if (pass.equals("")) {
                JOptionPane.showMessageDialog(null, "La llave no debe estar vacia..!");
                lg.txtPass.requestFocus();
                return;
            }            
            sql = "SELECT id FROM password WHERE pas = ?";
            try {
                pstm = cn.prepareStatement(sql);
                pstm.setString(1, pass);
                rs = pstm.executeQuery();
                if (rs.next()) {
                  id = rs.getInt("id");                   
                }else{
                  JOptionPane.showMessageDialog(null, "La llave es incorrecta");
                  return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }           
            if (id==1 && (Integer.parseInt(mesSelect) > Integer.parseInt(mesActual))) {              
                pr.setLocationRelativeTo(null);
                pr.setVisible(true);
                pr.txttitulo.requestFocus();
                autorController = new AutorController(pr);
                autorController.cargarCboAutor();
                areaController = new AreaController(pr);
                areaController.cargarCboArea();
                editorialController = new EditorialController(pr);
                editorialController.cargarCboEditorial();
                try {
                    librocontroller = new LibroController(pr, cr, ac, id);
                    librocontroller.cargarLibros(pr.tbLibros);
                    librocontroller.enabledBtnPaginator();
                } catch (NoSuchFieldException | IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                lg.dispose();
            } 
            if (id==2) {               
                pr.setLocationRelativeTo(null);
                pr.setVisible(true);
                pr.txttitulo.requestFocus();
                autorController = new AutorController(pr);
                autorController.cargarCboAutor();
                areaController = new AreaController(pr);
                areaController.cargarCboArea();
                editorialController = new EditorialController(pr);
                editorialController.cargarCboEditorial();
                try {
                    librocontroller = new LibroController(pr, cr, ac, id);
                    librocontroller.cargarLibros(pr.tbLibros);
                    librocontroller.enabledBtnPaginator();
                } catch (NoSuchFieldException | IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                lg.dispose();
            }

        }
        if (e.getSource() == lg.btnSalir) {
            System.exit(0);
        }
//        if (e.getSource() == pr.btnLogout) {
//            int response = JOptionPane.showConfirmDialog(null, "Esta seguro de cerrar la sesion ?", "Aviso..!",
//                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (response == JOptionPane.YES_OPTION) {
//                pr.dispose();
//                autorController = null;
//                pr = null;
//                LoginController lgc = new LoginController(lg = new Login(), pr = new Principal());
//                lg.setVisible(true);
//                lg.setLocationRelativeTo(null);
//            }
//        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == lg.txtPass) {
            char tecla = e.getKeyChar();
            if (tecla == KeyEvent.VK_ENTER) {              
                    lg.btnIngresar.doClick();                
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
