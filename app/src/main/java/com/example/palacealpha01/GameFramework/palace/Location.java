/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import java.io.Serializable;

/**
 * @author Maximilian
 * <p>
 * This class is an enumeration for the Pair.java class. It represents a card object's location in the
 * game.
 */
public enum Location implements Serializable
{
	PLAYER_ONE_HAND,
	PLAYER_ONE_UPPER_PALACE,
	PLAYER_ONE_LOWER_PALACE,
	PLAYER_TWO_HAND,
	PLAYER_TWO_UPPER_PALACE,
	PLAYER_TWO_LOWER_PALACE,
	DRAW_PILE,
	DISCARD_PILE,
	DEAD_PILE
}//END: Location enum
