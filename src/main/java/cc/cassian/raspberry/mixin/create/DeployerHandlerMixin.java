package cc.cassian.raspberry.mixin.create;

import cc.cassian.raspberry.ModCompat;
import cc.cassian.raspberry.RaspberryMod;
import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(DeployerHandler.class)
public class DeployerHandlerMixin {
    private static final ResourceLocation BEE_DISC_ID = new ResourceLocation("windswept", "music_disc_bumblebee");

    // > smallest inject annotation
    @Inject(at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/player/Inventory;placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;)V",
                     shift = At.Shift.AFTER),
            method = "safeOnBeehiveUse(Lnet/minecraft/world/level/block/state/BlockState;" +
                                      "Lnet/minecraft/world/level/Level;" +
                                      "Lnet/minecraft/core/BlockPos;" +
                                      "Lnet/minecraft/world/entity/player/Player;" +
                                      "Lnet/minecraft/world/InteractionHand;" +
                                      ")" + "Lnet/minecraft/world/InteractionResult;")
    private static void addBeeDiscDropOnDeployerShearingHive(BlockState state, Level world, BlockPos pos, Player player,
                                                               InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci) {
        if (!ModCompat.WINDSWEPT) return;

        Random random = new Random();
        int roll = random.nextInt(200); // Amounts to 0.5% (1/200) chance, same as the KubeJS chance
        if (roll != 0) return;

        Item discItem = ForgeRegistries.ITEMS.getValue(BEE_DISC_ID);
        if (discItem == null) {
            RaspberryMod.LOGGER.error("Failed to find music disc item {}!", BEE_DISC_ID);
            return;
        }

        player.getInventory().placeItemBackInInventory(new ItemStack(discItem, 1));
    }
}
