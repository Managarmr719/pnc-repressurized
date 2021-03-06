package me.desht.pneumaticcraft.client.render.pneumaticArmor.hacking;

import me.desht.pneumaticcraft.api.hacking.IHacking;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityHackingProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(IHacking.class)
    public static final Capability<IHacking> HACKING_CAPABILITY = null;

    private final IHacking instance = HACKING_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == HACKING_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == HACKING_CAPABILITY ? HACKING_CAPABILITY.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return HACKING_CAPABILITY.getStorage().writeNBT(HACKING_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        HACKING_CAPABILITY.getStorage().readNBT(HACKING_CAPABILITY, this.instance, null, nbt);
    }
}
