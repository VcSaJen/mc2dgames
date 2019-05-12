package com.vcsajen.mc2dgames;

import com.vcsajen.mc2dgames.nms.NmsBridge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.storage.MapDecoration;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.statistic.ChangeStatisticEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Plugin(id = "mc2dgames", name = "Mc2DGames",
        description = "Touchscreen 2D games in Minecraft! 1000 in 1*! Also scriptable!", authors = {"VcSaJen"})
public class Mc2DGames {
    @Inject
    private Logger logger;

    /*@Listener
    @Exclude({UnloadChunkEvent.class, LoadChunkEvent.class, ChangeStatisticEvent.class,
            CollideEntityEvent.class, SaveWorldEvent.class})
    public void onEvent(Event event) {
        if (event.getClass().getName().equals("org.spongepowered.api.event.MoveEntityEvent$Impl")) return;
        logger.info(event.getClass().getName() + ": " + event.toString());
    }*/

    @Listener
    public void onInit(GamePreInitializationEvent event) {
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Text.of("Hello World Command"))
                .permission("myplugin.command.helloworld")
                .executor(this::cmdTest)
                .build();

        Sponge.getCommandManager().register(this, myCommandSpec, "helloworld", "hello", "hi", "test");
    }

    private double x,y,dx,dy;
    private Task taskRenderMap;

    double flip1D(double val, double axis) {
        return 2*axis-val;
    }

    private CommandResult cmdTest(CommandSource src, CommandContext cmdContext) {
        if (!(src instanceof Player)) return CommandResult.successCount(0);
        Player player = (Player)src;
        if (taskRenderMap==null) {
            player.sendMessage(Text.of("Screen enabled"));
            /*World world = player.getWorld();
            AABB cuboid = new AABB(-32, 32, -32, 32, 48, 32);
            Sponge.getServer().getOnlinePlayers().stream()
                    .filter(plr -> plr.getWorld().equals(world) && cuboid.contains(plr.getPosition()))
                    .forEach(plr -> {});*/
            double size = 16;
            double speed = 8;
            double angle = ThreadLocalRandom.current().nextDouble(0, 2*Math.PI);

            dx = Math.sin(angle)*speed;
            dy = Math.cos(angle)*speed;

            taskRenderMap = Task.builder()
                    .intervalTicks(1)
                    .name("ExamplePlugin - Fetch Stats from Database")
                    .execute(() -> {
                        //SPacketChangeGameState packet = new SPacketChangeGameState((byte)5, 0.0f);
                        x+=dx;
                        y+=dy;
                        if (x<0) {
                            x*=-1;
                            dx*=-1;
                        }
                        if (y<0) {
                            y*=-1;
                            dy*=-1;
                        }
                        if (x+size>128) {
                            x=flip1D(x+size, 128)-size;
                            dx*=-1;
                        }
                        if (y+size>128) {
                            y=flip1D(y+size, 128)-size;
                            dy*=-1;
                        }

                        byte[] colors = new byte[128*128];
                        for (int i=0;i<128*128;i++) colors[i]=(byte)22;
                        int jstart = (int)Math.round(y);
                        int istart = (int)Math.round(x);
                        int jend = (int)Math.round(y+size);
                        int iend = (int)Math.round(x+size);
                        for (int j=jstart;j<jend;j++)
                            for (int i=istart;i<iend;i++) {
                                colors[j*128+i]=(byte)34;
                            }

                        Sponge.getServer().getOnlinePlayers()
                                .forEach(plr -> NmsBridge.sendMapToPlayer(plr, colors));
                    })
                    .submit(this);

        } else {
            player.sendMessage(Text.of("Screen disabled"));
            taskRenderMap.cancel();
            taskRenderMap = null;
        }
        return CommandResult.success();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("+++++++++++++++++++++++++++++++++");
        logger.info("Successfully running Mc2DGames!!!");
        logger.info("+++++++++++++++++++++++++++++++++");
        x=64;
        y=64;
        dx=1;
        dy=0;
    }
}
