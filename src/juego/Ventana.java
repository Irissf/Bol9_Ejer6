package juego;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Ventana extends JFrame implements ActionListener {

    private JLabel fondo;
    private JLabel lblPeste;
    private JLabel lblMedico;
    private JLabel lblIzq;
    private JLabel lblDecha;
    private JLabel contadorBombas;
    Timer temporizador;

    int segundo;
    int[] posiciones = { 0, 64, 128, 192, 256, 320, 384 };
    int posicionX = 0;
    int posicionY = 512;
    int fila = 0;
    int columna = 0;
    int contador = 0;
    int velocidad = 5;
    boolean vivo = true; // consejo de Raúl
    int puntuacion = 0;
    int puntos = 1;
    int contadorBombasInfo = 0;

    boolean usuario = false;
    String nombreUsuario = "--";

    String ruta = "src/juego/archivo/registros.txt";
    File archivo = new File(ruta);

    // Ayuda obtenida de http://lineadecodigo.com/java/obtener-fecha-actual-con-java/
    Calendar fecha = new GregorianCalendar();
    String dia = Integer.toString(fecha.get(Calendar.DATE));
    int mesFalso = fecha.get(Calendar.MONTH) + 1;
    String mes = Integer.toString(mesFalso); 
    String ano = Integer.toString(fecha.get(Calendar.YEAR));
    

    public Ventana() {

        super();
        this.setLayout(null);

        System.err.println("mando a inicio");
        IniciarSesion inicio = new IniciarSesion(this);
        inicio.setSize(428, 428);
        inicio.setLocationRelativeTo(this);
        inicio.getContentPane().setBackground(new Color(126, 109, 146));
        inicio.setVisible(true);
        System.err.println(nombreUsuario);

        //contador del numero de bombas
        contadorBombas = new JLabel();
        contadorBombas.setSize(30, 30);
        contadorBombas.setLocation(10, 10);
        contadorBombas.setFont(new Font("Georgia", Font.BOLD, 20));
        this.add(contadorBombas);

        // componente medico
        lblMedico = new JLabel();
        lblMedico.setSize(128, 320);
        lblMedico.setLocation(posicionX, posicionY);
        lblMedico.setIcon(new ImageIcon("src/juego/graficos/muneco.png"));
        this.add(lblMedico);

        // componente botones
        lblIzq = new JLabel();
        lblIzq.setSize(256, 128);
        lblIzq.setLocation(0, 832);
        lblIzq.setIcon(new ImageIcon("src/juego/graficos/botonIZQRatonFuera.png"));
        lblIzq.addMouseListener(new RatonEncima());
        lblIzq.addMouseMotionListener(new RatonEncima());

        this.add(lblIzq);

        lblDecha = new JLabel();
        lblDecha.setSize(256, 128);
        lblDecha.setLocation(256, 832);
        lblDecha.setIcon(new ImageIcon("src/juego/graficos/botonDCHAatonFuera.png"));

        lblIzq.addMouseListener(new RatonEncima());
        lblIzq.addMouseMotionListener(new RatonEncima());
        lblDecha.addMouseListener(new RatonEncima());
        lblDecha.addMouseMotionListener(new RatonEncima());
        this.add(lblDecha);

        // Virus de la peste
        lblPeste = new JLabel();
        this.add(lblPeste);
        temporizador = new Timer(20, this);
        temporizador.start();

        // Componente fondo
        fondo = new JLabel();
        fondo.setSize(512, 1000);
        fondo.setLocation(0, 0);
        fondo.setIcon(new ImageIcon("src/juego/graficos/fondo.png"));
        this.add(fondo);

        this.addKeyListener(new tecladoAccion());

    }

    // RATÓN===============================================================

    private class RatonEncima extends MouseAdapter {

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            if (e.getSource() == Ventana.this.lblDecha) {
                Ventana.this.lblDecha.setIcon(new ImageIcon("src/juego/graficos/botonDCHAatonFuera.png"));
            } else {
                Ventana.this.lblIzq.setIcon(new ImageIcon("src/juego/graficos/botonIZQRatonFuera.png"));
            }
        }

        @Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
            if (e.getSource() == Ventana.this.lblDecha) {
                Ventana.this.lblDecha.setIcon(new ImageIcon("src/juego/graficos/botonDCHARatonEncima.png"));
            } else {
                Ventana.this.lblIzq.setIcon(new ImageIcon("src/juego/graficos/botonIZQRatonEncima.png"));
            }
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getSource() == Ventana.this.lblDecha) {
                if (Ventana.this.posicionX != 384 && vivo) {
                    Ventana.this.posicionX = posicionX + 64;
                    Ventana.this.lblMedico.setLocation((Ventana.this.posicionX), 512);
                }
            } else {
                if (Ventana.this.posicionX != 0 && vivo) {
                    Ventana.this.posicionX = posicionX - 64;
                    Ventana.this.lblMedico.setLocation((Ventana.this.posicionX), 512);
                }
            }
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (e.getSource() == Ventana.this.lblDecha) {
                Ventana.this.lblDecha.setIcon(new ImageIcon("src/juego/graficos/botonDCHAPulsado.png"));
            } else {
                Ventana.this.lblIzq.setIcon(new ImageIcon("src/juego/graficos/botonIZQPulsado.png"));
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.getSource() == Ventana.this.lblDecha) {
                Ventana.this.lblDecha.setIcon(new ImageIcon("src/juego/graficos/botonDCHARatonEncima.png"));
            } else {
                Ventana.this.lblIzq.setIcon(new ImageIcon("src/juego/graficos/botonIZQRatonEncima.png"));
            }
        }

    }
    // TECLADO=============================================================

    private class tecladoAccion extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                if (Ventana.this.posicionX != 0 && vivo) {
                    Ventana.this.posicionX = posicionX - 64;
                    Ventana.this.lblMedico.setLocation((Ventana.this.posicionX), 512);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                if (Ventana.this.posicionX != 384 && vivo) {
                    Ventana.this.posicionX = posicionX + 64;
                    Ventana.this.lblMedico.setLocation((Ventana.this.posicionX), 512);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int random;
        this.contadorBombas.setText(Integer.toString(contadorBombasInfo));
        if (segundo == 0) {
            random = (int) (Math.random() * 6);
            this.columna = this.posiciones[random];
            lblPeste.setSize(128, 128);
            lblPeste.setLocation(columna, fila);
            lblPeste.setIcon(new ImageIcon("src/juego/graficos/virus.png"));
            this.segundo++;
        } else {
            this.fila = this.fila + velocidad;
            lblPeste.setLocation(this.columna, this.fila);
            this.segundo++;
            if (this.fila >= 384 && this.fila <= 768) { // solo empezará a comprobar cuando la altura sea la adecuada
                                                        // para poder coincidir con el médico en todos sus vertices
                //aqui pierde
                if (this.columna == this.posicionX || this.columna == (this.posicionX + 64)
                        || (this.columna + 128) == (this.posicionX + 64)) { 
                    this.vivo = false;
                    this.remove(lblMedico);
                    this.remove(lblPeste);
                    System.err.println(puntuacion);
                    this.temporizador.stop();

                    //archivo de puntuaciones
                    try (PrintWriter f = new PrintWriter(new FileWriter(archivo,true))){
                        f.printf("%d-%s-%s-%s-%s\n", puntuacion,nombreUsuario,dia,mes,ano);
                    } catch (IOException except) {
                        System.err.println("error de acceso al archivo");
                    }

                    // abrimos la ventana secundaria
                    VentanaSecundaria secundaria = new VentanaSecundaria(this);
                    secundaria.setSize(428, 900);
                    secundaria.setLocationRelativeTo(this);
                    secundaria.getContentPane().setBackground(new Color(43, 43, 42));
                    secundaria.setVisible(true);
                }
            }
            //aqui no perdió
            if (this.fila > 768) {
                contadorBombasInfo ++;
                this.contador++;
                puntuacion = puntuacion + puntos;
                if (contador == 5 && velocidad < 18) {
                    this.velocidad = velocidad + 2;
                    contador = 0;
                    puntos++;
                }
                segundo = 0;
                fila = 0;
                columna = 0;
            }
        }

    }
}