/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GameComputerPlayer;
import com.example.palacealpha01.GameFramework.actionMessage.GameAction;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.ArrayList;

import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_HAND;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_LOWER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_ONE_UPPER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_HAND;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_LOWER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Location.PLAYER_TWO_UPPER_PALACE;
import static com.example.palacealpha01.GameFramework.palace.Rank.JACK_INT;
import static com.example.palacealpha01.GameFramework.palace.Rank.QUEEN_INT;

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
	private boolean started_building_palace;
	private ArrayList<GameAction> action_queue;



	/**
	 * Default constructor for PalaceComputerPlayerSmartAI.java
	 * @param name
	 */
	public PalaceComputerPlayerSmartAI(String name)
	{
		super(name);
		this.are_locations_set       = false;
		this.is_palace_built         = false;
		this.started_building_palace = false;
		this.action_queue = new ArrayList<>();
	}//END: PalaceComputerPlayerSmartAI() constructor

	/**
	 * This method receives a GameInfo object, which can be a PalaceGameState or NotYourTurnInfo object,
	 * and based on the data within the PalaceGameState object, sends a GameAction object to PalaceLocalGame.java,
	 * which is how the SmartAI makes moves in the game.
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

			if (this.action_queue.size() > 0)
			{
				this.send_first_action();
				return;
			}

			if (! this.is_palace_built)
			{
				if (! this.started_building_palace)
				{
					sleep(1);
					this.game.sendAction(new PalaceChangePalaceAction(this));
					this.started_building_palace = true;
					return;
				}

				if (pgs.getSelectedCards().size() > 0)
				{
					sleep(1);
					this.game.sendAction(new PalaceConfirmPalaceAction(this));
					this.is_palace_built = true;
					return;
				}

				Pair[] hand_palace = new Pair[8];
				{
					int i = 0;
					for (Pair p : pgs.the_deck)
						if (p.get_location() == this.my_hand)
							hand_palace[i++] = p;
				}
				merge_sort(hand_palace);

				Pair[] cards_to_be_selected = new Pair[3];
				{
					boolean have_wild_card = false;
					boolean have_high_card = false;
					Rank tmp_rank;
					int j = 0;
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
								default:
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
									have_wild_card = true;
									have_high_card = true;
									continue;
								default:
							}
						}
						if (tmp_rank.get_int_value() < JACK_INT)
							cards_to_be_selected[j++] = hand_palace[i];
					}
				}

				for (Pair p : cards_to_be_selected)
				{
					this.action_queue.add(new PalaceSelectPalaceCardAction(this, p));
				}

				this.send_first_action();
				return;
			}

			// if we've already selected a card,
			// 		then play it
			if (pgs.getSelectedCards().size() > 0)
			{
				sleep(1);
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
			if (has_hand && legal_hand.size() == 0 ||
					! has_hand && has_upper_palace && legal_upper_palace.size() == 0)
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
				sleep(1);
				this.game.sendAction(new PalacePlayLowerPalaceCardAction(this,
						lower_palace.get((int) (Math.random() * lower_palace.size()))));
				return;
			}

			if (has_hand)
			{
				merge_sort(legal_hand);

				Pair smallest_pair = legal_hand.get(0);
				this.action_queue.add(new PalaceSelectCardAction(this, smallest_pair));

				if (smallest_pair.get_card().get_rank().get_int_value() >= QUEEN_INT)
				{
					this.send_first_action();
					return;
				}

				for (int i = 1;
					 i < 4 &&
							 i < legal_hand.size() &&
							 legal_hand.get(i).get_card().get_rank() == smallest_pair.get_card().get_rank();
					 i++)
				{
					this.action_queue.add(new PalaceSelectCardAction(this, legal_hand.get(i)));
				}
				this.send_first_action();
			}
			else
			{
				merge_sort(legal_upper_palace);

				Pair smallest_pair = legal_upper_palace.get(0);
				this.action_queue.add(new PalaceSelectCardAction(this, smallest_pair));

				if (smallest_pair.get_card().get_rank().get_int_value() >= QUEEN_INT)
				{
					this.send_first_action();
					return;
				}

				for (int i = 1;
					 i < 3 &&
							 i < legal_upper_palace.size() &&
							 legal_upper_palace.get(i).get_card().get_rank() == smallest_pair.get_card().get_rank();
					 i++)
					this.action_queue.add(new PalaceSelectCardAction(this, legal_upper_palace.get(i)));
				this.send_first_action();
			}
		}
	}//END: receiveInfo() method



	/**
	 * This method sends the head gameAction, of the action_queue, to PalaceLocalGame.java
	 */
	private void send_first_action()
	{
		sleep(1);
		this.game.sendAction(this.action_queue.get(0));
		this.action_queue.remove(0);
		return;
	}

	/**
	 * This function takes an array of Pair objects, and sorts it based on the integer value of the
	 * Rank enum of the Card object within each Pair object.
	 * @param arr
	 */
	private static void merge_sort(Pair[] arr)
	{
		int l = 0;
		int r = arr.length - 1;
		merge_sort(arr, l, r);
	}//END: merge_sort() function

	/**
	 * This function takes a sub array of 'arr' and sorts it based on the integer value of the Rank
	 * enum of the Card object within each Pair object.
	 * @param arr
	 * @param l
	 * @param r
	 */
	private static void merge_sort(Pair[] arr, int l, int r)
	{
		/* EXTERNAL CITATION
		 *      Date:		2th December 2019
		 * 	    Problem:	The SmartAI's algorithm for determining which cards to pick for its upper
		 * 						palace, or which cards cards to play each turn, was inefficient. I found
		 * 						that if the list of legal cards the SmartAI could choose from was sorted,
		 * 						its algorithm would be much simpler. I chose merge-sort, because it's
		 * 						almost as efficient as quick-sort, and there's a chance the cards could
		 * 						be given to the SmartAI already sorted, which would make quick-sort
		 * 						really inefficient.
		 * 	    Resource:	https://www.geeksforgeeks.org/merge-sort/
		 * 	    Solution:	I read the GeeksForGeeks page on merge-sort, to get the idea of how to implement
		 * 						merge-sort in Java. Some of this code was copied, and the rest was written
		 * 						by me, but most of it was copied.
		 */

		if (l < r          &&
				r < arr.length)
		{
			int m = ((r - l) / 2) + l;

			// Split arr in half, via recursive calls
			merge_sort(arr, l, m);
			merge_sort(arr, m + 1, r);

			// Merge halves back together
			merge(arr, l, m, r);
		}
	}//END: merge_sort() function

	/**
	 * This function takes two sub arrays of 'arr' and merges them together, in order.
	 * @param arr
	 * @param l
	 * @param m
	 * @param r
	 */
	private static void merge(Pair[] arr, int l, int m, int r)
	{
		/* EXTERNAL CITATION
		 *      Date:		2th December 2019
		 * 	    Problem:	The SmartAI's algorithm for determining which cards to pick for its upper
		 * 						palace, or which cards cards to play each turn, was inefficient. I found
		 * 						that if the list of legal cards the SmartAI could choose from was sorted,
		 * 						its algorithm would be much simpler. I chose merge-sort, because it's
		 * 						almost as efficient as quick-sort, and there's a chance the cards could
		 * 						be given to the SmartAI already sorted, which would make quick-sort
		 * 						really inefficient.
		 * 	    Resource:	https://www.geeksforgeeks.org/merge-sort/
		 * 	    Solution:	I read the GeeksForGeeks page on merge-sort, to get the idea of how to implement
		 * 						merge-sort in Java. Some of this code was copied, and the rest was written
		 * 						by me, but most of it was copied.
		 */

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
	}//END: merge() function

	/**
	 * This function takes an ArrayList of Pair objects, and sorts it based on the integer value of
	 * the Rank enum of the Card object within each Pair object.
	 * @param arr_list
	 */
	private static void merge_sort(ArrayList<Pair> arr_list)
	{
		int l = 0;
		int r = arr_list.size() - 1;
		merge_sort(arr_list, l, r);
	}//END: merge_sort() function

	/**
	 * This function takes a sub ArrayList of 'arr_list' and sorts it based on the integer value of
	 * Rank enum of the Card object within each Pair object.
	 * @param arr_list
	 * @param l
	 * @param r
	 */
	private static void merge_sort(ArrayList<Pair> arr_list, int l, int r)
	{
		/* EXTERNAL CITATION
		 *      Date:		2th December 2019
		 * 	    Problem:	The SmartAI's algorithm for determining which cards to pick for its upper
		 * 						palace, or which cards cards to play each turn, was inefficient. I found
		 * 						that if the list of legal cards the SmartAI could choose from was sorted,
		 * 						its algorithm would be much simpler. I chose merge-sort, because it's
		 * 						almost as efficient as quick-sort, and there's a chance the cards could
		 * 						be given to the SmartAI already sorted, which would make quick-sort
		 * 						really inefficient.
		 * 	    Resource:	https://www.geeksforgeeks.org/merge-sort/
		 * 	    Solution:	I read the GeeksForGeeks page on merge-sort, to get the idea of how to implement
		 * 						merge-sort in Java. Some of this code was copied, and the rest was written
		 * 						by me, but most of it was copied.
		 */

		if (l < r               &&
				r < arr_list.size())
		{
			int m = ((r - l) / 2) + l;

			// Split arr in half, via recursive calls
			merge_sort(arr_list, l, m);
			merge_sort(arr_list, m + 1, r);

			// Merge halves back together
			merge(arr_list, l, m, r);
		}
	}//END: merge_sort() function

	/**
	 * This function takes two sub ArrayLists of 'arr_list' and merges them together, in order.
	 * @param arr_list
	 * @param l
	 * @param m
	 * @param r
	 */
	private static void merge(ArrayList<Pair> arr_list, int l, int m, int r)
	{
		/* EXTERNAL CITATION
		 *      Date:		2th December 2019
		 * 	    Problem:	The SmartAI's algorithm for determining which cards to pick for its upper
		 * 						palace, or which cards cards to play each turn, was inefficient. I found
		 * 						that if the list of legal cards the SmartAI could choose from was sorted,
		 * 						its algorithm would be much simpler. I chose merge-sort, because it's
		 * 						almost as efficient as quick-sort, and there's a chance the cards could
		 * 						be given to the SmartAI already sorted, which would make quick-sort
		 * 						really inefficient.
		 * 	    Resource:	https://www.geeksforgeeks.org/merge-sort/
		 * 	    Solution:	I read the GeeksForGeeks page on merge-sort, to get the idea of how to implement
		 * 						merge-sort in Java. Some of this code was copied, and the rest was written
		 * 						by me, but most of it was copied.
		 */

		// Sub-array sizes
		int len_l = (m - l) + 1;
		int len_r = r - m;

		// Temp arrays
		Pair[] L = new Pair[len_l];
		Pair[] R = new Pair[len_r];

		// Populate temp arrays
		for (int i = 0; i < len_l; i++)
			L[i] = arr_list.get(l + i);
		for (int i = 0; i < len_r; i++)
			R[i] = arr_list.get((m + 1) + i);

		// Initial indexes
		int i = 0;	// first sub-array
		int j = 0;	// second sub-array
		int k = l;	// merged array

		// Repopulate arr
		while (i < len_l &&
				j < len_r)
		{
			if (L[i].get_card().get_rank().get_int_value() <= R[j].get_card().get_rank().get_int_value())
				arr_list.set(k++, L[i++]);
			else
				arr_list.set(k++, R[j++]);
		}

		// Copy remaining elements of L[] if any
		while (i < len_l)
			arr_list.set(k++, L[i++]);

		// Copy remaining elements of R[] if any
		while (j < len_r)
			arr_list.set(k++, R[j++]);
	}//END: merge() function
}//END: PalaceComputerPlayerSmartAI class
