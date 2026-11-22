package net.racquo.raccoonsco;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.racquo.raccoonsco.entity.ModEntities;
import net.racquo.raccoonsco.entity.client.raccoon.RaccoonModel;
import net.racquo.raccoonsco.entity.client.raccoon.RaccoonRenderer;

public class RaccoonsCoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModelLayerRegistry.registerModelLayer(RaccoonModel.RACCOON, RaccoonModel::getTexturedModelData);
        EntityRenderers.register(ModEntities.RACCOON, RaccoonRenderer::new);


    }
}
