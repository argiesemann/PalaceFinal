package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

public class PalaceTakeDiscardPileAction extends GameAction implements Serializable
{
	/**
	 * constructor for GameAction
	 *
	 * @param player the player who created the action
	 */
	public PalaceTakeDiscardPileAction(GamePlayer player)
	{
		super(player);
	}//PalaceTakeDiscardPileAction
}//class PalaceTakeDiscardPileAction

