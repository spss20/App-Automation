package com.ssoftwares.appmaker.modals;

public class Step {


    private int order;
    private String stepName;
    private String stepSlug;
    private String stepMessage;

    public Step() {
    }

    public Step(int order, String stepName, String stepSlug, String stepMessage) {
        this.order = order;
        this.stepName = stepName;
        this.stepSlug = stepSlug;
        this.stepMessage = stepMessage;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepSlug() {
        return stepSlug;
    }

    public void setStepSlug(String stepSlug) {
        this.stepSlug = stepSlug;
    }

    public String getStepMessage() {
        return stepMessage;
    }

    public void setStepMessage(String stepMessage) {
        this.stepMessage = stepMessage;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
