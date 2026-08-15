package com.mkcorp.mixin;

import com.mkcorp.module.modules.Xray;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Xray hook: forces every non-whitelisted block to report an INVISIBLE
 * render type, so the chunk builder simply skips it.
 */
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void mkcorp$xrayHide(CallbackInfoReturnable<BlockRenderType> cir) {
        AbstractBlock.AbstractBlockState self = (AbstractBlock.AbstractBlockState) (Object) this;
        if (Xray.shouldHide(self)) {
            cir.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }
}
