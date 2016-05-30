/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Mauricio Herrera
 */
public class LibroDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public LibroDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(Libro l, String opc) {
        String responseCreate = null;
        FileInputStream fis = null;
        File img = null;
        String strimg = "";
        try {
            if (!l.getLargo().equals("")) {
                img = new File(l.getLargo());
                fis = new FileInputStream(img);
                strimg = ", caratula = ?";
            }
            if (opc.equals("C")) {
                sql = "INSERT INTO libro (titulo, id_autor, id_editorial, id_area, tipo_libro, precio, paginas, tipo_pasta, "
                        + "fecha_compra, comentarios, es_importante, num_edicion, pais, año_publicacion	, caratula) VALUES (?, ?, ?, ? ,? ,? ,? ,?, ?, ?, ?,?,?,?,?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE libro SET titulo = ?, id_autor = ?, id_editorial = ?,"
                        + " id_area = ?, tipo_libro = ?, precio = ?, paginas = ?, tipo_pasta = ?,  fecha_compra = ?,"
                        + " comentarios = ?, es_importante = ?, num_edicion = ?, pais = ?, año_publicacion = ? " + strimg + " WHERE id_libro = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, l.getTitulo());
            if (l.getIdAutor() != 0) {
                pstm.setInt(2, l.getIdAutor());
            } else {
                pstm.setInt(2, 0);
            }
            if (l.getIdEditorial() != 0) {
                pstm.setInt(3, l.getIdEditorial());
            } else {
                pstm.setInt(3, 0);
            }
            if (l.getIdArea() != 0) {
                pstm.setInt(4, l.getIdArea());
            } else {
                pstm.setInt(4, 0);
            }
            if (!l.getTipoLibro().equals("")) {
                pstm.setString(5, l.getTipoLibro());
            } else {
                pstm.setString(5, null);
            }
            if (l.getPrecio() != 0) {
                pstm.setInt(6, l.getPrecio());
            } else {
                pstm.setInt(6, 0);
            }
            if (l.getPaginas() != 0) {
                pstm.setInt(7, l.getPaginas());
            } else {
                pstm.setInt(7, 0);
            }
            if (!l.getTipoPasta().equals("")) {
                pstm.setString(8, l.getTipoPasta());
            } else {
                pstm.setString(8, null);
            }
            if (!l.getFechaCompra().equals("")) {
                pstm.setString(9, l.getFechaCompra());
            } else {
                pstm.setString(9, null);
            }
            if (!l.getComentarios().equals("")) {
                pstm.setString(10, l.getComentarios());
            } else {
                pstm.setString(10, null);
            }            
            pstm.setBoolean(11, l.isImportante());
            pstm.setString(12, l.getEdicion());            
            pstm.setString(13, l.getPais());
            pstm.setString(14, l.getPublicacion());           
            if (opc.equals("C")) {
                if (!l.getLargo().equals("")) {
                    pstm.setBinaryStream(15, fis, (int) img.length());
                } else {
                    pstm.setBinaryStream(15, null);
                }
            }
            if (opc.equals("U")) {
                if (!l.getLargo().equals("")) {
                    pstm.setBinaryStream(15, fis, (int) img.length());
                    pstm.setInt(16, l.getIdLibro());
                } else {
                    pstm.setInt(15, l.getIdLibro());
                }
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Libro creado con exito";
                } else {
                    responseCreate = "Libro actualizado con exito";
                }
            }
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("error: " + e + " " + e.getClass());
        }
        return responseCreate;
    }

    public ArrayList<Libro> getListLibros(int pagina, String dato) {
        int regitrosXpagina = 10;
        int inicio = ((pagina - 1) * regitrosXpagina);
        if (inicio < regitrosXpagina) {
            inicio = 0;
        }
        ArrayList ListaLibros = new ArrayList();
        Libro l;
        try {
            if (!dato.equals("")) {
                sql = "SELECT l.* FROM libro l"
                        + " LEFT JOIN autor a ON l.id_autor = a.id_autor"
                        + " LEFT JOIN areas ar ON l.id_area = ar.id_area"
                        + " LEFT JOIN editorial e ON l.id_editorial = e.id_editorial"
                        + " WHERE  l.titulo LIKE '" + dato + "%' OR a.nombre_completo LIKE '" + dato + "%'"
                        + " OR ar.area LIKE '" + dato + "%' OR e.nom_editorial LIKE '" + dato + "%'"
                        + " ORDER BY l.titulo LIMIT " + inicio + "," + regitrosXpagina + "";
            } else {
                sql = "SELECT l.* FROM libro l ORDER BY l.titulo LIMIT " + inicio + "," + regitrosXpagina + "";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                l = new Libro();
                l.setIdLibro(rs.getInt("l.id_libro"));
                l.setTitulo(rs.getString("l.titulo"));
                l.setIdAutor(rs.getInt("l.id_autor"));
                l.setIdArea(rs.getInt("l.id_area"));
                l.setIdEditorial(rs.getInt("l.id_editorial"));
                l.setCaratula(rs.getBinaryStream("caratula"));
                l.setTipoLibro(rs.getString("l.tipo_libro"));
                l.setPrecio(rs.getInt("l.precio"));
                l.setPaginas(rs.getInt("l.paginas"));
                l.setTipoPasta(rs.getString("l.tipo_pasta"));
                l.setFechaCompra(rs.getString("l.fecha_compra"));
                l.setComentarios(rs.getString("l.comentarios"));
                l.setImportante(rs.getBoolean("l.es_importante"));
                l.setEdicion(rs.getString("l.num_edicion"));
                ListaLibros.add(l);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaLibros;

    }

    public ArrayList<Libro> getCaratulaByIdLibro(int idLibro) {
        ArrayList ListaLibros = new ArrayList();
        Libro l;
        try {
            sql = "SELECT caratula, año_publicacion, comentarios, tipo_libro, tipo_pasta, fecha_compra"
                    + " , precio, num_edicion, 	paginas, pais FROM libro WHERE id_libro = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idLibro);
            rs = pstm.executeQuery();
            if (rs.next()) {
                l = new Libro();
                l.setPublicacion(rs.getString("año_publicacion"));
                l.setCaratula(rs.getBinaryStream("caratula"));
                if (rs.getString("comentarios")!=null) {
                  l.setComentarios(rs.getString("comentarios"));
                }else{
                  l.setComentarios("");
                }                
                l.setTipoLibro(rs.getString("tipo_libro"));
                l.setTipoPasta(rs.getString("tipo_pasta"));
                l.setFechaCompra(rs.getString("fecha_compra"));
                l.setPrecio(rs.getInt("precio"));
                l.setEdicion(rs.getString("num_edicion"));
                l.setPaginas(rs.getInt("paginas"));
                l.setPais(rs.getString("pais"));
                ListaLibros.add(l);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaLibros;

    }

    public ArrayList<Libro> getImportnte(int idLibro) {
        ArrayList ListaLibros = new ArrayList();
        Libro l;
        try {
            sql = "SELECT es_importante FROM libro WHERE id_libro = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idLibro);
            rs = pstm.executeQuery();
            if (rs.next()) {
                l = new Libro();
                l.setImportante(rs.getBoolean("es_importante"));
                ListaLibros.add(l);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaLibros;

    }

    public String deleteLibro(int idlibro) {
        String responseDelete = null;
        try {
            sql = "DELETE FROM libro WHERE id_libro = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, idlibro);
            int rowDelete = pstm.executeUpdate();
            if (rowDelete > 0) {
                responseDelete = "registro eliminado con exito..!";
            }
        } catch (Exception e) {
        }
        return responseDelete;
    }

    public int totalPaginas(String dato) {
        int regitrosXpagina = 10;
        double creg = 0;
        double paginas = 0;
        if (dato.equals("")) {
            sql = "SELECT COUNT(*) AS con FROM libro";
        } else {
            sql = "SELECT COUNT(*) AS con FROM libro WHERE titulo LIKE '" + dato + "%'";
        }
        try {
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                creg = rs.getInt("con");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        paginas = (creg / regitrosXpagina);
//        System.out.println("paginas "+Math.ceil(paginas));
        return (int) Math.ceil(paginas);
    }

}
