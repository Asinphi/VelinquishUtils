package net.velinquish.utils;

import net.velinquish.utils.lang.LangManager;

public interface VelinquishPlugin {
	static VelinquishPlugin getInstance() {
		return null;
	}
	LangManager getLangManager();
	String getPermission();
	String getPrefix();
}
