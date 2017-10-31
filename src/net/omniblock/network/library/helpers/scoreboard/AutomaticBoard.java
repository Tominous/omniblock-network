/*
 *  TheXTeam - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package net.omniblock.network.library.helpers.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class AutomaticBoard extends UpdatableBoard implements Runnable {
	private int taskId, delay;

	public AutomaticBoard(int delay) {
		this.taskId = -1;
		this.delay = delay;
	}

	public void start(Plugin plugin) {
		this.taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, this.delay).getTaskId();
	}

	public void stop() {
		if (this.taskId != -1) {
			Bukkit.getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
		}
	}

	@Override
	public void run() {
		this.update();
	}

}
