/*
 * Code Lyoko Mod for Minecraft v@VERSION
 * Copyright 2013 Cortex Modders, Matthew Warren, Jacob Rhoda, and
 * other contributors.
 * Released under the MIT license
 * http://opensource.org/licenses/MIT
 */

package matt.lyoko.lib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public final class PlayerInformation implements IExtendedEntityProperties
{
    public static final String IDENTIFIER = "lyoko_data";

    public static PlayerInformation forPlayer(Entity player)
    {
        return (PlayerInformation) player.getExtendedProperties(IDENTIFIER);
    }
    
    /** The current amount of lifepoints the player has */
    private int lifePoints;
    public int coolDown;
    private int scannerPosX;
    private int scannerPosY;
    private int scannerPosZ;
    public int scannerDim;
    public int scannerYaw;

    private final EntityPlayer player;

    public PlayerInformation(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    public void init(Entity entity, World world)
    {
        this.lifePoints = 100;
        this.coolDown = 0;
        this.scannerPosX = 0;
        this.scannerPosY = 80;
        this.scannerPosZ = 0;
        this.scannerDim = 0;
        this.scannerYaw = 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("lifePoints", this.lifePoints);
        nbt.setInteger("coolDown", this.coolDown);
        nbt.setInteger("spx", this.scannerPosX);
        nbt.setInteger("spy", this.scannerPosY);
        nbt.setInteger("spz", this.scannerPosZ);
        nbt.setInteger("sd", this.scannerDim);
        nbt.setInteger("sy", this.scannerYaw);
        compound.setCompoundTag(IDENTIFIER, nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound playerNbt)
    {
        NBTTagCompound nbt = playerNbt.getCompoundTag(IDENTIFIER);
        this.lifePoints = nbt.getInteger("lifePoints");
        this.coolDown = nbt.getInteger("coolDown");
        this.scannerPosX = nbt.getInteger("spx");
        this.scannerPosY = nbt.getInteger("spy");
        this.scannerPosZ = nbt.getInteger("spz");
        this.scannerDim = nbt.getInteger("sd");
        this.scannerYaw = nbt.getInteger("sy");
    }

    public int getLifePoints()
    {
        return this.lifePoints;
    }

    public int setLifePoints(int lifePoints)
    {
        if(this.lifePoints != lifePoints)
        {
            this.lifePoints = lifePoints;
            this.setDirty();
        }
        return this.lifePoints;
    }

    public int getMaxLifePoints()
    {
        return 100;
    }

    public int decreaseLifePoints(int decrement)
    {
        this.lifePoints -= decrement;
        if (this.lifePoints < 0)
        {
            this.lifePoints = 0;
        }
        this.setDirty();
        return this.lifePoints;
    }

    public int increaseLifePoints(int increment)
    {
        this.lifePoints += increment;
        if (this.lifePoints > 100)
        {
            this.lifePoints = 100;
        }
        this.setDirty();
        return this.lifePoints;
    }

    public int getCoolDown()
    {
        return this.coolDown;
    }

    public void resetCoolDown()
    {
        this.coolDown = 10;
    }

    public void decreaseCoolDown(int amt)
    {
        this.coolDown -= amt;
        if (this.coolDown > 10)
            this.coolDown = 10;
        if (this.coolDown < 0)
            this.coolDown = 0;
        this.setDirty();
    }

    public int getMaxCoolDown()
    {
        return 10;
    }

    public void setScannerPosition(int x, int y, int z)
    {
        this.scannerPosX = x;
        this.scannerPosY = y;
        this.scannerPosZ = z;
    }

    public int getScannerPosX()
    {
        return this.scannerPosX;
    }

    public int getScannerPosY()
    {
        return this.scannerPosY;
    }

    public int getScannerPosZ()
    {
        return this.scannerPosZ;
    }

    /*
     * marks that this needs to be resend to the client
     */
    public void setDirty()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try
        {
            outputStream.writeInt(this.getLifePoints());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "LifePoints";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        
        PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
    }
}