package com.xtree.anatolij.data;

public class FailedEvent extends Event {
    private String message;
    private String reason;

    public FailedEvent(String originator,String message, String reason) {
        this.originator = originator;
        this.message = message;
        this.reason = reason;
    }

    public FailedEvent(String originator, String[] parts, String reason) {
        this(originator,String.join(" ",parts),reason);
    }

    @Override
    public boolean isSignificant() {
        return false;
    }

    @Override
    public boolean notifyNeeded() {
        return false;
    }

    @Override
    public String getAnalyticHeader(Affiliation affiliation) {
        return "tohle se tu nemelo objevit.";
    }

    @Override
    public String getAnalyticMessage(Affiliation affiliation) {
        return "V seru se neco deje neco nepopsatelneho: \""+message+"\" "+reason;
    }

    @Override
    public String getMeta() {
        return "Nekdo neco posral a poslal: "+message;
    }

    @Override
    public String getConfirmationMessage() {
        return "Text "+ reason + "("+message+")";
    }

    @Override
    public Event dependentEvent() {
        return null;
    }
}
