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
package net.pl3x.map.claims.hook.griefprevention;

import java.util.ArrayList;
import java.util.UUID;
import me.ryanhamshire.GriefPrevention.Claim;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.world.World;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class GriefPreventionClaim {
    private final World world;
    private final Claim claim;
    private final Point min;
    private final Point max;

    private UUID ownerId;
    private String ownerName;

    public GriefPreventionClaim(@NotNull World world, @NotNull Claim claim) {
        this.world = world;
        this.claim = claim;

        Location min = this.claim.getLesserBoundaryCorner();
        Location max = this.claim.getGreaterBoundaryCorner();
        this.min = Point.of(min.getX(), min.getZ());
        this.max = Point.of(max.getX(), max.getZ());
    }

    public @NotNull World getWorld() {
        return this.world;
    }

    public boolean isAdminClaim() {
        return this.claim.isAdminClaim();
    }

    public @NotNull Long getID() {
        return this.claim.getID();
    }

    public @NotNull String getOwnerName() {
        if (this.claim.getOwnerID() != this.ownerId) {
            this.ownerId = this.claim.getOwnerID();
            this.ownerName = this.claim.getOwnerName();
        }
        return this.ownerName;
    }

    public @NotNull Point getMin() {
        return this.min;
    }

    public @NotNull Point getMax() {
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
            @NotNull ArrayList<String> builders,
            @NotNull ArrayList<String> containers,
            @NotNull ArrayList<String> accessors,
            @NotNull ArrayList<String> managers
    ) {
        this.claim.getPermissions(builders, containers, accessors, managers);
    }
}
