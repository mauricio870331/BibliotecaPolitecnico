package Model;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {   

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestor_libros", "root", "");
//            conectar = DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/pm_medex", "mauricio", "openEHR2008");
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }


}
