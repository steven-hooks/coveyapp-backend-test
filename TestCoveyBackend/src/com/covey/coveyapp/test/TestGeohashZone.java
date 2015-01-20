package com.covey.coveyapp.test;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.WGS84Point;

import com.covey.coveyapp.GeohashZone;
import com.covey.coveyapp.GeohashZone.Proximity;
import com.covey.coveyapp.Profile;

public class TestGeohashZone {

	private static BoundingBox _boxClose;
	private static BoundingBox _boxNear;
	private static BoundingBox _boxOuter;
	
	private static Profile _profileClose;
	private static Profile _profileNear;
	private static Profile _profileOuter;
	private static Profile _profileFar;
	private static WGS84Point _closeUpperLeft;
	
	//TODO: Add tests for changing box
	//TODO: Add tests for selecting max profiles across zones, depending on positioning
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	@Before
	public void setUp() throws Exception {
		_profileClose = TestUtilities.makeProfilePositionOnly(40.4595657, -79.9280203); //these are the coordinates of cube
		_boxClose = GeohashZone.getCloseZoneBoundingBox(_profileClose);
		_closeUpperLeft = _boxClose.getUpperLeft();
		
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .0089, _closeUpperLeft.getLongitude() + .01);
		_boxNear = GeohashZone.getNearbyZoneBoundingBox(_profileNear);
		
		_profileOuter = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .1, _closeUpperLeft.getLongitude() + .1);
		_boxOuter = GeohashZone.getOuterLimitZoneBoundingBox(_profileOuter);
		
		_profileFar = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + 1, _closeUpperLeft.getLongitude() + 1);
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
		// passes at 0.8373856841881587 miles
		// fails at 0.8379558510785096 miles
		_profileNear = TestUtilities.makeProfilePositionOnly(_closeUpperLeft.getLatitude() + .0089, _closeUpperLeft.getLongitude() + .01);
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
		//7.82 miles
		_profileOuter = TestUtilities.makeProfilePositionOnly(_boxClose.getCenterPoint().getLatitude() + .113, _boxClose.getCenterPoint().getLongitude() + .01);
		System.out.println("Proximity outer max distance = "  + TestUtilities.getDistanceBetween2Points(makeWGS(_profileClose), makeWGS(_profileOuter)));
		Proximity proximity = GeohashZone.getBestProximity(_profileClose, _profileOuter);
		Assert.assertEquals(Proximity.OUTERLIMIT, proximity);
	}

	//this seems like it could be problematic when moving into close zone because near contains close
	@Test
	public void testUserLeftNearbyZone() {
		fail("Not yet implemented");
	}

	public void testIsWithinNearbyZoneFalse() {
		Assert.assertFalse(GeohashZone.isWithinNearbyZone(_profileOuter, _profileNear));
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
		_profileNear = TestUtilities.makeProfilePositionOnly(_boxClose.getCenterPoint().getLatitude() + .0001, _boxClose.getCenterPoint().getLongitude()+.001);		
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
	public void testDetermineGeohashLocation() {
		String ghString = GeohashZone.determineGeohashLocation(_profileClose);
		Assert.fail("Not implemented yet");		
	}
	
	@Test
	public void testIsWithinOuterLimitZone_Outer() {
		Assert.assertTrue(GeohashZone.isWithinOuterLimitZone(_profileOuter,_profileOuter));
	}
	
	@Test
	public void testIsWithinOuterLimitZone_Near() {		
		Assert.assertTrue(GeohashZone.isWithinOuterLimitZone(_profileNear,_profileOuter));			
	}
	
	@Test
	public void testIsWithinOuterLimitZone_Close() {
		Assert.assertTrue(GeohashZone.isWithinOuterLimitZone(_profileClose,_profileOuter));		
	}
	
	public void testIsWithinOuterLimitZone_Far() {
		Assert.assertFalse(GeohashZone.isWithinOuterLimitZone(_profileFar,_profileOuter));
	}

	@Test
	public void testIsWithinNearbyLimitZone_Near() {		
		Assert.assertTrue(GeohashZone.isWithinOuterLimitZone(_profileNear,_profileNear));			
	}
	
	@Test
	public void testIsWithinNearbyLimitZone_Close() {
		Assert.assertTrue(GeohashZone.isWithinOuterLimitZone(_profileClose,_profileNear));		
	}
	
	@Test
	public void testIsWithinNearbyLimitZone_Outer() {		
		Assert.assertFalse(GeohashZone.isWithinOuterLimitZone(_profileOuter,_profileNear));			
	}
	
	public void testIsWithinNearbyLimitZone_Far() {
		Assert.assertFalse(GeohashZone.isWithinOuterLimitZone(_profileFar,_profileNear));
	}
		    
	@Test
	public void testIsWithinGeohashBoundingBox_CloseClose() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxClose, _profileClose));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_CloseNear() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxClose, _profileNear));
	}
	
	
	@Test
	public void testIsWithinGeohashBoundingBox_CloseOuter() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxClose, _profileOuter));	
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_CloseFar() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxClose, _profileFar));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_NearClose() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxNear, _profileClose));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_NearNear() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxNear, _profileNear));
	}	
	
	@Test
	public void testIsWithinGeohashBoundingBox_NearOuter() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxNear, _profileOuter));	
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_NearFar() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxNear, _profileFar));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_OuterClose() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxOuter, _profileClose));
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_OuterNear() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxOuter, _profileNear));
	}
	
	
	@Test
	public void testIsWithinGeohashBoundingBox_OuterOuter() {
		Assert.assertTrue(GeohashZone.isWithinGeohashBoundingBox(_boxOuter, _profileOuter));	
	}
	
	@Test
	public void testIsWithinGeohashBoundingBox_OuterFar() {
		Assert.assertFalse(GeohashZone.isWithinGeohashBoundingBox(_boxOuter, _profileFar));
	}
	//Inclusion tests - end
	
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

