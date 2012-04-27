package org.agmip.webservices.realdata.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashCode;

import org.agmip.core.types.AdvancedHashMap;

/**
 * @author Christopher Villalobos <cvillalobos@ufl.edu>
 */
@Path("/datasets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RealdataResource {
    private final HashFunction hf = Hashing.sha1();
    private final ArrayList<String> requiredMetadata;

    public RealdataResource() {
        requiredMetadata = new ArrayList<String>(Arrays.asList("exname", "pdate", "cr"));
    }
/**
 * Resource: {@code POST <mount-point>/dataset/new}
 *
 */
    @POST
    public AdvancedHashMap createDataset(@Context UriInfo uriInfo, AdvancedHashMap data) {
        AdvancedHashMap key = data.extract(requiredMetadata);
        AdvancedHashMap status = new AdvancedHashMap<String, String>();
        status.put("status", "ok");
        
        // Do we have enough to build the key (or have all the required metadata)?
        if(key.size() != requiredMetadata.size()) {
            status.put("status", "Validation Error: Missing required metadata.");
            throw new WebApplicationException(Response.status(422).entity(status).build());
        } 
        String hashable = key.get("exname")+"+"+key.get("pdate")+"+"+key.get("cr");
        String id = hf.newHasher()
            .putString(hashable)
            .hash().toString();
        status.put("key", id);
        //status.put("hashcode", hc);
        status.put("data", data);
        return status;
    }

    @GET
    @Path("/{id}")
    public Response readDataset(@PathParam("id") String id,
                                @QueryParam("tc") String timeStamp,
                                @QueryParam("s") String sourceServer,
                                @QueryParam("v") String serverKey) {
        AdvancedHashMap status = new AdvancedHashMap<String, String>();
        int response_status = 200;
        if(timeStamp    == null || 
           sourceServer == null || 
           serverKey    == null) {
            response_status = 400;
            status.put("status", "Missing required data. Please check the API documentation.");
        } else {
            status.put("status", "ok");
        }
        return Response.status(response_status).entity(status).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateDataset(@PathParam("id") String id,
                                  AdvancedHashMap data) {
        // The information in data should include the verification inforation
        // from the servers (only the GET requires it explictly passed).

        final HashCode hc = hf.newHasher()
            .putString(data.toString())
            .hash();
        return Response
            .status(200)
            .entity(new AdvancedHashMap<String, String>(){{
                put("status", "ok");
                put("hash", hc.toString());
            }}).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDataset(@PathParam("id") String id,
                                  AdvancedHashMap data) {
        return Response.status(200).entity(new AdvancedHashMap<String, String>(){{ put("status", "ok");}}).build();
    }
}
