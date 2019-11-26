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
	private enum Location
	{
		UNKNOWN,
		CCAI,
		PLAYER_ONE,
		PLAYER_TWO,
		DISCARD_PILE,
		DEAD_PILE
	}//END: Location enum

	/**
	 *
	 * @param name
	 */
	public PalaceComputerPlayerCardCountingAI(String name)
	{
		super(name);
	}//END: PalaceComputerPlayerCardCountingAI() constructor

	/**
	 *
	 * @param info
	 */
	@Override
	protected void receiveInfo(GameInfo info)
	{
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
