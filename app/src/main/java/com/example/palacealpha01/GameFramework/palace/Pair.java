/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import android.util.Log;

import java.io.Serializable;

/**
 * @author Maximilian
 * @author Andres Giesemann
 *
 * This class combines both a card object and a location enumeration to create a pair object. These
 * pair objects simply represent the both a card and where it is in the game.
 */
public class Pair implements Serializable
{
	private Card card;
	private Location location;
	private int x;
	private int y;

	/**
	 * Default constructor for Pair.java
	 * @param card
	 * @param location
	 */
	public Pair(Card card, Location location)
	{
		this.card = card;
		this.location = location;
		this.x = -1; // initial invalid value
		this.y = -1; // initial invalid value
	}//END: Pair() constructor

	/**
	 * Copy constructor for Pair.java
	 * @param that
	 */
	public Pair(Pair that)
	{
		this.card = new Card(that.card);
		this.location = that.location;
		this.x = that.x;
		this.y = that.y;
	}//END: Pair() copy constructor

	/**
	 *
	 * @return
	 */
	public Card get_card()
	{
		return card;
	}//END: get_card() method

	/**
	 *
	 * @return
	 */
	public Location get_location()
	{
		return location;
	}//END: get_location() method

	/**
	 *
	 * @param location
	 */
	public void set_location(Location location)
	{
		this.location = location;
	}//END: set_location() method

	/**
	 *
	 * @return
	 */
	public int getX()
	{
		return this.x;
	}//END: getY() method

	/**
	 *
	 * @return
	 */
	public int getY()
	{
		return this.y;
	}//END: getY() method

	/**
	 *
	 * @param x
	 */
	public void setX(int x)
	{
		this.x = x;
	}//END: setX() method

	/**
	 *
	 * @param y
	 */
	public void setY(int y)
	{
		this.y = y;
	}//END: setY() method

	/**
	 *
	 * @return
	 */
	@Override
	public String toString()
	{
		String return_str = card.toString() + " in ";

		switch (location)
		{
			case PLAYER_ONE_HAND:
				return_str += "Player one's hand";
				break;
			case PLAYER_TWO_HAND:
				return_str += "Player two's hand";
				break;
			case PLAYER_ONE_UPPER_PALACE:
				return_str += "Player one's upper palace";
				break;
			case PLAYER_TWO_UPPER_PALACE:
				return_str += "Player two's upper palace";
				break;
			case PLAYER_ONE_LOWER_PALACE:
				return_str += "Player one's lower palace";
				break;
			case PLAYER_TWO_LOWER_PALACE:
				return_str += "Player two's lower palace";
				break;
			case DEAD_PILE:
				return_str += "Dead pile";
				break;
			case DISCARD_PILE:
				return_str += "Discard pile";
				break;
			case DRAW_PILE:
				return_str += "Draw pile";
				break;
			default:
				Log.d("Pair.java", "toString()");
				return null;
		}

		return return_str;
	}//END: toString() method

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (! (obj instanceof Pair))
			return false;

		if (! this.card.equals(((Pair) obj).get_card()))
			return false;
		if (this.location != ((Pair) obj).location)
			return false;

		return true;
	}//END: equals() method
}//END: Pair class
