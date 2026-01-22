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
        chaine = chaine.replace('‑', ' ');


        String[] tabchaine = chaine.split(" ");
        ArrayList<String> resultat = new ArrayList<>();

        for (int i = 0; i < tabchaine.length; ++i) {
            if (!tabchaine[i].equals("")) {
                resultat.add(tabchaine[i]);
            }
        }
        return resultat;
    }


    static private boolean existeChaine(ArrayList<String> mots, String mot) {
        //{}=>  {recherche séquentielle de mot dans mots
        // résultat =  true si trouvé et false sinon }
        int i = 0;
        boolean trouve = false;

        while (i < mots.size() && !trouve) {
            // parcours de la liste tant qu'on n'a pas atteint la fin et que le mot n'a pas été trouvé
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
                // on continue de chercher tant que inf n'égal ou ne dépasse pas sup
                m = (inf + sup) / 2;
                if (lesChaines.get(m).compareTo(chaine) >= 0) {
                    sup = m; // poursuivre la recherche à gauche sur [inf..m]
                } else {
                    inf = m + 1;// poursuivre la recherche à droite sur [m+1..sup-1]
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
        ArrayList<String> motsQuestion = decoupeEnMots(question);
        boolean bon = true;
        int i = 0;

        while(i< motsQuestion.size() && bon){
            // on parcourt chaque mot de la question tant qu'ils sont tous trouvés
            if(!existeChaineDicho(mots, motsQuestion.get(i))){
                bon = false;
            }
            i++;
        }
        return bon;

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


    static public void integrerNouvelleQuestionReponse(String question, String reponse, ArrayList<String> formes, Index indexFormes, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{la forme de reponse n'existe pas ou n'est pas associée à question dans indexFormes}=>{la forme de reponse est ajoutée à la fin de formes si elle n'y est pas déjà
        // et indexFormes est mis à jour pour tenir compte de cette nouvelle question-réponse
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        // remarque 2 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        String repForme = calculForme(reponse, motsOutils , thesaurus); // on prend la forme (des mots-outils) dans la réponse
        if (rechercherChaine(formes, repForme )< 0 ){
            // si cette structure de réponse est inconnue, on l'ajoute à la liste globale des formes
            formes.add(repForme);
        }
        int compteur =0;
        ArrayList<String> motQuest = decoupeEnMots(repForme);
        for (int i = 0 ; i<motQuest.size() && compteur<= NBMOTS_FORME; i++){
            // On boucle sur les mots-outils de la question dans la limite de NBMOTS_FORME
            motQuest.set(i, thesaurus.rechercherSortiePourEntree(motQuest.get(i)));
            indexFormes.ajouterSortieAEntree(motQuest.get(i)+"_"+i, formes.size()-1);
            // On crée le lien dans Index : "mot-outil_position" -> indice de la forme de réponse
            compteur++;
        }

//        indexFormes.afficher();

    }


    static public void integrerNouvelleReponse(String reponse, ArrayList<String> reponses, Index indexContenu, ArrayList<String> motsOutils , Thesaurus thesaurus) {
        //{reponse n'est pas présent dans reponses}=>{reponse est ajoutée à la fin de reponses et indexContenu est mis à jour pour tenir compte de cette nouvelle réponse
        // remarque : utilise decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        reponses.add(reponse); // ajout de la phrase de réponse brute à la liste globale des réponses

        ArrayList<String> motRep = decoupeEnMots(reponse);
        for (int i = 0 ; i<motRep.size() ; i++){
            motRep.set(i, thesaurus.rechercherSortiePourEntree(motRep.get(i))); // on remplace le mot par sa forme canonique du thésaurus
            if (existeChaineDicho(motsOutils,motRep.get(i))){
                motRep.remove(i);
            }
        }

        for (int j = 0 ; j < motRep.size(); j++){
            // on associe chaque mot-clé restant à l'indice de cette nouvelle réponse
            indexContenu.ajouterSortieAEntree(motRep.get(j), reponses.size()-1);
        }


    }

    static public Index constructionIndexReponses(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les mots des réponses (reponses) absents de motsOutils.
        // et les sorties sont les indices (dans reponses) des réponses les contenant.
        // remarque : utilise existeChaineDicho, decoupeEnMots et ajouterSortieAEntree }
        Index index = new Index();

        for (int i = 0 ; i < reponses.size() ; i++) {
            // extraction de chaque mot de la réponse actuelle
            ArrayList<String> motDeReponses = decoupeEnMots(reponses.get(i));

            for (int j = 0 ; j < motDeReponses.size(); j++){
                String motActu = motDeReponses.get(j);
                motActu = thesaurus.rechercherSortiePourEntree(motActu);
                if (!existeChaineDicho(motsOutils, motActu)){
                    // on indexe chaque mot qui n'est pas outils (tout les mots "thèmes")
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
        if (v == null || v.isEmpty()) {
            return vfin;
        }

        int compteur = 1; // Compte les apparitions successives du même nombre
        int maxTrouve = seuil; // Stocke le nombre d'apparitions le plus élevé

        for (int i = 1; i < v.size(); i++) {
            // on compare chaque élément avec le précédent
            if (v.get(i).compareTo(v.get(i-1)) == 0) {
                // si c'est le même nombre, on augmente le score de cette réponse
                compteur++;
            } else {
                if (compteur > maxTrouve) {
                    // si ce nombre est superieur au nombre d'apparitions le plus élevé
                    maxTrouve = compteur;
                    vfin.clear(); // on efface les anciens résultats
                    vfin.add(v.get(i-1)); // on mémorise le nouveau
                } else if (compteur == maxTrouve) {
                    vfin.add(v.get(i-1)); // on mémorise le nombre avec les autres
                }
                compteur = 1;
            }
        }
        if (compteur > maxTrouve) {
            // si ce nombre est superieur au nombre d'apparitions le plus élevé
            vfin.clear(); // on efface les anciens résultats
            vfin.add(v.get(v.size() - 1)); // on mémorise le nouveau
        } else if (compteur == maxTrouve && maxTrouve > 0) {
            vfin.add(v.get(v.size() - 1)); // on mémorise le nombre avec les autres
        }
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
        int nbMots = 0; // compteur pour limiter la forme à NBMOTS_FORME


        while (i < listeMots.size() && nbMots < NBMOTS_FORME ) {
            if(estUnNombre(listeMots.get(i))){ // tous les chiffres deviennent "NUM"
                listeMots.set(i, "NUM");
            }
            String motActu = listeMots.get(i);
            motActu = thesaurus.rechercherSortiePourEntree(motActu); // thésaurus pour obtenir la forme canonique
            if (existeChaineDicho(motsOutils, motActu.toLowerCase())){
                // si le mot traité fait parti de la liste officielle des mots-outils, on l'ajoute à notre chaîne de résultat
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
            if (!existeChaine(formeRep, calculForme(reponses.get(i), motsOutils , thesaurus))) {
                // si la forme de la réponse n'est pas déjà présente dans notre liste de formes, on l'ajoute à la liste des formes connues
                formeRep.add(calculForme(reponses.get(i), motsOutils, thesaurus));
            }
        }

        return formeRep;
    }

    static public Index constructionIndexFormes(ArrayList<String> questionsReponses, ArrayList<String> formes, ArrayList<String> motsOutils , Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les "mots-outils positionnés" des questions (par exemple l'entrée pour un "Qui" en première position sera "qui_0")
        // et les sorties sont les indices (dans formes) des formes de réponses répondant aux questions contenant le mot-outil à cette position.
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots,  existeChaineDicho et ajouterSortieAEntree
        // remarque 2 : utilisez les méthodes indexOf et substring de String pour décomposer la question-réponse en question et réponse
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        Index index = new Index();

        for (int i = 0; i < questionsReponses.size(); i++) {
            // parcours de la liste des couples
            String questionRep = questionsReponses.get(i);

            String question = questionRep.substring(0, questionRep.indexOf("?"));
            String formeReponse = calculForme(questionRep.substring(questionRep.indexOf("?") + 2), motsOutils , thesaurus);

            String formeQuestion = calculForme(question, motsOutils , thesaurus);
            ArrayList<String> motsQuestion = decoupeEnMots(formeQuestion);

            int indiceForme = rechercherChaine(formes, formeReponse); // récupération de l'identifiant (l'indice) de la forme de la réponse dans la table globale

            if (indiceForme != -1) {
                // si la forme de réponse est connue, on indexe chaque mot-outil de la question
                for (int j = 0; j < motsQuestion.size(); j++) {

                    if(estUnNombre(motsQuestion.get(j))){
                        motsQuestion.set(j , "NUM");
                    }

                    String motActu = motsQuestion.get(j);
                    motActu = thesaurus.rechercherSortiePourEntree(motActu);
                    motsQuestion.set(j, motActu);

                    if (existeChaineDicho(motsOutils, motsQuestion.get(j))) {
                        index.ajouterSortieAEntree(motsQuestion.get(j) + "_" + j, indiceForme);
                    }
                }
            }
        }
//        index.afficher();
        return index;
    }

    static public ArrayList<Integer> constructionReponsesCandidates(String question, Index IndexReponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses contenant l'ensemble des mots non outils de la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion et maxOccurences
        // remarque 2 : maxOccurences est appelé en passant le nombre de mots non outils de la question comme valeur de seuil.
        // remarque 3 : on aurait pu calculer directement une intersection au lieu d'une fusion et se passer de maxOccurences mais on
        // souhaite pouvoir garder la possibilité d'assouplir par la suite la contrainte sur la présence de l'intégralité
        // des mots de la question dans la réponse }
        ArrayList<String> motDeQuestion = decoupeEnMots(question);
        ArrayList<String> motDeQuestionSansMotsOutil = new ArrayList<>();
        ArrayList<Integer> vIndex = new ArrayList<>();
        int nbMotsTrouves = 0;

        for (int i = 0; i < motDeQuestion.size(); i++) {
            // analyse de chaque mot de la question
            String mot = motDeQuestion.get(i);
            mot = thesaurus.rechercherSortiePourEntree(mot);
            if (!existeChaineDicho(motsOutils, mot)) {
                // n ne traite le mot que si ce n'est pas un mot-outil
                motDeQuestionSansMotsOutil.add(mot);
                ArrayList<Integer> sorties = IndexReponses.rechercherSorties(mot); // on récupère la liste des réponses contenant ce mot-clé précis
                vIndex = fusion(vIndex, sorties); // on fusionne cette liste avec les précédentes pour cumuler les occurrences
                nbMotsTrouves++;
            }
        }

        // 4. On ne garde que les réponses qui contiennent tous les mots-clés de la question
        vIndex = maxOccurences(vIndex, nbMotsTrouves);

        if(nbMotsTrouves == 0) {
            return new ArrayList<>();
        }
        return vIndex;
    }


    static public boolean estUnNombre(String s) {
        //{s est non vide}=>{résultat = true si s ne contient que des caractères représentant des chiffres (>='0'&<='9') et false sinon}
        boolean estUnNb = true;
        for (int i =  0 ; i<s.length() ; i++){
            if (s.charAt(i) < '0' | s.charAt(i) > '9'){
                estUnNb = false;
            }
        }

        return estUnNb;
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
        ArrayList<Integer> reponsesInt = new ArrayList<>();
        ArrayList<String> vDeQuestion = decoupeEnMots(calculForme(question, motsOutils, thesaurus));
        ArrayList<Integer> vFusion = new ArrayList<>();
        int seuil = 0;
        for(int i=0; i<vDeQuestion.size(); i++){
            // recherche des formes de réponses compatibles dans l'IndexFormes
            String motActu = vDeQuestion.get(i);
            if(estUnNombre(motActu)){
                motActu = "NUM";
            }
            String motTraite = thesaurus.rechercherSortiePourEntree(motActu);
            String motCle = motTraite.toLowerCase() + "_" + i;
            // fusion des indices de formes de réponses correspondant à ce mot-outil
            vFusion = fusion(vFusion, IndexFormes.rechercherSorties(motCle));
        }
        vFusion = maxOccurences(vFusion, seuil); // sélection des formes qui apparaissent le plus souvent
        for(int i = 0; i<candidates.size(); i++){
            // filtrage des réponses candidates : on ne garde que celles qui ont une forme validée
            String formeCandidate = calculForme(reponses.get(candidates.get(i)), motsOutils, thesaurus); // on récupère la forme de la réponse candidate actuelle
            boolean valide = false;
            int j = 0;
            while (j<vFusion.size() && !valide){
                // on compare cette forme avec vFusion des formes validées
                if(formeCandidate.compareTo(formesReponses.get(vFusion.get(j))) == 0){
                    valide = true;
                }
                j++;
            }
            if(valide){
                // si la forme correspond, la réponse est ajoutée à la sélection finale
                reponsesInt.add(candidates.get(i));
            }
        }
        return reponsesInt;
    }

    static public boolean reponseExiste(String reponse,
                                        Index indexReponses,
                                        ArrayList<String> reponses,
                                        ArrayList<String> motsOutils,
                                        Thesaurus thesaurus) {
        //{}=>{résultat = true si la reponse est présente dans reponses et false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences
        // remarque 2 : Le vecteur reponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, on utilise indexReponses pour trouver les réponses contenant tous les mots non outils de la
        // reponse, puis on vérifie si l'une d'entre elle est identique à reponse.}

        ArrayList<String> motDeRep = decoupeEnMots(reponse);
        ArrayList<Integer> repPossible = new ArrayList<>();
        for (int i = 0 ; i < motDeRep.size() ; i++) {

            String motActu = motDeRep.get(i);
            motActu = thesaurus.rechercherSortiePourEntree(motActu);
            motDeRep.set(i, motActu);

            if (existeChaineDicho(motsOutils, motDeRep.get(i))) {
                // si le mot est un mot-outil, on le supprime de la liste de recherche
                motDeRep.remove(i);
            }

        }

        for (int j = 0; j <motDeRep.size(); j++){
            // on fusionne les listes d'indices de réponses trouvées pour chaque mot-clé
            repPossible = fusion(repPossible, indexReponses.rechercherSorties(motDeRep.get(j)));
        }

        repPossible = maxOccurences(repPossible, motDeRep.size()); // on ne garde que les indices de réponses qui contiennent tous les mots-clés de la recherche
        int k =0;
        while (k < repPossible.size() && reponses.get(repPossible.get(k)).compareTo(reponse) ==0 ){
            // on parcourt les candidats trouvés pour voir si l'un d'eux est strictement identique à 'reponse'
            k++;
        }
        if (k != repPossible.size()){
            // si k n'est pas arrivé au bout de repPossible, c'est qu'une correspondance a été trouvée
            return true;
        } else {
            return false;
        }
    }


    static public boolean formeQuestionReponseExiste(String question,
                                                     String reponse,
                                                     Index indexFormes,
                                                     ArrayList<String> formesReponses,
                                                     ArrayList<String> motsOutils,
                                                     Thesaurus thesaurus) {
        //{}=>{résultat = * true si la forme de reponse est présente dans formesReponses
        // et qu'elle est accessible à partir des mots de la question en utilisant indexFormes.
        //                * false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : Le vecteur formesReponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, et afin de vérifier l'accessibilité à partir des mots de la question en utilisant indexFormes,
        // on utilise indexFormes pour trouver les formes indexées par les mots-outils de la
        // question, puis on vérifie si l'une de ces formes est identique à la forme de reponse.
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de question sont pris en compte}

        ArrayList<String> formesQuestion = decoupeEnMots(calculForme(question, motsOutils, thesaurus));

        // conversion des indices obtenus en chaînes de caractères
        ArrayList<Integer> vFusion = new ArrayList<>();
        for (int i = 0; i < formesQuestion.size(); i++) {
            // on fusionne les indices de formes de réponses liés à chaque mot-outil de la question
            vFusion = fusion(vFusion, indexFormes.rechercherSorties(formesQuestion.get(i)));
        }

        vFusion = maxOccurences(vFusion, formesQuestion.size());

        ArrayList<String> formeRepPossible = new ArrayList<>();
        for (int j = 0; j < vFusion.size(); j++) {
            formeRepPossible.add(formesReponses.get(vFusion.get(j)));
        }
        // la forme de la réponse proposée est-elle dans la liste des formes autorisées ?
        return existeChaineDicho(formeRepPossible, calculForme(reponse, motsOutils, thesaurus));
    }
}
