package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.io.Serializable;

/**
 * An Action in which the Play Lower Palace Card Action is implemented
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */
public class PalacePlayLowerPalaceCardAction extends GameAction implements Serializable
{

    private Pair userSelectedCard;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PalacePlayLowerPalaceCardAction(GamePlayer player, Pair userSelectedCard)
    {
        super(player);
        this.userSelectedCard = userSelectedCard;
    }//PalacePlayLowerPalaceCardAction

    public Pair getUserSelectedCard() {
        return userSelectedCard;
    }
}//class PalacePlayLowerPalaceCardAction