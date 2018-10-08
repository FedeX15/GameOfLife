package gameoflife;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

/**
 * ActionListener che analizza e simula le generazioni, ferma la simulazione, randomizza la matrice e salva su file
 * @version 28022014
 * @author Federico Matteoni
 */
public class ButtonListener implements ActionListener {
    /**
     * La GUI su cui lavorare
     */
    public GUIClass win;
    /**
     * Conteggio delle generazioni
     */
    public int generazioni;
    /**
     * Flag per avviare/fermare il thread
     */
    public boolean esegui;
    
    /**
     * Inizializza il riferimento alla GUI, il conteggio delle generazioni e il flag di esecuzione del thread
     * @param win La GUI a cui riferirsi per analizzare e modificare la matrice
     */
    public ButtonListener(GUIClass win) {
        this.win = win;
        generazioni = 0;
        esegui = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "random": {  //Randomizzo la matrice
                System.out.println("==== RANDOMIZZA =====");    //Intestazione
                for (int i = 0; i < win.righe; i++) {
                    for (int j = 0; j < win.colonne; j++) {
                        double random = Math.random();
                        if (random > 0.7) {
                            win.statoprecedente[i][j] = true;   //Attivo la corrispondente coordinata
                            win.matrice[i][j].setBackground(Color.BLACK);
                            System.out.println("Cella[" + i + "][" + j + "] selezionata");     //Segnalo che la cella è selezionara
                        } else {
                            win.statoprecedente[i][j] = false;
                            win.matrice[i][j].setBackground(Color.WHITE);
                            System.out.println("Cella[" + i + "][" + j + "] deselezionata");   //Segnalo che la cella è deselezionata
                        }

                    }
                }
                break;
            }
            
            case "avvia": {
                esegui = true;  //Abilito il thread ad essere eseguito
                new Thread() {  //Crea un thread per la esecuzione del codice e permettere a Swing di aggiornare la GUI
                    @Override
                    public void run() {
                        do {
                            generazioni++;
                            System.out.println("\n=== Generazione " + generazioni + " ==="); //Conteggio delle generazione, scopo informativo
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            }
                            catch (InterruptedException ex) {}
                            //Rallento l'esecuzione per poter vedere e per alleggerire il programa
                            
                            for (int i = 0; i < win.righe; i++) {
                                for (int j = 0; j < win.colonne; j++) {
                                    int conteggio = 0;      //Azzero il conteggio delle celle vive adiacenti alla cella corrente
                                    
                                    //Inizio controllo celle adiacenti
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
                                    //Fine controllo celle adiacenti
                                    
                                    if (conteggio == 3 && !(win.statoprecedente[i][j])) {
                                        System.out.println(i + " " + j + " piena"); //Segnalo il riempimento di una cella
                                        win.matrice[i][j].setBackground(Color.BLACK);
                                    }
                                    else if ((conteggio < 2 || conteggio > 3) && win.statoprecedente[i][j]) {
                                        System.out.println(i + " " + j + " vuota"); //Segnalo lo svuotamento di una cella
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
                esegui = false;     //Fermo l'esecuzione del thread
                break;
            }
            
            case "salva": {
                win.SaveState();    //Serializzazione su file binario
                break;
            }
            
            default: {   //Se nessuno degli action command è riconosciuto allora ho per forza cliccato su un pulsante della matrice, quindi lo gestisco
                int riga, colonna;
                riga = Integer.parseInt(e.getActionCommand().substring(0, e.getActionCommand().indexOf(' ')));
                colonna = Integer.parseInt(e.getActionCommand().substring(e.getActionCommand().indexOf(' ') + 1, e.getActionCommand().length()));   //Ricavo le coordinate del pulsante dal suo actionCommand
                System.out.print("Cella[" + riga + "][" + colonna + "]");  //Segnalo la cella che sto analizzando
                if (!win.statoprecedente[riga][colonna]) { //Se la coordinata corrispondente nella matrice di boolean è falsa, cioè che la cella era morta
                    win.statoprecedente[riga][colonna] = true;  //La faccio vivere *musica celestiale"
                    win.matrice[riga][colonna].setBackground(Color.BLACK);
                    System.out.println(" selezionata");     //Segnalo che la cella è stata selezionata
                }
                else {
                    win.statoprecedente[riga][colonna] = false; //Se era già viva la uccido *risata malefica*
                    win.matrice[riga][colonna].setBackground(Color.WHITE);
                    System.out.println(" deselezionata");   //Segnalo che la cella è stata deselezionata
                }   break;
            }
        }
    }
}
