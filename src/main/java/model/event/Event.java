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
            this.village.getCloud().setRadiation(this.village.getCloud().getRadiation() + 100);
        }

        public void disease(){
            this.village.setHealth(this.village.getHealth() - 250);
        }

        public void discovery(){
            ResourcesManagement resourcesManagement = village.getResourcesManagement();

            resourcesManagement.addResource(500, ResourcesType.IRON);
            resourcesManagement.addResource(500, ResourcesType.WOOD);
            resourcesManagement.addResource(500, ResourcesType.CLEAN_SOIL);
            resourcesManagement.addResource(500, ResourcesType.CLEAN_WATER);
            resourcesManagement.addResource(500, ResourcesType.COIN);
            resourcesManagement.addResource(500, ResourcesType.GUN_POWDER);
            // ,. . .
        }
    }

