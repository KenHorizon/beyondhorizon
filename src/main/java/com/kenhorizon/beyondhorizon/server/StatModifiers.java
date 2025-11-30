package com.kenhorizon.beyondhorizon.server;

public class StatModifiers {
    public static final StatModifiers DEFAULT = new StatModifiers();

    private float baseValue = 0.0F;
    private final float additive;
    private final float multiplicative;
    private final float flat;

    public StatModifiers() {
        this.additive = 1.0F;
        this.multiplicative = 1.0F;
        this.flat = 0.0F;
        this.baseValue = 0.0F;
    }
    public StatModifiers(float baseValue) {
        this.additive = 1.0f;
        this.multiplicative = 1.0F;
        this.flat = 0.0F;
        this.baseValue = baseValue;
    }
    public StatModifiers(float additive, float multiplicative, float flat, float baseValue) {
        this.additive = additive;
        this.multiplicative = multiplicative;
        this.flat = flat;
        this.baseValue = baseValue;
    }

    public float getBase() {
        return baseValue;
    }

    public float getAdditive() {
        return additive;
    }

    public float getMultiplicative() {
        return multiplicative;
    }

    public float getFlat() {
        return flat;
    }
    public StatModifiers add(float add) {
        return new StatModifiers(this.getAdditive() + add, this.getMultiplicative(), this.getFlat(), this.getBase());
    }

    public StatModifiers subtract(float sub) {
        return new StatModifiers(this.getAdditive() - sub, this.getMultiplicative(), this.getFlat(), this.getBase());
    }

    public StatModifiers multiply(float mul) {
        return new StatModifiers(this.getAdditive(), this.getMultiplicative() * mul, this.getFlat(), this.getBase());
    }

    public StatModifiers divide(float div) {
        return new StatModifiers(this.getAdditive(), this.getMultiplicative() / div, this.getFlat(), this.getBase());
    }

    public static StatModifiers add(float add, StatModifiers statModifiers) {
        return statModifiers.add(add);
    }

    public static StatModifiers multiply(float mul, StatModifiers statModifiers) {
        return statModifiers.multiply(mul);
    }

    public StatModifiers combineWith(StatModifiers m) {
        return new StatModifiers(
                this.getAdditive() + m.getAdditive() - 1,
                this.getMultiplicative() * m.getMultiplicative(),
                this.getFlat() + m.getFlat(),
                this.getBase() + m.getBase()
        );
    }

    public StatModifiers scale(float scale) {
        return new StatModifiers(
                1 + (this.getAdditive() - 1) * scale,
                1 + (this.getMultiplicative() - 1) * scale,
                this.getFlat() * scale,
                this.getBase() * scale
        );
    }
    public float applyTo(float baseVal) {
        return (baseVal + this.getBase()) * this.getAdditive() * this.getMultiplicative() + this.getFlat();
    }
}
