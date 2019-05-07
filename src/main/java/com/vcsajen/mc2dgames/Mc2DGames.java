package com.vcsajen.mc2dgames;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.event.statistic.ChangeStatisticEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import com.google.inject.Inject;
import org.slf4j.Logger;

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
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Successfully running Mc2DGames!!!");
        logger.info("Successfully running Mc2DGames!!!");
        logger.info("Successfully running Mc2DGames!!!");
    }
}
