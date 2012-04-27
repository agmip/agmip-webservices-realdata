package org.agmip.webservices.realdata.services;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;
import org.agmip.webservices.realdata.services.config.StandaloneConfig;
import org.agmip.webservices.realdata.resources.RealdataResource;

public class RealdataService extends Service<StandaloneConfig> {
    public static void main(String[] args) throws Exception {
        new RealdataService().run(args);
    }

    private RealdataService() {
        super("realdata");
    }

    @Override
    protected void initialize(StandaloneConfig config, Environment env) {
        env.addResource(new RealdataResource());
    }
}
