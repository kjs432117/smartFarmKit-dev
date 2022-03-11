package com.smart.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SmartKitController {

	@Autowired
	SmartKit smartKit;

	//재배시작
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
	//재배중지
	@RequestMapping(method = RequestMethod.GET, path = "/stopGrow")
	public String stopGrow() {

		String result = smartKit.stopGrow();

		return result;
	}
	//재배완료
	@RequestMapping(method = RequestMethod.GET, path = "/complete")
	public String complete() {
		String result = smartKit.completeGrow();

		return result;
	}	
	//재배값변경
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
	@ResponseBody
	public Map<String, String> checkValue() {
		
		Map<String, String> growInfo = new HashMap<>();
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		Date currentTime = new Date();
		String now;
		now = format.format(currentTime);

			
			Date s_date = null;
			Date c_date = null;
			Date e_date = null;
			int diffDays = 0;
			
			if(SmartKit.startDate!=null) {
				try {
					
					s_date = SmartKit.startDate;
					cal.setTime(s_date);
					cal.add(Calendar.DATE, SmartKit.requiredDate);
					c_date = format.parse(now);
					e_date = format.parse(format.format(cal.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				float diffSec = (c_date.getTime() - s_date.getTime()) / 1000; //초 차이
				System.out.println(diffSec);
				diffDays = Math.round(diffSec*100/(SmartKit.requiredDate)); //일자수 차이
//				diffDays = Math.round(diffSec*100/(60*SmartKit.requiredDate)); //일자수 차이
//				diffDays = Math.round(diffSec*100/(24*60*60*SmartKit.requiredDate)); //일자수 차이
				System.out.println(diffDays);
			}
			
		if (smartKit.getStatus() == 0) {
			growInfo.put("message", "Kit Stand-by");
			growInfo.put("status", Integer.toString(smartKit.getStatus()));
			growInfo.put("id", Logger.DEVICE_ID);
			growInfo.put("percent", "재배를 시작해 주세요");
			return growInfo;
			
		} else if (smartKit.getStatus() == 2) {
			growInfo.put("message", "Ready for complete");
			System.out.println(SmartKit.format.format(SmartKit.startDate));
			growInfo.put("status", Integer.toString(smartKit.getStatus()));
			growInfo.put("startDate", SmartKit.format.format(SmartKit.startDate));
			growInfo.put("temp", Integer.toString(smartKit.getTemp()));
			growInfo.put("hum", Integer.toString(smartKit.getHum()));
			growInfo.put("light", Integer.toString(smartKit.getLight()));
			growInfo.put("water", Integer.toString(smartKit.getWater()));
			growInfo.put("pes", Integer.toString(smartKit.getPes()));
			growInfo.put("id", Logger.DEVICE_ID);
			growInfo.put("required", Integer.toString(SmartKit.requiredDate));
			growInfo.put("end", format.format(e_date));
			growInfo.put("percent", Integer.toString(100));
			return growInfo;
			
		} else if (smartKit.getStatus() == 1) {
			growInfo.put("message", "On growing");
			System.out.println(SmartKit.format.format(SmartKit.startDate));
			growInfo.put("status", Integer.toString(smartKit.getStatus()));
			growInfo.put("startDate", SmartKit.format.format(SmartKit.startDate));
			growInfo.put("temp", Integer.toString(smartKit.getTemp()));
			growInfo.put("hum", Integer.toString(smartKit.getHum()));
			growInfo.put("light", Integer.toString(smartKit.getLight()));
			growInfo.put("water", Integer.toString(smartKit.getWater()));
			growInfo.put("pes", Integer.toString(smartKit.getPes()));
			growInfo.put("id", Logger.DEVICE_ID);
			growInfo.put("required", Integer.toString(SmartKit.requiredDate));
			growInfo.put("end", format.format(e_date));
			growInfo.put("percent", Integer.toString(diffDays));
			return growInfo;
			
		} else {
			growInfo.put("message", "Error");
			growInfo.put("id", Logger.DEVICE_ID);
			return growInfo;
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/changeEnv")
	public String testKit(HttpServletRequest request) {

		smartKit.changeEnv(Integer.parseInt(request.getParameter("currentTemp")),
				Integer.parseInt(request.getParameter("currentHum")));

		return "Test Value Sent";
	}

}
