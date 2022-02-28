package com.github.ivanig.atmserver;

import com.github.ivanig.atmserver.dto.ResponseToClient;
import com.github.ivanig.common.dto.ResponseToAtm;

public interface AtmService {

    ResponseToClient prepareResponseToClient(ResponseToAtm responseFromBank);

}
