package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class BadGatewayException extends ChipChipPokerBaseException{
    public BadGatewayException(ErrorBase errorBase) {
        super(errorBase);
    }
}
