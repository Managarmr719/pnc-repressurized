package me.desht.pneumaticcraft.client.semiblock;

import java.util.HashMap;
import java.util.Map;

import me.desht.pneumaticcraft.common.semiblock.SemiBlockTransferGadget;
import me.desht.pneumaticcraft.common.semiblock.SemiBlockTransferGadget.EnumInputOutput;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class SemiBlockRendererTransferGadget implements ISemiBlockRenderer<SemiBlockTransferGadget> {
    private final Map<AxisAlignedBB, Integer> models = new HashMap<>();

    @Override
    public void render(SemiBlockTransferGadget semiBlock, float partialTick) {
        IBlockState state = semiBlock.getBlockState();
        if(state.getBlock().isAir(state, semiBlock.getWorld(), semiBlock.getPos())) return;
        
        GL11.glPushMatrix();
        //Minecraft.getMinecraft().renderEngine.bindTexture(Textures.MODEL_HEAT_FRAME);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlendProfile(Profile.TRANSPARENT_MODEL);
        
        EnumInputOutput io = semiBlock.getInputOutput();
        if(io == EnumInputOutput.INPUT){
            GL11.glColor4d(0, 0, 1, 0.5);
        }else{
            GL11.glColor4d(1, 0.3, 0, 0.5);
        }
        
        double indent = 1/16D;
        double outdent = 1/32D;
        double antiZFight = 0.001D;

        AxisAlignedBB bAABB;
        if (semiBlock.getWorld() != null) {
            bAABB = state.getBoundingBox(semiBlock.getWorld(), semiBlock.getPos());
        } else {
            bAABB = new AxisAlignedBB(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D);
        }
        
        AxisAlignedBB aabb;
        if(semiBlock.getFacing() != null){
            switch(semiBlock.getFacing()){
                case UP:
                    aabb = new AxisAlignedBB(bAABB.minX - outdent, bAABB.maxY - indent, bAABB.minZ - outdent, 
                                             bAABB.maxX + outdent, 1 + antiZFight, bAABB.maxZ + outdent);
                    break;
                case DOWN:
                    aabb = new AxisAlignedBB(bAABB.minX - outdent, 0 - antiZFight, bAABB.minZ - outdent, 
                                             bAABB.maxX + outdent, bAABB.minY + indent, bAABB.maxZ + outdent);
                    break;
                case NORTH:
                    aabb = new AxisAlignedBB(bAABB.minX - outdent, bAABB.minY - outdent, 0 - antiZFight, 
                                             bAABB.maxX + outdent, bAABB.maxY + outdent, bAABB.minZ + indent);
                    break;
                case SOUTH:
                    aabb = new AxisAlignedBB(bAABB.minX - outdent, bAABB.minY - outdent, bAABB.maxZ - indent, 
                                             bAABB.maxX + outdent, bAABB.maxY + outdent, 1 + antiZFight);
                    break;
                case WEST:
                    aabb = new AxisAlignedBB(0 - antiZFight, bAABB.minY - outdent, bAABB.minZ - outdent, 
                                             bAABB.minX + indent, bAABB.maxY + outdent, bAABB.maxZ + outdent);
                    break;
                case EAST:
                    aabb = new AxisAlignedBB(bAABB.maxX - indent, bAABB.minY - outdent, bAABB.minZ - outdent, 
                                             1 + antiZFight, bAABB.maxY + outdent, bAABB.maxZ + outdent);
                    break;
                default:
                    aabb = bAABB;
            }
        }else{
            aabb = bAABB;
        }
        
       // GL11.glTranslated(aabb.minX, aabb.minY, aabb.minZ);
        //GL11.glScaled(aabb.maxX - aabb.minX, aabb.maxY - aabb.minY, aabb.maxZ - aabb.minZ);
        //GL11.glTranslated(0.5, -0.5, 0.5);
        //model.render(null, 0, 0, 0, 0, 0, 1 / 16F);
        
        Integer renderList = models.get(aabb);
        if(renderList == null){
            renderList = compileRenderList(aabb);
            models.put(aabb, renderList);
        }
        GL11.glCallList(renderList);
        
        GL11.glPopMatrix();
        GL11.glColor4d(1, 1, 1, 1);
    }
    
    private int compileRenderList(AxisAlignedBB aabb) {
        int renderList = GL11.glGenLists(1);
        GL11.glNewList(renderList, GL11.GL_COMPILE);

        BufferBuilder wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        
        wr.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        wr.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        wr.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        wr.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

        wr.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        wr.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();

        wr.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        wr.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        wr.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

        wr.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        wr.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        wr.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();

        wr.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        wr.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        wr.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        wr.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();

        wr.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        wr.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        wr.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();

        Tessellator.getInstance().draw();

        GL11.glEndList();
        return renderList;
    }
}
