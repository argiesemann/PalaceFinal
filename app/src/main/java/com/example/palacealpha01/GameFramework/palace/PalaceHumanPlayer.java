package com.example.palacealpha01.GameFramework.palace;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.palacealpha01.GameFramework.GameHumanPlayer;
import com.example.palacealpha01.GameFramework.GameMainActivity;
import com.example.palacealpha01.GameFramework.infoMessage.GameInfo;
import com.example.palacealpha01.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.palacealpha01.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.Hashtable;

/**
 * This is the class that hold the Human Player for Palace
 *
 * @author Andres Giesemann, Fredrik Olsson, Meredith Marcinko, Maximilian Puglielli
 * @version November 2019
 */

//needs to handle screen interaction (implement listeners)
public class PalaceHumanPlayer extends GameHumanPlayer implements View.OnClickListener, View.OnTouchListener
{

	/**
	 * Initialize variables
	 */
	private Activity myActivity;
	private PalaceSurfaceView palaceSurfaceView;
	private int layoutId;
	private Location handLoc;
	private Location upLoc;
	private Location lowLoc;
	//the pictures are stored in a hashmap and are initialized in the initCardImages-method.
	private Hashtable<String, Bitmap> pictures = new Hashtable<>();
	private PalaceGameState pgs;
	private Toast toast;

    private Pair tappedCard;
    private int lastTapX;
	private int lastTapY;






	/**
	 * The contructor for the PalaceHumanPlayer
	 *
	 * @param name
	 *      the player's name
	 * @param layoutId
	 *      the id of the layout to use
	 */
	public PalaceHumanPlayer(String name, int layoutId)
	{
		super(name);
		this.layoutId = layoutId;
		lastTapX = 0;
		lastTapY = 0;
		tappedCard = null;
		handLoc = (this.playerNum == 0)? Location.PLAYER_ONE_HAND : Location.PLAYER_TWO_HAND;
		upLoc = (this.playerNum == 0)? Location.PLAYER_ONE_UPPER_PALACE : Location.PLAYER_TWO_UPPER_PALACE;
		lowLoc = (this.playerNum == 0)? Location.PLAYER_ONE_LOWER_PALACE : Location.PLAYER_TWO_LOWER_PALACE;

	}//PalaceHumanPlayer

	/**
	 * initCardImages Method:
	 * the cards are initialed in this class in order to avoid nullpointerexceptions
	 */
	private void initCardImages()
	{
		Resources resources = myActivity.getApplicationContext().getResources();

		//threes
		pictures.put("Three of Spades", BitmapFactory.decodeResource(resources, R.drawable.three_of_spades));
		pictures.put("Three of Clubs", BitmapFactory.decodeResource(resources, R.drawable.three_of_clubs));
		pictures.put("Three of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.three_of_diamonds));
		pictures.put("Three of Hearts", BitmapFactory.decodeResource(resources, R.drawable.three_of_hearts));
		//Fours
		pictures.put("Four of Spades", BitmapFactory.decodeResource(resources, R.drawable.four_of_spades));
		pictures.put("Four of Clubs", BitmapFactory.decodeResource(resources, R.drawable.four_of_clubs));
		pictures.put("Four of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.four_of_diamonds));
		pictures.put("Four of Hearts", BitmapFactory.decodeResource(resources, R.drawable.four_of_hearts));
		//fives
		pictures.put("Five of Spades", BitmapFactory.decodeResource(resources, R.drawable.five_of_spades));
		pictures.put("Five of Clubs", BitmapFactory.decodeResource(resources, R.drawable.five_of_clubs));
		pictures.put("Five of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.five_of_diamonds));
		pictures.put("Five of Hearts", BitmapFactory.decodeResource(resources, R.drawable.five_of_hearts));
		//sixes
		pictures.put("Six of Spades", BitmapFactory.decodeResource(resources, R.drawable.six_of_spades));
		pictures.put("Six of Clubs", BitmapFactory.decodeResource(resources, R.drawable.six_of_clubs));
		pictures.put("Six of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.six_of_diamonds));
		pictures.put("Six of Hearts", BitmapFactory.decodeResource(resources, R.drawable.six_of_hearts));
		//sevens
		pictures.put("Seven of Spades", BitmapFactory.decodeResource(resources, R.drawable.seven_of_spades));
		pictures.put("Seven of Clubs", BitmapFactory.decodeResource(resources, R.drawable.seven_of_clubs));
		pictures.put("Seven of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.seven_of_diamonds));
		pictures.put("Seven of Hearts", BitmapFactory.decodeResource(resources, R.drawable.seven_of_hearts));
		//Eights
		pictures.put("Eight of Spades", BitmapFactory.decodeResource(resources, R.drawable.eight_of_spades));
		pictures.put("Eight of Clubs", BitmapFactory.decodeResource(resources, R.drawable.eight_of_clubs));
		pictures.put("Eight of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.eight_of_diamonds));
		pictures.put("Eight of Hearts", BitmapFactory.decodeResource(resources, R.drawable.eight_of_hearts));
		//Nines
		pictures.put("Nine of Spades", BitmapFactory.decodeResource(resources, R.drawable.nine_of_spades));
		pictures.put("Nine of Clubs", BitmapFactory.decodeResource(resources, R.drawable.nine_of_clubs));
		pictures.put("Nine of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.nine_of_diamonds));
		pictures.put("Nine of Hearts", BitmapFactory.decodeResource(resources, R.drawable.nine_of_hearts));
		//jacks
		pictures.put("Jack of Spades", BitmapFactory.decodeResource(resources, R.drawable.jack_of_spades));
		pictures.put("Jack of Clubs", BitmapFactory.decodeResource(resources, R.drawable.jack_of_clubs));
		pictures.put("Jack of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.jack_of_diamonds));
		pictures.put("Jack of Hearts", BitmapFactory.decodeResource(resources, R.drawable.jack_of_hearts));
		//queens
		pictures.put("Queen of Clubs", BitmapFactory.decodeResource(resources, R.drawable.queen_of_clubs));
		pictures.put("Queen of Spades", BitmapFactory.decodeResource(resources, R.drawable.queen_of_spades));
		pictures.put("Queen of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.queen_of_diamonds));
		pictures.put("Queen of Hearts", BitmapFactory.decodeResource(resources, R.drawable.queen_of_hearts));
		//Kings
		pictures.put("King of Spades", BitmapFactory.decodeResource(resources, R.drawable.king_of_spades));
		pictures.put("King of Clubs", BitmapFactory.decodeResource(resources, R.drawable.king_of_clubs));
		pictures.put("King of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.king_of_diamonds));
		pictures.put("King of Hearts", BitmapFactory.decodeResource(resources, R.drawable.king_of_hearts));
		//Ace
		pictures.put("Ace of Spades", BitmapFactory.decodeResource(resources, R.drawable.ace_of_spades));
		pictures.put("Ace of Clubs", BitmapFactory.decodeResource(resources, R.drawable.ace_of_clubs));
		pictures.put("Ace of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.ace_of_diamonds));
		pictures.put("Ace of Hearts", BitmapFactory.decodeResource(resources, R.drawable.ace_of_hearts));
		//twos
		pictures.put("Two of Spades", BitmapFactory.decodeResource(resources, R.drawable.two_of_spades));
		pictures.put("Two of Clubs", BitmapFactory.decodeResource(resources, R.drawable.two_of_clubs));
		pictures.put("Two of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.two_of_diamonds));
		pictures.put("Two of Hearts", BitmapFactory.decodeResource(resources, R.drawable.two_of_hearts));
		//tens
		pictures.put("Ten of Spades", BitmapFactory.decodeResource(resources, R.drawable.ten_of_spades));
		pictures.put("Ten of Clubs", BitmapFactory.decodeResource(resources, R.drawable.ten_of_clubs));
		pictures.put("Ten of Diamonds", BitmapFactory.decodeResource(resources, R.drawable.ten_of_diamonds));
		pictures.put("Ten of Hearts", BitmapFactory.decodeResource(resources, R.drawable.ten_of_hearts));

	}//initCardImages

	/**
	 * initAfterReady Method:
	 * sets up all the buttons and surfaceview
	 */
	protected void initAfterReady()
	{
		Button leftButton = myActivity.findViewById(R.id.leftButton);
		leftButton.setOnClickListener(this);
		Button rightButton = myActivity.findViewById(R.id.rightButton);
		rightButton.setOnClickListener(this);
		Button palaceButton = myActivity.findViewById(R.id.PalaceButton);
		palaceButton.setOnClickListener(this);
		Button playCardButton = myActivity.findViewById(R.id.playCardButton);
		playCardButton.setOnClickListener(this);
		Button confirmPalace = myActivity.findViewById(R.id.confirmPalace);
		confirmPalace.setOnClickListener(this);
		palaceSurfaceView.setGame(game);

	}//initAfterReady


	/**
	 * getTopView method:
	 * returns the surfaceView
	 *
	 * @return null
	 */
	@Override
	public View getTopView()
	{
		return myActivity.findViewById(R.id.TableSurfaceView);
	}//getTopView

	/**
	 * recieveInfo method:
	 * receives info from the gameFrameWork and sets the GameState
	 * if the GameState is not null
	 *
	 * @param info
	 */
	@Override
	public void receiveInfo(GameInfo info)
	{

		if (palaceSurfaceView == null)
			return;

		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo)
		{

		}
		else if (!(info instanceof PalaceGameState))
		{
			return;
		}
		else
		{
			palaceSurfaceView.setPgs((PalaceGameState) info);
			pgs = (PalaceGameState) info;
			palaceSurfaceView.invalidate();
		}

	}//receiveInfo

	/**
	 * setAsGui method:
	 * can access mainActivity from this class for resources
	 *
	 * @param activity
	 */
	@Override
	public void setAsGui(GameMainActivity activity)
	{

		myActivity = activity;
		initCardImages();

		//load the layout resource for the new config
		activity.setContentView(R.layout.palace_activity_main);

		palaceSurfaceView = (PalaceSurfaceView) myActivity.findViewById(R.id.TableSurfaceView);

		//sets on the listener for the surfaceview
		palaceSurfaceView.setOnTouchListener(this);



		//sets up the pictures stored in the hashmap
		palaceSurfaceView.setPictures(pictures);


		palaceSurfaceView.setHumanPlayer(this);

		//  palaceSurfaceView.setHumanPlayer(this);
		palaceSurfaceView.setActivity(myActivity);

		toast = Toast.makeText(myActivity.getApplicationContext(), "You can no longer change your palace!", Toast.LENGTH_SHORT);
	}

	/**
	 * onClick method:
	 * the listeners do not need to know about the gameState,
	 * they are speficially for the human player to use
	 *
	 * @param button
	 */
	@Override
	public void onClick(View button)
	{
		if (button.getId() == R.id.confirmPalace)
		{
			PalaceConfirmPalaceAction confirmPalace = new PalaceConfirmPalaceAction(this);
			game.sendAction(confirmPalace);
			button.invalidate();
		}
		else if (button.getId() == R.id.PalaceButton)
		{
			if (pgs != null && !pgs.getP1CanChangePalace()) {
				toast.show();
			}
			PalaceChangePalaceAction changePalace = new PalaceChangePalaceAction(this);
			game.sendAction(changePalace);
			button.invalidate();
		}
		else if(button.getId()== R.id.leftButton){
			palaceSurfaceView.setOffset(-1);

		}
		else if(button.getId() == R.id.rightButton){
			palaceSurfaceView.setOffset(1);
		}
		else if (button.getId() == R.id.playCardButton)
		{
			for (Pair p : pgs.getSelectedCards())
			{
				if (pgs.getSelectedCards().contains(p))
				{
					PalacePlayCardAction playCardAction = new PalacePlayCardAction(this);
					game.sendAction(playCardAction);
					button.invalidate();
				}
			}
		}
		palaceSurfaceView.invalidate();

	}//onClick


	/**
	 * onTouch method:
	 * handles the taps on the different cards
	 * @param v
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    lastTapX = (int) event.getX();
		    lastTapY = (int) event.getY();
			tappedCard = pgs.getPairAt(lastTapX, lastTapY);
			return true;
		}

		else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (tappedCard == null) {
                return false;
            }

            if (tappedCard.get_location() == handLoc) {
                if (pgs.getIsChangingPalace()) {
                    game.sendAction(new PalaceSelectPalaceCardAction(this, tappedCard));
                } else {
                    if (lastTapY - 50 > event.getY()) {
                    	if (pgs.getSelectedCards().isEmpty()) {
							game.sendAction(new PalaceSelectCardAction(this, tappedCard));
						}

						game.sendAction(new PalacePlayCardAction(this));
                    }
                    else {
                        game.sendAction(new PalaceSelectCardAction(this, tappedCard));
                    }
                }
            } else if (tappedCard.get_location() == upLoc) {
                if (lastTapY - 50 > event.getY()) {
					if (pgs.getSelectedCards().isEmpty()) {
						game.sendAction(new PalaceSelectCardAction(this, tappedCard));
					}
					if (!pgs.getSelectedCards().isEmpty()) {
						game.sendAction(new PalacePlayCardAction(this));
					}
                }
                else {
                    game.sendAction(new PalaceSelectCardAction(this, tappedCard));
                }
            } else if (tappedCard.get_location() == lowLoc) {
                game.sendAction(new PalacePlayLowerPalaceCardAction(this, tappedCard));
            } else if (tappedCard.get_location() == Location.DISCARD_PILE) {
                game.sendAction(new PalaceTakeDiscardPileAction(this));
            }
        }




		v.invalidate();
		return true;
	}
}//class PalaceHumanPlayer
