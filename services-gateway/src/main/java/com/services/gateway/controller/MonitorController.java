package com.services.gateway.controller;


import com.services.gateway.responses.MonitorResponse;

import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

	private static Logger LOGGER = Logger.getLogger(MonitorController.class.getName());

    @RequestMapping(value = "/checkstatus", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MonitorResponse checkStatus() {
    	LOGGER.log(Level.INFO, "Called checkStatus");
        return new MonitorResponse(200, "Status OK", System.currentTimeMillis());
    }
}
