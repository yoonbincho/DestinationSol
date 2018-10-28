/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.destinationsol.game.screens;

import org.destinationsol.GameOptions;
import org.destinationsol.SolApplication;
import org.destinationsol.game.SolGame;
import org.destinationsol.game.item.ItemContainer;
import org.destinationsol.ui.SolInputManager;
import org.destinationsol.ui.SolUiBaseScreen;
import org.destinationsol.ui.UiDrawer;
import org.destinationsol.ui.responsiveUi.UiWindow;

import java.util.HashMap;

public class InventoryScreen extends SolUiBaseScreen {
    public final HashMap<Class<? extends InventoryOperationsScreen>, InventoryOperationsScreen> inventoryOperationsMap;

    private InventoryOperationsScreen myOperation;

    public InventoryScreen() {
        rootUiElement = new UiWindow();

        inventoryOperationsMap = new HashMap<>();
        inventoryOperationsMap.put(ShowInventory.class, new ShowInventory());
    }

    @Override
    public void updateCustom(SolApplication solApplication, SolInputManager.InputPointer[] inputPointers, boolean clickedOutside) {

    }

    @Override
    public boolean isCursorOnBackground(SolInputManager.InputPointer inputPointer) {
        return rootUiElement.getScreenArea().contains(inputPointer.x, inputPointer.y);
    }

    @Override
    public void onAdd(SolApplication solApplication) {
    }

    @Override
    public void drawBackground(UiDrawer uiDrawer, SolApplication solApplication) {
    }

    @Override
    public void draw(UiDrawer uiDrawer, SolApplication solApplication) {

    }

    @Override
    public boolean reactsToClickOutside() {
        return true;
    }

    @Override
    public void blurCustom(SolApplication solApplication) {
        if (!showingHeroItems(solApplication)) {
            return;
        }
        SolGame game = solApplication.getGame();
        ItemContainer items = myOperation.getItems(game);
        if (items != null) {
            items.markAllAsSeen();
        }
    }

    private boolean showingHeroItems(SolApplication application) {
        return false;
    }

    public void setOperations(InventoryOperationsScreen operations) {
        myOperation = operations;
    }
}
