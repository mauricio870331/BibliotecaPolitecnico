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
public class PaisDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public PaisDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(Pais a, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO paises (descripcion) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE paises SET descripcion = ? WHERE id = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, a.getNombre());
            if (opc.equals("U")) {
                pstm.setInt(2, a.getIdPais());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Ciudad creada con exito";
                } else {
                    responseCreate = "Ciudad actualizada con exito";
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e + " " + e.getClass());
        }

        return responseCreate;
    }

    public ArrayList<Pais> getPaisById(int id) {
        ArrayList listPaises = new ArrayList();
        Pais pais;
        try {
            sql = "SELECT * FROM paises WHERE id = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                pais = new Pais();                
                pais.setNombre(rs.getString("descripcion"));
                listPaises.add(pais);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listPaises;

    }

    public ArrayList<Pais> getListPais(String dato) {
        ArrayList listPaises = new ArrayList();
        Pais pais;
        try {
            if (!dato.equals("")) {
                sql = "SELECT * FROM paises WHERE descripcion LIKE '" + dato + "%' ORDER BY descripcion";
            } else {
                sql = "SELECT * FROM paises ORDER BY descripcion";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                pais = new Pais();
                pais.setIdPais(rs.getInt("id"));
                pais.setNombre(rs.getString("descripcion"));
                listPaises.add(pais);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listPaises;

    }

    public ArrayList<Pais> getLastInsert() {
        ArrayList listaPais = new ArrayList();
        Pais pais;
        try {
            sql = "SELECT * FROM paises ORDER BY id DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                pais = new Pais();
                pais.setIdPais(rs.getInt("id"));
                pais.setNombre(rs.getString("descripcion"));
                listaPais.add(pais);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaPais;

    }

    public String deleteArea(int idArea) {
        String responseDelete = null;
        try {
            sql = "DELETE FROM paises WHERE id = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idArea);
            int rowDelete = pstm.executeUpdate();
            if (rowDelete > 0) {
                responseDelete = "registro eliminado con exito..!";
            }
        } catch (Exception e) {
        }
        return responseDelete;
    }

     public ArrayList<Pais> getIdPaisByNombre(String nombre) {
        ArrayList listaPais = new ArrayList();
        Pais pais;
        try {
            sql = "SELECT id FROM paises WHERE  descripcion = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, nombre);
            rs = pstm.executeQuery();
            if (rs.next()) {
                pais = new Pais();                
                pais.setIdPais(rs.getInt("id"));
                listaPais.add(pais);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaPais;
    }
    
}
