package com.smart.kit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SmartKit {
	
	@Autowired
	Logger logger;
	
	@Autowired
	Evaluator eva;
	
	private int status = 0;
	private Date startDate;

	private int temp;
	private int hum;
	private int light;
	private int water;
	private int pes;
	
	private int currentTemp;
	private int currentHum;
	
	private String lightStatus;
	private String latestWaterTime;
	private String latestPesTime;
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	static SimpleDateFormat formatCal = new SimpleDateFormat("HH");
	
	static String DEVICE_ID = "1";
	static int requiredDate = 14;
	
	static String currentDay;
	static Date currentTime;

	private Timer m_timer;
	
//	싱글톤 인스턴스
//	private static final SmartKit instance = new SmartKit();
//	인스턴스호출
//	public static SmartKit getInstance() {
//		return instance;
//	}
//	싱글톤 생성자
//	private SmartKit() {
//	}

//	재배시작
	public String startGrow(int temp, int hum, int light, int water, int pes) {
		
		if(status==0) {
			List<String> log = new ArrayList<>();
			
			startDate = new Date();
			
			log.add(format.format(startDate)+"재배 시작 설정값(온도:"+temp+"습도:"+hum+"일사량:"+light+"급액량:"+water+"농약량:"+pes+")");
			
			m_timer = new Timer();
			
			this.status = 1;
			this.temp = temp;
			this.hum = hum;
			this.light = light;
			this.water = water;
			this.pes = pes;
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, requiredDate);
			
			String endDay = format.format(cal.getTime());
			
			
			
			TimerTask m_task = new TimerTask() {
				
				@Override
				public void run() {
					
					if (endDay.equals(currentDay)) {
						
						stopGrow();
					} else {
						
						currentTime = new Date();
						currentDay = format.format(currentTime);
						System.out.println(endDay);
						System.out.println(currentDay);
						
						if(SmartKit.this.temp != currentTemp) {
							log.add(currentDay + "온도 변화 감지 " + currentTemp);
							log.add(currentDay + thermoStat());
							log.add(currentDay + "현재 온도 " + currentTemp);
							log.add(currentDay + "온도 영향인자 발생");
							eva.evaluateTemp(SmartKit.this.temp);
						}
						if(SmartKit.this.hum != currentHum) {
							log.add(currentDay + "습도 변화 감지 " + currentHum);
							log.add(currentDay + humController());
							log.add(currentDay + "현재 습도 " + currentHum);
							eva.evaluateHum(SmartKit.this.hum);
						}
						if(formatCal.format(currentTime).equals("00")) {
							
							log.add(currentDay + lightController(1));
							System.out.println(waterController());
							System.out.println(pesController());
						}
						if(formatCal.format(currentTime).equals(Integer.toString(light))) {
							
							log.add(currentDay + lightController(0));
						}
					}
				}
			};
			
			TimerTask m_task2 = new TimerTask() {
				
				@Override
				public void run() {
					
					currentTime = new Date();
					
					log.add(format.format(currentTime) + eva.evaluateAll(SmartKit.this.temp, SmartKit.this.hum, SmartKit.this.light, SmartKit.this.water, SmartKit.this.pes)); 				
					
					logger.write(log);
					
					log.clear();
					System.out.println("sent log");
				}
			};
			
			m_timer.schedule(m_task, 0, 1000);
			
			m_timer.schedule(m_task2, 0, 10000);
			
			return "Grow Started";
		} else if(status==1) {
			
			return "Already Started";
		} else {
			
			return "Error";
		}
		
	}
	
//	재배중지
	public String stopGrow() {
		if(status == 0) {
			return "Already Stopped";
		}else if(status == 1) {
			status = 0;
			temp = 0;
			hum = 0;
			light = 0;
			water = 0;
			pes = 0;
			m_timer.cancel();
			return "Stop Grow";
		}else {
			return "Error";
		}
	}
	
//	재배값 변경
	public String changeValue(int temp, int hum, int light, int water, int pes) {
		if(status == 0) {
			return "Start Grow First";
		}else if(status == 1) {
			this.temp = temp;
			this.hum = hum;
			this.light = light;
			this.water = water;
			this.pes = pes;
			
			return "Grow Value Changed";
		}else {
			return "Error";
		}
	}
	
	public String thermoStat() {
		
		currentTemp = temp;
		return "Operate ThermoStat";
	}
	
	public String humController() {
		
		currentHum = hum;
		return "Operate All-In-One Humidifier&Dehumidifier";
	}
	
	public String lightController(int a) {
		
		if (a == 0) {
			lightStatus = "Off";
		}else if (a == 1) {
			lightStatus = "On";
		}else {
			return "error";
		}
		return "Light is" + lightStatus;
	}

	public String waterController() {
		
		latestWaterTime = new Date().toGMTString();
		
		return water + "mL Watered at" + latestWaterTime;
	}
	
	public String pesController() {
		
		latestPesTime = new Date().toGMTString();
		
		return pes + "Sprayed at" + latestPesTime;
	}

	public void changeEnv(int t, int h) {
		currentTemp = t;
		currentHum = h;
	}
	
}
