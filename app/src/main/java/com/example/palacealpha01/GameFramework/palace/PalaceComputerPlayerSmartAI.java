/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import android.util.Log;

import com.example.palacealpha01.GameFramework.GameComputerPlayer;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.ArrayList;

import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_HAND;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_LOWER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_UPPER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_HAND;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_LOWER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_UPPER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Rank.JACK_INT;import static com.example.palacealpha01.GameFramework.palace.Rank.QUEEN_INT;
import static com.example.palacealpha01.GameFramework.palace.Rank.TEN;import static com.example.palacealpha01.GameFramework.palace.Rank.TEN_INT;import static com.example.palacealpha01.GameFramework.palace.Rank.TWO;

/**
 * @author Maximilian
 */
public class PalaceComputerPlayerSmartAI extends GameComputerPlayer
{
	private Location my_hand;
	private Location my_upper_palace;
	private Location my_lower_palace;
	private boolean are_locations_set;
	private boolean is_palace_built;

	/**
	 *
	 * @param name
	 */
	public PalaceComputerPlayerSmartAI(String name)
	{
		super(name);
		this.are_locations_set = false;
		this.is_palace_built   = false;
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
				this.my_hand         = PLAYER_ONE_HAND;
				this.my_upper_palace = PLAYER_ONE_UPPER_PALACE;
				this.my_lower_palace = PLAYER_ONE_LOWER_PALACE;
			}
			else if (this.playerNum == 1)
			{
				this.my_hand         = PLAYER_TWO_HAND;
				this.my_upper_palace = PLAYER_TWO_UPPER_PALACE;
				this.my_lower_palace = PLAYER_TWO_LOWER_PALACE;
			}
			this.are_locations_set = true;
		}

		if (info instanceof NotYourTurnInfo)
			return;

		if (info instanceof PalaceGameState &&
			((PalaceGameState) info).getTurn() == this.playerNum)
		{
			info.setGame(this.game);
			PalaceGameState pgs = (PalaceGameState) info;

			// TODO LIST:
			//		implement Smart AI's ability to modify its upper palace at game start

			if (! this.is_palace_built)
			{
				if (pgs.getSelectedCards().size() > 0)
				{
					this.game.sendAction(new PalaceConfirmPalaceAction(this));
					this.is_palace_built = true;
					return;
				}

				this.game.sendAction(new PalaceChangePalaceAction(this));

				Pair[] hand_palace = new Pair[8];
				{
					int i = 0;
					for (Pair p : pgs.the_deck)
						if (p.get_location() == this.my_hand)
							hand_palace[i++] = p;
				}
				merge_sort(hand_palace, 0, hand_palace.length - 1);

				Pair[] cards_to_be_selected = new Pair[3];
				int j = 0;
				boolean have_wild_card = false;
				boolean have_high_card = false;
				Rank tmp_rank;
				for (int i = hand_palace.length - 1; i >= 0 && j < cards_to_be_selected.length; i--)
				{
					tmp_rank = hand_palace[i].get_card().get_rank();
					if (! have_wild_card)
					{
						switch (tmp_rank)
						{
							case TWO:
							case TEN:
								cards_to_be_selected[j++] = hand_palace[i];
								have_wild_card = true;
								continue;
						}
					}
					if (! have_high_card)
					{
						switch (tmp_rank)
						{
							case JACK:
							case QUEEN:
							case KING:
							case ACE:
								cards_to_be_selected[j++] = hand_palace[i];
								have_high_card = true;
								continue;
						}
					}
					else if (tmp_rank.get_int_value() < JACK_INT)
						cards_to_be_selected[j++] = hand_palace[i];
				}

				for (Pair p : cards_to_be_selected)
					this.game.sendAction(new PalaceSelectCardAction(this, p));

				return;
			}

			// if we've already selected a card,
			// 		then play it
			if (pgs.getSelectedCards().size() > 0)
			{
				sleep(2);
				this.game.sendAction(new PalacePlayCardAction(this));
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
				if (tmp == this.my_hand)
				{
					has_hand = true;
					if (pgs.isLegal(p))
						legal_hand.add(p);
				}
				else if (tmp == this.my_upper_palace)
				{
					has_upper_palace = true;
					if (has_hand)
						legal_upper_palace.add(p);
					else if (pgs.isLegal(p))
						legal_upper_palace.add(p);
				}
				else if (tmp == this.my_lower_palace)
					lower_palace.add(p);
			}

			// if we have cards in our hand, but none are playable, or have cards in our upper palace,
			// but none are playable,
			//		then take the discard pile
			if (has_hand         && legal_hand.size()         == 0 ||
				has_upper_palace && legal_upper_palace.size() == 0)
			{
				sleep(2);
				this.game.sendAction(new PalaceTakeDiscardPileAction(this));
				return;
			}

			// if we don't have cards in neither our hand, nor our upper palace,
			//		then randomly select are card from our lower palace
			if (! has_hand         &&
				! has_upper_palace)
			{
				this.game.sendAction(new PalaceSelectCardAction(this,
						lower_palace.get((int) (Math.random() * lower_palace.size()))));
				return;
			}

			if (has_hand)
			{
				Pair smallest_pair = null;
				int smallest_rank = TEN_INT + 1;
				int p_int;
				for (Pair p : legal_hand)
				{
					p_int = p.get_card().get_rank().get_int_value();
					if (p_int < smallest_rank)
					{
						smallest_pair = p;
						smallest_rank = p_int;
					}
				}

				// if the smallest card is a Queen or Higher,
				// 		then play it
				if (smallest_rank >= QUEEN_INT)
				{
					this.game.sendAction(new PalaceSelectCardAction(this, smallest_pair));
					return;
				}

				Pair[] cards_to_be_selected = new Pair[4];
				int i = 0;
				for (Pair p : legal_hand)
					if (p.get_card().get_rank().get_int_value() == smallest_rank)
						cards_to_be_selected[i++] = p;

				for (Pair pair : cards_to_be_selected)
					if (pair != null)
						this.game.sendAction(new PalaceSelectCardAction(this, pair));
			}
			else
			{
				Pair smallest_pair = null;
				int smallest_rank = TEN_INT + 1;
				int p_int;
				for (Pair p : legal_upper_palace)
				{
					p_int = p.get_card().get_rank().get_int_value();
					if (p_int < smallest_rank)
					{
						smallest_pair = p;
						smallest_rank = p_int;
					}
				}

				// if the smallest card is a Queen or Higher,
				// 		then play it
				if (smallest_rank >= QUEEN_INT)
				{
					this.game.sendAction(new PalaceSelectCardAction(this, smallest_pair));
					return;
				}

				Pair[] cards_to_be_selected = new Pair[3];
				int i = 0;
				for (Pair p : legal_upper_palace)
					if (p.get_card().get_rank().get_int_value() == smallest_rank)
						cards_to_be_selected[i++] = p;

				for (Pair p : cards_to_be_selected)
					if (p != null)
						this.game.sendAction(new PalaceSelectCardAction(this, p));
			}
		}
	}//END: receiveInfo() method

	/**
	 *
	 * @param arr
	 * @param l
	 * @param r
	 */
	private static void merge_sort(Pair[] arr, int l, int r)
	{
		if (l >= r)
		{
			Log.d("SmartAI Class", "ERROR in merge_sort(): 'l' is >= 'r'");
			return;
		}
		if (r >= arr.length)
		{
			Log.d("SmartAI Class", "ERROR in merge_sort(): 'r' is >= arr.length");
			return;
		}

		int m = (l + r) / 2;

		// Split arr in half, via recursive calls
		merge_sort(arr, l, m);
		merge_sort(arr, m + 1, r);

		// Merge halves back together
		merge(arr, l, m, r);
	}//END: merge_sort() method

	/**
	 *
	 * @param arr
	 * @param l
	 * @param m
	 * @param r
	 */
	private static void merge(Pair[] arr, int l, int m, int r)
	{
		// Sub-array sizes
		int len_l = (m - l) + 1;
		int len_r = r - m;

		// Temp arrays
		Pair[] L = new Pair[len_l];
		Pair[] R = new Pair[len_r];

		// Populate temp arrays
		for (int i = 0; i < len_l; i++)
			L[i] = arr[l + i];
		for (int i = 0; i < len_r; i++)
			R[i] = arr[(m + 1) + i];

		// Initial indexes
		int i = 0;	// first sub-array
		int j = 0;	// second sub-array
		int k = l;	// merged array

		// Repopulate arr
		while (i < len_l &&
			   j < len_r)
		{
			if (L[i].get_card().get_rank().get_int_value() <= R[j].get_card().get_rank().get_int_value())
				arr[k++] = L[i++];
			else
				arr[k++] = R[j++];
		}

		// Copy remaining elements of L[] if any
		while (i < len_l)
			arr[k++] = L[i++];

		// Copy remaining elements of R[] if any
		while (j < len_r)
			arr[k++] = R[j++];
	}//END: merge() method
}//END: PalaceComputerPlayerSmartAI class
