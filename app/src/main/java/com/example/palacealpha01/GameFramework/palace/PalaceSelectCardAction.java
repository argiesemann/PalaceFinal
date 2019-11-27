package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

/**
 * An Action in which the user selects the card that they want to play
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */
public class PalaceSelectCardAction extends GameAction implements Serializable
{
	private Pair userSelectedCard;

	/**
	 * constructor for GameAction
	 *
	 * @param player the player who created the action
	 */
	public PalaceSelectCardAction(GamePlayer player, Pair userSelectedCard)
	{
		super(player);
		this.userSelectedCard = userSelectedCard;

	}//PalaceSelectCardAction

	/**
	 * getUserSelectedCard method:
	 * gets the card that the user selected
	 * @return the Pair object of the card selected
	 */
	public Pair getUserSelectedCard()
	{
		return userSelectedCard;
	}//getUserSelectedCard

}//class PalaceSelectCardAction
