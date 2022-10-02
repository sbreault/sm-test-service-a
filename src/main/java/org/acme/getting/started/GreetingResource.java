package org.acme.getting.started;

import org.eclipse.microprofile.config.ConfigProvider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/service-a")
public class GreetingResource {

    private static String HOSTNAME;
    private static int requestCounter;

    String appVersion = ConfigProvider.getConfig().getValue("app.version", String.class);
    int waitSec = ConfigProvider.getConfig().getValue("app.wait.duration.ms.int", Integer.class);
    int waitCounter = ConfigProvider.getConfig().getValue("app.wait.exec.count.int", Integer.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        GreetingResource.requestCounter++;

        if(isMustWait()){
            System.out.println("issuing wait...");
            wait(getWaitTime());
        }
        return "Why did the chicken cross the road? (" + getIdentification() + ").";
    }

    private int getWaitTime(){
        return waitSec > 0 ? waitSec : 2; //default 2 sec
    }

    private boolean isMustWait(){
        //use counter to determine when to wait (i.e. every 3 requests)
        boolean wait = (GreetingResource.requestCounter == waitCounter);
        if(wait) GreetingResource.requestCounter = 0;  //reset counter
        return wait;
    }

    private void wait(int waitDuration){
        try{
            System.out.println("sleep..." + waitDuration);
            Thread.sleep(waitDuration);
            System.out.println("...end sleep.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getIdentification(){
        return appVersion + "; " + HOSTNAME;
    }

    private String getHostName(){
        if(GreetingResource.HOSTNAME == null){
            try{    
                GreetingResource.HOSTNAME = System.getenv("HOSTNAME");
                if(GreetingResource.HOSTNAME == null) 
                    GreetingResource.HOSTNAME = System.getenv("COMPUTERNAME");  //Windows desktop name
                
                System.out.println("HOSTNAME=" + GreetingResource.HOSTNAME);
                
                //max 128 char
                if(GreetingResource.HOSTNAME != null && GreetingResource.HOSTNAME.length() > 128){                    
                    GreetingResource.HOSTNAME = GreetingResource.HOSTNAME.substring(0, 127);
                }                
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if(GreetingResource.HOSTNAME == null) GreetingResource.HOSTNAME = "/local PC/not hosted";
        return GreetingResource.HOSTNAME;
    }     
}