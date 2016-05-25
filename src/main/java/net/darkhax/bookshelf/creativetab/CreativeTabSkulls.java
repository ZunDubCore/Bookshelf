package net.darkhax.bookshelf.creativetab;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.bookshelf.lib.util.SkullUtils;
import net.darkhax.bookshelf.lib.util.SkullUtils.MHFAccount;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabSkulls extends CreativeTabs {
    
    private static ItemStack displayStack = null;
    private static List<ItemStack> cache = new ArrayList<ItemStack>();
    
    public CreativeTabSkulls() {
        
        super("bookshelfheads");
        this.setBackgroundImageName("item_search.png");
        displayStack = SkullUtils.createSkull("Darkhax");
        
        for (final MHFAccount mhf : MHFAccount.values())
            cache.add(SkullUtils.createSkull(mhf));
    }
    
    @Override
    public Item getTabIconItem () {
        
        return Items.SKULL;
    }
    
    @Override
    public ItemStack getIconItemStack () {
        
        return displayStack;
    }
    
    @Override
    public boolean hasSearchBar () {
        
        return true;
    }
    
    @Override
    public void displayAllRelevantItems (List<ItemStack> itemList) {
        
        itemList.addAll(cache);
    }
}