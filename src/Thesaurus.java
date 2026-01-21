import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Thesaurus {

    private class EntreeSortie implements Comparable<EntreeSortie> {
        private String entree; // un mot
        private String sortie; // sa forme canonique

        public EntreeSortie(String entree, String sortie) {
            this.entree = entree;
            this.sortie = sortie;
        }

        public int compareTo(EntreeSortie o) {
            return this.entree.compareTo(o.entree);
        }
    }

    private ArrayList<EntreeSortie> table;

    public Thesaurus(String nomFichier) {
        //{}=>{ constructeur créant et initialisant l'attribut table à partir du contenu du fichier dont le nom est passé en paramètre, puis triant la table
        // en utilisant la méthode compareTo d'EntreeSortie
        // remarque 1 : utilise ajouterEntreeSortie et trierEntreesSorties
        // remarque 2 : pour la lecture du fichier, inspirez-vous de lireMotsOutils de Utilitaire
        // remarque 3 : pour les traitements de la chaîne lue, utilisez les méthodes indexOf,substring de String
        table = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int indEntre = ligne.indexOf(":");
                String entre = ligne.substring(0, indEntre);
                String sortie = ligne.substring(indEntre+1);
                ajouterEntreeSortie(entre, sortie);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        trierEntreesSorties(table);
        for (int i = 0 ; i <table.size() ; i++){
            System.out.println(table.get(i).entree +  "  " +table.get(i).sortie);
        }

    }

    public void ajouterEntreeSortie(String entree, String sortie) {
        //{}=>{ajoute à la fin de la table une nouvelle EntreeSortie avec les attributs entree et sortie}
        table.add(new EntreeSortie(entree, sortie));
    }


    public String rechercherSortiePourEntree(String entree) {
        // {l'attribut table du thesaurus est trié sur l'attribut entree des Entree-Sortie}=>
        // {résultat = la forme canonique associée à entree dans le thésaurus si l'entrée entree existe,
        // entree elle-même si elle n'existe pas. La recherche doit être dichotomique.
        // remarque : utilise compareTo de EntreeSortie }
        if (table.get(table.size()-1).entree.compareTo(entree)<0){
            return entree;
        } else {
            int inf = 0;
            int sup = table.size() - 1;

            while (inf < sup) {
                int m = (inf + sup) / 2;
                if (table.get(m).entree.toLowerCase().compareTo(entree.toLowerCase()) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (table.get(sup).entree.compareTo(entree) == 0) {
                return table.get(sup).sortie;
            } else {
                return entree;
            }
        }
        }

    static void trierEntreesSorties(ArrayList<EntreeSortie> v) {
        //{} => {trie v sur la base de la méthode compareTo de EntreeSortie}
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
                EntreeSortie temporaire = v.get(i);
                v.set(i, v.get(indMin));
                v.set(indMin, temporaire);
            }

            i = i + 1;
        }

    }


}
