package com.projectsmile.youtube_reward;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

/* This fragment is used to house the YoutubePlayer and interact with the Youtube API */
public class YTPlayer extends YouTubePlayerFragment{

    //API Key used to initialize Youtube Player
    private String apiKey;
    protected void SetApiKey(String s){
        Log.d("*************", "SetKey entered");
        apiKey = s;}

    //Public string used to set the video id for local use
    public String vid;

    //Reference to the video player
    public YouTubePlayer videoPlayer;

    //Required listeners
    protected YouTubePlayer.OnInitializedListener initListener;
    private YouTubePlayer.PlaybackEventListener playbackEventListener;

    //Playback event flag used to signify a video has started playing
    private boolean hasStartedPlaying = false;

    /* Loads the player with a videoId string, calls Play, then sets the player
     * to full screen
     * Param: videoId - a videoId from youtube data query used to play a particular video
     */
    protected void PlayVideo (String videoId){
            videoPlayer.loadVideo(videoId);
            videoPlayer.seekToMillis(0);
            videoPlayer.play();
            videoPlayer.setFullscreen(true);
    }

    // Called by Youtube_Reward to create needed listeners and initialize the YoutubePlayer
    protected void InitPlayer(){
        CreateInitializationListener();
        CreatePlaybackEventListener();
        initialize(apiKey, initListener);
    }

    //Create the listener required for the YoutubePlayer initialization inside the fragment
    protected void CreateInitializationListener() {

         initListener = new YouTubePlayer.OnInitializedListener() {
             //Callback if initialization of Player succeeded
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("*******************", "Method: onInitializationSuccess\n" +
                        "Restored fm prev state?" + String.valueOf(b));
                if (videoPlayer != null){
                    videoPlayer = null;
                }
                //Set reference to new video player
                videoPlayer = youTubePlayer;
                //Set player style
                videoPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                //Disable fullscreen button
                videoPlayer.setShowFullscreenButton(false);
                //Set playback event listener on video player
                videoPlayer.setPlaybackEventListener(playbackEventListener);
                //Set flag used during Playback events
                hasStartedPlaying = false;
                PlayVideo(vid);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("********************", "Player Initialization failed");
                //Init has failed, calling ErrorLoad func in Unity Plugin script to return to main menu
                Youtube_Reward.PlayerInitFailure(youTubeInitializationResult.toString());
            }
        };
    }

    //Create the listener required to attach to the play and receive PlayBack events
    protected void CreatePlaybackEventListener() {
        //Listener for dealing with PlayBack events for the video this listener is attached to
        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

            //Video has begun playing
            @Override
            public void onPlaying() {
                Log.d("**************", "onPlaying Entered ! ! ! !");
                //Set flag to signify the video has started playing
                hasStartedPlaying = true;
            }

            //Video has been paused
            @Override
            public void onPaused() {
                Log.d("**************", "onPaused Entered ! ! ! !");
            }

            //Video has stopped playing because it ended or an error occured
            @Override
            public void onStopped() {
                Log.d("**************", "onStopped Entered ! ! ! !");
                if (hasStartedPlaying) {
                    Youtube_Reward.DetachPlayer();
                }
            }

            //Video is buffering
            @Override
            public void onBuffering(boolean b) {
                Log.d("**************", "onBuffering Entered ! ! ! !");
            }

            //User has fast forwarded / rewound the video
            @Override
            public void onSeekTo(int i) {
                Log.d("**************", "onSeekTo Entered ! ! ! !");

                //User seeked to the end of the video
                if (i == videoPlayer.getDurationMillis()){
                    Youtube_Reward.DetachPlayer();
                }
            }
        };
    }

    //Destroys the listeners created to avoid OS problems
    private void DestroyListeners(){
        Log.d("****************", "Destroy Listeners called ! ! ! !");
        playbackEventListener = null;
        initListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("*******************", "Fragment's onAttach entered");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("*******************", "Fragment's onCreate entered");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {super.onActivityCreated(savedInstanceState);}

    //Callback when the fragment becomes visable. Creates both listeners and initializes the Youtube Player
    @Override
    public void onStart(){super.onStart();}

    @Override
    public void onResume(){
        super.onResume();
    }

    //Callback when the fragment is no longer visable to the user. Calls to destroy the listeners.
    @Override
    public void onStop(){super.onStop();}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
