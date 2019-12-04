package com.example.palacealpha01.GameFramework.palace;

import android.util.Log;

import com.example.palacealpha01.GameFramework.GamePlayer;
import com.example.palacealpha01.GameFramework.LocalGame;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;

import java.util.ArrayList;

/**
 * PalaceLocalGame Class
 * Returns the new local GameState
 * checks if the game is over
 */
public class PalaceLocalGame extends LocalGame
{

	//the game's state
	protected PalaceGameState pgs;
	private ArrayList<Pair> selectedCards = new ArrayList<>();

	/**
	 * Constructor for PalaceLocalGame
	 */
	public PalaceLocalGame()
	{
		super();

		pgs = new PalaceGameState();
	}//PalaceLocalGame


	/**
	 * sendUpdatedStateTo method:
	 * Notify the given player that its state has changed.
	 * This should involve sending a GameInfo object to the player.
	 *
	 * @param p
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p)
	{
		p.sendInfo(new PalaceGameState(pgs));
	}//sendUpdatedStateTo

	/**
	 * canMove method:
	 * Tell whether the given player is allowed to make a move
	 * at the present point in the game.
	 *
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 *      true iff the player is allowed to move
	 */
	@Override
	protected boolean canMove(int playerIdx)
	{
		return playerIdx == pgs.getTurn();
	}//canMove

	//

	/**
	 * checkIfGameOver method:
	 * checks the player's/computer's hand, lower palace and upper palacep for cards.
	 * If no cards are found then the game is over and the player without cards wins.
	 * @return
	 */
	@Override
	protected String checkIfGameOver()
	{
		boolean playerOneWin = true;
		boolean playerTwoWin = true;
		for (Pair p : pgs.the_deck) {
			if (p.get_location() == Location.PLAYER_ONE_HAND || p.get_location() == Location.PLAYER_ONE_UPPER_PALACE || p.get_location() == Location.PLAYER_ONE_LOWER_PALACE) {
				playerOneWin = false;
			}
			else if (p.get_location() == Location.PLAYER_TWO_HAND || p.get_location() == Location.PLAYER_TWO_UPPER_PALACE || p.get_location() == Location.PLAYER_TWO_LOWER_PALACE) {
				playerTwoWin = false;
			}
		}

		if (playerOneWin) {
			return playerNames[0] + " is the winner ";
		}

		if (playerTwoWin) {
			return playerNames[1] + " is the winner ";
		}

		return null;
		/*int p0counter = 0;
		int p1counter = 0;

		for (Pair p : pgs.the_deck)
		{
			if (p.get_location() == Location.PLAYER_ONE_LOWER_PALACE)
			{
				p0counter++;
			}
		}


		for (Pair p : pgs.the_deck)
		{
			if (p.get_location() == Location.PLAYER_TWO_LOWER_PALACE)
			{
				p1counter++;
			}
		}

		for (Pair p : pgs.the_deck)
		{
			if (p.get_location() == Location.PLAYER_ONE_HAND)
			{
				p0counter++;
			}
		}

		for (Pair p : pgs.the_deck)
		{
			if (p.get_location() == Location.PLAYER_TWO_HAND)
			{
				p1counter++;
			}
		}

		if (p0counter == 0)
		{
			return playerNames[0] + " is the winner ";
		}
		else if (p1counter == 0)
		{
			return playerNames[1] + " is the winner ";
		}
		else
		{

			return null;
		} */
	}//checkIfGameOver

	/**
	 * makeMove method:
	 * Makes the move of the player
	 *
	 * @param action
	 * 			The move that the player has sent to the game
	 * @return
	 *      tells whether the move was a legal one
	 */
	@Override
	protected boolean makeMove(GameAction action)
	{

		GamePlayer p = action.getPlayer();
		int playerNum = 0;

		//finds the playernum to the active player
		for (int i = 0; i < players.length; i++)
		{
			if (players[i] == p)
			{
				playerNum = i;
			}
		}
		//if it is not the player's turn return false
		if (pgs.getTurn() != playerNum)
		{
			return false;
		}

		pgs.setWasBombed(false);

		//if action is a selectCardAction, perform this action
		if (action instanceof PalaceSelectCardAction)
		{

			if (canMove(playerNum))
			{
				Pair pair = ((PalaceSelectCardAction) action).getUserSelectedCard();

				if (pgs.getTurn() == 0)
				{

					pgs.selectCards(0, pair);

					return true;
				}
				else
				{
					pgs.selectCards(1, pair);
					return true;
				}

			}

		}
		//if action is a playCardAction, perform this move
		else if (action instanceof PalacePlayCardAction)
		{
			if (canMove(playerNum) && pgs.getSelectedCards().size() > 0)
			{

				if (pgs.getTurn() == 0)
				{
					pgs.playCards(0);

					//If this playCardAction causes the deck to be bombed, the player still has their turn
					if (!pgs.discardPile.is_empty()) {
                        pgs.setTurn(1);
                    }

					return true;
				}
				else
				{
					pgs.playCards(1);

                    //If this playCardAction causes the deck to be bombed, the player still has their turn
                    if (!pgs.discardPile.is_empty()) {
                        pgs.setTurn(0);
                    }

					return true;
				}

			}
		}
		//if action is takeDiscardPile, perform this move
		else if (action instanceof PalaceTakeDiscardPileAction)
		{
			if (canMove(playerNum))
			{
				if (pgs.getTurn() == 0)
				{
					pgs.takeDiscardPile(0);
					pgs.setTurn(1);
					return true;
				}

				else
				{
					pgs.takeDiscardPile(1);
					pgs.setTurn(0);
					return true;
				}
			}
		}
		else if (action instanceof PalaceConfirmPalaceAction)
		{
			if (canMove(playerNum))
			{
				if (pgs.getTurn() == 0)
				{
					pgs.confirmPalace(0);
					return true;
				}
				else
				{
					pgs.confirmPalace(1);
					return true;
				}
			}
		}

		//if action is of changepalaceaction, perform this action
		else if (action instanceof PalaceChangePalaceAction)
		{
			int turn = pgs.getTurn();
			if (turn == 0)
			{
				pgs.changePalace(0);
				return true;
			}
			else
			{
				pgs.changePalace(1);
				return true;
			}
		}
		else if (action instanceof PalaceSelectPalaceCardAction) {

			if (canMove(playerNum))
			{
				Pair pair = ((PalaceSelectPalaceCardAction) action).getUserSelectedCard();

				if (pgs.getTurn() == 0)
				{

					pgs.selectPalaceCards(0, pair);

					return true;
				}
				else
				{
					pgs.selectPalaceCards(1, pair);
					return true;
				}

			}

		}

		else if (action instanceof PalacePlayLowerPalaceCardAction) {

			if (canMove(playerNum))
			{
				Pair pair = ((PalacePlayLowerPalaceCardAction) action).getUserSelectedCard();

				if (pgs.getTurn() == 0)
				{
					pgs.playLowerPalaceCard(0, pair);
					if (pair.get_location() != Location.PLAYER_ONE_LOWER_PALACE) {
						pgs.setTurn(1);
					}
					return true;
				}
				else
				{
					pgs.playLowerPalaceCard(1, pair);
					if (pair.get_location() != Location.PLAYER_TWO_LOWER_PALACE) {
						pgs.setTurn(0);
					}
					return true;
				}

			}

		}

		return false;
	}//makeMove
}//class PalaceLocalGame
