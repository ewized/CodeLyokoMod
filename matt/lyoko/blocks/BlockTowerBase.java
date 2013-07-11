package matt.lyoko.blocks;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import matt.lyoko.CodeLyoko;
import matt.lyoko.lib.PlayerInformation;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockTowerBase extends BlockBreakable
{
	public BlockTowerBase(int par1, String par2, boolean flag)
	{
		super(par1, par2, Material.iron, flag);
		this.setCreativeTab(CodeLyoko.LyokoTabs);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("lyoko:towerbase");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
    	return null;
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity ent)
	{
		if(ent instanceof EntityPlayer && !ent.worldObj.isRemote)
		{
			EntityPlayer player = (EntityPlayer) ent;
			PlayerInformation pi = PlayerInformation.forPlayer(player);
			
			if(pi.getCoolDown() <= 0)
			{
				pi.increaseLifePoints(25);
				pi.resetCoolDown();
			}
		}
	}
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public boolean isOpaqueCube()
    {
        return false;
    }
}