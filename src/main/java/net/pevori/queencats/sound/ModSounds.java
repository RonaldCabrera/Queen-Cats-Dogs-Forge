package net.pevori.queencats.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pevori.queencats.QueenCats;

public class ModSounds {
    public static SoundEvent HUMANOID_CAT_AMBIENT = registerSoundEvent("humanoid_cat_ambient");
    public static SoundEvent HUMANOID_CAT_EAT = registerSoundEvent("humanoid_cat_eat");
    public static SoundEvent HUMANOID_CAT_HURT = registerSoundEvent("humanoid_cat_hurt");
    public static SoundEvent HUMANOID_CAT_DEATH = registerSoundEvent("humanoid_cat_death");

    public static SoundEvent HUMANOID_DOG_AMBIENT = registerSoundEvent("humanoid_dog_ambient");
    public static SoundEvent HUMANOID_DOG_EAT = registerSoundEvent("humanoid_dog_eat");
    public static SoundEvent HUMANOID_DOG_HURT = registerSoundEvent("humanoid_dog_hurt");
    public static SoundEvent HUMANOID_DOG_ANGRY = registerSoundEvent("humanoid_dog_angry");
    public static SoundEvent HUMANOID_DOG_DEATH = registerSoundEvent("humanoid_dog_death");

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(QueenCats.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void bootSounds(){
        
    }
}
