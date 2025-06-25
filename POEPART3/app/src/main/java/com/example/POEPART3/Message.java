/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.POEPART3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int messageCount = 0;
    private static ArrayList<Message> sentMessages = new ArrayList<>();     //array for sent messages
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();  //array for disregarded messages
    private static ArrayList<Message> storedMessages = new ArrayList<>();   //array for stored messages createrd by chatgpt
    private static ArrayList<String> messageHashes = new ArrayList<>();     //array for message hashes
    private static ArrayList<String> messageIDs = new ArrayList<>();        // array for message ids

    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 8999999999L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    public boolean checkRecipientCell() {
        return recipient.length() <= 13 && recipient.startsWith("+");
    }

    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (messageID.substring(0, 2) + ":" + (messageCount + 1) + ":" + firstWord + ":" + lastWord).toUpperCase();
    }

    public String sendMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose what to do with the message:",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: // switch with new function calls added to append the relevant fields to Sent Messages, Message ID and Message Hash arrays
                sentMessages.add(this);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                messageCount++;
                showMessage();
                return "Message sent";
            case 1:
                disregardedMessages.add(this);  //new function call to store disregarded messages
                return "Message disregarded";
            case 2:
                storedMessages.add(this);   //call to store messages for later
                storeMessage();
                return "Message stored to send later";
            default:
                return "No action taken";
        }
    }

    public void showMessage() {
        String messageDetails = "Message ID: " + messageID +
                "\nMessage Hash: " + messageHash +
                "\nRecipient: " + recipient +
                "\nMessage: " + messageText;
        JOptionPane.showMessageDialog(null, messageDetails, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String printMessages() {
        StringBuilder builder = new StringBuilder("All sent messages:\n");
        for (Message msg : sentMessages) {
            builder.append("To: ").append(msg.recipient)
                    .append(" | Message: ").append(msg.messageText)
                    .append(" | ID: ").append(msg.messageID)
                    .append("\n");
        }
        return builder.toString();
    }

    public static void displaySendersAndRecipients() {
        for (Message msg : sentMessages) {
            System.out.println("Sender: CurrentUser | Recipient: " + msg.getRecipient());
        }
    }

    public static void displayLongestMessage() {    //function to search for and display longest message sent
        Message longest = sentMessages.stream()
                .max((m1, m2) -> Integer.compare(m1.messageText.length(), m2.messageText.length()))
                .orElse(null);
        if (longest != null) {
            System.out.println("Longest Message:\n" + longest.getMessageText());
        }
    }

    public static void searchByMessageID(String id) {   //function to search for message via ID
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(id)) {
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

    public static void searchByRecipient(String recipient) {    //function to search for message Via recipient
        boolean found = false;
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(recipient)) {
                System.out.println("Message: " + msg.getMessageText() + " | ID: " + msg.getMessageID());
                found = true;
            }
        }
        if (!found) System.out.println("No messages found for recipient.");
    }

    public static boolean deleteByHash(String hash) {   //function to delete message via Hash
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                sentMessages.remove(i);
                messageHashes.remove(hash);
                System.out.println("Message deleted.");
                return true;
            }
        }
        System.out.println("Message hash not found.");
        return false;
    }

    public static void displayFullReport() {    // function to display a full report of a message
        for (Message msg : sentMessages) {
            System.out.println("ID: " + msg.getMessageID());
            System.out.println("Hash: " + msg.getMessageHash());
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessageText());
            System.out.println("----------------------");
        }
    }

    public static void loadStoredMessagesFromFiles() {      //chatGPT created function to load messages from files to array
        File dir = new File(".");
        Gson gson = new Gson();
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith("message_") && file.getName().endsWith(".json")) {
                try (FileReader reader = new FileReader(file)) {
                    Message msg = gson.fromJson(reader, Message.class);
                    storedMessages.add(msg);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error loading message: " + e.getMessage());
                }
            }
        }
    }

    public void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String filename = "message_" + messageID + ".json";
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save message: " + e.getMessage());
        }
    }

    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
}