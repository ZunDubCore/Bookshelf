/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public final class EntityUtils {
    
    /**
     * Calculates the distance between two entities.
     *
     * @param firstEntity The first entity to use.
     * @param secondEntity The second entity to use.
     * @return double The distance between the two entities passed.
     */
    public static double getDistanceFromEntity (Entity firstEntity, Entity secondEntity) {
        
        return MathsUtils.getDistanceBetweenPoints(firstEntity.getPositionVector(), secondEntity.getPositionVector());
    }
    
    /**
     * Calculates the distance between an entity and a BlockPos.
     *
     * @param entity The Entity to use for the first position.
     * @param pos The BlockPos to use for the second position.
     * @return double The distance between the Entity and the BlockPos.
     */
    public static double getDistaceFromPos (Entity entity, BlockPos pos) {
        
        return MathsUtils.getDistanceBetweenPoints(entity.getPositionVector(), new Vec3d(pos));
    }
    
    /**
     * Pushes an entity towards a specific direction.
     *
     * @param entityToMove The entity that you want to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, Direction direction, double force) {
        
        pushTowards(entityToMove, entityToMove.getPosition().offset(direction.getOpposite(), 1), force);
    }
    
    /**
     * Pushes an Entity towards a BlockPos.
     *
     * @param entityToMove The entity that you want to push.
     * @param pos The BlockPos to push the entity towards.
     * @param force The amount of force to push the entity with.
     */
    public static void pushTowards (Entity entityToMove, BlockPos pos, double force) {
        
        final BlockPos entityPos = entityToMove.getPosition();
        final double distanceX = (double) pos.getX() - entityPos.getX();
        final double distanceY = (double) pos.getY() - entityPos.getY();
        final double distanceZ = (double) pos.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0)
            entityToMove.setMotion(new Vec3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
    }
    
    /**
     * Pushes an entity towards another one.
     *
     * @param entityToMove The entity that should be pushed towards the other entity.
     * @param destination The destination entity, that the entity to move should be pushed
     *        towards.
     * @param force The amount of force to push the entityToMove with.
     */
    public static void pushTowards (Entity entityToMove, Entity destination, double force) {
        
        final double distanceX = destination.posX - entityToMove.posX;
        final double distanceY = destination.posY - entityToMove.posY;
        final double distanceZ = destination.posZ - entityToMove.posZ;
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0)
            entityToMove.setMotion(new Vec3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
    }
    
    /**
     * Creates a Vec3d that represents the additional motion that would be needed to push an
     * entity towards a destination.
     *
     * @param entityToMove The entity to push.
     * @param direction The direction to push the entity.
     * @param force The amount of force to use.
     * 
     */
    public static void pushTowardsDirection (Entity entityToMove, Direction direction, double force) {
        
        final BlockPos entityPos = entityToMove.getPosition();
        final BlockPos destination = entityToMove.getPosition().offset(direction.getOpposite(), 1);
        
        final double distanceX = (double) destination.getX() - entityPos.getX();
        final double distanceY = (double) destination.getY() - entityPos.getY();
        final double distanceZ = (double) destination.getZ() - entityPos.getZ();
        final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
        
        if (distance > 0)
            entityToMove.setMotion(new Vec3d(distanceX / distance * force, distanceY / distance * force, distanceZ / distance * force));
    }
    
    /**
     * Checks if two entities are close enough together.
     *
     * @param firstEntity The first entity to check.
     * @param secondEntity The second entity to check.
     * @param maxDistance The maximum distance that the entities can be apart.
     * @return boolean True if the distance between the entities are within range of the
     *         maxDistance.
     */
    public static boolean areEntitiesCloseEnough (Entity firstEntity, Entity secondEntity, double maxDistance) {
        
        return getDistanceFromEntity(firstEntity, secondEntity) < maxDistance * maxDistance;
    }
    
    /**
     * Gets a List of entities that are within the provided area.
     *
     * @param <T> The type of entities to look for.
     * @param entityClass The type of entity you are looking for.
     * @param world The world to search in.
     * @param pos The position to start the search around.
     * @param range The range of the search.
     * @return A List containing all entities of the specified type that are within the range.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getEntitiesInArea (Class<? extends Entity> entityClass, World world, BlockPos pos, int range) {
        
        return (List<T>) world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)));
    }
    
    /**
     * A check to see if an entity is wearing a full suit of the armor. This check is based on
     * the class names of armor.
     *
     * @param living: The living entity to check the armor of.
     * @param armorClass: The class of the armor to check against.
     * @return boolean: True if every piece of armor the entity is wearing are the same class
     *         as the provied armor class.
     */
    public static boolean isWearingFullSet (MobEntity living, Class<Item> armorClass) {
        
        for (final EquipmentSlotType slot : EquipmentSlotType.values())
            if (slot.getSlotType().equals(EquipmentSlotType.Group.ARMOR)) {
                
                final ItemStack armor = living.getItemStackFromSlot(slot);
                
                if (armor.isEmpty() || !armor.getItem().getClass().equals(armorClass))
                    return false;
            }
        
        return true;
    }
    
    /**
     * Gets the max health value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMaxHealth (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue();
    }
    
    /**
     * Gets the follow/tracking range value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getFollowRange (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
    }
    
    /**
     * Gets the knockback resistance value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getKnockbackResistance (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue();
    }
    
    /**
     * Gets the movement speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getMovementSpeed (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
    }
    
    /**
     * Gets the attack value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackDamage (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
    }
    
    /**
     * Gets the attack speed value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getAttackSpeed (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getValue();
    }
    
    /**
     * Gets the armor value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmor (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.ARMOR).getValue();
    }
    
    /**
     * Gets the armor toughness value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getArmorToughness (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue();
    }
    
    /**
     * Gets the luck value of an entity.
     *
     * @param entity The entity to get the value from.
     * @return The value of the attribute.
     */
    public static double getLuck (MobEntity entity) {
        
        return entity.getAttribute(SharedMonsterAttributes.LUCK).getValue();
    }
    
    /**
     * Gets a value of an attribute for an entity.
     *
     * @param entity The entity to get the value of.
     * @param attribute The attribute to get the value of.
     * @return The value of the attribute.
     */
    public static double getValue (MobEntity entity, IAttribute attribute) {
        
        return entity.getAttribute(attribute).getValue();
    }
    
    /**
     * Quickly creates a new entity item and adds it to the drops.
     * 
     * @param stack The item to add.
     * @param event The event list.
     */
    public static void addDrop (ItemStack stack, LivingDropsEvent event) {
        
        final LivingEntity living = event.getEntityLiving();
        event.getDrops().add(new ItemEntity(living.getEntityWorld(), living.posX, living.posY, living.posZ, stack));
    }
}