import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tests {
    public static void main(String[] args) {
        String queNb = "1950";
        String nbetLettre = "en1980";
        String quelettre = "bonjour";

        System.out.println("Que nb : " + Utilitaire.estUnNombre(queNb));
        System.out.println("nb et lettre: " + Utilitaire.estUnNombre(nbetLettre));
        System.out.println("Que lettre : " + Utilitaire.estUnNombre(quelettre));


    }
}
