package org.example.lr3;

import javafx.scene.control.TextArea;
import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Base64;

public class FeishtelCypher {
    private long k1;
    private long k2;
    private TextArea outputTextEncLog;

    public FeishtelCypher(String key, TextArea outputTextEncLog){
        long hash = key.hashCode();
        this.k1 = hash;
        this.k2 = Long.rotateLeft(hash, 32);
        this.outputTextEncLog = outputTextEncLog;
    }

    public String encrypt(String data, Integer roundCount) {
        StringBuilder log = new StringBuilder();
        StringBuilder sb = new StringBuilder(data);
        while (sb.length() % 4 != 0) {
            sb.append(" ");
        }
        data = sb.toString();
        int[] words = data.codePoints().toArray();
        int[] result = new int[words.length];

        for (int i = 0; i < words.length; i += 4) {
            int x1 = words[i];
            int x2 = words[i + 1];
            int x3 = words[i + 2];
            int x4 = words[i + 3];

            log.append(String.format("--- Блок %d ---\n", i / 4 + 1));

            for (int r = 1; r <= roundCount; r++) {
                log.append(String.format("Раунд %d. Вход: [X1:%d, X2:%d, X3:%d, X4:%d]\n", r, x1, x2, x3, x4));
                int vi = calculateVi(r);
                int fOut = vi >> 1;

                log.append(String.format("  f(Vi) = %d\n", fOut));
                int tempX1;
                if (r!=roundCount){
                    tempX1 = x2 ^ fOut;
                }else{
                    tempX1 = x2;
                }

                int tempX2 = x3;
                int tempX3 = x4;
                int tempX4 = x1;

                x1 = tempX1; x2 = tempX2; x3 = tempX3; x4 = tempX4;

                log.append(String.format("  Выход: [X1:%d, X2:%d, X3:%d, X4:%d]\n", x1, x2, x3, x4));
            }
            result[i] = x1; result[i+1] = x2; result[i+2] = x3; result[i+3] = x4;
            log.append("\n");
        }

        if (outputTextEncLog!= null) {
            outputTextEncLog.setText(log.toString());
        }

//        ByteBuffer byteBuffer = ByteBuffer.allocate(result.length * 4);
//        byteBuffer.asIntBuffer().put(result);
        //return Base64.getEncoder().encodeToString(byteBuffer.array());
        StringBuilder binaryResult = new StringBuilder();
        for (int word : result) {
            binaryResult.append(toBinary32(word));
            //binaryResult.append("  ");
        }

        return binaryResult.toString();
    }

    private String toBinary32(int n) {
        return String.format("%32s", Integer.toBinaryString(n)).replace(' ', '0');
    }

    public String decrypt(String base64Data, Integer roundCount) {
        StringBuilder log = new StringBuilder();

        int wordCount = base64Data.length() / 32;
        int[] words = new int[wordCount];
        for (int i = 0; i < wordCount; i++) {
            String bitChunk = base64Data.substring(i * 32, (i + 1) * 32);
            words[i] = (int) Long.parseLong(bitChunk, 2);
        }

        int[] result = new int[words.length];

        for (int i = 0; i < words.length; i += 4) {

            int x1 = words[i];
            int x2 = words[i + 1];
            int x3 = words[i + 2];
            int x4 = words[i + 3];

            log.append(String.format("--- Блок %d ---\n", i / 4 + 1));

            for (int r = roundCount; r >= 1; r--) {
                log.append(String.format("Обратный раунд %d. Вход: [X1:%d, X2:%d, X3:%d, X4:%d]\n", r, x1, x2, x3, x4));

                int vi = calculateVi(r);
                int fOut = vi >> 1;

                log.append(String.format("  f(Vi) = %d\n", fOut));
                int prevX1 = x4;
                int prevX2 = x1 ^ fOut;
                int prevX3 = x2;
                int prevX4 = x3;

                x1 = prevX1; x2 = prevX2; x3 = prevX3; x4 = prevX4;
                log.append(String.format("  Выход: [X1:%d, X2:%d, X3:%d, X4:%d]\n", x1, x2, x3, x4));

            }
            result[i] = x1; result[i+1] = x2; result[i+2] = x3; result[i+3] = x4;
        }
        if (outputTextEncLog != null) {
            outputTextEncLog.setText(log.toString());
        }

        return new String(result, 0, result.length).trim();
    }

    private int calculateVi(int round) {
        long part1 = Long.rotateLeft(k1, round);
        long part2 = k2 >>> round;
        return (int) (part1 + part2);
    }
}
