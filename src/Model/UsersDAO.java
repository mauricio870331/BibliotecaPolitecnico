/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Mauricio
 */
public class UsersDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public UsersDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String create(String documento, String tipo_documento, String direccion,
            String telefono, String nombres, String apellidos,
            String usuario, String password, String fecha, int creadoPor, String opc, int lblIdAdminUpdate, int idGym, String foto, int idRol) {
        String responseCreate = null;
        FileInputStream fis = null;
        File file = null;
        try {
            String ruta = "src/ImagenPerfilTmp/" + documento + ".png";
            String f = "";
            if (!foto.equals("")) {
                file = new File(foto);
                fis = new FileInputStream(file);
                f = ", foto = ?";
            }

            if (opc.equals("C")) {
                sql = "INSERT INTO usuarios (tipo_doc, documento, direccion, telefonos,"
                        + " usuario, pass, nombres, apellidos, id_gym, id_rol, foto, fecha_creacion, creado_por)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE usuarios SET tipo_doc = ?, documento = ?, direccion = ?, telefonos = ?,"
                        + " usuario = ?, pass = ?, nombres = ?, apellidos = ?, id_gym = ?, id_rol = ?" + f + " WHERE id_user = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, tipo_documento);
            pstm.setString(2, documento);
            pstm.setString(3, direccion);
            pstm.setString(4, telefono);
            pstm.setString(5, usuario);
            pstm.setString(6, password);
            pstm.setString(7, nombres);
            pstm.setString(8, apellidos);
            if (idGym > 0) {
                pstm.setInt(9, idGym);
            } else {
                pstm.setString(9, null);
            }
            if (idRol > 0) {
                pstm.setInt(10, idRol);
            } else {
                pstm.setString(10, null);
            }
            if (opc.equals("U")) {
                if (!foto.equals("")) {
                    pstm.setBinaryStream(11, fis, (int) file.length());
                    pstm.setInt(12, lblIdAdminUpdate);
                } else {
                    pstm.setInt(11, lblIdAdminUpdate);
                }
            }
            if (opc.equals("C")) {
                if (!foto.equals("")) {
                    pstm.setBinaryStream(11, fis, (int) file.length());
                } else {
                    pstm.setString(11, null);
                }
                pstm.setString(12, fecha);
                if (creadoPor > 0) {
                    pstm.setInt(13, creadoPor);
                } else {
                    pstm.setString(13, null);
                }
            }
            int rowAfected = pstm.executeUpdate();

            if (rowAfected > 0) {
                if (!foto.equals("")) {
                    fis.close();
                    try {
                        if (ruta.equals(foto)) {
                            if (file.delete()) {
                                System.out.println("borrado");
                            } else {
                                System.out.println("No borrado");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("error " + e);
                    }
                }
                if (opc.equals("C")) {
                    responseCreate = "Registro creado con exito";
                } else {
                    responseCreate = "Registro actualizado con exito";
                }
            }
        } catch (SQLException | IOException e) {

            System.out.println("error: " + e);
        }
        return responseCreate;
    }

    public ArrayList<Users> getListAdministrador(int pagina, String dato, int rol) {
        String adnWhere = "";
        int regitrosXpagina = 10;
        int inicio = ((pagina - 1) * regitrosXpagina);
        if (inicio < regitrosXpagina) {
            inicio = 0;
        }
        if (rol == 3) {
            adnWhere = "AND id_rol = 3";
        }
        if (rol == 1) {
            adnWhere = "AND id_rol = 1";
        }
        if (rol == 2) {
            adnWhere = "AND id_rol = 2";
        }

        ArrayList listaAdministrador = new ArrayList();
        Users admin;
        try {
            if (dato.equals("")) {
                sql = "SELECT * FROM usuarios WHERE id_rol != 4 " + adnWhere + " ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
            } else {
                sql = "SELECT * FROM usuarios WHERE id_rol != 4 " + adnWhere + " AND (documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%') ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
            }

            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                admin = new Users();
                admin.setTipoDoc(rs.getString("tipo_doc"));
                admin.setDocumento(rs.getString("documento"));
                admin.setNombres(rs.getString("nombres"));
                admin.setApellidos(rs.getString("apellidos"));
                admin.setDireccion(rs.getString("direccion"));
                admin.setTelefono(rs.getString("telefonos"));
                listaAdministrador.add(admin);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAdministrador;

    }

    public ArrayList<Users> getExistAdmin(String usuario, String password) {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {
            sql = "SELECT * FROM usuarios where usuario = ? AND pass = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, usuario);
            pstm.setString(2, password);
            rs = pstm.executeQuery();
            if (rs.next()) {
                admin = new Users();
                admin.setNombres(rs.getString("nombres"));
                admin.setApellidos(rs.getString("apellidos"));
                admin.setId(rs.getInt("id_user"));
                admin.setFoto(rs.getBinaryStream("foto"));
                admin.setRol(rs.getInt("id_rol"));
                listaAdmin.add(admin);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAdmin;
    }

    public ArrayList<Users> getIdToUpdate(String documento) {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {
            sql = "SELECT * FROM usuarios where documento = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, documento);
            rs = pstm.executeQuery();
            if (rs.next()) {
                admin = new Users();
                admin.setUsuario(rs.getString("usuario"));
                admin.setPassword(rs.getString("pass"));
                admin.setId(rs.getInt("id_user"));
                admin.setRol(rs.getInt("id_rol"));
                listaAdmin.add(admin);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAdmin;
    }

    public String deleteAdmin(int id) {
        String responseDelete = null;
        try {
            sql = "DELETE FROM usuarios WHERE id_user = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            int rowDelete = pstm.executeUpdate();
            if (rowDelete > 0) {
                responseDelete = "registro eliminado con exito";
            }
        } catch (Exception e) {
        }
        return responseDelete;
    }

    public int totalPaginas(String dato) {
        int regitrosXpagina = 10;
        int creg = 0;
        int paginas = 0;
        if (dato.equals("")) {
            sql = "SELECT COUNT(*) AS con FROM usuarios";
        } else {
            sql = "SELECT COUNT(*) AS con FROM usuarios WHERE documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%'";
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
        return paginas;
    }

    public int getIdToPay(String dato) {
        int iduser = 0;
        try {
            if (!dato.equals("")) {
                sql = "SELECT id_user FROM usuarios WHERE id_rol = 3 AND documento = '" + dato + "'";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                iduser = rs.getInt("id_user");
            }
        } catch (Exception e) {
            System.out.println("error "+e+" "+e.getClass());
        }
        return iduser;
    }

}
