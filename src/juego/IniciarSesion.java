package juego;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class IniciarSesion extends JDialog implements ActionListener {

    private JComboBox<String> usuarios;
    private JTextField nombre;
    private JButton sesion;
    private JButton eliminarUsuario;
    private JButton crearUsuario;

    ArrayList<String> nombres = new ArrayList<>();
    String ruta = "src/juego/archivo/usuarios.txt";
    File archivo = new File(ruta);
    String texto;

    public IniciarSesion(Ventana f) {
        super(f, true);
        this.setLayout(null);
        System.err.println("creo la pantalla");

        // combobox: usuarios
        try (Scanner pw = new Scanner(new File(ruta))) {
            // llenar el combobox con los usuarios.
            while (pw.hasNext()) {
                texto = pw.nextLine();
                nombres.add(texto);
            }
        } catch (IOException e) {
            System.err.println("Error de acceso al archivo, Iris ¬¬");
        }
        usuarios = new JComboBox<String>();
        for (int i = 0; i < nombres.size(); i++) {
            usuarios.addItem(nombres.get(i));
        }
        usuarios.setSize(350, 30);
        usuarios.setLocation(25, 25);
        this.add(usuarios);
        nombres.clear();

        // textfiel: nombre

        nombre = new JTextField();
        nombre.setSize(350, 30);
        nombre.setLocation(25, 70);
        this.add(nombre);

        // botones

        sesion = new JButton("Iniciar Sesión");
        sesion.setFont(new Font("Georgia", Font.PLAIN, 15));
        sesion.setSize(350, 50);
        sesion.setLocation(25, 130);
        sesion.addActionListener(this);
        this.add(sesion);

        crearUsuario = new JButton("Crear Usuario");
        crearUsuario.setFont(new Font("Georgia", Font.PLAIN, 15));
        crearUsuario.setSize(350, 50);
        crearUsuario.setLocation(25, 200);
        crearUsuario.addActionListener(this);
        this.add(crearUsuario);

        eliminarUsuario = new JButton("Eliminar Usuario");
        eliminarUsuario.setFont(new Font("Georgia", Font.PLAIN, 15));
        eliminarUsuario.setSize(350, 50);
        eliminarUsuario.setLocation(25, 270);
        eliminarUsuario.addActionListener(this);
        this.add(eliminarUsuario);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sesion) {
            System.err.println("entro aqui");
            String nombre =  this.usuarios.getItemAt(this.usuarios.getSelectedIndex()); 
            Ventana ventana = (Ventana)this.getOwner();
            ventana.nombreUsuario = nombre;
            try (PrintWriter f = new PrintWriter(new FileWriter(archivo))) {
                for (int i = 0; i < usuarios.getItemCount(); i++) {
                   f.println(usuarios.getItemAt(i)); 
                }
            } catch (IOException except) {
               System.err.println("Error de accedo al archivo");
            }
            IniciarSesion.this.processWindowEvent(new WindowEvent(IniciarSesion.this, WindowEvent.WINDOW_CLOSING));
        }
        if (e.getSource() == crearUsuario || e.getSource() == nombre) {
            this.usuarios.addItem(this.nombre.getText());
        }
        if (e.getSource() == eliminarUsuario) {
            this.usuarios.removeItemAt(this.usuarios.getSelectedIndex());
        }


    }

}