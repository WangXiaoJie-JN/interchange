package com.viewcent.data.interchange.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/weimob")
public class WeimobController
{
    Logger logger = LoggerFactory.getLogger(WeimobController.class);
    
    @RequestMapping("/accessHandler")
    public void redirectCallbackHandler(@RequestParam String code, @RequestParam(name = "state", required = false) String state)
    {
        logger.info("code=" + code);
        code = "VlPLBr";
        
    }
    
}
