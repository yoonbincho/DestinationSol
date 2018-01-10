/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.destinationsol.game.screens;

import java.util.ArrayList;
import java.util.List;

import org.destinationsol.GameOptions;
import org.destinationsol.SolApplication;
import org.destinationsol.game.SolGame;
import org.destinationsol.game.item.ItemContainer;
import org.destinationsol.game.item.SolItem;
import org.destinationsol.game.ship.FarShip;
import org.destinationsol.game.ship.SolShip;
import org.destinationsol.ui.SolInputManager;
import org.destinationsol.ui.SolUiControl;

public class GiveItems implements InventoryOperations {

    private final ArrayList<SolUiControl> controls = new ArrayList<>();
    private final SolUiControl giveControl;
    private FarShip target;

    GiveItems(InventoryScreen inventoryScreen, GameOptions gameOptions) {
        giveControl = new SolUiControl(inventoryScreen.itemCtrl(0), true, gameOptions.getKeySellItem());
        giveControl.setDisplayName("Give");
        controls.add(giveControl);
    }

    @Override
    public ItemContainer getItems(SolGame game) {
        SolShip hero = game.getHero();
        return hero == null ? null : hero.getItemContainer();
    }

    @Override
    public boolean isUsing(SolGame game, SolItem item) {
        SolShip hero = game.getHero();
        return hero != null && hero.maybeUnequip(game, item, false);
    }

    @Override
    public String getHeader() {
        return "Give:";
    }

    @Override
    public List<SolUiControl> getControls() {
        return controls;
    }

    @Override
    public void updateCustom(SolApplication solApplication, SolInputManager.InputPointer[] inputPointers, boolean clickedOutside) {
        SolGame game = solApplication.getGame();
        InventoryScreen is = game.getScreens().inventoryScreen;
        SolShip hero = game.getHero();

        SolItem selItem = is.getSelectedItem();
        if (selItem == null) {
            giveControl.setDisplayName("----");
            giveControl.setEnabled(false);
            return;
        }

        boolean isWornAndCanBeGiven = isItemEquippedAndGiveable(selItem, solApplication.getOptions());
        boolean enabled = isItemGiveable(selItem, target);

        if (enabled && isWornAndCanBeGiven) {
            giveControl.setDisplayName("Give");
            giveControl.setEnabled(true);
        } else if (enabled && !isWornAndCanBeGiven) {
            giveControl.setDisplayName("Unequip it!");
            giveControl.setEnabled(false);
        } else {
            giveControl.setDisplayName("----");
            giveControl.setEnabled(false);
        }

        if (!enabled || !isWornAndCanBeGiven) {
            return;
        }
        if (giveControl.isJustOff()) {
            ItemContainer ic = hero.getItemContainer();
            is.setSelected(ic.getSelectionAfterRemove(is.getSelected()));
            ic.remove(selItem);
            target.getIc().add(selItem);
        }
    }

    private boolean isItemGiveable(SolItem item, FarShip target) {
        return target.getIc().canAdd(item);
    }

    // Return true if the item is not worn, or is worn and canSellEquippedItems is true
    private boolean isItemEquippedAndGiveable(SolItem item, GameOptions options) {
        return (item.isEquipped() == 0 || options.canSellEquippedItems);
    }

    public void setTarget(FarShip farship) {
        this.target = farship;
    }
}
