package com.techelevator.hangman.services;

import com.techelevator.hangman.model.Player;

public interface NotificationService {
    void sendCrownTakenNotificationTo(Player player);
}
