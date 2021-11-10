package advent;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class day11 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day11.txt"))) {
            final List<char[]> seats = bufferedReader.lines()
                .map(String::toCharArray)
                .collect(Collectors.toList());

            final List<char[]> newSeats = part2(seats, new HashMap<Point, Map<String, Point>>());

            int oc = 0;
            for (int i =0; i < newSeats.size(); i++) {
                for(int j =0; j < newSeats.get(i).length; j++) {
                    if (newSeats.get(i)[j] == '#') {
                        oc++;
                    }
                }
            }
            System.out.println(oc);
        }
    }

    private static List<char[]> part2(List<char[]> seats, HashMap<Point, Map<String, Point>> points) {
        int count = 0;
        List<char[]> newSeats = seats.stream().map(chars -> Arrays.copyOf(chars, chars.length)).collect(Collectors.toList());

        for (int i =0; i < seats.size(); i++) {
            for(int j =0; j < seats.get(i).length; j++) {
                final char c = seats.get(i)[j];
                if (c == '.') {
                    continue;
                }
                Map<Character, AtomicInteger> adjacents = findFirstAdjacents(new Point(j,i), seats, points);
                final AtomicInteger atomicInteger = adjacents.get('#');
                if (c == 'L' && atomicInteger == null) {
                    count++;
                    newSeats.get(i)[j] = '#';
                }
                if (c == '#' && atomicInteger != null && atomicInteger.get() >= 5) {
                    count++;
                    newSeats.get(i)[j] = 'L';
                }
            }
        }

        if (count > 0) {
            newSeats = part2(newSeats, points);
        }

        return newSeats;
    }

    private static List<char[]> part1(List<char[]> seats) {
        int count = 0;
        List<char[]> newSeats = seats.stream().map(chars -> Arrays.copyOf(chars, chars.length)).collect(Collectors.toList());

        for (int i =0; i < seats.size(); i++) {
            for(int j =0; j < seats.get(i).length; j++) {
                Map<Character, AtomicInteger> adjacents = findAdjacents(i,j, seats);
                final AtomicInteger atomicInteger = adjacents.get('#');
                final char c = seats.get(i)[j];
                if (c == 'L' && atomicInteger == null) {
                    count++;
                    newSeats.get(i)[j] = '#';
                }
                if (c == '#' && atomicInteger != null && atomicInteger.get() >= 4) {
                    count++;
                    newSeats.get(i)[j] = 'L';
                }
            }
        }

        if (count > 0) {
            newSeats = part1(newSeats);
        }

        return newSeats;
    }

    private static Map<Character, AtomicInteger> findFirstAdjacents(Point point, List<char[]> seats, Map<Point, Map<String, Point>> points) {
        Map<Character, AtomicInteger> adjacents = new HashMap<>();


        final Map<String, Point> stringPointMap = points.get(point);
        if (stringPointMap != null) {
            final String t = getDirectionString(true, false, false, false);
            final String tl = getDirectionString(true, true, false, false);
            final String tr = getDirectionString(true, false, true, false);
            final String l = getDirectionString(false, true, false, false);
            final String r = getDirectionString(false, false, true, false);
            final String br = getDirectionString(false, false, true, true);
            final String bl = getDirectionString(false, true, false, true);
            final String b = getDirectionString(false, false, false, true);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, t);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, tl);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, tr);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, l);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, r);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, br);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, bl);
            loadAdjacentsFromCache(seats, adjacents, stringPointMap, b);
            return adjacents;
        }


        final int maxY = seats.size() - 1;
        final int maxX = seats.get(point.y).length - 1;
        Point top = new Point(point.x, Math.max(0, point.y -1));
        Point left = new Point(Math.max(0, point.x -1), point.y);
        Point bottom = new Point(point.x, Math.min(maxY, point.y + 1));
        Point right = new Point(Math.min(maxX, point.x + 1), point.y);

        Point topLeft = new Point(Math.max(0, point.x -1), Math.max(0, point.y -1));
        Point bottomLeft = new Point(Math.max(0, point.x -1), Math.min(maxY, point.y + 1));
        Point topRight = new Point(Math.min(maxX, point.x + 1), Math.max(0, point.y -1));
        Point bottomRight = new Point(Math.min(maxX, point.x + 1), Math.min(maxY, point.y + 1));


        if (point.x == 0) {
            bottomLeft = null;
            topLeft = null;
            left = null;
        }

        if (point.x == maxX) {
            right = null;
            topRight =null;
            bottomRight = null;
        }

        if (point.y == maxY) {
            bottom = null;
            bottomRight = null;
            bottomLeft = null;
        }
        if (point.y == 0) {
            top = null;
            topRight =null;
            topLeft = null;
        }

        findFirst(point, seats, adjacents, top, points, true, false, false, false);
        findFirst(point, seats, adjacents, left, points,false, true, false, false);
        findFirst(point, seats, adjacents, right, points,false, false, true, false);
        findFirst(point, seats, adjacents, bottom, points, false, false, false, true);

        findFirst(point, seats, adjacents, topLeft, points,true, true, false, false);
        findFirst(point, seats, adjacents, topRight, points, true, false, true, false);
        findFirst(point, seats, adjacents, bottomRight, points,false, false, true, true);
        findFirst(point, seats, adjacents, bottomLeft, points,false, true, false, true);

        return adjacents;
    }

    private static void loadAdjacentsFromCache(List<char[]> seats, Map<Character, AtomicInteger> adjacents, Map<String, Point> stringPointMap, String direction) {
        Point cachedPoint = stringPointMap.get(direction);
        if (cachedPoint != null) {
            char c = charAt(seats, cachedPoint);
            if (c == 'L' || c == '#') {
                adjacents.computeIfAbsent(c, character -> new AtomicInteger()).incrementAndGet();
            }
        }
    }

    private static void findFirst(Point findFrom, List<char[]> seats, Map<Character, AtomicInteger> adjacents, Point start, Map<Point, Map<String, Point>> points,
                                  boolean top, boolean left, boolean right, boolean bottom) {
        if (start == null) {
            return;
        }
        int maxX = seats.get(findFrom.y).length -1;
        int maxY = seats.size() -1;

        points.putIfAbsent(findFrom, new HashMap<>());
        final String direction = getDirectionString(top, left, right, bottom);

        final Point point = points.get(findFrom).get(direction);
        if (point != null) {
            char c = charAt(seats, point);
            if (c == 'L' || c == '#') {
                adjacents.computeIfAbsent(c, character -> new AtomicInteger()).incrementAndGet();
            }
        }

        while (start.x <= maxX && start.y <= maxY && start.x >=0 && start.y >=0) {
            char c = charAt(seats, start);
            if (c == 'L' || c == '#') {
                adjacents.computeIfAbsent(c, character -> new AtomicInteger()).incrementAndGet();
                points.get(findFrom).put(direction, start);
                break;
            }
            if (top) {
                start.y = start.y - 1;
            }
            if (bottom) {
                start.y = start.y + 1;
            }
            if (left) {
                start.x = start.x - 1;
            }
            if (right) {
                start.x =start.x + 1;
            }
        }
    }

    private static String getDirectionString(boolean top, boolean left, boolean right, boolean bottom) {
        String direction = "";
        if (top) {
            direction += "t";
        }
        if (left) {
            direction += "l";
        }
        if (right) {
            direction += "r";
        }
        if (bottom) {
            direction += "b";
        }
        return direction;
    }

    private static char charAt(List<char[]> seats, Point point) {
        return seats.get(point.y)[point.x];
    }

    private static Map<Character, AtomicInteger> findAdjacents(int i, int j, List<char[]> seats) {
        Map<Character, AtomicInteger> adjacents = new HashMap<>();
        int length = seats.size();
        IntStream.range(-1,2).forEach((n) -> {
            final int i1 = i + n;
            if (i1 >= 0 && i1 < length) {
                IntStream.range(-1,2).forEach((n1) -> {
                    final int j1 = j + n1;
                    if (j1 >= 0 && j1 < seats.get(i1).length && (j1 != j || (j1 == j && i1 != i))) {
                        adjacents.computeIfAbsent(seats.get(i1)[j1], character -> new AtomicInteger()).incrementAndGet();
                    }

                });
            }
        });

        return adjacents;
    }
}
