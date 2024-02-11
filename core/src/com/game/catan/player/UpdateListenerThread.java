package com.game.catan.player;

import com.game.catan.Functionality.MessageType;
import com.game.catan.Functionality.MessageWrapper;
import com.game.catan.Functionality.Offer;
import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.RoadCell;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class UpdateListenerThread extends Thread{
    private final CatanPlayer player;
    private final ObjectInputStream in;
    private boolean isWorking = true;

    public UpdateListenerThread(CatanPlayer player, ObjectInputStream in) {
        this.player = player;
        this.in = in;
    }

    @Override
    public void run() {
        while(isWorking) {
            try {
                Object input = in.readObject();
                System.out.println(input);
                if (input instanceof Integer) {
                    if((int) input < 13) {
                        player.setDiceThrow((Integer) input);
                        player.setDiceThrown(true);
                        System.out.println(player.getIsDiceThrown());
                    }
                    else if((int) input == 100) {
                        System.out.println("Your turn");
                        player.setIsTurn(true);
                        player.resetFlags();
                        player.resetOffer();
                    }
                    else if((int) input == 200) {
                        System.out.println("Not your turn");
                        player.setIsTurn(false);
                        player.resetOffer();
                        player.resetFlags();
                    }
                }
                else if(input instanceof MessageWrapper<?>) {
                    if(((MessageWrapper<?>) input).getType() == MessageType.STRING) {
                        if(((String) ((MessageWrapper<?>) input).getMessage()).contains("Offer accepted")) {
                            int id = Integer.parseInt(((String) ((MessageWrapper<?>) input).getMessage()).split(":")[1]);
                            System.out.println("Offer accepted");
                            if(player.isOfferBeingCreated()) {
                                player.addOutgoingOfferAcceptance(id);
                            }
                            else {
                                player.addIncomingOfferAcceptance(id);

                            }
                        }
                    }
                }
                else if(input instanceof String) {
                    if(((String) input).contains("Points")) {
                        System.out.println("Points received");
                        player.setPoints(Integer.parseInt(((String) input).split(":")[1]));
                        player.displayPoints();
                    }
                    else if(((String) input).contains("Players")) {
                        System.out.println("Players received");
                        player.setPlayersAmount(Integer.parseInt(((String) input).split(":")[1]));
                    }
                    else if(((String) input).contains("Loss")) {
                        System.out.println("Loss received");
                        player.setLoss(true);
                    }
                    else if(((String) input).contains("Win")) {
                        System.out.println("Win received");
                        player.setWin(true);
                    }
                }
                else if(input instanceof Map) {
                    System.out.println("Map received");
                    player.setMap((Map) input);
                }
                else if(input instanceof HashMap) {
                    System.out.println("Deck received");
                    player.setDeck((HashMap<ResourceType, Integer>) input);
                }
                else if(input instanceof ResourceCell) {
                    System.out.println("Resource cell received");
                    player.setRobberCell((ResourceCell) input);
                }
                else if(input instanceof VillageCell) {
                    System.out.println("Village cell received");
                    player.setVillageCell((VillageCell) input);
                }
                else if(input instanceof RoadCell) {
                    System.out.println("Road cell received");
                    player.setRoadCell((RoadCell) input);
                }
                else if(input instanceof Offer) {
                    if(((Offer) input).getPlayerId() != player.getId()) {
                        player.setIncomingOffer((Offer) input);
                        System.out.println("Incoming offer set");
                    }
                    else {
                        player.setOutgoingOffer((Offer) input);
                        System.out.println("Outgoing offer set");
                    }
                }

            } catch (Exception e) {
                System.out.println("Could not read input");
            }
        }
    }

    public void stopThread() {
        isWorking = false;
    }
}
