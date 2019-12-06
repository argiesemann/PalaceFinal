package com.example.palacealpha01.GameFramework.palace;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
public class PalaceHumanPlayer extends GameHumanPlayer implements View.OnClickListener, View.OnTouchListener
{

	private Activity myActivity;
	private PalaceSurfaceView palaceSurfaceView;
	private PalaceGameState pgs;

	private int layoutId;

	private TextView helpText;

	private Button leftButton;
	private Button rightButton;
	private Button playCardButton;
	private Button palaceButton;
	private Button confirmPalace;

	private Location handLoc;
	private Location upLoc;
	private Location lowLoc;

	//the pictures are stored in a hashmap and are initialized in the initCardImages-method.
	private Hashtable<String, Bitmap> pictures;

	private Toast toast;
	private Toast toastBomb;
	private MediaPlayer bomber;

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
		pictures = new Hashtable<>();


	}//PalaceHumanPlayer


	/**
	 External Citation

	 Date: 4 October 2019

	 Problem: Could not get the game to boot up without having a NULLPOINTEREXCEPTION.

	 Resource: Andrew Nuxoll

	 Solution: He helped us set up a Hashtable for the Bitmaps which in the SurfaceView are then connected
	 to a Pair Object using that Object's toString-method.
	 * */

	/**
	 * initCardImages Method:
	 * the cards are initialized in this class in order to avoid nullpointerexceptions
	 * Card objects are assigned a bitmap in PalaceSurfaceView
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
	 * sets up all the layout elements (buttons, textViews, surfaceView)
	 */
	protected void initAfterReady()
	{
		leftButton = myActivity.findViewById(R.id.leftButton);
		leftButton.setOnClickListener(this);
		if (this.playerNum == 1)
		{
			leftButton.setY(leftButton.getY() - 7*palaceSurfaceView.getHeight()/10);
		}

		rightButton = myActivity.findViewById(R.id.rightButton);
		rightButton.setOnClickListener(this);
		if (this.playerNum == 1)
		{
			rightButton.setY(rightButton.getY() - 7*palaceSurfaceView.getHeight()/10);
		}

		palaceButton = myActivity.findViewById(R.id.PalaceButton);
		palaceButton.setOnClickListener(this);

		playCardButton = myActivity.findViewById(R.id.playCardButton);
		playCardButton.setOnClickListener(this);

		confirmPalace = myActivity.findViewById(R.id.confirmPalace);
		confirmPalace.setOnClickListener(this);

		Button helpButton = myActivity.findViewById(R.id.helpButton);
		helpButton.setOnClickListener(this);

		helpText = myActivity.findViewById(R.id.helpText);
		helpText.setText(R.string.manual_text);
		helpText.setBackgroundColor(Color.WHITE);
		helpText.setTextColor(Color.BLACK);
		helpText.setGravity(Gravity.TOP);
		helpText.setEnabled(false);
		helpText.setVisibility(View.INVISIBLE);


		/**
		 External Citation

		 Date: 3 December 2019

		 Problem: We did not know how to incorporate a Sound Effect into our game

		 Resource: https://abhiandroid.com/androidstudio/add-audio-android-studio.html

		 Solution: We used the example from this page
		 * */

		bomber = MediaPlayer.create(myActivity.getApplicationContext(), R.raw.bomb_discard_pile_sfx);

		palaceSurfaceView.setGame(game);
		handLoc = (this.playerNum == 0)? Location.PLAYER_ONE_HAND : Location.PLAYER_TWO_HAND;
		upLoc = (this.playerNum == 0)? Location.PLAYER_ONE_UPPER_PALACE : Location.PLAYER_TWO_UPPER_PALACE;
		lowLoc = (this.playerNum == 0)? Location.PLAYER_ONE_LOWER_PALACE : Location.PLAYER_TWO_LOWER_PALACE;

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
			if (pgs.getWasBombed())
			{
				toastBomb.show();
				bomber.start();
			}
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

		toast = Toast.makeText(myActivity.getApplicationContext(),
				"You can no longer change your palace!", Toast.LENGTH_SHORT);

		toastBomb = Toast.makeText(myActivity.getApplicationContext(),
				"Discard pile was BOMBED", Toast.LENGTH_SHORT);
	}

	/**
	 * onClick method:
	 *
	 * Handles any button presses and takes action depending on which button was pressed
	 *
	 * @param button button that was pressed
	 */
	@Override
	public void onClick(View button)
	{
		//confirm palace button
		if (button.getId() == R.id.confirmPalace)
		{
			PalaceConfirmPalaceAction confirmPalace = new PalaceConfirmPalaceAction(this);
			game.sendAction(confirmPalace);
			button.invalidate();
		}

		//change palace button
		else if (button.getId() == R.id.PalaceButton)
		{
			if (pgs != null && playerNum == 0 && !pgs.getP1CanChangePalace())
			{
				toast.show();
			}
			else if (pgs != null && playerNum == 1 && !pgs.getP2CanChangePalace())
			{
				toast.show();
			}
			PalaceChangePalaceAction changePalace = new PalaceChangePalaceAction(this);
			game.sendAction(changePalace);
			button.invalidate();
		}

		//scroll hand left button
		else if(button.getId()== R.id.leftButton)
		{
			palaceSurfaceView.setOffset(-1);

		}

		//scroll hand right button
		else if(button.getId() == R.id.rightButton)
		{
			palaceSurfaceView.setOffset(1);
		}

		//play card button
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

		//help button
		else if (button.getId() == R.id.helpButton)
		{
			if (helpText.getVisibility() == View.INVISIBLE)
			{
				helpText.setVisibility(View.VISIBLE);
				leftButton.setEnabled(false);
				rightButton.setEnabled(false);
				playCardButton.setEnabled(false);
				palaceButton.setEnabled(false);
				confirmPalace.setEnabled(false);
			}
			else if (helpText.getVisibility() == View.VISIBLE)
			{
				helpText.setVisibility(View.INVISIBLE);
				leftButton.setEnabled(true);
				rightButton.setEnabled(true);
				playCardButton.setEnabled(true);
				palaceButton.setEnabled(true);
				confirmPalace.setEnabled(true);
			}
		}
		palaceSurfaceView.invalidate();

	}//onClick


	/**
	 * onTouch method:
	 *
	 * handles taps on the surface view
	 * @param v the view that was tapped (always PalaceSurfaceView)
	 * @param event type of tap
	 * @return true if a card was tapped
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
		    lastTapX = (int) event.getX();
		    lastTapY = (int) event.getY();
			tappedCard = pgs.getPairAt(lastTapX, lastTapY, lowLoc);
			return true;
		}

		else if (event.getAction() == MotionEvent.ACTION_UP)
		{

            if (tappedCard == null)
            {
                return false;
            }

            if (tappedCard.get_location() == handLoc)
            {
                if (pgs.getIsChangingPalace())
                {
                    game.sendAction(new PalaceSelectPalaceCardAction(this, tappedCard));
                }
                else
				{
                    if (isSwipe(lastTapY, event))
                    {
                    	if (pgs.getSelectedCards().isEmpty())
                    	{
							game.sendAction(new PalaceSelectCardAction(this, tappedCard));
						}

						game.sendAction(new PalacePlayCardAction(this));
                    }
                    else
					{
                        game.sendAction(new PalaceSelectCardAction(this, tappedCard));
                    }
                }
            }
            else if (tappedCard.get_location() == upLoc)
            {
				if (isSwipe(lastTapY, event))
				{
					if (pgs.getSelectedCards().isEmpty())
					{
						game.sendAction(new PalaceSelectCardAction(this, tappedCard));
					}

					game.sendAction(new PalacePlayCardAction(this));
				}
				else
				{
					game.sendAction(new PalaceSelectCardAction(this, tappedCard));
				}
            }
            else if (tappedCard.get_location() == lowLoc)
            {
                game.sendAction(new PalacePlayLowerPalaceCardAction(this, tappedCard));
            }
            else if (tappedCard.get_location() == Location.DISCARD_PILE)
            {
                game.sendAction(new PalaceTakeDiscardPileAction(this));
            }
        }

		v.invalidate();
		return true;
	}

	/**
	 * isSwipe method:
	 *
	 * determines whether the user swiped the screen by checking the distance between the
	 * beginning and end of their contact with the screen
	 *
	 * @param lastTapY y-coord of the beginning of their touch
	 * @param event ACTION_UP event from the end of a touch
	 * @return true if the screen was swiped
	 */
	private boolean isSwipe(int lastTapY, MotionEvent event)
	{
		if (this.playerNum == 0)
		{
			return lastTapY - 50 > event.getY();
		}
		return lastTapY + 50 < event.getY();
	}

	/**
	 * getPlayerNum method:
	 *
	 * @return int value of instance variable: playerNum
	 */
	public int getPlayerNum() {
		return playerNum;
	}
}//class PalaceHumanPlayer
