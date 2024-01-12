package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class ForbiddenException extends ChipChipPokerBaseException {

    public ForbiddenException(ErrorBase errorBase) {
        super(errorBase);
    }


}