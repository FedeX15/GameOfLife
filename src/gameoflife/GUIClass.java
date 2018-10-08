package gameoflife;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * GUI del programma, con la matrice di bottoni e i pulsanti avvia e randomizza
 * @see ButtonListener
 * @version 28022014
 * @author Federico Matteoni
 */
public class GUIClass extends JFrame implements java.io.Serializable {
    /**
     * Le colonne della matrice
     */
    public final int colonne;
    /**
     * Le righe della matrice
     */
    public final int righe;
    /**
     * Matrice analizzata per creare la generazione successiva
     * @see ButtonListener
     */
    public boolean[][] statoprecedente;
    /**
     * Matrice di pulsanti, per selezionare le cellule di partenza e visualizzarne la progressione
     * @see ButtonListener
     */
    public JButton[][] matrice = null;
    /**
     * Avvia l'algoritmo del Game of Life
     */
    public JButton avvia = null;
    /**
     * Randomizza la matrice, for science
     */
    public JButton casuale = null;
    /**
     * Ferma l'algoritmo
     */
    public JButton ferma = null;
    /**
     * Salva la classe su file, serializazzione
     */
    public JButton salva = null;
    /**
     * Contiene la matrice, con FlowLayout per una più "semplice" visualizzazione
     */
    public JPanel pnlMatrice = null;
    /**
     * Contiene pnlComandiA e pnlComandiB
     */
    public JPanel pnlComandi = null;
    /**
     * Container principale
     */
    public Container cnt = null;
    
    /**
     * Inizializzazione pulsanti-comandi, matrice, settaggio listener e actionCommand, inizializzazione pannelli, riempimento e posizionamento
     */
    public GUIClass() {
        super("The Game of Life");
        ButtonListener listener = new ButtonListener(this); //Listener dei pulsanti, classe ButtonListner
        righe = 20; colonne = 20;
        matrice = new JButton[righe][colonne];  //Inizializzo la matrice
        avvia = new JButton("Simula");
        avvia.setBackground(Color.LIGHT_GRAY);
        avvia.addActionListener(listener);
        avvia.setActionCommand("avvia");        //Inizializzazione del pulsante di avvio con listener e comando
        casuale = new JButton("Random");
        casuale.setBackground(Color.LIGHT_GRAY);
        casuale.addActionListener(listener);
        casuale.setActionCommand("random"); //Inizializzazione del pulsante di random con listener e comando
        ferma = new JButton("Ferma");
        ferma.setBackground(Color.LIGHT_GRAY);
        ferma.addActionListener(listener);
        ferma.setActionCommand("ferma");        //Inizializzazione del pulsante di arresto
        salva = new JButton("Salva");
        salva.setBackground(Color.LIGHT_GRAY);
        salva.addActionListener(listener);
        salva.setActionCommand("salva");        //Inizializzazione del pulsante di salvataggio
        
        pnlMatrice = new JPanel();
        pnlMatrice.setLayout(new GridLayout(righe, colonne));
        pnlMatrice.setBackground(Color.WHITE);
        statoprecedente = new boolean[righe][colonne];  //Le due matrici che mi serviranno nell'algoritmo di simulazione
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                statoprecedente[i][j] = false;      //Inizializzo tutto a false poichè nessuna cella è stata selezionata e non è ancora stata avviata la simulazione
                matrice[i][j] = new JButton();
                matrice[i][j].setBackground(Color.WHITE);
                matrice[i][j].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                matrice[i][j].addActionListener(listener);
                matrice[i][j].setActionCommand(i + " " + j);
                pnlMatrice.add(matrice[i][j]);  //A ogni pulsante della matrice assegno un listener e un comando composto dalle sue coordinate, in modo da memorizzare i pulsanti di partenza
            }
        }
        pnlComandi = new JPanel();
        pnlComandi.setLayout(new FlowLayout());
        pnlComandi.add(avvia);
        pnlComandi.add(ferma);
        pnlComandi.add(casuale);
        pnlComandi.add(salva);
        cnt = getContentPane();
        cnt.setLayout(new BorderLayout());
        cnt.add(pnlMatrice, BorderLayout.CENTER);
        cnt.add(pnlComandi, BorderLayout.SOUTH);    //Inizializzazione pannelli, riempimento, inizializzazione container e riempimento
    }
    
    /**
     * Carica la GUI da file o la inizializza con il suo costruttore, setta defaultCloseOperation, visibilità e dimensione
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
        //Dimensionamento della finestra a seconda della matrice, sembra ok -inutile con GridLayout
        //win.setSize((win.matrice[0][0].getWidth() * win.colonne + 5*win.colonne + 18), (win.matrice[0][0].getHeight()*win.righe + 5*win.righe + 67));
        win.setSize(450, 450);
    }
    
    /**
     * Serializzazione della GUI su file binario "gameoflife.bin"
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
     * Deserializzazione della GUI da file binario "gameoflife.bin"
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
