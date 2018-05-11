package hu.bme.mit.spaceship;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class GT4500Test {

  private TorpedoStore primary;
  private TorpedoStore secondary;
  private GT4500 ship;

  @Before
  public void init(){
    primary = mock(TorpedoStore.class);
    secondary = mock(TorpedoStore.class);
    ship = new GT4500(primary, secondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
	when(primary.isEmpty()).thenReturn(false);
	when(primary.fire(anyInt())).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
	when(primary.isEmpty()).thenReturn(false);
	when(primary.getTorpedoCount()).thenReturn(1);
	when(primary.fire(1)).thenReturn(true);
	when(secondary.isEmpty()).thenReturn(false);
	when(secondary.getTorpedoCount()).thenReturn(3);
	when(secondary.fire(3)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testSingleFiresOnlyOne(){
	 when(primary.isEmpty()).thenReturn(false);
	 when(primary.getTorpedoCount()).thenReturn(10);
	 when(primary.fire(1)).thenReturn(true);
	 
	 boolean result = ship.fireTorpedo(FiringMode.SINGLE);
	 
	 assertEquals(true, result); //So it called the fire method with 1
	 verify(primary, times(1)).fire(anyInt()); //It only invoked once
		
  }
  
  @Test
  public void testSingleTriesPrimaryFirst(){
	  when(primary.isEmpty()).thenReturn(false);
	  when(primary.getTorpedoCount()).thenReturn(1);
	  when(primary.fire(1)).thenReturn(true);
	  
	  when(secondary.isEmpty()).thenReturn(false);
	  when(secondary.getTorpedoCount()).thenReturn(1);
	  when(secondary.fire(1)).thenReturn(true);
	  
	  InOrder inOrder = inOrder(primary, secondary);
	  
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  
	  inOrder.verify(primary).fire(1);
	  inOrder.verify(secondary).fire(1);
  }
  
  @Test
  public void testSingleFiresAlternating(){
	  when(primary.isEmpty()).thenReturn(false);
	  when(primary.getTorpedoCount()).thenReturn(1);
	  when(primary.fire(1)).thenReturn(true);
	  
	  when(secondary.isEmpty()).thenReturn(false);
	  when(secondary.getTorpedoCount()).thenReturn(1);
	  when(secondary.fire(1)).thenReturn(true);
	  
	  InOrder inOrder = inOrder(primary, secondary, primary, secondary);
	  
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  
	  inOrder.verify(primary).fire(1);
	  inOrder.verify(secondary).fire(1);
	  inOrder.verify(primary).fire(1);
	  inOrder.verify(secondary).fire(1);
  }
  
  @Test
  public void testSingleStopsAlternatingWhenOneOfTheStoresAreEmpty(){
	  when(primary.isEmpty()).thenReturn(false);
	  when(primary.getTorpedoCount()).thenReturn(1);
	  when(primary.fire(1)).thenReturn(true);
	  
	  when(secondary.isEmpty()).thenReturn(false);
	  when(secondary.getTorpedoCount()).thenReturn(1);
	  when(secondary.fire(1)).thenReturn(true);
	  
	  InOrder inOrder = inOrder(primary, secondary, secondary, secondary);
	  
	  ship.fireTorpedo(FiringMode.SINGLE);
	  
	  when(primary.isEmpty()).thenReturn(true); //The primary store is empty now
	  
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  ship.fireTorpedo(FiringMode.SINGLE);
	  
	  inOrder.verify(primary).fire(1);
	  inOrder.verify(secondary).fire(1);
	  inOrder.verify(secondary).fire(1);
	  inOrder.verify(secondary).fire(1);
  }
  
  @Test
  public void testSingleStopsAtAnyFailure(){
	  when(primary.isEmpty()).thenReturn(false);
	  when(primary.getTorpedoCount()).thenReturn(1);
	  
	  when(primary.fire(1)).thenReturn(false); //First store returns with problem
	  
	  when(secondary.isEmpty()).thenReturn(false); //the second store is able to shoot
	  when(secondary.getTorpedoCount()).thenReturn(1);
	  when(secondary.fire(1)).thenReturn(true);
	  
	  ship.fireTorpedo(FiringMode.SINGLE);
	  
	  verify(primary, times(1)).fire(anyInt());
	  verify(secondary, times(0)).fire(anyInt());
  }
  
  @Test
  public void testAllTriesToFireAllStores(){
	  int primaryCount = 15; 
	  int secondaryCount = 30;
	  
	  when(primary.isEmpty()).thenReturn(false);
	  when(primary.getTorpedoCount()).thenReturn(primaryCount);
	  when(primary.fire(primaryCount)).thenReturn(true);
	  
	  when(secondary.isEmpty()).thenReturn(false);
	  when(secondary.getTorpedoCount()).thenReturn(secondaryCount);
	  when(secondary.fire(secondaryCount)).thenReturn(true);
	  
	  ship.fireTorpedo(FiringMode.ALL);
	  
	  verify(primary, times(1)).fire(primaryCount);
	  verify(secondary, times(1)).fire(secondaryCount);
  }

}
