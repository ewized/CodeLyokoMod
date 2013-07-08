package matt.lyoko.entities.tileentity;

import matt.lyoko.CodeLyoko;
import matt.lyoko.blocks.BlockSuperCalc;
import matt.lyoko.blocks.ModBlocks;
import matt.lyoko.items.ItemLyokoFuel;
import matt.lyoko.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntitySuperCalc extends TileEntity implements IInventory//, ISidedInventory
{
	private ItemStack[] inv;
	public float timeLeft;
	public String sector = "";
	public int flush = 20;
	//private Ticket ticket;
	
	public TileEntitySuperCalc(){
		inv = new ItemStack[2];
		timeLeft = 100.0F;
		
		//ticket = ForgeChunkManager.requestTicket(CodeLyoko.instance, worldObj, Type.NORMAL);
		//ticket.getModData().setInteger("SuperCalcX", xCoord);
		//ticket.getModData().setInteger("SuperCalcY", yCoord);
		//ticket.getModData().setInteger("SuperCalcZ", zCoord);
		//ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(xCoord, zCoord));
		//System.out.println("chunk loaded");
	}
	
	@Override
	public int getSizeInventory() {
		return inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if (stack.stackSize <= amt)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}
	
	public void resetSector(World world, int x, int y, int z)
	{
		((TileEntitySuperCalc)world.getBlockTileEntity(x + 1, y, z + 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x + 1, y, z)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x + 1, y, z - 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x, y, z + 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x, y, z)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x, y, z - 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x - 1, y, z + 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x - 1, y, z)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x - 1, y, z - 1)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x, y + 1, z)).sector = "";
		((TileEntitySuperCalc)world.getBlockTileEntity(x, y + 2, z)).sector = "";
	}
	
	public void syncCable(World world, int x, int y, int z)
	{
		if(world.getBlockId(x, y, z) == ModBlocks.Cable.blockID && world.getBlockTileEntity(x, y, z) != null)
		{
			TileEntityCable cable = (TileEntityCable)world.getBlockTileEntity(x, y, z);
			if(cable != null && cable.getCoolDown() == 0 && cable.getSector().equals(""))
			{
				cable.resetCoolDown();
				cable.setSector(sector.substring(0, sector.length() - 3));
				world.notifyBlocksOfNeighborChange(x, y, z, ModBlocks.Cable.blockID);
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		if(!sector.equals(""))
		{
			syncCable(worldObj, xCoord + 1, yCoord, zCoord);
			syncCable(worldObj, xCoord - 1, yCoord, zCoord);
			syncCable(worldObj, xCoord, yCoord + 1, zCoord);
			syncCable(worldObj, xCoord, yCoord - 1, zCoord);
			syncCable(worldObj, xCoord, yCoord, zCoord + 1);
			syncCable(worldObj, xCoord, yCoord, zCoord - 1);
		}
		
		if(BlockSuperCalc.isMultiBlock(worldObj, xCoord, yCoord, zCoord))
		{
			if(flush > 0)
			{
				flush--;
			}
			else if(flush < 0)
			{
				flush = 0;
			}
			else if(flush == 0)
			{
				resetSector(worldObj, xCoord, yCoord, zCoord);
				flush = 20;
			}
			
			for(int i = -1; i < 2; i++)
			{
				for(int k = -1; k < 2; k++)
				{
					for(int j = 0; j < 3; j++)
					{
						if(i != 0 || j != 0 || k != 0)
						{
							TileEntitySuperCalc slave = (TileEntitySuperCalc)worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
							{
								if(slave != null && !(slave.sector.equals("")))
								{
									if(this.sector.equals(""))
									{
										this.sector = slave.sector;
									}
									slave.sector = "";
								}
								
								if(slave != null && !(this.sector.equals("")))
								{
									slave.sector = this.sector;
								}
							}
						}
					}
				}
			}
		}
		
		int slot = 0;
		int slot2 = 1;
		
		ItemStack stack = getStackInSlot(slot);
		ItemStack stack2 = getStackInSlot(slot2);
		
		if(stack != null && stack.getItem() == ModItems.LaserArrow)
		{
			setInventorySlotContents(slot2, new ItemStack(ModItems.DataFragment, 64));
		}
		else if(stack != null && stack.getItemDamage() == stack.getMaxDamage())
		{
			if(stack.getItem() instanceof ItemLyokoFuel && stack.getItem() == ModItems.LeadCell)
			{
				setInventorySlotContents(slot, new ItemStack(ModItems.DepletedLeadCell));
			}
			else if(stack.getItem() instanceof ItemLyokoFuel && stack.getItem() == ModItems.UraniumCell)
			{
				setInventorySlotContents(slot, new ItemStack(ModItems.DepletedUraniumCell));
			}
		}
		else if(stack != null && stack.getItemDamage() < stack.getMaxDamage() && ((stack2 != null && stack2.stackSize < 64) || stack2 == null))
		{
			setInventorySlotContents(slot, new ItemStack(stack.getItem(), 1, stack.getItemDamage() + 1));
		}
		
		if(timeLeft <= 0.0F)
		{
			if(stack2 == null)
			{
				setInventorySlotContents(slot2, new ItemStack(ModItems.DataFragment));
			}
			else if(stack2.stackSize < 64)
			{
				stack2.stackSize++;
			}
			timeLeft = 100.0F;
		}
		else if(timeLeft > 0.0F && ((stack2 != null && stack2.stackSize < 64) || stack2 == null) && stack != null && stack.getItem() instanceof ItemLyokoFuel)
		{
			timeLeft = timeLeft - 0.05F;
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		NBTTagCompound tag = pkt.customParam1;
		this.readFromNBT(tag);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}
	
	@Override
	public void openChest() {}
	
	@Override
	public void closeChest() {}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		this.timeLeft = tagCompound.getFloat("remainingTime");
		this.sector = tagCompound.getString("sector");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("remainingTime", this.timeLeft);
		tagCompound.setString("sector", this.sector);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}
	
	@Override
	public String getInvName()
	{
		return "tileentitysupercalc";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int k = -1; k < 2; k++)
			{
				for(int j = -2; j < 1; j++)
				{
					if(BlockSuperCalc.isMultiBlock(worldObj, xCoord + i, yCoord + j, zCoord + k))
					{
						if(slot == 0 && stack != null && stack.getItem() instanceof ItemLyokoFuel)
						{
							return true;
						}
						//else if(slot == 1 && stack != null && stack.getItem() instanceof ItemDataFragment)
						//{
						//	return true;
						//}
					}
				}
			}
		}
		return false;
	}
	
	/*@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		int[] slot = {1};
		if(side == 0 || side == 1)
		{
			slot[0] = 0;
			return slot;
		}
		return slot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		return this.isStackValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		return slot == 1 && (side != 0 && side != 1);
	}*/
}