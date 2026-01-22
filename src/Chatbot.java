import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.Scanner;

public class Chatbot {

    private static final String MESSAGE_IGNORANCE = "Je ne sais pas.";
    private static final String MESSAGE_APPRENTISSAGE = "Je vais te l'apprendre.";
    private static final String MESSAGE_BIENVENUE = "Bonjour Padawan. pose moi ta question.";
    private static final String MESSAGE_QUITTER = "Au revoir.";
    private static final String MESSAGE_INVITATION = "Je t'écoute.";
    private static final String MESSAGE_CONFIRMATION = "Très bien, c'est noté.";

    private static Index indexThemes; // index pour trouver rapidement les réponses à partir des mots NON outils de la question
    private static Index indexFormes; // index pour trouver rapidement les formes de réponse possibles à partir des mots-outils de la question

    static private ArrayList<String> motsOutils; // vecteur trié des mots outils
    static private ArrayList<String> reponses; // vecteur des réponses
    private static ArrayList<String> formesReponses; //vecteur des formes de réponses
    private static Thesaurus thesaurus; //thésaurus
    private static ArrayList<String> reponseMeta ; // vecteur des réponses meta
    private  static ArrayList<String> questionsReponses ;




    public static void main(String[] args) {

        // initialisation du vecteur des mots outils
        motsOutils = Utilitaire.lireMotsOutils("mots-outils.txt");
        // tri du vecteur des mots outils
        Utilitaire.trierChaines(motsOutils);


        // initialisation du vecteur des réponses
        reponses = Utilitaire.lireReponses("projet_reponse.txt");

        // initialisation du thésaurus (partie 2)
        thesaurus = new Thesaurus("thesaurus.txt");

        // initialisation du vecteur des réponses méta.
        reponseMeta = Utilitaire.lireReponses("meta-question.txt");

        // construction de l'index pour retrouver rapidement les réponses sur leurs thématiques
        indexThemes = Utilitaire.constructionIndexReponses(reponses, motsOutils, thesaurus);
//        indexThemes.afficher();

        // construction de la table des formes de réponses
        formesReponses = Utilitaire.constructionTableFormes(reponses, motsOutils, thesaurus);
        Utilitaire.trierChaines(formesReponses);

        // initialisation du vecteur des questions/réponses idéales
        questionsReponses = Utilitaire.lireQuestionsReponses("projet_question-reponse.txt");

        // construction de l'index pour retrouver rapidement les formes possibles de réponses à partir des mots outils de la question
        indexFormes = Utilitaire.constructionIndexFormes(questionsReponses, formesReponses, motsOutils, thesaurus);
//        indexFormes.afficher();

        String reponse = "";
        String entreeUtilisateur = ""; // la dernière entrée de l'utilisateur


        Scanner lecteur = new Scanner(System.in);
        System.out.println();
        System.out.print("> ");
        System.out.println(MESSAGE_BIENVENUE);

        String entreUtilisateurPrec = "";
        do { // on attend des questions
            System.out.print("> ");
            entreeUtilisateur = lecteur.nextLine();
            if (entreeUtilisateur.compareTo(MESSAGE_QUITTER) != 0) { //tant que l'utilisateur ne veut pas arrêter
                if (entreeUtilisateur.compareToIgnoreCase(MESSAGE_APPRENTISSAGE) == 0){ //si l'utilisateur écrit "Je vais te l'apprendre."
                    if (entreeUtilisateur.isEmpty()) {
                        System.out.println("> Que veux tu m'apprendre ?");
                    } else {
                       reponse= apprentissage(entreUtilisateurPrec, lecteur);
                    }
                } else {
                    reponse = repondre(entreeUtilisateur);
                    entreUtilisateurPrec = entreeUtilisateur;
                }
                System.out.println("> " + reponse); //affichage de la réponse
            }
        } while (entreeUtilisateur.compareToIgnoreCase(MESSAGE_QUITTER) != 0); //continuer tant que l'utilsateur le souhaite
    }

    private static String question_prec = ""; //variable pour garder la question pécédente

    static private String repondre(String question) {
        int reponseEstMeta = estMetaQuestion(question,reponseMeta ) ;
        if (reponseEstMeta == -1){

            if (Utilitaire.entierementInclus(motsOutils, question)) { //si la question ne possède que des mots outils, on doit répondre dans le contexte de la question précédente
                return repondreEnContexte(question, question_prec);
            } else {
                //on crée une array list de réponses candidates par rapport à la question
                ArrayList<Integer> reponsesCandidates = Utilitaire.constructionReponsesCandidates(question, indexThemes, motsOutils, thesaurus);
//            System.out.println(reponsesCandidates);
                if (reponsesCandidates.isEmpty()) {
                    // si on ne trouve pas de réponses candidates, on retourne "ne sait pas"
                    return MESSAGE_IGNORANCE;
                }
                //on crée une array list de réponses séléction par rapport à la question
                ArrayList<Integer> reponsesSelectionnees = Utilitaire.selectionReponsesCandidates(question, reponsesCandidates, indexFormes, reponses, formesReponses, motsOutils, thesaurus);
                if (reponsesSelectionnees.isEmpty()) {
                    // si on ne trouve pas de réponses séléctionnés, on retourne "ne sait pas"
                    return MESSAGE_IGNORANCE;
                }
                //si on selectionne plusieurs réponses, on va en choisir une aléatoirement
                int choix = (int) (Math.random() * reponsesSelectionnees.size());
                question_prec = question;
                return reponses.get(reponsesSelectionnees.get(choix));
            }
        } else {
            int indexRep = reponseMeta.get(reponseEstMeta).indexOf("?");
            return reponseMeta.get(reponseEstMeta).substring(indexRep+1).trim();
        }
    }

    // partie 2
    static private String repondreEnContexte(String question, String questionPrecedente) {
        //on crée une array list de réponses candidates par rapport à la question précédente
        ArrayList<Integer> reponsesCandidates = Utilitaire.constructionReponsesCandidates(questionPrecedente, indexThemes, motsOutils, thesaurus);
        if (reponsesCandidates.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }
        //on crée une array list de réponses sélectionnées par rapport à la question actuelle
        ArrayList<Integer> reponsesSelectionnees = Utilitaire.selectionReponsesCandidates(question, reponsesCandidates, indexFormes, reponses, formesReponses, motsOutils, thesaurus);
        if (reponsesSelectionnees.isEmpty()) {
            return MESSAGE_IGNORANCE;
        }
        int choix = (int) (Math.random() * reponsesSelectionnees.size());
        return reponses.get(reponsesSelectionnees.get(choix));
    }

    static private String apprentissage(String question_prec, Scanner lecteur) {
        System.out.println("> " + MESSAGE_INVITATION);
        String reponse = lecteur.nextLine();

        if (Utilitaire.reponseExiste(reponse, indexThemes, reponses, motsOutils, thesaurus)) {
            //si la réponse existe déjà dans index
            // On vérifie si le lien entre la question précédente et cette réponse existe déjà
            if (Utilitaire.formeQuestionReponseExiste(question_prec, reponse, indexFormes, formesReponses, motsOutils, thesaurus)) {
                return "Je connais déjà cette information.";
            } else {
                Utilitaire.integrerNouvelleQuestionReponse(question_prec, reponse, formesReponses, indexFormes, motsOutils, thesaurus);
                // Sauvegarde de la nouvelle paire question-réponse dans le fichier d'apprentissage
                Utilitaire.ecrireFichier("projet_question-reponse.txt", question_prec + " " + reponse);
                return MESSAGE_CONFIRMATION;
            }
        } else { // si la réponse est totalement inconnue du système
            // On ajoute la nouvelle réponse à la table des formes et au fichier
            Utilitaire.integrerNouvelleReponse(reponse, reponses, indexThemes, formesReponses, thesaurus);
            Utilitaire.ecrireFichier("projet_reponse.txt", reponse);
            // Sauvegarde de la nouvelle paire question-réponse dans le fichier d'apprentissage
            Utilitaire.integrerNouvelleQuestionReponse(question_prec, reponse, formesReponses, indexFormes, motsOutils, thesaurus);
            Utilitaire.ecrireFichier("projet_question-reponse.txt", question_prec + " " + reponse);
            return MESSAGE_CONFIRMATION;
        }
    }

    static private int estMetaQuestion(String question, ArrayList<String> reponseMeta) {

        final  String LIST_QUESTION = "Liste des questions ?";

        //Si la question est LIST_QUESTION alors on renvois Toute les questions de projet-question-reponse
        /*Attention, si une nouvelle information est enregistré, elle n'apparaitra pas dans la liste des questions disponible
        si on veut quelle apparaisse il faut décommanter la ligne si dessous, mais
        cela forcera le chatbot a lire le fichier  projet_question-reponse.txt à chaque nouvelle question se qui n'est pas optimal pour le temps d'éxécution. */

        //ArrayList<String> questionsReponses =  Utilitaire.lireQuestionsReponses("projet_question-reponse.txt");

        if (question.compareToIgnoreCase(LIST_QUESTION) == 0){
            int indexQuestionActu ;
            String questionActu ;
            for (int j = 0; j < questionsReponses.size() ; j++){
                indexQuestionActu = questionsReponses.get(j).indexOf("?");
                questionActu = questionsReponses.get(j).substring(0, indexQuestionActu+1);
                System.out.println("["+j+"] --> " + questionActu);
            }
            return 0 ; //la première meta-question est liste-question
        }

        for (int i = 0; i < reponseMeta.size(); i++) {
            String ligne = reponseMeta.get(i);
            int pos = ligne.indexOf("?");
            String reponseActu = ligne.substring(0, pos + 1).trim();
            if (reponseActu.equalsIgnoreCase(question)) {
              return i;
            }
        }
        return -1;
    }
}