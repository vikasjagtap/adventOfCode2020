package advent;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day13 {

    static class Bus {
        final long id;
        final int interval;
        Bus left;
        Bus right;

        public Bus(long id, int interval) {
            this.id = id;
            this.interval = interval;
        }

        public long getId() {
            return id;
        }

        public int getInterval() {
            return interval;
        }

        public Optional<Bus> getLeft() {
            return Optional.ofNullable(left);
        }

        public void setLeft(Bus left) {
            this.left = left;
        }

        public Optional<Bus> getRight() {
            return Optional.ofNullable(right);
        }

        public void setRight(Bus right) {
            this.right = right;
        }
    }

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day13.txt"))) {

            final List<String> lines = bufferedReader.lines().collect(Collectors.toList());

//            System.out.println(part2(lines.get(7)));
            System.out.println(part2CRT(lines.get(1)));
//            System.out.println(part2(lines.get(6)));
        }
    }

    private static long part2CRT(String s) {
        final String[] busIds  = s.split(",");
        List<Bus> buses = new ArrayList<>();
        long product = 1;
        for (int i = 0; i < busIds.length; i++) {
            final String busId = busIds[i];
            if (!busId.equalsIgnoreCase("x")) {
                final long id = Long.parseLong(busId);
                Bus bus = new Bus(id, i);
                buses.add(bus);
                product *= id;
            }
        }

        long sum = 0;

        for (Bus bus : buses) {
            final long moduli = bus.getId();
            final int interval = bus.getInterval();
            final long remainder = interval == 0 ? 0 : moduli - interval;
            long m = product/ moduli;
            long rem = m % moduli;

            long y = 1;
            long i;

            do {
                y+=moduli;
                i = y%rem;
            } while (i != 0);
            y = y/rem;

            final long n = m * remainder * y;
            sum += n;
        }
       return sum%product;
    }


    private static long part2(String s) {
        final String[] busIds  = s.split(",");
        final Map<Long, Integer> maps = new TreeMap<>();
        List<Long> bi = new ArrayList<>();
        Bus prevBus = null;
        Bus maxIdBus = null;
        for (int i = 0; i < busIds.length; i++) {
            final String busId = busIds[i];
            if (!busId.equalsIgnoreCase("x")) {
                final long id = Long.parseLong(busId);
                Bus bus = new Bus(id, i + 1);
                if (prevBus != null) {
                    bus.setLeft(prevBus);
                    prevBus.setRight(bus);
                }

                if (maxIdBus == null || maxIdBus.getId() < bus.getId()) {
                    maxIdBus = bus;
                }
                prevBus = bus;
                maps.put(id, i + 1);
                bi.add(id);
            }
        }

        int i = 1;
        long timestamp = 0L;
        long leftBusTime = 0;
        while (true) {
            final long busId = maxIdBus.getId();
            long r = timestamp % busId;
            long time = r == 0 ? timestamp : timestamp - r + busId;
            timestamp += maxIdBus.getId();
            Optional<Bus> right = maxIdBus.getRight();
            Bus leftBus = maxIdBus;
            boolean continueLoop = false;
            long busTime = time;
            while (right.isPresent()) {
                Bus rightBus = right.get();
                long rightBusTime = busTime + (rightBus.getInterval() - leftBus.getInterval());
                if (rightBusTime % rightBus.getId() != 0) {
                    continueLoop = true;
                    break;
                } else {
                    busTime = rightBusTime;
                }
                leftBus = rightBus;
                right = rightBus.getRight();
            }

            if (continueLoop) {
                continue;
            }
            continueLoop = false;

            Optional<Bus> left = maxIdBus.getLeft();
            Bus rightBus = maxIdBus;
            busTime = time;
            if (!left.isPresent()) {
                leftBusTime = time;
                break;
            }

            while (left.isPresent()) {
                Bus lb = left.get();
                leftBusTime = busTime - (rightBus.getInterval() - lb.getInterval());
                if (leftBusTime % lb.getId() != 0) {
                    continueLoop = true;
                    break;
                } else {
                    busTime = leftBusTime;
                }
                rightBus = lb;
                left = lb.getLeft();
            }

            if (!continueLoop) {
                break;
            }

        }


        return leftBusTime;
    }

    private static long part1(List<String> lines) {
        long timestamp = Integer.parseInt(lines.get(0));
        long[] busIds  = Arrays.stream(lines.get(1).split(",")).filter(busId -> !busId.equalsIgnoreCase("x")).mapToLong(Long::parseLong).sorted().toArray();
        Map<Integer, Integer> maps = new HashMap<>();
        long min = Long.MAX_VALUE;
        long chosenBusId = 0;
        for (long busId : busIds) {
            long r = timestamp%busId;
            if (r == 0) {
                min = timestamp;
                chosenBusId = busId;
            } else {
                final long i = timestamp - r + busId;
                if (min > i) {
                    min = i;
                    chosenBusId = busId;
                }
            }
        }

        return (min - timestamp) * chosenBusId;
    }


}
