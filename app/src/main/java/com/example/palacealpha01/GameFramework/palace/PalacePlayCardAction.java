package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

/**
 * An Action in which the player can play their selected card
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */
public class PalacePlayCardAction extends GameAction implements Serializable
{
	/**
	 * constructor for GameAction
	 *
	 * @param player the player who created the action
	 */
	public PalacePlayCardAction(GamePlayer player)
	{
		super(player);

	}//PalacePlayCardAction

}//class PalacePlayCardAction
