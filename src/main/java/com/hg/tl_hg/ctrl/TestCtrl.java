package com.hg.tl_hg.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCtrl {

	@RequestMapping("/test")
	@ResponseBody
	public Object test() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		return result;
	}
}
