package com.kenhorizon.beyondhorizon.server.util;

/**<p>Handles all the Calculation within selected value of number
 * <p>Include additively, subraction, multiplication and divide
 * @author KenHorizon
 * @version 1.0
 * */
public record DamageContext(float damage) {

    public float add(final float magnitude) {
        return this.damage() + magnitude;
    }

    public float add(final double magnitude) {
        return (float) (this.damage() + magnitude);
    }

    public float sub(final float magnitude) {
        return this.damage() - magnitude;
    }

    public float sub(final double magnitude) {
        return (float) (this.damage() - magnitude);
    }

    public float divide(final float magnitude) {
        return this.damage() / magnitude;
    }

    public float divide(final double magnitude) {
        return (float) (this.damage() / magnitude);
    }

    public float multiply(final float magnitude) {
        return this.damage() + (this.damage() * magnitude);
    }

    public float multiply(final double magnitude) {
        return (float) (this.damage() + (this.damage() * magnitude));
    }
}
