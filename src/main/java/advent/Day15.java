package advent;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day15 {

    public static void main(String[] args) throws IOException {
        final String input = "20,9,11,0,1,2";
//        final String input = "0,3,6";


        System.out.println(part1(input));
    }

    static class Last2Turns {
        Long peek = null;
        Long second = null;

        void push(long peek) {
            this.second = this.peek;
            this.peek = peek;
        }

        long diff() {
            if (peek == null || second == null) {
                return 0;
            }
            return peek - second;
        }
    }

    private static long part1(String input) {
        final Map<Long, Last2Turns> lastSpokenTurn = new HashMap<>();
        final String[] numbers = input.split(",");
        int numbersLength = numbers.length;
        long lastSpokenNumber = 0;

        for (int i = 0; i < numbersLength; i++) {
            lastSpokenNumber = Integer.parseInt(numbers[i]);
            lastSpokenTurn.computeIfAbsent(lastSpokenNumber, number -> new Last2Turns()).push(i + 1L);
        }

        for(long turn = numbersLength + 1L; turn <= 30000000; turn++) {
            final Last2Turns last2Turns = lastSpokenTurn.get(lastSpokenNumber);

            if (last2Turns == null || last2Turns.diff() == 0) {
                lastSpokenNumber = 0;
            } else {
                lastSpokenNumber = last2Turns.diff();
            }
            lastSpokenTurn.computeIfAbsent(lastSpokenNumber, number -> new Last2Turns()).push(turn);
            System.out.print("turn: " + turn +"\r");
        }

        return lastSpokenNumber;


    }


}
