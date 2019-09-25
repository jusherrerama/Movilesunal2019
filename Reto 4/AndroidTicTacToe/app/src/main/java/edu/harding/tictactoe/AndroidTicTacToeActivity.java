package edu.harding.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
public class AndroidTicTacToeActivity extends AppCompatActivity {

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver;

    // Buttons making up the board
    private Button mBoardButtons[];

    // Various text displayed
    private TextView mInfoTextView;
    private TextView mDiffTextView;



    // Set up the game board.
    private void startNewGame( ){

        mGame.clearBoard();
        mGameOver = false;

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        // Human goes first
        mDiffTextView.setText(mGame.dificult);
        mInfoTextView.setText("You go first.");

    }    // End of startNewGame

    private void setMove(char player, int location) {

        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 200));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mDiffTextView = (TextView) findViewById(R.id.dificultad);

        mGame = new TicTacToeGame();

        startNewGame();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
        //menu.add("New Game One player");

        //menu.add("New Game Two player");

//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {







        switch (item.getItemId()) {
            case R.id.new_game:
                mGame.TWO_PLAYERS = false;
                startNewGame();
                return true;
            case R.id.new_game2:
                mGame.TWO_PLAYERS = true;
                startNewGame();
                return true;
            case R.id.Quit:

                AlertDialog.Builder builder = new AlertDialog.Builder(AndroidTicTacToeActivity.this);

                builder.setMessage("¿ Are you sure ?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null).show();


                return true;
            case R.id.Easy:
                mGame.dificult = "Easy";
                startNewGame();
                return true;
            case R.id.Harder:
                mGame.dificult = "Harder";
                startNewGame();
                return true;
            case R.id.Expert:
                mGame.dificult =  "Expert";
                startNewGame();
                return true;
        }
        return false;



    }


    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        public void onClick(View view) {

            if(mGameOver) return;

            if (mBoardButtons[location].isEnabled()) {


                if( mGame.TWO_PLAYERS &&  mGame.PLAYER_TURN  == 2){
                    setMove(TicTacToeGame.HUMAN_PLAYER2, location);
                    mGame.PLAYER_TURN  = 1;
                }else{
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    mGame.PLAYER_TURN  = 2;
                }


                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();

                if (winner == 0 &&  !mGame.TWO_PLAYERS) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove( mGame.dificult );
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner == 0) {
                    if(mGame.PLAYER_TURN  == 2){
                        mInfoTextView.setText(R.string.turn_human2);

                    }else{
                        mInfoTextView.setText(R.string.turn_human1);
                    }
                }else if (winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                }else if (winner == 2 &&  !mGame.TWO_PLAYERS) {
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                }else if (winner == 2 &&  mGame.TWO_PLAYERS &&  mGame.PLAYER_TURN  == 2) {
                    mInfoTextView.setText(R.string.result_p1_wins);
                    mGameOver = true;
                }else if (mGame.TWO_PLAYERS){
                    mInfoTextView.setText(R.string.result_p2_wins);
                }
                else {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                }
            }
        }
    }
}