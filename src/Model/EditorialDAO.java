/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Mauricio Herrera
 */
public class EditorialDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public EditorialDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(Editorial e, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO editorial (nom_editorial) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE editorial SET nom_editorial = ? WHERE id_editorial = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, e.getNombre());
            if (opc.equals("U")) {
                pstm.setInt(2, e.getIdEditorial());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Editorial creada con exito";
                } else {
                    responseCreate = "Editorial actualizada con exito";
                }
            }
        } catch (Exception ex) {
            System.out.println("error: " + ex + " " + ex.getClass());
        }

        return responseCreate;
    }

    public ArrayList<Editorial> getEditorialById(int id) {
        ArrayList ListEditorial = new ArrayList();
        Editorial editorial;
        try {
            sql = "SELECT nom_editorial FROM editorial WHERE id_editorial = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                editorial = new Editorial();                
                editorial.setNombre(rs.getString("nom_editorial"));
                ListEditorial.add(editorial);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListEditorial;

    }

    public ArrayList<Editorial> getListEditorial(String dato) {
        ArrayList ListEditorial = new ArrayList();
        Editorial editorial;
        try {
            if (!dato.equals("")) {
                sql = "SELECT * FROM editorial WHERE  nom_editorial LIKE '" + dato + "%' ORDER BY id_editorial LIMIT 10";
            } else {
                sql = "SELECT * FROM editorial ORDER BY id_editorial";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                editorial = new Editorial();
                editorial.setIdEditorial(rs.getInt("id_editorial"));
                editorial.setNombre(rs.getString("nom_editorial"));
                ListEditorial.add(editorial);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListEditorial;

    }

    public ArrayList<Editorial> getLastInsert() {
        ArrayList ListEditorial = new ArrayList();
        Editorial editorial;
        try {
            sql = "SELECT * FROM editorial ORDER BY id_editorial DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                editorial = new Editorial();
                editorial.setIdEditorial(rs.getInt("id_editorial"));
                editorial.setNombre(rs.getString("nom_editorial"));
                ListEditorial.add(editorial);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListEditorial;

    }

    public String deleteEditorial(int idEditorial) {
        String responseDelete = null;
        try {
            sql = "DELETE FROM editorial WHERE id_editorial = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idEditorial);
            int rowDelete = pstm.executeUpdate();
            if (rowDelete > 0) {
                responseDelete = "registro eliminado con exito..!";
            }
        } catch (Exception e) {
        }
        return responseDelete;
    }
    
     public ArrayList<Editorial> getIdEditorialByNombre(String nombre) {
        ArrayList listaEditorial = new ArrayList();
        Editorial editorial;
        try {
            sql = "SELECT id_editorial FROM editorial WHERE  nom_editorial = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, nombre);
            rs = pstm.executeQuery();
            if (rs.next()) {
                editorial = new Editorial();                
                editorial.setIdEditorial(rs.getInt("id_editorial"));
                listaEditorial.add(editorial);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaEditorial;
    }

    public boolean existEditorial(String editorial) {
       boolean existe = false;
        try {
            sql = "SELECT * FROM editorial WHERE nom_editorial = '"+editorial+"' OR nom_editorial LIKE '" + editorial + "%'";
            pstm = cn.prepareStatement(sql);        
            rs = pstm.executeQuery();
            if (rs.next()) {                
                existe = true;
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return existe;
    }

}
