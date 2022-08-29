package ru.aleksrad.testcontainersdemo.integration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aleksrad.testcontainersdemo.integration.dto.BankInfoDto;

@FeignClient(value = "bankinfo", url = "${integration.bank-info-client.url}")
public interface BankInfoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/bank-info", produces = "application/json")
    ResponseEntity<BankInfoDto> getBankInfo(@RequestParam("cardNumber") String cardNumber);

}
