package com.kenhorizon.libs.client;

public enum WeaponArmPose {
    EMPTY(false),
    HOLDING(true),
    HOLDING_ALT(false),
    STAFF(false),
    GUARDIAN_SWORD(true);
    private boolean twoHanded;

    private WeaponArmPose(boolean isTwoHanded) {
        this.twoHanded = isTwoHanded;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public void setTwoHanded(boolean twoHanded) {
        this.twoHanded = twoHanded;
    }
}
