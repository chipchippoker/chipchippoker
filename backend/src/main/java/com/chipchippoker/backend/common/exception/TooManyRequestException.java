package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class TooManyRequestException extends ChipChipPokerBaseException{
    public TooManyRequestException(ErrorBase errorBase) {
        super(errorBase);
    }
}
