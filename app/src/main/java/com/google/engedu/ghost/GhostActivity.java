package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostText;
    private int userScore;
    private int computerScore;
    private String wordFragment = "";
    private TextView score;
    private TextView turnState;
    private boolean easy;
    private boolean begin;
    private final String STATE_FRAGMENT="ghost";
    private final String STATE_STATUS="status";
    private final String STATE_SCORE="score";
    private final String LEVEL="level";
    private final String POS="begin";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_FRAGMENT,ghostText.getText().toString());
        outState.putString(STATE_STATUS,turnState.getText().toString());
        outState.putString(STATE_SCORE,score.getText().toString());
        outState.putBoolean(LEVEL,easy);
        outState.putBoolean(POS,begin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wordFragment="";
        userScore=0;
        computerScore=0;
        easy=true;
        begin=false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        try {
            InputStream wordInputStream=this.getAssets().open("words.txt");
            dictionary = new FastDictionary(wordInputStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        ghostText = (TextView)this.findViewById(R.id.ghostText);
        score=(TextView)findViewById(R.id.score);
        turnState=(TextView)findViewById(R.id.gameStatus);
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_level) {
            if (item.getTitle()==getString(R.string.level_easy)){
                item.setTitle(getString(R.string.level_hard));
                easy=false;
            }
            else {
                item.setTitle(getString(R.string.level_easy));
                easy = true;
            }
            return true;
        }

        else if (id==R.id.action_pos){
            if (item.getTitle()==getString(R.string.begin)){
                item.setTitle(R.string.end);
                begin=false;
            }
            else{
                item.setTitle(getString(R.string.begin));
                begin=true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ghostText.setText(savedInstanceState.getString(STATE_FRAGMENT));
        turnState.setText(savedInstanceState.getString(USER_TURN));
        score.setText(savedInstanceState.getString(STATE_SCORE));
        easy=savedInstanceState.getBoolean(LEVEL);
        begin=savedInstanceState.getBoolean(POS);
        if(!easy){
            MenuItem item=(MenuItem)findViewById(R.id.action_level);
            item.setTitle(R.string.level_hard);
        }
        if(begin){
            MenuItem item=(MenuItem)findViewById(R.id.action_pos);
            item.setTitle(R.string.begin);
        }
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        Button challengeButton=(Button)findViewById(R.id.challenge_button);
        // Do computer turn stuff then make it the user's turn again
        challengeButton.setEnabled(false);
        if (dictionary.isWord(wordFragment)){
            label.setText("Computer wins, that was a word!");
            computerScore++;
            score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
        }
        else {
            String word;
            if (easy)
                word = dictionary.getAnyWordStartingWith(wordFragment);
            else
                word=dictionary.getGoodWordStartingWith(wordFragment);
            if(word == null)
            {
                label.setText("Computer challenges and wins!");
                computerScore++;
                score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
            }
            else {
                userTurn = true;
                wordFragment+=word.charAt(wordFragment.length());
                ghostText.setText(wordFragment);
                label.setText(USER_TURN);
                challengeButton.setEnabled(true);
            }
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public void onStart(View view) {
        wordFragment="";
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
        TextView label = (TextView) findViewById(R.id.gameStatus);
            if (userTurn) {
                label.setText(USER_TURN);
            }
            else {
                label.setText(COMPUTER_TURN);
                computerTurn();
            }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char keyChar = (char) event.getUnicodeChar();
        if(Character.isLetter(keyChar)&&userTurn){
            keyChar = Character.toLowerCase(keyChar);
            if(!begin)
                wordFragment += keyChar;
            else
                wordFragment=keyChar+wordFragment;
            ghostText.setText(wordFragment);
            computerTurn();
            return true;
        }else {
            return false;
        }
    }

    public void challenge(View view) {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        Button challengeButton=(Button)findViewById(R.id.challenge_button);
        challengeButton.setEnabled(false);
        if (dictionary.isWord(wordFragment)){
            label.setText("User wins, that was a word!");
            userScore++;
            score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
        }
        else {
            String word = dictionary.getAnyWordStartingWith(wordFragment);
            if(word == null)
            {
                label.setText("User challenges and wins!");
                userScore++;
                score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
            }
            else {
                label.setText("User looses! It was a valid word!");
                wordFragment=word;
                computerScore++;
                score.setText("Your score: "+Integer.toString(userScore)+" Computer score: "+Integer.toString(computerScore));
                ghostText.setText(wordFragment);
            }
        }
    }
}
