package com.credit.controller;

import com.credit.entity.CreditEntity;
import com.credit.jpa.CreditRepository;
import com.credit.vo.CardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuhoujun
 * @description: 内部控制器
 * @date: Created In 下午10:39 on 2018/4/8.
 */
@RestController
@RequestMapping("/domestic/")
public class DomesticController {

    @Autowired
    private CreditRepository creditRepository;

    @RequestMapping(value = "/allCards", method = RequestMethod.GET)
    public List<CardVO> getAllCard() {
        return creditRepository.findByDelIsFalse().stream()
                .map(entity -> new CardVO(entity))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/saveAndUpdateCard", method = RequestMethod.POST)
    public void saveAndUpdateCard(@RequestBody CardVO card) {
        CreditEntity credit = new CreditEntity();
        if (card.id != null) {
            /* 先删除原纪录，在新增*/
            creditRepository.modifyByCreditEntityId(card.id);
            credit.cardName = card.name;
            credit.type = card.type;
            credit.shortName = card.shortName;
            credit.bankLogo = card.bankLogo;
            credit.bankDetail = card.bankDetail;
            credit.money = card.money;
            credit.isShow = card.show;
            creditRepository.saveAndFlush(credit);
        } else {
            credit.cardName = card.name;
            credit.type = card.type;
            credit.shortName = card.shortName;
            credit.bankLogo = card.bankLogo;
            credit.bankDetail = card.bankDetail;
            credit.money = card.money;
            credit.isShow = card.show;
            creditRepository.saveAndFlush(credit);
        }
    }
}
