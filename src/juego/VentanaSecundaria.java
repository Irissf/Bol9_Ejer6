package juego;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class VentanaSecundaria extends JDialog implements ActionListener {

    private JTextArea lblTexto;
    private JLabel lblYourLost;
    private JLabel lblSalir;
    private JLabel lblReiniciar;

    Timer quitarLabel;
    int segundos = 0;

    String texto;
    String[] textoSeparado;
    ArrayList<String[]> usuarios = new ArrayList<>();
    int mayor;
    int indice;
    int contador = 0;
    boolean valor = false;
    String[] comparar;
    String[][] definitiva = new String[5][5];

    public VentanaSecundaria(Ventana f) {
        super(f, true);
        this.setLayout(null);

        // pantalla perder

        lblYourLost = new JLabel();
        lblYourLost.setSize(328, 800);
        lblYourLost.setLocation(50, 25);
        lblYourLost.setIcon(new ImageIcon("src/juego/graficos/pantallaPerdida.gif"));

        this.add(lblYourLost);
        quitarLabel = new Timer(1000, this);
        quitarLabel.start();

        // botones

        lblSalir = new JLabel();
        lblSalir.setSize(400, 64);
        lblSalir.setLocation(5, 30);
        lblSalir.setIcon(new ImageIcon("src/juego/graficos/salirNormal.png"));
        lblSalir.addMouseListener(new RatonEncima());
        lblSalir.addMouseMotionListener(new RatonEncima());
        lblSalir.setVisible(false);
        this.add(lblSalir);

        lblReiniciar = new JLabel();
        lblReiniciar.setSize(400, 64);
        lblReiniciar.setLocation(5, 100);
        lblReiniciar.setVisible(false);
        lblReiniciar.addMouseListener(new RatonEncima());
        lblReiniciar.addMouseMotionListener(new RatonEncima());
        lblReiniciar.setIcon(new ImageIcon("src/juego/graficos/reiniciarNormal.png"));
        this.add(lblReiniciar);

        try (Scanner sc = new Scanner(new File("src/juego/archivo/registros.txt"))) {
            while (sc.hasNext()) {
                texto = sc.nextLine();
                textoSeparado = texto.split("-");
                usuarios.add(textoSeparado);
            }
        } catch (IOException e) {
            System.err.println("Error de acceso al archivo");
        }

        while (contador < 5) {
            if (usuarios.size() != 0) {
                for (int i = 0; i < usuarios.size(); i++) {
                    comparar = usuarios.get(i);
                    if (valor) {
                        if (mayor < Integer.parseInt(comparar[0])) {
                            mayor = Integer.parseInt(comparar[0]);
                            indice = i;
                        }
                    } else {
                        mayor = Integer.parseInt(comparar[0]);
                        indice = i;
                        valor = true;
                    }
                }
            }
            definitiva[contador] = usuarios.get(indice);
            usuarios.remove(indice);
            contador++;
            valor = false;
        }

        // Top 5 de la lista de ganadores. Añadir un botón para consultar lista completa
        lblTexto = new JTextArea("¡Las cinco mejores puntuaciones!\n\n");
        lblTexto.setEditable(false);
        for (int i = 0; i < definitiva.length; i++) {
            lblTexto.append(String.format("%10s\tPuntuación:%s\t%10s/%s/%s%n", definitiva[i][1],definitiva[i][0],definitiva[i][2],definitiva[i][3],definitiva[i][4]));    
        }
        lblTexto.setForeground(new Color(206,150, 88));
        lblTexto.setBackground(new Color(63, 61, 78));
        lblTexto.setFont(new Font("Georgia", Font.BOLD, 14));
        lblTexto.setSize(400, 500);
        lblTexto.setLocation(5, 200);
        lblTexto.setVisible(false);
        this.add(lblTexto);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.segundos == 1) {
            this.lblYourLost.setVisible(false);
            this.lblReiniciar.setVisible(true);
            this.lblSalir.setVisible(true);
            this.lblTexto.setVisible(true);
            this.quitarLabel.stop();
        }
        this.segundos++;
    }

    private class RatonEncima extends MouseAdapter {

        @Override
        public void mouseExited(final java.awt.event.MouseEvent e) {
            if (e.getSource() == VentanaSecundaria.this.lblReiniciar) {
                VentanaSecundaria.this.lblReiniciar.setIcon(new ImageIcon("src/juego/graficos/reiniciarNormal.png"));
            } else {
                VentanaSecundaria.this.lblSalir.setIcon(new ImageIcon("src/juego/graficos/salirNormal.png"));
            }
        }

        @Override
        public void mouseMoved(final java.awt.event.MouseEvent e) {
            if (e.getSource() == VentanaSecundaria.this.lblReiniciar) {
                VentanaSecundaria.this.lblReiniciar.setIcon(new ImageIcon("src/juego/graficos/reiniciarEncima.png"));
            } else {
                VentanaSecundaria.this.lblSalir.setIcon(new ImageIcon("src/juego/graficos/salirEncima.png"));
            }
        }

        @Override
        public void mouseClicked(final java.awt.event.MouseEvent e) {
            if (e.getSource() == VentanaSecundaria.this.lblReiniciar) {
                VentanaSecundaria.this
                        .processWindowEvent(new WindowEvent(VentanaSecundaria.this, WindowEvent.WINDOW_CLOSING));
                MainClass.main(null);
            } else {
                System.exit(0);
            }

        }

        @Override
        public void mousePressed(final java.awt.event.MouseEvent e) {
            if (e.getSource() == VentanaSecundaria.this.lblReiniciar) {
                VentanaSecundaria.this.lblReiniciar.setIcon(new ImageIcon("src/juego/graficos/reiniciarPulsado.png"));
            } else {
                VentanaSecundaria.this.lblSalir.setIcon(new ImageIcon("src/juego/graficos/salirPulsado.png"));
            }
        }

        @Override
        public void mouseReleased(final java.awt.event.MouseEvent e) {
            if (e.getSource() == VentanaSecundaria.this.lblReiniciar) {
                VentanaSecundaria.this.lblReiniciar.setIcon(new ImageIcon("src/juego/graficos/reiniciarPulsado.png"));
            } else {
                VentanaSecundaria.this.lblSalir.setIcon(new ImageIcon("src/juego/graficos/salirPulsado.png"));
            }
        }

    }

}