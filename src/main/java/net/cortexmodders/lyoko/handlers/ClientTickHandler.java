/*
 * Code Lyoko Mod for Minecraft v@VERSION
 * Copyright 2013 Cortex Modders, Matthew Warren, Jacob Rhoda, and
 * other contributors.
 * Released under the MIT license
 * http://opensource.org/licenses/MIT
 */

package net.cortexmodders.lyoko.handlers;

import net.cortexmodders.lyoko.CodeLyoko;
import net.cortexmodders.lyoko.client.render.TileAnimator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ClientTickHandler
{
    /**
     * Used to advance all the animator instances once every tick.
     * 
     * @param tickData
     */
    public void advanceAnimatorInstances(Object... tickData)
    {
        for (TileAnimator inst : CodeLyoko.animatorInstances)
            inst.animate();
    }

    @SubscribeEvent
    public void renderTick(RenderTickEvent event)
    {
        this.advanceAnimatorInstances(event);
    }
}