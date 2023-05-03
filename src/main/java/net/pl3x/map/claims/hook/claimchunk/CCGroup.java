package net.pl3x.map.claims.hook.claimchunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CCGroup {
    private final List<CCClaim> claims = new ArrayList<>();
    private final UUID owner;

    public CCGroup(CCClaim claim, UUID owner) {
        add(claim);
        this.owner = owner;
    }

    public boolean isTouching(CCClaim claim) {
        for (CCClaim toChk : claims) {
            if (toChk.isTouching(claim)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouching(CCGroup group) {
        for (CCClaim claim : group.claims()) {
            if (isTouching(claim)) {
                return true;
            }
        }
        return false;
    }

    public void add(CCClaim claim) {
        claims.add(claim);
    }

    public void add(CCGroup group) {
        claims.addAll(group.claims());
    }

    public List<CCClaim> claims() {
        return claims;
    }

    public UUID owner() {
        return owner;
    }

    public String id() {
        if (claims.size() > 0) {
            CCClaim claim = claims.get(0);
            return claim.x() + "_" + claim.z();
        } else {
            return "NaN_NaN";
        }
    }
}
