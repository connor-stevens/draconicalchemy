package com.cleanroommc.draconicalchemy.mixin;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class DraconicAlchemyMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        List<String> list = new ArrayList<String>();
        list.add("mixins.draconicalchemy.json");
        return list;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return true;
    }

    @Override
    public void onMixinConfigQueued(String mixinConfig) {
    }
}
