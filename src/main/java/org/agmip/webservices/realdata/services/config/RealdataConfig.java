package org.agmip.webservices.realdata.services.config;

import org.agmip.webservices.core.services.config.RiakConfig;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonProperty;

public class RealdataConfig {
    // Nothing needed at the moment
    @NotNull
    @JsonProperty
    private RiakConfig riakConfig = new RiakConfig();

    public RiakConfig getRiakConfig() {
        return riakConfig;
    }
}
