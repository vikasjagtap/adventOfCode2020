package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day18.txt"))) {
            calculate(bufferedReader);
        }
    }

    private static void calculate(BufferedReader bufferedReader) throws IOException {
        String line;
        long sum = 0;
        while ((line = bufferedReader.readLine()) != null) {
            sum  += calcPart2(line);
        }

        System.out.println(sum);
    }

    private static long calcPart2(String line) {
        final StringBuilder token = new StringBuilder();
        Long leftNumber = null;
        Long rightNumber = null;
        Character operation = null;
        int bracketCount = 0;
        List<Long> multipliers = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            switch (c) {
                case '+':
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    operation = '+';
                    rightNumber = null;
                    break;
                case '*':
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    operation = '*';
                    rightNumber = null;
                    break;
                case ' ':
                    if (bracketCount > 0) {
                        token.append(c);
                    }
                    break;
                case '(':
                    if (bracketCount > 0) {
                        token.append(c);
                    }
                    rightNumber = null;
                    bracketCount++;
                    break;
                case ')':
                    bracketCount--;
                    if (bracketCount == 0) {
                        final long number = calcPart2(token.toString());
                        if (leftNumber != null) {
                            rightNumber = number;
                        } else {
                            leftNumber = number;
                        }

                        if (rightNumber != null && operation != null) {

                            if (operation == '+') {
                                leftNumber = leftNumber + rightNumber;
                            }
                            if (operation == '*') {
                                multipliers.add(leftNumber);
                                leftNumber = rightNumber;
                            }

                            operation = null;
                            rightNumber = null;
                        }
                        token.setLength(0);
                    } else {
                        token.append(c);
                    }
                    break;
                default:
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    final long number = Character.getNumericValue(c);
                    if (leftNumber != null) {
                        rightNumber = number;
                    } else {
                        leftNumber = number;
                    }

                    if (rightNumber != null && operation != null) {

                        if (operation == '+') {
                            leftNumber = leftNumber + rightNumber;
                        }
                        if (operation == '*') {
                            multipliers.add(leftNumber);
                            leftNumber = rightNumber;
                        }

                        operation = null;
                        rightNumber = null;
                    }

            }
        }
        for (Long m : multipliers) {
            leftNumber *= m;
        }
        return leftNumber;
    }
    private static long calcPart1(String line) {
        final StringBuilder token = new StringBuilder();
        Long leftNumber = null;
        Long rightNumber = null;
        Character operation = null;
        int bracketCount = 0;
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            switch (c) {
                case '+':
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    operation = '+';
                    rightNumber = null;
                    break;
                case '*':
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    operation = '*';
                    rightNumber = null;
                    break;
                case ' ':
                    if (bracketCount > 0) {
                        token.append(c);
                    }
                    break;
                case '(':
                    if (bracketCount > 0) {
                        token.append(c);
                    }
                    rightNumber = null;
                    bracketCount++;
                    break;
                case ')':
                    bracketCount--;
                    if (bracketCount == 0) {
                        final long number = calcPart1(token.toString());
                        if (leftNumber != null) {
                            rightNumber = number;
                        } else {
                            leftNumber = number;
                        }

                        if (rightNumber != null && operation != null) {

                            if (operation == '+') {
                                leftNumber = leftNumber + rightNumber;
                            }
                            if (operation == '*') {
                                leftNumber = leftNumber * rightNumber;
                            }

                            operation = null;
                            rightNumber = null;
                        }
                        token.setLength(0);
                    } else {
                        token.append(c);
                    }
                    break;
                default:
                    if (bracketCount > 0) {
                        token.append(c);
                        break;
                    }
                    final long number = Character.getNumericValue(c);
                    if (leftNumber != null) {
                        rightNumber = number;
                    } else {
                        leftNumber = number;
                    }

                    if (rightNumber != null && operation != null) {

                        if (operation == '+') {
                            leftNumber = leftNumber + rightNumber;
                        }
                        if (operation == '*') {
                            leftNumber = leftNumber * rightNumber;
                        }

                        operation = null;
                        rightNumber = null;
                    }

            }
        }
        return leftNumber;
    }


}
