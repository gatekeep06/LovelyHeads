package com.metacontent.lovelyheads.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.metacontent.lovelyheads.LovelyHeads;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class HeadConstructorRecipe implements Recipe<Inventory> {
    private final Ingredient baseInput;
    private final Ingredient extraInput;
    private final boolean decrementExtraInput;
    private final ItemStack outputStack;
    private final Identifier id;

    public HeadConstructorRecipe(Ingredient baseInput, Ingredient extraInput, boolean decrementExtraInput, ItemStack outputStack, Identifier id) {
        this.baseInput = baseInput;
        this.extraInput = extraInput;
        this.decrementExtraInput = decrementExtraInput;
        this.outputStack = outputStack;
        this.id = id;
    }

    public Ingredient getBaseInput() {
        return baseInput;
    }

    public Ingredient getExtraInput() {
        return extraInput;
    }

    public boolean isDecrementExtraInput() {
        return decrementExtraInput;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return inventory.size() >= 3 && baseInput.test(inventory.getStack(0)) && extraInput.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return outputStack;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public static class Type implements RecipeType<HeadConstructorRecipe> {
        private Type() { };
        public static final Type INSTANCE = new Type();
        public static final String ID = "head_constructor_recipe";
    }

    private record HeadConstructorRecipeJsonFormat(JsonObject baseInput, JsonObject extraInput, boolean decrementExtraInput, String outputItem) { }

    public static class Serializer implements RecipeSerializer<HeadConstructorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(LovelyHeads.ID, "head_constructor_recipe");

        private Serializer() { }

        @Override
        public HeadConstructorRecipe read(Identifier id, JsonObject json) {
            HeadConstructorRecipeJsonFormat recipeJson = new Gson().fromJson(json, HeadConstructorRecipeJsonFormat.class);

            if (recipeJson.baseInput == null || recipeJson.extraInput == null || recipeJson.outputItem == null) {
                throw new JsonSyntaxException("A required attribute is missing!");
            }

            Ingredient baseInput = Ingredient.fromJson(recipeJson.baseInput);
            Ingredient extraInput = Ingredient.fromJson(recipeJson.extraInput);
            boolean decrementExtraInput = recipeJson.decrementExtraInput;
            Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                    .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));;
            ItemStack outputStack = new ItemStack(outputItem);

            return new HeadConstructorRecipe(baseInput, extraInput, decrementExtraInput, outputStack, id);
        }

        @Override
        public HeadConstructorRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient baseInput = Ingredient.fromPacket(buf);
            Ingredient extraInput = Ingredient.fromPacket(buf);
            boolean decrementExtraInput = buf.readBoolean();
            ItemStack outputStack = buf.readItemStack();
            return new HeadConstructorRecipe(baseInput, extraInput, decrementExtraInput, outputStack, id);
        }

        @Override
        public void write(PacketByteBuf buf, HeadConstructorRecipe recipe) {
            recipe.getBaseInput().write(buf);
            recipe.getExtraInput().write(buf);
            buf.writeBoolean(recipe.isDecrementExtraInput());
            buf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
        }
    }
}
