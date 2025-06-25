/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.POEPART3;

import java.util.Scanner;

public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhoneNumber;

    public boolean checkUserName(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    public boolean checkPasswordComplexity(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*\\d.*") &&
               password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser(Scanner scanner) {
        StringBuilder result = new StringBuilder();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no longer than five characters in length.";
        }
        result.append("Username successfully captured.\n");
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted: please ensure that the password contains at least eight characters, a capital letter, a number and a special character.";
        }
        result.append("Password successfully captured.\n");
        System.out.print("Enter South African cell phone number (e.g. +27831234567): ");
        String cellPhoneNumber = scanner.nextLine();
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        result.append("Cell phone number successfully added.\n");
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhoneNumber = cellPhoneNumber;
        result.append("Registration successful!");
        return result.toString();
    }

    public String returnLoginStatus(Scanner scanner) {
        if (registeredUsername == null || registeredPassword == null) {
            return "No user registered yet.";
        }
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();
        if (inputUsername.equals(registeredUsername) && inputPassword.equals(registeredPassword)) {
            return "Welcome " + registeredUsername + ", it is great to see you again!";
        } else {
            return "Login failed. Username or password incorrect.";
        }
    }

    public void setRegisteredUsername(String username) {
        this.registeredUsername = username;
    }

    public void setRegisteredPassword(String password) {
        this.registeredPassword = password;
    }
}