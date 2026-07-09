package io.github.jeongdonghee.excelkit.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class DashboardExcelTest {

    @Autowired
    MockMvc mvc;

    @Test
    void dashboardEndpointReturnsWorkbookWithCharts() throws Exception {
        MvcResult result = mvc.perform(get("/dashboard/excel"))
                .andExpect(status().isOk())
                .andReturn();

        String contentType = result.getResponse().getContentType();
        assertTrue(contentType != null && contentType.contains("spreadsheetml.sheet"), contentType);

        try (XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(result.getResponse().getContentAsByteArray()))) {
            XSSFSheet charts = wb.getSheet("Charts");
            assertTrue(charts != null, "'Charts' 시트가 있어야 한다");
            // 바 + 파이 = 차트 2개가 그려졌는지
            assertEquals(2, charts.getDrawingPatriarch().getCharts().size());
        }
    }
}
