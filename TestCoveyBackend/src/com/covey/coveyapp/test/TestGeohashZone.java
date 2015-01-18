package com.covey.coveyapp.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.covey.coveyapp.GeohashZone;
import com.covey.coveyapp.Profile;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.WGS84Point;

public class TestGeohashZone {

	private static BoundingBox _boxClose;
	private static BoundingBox _boxNear;
	private static BoundingBox _boxOuter;
	
	private static Profile _profileClose;
	private static Profile _profileNear;
	private static Profile _profileOuter;
	private static Profile _profileFar;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	@Before
	public void setUp() throws Exception {
		_profileClose = TestUtilities.makeProfile(40.4595657, -79.9280203);
		_boxClose = GeohashZone.getCloseZoneBoundingBox(_profileClose);
		WGS84Point closeUpperLeft = _boxClose.getUpperLeft();
		
		_profileNear = TestUtilities.makeProfile(closeUpperLeft.getLatitude() - 100, closeUpperLeft.getLongitude() + 100);
		_boxNear = GeohashZone.getNearbyZoneBoundingBox(_profileNear);
		
		System.out.println(distance(_profileClose.getLatitude(), _profileClose.getLongitude(), _profileNear.getLatitude(), _profileClose.getLongitude(), 'K'));
	}	

	@Test
	public void testGetBestProximity() {
		fail("Not yet implemented");
	}

	@Test
	public void testUserLeftNearbyZone() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsWithinOuterLimitZone() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsWithinNearbyZone() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsWithinCloseZoneFalse() {
		Assert.assertFalse(GeohashZone.isWithinCloseZone(_profileNear, _profileClose));
	}
	
	@Test
	public void testIsWithinCloseZoneTrue() {
		Assert.assertTrue(GeohashZone.isWithinCloseZone(_profileClose, _profileNear));
	}
	
	@Test
	public void testIsWithinCloseZoneP1OnBorder(){
		WGS84Point boundry = GeohashZone.getCloseZoneBoundingBox(_profileClose).getUpperLeft();
		_profileNear = TestUtilities.makeProfile(boundry.getLatitude(), boundry.getLongitude());
		Assert.assertTrue(GeohashZone.isWithinCloseZone(_profileNear, _profileClose));
	}
	
	@Test
	public void testIsWithinCloseZoneP2OnBorder(){
		WGS84Point boundry = GeohashZone.getCloseZoneBoundingBox(_profileClose).getUpperLeft();
		_profileNear = TestUtilities.makeProfile(boundry.getLatitude(), boundry.getLongitude());
		Assert.assertTrue(GeohashZone.isWithinCloseZone(_profileNear, _profileClose));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetermineGeohashLocation() {
		fail("Not yet implemented");
	}

	@After
	public void tearDown() throws Exception {
		_profileClose = null;
		_profileNear = null;
		_boxClose = null;
		_boxNear = null;
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
          dist = dist * 1.609344;
        } else if (unit == 'N') {
          dist = dist * 0.8684;
          }
        
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'M') + " Miles\n");
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'K') + " Kilometers\n");
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'N') + " Nautical Miles\n");
        return (dist);
      }

      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      /*::  This function converts decimal degrees to radians             :*/
      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
      }

      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      /*::  This function converts radians to decimal degrees             :*/
      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
      }
}
