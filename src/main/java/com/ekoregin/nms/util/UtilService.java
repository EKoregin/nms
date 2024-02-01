package com.ekoregin.nms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilService {

    public static String convertStringWithExpressions(String source) {
        var stringArr = source.split("\\s+");
        StringBuilder newString = new StringBuilder();
        for (String word: stringArr) {
            newString.append(replaceExpressionInString(word)).append(" ");
        }
        return newString.toString();
    }

    /*Find expression in string and replace it with expression result. String in [] is an expression*/
    private static String replaceExpressionInString(String input) {
        String regex = "\\[[^\\]]*\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String foundExpr = "";
        String exprString = input;
        while (matcher.find()) {
            foundExpr = matcher.group(0);
            exprString = exprString.replace(foundExpr, stringCalc(foundExpr.substring(1, foundExpr.length()-1)));
        }
        return foundExpr.isEmpty() ? input : exprString;
    }

    /*Calc expression in string*/
    private static String stringCalc(String expr) {
        String[] numbers = expr.split("[+-]");
        String[] operators = expr.split("[0-9]+");
        int aggregator = Integer.parseInt(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            if (operators[i].equals("+"))
                aggregator += Integer.parseInt(numbers[i]);
            else if (operators[i].equals("-")) {
                aggregator -= Integer.parseInt(numbers[i]);
            }
        }
        return Integer.toString(aggregator);
    }
}
