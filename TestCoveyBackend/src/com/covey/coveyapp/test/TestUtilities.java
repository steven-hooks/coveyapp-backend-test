package com.covey.coveyapp.test;

import com.covey.coveyapp.GeohashZone;
import com.covey.coveyapp.Profile;

public class TestUtilities {
	
	//This is to make a profile that only has location data, this will most likely change
	public static Profile makeProfile(double latitude, double longitude){
		Profile profile = new Profile();
		profile.setLatitude(40.4595657);
		profile.setLongitude( -79.9280203);
		profile.setGeohashLocation(GeohashZone.determineGeohashLocation(profile));
		profile.setGeohashZoneCenterLatitude(profile.getLatitude());
		profile.setGeohashZoneCenterLongitude(profile.getLongitude());
		
		return profile;
	}
}
