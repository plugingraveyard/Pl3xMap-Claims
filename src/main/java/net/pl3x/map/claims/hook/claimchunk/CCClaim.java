package net.pl3x.map.claims.hook.claimchunk;

import java.util.UUID;

public record CCClaim(int x, int z, UUID owner) {
    public boolean isTouching(CCClaim other) {
        return owner().equals(other.owner()) && ( // same owner
                (other.x() == x() && other.z() == z() - 1) || // touches north
                (other.x() == x() && other.z() == z() + 1) || // touches south
                (other.x() == x() - 1 && other.z() == z()) || // touches west
                (other.x() == x() + 1 && other.z() == z()) // touches east
        );
    }
}
