/*
 * Created on 14 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.istic.mnco.TP6.biglang.generation;

import org.istic.mnco.TP6.biglang.BigLangException;
import org.istic.mnco.TP6.es.Ecriture;
import org.istic.mnco.TP6.es.LectureFichierTexte;
import org.istic.mnco.TP6.bigmach.JeuDInstructions;
import org.istic.mnco.TP6.bigmach.MemoireInstructions;
import org.istic.mnco.TP6.service.Service;

/**
 * Gestion du code objet
 */
public class CodeObjet {
    /**
     * taille maximum du code objet, fix�e � la taille de la m�moire de la machine
     */
    public static final int tObj = MemoireInstructions.tailleInsts;
    /**
     * tampon utilis� pour produire le code en m�moire
     */
    int[] tamponObj = new int[tObj];
    /**
     * Le plus grand index des mots de <code>tampoObj>/code>
     * qui ont �t� modifi�s depuis l'initialisation du tampon
     */
    int iObj = -1;
    /**
     * Nom du fichier objet (extention �ventuelle comprise)
     */
    String nomFichierObjet = null;
    /**
     * Fichier Objet
     */
    Ecriture fichierObjet = null;

    /**
     * Initialise le nom du fichier, le ficier et le tampon
     *
     * @param n : nom du fichier objet sans extension
     */
    public CodeObjet(String n) {
        nomFichierObjet = n + ".obj";
        iObj = 0;
    }

    /**
     * Ajout d'un mot � la fin du tampon
     *
     * @param v : mot � ajouter
     * @throws BigLangException : quand le tampon est plein
     * @deprecated
     */
    public void ajout(int v) throws BigLangException {
        if (iObj == tObj) {
            throw new BigLangException(BigLangException.BL_GEN, BigLangException.BL_GEN_TAMPON_DEBORDEMENT);
        }
        tamponObj[iObj] = v;
        iObj++;
    }

    /**
     * Modification du <code>i<sup>e</sup></code> mot du tampon. Le cas �ch�ant,
     * modifie <code>iObj</code>
     *
     * @param i : l'index du mot � mofifier
     * @param v : la nouvelle valeur
     * @throws BigLangException : si <code>i></code> n'est pas un index valide.
     */
    public void modifie(int i, int v) throws BigLangException {
        if (i >= tObj || i < 0) {
            throw new BigLangException(BigLangException.BL_GEN, BigLangException.BL_GEN_TAMPON_DEBORDEMENT);
        }
        tamponObj[i] = v;
        if (i > iObj) {
            iObj = i;
        }
    }

    /**
     * Provoque l'�criture du code Objet d�j� produit dans
     * le tampon (entre <code>0</code> inclus et <code>iObj</code> exclus)
     * sur le fichier <code>nomFichierObj</code>
     */
    public void sortie() {
        fichierObjet = new Ecriture(nomFichierObjet);
        for (int i = 0; i <= iObj; i++) {
            fichierObjet.ecrire(tamponObj[i]);
            fichierObjet.ecrire("\n");
        }
        fichierObjet.fermer();
    }

    /**
     * Provoque la lecture d'un fichier objet dans
     * le tampon (entre <code>0</code> inclus et <code>iObj</code> exclus)
     * sur le fichier <code>nomFichierObj</code>
     *
     * @throws BigLangException
     */
    public void lecture() throws BigLangException {
        LectureFichierTexte fichier = new LectureFichierTexte(nomFichierObjet);
        iObj = 0;
        while (!fichier.finDeFichier()) {
            if (iObj == tObj) {
                throw new BigLangException(BigLangException.BL_GEN, BigLangException.BL_GEN_TAMPON_DEBORDEMENT);
            }
            tamponObj[iObj] = fichier.lireUnEntier();
            iObj++;
        }
        fichier.fermer();
    }

    /**
     * D�compilation du code objet
     */
    public String toString() {
        String code = "";
        int i = 0;
        while (i <= iObj) {
            if (tamponObj[i] < 0 || tamponObj[i] >= JeuDInstructions.instructions.length) {
                code = code + Service.formaterUnEntier(i, 6) + ": " + "instruction inexistante" + "\n";
            } else {
                code = code + Service.formaterUnEntier(i, 6) + ": " + JeuDInstructions.instructions[tamponObj[i]].nom + "\n";
                if (JeuDInstructions.instructions[tamponObj[i]].format == 2 && i + 1 <= iObj) {
                    i++;
                    code = code + Service.formaterUnEntier(i, 6) + ": " + tamponObj[i] + "\n";
                }
            }
            i++;
        }
        return code;
    }

}
