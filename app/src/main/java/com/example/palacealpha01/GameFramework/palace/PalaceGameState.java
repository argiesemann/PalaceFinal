package com.example.palacealpha01.GameFramework.palace;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.palacealpha01.GameFramework.infoMessage.GameState;
//import com.example.palacealpha01.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static android.os.SystemClock.sleep;
import static com.example.palacealpha01.GameFramework.palace.PalaceSurfaceView.cardHeight;
import static com.example.palacealpha01.GameFramework.palace.PalaceSurfaceView.cardWidth;


/**
 * Data representation of a game of Palace for use with the CS301 Game Framework
 * Contains the state of the Palace game.
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 */
public class PalaceGameState extends GameState implements Serializable
{


	//Declare Variables

	public ArrayList<Pair> the_deck;
	private ArrayList<Pair> selectedCards;
	public Stack discardPile;
	private int turn;
	private boolean isChangingPalace;
	private boolean p1CanChangePalace;
	private boolean p2CanChangePalace;
	private boolean discardPileWasBombed;
	private boolean testingP1Palace = false; //change this for debugging issues with playing cards from player one's lower palace


	/**
	 * Default Constructor for PalaceGameState
	 * <p>
	 * Creates a deck of cards, shuffles it and deals it
	 */
	public PalaceGameState()
	{
		the_deck = new ArrayList<>();
		selectedCards = new ArrayList<>();
		discardPile = new Stack();
		initialize_the_deck();
		shuffleTheDeck();
		turn = 0;
		dealTheDeck();
		isChangingPalace = false;
		p1CanChangePalace = true;
		p2CanChangePalace = true;
		discardPileWasBombed = false;
	}//constructor

	/**
	 * Deep Copy Constructor
	 *
	 * @param state The one true state of the game, to be copied
	 */
	public PalaceGameState(PalaceGameState state)
	{
		turn = state.turn;
		the_deck = new ArrayList<>();

		for (Pair p : state.the_deck)
		{
			the_deck.add(new Pair(p));
		}

		selectedCards = new ArrayList<>();
		for (Pair p : state.selectedCards)
		{
			selectedCards.add(new Pair(p));
		}

		discardPile = new Stack(state.discardPile);

		isChangingPalace = state.getIsChangingPalace();
		p1CanChangePalace = state.getP1CanChangePalace();
		p2CanChangePalace = state.getP2CanChangePalace();
		discardPileWasBombed = state.getWasBombed();

	}//deep copy constructor

	public ArrayList<Pair> getSelectedCards()
	{
		return selectedCards;
	}

	/**
	 * initialize_the_deck method:
	 * Creates a deck of 52 Pair objects. All Pair objects have initial location of DRAW_PILE
	 */
	private void initialize_the_deck()
	{
		for (int i = Rank.THREE_INT; i <= Rank.TEN_INT; i++)
		{
			for (int j = Suit.SPADES_INT; j <= Suit.HEARTS_INT; j++)
			{
				the_deck.add(new Pair(new Card(Rank.int_to_rank(i), Suit.int_to_suit(j)), Location.DRAW_PILE));
			}
		}

	}//initialize_the_deck

	/**
	 * shuffleTheDeck method:
	 * Simply shuffles the deck using the shuffle method from Collections
	 */
	public void shuffleTheDeck()
	{
		Collections.shuffle(the_deck);
	}//shuffleTheDeck

	/**
	 * selectCards method:
	 * Adds legal, user-selected cards to the selectedCards ArrayList
	 *
	 * @param playerID         ID of player who called the method
	 * @param userSelectedCard card that user attempted to select
	 *
	 * @return true if a card was successfully selected OR deselected
	 */
	public boolean selectCards(int playerID, Pair userSelectedCard)
	{
		if (isLegal(userSelectedCard))
		{
			if (selectedCards.size() == 0)
			{
				selectedCards.add(userSelectedCard);
				return true;
			}
			//also select the card if the other selected cards are of the same rank
			else if (!selectedCards.contains(userSelectedCard)
					&& userSelectedCard.get_card().get_rank()
					== selectedCards.get(selectedCards.size() - 1).get_card().get_rank())
			{
				selectedCards.add(userSelectedCard);
				return true;
			}
			//deselect a card that is already selected
			else if (selectedCards.contains(userSelectedCard))
			{
				selectedCards.remove(userSelectedCard);
				return true;
			}
			return false;
		}
		return false;
	}//selectCards

	/**
	 * selectPalaceCards method:
	 * Selects cards to be placed into the palace of the player who called the method.
	 * Different from selectCards because any cards can be legally added to the palace.
	 *
	 * @param playerID         ID of player who called the method
	 * @param userSelectedCard card that user attempted to select
	 *
	 * @return true if a card was successfully selected OR deselected
	 */
	public boolean selectPalaceCards(int playerID, Pair userSelectedCard)
	{

		//deselects the card if it is already selected
		if (selectedCards.contains(userSelectedCard))
		{
			selectedCards.remove(userSelectedCard);
			return true;
		}

		//selects a card if there are not already three selected cards
		if (selectedCards.size() < 3)
		{
			selectedCards.add(userSelectedCard);
			return true;
		}

		return false;

	}//selectPalaceCards

	/**
	 * playLowerPalaceCards method:
	 *
	 * Plays a lower palace card immediately without selecting it first and automatically
	 * takes the discard pile if the played card was illegal
	 *
	 * @param playerID player who is playing the card
	 * @param userSelectedCard card that was chosen
	 * @return true if the card is legal OR if the card was illegal and the discard pile was taken
	 */
	public boolean playLowerPalaceCard(int playerID, Pair userSelectedCard)
	{

		if (isLegal(userSelectedCard))
		{
			selectCards(playerID, userSelectedCard);
			playCards(playerID);
			return true;
		}

		else
		{
			if (userSelectedCard.get_location() == Location.PLAYER_ONE_LOWER_PALACE
				&& getPlayerOneHandSize() == 0 && getPlayerOneUpperPalaceSize() == 0)
			{
				selectedCards.add(userSelectedCard);
				playCards(playerID);
				takeDiscardPile(playerID);
				return true;
			}
			else if (userSelectedCard.get_location() == Location.PLAYER_TWO_LOWER_PALACE
					&& getPlayerTwoHandSize() == 0 && getPlayerTwoUpperPalaceSize() == 0)
			{
				selectedCards.add(userSelectedCard);
				playCards(playerID);
				takeDiscardPile(playerID);
				return true;
			}
		}

		return false;
	}

	/**
	 * playCards method:
	 * Places all selected cards into the discard pile. Bombs the discard pile (bombDiscardPile()) if
	 * 4 of a kind are on top of the discard pile or a ten is played.
	 *
	 * @param playerID player who called this method,
	 *
	 * @return true if cards have been selected for play
	 */
	public boolean playCards(int playerID)
	{
		if (isChangingPalace)
		{
			return false;
		}
		if (selectedCards.size() != 0)
		{
			for (int i = 0; i < selectedCards.size(); i++)
			{
				discardPile.push(selectedCards.get(i));

			}
			for (Pair p : the_deck)
			{
				if (selectedCards.contains(p))
				{
					p.set_location(Location.DISCARD_PILE);
				}
			}
			discardPile.peek().set_location(Location.DISCARD_PILE);

			selectedCards.clear();

			//bomb the discard pile if the top four cards are of the same rank OR if the top card is a ten
			if (discardPile.are_next_four_equal()
				|| discardPile.peek().get_card().get_rank() == Rank.TEN)
			{
				bombDiscardPile();
			}

			takeFromDrawPile(playerID);

			if (playerID == 0 && p1CanChangePalace)
				p1CanChangePalace = false;

			else if (playerID == 1 && p2CanChangePalace)
				p2CanChangePalace = false;

			return true;
		}
		return false;
	}//playCards

	/**
	 * takeFromDrawPile method:
	 *
	 * Moves cards from draw pile into the player's hand when 1) they have fewer than five cards and
	 * 2) the draw pile is not empty
	 * @param playerID the player whose hand is getting refilled
	 */
	private void takeFromDrawPile(int playerID)
	{

		int drawPileSize = 0;
		int handSize = 0;

		for (Pair p : the_deck) {

			if (p.get_location() == Location.DRAW_PILE) {
				drawPileSize++;
			}
		}

		if (drawPileSize == 0) {
			return;
		}

		if (playerID == 0) {

			for (Pair p : the_deck) {
				if (p.get_location() == Location.PLAYER_ONE_HAND) {
					handSize++;
				}
			}


			for (Pair p : the_deck) {
				if (p.get_location() == Location.DRAW_PILE) {

					if (handSize >= 5 || drawPileSize == 0) {
						break;
					}

					p.set_location(Location.PLAYER_ONE_HAND);
					drawPileSize--;
					handSize++;
				}
			}

		}

		else if (playerID == 1) {

			for (Pair p : the_deck) {
				if (p.get_location() == Location.PLAYER_TWO_HAND) {
					handSize++;
				}
			}


			for (Pair p : the_deck) {
				if (p.get_location() == Location.DRAW_PILE) {

					if (handSize >= 5 || drawPileSize == 0) {
						break;
					}

					p.set_location(Location.PLAYER_TWO_HAND);
					drawPileSize--;
					handSize++;
				}
			}

		}

	}


	/**
	 * changePalace method:
	 * Places cards from player's upper palace to their hand.
	 *
	 * @param playerID player who called this method
	 *
	 * @return true if called by a valid player
	 */
	public boolean changePalace(int playerID)
	{
		if (playerID == 0)
		{
			if (!p1CanChangePalace) {
				return false;
			}
			isChangingPalace = true;
			for (Pair p : the_deck)
			{
				if (p.get_location() == Location.PLAYER_ONE_UPPER_PALACE)
				{
					p.set_location(Location.PLAYER_ONE_HAND);
				}
			}
			return true;
		}
		if (playerID == 1)
		{
			if (!p2CanChangePalace) {
				return false;
			}
			isChangingPalace = true;
			for (Pair p : the_deck)
			{
				if (p.get_location() == Location.PLAYER_TWO_UPPER_PALACE)
				{
					p.set_location(Location.PLAYER_TWO_HAND);
				}
			}
			return true;
		}
		return false;
	}//changePalace


	/**
	 * confirmPalace method:
	 * places selected cards into the upper palace of the player with playerID
	 *
	 * @param playerID player who called this method
	 *
	 * @return true if called by a valid player and there are three selected cards
	 */
	public boolean confirmPalace(int playerID)
	{
		if (playerID == 0)
		{
			if (selectedCards.size() == 3)
			{

				for (Pair p : the_deck)
				{

					if (p.get_location() == Location.PLAYER_ONE_HAND && selectedCards.contains(p))
					{

						p.set_location(Location.PLAYER_ONE_UPPER_PALACE);

					}
				}
				selectedCards.clear();
				isChangingPalace = false;
				return true;
			}
		}
		else if (playerID == 1)
		{
			if (selectedCards.size() == 3)
			{
				for (Pair p : the_deck)
				{
					if (p.get_location() == Location.PLAYER_TWO_HAND && selectedCards.contains(p))
					{
						p.set_location(Location.PLAYER_TWO_UPPER_PALACE);
					}
				}
				selectedCards.clear();
				isChangingPalace = false;
				return true;
			}
		}
		return false;
	}//confirmPalace

	/**
	 * takeDiscardPile method:
	 * Reassigns location of cards in discard pile to the
	 * player with the PlayerID passed as parameter.
	 *
	 * @param playerID player who called the method
	 *
	 * @return true if called by a valid player
	 */
	public boolean takeDiscardPile(int playerID)
	{

		if (!discardPile.is_empty())
		{

			if (playerID == 0)
			{

				for (Pair p : the_deck)
				{

					if (p.get_location() == Location.DISCARD_PILE)
					{
						p.set_location(Location.PLAYER_ONE_HAND);
					}
				}
				discardPile.clear();
				return true;

			}
			else if (playerID == 1)
			{

				for (Pair p : the_deck)
				{

					if (p.get_location() == Location.DISCARD_PILE)
					{
						p.set_location(Location.PLAYER_TWO_HAND);
					}
				}
				discardPile.clear();
				return true;
			}
		}
		return false;
	}//takeDiscardPile


	/**
	 * dealTheDeck method:
	 * Deals cards from DRAW_PILE to palaces and hands of players
	 */
	public void dealTheDeck()
	{
		if (!testingP1Palace) {
			for (int i = 0; i < 52; i++) {

				if (i < 10) {

					if (i % 2 == 0) {
						the_deck.get(i).set_location(Location.PLAYER_ONE_HAND);
					} else {
						the_deck.get(i).set_location(Location.PLAYER_TWO_HAND);
					}
				}
				if (10 <= i && i < 16) {
					if (i % 2 == 0) {
						the_deck.get(i).set_location(Location.PLAYER_ONE_LOWER_PALACE);
					} else {
						the_deck.get(i).set_location(Location.PLAYER_TWO_LOWER_PALACE);
					}
				}
				if (16 <= i && i < 22) {
					if (i % 2 == 0) {
						the_deck.get(i).set_location(Location.PLAYER_ONE_UPPER_PALACE);
					} else {
						the_deck.get(i).set_location(Location.PLAYER_TWO_UPPER_PALACE);
					}
				}
			}
		}

		else {
			for (int i = 0; i < 52; i++) {

				if (i < 3) {
					the_deck.get(i).set_location(Location.PLAYER_ONE_LOWER_PALACE);
				}
				else if (i < 6) {
					the_deck.get(i).set_location(Location.PLAYER_TWO_LOWER_PALACE);
				}
				else if (i < 9) {
					the_deck.get(i).set_location(Location.PLAYER_TWO_UPPER_PALACE);
				}
				else {
					the_deck.get(i).set_location(Location.PLAYER_TWO_HAND);
				}
			}
		}

	}//dealTheDeck

	/**
	 * isLegal method:
	 * Checks if playing the selected card is legal according to the rules of Palace
	 *
	 * @param selectedCard the card that is selected
	 *
	 * @return true if the move is legal, else false
	 */
	public boolean isLegal(Pair selectedCard)
	{
		if (selectedCard.get_location() == Location.PLAYER_ONE_UPPER_PALACE
			&& getPlayerOneHandSize() > 0) {
			return false;
		}
		if (selectedCard.get_location() == Location.PLAYER_TWO_UPPER_PALACE
			&& getPlayerTwoHandSize() > 0) {
			return false;
		}
		if (selectedCard.get_location() == Location.PLAYER_ONE_LOWER_PALACE
			&& (getPlayerOneHandSize() > 0 || getPlayerOneUpperPalaceSize() > 0)) {
			return false;
		}
		if (selectedCard.get_location() == Location.PLAYER_TWO_LOWER_PALACE
			&& (getPlayerTwoHandSize() > 0 || getPlayerTwoUpperPalaceSize() > 0)) {
			return false;
		}
		if (discardPile.is_empty())
		{
			return true;
		}
		//playing a two or a ten or playing on a two is always legal
		else if (discardPile.peek().get_card().get_rank() == Rank.TWO
				|| selectedCard.get_card().get_rank() == Rank.TWO
				|| selectedCard.get_card().get_rank() == Rank.TEN)
		{
			return true;
		}
		//cards of equal or lower rank are allowed on top of sevens
		else if (discardPile.peek().get_card().get_rank() == Rank.SEVEN
				&& (selectedCard.get_card().get_rank().get_int_value() <= Rank.SEVEN_INT))
		{
			return true;
		}
		//otherwise, a card is only legal if its rank is higher than the top card of the discard pile
		else if (discardPile.peek().get_card().get_rank() != Rank.SEVEN
				&& discardPile.peek().get_card().get_rank().get_int_value()
				<= selectedCard.get_card().get_rank().get_int_value())
		{
			return true;
		}

		return false;
	}//isLegal

	/**
	 * bombDiscardPile method:
	 * Removes the discardPile from play by moving it to the dead pile.
	 */
	private void bombDiscardPile()
	{
		discardPile.clear();
		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.DISCARD_PILE)
			{
				p.set_location(Location.DEAD_PILE);
				Log.i("discard", "card" + p.toString() + "was bombed");
			}
		}
		discardPileWasBombed = true;
	}//bombDiscardPile

	/**
	 * getTurn method:
	 * gets the turn of the player
	 * @return the turn of the user
	 */
	public int getTurn()
	{
		return turn;
	}//getTurn

	/**
	 * setTurn method:
	 * sets the turn
	 * @param newTurn return whos turn it is
	 */
	public void setTurn(int newTurn)
	{
		turn = newTurn;
	}//setTurn

	/**
	 * isDrawPileEmpty method:
	 * checks to see if the draw pile is empty
	 *
	 * @return true is the draw pile is empty
	 */
	public boolean isDrawPileEmpty()
	{
		boolean empty = true;

		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.DRAW_PILE)
			{
				empty = false;
				break;
			}
		}

		return empty;
	}//isDrawPileEmpty

	/**
	 * toString method:
	 * Creates a String representation of the results of the Use Case methods.
	 *
	 * @return A string representation of the result of the Use Case methods.
	 */
	@Override
	public String toString()
	{
		String gameStateString = "";

		gameStateString += "Turn is: " + turn + "\n";

		gameStateString += "Deck contains:\n";

		for (Pair p : the_deck)
		{
			gameStateString += p.toString() + "\n";
		}

		gameStateString += "Discard pile contains:\n";

		gameStateString += discardPile.toString();

/*		for (Pair p : discardPile)
		{
			gameStateString += p.toString() + "\n";
		}
*/
		gameStateString += "Selected cards contains:\n";

		for (Pair p : selectedCards)
		{
			gameStateString += p.toString() + "\n";
		}

		return gameStateString;
	}//toString

	/**
	 * getPairAt method:
	 *
	 * Finds the Pair object (representing a card) that is located at the coordinate specified by
	 * parameters x and y and returns it.
	 *
	 * @param x x-coord of tap
	 * @param y y-coord of tap
	 * @param lowerPalaceLoc location of lower palace (whether it's player one or two)
	 * @return the Pair at the given location
	 */
	public Pair getPairAt(int x, int y, Location lowerPalaceLoc)
	{
		for (Pair p : the_deck)
		{
			if (x > p.getX() && x < p.getX() + cardWidth
				&& y > p.getY() && y < p.getY() + cardHeight)
			{
				if (p.get_location() != lowerPalaceLoc)
				{
					return p;
				}
			}
		}

		for (Pair p : the_deck)
		{
			if (x > p.getX() && x < p.getX() + cardWidth
				&& y > p.getY() && y < p.getY() + cardHeight)
			{
				if (p.get_location() == lowerPalaceLoc)
				{
					return p;
				}
			}
		}

		Pair discardTop = discardPile.peek();
		if (discardTop != null && x > discardTop.getX() && x < discardTop.getX() + cardWidth
			&& y > discardTop.getY() && y < discardTop.getY() + cardHeight)
		{
			return discardTop;
		}
		return null;
	}

	/**
		* getIsChangingPalace method:
		*
		* @return truth value of instance variable: isChangingPalace
	 */
	public boolean getIsChangingPalace () {
	 	return isChangingPalace;
	}

	/**
	 * getP1CanChangePalace method:
	 *
	 * @return truth value of instance variable: p1CanChangePalace
	 */
	public boolean getP1CanChangePalace() {
	 	return p1CanChangePalace;
	}

	/**
	 * getP2CanChangePalace method:
	 *
	 * @return truth value of instance variable: p2CanChangePalace
	 */
	public boolean getP2CanChangePalace() {
		return p2CanChangePalace;
	}

	/**
	 * getPlayerOneHandSize method:
	 *
	 * @return number of cards in player one's hand
	 */
	public int getPlayerOneHandSize()
	{
		int counter = 0;
		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.PLAYER_ONE_HAND)
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * getPlayerTwoHandSize method:
	 *
	 * @return number of cards in player two's hand
	 */
	public int getPlayerTwoHandSize()
	{
		int counter = 0;
		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.PLAYER_TWO_HAND)
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * getPlayerOneUpperPalaceSize method:
	 *
	 * @return number of cards in player one's upper palace
	 */
	public int getPlayerOneUpperPalaceSize()
	{
		int counter = 0;
		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.PLAYER_ONE_UPPER_PALACE)
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * getPlayerTwoUpperPalaceSize method:
	 *
	 * @return number of cards in player two's upper palace
	 */
	public int getPlayerTwoUpperPalaceSize()
	{
		int counter = 0;
		for (Pair p : the_deck)
		{
			if (p.get_location() == Location.PLAYER_TWO_UPPER_PALACE)
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * getWasBombed method:
	 *
	 * @return truth value of instance variable: discardPileWasBombed
	 */
	public boolean getWasBombed()
	{
		return discardPileWasBombed;
	}

	/**
	 * setWasBombed method:
	 *
	 * Sets the truth value of instance variable: discardPileWasBombed
	 * @param b the new boolean value for discardPileWasBombed
	 */
	public void setWasBombed(boolean b) {
		discardPileWasBombed = b;
	}



}//class PalaceGameState
