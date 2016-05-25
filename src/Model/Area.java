/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Mauricio Herrera
 */
public class Area {
    private int idArea;
    private String nombre;


    public Area() {
        this.idArea = 0;
        this.nombre = "";
        
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idAutor) {
        this.idArea = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    
    
}
