package com.mygdx.game;

import java.util.ArrayList;

public class WaterPowerTimer extends Thread {
    private int waterPowerCount;
    private int maxWaterPowerCount;
    private float resetInterval;
    private float elapsedTime;
    private int waterCounterDrops;
    private ArrayList<Float> waterPowerTimers;
    private boolean isRunning;

    public WaterPowerTimer(int waterPowerCount, int maxWaterPowerCount, float resetInterval) {
        this.waterPowerCount = waterPowerCount;
        this.maxWaterPowerCount = maxWaterPowerCount;
        this.resetInterval = resetInterval;
        this.elapsedTime = 0;
        this.waterCounterDrops = 0;
        this.waterPowerTimers = new ArrayList<>();
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            synchronized (this) {
                elapsedTime += 1.0f; // Simulación de tiempo transcurrido
            }

            if (waterCounterDrops > 0) {
                synchronized (this) {
                    if (elapsedTime >= resetInterval && waterPowerCount < maxWaterPowerCount) {
                        waterPowerCount++;
                        updateCounterLabel();
                        waterPowerTimers.add(elapsedTime);
                        waterCounterDrops--;
                        elapsedTime = 0;
                    }
                }
            }

            for (int i = 0; i < waterPowerTimers.size(); i++) {
                synchronized (this) {
                    float timer = waterPowerTimers.get(i);
                    if (timer >= 5.0f) {
                        // Realiza alguna acción necesaria
                        waterPowerTimers.remove(i);
                    }
                }
            }

            // Otras partes de tu código

            try {
                Thread.sleep(1000); // Espera 1 segundo (simulando un segundo en el juego)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopTimer() {
        isRunning = false;
    }

    public void updateCounterLabel() {
        // Actualiza la etiqueta del contador
    }
}