package net.pevori.queencats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.pevori.queencats.entity.ModEntities;
import net.pevori.queencats.entity.client.PrincessCatRenderer;
import net.pevori.queencats.entity.client.QueenCatRenderer;

public class QueenCatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.QUEEN_CAT, QueenCatRenderer::new);
        EntityRendererRegistry.register(ModEntities.PRINCESS_CAT, PrincessCatRenderer::new);
    }
    
}
