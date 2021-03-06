package net.darkhax.bookshelf.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

/**
 * A loot condition that checks the ID of the dimension.
 */
public class CheckDimensionId implements ILootCondition {
    
    /**
     * The serializer for this function.
     */
    public static final Serializer SERIALIZER = new Serializer();
    
    private final ResourceLocation dimensionId;
    
    public CheckDimensionId(ResourceLocation dimensionId) {
        
        this.dimensionId = dimensionId;
    }
    
    @Override
    public boolean test (LootContext ctx) {
        
        final World world = ctx.getWorld();
        final Dimension dimension = world.getDimension();
        
        if (dimension != null) {
            
            final DimensionType type = dimension.getType();
            
            if (type != null) {
                
                return type.getRegistryName().equals(this.dimensionId);
            }
        }
        
        return false;
    }
    
    static class Serializer extends ILootCondition.AbstractSerializer<CheckDimensionId> {
        
        Serializer() {
            
            super(new ResourceLocation(Bookshelf.MOD_ID, "check_dimension"), CheckDimensionId.class);
        }
        
        @Override
        public CheckDimensionId deserialize (JsonObject json, JsonDeserializationContext context) {
            
            final ResourceLocation id = ResourceLocation.tryCreate(JSONUtils.getString(json, "dimension"));
            
            return new CheckDimensionId(id);
        }
        
        @Override
        public void serialize (JsonObject json, CheckDimensionId value, JsonSerializationContext context) {
            
            json.addProperty("dimension", value.dimensionId.toString());
        }
    }
}