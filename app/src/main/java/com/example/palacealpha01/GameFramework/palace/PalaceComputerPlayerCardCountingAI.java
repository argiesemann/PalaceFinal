/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import com.example.palacealpha01.GameFramework.GameComputerPlayer;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

/**
 * @author Maximilian
 */
public class PalaceComputerPlayerCardCountingAI extends GameComputerPlayer
{
	/**
	 * @author Maximilian
	 */
	private enum CCAILocation
	{
		UNKNOWN,
		CCAI,
		PLAYER_ONE,
		PLAYER_TWO,
		DISCARD_PILE,
		DEAD_PILE
	}//END: Location enum

	private Location my_hand;
	private Location my_upper_palace;
	private Location my_lower_palace;
	private boolean are_locations_set;

	/**
	 *
	 * @param name
	 */
	public PalaceComputerPlayerCardCountingAI(String name)
	{
		super(name);
		this.are_locations_set = false;
	}//END: PalaceComputerPlayerCardCountingAI() constructor

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

		if (info instanceof PalaceGameState)
		{
			info.setGame(game);
			PalaceGameState pgs = (PalaceGameState) info;

			// TODO: complete this method
			//pgs.doSomeStuff()
		}
	}
}//END: PalaceComputerPlayerCardCountingAI class
