package com.example.demo.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SequenceUtils {


	@Autowired
	private CacheService cacheService;
	
    private static final int DEFAULT_LENGTH = 9;

    public static String getSequence(long seq) {
        String str = String.valueOf(seq);
        int len = str.length();
        if (len >= DEFAULT_LENGTH) {// 取决于业务规模,应该不会到达3
            return str;
        }
        int rest = DEFAULT_LENGTH - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    public String getAutoFlowCode() {
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String currentDate2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Long num = cacheService.getIncrementNum(""+currentDate);
        String flowCode = SequenceUtils.getSequence(num);
        return currentDate2 + "**" + flowCode;
    }
    
    
}
