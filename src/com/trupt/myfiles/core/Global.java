package com.trupt.myfiles.core;

import android.view.ActionMode;

public class Global {
	
	private static ActionMode actionMode;

	public static ActionMode getActionMode() {
		return actionMode;
	}
	
	public static void setActionMode(ActionMode actionMode) {
		Global.actionMode = actionMode;
	}
	
}
