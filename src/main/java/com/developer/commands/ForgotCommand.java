package com.developer.commands;

import com.developer.AuthPlugin;
import com.developer.models.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ForgotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            User user = AuthPlugin.database.getUserByName(player.getName());
            if (user != null) {
                String to = user.getEmail();
                String subject = "Forgot Password";
                String text = String.format("Hello %s,<br> Here is your password for your minecraft account: %s<br>Thank You.", user.getName(), user.getPassword());
                if (sendMail(to, subject, text)) {
                    player.sendMessage("Please check your email " + user.getEmail() + " to get your account password.");
                } else {
                    player.sendMessage(String.format("Failed to send email to your %s account. Please contact admins for assistance.", user.getEmail()));
                }
            } else {
                player.sendMessage("You are not registered.");
            }
        }
        return true;
    }

    private boolean sendMail(String to, String subject, String text) {
        String json = "{\"receiver\": \"" + to + "\",\"subject\": \"" + subject + "\",\"body\": \"" + text + "\"}";
        try {
            URL url = new URL("http://localhost:3030/api/mail/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                if (connection.getResponseCode() != 200) {
                    AuthPlugin.plugin.getLogger().log(Level.INFO, String.format("Failed to send email at: %s Response: %s", to, response));
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
