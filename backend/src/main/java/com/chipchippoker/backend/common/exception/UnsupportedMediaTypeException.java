package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class UnsupportedMediaTypeException extends ChipChipPokerBaseException{
    public UnsupportedMediaTypeException(ErrorBase errorBase) {
        super(errorBase);
    }
}
