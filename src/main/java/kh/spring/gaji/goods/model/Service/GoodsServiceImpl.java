package kh.spring.gaji.goods.model.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.spring.gaji.goods.model.dao.GoodsDao;
import kh.spring.gaji.goods.model.dto.GoodsDto;
import kh.spring.gaji.goods.model.dto.GoodsInfoDto;
import kh.spring.gaji.goods.model.dto.GoodsListDto;
import kh.spring.gaji.goods.model.dto.GoodsListInfoDto;
import kh.spring.gaji.goods.model.dto.GuDongInfoDto;
import kh.spring.gaji.goods.model.dto.MainGoodsDto;
import kh.spring.gaji.user.model.dto.FavoriteUserDto;
import kh.spring.gaji.user.model.dto.WishListDto;


@Service
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsDao goodsDao;
	
	@Override
	public int insertGoods(GoodsDto goodsDto) {
		return goodsDao.insertGoods(goodsDto);
	}

	@Override
	public GoodsInfoDto getGoodsInfo(int goodsId) {
		return goodsDao.getGoodsInfo(goodsId);
	}
	
	@Override
	public GoodsInfoDto getHideGoodsInfo(int goodsId) {
		return goodsDao.getHideGoodsInfo(goodsId);
	}
	
	@Override
	public List<GoodsInfoDto> goodsUrl(int goodsId) {
		return goodsDao.goodsUrl(goodsId);
	}
	
	@Override
	public GoodsInfoDto goodsUserInfo(int goodsId) {
		return goodsDao.goodsUserInfo(goodsId);
	}
	
	@Override
	public GoodsDto getGoods(int goodsId) {
		return goodsDao.getGoods(goodsId);
	}
	
	@Override
	public WishListDto checkWiskList(Map<String, String> map) {
		return goodsDao.checkWishList(map);
	}
	
	@Override
	public FavoriteUserDto checkFavoriteUser(Map<String, String> map) {
		return goodsDao.checkFavoriteUser(map);
	}
	
	@Override
	public List<GoodsInfoDto> userGoodsList(int goodsId) {
		return goodsDao.userGoodsList(goodsId);
	}
	
	@Override
	public int pullUpGoods(int goodsId) {
		return goodsDao.pullUpGoods(goodsId);
	}

	@Override
	public int updateStatus(Map<String, Object> map) {
		return goodsDao.updateStatus(map);
	}

	@Override
	public int updateGoods(GoodsDto goodsDto) {
		return goodsDao.updateGoods(goodsDto);
	}

	@Override
	public int deleteGoods(int goodsId) {
		return goodsDao.deleteGoods(goodsId);
	}

	@Override
	public Map<String,Object> getGoodsList(Integer currentPage, Integer PAGESIZE, Integer sort, Integer priceCeiling,
			Integer category, Integer dongId,Integer onsale, String searchWord) {
		Map<String,Object> result=new HashMap<String,Object>();
		GoodsListInfoDto goodsListInfo=goodsDao.getGoodsListInfo(priceCeiling, category, dongId, onsale,searchWord);
		int totalCnt=goodsListInfo.getTotalCnt();
		int averagePrice=goodsListInfo.getAveragePrice();
		int topPrice=goodsListInfo.getTopPrice();
		int bottomPrice=goodsListInfo.getBottomPrice();
		result.put("totalCnt",totalCnt);
		result.put("averagePrice",averagePrice);
		result.put("topPrice",topPrice);
		result.put("bottomPrice",bottomPrice);
		result.put("totalCnt",totalCnt);
		result.put("goodsListDto", goodsDao.getGoodsList(currentPage,PAGESIZE,sort,priceCeiling,
				category,dongId,onsale,searchWord,totalCnt));
		return result;
	}
	

	@Override
	public GuDongInfoDto getGuDongInfo(String userId) {
		return goodsDao.getGuDongInfo(userId);
	}

	@Override
	public int updateViewCount(int goodsId) {
		return goodsDao.updateViewCount(goodsId);
	}

	@Override
	public List<MainGoodsDto> getMainGoods(int category) {
		return goodsDao.getMainGoods(category);
	}
	
	 @Override
	    public Map<String,Object> getOnSaleList(String userId,int currentPage,int PAGESIZE){
	    	Map<String,Object> result=new HashMap<String,Object>();
	    	int totalCnt = goodsDao.getOnsaleTotalCnt(userId);
	    	result.put("totalCnt",totalCnt);
	    	result.put("myGoodsList",goodsDao.getOnsaleList(userId,currentPage,PAGESIZE,totalCnt));
	    	return result;
	    }
	    @Override
	    public Map<String,Object> getSearchOnSaleList(String userId,int currentPage,int PAGESIZE,String searchWord){
	    	Map<String,Object> result=new HashMap<String,Object>();
	    	int totalCnt = goodsDao.getSearchOnsaleTotalCnt(userId,searchWord);
	    	result.put("totalCnt",totalCnt);
	    	result.put("myGoodsList",goodsDao.getSearchOnsaleList(userId,currentPage,PAGESIZE,totalCnt,searchWord));
	    	return result;
	    }
}
