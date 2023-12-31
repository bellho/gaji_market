package kh.spring.gaji.goods.model.Service;
import kh.spring.gaji.goods.model.dto.GoodsDto;
import kh.spring.gaji.goods.model.dto.GoodsInfoDto;
import kh.spring.gaji.goods.model.dto.GuDongInfoDto;
import kh.spring.gaji.goods.model.dto.MainGoodsDto;
import kh.spring.gaji.user.model.dto.FavoriteUserDto;
import kh.spring.gaji.user.model.dto.WishListDto;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    int insertGoods(GoodsDto goodsDto);

    GoodsInfoDto getGoodsInfo(int goodsId);
    
    GoodsInfoDto getHideGoodsInfo(int goodsId);
    
    List<GoodsInfoDto> goodsUrl(int goodsID);
    
    List<GoodsInfoDto> userGoodsList(int goodsId);
    
    public Map<String,Object> getOnSaleList(String userId,int currentPage,int PAGESIZE);
    
    public Map<String,Object> getSearchOnSaleList(String userId,int currentPage,int PAGESIZE,String searchWord);
    
    GoodsInfoDto goodsUserInfo(int goodsId);
    
    GoodsDto getGoods(int goodsId);
    
    WishListDto checkWiskList(Map<String, String> map);
    
    FavoriteUserDto checkFavoriteUser(Map<String, String> map);
    
    Map<String,Object> getGoodsList(Integer currentPage,Integer PAGESIZE,Integer sort,Integer priceCeiling,Integer category,Integer dongId,Integer onsale,String searchWord);

    int updateStatus(Map<String, Object> map);
    
    int pullUpGoods(int goodsId);

    int updateGoods(GoodsDto goodsDto);

    int deleteGoods(int goodsId);
    
    int updateViewCount(int goodsId);
    
    GuDongInfoDto getGuDongInfo(String userId);
    
    public List<MainGoodsDto> getMainGoods(int category);
}
