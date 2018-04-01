package org.lawlsec.egg.egg;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@Mod(modid = egg.MOD_ID, name = egg.MOD_NAME, version = egg.VERSION)
@Mod.EventBusSubscriber
public class egg {

    public static final String MOD_ID = "egg";
    public static final String MOD_NAME = "egg";
    public static final String VERSION = "1.0";

    public static final int RADIUS = 1024;

    @Mod.Instance(MOD_ID)
    public static egg INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        List<Entity> entities = event.getPlayer().world.getEntitiesWithinAABB(
            Entity.class,
            new AxisAlignedBB(
                event.getPlayer().getPosition().getX() - RADIUS,
                event.getPlayer().getPosition().getY() - RADIUS,
                event.getPlayer().getPosition().getZ() - RADIUS,
                event.getPlayer().getPosition().getX() + RADIUS,
                event.getPlayer().getPosition().getY() + RADIUS,
                event.getPlayer().getPosition().getZ() + RADIUS
            )
        );

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(
            -event.getPlayer().getPosition().getX(),
            -event.getPlayer().getPosition().getY(),
            -event.getPlayer().getPosition().getZ()
        );

        Color c = new Color(255, 0, 0, 150);
        GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        GL11.glLineWidth(3);
        GL11.glDepthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        entities.forEach(entity -> {
            if (entity.getName().equals("item.item.egg")) {
                bufferBuilder.pos(
                    event.getPlayer().getPosition().getX(),
                    event.getPlayer().getPosition().getY(),
                    event.getPlayer().getPosition().getZ()

                ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

                bufferBuilder.pos(entity.posX, entity.posY, entity.posZ).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
            }
        });

        tessellator.draw();

        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }
}
