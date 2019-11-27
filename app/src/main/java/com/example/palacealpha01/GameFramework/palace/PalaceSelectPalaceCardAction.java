package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

/**
 * An Action in which the user selects the cards they want in their upper palace
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */
public class PalaceSelectPalaceCardAction extends GameAction implements Serializable
{
	private Pair userSelectedCard;

	/**
	 * constructor for GameAction
	 *
	 * @param player the player who created the action
	 */
	public PalaceSelectPalaceCardAction(GamePlayer player, Pair userSelectedCard)
	{
		super(player);
		this.userSelectedCard = userSelectedCard;
	}//PalaceSelectPalaceCardAction

	/**
	 * getUserSelectedCard method:
	 * gets the card that the user selected
	 * @return
	 */
	public Pair getUserSelectedCard()
	{
		return userSelectedCard;
	}//getUserSelectedCard
}//class PalaceSelectPalaceCardAction
