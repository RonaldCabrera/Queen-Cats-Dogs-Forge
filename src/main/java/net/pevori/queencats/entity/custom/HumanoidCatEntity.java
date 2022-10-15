package net.pevori.queencats.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class HumanoidCatEntity extends TameableEntity{
    public static final String okayuSan = "okayu";
    
    protected HumanoidCatEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2) {
        return null;
    }
    
    public boolean isMogu(){
        String s = Formatting.strip(this.getName().getString());
        return (s != null && s.toLowerCase().contains(okayuSan));
    }
}
