package com.techelevator.hangman.services;

import com.techelevator.hangman.model.Player;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService {
    @Override
    public void sendCrownTakenNotificationTo(Player player) {

    }
}
