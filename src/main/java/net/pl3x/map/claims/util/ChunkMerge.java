/*
 * MIT License
 *
 * Copyright (c) 2020-2023 William Blake Galbreath
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.pl3x.map.claims.util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import net.pl3x.map.claims.Chunk;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import org.jetbrains.annotations.NotNull;

// https://stackoverflow.com/a/56326866

public class ChunkMerge {
    public static @NotNull Polygon getPoly(@NotNull String key, @NotNull List<? extends Chunk> chunks) {
        Area area = new Area();
        for (Chunk chunk : chunks) {
            int x = chunk.x() << 4;
            int z = chunk.z() << 4;
            Path2D path = new Path2D.Double();
            path.moveTo(x, z);
            path.lineTo(x, z + 16);
            path.lineTo(x + 16, z + 16);
            path.lineTo(x + 16, z);
            path.closePath();
            area.add(new Area(path));
        }
        return Marker.polygon(key, toLines(key, area));
    }

    private static @NotNull List<Polyline> toLines(@NotNull String key, @NotNull Shape shape) {
        List<Polyline> lines = new ArrayList<>();
        Polyline line = new Polyline(key, Point.ZERO);
        double[] coords = new double[6];
        PathIterator iter = shape.getPathIterator(null, 1);
        while (!iter.isDone()) {
            switch (iter.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO -> line = new Polyline(key, Point.of(coords[0], coords[1]));
                case PathIterator.SEG_LINETO -> line.addPoint(Point.of(coords[0], coords[1]));
                case PathIterator.SEG_CLOSE -> lines.add(line);
            }
            iter.next();
        }
        return lines;
    }
}
