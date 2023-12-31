package kh.spring.gaji.goods.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;
import kh.spring.gaji.category.model.service.CategoryService;
import kh.spring.gaji.file.controller.UploadController;
import kh.spring.gaji.file.model.dto.FileDto;
import kh.spring.gaji.file.model.service.FileService;
import kh.spring.gaji.goods.model.Service.GoodsService;
import kh.spring.gaji.goods.model.dto.GoodsDto;
import kh.spring.gaji.goods.model.dto.GoodsInfoDto;
import kh.spring.gaji.goods.model.dto.GoodsListDto;
import kh.spring.gaji.goods.model.dto.GuDongInfoDto;
import kh.spring.gaji.goods.model.dto.MyGoodsListDto;
import kh.spring.gaji.region.model.dto.DongDto;
import kh.spring.gaji.region.model.service.RegionService;
import kh.spring.gaji.user.model.dto.UserSafeTradingDto;
import kh.spring.gaji.user.model.service.UserService;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	Dotenv dotenv = Dotenv.load();
	Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

	private final int PAGESIZE=20;
	private final int PAGEBLOCKSIZE=5;
	
	@Autowired
	private GoodsService goodsService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/board")
	public String board(Model model,Integer currentPage,String searchWord,Integer sort,Integer priceFloor, Integer priceCeiling,Integer category,Integer guId,String guName,String dongName,Integer dongId,Integer onsale,Principal principal) { // 중고거래 게시판
		model.addAttribute("guList", regionService.guList());
	
		String userId=null;
		if(principal!=null) {	//principal이 존재한다면 로그인 상태라는것이므로 
			userId=principal.getName();	//userId 가져오기
			model.addAttribute("userId",userId);	// userId JSP에 넣기
		if(guId==null && dongId==null) {	//구 아이디와 동 아이디가 null이라면
			GuDongInfoDto guDongInfo=goodsService.getGuDongInfo(userId);	// 해당 유저의 동 정보를 이용해 상품조회 지역조건을 설정한다.
			model.addAttribute("guId",guId=guDongInfo.getGuId());
			model.addAttribute("dongList", regionService.dongList(guId));
			model.addAttribute("guName",guName=guDongInfo.getGuName());
			model.addAttribute("dongId",dongId=guDongInfo.getDongId());
			model.addAttribute("dongName",guName=guDongInfo.getDongName());
		}
		else {
			if(guId!=null) {// guId가 null이 아니면
				model.addAttribute("guId",guId);// guId
				model.addAttribute("guName",guName);// guName
				model.addAttribute("dongList", regionService.dongList(guId));//dongList를 jsp로 보내준다.
			}

			if(dongName!=null&&!dongName.equals("")) {// dongName
				model.addAttribute("dongName",dongName);// dongName JSP로 보내기
				model.addAttribute("dongId", dongId);// dongId JSP로 보내기
			}
			}
		}
	else{	//principal이 존재하지 않는다는것은 로그인 상태가 아니라는것.
			if(guId!=null) {//guId가 null이 아니면
				model.addAttribute("guId",guId);//위와 똑같이 guid와 dongList를 JSP로 전송
				model.addAttribute("guName",guName);
				model.addAttribute("dongList", regionService.dongList(guId));
			}
			if(dongName!=null&&!dongName.equals("")) {// dongName이 존재한다면
				model.addAttribute("dongName",dongName);// dongName와 dongId도 보내줘야함.
				model.addAttribute("dongId", dongId);
			}
		}
		
		int totalCnt=0;
		List<GoodsListDto> goodsListDto=null;
		
		if(currentPage==null)	//현재 페이지가 들어온게 없다면 1페이지.
			currentPage=1;
		
		if(searchWord!=null) {
			if(searchWord.equals(""))
				searchWord=null;
			else if(searchWord.charAt(0)!='%')
				searchWord="%"+searchWord+"%";
		}
			
		
		
		Map<String,Object> map= goodsService.getGoodsList((int)currentPage,PAGESIZE,sort,priceCeiling,category,dongId,onsale,searchWord);
		goodsListDto = (List<GoodsListDto>)map.get("goodsListDto");
		totalCnt= (int)map.get("totalCnt");	

		//구해온 목록으로 페이징번호들 구하고 JSP로 전송하기
		int totalPageNum = totalCnt/PAGESIZE + (totalCnt%PAGESIZE == 0 ? 0 : 1);
		int startPageNum = 1;
		if((currentPage%PAGEBLOCKSIZE) == 0) {
			startPageNum = ((currentPage/PAGEBLOCKSIZE)-1)*PAGEBLOCKSIZE +1;
		} else {
			startPageNum = ((currentPage/PAGEBLOCKSIZE))*PAGEBLOCKSIZE +1;
		}
		int endPageNum = (startPageNum+PAGEBLOCKSIZE > totalPageNum) ? totalPageNum : startPageNum+PAGEBLOCKSIZE-1;
		model.addAttribute("totalPageNum", totalPageNum);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("goodsListDto",goodsListDto);
		model.addAttribute("totalCnt",totalCnt);
		model.addAttribute("category",category);
		model.addAttribute("priceCeiling",priceCeiling);
		model.addAttribute("searchWord",searchWord);
		model.addAttribute("goodsListDto",goodsListDto);
		model.addAttribute("sort",sort);
		model.addAttribute("onsale",onsale);
		model.addAttribute("topPrice",(int)map.get("topPrice"));
		model.addAttribute("averagePrice",(int)map.get("averagePrice"));
		model.addAttribute("bottomPrice",(int)map.get("bottomPrice"));
		return "goods/goodsboard";
	}

	@GetMapping("/write")
	public ModelAndView write(ModelAndView mv, Principal principal, RedirectAttributes ra) { // 중고거래 게시판 글 작성
		if(principal != null) {
		String loginId = principal.getName();
		mv.setViewName("goods/goodswrite");
		mv.addObject("loginId",loginId);
		mv.addObject("categoryList", categoryService.categoryList());
		mv.addObject("dongList", regionService.dongList());
		mv.addObject("guList", regionService.guList());
		} else {
			mv.setViewName("redirect:/login");
			mv.addObject("msg", "로그인이 필요한 페이지입니다. 로그인화면으로 전송됩니다.");
		}
		return mv;
	}

	@Transactional
	@PostMapping("/write.do")
	public String wirteDo(GoodsDto goodsDto, RedirectAttributes ra,
			@RequestParam(value = "files", required = false) MultipartFile[] files, FileDto fileDto)
			throws IOException {
		if (goodsService.insertGoods(goodsDto) > 0) {
			System.out.println(files.length);
			if (files != null && files.length > 0 && files.length < 9) {
				Map params1 = ObjectUtils.asMap("use_filename", true, "unique_filename", true, "overwrite", false);
				for (MultipartFile file : files) {
					if (file.getSize() > 0) {
						System.out.println(file.getName());
						System.out.println(file.getSize());
						File imageFile = new UploadController().convertMultipartFileToFile(file);
						Map imageUrl2 = cloudinary.uploader().upload(imageFile, params1);
						System.out.println(imageUrl2);
						String imageUrl = cloudinary.url().generate((String) imageUrl2.get("secure_url"));
						fileDto.setUrl(imageUrl);
						fileService.insertFile(fileDto);
					}
				}
			}
			ra.addFlashAttribute("msg", "상품 등록이 성공하였습니다.");
			return "redirect:/goods/board"; // 성공 시 게시판으로 리다이렉트
		} else {
			ra.addFlashAttribute("msg", "상품 등록이 실패하였습니다. 다시 시도해주십시오.");
			return "redirect:/goods/goodswrite"; // 실패 시 다시 글 작성 페이지로 이동
		}
	}
	
	@GetMapping("/get")
	public ModelAndView getBoard(ModelAndView mv,int goodsId, GoodsInfoDto goodsDto,Principal principal) {	// 중고거래 게시판 글 상세보기
		mv.setViewName("goods/goodsget");
		if(principal != null) {
			String userId = principal.getName();
			mv.addObject("loginId", userId);
		}
		mv.addObject("goodsDto",goodsService.getGoodsInfo(goodsId)); //상품글 정보와 해당 상품 등록한 사용자의 정보
		mv.addObject("goodsUrl",goodsService.goodsUrl(goodsId)); // 상품 url값만 리스트형태로
		mv.addObject("goodsUserInfo",goodsService.goodsUserInfo(goodsId)); // 상품 등록한 사용자의 후기 수 안전거래 횟수 상품 수
		mv.addObject("userGoodsList", goodsService.userGoodsList(goodsId)); // 해당 상품 등록자의 상품 리스트
		goodsService.updateViewCount(goodsId);
		return mv;
	}
	
	@GetMapping("/get/hide")
	public ModelAndView getHideBoard(ModelAndView mv,int goodsId, GoodsInfoDto goodsDto,Principal principal) {	// 중고거래 게시판 글 상세보기
		mv.setViewName("goods/goodsget");
		if(principal != null) {
			String userId = principal.getName();
			mv.addObject("loginId", userId);
		}
		mv.addObject("goodsDto",goodsService.getHideGoodsInfo(goodsId)); //상품글 정보와 해당 상품 등록한 사용자의 정보
		mv.addObject("goodsUrl",goodsService.goodsUrl(goodsId)); // 상품 url값만 리스트형태로
		mv.addObject("goodsUserInfo",goodsService.goodsUserInfo(goodsId)); // 상품 등록한 사용자의 후기 수 안전거래 횟수 상품 수
		mv.addObject("userGoodsList", goodsService.userGoodsList(goodsId)); // 해당 상품 등록자의 상품 리스트
		goodsService.updateViewCount(goodsId);
		return mv;
	}
	
	@GetMapping("/update")
	public ModelAndView modifyGoods(Principal principal ,int goodsId, ModelAndView mv) { // 중고거래 게시판 글 수정
		
		if(principal != null) {
			String loginId = principal.getName();
			mv.setViewName("goods/goodsupdate");
			mv.addObject("loginId", loginId);
			mv.addObject("imageList",fileService.goodsImageList(goodsId)); // 해당 상품 url list값
			mv.addObject("goodsDto",goodsService.getGoods(goodsId));	// 해당상품의 정보
			mv.addObject("categoryList", categoryService.categoryList());
			mv.addObject("dongList", regionService.dongList());
			mv.addObject("guList", regionService.guList());
			
		} else {
			mv.setViewName("redirect:/login");
			mv.addObject("msg", "로그인이 필요한 페이지입니다. 로그인화면으로 전송됩니다.");
		}
		return mv;
	}
	
	@PostMapping("/goodsupdate")
	public String goodsUpdate(RedirectAttributes ra, FileDto fileDto, GoodsDto goodsDto,
			@RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException{
		if (goodsService.updateGoods(goodsDto) > 0) {
			System.out.println(files.length);
			if (files != null && files.length > 0 && files.length < 9) {
				Map params1 = ObjectUtils.asMap("use_filename", true, "unique_filename", true, "overwrite", false);
				for (MultipartFile file : files) {
					if (file.getSize() > 0) {
						System.out.println(file.getName());
						System.out.println(file.getSize());
						File imageFile = new UploadController().convertMultipartFileToFile(file);
						Map imageUrl2 = cloudinary.uploader().upload(imageFile, params1);
						System.out.println(imageUrl2);
						String imageUrl = cloudinary.url().generate((String) imageUrl2.get("secure_url"));
						fileDto.setUrl(imageUrl);
						fileService.modifyFile(fileDto);
					}
				}
			}
			ra.addFlashAttribute("msg", "상품 수정이 성공하였습니다.");
			return "redirect:/goods/board"; // 성공 시 게시판으로 리다이렉트
		} else {
			ra.addFlashAttribute("msg", "상품 수정이 실패하였습니다. 다시 시도해주십시오.");
			return "redirect:/goods/board"; // 실패 시 다시 글 작성 페이지로 이동
		}
	}
	
	@PostMapping("/deleteimg")
	@ResponseBody
	public String deleteImg(@RequestParam("imageUrl") String imageUrl, @RequestParam("imageFilename") String imageFilename) {
		try {
			cloudinary.uploader().destroy(imageFilename, ObjectUtils.emptyMap());
			fileService.deleteImg(imageUrl);
            return "success";
        } catch (Exception e) {
            return "error";
        }
	}
	
	@PostMapping("/wish")
	@ResponseBody
	public String likeButton(@RequestParam Map<String, String> map) {
		
		try {
	        userService.insertWishList(map);
	        return "added"; // 찜하기 추가 성공
	    } catch (Exception e) {
	        userService.deleteWishList(map);
	        return "removed"; // 찜하기 제거 성공
	    }
		
	}
	
	@PostMapping("/favorite")
	@ResponseBody
	public String favoriteButton(@RequestParam Map<String, String> map) {
		
		try {
	        userService.addFavoriteUser(map);
	        return "favoriteadd"; // 모아보기 추가 성공
	    } catch (Exception e) {
	        userService.deleteFavoriteUser(map);
	        return "favoriteremoved"; // 모아보기 제거 성공
	    }
		
	}
	
	@PostMapping("/checkwishlist")
	@ResponseBody
	public String checkWiskList(@RequestParam Map<String, String> map) {
		
		if (goodsService.checkWiskList(map) != null) {
	        return "added"; // 찜한 상품임
	    } else {
	        return "removed"; // 찜하지 않은 상품임
	    }
		
	}
	
	@PostMapping("/checkfavoriteuser")
	@ResponseBody
	public String checkFavoriteUser(@RequestParam Map<String, String> map) {
		if(goodsService.checkFavoriteUser(map) != null) {
			return "addedUser";
		}else {
	        return "removedUser"; 
	    }
	}
	
	@PostMapping("/pullup")
	@ResponseBody
	public String pullUpGoods(int goodsId) {
		if(goodsService.pullUpGoods(goodsId)== 1) {
			return "update";
		} else {
			return "error";
		}
	}
	
	@PostMapping("/deletegoods")
	@ResponseBody
	public String deleteGoods(int goodsId) {
		if(goodsService.deleteGoods(goodsId)== 1) {
			return "delete";
		} else {
			return "fail";
		}
	}
	
	
	
	@PostMapping("/getdong")
	@ResponseBody
	public List<DongDto> getdong(int guId) {
		return regionService.dongList(guId);
	}
	
	@GetMapping("/usergoods")	// 유저 상품
	public String onsaleGoods(Model model,Integer currentPage,String searchWord,String nickname) {	
		int totalCnt=0;
		int pageSize1=8;
		List<MyGoodsListDto> myGoodsList=null;
		model.addAttribute("myGoodsList",myGoodsList);
		if(currentPage==null)	//현재 페이지가 들어온게 없다면 1페이지.
			currentPage=1;
		if(searchWord==null) {	// 검색어가 들어온게 없다면 검색어없는 mapper로 목록 가져오기
			Map<String,Object> map= userService.getNOnSaleList(nickname,(int)currentPage,pageSize1);	//추후 userId들어가야함
			myGoodsList = (List<MyGoodsListDto>)map.get("myGoodsList");
			totalCnt= (int)map.get("totalCnt");
		}
		else {					//검색어가 있다면 그에따른 mapper로 목록 가져오기
			Map<String,Object> map= userService.getNSearchOnSaleList(nickname,(int)currentPage,pageSize1,searchWord);//추후 userId들어가야함
			myGoodsList = (List<MyGoodsListDto>)map.get("myGoodsList");
			totalCnt= (int)map.get("totalCnt");
			model.addAttribute("searchWord",searchWord);
		}
		int totalPageNum = totalCnt/pageSize1 + (totalCnt%pageSize1 == 0 ? 0 : 1);
		int startPageNum = 1;
		if((currentPage%PAGEBLOCKSIZE) == 0) {
			startPageNum = ((currentPage/PAGEBLOCKSIZE)-1)*PAGEBLOCKSIZE +1;
		} else {
			startPageNum = ((currentPage/PAGEBLOCKSIZE))*PAGEBLOCKSIZE +1;
		}
		int endPageNum = (startPageNum+PAGEBLOCKSIZE > totalPageNum) ? totalPageNum : startPageNum+PAGEBLOCKSIZE-1;
		model.addAttribute("totalPageNum", totalPageNum);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("myGoodsList",myGoodsList);
		model.addAttribute("nickname",nickname);
		return "goods/usergoods";
	}
}
