/*
 * Code Lyoko Mod for Minecraft v@VERSION
 * Copyright 2013 Cortex Modders, Matthew Warren, Jacob Rhoda, and
 * other contributors.
 * Released under the MIT license
 * http://opensource.org/licenses/MIT
 */

package matt.lyoko;

import matt.lyoko.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LyokoTab extends CreativeTabs
{
    public LyokoTab(String par2Str)
    {
        super(par2Str);
    }
    
    @Override
    public Item getTabIconItem()
    {
        return new ItemStack(ModBlocks.Log).getItem();
    }
    
    @Override
    public String getTranslatedTabLabel()
    {
        return "Lyoko";
    }
}
