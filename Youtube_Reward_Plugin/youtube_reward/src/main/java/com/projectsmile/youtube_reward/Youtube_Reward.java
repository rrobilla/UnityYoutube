package com.projectsmile.youtube_reward;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.SurfaceView;

import com.unity3d.player.UnityPlayer; //Required to use the UnityPlayerActivity
import com.unity3d.player.UnityPlayerActivity;


/*
This custom activity extends UnityPlayerActivity to load additional UI elements into the view.
 */

public class Youtube_Reward extends UnityPlayerActivity{

    /*Use this to send function calls back to Unity

    UnityPlayer.UnitySendMessage("GameObjectName", "MethodName", "MessageToSend");

    */

    private static String FragmentTag = "YTPlayer";

    //Reference to assist with communications to Unity
    private static String gameObjectName;

    //Api key for Youtube API calls
    private static String apiKey;

    //References to the fragments
    public static YTPlayer ytFragment;

    //Flag for determining whether the parent view reference has been set already
    private static boolean fragmentCreatedPreviously;

    //Flag for determining if Fragment is currently visable
    private static boolean isAttached;


    // ++ Entire lifetime starts here ++ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCreatedPreviously = false;
        isAttached = false;
    }

    // ++ Visible lifetime starts here ++ //
    @Override
    protected void onStart() {
        super.onStart();
    }

    // ++ Foreground lifetime starts here ++ //
    @Override
    protected void onResume() {
        super.onResume();
    }

    // -- Foreground lifetime ends -- //
    @Override
    protected void onPause() {
        super.onPause();
    }

    // -- Visible lifetime ends -- //
    @Override
    protected void onStop() {
        super.onStop();
    }

    // -- Entire lifetime ends -- //
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* Detaches the ytFragment from the UI, causing the player to release the resources it is using
     * and removes the fragment from view
     */
    public static void DetachPlayer(){
        Log.d("*************", "Youtube_Reward - DeletePlayer called");
        if (isAttached) {
            UnityPlayer.currentActivity.getFragmentManager().beginTransaction().detach(ytFragment).commit();
            isAttached = false;
        }
    }

    /* Overlays the YT Fragment onto Unity and initializes the YoutubePlayer inside the fragment
     * Params: String videoId - the Youtube video ID of the video the user wants to watch
     */
    public static void CreatePlayer(String videoId){
        //Fragment has not been created yet
        if (!fragmentCreatedPreviously) {
            fragmentCreatedPreviously = true;
            //Creates the fragment
            ytFragment = new YTPlayer();
            //Sets the API key and video id inside the fragment
            ytFragment.SetApiKey(apiKey);
            ytFragment.vid = videoId;
            //Initializes the YoutubePlayer
            ytFragment.InitPlayer();
            //Adds the fragment to the main Unity activity
            UnityPlayer.currentActivity.getFragmentManager().beginTransaction().add(android.R.id.content, ytFragment, FragmentTag).commit();

        }
        //Fragment has already been created
        else{
            //Re-attaches the fragment to the main Unity activity
            UnityPlayer.currentActivity.getFragmentManager().beginTransaction().attach(ytFragment).commitAllowingStateLoss();
            //Sets the video id inside the fragment
            ytFragment.vid = videoId;
            //Initializes the YoutubePlayer
            ytFragment.InitPlayer();
        }
        isAttached = true;
    }

    public static void PlayerInitFailure(String result){
        UnityPlayer.UnitySendMessage(gameObjectName, "ErrorLoad", result);
    }

    public static void SetGameObjectReference(String s){
        Log.d("*************", "SetGameObjectReference entered");
        gameObjectName = s;
    }

    public static void SetApiKey(String s){
        Log.d("*************", "SetApiKey entered");
        apiKey = s;
    }

}