package hu.bme.mit.spaceship;

/**
* A simple spaceship with two proton torpedo stores and four lasers
*/
public class GT4500 implements SpaceShip {

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  private boolean wasPrimaryFiredLast = false;
  
  public GT4500() {
    this.primaryTorpedoStore = new TorpedoStore(10);
    this.secondaryTorpedoStore = new TorpedoStore(10);
  }

  public GT4500(TorpedoStore primary, TorpedoStore secondary) {
    this.primaryTorpedoStore = primary;
    this.secondaryTorpedoStore = secondary;
  }

  public boolean fireLaser(FiringMode firingMode) {
    // TODO not implemented yet
    return false;
  }

  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */
  @Override
  public boolean fireTorpedo(FiringMode firingMode) {
    return (firingMode == FiringMode.SINGLE) ? fireSingleTorpedo() : fireAllTorpedo();
  }

  private boolean fireSingleTorpedo(){
    if (wasPrimaryFiredLast) {
    	return secondaryTorpedoStore.isEmpty() ? tryFiringFromStore(primaryTorpedoStore) : tryFiringFromStore(secondaryTorpedoStore);
    } else {
    	return primaryTorpedoStore.isEmpty() ? tryFiringFromStore(secondaryTorpedoStore) : tryFiringFromStore(primaryTorpedoStore);
    }
  }

  private boolean fireAllTorpedo() {
	boolean firingSuccess = false;
    if (!primaryTorpedoStore.isEmpty()) {
      firingSuccess = primaryTorpedoStore.fire(primaryTorpedoStore.getTorpedoCount());
      wasPrimaryFiredLast = firingSuccess || wasPrimaryFiredLast;
    }
    if (!secondaryTorpedoStore.isEmpty()) {
      boolean secondarySuccess = secondaryTorpedoStore.fire(secondaryTorpedoStore.getTorpedoCount());
      firingSuccess = secondarySuccess || firingSuccess;
      wasPrimaryFiredLast = !secondarySuccess && wasPrimaryFiredLast;
    }
    return firingSuccess;
  }

  private boolean tryFiringFromStore(TorpedoStore store) {
    if(store.isEmpty()){
      return false;
    }
    boolean firingSuccess = store.fire(1);
    wasPrimaryFiredLast = firingSuccess ? primaryTorpedoStore.equals(store) : wasPrimaryFiredLast;
    return firingSuccess;
  }
}