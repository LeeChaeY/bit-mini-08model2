package com.model2.mvc.web.products;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

import oracle.net.aso.f;


//==> 회원관리 Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
//	@Value("#{commonProperties['pageUnit']}")
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
//	@Value("#{commonProperties['pageSize']}")
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping(value="addProduct", method=RequestMethod.GET )
	public String addProductView() throws Exception {

		System.out.println("/product/addProduct : GET");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct( @RequestParam("file") MultipartFile file, @ModelAttribute("product") Product product , Model model, 
						HttpServletRequest request) throws Exception {
			System.out.println("addProduct : POST : "+product);	
			
			if (!file.isEmpty()) {
	            try {
	                // 업로드된 파일 저장
	            	String uploadDir = "C:\\work\\03.git\\bit-mini-09model2\\09.Model2MVCShop(stu)\\src\\main\\\\webapp\\images\\uploadFiles\\";
//	                String uploadDir = "\\images\\uploadFiles\\"; // 실제 경로로 변경해야 합니다.
	                File uploadFile = new File(uploadDir, file.getOriginalFilename());
	                file.transferTo(uploadFile);
	                
	                product.setFileName(file.getOriginalFilename());
	                // 파일 업로드 성공 메시지 등을 처리하거나 다른 작업을 수행합니다.
	                product.setManuDate(product.getManuDate().replace("-", ""));
	                
	                productService.addProduct(product);
	    			model.addAttribute("product", product);
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	                // 파일 업로드 실패 처리
	                System.out.println("<scrpt>alert('파일의 크기는 10MB까지 입니다.");
	            }
	        } else {
	            // 업로드된 파일이 없는 경우 처리
	        	System.out.println("인코딩 타입이 multipart/form-data가 아닙니다..");
	        }	
			
		return "forward:/product/addProduct.jsp";
	}
	
//	@RequestMapping(value="addProduct", method=RequestMethod.POST)
//	public String addProduct( @ModelAttribute("product") Product product , Model model, HttpServletRequest request) throws Exception {
//		System.out.println("addProduct : POST : "+product);
//
//		if (FileUpload.isMultipartContent(request)) {
////			String temDir2 = "/image/uploadFiles/";
//			String temDir2 = "C:\\work\\03.git\\bit-mini-09model2\\09.Model2MVCShop(stu)\\src\\main\\webapp\\images\\uploadFiles\\";
//			
////			product.setManuDate(product.getManuDate().replace("-", ""));
//			
//			System.out.println("addProduct : POST : "+product);
//			
//			DiskFileUpload fileUpload = new DiskFileUpload();
//			fileUpload.setRepositoryPath(temDir2);
//			fileUpload.setSizeMax(1024 * 1024 * 100);
//			fileUpload.setSizeThreshold(1024 * 100);
//			
//			System.out.println(request.getContentLength()+", "+fileUpload.getSizeMax());
//			if(request.getContentLength() < fileUpload.getSizeMax()) {
//				StringTokenizer token = null;
//				
//				List fileItemList = fileUpload.parseRequest(request);
//				int size = fileItemList.size();
//				
//				for (int i=0; i<size; i++) {
//					FileItem fileItem = (FileItem)fileItemList.get(i);
//					
//					if(fileItem.isFormField()) {
//						if(fileItem.getFieldName().equals("manuDate")) {
//							token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
//							String manuDate = token.nextToken() + token.nextToken() + token.nextToken();
//							product.setManuDate(manuDate);
//						} 
//						else if (fileItem.getFieldName().equals("prodName"))
//							product.setProdName(fileItem.getString("euc-kr"));
//						else if (fileItem.getFieldName().equals("prodDetail"))
//							product.setProdDetail(fileItem.getString("euc-kr"));
//						else if (fileItem.getFieldName().equals("price"))
//							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
//					} else {
//						if (fileItem.getSize() > 0) {
//							int idx = fileItem.getName().lastIndexOf("\\");
//							if (idx == -1)
//								idx =  fileItem.getName().lastIndexOf("/");
//							String fileName = fileItem.getName().substring(idx + 1);
//							product.setFileName(fileName);
//							try {
//								File uploadedFile = new File(temDir2, fileName);
//								fileItem.write(uploadedFile);
//							} catch (IOException e) {
//								System.out.println(e);
//							}
//						} else {
//							product.setFileName("../../images/empty.GIF");
//						}
//					}
//				}
//				productService.addProduct(product);
//				model.addAttribute("product", product);
//			} else {
//				int overSize = (request.getContentLength() / 1000000);
//				System.out.println("<scrpt>alert('파일의 크기는 1MB까지 입니다. 올리신 파일 용량은"
//						+ overSize + "MB입니다');");
//				System.out.println("history.back();</script>");
//			}
//		} else {
//			System.out.println("인코딩 타입이 multipart/form-data가 아닙니다..");
//		}
//		return "forward:/product/addProduct.jsp";
//	}
	
	@RequestMapping(value="getProduct", method=RequestMethod.GET )
	public String getProduct(HttpServletRequest request, HttpServletResponse response,  
									@RequestParam("prodNo") int prodNo, @RequestParam("menu") String menu , Model model ) throws Exception {
		String resultPage = "forward:/product/getProduct.jsp";
		System.out.println("getProduct : GET : prodNo : "+prodNo+", menu : "+menu);
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		model.addAttribute("menu", menu);
		
		if (menu.equals("manage") && product.getProTranCode() == null) 
			resultPage = "redirect:/product/updateProduct?prodNo="+prodNo;

		Cookie[] cookies = request.getCookies();
		String history = null;
		if (cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("history")) {
					history = cookie.getValue();
				}
			}
			if (history != null) {
				List<String> h = new ArrayList<String>(Arrays.asList(history.split(URLEncoder.encode(","))));
				if (h.indexOf(prodNo+"") != -1) h.remove(h.indexOf(prodNo+""));
				h.add(""+prodNo);
				history = String.join(URLEncoder.encode(","), h);
			}
			else {
				history = prodNo+"";
			}
		}
		
		System.out.println("getProduct :: cookie history : "+history);
		Cookie cookie = new Cookie("history", history);
		cookie.setMaxAge(60*60);
		response.addCookie(cookie);
		
		return resultPage;
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.GET )
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("updateProduct : GET : "+prodNo);
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @RequestParam("file") MultipartFile file, @ModelAttribute("product") Product product) throws Exception{
		String menu = "search";
		
		System.out.println("updateProduct : POST : "+product);
		
		if (!file.isEmpty()) {
            try {
                // 업로드된 파일 저장
            	String uploadDir = "C:\\work\\03.git\\bit-mini-09model2\\09.Model2MVCShop(stu)\\src\\main\\\\webapp\\images\\uploadFiles\\";
//                String uploadDir = "\\images\\uploadFiles\\"; // 실제 경로로 변경해야 합니다.
                File uploadFile = new File(uploadDir, file.getOriginalFilename());
                file.transferTo(uploadFile);
                
                product.setFileName(file.getOriginalFilename());
                
                product.setManuDate(product.getManuDate().replace("-", ""));
                
                //Business Logic
        		productService.updateProduct(product);
                
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 업로드 실패 처리
                System.out.println("<scrpt>alert('파일의 크기는 10MB까지 입니다.");
            }
        } else {
            // 업로드된 파일이 없는 경우 처리
        	System.out.println("인코딩 타입이 multipart/form-data가 아닙니다..");
        }
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo()+"&menu="+menu;
	}
	
	@RequestMapping(value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search, @RequestParam("menu") String menu , 
											Model model , HttpServletRequest request) throws Exception{
		String beginPrice = null;
		String endPrice = null;
		
		if(request.getParameter("beginPrice") != null && !request.getParameter("beginPrice").equals("") 
				&& request.getParameter("endPrice") != null &&!request.getParameter("endPrice").equals("")) {
			search.setSearchCondition("2");
			beginPrice = request.getParameter("beginPrice");
			model.addAttribute("beginPrice", beginPrice);
			endPrice = request.getParameter("endPrice");
			model.addAttribute("endPrice", endPrice);
		} else if (search.getSearchKeyword() != null && !search.getSearchKeyword().equals("")) {
			search.setSearchCondition("1");
		}
		System.out.println("listProduct : GET / POST : "+search);
		System.out.println("listProduct : beginPrice : "+beginPrice+", endPrice : "+endPrice);
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		String searchKeyword = null;
		if(search.getSearchKeyword() != null && !search.getSearchKeyword().equals(""))
			searchKeyword = search.getSearchKeyword();
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		if(search.getSearchKeyword() != null && !search.getSearchKeyword().equals(""))
			search.setSearchKeyword(searchKeyword);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("listProduct ::"+resultPage);
		search.setSearchKeyword("");
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		System.out.println(map.get("list"));
		System.out.println(search);
		System.out.println(menu);
		
		return "forward:/product/listProduct.jsp";
	}
}