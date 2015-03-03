package net.infobosccoma.backgroundplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class activity_video extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private final String API_KEY="AIzaSyApO-0K77lkSI0oHvXlMOpFpq4lwk3aQ0Y";
    private String VIDEO_ID="o7VVHhK9zf0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);


        /** Initializing YouTube player view **/
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);
        youTubePlayerView.animate();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        /** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        /** Start buffering **/

            player.loadVideo(getIntent().getStringExtra("VIDEO_ID"));
            //TextView description = (TextView) findViewById(R.id.textViewDescription);
            //description.setText(getIntent().getStringExtra("DESCRIPTION").toString());

    }






    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
            Toast.makeText(getBaseContext(),"loading...", Toast.LENGTH_LONG );

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onPlaying() {

        }

        @Override
        public void onSeekTo(int arg0) {

        }

        @Override
        public void onStopped() {

        }

    };



    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {

        }

        @Override
        public void onLoaded(String arg0) {

        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onVideoStarted() {

        }
    };

}
