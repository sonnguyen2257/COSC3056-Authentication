package com.example.climatechange.project;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test{
    @RequestMapping("/")
    @ResponseBody
	public String indexPage() {
		String body= "<div>Username: "+UserMaintenance.username+"</div>";
        body+= "<div>UserabletoDownload: "+UserMaintenance.ableToDownload+"</div>";
        body+= "<div>UserDarkMode: "+UserMaintenance.darkMode+"</div>";

        return body;
	}
}
