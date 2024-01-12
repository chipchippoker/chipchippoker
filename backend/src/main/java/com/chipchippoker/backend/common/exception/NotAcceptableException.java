package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class NotAcceptableException extends ChipChipPokerBaseException{
    public NotAcceptableException(ErrorBase errorBase) {
        super(errorBase);
    }
}
