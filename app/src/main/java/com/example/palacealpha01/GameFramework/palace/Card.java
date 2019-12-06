/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import android.util.Log;

import java.io.Serializable;

/**
 * @author Maximilian
 * <p>
 * This class combines both a suit and rank enumeration to create a card object. These card objects
 * represent the card objects in the PalaceGameState.java class.
 */
public class Card implements Serializable
{
	private Suit suit;
	private Rank rank;

	/**
	 * Default constructor for Card.java
	 * @param rank
	 * @param suit
	 */
	public Card(Rank rank, Suit suit)
	{
		this.suit = suit;
		this.rank = rank;
	}//END: Card() constructor

	/**
	 * Copy constructor for Card.java
	 * @param that
	 */
	public Card(Card that)
	{
		this.suit = that.suit;
		this.rank = that.rank;
	}//END: Card() copy constructor

	/**
	 *
	 * @return
	 */
	public Rank get_rank()
	{
		return this.rank;
	}//END: get_rank() method

	/**
	 * Returns a String representation of a Card object
	 * @return
	 */
	@Override
	public String toString()
	{
		String return_str = "";
		switch (rank)
		{
			case THREE:
				return_str += "Three";
				break;
			case FOUR:
				return_str += "Four";
				break;
			case FIVE:
				return_str += "Five";
				break;
			case SIX:
				return_str += "Six";
				break;
			case SEVEN:
				return_str += "Seven";
				break;
			case EIGHT:
				return_str += "Eight";
				break;
			case NINE:
				return_str += "Nine";
				break;
			case JACK:
				return_str += "Jack";
				break;
			case QUEEN:
				return_str += "Queen";
				break;
			case KING:
				return_str += "King";
				break;
			case ACE:
				return_str += "Ace";
				break;
			case TWO:
				return_str += "Two";
				break;
			case TEN:
				return_str += "Ten";
				break;

			default:
				Log.d("Card.java", "toString()");
		}
		return_str += " of ";
		switch (suit)
		{
			case SPADES:
				return_str += "Spades";
				break;
			case CLUBS:
				return_str += "Clubs";
				break;
			case DIAMONDS:
				return_str += "Diamonds";
				break;
			case HEARTS:
				return_str += "Hearts";
				break;

			default:
				Log.d("Card.java", "toString()");
		}
		return return_str;
	}//END: toString() method

	/**
	 * Retruns true iff the passed Object (obj) is a Card object, and has identical Suit and Rank enums
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (! (obj instanceof Card))
			return false;

		if (this.suit != ((Card) obj).suit)
			return false;
		if (this.rank != ((Card) obj).rank)
			return false;

		return true;
	}//END: equals() method
}//END: Card Class
