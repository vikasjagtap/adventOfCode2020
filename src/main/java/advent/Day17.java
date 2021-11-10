package advent;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Objects;

public class Day17 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day17.txt"))) {
            find(bufferedReader, point -> new Cube(point.x, point.y, 0));
            find(bufferedReader, point -> new HyperCube(point.x, point.y, 0, 0));
        }

    }

    private static void find(BufferedReader bufferedReader, Function<Point, Surrounded> supplier) throws IOException {
        final Map<Surrounded, Boolean> cubes = new HashMap<>();

        String line;
        int y = 0;
        while ((line = bufferedReader.readLine()) != null) {
            final char[] chars = line.toCharArray();
            for (int x = 0, charsLength = chars.length; x < charsLength; x++) {
                char c = chars[x];
                final Surrounded cube = supplier.apply(new Point(x, y));
                cubes.put(cube, c == '#');
                cube.getNeighbors().forEach(cube1 -> cubes.putIfAbsent(cube1, false));
            }
            y++;
        }


        final Map<Surrounded, Boolean> newCubes = boot(cubes, 0);
        final long count = newCubes.values().stream().filter(aBoolean -> aBoolean).count();
        System.out.println(count);
    }

    interface Surrounded {
        Set<Surrounded> getNeighbors();
    }

    static class Cube implements Surrounded {
        final int x;
        final int y;
        final int z;
        private Set<Surrounded> neighbors;

        Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public Set<Surrounded> getNeighbors() {
            if (neighbors != null) {
                return neighbors;
            }
            this.neighbors = new HashSet<>(26);
            for (int i = -1; i<=1; i++) {
                for (int j = -1; j<=1; j++) {
                    for (int k = -1; k<=1; k++) {
                        final Cube neighbor = new Cube(this.x + i, this.y + j, this.z + k);
                        if (!this.equals(neighbor)) {
                            this.neighbors.add(neighbor);
                        }
                    }
                }
            }
            return neighbors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Cube)) {
                return false;
            }
            Cube that = (Cube) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x, y, z);
        }
    }

    static class HyperCube extends Cube {
        final int w;
        private Set<Surrounded> neighbors;


        public HyperCube(int x, int y, int z, int w) {
            super(x, y, z);
            this.w = w;
        }

        @Override
        public Set<Surrounded> getNeighbors() {
            if (neighbors != null) {
                return neighbors;
            }
            this.neighbors = new HashSet<>(80);
            for (int i = -1; i<=1; i++) {
                for (int j = -1; j<=1; j++) {
                    for (int k = -1; k<=1; k++) {
                        for (int l = -1; l<=1; l++) {
                            final HyperCube neighbor = new HyperCube(this.x + i, this.y + j, this.z + k, this.w + l);
                            if (!this.equals(neighbor)) {
                                this.neighbors.add(neighbor);
                            }
                        }
                    }
                }
            }
            return neighbors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof HyperCube)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            HyperCube hyperCube = (HyperCube) o;
            return w == hyperCube.w;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(super.hashCode(), w);
        }
    }
    private static Map<Surrounded, Boolean> boot(Map<Surrounded, Boolean> cubes, int cycle) {
        if (cycle == 6) {
            return cubes;
        }
        Map<Surrounded, Boolean> newCubes = new HashMap<>(cubes);

        cubes.forEach((cube, active) -> {
            long activeNeighbors = 0L;
            for (Surrounded c : cube.getNeighbors()) {
                final boolean aBoolean = cubes.getOrDefault(c, false);
                newCubes.putIfAbsent(c, aBoolean);
                if (aBoolean) {
                    activeNeighbors++;
                }
            }
            if (active && activeNeighbors != 2 && activeNeighbors != 3) {
                newCubes.put(cube, false);
            } else if (!active && activeNeighbors == 3) {
                newCubes.put(cube, true);
            }
        });

        return boot(newCubes, cycle + 1);
    }

}
