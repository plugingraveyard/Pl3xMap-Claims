package net.pl3x.map.claims.hook.claimchunk;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;

// https://stackoverflow.com/a/56326866

public class RectangleMerge {
    public static Polygon getPoly(String key, List<CCClaim> claims) {
        Area area = new Area();
        for (CCClaim claim : claims) {
            int x = claim.x() << 4;
            int z = claim.z() << 4;
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

    private static List<Polyline> toLines(String key, Shape shape) {
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
