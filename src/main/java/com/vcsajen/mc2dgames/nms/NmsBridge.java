package com.vcsajen.mc2dgames.nms;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.storage.MapDecoration;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;

public class NmsBridge {
    public static void sendMapToPlayer(Player player, byte[] colors) { //TODO: add partial update
        //SPacketMaps(int id, byte scale, boolean trackingPosition, Collection<MapDecoration> mapDecorations, byte[] colors, int minX, int minY, int width, int height)
        SPacketMaps packet = new SPacketMaps(1, (byte)0, false, new ArrayList<MapDecoration>(), colors, 0, 0, 128, 128);
        ((NetHandlerPlayServer)player.getConnection()).sendPacket(packet);
    }

    //sendMapToPlayers? Collection<Player> players
}
