/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.AddComment;
import App.ChangePass;
import App.ConfirmarRegistro;
import App.Principal;
import Model.AreaDAO;
import Model.AutorDAO;
import Model.EditorialDAO;
import Model.Libro;
import Model.LibroDAO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 *
 * @author Mauricio Herrera
 */
public final class LibroController implements ActionListener, MouseListener, KeyListener, ItemListener, FocusListener {

    int idLibroUpdate = 0;
    int idUser = 0;
    int pagina = 1;
    String dato = "";
    Libro l = null;
    Principal pr;
    ConfirmarRegistro cr;
    AddComment ac;
    LibroDAO librodao = new LibroDAO();
    AutorDAO autordao = new AutorDAO();
    AreaDAO areadao = new AreaDAO();
    EditorialDAO editorialdao = new EditorialDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    String opc = "C";
    int countAction = 0;
    JFileChooser FileChooser = new JFileChooser();
    String foto = "";
    ImageIcon ii = null;
    ImageIcon iin = null;
    Date date = new Date();//para capturar la fecha actual
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfY = new SimpleDateFormat("yyyy");
    SimpleDateFormat dfM = new SimpleDateFormat("MM");
    SimpleDateFormat dfD = new SimpleDateFormat("dd");
    SimpleDateFormat dfupdate = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat format = DateFormat.getDateInstance();
    boolean importante = false;
    String comentario = "";
    int id = 0;

    public LibroController(Principal pr, ConfirmarRegistro cr, AddComment ac, int id) throws NoSuchFieldException {
        this.pr = pr;
        this.cr = cr;
        this.ac = ac;
        this.id = id;
        this.pr.btnRegistrar.addActionListener(this);
        this.pr.cboArea.addActionListener(this);
        this.pr.cboAutor.addActionListener(this);
        this.pr.cboEditorial.addActionListener(this);
        this.pr.mnuUpdateLibro.addActionListener(this);
        this.pr.mnuDeleteLibro.addActionListener(this);
        this.pr.lblCarattula.addMouseListener(this);
        this.pr.btnCancelar.addActionListener(this);
        this.pr.btnAdelante.addActionListener(this);
        this.pr.btnAtras.addActionListener(this);
        this.pr.btnPrimero.addActionListener(this);
        this.pr.btnUltimo.addActionListener(this);
        this.pr.txtBuscarLibro.addKeyListener(this);
        this.cr.chkImportante.addItemListener(this);
        this.ac.btAddCo.addActionListener(this);
        this.ac.btnCancelComm.addActionListener(this);
        this.pr.btnAddComment.addActionListener(this);
        this.cr.btncancel.addActionListener(this);
        this.cr.btnConfirm.addActionListener(this);
        this.pr.exit.addActionListener(this);
        this.pr.changePassword.addActionListener(this);
        this.pr.copiaDeSeguridad.addActionListener(this);
        this.pr.cldFechaCompra.setDate(date);
        this.pr.spnEdicion.addFocusListener(this);

    }

    public void cargarLibros(JTable tbLibros) throws NoSuchFieldException, IOException {
//        tbLibros.setDefaultRenderer(Object.class, new ImagensTabla());
        String Titulos[] = {"id Libro", "Titulo", "Autor", "Editorial", "Area"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[5];
        Iterator<Libro> nombreIterator = librodao.getListLibros(pagina, dato).iterator();
        while (nombreIterator.hasNext()) {
            Libro l = nombreIterator.next();
            columna[0] = l.getIdLibro();
            columna[1] = l.getTitulo();
            columna[2] = (l.getIdAutor() != 0) ? autordao.getAutorById(l.getIdAutor()).get(0).getNombre() : "";
            columna[3] = (l.getIdEditorial() != 0) ? editorialdao.getEditorialById(l.getIdEditorial()).get(0).getNombre() : "";
            columna[4] = (l.getIdArea() != 0) ? areadao.getAreaById(l.getIdArea()).get(0).getNombre() : "";
//            columna[5] = (l.getTipoLibro() != null) ? l.getTipoLibro() : "";
//            InputStream img = l.getCaratula();
//            if (img != null) {
//                BufferedImage bi = ImageIO.read(img);
//                ii = new ImageIcon(bi);
//                Image conver = ii.getImage();
//                Image tam = conver.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
//                iin = new ImageIcon(tam);
//                columna[6] = new JLabel(iin);
//            } else {
//                columna[6] = new JLabel();
//            }
            modelo.addRow(columna);
        }
        tbLibros.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbLibros.setRowSorter(ordenar);
        tbLibros.getColumnModel().getColumn(0).setMaxWidth(0);
        tbLibros.getColumnModel().getColumn(0).setMinWidth(0);
        tbLibros.getColumnModel().getColumn(0).setPreferredWidth(0);
//        tbLibros.getColumnModel().getColumn(5).setPreferredWidth(25);
        tbLibros.setModel(modelo);
//        tbLibros.setRowHeight(70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.copiaDeSeguridad) {
            System.out.println("exportando");
            Runtime backup = Runtime.getRuntime();
            try {
                backup.exec("C:/xampp/mysql/bin/mysqldump -v -v -v --host=localhost --user=root --port=3306 --protocol=tcp --force --allow-keywords --compress --add-drop-table --result-file=bd.sql --databases biblioteca_politecnico");

            } catch (IOException ex) {
                Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Backup creado correctamente");
        }

        if (e.getSource() == pr.exit) {
            System.exit(0);
        }

        if (e.getSource() == pr.changePassword) {
            ChangePass cp = new ChangePass(null, true);
            cp.idpass = id;
            cp.setLocationRelativeTo(null);
            cp.setVisible(true);
        }

        if (e.getSource() == pr.btnRegistrar) {
            String fecha = "";
            String Now = "";
            String añoActual = "";
            String añoSelc = "";
            String mesActual = "";
            String mesSelect = "";
            String diaActual = "";
            String diaSel = "";
            String publicacion = "";
            String edicion = "";

            añoActual = dfY.format(date);
            mesActual = dfM.format(date);
            diaActual = dfD.format(date);
            if (pr.txttitulo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el Titulo");
                pr.txttitulo.requestFocus();
                return;
            }
            l = new Libro();
            if (opc.equals("U")) {
                l.setIdLibro(idLibroUpdate);
            }
            String titulo = pr.txttitulo.getText();
            l.setTitulo(titulo);
            String idAutor = (String) pr.cboAutor.getSelectedItem();
            String idArea = (String) pr.cboArea.getSelectedItem();
            String idEditorial = (String) pr.cboEditorial.getSelectedItem();
            int autor = 0;
            int area = 0;
            int editorial = 0;
            if (idAutor.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione Autor");
                pr.cboAutor.requestFocus();
                return;
            }
            autor = autordao.getIdAutorByNombre(idAutor).get(0).getIdAutor();
            if (idArea.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione Area");
                pr.cboArea.requestFocus();
                return;
            }
            area = areadao.getIdAreaByNombre(idArea).get(0).getIdArea();
            if (idEditorial.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione Editorial");
                pr.cboEditorial.requestFocus();
                return;

            }
            editorial = editorialdao.getIdEditorialByNombre(idEditorial).get(0).getIdEditorial();
            String pais = "";
            if (!pr.txtPais.getText().equals("")) {
                pais = pr.txtPais.getText();
            }

            if (pr.yearPublicacion.getYear() > Integer.parseInt(añoActual)) {
                JOptionPane.showMessageDialog(null, "Seleccione una fecha de publicacion valida");
                pr.yearPublicacion.requestFocus();
                return;
            }
            publicacion = Integer.toString(pr.yearPublicacion.getValue());

            String tipoLibro = "";
            if (pr.cboTipoLibro.getSelectedItem().equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione Tipo de Libro");
                pr.cboTipoLibro.requestFocus();
                return;
            }
            tipoLibro = (String) pr.cboTipoLibro.getSelectedItem();
            String tipoPasta = "";
            if (pr.cboTipoPasta.getSelectedItem().equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione Tipo de pasta");
                pr.cboTipoPasta.requestFocus();
                return;
            }
            tipoPasta = (String) pr.cboTipoPasta.getSelectedItem();
            int precio = 0;
            if (!pr.txtPrecio.getText().equals("")) {
                String Str = pr.txtPrecio.getText();
                String replace = Str.replace(".", "");
                precio = Integer.parseInt(replace);
            }

            if (pr.cldFechaCompra.getDate() != null) {
                fecha = df.format(pr.cldFechaCompra.getDate());
                añoSelc = dfY.format(pr.cldFechaCompra.getDate());
                mesSelect = dfM.format(pr.cldFechaCompra.getDate());
                diaSel = dfD.format(pr.cldFechaCompra.getDate());
            }
            if ((Integer.parseInt(añoSelc) > Integer.parseInt(añoActual)) || (Integer.parseInt(mesSelect) > Integer.parseInt(mesActual)) || (Integer.parseInt(diaSel) > Integer.parseInt(diaActual))) {
                JOptionPane.showMessageDialog(null, "La fecha no debe ser duperior a la actual");
                pr.cldFechaCompra.requestFocus();
                return;
            }

            if (pr.spnEdicion.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese Número de Edición");
                pr.spnEdicion.requestFocus();
                return;
            }
            edicion = pr.spnEdicion.getText();
            int paginas = 0;
            if (pr.txtPaginas.getValue().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese Cantidad de paginas");
                pr.txtPaginas.requestFocus();
                return;

            }
            paginas = (int) pr.txtPaginas.getValue();

            l.setPublicacion(publicacion);
            l.setEdicion(edicion);
            l.setComentarios(comentario);
            l.setFechaCompra(fecha);
            l.setPaginas(paginas);
            l.setPrecio(precio);
            l.setTipoLibro(tipoLibro);
            l.setTipoPasta(tipoPasta);
            l.setIdAutor(autor);
            l.setIdArea(area);
            l.setIdEditorial(editorial);
            l.setPais(pais);
            l.setImportante(importante);
            l.setLargo(foto);
            cr.lblTitulo.setText(l.getTitulo());
            cr.lblAutor.setText(autordao.getAutorById(l.getIdAutor()).get(0).getNombre());
            cr.lblArea.setText(areadao.getAreaById(l.getIdArea()).get(0).getNombre());
            cr.lblEditorial.setText(editorialdao.getEditorialById(l.getIdEditorial()).get(0).getNombre());
            cr.lbltipolibro.setText(l.getTipoLibro());
            cr.lblComentarios.setText(l.getComentarios());
            cr.lblPaginas.setText(Integer.toString(l.getPaginas()));
            cr.lblTipoPasta.setText(l.getTipoPasta());
            cr.lblFecha.setText(l.getFechaCompra());
            cr.lblPais.setText(l.getPais());
            cr.lblPrecio.setText(Integer.toString(l.getPrecio()));
            cr.lblEdicion.setText(l.getEdicion());
            cr.setLocationRelativeTo(null);
            cr.setVisible(true);
            cr.setTitle("Confirmacion de Registro");
        }

        if (e.getSource() == cr.btnConfirm) {
            cr.dispose();
            limpiar();
            String rpta = librodao.create(l, opc);
            if (rpta != null) {
                JOptionPane.showMessageDialog(null, rpta);
                try {
                    cargarLibros(pr.tbLibros);
                } catch (NoSuchFieldException | IOException ex) {
                    Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                limpiarFormLibro();
                enabledBtnPaginator();
                l = null;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Libro..");
            }
        }

        if (e.getSource() == cr.btncancel) {
            limpiar();
            cr.dispose();
        }

        if (e.getSource() == pr.btnAddComment) {
            ac.setLocationRelativeTo(null);
            ac.setVisible(true);
        }

        if (e.getSource() == pr.mnuUpdateLibro) {
            int fila = pr.tbLibros.getSelectedRow();
            if (fila >= 0) {
                opc = "U";
                String idlibro = pr.tbLibros.getValueAt(fila, 0).toString();
                idLibroUpdate = Integer.parseInt(idlibro);
                String idAutor = pr.tbLibros.getValueAt(fila, 2).toString();
                String idEditorial = pr.tbLibros.getValueAt(fila, 3).toString();
                String idArea = pr.tbLibros.getValueAt(fila, 4).toString();
                pr.txttitulo.setText(pr.tbLibros.getValueAt(fila, 1).toString());
                pr.cboAutor.setSelectedItem(idAutor);
                pr.cboArea.setSelectedItem(idArea);
                pr.cboEditorial.setSelectedItem(idEditorial);
                cr.chkImportante.setSelected(librodao.getImportnte(idLibroUpdate).get(0).isImportante());
                pr.yearPublicacion.setValue(Integer.parseInt(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getPublicacion()));
                ac.textAComentarios.setText(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getComentarios());
                comentario = librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getComentarios();
                pr.cboTipoLibro.setSelectedItem(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getTipoLibro());
                pr.cboTipoPasta.setSelectedItem(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getTipoPasta());
                pr.spnEdicion.setText(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getEdicion());
                pr.txtPrecio.setValue(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getPrecio());
                pr.txtPaginas.setValue(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getPaginas());
                try {
                    pr.cldFechaCompra.setDate(df.parse(librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getFechaCompra()));
                } catch (ParseException ex) {
                    System.out.println("error de fecha "+ex);
                }
                try {
                    InputStream img = librodao.getCaratulaByIdLibro(idLibroUpdate).get(0).getCaratula();
                    if (img != null) {
                        BufferedImage bi = ImageIO.read(img);
                        ii = new ImageIcon(bi);
                        Image conver = ii.getImage();
                        Image tam = conver.getScaledInstance(pr.lblCarattula.getWidth(), pr.lblCarattula.getHeight(), Image.SCALE_SMOOTH);
                        iin = new ImageIcon(tam);
                        pr.lblCarattula.setIcon(iin);
                    } else {
                        String path = "/Imagenes/Book.png";
                        URL url = this.getClass().getResource(path);
                        ImageIcon icon = new ImageIcon(url);
                        pr.lblCarattula.setIcon(icon);
                    }

                } catch (NumberFormatException | IOException ex) {
                    System.out.println("error aqui" + ex);
                }

            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }
        if (e.getSource() == ac.btAddCo) {
            if (!ac.textAComentarios.getText().equals("")) {
                comentario = ac.textAComentarios.getText();
                ac.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el comentario");
                ac.textAComentarios.requestFocus();
            }

        }
        if (e.getSource() == ac.btnCancelComm) {
            ac.textAComentarios.setText("");
            comentario = "";
            ac.dispose();
        }

        if (e.getSource() == pr.mnuDeleteLibro) {
            int fila = pr.tbLibros.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    String rptaDelete = librodao.deleteLibro(Integer.parseInt(pr.tbLibros.getValueAt(fila, 0).toString()));
                    if (rptaDelete != null) {
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        try {
                            cargarLibros(pr.tbLibros);
                        } catch (NoSuchFieldException | IOException ex) {
                            Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == pr.btnCancelar) {
            limpiarFormLibro();
        }

        if (e.getSource() == pr.btnAdelante) {
            pagina = pagina + 1;
            try {
                cargarLibros(pr.tbLibros);
            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (pagina >= librodao.totalPaginas(dato)) {
                pr.btnAdelante.setEnabled(false);
                pr.btnUltimo.setEnabled(false);
            }
            if (pagina > 1) {
                pr.btnAtras.setEnabled(true);
                pr.btnPrimero.setEnabled(true);
            }
        }
        if (e.getSource() == pr.btnAtras) {
            pagina = pagina - 1;
            try {
                cargarLibros(pr.tbLibros);
            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (pagina <= 1) {
                pr.btnAtras.setEnabled(false);
                pr.btnPrimero.setEnabled(false);
            }
            if (pagina < librodao.totalPaginas(dato)) {
                pr.btnAdelante.setEnabled(true);
                pr.btnUltimo.setEnabled(true);
            }
//            System.out.println("pagina = " + pagina + "------- total paginas = " + admDao.cantidadRegistros());
        }
        if (e.getSource() == pr.btnUltimo) {
            pagina = librodao.totalPaginas(dato);
            try {
                cargarLibros(pr.tbLibros);
            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (pagina >= librodao.totalPaginas(dato)) {
                pr.btnAdelante.setEnabled(false);
                pr.btnUltimo.setEnabled(false);
            }
            if (pagina > 1) {
                pr.btnAtras.setEnabled(true);
                pr.btnPrimero.setEnabled(true);
            }
        }
        if (e.getSource() == pr.btnPrimero) {
            pagina = 1;
            try {
                cargarLibros(pr.tbLibros);
            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (pagina <= 1) {
                pr.btnAtras.setEnabled(false);
                pr.btnPrimero.setEnabled(false);
            }
            if (pagina < librodao.totalPaginas(dato)) {
                pr.btnAdelante.setEnabled(true);
                pr.btnUltimo.setEnabled(true);
            }

        }

    }

    private void addFilter() {
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.PNG)", "png"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.JPG)", "jpg"));
    }
//Eventos del mouse

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == pr.lblCarattula) {
            countAction++;
            File archivo;
            if (countAction == 1) {
                addFilter();
            }
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (FileChooser.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
                archivo = FileChooser.getSelectedFile();
                if (archivo.getName().endsWith("png") || archivo.getName().endsWith("PNG") || archivo.getName().endsWith("jpg")) {
                    foto = String.valueOf(archivo);
                    ImageIcon icon = new ImageIcon(archivo.toString());
                    Image conver = icon.getImage();
                    Image tam = conver.getScaledInstance(pr.lblCarattula.getWidth(), pr.lblCarattula.getHeight(), Image.SCALE_SMOOTH);
                    iin = new ImageIcon(tam);
                    pr.lblCarattula.setIcon(iin);
                    String NombreArchivo = FileChooser.getName(archivo);
                    JOptionPane.showMessageDialog(null, "Archivo Seleccionado: " + String.valueOf(NombreArchivo));
                } else {
                    JOptionPane.showMessageDialog(null, "Elija un formato valido");
                }
            } else {

            }
        }

    }

    public void limpiar() {
        cr.lblTitulo.setText("");
        cr.lblAutor.setText("");
        cr.lblArea.setText("");
        cr.lblEditorial.setText("");
        cr.lbltipolibro.setText("");
        cr.lblComentarios.setText("");
        cr.lblPaginas.setText("");
        cr.lblTipoPasta.setText("");
        cr.lblFecha.setText("");
        cr.lblPais.setText("");
        cr.lblPrecio.setText("");
        cr.lblEdicion.setText("");
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void limpiarFormLibro() {
        String añoActual = dfY.format(date);
        foto = "";
        pr.txttitulo.setText("");
        pr.txttitulo.requestFocus();
        pr.cboArea.setSelectedItem("-- Seleccione --");
        pr.cboAutor.setSelectedItem("-- Seleccione --");
        pr.cboEditorial.setSelectedItem("-- Seleccione --");
        pr.cboTipoLibro.setSelectedItem("-- Seleccione --");
        pr.cboTipoPasta.setSelectedItem("-- Seleccione --");
        pr.txtPrecio.setText("");
        pr.txtPaginas.setValue(50);
        pr.cldFechaCompra.setDate(date);
        pr.txtPais.setText("");
        pr.yearPublicacion.setYear(Integer.parseInt(añoActual));
        pr.spnEdicion.setText("");
        ac.textAComentarios.setText("");
        cr.chkImportante.setSelected(false);
        importante = false;
        String path = "/Imagenes/Book.png";
        URL url = this.getClass().getResource(path);
        ImageIcon icon = new ImageIcon(url);
        pr.lblCarattula.setIcon(icon);
        idLibroUpdate = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == pr.txtBuscarLibro) {
            dato = pr.txtBuscarLibro.getText();
            if (dato.equals("")) {
                pagina = 1;
            }
            try {
                cargarLibros(pr.tbLibros);

            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(LibroController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            if (librodao.totalPaginas(dato) <= 1) {
                pr.btnAdelante.setEnabled(false);
                pr.btnUltimo.setEnabled(false);
                pr.btnAtras.setEnabled(false);
                pr.btnPrimero.setEnabled(false);
            }
            if (librodao.totalPaginas(dato) > 1) {
                pr.btnAdelante.setEnabled(true);
                pr.btnUltimo.setEnabled(true);
                pagina = 1;
            }
        }

    }

    public void enabledBtnPaginator() {

        if (pagina >= librodao.totalPaginas("")) {
            pr.btnAdelante.setEnabled(false);
            pr.btnUltimo.setEnabled(false);
        }
        if (pagina == 1) {
            pr.btnAtras.setEnabled(false);
            pr.btnPrimero.setEnabled(false);
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cr.chkImportante) {
            if (cr.chkImportante.isSelected()) {
                importante = true;                
//                System.out.println("aqui");
            } else {
                importante = false;                
//                System.out.println("no se");
            }
        }
    }

    //método para pasar a números romanos
    public String convertirANumerosRomanos(int numero) {
        int i, miles, centenas, decenas, unidades;
        String romano = "";
        //obtenemos cada cifra del número
        miles = numero / 1000;
        centenas = numero / 100 % 10;
        decenas = numero / 10 % 10;
        unidades = numero % 10;

        //millar
        for (i = 1; i <= miles; i++) {
            romano = romano + "M";
        }

        //centenas
        if (centenas == 9) {
            romano = romano + "CM";
        } else if (centenas >= 5) {
            romano = romano + "D";
            for (i = 6; i <= centenas; i++) {
                romano = romano + "C";
            }
        } else if (centenas == 4) {
            romano = romano + "CD";
        } else {
            for (i = 1; i <= centenas; i++) {
                romano = romano + "C";
            }
        }

        //decenas
        if (decenas == 9) {
            romano = romano + "XC";
        } else if (decenas >= 5) {
            romano = romano + "L";
            for (i = 6; i <= decenas; i++) {
                romano = romano + "X";
            }
        } else if (decenas == 4) {
            romano = romano + "XL";
        } else {
            for (i = 1; i <= decenas; i++) {
                romano = romano + "X";
            }
        }

        //unidades
        if (unidades == 9) {
            romano = romano + "IX";
        } else if (unidades >= 5) {
            romano = romano + "V";
            for (i = 6; i <= unidades; i++) {
                romano = romano + "I";
            }
        } else if (unidades == 4) {
            romano = romano + "IV";
        } else {
            for (i = 1; i <= unidades; i++) {
                romano = romano + "I";
            }
        }
        return romano;
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == pr.spnEdicion) {
            if (!pr.spnEdicion.getText().equals("")) {
                if (isNumeric(pr.spnEdicion.getText())) {
                    String n = trim(pr.spnEdicion.getText());
                    String r = convertirANumerosRomanos(Integer.parseInt(n));
                    pr.spnEdicion.setText(r);
                } else {
                    pr.spnEdicion.setText("");
                }
            }

        }

    }

    public static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

}
