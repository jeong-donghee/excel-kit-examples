package io.github.jeongdonghee.excelkit.examples;

import java.io.IOException;
import java.util.List;

import io.github.jeongdonghee.excelkit.canvas.ChartCategory;
import io.github.jeongdonghee.excelkit.canvas.ChartColor;
import io.github.jeongdonghee.excelkit.canvas.ChartSeries;
import io.github.jeongdonghee.excelkit.canvas.ChartType;
import io.github.jeongdonghee.excelkit.canvas.ExcelCanvas;
import io.github.jeongdonghee.excelkit.canvas.ExcelChart;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * excel-kit-canvas 예제 — 차트 대시보드를 엑셀로 다운로드.
 *
 * <p>canvas는 프로그래매틱 API(스프링 통합 없음)라, 컨트롤러에서 직접 응답 스트림에 쓴다.
 * (grid의 {@code @ExcelDownload}처럼 자동 다운로드는 목록 익스포트 전용)
 */
@RestController
public class DashboardController {

    // 좌상단 — 바 차트 (2개 시리즈)
    @ExcelChart(type = ChartType.BAR, title = "월별 매출", col = 0, row = 0, width = 8, height = 14)
    static class Sales {
        @ChartCategory String month;
        @ChartSeries(name = "매출") int revenue;
        @ChartSeries(name = "비용") int cost;
        Sales(String m, int r, int c) { month = m; revenue = r; cost = c; }
    }

    // 우측 — 파이 차트, 슬라이스별 색 지정(@ChartColor)
    @ExcelChart(type = ChartType.PIE, title = "트래픽 구성", col = 10, row = 0, width = 6, height = 12)
    static class Traffic {
        @ChartCategory String source;
        @ChartSeries int value;
        @ChartColor String color;
        Traffic(String s, int v, String c) { source = s; value = v; color = c; }
    }

    @GetMapping("/dashboard/excel")
    public void dashboard(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=dashboard.xlsx");

        ExcelCanvas.create()
                .chart(List.of(
                        new Sales("1월", 100, 80),
                        new Sales("2월", 150, 90),
                        new Sales("3월", 130, 110)))
                .chart(List.of(
                        new Traffic("검색", 50, "#4E79A7"),
                        new Traffic("직접", 30, "#F28E2B"),
                        new Traffic("추천", 20, "#59A14F")))
                .writeTo(response.getOutputStream());
    }
}
