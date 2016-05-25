/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;
//import Model.*;
//import Controllers.*;

import Controllers.LoginController;
import Model.UsersDAO;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mauricio
 */
public class Start {

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("logoString", "");
            AcrylLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }       
        Login lg = new Login();
        Principal pr = new Principal();
        ConfirmarRegistro cr = new ConfirmarRegistro(null, true);
        AddComment ac = new AddComment(null, true);
//        UsersController uc = new UsersController(pr, adminDao, 0, 0);
        LoginController lgController = new LoginController(lg, pr, cr, ac);
        lg.setVisible(true);
        lg.setLocationRelativeTo(null);
    }
}
