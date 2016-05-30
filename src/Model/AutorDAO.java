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
public class AutorDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public AutorDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(Autor a, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO autor (nombre_completo) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE autor SET nombre_completo = ? WHERE id_autor = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, a.getNombre());
            if (opc.equals("U")) {
                pstm.setInt(2, a.getIdAutor());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Autor creado con exito";
                } else {
                    responseCreate = "Autor actualizado con exito";
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e + " " + e.getClass());
        }

        return responseCreate;
    }

    public ArrayList<Autor> getAutorById(int id) {
        ArrayList listaAutor = new ArrayList();
        Autor autor;
        try {
            sql = "SELECT * FROM autor WHERE  id_autor = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                autor = new Autor();
                autor.setNombre(rs.getString("nombre_completo"));
                listaAutor.add(autor);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAutor;
    }

    public ArrayList<Autor> getIdAutorByNombre(String nombre) {
        ArrayList listaAutor = new ArrayList();
        Autor autor;
        try {
            sql = "SELECT id_autor FROM autor WHERE  nombre_completo = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, nombre);
            rs = pstm.executeQuery();
            if (rs.next()) {
                autor = new Autor();
                autor.setIdAutor(rs.getInt("id_autor"));
                listaAutor.add(autor);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAutor;
    }

    public ArrayList<Autor> getListAutor(String dato) {
        ArrayList listaAutor = new ArrayList();
        Autor autor;
        try {
            if (!dato.equals("")) {
                sql = "SELECT * FROM autor WHERE  nombre_completo LIKE '" + dato + "%' ORDER BY id_autor LIMIT 10";
            } else {
                sql = "SELECT * FROM autor ORDER BY id_autor";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                autor = new Autor();
                autor.setIdAutor(rs.getInt("id_autor"));
                autor.setNombre(rs.getString("nombre_completo"));
                listaAutor.add(autor);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAutor;

    }

    public ArrayList<Autor> getLastInsert() {
        ArrayList listaAutor = new ArrayList();
        Autor autor;
        try {
            sql = "SELECT * FROM autor ORDER BY id_autor DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                autor = new Autor();
                autor.setIdAutor(rs.getInt("id_autor"));
                autor.setNombre(rs.getString("nombre_completo"));
                listaAutor.add(autor);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAutor;

    }

    public String deleteAutor(int idAutor) {
        String responseDelete = null;
        try {

            sql = "DELETE FROM autor WHERE id_autor = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idAutor);
            int rowDelete = pstm.executeUpdate();
            if (rowDelete > 0) {
                responseDelete = "registro eliminado con exito..!";
            }
        } catch (Exception e) {
        }
        return responseDelete;
    }

    public boolean existAutor(String autor) {
        boolean existe = false;
        try {
            sql = "SELECT * FROM autor WHERE nombre_completo = '"+autor+"' OR nombre_completo LIKE '" + autor + "%'";
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
