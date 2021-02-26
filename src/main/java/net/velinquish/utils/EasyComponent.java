package net.velinquish.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class EasyComponent {

	private BaseComponent current;
	private final List<BaseComponent> parts = new ArrayList<>();

	public EasyComponent(EasyComponent original) {
		current = original.current.duplicate();

		for (final BaseComponent baseComponent : original.parts)
			parts.add(baseComponent.duplicate());
	}

	public EasyComponent(String text) {
		current = new TextComponent(TextComponent.fromLegacyText(Common.colorize(text)));
	}

	public EasyComponent(BaseComponent component) {
		current = component.duplicate();
	}

	public EasyComponent append(BaseComponent component) {
		return append(component, FormatRetention.ALL);
	}

	public EasyComponent append(BaseComponent component, FormatRetention retention) {
		parts.add(current);

		final BaseComponent previous = current;
		current = component.duplicate();
		current.copyFormatting(previous, retention, false);
		return this;
	}

	public EasyComponent append(BaseComponent[] components) {
		return append(components, FormatRetention.ALL);
	}

	public EasyComponent append(BaseComponent[] components, FormatRetention retention) {
		Preconditions.checkArgument(components.length != 0, "No components to append");

		final BaseComponent previous = current;
		for (final BaseComponent component : components) {
			parts.add(current);

			current = component.duplicate();
			current.copyFormatting(previous, retention, false);
		}

		return this;
	}

	public EasyComponent append(String text) {
		return append(text, FormatRetention.FORMATTING);
	}

	public EasyComponent append(String text, FormatRetention retention) {
		parts.add(current);

		final BaseComponent old = current;
		current = new TextComponent(TextComponent.fromLegacyText(Common.colorize(text)));
		if ( retention == FormatRetention.EVENTS || retention == FormatRetention.ALL )
		{
			if ( current.getClickEvent() == null )
				current.setClickEvent( old.getClickEvent() );
			if ( current.getHoverEvent() == null )
				current.setHoverEvent( old.getHoverEvent() );
		}
		//current.copyFormatting(old, Format.retention, false) kept giving noSuchMethod...
		//So I just copied the copyFormatting() method from the bungee api github :P
		if ( retention == FormatRetention.FORMATTING || retention == FormatRetention.ALL )
		{
			if ( current.getColor() == null )
				current.setColor( old.getColorRaw() );
			if ( Boolean.valueOf(current.isBold()) == null )
				current.setBold( old.isBoldRaw() );
			if ( Boolean.valueOf(current.isItalic()) == null )
				current.setItalic( old.isItalicRaw() );
			if ( Boolean.valueOf(current.isUnderlined()) == null )
				current.setUnderlined( old.isUnderlinedRaw() );
			if ( Boolean.valueOf(current.isStrikethrough()) == null )
				current.setStrikethrough( old.isStrikethroughRaw() );
			if ( Boolean.valueOf(current.isObfuscated()) == null )
				current.setObfuscated( old.isObfuscatedRaw() );
			if ( current.getInsertion() == null )
				current.setInsertion( old.getInsertion() );
		}

		return this;
	}

	public EasyComponent onClickRunCmd(String text) {
		if (text == null)
			return this;
		return onClick(Action.RUN_COMMAND, text);
	}

	public EasyComponent onClickSuggestCmd(String text) {
		if (text == null)
			return this;
		return onClick(Action.SUGGEST_COMMAND, text);
	}

	public EasyComponent onClick(Action action, String text) {
		current.setClickEvent(new ClickEvent(action, Common.colorize(text)));
		return this;
	}

	public EasyComponent onHover(String text) {
		if (text == null)
			return this;
		return onHover(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, text);
	}

	public EasyComponent onHover(net.md_5.bungee.api.chat.HoverEvent.Action action, String text) {
		if (text == null)
			return this;
		current.setHoverEvent(new HoverEvent(action, TextComponent.fromLegacyText(Common.colorize(text))));

		return this;
	}

	public EasyComponent retain(FormatRetention retention) {
		current.retain(retention);
		return this;
	}

	public BaseComponent[] create() {
		final BaseComponent[] result = parts.toArray(new BaseComponent[parts.size() + 1]);
		result[parts.size()] = current;

		return result;
	}

	public void send(Player... players) {
		final BaseComponent[] comp = create();

		for (final Player player : players)
			player.spigot().sendMessage(comp);
	}

	public static EasyComponent builder(String text) {
		return new EasyComponent(text);
	}
}
