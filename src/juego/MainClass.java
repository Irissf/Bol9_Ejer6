package juego;

//Letra del juego https://www.dafont.com/es/berkahi-blackletter.font

import javax.swing.JFrame;

public class MainClass {

    public static void main(String[] args) { // si le paso algo del array es que hay usuarios

        Ventana ventana = new Ventana();
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(528, 1000); // pues a ojo al final
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

    }
}