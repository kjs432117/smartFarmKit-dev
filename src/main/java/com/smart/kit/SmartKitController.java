package com.smart.kit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmartKitController {

	@Autowired
	SmartKit smartKit;

	@RequestMapping(method = RequestMethod.GET, path = "/startGrow")
	public String startGrow(HttpServletRequest request, Model model) {
		int temp = Integer.parseInt(request.getParameter("temp"));
		int hum = Integer.parseInt(request.getParameter("hum"));
		int light = Integer.parseInt(request.getParameter("light"));
		int water = Integer.parseInt(request.getParameter("water"));
		int pes = Integer.parseInt(request.getParameter("pes"));

		String result = smartKit.startGrow(temp, hum, light, water, pes);

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/stopGrow")
	public String stopGrow() {

		String result = smartKit.stopGrow();

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/changeValue")
	public String changeValue(HttpServletRequest request) {
		int temp = Integer.parseInt(request.getParameter("temp"));
		int hum = Integer.parseInt(request.getParameter("hum"));
		int light = Integer.parseInt(request.getParameter("light"));
		int water = Integer.parseInt(request.getParameter("water"));
		int pes = Integer.parseInt(request.getParameter("pes"));

		String result = smartKit.changeValue(temp, hum, light, water, pes);

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/checkGrow")
	public String checkValue() {

		if (smartKit.getStatus() == 0) {
			return "Kit Stand-by";
			
		} else if (smartKit.getStatus() == 1) {
			return smartKit.toString();
			
		} else {
			return "Error";
			
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/changeEnv")
	public String testKit(HttpServletRequest request) {

		smartKit.changeEnv(Integer.parseInt(request.getParameter("currentTemp")),
				Integer.parseInt(request.getParameter("currentHum")));

		return "Test Value Sent";
	}

}
