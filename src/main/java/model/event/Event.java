package model.event;


import model.resources.ResourcesType;
import model.village.Village;
import service.resource.ResourcesManagement;

public class Event {

    private final Village village;

    public Event(Village village) {
        this.village = village;

    }

    public void storm(){

        village.getLock().writeLock().lock();
        try {
            this.village.getCloud().setRadiation(this.village.getCloud().getRadiation() + 100);
        }finally {
            village.getLock().writeLock().unlock();
        }
    }

    public void disease(){

        village.getLock().writeLock().lock();
        try {
            this.village.setHealth(this.village.getHealth() - 250);
        }finally {
            village.getLock().writeLock().unlock();
        }
    }

    public void discovery(){

        village.getLock().writeLock().lock();
        try {
            ResourcesManagement resourcesManagement = village.getResourcesManagement();

            resourcesManagement.addResource(500, ResourcesType.IRON);
            resourcesManagement.addResource(500, ResourcesType.WOOD);
            resourcesManagement.addResource(500, ResourcesType.CLEAN_SOIL);
            resourcesManagement.addResource(500, ResourcesType.CLEAN_WATER);
            resourcesManagement.addResource(500, ResourcesType.COIN);
            resourcesManagement.addResource(500, ResourcesType.GUN_POWDER);
            // ,. . .
        }finally {
            village.getLock().writeLock().unlock();
        }
    }
}

