package es;
import java.io.*;

public class EcritureFichierTexte {
// pour ecrire dans un fichier texte
// exemple d'utilisation :
// EcritureFichierTexte maSortie = new EcritureFichierTexte("monFichier.txt");
// maSortie.ecrire("\nje m'appelle toto et j'ai "); // ecrit une String
// maSortie.ecrire(12); // ecrit un int (�criture d�cimale d'un entier)
// maSortie.ecrire(" ans.") // ecrit une String
// maSortie.fermer();

  private PrintWriter leFichier; 
  private String nom;

  public EcritureFichierTexte(String nom) {
  // initialise un acces en ecriture sur le fichier de nom nom
  // cree le fichier s'il n'existe pas
    this.nom=nom;
    try {
      leFichier = new PrintWriter(new FileOutputStream(nom));
    } 
    catch(IOException e){
      System.out.println("erreur lors de la creation du fichier "+nom);
      Thread.dumpStack();
    }
  }

  public void fermer() { // ferme le fichier
    leFichier.close();
  }

    public void ecrire(char c) { // ecrit un caractere
    leFichier.print(c);
  }

  public void ecrire(String s) { // ecrit une chaene de caracteres
    leFichier.print(s);
  }

  public void ecrire(int k) { // ecrit un entier sous forme decimale
     leFichier.print(k);
  }

  public void ecrire(boolean b) { // ecrit un booleen, "true" ou "false"
     leFichier.print(b); 
  }

  public void ecrire(double x) { // ecrit un reel sous forme usuelle 
     leFichier.print(x);   
  }
}








