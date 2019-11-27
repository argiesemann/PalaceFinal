/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;
/*
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
*/
import android.util.Log;

import java.io.Serializable;

/**
 * @author Maximilian
 * <p>
 * This class combines both a suit and rank enumeration to create a card object. These card objects
 * represent the card objects in the PalaceGameState.java class, and in the future we plan to include these
 * card objects in the CardCountingAI.java class. In the furture, we also plan to include a BitmapDrawable
 * variable, which will link the card object to a .PNG file that is an image of the card that the card
 * object is representing, and a draw() method, which will allow the a card object to draw itself using
 * the BitmagImage it is linked with.
 */
public class Card implements Serializable
{
	private Suit suit;
	private Rank rank;
//	private Bitmap image;

	public Card(Rank rank, Suit suit) /*BitmapDrawable image)*/
	{
		this.suit = suit;
		this.rank = rank;
//      this.image = image;
	}//END: Card() constructor

	public Card(Card that)
	{
		this.suit = that.suit;
		this.rank = that.rank;
//		this.image = that.image;
	}//END: Card() copy constructor

/*	public void draw(Canvas c, float x, float y, Paint p)
	{
		c.drawBitmap(this.image, x, y, p);
	}//END: draw() method
*/

	public Rank get_rank()
	{
		return this.rank;
	}//END: get_rank() method

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

	@Override
	public boolean equals(Object obj)
	{
		if (! (obj instanceof Card))
			return false;

		if (this.suit != ((Card) obj).suit)
			return false;
		if (this.rank != ((Card) obj).rank)
			return false;
//		if (! this.image.equals(((Card) obj).image))
		//	return false;

		return true;
	}//END: equals() method

/*	public void setImage(Bitmap image)
	{
		this.image = image;
	}
*/
/*	public Bitmap getImage()
	}//END: setImage() method

	public Bitmap getImage()
	{
		return image;
	}
*/
	}//END: Card Class

