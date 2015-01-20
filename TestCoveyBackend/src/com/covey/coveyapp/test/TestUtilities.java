package com.covey.coveyapp.test;

import ch.hsr.geohash.WGS84Point;

import com.covey.coveyapp.GeohashZone;
import com.covey.coveyapp.Profile;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class TestUtilities {

	/**
	 * Returns a profile with only its position (Fields below):
	 * <ul>
	 * <li>Latitude</li>
	 * <li>Longitude</li>
	 * <li>GeohashLocation</li>
	 * <li>GeohashZoneCenterLatitude</li>
	 * <li>GeohashZoneCenterLongitude</li>
	 * </ul>
	 * @param latitude
	 * @param longitude
	 * @return new Profile
	 */
	public static Profile makeProfilePositionOnly(double latitude, double longitude) {
		Profile profile = new Profile();
		profile.setLatitude(latitude);
		profile.setLongitude(longitude);
		profile.setGeohashLocation(GeohashZone.determineGeohashLocation(profile));
		profile.setGeohashZoneCenterLatitude(profile.getLatitude());
		profile.setGeohashZoneCenterLongitude(profile.getLongitude());

		return profile;
	}

	/**
	 * Gets the distance, in miles, between 2 points
	 * @param point1
	 * @param point2
	 * @return distance in miles
	 */
	public static double getDistanceBetween2Points(WGS84Point point1,WGS84Point point2) {
		LatLng p1 = new LatLng(point1.getLatitude(), point1.getLongitude());
		LatLng p2 = new LatLng(point2.getLatitude(), point2.getLongitude());
		return LatLngTool.distance(p1, p2, LengthUnit.MILE);
	}
}
