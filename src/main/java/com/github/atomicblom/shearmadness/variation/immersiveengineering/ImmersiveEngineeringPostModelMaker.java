package com.github.atomicblom.shearmadness.variation.immersiveengineering;

import com.github.atomicblom.shearmadness.api.rendering.PartDefinition;
import com.github.atomicblom.shearmadness.api.modelmaker.DefaultModelMaker;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ImmersiveEngineeringPostModelMaker extends DefaultModelMaker
{

    private final float nintyDegrees = (float) Math.toRadians(90);
    private final float fifteenDegrees = (float) Math.toRadians(15);
    private final float thirtyDegrees =(float) Math.toRadians(30);

    @Override
    public ModelQuadruped createModel(ItemStack itemStack, EntityLivingBase entity)
    {
        ModelQuadruped quadruped = super.createModel(itemStack, entity);

        quadruped.body = new ModelRenderer(new ModelSheep1(), 0, 0);
        quadruped.head = new ModelRenderer(new ModelSheep1(), 0, 0);

        Matrix4f antenna1Matrix = new Matrix4f();

        antenna1Matrix.translate(new Vector3f(1, -3, -9));
        antenna1Matrix.scale(new Vector3f(20, -20, 20));

        antenna1Matrix.rotate(fifteenDegrees, new Vector3f(1, 0, 0));
        antenna1Matrix.rotate(-thirtyDegrees, new Vector3f(0, 0, 1));
        antenna1Matrix.rotate(-nintyDegrees, new Vector3f(0, 1, 0));
        antenna1Matrix.translate(new Vector3f(-0.5f, -0.5f, -0.5f));

        quadruped.head.addChild(getChiselBodyModelRenderer(itemStack, entity, new PartDefinition(
                new Vector3f(0, 0, 0),
                antenna1Matrix,
                new Matrix3f()
        )));

        antenna1Matrix = new Matrix4f();

        antenna1Matrix.translate(new Vector3f(-1, -3, -9));
        antenna1Matrix.scale(new Vector3f(20, -20, 20));
        antenna1Matrix.rotate(fifteenDegrees, new Vector3f(1, 0, 0));
        antenna1Matrix.rotate(thirtyDegrees, new Vector3f(0, 0, 1));
        antenna1Matrix.rotate(nintyDegrees, new Vector3f(0, 1, 0));
        antenna1Matrix.translate(new Vector3f(-0.5f, -0.5f, -0.5f));

        quadruped.head.addChild(getChiselBodyModelRenderer(itemStack, entity, new PartDefinition(
                new Vector3f(0, 0, 0),
                antenna1Matrix,
                new Matrix3f()
        )));

        return quadruped;
    }
}
