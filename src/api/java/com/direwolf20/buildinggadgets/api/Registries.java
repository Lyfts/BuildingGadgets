package com.direwolf20.buildinggadgets.api;

import com.direwolf20.buildinggadgets.api.registry.IOrderedRegistry;
import com.direwolf20.buildinggadgets.api.registry.OrderedRegistryEvent;
import com.direwolf20.buildinggadgets.api.registry.TopologicalRegistryBuilder;
import com.direwolf20.buildinggadgets.api.template.building.tilesupport.ITileDataFactory;
import com.direwolf20.buildinggadgets.api.template.serialisation.ITemplateSerializer;
import com.direwolf20.buildinggadgets.api.template.serialisation.ITileDataSerializer;
import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class Registries {
    public static final ResourceLocation REGISTRY_ID_TEMPLATE_SERIALIZER = new ResourceLocation("buildinggadgets:template/serializer");
    public static final ResourceLocation REGISTRY_ID_TILE_DATA_SERIALIZER = new ResourceLocation("buildinggadgets:tile_data/serializer");
    private Registries() {}

    private static IForgeRegistry<ITemplateSerializer> templateSerializers = null;
    private static IForgeRegistry<ITileDataSerializer> tileDataSerializers = null;
    private static IOrderedRegistry<ITileDataFactory> tileDataFactories = null;

    public static IForgeRegistry<ITemplateSerializer> getTemplateSerializers() {
        Preconditions
                .checkState(templateSerializers != null, "Attempted to retrieve TemplateSerializerRegistry before registries were created!");
        return templateSerializers;
    }

    public static IForgeRegistry<ITileDataSerializer> getTileDataSerializers() {
        Preconditions
                .checkState(tileDataSerializers != null, "Attempted to retrieve TileDataSerializerRegistry before registries were created!");
        return tileDataSerializers;
    }

    public static IOrderedRegistry<ITileDataFactory> getTileDataFactories() {
        Preconditions
                .checkState(tileDataFactories != null, "Attempted to retrieve TileDataFactoryRegistry before it was created!");
        return tileDataFactories;
    }

    static void onCreateRegistries(final IEventBus forgeEventBus) {
        templateSerializers = new RegistryBuilder<ITemplateSerializer>()
                .setType(ITemplateSerializer.class)
                .setName(REGISTRY_ID_TEMPLATE_SERIALIZER)
                .create();
        tileDataSerializers = new RegistryBuilder<ITileDataSerializer>()
                .setType(ITileDataSerializer.class)
                .setName(REGISTRY_ID_TILE_DATA_SERIALIZER)
                .create();
        DeferredWorkQueue.runLater(() -> {
            TopologicalRegistryBuilder<ITileDataFactory> builder = TopologicalRegistryBuilder.create();
            forgeEventBus.post(new OrderedRegistryEvent<>(ITileDataFactory.class, builder));
            tileDataFactories = builder.build();
        });
    }
}
