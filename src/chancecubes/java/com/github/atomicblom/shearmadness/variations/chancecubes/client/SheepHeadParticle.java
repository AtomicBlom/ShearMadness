package com.github.atomicblom.shearmadness.variations.chancecubes.client;

import com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesLibrary;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static com.github.atomicblom.shearmadness.api.Capability.CHISELED_SHEEP;

@OnlyIn(Dist.CLIENT)
public class SheepHeadParticle extends Particle
{
    private LivingEntity entity;

    protected SheepHeadParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.maxAge = 30;
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.entity == null)
        {
            this.entity = new SheepEntity(EntityType.SHEEP, this.world);
            entity.getCapability(CHISELED_SHEEP).ifPresent(capability -> {
                capability.chisel(new ItemStack(ChanceCubesLibrary.chance_cube, 1));
            });
        }
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        if (this.entity != null)
        {
            EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
            rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);

            float particleAge = ((float)this.age + partialTicks) / (float)this.maxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepthTest();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float lightLevel = 240.0F;

            //FIXME: Is this too dark?
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightLevel, lightLevel);
            GlStateManager.pushMatrix();
            float alpha = 0.05F + 0.5F * MathHelper.sin(particleAge * (float)Math.PI);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.translatef(0.0F, 1.8F, 0.0F);
            GlStateManager.rotatef(180.0F - entityIn.getYaw(), 0.0F, 1.0F, 0.0F);
            float speed = 150.0F;
            GlStateManager.rotatef(260 - (60.0F - speed * particleAge - entityIn.getPitch()), 1.0F, 0.0F, 0.0F);
            float scale = 1.2F;
            GlStateManager.translatef(0.0F, -0.4F, -2.2f);
            GlStateManager.rotatef(30.0f, 1.0F, 0.0F, 0.0F);
            GlStateManager.scalef(scale, scale, scale);

            this.entity.rotationYaw = 0.0F;
            this.entity.rotationYawHead = 0.0F;
            this.entity.prevRotationYaw = 0.0F;
            this.entity.prevRotationYawHead = 0.0F;
            rendermanager.renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
            GlStateManager.popMatrix();
            GlStateManager.enableDepthTest();
        }
    }

    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SheepHeadParticle(worldIn, x, y, z);
        }
    }
}