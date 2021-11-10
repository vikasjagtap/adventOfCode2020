package advent;

import static advent.Day12.Dir.*;
import static java.lang.Math.abs;


import java.io.BufferedReader;
import java.io.IOException;
import java.net.Authenticator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

public class Day12 {

    static class Turn {
        Character turn;
        int degree;

        public Turn(Character turn, int degree) {
            this.turn = turn;
            this.degree = degree;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Turn)) {
                return false;
            }
            Turn turn1 = (Turn) o;
            return degree == turn1.degree &&
                Objects.equal(turn, turn1.turn);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(turn, degree);
        }
    }

    enum Dir {
        E,
        W,
        S,
        N;

        static ImmutableBiMap<Dir, Dir> TURNS = ImmutableBiMap.of(E, N, W, S, N, W, S, E);

    }

    static class Pos {
        AtomicInteger forward = new AtomicInteger();
        AtomicInteger backward = new AtomicInteger();

        void setPos(int f, int b) {
            forward.set(f);
            backward.set(b);
        }

        void moveForward(int value) {
            move(value, forward, backward);
        }

        void moveBackward(int value) {
            move(value, backward, forward);
        }

        private void move(int value, AtomicInteger fDir, AtomicInteger bDir) {
            final int back = bDir.get();
            if (back > 0) {
                if (value > back) {
                    value -= back;
                    fDir.set(fDir.get() + value);
                    bDir.set(0);
                } else {
                    bDir.set(back - value);
                }
            } else {
                fDir.set(fDir.get() + value);
            }
        }

        public int getMDistance() {
            return forward.intValue() + backward.intValue();
        }

        public void move(int value, Pos pos) {
            final int xF = pos.forward.get();
            final int xB = pos.backward.get();
            moveForward(value * xF);
            moveBackward(value * xB);

        }
    }

    static class CoordPos {
        Pos x = new Pos();
        Pos y = new Pos();

        void rotateRight() {
            int xf = x.forward.get();
            int xb = x.backward.get();

            int yf = y.forward.get();
            int yb = y.backward.get();

            x.setPos(yf, yb);
            y.setPos(xb, xf);
        }

        void rotateLeft() {
            int xf = x.forward.get();
            int xb = x.backward.get();

            int yf = y.forward.get();
            int yb = y.backward.get();

            x.setPos(yb, yf);
            y.setPos(xf, xb);
        }

        public int getMDistance() {
            return x.getMDistance() + y.getMDistance();
        }
    }

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day12.txt"))) {

            final List<String> lines = bufferedReader.lines().collect(Collectors.toList());

            part2(lines);
        }
    }

    private static void part2(List<String> lines) {
        CoordPos shipPos = new CoordPos();
        CoordPos wayPointPos = new CoordPos();

        wayPointPos.x.setPos(10, 0);
        wayPointPos.y.setPos(1, 0);

        for (String line : lines) {
            final char dir = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));

            switch (dir) {
                case 'E': {
                    wayPointPos.x.moveForward(value);
                    break;
                }
                case 'W': {
                    wayPointPos.x.moveBackward(value);
                    break;
                }
                case 'N': {
                    wayPointPos.y.moveForward(value);
                    break;
                }
                case 'S': {
                    wayPointPos.y.moveBackward(value);
                    break;
                }
                case 'F': {
                    shipPos.x.move(value, wayPointPos.x);
                    shipPos.y.move(value, wayPointPos.y);
                    break;
                }
                case 'L': {
                    while(value > 0) {
                        wayPointPos.rotateLeft();
                        value -= 90;
                    }
                    break;
                }
                case 'R': {
                    while(value > 0) {
                        wayPointPos.rotateRight();
                        value -= 90;
                    }
                    break;
                }



            }

        }


        System.out.println(shipPos.getMDistance());
    }

    private static void part1(List<String> lines) {
        int index = 0;
        int acc = 0;
        boolean replaced = false;
        long eastDistance = 0;
        long westDistance = 0;
        long northDistance = 0;
        long southDistance = 0;
        String line = lines.get(index);
        Dir shipDir = E;
        while(line != null) {
            final char dir = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));
            boolean moved = false;

            switch (dir) {
                case 'E': {
                    if (westDistance > 0) {
                        if (value > westDistance) {
                            value -= westDistance;
                            eastDistance += value;
                            westDistance = 0;
                        } else {
                            westDistance -= value;
                        }
                    } else {
                        eastDistance += value;
                    }
                    break;
                }
                case 'W': {
                    if (eastDistance > 0) {
                        if (value > eastDistance) {
                            value -= eastDistance;
                            westDistance += value;
                            eastDistance = 0;
                        } else {
                            eastDistance -= value;
                        }
                    } else {
                        westDistance += value;
                    }
                    break;
                }
                case 'N': {
                    if (southDistance > 0) {
                        if (value > southDistance) {
                            value -= southDistance;
                            northDistance += value;
                            southDistance = 0;
                        } else {
                            southDistance -= value;
                        }
                    } else {
                        northDistance += value;
                    }
                    break;
                }
                case 'S': {
                    if (northDistance > 0) {
                        if (value > northDistance) {
                            value -= northDistance;
                            southDistance += value;
                            northDistance = 0;
                        } else {
                            northDistance -= value;
                        }
                    } else {
                        southDistance += value;
                    }
                    break;
                }
                case 'F': {
                    line = shipDir+String.valueOf(value);
                    moved = true;
                    break;
                }
                case 'L': {
                    while(value > 0) {
                        shipDir = TURNS.get(shipDir);
                        value -= 90;
                    }
                    break;
                }
                case 'R': {
                    while(value > 0) {
                        shipDir = TURNS.inverse().get(shipDir);
                        value -= 90;
                    }
                    break;
                }

            }
            if (moved) {
                continue;
            }

            index++;
            if (index < lines.size()) {
                line = lines.get(index);
            } else {
                break;
            }

        }


        System.out.println(eastDistance + northDistance + southDistance + westDistance);
    }


}
