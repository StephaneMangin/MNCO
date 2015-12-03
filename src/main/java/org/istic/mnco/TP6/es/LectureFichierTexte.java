package org.istic.mnco.TP6.es;

import org.istic.mnco.TP6.biglang.BigLangException;

import java.io.*;

public class LectureFichierTexte {
// pour lire un fichier texte.
// Les délimiteurs d'entités lues sont par défaut
// l'espace et le passage à la ligne
// exemple d'utilisation :
// LectureFichierTexte monEntree = new LectureFichierTexte("monFichier.txt");
// String item1 = monEntree.lireChaine(); // lit une String
// int item2 = monEntree.lireUnEntier(); // lit un entier écrit en décimal
// monEntree.fermer();
//
// Peut lire sur le clavier :
// LectureFichierTexte monEntree = new LectureFichierTexte(System.in);
// ou
// LectureFichierTexte monEntree = new LectureFichierTexte(System.in, carDeFin);	

    private BufferedReader leFichier;
    private char prochainCaractere;
    private boolean finDeFichier;
    private String nom;
    private int carEOF;
    private boolean cestUnfichier;

    public LectureFichierTexte(String nom) {
        // initialise un accès en lecture sur le fichier de nom nom
        // erreur si le fichier n'existe pas
        this.nom = nom;
        if (nom.equals("clavier")) {
            this.cestUnfichier = false;
            this.carEOF = -1;

            leFichier = new BufferedReader(new InputStreamReader(System.in));
            finDeFichier = false;
            prochainCaractere = '\n';
        } else {
            this.cestUnfichier = true;
            this.carEOF = -1;
            try {
                leFichier = new BufferedReader(new FileReader(nom));
                finDeFichier = false;
                tenteDeLireProchainCaractere();
            } catch (FileNotFoundException e) {
                System.err.println("fichier " + nom + " inexistant");
                Thread.dumpStack();
            }
        }
    }

    public LectureFichierTexte(File fich) {
        // initialise un accès en lecture sur le fichier fich
        // erreur si le fichier n'existe pas
        this.nom = fich.getAbsolutePath();
        if (nom.equals("clavier")) {
            this.cestUnfichier = false;
            this.carEOF = -1;

            leFichier = new BufferedReader(new InputStreamReader(System.in));
            finDeFichier = false;
            prochainCaractere = '\n';
        } else {
            this.cestUnfichier = true;
            this.carEOF = -1;
            try {
                leFichier = new BufferedReader(new FileReader(nom));
                finDeFichier = false;
                tenteDeLireProchainCaractere();
            } catch (FileNotFoundException e) {
                System.err.println("fichier " + nom + " inexistant");
                Thread.dumpStack();
            }
        }
    }

    public LectureFichierTexte(InputStream dispositif, char fin) {
        // initialise un accès en lecture sur une InputStream
        // erreur si le dispositif n'est pas conforme ou n'existe pas
        // la rencontre du caractère fin dans le flot d'entrée provoque une fin de fichier
        this.nom = "clavier";
        this.cestUnfichier = false;
        this.carEOF = (int) fin;

        leFichier = new BufferedReader(new InputStreamReader(dispositif));
        finDeFichier = false;
        prochainCaractere = '\n';
    }

    public LectureFichierTexte(InputStream dispositif) {
        // initialise un accès en lecture sur une InputStream
        // erreur si le dispositif n'est pas conforme ou n'existe pas
        // le flot d'entrée est potentiellement infini : la fin de fichier n'est jamis atteinte
        this.nom = "clavier";
        this.cestUnfichier = false;
        this.carEOF = -1;

        leFichier = new BufferedReader(new InputStreamReader(dispositif));
        finDeFichier = false;
        prochainCaractere = '\n';
    }

    public static void ilExiste(String nom) throws BigLangException {
        File f = new File(nom);
        if (!f.exists()) {
            throw new BigLangException(nom, BigLangException.BL_COM, BigLangException.BL_COM_FICHIER_INEXISTANT);
        }
    }

    private void tenteDeLireProchainCaractere() {
        if (!cestUnfichier && prochainCaractere == '\n') {
            prochainCaractere = ' ';
        } else {
            try {
                int x = leFichier.read();
                prochainCaractere = (char) x;
                if (x == carEOF) {
                    finDeFichier = true;
                }
            } catch (IOException e) {
                System.err.println("erreur de lecture du fichier " + nom);
                Thread.dumpStack();
            }
        }
    }

    public void fermer() { // fermeture du fichier
        try {
            leFichier.close();
        } catch (IOException e) {
            System.err.println("erreur lors de la fermeture du fichier " + nom);
            Thread.dumpStack();
        }
    }

    public char lireUnCar() { // lecture d'un caractère
        char courant = prochainCaractere;
        tenteDeLireProchainCaractere();
        return courant;
    }

    public String lireChaine(String delimiteurs) {
        // lecture d'une chaine comprise entre delimiteurs
        // ou jusqu'à fin de fichier.
        // rend la chaine vide si fin de fichier.
        if (finDeFichier()) {
            return "";
        }
        char c = lireUnCar();
        // ignore les delimiteurs de tete
        while (!finDeFichier() && delimiteurs.indexOf(c) != -1) {
            c = lireUnCar();
        }
        if (finDeFichier()) {
            return "";
        }
        // lit jusqu'au prochain delimiteur ou fin de fichier
        StringBuffer b = new StringBuffer();
        // b.append(c);
        // c=lireUnCar();
        while (!finDeFichier() && delimiteurs.indexOf(c) == -1) {
            b.append(c);
            c = lireUnCar();
        }
        if (delimiteurs.indexOf(c) == -1) {
            b.append(c);
        }
        if (cestUnfichier) {
            // dans le cas d'un vrai fichier : consomme les éventuels delimiteurs suivants
            while (!finDeFichier() && delimiteurs.indexOf(prochainCaractere) != -1) {
                c = lireUnCar();
            }
        }
        return b.toString();
    }

    public String lireChaine() {
        // lecture d'une chaine comprise entre delimiteurs ' ', '\r' ou '\n'
        return lireChaine(" \r\n");
    }

    public int lireUnEntier() { // lecture d'un entier
        try {
            return Integer.parseInt(lireChaine());
        } catch (NumberFormatException e) {
            System.err.println("Erreur lecture d'entier sur fichier " + nom);
            System.err.println("valeur 0 retournée");
            return 0;
        }
    }

    public int lireUnEntierHexa() { // lecture d'un entier en notation hexadécimale
        try {
            return Integer.parseInt(lireChaine(), 16);
        } catch (NumberFormatException e) {
            System.err.println("Erreur lecture d'entier sur fichier " + nom);
            System.err.println("valeur 0 retournée");
            return 0;
        }
    }

    public double lireUnReel() { // lecture d'un nombre réel
        try {
            return (Double.valueOf(lireChaine())).floatValue();
        } catch (NumberFormatException e) {
            System.err.println("Erreur lecture de reél sur fichier " + nom);
            System.err.println("valeur 0 retournée");
            return 0;
        }
    }

    public boolean finDeFichier() { // indique si fin de fichier
        return finDeFichier;
    }

}
