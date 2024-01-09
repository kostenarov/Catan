package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceType;

import java.util.HashMap;

public class Deck {
    private HashMap<ResourceType, Integer> resources;

    public Deck() {
        resources = new HashMap<>();
        resources.put(ResourceType.WOOD, 4);
        resources.put(ResourceType.BRICK, 2);
        resources.put(ResourceType.STONE, 2);
        resources.put(ResourceType.SHEEP, 2);
        resources.put(ResourceType.WHEAT, 2);
    }

    public Deck(boolean helper) {
        resources = new HashMap<>();
        resources.put(ResourceType.WOOD, 0);
        resources.put(ResourceType.BRICK, 0);
        resources.put(ResourceType.STONE, 0);
        resources.put(ResourceType.SHEEP, 0);
        resources.put(ResourceType.WHEAT, 0);
    }

    public void addResource(ResourceType resource) {
        resources.put(resource, resources.get(resource) + 1);
    }

    public void removeResource(ResourceType resource) {
        resources.put(resource, resources.get(resource) - 1);
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    public void removeResources(ResourceType resource, int amount) {
        resources.put(resource, resources.get(resource) - amount);
    }

    public void addResources(ResourceType resource, int amount) {
        resources.put(resource, resources.get(resource) + amount);
    }

    public void setDeck(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public void addDeck(Deck deck) {
        for (ResourceType resource : deck.getResources().keySet()) {
            resources.put(resource, resources.get(resource) + deck.getResources().get(resource));
        }
    }

    public Integer getResourceAmount(ResourceType resource) {
        return resources.get(resource);
    }

    public void removeVillageResources() {
        removeResources(ResourceType.WOOD, 1);
        removeResources(ResourceType.STONE, 1);
        removeResources(ResourceType.SHEEP, 1);
        removeResources(ResourceType.WHEAT, 1);
    }

    public void removeRoadResources() {
        removeResources(ResourceType.WOOD, 1);
        removeResources(ResourceType.BRICK, 1);
    }
}
