package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GameComputerPlayer;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.ArrayList;

/**
 * This is the Computer Player class for Palace
 * The Computer Player is implemented here
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */

public class PalaceComputerPlayer extends GameComputerPlayer
{

	private Location handLoc;
	private Location upLoc;
	private Location lowLoc;
	private boolean locationsAreSet = false;
	/**
	 * PalaceComputerPlayer method:
	 * The PalaceComputerPlayer is exteneded from the GameComputerPlayer Class
	 * @param name
	 */
	public PalaceComputerPlayer(String name)
	{
		super(name);

	}//PalaceComputerPlayer

	/**
	 *receiveInfo method:
	 *
	 * @param info
	 */
	@Override
	protected void receiveInfo(GameInfo info)
	{
		if (!locationsAreSet) {
			handLoc = (this.playerNum == 0) ? Location.PLAYER_ONE_HAND : Location.PLAYER_TWO_HAND;
			upLoc = (this.playerNum == 0) ? Location.PLAYER_ONE_UPPER_PALACE : Location.PLAYER_TWO_UPPER_PALACE;
			lowLoc = (this.playerNum == 0) ? Location.PLAYER_ONE_LOWER_PALACE : Location.PLAYER_TWO_LOWER_PALACE;
			locationsAreSet = true;
		}
		if (info instanceof NotYourTurnInfo)
			return;

		if (info instanceof PalaceGameState)
		{
			info.setGame(game);
			PalaceGameState pgs = (PalaceGameState) info;

			if (pgs.getTurn() == this.playerNum)
			{


				if (pgs.getSelectedCards().size() > 0)
				{
					sleep(2);
					game.sendAction(new PalacePlayCardAction(this));
					return;
				}

		//game.sendAction(new PalaceSkipTurn(this));

				//used for temporarily storing the cards in computer player's hand and palace
				ArrayList<Pair> player_two_hand = new ArrayList<>();
				//stores the legal cards, if there are any, if the size of this array is 0,
				//then computer player picks up the discard pile
				ArrayList<Pair> legalCards = new ArrayList<>();

				//if there are any cards in computer player's hand, add them to player_two_hand
				for (Pair p : pgs.the_deck)
					if (p.get_location() == handLoc)
					{
						player_two_hand.add(p);
						//if any of these cards are legal, add them to legalCards arraylist.
						if(pgs.isLegal(p)){
							legalCards.add(p);
						}
					}
				//repeats above procedure on upper palace cards, if computer's hand is empty
				if (player_two_hand.size() == 0)
					for (Pair p : pgs.the_deck)
						if (p.get_location() == upLoc)
						{
							player_two_hand.add(p);
							if(pgs.isLegal(p)){
								legalCards.add(p);
							}
						}
				//repeats above process on lower palace cards if computer's hand and upper palace is empty
				if (player_two_hand.size() == 0)
					for (Pair p : pgs.the_deck)
						if (p.get_location() == lowLoc)
						{
							player_two_hand.add(p);
							if(pgs.isLegal(p)){
								legalCards.add(p);
							}
						}

				//if there are cards in the computer's hand, or hand is empty but there are cards
				// in upper palace but none of them are legal or there are cards in lower palace
				// but none of them are legal.
				//then the computer picks up the discard pile.
				if(legalCards.size()==0){
					sleep(2);
					game.sendAction(new PalaceTakeDiscardPileAction(this));
				}
				else if (player_two_hand.size() > 0)
				{
					Pair selected_pair = player_two_hand.get((int) (Math.random() * player_two_hand.size()));
					game.sendAction(new PalaceSelectCardAction(this, selected_pair));
					return;
				}


				game.sendAction(new PalaceTakeDiscardPileAction(this));
			}
		}
	}//receiveInfo
}//class PalaceComputerPlayer
