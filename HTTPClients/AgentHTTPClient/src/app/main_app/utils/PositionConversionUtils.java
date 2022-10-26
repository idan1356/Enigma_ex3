package app.main_app.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PositionConversionUtils {
    String ABC;
    Integer numOfRotors;

    public PositionConversionUtils(String ABC, int numOfRotors){
        this.ABC = ABC;
        this.numOfRotors = numOfRotors;
    }

    public PositionConversionUtils(String ABC){
        this.ABC = ABC;
    }

    public int rotorsPositionToInt(String rotorsPosition){
        String reversed = new StringBuilder(rotorsPosition).reverse().toString();
        int cur = 1;
        int res = 0;

        for (char ch : reversed.toCharArray()){
            res += ABC.indexOf(ch) * cur;
            cur *= ABC.length();
        }
        return res;
    }

    public String intToRotorPosition(int rotorPosition){
        StringBuilder s = new StringBuilder();

        while (rotorPosition > 0) {
            s.append(ABC.charAt(rotorPosition % ABC.length()));
            rotorPosition /= ABC.length();
        }

        return padString(s.reverse().toString());
    }

    private String padString(String str){
        int padNum = numOfRotors - str.length();
        String res = "";
        if (padNum > 0)
            res = IntStream.range(1, padNum + 1).mapToObj(index -> "" + ABC.charAt(0))
                    .collect(Collectors.joining());

        return res + str;
    }
}
