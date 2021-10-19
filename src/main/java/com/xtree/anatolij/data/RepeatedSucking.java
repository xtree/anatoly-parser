package com.xtree.anatolij.data;

public class RepeatedSucking extends Event{
    private String vampire;

    public RepeatedSucking(String vampire) {

        this.vampire = vampire;
    }

    @Override
    public boolean isSignificant() {
        return false;
    }

    @Override
    public boolean notifyNeeded() {
        return true;
    }

    @Override
    public String getAnalyticHeader(Affiliation affiliation) {
        return null;
    }

    @Override
    public String getAnalyticMessage(Affiliation affiliation) {
        return null;
    }

    @Override
    public String getMeta() {
        return "podezrele opakovane cerpani "+vampire;
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public Event dependentEvent() {
        return null;
    }
}
