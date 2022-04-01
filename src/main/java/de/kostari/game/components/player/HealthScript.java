package de.kostari.game.components.player;

import de.kostari.cloud.core.components.Component;

public class HealthScript extends Component {

    public float maxHealth, currentHealth;

    public HealthScript(float maxHealth, float currentHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
    }

    public HealthScript(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public float getHealthPercentage() {
        return currentHealth / maxHealth;
    }

    public void damage(float damage) {
        currentHealth -= damage;
    }

    public void heal(float heal) {
        float difference = maxHealth - currentHealth;
        currentHealth += heal > difference ? difference : heal;
    }
}
