package ru.aleksrad.testcontainersdemo.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aleksrad.testcontainersdemo.integration.client.BankInfoClient;
import ru.aleksrad.testcontainersdemo.integration.dto.BankInfoDto;

@RestController
@AllArgsConstructor
public class BankInfoController {

    private BankInfoClient bankInfoClient;

    @RequestMapping(method = RequestMethod.GET, value = "/bank-info", produces = "application/json")
    public BankInfoDto getBankInfo(@RequestParam("cardNumber") String cardNumber){
        ResponseEntity<BankInfoDto> bankInfo = bankInfoClient.getBankInfo(cardNumber);
        if (bankInfo.getStatusCode() == HttpStatus.OK) return bankInfo.getBody();

        throw new RuntimeException(bankInfo.toString());
    }

}
