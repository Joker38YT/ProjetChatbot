import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utilitaire {

    private static final int NBMOTS_FORME = 5; // nombre maximal de mots-outils pris en compte pour les formes dans l'étape 2

    static public ArrayList<String> lireMotsOutils(String nomFichier) {
        //{}=>{résultat = le vecteur des mots outils construit à partir du fichier nomFichier}
        ArrayList<String> motsOutils = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                motsOutils.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return motsOutils;
    }

    static public ArrayList<String> lireReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des réponses construit à partir du fichier nomFichier}
        ArrayList<String> reponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                reponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponses;
    }

    static public ArrayList<String> lireQuestionsReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des questions/réponses construit à partir du fichier nomFichier}
        ArrayList<String> questionsReponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                questionsReponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionsReponses;


    }

    public static void ecrireFichier(String nomFichier, String chaineAEcrire) {
        //{}=>{la chaîne  chaineAEcrire est écrite après saut de ligne à la suite du fichier nomFichier}
        // true = mode append ? écrit à la suite sans effacer ce qui existe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.newLine();
            writer.write(chaineAEcrire);// ajoute un retour à la ligne
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static private ArrayList<String> decoupeEnMots(String contenu) {
        //{}=>{résultat = le vecteur des mots de la chaîne contenu après pré-traitements divers}
        String chaine = contenu.toLowerCase();
        chaine = chaine.replace('\n', ' ');
        chaine = chaine.replace('?', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('.', ' ');
        chaine = chaine.replace(',', ' ');
        chaine = chaine.replace(':', ' ');
        chaine = chaine.replace(';', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('"', ' ');
        chaine = chaine.replace('', ' ');
        chaine = chaine.replace('', ' ');
        chaine = chaine.replace("'", " ");
        chaine = chaine.replace('(', ' ');
        chaine = chaine.replace(')', ' ');
        chaine = chaine.replace('«', ' ');
        chaine = chaine.replace('»', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('’', ' ');


        String[] tabchaine = chaine.split(" ");
        ArrayList<String> resultat = new ArrayList<>();

        for (int i = 0; i < tabchaine.length; ++i) {
            if (!tabchaine[i].equals("")) {
                resultat.add(tabchaine[i]);
            }
        }
        System.out.println(resultat);
        return resultat;
    }


    static private boolean existeChaine(ArrayList<String> mots, String mot) {
        //{}=>  {recherche séquentielle de mot dans mots
        // résultat =  true si trouvé et false sinon }
        int i = 0;
        boolean trouve = false;
        while (i < mots.size() && !trouve) {
            if (mots.get(i).compareTo(mot) == 0) {
                trouve = true;
            }
            i++;
        }
        return trouve;
    }


    static private boolean existeChaineDicho(ArrayList<String> lesChaines, String chaine) {
        //{lesChaines (triée dans l'ordre lexicographique)}=>  {recherche dichotomique de chaine dans lesChaines
        // résultat =  true si trouvé et false sinon }
        if (lesChaines.isEmpty() || lesChaines.get(lesChaines.size() - 1).compareTo(chaine) < 0) {
            return false;
        } else {
            int inf = 0;
            int sup = lesChaines.size() - 1;
            int m;
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (lesChaines.get(m).compareTo(chaine) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (lesChaines.get(sup).compareTo(chaine) == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    static public boolean entierementInclus(ArrayList<String> mots, String question) {
        //{mots est trié dans l'ordre lexicographique}=>
        // résultat = true si tous les mots de questions sont dans mots, false sinon
        // remarque : utilise decoupeEnMots et existeChaineDicho}
        boolean toutLesMotDedans = true;
        ArrayList<String> motsDeQuestion = decoupeEnMots(question);
        for (int i = 0 ; i<motsDeQuestion.size() ; i++){
            if (!existeChaineDicho(mots, motsDeQuestion.get(i))){
                toutLesMotDedans = false;
            }
        }

        return toutLesMotDedans;
    }


    static private int rechercherChaine(ArrayList<String> lesChaines, String chaine) {
        // {}=>{résultat = l'indice de chaine dans lesChaines si trouvé et -1 sinon }
        int i = 0;
        while (i < lesChaines.size() && !lesChaines.get(i).equals(chaine)) {
            i++;
        }

        if (i >= lesChaines.size()) {
            return -1;
        }
        return i;
    }


    static public void integrerNouvelleQuestionReponse(String question, String reponse, ArrayList<String> formes, Index indexFormes, ArrayList<String> motsOutils) {
        //{la forme de reponse n'existe pas ou n'est pas associée à question dans indexFormes}=>{la forme de reponse est ajoutée à la fin de formes si elle n'y est pas déjà
        // et indexFormes est mis à jour pour tenir compte de cette nouvelle question-réponse
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        // remarque 2 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}

    }


    static public void IntegrerNouvelleReponse(String reponse, ArrayList<String> reponses, Index indexContenu, ArrayList<String> motsOutils) {
        //{reponse n'est pas présent dans reponses}=>{reponse est ajoutée à la fin de reponses et indexContenu est mis à jour pour tenir compte de cette nouvelle réponse
        // remarque : utilise decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
    }

    static public Index constructionIndexReponses(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les mots des réponses (reponses) absents de motsOutils.
        // et les sorties sont les indices (dans reponses) des réponses les contenant.
        // remarque : utilise existeChaineDicho, decoupeEnMots et ajouterSortieAEntree }
        Index index = new Index();
        for (int i = 0 ; i < reponses.size() ; i++) {
            ArrayList<String> motDeReponses = decoupeEnMots(reponses.get(i));
            for (int j = 0 ; j < motDeReponses.size(); j++){
               String motActu = motDeReponses.get(j);
               motActu = thesaurus.rechercherSortiePourEntree(motActu);
                if (!existeChaineDicho(motsOutils, motActu)){
                    index.ajouterSortieAEntree(motDeReponses.get(j), i);
                }
            }
        }
        return index;
    }


    static void trierChaines(ArrayList<String> v) {
        //{}=>{v est trié dans l'ordre lexicographique }
        int i = 0;
        while (i < v.size()-1) {
            int indMin = i;
            int j = i + 1;
            while (j < v.size()) {
                if (v.get(j).compareTo(v.get(indMin)) < 0) {
                    indMin = j;
                }
                j = j + 1;
            }
            if (indMin != i) {
                String temporaire = v.get(i);
                v.set(i, v.get(indMin));
                v.set(indMin, temporaire);
            }
            i = i + 1;
        }
    }


    static ArrayList<Integer> maxOccurences(ArrayList<Integer> v, int seuil) {
        //{v trié} => {résultat = vecteur des entiers dont le nombre d'occurences
        // est maximal et au moins égal au seuil. Si le nombre d'occurences maximal est inférieur au seuil , un vecteur vide est retourné.
        // Par exemple, si V est [3,4,5,5,5,6,6,8,8,8,12,16,16,20]
        // si seuil<=3 alors le résultat est [5,8].
        // si le seuil>3 alors le résultat est []}
        ArrayList<Integer> vfin = new ArrayList<>();
        if (v == null || v.isEmpty()) return vfin;

        int compteur = 1;
        int maxTrouve = 0;

        for (int i = 1; i < v.size(); i++) {
            if (v.get(i).compareTo(v.get(i-1)) == 0) {
                compteur++;
            } else {
                System.out.println(v.get(i-1) + ": " + compteur);
                if (compteur >= seuil) {
                    if (compteur > maxTrouve) {
                        maxTrouve = compteur;
                        vfin.clear();
                        vfin.add(v.get(i-1));
                    } else if (compteur == maxTrouve) {
                        vfin.add(v.get(i-1));
                    }
                }
                compteur = 1;
            }
        }

        if (compteur >= seuil) {
            if (compteur > maxTrouve) {
                vfin.clear();
                vfin.add(v.get(v.size() - 1));
            } else if (compteur == maxTrouve && maxTrouve > 0) {
                vfin.add(v.get(v.size() - 1));
            }
        }
        System.out.println(vfin);
        return vfin;
    }

    static ArrayList<Integer> fusion(ArrayList<Integer> v1, ArrayList<Integer> v2) {
        //{v1 et v2 triés}=>{résultat = vecteur trié fusionnant v1 et v2 sans supprimer les répétitions
        // par exemple si v1 est [4,8,8,10,25] et v2 est [5,8,9,25]
        // le résultat est [4,5,8,8,8,9,10,25,25]}
        ArrayList<Integer> resultat = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < v1.size() && j < v2.size()) {
            if (v1.get(i) <= v2.get(j)) {
                resultat.add(v1.get(i));
                i++;
            } else {
                resultat.add(v2.get(j));
                j++;
            }
        }

        while (i < v1.size()) {
            resultat.add(v1.get(i));
            i++;
        }

        while (j < v2.size()) {
            resultat.add(v2.get(j));
            j++;
        }

        return resultat;
    }


    static String calculForme(String chaine, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = la concaténation des NBMOTS_FORME premiers mots-outils de chaine séparés par des blancs
        // remarque 1 : utilise decoupeMots et existeChaineDicho
        // remarque 2 : la limitation de la taille des formes permet d'accepter des réponses terminant par des précisions }
        ArrayList<String> listeMots = decoupeEnMots(chaine);
        String motsOutils2 = "";
        int i = 0;
        int nbMots = 0;

        System.out.println("Phrase avant thesaurus calcul forme " + listeMots );
        for (int j = 0 ; j < listeMots.size() ; j++ ) {
            String motActu = listeMots.get(j);
            motActu = thesaurus.rechercherSortiePourEntree(motActu);
            listeMots.set(j, motActu);
        }
        System.out.println("Phrase après thesaurus calcul forme " + listeMots );

        while (i < listeMots.size() && nbMots < NBMOTS_FORME ) {
            if (existeChaineDicho(motsOutils, listeMots.get(i))){
                motsOutils2 = motsOutils2 + " " + listeMots.get(i);
                nbMots++;
            }
            i++;

        }
        return motsOutils2;
    }

    static public ArrayList<String> constructionTableFormes(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = le vecteur de toutes les formes de réponses dans reponses.
        // remarque : utilise calculForme et existeChaine }

        ArrayList<String> formeRep = new ArrayList<>();
        for (int i = 0 ; i < reponses.size() ; i++){
            if (!existeChaine(formeRep, calculForme(reponses.get(i), motsOutils, thesaurus))) {
                formeRep.add(calculForme(reponses.get(i), motsOutils, thesaurus));
            }
        }

        return formeRep;
    }

    static public Index constructionIndexFormes(ArrayList<String> questionsReponses, ArrayList<String> formes, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les "mots-outils positionnés" des questions (par exemple l'entrée pour un "Qui" en première position sera "qui_0")
        // et les sorties sont les indices (dans formes) des formes de réponses répondant aux questions contenant le mot-outil à cette position.
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots,  existeChaineDicho et ajouterSortieAEntree
        // remarque 2 : utilisez les méthodes indexOf et substring de String pour décomposer la question-réponse en question et réponse
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        Index index = new Index();
        for (int i = 0 ; i < questionsReponses.size() ; i++){
            String questionReponse = questionsReponses.get(i);
            String question = questionReponse.substring(0, questionReponse.indexOf("?"));
            String reponse = calculForme(questionReponse.substring(questionReponse.indexOf("?")+2), motsOutils, thesaurus);
            String questionMotsOutils = calculForme(question, motsOutils, thesaurus);
            ArrayList<String> motDeQuestion = decoupeEnMots(questionMotsOutils);

            System.out.println(reponse);


            for (int j = 0 ; j < motDeQuestion.size(); j++){
                if (existeChaineDicho(motsOutils, motDeQuestion.get(j))){
                    index.ajouterSortieAEntree(motDeQuestion.get(j) + "_" + j, rechercherChaine(formes, reponse));
                }
            }
        }
        return index;
    }

    static public ArrayList<Integer> constructionReponsesCandidates(String question, Index IndexReponses, ArrayList<String> motsOutils) {
        //{}=>{résultat = vecteur des identifiants de réponses contenant l'ensemble des mots non outils de la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion et maxOccurences
        // remarque 2 : maxOccurences est appelé en passant le nombre de mots non outils de la question comme valeur de seuil.
        // remarque 3 : on aurait pu calculer directement une intersection au lieu d'une fusion et se passer de maxOccurences mais on
        // souhaite pouvoir garder la possibilité d'assouplir par la suite la contrainte sur la présence de l'intégralité
        // des mots de la question dans la réponse }
        ArrayList<String> motDeQuestion = decoupeEnMots(question);
        ArrayList<String> motDeQuestionSansMotsOutil = new ArrayList<>();
        for (int j = 0 ; j < motDeQuestion.size(); j++){
            if (!existeChaineDicho(motsOutils, motDeQuestion.get(j))){
                motDeQuestionSansMotsOutil.add(motDeQuestion.get(j));
            }
        }
        System.out.println(" construction reponses candidate Phrase avant thesaurus " + motDeQuestion + "\nPhrase sans mot outil " + motDeQuestionSansMotsOutil );
        for (int j = 0 ; j < motDeQuestionSansMotsOutil.size() ; j++ ) {
            String motActu = motDeQuestionSansMotsOutil.get(j);
            motActu = thesaurus.rechercherSortiePourEntree(motActu);
            motDeQuestionSansMotsOutil.set(j, motActu);
        }
        System.out.println("\nconstruction reponses candidate Phrase sans mot outil après thesaurus" + motDeQuestionSansMotsOutil );


        int seuil = motDeQuestionSansMotsOutil.size();
        ArrayList<Integer> vIndex = new ArrayList<>();

        for (int i=0 ; i<motDeQuestionSansMotsOutil.size() ; i++){
          vIndex= fusion(vIndex,IndexReponses.rechercherSorties(motDeQuestionSansMotsOutil.get(i)));
        }
        System.out.println(maxOccurences(vIndex, seuil));
        return maxOccurences(vIndex, seuil);
    }


    static public boolean estUnNombre(String s) {
        //{s est non vide}=>{résultat = true si s ne contient que des caractères représentant des chiffres (>='0'&<='9') et false sinon}
        return false;
    }


    static public ArrayList<Integer> selectionReponsesCandidates(String question,
                                                                 ArrayList<Integer> candidates,
                                                                 Index IndexFormes,
                                                                 ArrayList<String> reponses,
                                                                 ArrayList<String> formesReponses,
                                                                 ArrayList<String> motsOutils,
                                                                 Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses (parmi les candidates) dont la forme est cohérente
        // avec la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : l'algorithme procède en 2 temps. D'abord il trouve les formes de réponses qui répondent à la question.
        // puis ajoute au résultat l'identifiant des réponses candidates qui respectent au moins une de ces formes.
        // remarque 3 : pour trouver les formes de réponses qui répondent à la question, on utilise l'index des formes, et on sélectionne
        // en appelant maxOccurences (avec seuil = nombre des mots-outils de la question) celles associées dans l'index à tous les mots-outils de la question.
        // remarque 4 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        int compteur = 0;
        ArrayList<Integer> reponsesInt = new ArrayList<>();
        ArrayList<String> vDeQuestion = decoupeEnMots(question);
        ArrayList<Integer> vFusion = new ArrayList<>();
        for(int i=0; i<vDeQuestion.size(); i++){
            if(existeChaine(motsOutils, vDeQuestion.get(i))){
                String mot = vDeQuestion.get(i).toLowerCase() + "_" + i;
                vFusion = fusion(vFusion, IndexFormes.rechercherSorties(mot));
                compteur++;
            }
        }
        System.out.println(vFusion);
        System.out.println(compteur);
        vFusion = maxOccurences(vFusion, compteur-1);
        System.out.println(vFusion);
        for(int i = 0; i< candidates.size(); i++){
            int y;
            for(y=0; y<vFusion.size() && calculForme(reponses.get(candidates.get(i)), motsOutils, thesaurus).compareTo(formesReponses.get(vFusion.get(y)))<0; y++);
            if(y<vFusion.size() && calculForme(reponses.get(candidates.get(i)), motsOutils, thesaurus).compareTo(formesReponses.get(vFusion.get(y))) == 0){
                reponsesInt.add(candidates.get(i));
            }
        }
        System.out.println(reponsesInt);
        return reponsesInt;
    }

    static public boolean reponseExiste(String reponse,
                                        Index indexReponses,
                                        ArrayList<String> reponses,
                                        ArrayList<String> motsOutils) {
        //{}=>{résultat = true si la reponse est présente dans reponses et false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences
        // remarque 2 : Le vecteur reponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, on utilise indexReponses pour trouver les réponses contenant tous les mots non outils de la
        // reponse, puis on vérifie si l'une d'entre elle est identique à reponse.}
        return false;
    }


    static public boolean formeQuestionReponseExiste(String question,
                                                     String reponse,
                                                     Index indexFormes,
                                                     ArrayList<String> formesReponses,
                                                     ArrayList<String> motsOutils) {
        //{}=>{résultat = * true si la forme de reponse est présente dans formesReponses
        // et qu'elle est accessible à partir des mots de la question en utilisant indexFormes.
        //                * false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : Le vecteur formesReponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, et afin de vérifier l'accessibilité à partir des mots de la question en utilisant indexFormes,
        // on utilise indexFormes pour trouver les formes indexées par les mots-outils de la
        // question, puis on vérifie si l'une de ces formes est identique à la forme de reponse.
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de question sont pris en compte}
        return false;
    }
}
