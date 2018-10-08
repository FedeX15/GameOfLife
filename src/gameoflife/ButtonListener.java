package gameoflife;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

/**
 * ActionListener which analizes and simulates generations, stops the simulation, randomize matrix and saves on file. Original written on 2014
 * @version 28022014
 * @author Federico Matteoni
 */
public class ButtonListener implements ActionListener {
    /**
     * GUI where to work
     */
    public GUIClass win;
    /**
     * Generation counter
     */
    public int generazioni;
    /**
     * Starts/Stops the tread
     */
    public boolean esegui;
    
    /**
     * Initializes GUI reference, generation counter and thread flag
     * @param win GUI reference where to update the matrix
     */
    public ButtonListener(GUIClass win) {
        this.win = win;
        generazioni = 0;
        esegui = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "random": {  //Matrix randomizer
                System.out.println("==== RANDOMIZE =====");    //Header
                for (int i = 0; i < win.righe; i++) {
                    for (int j = 0; j < win.colonne; j++) {
                        double random = Math.random();
                        if (random > 0.7) {
                            win.statoprecedente[i][j] = true;   //Activate the coordinate
                            win.matrice[i][j].setBackground(Color.BLACK);
                            System.out.println("Cella[" + i + "][" + j + "] activated");
                        } else {
                            win.statoprecedente[i][j] = false;  //Deactivate the coordinate
                            win.matrice[i][j].setBackground(Color.WHITE);
                            System.out.println("Cella[" + i + "][" + j + "] deactivated");
                        }

                    }
                }
                break;
            }
            
            case "avvia": {
                esegui = true;  //Flag the thread to run
                new Thread() {  //Thread execs the code and lets Swing update the GUI
                    @Override
                    public void run() {
                        do {
                            generazioni++;
                            System.out.println("\n=== Generation no. " + generazioni + " ==="); //Generation counter just for info
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            }
                            catch (InterruptedException ex) {}
                            //Slows down the execution to let the user see the various generations (and let the program be lighter)
                            
                            for (int i = 0; i < win.righe; i++) {
                                for (int j = 0; j < win.colonne; j++) {
                                    int conteggio = 0;      //Counter of live cells next to the current cell
                                    
                                    //Check the next cells
                                    try {
                                        conteggio = ((win.statoprecedente[i-1][j]) ? conteggio + 1 : conteggio);
                                        try {
                                            conteggio = ((win.statoprecedente[i-1][j-1]) ? conteggio + 1 : conteggio);
                                        } catch (IndexOutOfBoundsException erroreIndice){}
                                        try {
                                            conteggio = ((win.statoprecedente[i-1][j+1]) ? conteggio + 1 : conteggio);
                                        } catch (IndexOutOfBoundsException erroreIndice){}
                                    } catch (IndexOutOfBoundsException erroreIndice){}
                                    try {
                                        conteggio = ((win.statoprecedente[i+1][j]) ? conteggio + 1 : conteggio);
                                        try {
                                            conteggio = ((win.statoprecedente[i+1][j-1]) ? conteggio + 1 : conteggio);
                                        } catch (IndexOutOfBoundsException erroreIndice){}
                                        try {
                                            conteggio = ((win.statoprecedente[i+1][j+1]) ? conteggio + 1 : conteggio);
                                        } catch (IndexOutOfBoundsException erroreIndice){}
                                    } catch (IndexOutOfBoundsException erroreIndice){}
                                    try {
                                        conteggio = ((win.statoprecedente[i][j-1]) ? conteggio + 1 : conteggio);
                                    } catch (IndexOutOfBoundsException erroreIndice){}
                                    try {
                                        conteggio = ((win.statoprecedente[i][j+1]) ? conteggio + 1 : conteggio);
                                    } catch (IndexOutOfBoundsException erroreIndice){}
                                    //end check
                                    
                                    if (conteggio == 3 && !(win.statoprecedente[i][j])) {
                                        System.out.println(i + " " + j + " full");
                                        win.matrice[i][j].setBackground(Color.BLACK);
                                    }
                                    else if ((conteggio < 2 || conteggio > 3) && win.statoprecedente[i][j]) {
                                        System.out.println(i + " " + j + " empty");
                                        win.matrice[i][j].setBackground(Color.WHITE);
                                    }
                                }
                            }
                            for (int i = 0; i < win.righe; i++) {
                                for (int j = 0; j < win.colonne; j++) {
                                    win.statoprecedente[i][j] = ((win.matrice[i][j].getBackground().equals(Color.BLACK)) ? true : false);
                                }
                            }
                        } while (esegui);
                    }
                }.start();
                break;
            }
            
            case "ferma": {
                esegui = false;     //Stops thread execution
                break;
            }
            
            case "salva": {
                win.SaveState();    //Serialization on binary file
                break;
            }
            
            default: {   //The dafult behavior is handling a matrix button click, given that if the action command is unrecognized I must have clicked on a matrix button
                int riga, colonna;
                riga = Integer.parseInt(e.getActionCommand().substring(0, e.getActionCommand().indexOf(' ')));
                colonna = Integer.parseInt(e.getActionCommand().substring(e.getActionCommand().indexOf(' ') + 1, e.getActionCommand().length()));   //Coordinates from button action command
                System.out.print("Cella[" + riga + "][" + colonna + "]"); 
                if (!win.statoprecedente[riga][colonna]) { //If the current coordinate is false, which means the cell was dead
                    win.statoprecedente[riga][colonna] = true;  //I'll resurrect it *celestial music*
                    win.matrice[riga][colonna].setBackground(Color.BLACK);
                    System.out.println(" activated");
                }
                else {
                    win.statoprecedente[riga][colonna] = false; //If it was alive I kill it *evil laugh*
                    win.matrice[riga][colonna].setBackground(Color.WHITE);
                    System.out.println(" deactivated");
                }   break;
            }
        }
    }
}
