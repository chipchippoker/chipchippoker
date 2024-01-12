package com.chipchippoker.backend.common.exception;


import com.chipchippoker.backend.common.dto.ErrorBase;

public class InvalidException extends ChipChipPokerBaseException {

    public InvalidException(ErrorBase errorBase) {
        super(errorBase);
    }
}