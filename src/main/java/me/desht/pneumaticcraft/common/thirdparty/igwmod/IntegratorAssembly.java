package me.desht.pneumaticcraft.common.thirdparty.igwmod;

import igwmod.TextureSupplier;
import igwmod.api.IRecipeIntegrator;
import igwmod.gui.*;
import me.desht.pneumaticcraft.common.item.ItemAssemblyProgram;
import me.desht.pneumaticcraft.common.item.Itemss;
import me.desht.pneumaticcraft.common.recipes.AssemblyRecipe;
import me.desht.pneumaticcraft.lib.Textures;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IntegratorAssembly implements IRecipeIntegrator {

    @Override
    public String getCommandKey() {
        return "assemblyLine";
    }

    @Override
    public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces, List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures) throws IllegalArgumentException {
        if (arguments.length != 3 && arguments.length != 4)
            throw new IllegalArgumentException("Code needs 3 or 4 arguments!");
        int x;
        try {
            x = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The first parameter (the x coordinate) contains an invalid number. Check for invalid characters!");
        }
        int y;
        try {
            y = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The second parameter (the y coordinate) contains an invalid number. Check for invalid characters!");
        }
        locatedTextures.add(new LocatedTexture(TextureSupplier.getTexture(Textures.ICON_LOCATION + "textures/wiki/assembly_line_recipe.png"), x, y, 1 / GuiWiki.TEXT_SCALE));

        int recipeIndex = 0;
        if (arguments.length == 4) {
            try {
                recipeIndex = Integer.parseInt(arguments[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The fourth parameter (the recipeIndex) contains an invalid number. Check for invalid characters!");
            }
        }

        int[] hits = new int[]{recipeIndex};
        int program = ItemAssemblyProgram.DRILL_LASER_DAMAGE;
        AssemblyRecipe foundRecipe = findRecipe(hits, AssemblyRecipe.drillRecipes, arguments[2]);
        if (foundRecipe == null) {
            foundRecipe = findRecipe(hits, AssemblyRecipe.laserRecipes, arguments[2]);
        } else {
            program = ItemAssemblyProgram.DRILL_DAMAGE;
        }

        if (foundRecipe == null) {
            foundRecipe = findRecipe(hits, AssemblyRecipe.drillLaserRecipes, arguments[2]);
        } else {
            program = ItemAssemblyProgram.LASER_DAMAGE;
        }

        if (foundRecipe == null)
            throw new IllegalArgumentException("No recipe found for the string " + arguments[2] + " and the requested index " + recipeIndex + ".");

        locatedStacks.add(new LocatedStack(foundRecipe.getInput(), (int) (GuiWiki.TEXT_SCALE * x) + 1, (int) (GuiWiki.TEXT_SCALE * y) + 46));
        locatedStacks.add(new LocatedStack(foundRecipe.getOutput(), (int) (GuiWiki.TEXT_SCALE * x) + 68, (int) (GuiWiki.TEXT_SCALE * y) + 46));
        locatedStacks.add(new LocatedStack(new ItemStack(Itemss.ASSEMBLY_PROGRAM, 1, program), (int) (GuiWiki.TEXT_SCALE * x) + 78, (int) (GuiWiki.TEXT_SCALE * y) + 15));

        locatedStrings.add(new LocatedString("Program:", x + 150, y + 5, 0xFF000000, false));
        locatedStrings.add(new LocatedString("Assembly Line", x + 40, y + 30, 0xFF000000, false));
    }

    public AssemblyRecipe findRecipe(int[] curHits, List<AssemblyRecipe> recipes, String search) {
        for (AssemblyRecipe recipe : recipes) {
            if (IGWHandler.getNameFromStack(recipe.getOutput()).equals(search)) {
                if (curHits[0] == 0) return recipe;
                curHits[0]--;
            }
        }
        return null;
    }
}
