package com.ecommerce.admin.admin.Controller;

import com.ecommerce.library.Dto.MonthlyOrderCount;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.impl.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value= {"/","/index"},method = RequestMethod.GET)
    public String getHome(Principal principal, Model model){
        try {
            if(principal==null){
                return "redirect:/login";
            }
            List<Map<String, Object>> getOrder=orderService.getMonthlyOrder();
            List<Map<String, Object>> monthlyEarnings=orderService.getMonthlyEarnings();
            double totalPrice=orderService.sumofTotalPrice();
            List<Map<String,Object>> dailyOrder=orderService.getOrdersPerDay();

            model.addAttribute("orderData",getOrder);
            model.addAttribute("monthlyEarnings",monthlyEarnings);
            model.addAttribute("totalPrice",totalPrice);
            model.addAttribute("dailyOrder",dailyOrder);

            return "index";

        }catch (Exception e){
            e.printStackTrace();
            return "index";
        }
    }

    @GetMapping("/sales-report-monthly")
    public ResponseEntity<InputStreamResource> createMonthlySalesReportPdf(){

        ByteArrayInputStream pdf=invoiceService.createMonthlySales();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=lcwd.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/cancel-report-monthly")
    public ResponseEntity<InputStreamResource> createMonthlyCancelReportPdf(){

        ByteArrayInputStream pdf=invoiceService.createMonthlyCancelReport();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=lcwd.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/stock-report")
    public ResponseEntity<InputStreamResource> stockReportPdf(){

        ByteArrayInputStream pdf=invoiceService.createStockReport();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=lcwd.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
