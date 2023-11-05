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
package com.ryderbelserion.map.claims.hook.claimchunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ClaimChunkGroup {

    private final List<ClaimChunkClaim> claims = new ArrayList<>();
    private final UUID owner;

    public ClaimChunkGroup(@NotNull ClaimChunkClaim claim, @NotNull UUID owner) {
        add(claim);
        this.owner = owner;
    }

    public boolean isTouching(@NotNull ClaimChunkClaim claim) {
        for (ClaimChunkClaim toChk : claims) {
            if (toChk.isTouching(claim)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouching(@NotNull ClaimChunkGroup group) {
        for (ClaimChunkClaim claim : group.claims()) {
            if (isTouching(claim)) {
                return true;
            }
        }
        return false;
    }

    public void add(@NotNull ClaimChunkClaim claim) {
        claims.add(claim);
    }

    public void add(@NotNull ClaimChunkGroup group) {
        claims.addAll(group.claims());
    }

    public @NotNull List<ClaimChunkClaim> claims() {
        return claims;
    }

    public @NotNull UUID owner() {
        return owner;
    }

    public @NotNull String id() {
        if (!claims.isEmpty()) {
            ClaimChunkClaim claim = claims.get(0);
            return claim.minX() + "_" + claim.minZ();
        } else {
            return "NaN_NaN";
        }
    }
}