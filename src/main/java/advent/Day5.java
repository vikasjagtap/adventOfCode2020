package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        int maxSeatId = 0;
        List<Integer> seats = new ArrayList<>();
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day5.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                int i = 0;
                int sRow = 0;
                int eRow = 127;

                int sCol = 0;
                int eCol = 7;

                for (char c: line.toCharArray()) {
                    if (i < 7) {
                        final int middle = (eRow - sRow)/2 + 1;
                        if (c == 'F') {
                            eRow = eRow - middle;
                        } else if (c == 'B') {
                            sRow = sRow + middle;
                        }
                    } else {
                        final int middle = (eCol - sCol)/2 + 1;
                        if (c == 'L') {
                            eCol = eCol - middle;
                        } else if (c == 'R') {
                            sCol = sCol + middle;
                        }
                    }

                    i++;
                }
                int seatId = (eRow) * 8 + (eCol);
                seats.add(seatId);

            }
            seats.sort(Comparator.naturalOrder());
            int lastNumber = 0;
            for (Integer integer : seats) {
                if (lastNumber != 0 && lastNumber == integer - 2) {
                    System.out.println(integer-1);
                }
                lastNumber = integer;
            }

        }
    }
}
