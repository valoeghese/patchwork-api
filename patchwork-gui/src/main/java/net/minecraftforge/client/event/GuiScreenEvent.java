/*
 * Minecraft Forge, Patchwork Project
 * Copyright (c) 2016-2020, 2019-2020
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.client.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.minecraftforge.eventbus.api.Event;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Event classes for GuiScreen events.
 *
 * @author bspkrs
 */
@Environment(EnvType.CLIENT)
public class GuiScreenEvent extends Event {
	private final Screen gui;

	// For EventBus
	public GuiScreenEvent() {
		this(null);
	}

	public GuiScreenEvent(Screen gui) {
		this.gui = gui;
	}

	/**
	 * The {@link Screen} object generating this event.
	 */
	public Screen getGui() {
		return gui;
	}

	public static class InitGuiEvent extends GuiScreenEvent {
		private Consumer<AbstractButtonWidget> add;
		private Consumer<AbstractButtonWidget> remove;

		private List<AbstractButtonWidget> list;

		// For EventBus
		public InitGuiEvent() {
		}

		public InitGuiEvent(Screen gui, List<AbstractButtonWidget> list, Consumer<AbstractButtonWidget> add, Consumer<AbstractButtonWidget> remove) {
			super(gui);
			this.list = Collections.unmodifiableList(list);
			this.add = add;
			this.remove = remove;
		}

		/**
		 * Unmodifiable reference to the list of buttons on the {@link #gui}.
		 */
		public List<AbstractButtonWidget> getWidgetList() {
			return list;
		}

		public void addWidget(AbstractButtonWidget button) {
			add.accept(button);
		}

		public void removeWidget(AbstractButtonWidget button) {
			remove.accept(button);
		}

		/**
		 * This event fires just after initializing {@link Screen#minecraft}, {@link Screen#font},
		 * {@link Screen#width}, and {@link Screen#height}.
		 *
		 * <p>If canceled the following lines are skipped in {@link Screen#init(net.minecraft.client.MinecraftClient, int, int)}:</p>
		 * <p>{@code this.buttons.clear();}</p>
		 * <p>{@code this.children.clear();}</p>
		 * <p>{@code this.init();}</p>
		 */
		public static class Pre extends InitGuiEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, List<AbstractButtonWidget> list, Consumer<AbstractButtonWidget> add, Consumer<AbstractButtonWidget> remove) {
				super(gui, list, add, remove);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires right after {@link Screen#init()}.
		 * This is a good place to alter a Screen's component layout if desired.
		 */
		public static class Post extends InitGuiEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, List<AbstractButtonWidget> list, Consumer<AbstractButtonWidget> add, Consumer<AbstractButtonWidget> remove) {
				super(gui, list, add, remove);
			}
		}
	}

	public static class DrawScreenEvent extends GuiScreenEvent {
		private final int mouseX;
		private final int mouseY;
		private final float renderPartialTicks;

		// For EventBus
		public DrawScreenEvent() {
			this(null, -1, -1, 0);
		}

		public DrawScreenEvent(Screen gui, int mouseX, int mouseY, float renderPartialTicks) {
			super(gui);
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			this.renderPartialTicks = renderPartialTicks;
		}

		/**
		 * The x coordinate of the mouse pointer on the screen.
		 */
		public int getMouseX() {
			return mouseX;
		}

		/**
		 * The y coordinate of the mouse pointer on the screen.
		 */
		public int getMouseY() {
			return mouseY;
		}

		/**
		 * Partial render ticks elapsed.
		 */
		public float getRenderPartialTicks() {
			return renderPartialTicks;
		}

		/**
		 * This event fires just before {@link Screen#render(int, int, float)} is called.
		 * Cancel this event to skip {@link Screen#render(int, int, float)}.
		 */
		public static class Pre extends DrawScreenEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, int mouseX, int mouseY, float renderPartialTicks) {
				super(gui, mouseX, mouseY, renderPartialTicks);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires just after {@link Screen#render(int, int, float)} is called.
		 */
		public static class Post extends DrawScreenEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, int mouseX, int mouseY, float renderPartialTicks) {
				super(gui, mouseX, mouseY, renderPartialTicks);
			}
		}
	}

	/**
	 * This event fires at the end of {@link Screen#renderBackground(int)} and before the rest of the Gui draws.
	 * This allows drawing next to Guis, above the background but below any tooltips.
	 */
	public static class BackgroundDrawnEvent extends GuiScreenEvent {
		// For EventBus
		public BackgroundDrawnEvent() {
		}

		public BackgroundDrawnEvent(Screen gui) {
			super(gui);
		}
	}

	/**
	 * This event fires in {@link AbstractInventoryScreen#drawPotionEffects()}
	 * when potion effects are active and the gui wants to move over.
	 * Cancel this event to prevent the Gui from being moved.
	 */
	public static class PotionShiftEvent extends GuiScreenEvent {
		// For EventBus
		public PotionShiftEvent() {
		}

		public PotionShiftEvent(Screen gui) {
			super(gui);
		}

		@Override
		public boolean isCancelable() {
			return true;
		}
	}

	public static class ActionPerformedEvent extends GuiScreenEvent {
		private ButtonWidget button;
		private List<ButtonWidget> buttonList;

		// For EventBus
		public ActionPerformedEvent() {
		}

		public ActionPerformedEvent(Screen gui, ButtonWidget button, List<ButtonWidget> buttonList) {
			super(gui);
			this.setButton(button);
			this.setButtonList(new ArrayList<>(buttonList));
		}

		/**
		 * The button that was clicked.
		 */
		public ButtonWidget getButton() {
			return button;
		}

		public void setButton(ButtonWidget button) {
			this.button = button;
		}

		/**
		 * A COPY of the {@link #buttonList} field from {@link Screen#buttons} referenced by {@link #gui}.
		 */
		public List<ButtonWidget> getButtonList() {
			return buttonList;
		}

		public void setButtonList(List<ButtonWidget> buttonList) {
			this.buttonList = buttonList;
		}

		/**
		 * This event fires once it has been determined that a {@link ButtonWidget} object has been clicked.
		 * Replace button with a different button from buttonList to have that button's action executed.
		 */
		public static class Pre extends ActionPerformedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, ButtonWidget button, List<ButtonWidget> buttonList) {
				super(gui, button, buttonList);
			}
		}

		/**
		 * This event fires after {@link Screen#actionPerformed(GuiButton)} provided that the active
		 * screen has not been changed as a result of {@link Screen#actionPerformed(GuiButton)}.
		 */
		public static class Post extends ActionPerformedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, ButtonWidget button, List<ButtonWidget> buttonList) {
				super(gui, button, buttonList);
			}
		}
	}

	public abstract static class MouseInputEvent extends GuiScreenEvent {
		private final double mouseX;
		private final double mouseY;

		// For EventBus
		public MouseInputEvent() {
			this(null, -1, -1);
		}

		public MouseInputEvent(Screen gui, double mouseX, double mouseY) {
			super(gui);
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}

		public double getMouseX() {
			return mouseX;
		}

		public double getMouseY() {
			return mouseY;
		}
	}

	public abstract static class MouseClickedEvent extends MouseInputEvent {
		private final int button;

		// For EventBus
		public MouseClickedEvent() {
			this(null, -1, -1, -1);
		}

		public MouseClickedEvent(Screen gui, double mouseX, double mouseY, int button) {
			super(gui, mouseX, mouseY);
			this.button = button;
		}

		public int getButton() {
			return button;
		}

		/**
		 * This event fires when a mouse click is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#mouseClicked(double, double, int)}.
		 */
		public static class Pre extends MouseClickedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, double mouseX, double mouseY, int button) {
				super(gui, mouseX, mouseY, button);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#mouseClicked(double, double, int)} if the click was not already handled.
		 * Cancel this event when you successfully use the mouse click, to prevent other handlers from using the same input.
		 */
		public static class Post extends MouseClickedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, double mouseX, double mouseY, int button) {
				super(gui, mouseX, mouseY, button);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public abstract static class MouseReleasedEvent extends MouseInputEvent {
		private final int button;

		// For EventBus
		public MouseReleasedEvent() {
			this(null, -1, -1, -1);
		}

		public MouseReleasedEvent(Screen gui, double mouseX, double mouseY, int button) {
			super(gui, mouseX, mouseY);
			this.button = button;
		}

		public int getButton() {
			return button;
		}

		/**
		 * This event fires when a mouse release is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#mouseReleased(double, double, int)}.
		 */
		public static class Pre extends MouseReleasedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, double mouseX, double mouseY, int button) {
				super(gui, mouseX, mouseY, button);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#mouseReleased(double, double, int)} if the release was not already handled.
		 * Cancel this event when you successfully use the mouse release, to prevent other handlers from using the same input.
		 */
		public static class Post extends MouseReleasedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, double mouseX, double mouseY, int button) {
				super(gui, mouseX, mouseY, button);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public abstract static class MouseDragEvent extends MouseInputEvent {
		private final int mouseButton;
		private final double dragX;
		private final double dragY;

		// For EventBus
		public MouseDragEvent() {
			this(null, -1, -1, -1, -1, -1);
		}

		public MouseDragEvent(Screen gui, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
			super(gui, mouseX, mouseY);
			this.mouseButton = mouseButton;
			this.dragX = dragX;
			this.dragY = dragY;
		}

		public int getMouseButton() {
			return mouseButton;
		}

		public double getDragX() {
			return dragX;
		}

		public double getDragY() {
			return dragY;
		}

		/**
		 * This event fires when a mouse drag is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#mouseDragged(double, double, int, double, double)}.
		 */
		public static class Pre extends MouseDragEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
				super(gui, mouseX, mouseY, mouseButton, dragX, dragY);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#mouseDragged(double, double, int, double, double)} if the drag was not already handled.
		 * Cancel this event when you successfully use the mouse drag, to prevent other handlers from using the same input.
		 */
		public static class Post extends MouseDragEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
				super(gui, mouseX, mouseY, mouseButton, dragX, dragY);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public abstract static class MouseScrollEvent extends MouseInputEvent {
		private final double scrollDelta;

		// For EventBus
		public MouseScrollEvent() {
			this(null, -1, -1, -1);
		}

		public MouseScrollEvent(Screen gui, double mouseX, double mouseY, double scrollDelta) {
			super(gui, mouseX, mouseY);
			this.scrollDelta = scrollDelta;
		}

		public double getScrollDelta() {
			return scrollDelta;
		}

		/**
		 * This event fires when a mouse scroll is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#mouseScrolled(double)}.
		 */
		public static class Pre extends MouseScrollEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, double mouseX, double mouseY, double scrollDelta) {
				super(gui, mouseX, mouseY, scrollDelta);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#mouseScrolled(double, double, double)} if the scroll was not already handled.
		 * Cancel this event when you successfully use the mouse scroll, to prevent other handlers from using the same input.
		 */
		public static class Post extends MouseScrollEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, double mouseX, double mouseY, double scrollDelta) {
				super(gui, mouseX, mouseY, scrollDelta);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public abstract static class KeyboardKeyEvent extends GuiScreenEvent {
		private final int keyCode;
		private final int scanCode;
		private final int modifiers;

		// For EventBus
		public KeyboardKeyEvent() {
			this(null, -1, -1, -1);
		}

		public KeyboardKeyEvent(Screen gui, int keyCode, int scanCode, int modifiers) {
			super(gui);
			this.keyCode = keyCode;
			this.scanCode = scanCode;
			this.modifiers = modifiers;
		}

		/**
		 * The keyboard key that was pressed or released.
		 * https://www.glfw.org/docs/latest/group__keys.html
		 *
		 * @see GLFW key constants starting with "GLFW_KEY_"
		 */
		public int getKeyCode() {
			return keyCode;
		}

		/**
		 * Platform-specific scan code.
		 * Used for {@link net.minecraft.client.util.InputUtil#getKeyCode(int, int)}
		 *
		 * <p>The scan code is unique for every key, regardless of whether it has a key code.
		 * Scan codes are platform-specific but consistent over time, so keys will have different scan codes depending
		 * on the platform but they are safe to save to disk as custom key bindings.
		 */
		public int getScanCode() {
			return scanCode;
		}

		/**
		 * Bit field representing the modifier keys pressed.
		 * https://www.glfw.org/docs/latest/group__mods.html
		 *
		 * @see GLFW#GLFW_MOD_SHIFT
		 * @see GLFW#GLFW_MOD_CONTROL
		 * @see GLFW#GLFW_MOD_ALT
		 * @see GLFW#GLFW_MOD_SUPER
		 */
		public int getModifiers() {
			return modifiers;
		}
	}

	public abstract static class KeyboardKeyPressedEvent extends KeyboardKeyEvent {
		// For EventBus
		public KeyboardKeyPressedEvent() {
		}

		public KeyboardKeyPressedEvent(Screen gui, int keyCode, int scanCode, int modifiers) {
			super(gui, keyCode, scanCode, modifiers);
		}

		/**
		 * This event fires when keyboard input is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#keyPressed(int, int, int)}.
		 */
		public static class Pre extends KeyboardKeyPressedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, int keyCode, int scanCode, int modifiers) {
				super(gui, keyCode, scanCode, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#keyPressed(int, int, int)} if the key was not already handled.
		 * Cancel this event when you successfully use the keyboard input to prevent other handlers from using the same input.
		 */
		public static class Post extends KeyboardKeyPressedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, int keyCode, int scanCode, int modifiers) {
				super(gui, keyCode, scanCode, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public abstract static class KeyboardKeyReleasedEvent extends KeyboardKeyEvent {
		// For EventBus
		public KeyboardKeyReleasedEvent() {
		}

		public KeyboardKeyReleasedEvent(Screen gui, int keyCode, int scanCode, int modifiers) {
			super(gui, keyCode, scanCode, modifiers);
		}

		/**
		 * This event fires when keyboard input is detected for a Screen, before it is handled.
		 * Cancel this event to bypass {@link Element#keyReleased(int, int, int)}.
		 */
		public static class Pre extends KeyboardKeyReleasedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, int keyCode, int scanCode, int modifiers) {
				super(gui, keyCode, scanCode, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#keyReleased(int, int, int)} if the key was not already handled.
		 * Cancel this event when you successfully use the keyboard input to prevent other handlers from using the same input.
		 */
		public static class Post extends KeyboardKeyReleasedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, int keyCode, int scanCode, int modifiers) {
				super(gui, keyCode, scanCode, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}

	public static class KeyboardCharTypedEvent extends GuiScreenEvent {
		private final char codePoint;
		private final int modifiers;

		// For EventBus
		public KeyboardCharTypedEvent() {
			this(null, (char) -1, -1);
		}

		public KeyboardCharTypedEvent(Screen gui, char codePoint, int modifiers) {
			super(gui);
			this.codePoint = codePoint;
			this.modifiers = modifiers;
		}

		/**
		 * The code point typed, used for text entry.
		 */
		public char getCodePoint() {
			return codePoint;
		}

		/**
		 * Bit field representing the modifier keys pressed.
		 *
		 * @see GLFW#GLFW_MOD_SHIFT
		 * @see GLFW#GLFW_MOD_CONTROL
		 * @see GLFW#GLFW_MOD_ALT
		 * @see GLFW#GLFW_MOD_SUPER
		 */
		public int getModifiers() {
			return modifiers;
		}

		/**
		 * This event fires when keyboard character input is detected for a {@link Screen}, before it is handled.
		 * Cancel this event to bypass {@link Element#charTyped(char, int)}.
		 */
		public static class Pre extends KeyboardCharTypedEvent {
			// For EventBus
			public Pre() {
			}

			public Pre(Screen gui, char codePoint, int modifiers) {
				super(gui, codePoint, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}

		/**
		 * This event fires after {@link Element#charTyped(char, int)} if the character was not already handled.
		 * Cancel this event when you successfully use the keyboard input to prevent other handlers from using the same input.
		 */
		public static class Post extends KeyboardCharTypedEvent {
			// For EventBus
			public Post() {
			}

			public Post(Screen gui, char codePoint, int modifiers) {
				super(gui, codePoint, modifiers);
			}

			@Override
			public boolean isCancelable() {
				return true;
			}
		}
	}
}
