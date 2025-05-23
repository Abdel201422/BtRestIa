package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.infrastructure.dto.request.LoginResquestDto;
import BtRestIa.BTRES.infrastructure.dto.response.JwtResponseDto;

public interface AuthUseCase {
    JwtResponseDto login (LoginResquestDto loginRequestDto);
}
