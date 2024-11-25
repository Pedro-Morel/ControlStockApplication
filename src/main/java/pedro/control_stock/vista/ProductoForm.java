package pedro.control_stock.vista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pedro.control_stock.modelo.Producto;
import pedro.control_stock.servicio.ProductoServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ProductoForm extends JFrame {

    ProductoServicio productoServicio;
    private JPanel panel;
    private JTable tablaProductos;
    private JTextField idTexto;
    private JTextField productoTexto;
    private JTextField proveedorTexto;
    private JTextField precioTexto;
    private JTextField stockTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloProductos;

    @Autowired
    public ProductoForm(ProductoServicio productoServicio){
        this.productoServicio = productoServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarProducto());
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarProductoSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarProducto());
        eliminarButton.addActionListener(e -> eliminarProducto());
    }

    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth()/2);
        int y = (tamanioPantalla.height - getHeight()/2);
        setLocation(x, y);
    }

    private void agregarProducto(){
        if(productoTexto.getText().equals("")){
            mostrarMensaje("Proporcione el nombre del producto");
            productoTexto.requestFocusInWindow();
            return;
        }
        var nombreProducto = productoTexto.getText();
        var proveedor = proveedorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var stock = Integer.parseInt(stockTexto.getText());

        var producto = new Producto();
        producto.setNombreProducto(nombreProducto);
        producto.setProveedor(proveedor);
        producto.setPrecio(precio);
        producto.setStock(stock);
        this.productoServicio.guardarProducto(producto);
        mostrarMensaje("Se agrego el producto");
        limpiarFormulario();
        listarProductos();
    }

    private void cargarProductoSeleccionado(){
        var renglon = tablaProductos.getSelectedRow();
        if(renglon != -1){
            String idProducto = tablaProductos.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idProducto);
            String nombreProducto = tablaProductos.getModel().getValueAt(renglon, 1).toString();
            productoTexto.setText(nombreProducto);
            String proveedor = tablaProductos.getModel().getValueAt(renglon, 2).toString();
            proveedorTexto.setText(proveedor);
            String precio = tablaProductos.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(precio);
            String stock = tablaProductos.getModel().getValueAt(renglon, 4).toString();
            stockTexto.setText(stock);

        }
    }

    private void modificarProducto(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro");
        }
        else{
            if(productoTexto.getText().equals("")){
                mostrarMensaje("Proporcione el nombre del producto");
                productoTexto.requestFocusInWindow();
                return;
            }
            int idProducto = Integer.parseInt(idTexto.getText());
            var nombreProducto = productoTexto.getText();
            var proveedor = proveedorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var stock = Integer.parseInt(stockTexto.getText());
            var producto = new Producto(idProducto, nombreProducto, proveedor, precio, stock);
            productoServicio.guardarProducto(producto);
            mostrarMensaje("Se modifico el producto");
            limpiarFormulario();
            listarProductos();
        }
    }

    private void eliminarProducto(){
        var renglon = tablaProductos.getSelectedRow();
        if(renglon != -1){
            String idProducto = tablaProductos.getModel().getValueAt(renglon, 0).toString();
            var producto = new Producto();
            producto.setIdProducto(Integer.parseInt(idProducto));
            productoServicio.eliminarProducto(producto);
            mostrarMensaje("Se ha eliminado el producto " + idProducto);
            limpiarFormulario();
            listarProductos();
        }
        else{
            mostrarMensaje("No se ha seleccionado ningun producto");
        }
    }

    private void limpiarFormulario(){
        productoTexto.setText("");
        proveedorTexto.setText("");
        precioTexto.setText("");
        stockTexto.setText("");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // Creacion del idProducto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloProductos = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[] cabeceros = {"Id", "Producto", "Proveedor", "Precio", "Stock"};
        this.tablaModeloProductos.setColumnIdentifiers(cabeceros);
        this.tablaProductos = new JTable(tablaModeloProductos);
        listarProductos();
    }

    private void listarProductos(){
        tablaModeloProductos.setRowCount(0);
        var productos = productoServicio.listarProductos();
        productos.forEach((producto)->{
            Object[] renglonProducto = {
                    producto.getIdProducto(),
                    producto.getNombreProducto(),
                    producto.getProveedor(),
                    producto.getPrecio(),
                    producto.getStock()
            };
            this.tablaModeloProductos.addRow(renglonProducto);
        });
    }
}
