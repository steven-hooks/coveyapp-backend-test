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
		_profileClose = TestUtilities.makeProfilePositionOnly(40.4595657, -79.9280203);
		_boxClose = GeohashZone.getCloseZoneBoundingBox(_profileClose);
		WGS84Point closeUpperLeft = _boxClose.getUpperLeft();
		
		_profileNear = TestUtilities.makeProfilePositionOnly(closeUpperLeft.getLatitude() - 100, closeUpperLeft.getLongitude() + 100);
		_boxNear = GeohashZone.getNearbyZoneBoundingBox(_profileNear);
	}	
	
	@Test
	public void testDistanceIsCorrect(){
		WGS84Point p1 = new WGS84Point(_profileClose.getLatitude(), _profileClose.getLongitude());
		WGS84Point p2 = new WGS84Point(_profileNear.getLatitude(), _profileNear.getLongitude());
		double distance = TestUtilities.getDistanceBetween2Points(p1, p2);
		System.out.println("Distance is  "+ distance);
		
		Assert.assertTrue(distance > 0);
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
		_boxClose = null;
		_boxNear = null;
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
}
