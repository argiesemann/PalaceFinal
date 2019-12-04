/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import android.util.Log;

import java.io.Serializable;

/**
 * @author Maximilian
 * <p>
 * This class is an enumeration for the Card.java class. It represents the suit of a card object. The
 * integers associated with the various rank enumerations allow us to easily compare whether a card
 * is higher in rank than another card. TWO & TEN have integer values unequal to their number value,
 * because they are wild cards, making them the highest ranked in the game.
 */
public enum Rank implements Serializable
{
	THREE  (3),
	FOUR   (4),
	FIVE   (5),
	SIX    (6),
	SEVEN  (7),
	EIGHT  (8),
	NINE   (9),
	JACK  (10),
	QUEEN (11),
	KING  (12),
	ACE   (13),
	TWO   (14),
	TEN   (15);

	public static final int THREE_INT =  3;
	public static final int FOUR_INT  =  4;
	public static final int FIVE_INT  =  5;
	public static final int SIX_INT   =  6;
	public static final int SEVEN_INT =  7;
	public static final int EIGHT_INT =  8;
	public static final int NINE_INT  =  9;
	public static final int JACK_INT  = 10;
	public static final int QUEEN_INT = 11;
	public static final int KING_INT  = 12;
	public static final int ACE_INT   = 13;
	public static final int TWO_INT   = 14;
	public static final int TEN_INT   = 15;

	private int int_value;

	Rank(int int_value)
	{
		this.int_value = int_value;
	}//END: Rank() enum constructor


	public int get_int_value()
	{
		return this.int_value;
	}//END: get_int_value() method

	public static Rank int_to_rank(int num)
	{
		switch (num)
		{
			case THREE_INT: return THREE;
			case FOUR_INT:  return FOUR;
			case FIVE_INT:  return FIVE;
			case SIX_INT:   return SIX;
			case SEVEN_INT: return SEVEN;
			case EIGHT_INT: return EIGHT;
			case NINE_INT:  return NINE;
			case JACK_INT:  return JACK;
			case QUEEN_INT: return QUEEN;
			case KING_INT:  return KING;
			case ACE_INT:   return ACE;
			case TWO_INT:   return TWO;
			case TEN_INT:   return TEN;

			default:
				Log.d("Rank.java", "int_to_rank()");
				return null;
		}
	}//END: int_to_rank() function
}//END: Rank enum
