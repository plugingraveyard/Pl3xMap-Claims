package net.pl3x.map.claims.hook.griefprevention;

import java.util.ArrayList;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import me.ryanhamshire.GriefPrevention.Claim;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.world.World;
import org.bukkit.Location;

public class GPClaim {
    private final World world;
    private final Claim claim;
    private final Point min;
    private final Point max;

    public GPClaim(@NonNull World world, @NonNull Claim claim) {
        this.world = world;
        this.claim = claim;

        Location min = this.claim.getLesserBoundaryCorner();
        Location max = this.claim.getGreaterBoundaryCorner();
        this.min = Point.of(min.getX(), min.getZ());
        this.max = Point.of(max.getX(), max.getZ());
    }

    public @NonNull World getWorld() {
        return this.world;
    }

    public boolean isAdminClaim() {
        return this.claim.isAdminClaim();
    }

    public @NonNull Long getID() {
        return this.claim.getID();
    }

    public @NonNull String getOwnerName() {
        return this.claim.getOwnerName();
    }

    public @NonNull Point getMin() {
        return this.min;
    }

    public @NonNull Point getMax() {
        return this.max;
    }

    public int getArea() {
        return this.claim.getArea();
    }

    public int getWidth() {
        return this.claim.getWidth();
    }

    public int getHeight() {
        return this.claim.getHeight();
    }

    public void getPermissions(
            @NonNull ArrayList<@NonNull String> builders,
            @NonNull ArrayList<@NonNull String> containers,
            @NonNull ArrayList<@NonNull String> accessors,
            @NonNull ArrayList<@NonNull String> managers
    ) {
        this.claim.getPermissions(builders, containers, accessors, managers);
    }
}
