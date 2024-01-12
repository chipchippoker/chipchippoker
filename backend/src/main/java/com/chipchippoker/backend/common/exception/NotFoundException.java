package com.chipchippoker.backend.common.exception;


import com.chipchippoker.backend.common.dto.ErrorBase;

public class NotFoundException extends ChipChipPokerBaseException {

    public NotFoundException(ErrorBase errorBase) {
        super(errorBase);
    }
}