import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tests {
    public static void main(String[] args) {
        ArrayList<Integer> v = new ArrayList<>(Arrays.asList(3,4,5,5,5,6,6,8,8,8,12,16,16,20,20,20,20));
        ArrayList<Integer> y = new ArrayList<>(Arrays.asList(3,4,5,5,5,6,6,8,8,8,12,16,16,20,20,20));
        ArrayList<Integer> v2 = Utilitaire.maxOccurences(v, 3);
        ArrayList<Integer> y2 = Utilitaire.maxOccurences(v, 3);
        System.out.println(v2);
        System.out.println(y);
    }
}
