package gameoflife;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * GUI of the program, with matrix and control buttons. Original written on 28-02-2014
 * @see ButtonListener
 * @version 10102018
 * @author Federico Matteoni
 */
public class GUIClass extends JFrame implements java.io.Serializable {
    /**
     * Matrix columns
     */
    public final int colonne;
    /**
     * Matrix rows
     */
    public final int righe;
    /**
     * Boolean matrix, analyzed to get next generation
     * @see ButtonListener
     */
    public boolean[][] statoprecedente;
    /**
     * Button matrix, to select starting state and visualizing progression
     * @see ButtonListener
     */
    public JButton[][] matrice = null;
    /**
     * Starts the algorithm
     */
    public JButton avvia = null;
    /**
     * Randomize the matrix, for science
     */
    public JButton casuale = null;
    /**
     * Stops the algorithm
     */
    public JButton ferma = null;
    /**
     * Serialize class on file
     */
    public JButton salva = null;
    /**
     * Clears the buttons matrix
     */
    public JButton clear = null;
    /**
     * Contains the matrix, with FlowLayout for a "simpler" visualization
     */
    public JPanel pnlMatrice = null;
    /**
     * Contains pnlComandiA and pnlComandiB
     */
    public JPanel pnlComandi = null;
    /**
     * Primary container
     */
    public Container cnt = null;
    
    /**
     * Initializes buttons, matrix, listeners, action commands and panels
     */
    public GUIClass() {
        super("The Game of Life");
        ButtonListener listener = new ButtonListener(this); //ButtonListener
        righe = 20; colonne = 20;
        matrice = new JButton[righe][colonne];  //Matrix initialization
        avvia = new JButton("Simulate");
        avvia.setBackground(Color.LIGHT_GRAY);
        avvia.addActionListener(listener);
        avvia.setActionCommand("avvia");        //Buttons initializations
        casuale = new JButton("Randomize");
        casuale.setBackground(Color.LIGHT_GRAY);
        casuale.addActionListener(listener);
        casuale.setActionCommand("random");
        ferma = new JButton("Stop");
        ferma.setBackground(Color.LIGHT_GRAY);
        ferma.addActionListener(listener);
        ferma.setActionCommand("ferma");
        salva = new JButton("Save");
        salva.setBackground(Color.LIGHT_GRAY);
        salva.addActionListener(listener);
        salva.setActionCommand("salva");
        clear = new JButton("Clear");
        clear.setBackground(Color.LIGHT_GRAY);
        clear.addActionListener(listener);
        clear.setActionCommand("clear");
        
        pnlMatrice = new JPanel();
        pnlMatrice.setLayout(new GridLayout(righe, colonne));
        pnlMatrice.setBackground(Color.WHITE);
        statoprecedente = new boolean[righe][colonne];  //The matrices I'll need in the algorithm
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                statoprecedente[i][j] = false;      //Everything is false because life (no cell has been selected and the simulations has not yet started
                matrice[i][j] = new JButton();
                matrice[i][j].setBackground(Color.WHITE);
                matrice[i][j].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                matrice[i][j].addActionListener(listener);
                matrice[i][j].setActionCommand(i + " " + j);
                pnlMatrice.add(matrice[i][j]);  //Assigning a listener to each matrix button and an action command which contains the coordinates
            }
        }
        pnlComandi = new JPanel();
        pnlComandi.setLayout(new FlowLayout());
        pnlComandi.add(avvia);
        pnlComandi.add(ferma);
        pnlComandi.add(casuale);
        pnlComandi.add(clear);
        pnlComandi.add(salva);
        cnt = getContentPane();
        cnt.setLayout(new BorderLayout());
        cnt.add(pnlMatrice, BorderLayout.CENTER);
        cnt.add(pnlComandi, BorderLayout.SOUTH);    //Initializing panels and container
    }
    
    /**
     * Loads GUI from file or initializes it with its method, sets defaultCloseOperation, visibility and dimension
     * @param args Parametri da riga di comando
     */
    public static void main(String[] args) {
        GUIClass win = GUIClass.ReadState();
        if (win == null)
        {
            win = new GUIClass();
        }
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);
        //win.setSize((win.matrice[0][0].getWidth() * win.colonne + 5*win.colonne + 18), (win.matrice[0][0].getHeight()*win.righe + 5*win.righe + 67));
        win.setSize(450, 450);
    }
    
    /**
     * Serializes GUI on a binary file named "gameoflife.bin"
     */
    public void SaveState() {
        try {
            ObjectOutputStream saveStream = new ObjectOutputStream(new FileOutputStream("gameoflife.bin"));
            saveStream.writeObject(this);
            saveStream.close();
        }
        catch (IOException ex) {}
    }
    
    /**
     * Deserializes GUI from binary file "gameoflife.bin"
     * @return La matrice con listener inizializzati o null nel caso di errore
     */
    public static GUIClass ReadState() {
        try {
            ObjectInputStream inObjStream = new ObjectInputStream(new FileInputStream("gameoflife.bin"));
            GUIClass state = (GUIClass)inObjStream.readObject();
            inObjStream.close();
            ButtonListener listener = new ButtonListener(state);
            state.avvia.addActionListener(listener);
            state.avvia.setActionCommand("avvia");
            state.casuale.addActionListener(listener);
            state.casuale.setActionCommand("random");
            state.ferma.addActionListener(listener);
            state.ferma.setActionCommand("ferma");
            state.salva.addActionListener(listener);
            state.salva.setActionCommand("salva");
            state.clear.addActionListener(listener);
            state.clear.setActionCommand("clear");
            
            for (int i = 0; i < state.righe; i++) {
                for (int j = 0; j < state.colonne; j++) {   
                    state.matrice[i][j].addActionListener(listener);
                    state.matrice[i][j].setActionCommand(i + " " + j);
                }
            }
            return state;
        }
        catch (IOException | ClassNotFoundException ex){ return null; }
    }
}
