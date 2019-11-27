package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

/**
 * An Action in which the Change Palace Action is implemented
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */
public class PalaceChangePalaceAction extends GameAction implements Serializable
{
	/**
	 * constructor for GameAction
	 *
	 * @param player the player who created the action
	 */
	public PalaceChangePalaceAction(GamePlayer player)
	{
		super(player);
	}//PalaceChangePalaceAction
}//class PalaceChangePalaceAction