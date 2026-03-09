package com.example.upr4;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.Random;

public class HelloController {


    @FXML private TextField fieldP;
    @FXML private TextField fieldV;
    @FXML private TextField fieldT;
    @FXML private TextField fieldUserCount;


    @FXML private Button btnGenerate;

    // Поля вывода
    @FXML private TextField fieldSStar;
    @FXML private TextField fieldMinLength;
    @FXML private TextArea textAreaPasswords;


    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'";
    private static final int MIN_PASSWORD_LENGTH = 6;

    @FXML
    public void initialize() {
        fieldP.setText("10e-5");
        fieldV.setText("100");
        fieldT.setText("12");
        fieldUserCount.setText("10");


        btnGenerate.setOnAction(event -> generatePasswords());
    }

    private void generatePasswords() {
        try {

            double P = Double.parseDouble("0.0001");
            double V_per_day = Double.parseDouble(fieldV.getText().trim());
            int T_days = Integer.parseInt(fieldT.getText().trim());
            int userCount = Integer.parseInt(fieldUserCount.getText().trim());

            //double V_per_min = V_per_day / 60 / 24;
            //double totalAttempts = V_per_day * T_days;

            long S_star = (long) Math.ceil(V_per_day * T_days / P);
            fieldSStar.setText(String.valueOf(S_star));

            int A = ALPHABET.length();

            int L = (int) Math.ceil(Math.log(S_star) / Math.log(A));
//            L = Math.max(MIN_PASSWORD_LENGTH, L);
            fieldMinLength.setText(String.valueOf(L));

            
            StringBuilder sb = new StringBuilder();
            Random rand = new Random();
            for (int i = 0; i < userCount; i++) {
                StringBuilder password = new StringBuilder();
                for (int j = 0; j < L; j++) {
                    password.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
                }
                sb.append("User").append(i + 1).append(": ").append(password).append("\n");
            }
            textAreaPasswords.setText(sb.toString());

        } catch (NumberFormatException e) {
            textAreaPasswords.setText("Ошибка: проверьте формат введённых данных.");
        } catch (Exception e) {
            textAreaPasswords.setText("Произошла ошибка: " + e.getMessage());
        }
    }
}
