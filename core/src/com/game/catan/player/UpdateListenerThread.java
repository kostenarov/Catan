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
    private static final int MAX_DICE_THROW = 12;
    private static final int YOUR_TURN = 100;
    private static final int NOT_YOUR_TURN = 200;

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
                    if((int) input <= MAX_DICE_THROW) {
                        player.setDiceThrow((Integer) input);
                        player.setDiceThrown(true);
                    }
                    else if((int) input == YOUR_TURN) {
                        player.setIsTurn(true);
                        player.resetFlags();
                    }
                    else if((int) input == NOT_YOUR_TURN) {
                        player.setIsTurn(false);
                        player.resetFlags();
                    }
                }
                else if(input instanceof String) {
                    if(((String) input).contains("Points")) {
                        player.setPoints(Integer.parseInt(((String) input).split(":")[1]));
                        player.displayPoints();
                    }
                    else if(((String) input).contains("Players")) {
                        player.setPlayersAmount(Integer.parseInt(((String) input).split(":")[1]));
                    }
                    else if(((String) input).contains("Loss")) {
                        player.setLoss(true);
                    }
                    else if(((String) input).contains("Win")) {
                        player.setWin(true);
                    }
                    else if(((String) input).contains("Offer accepted")) {
                        if(player.isOfferBeingCreated() || player.hasOfferBeenCreated()) {
                            player.addOutgoingOfferAcceptance(Integer.parseInt(((String) input).split(":")[1]));
                        }
                        else if(player.isOfferBeingReceived()){
                            player.addIncomingOfferAcceptance(Integer.parseInt(((String) input).split(":")[1]));
                        }
                    }
                    else if(((String) input).contains("Offer rejected")) {
                        if(player.isOfferBeingCreated() || player.hasOfferBeenCreated()) {
                            player.addOutgoingOfferRejection(Integer.parseInt(((String) input).split(":")[1]));
                        }
                        else if(player.isOfferBeingReceived()){
                            player.addIncomingOfferRejection(Integer.parseInt(((String) input).split(":")[1]));
                        }
                    }
                    else if(((String) input).contains("All rejected")) {
                        player.resetOffer();
                    }
                    else if(((String) input).contains("Trade")) {
                        player.disposeIndicatorStage();
                        player.resetOffer();
                    }
                }
                else if(input instanceof Map) {
                    player.setMap((Map) input);
                }
                else if(input instanceof HashMap) {
                    player.setDeck((HashMap<ResourceType, Integer>) input);
                }
                else if(input instanceof ResourceCell) {
                    player.setRobberCell((ResourceCell) input);
                }
                else if(input instanceof VillageCell) {
                    player.setVillageCell((VillageCell) input);
                }
                else if(input instanceof RoadCell) {
                    player.setRoadCell((RoadCell) input);
                }
                else if(input instanceof Offer) {
                    if(((Offer) input).getPlayerId() != player.getId()) {
                        player.setIncomingOffer((Offer) input);
                    }
                    else {
                        player.setOutgoingOffer((Offer) input);
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
