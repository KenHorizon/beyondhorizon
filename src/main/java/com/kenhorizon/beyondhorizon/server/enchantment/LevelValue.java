package com.kenhorizon.beyondhorizon.server.enchantment;

public record LevelValue(double amount) {

    public double levelPerBased(double amount, int level) {
        return this.amount() + (amount * level);
    }

    public double levelBased(int level) {
        return this.amount() * level;
    }
}
