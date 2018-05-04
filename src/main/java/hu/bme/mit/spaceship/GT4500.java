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
      return tryFiringFromStore(secondaryTorpedoStore) || tryFiringFromStore(primaryTorpedoStore);
    } else {
      return tryFiringFromStore(primaryTorpedoStore) || tryFiringFromStore(secondaryTorpedoStore);
    }
  }



  private boolean fireAllTorpedo() {
    if (!primaryTorpedoStore.isEmpty()) {
      firingSuccess = primaryTorpedoStore.fire(primaryTorpedoStore.getTorpedoCount());
      wasPrimaryFiredLast = firingSuccess || wasPrimaryFiredLast;
    }
    if (!secondaryTorpedoStore.isEmpty()) {
      boolean secondarySuccess = secondaryTorpedoStore.fire(secondaryTorpedoStore.getTorpedoCount());
      firingSuccess = secondarySuccess || firingSuccess;
      wasPrimaryFiredLast = !secondarySuccess && wasPrimaryFiredLast;
    }
  }

  private boolean tryFiringFromStore(TorpedoStore store) {
    if(store.isEmpty){
      return false;
    }
    boolean firingSuccess = secondaryTorpedoStore.fire(1);
    wasPrimaryFiredLast = firingSuccess && (!primaryTorpedoStore.equals(store));
    return firingSuccess
  }
}