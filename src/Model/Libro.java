/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.InputStream;

/**
 *
 * @author Mauricio Herrera
 */
public class Libro {

    private int idLibro;
    private String titulo;
    private int idAutor;
    private int idEditorial;
    private int idArea;
    private String tipoLibro;
    private int precio;
    private int paginas;
    private String tipoPasta;
    private String fechaCompra;
    private String comentarios;
    private boolean importante;
    private String edicion;   
    private String pais;   
    private InputStream caratula;
    private String largo;
    private String publicacion;

    public Libro() {
        this.idLibro = 0;
        this.titulo = "";
        this.idAutor = 0;
        this.idEditorial = 0;
        this.idArea = 0;
        this.caratula = null;
        this.largo = "";
        this.tipoLibro = "";
        this.precio = 0;
        this.paginas = 0;
        this.edicion = "";
        this.tipoLibro = "";
         this.publicacion = "";
        this.tipoPasta = "";this.fechaCompra = "";
        this.comentarios = "";
        this.importante = false;        
        this.pais="";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public InputStream getCaratula() {
        return caratula;
    }

    public void setCaratula(InputStream caratula) {
        this.caratula = caratula;
    }

    public String getLargo() {
        return largo;
    }

    public void setLargo(String largo) {
        this.largo = largo;
    }

    public String getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(String tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getTipoPasta() {
        return tipoPasta;
    }

    public void setTipoPasta(String tipoPasta) {
        this.tipoPasta = tipoPasta;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isImportante() {
        return importante;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }


    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

}
