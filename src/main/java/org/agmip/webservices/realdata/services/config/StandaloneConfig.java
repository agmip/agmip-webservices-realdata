package org.agmip.webservices.realdata.services.config;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.validation.constraints.NotNull;

public class StandaloneConfig extends Configuration {
    @NotNull
    @JsonProperty
    private RealdataConfig realdataService = new RealdataConfig();

    public RealdataConfig getRealdataConfig() {
        return realdataService;
    }
}
