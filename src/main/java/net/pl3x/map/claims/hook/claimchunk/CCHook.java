package net.pl3x.map.claims.hook.claimchunk;

import com.cjburkey.claimchunk.ClaimChunk;
import com.cjburkey.claimchunk.chunk.DataChunk;
import com.cjburkey.claimchunk.data.newdata.IClaimChunkDataHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import libs.org.checkerframework.checker.nullness.qual.NonNull;
import net.pl3x.map.claims.hook.Hook;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Rectangle;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.util.Colors;
import net.pl3x.map.core.world.World;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

public class CCHook implements Listener, Hook {
    public CCHook() {
        CCConfig.reload();
    }

    @Override
    public void registerWorld(@NonNull World world) {
        world.getLayerRegistry().register(new CCLayer(this, world));
    }

    @Override
    public @NonNull Collection<@NonNull Marker<@NonNull ?>> getClaims(@NonNull World world) {
        @SuppressWarnings("deprecation")
        ClaimChunk cc = ClaimChunk.getInstance();

        IClaimChunkDataHandler dataHandler;
        try {
            Field field = ClaimChunk.class.getDeclaredField("dataHandler");
            field.setAccessible(true);
            dataHandler = (IClaimChunkDataHandler) field.get(cc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        DataChunk[] chunkArr = dataHandler.getClaimedChunks();
        if (chunkArr == null) {
            return EMPTY_LIST;
        }

        List<DataChunk> chunks = Arrays.stream(chunkArr)
                .filter(claim -> claim.chunk.getWorld().equals(world.getName()))
                .toList();

        if (CCConfig.SHOW_CHUNKS) {
            return chunks.stream().map(claim -> {
                int minX = claim.chunk.getX() << 4;
                int maxX = (claim.chunk.getX() + 1) << 4;
                int minZ = claim.chunk.getZ() << 4;
                int maxZ = (claim.chunk.getZ() + 1) << 4;
                String key = String.format("cc_%s_chunk_%d_%d", world.getName(), minX, minZ);
                Rectangle rect = Marker.rectangle(key, Point.of(minX, minZ), Point.of(maxX, maxZ));
                return rect.setOptions(options(world, claim.player));
            }).collect(Collectors.toList());
        }

        List<CCClaim> claims = chunks.stream().map(chunk ->
                new CCClaim(chunk.chunk.getX(), chunk.chunk.getZ(), chunk.player)
        ).collect(Collectors.toList());
        List<CCGroup> groups = groupClaims(claims);
        Collection<Marker<?>> markers = new ArrayList<>();
        for (CCGroup group : groups) {
            String key = String.format("cc_%s_chunk_%s", world.getName(), group.id());
            markers.add(RectangleMerge.getPoly(key, group.claims())
                    .setOptions(options(world, group.owner())));
        }
        return markers;
    }

    private Options.Builder options(World world, UUID owner) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
        String ownerName = player.getName() == null ? "unknown" : player.getName();
        return Options.builder()
                .strokeColor(Colors.fromHex(CCConfig.MARKER_STROKE_COLOR))
                .strokeWeight(CCConfig.MARKER_STROKE_WEIGHT)
                .fillColor(Colors.fromHex(CCConfig.MARKER_FILL_COLOR))
                .fillType(Fill.Type.NONZERO)
                .popupContent(CCConfig.MARKER_POPUP
                        .replace("<world>", world.getName())
                        .replace("<owner>", ownerName)
                );
    }

    private List<CCGroup> groupClaims(List<CCClaim> claims) {
        // break groups down by owner
        Map<UUID, List<CCClaim>> byOwner = new HashMap<>();
        for (CCClaim claim : claims) {
            List<CCClaim> list = byOwner.getOrDefault(claim.owner(), new ArrayList<>());
            list.add(claim);
            byOwner.put(claim.owner(), list);
        }

        // combine touching claims
        Map<UUID, List<CCGroup>> groups = new HashMap<>();
        for (Map.Entry<UUID, List<CCClaim>> entry : byOwner.entrySet()) {
            UUID owner = entry.getKey();
            List<CCClaim> list = entry.getValue();
            next1:
            for (CCClaim claim : list) {
                List<CCGroup> groupList = groups.getOrDefault(owner, new ArrayList<>());
                for (CCGroup group : groupList) {
                    if (group.isTouching(claim)) {
                        group.add(claim);
                        continue next1;
                    }
                }
                groupList.add(new CCGroup(claim, owner));
                groups.put(owner, groupList);
            }
        }

        // combined touching groups
        List<CCGroup> combined = new ArrayList<>();
        for (List<CCGroup> list : groups.values()) {
            next:
            for (CCGroup group : list) {
                for (CCGroup toChk : combined) {
                    if (toChk.isTouching(group)) {
                        toChk.add(group);
                        continue next;
                    }
                }
                combined.add(group);
            }
        }

        return combined;
    }
}
