package com.github.atomicblom.shearmadness.variations.chancecubes.client;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.particles.CustomParticleFactoryBase;
import com.github.atomicblom.shearmadness.api.particles.ICustomParticleFactory;
import com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.atomicblom.shearmadness.api.Capability.CHISELED_SHEEP;

@SideOnly(Side.CLIENT)
public class SheepHeadParticle extends Particle
{
    private EntityLivingBase entity;

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
        this.particleMaxAge = 30;
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    public int getFXLayer()
    {
        return 3;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.entity == null)
        {
            this.entity = new EntitySheep(this.worldObj);
            if (entity.hasCapability(CHISELED_SHEEP, null)) {
                IChiseledSheepCapability capability = entity.getCapability(CHISELED_SHEEP, null);
                capability.chisel(new ItemStack(ChanceCubesLibrary.chance_cube, 1));
            }
        }
    }

    /**
     * Renders the particle
     */
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        if (this.entity != null)
        {
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);

            float particleAge = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float lightLevel = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightLevel, lightLevel);
            GlStateManager.pushMatrix();
            float alpha = 0.05F + 0.5F * MathHelper.sin(particleAge * (float)Math.PI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.translate(0.0F, 1.8F, 0.0F);
            GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);
            float speed = 150.0F;
            GlStateManager.rotate(260 - (60.0F - speed * particleAge - entityIn.rotationPitch), 1.0F, 0.0F, 0.0F);
            float scale = 1.2F;
            GlStateManager.translate(0.0F, -0.4F, -2.2);
            GlStateManager.rotate(30.0f, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(scale, scale, scale);

            this.entity.rotationYaw = 0.0F;
            this.entity.rotationYawHead = 0.0F;
            this.entity.prevRotationYaw = 0.0F;
            this.entity.prevRotationYawHead = 0.0F;
            rendermanager.doRenderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }

    public static class Factory extends CustomParticleFactoryBase
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Particle createCustomParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new SheepHeadParticle(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}