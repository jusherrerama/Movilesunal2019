package edu.harding.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver;

    // Various text displayed
    private TextView mInfoTextView;
    private TextView mCountHumanTextView;
    private TextView mCountTiesTextView;
    private TextView mCountAndroidTextView;

    // Declare BoardView class
    private BoardView mBoardView;

    // Declare Media Player
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;

    private boolean turn;

    // Declare OnTouchListener class
    private View.OnTouchListener mTouchListener = new View.OnTouchListener( ){

        public boolean onTouch( View v, MotionEvent event ){

            if( !turn )
                return false;

            // Determine which cell was touched
            int col = (int) event.getX( ) / mBoardView.getBoardCellWidth( );
            int row = (int) event.getY( ) / mBoardView.getBoardCellHeight( );
            int pos = row * 3 + col;

            if( !mGameOver && setMove( TicTacToeGame.HUMAN_PLAYER, pos ) ){


                // If no winner yet, let the computer make a move
                mHumanMediaPlayer.start( );

                int winner = mGame.checkForWinner( );
                if( winner == 0 ){
                    turn = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mComputerMove( );
                            mBoardView.invalidate( );
                            winner();
                        }
                    }, 1000);
                }else{
                    winner();
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    // Set up the game board.
    private void startNewGame( ){

        mGame.clearBoard();
        mGameOver = false;
        turn = true;

        mBoardView.invalidate( );

        // Human goes first
        mInfoTextView.setText("You go first.");
        mCountHumanTextView.setText( mGame.getWins( 0 ).toString( ) );
        mCountTiesTextView.setText( mGame.getWins( 1 ).toString( ) );
        mCountAndroidTextView.setText( mGame.getWins( 2 ).toString( ) );

    }    // End of startNewGame

    private boolean setMove( char player, int location ){
        if( mGame.setMove( player, location ) ){
            mBoardView.setGame( mGame );
            mBoardView.invalidate( );
            return true;
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);

        mBoardView.setOnTouchListener( mTouchListener );

        mInfoTextView = (TextView) findViewById(R.id.information);
        mCountHumanTextView = (TextView) findViewById(R.id.countHuman);
        mCountTiesTextView = (TextView) findViewById(R.id.countTies);
        mCountAndroidTextView = (TextView) findViewById(R.id.countAndroid);

        mGame = new TicTacToeGame();

        startNewGame();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                int selected = mGame.getDifficultyLevel( ).ordinal();

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[item]);

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
        }

        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.android);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    public int mComputerMove( ){
        mInfoTextView.setText( R.string.turn_computer );
        int move = mGame.getComputerMove( );
        setMove( TicTacToeGame.COMPUTER_PLAYER, move );
        mComputerMediaPlayer.start( );
        turn = true;
        return mGame.checkForWinner( );
    }

    public void winner( ){
        int winner = mGame.checkForWinner( );
        if( winner == 0 ){
            mInfoTextView.setText( R.string.turn_human );
        }else if( winner == 1 ){
            mInfoTextView.setText( R.string.result_tie );
            mGame.setWins( 1 );
            mCountTiesTextView.setText( mGame.getWins( 1 ).toString( ) );
            mGameOver = true;
        }else if (winner == 2) {
            mInfoTextView.setText(R.string.result_human_wins);
            mGame.setWins( 0 );
            mCountHumanTextView.setText( mGame.getWins( 0 ).toString( ) );
            mGameOver = true;
        }else {
            mInfoTextView.setText(R.string.result_computer_wins);
            mGame.setWins( 2 );
            mCountAndroidTextView.setText( mGame.getWins( 2 ).toString( ) );
            mGameOver = true;
        }
    }

}