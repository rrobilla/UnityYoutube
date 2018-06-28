# UnityYoutube
An android-specific plugin to enable a Youtube Player overtop of Unity


#How to Use
- Place youtube_reward_plugin.aar and AndroidManifest.xml into the following folder in Unity:  Assets/Plugins/Android

- Use the following code in your Unity script to interact with the plugin

#Create custom activity with needed info to play a youtube video

		try{
			//Creates an AndroidJavaClass object
			_customActivity = new AndroidJavaClass("com.projectsmile.youtube_reward.Youtube_Reward");
			
			//Sets a reference in the plugin to the name of the object the script containing
			//this code is attached to
    		_customActivity.CallStatic("SetGameObjectReference", this.name);
			
			//Sets the youtube api key in the plugin
			_customActivity.CallStatic("SetApiKey", <api key>);
		}
		catch{
			Debug.Log("Error: Not on android");
		}

#Creates the YoutubePlayer fragment in Android, adds it to the fragment manager, and plays the video

		try{
			_customActivity.CallStatic("CreatePlayer", videoId);
		}
		catch{
			Debug.Log("Error: Not on android");
		}
		
		
#Removes the YoutubePlayer fragment

		try{
			_customActivity.CallStatic("DetachPlayer");
		}
		catch{
			Debug.Log("Error:  Not on android");
		}