package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class InternalServerException extends ChipChipPokerBaseException{
    public InternalServerException(ErrorBase errorBase) {
        super(errorBase);
    }
}
