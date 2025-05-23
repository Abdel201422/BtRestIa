package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.infrastructure.dto.request.RegisterRequestDto;

public interface RegisterUseCase {
    
    void register(RegisterRequestDto registerDto);
}
