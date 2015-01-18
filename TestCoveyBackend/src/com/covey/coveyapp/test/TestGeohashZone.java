package com.covey.coveyapp.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.covey.coveyapp.GeohashZone;
import com.covey.coveyapp.GeohashZone.Proximity;
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
	private static WGS84Point _closeUpperLeft;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	@Before
	public void setUp() throws Exception {
		_profileClose = TestUtilities.makeProfilePositionOnly(40.4595657, -79.9280203); //these are the coordinates of cube
		_boxClose = GeohashZone.getCloseZoneBoundingBox(_profileClose);
		_closeUpperLeft = _boxClose.getUpperLeft();
		
		//this value is not super meaningful, it gets changed a lot by different tests. this however is has it assigned to close... kinda silly
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .0000001, _closeUpperLeft.getLongitude() + .001);
		_profileOuter = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .1, _closeUpperLeft.getLongitude() + .1);
	}	
	
	//sanity test - make sure distance is calculated correctly
	@Test
	public void testDistanceIsCorrect(){
		WGS84Point p1 = new WGS84Point(_profileClose.getLatitude(), _profileClose.getLongitude());		
		WGS84Point p2 = new WGS84Point(_profileNear.getLatitude(), _profileNear.getLongitude());
		double distance = TestUtilities.getDistanceBetween2Points(p1, p2);
		System.out.println("DistanceIsCorrect distance = "+ distance);
		
		Assert.assertTrue(distance > .05 && distance < .1 );
	}

	@Test
	public void testGetBestProximityCloseDistEq0() {
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileClose);
		Assert.assertEquals(Proximity.CLOSE, proximity);
	}
	
	@Test
	public void testGetBestProximityNearbyMinBoundry() {
		//fails at 0.06840248838232732 miles
		//passes at  0.06909342260762119 miles
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() - .002,_closeUpperLeft.getLongitude() + .001);
		System.out.println("Proximity nearby min distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileNear)));
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileNear);
		Assert.assertEquals(Proximity.NEARBY, proximity);
	}
	
	@Test
	public void testGetBestProximityNearbyMaxBoundry() {
		//0.4928964831637052 miles
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .0115, _closeUpperLeft.getLongitude() + .01);
		System.out.println("Proximity nearby max distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileNear)));
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileNear);
		Assert.assertEquals(Proximity.NEARBY, proximity);
	}
	
	@Test
	public void testGetBestProximityOuterMinBoundry() {
		//1.0090787322269674 miles
		_profileOuter = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .013, _closeUpperLeft.getLongitude() + .01);
		System.out.println("Proximity outer min distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileOuter)));
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileOuter);
		Assert.assertEquals(Proximity.OUTERLIMIT, proximity);
	}
	
	@Test
	public void testGetBestProximityOuterMaxBoundry() {
		//1.0090787322269674 miles
		_profileOuter = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .117, _closeUpperLeft.getLongitude() + .01);
		System.out.println("Proximity outer distance (outside range) = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileOuter)));
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileOuter);
		Assert.assertEquals(Proximity.OUTERLIMIT, proximity);
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
	public void testIsWithinNearbyZoneTrue() {
		fail("Not yet implemented");
	}

	public void testIsWithinNearbyZoneFalse() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testIsWithinCloseZoneFalse() {
		//0.8373856841881587 miles
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .009, _closeUpperLeft.getLongitude() + .01);
		System.out.println("WithinCloseFalse distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileNear)));
		Assert.assertFalse(GeohashZone.isWithinCloseZone(_profileNear, _profileClose));
	}
	
	@Test
	public void testIsWithinCloseZoneTrue() {
		//0.06909342260762119 miles
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .0000001, _closeUpperLeft.getLongitude() + .001);
		
		//DAVE - the distance is 0.0690 this test should pass, yes?
		System.out.println("WithinCloseTrue distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileNear)));
		Assert.assertTrue(GeohashZone.isWithinCloseZone(_profileClose, _profileNear));
	}
	
	@Test
	public void testIsWithinCloseZoneP1OnBorder(){
		WGS84Point boundry = GeohashZone.getCloseZoneBoundingBox(_profileClose).getUpperLeft();
		_profileNear = TestUtilities.makeProfilePositionOnly(boundry.getLatitude(), boundry.getLongitude());
		
		Assert.assertTrue(GeohashZone.isWithinCloseZone(_profileNear, _profileClose));
	}
	
	@Test
	public void testIsWithinCloseZoneP2OnBorder(){
		WGS84Point boundry = GeohashZone.getCloseZoneBoundingBox(_profileClose).getUpperLeft();
		_profileNear = TestUtilities.makeProfilePositionOnly(boundry.getLatitude(), boundry.getLongitude());
		
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
		_profileOuter = null;
		_boxClose = null;
		_boxNear = null;
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	private static WGS84Point makeWGS(Profile profile){
		return new WGS84Point(profile.getLatitude(), profile.getLongitude());
	}
	
}
