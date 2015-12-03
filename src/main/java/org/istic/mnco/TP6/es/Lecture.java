package es;
import java.io.*;

public class Lecture {
  public static char unCar() {
  // effet : lit un caractère frappé
  // résultat : le caractère frappé
    char c;
    try { c = (char) System.in.read();}
    catch(IOException e) {c= (char) 0;};
    return c;
  }

  public static char unCarCmde() {
  // effet : lit caractère et consomme les suivants
  // jusqu'à la fin de ligne
  // résultat : le premier caractère frappé
    char c;
    try { c = (char) System.in.read();
          while(System.in.read()!='\r'){};
    }
    catch(IOException e) {c= (char) 0;};
    return c;
  }

  public static String chaine(String delimiteurs) {
  // prérequis : delimiteurs.length()!=0
  // effet : lit une séquence de caractères constituée de délimiteurs
  // puis de caractères autres que des délimiteurs.
  // les délimiteurs sont les caractères de délimiteurs.
  // résultat : la chaine formée des caractères compris entre les delimiteurs
    StringBuffer b = new  StringBuffer();
    char c=unCar();
    // ignore les delimiteurs de tete
    while (delimiteurs.indexOf(c)!=-1) {c=unCar();};
    // lit jusqu'au prochain delimiteur
    while (delimiteurs.indexOf(c)==-1) {b.append(c); c=unCar();};
    return b.toString();
  }

  public static String chaine() {
  // résultat : Lecture.chaine(" \r\n")
    return  chaine(" \r\n");
  }

  public static int unEntier() {
  // effet : lit une chaîne de caractères comprise entre les délimiteurs
  // ' ', '\r' ou '\n'
  // résultat : l'entier representé en decimal par cette chaîne
  // si la chaîne ne respecte pas la syntaxe d'un entier décimal,
  // affiche un message d'erreur et retourne 0.
    String s=Lecture.chaine(" \r\n");
    try { return Integer.parseInt(s);}
    catch(NumberFormatException e) {
      System.err.println("\nErreur lecture d'entier");
      System.err.println("valeur 0 retournée");     
      return 0;
    }
  }
  
  public static int unEntierHexa() {
    // effet : lit une chaîne de caractères comprise entre les délimiteurs
    // ' ', '\r' ou '\n'
    // résultat : l'entier representé en hexadecimal par cette chaîne
    // si la chaîne ne respecte pas la syntaxe d'un entier décimal,
    // affiche un message d'erreur et retourne 0.
      String s=Lecture.chaine(" \r\n");
      try { return Integer.parseInt(s, 16);}
      catch(NumberFormatException e) {
        System.err.println("\nErreur lecture d'entier");
        System.err.println("valeur 0 retournée");     
        return 0;
      }
    }

  public static float unReel() {
  // effet : lit une chaîne de caractères comprise entre les délimiteurs
  // ' ', '\r' ou '\n'
  // résultat : le flottant representé en format usuel par cette chaîne
  // si la chaîne ne respecte pas la syntaxe usuelle pour un nombre réel,
  // affiche un message d'erreur et retourne 0.
    String s=Lecture.chaine(" \r\n");
    try { return  (Float.valueOf(s)).floatValue();}
    catch(NumberFormatException e) {
      System.err.println("\nErreur lecture de reél");
      System.err.println("valeur 0 retournée");     
      return 0;
    }
  }

}
