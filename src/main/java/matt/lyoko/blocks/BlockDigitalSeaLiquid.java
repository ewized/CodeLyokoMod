/*
 * Code Lyoko Mod for Minecraft v@VERSION Copyright 2013 Cortex Modders, Matthew
 * Warren, Jacob Rhoda, and other contributors. Released under the MIT license
 * http://opensource.org/licenses/MIT
 */

package matt.lyoko.blocks;

import matt.lyoko.CodeLyoko;
import matt.lyoko.lib.LyokoDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockDigitalSeaLiquid extends BlockFluidClassic
{
    public BlockDigitalSeaLiquid(Fluid fluid, Material material)
    {
        super(fluid, material);
        this.setCreativeTab(CodeLyoko.LyokoTabs);
    }

    @Override
    // getIcon
    public IIcon getIcon(int side, int meta)
    {
        // getBlockFromName - getBlockFromName
        return Block.getBlockFromName("flowing_water").getIcon(side, meta);

    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        // fix later
        return 10;// this.maxScaledLight;
    }

    @Override
    // colorMultiplayer
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return this.getFluid().getColor();
    }

    @Override
    // onEntityCollidedWithBlock
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        par5Entity.attackEntityFrom(LyokoDamageSource.digitalSea, 100);
    }
}
