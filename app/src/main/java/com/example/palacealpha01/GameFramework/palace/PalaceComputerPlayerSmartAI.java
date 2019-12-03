/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GameComputerPlayer;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.ArrayList;

import static com.example.palacealpha01.GameFramework.palace.Rank.QUEEN_INT;
import static com.example.palacealpha01.GameFramework.palace.Rank.TEN_INT;

/**
 * @author Maximilian
 */
public class PalaceComputerPlayerSmartAI extends GameComputerPlayer
{
	private Location my_hand;
	private Location my_upper_palace;
	private Location my_lower_palace;
	private boolean are_locations_set;

	/**
	 *
	 * @param name
	 */
	public PalaceComputerPlayerSmartAI(String name)
	{
		super(name);
		this.are_locations_set = false;
	}//END: PalaceComputerPlayerSmartAI() constructor

	/**
	 *
	 * @param info
	 */
	@Override
	protected void receiveInfo(GameInfo info)
	{
		if (! this.are_locations_set)
		{
			if (this.playerNum == 0)
			{
				this.my_hand         = Location.PLAYER_ONE_HAND;
				this.my_upper_palace = Location.PLAYER_ONE_UPPER_PALACE;
				this.my_lower_palace = Location.PLAYER_ONE_LOWER_PALACE;
			}
			else if (this.playerNum == 1)
			{
				this.my_hand         = Location.PLAYER_TWO_HAND;
				this.my_upper_palace = Location.PLAYER_TWO_UPPER_PALACE;
				this.my_lower_palace = Location.PLAYER_TWO_LOWER_PALACE;
			}
			this.are_locations_set = true;
		}

		if (info instanceof NotYourTurnInfo)
			return;

		if (info instanceof PalaceGameState &&
			((PalaceGameState) info).getTurn() == this.playerNum)
		{
			info.setGame(game);
			PalaceGameState pgs = (PalaceGameState) info;

			// TODO LIST:
			//		implement Smart AI's ability to play multiple legal cards at a time ???
			//			maybe a good idea, or maybe a better strategy to not
			//		implement Smart AI's ability to modify its upper palace at game start

			// if we've already selected a card,
			// 		then play it
			if (pgs.getSelectedCards().size() > 0)
			{
				sleep(2);
				game.sendAction(new PalacePlayCardAction(this));
				return;
			}

			ArrayList<Pair> legal_hand 		   = new ArrayList<>();
			ArrayList<Pair> legal_upper_palace = new ArrayList<>();
			ArrayList<Pair> lower_palace       = new ArrayList<>();
			boolean has_hand         = false;
			boolean has_upper_palace = false;

			for (Pair p : pgs.the_deck)
			{
				Location tmp = p.get_location();
				if (tmp == my_hand)
				{
					has_hand = true;
					if (pgs.isLegal(p))
						legal_hand.add(p);
				}
				else if (tmp == my_upper_palace)
				{
					has_upper_palace = true;
					if (has_hand)
						legal_upper_palace.add(p);
					else if (pgs.isLegal(p))
						legal_upper_palace.add(p);
				}
				else if (tmp == my_lower_palace)
					lower_palace.add(p);
			}

			// if we have cards in our hand, but none are playable, or have cards in our upper palace,
			// but none are playable,
			//		then take the discard pile
			if (has_hand         && legal_hand.size()         == 0 ||
				has_upper_palace && legal_upper_palace.size() == 0)
			{
				sleep(2);
				game.sendAction(new PalaceTakeDiscardPileAction(this));
				return;
			}

			// if we don't have cards in neither our hand, nor our upper palace,
			//		then randomly select are card from our lower palace
			if (! has_hand         &&
				! has_upper_palace)
			{
				game.sendAction(new PalaceSelectCardAction(this,
						lower_palace.get((int) (Math.random() * lower_palace.size()))));
				return;
			}

			if (has_hand)
			{
				Pair smallest_pair = null;
				int smallest_rank = TEN_INT + 1;
				int pair_int;
				for (Pair pair : legal_hand)
				{
					pair_int = pair.get_card().get_rank().get_int_value();
					if (pair_int < smallest_rank)
					{
						smallest_pair = pair;
						smallest_rank = pair_int;
					}
				}

				// if the smallest card is a Queen or Higher,
				// 		then play it
				if (smallest_rank >= QUEEN_INT)
				{
					game.sendAction(new PalaceSelectCardAction(this, smallest_pair));
					return;
				}

				Pair[] cards_to_be_selected = new Pair[4];
				int ind = 0;
				for (Pair pair : legal_hand)
					if (pair.get_card().get_rank().get_int_value() == smallest_rank)
						cards_to_be_selected[ind++] = pair;

				for (Pair pair : cards_to_be_selected)
					if (pair != null)
						game.sendAction(new PalaceSelectCardAction(this, pair));
			}
			else
			{
				Pair smallest_pair = null;
				int smallest_rank = TEN_INT + 1;
				int pair_int;
				for (Pair pair : legal_upper_palace)
				{
					pair_int = pair.get_card().get_rank().get_int_value();
					if (pair_int < smallest_rank)
					{
						smallest_pair = pair;
						smallest_rank = pair_int;
					}
				}

				// if the smallest card is a Queen or Higher,
				// 		then play it
				if (smallest_rank >= QUEEN_INT)
				{
					game.sendAction(new PalaceSelectCardAction(this, smallest_pair));
					return;
				}

				Pair[] cards_to_be_selected = new Pair[3];
				int ind = 0;
				for (Pair pair : legal_upper_palace)
					if (pair.get_card().get_rank().get_int_value() == smallest_rank)
						cards_to_be_selected[ind++] = pair;

				for (Pair pair : cards_to_be_selected)
					if (pair != null)
						game.sendAction(new PalaceSelectCardAction(this, pair));
			}
		}
	}//END: receiveInfo() method
}//END: PalaceComputerPlayerSmartAI class
