package com.qiu.qiupicturebackend.ai.domain.model;

public class AiCapabilityView {

    private Long id;
    private String capabilityKey;
    private String displayName;
    private String description;
    private String provider;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCapabilityKey() { return capabilityKey; }
    public void setCapabilityKey(String capabilityKey) { this.capabilityKey = capabilityKey; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
