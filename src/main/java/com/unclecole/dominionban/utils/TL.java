package com.unclecole.dominionban.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;
import java.util.logging.Level;

public enum TL {
	NO_PERMISSION("messages.no-permission", "&cYou don't have the permission to do that."),
	INVALID_ARGUMENT_NUMBER("messages.invalid-number", "&c&lERROR: &c'<argument>' has to be a number"),
	INVALID_ARGUMENT_PLAYER("messages.invalid-number", "&c&lERROR: &c'<user>' has to be a Player"),
	INVALID_COMMAND_USAGE("messages.invalid-command-usage", "&cIncorrect Usage: &7<command>"),
	PLAYER_ONLY("messages.player-only", "&cThis command is for players only!"),
	SUCCESSFULLY_BANNED("messages.successfully-banned", "&a&lSUCCESS! &fSuccessfully banned %player%!"),
	SUCCESSFULLY_UNBANNED("messages.successfully-unbanned", "&a&lSUCCESS! &fSuccessfully unbanned %player%!");

	private final String path;

	private String def;

	TL(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}

	public void setDefault(String message) {
		this.def = message;
	}

	public void send(CommandSender sender) {
			sender.sendMessage(C.color(getDefault()));
	}

	public void send(CommandSender sender, PlaceHolder... placeHolders) {
			sender.sendMessage(C.color(getDefault(), placeHolders));
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(C.color(message));
	}

	public static void message(CommandSender sender, String message, PlaceHolder... placeHolders) {
		sender.sendMessage(C.color(message, placeHolders));
	}

	public static void message(CommandSender sender, List<String> message) {
		message.forEach(m -> sender.sendMessage(C.color(m)));
	}

	public static void message(CommandSender sender, List<String> message, PlaceHolder... placeHolders) {
		message.forEach(m -> sender.sendMessage(C.color(m, placeHolders)));
	}

	public static void log(Level lvl, String message) {
		ProxyServer.getInstance().getLogger().log(lvl, message);
	}
}
