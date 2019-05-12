package com.example.demo;

import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductInfo;
import com.example.demo.bean.ProductSummary;
import com.example.demo.repository.DeviceInfoRepository;
import com.example.demo.repository.ProductInfoRepository;
import com.example.demo.service.ProductInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private DeviceInfoRepository deviceInfoRepository;

	@Autowired
	private ProductInfoService productInfoService;


	@Test
	public void contextLoads() {
		List<ProductSummary> macSnAndBatchIds = productInfoRepository.getMacSnAndBatchId();
		for(ProductSummary productSummary : macSnAndBatchIds) {
			System.out.println("macSn:" + productSummary.getMacSn() + "   batchId:" + productSummary.getBatchId());
			System.out.println();
		}
	}

	@Test
	public void test1() {
		ProductInfo productInfo = productInfoRepository.findFirstByBatchIdAndMacSn("201905090917214000000002", "aa11111");
		System.out.println("batId:  " + productInfo.getBatchId() + "	macSn:  " + productInfo.getMacSn());

	}

	@Test
	public void test02() {
		List<DeviceInfo> des = deviceInfoRepository.findByBatchIdAndMacSn("201905090917918000000003", "bb11111");
		for(DeviceInfo deviceInfo : des) {
			System.out.println("BatchId:" + deviceInfo.getBatchId() + "	MacSn: " + deviceInfo.getMacSn());
			System.out.println();
		}
	}


	@Test
	public void test03() {
		List<ProductInfo> allProducts = productInfoService.getAllProduct();
	}

	@Test
	public void test04() {
		List<ProductSummary> pros = deviceInfoRepository.getMacSnAndBatchId("bdafa", "adafa");

		for (ProductSummary pro : pros) {
			System.out.println("macSn:" + pro.getMacSn() + "  batchId;" + pro.getBatchId());
		}

	}

	@Test
	public void test05() {
		//"2017-1-1 00:00:00"
		List<ProductSummary> macSnAndBatchId = productInfoRepository.getMacSnAndBatchId("", "",
				"", "", "",
				"2019-05-10 07:19:37", "2029-05-10 07:19:37");

		for (ProductSummary productSummary : macSnAndBatchId) {
			System.out.println("batchId:" + productSummary.getBatchId() + "  macSn:" + productSummary.getMacSn());
		}

	}


	//待测试
	@Test
	public void test06() {
		productInfoRepository.getMacSnAndBatchId("", "",
													"", "", "",
														"", "",
															"bdafa", "b32");
	}

}
