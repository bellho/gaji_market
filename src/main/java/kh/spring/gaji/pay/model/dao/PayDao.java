package kh.spring.gaji.pay.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kh.spring.gaji.pay.model.dto.GoodsPayInfoDto;
import kh.spring.gaji.pay.model.dto.InsertSafeTradingDto;
import kh.spring.gaji.pay.model.dto.PayUserInfoDto;
import kh.spring.gaji.user.model.dto.UserAddressDto;

@Repository
public class PayDao {
    @Autowired
    private SqlSession sqlSession;

    public int cancelSafeTrading(String transactionId) {	//거래취소
        return sqlSession.update("pay.cancelSafeTrading", transactionId);
    }
    
    public int updateGoodsToSelling(int goodsId) {
    	return sqlSession.update("pay.updateGoodsToSelling",goodsId);
    }

    public int closeSafeTrading(String transactionId) {	//거래확정
        return sqlSession.update("pay.closeSafeTrading", transactionId);
    }

    public int addSafeTrading(InsertSafeTradingDto insertSafeTradingDto) {	//안전거래 생성 
        return sqlSession.insert("pay.addSafeTrading", insertSafeTradingDto);
    }
    
    public int getAmount(int goodsId) {	// 결제시 가격확인을 위한 함수
    	return sqlSession.selectOne("pay.getAmount",goodsId);
    }
    
    
    public GoodsPayInfoDto getGoodsInfo(int goodsId) {	//결제시 상품정보 가져오기
    	return sqlSession.selectOne("pay.getGoodsInfo",goodsId);
    }
    
    public List<UserAddressDto> getUserAddressList(String userId) {	//결제시 구매자 주소가져오기
    	return sqlSession.selectList("pay.getUserAddressList",userId);
    }
    
    public PayUserInfoDto getUserInfo(String userId) {
    	return sqlSession.selectOne("pay.getUserInfo",userId);
    }
}
