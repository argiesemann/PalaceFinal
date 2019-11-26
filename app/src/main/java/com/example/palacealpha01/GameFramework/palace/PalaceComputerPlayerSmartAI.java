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
public class PalaceComputerPlayerSmartAI extends GameComputerPlayer
{
	/**
	 *
	 * @param name
	 */
	public PalaceComputerPlayerSmartAI(String name)
	{
		super(name);
	}//END: PalaceComputerPlayerSmartAI() constructor

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
	}//END: receiveInfo() method
}//END: PalaceComputerPlayerSmartAI class
