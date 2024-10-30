package safro.apocalypse.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import safro.apocalypse.entity.MeteorEntity;

@OnlyIn(Dist.CLIENT)
public class MeteorEntityRenderer extends EntityRenderer<MeteorEntity> {

    public MeteorEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(MeteorEntity meteor, float pEntityYaw, float pPartialTicks, PoseStack stack, MultiBufferSource pBuffer, int pPackedLight) {
        stack.pushPose();
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        RandomSource rand = RandomSource.create();
        stack.scale(4.0F, 4.0F, 4.0F);
        for (int i = 0; i < 4; i++) {
            stack.pushPose();
            float x = Mth.nextFloat(rand, 13.0F, 25.0F);
            float y = Mth.nextFloat(rand, 13.0F, 25.0F);
            float z = Mth.nextFloat(rand, 13.0F, 25.0F);
            stack.mulPose(Axis.XP.rotationDegrees(rand.nextBoolean() ? x : -x));
            stack.mulPose(Axis.YP.rotationDegrees(rand.nextBoolean() ? y : -y));
            stack.mulPose(Axis.ZP.rotationDegrees(rand.nextBoolean() ? z : -z));
            renderer.renderSingleBlock(meteor.getMeteorType().defaultBlockState(), stack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
        stack.popPose();
        super.render(meteor, pEntityYaw, pPartialTicks, stack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
