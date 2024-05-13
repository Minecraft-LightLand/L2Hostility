package dev.xkmc.l2hostility.content.item.beacon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.NameSetable;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@SerialClass
public class HostilityBeaconBlockEntity extends BaseBlockEntity
        implements TickableBlockEntity, MenuProvider, NameSetable, ContainerData {

   private static final int MAX_LEVELS = 3;
   public static final MobEffect[][] BEACON_EFFECTS = {
           {MobEffects.WEAKNESS, LCEffects.ICE.get()},
           {LCEffects.FLAME.get(), LCEffects.CURSE.get()},
           {LCEffects.CLEANSE.get(), LCEffects.STONE_CAGE.get()}};

   private static final int BLOCKS_CHECK_PER_TICK = 10;
   private static final Component DEFAULT_NAME = Component.translatable("container.beacon");

   List<Section> beamSections = Lists.newArrayList();

   private List<Section> checkingBeamSections = Lists.newArrayList();

   int levels;

   private int lastCheckY;

   @SerialClass.SerialField
   int power = -1;

   @Nullable
   private Component name;

   private LockCode lockKey = LockCode.NO_LOCK;

   public HostilityBeaconBlockEntity(BlockEntityType<? extends HostilityBeaconBlockEntity> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   @Override
   public void tick() {
      if (level == null) return;
      var pos = getBlockPos();
      int x = pos.getX();
      int y = pos.getY();
      int z = pos.getZ();
      BlockPos start;
      if (lastCheckY < y) {
         start = pos;
         checkingBeamSections = Lists.newArrayList();
         lastCheckY = y - 1;
      } else {
         start = new BlockPos(x, lastCheckY + 1, z);
      }

      Section sec = checkingBeamSections.isEmpty() ? null : checkingBeamSections.get(checkingBeamSections.size() - 1);
      int h = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

      for (int i = 0; i < BLOCKS_CHECK_PER_TICK && start.getY() <= h; ++i) {
         BlockState state = level.getBlockState(start);
         float[] afloat = state.getBeaconColorMultiplier(level, start, pos);
         if (afloat != null) {
            if (checkingBeamSections.size() <= 1) {
               sec = new Section(afloat);
               checkingBeamSections.add(sec);
            } else if (sec != null) {
               if (Arrays.equals(afloat, sec.color)) {
                  sec.increaseHeight();
               } else {
                  sec = new Section(new float[]{
                          (sec.color[0] + afloat[0]) / 2.0F,
                          (sec.color[1] + afloat[1]) / 2.0F,
                          (sec.color[2] + afloat[2]) / 2.0F}
                  );
                  checkingBeamSections.add(sec);
               }
            }
         } else {
            if (sec == null || state.getLightBlock(level, start) >= 15 && !state.is(Blocks.BEDROCK)) {
               checkingBeamSections.clear();
               lastCheckY = h;
               break;
            }
            sec.increaseHeight();
         }

         start = start.above();
         ++lastCheckY;
      }
      int oldLv = levels;
      if (level.getGameTime() % 80L == 0L) {
         if (!beamSections.isEmpty()) {
            levels = updateBase(level, x, y, z);
         }
         if (levels > 0 && !beamSections.isEmpty()) {
            applyEffects(level, pos);
            playSound(level, pos, SoundEvents.BEACON_AMBIENT);
         }
      }
      if (lastCheckY >= h) {
         lastCheckY = level.getMinBuildHeight() - 1;
         boolean flag = oldLv > 0;
         beamSections = checkingBeamSections;
         if (!level.isClientSide) {
            boolean flag1 = levels > 0;
            if (!flag && flag1) {
               playSound(level, pos, SoundEvents.BEACON_ACTIVATE);
               var aabb = new AABB(x, y, z, x, y - 4, z).inflate(10.0D, 5.0D, 10.0D);
               for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, aabb)) {
                  CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverplayer, levels);
               }
            } else if (flag && !flag1) {
               playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
            }
         }
      }

   }

   private static int updateBase(Level level, int x, int y, int z) {
      int ans = 0;
      for (int iy = 1; iy <= MAX_LEVELS; ans = iy++) {
         int y0 = y - iy;
         if (y0 < level.getMinBuildHeight()) {
            break;
         }
         boolean valid = true;
         for (int ix = x - iy; ix <= x + iy && valid; ++ix) {
            for (int iz = z - iy; iz <= z + iy; ++iz) {
               if (!level.getBlockState(new BlockPos(ix, y0, iz)).is(LHTagGen.BEACON_BLOCK)) {
                  valid = false;
                  break;
               }
            }
         }
         if (!valid) {
            break;
         }
      }

      return ans;
   }

   public void setRemoved() {
      if (level != null) {
         playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
      }
      super.setRemoved();
   }

   private void applyEffects(Level level, BlockPos pos) {
      if (!level.isClientSide && power >= 0) {
         var eff = BEACON_EFFECTS[power / 2][power % 2];
         double d0 = (levels * 10 + 10);
         int i = 0;
         int j = (9 + levels * 2) * 20;
         AABB aabb = new AABB(pos).inflate(d0).expandTowards(0.0D, level.getHeight(), 0.0D);
         var list = level.getEntitiesOfClass(LivingEntity.class, aabb);
         for (var e : list) {
            e.addEffect(new MobEffectInstance(eff, j, i, true, true));
         }

      }
   }

   public static void playSound(Level level, BlockPos pos, SoundEvent sound) {
      level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
   }

   public List<Section> getBeamSections() {
      return this.levels == 0 ? ImmutableList.of() : this.beamSections;
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      if (tag.contains("CustomName", 8)) {
         this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
      }
      this.lockKey = LockCode.fromTag(tag);
   }

   public void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putInt("Levels", this.levels);
      if (this.name != null) {
         tag.putString("CustomName", Component.Serializer.toJson(this.name));
      }
      this.lockKey.addToTag(tag);
   }

   public void setCustomName(@Nullable Component name) {
      this.name = name;
   }

   @Nullable
   public Component getCustomName() {
      return this.name;
   }

   @Nullable
   public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
      return level != null && BaseContainerBlockEntity.canUnlock(player, lockKey, getDisplayName()) ?
              new HostilityBeaconMenu(LHBlocks.MT_BEACON.get(), id, inv, this,
                      ContainerLevelAccess.create(level, getBlockPos())) : null;
   }

   public Component getDisplayName() {
      return this.getName();
   }

   public Component getName() {
      return this.name != null ? this.name : DEFAULT_NAME;
   }

   public void setLevel(Level level) {
      super.setLevel(level);
      this.lastCheckY = level.getMinBuildHeight() - 1;
   }

   // container data

   public int get(int index) {
      return switch (index) {
         case 0 -> levels;
         case 1 -> power;
         default -> 0;
      };
   }

   public void set(int index, int value) {
      if (level == null) return;
      switch (index) {
         case 0:
            levels = value;
            break;
         case 1:
            if (!level.isClientSide && !beamSections.isEmpty()) {
               HostilityBeaconBlockEntity.playSound(level, worldPosition, SoundEvents.BEACON_POWER_SELECT);
            }
            power = value;
            break;
      }

   }

   public int getCount() {
      return 2;
   }

   public static class Section {
      final float[] color;
      private int height;

      public Section(float[] color) {
         this.color = color;
         this.height = 1;
      }

      protected void increaseHeight() {
         ++this.height;
      }

      public float[] getColor() {
         return this.color;
      }

      public int getHeight() {
         return this.height;
      }

   }

}
