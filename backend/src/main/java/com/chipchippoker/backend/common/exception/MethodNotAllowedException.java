package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class MethodNotAllowedException extends ChipChipPokerBaseException {
    public MethodNotAllowedException(ErrorBase errorBase) {
        super(errorBase);
    }
}
