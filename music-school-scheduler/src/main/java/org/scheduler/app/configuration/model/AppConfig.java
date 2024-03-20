package org.scheduler.app.configuration.model;

import static org.scheduler.app.utilities.GsonHelper.loadConfig;

public class AppConfig {
    public String appName;

    public BusinessHours businessHours;

    public String getAppName() {
        return appName;
    }

    public void setBusinessHours(BusinessHours businessHours) {
        this.businessHours = businessHours;
    }

    public synchronized BusinessHours getBusinessHours() {
        if (businessHours == null) {
            AppConfig config = loadConfig(AppConfig.class);
            setBusinessHours(config.businessHours);
        }
        return businessHours;
    }
}

