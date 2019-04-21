package com.direwolf20.buildinggadgets.api;

import com.direwolf20.buildinggadgets.api.abstraction.IApiConfig;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum APIProxy {
    INSTANCE;
    public static final Logger LOG = LogManager.getLogger();
    private IEventBus modEventbus = null;
    private IEventBus forgeEventbus = null;
    private IApiConfig config = null;

    public APIProxy onCreate(IEventBus modEventbus, IEventBus forgeEventbus, IApiConfig config) {
        Preconditions.checkState(this.modEventbus == null && this.forgeEventbus == null && this.config == null);
        return createState(modEventbus,forgeEventbus,config);
    }

    @VisibleForTesting
    APIProxy createState(IEventBus modEventbus, IEventBus forgeEventbus, IApiConfig config) {
        this.modEventbus = modEventbus;
        this.forgeEventbus = forgeEventbus;
        this.config = config;
        modEventbus.addListener(this::registerRegistries);
        return this;
    }

    public void onSetup() {

    }

    public void onLoadComplete() {

    }

    private void registerRegistries(RegistryEvent.NewRegistry newRegistry) {
        Registries.onCreateRegistries(forgeEventbus);
    }

    public IApiConfig getConfig() {
        return config;
    }
}
