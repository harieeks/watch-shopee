package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    public ByteArrayInputStream createPdf(Long id){

        Order order=orderService.findOrderById(id);

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();

        PdfWriter.getInstance(document,out);
        document.open();

        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);
        Font fontHeading = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        Paragraph titlePara=new Paragraph("Order Invoice",fontTitle);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        Paragraph nameHeadingPara=new Paragraph("Customer name: ",fontHeading);
        Paragraph namePara=new Paragraph(order.getCustomer().getName());
        document.add(nameHeadingPara);
        document.add(namePara);
        Paragraph spacingPara = new Paragraph("\n");
        document.add(spacingPara);


        Paragraph addressAndPhonePara = new Paragraph();
        addressAndPhonePara.add(new Paragraph("Address:", fontHeading));
        addressAndPhonePara.add(new Paragraph(order.getAddress().getHouseName()));
        addressAndPhonePara.add(new Paragraph(order.getAddress().getCity()));
        addressAndPhonePara.add(new Paragraph(order.getAddress().getDistrict()));
        addressAndPhonePara.add(new Paragraph(order.getAddress().getState()));
        addressAndPhonePara.add(new Paragraph(order.getAddress().getPinCode()));
        addressAndPhonePara.add(new Paragraph(order.getCustomer().getPhoneNumber()));
        addressAndPhonePara.add(spacingPara);
        document.add(addressAndPhonePara);

        Paragraph productAndQuantity= new Paragraph();
        productAndQuantity.add(new Paragraph("Order Details:",fontHeading));
        for(OrderDetails orderDetails: order.getOrderDetails()){
            String product="Product name : "+orderDetails.getProduct().getName();
            productAndQuantity.add(new Paragraph(product));
            String quantity="Quantity : "+orderDetails.getQuantity();
            productAndQuantity.add(new Paragraph(quantity));
        }
        document.add(productAndQuantity);

        document.add(spacingPara);
        Paragraph total=new Paragraph();
        String amount="Total : "+order.getTotalPrice();
        total.add(new Paragraph(amount));
        document.add(total);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream createMonthlySales(){

        List<Map<String,Object>> monthlyReport=orderService.getMonthlyOrder();

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();

        PdfWriter.getInstance(document,out);
        document.open();

        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);

        Paragraph titlePara=new Paragraph("Monthly Sales report",fontTitle);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        for(Map<String,Object> entry: monthlyReport){
            String month= (String) entry.get("month");
            Long count= (Long) entry.get("orderCount");
            String orderCount= String.valueOf(count);
            Paragraph reportPara=new Paragraph();
            reportPara.add(new Paragraph(month));
            reportPara.add(new Paragraph(orderCount));
            document.add(reportPara);

            Paragraph spacingPara = new Paragraph("\n");
            document.add(spacingPara);
        }
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream createMonthlyCancelReport(){

        List<Map<String,Object>> cancelReport=orderService.getMonthlyCancelReport();

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();

        PdfWriter.getInstance(document,out);
        document.open();

        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);

        Paragraph titlePara=new Paragraph("Monthly cancel report",fontTitle);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        for(Map<String,Object> entry: cancelReport){
            String month= (String) entry.get("month");
            Long count= (Long) entry.get("count");
            String counts= String.valueOf(count);
            Paragraph reportPara=new Paragraph();
            reportPara.add(new Paragraph(month));
            reportPara.add(new Paragraph(counts));
            document.add(reportPara);

            Paragraph spacingPara = new Paragraph("\n");
            document.add(spacingPara);
        }
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream createStockReport(){

        List<Map<String,Object>> stockReport=productService.salesReport();

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();

        PdfWriter.getInstance(document,out);
        document.open();

        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);

        Paragraph titlePara=new Paragraph("Stock report",fontTitle);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        for(Map<String,Object> entry: stockReport){
            String name= (String) entry.get("productName");
            int quantity= (int) entry.get("productQuantity");
            String counts= String.valueOf(quantity);
            Paragraph reportPara=new Paragraph();
            reportPara.add(new Paragraph("Product name: "+name));
            reportPara.add(new Paragraph("Quantity :"+counts));
            document.add(reportPara);

            Paragraph spacingPara = new Paragraph("\n");
            document.add(spacingPara);
        }
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
