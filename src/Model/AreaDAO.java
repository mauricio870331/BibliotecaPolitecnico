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
public class AreaDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public AreaDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(Area a, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO areas (area) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE areas SET area = ? WHERE id_area = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, a.getNombre());
            if (opc.equals("U")) {
                pstm.setInt(2, a.getIdArea());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Area creada con exito";
                } else {
                    responseCreate = "Area actualizada con exito";
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e + " " + e.getClass());
        }

        return responseCreate;
    }

    public ArrayList<Area> getAreaById(int id) {
        ArrayList listaArea = new ArrayList();
        Area area;
        try {
            sql = "SELECT * FROM areas WHERE id_area = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                area = new Area();                
                area.setNombre(rs.getString("area"));
                listaArea.add(area);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaArea;

    }

    public ArrayList<Area> getListArea(String dato) {
        ArrayList listaArea = new ArrayList();
        Area area;
        try {
            if (!dato.equals("")) {
                sql = "SELECT * FROM areas WHERE  area LIKE '" + dato + "%' ORDER BY id_area LIMIT 10";
            } else {
                sql = "SELECT * FROM areas ORDER BY id_area";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("area"));
                listaArea.add(area);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaArea;

    }

    public ArrayList<Area> getLastInsert() {
        ArrayList listaArea = new ArrayList();
        Area area;
        try {
            sql = "SELECT * FROM areas ORDER BY id_area DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                area = new Area();
                area.setIdArea(rs.getInt("id_area"));
                area.setNombre(rs.getString("area"));
                listaArea.add(area);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaArea;

    }

    public String deleteArea(int idArea) {
        String responseDelete = null;
        try {
            sql = "DELETE FROM areas WHERE id_area = ?";
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

     public ArrayList<Area> getIdAreaByNombre(String nombre) {
        ArrayList listaArea = new ArrayList();
        Area area;
        try {
            sql = "SELECT id_area FROM areas WHERE  area = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, nombre);
            rs = pstm.executeQuery();
            if (rs.next()) {
                area = new Area();                
                area.setIdArea(rs.getInt("id_area"));
                listaArea.add(area);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaArea;
    }
    
}
