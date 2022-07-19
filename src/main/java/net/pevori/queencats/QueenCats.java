package net.pevori.queencats;

import net.fabricmc.api.ModInitializer;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.util.ModRegistries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueenCats implements ModInitializer {
	public static final String MOD_ID = "queencats";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		ModRegistries.registerQueenCats();
	}
}
